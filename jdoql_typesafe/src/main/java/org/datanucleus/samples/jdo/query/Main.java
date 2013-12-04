/**********************************************************************
Copyright (c) 2010 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jdo.query;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.query.typesafe.*;
import org.datanucleus.util.*;

public class Main
{
    public static void main(String args[])
    {
        // Create a PersistenceManagerFactory for this datastore
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        System.out.println("DataNucleus AccessPlatform with JDO Query");
        System.out.println("=========================================");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            System.out.println("Persisting Inventory and Products");

            Inventory inv = new Inventory(1, "Main Shop Inventory");
            Product prd1 = new Product(1, "Walkman", 12.99);
            Product prd2 = new Product(2, "Samsonite Suitcase", 53.99);
            Product prd3 = new Product(3, "Phillips HD TV", 329.99);
            inv.addProduct(prd1);
            inv.addProduct(prd2);
            inv.addProduct(prd3);
            pm.makePersistent(inv);
            System.out.println("Inventory and Products have been persisted");

            Person p1 = new Person(1, "Fred");
            Person p2 = new Person(2, "George");
            Person p3 = new Person(3, "Harry");
            Person p4 = new Person(4, "Simone");
            Person p5 = new Person(5, "Georgia");
            p1.setBestFriend(p2);
            p2.setBestFriend(p3);
            p3.setBestFriend(p4);
            p4.setBestFriend(p5);
            pm.makePersistent(p1);
            System.out.println("Persons have been persisted");

            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception persisting objects", e);
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

        // Perform some query operations
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            System.out.println("Performing JDO API queries");

            // TODO Remove this when part of JDO API
            JDOPersistenceManager jdopm = (JDOPersistenceManager)pm;

            // Filter + param + result + ordering
            // "SELECT FROM Product WHERE this.value < :criticalValue && this.name.startsWith('Wal') 
            //  ORDER BY this.name ASCENDING"
            NucleusLogger.GENERAL.info(">> Query1 : Products with filter, ordering, param and result");
            TypesafeQuery<Product> tq1 = jdopm.newTypesafeQuery(Product.class);
            QProduct cand = QProduct.candidate();
            tq1.filter(cand.value.lt(tq1.doubleParameter("criticalValue")).and(cand.name.startsWith("Wal")))
                .orderBy(cand.name.asc()).setParameter("criticalValue", 40.0);
            List<Object[]> results1 = tq1.executeResultList(true, cand.name, cand.value);
            if (results1 != null)
            {
                Iterator<Object[]> iter = results1.iterator();
                while (iter.hasNext())
                {
                    NucleusLogger.GENERAL.info(">> Result1 " + StringUtils.objectArrayToString(iter.next()));
                }
            }

            // Ordering + range
            // "SELECT FROM Product ORDER BY this.name ASCENDING RANGE 0,2"
            NucleusLogger.GENERAL.info(">> Query2 : Products with ordering and range");
            TypesafeQuery<Product> tq2 = jdopm.newTypesafeQuery(Product.class);
            cand = QProduct.candidate();
            tq2.orderBy(cand.name.asc()).range(0, 2);
            List<Product> results2 = tq2.executeList();
            if (results2 != null)
            {
                Iterator<Product> iter = results2.iterator();
                while (iter.hasNext())
                {
                    NucleusLogger.GENERAL.info(">> Result2 " + iter.next());
                }
            }

            // Filter using variable
            // "SELECT FROM Inventory WHERE this.products.contains(var) && var.name.startsWith('Wal')"
            NucleusLogger.GENERAL.info(">> Query3 : Inventory with filter using variable");
            TypesafeQuery<Inventory> tq3 = jdopm.newTypesafeQuery(Inventory.class);
            QProduct var3 = QProduct.variable("var");
            QInventory cand3 = QInventory.candidate();
            tq3.filter(cand3.products.contains(var3).and(var3.name.startsWith("Wal")));
            List<Inventory> results3 = tq3.executeList();
            if (results3 != null)
            {
                Iterator<Inventory> iter = results3.iterator();
                while (iter.hasNext())
                {
                    NucleusLogger.GENERAL.info(">> Result3 " + iter.next());
                }
            }

            // Subquery
            // "SELECT FROM Product WHERE this.value < (SELECT AVG(p.value) FROM Product p)"
            TypesafeQuery<Product> tq4 = jdopm.newTypesafeQuery(Product.class);
            QProduct cand4 = QProduct.candidate();
            TypesafeSubquery tqsub4 = tq4.subquery(Product.class, "p");
            QProduct candsub4 = QProduct.candidate("p");
            tq4.filter(cand4.value.lt(tqsub4.selectUnique(candsub4.value.avg())));
            List<Product> results4 = tq4.executeList();
            if (results4 != null)
            {
                Iterator<Product> iter = results4.iterator();
                while (iter.hasNext())
                {
                    NucleusLogger.GENERAL.info(">> Result4 " + iter.next());
                }
            }

            // Recursion
            NucleusLogger.GENERAL.info(">> Person recursion query");
            TypesafeQuery<Person> tq5 = jdopm.newTypesafeQuery(Person.class);
            QPerson cand5 = QPerson.candidate();
            NucleusLogger.GENERAL.info(">> Person recursion START");
            tq5.filter(cand5.bestFriend.bestFriend.name.startsWith("George"));
            NucleusLogger.GENERAL.info(">> Person recursion done filter");
            List<Person> results5 = tq5.executeList();
            if (results5 != null)
            {
                Iterator<Person> iter = results5.iterator();
                while (iter.hasNext())
                {
                    NucleusLogger.GENERAL.info(">> Result5 " + iter.next());
                }
            }


            tx.commit();
        }
        catch (Error er)
        {
            NucleusLogger.GENERAL.error(">> Error querying objects", er);
            return;
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception querying objects", e);
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

        // Clean out the database
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            System.out.println("Deleting Inventory+Product from persistence");
            Query q = pm.newQuery(Inventory.class);
            long numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " inventories");
            Query q2 = pm.newQuery(Product.class);
            numberInstancesDeleted = q2.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " products");

            tx.commit();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        System.out.println("");
        System.out.println("End of Sample");
    }
}
