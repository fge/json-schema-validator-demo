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

package com.github.fge.jsonschema;

import com.github.fge.jsonschema.crude.CrudeValidator;

import java.io.IOException;

import static com.github.fge.jsonschema.library.SchemaVersion.*;
import static com.github.fge.jsonschema.load.Dereferencing.*;

public final class CrudeValidators
{
    private static final CrudeValidator DRAFTV4_NO_ID;
    private static final CrudeValidator DRAFTV4_WITH_ID;
    private static final CrudeValidator DRAFTV3_NO_ID;
    private static final CrudeValidator DRAFTV3_WITH_ID;

    /*
     * In theory, we should build one factory each time someone wants to support
     * "id". But that is bloody expensive. So, no. What is more, the application
     * is stateless.
     */
    static {
        try {
            DRAFTV4_NO_ID = new CrudeValidator(DRAFTV4, CANONICAL);
            DRAFTV4_WITH_ID = new CrudeValidator(DRAFTV4, INLINE);
            DRAFTV3_NO_ID = new CrudeValidator(DRAFTV3, CANONICAL);
            DRAFTV3_WITH_ID = new CrudeValidator(DRAFTV3, INLINE);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private CrudeValidators()
    {
    }

    public static CrudeValidator withOptions(final boolean useDraftV3,
        final boolean useId)
    {
        if (useDraftV3)
            return useId ? DRAFTV3_WITH_ID : DRAFTV3_NO_ID;

        return useId ? DRAFTV4_WITH_ID : DRAFTV4_NO_ID;
    }
}
