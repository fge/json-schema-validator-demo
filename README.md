## What this is

<p>This is a Heroku-based Java webapp which demonstrates <a
href="https://github.com/fge/json-schema-validator">json-schema-validator</a>.</p>

<p>The URL is <a href="http://json-schema-validator.herokuapp.com">Here</a>. It is really basic
HTML/JSP/CSS stuff, and reflects the HTML/JSP/CSS skills of the author (that is, close to zero). But
it works.</p>

<p>Contributions are of course more than welcome!</p>

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -cp target/classes:target/dependency/* org.eel.kitchen.jsonschema.WebApp

