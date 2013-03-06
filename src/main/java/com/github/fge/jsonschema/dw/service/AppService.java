/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.jsonschema.dw.service;

import com.github.fge.jsonschema.dw.configuration.AppConfiguration;
import com.github.fge.jsonschema.dw.resources.AppResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

public final class AppService
    extends Service<AppConfiguration>
{
    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap)
    {
        bootstrap.setName("json-schema-demo");
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/content/css", "/css"));
        bootstrap.addBundle(new AssetsBundle("/content/js", "/js"));
    }

    @Override
    public void run(final AppConfiguration configuration,
        final Environment environment)
        throws Exception
    {
        environment.addResource(new AppResource());
    }

    public static void main(final String... args)
        throws Exception
    {
        new AppService().run(args);
    }
}
