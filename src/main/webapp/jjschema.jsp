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
<c:set var="pageName" value="jjschema" scope="request"/>
<c:set var="pageTitle" value="Java to JSON Schema" scope="request"/>
<c:set var="pageDescription"
    value="Generate a JSON Schema out of your Java classes" scope="request"/>
<c:set var="inputTitle" value="Java source code" scope="request"/>
<c:set var="buttonTitle" value="Generate schema" scope="request"/>
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

    <p>This page allows you to generate a JSON Schema out of a Java
    source code. Paste the source code into the text area, then press the <span
    style="font-family: monospace">${buttonTitle}</span> button. Notes:</p>

    <ul>
        <li>it is safe to put static initializers in the code: they will not be
        executed;</li>
        <li>on failure (compilation errors), the compiler messages are displayed
        instead, with line and column information.</li>
    </ul>

    <p>Software used: <a
    href="${software['JJSchema']}">JJSchema</a>.</p>

</div>

<jsp:include page="include/singleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>
</body>
</html>
