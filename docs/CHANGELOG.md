# JSON-VALUES
## v1.0.0  ( Mon Aug 26 2019 02:01:59 GMT+0200 (CEST) )


## Breaking changes

  - **Json Pointer specification: rfc 6902**
    - due to [890921d6](https://github.com/imrafaelmerino/json-values.git/commit/890921d626fa17a81bb061f8c874f1406320393d),
  the string representation of a JsPath is different.

fix #26

  ([890921d6](https://github.com/imrafaelmerino/json-values.git/commit/890921d626fa17a81bb061f8c874f1406320393d))

  - **Deleted methods**
    - due to [fa6bfd3d](https://github.com/imrafaelmerino/json-values.git/commit/fa6bfd3d504231b3677a156dc62a82d1dde8dbf1),
  stop calling methods that takes path-like strings. Use JsPath objects as arguments instead.

close #28

  ([fa6bfd3d](https://github.com/imrafaelmerino/json-values.git/commit/fa6bfd3d504231b3677a156dc62a82d1dde8dbf1))

  - **ParseOptions**
    - due to [2604deb2](https://github.com/imrafaelmerino/json-values.git/commit/2604deb24f01ce9a46a8e97ee44e3650ab09adf2),
  rename ParseOptions class to ParseBuilder

  ([2604deb2](https://github.com/imrafaelmerino/json-values.git/commit/2604deb24f01ce9a46a8e97ee44e3650ab09adf2))




## Bug Fixes

  - **JsPath**
    - allows creation of indexes with leading zeroes
  ([5c5e4b4e](https://github.com/imrafaelmerino/json-values.git/commit/5c5e4b4e6aa33d06d9ea43b04694f3a20818751a))

  - **Tests**
    - tests which name don't start with 'test' are not executed
  ([048e3490](https://github.com/imrafaelmerino/json-values.git/commit/048e34900d465acc678e476ae47fda443670f68c))

  - **remove by index**
    - the wrong index was removed from immutable arrays
  ([db76f3a6](https://github.com/imrafaelmerino/json-values.git/commit/db76f3a61c31ddf824c98a95f6e083dfcc88c94b))


## Features

  - **add methods**
    - implementation of different overloaded add(JsPath, ...) methods
  ([995197ef](https://github.com/imrafaelmerino/json-values.git/commit/995197ef46266a5a282b055e6104936500176454))

  - **rfc 6902**
    - jsonPatch implementation
  ([1588fd47](https://github.com/imrafaelmerino/json-values.git/commit/1588fd47c43e1a373261fa55a486a500cee03157))
