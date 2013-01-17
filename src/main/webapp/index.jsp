<%@page import="org.eel.kitchen.jsonschema.constants.ServletInputs"%>
<%@page import="org.eel.kitchen.jsonschema.constants.Links"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>JSON Schema validation demo</title>
    <link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="top">
    <p>Enter your schema in the left text area and the data to validate in the
        right text area; then press the "Validate" button. Both must be valid
        JSON, as defined by <a href="<%=Links.RFC_4627%>">RFC 4627</a>.
    </p>

    <p>This webapp uses the <a href="<%=Links.PROJECT_LINK%>">
        json-schema-validator Java library</a>. It is available on <a
        href="<%=Links.SELF_LINK%>">GitHub</a>.
        Contributions are welcome.
    </p>

    <p><b>NOTE:</b> the default schema format is draft v3. If you want to
        validate agaisnt draft v4, you <i>must</i> include this line in your
        schema:
    </p>
    <blockquote>
        "$schema": "http://json-schema.org/draft-04/schema#"
    </blockquote>
</div>
<form action="validate" method="POST">
    <div id="left" class="content">
        <label for="<%=ServletInputs.SCHEMA%>">Schema:</label>
        <textarea name="<%=ServletInputs.SCHEMA%>"
            id="<%=ServletInputs.SCHEMA%>"></textarea>
    </div>
    <div id="right" class="content">
        <label for="<%=ServletInputs.DATA%>">Data:</label>
        <textarea name="<%=ServletInputs.DATA%>"
            id="<%=ServletInputs.DATA%>"></textarea>
        <input type="submit" value="Validate">
    </div>
</form>
</body>
</html>
