package org.datanucleus.samples.jdo.geospatial;

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

        System.out.println("DataNucleus JDO Geospatial Sample");
        System.out.println("=================================");

        // Persist several Position objects
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            //create objects
            tx.begin();

            Position[] sps = new Position[3];
            sps[0] = new Position("market", new Point("SRID=4326;POINT(5 0)"));
            sps[1] = new Position("rent-a-car", new Point("SRID=4326;POINT(10 0)"));
            sps[2] = new Position("pizza shop", new Point("SRID=4326;POINT(20 0)"));

            Point homepoint = new Point("SRID=4326;POINT(0 0)");
            Position home = new Position("home", homepoint);

            System.out.println("Persisting spatial data...");
            System.out.println(home);
            System.out.println(sps[0]);
            System.out.println(sps[1]);
            System.out.println(sps[2]);
            System.out.println("");

            pm.makePersistentAll(sps);
            pm.makePersistent(home);

            tx.commit();
 
            // Query for all Positions within a distance
            tx.begin();

            Double distance = new Double(12.0);
            System.out.println("Retrieving Positions where distance to home is less than \"" + distance + "\" ... Found:");

            // TODO Make this more elaborate, demonstrating more of the power of spatial method querying
            Query query = pm.newQuery(Position.class, "name != 'home' && Spatial.distance(this.point, :homepoint) < :distance");
            List list = (List) query.execute(homepoint, distance);
            for (int i=0; i<list.size(); i++)
            {
                System.out.println(list.get(i));
            }

//          Query query = pm.newQuery(Position.class, "name != 'home' && (this.point.getX() > 10) && (this.point.getY() == 0)");
//          List list = (List) query.execute();

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
        pmf.close();
        System.out.println("End of Sample");
    }
}
