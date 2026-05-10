# MOA Backend Architecture

## Current Layering

The backend keeps a conventional Spring Boot layered structure:

```text
com.moa
  web/          REST controllers and request boundary
  service/      business use cases and transactions
  dao/          MyBatis mapper interfaces
  domain/       database-facing domain objects
  dto/          request/response contracts
  auth/         security filters, handlers, JWT provider
  config/       Spring configuration and external integration properties
  common/       cross-cutting utilities, events, exceptions, filters
  scheduler/    scheduled jobs
```

## Migration Direction

New backend code should move toward feature ownership while preserving Spring component scanning:

```text
com.moa
  feature/
    party/
      web/
      service/
      persistence/
      dto/
      domain/
    payment/
    settlement/
    community/
    product/
  global/
    config/
    security/
    error/
    util/
```

For the current codebase, migrate one feature at a time. Do not move all packages at once because MyBatis mapper scanning, XML namespaces, and DTO imports must be changed together.

## Rules

- Controllers do not call DAOs directly.
- Services own transactions and business validation.
- DAOs remain persistence-only and should not contain business decisions.
- DTOs are API contracts and should not be reused as persistence entities when a domain object exists.
- Environment-specific values stay in profile files or secrets, not in source defaults.
- Frontend build artifacts should not be committed into backend resources when the frontend is deployed separately.
