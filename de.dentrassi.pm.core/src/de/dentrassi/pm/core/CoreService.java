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
package de.dentrassi.pm.core;

import java.util.Map;

import de.dentrassi.pm.common.MetaKey;

public interface CoreService
{
    public String getCoreProperty ( String key );

    public void setCoreProperty ( String key, String value );

    public Map<MetaKey, String> list ();

    public void setProperties ( Map<String, String> properties );
}
