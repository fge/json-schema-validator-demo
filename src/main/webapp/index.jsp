<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="index" scope="request"/>
<c:set var="pageTitle" value="JSON Schema validation online" scope="request"/>
<c:set var="pageDescription"
    value="Validate your JSON data against a JSON schema" scope="request"/>
<c:set var="inputTitle" value="Schema" scope="request"/>
<c:set var="inputIsJson" value="true" scope="request"/>
<c:set var="inputTitle2" value="Data" scope="request"/>
<c:set var="input2IsJson" value="true" scope="request"/>
<c:set var="buttonTitle" value="Validate" scope="request"/>
<c:set var="resultTitle" value="Validation results" scope="request"/>
<c:set var="resultIsJson" value="true" scope="request"/>
<c:import url="include/software.jspf" var="devnull"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="include/head-common.jspf"/>
    <jsp:include page="include/head-js.jspf"/>
</head>
<body>
<jsp:include page="include/menu.jspf"/>
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

    <p>Software used: <a href="${software['json-schema-validator']}">
    json-schema-validator</a>.</p>

</div>

<jsp:include page="include/doubleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>

</body>
</html>
