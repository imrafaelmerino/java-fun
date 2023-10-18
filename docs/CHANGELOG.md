12.3.0
New method JsSpec.withReqKeys
New methods JsObjGen.withReqKeys and JsObjGen.withNonNullValues
Static factory methods to create specs and generators: up to 50 key-spec and key-gen pairs

12.3.1
Bug: Some static factory methods were missing in JsSpec

12.3.2
Improved javadoc
Improved implementation of JsObjGen

12.4.0
JsObjGen with optional and nullable fields, generates the whole json and with no null vales 50% of the times
refactor some tests
Improved javadoc

12.5.0
upgrade java-fun library
better optionals and nullable distribution in JsObjGen
New methods:
JsIntGen.biased(min)
JsLongGen.biased(min)
JsLongGen.arbitrary(min)
JsIntGen.arbitrary(min)

12.6.0
upgrade java-fun to 1.3.2

12.7.0
Bug: Previous versions compiled without enabling preview features. All preview features have been eliminated as they are
no longer necessary and were exclusively used for internal purposes.

Eliminate compilation warnings in both source and test code."

12.8.0
JsObjGen::concat method

12.9.0
JsObj new methods: 
    set(key,primitive)
    set(path,primitive)