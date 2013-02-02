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

// The list of our servlets
var Servlets = {
    VALIDATE_SYNTAX: "/validateSyntax"
};

// jQuery selectors for global elements
var DomElements = {
    FORM: "#validate",
    STARTHIDDEN: ".starthidden"
};

// jQuery selectors for input form elements
var FormElements = {
    INPUTS: "textarea, input",
    SCHEMA: "#schema"
};

// jQuery selectors for result pane elements
var ResultPane = {
    RESULTS: "textarea#results"
};

var Messages = {
    INVALID_SCHEMA: "#invalidSchema",
    TOOLTIP_SCHEMA: "#qtip-schema",
    VALIDATION_SUCCESS: "#validationSuccess",
    VALIDATION_FAILURE: "#validationFailure"
};

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

// On document.ready()
var main = function()
{
    // The guy has JavaScript, hide the warning that it should be enabled
    $(".noscript").hide();

    // Attach handler to the main form
    var $form = $(DomElements.FORM);

    // Create dummy qtips -- you cannot destroy a non existing one...
    $(Messages.INVALID_SCHEMA).find("a").qtip({content: ""});

    $form.submit(function (event)
    {
        // Clear/hide all necessary elements
        $(DomElements.STARTHIDDEN).hide();
        // Empty the results field
        TextAreas.clear(ResultPane.RESULTS);

        // Grab the necessary input fields
        var $inputs = $form.find(FormElements.INPUTS);

        // Serialize all of the form -- _very_ convenient, that!
        // Note that unchecked checkboxes will not be taken into account; as to
        // checked ones, they default to "on" but this can be changed by speci-
        // fying the "value" attribute of an input. Here we set it to "true",
        // this allows Java to effectively parse it (Boolean.parseBoolean()
        // returns false when its argument is null, which is what we want).
        var payload = $form.serialize();

        // Lock inputs
        $inputs.prop("disabled", true);

        // The request
        var request = $.ajax({
            url: Servlets.VALIDATE_SYNTAX,
            type: "post",
            data: payload,
            dataType: "json"
        });

        // On success
        // Since we specified that the data type we wanted was "json", the
        // response is directly passed along as a JavaScript object.
        request.done(function (response, status, xhr)
        {

            // This is the way to guarantee that an object has a key with
            // JavaScript
            var invalidSchema = response.hasOwnProperty("invalidSchema");

            if (invalidSchema) {
                reportParseError(response["invalidSchema"],
                    $(Messages.INVALID_SCHEMA), $(FormElements.SCHEMA));
                return;
            }

            var validationMessage = response["valid"]
                ? Messages.VALIDATION_SUCCESS
                : Messages.VALIDATION_FAILURE;

            // Show the appropriate validation message and inject pretty-printed
            // JSON into the results text area
            $(validationMessage).show();
            TextAreas.fillJson(ResultPane.RESULTS, response["results"]);
        });

        // On failure
        request.fail(function (xhr, status, error)
        {
            // FIXME: that is very, very basic
            alert("Server error: " + status + " (" + error + ")");
        });

        // Always executed
        request.always(function ()
        {
            // Unlock inputs
            $inputs.prop("disabled", false);
        });

        // Prevent default post method
        event.preventDefault();
    });
};
