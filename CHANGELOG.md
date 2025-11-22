# Changelog

All notable changes to **Bitronix Transaction Manager (BTM)** will be documented
in this file. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.0.0] - 2025-XX-XX

### Summary

After more than a decade of inactivity, **Bitronix Transaction Manager** has
been revived and modernized. This release focuses heavily on simplifying the
codebase, removing obsolete bytecode manipulation layers, adopting Java 17+,
improving test stability, and bringing the build system and documentation into
the modern era. It also switched from JTA 1.1 to Jakarta JTA 2.0 which is a
**breaking change**.

### Jakarta Migration

Bitronix 4.0 upgrades the project from **JTA 1.1 (`javax.transaction`)** to
**Jakarta JTA 2.0.1 (`jakarta.transaction`)**. This is a **major breaking
change** that requires all consuming applications to migrate from the `javax.*`
namespace to the `jakarta.*` namespace.

- Most JTA classes moved to `jakarta.transaction.*` except for
  `javax.transaction.xa.*` which remains part of the JDK.
- The API surface remains the same; the breaking change is the mandatory
  **package rename** introduced by Jakarta EE.
- Applications still using `javax.transaction` **cannot** use bitronix 4.0
  without updating imports and dependencies.
  
#### Required Changes

1. Update Imports from `javax.transaction.*` to `jakarta.transaction.*` except
   for `javax.transaction.xa.*` which remains part of the JDK.

```patch
-import javax.transaction.UserTransaction;
-import javax.transaction.TransactionManager;
+import jakarta.transaction.UserTransaction;
+import jakarta.transaction.TransactionManager;
```

2. Update Dependencies. For example:

```patch
-<dependency>
-    <groupId>javax.transaction</groupId>
-    <artifactId>jta</artifactId>
-    <version>1.1</version>
-</dependency>
+<dependency>
+    <groupId>jakarta.transaction</groupId>
+    <artifactId>jakarta.transaction-api</artifactId>
+    <version>2.0.1</version>
+</dependency>
```

### Changed

- **Set Java 17 as the minimum required JVM version.**
- Modernized reflection usage:
  - Replaced `Class.newInstance()` with
    `getDeclaredConstructor().newInstance()`.
  - Adopted auto-boxing (`Integer.valueOf()`, etc.) instead of obsolete
    constructors.
- Migrated build tooling to modern versions:
  - Maven Wrapper → **3.3.4** (Maven 3.9.11)
  - `maven-compiler-plugin` → **3.11.0**
  - `maven-surefire-plugin` → **3.5.4**
  - `maven-javadoc-plugin` → **3.12.0**
  - `maven-bundle-plugin` → **6.0.0**
  - `asciidoctor-maven-plugin` → **3.2.0**
- Updated project metadata to reflect the new GitHub organization.
- Improved `JdbcPoolTest` by replacing fragile string parsing with proper JDBC
  API usage.
- Disabled Javadoc doclint for compatibility with older comments.
- Replaced Mockito deprecated APIs with modern `ArgumentMatchers` equivalents.

### Removed

- **Removed Javassist and all related proxy implementation code.**
- **Removed CGLIB and all related proxy implementation code.**
- **Removed CORBA rmi obsolete implementation**
- **Removed OSGI obsolete implementation**
- The `"auto"` proxy-factory detection logic was simplified; Java Proxy is now
  the only mechanism.
- **Removed the entire `btm-spring` module**, because:
  - It used ancient Spring 2.x XML-based APIs incompatible with modern Spring.
  - It depended on CGLIB and fails on Java 17+.
  - Modern Spring usage requires only a few lines of configuration without this
    module.
- **Removed all obsolete lifecycle modules for jetty and tomcat**
- **Removed the deprecated maven-release-plugin.**
- **Removed GitHub CI workflow** that targeted outdated Java toolchains and
  Maven versions.
- **Removed JDBC overlay JAR files** (deprecated and unused).
- Cleaned up `.gitattributes` and `.gitignore`.

### Upgraded

- Mockito from **1.8.5 → 5.20.0** (`mockito-all` replaced with `mockito-core`).
- JUnit from **4.8.2 → 4.13.2**.
- Numerous dependencies and plugins updated to modern, maintained versions.

### Notes

- This release intentionally removes legacy subsystems that were unmaintained
  for 10+ years.
- Users relying on proxy customization (CGLIB/Javassist) should migrate to Java
  Proxy or contribute alternative extension points.
- The legacy `btm-dist` module has been **removed**.  
  It previously contained Ant-based distribution scripts and dozens of
  hand-maintained release-notes text files.  
  These notes have now been consolidated into this unified `CHANGELOG.md`.
- No functionality has been lost—the module only provided packaging artifacts
  for tools and processes that are obsolete today (Ant, manual distribution
  zips, bundled licenses, etc.).
- All historical release notes from 2006–2014 were preserved and merged here to
  maintain project history in a modern, accessible format.

### Fixed

- Many tests updated or refactored to be robust on modern Java.
- Stability improvements from modernizing reflection, reducing warnings, and
  removing legacy bytecode tools.

## [2.1.4] — 2013-09-15

### Fixed

- PreparedStatement equality/wrapping issues.
- Triple-DES (DES-EDE) password encryption not working.

## [2.1.3] — 2012-05-21

### Added

- Major concurrency improvements across the TM (significantly higher performance in multi-threaded environments).

### Fixed

- Improved Oracle XA error reporting.
- Debugging support for executing transactions with 0 enlisted resources.
- Exception handling improvements during `beforeCompletion`.

## [2.1.2] — 2011-10-30

### Fixed

- Hibernate 4.x lookup causing `OperationNotSupportedException`.
- Connection customization issues.
- EHCache XA cleanup after unregister.

## [2.1.1] — 2011-04-02

### Added

- Connection pool monitoring & management (BTM-73).
- Ability to eject connections on rollback failure.
- Friendlier behavior for non-production environments.
- Jetty 7 lifecycle support.

### Fixed

- JMS pool NPE when creating sessions outside transaction.
- Recovery re-initialization failures after incremental recovery issues.
- Race condition in disk force batching causing deadlocks.
- JMX registration failures with multiple serverIds.
- Infinite loop in `SchedulerNaturalOrderIterator`.

## [2.1.0] — 2010-10-31

### Changed

- Dropped support for JDK 1.4 → Requires JDK **1.5+**.
- Build migrated to **Maven 2**.

### Fixed

- `LrcXAResource` unusable after rollback.
- Resource pool long-running instability.
- Detect dead JMS connections using `ExceptionListener`.

## [2.0.1] — 2010-07-31

### Fixed

- Cannot recover lost JMS connection.
- NPE in `TransactionSynchronizationRegistry`.
- JMS pool deadlock.

## [2.0.0] — 2010-07-02

### Added

- SLF4J upgraded to 1.6.0.

### Changed
    
- JTA 1.1 jar from Sun adopted.
- Many concurrency and shutdown improvements.

### Fixed

- EHCache support stability.
- DiskForceWaitQueue hang.
- Exception handling in `beforeCompletion`.
- JDBC 4 API compatibility.
- LRC prepare behavior.
- PreparedStatement eviction issues.
- Transaction affinity.
- Many additional performance optimizations.

## [1.3.3] — 2009-10-25

### Added

- Background recoverer enabled by default.
- Current-node-only recovery enabled by default.
- MDC propagation of GTRID.

### Changed

- Safer recovery algorithm.
- Failed resources marked instead of blocking startup.
- SLF4J upgraded to 1.5.8.

### Fixed

- Numerous race conditions and XA issues.
- Tomcat lifecycle issues.
- JNDI errors and scheduler startup behaviors.
- PreparedStatement behavior and transaction log corruption issues.

## [1.3.2] — 2008-10-17

### Fixed

- Synchronization registration inconsistencies.

## [1.3.1] — 2008-09-28

### Added

- Set required isolation level for resources.

### Fixed

- Clustering recovery problems.
- Shutdown support for app servers.
- JMX disabled when uniqueName contains `:`.
- Race conditions mixing global/local TX.
- Random transaction log corruption on JDK 1.6.
- XADataSource type-mapping issues.
- Enforce LRC mandatory parameters.
- Removed deprecated `poolsize`.

## [1.3] — 2008-08-18

### Added

- Incremental recovery (**journal format changed**, backward compatibility maintained for ≤1.2).
- Embedded JNDI provider.
- New `journal` options: disk vs null.

### Changed

- Statement cache overhead reduced.
- All deprecated APIs removed.
- Manual shutdown required (shutdown hook removed).
- Ordering of XAResources in 2PC modified.

### Fixed

- Many concurrency issues, deadlocks, and XA handling edge-cases.

## [1.2] — 2008-04-03

### Added

- JMS credentials support.
- PreparedStatement caching.
- SLF4J upgrade.

### Fixed

- Recovery errors not reported.

## [1.1] — 2007-10-05

### Added

- Dynamic pool sizing.
- Keep-connection-open-after-2PC (DB2).

### Fixed

- Oracle 9 delisting bug.
- Password encryption.

## [1.0] — 2007-08-04

### Added

- Moved project to Codehaus.
- Pool and GUI improvements.
- Support for restarting TM after shutdown.

## [0.x Beta & Alpha Series] — 2006–2007

### Beta4 → Beta1

- Major refactors of configuration & enlistment.
- Last-Resource-Commit wrapper improvements.
- Statement sharing across JDBC/JMS.
- Shutdown improvements.
- Race-condition fixes.
- JNDI credentials support.
- Log integrity & `skipCorruptedLogs`.
- Transaction log file locking.
- Allow/disallow mixing XA & non-XA.

### Alpha7 → Alpha3

- Fixed major JDBC/JMS pool correctness issues.
- Removed Commons Logging → SLF4J.
- Improved joining logic & timeout detection.
- Shortened GTRID/BQUAL.
- Implemented suspend/resume without XA.
- Added JMS pooling.
- Many recovery and multi-XA fixes.
