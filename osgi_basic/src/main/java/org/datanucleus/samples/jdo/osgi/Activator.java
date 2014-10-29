package org.datanucleus.samples.jdo.osgi;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{
    BundleContext bundleContext = null;
    PersistenceManagerFactory pmf = null;

    public void start(BundleContext bundleContext)
    {
        System.out.println("Activator.start Creating PMF");
        this.bundleContext = bundleContext;

        ClassLoader cl = getClassLoader();

        // Create PMF using properties. OSGi seems to have problems finding persistence.xml
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
        props.put("datanucleus.plugin.pluginRegistryClassName", "org.datanucleus.plugin.OSGiPluginRegistry");
        props.put("datanucleus.primaryClassLoader", cl);
        props.put("javax.jdo.option.ConnectionURL", "jdbc:h2:mem:nucleus");
        props.put("javax.jdo.option.ConnectionDriverName", "org.h2.Driver");
        props.put("javax.jdo.option.ConnectionUserName", "sa");
        props.put("javax.jdo.option.ConnectionPassword", "");
        props.put("datanucleus.autoCreateSchema", "true");
        props.put("datanucleus.autoCreateColumns", "true");
        System.out.println("Activator.start props=" + props);

        pmf = JDOHelper.getPersistenceManagerFactory(props, JDOPersistenceManagerFactory.class.getClassLoader());
        System.out.println("Activator - PMF created");
    }

    private ClassLoader getClassLoader() 
    {
        ClassLoader classloader = null;
        Bundle[] bundles = bundleContext.getBundles();

        for (int x = 0; x < bundles.length; x++) 
        {
            System.out.println(">> bundle.name=" + bundles[x].getSymbolicName());
            if (bundles[x].getSymbolicName().startsWith("org.datanucleus.api.jdo")) 
            {
                try 
                {
                    classloader = bundles[x].loadClass("org.datanucleus.api.jdo.JDOPersistenceManagerFactory").getClassLoader();
                }
                catch (ClassNotFoundException e) 
                {
                    e.printStackTrace();
                }
                break;
            }
        }

        return classloader;
    }

    public void stop(BundleContext bundleContext)
    {
        System.out.println("Activator.stop Closing PMF");
        if (pmf != null && !pmf.isClosed())
        {
            pmf.close();
        }
    }
}