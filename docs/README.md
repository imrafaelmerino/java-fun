<img src="./logo/package_twitter_if9bsyj4/base/full/coverphoto/base_logo_white_background.png" alt="logo"/>

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=imrafaelmerino_java-fun)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/java-fun/0.9.4)](https://search.maven.org/artifact/com.github.imrafaelmerino/java-fun/0.9.4/jar)

"_When in doubt, use brute force._" **Ken Thompson**

- [Goal](#goal)
- [Pseudo Random Generators](#prg)
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
from Functional Programming. **It doesn't transliterate these patterns 
from other languages**, aiming that any standard Java programmer finds 
it easy to adopt them and understand the essence of these concepts, not 
getting lost in unfamiliar types and conventions.

The most important concepts implemented so far are:

- **Pseudo Random Generators**. The best way to do testing is by far 
Property-Based-Testing. And to do it well you need a good set of generators and 
to be able to compose them in any imaginable way. With java-fun, it's child's play!
- **Optics**. Functional programmers use optics instead of getters and setters. 
They are safe and composable. Using optics properly, you'll never see
again a NullPointerException!
- **Tuples**. Java won't probably implement tuples, but I still consider them very
useful. I've implemented tuples of two elements,i.e. pairs, and tuples of three elements,
i.e. triples. 

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
computation of type O. Laziness is key to be able to compose 
generators easily.

There are two crucial static factory methods to create generators:

- **arbitrary**, that produces a uniform distribution of values.
- **biased**, that generates with a higher probability some values that are 
proven to cause more bugs in our code. This is vital to do Property Based 
Testing.

You can create any generator just by implementing the interface _Gen_. Nevertheless 
there are a lot of predefined generators you can use. Let's go over them.


### <a name="ptg"><a/> Primitive Types Generators
- String generator

The bounded-string-biased generator produces with higher probability the empty string, 
if _minLength_ is zero, blank strings, and strings of length _minLength_ and _maxLength_:

 ```  java
 
Gen<String> :: StrGen.biased(int minLength, int maxLength)
 
```

For example:

``` java

Gen<String> gen =  StrGen.biased(0,3);

gen.sample(10).forEach(System.out::println);

```

would print out strings like "", "   " or any string of length from 0 to 3 made 
up of valid printable Unicode characters.

The bounded-string-arbitrary generator produces any string of length **uniformly distributed** over 
the interval [minLength, maxLength]. Not like the biased generator, all the values are generated 
with the same probability.

 ```  java
 
Gen<String> :: StrGen.arbitrary(int minLength, int maxLength)
              
```


Other bounded-string-arbitrary generators are:

 ```  java
 
Gen<String> :: StrGen.alphanumeric(int minLength, int maxLength)

Gen<String> :: StrGen.alphabetic(int minLength, int maxLength)

Gen<String> :: StrGen.letters(int minLength, int maxLength)

Gen<String> :: StrGen.digits(int minLength, int maxLength)

Gen<String> :: StrGen.ascii(int minLength, int maxLength)
              
```


- Integer generator

The unbounded-integer-biased generator produces with a higher probability the values zero, _Byte.MAX_VALUE_, 
_Byte.MIN_VALUE_, _Short.MAX_VALUE_, _Short.MIN_VALUE_, _Integer.MAX_VALUE_ and _Integer.MIN_VALUE_:

```  java

Gen<Integer> :: IntGen.biased()

```

The bounded-integer-biased generator produces with higher probability _min_ and _max_, and
all the above-mentioned values that fall into the interval (min, max):

```  java

Gen<Integer> :: IntGen.biased(int min, int max)

```


The unbounded-integer-arbitrary generator produces any integer number with the same 
probability (uniform distribution):

```  java

Gen<Integer> :: IntGen.arbitrary()

```

whereas the bounded-integer-arbitrary generator produces any integer number between min and max (inclusive)
with the same probability (uniform distribution):


```  java

Gen<Integer> :: IntGen.arbitrary(int min, int max)

```


- Long generator

The unbounded-long-biased generator produces with higher probability the values zero, _Byte.MAX_VALUE_,
_Byte.MIN_VALUE_, _Short.MAX_VALUE_, _Short.MIN_VALUE_, _Integer.MAX_VALUE_, _Integer.MIN_VALUE_,
_Long.MIN_VALUE_ and _Long.MAX_VALUE_:

```  java

Gen<Long> :: LongGen.biased()

```

The bounded-long-biased generator produces with higher probability _min_ and _max_, and
all the above-mentioned values that fall into the interval (min, max):

```  java

Gen<Long> :: LongGen.biased(long min, long max)

```


The unbounded-long-arbitrary generator produces any integer number with the same
probability (uniform distribution):

```  java

Gen<Long> :: LongGen.arbitrary()

```

whereas the bounded-long-arbitrary generator produces any integer number between min and max (inclusive)
with the same probability (uniform distribution):


```  java

Gen<Long> :: LongGen.arbitrary(long min, long max)

```

- Double generator

The unbounded-double-biased generator produces with higher probability the values zero, _Byte.MAX_VALUE_,
_Byte.MIN_VALUE_, _Short.MAX_VALUE_, _Short.MIN_VALUE_, _Integer.MAX_VALUE_, _Integer.MIN_VALUE_,
_Long.MIN_VALUE_, _Long.MAX_VALUE_, _Double.MIN_VALUE_ and _Double.MAX_VALUE_:

```  java

Gen<Double> :: DoubleGen.biased()

```

The bounded-double-biased generator produces with higher probability _min_ and _max_, and
all the above-mentioned values that fall into the interval (min, max):

```  java

Gen<Double> :: DoubleGen.biased(double min, double max)

```


The unbounded-double-arbitrary generator produces any integer number with the same
probability (uniform distribution):

```  java

Gen<Double> :: DoubleGen.arbitrary()

```

whereas the bounded-double-arbitrary generator produces any integer number between min and max (inclusive)
with the same probability (uniform distribution):


```  java

Gen<Double> :: DoubleGen.arbitrary(double min, double max)

```


- Big Integer generator

Big integer generators accept the maximum number of bits as a parameter. They 
always produce positive numbers.

The arbitrary generator generates big integers uniformly distributed between 0 
and 2^bits - 1. The biased generator produces with higher probability zero and,
if the maximum number of bits is big enough, Byte.MAX_VALUE, Short.MAX_VALUE,
Integer.MAX_VALUE and Long.MAX_VALUE:

 ```  java
 
Gen<BigInteger> :: BigIntGen.biased(int bits)

Gen<BigInteger> :: BigIntGen.arbitrary(int bits)

```


- Big Decimal generator

The unbounded-decimal-biased generator produces with higher probability the values zero, _Byte.MAX_VALUE_,
_Byte.MIN_VALUE_, _Short.MAX_VALUE_, _Short.MIN_VALUE_, _Integer.MAX_VALUE_, _Integer.MIN_VALUE_,
_Long.MIN_VALUE_, _Long.MAX_VALUE_, _Double.MIN_VALUE_ and _Double.MAX_VALUE_:

```  java

Gen<BigDecimal> :: BigDecGen.biased()

```

The bounded-decimal-biased generator produces with higher probability _min_ and _max_, and
all the above-mentioned values that fall into the interval (min, max):

```  java

Gen<BigDecimal> :: BigDecGen.biased(BigDecimal min, BigDecimal max)

```


The unbounded-decimal-arbitrary generator produces any integer number with the same
probability (uniform distribution):

```  java

Gen<BigDecimal> :: BigDecGen.arbitrary()

```

whereas the bounded-decimal-arbitrary generator produces any integer number between min and max (inclusive)
with the same probability (uniform distribution):


```  java

Gen<BigDecimal> :: BigDecGen.arbitrary(BigDecimal min, BigDecimal max)

```

- Bytes generator

 ```  java
 
Gen<byte[]> :: BytesGen.biased(int minLength, int maxLength)
 
Gen<byte[]> :: BytesGen.arbitrary(int minLength, int maxLength)


```

- Character generator


```  java
  
Gen<Character> :: CharGen.arbitrary()  

Gen<Character> :: CharGen.arbitrary(char min, char max)  
 
Gen<Character> :: CharGen.alphanumeric()

Gen<Character> :: CharGen.alphabetic()

Gen<Character> :: CharGen.letter()

Gen<Character> :: CharGen.digit()

Gen<Character> :: CharGen.ascii()
              

```

- Boolean generator

 ```  java
 
 Gen<Boolean> :: GenBool.arbitrary()

```

produces true or false with the same probability

- Instant generator

The unbounded-instant-biased generator produces with higher probability the values
1970-01-01T00:00:00Z (epoch time) 1901-12-13T20:45:52Z, (_Integer.MIN_VALUE_ from epoch time),
2038-01-19T03:14:07Z (_Integer.MAX_VALUE_ from epoch time), _Instant.MAX_ and _Instant.MIN_:

```  java

Gen<Instant> :: InstantGen.biased()

```

The bounded-instant-biased generator produces with higher probability _min_ and _max_ 
seconds from the epoch time, and all the above-mentioned instants that fall into the interval 
(min, max):

```  java

Gen<Instant> :: IntantGen.biased(long min, long max)

```


The unbounded-instant-arbitrary generator produces any instant  with the same
probability (uniform distribution):

```  java

Gen<Instant> :: InstantGen.arbitrary()

```

whereas the bounded-instant-arbitrary generator produces any instant between min and max (inclusive)
with the same probability (uniform distribution):


```  java

Gen<Instant> :: InstantGen.arbitrary(int min, int max)

```


### <a name="cg"><a/> Collection Generators

- List generator

 ```  java
 
Gen<List<T>> :: ListGen.biased(Gen<T> gen,
                                int minLength,
                                int maxLength
                               )

```

 ```  java
 
Gen<List<T>> :: ListGen.arbitrary(Gen<T> gen,
                                   int minLength,
                                   int maxLength
                                 )

```

- Set generator

If the generator cannot or is unlikely to produce enough distinct
elements (specified by the parameter size), this generator will fail after 10*size tries.
You can create a new generator with a different number of tries using the method _setMaxTries_.


```  java
 
Gen<Set<T>> ::  SetGen.of(Gen<T> gen,
                          int size
                         )

 
```

- Map generator

If the key generator cannot or is unlikely to produce enough distinct
keys (specified by the parameter size), this generator will fail after 10*size tries.
You can create a new generator with a different number of tries using the method _setMaxTries_.

 ```  java

Gen<Map<K,V>> ::  MapGen<K,V>.of(Gen<K> keyGen,
                                 Gen<V> valueGen,
                                 int size
                                 )

```

### <a name="trg"><a/> Tuples and Record Generators

- Pair generator

 ```  java

Gen<Pair<A, B>> ::  PairGen.of(Gen<A> _1,
                                Gen<B> _2
                              )

```

- Triple generator

 ```  java
 
Gen<Triple<A, B, C>> ::  TripleGen.of(Gen<A> _1,
                                       Gen<B> _2,
                                       Gen<C> _3
                                     ) 

```

- Record generator

A record is just a group of field names and their associated values. This is a record
generator in java-fun

 ```  java
 
import fun.gen.Record 
 
Gen<Record> person = RecordGen.of(name, StrGen.arbitrary(1,20),
                                   age, IntGen.biased(0,150),
                                   birthdate, InstantGen.arbitrary() )

```

The class fun.gen.Record it's just a Map<String,?> to hold the generated data and
syntactic sugar to get the value of any field without doing any type conversion.
Record generators are extremely useful to create any object generator using the 
function map. Find [here](#og) an example.


### <a name="com"><a/> Combinators

- OneOf 


 ```  java
 
Gen<A> :: Combinators.oneOf(Gen<? extends A> gen,
                             Gen<? extends A>... others)
                            

```

oneOf combinator chooses each generation a generator from the list passed in as parameters.
All the generators have the same probability to be picked, and are independent, i.e. they
generate values from a different seed.

Instead of a list of generators, you can pass in a list of values

 ```  java
 
Gen<A> :: Combinators.oneOf(List<A> values)                            
                         

```

or, using varargs

 ```  java
 
Gen<A> :: Combinators.oneOf(A value,
                             A... others)                           
                         

```

- Freq

Freq combinator is like oneOf, but you can assign a different weight to each generator to control
the probability they are chosen with

 ```  java
 
Gen<A> ::  Combinators.freq(Pair<Integer, Gen<? extends A>> freq,
                             Pair<Integer, Gen<? extends A>>... others
                            )

```

For example, let's generate strings with a 75% prob. and booleans with a 25%:

 ```  java
 
 Combinators.freq(Pair.of(3, StrGen.biased(0,10)),
                  Pair.of(1, BoolGen.arbitrary())
                 )  
 
 ```


- Nullable

Sometimes, you want to generate null, especially if you are interested in catching bugs. Remember that
NullPointerException is probably the most seen exception in Java! The nullable combinator produces null
50% of the generations.

 ```  java

Gen<A> :: Combinators.nullable(Gen<A> gen) 

```

You can control the probability null is generated.


 ```  java

//generates null % specified by prob

Gen<A> :: Combinators.nullable(Gen<A> gen,
                                int prob) 

```



- Combinations

Given a list with n elements, there are n!/k!(n - k)! combinations of k elements without repetition:

 ```  java
 
Gen<Set<A>> ::  Combinators.combinations(int k,
                                          Set<I> input) 


```

- Subsets

Given a set with n elements and considering that each element can be included or not 
(two possible states), we have 

2*2*2... n times = 2^n possible subsets

Since the empty set is not returned, the subsets combinator generates  2^n -1
different subsets. For example, given the set ["a","b","c"], all the possible values are
[[a], [b], [c], [a, b], [a, c],  [b, c],  [a, b, c]]


 ```  java
 
Gen<Set<A>> ::  Combinators.subsets(Set<A> input) 

```

This combinator is used to generate all possible combinations of optional and nullable fields
in the record generator.

### <a name="og"><a/> Objects Generators

You can generate any object from your model just by using the _RecordGen_ and 
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
    
    //toString, equals, hashcode, getters all that stuff

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
                              .setAllOptional()
                              .setAllNullable()
                              .map(record ->
                                             new User(record.getStr("login").orElse(null),
                                                      record.getStr("name").orElse(null),
                                                      record.getStr("password").orElse(null))
                                  );

```
### <a name="ucp"><a/> Useful and common patterns

#### <a name="suchthat"><a/> Such-That

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

#### <a name="permt"><a/> Generating all optional and nullable fields


#### <a name="flatmap"><a/> Flatmap

You can create new generators from existing ones using the flatmap function. For example,
let's create a set generator where the number of elements is random between 0 and ten.

```  java


Gen<String> elemGen = StrGen.letters(5, 10);

Gen<Set<String>> setGen = IntGen.arbitrary(1,10)
                                .then(size ->  SetGen.of(elemGen,size))


```

Do notice that the size is determined at creation time, in other words, all the 
generated sets will have the same size.


## <a name="optics"><a/> Optics: Lenses, Optionals, and Prism

It???s ubiquitous to have to navigate through recursive data structures 
like records and tuples to find, insert, and modify data. It???s a 
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

The order of the fields doesn't matter. Records are other classes of product-types 
(2 x 3 possible values). Java added records in release 14. Thank god!

We can sum A and B and get a sum-type:

```code

S = A | B
S= [ "a", "b", 1, 2, 3 ]

```

It has  2 + 3 possible values. A sum-type is a type that can be one of the 
multiple possible options. In other words, S is either A or B.

In FP, optics are used to work with ADTs. There are different kinds of optics.
**Lenses** and **Optionals**(don't confuse them with Java Optional class) work 
great with product-types. **Prisms** help us work with sum-types. Optics allow 
us to separate concerns.

It's important to distinguish the following concepts:

- The action. An action is a function that executes some operation over the 
focus of a path. The most important actions are _get_, _set_, and, _modify_.
- The path. The path indicates which data to focus on and where to find it within 
the structure.
- The structure. The structure is the hunk of data that we want to work with. 
The path selects data from within the structure, and that data will be passed 
to the action. 
- The focus. The smaller piece of the structure is indicated by the path. The focus 
will be passed to the action.

A Lens zooms in on a piece of data within a larger structure. **A Lens must never 
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

As you can see, to create a Lens or an Optional you just need to define the 
_get_ and _set_ actions. In lenses, the get action returns the focus, whereas 
in optionals it returns the focus wrapped in a Java Optional since it may not 
exist. In java-fun the optional optic is called Option.

And what about the modify action? It's created internally from _get_ and _set_! 

Defining the _set_ action you may notice how cumbersome to create records is. 
They are immutable data structures and every modification means to create a 
new instance. And it's even more cumbersome when we have nested records. We'll 
see how composing optics can help us with this.


Let's discuss the type of the most important actions of a lens and an optional, 
where S is the type of the whole structure and F is the type of the focus:

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

Do notice that it???s at the very end when we passed in the person into the
functions. In OOP, it would be just the opposite, the starting point would 
be a person object, and then we would get or set a value with a getter or 
setter. In FP, we describe actions; then, we may compose them, and it???s at 
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

You need to create two functions. The first one is how to go from the type 
Exception to the specific subtype RuntimeException, and the second one is
the other way around (since a RuntimeException is aN Exception, just return it).

But you can create Prism to extract a subset of values with a specific property 
But you can create Prism to extract a subset of values with a specific property 
from a more generic set. For example, let's create a Prism to get all strings that 
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

As you can see the first function goes from String to Integer, and it can fail 
since there are a lot of strings that are not a number. And the second one goes 
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
    <version>0.9.4</version>
    <classifier>jdk8</classifier>
</dependency>

```

## <a name="rp"><a/> Related projects

[json-values](https://github.com/imrafaelmerino/json-values) has defined a JSON generator
and some optics to manipulate JSON using this library.

