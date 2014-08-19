/**********************************************************************
Copyright (c) 2014 Andy Jefferson and others. All rights reserved.
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

import org.datanucleus.store.types.converters.TypeConverter;

/**
 * Base class for converter to String type for IPAddress class.
 */
public abstract class BaseIPAddressStringConverter implements TypeConverter<IPAddress, String>
{
    private static final long serialVersionUID = -4121349057012405220L;

    public String toDatastoreType(IPAddress ipaddr)
    {
        if (ipaddr == null)
        {
            return null;
        }

        return ipaddr.toString();
    }

    public IPAddress toMemberType(String str)
    {
        if (str == null)
        {
            return null;
        }

        return new IPAddress(str.trim());
    }
}