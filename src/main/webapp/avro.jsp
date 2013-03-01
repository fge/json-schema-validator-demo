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
    <p>This page allows you to convert an Avro schema to a JSON Schema. Paste
    your Avro schema in the text area and press the <span
    style="font-family: monospace">${buttonTitle}</span> button. You will note
    the following:</p>

    <ul>
        <li><span style="font-family: monospace">minimum</span> and <span
        style="font-family: monospace">maximum</span> are used to limit the
        range of Avro's <span style="font-family: monospace">int</span> and
        <span style="font-family: monospace;">long</span> types;</li>
        <li>a regular expression is used to limit the range of permissible
        characters in strings for Avro's <span
        style="font-family: monospace">bytes</span> and <span
        style="font-family: monospace">fixed</span> types.
        </li>
    </ul>

    <p>Software used: <a href="${software['avro']}">Avro Java library</a> (Avro
    schema parsing), <a href="${software['json-schema-processor-examples']}">
    json-schema-processor-examples</a> (conversion), <a
    href="${software['json-schema-validator']}">json-schema-validator</a> (post
    generation JSON Schema syntax checking).</p>
</div>

<jsp:include page="include/singleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>
</body>
</html>
