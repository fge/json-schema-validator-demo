<h2>What this is</h2>

<p>This is a Heroku-based Java webapp which demonstrates usages of the following packages:</p>

<ul>
    <li><a href="https://github.com/fge/json-schema-core">json-schema-core</a>;</li>
    <li><a href="https://github.com/fge/json-schema-validator">json-schema-validator</a>;</li>
    <li><a href="https://github.com/reinert/JJSchema">JJSchema</a>;</li>
    <li><a href="https://github.com/fge/json-schema-processor-examples">json-schema-processor-examples</a>.</li>
</ul>

<p>This application is available <a href="http://json-schema-validator.herokuapp.com">here</a>.
Right now, it has pages demonstrating the following:</p>

<ul>
    <li>full validation (schema + data);</li>
    <li>syntax validation for draft v4;</li>
    <li>generation of a JSON Schema from a Java source file.</li>
</ul>

<p>Please see the links to the relevant projects for more information.</p>

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

