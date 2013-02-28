<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="syntax" scope="request"/>
<c:set var="resultTitle" value="Validation results" scope="request"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Draft v4 JSON Schema syntax validation</title>
    <meta name="description" content="JSON Schema syntax validation">
    <jsp:include page="head.jspf"/>
</head>
<body>
<jsp:include page="menu.jspf"/>
<div id="top">
    <div class="noscript">
        <p>
            <span style="font-weight: bold">This site requires Javascript to run
            correctly</span>
        </p>
    </div>

    <!-- TODO -->
    <p>This page allows you to validate the syntax of your schemas. Paste your
    schema in the text area and press the <span style="font-family: monospace">
    Validate</span> button. You will note the following:</p>

    <ul>
        <li>unknown keywords are spotted, and reported as warnings;</li>
        <li>on an error, you have the path (as a JSON Pointer) into the schema,
        the keyword which raised the error and details about the error.</li>
    </ul>

    <p>Software used: <a href="https://github.com/fge/json-schema-validator">
    json-schema-validator</a>.</p>
</div>

<form id="process" method="POST">
    <div id="left" class="content">
        <div class="horiz">
            <label for="input">Schema:</label>
            <span class="error starthidden" id="invalidInput">Invalid JSON:
                parse error, <a href="#"></a></span>
        </div>
        <textarea name="input" rows="20" cols="20" id="input"></textarea>
        <div class="horiz">
            <input type="submit" value="Validate">
            <span>(<a id="load" href="#">load sample data</a>)</span>
        </div>
    </div>
</form>
<jsp:include page="resultPane.jspf"/>
</body>
</html>
