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

/*
 * This file contains common elements between (for now) the two existing,
 * "active" pages.
 */

var TextAreas = {
    fillJson: function(selector, value)
    {
        $(selector).val(JSON.stringify(value, undefined, 4));
    },
    clear: function(selector)
    {
        $(selector).val("");
    }
};

// jQuery selectors for global elements
var DomElements = {
    FORM: "#process",
    STARTHIDDEN: ".starthidden"
};

// Servlets
var Servlets = {
    PROCESS: "/process/" + pageName,
    LOAD: "/load/" + pageName
};

/*
 * Function added to set the cursor position at a given offset in a text area
 *
 * Found at:
 *
 * http://stackoverflow.com/questions/499126/jquery-set-cursor-position-in-text-area
 */
new function ($)
{
    $.fn.setCursorPosition = function (pos)
    {
        if ($(this).get(0).setSelectionRange) {
            $(this).get(0).setSelectionRange(pos, pos);
        } else if ($(this).get(0).createTextRange) {
            var range = $(this).get(0).createTextRange();
            range.collapse(true);
            range.moveEnd('character', pos);
            range.moveStart('character', pos);
            range.select();
        }
    }
}(jQuery);

// Function to report a parse error
function reportParseError(parseError, msgHandle, textArea)
{
    // Find the inner link element -- there is only one, so this is "safe".
    var link = msgHandle.find("a");

    link.text("line " + parseError["line"]);

    // Add an onclick hook to the link. When clicking on the link, the caret
    // in the text area will move to the position of the error.
    link.on("click", function(e)
    {
        e.preventDefault();
        textArea.focus().setCursorPosition(parseError["offset"]);
    });

    link.qtip("destroy");
    link.qtip({
        content: parseError["message"],
        show: "mouseover",
        hide: "mouseout",
        position: {
            corner: {
                target: "topMiddle",
                tooltip: "bottomMiddle"
            }
        }
    });
    // Show the message
    msgHandle.show();
}

// jQuery selectors for input form elements
var FormElements = {
    INPUTS: "textarea, input",
    INPUT: "#input",
    INPUT2: "#input2",
    INVALID_INPUT: "#invalidInput",
    INVALID_INPUT2: "#invalidInput2",
    INVALID_QTIP: "#qtip-input",
    INVALID_QTIP2: "#qtip-input2"
};

// jQuery selectors for result pane elements
var ResultPane = {
    RESULTS: "textarea#results",
    PROCESSING_SUCCESS: "#processingSuccess",
    PROCESSING_FAILURE: "#processingFailure"
};

// Message
var Message = {
    INPUT: "input",
    INVALID_INPUT: "invalidInput",
    INPUT2: "input2",
    INVALID_INPUT2: "invalidInput2",
    VALID: "valid",
    RESULTS: "results"
};

function Result(jsonContent)
{
    this.textArea = $("textArea#results");
    this.success = $("#processingSuccess");
    this.failure = $("#processingFailure");
    this.setResponse = function(response)
    {
        var valid = response[Message.VALID];
        var msg = valid ? this.success : this.failure;
        var content = response[Message.RESULTS];
        // Assumed: when errors, always JSON
        if (jsonContent || !valid)
            this.textArea.val(JSON.stringify(content, undefined, 4));
        else
            this.textArea.val(content);
        msg.show();
    };
    this.reset = function()
    {
        this.success.hide();
        this.failure.hide();
        this.textArea.val("");
    }
}
