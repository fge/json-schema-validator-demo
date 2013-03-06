<!DOCTYPE html>
<#-- @ftlvariable name="" type="com.github.fge.jsonschema.dw.views.DoubleInputPage"-->
<html>
<head>
    <title>${pageTitle}</title>
    <link href="css/style2.css" rel="stylesheet" type="text/css">
    <meta name="description" content="${pageDescription}">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="js/ext/jquery.qtip.min.css" rel="stylesheet" type="text/css">
    <script src="js/ext/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="js/ext/jquery.browser.js" type="text/javascript"></script>
    <script src="js/ext/jquery.qtip.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        var pageName = "${pageName}";
    </script>
    <script src="js/common.js" type="text/javascript"></script>
</head>
<body>
<table id="page" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="2" class="fullwidth">
            <div class="menu">
                <ul>
                    <li>Select page:</li>
                    <li><a href="index.jsp">Instance validation</a></li>
                    <li><a href="syntax.jsp">Schema syntax validation</a></li>
                    <li><a href="jjschema.jsp">Java to JSON Schema</a></li>
                    <li><a href="schema2pojo.jsp">JSON Schema to Java</a></li>
                    <li><a href="avro.jsp">Avro to JSON Schema</a></li>
                    <li><a href="about.jsp">About this site</a></li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="spacing fullwidth">
            <div>
                <p>This page allows you to validate your JSON instances. Paste
                your schema and data in the appropriate text areas and press the
                <span style="font-family: monospace;">${buttonTitle}</span>
                button. Notes:</p>

                <ul>
                    <li>inline dereferencing (using <span
                    style="font-family: monospace">id</span>) is disabled for
                    security reasons;</li>
                    <li><b>Draft v4 is assumed</b>. If you want to use a draft
                    v3 schema, add a <span
                    style="font-family: monospace">$schema</span> at the root of
                    your schema, with <span
                    style="font-family: monospace;">http://json-schema.org/draft-03/schema#
                    </span> as a value.</li>
                </ul>

                <p>Software used: <a href="${software["json-schema-validator"]}">
                    json-schema-validator</a>.</p>

            </div>
        </td>
    </tr>
    <tr id="content">
        <td class="fullheight halfwidth">
            <form class="fullheight" method="POST">
                <div class="horiz">
                    <label for="input">${inputTitle}:</label>
    <span class="error starthidden" id="input-invalid">Invalid JSON:
        parse error, <a id="input-link" href="#"></a></span>
                </div>
                <textarea name="input" rows="10" cols="10" class="half"
                    id="input"></textarea>
                <div class="horiz">
                    <label for="input2">${inputTitle2}:</label>
    <span class="error starthidden" id="input2-invalid">Invalid JSON:
    parse error, <a id="input2-link" href="#"></a></span>
                </div>
                <textarea name="input2" rows="10" cols="10" class="half"
                    id="input2"></textarea>
                <div class="horiz">
                    <input type="submit" value="${buttonTitle}">
                    <span>(<a id="load" href="#">load sample data</a>)</span>
                </div>
            </form>
        </td>
        <td class="fullheight halfwidth">
            <div class="fullheight">
                <div class="horiz">
                    <label for="results">${resultTitle}:</label>
                <span class="error starthidden" id="processingFailure">failure
                </span>
                <span class="success starthidden" id="processingSuccess">success
                </span>
                </div>
                <textarea name="results" rows="10" cols="10" id="results"
                    readonly="readonly"></textarea>
            </div>
        </td>
    </tr>
</table>
</body>
</html>