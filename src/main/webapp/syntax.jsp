<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="syntax" scope="request"/>
<c:set var="pageTitle" value="JSON Schema syntax validation" scope="request"/>
<c:set var="pageDescription"
    value="Validate the syntax of your JSON Schemas" scope="request"/>
<c:set var="inputTitle" value="Schema" scope="request"/>
<c:set var="buttonTitle" value="Check syntax" scope="request"/>
<c:set var="resultTitle" value="Validation results" scope="request"/>
<c:import url="software.jspf" var="devnull"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="head-common.jspf"/>
    <jsp:include page="head-js.jspf"/>
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

    <p>Software used: <a href="${software['json-schema-validator']}">
    json-schema-validator</a>.</p>
</div>

<jsp:include page="singleInputForm.jspf"/>
<jsp:include page="resultPane.jspf"/>
</body>
</html>
