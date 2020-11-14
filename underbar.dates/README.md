# Date

Working with dates is historically a problem with Java and has traditionally been solved with JodaTime. Java 7 went someway to fix that by introducing Calendar and Instant, both of whom are incredibly powerful, but unwieldy in the common case.

Currently this does the bare minimum. Simplify creation of Java Date for interaction with older libraries, and a few convenience methods to make working with ISO and Instant easier.

Expect this to expand dramatically next time I need to do some date maths.