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

* Autoconfiguration driven with [standard `@EnableAutoConfiguration` and
  `META-INF/spring.factories`](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-auto-configuration.html).
  Include the jar in your classpath and add the annotation to a configuration
  class.
* Autoconfigure `CommandBus` and `EventBus` with default
  implementations.  In particular, your configuration must define a bean for
  `EventStore`: there are too many strategies for event stores for
  autoconfiguration to pick a default one for you.
* Autoconfigure aggregate root repositories marked with `@MetaInfServices`.
  The matching repository bean is the aggregate bean name appended with
  "Repository" ("fooRoot" becomes "fooRootRepository").  These generate
  entries in
  `/META-INF/services/org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot`.
  Note you *must* annotate injected repository fields with
  `@Qualified(name-of-repostiroy)` because of limitations in Spring's support
  for generics.
* Autoconfiguration for event bus clusters.
* Autoconfiguration for JGroups distributed command bus.
* Autoconfiguration for event processing monitors.

## Minimal Example

### Read-side configuration

```
@Configuration
@EnableAutoConfiguration
public class AConfiguration {}
```

### Write-side configuration

```
@Configuration
@EnableAutoConfiguration
public class AConfiguration {
    @Bean
    public EventStore eventStore() {
        return ...;
    }
}
```

### Aggregate root and repository

```
@MetaInfServices
public class SomeAggregateRoot
        extends AbstractAnnotatedAggregateRoot<SomeIDType> {
    @AggregateIdentifier
    private SomeIDType id;
}
```

```
@Component
public final class SomeClassUsingRespository {
    @Autowired
    @Qualifier("someAggregateRootRepository")
    private Repository<SomeAggregateRoot> repository;
}
```

### Event processing monitor

_Note_: all monitors subscribe to all clusters in the Spring context.

```
@Component
public final class SomeEventProcessingMonitor
        implements EventProcessingMonitor {
    @Override
    public void onEventProcessingCompleted(
            final List<? extends EventMessage> eventMessages) {
    }

    @Override
    public void onEventProcessingFailed(
            final List<? extends EventMessage> eventMessages,
            final Throwable cause) {
    }
}
```

## Releases

### 4

* Event processing monitor automation.

### 3

* Read-side (query) uses autoconfiguration as well as write-side.
* Clustering event bus starter.

### 2

* Renamed project and modules to meet Spring Boot 3rd-party naming guidelines.
  So spring-boot-axon-starter becomes axon-spring-boot-starter.  Apologies
  committers and cloners.  Better now than later.

### 1

* Break project into multi-module reactor build.  Separate axon starters in
  separate modules.
* Support JGroups distriuted command bus.

### 0

* Basic functionality, single project, simple defaults for Axon components
