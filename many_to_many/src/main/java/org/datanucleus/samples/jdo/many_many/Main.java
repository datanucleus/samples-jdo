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
package org.datanucleus.samples.jdo.many_many;

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

public class Main
{
    public Main()
    {
        super();
    }

    public static void main(String[] args)
    {
        System.out.println("DataNucleus Samples : M-N Relation");
        System.out.println("===================================");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

        // Persist some objects
        System.out.println(">> Persisting some Customers and Suppliers");
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        Object[] custIds = new Object[2];
        Object[] suppIds = new Object[3];
        try
        {
            tx.begin();
            Customer cust1 = new Customer("DFG Stores", "Small shop in London");
            Customer cust2 = new Customer("Kevins Cards", "Gift shop");
            Supplier supp1 = new Supplier("Stationery Direct", "123 The boulevard, Milton Keynes, UK");
            Supplier supp2 = new Supplier("Grocery Wholesale", "56 Jones Industrial Estate, London, UK");
            Supplier supp3 = new Supplier("Makro", "1 Parkville, Wembley, UK");
        
            pm.makePersistent(cust1);
            pm.makePersistent(cust2);
            pm.makePersistent(supp1);
            pm.makePersistent(supp2);
            pm.makePersistent(supp3);
            tx.commit();

            custIds[0] = JDOHelper.getObjectId(cust1);
            custIds[1] = JDOHelper.getObjectId(cust2);
            suppIds[0] = JDOHelper.getObjectId(supp1);
            suppIds[1] = JDOHelper.getObjectId(supp2);
            suppIds[2] = JDOHelper.getObjectId(supp3);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
        
            Customer cust1 = (Customer)pm.getObjectById(custIds[0]);
            Customer cust2 = (Customer)pm.getObjectById(custIds[1]);
            Supplier supp1 = (Supplier)pm.getObjectById(suppIds[0]);
            Supplier supp2 = (Supplier)pm.getObjectById(suppIds[1]);
        
            // Establish the relation customer1 uses supplier2
            cust1.addSupplier(supp2);
            supp2.addCustomer(cust1);

            // Establish the relation customer2 uses supplier1
            supp1.addCustomer(cust2);
            cust2.addSupplier(supp1);

            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

        System.out.println(">> Retrieving Customer/Supplier");
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            Customer cust1 = (Customer)pm.getObjectById(custIds[0]);
            System.out.println(">> Retrieved customer : " + cust1);
            Collection suppliers = cust1.getSuppliers();
            Iterator supplierIter = suppliers.iterator();
            while (supplierIter.hasNext())
            {
                System.out.println(">>     with Supplier : " + supplierIter.next());
            }

            Supplier supp2 = (Supplier)pm.getObjectById(suppIds[1]);
            System.out.println(">> Retrieved supplier : " + supp2);
            Collection customers = supp2.getCustomers();
            Iterator customerIter = customers.iterator();
            while (customerIter.hasNext())
            {
                System.out.println(">>     with Customers : " + customerIter.next());
            }

            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    }
}
