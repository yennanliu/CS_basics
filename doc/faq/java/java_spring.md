# Spring & Spring Boot FAQ

> **Scope** — the container (IoC/DI, bean lifecycle and scopes), AOP, `@Transactional` semantics, Spring MVC's request path, Spring Boot auto-configuration, and testing.
> **See also**: [`java_design_pattern.md`](./java_design_pattern.md) — the patterns Spring
> is built from; [`../backend/be_programming_notes.md`](../backend/be_programming_notes.md)
> — production backend patterns.

---

## 1) IoC and Dependency Injection ⭐⭐⭐⭐⭐

**Inversion of Control** — objects do not create their collaborators; a container creates
them and hands them over. **Dependency injection** is how that handover happens. The
benefit is not "less `new`": it is that a class depends on an *interface* it can be handed
a different implementation of — a stub in tests, a different adapter in another
environment.

```java
// java
@Service
public class OrderService {

    private final PaymentGateway gateway;      // an interface, not a concrete class
    private final OrderRepository repo;

    // Constructor injection — no @Autowired needed for a single constructor (4.3+)
    public OrderService(PaymentGateway gateway, OrderRepository repo) {
        this.gateway = gateway;
        this.repo = repo;
    }
}
```

| Injection style | Verdict |
|-----------------|---------|
| **Constructor** | ✅ The default. Fields can be `final`, dependencies are explicit, the object is never half-built, and it is trivially testable with `new` |
| Setter | For genuinely optional dependencies |
| Field (`@Autowired` on a field) | ❌ Hides dependencies, cannot be `final`, needs reflection to test. A constructor with eight parameters is *telling you* the class does too much — don't hide it |

**Resolution**: by type first; ambiguity is broken by `@Qualifier("name")`, `@Primary`, or
matching the parameter name to the bean name. A `Map<String, Handler>` or
`List<Handler>` parameter is injected with **every** implementation — the clean way to
build a strategy registry.

`@Component` (and its stereotypes `@Service`, `@Repository`, `@Controller`) mark classes
for component scanning; `@Bean` methods inside a `@Configuration` class register beans you
do not own or that need construction logic.

---

## 2) Bean Lifecycle and Scopes ⭐⭐⭐⭐

```text
definition parsed  →  instantiate  →  populate properties (DI)
   →  *Aware callbacks (BeanNameAware, ApplicationContextAware…)
   →  BeanPostProcessor.postProcessBeforeInitialization
   →  @PostConstruct  →  afterPropertiesSet()  →  custom init-method
   →  BeanPostProcessor.postProcessAfterInitialization   ← AOP proxies are created HERE
   →  READY
   →  @PreDestroy  →  destroy()  →  custom destroy-method
```

`BeanPostProcessor` is the extension point that matters: it is where Spring wraps your
bean in a proxy, which is why `@Transactional`, `@Async` and `@Cacheable` behave the way
§3 describes.

| Scope | One instance per | Notes |
|-------|------------------|-------|
| `singleton` (default) | Container | Created eagerly at start-up. **Must be stateless** — it is shared by every request thread |
| `prototype` | Injection point | Spring does not manage its destruction |
| `request` / `session` | HTTP request / session | Web only; injected as a proxy |
| `application` / `websocket` | `ServletContext` / socket | Rare |

The classic trap: injecting a **prototype** bean into a **singleton** gives you one
instance forever, because injection happens once. Use `ObjectProvider<T>`, a `@Lookup`
method, or a scoped proxy.

Singleton beans are not thread-safe by magic — a mutable field on a `@Service` is shared
state across concurrent requests.

---

## 3) AOP and Proxies ⭐⭐⭐⭐⭐

**Aspect-Oriented Programming** factors out cross-cutting concerns (transactions, caching,
security, logging, metrics) so business code does not repeat them.

| Term | Meaning |
|------|---------|
| Aspect | The module holding the concern |
| Join point | A point where advice can apply (in Spring: a method call) |
| Pointcut | The expression selecting join points |
| Advice | The code to run: `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`, `@Around` |
| Weaving | Attaching the aspect — Spring does it at **runtime**, with proxies |

```java
// java
@Aspect @Component
public class TimingAspect {

    @Around("@annotation(com.acme.Timed)")
    public Object time(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            metrics.record(pjp.getSignature().toShortString(), System.nanoTime() - start);
        }
    }
}
```

### The proxy rules that cause real bugs ⭐⭐⭐⭐⭐

Spring AOP wraps the bean: a **JDK dynamic proxy** if it implements an interface,
otherwise a **CGLIB subclass**. Advice runs only when the call arrives *through the
proxy*. Therefore:

- **Self-invocation is not advised.** `this.otherMethod()` inside the same class bypasses
  the proxy, so `@Transactional` / `@Cacheable` / `@Async` on `otherMethod` does nothing.
  Fix by moving the method to another bean (or self-injecting, which is a smell).
- **`private`, `final` and `static` methods cannot be advised** (nothing to override).
- The proxy is a *different object*: `getClass()` shows `…$$EnhancerBySpringCGLIB…`, and
  an `instanceof` on a concrete class can fail.
- Calls from a constructor or `@PostConstruct` run before the proxy exists.

AspectJ (compile/load-time weaving) has none of these limits but needs a weaver.

---

## 4) `@Transactional` ⭐⭐⭐⭐⭐

Declarative transactions are an AOP proxy around your method that opens a transaction,
commits on normal return and rolls back on failure.

```java
// java
@Transactional(propagation = Propagation.REQUIRED,
               isolation   = Isolation.READ_COMMITTED,
               rollbackFor = Exception.class,
               timeout     = 5)
public void placeOrder(Order order) { ... }
```

**By default Spring rolls back only on unchecked exceptions (`RuntimeException`) and
`Error`** — a checked exception commits. Say `rollbackFor = Exception.class` if that is
not what you want. This is the single most common `@Transactional` surprise, with
self-invocation (§3) a close second.

| Propagation | Behaviour when a transaction already exists |
|-------------|---------------------------------------------|
| `REQUIRED` (default) | Join it |
| `REQUIRES_NEW` | Suspend it, run in a new one — use for audit rows that must survive a rollback |
| `NESTED` | A savepoint inside it |
| `SUPPORTS` / `NOT_SUPPORTED` / `MANDATORY` / `NEVER` | Join if present / suspend / require / forbid |

Isolation levels and the anomalies they prevent are covered in
[`../backend/後端面試題總整理.md`](../backend/後端面試題總整理.md) and demonstrated in
[`../backend/db_isolation_demo_mysql.md`](../backend/db_isolation_demo_mysql.md).

Other rules: keep transactions **short** (never hold one across an HTTP call), remember
the transaction ends at the method boundary — so lazy-loading afterwards throws
`LazyInitializationException` — and don't catch an exception inside the method and swallow
it, because then the proxy sees a normal return and commits.

---

## 5) Spring MVC: the Request Path ⭐⭐⭐

```text
request → DispatcherServlet (front controller)
        → HandlerMapping        which @RequestMapping method?
        → HandlerAdapter        bind args (@PathVariable, @RequestBody, @RequestParam),
                                run interceptors, validate (@Valid)
        → your @RestController method
        → HttpMessageConverter  return value → JSON (Jackson)
        → response
```

```java
// java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable long id) { ... }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody CreateOrderRequest body) { ... }
}

@RestControllerAdvice                       // one place for error → HTTP mapping
class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
```

`@RestController` = `@Controller` + `@ResponseBody`. **Filters** (Servlet level) wrap the
whole request; **interceptors** (`HandlerInterceptor`) run around the handler and know
which one it is. For error responses see the API-design rules in
[`../backend/api_design.md`](../backend/api_design.md).

---

## 6) Spring Boot ⭐⭐⭐⭐

Boot is Spring plus opinionated defaults: an embedded server, starters that pull a
consistent dependency set, externalised configuration and production endpoints.

```java
// java
@SpringBootApplication      // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class App {
    public static void main(String[] args) { SpringApplication.run(App.class, args); }
}
```

**How auto-configuration works**: starters put candidate `@Configuration` classes on the
classpath (registered in `AutoConfiguration.imports`); each is guarded by `@Conditional`
annotations — `@ConditionalOnClass`, `@ConditionalOnMissingBean`,
`@ConditionalOnProperty`. So "add `spring-boot-starter-data-jpa` and a `DataSource`
appears" is: the class is present, you did not define your own bean, therefore Boot
defines one. **Define your own bean and Boot backs off** — that is the whole contract.
Run with `--debug` to print the conditions report showing what matched and why.

Configuration: `application.yml` per **profile** (`application-prod.yml`), overridden by
environment variables and command-line args (a documented precedence order), bound to
typed objects with `@ConfigurationProperties`. Keep secrets out of the file — inject them
from the environment or a vault.

`spring-boot-starter-actuator` exposes `/actuator/health`, `/metrics` (Micrometer →
Prometheus), `/env` and `/loggers`. Expose only what you need, and secure the rest.

---

## 7) Testing ⭐⭐⭐

| Annotation | Starts | Use for |
|------------|--------|---------|
| none — plain JUnit + Mockito | Nothing | Unit tests. Constructor injection makes this trivial |
| `@WebMvcTest` | The web layer only | Controller mapping, validation, error handling (`MockMvc`) |
| `@DataJpaTest` | JPA + an in-memory/Testcontainers DB | Repositories and queries |
| `@SpringBootTest` | The whole context | A few end-to-end tests — slow, so don't make it the default |

`@MockBean` replaces a bean in the context; Testcontainers gives a real database in
Docker, which is worth it as soon as your SQL is non-trivial. See
[`java_tdd.md`](./java_tdd.md) for test structure.

---

## 8) Design Patterns Inside Spring ⭐⭐⭐

| Pattern | Where |
|---------|-------|
| Factory | `BeanFactory` / `ApplicationContext` |
| Singleton | The default bean scope (container-scoped, not the GoF static one) |
| Proxy | AOP, `@Transactional`, `@Async`, `@Cacheable` |
| Template method | `JdbcTemplate`, `RestTemplate`, `TransactionTemplate` |
| Front controller | `DispatcherServlet` |
| Observer | `ApplicationEvent` / `@EventListener` |
| Adapter | `HandlerAdapter` |
| Decorator | `HttpServletRequestWrapper`, filter chains |

---

## 9) Common Interview Q&A

**Q: IoC vs DI?**
IoC is the principle (the framework owns construction and the calling flow); DI is the
concrete technique for supplying collaborators. DI is one way to achieve IoC.

**Q: Are singleton beans thread-safe?**
No. One instance serves all threads, so any mutable instance field is shared state. Keep
beans stateless, or scope the state per request.

**Q: Why didn't my `@Transactional` roll back?**
Checked exception (default rollback is unchecked only), self-invocation bypassing the
proxy, the exception was caught and swallowed, the method is `private`/`final`, or the
storage engine has no transactions (MyISAM).

**Q: `BeanFactory` vs `ApplicationContext`?**
`ApplicationContext` is a superset: eager singleton instantiation, event publishing,
i18n, `BeanPostProcessor` support and AOP integration. Use it.

**Q: How do you resolve a circular dependency?**
Spring can resolve singleton **field/setter** cycles via its three-level cache, but not
constructor ones. The right fix is design — extract the shared behaviour into a third bean.
`@Lazy` is a workaround, not an answer. (Boot 2.6+ forbids cycles by default.)

**Q: `@Component` vs `@Bean`?**
`@Component` is class-level, discovered by scanning — for your own classes. `@Bean` is
method-level inside a `@Configuration` — for third-party classes or beans needing
construction logic.

**Q: What does `@Configuration` add over `@Component` on a config class?**
Its `@Bean` methods are proxied so that calling one twice returns the *same* singleton
instead of a second instance (`proxyBeanMethods = true`).

**Q: How does Boot's auto-configuration know what to configure?**
Conditional configuration classes listed by starters; see §6.

---

## 10) Recap Checklist

```text
[ ] IoC vs DI; why constructor injection is the default
[ ] Bean lifecycle, and that AOP proxies come from a BeanPostProcessor
[ ] Scopes, and the prototype-in-singleton trap
[ ] Singleton beans are shared — no mutable fields
[ ] AOP vocabulary; JDK vs CGLIB proxies
[ ] Self-invocation, private/final: why the annotation silently does nothing
[ ] @Transactional: rollback rules, propagation, keep it short
[ ] DispatcherServlet flow; @RestControllerAdvice for errors
[ ] Auto-configuration = @Conditional + "your bean wins"
[ ] Test slices vs full @SpringBootTest
```

---

## References

- [Spring Framework reference — Core](https://docs.spring.io/spring-framework/reference/core.html)
- [Spring Boot reference](https://docs.spring.io/spring-boot/index.html)
- [`java_design_pattern.md`](./java_design_pattern.md) — the underlying patterns
- [`../backend/be_programming_notes.md`](../backend/be_programming_notes.md) — backend patterns in production
