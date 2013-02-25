## What this is

<p>This is a Heroku-based Java webapp which demonstrates usages of the following packages:</p>

* <a href="https://github.com/fge/json-schema-core">json-schema-core</a>;
* <a href="https://github.com/fge/json-schema-validator">json-schema-validator</a>;
* <a href="https://github.com/reinert/JJSchema">JJSchema</a>;
* <a href="https://github.com/fge/json-schema-processor-examples">json-schema-processor-examples</a>.

<p>This application is available <a href="http://json-schema-validator.herokuapp.com">here</a>.
Right now, it has pages demonstrating the following:</p>

* full validation (schema + data);
* syntax validation for draft v4;
* generation of a JSON Schema from a Java source file.

<p>Please see the links to the relevant projects for more information.</p>

## Running the application locally

<p>This application uses an embedded Jetty server and is self contained. You need to have a JDK (at
least 1.6) and maven installed (3.0.x preferably, but 2.x will also work).</p>

<p>Into the main directory, type:</p>

</pre>
mvn clean install
```

<p>This will download all necessary artifacts and prepare everything. Then run the application
with:</p>

```
java -cp target/classes:target/dependency/* com.github.fge.jsonschema.WebApp
```

<p>The application will listen on the loopback interface, and on port 8080 by default; in order to
access it, you will therefore type the following as a URL in your browser:</p>

<p>
http://localhost:8080
</p>

