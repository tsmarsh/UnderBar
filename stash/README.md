# Stash

If dynamic languages have taught is anything, it is the power of schemaless datastructures.

JSON beat XML because you can complete a project in JSON before you've finished having the meetings to complete the design of the schema for XML.

Get it done. Get it right.

In that order.

* Is JSON faster parse to native objects? No
* Is JSON faster to transmit? No (gzip)
* Does JSON have a date format? No
* Does JSON have comments? No

And yet I've written more JSON in the last 5 years than almost any other object literal syntax.


Stash hopes to fill the same gap that JSON fills in the Javascript world.

It is just Map<String, Object>, but with a bunch of functions that makes pulling typed data out of it easier, and allows the correct schema to evolve instead of being planned, and its happiest when it is being used as a receptacle for incoming data or for passing around arbitrary data when you really don't need an object.
