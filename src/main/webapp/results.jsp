<%@page import="org.eel.kitchen.jsonschema.Utils"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <link href="style.css" rel="stylesheet" type="text/css">
    <title>Validation results</title>
</head>
<body>
<%
    String origSchema = (String) request.getAttribute("origSchema");
    if (origSchema == null)
        origSchema = "";
    else
        origSchema = Utils.inputValueEscape(origSchema);
%>
<div id="top">
    <p>Validation messages appear as a JSON object in the right text area. An
        empty object means no errors.</p>

    <p>Keys of the object are <a
        href="http://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-07">
        JSON Pointer</a>s into the validated instances, and values are arrays
        of validation errors encountered at that point in the instance. You will
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
<form action="index.jsp" method="POST">
    <div id="left" class="content">
        <label for="data">Data</label>
        <textarea name="data" id="data"
            readonly="readonly"><%=request.getAttribute("data")%>
        </textarea>
    </div>
    <div id="right" class="content">
        <label for="results">Results</label>
        <textarea name="results" id="results"
            readonly="readonly"><%=request.getAttribute("results")%>
        </textarea>
        <input type="hidden" name="origSchema" value="<%=origSchema%>">
        <input type="submit" value="Back">
    </div>
</form>
</body>
</html>