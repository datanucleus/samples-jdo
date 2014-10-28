/**********************************************************************
Copyright (c) 2012 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jdo.osgi;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.*;

import org.datanucleus.util.NucleusLogger;

/**
 * Sample JDO main to demonstrate the creation of PMF, persistence, query, and delete.
 */
public class JdoMain
{
    private int MAX_NUM_ITERATIONS = 10;    

    public JdoMain()
    {
        super();        
    }

    /**
     * spring bean as init method
     */
    public void performJdoPersistence()
    {
        System.out.println("DataNucleus:OSGi:JDO - starting");

        // Create PMF using additional properties needed that can't be specified in persistence.xml
        Map<String, Object> overrideProps = new HashMap<String, Object>();
        overrideProps.put("datanucleus.primaryClassLoader", this.getClass().getClassLoader());
        overrideProps.put("datanucleus.plugin.pluginRegistryClassName", "org.datanucleus.plugin.OSGiPluginRegistry");

        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(overrideProps, "PU");
        System.out.println("DataNucleus:OSGi:JDO - PMF created");

        // Create PM to persist a number of objects
        PersistenceManager pm = pmf.getPersistenceManager();
        System.out.println("DataNucleus:OSGi:JDO - PM created");

        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Person p;
            for (int i=0; i<MAX_NUM_ITERATIONS; i++)
            {
                p = new Person(i, "Name"+i, "Address"+i, 20+i);
                pm.makePersistent(p);
            }
            tx.commit();
            System.out.println("DataNucleus:OSGi:JDO - " + MAX_NUM_ITERATIONS + " Person objects have been persisted");
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            System.out.println("DataNucleus:OSGi:JDO - PM closed");
        }

        // Create PM to query the objects
        pm = pmf.getPersistenceManager();
        System.out.println("DataNucleus:OSGi:JDO - PM created");
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query q = pm.newQuery("SELECT max(p.id) FROM Person p");
            Object result = q.execute();
            System.out.println("DataNucleus:OSGi:JDO - max(id)=" + result);
            NucleusLogger.GENERAL.info(">> Query done, max=" + result);
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            System.out.println("DataNucleus:OSGi:JDO - PM closed");
        }

        // Create PM to delete the objects
        pm = pmf.getPersistenceManager();
        System.out.println("DataNucleus:OSGi:JDO - PM created");
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query query = pm.newQuery("SELECT FROM Person p");
            long numberInstancesDeleted = query.deletePersistentAll();
            System.out.println("DataNucleus:OSGi:JDO - number of objects deleted=" + numberInstancesDeleted);
            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.info(">> Exception in query", e);
            e.printStackTrace();
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            System.out.println("DataNucleus:OSGi:JDO - PM closed");
        }

        System.out.println("DataNucleus:OSGi:JDO - ended");
    }
}
