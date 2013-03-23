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
<c:set var="pageName" value="jsonpatch" scope="request"/>
<c:set var="pageTitle" value="JSON Patch online" scope="request"/>
<c:set var="pageDescription" value="Patch your JSON values with JSON Patch"
    scope="request"/>
<c:set var="inputTitle" value="Patch" scope="request"/>
<c:set var="inputTitle2" value="Data" scope="request"/>
<c:set var="buttonTitle" value="Apply patch" scope="request"/>
<c:set var="resultTitle" value="Patch result" scope="request"/>
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

    <p>This page allows you to test your <a
    href="http://tools.ietf.org/html/draft-ietf-appsawg-json-patch-10">JSON
    Patch</a>es. Paste your JSON Patch and value to patch in the appropriate
    text areas and press the <span
    style="font-family: monospace;">${buttonTitle}</span> button.</p>

    <p>Note that for reporting purposes, the syntax of the patch is checked
    using <a
    href="https://github.com/fge/sample-json-schemas/blob/master/json-patch/json-patch.json">the
    appropriate JSON Schema</a>.</p>

    <p>Software used: <a href="${software['json-patch']}">json-patch</a> (patch
    processing), <a href="${software['json-schema-validator']}">
    json-schema-validator</a> (patch syntax checking).</p>

</div>

<jsp:include page="include/doubleInputForm.jspf"/>
<jsp:include page="include/resultPane.jspf"/>

</body>
</html>
