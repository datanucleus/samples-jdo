package org.datanucleus.test;

import java.util.List;
import java.util.Set;

import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;

import mydomain.usertypes.IPAddress;
import org.datanucleus.samples.Machine;
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

            Machine m1 = new Machine("nucleus1", new IPAddress("192.168.1.1"));
            m1.getAddresses().add(new IPAddress("192.168.1.254"));
            m1.getAddresses().add(new IPAddress("192.168.1.255"));
            pm.makePersistent(m1);
            Machine m2 = new Machine("nucleus2", new IPAddress("192.168.1.2"));
            m2.getAddresses().add(new IPAddress("192.168.1.253"));
            pm.makePersistent(m2);

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
            Query q = pm.newQuery("SELECT FROM " + Machine.class.getName() + " ORDER BY this.hostName");
            List<Machine> machines = (List<Machine>) q.execute();
            assertNotNull(machines);
            assertEquals(2, machines.size());
            Machine mc1 = machines.get(0);
            Machine mc2 = machines.get(1);
            assertNotNull(mc1);
            assertEquals(mc1.getIP().toString(),"192.168.1.1");
            Set<IPAddress> mc1addrs = mc1.getAddresses();
            assertEquals(2, mc1addrs.size());
            IPAddress addr = new IPAddress("192.168.1.254");
            assertTrue(mc1addrs.contains(addr));
            addr = new IPAddress("192.168.1.255");
            assertTrue(mc1addrs.contains(addr));
            assertNotNull(mc2);
            assertEquals(mc2.getIP().toString(),"192.168.1.2");
            Set<IPAddress> mc2addrs = mc2.getAddresses();
            assertEquals(1, mc2addrs.size());
            addr = new IPAddress("192.168.1.253");
            assertTrue(mc2addrs.contains(addr));

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
