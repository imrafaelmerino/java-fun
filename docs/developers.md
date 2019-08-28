
- [Why is json-values a one-package library?](#opl)
- [Why some methods have a well-documented Javadoc and others not?](#javadoc)
- [Has json-values been developed using TDD?](#tests)

## <a name="tests"><a/> Why is json-values a one-package library?
I don't want the user to get drowned in hundred of classes that they don't have to know about after hitting the 
autocomplete shortcut of their IDE. Before java 9, it's not possible to do that and having several packages at 
the same time, mainly when one package depends on another. From my point of view, the access modifier public 
in Java, it too public. json-values has been developed and compiled in Java 8. 

## <a name="javadoc"><a/> Why some methods have a well-documented Javadoc and others not?
Writing comments and Javadoc is a polarizing issue in our industry. 
The way I go about it in json-values is the following:
    
   - If a class, method, or field is public, it has to be well-documented. There is no excuse. 
   No matter how good is the code. From my point of view, good and bad code seems the same to 
   the users across the big pond using your library. They both are just bytecode interpreted 
   by the JIT compiler and running on a JVM. 

   - If a class, method, or field is not public, I don't usually write Javadoc unless something important has 
   to be pointed out. I try to write self-documented code following the philosophy that simplicity matters.
 
## <a name="opl"><a/> Has json-values been developed using TDD?
No, it doesn't. I don't like TDD. I know, shame on me! I care about testing, but I usually write tests after 
coding for a while. It helps me be in the user's shoes and focus on aspects like how good is an interface, 
how easy it is to use the API etc. Doing TDD my brain is focused mainly on just passing the test. In the end, 
having quality tests and proper test coverage is what matters, and not the means.

[JUnit 5](https://junit.org/junit5/) and Property Based Testing with [ScalaCheck](https://www.scalacheck.org/) has
been used to test json-values. PBT is excellent, and if you think about it, computers are way better than humans at 
generating random data, and we should take advantage of that. The test coverage figure that appears in [Codecov](https://codecov.io/gh/imrafaelmerino/json-values), corresponds to 
the tests implemented using only JUnit. I haven't found a way of adding up both test coverages figures from JUnit and 
ScalaCheck.

    