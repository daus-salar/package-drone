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
package de.dentrassi.pm.generator.p2;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.osgi.framework.FrameworkUtil;

import de.dentrassi.osgi.web.Controller;
import de.dentrassi.osgi.web.ModelAndView;
import de.dentrassi.osgi.web.RequestMapping;
import de.dentrassi.osgi.web.RequestMethod;
import de.dentrassi.osgi.web.ViewResolver;
import de.dentrassi.osgi.web.controller.binding.BindingResult;
import de.dentrassi.osgi.web.controller.binding.PathVariable;
import de.dentrassi.osgi.web.controller.form.FormData;
import de.dentrassi.pm.common.MetaKey;
import de.dentrassi.pm.generator.GeneratorProcessor;
import de.dentrassi.pm.storage.service.StorageService;

@Controller
@ViewResolver ( "/WEB-INF/views/%s.jsp" )
public class GeneratorController
{
    private StorageService service;

    private final GeneratorProcessor generators = new GeneratorProcessor ( FrameworkUtil.getBundle ( GeneratorController.class ).getBundleContext () );

    public void start ()
    {
        this.generators.open ();
    }

    public void stop ()
    {
        this.generators.close ();
    }

    public void setService ( final StorageService service )
    {
        this.service = service;
    }

    @RequestMapping ( value = "/generators/p2.feature/channel/{channelId}/create", method = RequestMethod.GET )
    public ModelAndView create ( @PathVariable ( "channelId" ) final String channelId )
    {
        final ModelAndView mav = new ModelAndView ( "create" );

        mav.put ( "generators", this.generators.getInformations ().values () );
        mav.put ( "channelId", channelId );
        mav.put ( "command", new CreateData () );

        return mav;
    }

    @RequestMapping ( value = "/generators/p2.feature/channel/{channelId}/create", method = RequestMethod.POST )
    public ModelAndView createPost ( @PathVariable ( "channelId" ) final String channelId, @Valid @FormData ( "command" ) final CreateData data, final BindingResult result )
    {
        if ( result.hasErrors () )
        {
            final ModelAndView mav = new ModelAndView ( "create" );
            mav.put ( "generators", this.generators.getInformations ().values () );
            mav.put ( "channelId", channelId );
            return mav;
        }

        final Map<MetaKey, String> providedMetaData = new HashMap<> ();
        providedMetaData.put ( new MetaKey ( FeatureGenerator.ID, "id" ), data.getId () );
        providedMetaData.put ( new MetaKey ( FeatureGenerator.ID, "version" ), data.getVersion () );

        final String name = String.format ( "%s-%s.jar", data.getId (), data.getVersion () );
        this.service.createGeneratedArtifact ( channelId, name, FeatureGenerator.ID, new ByteArrayInputStream ( new byte[0] ), providedMetaData );

        return new ModelAndView ( "redirect:/channel/" + channelId + "/view" );
    }
}