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

package de.dentrassi.pm.p2.servlet;

import java.io.IOException;
import java.io.OutputStream;

import de.dentrassi.pm.common.servlet.Handler;

public interface SpoolOutHandler extends Handler
{
    public void process ( OutputStream out ) throws IOException;
}
