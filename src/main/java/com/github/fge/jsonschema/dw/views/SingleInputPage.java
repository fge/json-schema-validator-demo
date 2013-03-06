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

package com.github.fge.jsonschema.dw.views;

import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.views.View;

import java.util.Map;

public abstract class SingleInputPage
    extends View
{
    private static final Map<String, String> SOFTWARE
        = ImmutableMap.of("json-schema-validator", "foo");

    protected SingleInputPage(final String templateName)
    {
        super(templateName);
    }

    public abstract String getPageName();

    public abstract String getPageTitle();

    public abstract String getPageDescription();

    public abstract String getButtonTitle();

    public abstract String getInputTitle();

    public abstract String getResultTitle();

    public final Map<String, String> getSoftware()
    {
        return SOFTWARE;
    }
}
