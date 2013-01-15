<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JSON Schema validation demo</title>
    <link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="validate" method="POST">
    <div class="left">
        <label for="schema">Schema</label>
        <textarea name="schema" id="schema"></textarea>
    </div>
    <div class="right">
        <label for="data">Data</label>
        <textarea name="data" id="data"></textarea>
        <input type="submit" value="Validate">
    </div>
</form>
</body>
</html>
