<%@page import="org.eel.kitchen.jsonschema.Utils"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>JSON Schema validation demo</title>
    <link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%
    /*
     * Piggybacked values from results.jsp's POST, if any
     */
    String origSchema = request.getParameter("origSchema");
    if (origSchema == null)
        origSchema = "";

    String origData = request.getParameter("data");
    if (origData == null)
        origData = "";

    /*
     * Hash value
     */
    final String hash = Utils.getHash(session.getId(), request.getRemoteAddr());
%>
<div id="top">
    <p>Enter your schema in the left text area and the data to validate in the
        right text area; then press the "Validate" button. Both must be valid
        JSON, as defined by <a href="http://tools.ietf.org/html/rfc4627">RFC
        4627</a>.
    </p>

    <p>This webapp uses the <a
        href="https://github.com/fge/json-schema-validator">
        json-schema-validator Java library</a>. It is available on <a
        href="https://github.com/fge/json-schema-validator">GitHub</a>.
        Contributions are welcome.
    </p>

    <p><b>NOTE:</b> the default schema format is draft v3. If you want to
        validate agaisnt draft v4, you <i>must</i> include this line in your
        schema:
    </p>
    <blockquote>"$schema": "http://json-schema.org/draft-04/schema#"</blockquote>
</div>
<form action="validate" method="POST">
    <div id="left" class="content">
        <label for="schema">Schema:</label>
        <textarea name="schema" id="schema"><%=origSchema%></textarea>
    </div>
    <div id="right" class="content">
        <label for="data">Data:</label>
        <textarea name="data" id="data"><%=origData%></textarea>
        <input type="hidden" name="hash" value="<%=hash%>">
        <input type="submit" value="Validate">
    </div>
</form>
</body>
</html>
