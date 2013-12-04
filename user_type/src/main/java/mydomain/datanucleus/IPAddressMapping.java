/**********************************************************************
Copyright (c) 2008 Andy Jefferson and others. All rights reserved.
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
package mydomain.datanucleus;

import mydomain.usertypes.IPAddress;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.store.rdbms.mapping.java.ObjectAsStringMapping;

/**
 * Sample JavaTypeMapping for the user type "IPAddress" when used with RDBMS.
 */
public class IPAddressMapping extends ObjectAsStringMapping
{
    private static IPAddress mappingSampleValue = new IPAddress("192.168.1.1");

    public Object getSampleValue(ClassLoaderResolver clr)
    {
        return mappingSampleValue;
    }

    /**
     * Method to return the Java type being represented
     * @return The Java type we represent
     */
    public Class getJavaType()
    {
        return IPAddress.class;
    }

    /**
     * Method to return the default length of this type in the datastore.
     * An IP address can be maximum of 15 characters ("WWW.XXX.YYY.ZZZ")
     * @return The default length
     */
    public int getDefaultLength(int index)
    {
        return 15;
    }

    /**
     * Method to set the datastore string value based on the object value.
     * @param object The object
     * @return The string value to pass to the datastore
     */
    protected String objectToString(Object object)
    {
        String ipaddr;
        if (object instanceof IPAddress)
        {
            ipaddr = ((IPAddress)object).toString();
        }
        else
        {
            ipaddr = (String)object;
        }
        return ipaddr;
    }

    /**
     * Method to extract the objects value from the datastore string value.
     * @param datastoreValue Value obtained from the datastore
     * @return The value of this object (derived from the datastore string value)
     */
    protected Object stringToObject(String datastoreValue)
    {
        return new IPAddress(datastoreValue.trim());
    }
}