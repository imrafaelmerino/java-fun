[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=alert_status)](https://sonarcloud.io/dashboard?id=imrafaelmerino_java-fun)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=imrafaelmerino_java-fun&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=imrafaelmerino_java-fun)
[![Maven](https://img.shields.io/maven-central/v/com.github.imrafaelmerino/java-fun/0.9.1)](https://search.maven.org/artifact/com.github.imrafaelmerino/java-fun/0.9.1/jar)

- [Goal](#goal)
- [PRG: Pseudo Random Generators](#prg)
    - [Primitive Types Generators](#ptg)
    - [Collection Generators](#cg)
    - [Tuples and Record Generators](#trg)
    - [Objects Generators](#og)
    - [Combinators](#com)
- [Optics](#optics)
    - [Lenses](#lenses) 
    - [Optionals](#optionals)
    - [Prism](#prism)
- [Requirements](#req)
- [Installation](#inst)
- [Related projects](#rp)

## <a name="goal"><a/> Goal

The goal of java-fun is to implement some important FP patterns in Java. It
doesn't transliterate these patterns from other languages, aiming that any standard
Java programmer finds easy to adopt them and understand the essence of these concepts
,not getting lost in unfamiliar types and conventions.

The most important concepts implemented so far are:

- **Pseudo Random Generators**. The best way to do testing is by far Property-Based-Testing.
And to do it well you need a good set of generators and to be able to compose them in any imaginable
way. With java-fun, it's child's play!
- **Optics**. Functional programmers use optics instead of getters and setters. They are more safe
  and composable.

## <a name="prg"><a/> Pseudo Random Generators

### <a name="ptg"><a/> Primitive Types Generators

### <a name="prg"><a/> Pseudo Random Generators

### <a name="cg"><a/> Collection Generators

### <a name="trg"><a/> Tuples and Record Generators

### <a name="og"><a/> Objects Generators

### <a name="com"><a/> Combinators

## <a name="optics"><a/> Optics

### <a name="lenses"><a/> Lenses

### <a name="optionals"><a/> Optionals

### <a name="prism"><a/> Prism

## <a name="req"><a/> Requirements

Requires Java 8 or greater


## <a name="inst"><a/> Installation

```xml

<dependency>
    <groupId>com.github.imrafaelmerino</groupId>
    <artifactId>java-fun</artifactId>
    <version>0.9.1</version>
    <classifier>jdk8</classifier>
</dependency>

```

## <a name="rp"><a/> Related projects

[json-values](https://github.com/imrafaelmerino/json-values) has defined a JSON generator
and some optics to manipulate JSON using this library.

