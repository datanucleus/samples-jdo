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
package org.datanucleus.samples.jdo.many_many_attributed2;

//@PersistenceCapable
public class BusinessRelation2
{
    private Customer2 customer;

    private Supplier2 supplier;

    private String relationLevel;

    private String meetingLocation;

    public BusinessRelation2(Customer2 cust, Supplier2 supp, String level, String meeting)
    {
        this.customer = cust;
        this.supplier = supp;
        this.relationLevel = level;
        this.meetingLocation = meeting;
    }

    public Customer2 getCustomer()
    {
        return customer;
    }

    public Supplier2 getSupplier()
    {
        return supplier;
    }

    public String getRelationLevel()
    {
        return relationLevel;
    }

    public String getMeetingLocation()
    {
        return meetingLocation;
    }
}
