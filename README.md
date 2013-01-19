## What this is

<p>This is a Heroku-based Java webapp which demonstrates the latest development release of <a
href="https://github.com/fge/json-schema-validator">json-schema-validator</a>.</p>

<p>The URL is <a href="http://json-schema-validator.herokuapp.com">here</a>. It is quite basic:
input your schema and your data, press "validate", see results.</p>

<p>Contributions are of course more than welcome!</p>

## Credits

My HTML/CSS/JavaScript/JQuery skills are close to nil. I'd never have obtained
such a result without help, so I'd like to mention here where I got help:

* IRC channels `#css`, `#html`, `##javascript` and `#jquery` on <a
  href="http://freenode.net">FreeNode</a>;
* helpful answers on <a href="http://stackoverflow.com">Stack Overflow</a>.

## Running the application locally

This application uses an embedded Jetty server and is self contained. You need to have a JDK (at
least 1.6) and maven installed (3.0.x preferably, but 2.x will also work).

Into the main directory, type

    mvn clean install

Then run the application with:

    java -cp target/classes:target/dependency/* org.eel.kitchen.jsonschema.WebApp

It listens on port 8080 by default.

