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
  Include the jar in your classpath and add the annotation to a
  configuration class.
* Autoconfigure `CommandBus` and `EventBus` with default
  implementations.  In particular, your configuration must define a bean
  for `EventStore`: there are too many strategies for event stores for
  autoconfiguration to pick a default one for you.
* Autoconfigure aggregate root repositories marked with
  `@MetaInfServices`.  The matching repository bean is the aggregate
  bean name appended with "Repository" ("fooRoot" becomes
  "fooRootRepository").  These generate entries in
  `/META-INF/services/org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot`.
  Note you *must* annotate injected repository fields with
  `@Qualified(name-of-repostiroy)` because of limitations in Spring's
  support for generics.
* Autoconfiguration for event bus clusters.
* Autoconfiguration for JGroups distributed command bus.
* Autoconfiguration for event processing monitors.
* Autoconfiguration for Spring Messaging event cluster.
* Autoconfiguration for command dispatch and handler interceptors.
* Autoconfiguration for audit and monitoring.

## Minimal Example

### Read-side configuration

Include `axon-spring-boot-starter-query` in your dependencies.

```
@Configuration
@EnableAutoConfiguration
public class AConfiguration {}
```

### Write-side configuration

Include `axon-spring-boot-starter` in your dependencies.

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

Include `axon-spring-boot-starter` in your dependencies.

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

### JGroups command bus

Include `axon-spring-boot-starter-distributed-commandbus` in your
dependencies.

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

In your `application.yaml`:

```
axon:
  jgroups:
    cluster-name: TEST
```

And include a suitable `jgroups-config.xml` in your classpath.

### Command dispatch interceptors

```
@Order(1)
public class ADispatchInterceptor implements CommandDispatchInterceptor {
    // ...
}
```

Exposed as a bean, such interceptors are invoked in order.

### Command handler interceptors

```
@Order(1)
public class AHandlerInterceptor implements CommandHandlerInterceptor {
    // ...
}
```

Exposed as a bean, such interceptors are invoked in order.

### Event processing monitor

Include `axon-spring-boot-starter-clustering-eventbus` in your dependencies.

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

### Spring messaging

Include `axon-spring-boot-start-springmessaging` in your dependencies.

```
@Configuration
@EnableAutoConfiguration
public class AConfiguration {
    @Bean
    public SubscribableChannel subscribableChannel() {
        return ...;
    }
}
```

A typical implementation uses JMS (`spring-jms` dependency).

### Spring Boot auditing

```
@Configuration
@EnableAutoConfiguration
public class AConfiguration {
    @Bean
    public AuditEventRepository auditEventRepository() {
        return ...;
    }
}
```

Include `axon-spring-boot-start-monitoring` in your dependencies.  Both
commands and events are audited through Spring Boot.

## Releases

### 5

* Autoconfiguration of command dispatch and handler interceptors
* Flow of metadata from initial command through consequences

### 4

* Event processing monitor automation.
* Spring Messaging support.

### 3

* Read-side (query) uses autoconfiguration as well as write-side.
* Clustering event bus starter.

### 2

* Renamed project and modules to meet Spring Boot 3rd-party naming
  guidelines.  So spring-boot-axon-starter becomes
  axon-spring-boot-starter.  Apologies committers and cloners.  Better
  now than later.

### 1

* Break project into multi-module reactor build.  Separate axon starters
  in separate modules.
* Support JGroups distriuted command bus.

### 0

* Basic functionality, single project, simple defaults for Axon
  components
