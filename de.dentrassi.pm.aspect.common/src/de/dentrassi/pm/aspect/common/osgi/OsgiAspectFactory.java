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

import de.dentrassi.pm.aspect.ChannelAspect;
import de.dentrassi.pm.aspect.ChannelAspectFactory;
import de.dentrassi.pm.aspect.extract.Extractor;
import de.dentrassi.pm.aspect.virtual.Virtualizer;

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
        public Virtualizer getArtifactVirtualizer ()
        {
            return new TychoVirtualizer ();
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

}