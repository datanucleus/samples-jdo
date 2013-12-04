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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Embedded;

@PersistenceCapable
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY)
public class Account
{
    protected String firstName = null;
    protected String lastName = null;
    protected int level = 0;

    @Embedded
    protected Login login = null;

    public Account(String first, String last, int level)
    {
        this.firstName = first;
        this.lastName = last;
        this.level = level;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLogin(Login login)
    {
        this.login = login;
    }

    public Login getLogin()
    {
        return login;
    }

    public String toString()
    {
        return "Account : " + firstName + " " + lastName + " [level=" + level + "]";
    }
}
