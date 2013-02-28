<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="jjschema" scope="request"/>
<c:set var="inputTitle" value="Java source code" scope="request"/>
<c:set var="buttonTitle" value="Generate schema" scope="request"/>
<c:set var="resultTitle" value="Generation result" scope="request"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>POJO to JSON Schema</title>
    <meta name="description" content="Java source to JSON Schema conversion">
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

    <p>This page allows you to generate a JSON Schema out of a Java
    source code. Paste the source code into the text area, then press the <span
    style="font-family: monospace">Generate schema</span> button. Notes:</p>

    <ul>
        <li>it is safe to put static initializers in the code: they will not be
        executed;</li>
        <li>on failure (compilation errors), the compiler messages are displayed
        instead, with line and column information.</li>
    </ul>

    <p>Software used: <a
    href="https://github.com/reinert/JJSchema">JJSchema</a>.</p>

</div>

<jsp:include page="singleInputForm.jspf"/>
<jsp:include page="resultPane.jspf"/>
</body>
</html>
