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
<c:set var="pageName" value="avro" scope="request"/>
<c:set var="pageTitle" value="Avro schema to JSON Schema" scope="request"/>
<c:set var="pageDescription"
    value="Convert Avro schemas to JSON Schemas" scope="request"/>
<c:set var="inputTitle" value="Avro schema" scope="request"/>
<c:set var="buttonTitle" value="Convert" scope="request"/>
<c:set var="resultTitle" value="Conversion result" scope="request"/>
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

    <p>This page allows you to convert an Avro schema to a JSON Schema. Paste
    your Avro schema in the text area and press the <span
    style="font-family: monospace">${buttonTitle}</span> button. You will note
    the following:</p>

    <ul>
        <li>Avro records have a notion of order of their fields; however, JSON
        has no such notion, and neither has JSON Schema: the <span
        style="font-family: monospace">order</span> keyword is therefore
        ignored;</li>
        <li>JSON numbers, unlike Avro's numeric types, are not limited in
        precision and/or scale; for integer types, <span
        style="font-family: monospace">minimum</span> and <span
        style="font-family: monospace">maximum</span> are used to emulate Avro
        limtations.</li>
    </ul>

    <p>Software used: <a href="${software['json-schema-avro']}">json-schema-avro
    </a> (conversion), <a href="${software['json-schema-validator']}">
    json-schema-validator</a> (post generation JSON Schema syntax checking).</p>
</div>

<jsp:include page="include/singleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>
</body>
</html>
