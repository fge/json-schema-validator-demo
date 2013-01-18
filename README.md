## What this is

<p>This is a Heroku-based Java webapp which demonstrates the latest development release of<a
href="https://github.com/fge/json-schema-validator">json-schema-validator</a>.</p>

<p>The URL is <a href="http://json-schema-validator.herokuapp.com">Here</a>. It is quite basic:
input your schema and your data, press "validate", see results.</p>

<p>Contributions are of course more than welcome!</p>

## Credits

My HTML/CSS/JavaScript/JQuery skills are close to nil. I'd never have obtained
such a result without help, so I'd like to mention here where I got help:

* IRC channels `#css`, `#html`, `##javascript` and `#jquery` on <a
  href="http://freenode.net">FreeNode</a>;
* helpful answers on <a href="http://stackoverflow.com">Stack Overflow</a>.

## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $java -cp target/classes:target/dependency/* org.eel.kitchen.jsonschema.WebApp

