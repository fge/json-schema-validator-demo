package com.github.fge.jsonschema.constants;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class ParseError
{
    private ParseError()
    {
    }

    private static final String LINE = "line";
    private static final String OFFSET = "offset";
    private static final String MESSAGE = "message";

    public static JsonNode build(final JsonProcessingException e,
        final boolean crlf)
    {
        final JsonLocation location = e.getLocation();
        final ObjectNode ret = JsonNodeFactory.instance.objectNode();

        /*
         * Unfortunately, for some reason, Jackson botches the column number in
         * its JsonPosition -- I cannot figure out why exactly. However, it does
         * have a correct offset into the buffer.
         *
         * The problem is that if the input has CR/LF line terminators, its
         * offset will be "off" by the number of lines minus 1 with regards to
         * what JavaScript sees as positions in text areas. Make the necessary
         * adjustments so that the caret jumps at the correct position in this
         * case.
         */
        final int lineNr = location.getLineNr();
        int offset = (int) location.getCharOffset();
        if (crlf)
            offset = offset - lineNr + 1;
        ret.put(LINE, lineNr);
        ret.put(OFFSET, offset);

        // Finally, put the message
        ret.put(MESSAGE, e.getOriginalMessage());
        return ret;
    }
}
