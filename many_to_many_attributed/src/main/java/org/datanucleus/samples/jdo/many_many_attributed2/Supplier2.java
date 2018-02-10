/**********************************************************************
Copyright (c) 2018 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples.jdo.many_many_attributed2;

import java.util.HashSet;
import java.util.Set;

//@PersistenceCapable
public class Supplier2
{
    long id;

    String name = null;

    //@Persistent(mappedBy="supplier")
    Set<BusinessRelation2> customerRelations = new HashSet();

    public Supplier2(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addRelation(BusinessRelation2 rel)
    {
        if (customerRelations == null)
        {
            customerRelations = new HashSet<BusinessRelation2>();
        }
        customerRelations.add(rel);
    }

    public void removeRelation(BusinessRelation2 rel)
    {
        if (customerRelations == null)
        {
            return;
        }
        customerRelations.remove(rel);
    }

    public Set<BusinessRelation2> getRelations()
    {
        if (customerRelations == null)
        {
            customerRelations = new HashSet<BusinessRelation2>();
        }
        return customerRelations;
    }

    public int getNumberOfRelations()
    {
        if (customerRelations == null)
        {
            return 0;
        }
        return customerRelations.size();
    }

    public String toString()
    {
        return "Supplier : " + name + " - " + customerRelations.size() + " customers";
    }
}
