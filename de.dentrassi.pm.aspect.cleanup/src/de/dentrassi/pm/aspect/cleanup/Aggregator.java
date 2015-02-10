/*******************************************************************************
 * Copyright (c) 2015 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.aspect.cleanup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;

import de.dentrassi.osgi.converter.JSON;
import de.dentrassi.pm.common.MetaKey;

@JSON
public class Aggregator
{
    private List<MetaKey> fields = new LinkedList<> ();

    public List<MetaKey> getFields ()
    {
        return this.fields;
    }

    public void setFields ( final List<MetaKey> fields )
    {
        this.fields = fields;
    }

    public List<String> makeKey ( final SortedMap<MetaKey, String> metaData )
    {
        final List<String> result = new ArrayList<> ( this.fields.size () );

        for ( final MetaKey field : this.fields )
        {
            result.add ( metaData.get ( field ) );
        }

        return result;
    }

}
