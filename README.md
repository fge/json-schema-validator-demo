<h2>What this is</h2>

<p>This is a Heroku-based Java webapp which demonstrates usages of the following packages:</p>

<ul>
    <li><a href="https://github.com/fge/json-schema-core">json-schema-core</a>;</li>
    <li><a href="https://github.com/fge/json-schema-validator">json-schema-validator</a>;</li>
    <li><a href="https://github.com/reinert/JJSchema">JJSchema</a>;</li>
    <li><a href="https://github.com/joelittlejohn/jsonschema2pojo">jsonschema2pojo</a>;</li>
    <li><a href="https://github.com/fge/json-patch">json-patch</a>;</li>
    <li><a href="https://github.com/fge/json-schema-avro">json-schema-avro</a>.</li>
</ul>

<p>This application is available <a href="http://json-schema-validator.herokuapp.com">here</a>.</p>

<h2>Demo pages</h2>

<p>All processors written for this site are gathered together in a single package: <a
href="https://github.com/fge/json-schema-processor-examples">json-schema-processor-examples</a>.
Right now, the following pages exist:</p>

<ul>
    <li>full validation (schema + data) (<a
    href="http://json-schema-validator.herokuapp.com/index.jsp">link</a>);</li>
    <li>syntax validation (<a
    href="http://json-schema-validator.herokuapp.com/syntax.jsp">link</a>);</li>
    <li>generation of a JSON Schema from a Java source file (<a
    href="http://json-schema-validator.herokuapp.com/jjschema.jsp">link</a>);</li>
    <li>generation of a Java source file form a JSON Schema (<a
    href="http://json-schema-validator.herokuapp.com/schema2pojo.jsp">link</a>);</li>
    <li>JSON Patch (<a
    href="http://json-schema-validator.herokuapp.com/jsonpatch.jsp">link</a>);</li>
    <li>conversion of an Avro schema to a JSON Schema (<a
    href="http://json-schema-validator.herokuapp.com/avro.jsp">link</a>);</li>

</ul>

<h2>Running the application locally</h2>

<p>This application uses an embedded Jetty server and is self contained. You need to have a JDK (at
least 1.6) and maven installed (3.0.x preferably, but 2.x will also work).</p>

<p>Into the main directory, type:</p>

<pre>
mvn clean install
</pre>

<p>This will download all necessary artifacts and prepare everything. Then run the application
with:</p>

<pre>
java -cp target/classes:target/dependency/* com.github.fge.jsonschema.WebApp
</pre>

<p>The application will listen on the loopback interface, and on port 8080 by default; in order to
access it, you will therefore type the following as a URL in your browser:</p>

<p>
http://localhost:8080
</p>

