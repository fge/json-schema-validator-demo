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

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="pageName" value="avro" scope="request"/>
<c:set var="pageTitle" value="Avro schema to JSON Schema" scope="request"/>
<c:set var="pageDescription"
    value="Convert Avro schemas to JSON Schemas" scope="request"/>
<c:set var="inputTitle" value="Avro schema" scope="request"/>
<c:set var="inputIsJson" value="true" scope="request"/>
<c:set var="buttonTitle" value="Convert" scope="request"/>
<c:set var="resultTitle" value="Conversion result" scope="request"/>
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

    <!-- TODO -->
    <p>This page allows you to validate the syntax of your schemas. Paste your
    schema in the text area and press the <span style="font-family: monospace">
    Validate</span> button. You will note the following:</p>

    <ul>
        <li>unknown keywords are spotted, and reported as warnings;</li>
        <li>on an error, you have the path (as a JSON Pointer) into the schema,
        the keyword which raised the error and details about the error.</li>
    </ul>

    <p>Software used: <a href="${software['json-schema-processor-examples']}">
    json-schema-processor-examples</a>.</p>
</div>

<jsp:include page="include/singleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>
</body>
</html>
