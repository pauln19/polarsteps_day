# polarsteps

Spring Boot 4.1 / Java 25 REST API over PostgreSQL.

The functional specification is not in yet. What is here is the skeleton every future
endpoint will sit on, plus a throwaway `example` slice that proves it works end to end.

The source carries no comments by design — everything worth explaining is in this file.

## Running it

```bash
./mvnw spring-boot:run
```

`spring-boot-docker-compose` starts the services in [compose.yaml](compose.yaml) and derives
the datasource from them, so there is no `spring.datasource.*` in
[application.yaml](src/main/resources/application.yaml) and no separate `docker compose up`
step. `lifecycle-management: start-only` means containers survive a Ctrl-C, so local data
persists between runs.

| What | Where |
| --- | --- |
| API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health | http://localhost:8080/actuator/health |
| Grafana | http://localhost:3000 (opt-in, see below) |

To run against a clean throwaway database instead, start
[`TestPolarstepsApplication`](src/test/java/org/example/polarsteps/TestPolarstepsApplication.java)
from the IDE — same app, Testcontainers Postgres.

The Postgres image tag is pinned in **both** `compose.yaml` and
[`TestcontainersConfiguration`](src/test/java/org/example/polarsteps/support/TestcontainersConfiguration.java);
keep them equal so tests and local development never run different database versions.

## Adding a feature

One package per domain concept, holding its own controller, service, repository, entity and
DTO records:

```
org.example.polarsteps.<feature>/
├─ <Feature>Controller.java   <Feature>Service.java
├─ <Feature>.java (@Entity)   <Feature>Repository.java
└─ <Feature>Dtos.java         (request/response records)
```

`common/` is only for things two or more features share — it is not a home for a controller
that has nowhere else to go.

`example/` is a **template, not part of the assignment**. Copy its shape, then delete the
package together with `V1__init.sql`. That deletion is verified to leave a build that still
passes, so nothing in the skeleton depends on it.

### Conventions

- **Migrations** are `V<n>__<snake_case_description>.sql` in
  `src/main/resources/db/migration`. Flyway owns the schema and `ddl-auto: validate` means
  Hibernate refuses to start when an entity and its table disagree. That check is worth
  keeping strict: it is how a `char(64)` column mapped as `varchar(64)` gets caught at
  startup instead of in production.
- **Errors** are RFC 9457 `application/problem+json`, produced centrally by
  [`GlobalExceptionHandler`](src/main/java/org/example/polarsteps/common/error/GlobalExceptionHandler.java).
  Throw `ResourceNotFoundException` / `ConflictException` rather than building error
  responses in a controller.
- **Paging** returns
  [`PageResponse`](src/main/java/org/example/polarsteps/common/web/PageResponse.java), not
  Spring Data's `Page`, whose JSON shape is internal and unstable. Page size is capped at
  100 — an uncapped `?size=` is a denial-of-service vector.
- **Ids** are UUIDv7 via `@UuidGenerator(style = VERSION_7)`. A v7 value leads with a
  millisecond timestamp, so inserts land at the right-hand edge of the primary-key index
  instead of scattering across it the way random v4 does. Watch the enum name: `Style.TIME`
  is version **1**, whose layout puts the low time bits first and does *not* sort
  chronologically. Use `BIGSERIAL` instead if the spec calls for sequential public ids.
- **DTOs** are hand-written records with a static `from(entity)` factory. Explicit and
  reviewable, and there is not enough of it to justify a code generator.

### Error contract details

[`GlobalExceptionHandler`](src/main/java/org/example/polarsteps/common/error/GlobalExceptionHandler.java)
extends `ResponseEntityExceptionHandler` deliberately: that is what makes Spring's *own*
failures — malformed JSON, unsupported media type, an unparseable path variable — render as
ProblemDetail rather than falling through to Boot's default error body.

| Thrown | Status | Notes |
| --- | --- | --- |
| `MethodArgumentNotValidException` | 400 | adds a field-level `errors` array |
| `ConstraintViolationException` | 400 | `@Validated` path and query parameters |
| `ResourceNotFoundException` | 404 | |
| `ConflictException` | 409 | |
| `DataIntegrityViolationException` | 409 | driver message is logged, never returned |
| anything else | 500 | logged at ERROR, response carries the `traceId` |

The `DataIntegrityViolationException` case matters: the driver's message names tables,
columns and values, so it stays in the log and the client gets a generic detail. The 500
case carries the trace id so a user-reported failure can be looked up directly in the
tracing backend.

### Resilience

`@Retryable` and `@ConcurrencyLimit` are Spring Framework 7 features from `spring-context`,
switched on by
[`ResilienceConfig`](src/main/java/org/example/polarsteps/common/config/ResilienceConfig.java).
No external resilience library is involved.

`ExampleItemService` shows the intended usage. The retry is scoped to
`TransientDataAccessException` — a dropped connection or a deadlock victim, where the same
call may well succeed next attempt. Retrying a deterministic business failure would just
multiply load and delay an error the caller already earned. The `jitter` is not decoration:
without it a burst of failures retries in lockstep and hammers a recovering database.

`@ConcurrencyLimit` on the write path is backpressure — see *Load behaviour* below.

### Outbound HTTP

Nothing calls anything external yet, but `spring.http.client.connect-timeout` and
`read-timeout` are already set, so the first `RestClient` built from the auto-configured
builder inherits them. A client with no read timeout parks a request thread indefinitely
against a slow peer, which is how one degraded dependency takes a whole service down.

A future downstream call belongs in a `common/client/` package as an `@HttpExchange`
interface, and that is also where a circuit breaker would go.

## Observability

The Grafana LGTM stack is behind a compose profile because the image is roughly 1 GB.
Enable it by uncommenting `spring.docker.compose.profiles.active: observability` in
`application.yaml`, then hit an endpoint and find the trace in Grafana at `localhost:3000`.

Three settings are load-bearing, and none of them announce their own failure:

- `management.opentelemetry.tracing.export.otlp.endpoint` — the **metrics** registry
  defaults to `localhost:4318/v1/metrics` and the compose service connection fills it in
  automatically, but the **tracing** exporter has no default and is not covered by that
  connection. Without the explicit endpoint the app starts cleanly and exports no traces at
  all.
- `management.observations.annotations.enabled` — defaults to `false`, which means
  `@Observed` is not merely inert: the aspect implementing it is never registered.
- `management.tracing.sampling.probability: 1.0` — fine locally, lower it in production.

If the compose project is already running when you switch the profile on, the LGTM container
is not picked up (`start-only` means Boot does not re-run `up`). Run
`docker compose --profile observability up -d` once, then restart the app.

Actuator exposes only `health` and `info`; `health` uses `show-details: when-authorized`, so
with no security on the classpath it reports status without component detail. Switch to
`always` if you want to see the database component locally.

## Testing

```bash
./mvnw verify
```

Two kinds, deliberately:

- **Slice tests** (`@WebMvcTest`) — web layer only, no database, no container. Fast, and
  where the HTTP contract is pinned down. See
  [`ExampleItemControllerTest`](src/test/java/org/example/polarsteps/example/ExampleItemControllerTest.java).
- **Integration tests** — extend
  [`IntegrationTest`](src/test/java/org/example/polarsteps/support/IntegrationTest.java): full
  context, real Postgres via Testcontainers, real filter chain. It uses a MOCK web
  environment rather than a random port, so the whole MVC and persistence stack still runs
  but no socket is opened, and exposes `MockMvcTester` and an `ObjectMapper` to subclasses.

Three things to know before changing the base class:

- **Keep the annotations on the base, not the subclasses.** Identical context configuration
  is what lets the test context cache reuse a single container for the entire suite. Adding
  `@TestPropertySource` or an extra `@Import` to one test forks a second context, and a
  second container. The suite is verified to start Postgres exactly once.
- **Cleanup is `TRUNCATE`, not `@Transactional` rollback**
  ([`DatabaseCleaner`](src/test/java/org/example/polarsteps/support/DatabaseCleaner.java)).
  Rollback is faster, but it keeps the test and the handler inside one transaction that never
  commits, hiding exactly the bugs an integration test is for: missing flushes, and
  constraints that only fire at commit. Tables are discovered from `information_schema`, so a
  new migration needs no change there.
- **Test config is `application-test.yaml`, activated by `@ActiveProfiles("test")`.** It is
  deliberately *not* named `application.yaml`: a file by that name in `src/test/resources`
  **replaces** the main one on the test classpath instead of merging with it, silently
  dropping every setting under test. It disables OTLP export so the suite never stalls
  retrying a collector that is not there, while leaving observation and tracing wired so the
  instrumentation itself stays exercised.

Note that `target/test-classes` keeps stale resources across builds — if config changes seem
not to apply in tests, run `./mvnw clean`.

## Load behaviour

Virtual threads are on (`spring.threads.virtual.enabled`). The consequence worth
remembering: Tomcat's thread pool is no longer the concurrency limit, so the **Hikari pool
is**. An unbounded burst of virtual threads queues on ten connections and times out
together. That is why `connection-timeout` is set explicitly, why there is a JPA query
timeout, and why write paths carry `@ConcurrencyLimit` to shed load early rather than late.

`server.shutdown: graceful` with `spring.lifecycle.timeout-per-shutdown-phase` drains
in-flight requests instead of dropping them on SIGTERM.

## Deliberately not here yet

Decisions taken knowingly, so they can be discussed rather than improvised.

**Idempotency.** Mutating endpoints should accept an `Idempotency-Key` header: a client that
times out on a `POST` cannot know whether the write landed, and repeating the call with the
same key should return the original response instead of creating a second resource. The
design, should the spec call for it:

| Situation | Response |
| --- | --- |
| First call | the handler's own response |
| Same key, same body, original finished | replayed response + `Idempotency-Replayed: true` |
| Same key, same body, original still running | `409` + `Retry-After` |
| Same key, **different** body | `422` — a client bug worth failing loudly on |
| Original call failed | key released, so the retry runs normally |

A `OncePerRequestFilter` claims the key in its own transaction *before* the handler runs,
using `INSERT ... ON CONFLICT DO NOTHING` against a table keyed on the idempotency key. That
detail is the whole point: a SELECT-then-INSERT leaves a window in which two concurrent
duplicates both pass the check, so the primary key — not application code — has to decide the
race. Only 2xx responses are stored, so a transient 500 stays retryable, and expiring old
keys is a scheduled-job concern rather than request-path work.

**Rate limiting.** Nothing in Spring provides it. The design: Bucket4j behind a
`OncePerRequestFilter` keyed by API key or IP, returning `429` with `Retry-After` and
`RateLimit-*` headers. In-memory buckets are per-instance, so anything multi-instance needs
Redis-backed buckets or an edge/gateway limiter — usually the better answer regardless.

**Circuit breaker.** There is no downstream dependency to protect. It would arrive as
`resilience4j-spring-boot4` with an explicitly pinned version, because that artifact is still
missing from the Resilience4j BOM
([#2427](https://github.com/resilience4j/resilience4j/issues/2427)). A breaker around our own
database would be an anti-pattern; the Hikari connection and query timeouts cover that case.

**API versioning.** Spring Framework 7 supports `@GetMapping(version = "1.1")` with header,
path or media-type strategies, and RFC 9745 deprecation headers. Not switched on because
there is no second version to serve; it is a one-line `WebMvcConfigurer` change when there
is.

**Authentication.** No Spring Security until the spec says what identity looks like. Adding
it blind would lock down every endpoint and distort the error contract.
