# Spring Boot Starter for Axon Framework

This software is in the Public Domain.  Please see [LICENSE.md](LICENSE.md).

[![License](https://img.shields.io/badge/license-PD-blue.svg)](http://unlicense.org)
[![Build Status](https://img.shields.io/travis/binkley/spring-boot-starter-axon.svg)](https://travis-ci.org/binkley/spring-boot-starter-axon)
[![Issues](https://img.shields.io/github/issues/binkley/spring-boot-starter-axon.svg)](https://github.com/binkley/spring-boot-starter-axon/issues)
[![maven-central](https://img.shields.io/maven-central/v/hm.binkley/spring-boot-starter-axon.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22hm.binkley%22%20AND%20a%3A%22spring-boot-starter-axon%22)

## Acknowledgment

This project is heavily indebted to:

* [Lieven Doclo](https://github.com/lievendoclo/axon-spring-boot)
* [Christopher Elkins](https://github.com/esha/spring-boot-starter-axon)
* [Tom Soete](https://github.com/tomsoete/spring-boot-starter-axon)

## Features

* Autoconfiguration driven with standard `@EnableAutoConfiguration`.
* Autoconfigure `CommandBus`, `EventBus` and `EventStore` with default
  implementations.  In particular, replace the event store: the default
  is in-memory.
* Autoconfigure aggregate root repositories marked with `@MetaInfServices`.
