package org.datanucleus.test;

import org.datanucleus.store.schema.MultiTenancyProvider;
import org.datanucleus.ExecutionContext;

/**
 * Example of a provider of tenant "id".
 * To use this, you should specify the persistence property "datanucleus.TenantProvider" and set to an instance of this.
 */
public class MyTenancyProvider implements MultiTenancyProvider
{
    public String getTenantId(ExecutionContext ec)
    {
        return "First";
    }
}
