<%@page import="org.eel.kitchen.jsonschema.constants.ServletOutputs"%>
<%@page import="org.eel.kitchen.jsonschema.constants.Links"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <link href="style.css" rel="stylesheet" type="text/css">
    <title>Validation results</title>
</head>
<body>
<div id="top">
    <p>Validation messages appear as a JSON object in the right text area. An
        empty object means no errors.</p>

    <p>Keys of the object are <a href="<%=Links.JSON_POINTER_DRAFT%>">JSON
        Pointer</a>s into the validated instances, and values are arrays of
        validation errors encountered at that point in the instance. You will
        notice that some messages have "syntax" or "$ref" as validation domains,
        which refer to schema syntax errors or JSON Reference processing
        failures respectively.
    </p>

    <p>If the message begins with "ERROR:", it
        means a fatal error has occurred during processing. This can happen if
        you did not enter valid JSON data in the previous step.
    </p>

    <p class="intro">The "Back" button will bring you back to the previous page.
    </p>
</div>
<form action="">
    <div id="left" class="content">
        <label for="<%=ServletOutputs.DATA%>">Data</label>
        <textarea name="<%=ServletOutputs.DATA%>" id="<%=ServletOutputs.DATA%>"
            readonly="readonly"><%=request.getAttribute("data")%>
        </textarea>
    </div>
    <div id="right" class="content">
        <label for="<%=ServletOutputs.RESULTS%>">Results</label>
        <textarea name="<%=ServletOutputs.RESULTS%>"
            id="<%=ServletOutputs.RESULTS%>"
            readonly="readonly"><%=request.getAttribute("results")%>
        </textarea>
        <input type="button" value="Back" onclick="history.back()">
    </div>
</form>
</body>
</html>