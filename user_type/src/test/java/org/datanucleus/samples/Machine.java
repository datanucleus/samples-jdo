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
package org.datanucleus.samples;

import java.util.*;
import mydomain.usertypes.IPAddress;

/**
 * Sample class that is persistable that has a field of a usertype.
 */
public class Machine
{
    String hostName;
    IPAddress ip;

    Set<IPAddress> addresses = new HashSet();

    public Machine(String host, IPAddress ip)
    {
        this.hostName = host;
        this.ip = ip;
    }

    public String getHostname()
    {
        return hostName;
    }

    public IPAddress getIP()
    {
        return ip;
    }

    public Set<IPAddress> getAddresses()
    {
        return addresses;
    }

    public String toString()
    {
        return "Machine : " + hostName + " [" + ip.toString() + "]";
    }
}
