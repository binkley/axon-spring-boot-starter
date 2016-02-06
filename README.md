# Spring Boot Starter for Axon Framework

This software is in the Public Domain.  Please see [LICENSE.md](LICENSE.md).

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
