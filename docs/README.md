[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=imrafaelmerino_java-fun)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/java-fun/0.9.3)](https://search.maven.org/artifact/com.github.imrafaelmerino/java-fun/0.9.3/jar)

- [Goal](#goal)
- [PRG: Pseudo Random Generators](#prg)
    - [Primitive Types Generators](#ptg)
    - [Collection Generators](#cg)
    - [Tuples and Record Generators](#trg)
    - [Combinators](#com)
    - [Objects Generators](#og)
    - [Useful and common patterns](#ucp)
- [Optics](#optics)
- [Requirements](#req)
- [Installation](#inst)
- [Related projects](#rp)

## <a name="goal"><a/> Goal

The goal of java-fun is to implement in Java some important patterns 
from Functional Programming. It doesn't transliterate these patterns 
from other languages, aiming that any standard Java programmer finds 
easy to adopt them and understand the essence of these concepts, not 
getting lost in unfamiliar types and conventions.

The most important concepts implemented so far are:

- **Pseudo Random Generators**. The best way to do testing is by far 
Property-Based-Testing. And to do it well you need a good set of generators and 
to be able to compose them in any imaginable way. With java-fun, it's child's play!
- **Optics**. Functional programmers use optics instead of getters and setters. 
They are more safe and composable.

## <a name="prg"><a/> Pseudo Random Generators
Pseudorandom number generators (PRN) are important in practice for their speed 
in number generation and their reproducibility. These properties, when it comes 
to testing, are very significant.

A PRG is modeled with the following type Gen:

``` java
import fun.gen.Gen;
import java.util.Random;

public interface Gen<O> extends Function<Random, Supplier<O>>{}

```

A Gen is a function that takes a seed of type Random and returns a lazy 
computation of a type O. Laziness is key in order to be able to compose 
generators easily.

There are two crucial static factory methods to create generators:

- **arbitrary**, that produces a uniform distribution of values
- **biased**, that generates with a higher probability values that are 
proven to cause more bugs in our code. This is vital to do Property Based 
Testing and ultimately, which is more important, to find bugs.

You can create any generator just implementing the interface Gen. Nevertheless 
there are a lot predefined generators you can use. Let's go over them


### <a name="ptg"><a/> Primitive Types Generators
- String generator

 ```  java
 
Gen<String> :: StrGen.biased(int minLength, int maxLength)
              
```

Biased produces with higher probability the empty string if minLength is zero, blank strings,
and strings of length minLength and maxLength (the bounds of the interval).

 ```  java
 
Gen<String> :: StrGen.arbitrary(int minLength, int maxLength)
              
```

Arbitrary produces any string with a length uniformly distributed over the interval [minLength, maxLength]

Other arbitrary string generators:

 ```  java
 
Gen<String> :: StrGen.alphanumeric(int minLength, int maxLength)

Gen<String> :: StrGen.alphabetic(int minLength, int maxLength)

Gen<String> :: StrGen.letters(int minLength, int maxLength)

Gen<String> :: StrGen.digits(int minLength, int maxLength)

Gen<String> :: StrGen.ascii(int minLength, int maxLength)
              
```

- Integer generator

```  java

Gen<Integer> :: IntGen.biased()

```

Biased produces with higher probability the values zero, Byte.MAX_VALUE, Byte.MIN_VALUE,
Short.MAX_VALUE, Short.MIN_VALUE, Integer.MAX_VALUE and Integer.MIN_VALUE


```  java

Gen<Integer> :: IntGen.biased(int min, int max)

```

Bounded biased produces with higher probability the bounds  min and max, and
all the mentioned values produced by the unbounded biased that fall into the interval 

The arbitrary integer generators produces any integer with a uniform distribution.

```  java

Gen<Integer> :: IntGen.arbitrary()

Gen<Integer> :: IntGen.arbitrary(int min, int max)

```


- Long generator

```  java

Gen<Long> :: LongGen.biased()

```

Biased produces with higher probability the values zero, Byte.MAX_VALUE, Byte.MIN_VALUE,
Short.MAX_VALUE, Short.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Long.MIN_VALUE
and Long.MAX_VALUE


```  java

Gen<Long> :: LongGen.biased(long min, long max)

```

Bounded biased produces with higher probability the bounds  min and max, and
all the mentioned values produced by the unbounded biased that fall into the interval.

The arbitrary long generators produces any long with a uniform distribution.

```  java

Gen<Integer> :: LongGen.arbitrary()

Gen<Integer> :: LongGen.arbitrary(long min, long max)

```

- Double generator

 ```  java

```

- Big Integer generator

 ```  java

```

- Big Decimal generator

 ```  java

```

- Bytes generator

 ```  java

```

- Character generator

 ```  java

```

- Boolean generator

 ```  java

```

- Instant generator

 ```  java

```



### <a name="cg"><a/> Collection Generators

- List generator

 ```  java

```

- Set generator

 ```  java

```

- Map generator

 ```  java

```

### <a name="trg"><a/> Tuples and Record Generators

- Pair generator

 ```  java

```

- Triple generator

 ```  java

```

- Record generator

 ```  java

```


### <a name="com"><a/> Combinators

- OneOf 

 ```  java

```

- Freq

 ```  java

```

- Nullable

 ```  java

```

- Combinations

 ```  java

```

- Permutations

 ```  java

```

### <a name="og"><a/> Objects Generators

You can generate any object from your model just using the _RecordGen_ and 
the function map. Consider the class User:

```java 

public class User {

    String login;
    String name;
    String password;
 
    public User(String login,
                String name,
                String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }
    
    //toString,equals, hashcode, getters all that stuff

}

```

Let's define a User generator:


```java   

 Gen<String> loginGen = StrGen.alphabetic(0, 100);

 Gen<String> passwordGen = StrGen.biased(0, 100);

 Gen<String> nameGen = StrGen.alphabetic(0, 100);

 Gen<User> userGen = RecordGen.of("login", loginGen,
                                  "name", nameGen,
                                  "password", passwordGen)
                              .setAllOptionals()
                              .map(record ->
                                             new User(record.getStr("login").orElse(null),
                                                      record.getStr("name").orElse(null),
                                                      record.getStr("password").orElse(null))
                                  );

```
### <a name="ucp"><a/> Useful and common patterns

The function suchThat takes a predicate and returns a new generator that 
produces only values that satisfy the condition. For example, let's use 
this idea to create generators of valid and invalid data:    

```java   

  Predicate<User> isValid = user ->
                                    user.getLogin() != null &&
                                    user.getPassword() != null &&
                                    user.getName() != null &&
                                    !user.getLogin().trim().isEmpty() &&
                                    !user.getName().trim().isEmpty() &&
                                    !user.getPassword().trim().isEmpty();

  Gen<User> validUserGen = userGen.suchThat(isValid);

  Gen<User> invalidUserGen = userGen.suchThat(isValid.negate());
  

```
## <a name="optics"><a/> Optics: Lenses, Optionals and Prism

It’s ubiquitous to have to navigate through recursive data structures 
like records and tuples to find, insert, and modify data. It’s a 
cumbersome and error-prone task (a NullPointerException is always lurking 
around) that requires a defensive style of programming with much boilerplate 
code. The more nested the structure is, the worse. The imperative style 
uses getters and setters with the mentioned inconveniences, whereas 
Functional Programming uses optics to cope with these limitations,. 

Before getting into more details about optics and their implementation 
in **java-fun**, I'm going to explain ADTs (Algebraic Data Types).

A type is nothing else than a name for a set of values. Not like objects, 
they don't have any behavior. We can operate with types. Given the types 
A and B and their domains:

```code

A = { "a", "b" }
B = { 1, 2, 3 }

```

It's possible to create new types out of them. We can pair A and B and 
get a tuple of two elements:

```code

T = ( A, B )
T = [ ("a", 1), ("a", 2), ("a", 3), ("b", 1), ("b", 2), ("b", 3) ]

```

The order matter; (B, A) would be a different type. Tuples are product-types 
( 2 x 3 possible values).

We can group A and B in fields and get a record:

```code

R = { f: A,  f1: B }
R = [
{ f:"a", f1:1}, { f:"a", f1:2}, { f:"a", f1:3}, { f:"b", f1:1},
{ f:"b", f1:2}, { f:"b", f1:3}
]

```

The order of the fields doesn't matter. Records are other class of product-types 
(2 x 3 possible values). Java added records in release 14. Thank god!

We can sum A and B and get a sum-type:

```code

S = A | B
S= [ "a", "b", 1, 2, 3 ]

```

It has  2 + 3 possible values. A sum-type is a type that can be one of the 
multiple possible options. In other words, S is either A or B.

In FP, optics are used to work with ADTs. There are different kinds of optics.
**Lenses** and **Optionals**(don't confuse with Java Optional class) work 
great with product-types. **Prisms** help us work with sum-types. Optics allow 
us to separate concerns.

It's important to distinguish the following concepts:

- The action. An action is a function that executes some operation over the 
focus of a path. The most important actions are _get_, _set_ and, _modify_.
- The path. The path indicates which data to focus on and where to find it within 
the structure.
- The structure. The structure is the hunk of data that we want to work with. 
The path selects data from within the structure, and that data will be passed 
to the action. 
- The focus. The smaller piece of the structure indicated by the path. The focus 
will be passed to the action.

A Lens zooms in a piece of data within a larger structure. **A Lens must never 
fail to get or modify its focus.** Optional is another optic just like a lens, 
**but the focus may not exist**.  

We'll use the following records to put some examples:

```java     

public record Person(String name, Address address, Integer ranking) {

    public Person {
        if(name==null || name.isBlank()) 
            throw new IllegalArgumentException("name empty");
  
        if(address == null) 
            throw new IllegalArgumentException("address empty");
    }

}

public record Address(Coordinates coordinates, String description) {

    public Address {
        if(coordinates == null && description == null)  
            throw new IllegalArgumentException("invalid address");
    }

}

public record Coordinates(double longitude, double latitude) {

    public Coordinates {
        if(longitude < -180 || longitude > 180) 
            throw new IllegalArgumentException("180 => longitude >= -180");
        
        if(latitude < -90 || latitude > 90) 
            throw new IllegalArgumentException("90 => latitude >= -90");
    }
}



```

Let's create some optics with json-fun:

```java   
   // Person is the whole structure
   // Address is the focus, an it's a required field according to Person constructor
   
   Lens<Person, Address> addressLens =
         new Lens<>(Person::address,
                    address -> person -> new Person(person.name(),
                                                    address,
                                                    person.birthDate()));

      
   // Person is the whole structure
   // The String representing the name is the focus, and it's also a required field
   
   Lens<Person, String> nameLens =
         new Lens<>(Person::name,
                    name -> person -> new Person(name,
                                                 person.address(),
                                                 person.birthDate()));
   
   // Person is the whole structure
   // The Integer representing the ranking is the focus, and it's not required
   // we use an optional instead of a lens                                           
   
   Option<Person, Integer> rankingOpt =
         new Option<>(person -> Optional.ofNullable(person.ranking()),
                      ranking -> person -> new Person(person.name(),
                                                      person.address(),
                                                      ranking));
```

As you can see, to create a Lens or and Optional you just need to define the 
_get_ and _set_ actions. In lenses the get action returns the focus, whereas 
in optionals it returns the focus wrapped in a Java Optional since it may not 
exist. In java-fun the optional optic is called Option.

And what about the modify action? It's created internally from _get_ and _set_! 

Defining the _set_ action you may notice how cumbersome to create records is. 
They are immutable data structures and every modification means to create a 
new instance. And it's even more cumbersome when we have nested records. We'll 
see how composing optics can help us with this.


Let's discuss the type of the most important actions of a lens and an optional, 
where S is the type of the whole structure and F the type of the focus:

```code

get :: Function<S, F>            // for lenses
get :: Function<S, Optional<F>>  // for optionals
set :: Function<F, Function<S, S>>
modify :: Function<Function<F, F>, Function<S, S>>

```

Imagine the focus is the name of the person. The get action is a function that
takes a person and returns their name. The set action is a function that takes
a new name and returns a function that, given a person, it produces a new one
with the new name. The modify action is like set, but instead of a name, it 
takes a function to produce a new name from the old one. 


Let's check out a practical example.

```java   

Person joe = new Person("Joe",address,null);

Person joeArmstrong = nameLens.set.apply("Joe Armstrong").apply(joe);

// records are immutable
Assertions.assertEquals("Joe",
                        nameLens.get.apply(joe));
                        
Assertions.assertEquals("Joe Armstrong",
                        nameLens.get.apply(joeArmstrong));                        

Function<String, String> toUpper = String::toUpperCase;

Person joeUpper = nameLens.modify.apply(toUpper).apply(joe);

Assertions.assertEquals("JOE",
                        nameLens.get.apply(updated));
                        
//let's increment the ranking by one

//since ranking is null, the same person is returned       
Assertions.assertEquals(joe, 
                        rankingOpt.modify.apply(ranking -> ranking + 1).apply(joe))       

Person joeRanked = rankingOpt.set(1).apply(joe);

Assertions.assertEquals(1, 
                        joeRanked.ranking())       

//since ranking is 1, the function is applied and a new person is created
Person joeRankedUpdated = rankingOpt.modify.apply(ranking -> ranking * 10).apply(joeRanked);

Assertions.assertEquals(10, 
                        joeRankedUpdated.ranking())       
               
```

Do notice that it’s at the very end when we passed in the person into the
functions. In OOP, it would be just the opposite, the starting point would 
be a person object, and then we would get or set a value with a getter or 
setter. In FP, we describe actions; then, we may compose them, and it’s at 
the last moment when we specify the inputs and execute them.

A Lens must respect the getSet law, which states that if you get a value and
set it back in, the result is a value identical to the original one. A side
effect of this law is that set must only update the value it points to,
nothing else. On the other hand, the setGet law states that if you set a value,
you always get the same value. This law guarantees that set is updating a value
inside the container. Laws are relevant in FP. They help us reason about our
code more clearly.


Let's change gears and talk about Prisms. If you think of a Prism does to light,
it happens the same with any sum-type. We have several subtypes to
consider, and we want to focus on a specific one.  Let's create a Prism

```code  

  Prism<Exception, RuntimeException> prism =
                  new Prism<>(e -> {
                      if (e instanceof RuntimeException) return Optional.of(((RuntimeException) e));
                      return Optional.empty();
                  },
                              r -> r);
                            
```      

You need to create two functions. The first one, how to go from the type 
Exception to the specific subtype RuntimeException, and the second one, 
the other way around (since a RuntimeException is aN Exception, just return it).

But you can create Prism to extract a subset of values with a specific property 
from a more generic set. For example, let's create a Prism to get all string that 
are integer numbers:

```code         

   Prism<String, Integer> intPrism =
                new Prism<>(str -> {
                     try {
                        return Optional.of(Integer.parseInt(str));
                        } 
                     catch (NumberFormatException e) {
                        return Optional.empty();
                     }
                    },
                            integer -> Integer.toString(integer)
                );
```

As you can see the first function goes from String to Integer,and it can fail, 
since there are a lot of strings that are not numbers. And the second one goes 
from Integer to String and of course, it never fails (every number has a string 
representation)

Considering the above Prism, let's take a look at the most important actions and 
their signatures:

```code  

getOptional :: Function<String, Optional<Integer>>

modify :: Function<Function<Integer, Integer>, Function<String, String>>

modifyOpt :: Function<Function<Integer, Integer>, Function<String, Optional<String>>>

```

The getOptional function takes a String, and if it's not a number, it returns an 
_Optional.empty_. If it's a number, it returns its value wrapped in an Optional. 
Nothing exceptional, isn't it?

The modify function is handy. I use it all the time. It takes a function to map 
numbers and returns a function from String to String. If the input value is a 
number, it applies the map function on it and returns it as a String. If it is 
not a number, we can not use the map function, and the input is returned as it was. 
Do notice that we don't care about the success of the operation. If we do, we can 
use the modifyOpt action.  It's the same, but when de map function can not be 
applied, an empty Optional is returned.

Let's put some examples with the intPrism defined above:

```java 

Assertions.assertEquals(Optional.of("10"),
                        intPrism.getOptional.apply("10"));

// apple is not a string, empty is returned
Assertions.assertEquals(Optional.empty(),
                        intPrism.getOptional.apply("apple"));

Assertions.assertEquals("10",
                        intPris.mmodify.apply(a -> a * 10)
                                      .apply("1"));

// apple is not a string, the same value is returned
Assertions.assertEquals("apple",
                        intPrism.modify.apply(a -> a * 10)
                                       .apply("apple"));

``` 

You can compose lenses, optional and prism. For example:

``` java

   Option<Address, Coordinates> coordinatesOpt =
         new Option<>(address -> Optional.ofNullable(address.coordinates()),
                      coordiantes -> address -> new Address(coordinates,
                                                            address.description(),
                                                            )
                     );
            
   Lens<Coordinate, Double> longitudeLens =
         new Option<>(Coordinates::longitude,
                      coordinates -> lon -> new Coordinates(long,
                                                            coordinates.latitude()
                                                            )
                     );
            
                        
   Lens<Coordinate, Double> latitudeLens =
         new Option<>(Coordinates::latitude,
                      coordinates -> lat -> new Coordinates(coordiantes.longitude(),
                                                            lat
                                                            )
                     );
            
   // let's apply composition!
 
   Option<Person,Double> personLatitudeOpt = 
                addressLens.compose(coordinatesOpt)
                           .compose(latitudeLens);    

   Option<Person,Double> personLongitudeOpt = 
                addressLens.compose(coordinatesOpt)
                           .compose(longitudeLens);                        
            
            
```    

Composition is key to handle complexity. Imagine you have to create a function 
to set the latitude and longitude of a person:

```  java

Function<Coordinates,Function<Person,Person>> setCoordinates = 
        c -> personLatitudeOpt.set.apply(c.latitude())
                              .andThen(personLongitudeOpt.set.apply(c.longitude()))
                               
Person newPerson = setCoordiantes.apply(new Coordinates(14.5,45.78))
                                 .apply(person);                          


```  

## <a name="req"><a/> Requirements

Requires Java 8 or greater


## <a name="inst"><a/> Installation

```xml

<dependency>
    <groupId>com.github.imrafaelmerino</groupId>
    <artifactId>java-fun</artifactId>
    <version>0.9.3</version>
    <classifier>jdk8</classifier>
</dependency>

```

## <a name="rp"><a/> Related projects

[json-values](https://github.com/imrafaelmerino/json-values) has defined a JSON generator
and some optics to manipulate JSON using this library.

