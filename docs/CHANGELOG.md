# JSON-VALUES
## v3.1.0  ( Sun Sep 08 2019 23:32:22 GMT+0200 (CEST) )


## Breaking changes

  - **new Json factories**
    - Migrate static factory methods: 
      - Json.parse to Jsons.immutable.parse
      - JsObj.of to  Jsons.immutable.object.of
      - JsArray.of to  Jsons.immutable.array.of
      - JsObj.\_of\_ to Jsons.mutable.object.of
      - JsArray.\_of\_ to Jsons.mutable.array.of
      - ...
  - **JsObj and JsArray are not Serializable in this release.**    
  - **collector from immutable Jsons is removed**
  - **equals method returns false if the implementations are different, even if the json is the same:**

## Features

  - **JsArray and JsObj implemented using any custom data structure**
    - Using the methods _withMap_ and _withSeq_ in factories _MutableJsons_ and _ImmutableJsons_
  - **json.same(json) method to check if two Jsons are the same, no matter the implementation**
