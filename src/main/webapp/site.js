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

// My first ever JavaScript code! Yay!

// Some constants
var Servlets = { VALIDATE: "validate" };
var DomElements = {
    FORM: "#validate",
    RESULTS: "textarea#results",
    INVALID_SCHEMA: "#invalidSchema",
    INVALID_DATA: "#invalidData",
    VALIDATION_SUCCESS: "#validationSuccess",
    VALIDATION_FAILURE: "#validationFailure",
    STARTHIDDEN_SELECTOR: ".errmsg, .success",
    FORM_FIELDS: "textarea, input"
};

var main = function()
{
    // References to what we need
    var $form = $(DomElements.FORM);
    var $results = $(DomElements.RESULTS);

    $form.submit(function (event)
    {
        // Clear/hide all necessary elements
        $(DomElements.STARTHIDDEN_SELECTOR).hide();
        // Empty the results field
        $results.val("");

        // Grab fields in the form
        // TODO: Complete list when necessary
        var $inputs = $form.find(DomElements.FORM_FIELDS);

        // Serialize all of the form -- _very_ convenient, that!
        // Note that unchecked checkboxes will not be taken into account; as to
        // checked ones, they default to "on" but this can be changed by speci-
        // fying the "value" attribute of an input. Here we set it to "true",
        // this allows Java to effectively parse it (Boolean.parseBoolean()
        // returns false when its argument is null).
        var payload = $form.serialize();

        // Lock inputs
        $inputs.prop("disabled", true);

        // The request
        var request = $.ajax({
            url: Servlets.VALIDATE,
            type: "post",
            data: payload,
            dataType: "json"
        });

        // On success
        // Since we specified that the data type we wanted was "json", the
        // response is directly passed along as a JavaScript object.
        request.done(function (response, status, xhr)
        {
            var invalidSchema = response["invalidSchema"];
            var invalidData = response["invalidData"];

            if (invalidSchema)
                $(DomElements.INVALID_SCHEMA).show();
            if (invalidData)
                $(DomElements.INVALID_DATA).show();

            // Stop if we don't have valid data, it makes no sense to validate
            if (invalidSchema || invalidData)
                return;

            var validationMessage = response["valid"]
                ? DomElements.VALIDATION_SUCCESS
                : DomElements.VALIDATION_FAILURE;

            // Show the appropriate validation message and inject pretty-printed
            // JSON into the results text area
            $(validationMessage).show();
            $results.val(JSON.stringify(response["results"], undefined, 4));
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

        // TODO: test if actually necessary
        return false;
    });
};
