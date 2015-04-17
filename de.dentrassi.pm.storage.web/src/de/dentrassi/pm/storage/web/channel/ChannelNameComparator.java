/*******************************************************************************
 * Copyright (c) 2014, 2015 IBH SYSTEMS GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBH SYSTEMS GmbH - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.storage.web.channel;

import java.util.Comparator;

import de.dentrassi.pm.storage.Channel;

/**
 * Sort channels by name and then by id
 * <p>
 * Allows sorting channels first by name and then by id, ignoring the case. This
 * will result in a stable, understandable order. All channels
 * without name will be last, sorted by ID then.
 * </p>
 */
public class ChannelNameComparator implements Comparator<Channel>
{
    public static final Comparator<Channel> INSTANCE = new ChannelNameComparator ();

    private ChannelNameComparator ()
    {
    }

    @Override
    public int compare ( final Channel o1, final Channel o2 )
    {
        final String n1 = o1.getName () != null ? o1.getName () : "";
        final String n2 = o2.getName () != null ? o2.getName () : "";

        if ( !n1.isEmpty () && n2.isEmpty () )
        {
            return -1;
        }
        if ( n1.isEmpty () && !n2.isEmpty () )
        {
            return 1;
        }

        final int rc = n1.compareToIgnoreCase ( n2 );
        if ( rc != 0 )
        {
            return rc;
        }

        return o1.getId ().compareTo ( o2.getId () );
    }
}
