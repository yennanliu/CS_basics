<!-- e66d38c12bbd -->
# Spring 與 Spring Boot FAQ

> **範圍** — 容器（IoC/DI、bean 生命週期與 scope）、AOP、`@Transactional` 的語意、Spring MVC 的請求路徑、Spring Boot 的自動組態，以及測試。
> **另見**：[`java_design_pattern.md`](./java_design_pattern.md) — 組成 Spring 的那些
> 模式；[`../backend/be_programming_notes.md`](../backend/be_programming_notes.md)
> — 上線環境的後端模式。

---

<!-- cf2df9b87972 -->
## 1) IoC 與依賴注入 ⭐⭐⭐⭐⭐

**控制反轉（IoC）** —— 物件不自己建立協作者；由容器建立好再交給它。
**依賴注入（DI）**就是這個交付的方式。它的好處不是「少寫幾個 `new`」：
而是一個類別依賴的是*介面*，可以被塞進不同的實作 —— 測試時給 stub，
別的環境給另一個轉接器。

<!--CODE-->

| 注入方式 | 評價 |
|-----------------|---------|
| **建構子** | ✅ 預設就該用它。欄位可以 `final`、依賴一目了然、物件不會處於半成品狀態，用 `new` 就能測 |
| Setter | 留給真正選填的依賴 |
| 欄位（在欄位上 `@Autowired`） | ❌ 把依賴藏起來、不能 `final`、測試還得靠反射。有八個參數的建構子是在*告訴你*這個類別做太多事了 —— 別把它藏起來 |

**解析順序**：先看型別；有歧義時由 `@Qualifier("name")`、`@Primary`，或讓參數名稱
對上 bean 名稱來解決。宣告成 `Map<String, Handler>` 或 `List<Handler>` 的參數會被注入
**所有**實作 —— 這是建立策略註冊表最乾淨的做法。

`@Component`（以及它的衍生語意標註 `@Service`、`@Repository`、`@Controller`）
標記要被掃描的類別；`@Configuration` 類別裡的 `@Bean` 方法則用來註冊你沒有原始碼、
或需要建構邏輯的 bean。

---

<!-- 642957140049 -->
## 2) Bean 的生命週期與 scope ⭐⭐⭐⭐

<!--CODE-->

`BeanPostProcessor` 是真正重要的擴充點：Spring 就是在這裡把你的 bean 包進代理，
這也是為什麼 `@Transactional`、`@Async` 與 `@Cacheable` 的行為會像 §3 說的那樣。

| Scope | 每個什麼一份 | 備註 |
|-------|------------------|-------|
| `singleton`（預設） | 容器 | 啟動時就急切建立。**必須無狀態** —— 它被每一條請求執行緒共用 |
| `prototype` | 每次向容器要 | 每次 `getBean`/`ObjectProvider` 都給新實例 —— 但注入進 singleton 只會發生**一次**，所以那個 singleton 會永遠抱著同一個實例。Spring 也不管 prototype 的銷毀 |
| `request` / `session` | HTTP 請求／session | 只有 web 環境；以代理的形式注入 |
| `application` / `websocket` | `ServletContext` / socket | 少見 |

經典陷阱：把 **prototype** bean 注入 **singleton**，你會永遠拿到同一個實例，
因為注入只發生一次。要用 `ObjectProvider<T>`、`@Lookup` 方法，或 scoped proxy。

Singleton bean 不會自己變成執行緒安全 —— `@Service` 上一個可變欄位，就是跨並發請求
的共享狀態。

---

<!-- 9b42bb7d378d -->
## 3) AOP 與代理 ⭐⭐⭐⭐⭐

**切面導向程式設計**把橫切關注點（交易、快取、安全、記錄、指標）抽出來，
讓商業邏輯不用一直重複它們。

| 名詞 | 意思 |
|------|---------|
| Aspect 切面 | 裝著這個關注點的模組 |
| Join point 連接點 | advice 可以套用的位置（在 Spring 裡就是方法呼叫） |
| Pointcut 切入點 | 挑出哪些 join point 的表示式 |
| Advice 通知 | 要執行的程式：`@Before`、`@After`、`@AfterReturning`、`@AfterThrowing`、`@Around` |
| Weaving 織入 | 把切面接上去 —— Spring 是在**執行期**用代理做的 |

<!--CODE-->

<!-- e7a157009012 -->
### 真的會造成 bug 的那幾條代理規則 ⭐⭐⭐⭐⭐

Spring AOP 會把 bean 包進 **JDK 動態代理**（以介面為基礎）或 **CGLIB 子類別**。
用哪一種取決於設定，不只是型別：純 Spring 在 bean 有實作介面時選 JDK 代理，
而 **Spring Boot 設了 `spring.aop.proxy-target-class=true`**，所以在 Boot 裡就算
bean 有介面，預設也是 CGLIB。而 advice 只有在呼叫*經過代理*時才會生效。因此：

- **自我呼叫不會被織入。**同一個類別內的 `this.otherMethod()` 會繞過代理，
  所以 `otherMethod` 上的 `@Transactional` / `@Cacheable` / `@Async` 完全沒作用。
  修法是把那個方法搬到另一個 bean（或注入自己，但那是壞味道）。
- **`private`、`final` 與 `static` 方法無法被織入**（沒有東西可以覆寫）。
- 代理是*另一個物件*：`getClass()` 會顯示 `…$$EnhancerBySpringCGLIB…`，
  對具體類別做 `instanceof` 可能會失敗。
- 從建構子或 `@PostConstruct` 發出的呼叫，發生在代理存在之前。

AspectJ（編譯期／載入期織入）沒有這些限制，但需要一個 weaver。

---

<!-- 7dbf9f1903b1 -->
## 4) `@Transactional` ⭐⭐⭐⭐⭐

宣告式交易就是在你的方法外面包一層 AOP 代理：開啟交易、正常返回就 commit、
失敗就 rollback。

<!--CODE-->

**預設情況下，Spring 只在 unchecked 例外（`RuntimeException`）與 `Error` 時 rollback**
—— checked 例外會 commit。如果那不是你要的，就寫 `rollbackFor = Exception.class`。
這是 `@Transactional` 最常見的意外，第二名是自我呼叫（§3）。

| 傳播行為 Propagation | 已經有交易在跑時的行為 |
|-------------|---------------------------------------------|
| `REQUIRED`（預設） | 加入它 |
| `REQUIRES_NEW` | 把它掛起，開一個新的跑 —— 適合那種必須在 rollback 後仍留下的稽核紀錄 |
| `NESTED` | 在它裡面開一個 savepoint |
| `SUPPORTS` / `NOT_SUPPORTED` / `MANDATORY` / `NEVER` | 有就加入／掛起／必須要有／絕對不能有 |

隔離級別與它們各自防住的異常，在
[`../backend/後端面試題總整理.md`](../backend/後端面試題總整理.md) 有整理，
[`../backend/db_isolation_demo_mysql.md`](../backend/db_isolation_demo_mysql.md) 有實際演示。

其他規則：交易要**短**（絕對不要抱著交易去打 HTTP）；記得交易在方法邊界就結束了，
所以之後才去 lazy load 一個已經 detached 的 entity 會丟
`LazyInitializationException`（Spring Boot 預設的 `spring.jpa.open-in-view=true` 讓
persistence context 整個請求都開著，把這件事蓋掉了 —— 很方便，也是為什麼有人一關掉
它才開始出錯）；還有，不要在方法裡把例外接住又吞掉，那樣代理看到的是正常返回，
於是就 commit 了。

---

<!-- c0e175c11bd0 -->
## 5) Spring MVC：請求的路徑 ⭐⭐⭐

<!--CODE-->

<!--CODE-->

`@RestController` = `@Controller` + `@ResponseBody`。**Filter**（Servlet 層級）包住
整個請求；**interceptor**（`HandlerInterceptor`）則跑在 handler 前後，而且知道
是哪一個 handler。錯誤回應的規則見
[`../backend/api_design.md`](../backend/api_design.md) 的 API 設計那一節。

---

<!-- 0476b8043aab -->
## 6) Spring Boot ⭐⭐⭐⭐

Boot 就是 Spring 加上一組有主見的預設值：內嵌伺服器、把相容的依賴一次帶進來的
starter、外部化的設定，以及上線用的端點。

<!--CODE-->

**自動組態是怎麼運作的**：*starter* 是依賴的聚合器 —— 它把函式庫加上
`spring-boot-autoconfigure` 拉進來，而後者才是真正帶著候選 `@Configuration` 類別的
那個 jar（列在它的 `META-INF/spring/…AutoConfiguration.imports`）。每個候選者都被
`@Conditional` 系列標註守著 —— `@ConditionalOnClass`、`@ConditionalOnMissingBean`、
`@ConditionalOnProperty`。所以「加了 `spring-boot-starter-data-jpa`，`DataSource`
就冒出來了」的意思是：那個類別在、你沒有自己定義 bean，所以 Boot 幫你定義一個。
**你自己定義了，Boot 就退讓** —— 整份契約就是這樣。加上 `--debug` 執行，
可以印出條件報告，看看什麼命中了、為什麼。

設定：`application.yml` 依 **profile** 分（`application-prod.yml`），可以被環境變數
與命令列參數覆蓋（優先順序有明文規定），並用 `@ConfigurationProperties` 綁到型別化的
物件上。祕密不要放在檔案裡 —— 從環境變數或 vault 注入。

`spring-boot-starter-actuator` 提供 `/actuator/health`、`/metrics`（Micrometer →
Prometheus）、`/env` 與 `/loggers`。只開你需要的，其他要加上保護。

---

<!-- 7b2b03175f96 -->
## 7) 測試 ⭐⭐⭐

| 標註 | 會啟動什麼 | 用在 |
|------------|--------|---------|
| 什麼都不加 —— 純 JUnit + Mockito | 什麼都不啟動 | 單元測試。用建構子注入的話這超簡單 |
| `@WebMvcTest` | 只有 web 層 | controller 對應、驗證、錯誤處理（`MockMvc`） |
| `@DataJpaTest` | JPA 加上記憶體／Testcontainers 資料庫 | repository 與查詢 |
| `@SpringBootTest` | 整個 context | 少量的端對端測試 —— 很慢，別把它當預設 |

`@MockBean` 用來替換 context 裡的 bean（Spring Boot 3.4+ 已棄用它，改用 `@MockitoBean`）；
Testcontainers 則在 Docker 裡給你一個真的資料庫，只要你的 SQL 稍微複雜一點就很值得。
測試結構見 [`java_tdd.md`](./java_tdd.md)。

---

<!-- f2514f12a6c8 -->
## 8) Spring 內部的設計模式 ⭐⭐⭐

| 模式 | 出現在哪 |
|---------|-------|
| Factory | `BeanFactory` / `ApplicationContext` |
| Singleton | 預設的 bean scope（容器範圍內，不是 GoF 那種 static 單例） |
| Proxy | AOP、`@Transactional`、`@Async`、`@Cacheable` |
| Template method | `JdbcTemplate`、`RestTemplate`、`TransactionTemplate` |
| Front controller | `DispatcherServlet` |
| Observer | `ApplicationEvent` / `@EventListener` |
| Adapter | `HandlerAdapter` |
| Decorator | `HttpServletRequestWrapper`、filter chain |

---

<!-- 248387da76b7 -->
## 9) 面試常見問答

**Q：IoC 和 DI 差在哪？**
IoC 是原則（由框架掌握建構與呼叫流程）；DI 是提供協作者的具體技術。
DI 是達成 IoC 的其中一種方式。

**Q：Singleton bean 是執行緒安全的嗎？**
不是。一個實例服務所有執行緒，所以任何可變的實例欄位都是共享狀態。
讓 bean 保持無狀態，或把狀態的 scope 縮到每個請求。

**Q：我的 `@Transactional` 為什麼沒有 rollback？**
丟的是 checked 例外（預設只對 unchecked rollback）、自我呼叫繞過了代理、
例外被接住又吞掉、方法是 `private`/`final`，或儲存引擎根本不支援交易（MyISAM）。

**Q：`BeanFactory` 和 `ApplicationContext` 差在哪？**
`ApplicationContext` 是超集合：急切建立 singleton、事件發布、
國際化、支援 `BeanPostProcessor`，以及整合 AOP。用它就對了。

**Q：循環依賴怎麼解？**
Spring 可以靠三層快取解掉 singleton 的**欄位／setter** 循環，但解不掉建構子的循環。
真正的修法是改設計 —— 把共用的行為抽成第三個 bean。
`@Lazy` 是繞路，不是答案。（Boot 2.6+ 預設直接禁止循環。）

**Q：`@Component` 和 `@Bean` 差在哪？**
`@Component` 標在類別上，由掃描發現 —— 給你自己的類別用。`@Bean` 標在
`@Configuration` 裡的方法上 —— 給第三方類別，或需要建構邏輯的 bean 用。

**Q：設定類別上用 `@Configuration` 比 `@Component` 多了什麼？**
它的 `@Bean` 方法會被代理，所以呼叫兩次拿到的是*同一個* singleton，
而不是第二個實例（`proxyBeanMethods = true`）。

**Q：Boot 的自動組態怎麼知道要設定什麼？**
靠 starter 列出來的那些帶條件的組態類別；見 §6。

---

<!-- 51c56a91eed5 -->
## 10) 重點檢查表

<!--CODE-->

---

<!-- a640be264748 -->
## 參考資料

- [Spring Framework reference — Core](https://docs.spring.io/spring-framework/reference/core.html)
- [Spring Boot reference](https://docs.spring.io/spring-boot/index.html)
- [`java_design_pattern.md`](./java_design_pattern.md) — 底下的那些模式
- [`../backend/be_programming_notes.md`](../backend/be_programming_notes.md) — 上線環境的後端模式
