/*******************************************************************************
 * Copyright (c) 2014, 2015 Jens Reimann.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.pm.storage.web.menu;

import java.util.LinkedList;
import java.util.List;

import de.dentrassi.osgi.web.LinkTarget;
import de.dentrassi.pm.storage.web.InterfaceExtender;

public class DefaultMenuExtender implements InterfaceExtender
{
    private final List<MenuEntry> entries = new LinkedList<> ();

    public DefaultMenuExtender ()
    {
    }

    @Override
    public List<MenuEntry> getMainMenuEntries ()
    {
        return this.entries;
    }

    public void addEntry ( final String target, final String label, final int order )
    {
        this.entries.add ( new MenuEntry ( label, order, new LinkTarget ( target ), null ) );
    }

}
