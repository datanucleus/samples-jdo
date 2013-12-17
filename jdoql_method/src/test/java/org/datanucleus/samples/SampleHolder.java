/**********************************************************************
Copyright (c) 2013 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.samples;

import javax.jdo.annotations.*;

/**
 * Sample class that has a String field holding a Long.
 */
@PersistenceCapable
public class SampleHolder
{
    @PrimaryKey
    long id;

    String stringHoldingLong;

    public SampleHolder(long id, String value)
    {
        this.id = id;
        this.stringHoldingLong = value;
    }

    public String getStringHoldingLong()
    {
        return stringHoldingLong;
    }

    public long getId()
    {
        return id;
    }

    public String toString()
    {
        return "SampleHolder : " + id + " [" + stringHoldingLong + "]";
    }
}
