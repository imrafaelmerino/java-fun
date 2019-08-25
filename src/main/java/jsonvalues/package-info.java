/**
 json-values is a one-package and zero-dependency library to work with jsons in a declarative and functional way.
 From now on, an immutable object is a value. There are different static factory methods to create objects:
 <ul>
 <li><code>of</code> methods to create values.</li>
 <li><code>parse</code> methods to parse strings into immutable values.</li>
 <li><code>_of_</code> methods to create mutable objects.</li>
 <li><code>_parse_</code> methods to parse strings into mutable objects.</li>
 </ul>
 Four exceptions are thrown by the library:
 <ul>
 <li>the custom unchecked {@link jsonvalues.UserError}, when the client makes a programming error. A suggestion in the message
 to avoid the error is returned.</li>
 <li>the custom unchecked {@link jsonvalues.InternalError}, when something unexpected happens because a developer made a mistake.
 An issue in GitHub must be created</li>
 <li>the checked {@link jsonvalues.MalformedJson}, when a string can not be parsed into a json.</li>
 <li>the unchecked NullPointerException, when a method different than equals receives a null parameter.</li>
 </ul>
 All the methods which name ends with an underscore are applied to the whole json recursively, and not only
 to its first level. For example:
 <pre>
 {@code
 x={"a":1, "b":[{"c":1, "d":true}]}
 x.size() = 2  // a and b
 x.size_() = 3 // a, b.0.c and b.0.1
 x.mapKeys(toUppercase)  =  {"A":1, "B":[{"c":1, "d":true}]}
 x.mapKeys_(toUppercase) =  {"A":1, "B":[{"C":1, "D":true}]}
 }
 </pre>
 */
package jsonvalues;