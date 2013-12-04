/**********************************************************************
Copyright (c) 2011 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jdo.one_one_embedded;

import java.util.List;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;

public class Main
{
    public static void main(String args[])
    {
        // Create a PersistenceManagerFactory for this datastore
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        System.out.println("DataNucleus AccessPlatform with JDO");
        System.out.println("===================================");

        Object acctId = null;

        {
            // Persistence of a Account and Login
            PersistenceManager pm = pmf.getPersistenceManager();
            System.out.println("Persisting Account+Login");
            Account acct = new Account("John","Cameron", 3);
            Login login = new Login("jcameron", "xxxx");
            acct.setLogin(login);
            pm.makePersistent(acct);
            acctId = pm.getObjectId(acct);
            System.out.println("Account+Login have been persisted, with account-id=" + acctId);

            pm.close();
        }
        System.out.println("");

        {
            // Perform some query operations
            PersistenceManager pm = pmf.getPersistenceManager();

            System.out.println("Executing Query for Accounts with level of 3 or above");
            Query q = pm.newQuery("SELECT FROM " + Account.class.getName() +
                    " WHERE level >= 3 ORDER BY lastName ASC");
            List<Account> c = (List)q.execute();
            Iterator<Account> iter = c.iterator();
            while (iter.hasNext())
            {
                Account acct = iter.next();
                System.out.println(">  " + acct);

                // Give an example of an update
                Login login = acct.getLogin();
                login.setPassword("yyyy");
            }

            pm.close();
        }
        System.out.println("");

        {
            // Clean out the database
            PersistenceManager pm = pmf.getPersistenceManager();

            System.out.println("Deleting all Accounts from persistence");
            Query q = pm.newQuery(Account.class);
            long numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted + " accounts");

            pm.close();
        }

        System.out.println("");
        System.out.println("End of Tutorial");
    }
}
