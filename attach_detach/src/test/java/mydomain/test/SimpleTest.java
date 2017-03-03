/**********************************************************************
Copyright (c) 2017 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
     ...
**********************************************************************/
package mydomain.test;

import org.junit.*;

import mydomain.model.Owner;
import mydomain.model.Pet;
import mydomain.model.Vet;

import javax.jdo.*;

import static org.junit.Assert.*;

import java.util.Set;

import org.datanucleus.util.NucleusLogger;

/**
 * Simple test of detach-attach process where we detach an object graph, modify some parts of the graph, and then attach it later.
 */
public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");

        // Create PMF to handle persistence
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        // Create some data
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            Owner owner1 = new Owner(1, "John", "Green");
            Pet dog1 = new Pet(1, "Fido");
            Vet vet1 = new Vet(1, "Mr Ford");

            Owner owner2 = new Owner(2, "Mary", "Jones");
            Pet cat1 = new Pet(2, "Miaow");
            Pet cat2 = new Pet(3, "Stanley");

            // Establish relations, setting both sides when bidirectional
            owner1.addPet(dog1);
            dog1.setOwner(owner1);
            dog1.setVet(vet1);

            owner2.addPet(cat1);
            owner2.addPet(cat2);
            cat1.setOwner(owner2);
            cat2.setOwner(owner2);
            cat1.setVet(vet1);
            cat2.setVet(vet1);

            // Persist the owners, which will cascade to the pets and the vet
            pm.makePersistent(owner1);
            pm.makePersistent(owner2);

            tx.commit(); // PMF is marked as detachAllOnCommit

            // objects will now be detached
            NucleusLogger.GENERAL.info(">> owner1.state=" + JDOHelper.getObjectState(owner1));
            NucleusLogger.GENERAL.info(">> owner2.state=" + JDOHelper.getObjectState(owner2));
            NucleusLogger.GENERAL.info(">> dog1.state=" + JDOHelper.getObjectState(dog1));
            NucleusLogger.GENERAL.info(">> cat1.state=" + JDOHelper.getObjectState(cat1));
            NucleusLogger.GENERAL.info(">> cat2.state=" + JDOHelper.getObjectState(cat2));
            NucleusLogger.GENERAL.info(">> vet1.state=" + JDOHelper.getObjectState(vet1));
            owner1.getPets();
            cat1.getVet();
            cat1.getOwner();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        pmf.getDataStoreCache().evictAll();

        // Some later point in the application

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        Owner myOwner = null;
        try
        {
            tx.begin();

            // Retrieve the first owner
            myOwner = pm.getObjectById(Owner.class, 1);
            // If we detach at this point the "pets" field will not be loaded so cannot access it after the transaction

            pm.getFetchPlan().addGroup("owner_pets"); // This adds the Owner.pets field to be detached
            // If we detach at this point the "owner" and "vet" fields of the Pet will not be detached so cannot access them after the transaction

            pm.getFetchPlan().addGroup("pet_owner"); // This adds the Pet.owner field to be detached
            pm.getFetchPlan().addGroup("pet_vet"); // This adds the Pet.vet field to be detached
            // If we detach at this point the "vet" field will still not be detached since the default "maxFetchDepth" is 1
            pm.getFetchPlan().setMaxFetchDepth(3); // Extend the detach process to include the Vet level

            tx.commit(); // Detaches the current objects
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        pmf.getDataStoreCache().evictAll();

        // Inspect our detached objects
        NucleusLogger.GENERAL.info(">> Owner1=" + myOwner + " state=" + JDOHelper.getObjectState(myOwner));
        Set<Pet> myPets = myOwner.getPets(); // Accessing this would throw an exception if not detached
        for (Pet myPet : myPets)
        {
            NucleusLogger.GENERAL.info(">> Pet1=" + myPet + " state=" + JDOHelper.getObjectState(myPet));
            Vet myVet = myPet.getVet();
            NucleusLogger.GENERAL.info(">> Vet1=" + myPet + " state=" + JDOHelper.getObjectState(myVet));
        }

        // Update the name of the Pet whilst detached
        myPets.iterator().next().setName("Biffer");


        // Some later point in the application
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            // Attach the modified owner+pet+vet to persistence
            pm.makePersistent(myOwner);

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        pmf.getDataStoreCache().evictAll();

        // Test that the cat name was changed
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            Pet pet1 = pm.getObjectById(Pet.class, 1);
            NucleusLogger.GENERAL.info(">> pet1 (after attach) id=" + pet1.getId() + " name=" + pet1.getName());
            assertEquals("Biffer", pet1.getName());

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        
        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
