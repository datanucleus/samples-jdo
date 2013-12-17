package org.datanucleus.test;

import java.util.List;

import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;

import org.datanucleus.samples.SampleHolder;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();

            SampleHolder holder1 = new SampleHolder(1, "45");
            pm.makePersistent(holder1);
            SampleHolder holder2 = new SampleHolder(2, "523");
            pm.makePersistent(holder2);

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown persisting data", thr);
            fail("Failed to persist data : " + thr.getMessage());
        }
        finally 
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }

        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query q = pm.newQuery("SELECT FROM " + SampleHolder.class.getName() + 
                    " WHERE Long.valueOf(stringHoldingLong) == 45");
            List<SampleHolder> holders = (List<SampleHolder>) q.execute();
            assertNotNull(holders);
            assertEquals(1, holders.size());
            SampleHolder holder = holders.get(0);
            assertEquals(1, holder.getId());
            assertEquals("45", holder.getStringHoldingLong());

            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception thrown querying data", thr);
            fail("Failed to query data : " + thr.getMessage());
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
