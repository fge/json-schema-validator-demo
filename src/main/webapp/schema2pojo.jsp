<!DOCTYPE html>
<%--
  ~ Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
  ~
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the Lesser GNU General Public License as
  ~ published by the Free Software Foundation, either version 3 of the
  ~ License, or (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ Lesser GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="schema2pojo" scope="request"/>
<c:set var="pageTitle" value="JSON Schema to Java" scope="request"/>
<c:set var="pageDescription"
    value="Generate a Java class out from a JSON Schema" scope="request"/>
<c:set var="inputTitle" value="Schema" scope="request"/>
<c:set var="buttonTitle" value="Generate source code" scope="request"/>
<c:set var="resultTitle" value="Generation result" scope="request"/>
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

    <!-- TODO -->
    <p>This page allows you to generate a Java source file out of a JSON Schema.
    Paste a JSON Schema into the text area and press the <span
    style="font-family: monospace">${buttonTitle}</span> button. Notes:</p>

    <ul>
        <li>the schema is first analyzed for syntax errors; it also aborts the
        generation process if the schema is not a draft v3 schema, since
        jsonschema2pojo does not support draft v4 yet;</li>
        <li>a more complete demonstration of jsonschema2pojo is available <a
        href="http://jsonschema2pojo.org">here</a>.</li>
    </ul>

    <p>Software used: <a href="${software['jsonschema2pojo']}">
    jsonschema2pojo</a> (source code generation), <a
    href="${software['json-schema-validator']}">json-schema-validator</a>
    (syntax checking).</p>
</div>

<jsp:include page="include/singleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>
</body>
</html>
