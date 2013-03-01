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

// jQuery selectors for global elements
var DomElements = {
    FORM: "#process",
    INPUTS: "textarea, input"
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

// Message
var Message = {
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

function Input(name, contentIsJson)
{
    this.name = name;
    this.contentIsJson = contentIsJson;

    this.baseId = "#" + name;
    this.textArea = $(this.baseId);

    this.errorLink = $(this.baseId + "-link");
    this.errorLink.qtip({content: ""});

    this.errId = name + "-invalid";
    this.errorContainer = $("#" + this.errId);

    this.hasError = function (response)
    {
        if (!response.hasOwnProperty(this.errId))
            return false;

        var parseError = response[this.errId];
        var textArea = this.textArea;

        this.errorLink.text("line " + parseError["line"]);
        this.errorLink.on("click", function(e)
        {
            e.preventDefault();
            textArea.focus().setCursorPosition(parseError["offset"]);
        });
        this.errorLink.qtip("destroy");
        this.errorLink.qtip({
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

        this.errorContainer.show();
        return true;
    };

    this.reset = function ()
    {
        this.errorContainer.hide();
    };

    this.fill = function (response)
    {
        if (!response.hasOwnProperty(this.name))
            return;

        var value = response[this.name];
        if (this.contentIsJson)
            this.textArea.val(JSON.stringify(value, undefined, 4));
        else
            this.textArea.val(value);
    };
}

function DummyInput()
{
    this.hasError = function (response)
    {
        return false;
    };

    this.reset = function ()
    {
    };

    this.fill = function (response)
    {
    };
}

// On document.ready()
var main = function()
{
    // The guy has JavaScript, hide the warning that it should be enabled
    $(".noscript").hide();

    var result = new Result(resultIsJson);
    var input = new Input("input", inputIsJson);
    var input2 = typeof input2IsJson === "undefined"
        ? new DummyInput()
        : new Input("input2", input2IsJson);

    // Attach sample source loading to the appropriate link
    $("#load").on("click", function(event)
    {
        event.preventDefault();
        result.reset();
        input.reset();
        input2.reset();

        var request = $.ajax({
            url: Servlets.LOAD,
            type: "get",
            dataType: "json"
        });

        request.done(function(response, status, xhr)
        {
            input.fill(response);
            input2.fill(response);
        });

        request.fail(function (xhr, status, error)
        {
            // FIXME: that is very, very basic
            alert("Server error: " + status + " (" + error + ")");
        });
    });

    // Attach handler to the main form
    var $form = $(DomElements.FORM);

    $form.submit(function (event)
    {
        result.reset();
        input.reset();
        input2.reset();

        // Grab the necessary input fields
        var $inputs = $form.find(DomElements.INPUTS);

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
            var error = input.hasError(response);
            var error2 = input2.hasError(response);
            if (error || error2)
                return;
            result.setResponse(response);
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

$(document).ready(main);
