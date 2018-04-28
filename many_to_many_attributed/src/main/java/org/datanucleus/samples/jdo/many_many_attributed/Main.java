/**********************************************************************
Copyright (c) 2006 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jdo.many_many_attributed;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.datanucleus.util.NucleusLogger;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("DataNucleus Samples : M-N Relation (attributed)");
        System.out.println("===============================================");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        Customer cust1 = null;
        Customer cust2 = null;
        Supplier supp1 = null;
        Supplier supp2 = null;
        Supplier supp3 = null;

        // Persist some objects
        System.out.println(">> Persisting some Customers and Suppliers");
        PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setGroup("all");
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            cust1 = new Customer("DFG Stores");
            cust2 = new Customer("Kevins Cards");
            supp1 = new Supplier("Stationery Direct");
            supp2 = new Supplier("Grocery Wholesale");
            supp3 = new Supplier("Makro");

            pm.makePersistent(cust1);
            pm.makePersistent(cust2);
            pm.makePersistent(supp1);
            pm.makePersistent(supp2);
            pm.makePersistent(supp3);

            // We have javax.jdo.option.DetachAllOnCommit set, so all get detached at this point
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception in persist", e);
            System.exit(1);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        // Establish the relationships
        System.out.println(">> Adding relationships between Customers and Suppliers");

        // Establish the relation customer1 uses supplier2
        BusinessRelation rel1 = new BusinessRelation(cust1, supp2, "Friendly", "Hilton Hotel, London");
        cust1.addRelation(rel1);
        supp2.addRelation(rel1);

        // Establish the relation customer2 uses supplier1
        BusinessRelation rel2 = new BusinessRelation(cust2, supp1, "Frosty", "M61 motorway service station junction 23");
        cust2.addRelation(rel2);
        supp1.addRelation(rel2);

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        pm.getFetchPlan().setGroup("all");
        try
        {
            tx.begin();

            // Reattach the changed objects
            pm.makePersistent(rel1);
            pm.makePersistent(rel2);

            // We have javax.jdo.option.DetachAllOnCommit set, so all get (re)detached at this point
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception in reattach", e);
            System.exit(2);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        // Print out the results
        System.out.println(">> Customer1 has " + cust1.getNumberOfRelations() + " relations");
        System.out.println(">> Customer2 has " + cust2.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier1 has " + supp1.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier2 has " + supp2.getNumberOfRelations() + " relations");
        
        // Removing the relationship
        System.out.println(">> Now removing relationship between Customer1 and Supplier2");

        // Remove the relation customer1 uses supplier2
        BusinessRelation rel = (BusinessRelation)pm.getObjectById(rel1.getId());
        cust1.removeRelation(rel);
        supp2.removeRelation(rel);

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        pm.getFetchPlan().setGroup("all");
        try
        {
            tx.begin();

            // Reattach the changed objects
            pm.makePersistent(rel);
            
            // We have javax.jdo.option.DetachAllOnCommit set, so all get (re)detached at this point
            tx.commit();
         }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception in reattach", e);
            System.exit(3);
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        
        // Print out the results
        System.out.println(">> Customer1 has " + cust1.getNumberOfRelations() + " relations");
        System.out.println(">> Customer2 has " + cust2.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier1 has " + supp1.getNumberOfRelations() + " relations");
        System.out.println(">> Supplier2 has " + supp2.getNumberOfRelations() + " relations");
        

    }
}
