<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="index" scope="request"/>
<c:set var="resultTitle" value="Validation results" scope="request"/>
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
        <li>inline dereferencing (using <span
        style="font-family: monospace">id</span>) is disabled for security
        reasons;</li>
        <li><b>Draft v4 is assumed</b>. If you want to use a draft v3 schema,
        add a <span style="font-family: monospace">$schema</span> at the root of
        your schema, with <span style="font-family: monospace;">
        http://json-schema.org/draft-03/schema#</span> as a value.</li>
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
        <textarea name="input" rows="20" cols="20" class="half"
            id="input"></textarea>
        <div class="horiz">
            <label for="input2">Data:</label>
            <span class="error starthidden" id="invalidInput2">Invalid JSON:
            parse error, <a href="#"></a></span>
        </div>
        <textarea name="input2" rows="20" cols="20" class="half"
            id="input2"></textarea>
        <div class="horiz">
            <input type="submit" value="Validate">
            <span>(<a id="load" href="#">load sample data</a>)</span>
        </div>
    </div>
</form>
<jsp:include page="resultPane.jspf"/>
</body>
</html>
