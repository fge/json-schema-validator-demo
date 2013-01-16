<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <link href="style.css" rel="stylesheet" type="text/css">
    <title>Validation results</title>
</head>
<body>
<div id="top">
    <p>Validation messages appear as a JSON array in the right text area. An
        empty array means no errors. If the message begins with "ERROR:", it
        means a fatal error has occurred during processing. This can happen if
        you did not enter valid JSON data in the previous step.
    </p>

    <p class="intro">The "Back" button will bring you back to the previous page.
    </p>
</div>
<form action="">
    <div id="left" class="content">
        <label for="data">Data</label>
        <textarea name="data" id="data"
            readonly="readonly"><%=request.getAttribute("data")%>
        </textarea>
    </div>
    <div id="right" class="content">
        <label for="results">Results</label>
        <textarea name="results" id="results"
            readonly="readonly"><%=request.getAttribute("results")%>
        </textarea>
        <input type="button" value="Back" onclick="history.back()">
    </div>
</form>
</body>
</html>