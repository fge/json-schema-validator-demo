<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="schema2pojo" scope="request"/>
<c:set var="resultTitle" value="Generation result" scope="request"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>JSON Schema to Java source</title>
    <meta name="description" content="JSON Schema to Java source">
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
    <p>This page allows you to generate a Java source file out of a JSON Schema.
    Paste a JSON Schema into the text area and press the <span
    style="font-family: monospace">Generate source</span> button. Notes:</p>

    <ul>
        <li>the schema is first analyzed for syntax errors; it also aborts the
        generation process if the schema is not a draft v3 schema, since
        jsonschema2pojo does not support draft v4 yet;</li>
        <li>a more complete demonstration of jsonschema2pojo is available <a
        href="http://jsonschema2pojo.org">here</a>.</li>
    </ul>

    <p>Software used: <a
    href="https://github.com/joelittlejohn/jsonschema2pojo">jsonschema2pojo</a>
    (source code generation), <a
    href="https://github.com/fge/json-schema-validator">
    json-schema-validator</a> (syntax checking).</p>
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
            <input type="submit" value="Generate source code">
            <span>(<a id="load" href="#">load sample data</a>)</span>
        </div>
    </div>
</form>
<jsp:include page="resultPane.jspf"/>
</body>
</html>
