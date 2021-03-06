# jsonrpc-bind-jackson

[![Java CI with Maven](https://github.com/jinahya/jsonrpc-bind-jackson/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/jsonrpc-bind-jackson/actions)
[![Build Status](https://travis-ci.org/jinahya/jsonrpc-bind-jackson.svg?branch=develop)](https://travis-ci.org/jinahya/jsonrpc-bind-jackson)
[![CircleCI](https://circleci.com/gh/jinahya/jsonrpc-bind-jackson/tree/develop.svg?style=svg)](https://circleci.com/gh/jinahya/jsonrpc-bind-jackson/tree/develop)

[![Known Vulnerabilities](https://snyk.io//test/github/jinahya/jsonrpc-bind-jackson/badge.svg?targetFile=pom.xml)](https://snyk.io//test/github/jinahya/jsonrpc-bind-jackson?targetFile=pom.xml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.jinahya%3Ajsonrpc-bind-jackson%3Adevelop&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.jinahya%3Ajsonrpc-bind-jackson%3Adevelop)

![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/jsonrpc-bind-jackson.svg)
[![Javadocs](https://javadoc.io/badge/com.github.jinahya/jsonrpc-bind-jackson.svg?label=javadoc)](https://javadoc.io/doc/com.github.jinahya/jsonrpc-bind-jackson)

An implementation of [jsonrpc-bind](https://github.com/jinahya/jsonrpc-bind) for [Jackson](https://github.com/FasterXML/jackson).

## Configuration

### `JacksonJsonrpcConfiguration`

This class holds an instance of `ObjectMapper`. You can use your own one if you want/need to.

```java
// Doing once is enough.
final ObjectMapper objectMapper = getYourOwn();
JacksonJsonrpcConfiguration.setObjectMapper(objectMapper);
```
