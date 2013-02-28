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

// The list of member names in a sample response
var SampleResponse = {
    SCHEMA: "schema",
    DATA: "data"
};

// jQuery selectors for input form elements
var FormElements = {
    INPUTS: "textarea, input",
    SCHEMA: "#schema",
    DATA: "#data"
};

// jQuery selectors for result pane elements
var ResultPane = {
    RESULTS: "textarea#results"
};

var Messages = {
    INVALID_SCHEMA: "#invalidSchema",
    INVALID_DATA: "#invalidData",
    TOOLTIP_SCHEMA: "#qtip-schema",
    TOOLTIP_DATA: "#qtip-data",
    VALIDATION_SUCCESS: "#validationSuccess",
    VALIDATION_FAILURE: "#validationFailure"
};

// FIXME: #jquery people on FreeNode say this is not the way to do it
function loadSamples()
{
    $(DomElements.STARTHIDDEN).hide();
    TextAreas.clear(ResultPane.RESULTS);

    var request = $.ajax({
        url: Servlets.LOAD,
        type: "get",
        dataType: "json"
    });

    request.done(function(response, status, xhr)
    {
        var schema = response[SampleResponse.SCHEMA];
        var data = response[SampleResponse.DATA];

        TextAreas.fillJson(FormElements.SCHEMA, schema);
        TextAreas.fillJson(FormElements.DATA, data);
    });

    request.fail(function (xhr, status, error)
    {
        // FIXME: that is very, very basic
        alert("Server error: " + status + " (" + error + ")");
    });
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
    $(Messages.INVALID_DATA).find("a").qtip({content: ""});

    // Attach loadSamples to the appropriate link
    $("#load").on("click", function (event)
    {
        event.preventDefault();
        loadSamples();
    });

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
            url: Servlets.PROCESS,
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
            var invalidData = response.hasOwnProperty("invalidData");

            if (invalidSchema)
                reportParseError(response["invalidSchema"],
                    $(Messages.INVALID_SCHEMA), $(FormElements.SCHEMA));
            if (invalidData)
                reportParseError(response["invalidData"],
                    $(Messages.INVALID_DATA), $(FormElements.DATA));

            // Stop right now if we have invalid inputs. Other fields will not
            // be defined.
            if (invalidSchema || invalidData)
                return;

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
