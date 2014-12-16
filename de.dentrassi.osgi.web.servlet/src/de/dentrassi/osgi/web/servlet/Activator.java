package de.dentrassi.osgi.web.servlet;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator
{

    private ServiceRegistration<ContextHandler> handle;

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start ( final BundleContext bundleContext ) throws Exception
    {
        final Dictionary<String, Object> properties = new Hashtable<> ();

        final ContextImpl appContext = new ContextImpl ();
        properties.put ( "Jetty-WarFolderPath", "." );
        properties.put ( "contextPath", "/" );

        this.handle = bundleContext.registerService ( ContextHandler.class, appContext, properties );
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop ( final BundleContext bundleContext ) throws Exception
    {
        this.handle.unregister ();
    }

}