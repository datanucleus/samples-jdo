package org.datanucleus.samples.jdo.spatial;

import java.sql.SQLException;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.util.NucleusLogger;

import org.postgis.Point;

public class Main
{
    public static void main(String args[])
    throws SQLException
    {
        // Create a PersistenceManagerFactory for this datastore
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        System.out.println("DataNucleus JDO Spatial Sample");
        System.out.println("==============================");

        // Persist several Position objects
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            //create objects
            tx.begin();

            Position[] sps = new Position[3];
            Point[] points = new Point[3];
            points[0] = new Point("SRID=4326;POINT(5 0)");
            points[1] = new Point("SRID=4326;POINT(10 0)");
            points[2] = new Point("SRID=4326;POINT(20 0)");
            sps[0] = new Position("market",points[0]);
            sps[1] = new Position("rent-a-car",points[1]);
            sps[2] = new Position("pizza shop",points[2]);
            Point homepoint = new Point("SRID=4326;POINT(0 0)");
            Position home = new Position("home",homepoint);

            System.out.println("Persisting spatial data...");
            System.out.println(home);
            System.out.println(sps[0]);
            System.out.println(sps[1]);
            System.out.println(sps[2]);
            System.out.println("");

            pm.makePersistentAll(sps);
            pm.makePersistent(home);

            tx.commit();
 
            //query for the distance
            tx.begin();

            System.out.println("Retrieving position where X position is > 10 and Y position is 0 ... Found:");

            // TODO Make this more elaborate, demonstrating more of the power of spatial method querying
            Query query = pm.newQuery(Position.class,
                "name != 'home' && (this.point.getX() > 10) && (this.point.getY() == 0)");
            List list = (List) query.execute();
            for( int i=0; i<list.size(); i++)
            {
                System.out.println(list.get(i));
            }

            //clean up database.. just for fun :)
            pm.newQuery(Position.class).deletePersistentAll();

            tx.commit();
        }
        catch (Exception e)
        {
            NucleusLogger.GENERAL.error(">> Exception during tutorial", e);
            System.err.println("Exception thrown during sample execution, consult the log for details : " + e.getMessage());
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
        System.out.println("End of Sample");
    }
}
