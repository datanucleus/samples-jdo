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
package org.datanucleus.samples.jdo.many_many_attributed;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@PersistenceCapable
public class Supplier
{
    long id;

    String name = null;

    //@Persistent(mappedBy="supplier")
    Set<BusinessRelation> customerRelations = new HashSet();

    public Supplier(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addRelation(BusinessRelation rel)
    {
        if (customerRelations == null)
        {
            customerRelations = new HashSet<BusinessRelation>();
        }
        customerRelations.add(rel);
    }

    public void removeRelation(BusinessRelation rel)
    {
        if (customerRelations == null)
        {
            return;
        }
        customerRelations.remove(rel);
    }

    public Set<BusinessRelation> getRelations()
    {
        if (customerRelations == null)
        {
            customerRelations = new HashSet<BusinessRelation>();
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

    public static class PK implements Serializable
    {
        private static final long serialVersionUID = -4093751077746226151L;
        public long id;

        public PK()
        {
        }

        public PK(java.lang.String str)
        {
            java.util.StringTokenizer token = new java.util.StringTokenizer(str, "::");
            token.nextToken(); // Class name
            this.id = Long.valueOf(token.nextToken());
        }

        public java.lang.String toString()
        {
            return Supplier.class.getName() + "::" + java.lang.String.valueOf(this.id);
        }

        public int hashCode()
        {
            return (int) id;
        }

        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null)
            {
                return false;
            }
            if (o.getClass() != getClass())
            {
                return false;
            }
            PK objToCompare = (PK) o;
            return ((this.id == objToCompare.id));
        }
    }
}
