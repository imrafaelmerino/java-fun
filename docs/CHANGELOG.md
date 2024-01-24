1.4.0
New feature:

- Named generators: NamedGen
  Refactor:
- JsObjGen (improvement nullable and optional fields generation)
  Doc:
- New section in readme about recursive generators

2.0.0

- This version requires java 17
- Refactor `Gen` interface: `RandomGenerator` interface instead of Random implementation
- Refactor `SplitGen` implementation: uses the default implementation `RandomGenerator.getDefault()`  instead
  of `Random`
- New Quadruple, Quintuple and SexTuple classes
- New QuadrupleGen, QuintupleGen and SexTupleGen classes

2.1.0

Breaking:

- `getXXX` methods in `Record` class may return null and doesn't return `Optional`.
  Migrate those to `getOptXXX` methods.

New:

- `CSVStreamBuilder` class to read csv files into Stream of `Record`
- upgrade error prone library
- New `getOptXXX` methods in `Record` class (`getOptStr`, `getOptInt` ...)

