<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="index" scope="request"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JSON Schema validation online</title>
    <meta name="description" content="Validate your JSON Schema online">
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

    <p>This page allows you to validate your JSON instances. Paste your schema
    and data in the appropriate text areas and press the <span
    style="font-family: monospace;">Validate</span> button. Notes:</p>

    <ul>
        <li>schema syntax validation is also performed;</li>
        <li>on errors, you have the full paths into the schema and the instance
        where the error has occurred, and details about the error.</li>
    </ul>

    <p>Software used: <a href="https://github.com/fge/json-schema-validator">
    json-schema-validator</a>.</p>

</div>

<form id="process" method="POST">
    <div id="left" class="content">
        <div class="horiz">
            <label for="schema">Schema:</label>
            <span class="error starthidden" id="invalidSchema">Invalid JSON:
                parse error, <a href="#"></a></span>
        </div>
        <textarea name="schema" rows="20" cols="20" class="half"
            id="schema"></textarea>
        <div class="options">
            <p>Validation options:</p>
            <div>
                <input type="checkbox" name="useV3" id="useV3" value="true">
                <label for="useV3">Use draft v3</label>
            </div>
            <div>
                <input type="checkbox" name="useId" id="useId" value="true">
                <label for="useId">Trust <span style="font-family: monospace">id
                </span> (inline dereferencing)</label>
            </div>
        </div>
        <div class="horiz">
            <label for="data">Data:</label>
            <span class="error starthidden" id="invalidData">Invalid JSON: parse
                error, <a href="#"></a></span>
        </div>
        <textarea name="data" rows="20" cols="20" class="half"
            id="data"></textarea>
        <div class="horiz">
            <input type="submit" value="Validate">
            <span>(you can also <a id="load" href="#">load samples</a> from the
            <a href="https://github.com/json-schema/JSON-Schema-Test-Suite">JSON
            Schema test suite)</a></span>
        </div>
    </div>
</form>
<div id="right" class="content">
    <div class="horiz">
        <label for="results">Validation result:</label>
        <span class="error starthidden" id="validationFailure">failure</span>
        <span class="success starthidden" id="validationSuccess">success</span>
    </div>
    <textarea name="results" rows="20" cols="20"
        id="results" readonly="readonly"></textarea>
</div>
</body>
</html>
