<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="style.css" rel="stylesheet" type="text/css">
    <title>Validation results</title>
</head>
<body>
<form action="">
    <div class="left">
        <label for="data">Data</label>
        <textarea name="data" id="data"
                  readonly="readonly"><%=request.getAttribute("data")%>
        </textarea>
    </div>
    <div class="right">
        <label for="results">Results</label>
        <textarea name="results" id="results"
                  readonly="readonly"><%=request.getAttribute("results")%>
        </textarea>
        <input type="button" value="Back" onclick="history.back()">
    </div>
</form>
</body>
</html>