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

import org.datanucleus.store.types.converters.ColumnLengthDefiningTypeConverter;
import org.datanucleus.store.types.converters.TypeConverter;

/**
 * Converter to String type for IPAddress class when handling IPv6 addresses.
 */
public class IPv6AddressStringConverter extends BaseIPAddressStringConverter implements TypeConverter<IPAddress, String>, ColumnLengthDefiningTypeConverter
{
    private static final long serialVersionUID = 7201818610115046601L;

    public int getDefaultColumnLength(int columnPosition)
    {
        if (columnPosition != 0)
        {
            return -1;
        }
        // An IP address can be maximum of 23 characters ("UUU.VVV.WWW.XXX.YYY.ZZZ")
        return 23;
    }
}