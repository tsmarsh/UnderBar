[![Build Status](https://travis-ci.org/tsmarsh/UnderBar.svg?branch=master)](https://travis-ci.org/tsmarsh/UnderBar)
[![codecov](https://codecov.io/gh/tsmarsh/UnderBar/branch/master/graph/badge.svg)](https://codecov.io/gh/tsmarsh/UnderBar)


#Underbar

## Summary

This project is a demonstration of how to craft a reusable Java 8 library that promotes the use of type inference, data and lambda over traditional OO Java.

It does not represent a complete solution. There maybe polymorphs of certain functions that you find essential. In those situations you have two options:

1. Create your own library with the functions and statically import them into your project
2. Create a pull request for this project

I hope you choose the latter. If you do, please make sure you have unit tests that describe the function.

I have split the project into 4 modules.

* **The Ocho:** The bedrock of this library. Contains traditional building block functions that I have stolen mercilessly from functional programming.
* **Stash:** A "String Hash" that fills an enormous gap in Javas data structures. Think of it as JSON for Java.
* **Dates:** Dates in Java are less miserable than ever, this module hopes to make them a joy.
* **IO:** A good example of how to remove Java boiler plate with static functions

## History

This project was cooked up after a series of conversations with [Jeff Bay](https://www.linkedin.com/in/jeff-bay-7411b6) as he explained why:

1. Java 8 is the best platform he's ever used
2. I was doing it wrong

The first point was interesting. I know Jeff because, along with [Eric Knell](https://www.linkedin.com/in/eric-knell-31bb072), he was my boss at Outpace, a company that I joined predominantly because it was a Clojure shop. After Outpace they had worked together again and 'fixed' Java.

The second... the second bit peeked my interest.

Jeff and Eric's premise is that Java 8 can become functional enough if you abandon classes, tricky in a language where everything is a class based object. The trick?

### Static functions

The hints are in the language.

Java has had static imports since Java 5. With lambda, and 'effectively final', static functions get the closures they've desperately needed.

With static functions come static initializers:

```java
Map<String, Integer> foo = hash("foo", 1, "bar", 2);
```

It might not be homioiconic, but its better than:

```java
Map<String, Integer> foo = HashMap<>(){{
  put("foo", 1);
  put("bar", 2);
}}
```

and, because of generics, its type checked.

There are similar methods for other core data types:

```java
Map<String, Integer> m = hash("foo", 1, "bar", 2);
List<Integer> l = list(1,2,3);
Set<Integer> s = set(1,1,2);
Int[] a = array(1,2,3,4);
```

Another issue with Java is boilerplate. Traditionally you'd fix it with very specific classes. UnderBar hopes to demonstrate a better way.

Take the StreamAPI, which is fine, but accessing it is different depending on the data type. UnderBar provides a consistent ```stream(T t)``` so that you can call


```java
import static com.tailoredshapes.underbar.*;
...

stream(array(1,2,3)).forEach...;
stream(list(1,2,3)).forEach...;
stream(set(1,1,3).forEach...;
```

of course, thats still a fair amount of boiler plate for a ```map()```

## Comparison

### Java

```java
List<Integer> l = new ArrayList<>();
List<String> s = new ArrayList<>();
l.add(1);
l.add(2);
l.add(3);
for(Integer i : l){
  s.add(l.toString());
}
```

### Clojure

```clojure
(map str [1,2,3])
```

### Java with Underbar

```java
map(list(1,2,3), Object::toString());
```
