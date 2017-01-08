package org.datanucleus.test;

import org.datanucleus.store.schema.MultiTenancyProvider;
import org.datanucleus.ExecutionContext;

public class MyTenancyProvider implements MultiTenancyProvider
{
    public String getTenantId(ExecutionContext ec)
    {
        return "First";
    }
}
