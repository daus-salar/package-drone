/*******************************************************************************
 * Copyright (c) 2014 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.aspect.common.osgi;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.dentrassi.pm.aspect.ChannelAspect;
import de.dentrassi.pm.aspect.ChannelAspectFactory;
import de.dentrassi.pm.aspect.extract.Extractor;
import de.dentrassi.pm.common.MetaKey;
import de.dentrassi.pm.osgi.bundle.BundleInformation;
import de.dentrassi.pm.osgi.feature.FeatureInformation;

public class OsgiAspectFactory implements ChannelAspectFactory
{
    public static final String ID = "osgi";

    private static class ChannelAspectImpl implements ChannelAspect
    {
        @Override
        public Extractor getExtractor ()
        {
            return new OsgiExtractor ( ChannelAspectImpl.this );
        }

        @Override
        public String getId ()
        {
            return ID;
        }
    }

    @Override
    public ChannelAspect createAspect ()
    {
        return new ChannelAspectImpl ();
    }

    public static BundleInformation fetchBundleInformation ( final Map<MetaKey, String> metadata )
    {
        final String string = metadata.get ( new MetaKey ( OsgiAspectFactory.ID, OsgiExtractor.KEY_BUNDLE_INFORMATION ) );
        if ( string == null )
        {
            return null;
        }

        final GsonBuilder gb = new GsonBuilder ();
        final Gson gson = gb.create ();
        return gson.fromJson ( string, BundleInformation.class );
    }

    public static FeatureInformation fetchFeatureInformation ( final Map<MetaKey, String> metadata )
    {
        final String string = metadata.get ( new MetaKey ( OsgiAspectFactory.ID, OsgiExtractor.KEY_FEATURE_INFORMATION ) );
        if ( string == null )
        {
            return null;
        }

        final GsonBuilder gb = new GsonBuilder ();
        final Gson gson = gb.create ();
        return gson.fromJson ( string, FeatureInformation.class );
    }

}
