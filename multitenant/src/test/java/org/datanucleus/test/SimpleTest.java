package org.datanucleus.test;

import java.util.*;
import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");

        // Enable this to specify the tenant name via the "provider"
        Map props = new HashMap();
//      props.put("datanucleus.TenantProvider", new MyTenancyProvider());
  
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props, "MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();

        // Enable this to specify the tenant name per PM
//      pm.setProperty("datanucleus.tenantID", "First");
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.makePersistent");
            Person p = new Person(1, "First Person");
            pm.makePersistent(p);
            NucleusLogger.GENERAL.info(">> pm.makePersistent DONE");

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

        pm = pmf.getPersistenceManager();

        // Enable this to specify the tenant name per PM
//      pm.setProperty("datanucleus.tenantID", "Second");
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.getObjectById SECOND");
            Person p = pm.getObjectById(Person.class, 1);
            NucleusLogger.GENERAL.info(">> pm.getObjectById returned " + p);
            NucleusLogger.GENERAL.info(">> UPDATE field");
            p.setName("Second Person");
            NucleusLogger.GENERAL.info(">> UPDATE done");

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

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();

            NucleusLogger.GENERAL.info(">> pm.newQuery");
            Query q = pm.newQuery("SELECT FROM " + Person.class.getName());
            List<Person> people = q.executeList();
            NucleusLogger.GENERAL.info(">> query.execute returned " + people.size());
            for (Person p : people)
            {
                NucleusLogger.GENERAL.info(">> query.result=" + p);
            }

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
