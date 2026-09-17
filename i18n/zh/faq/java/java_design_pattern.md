<!-- bfcadadee6fd -->
# Java 設計模式

> **範圍** — 真正會在 Java 面試與 code review 出現的 GoF 模式，每個都附最小範例，以及 JDK 或 Spring 已經用它的地方。
> **另見**：[`faq_OOP.md`](./faq_OOP.md) — 這些模式在套用的原則（SOLID、組合優於繼承）；
> [`java_spring.md`](./java_spring.md) — 由這些模式堆出來的框架。

一份精簡的參考：挑面試最相關的模式，配上最小的 Java 範例。

<!-- 6c9437aa9751 -->
## 0) 分類

| 分類 | 目的 | 例子 |
|----------|---------|----------|
| 創建型 Creational | 物件怎麼被建立 | Singleton、Factory、Builder |
| 結構型 Structural | 物件怎麼被組合 | Decorator、Adapter、Proxy |
| 行為型 Behavioral | 物件之間怎麼互動 | Strategy、Observer、Template Method |

---

<!-- e85f3597d3f3 -->
## 1) Singleton 單例（創建型）

只有一個實例、全域可取用。執行緒安全、延遲初始化又有效率的版本：

<!--CODE-->

用 `enum` 做單例還能同時擋掉反射與序列化的破解：
<!--CODE-->
**注意**：單例就是全域狀態 —— 難測試，也容易把依賴藏起來。

---

<!-- 8d99c80f0580 -->
## 2) Factory Method 工廠方法（創建型）

把實例化延後到一個方法裡，讓呼叫端依賴介面而不是具體類別。

<!--CODE-->

---

<!-- fcb446f460be -->
## 3) Builder 建造者（創建型）

一步一步組出複雜、選填參數又多的物件；避免建構子一路長下去。

<!--CODE-->

---

<!-- e982556838ab -->
## 4) Strategy 策略（行為型）

把可互換的演算法各自封裝起來；行為可以在執行期抽換。

<!--CODE-->

---

<!-- d9566a9eb092 -->
## 5) Observer 觀察者（行為型）

一對多：主體狀態改變時通知所有訂閱者（pub/sub、事件監聽器）。

<!--CODE-->

---

<!-- 1ab541aebe07 -->
## 6) Decorator 裝飾器（結構型）

用包裹的方式動態加上責任 —— 繼承之外的另一條路。

<!--CODE-->
Java I/O（`BufferedReader(new FileReader(...))`）就是現實中的裝飾器串。

---

<!-- 8211dd1a16f6 -->
## 7) Adapter 轉接器（結構型）

讓既有的類別去符合呼叫端期望的介面 —— 兩邊都不用改。

<!--CODE-->

每一個第三方邊界都該用它：你的領域只認一個介面，換供應商就只動一個類別。

---

<!-- ac6545a62d88 -->
## 8) Proxy 代理（結構型）

介面一樣，但由代理控制存取 —— 在真正的物件外面加上延遲初始化、快取、遠端呼叫、
權限或監控。

<!--CODE-->

Java 的 `java.lang.reflect.Proxy` 會在執行期用一個介面加上
`InvocationHandler` 生成代理 —— 這就是 `@Transactional`、`@Cacheable` 與 RPC stub
背後的機制（見 [`java_spring.md`](./java_spring.md)）。

> **Decorator 與 Proxy 的差別**：兩者都在包裹。裝飾器*加上呼叫端要的行為*，而且是
> 有意識地一層層疊；代理則是*控制存取*，而呼叫端以為自己直接在用那個物件。

---

<!-- 00fcac4fb238 -->
## 9) Template Method 模板方法（行為型）

基底類別把演算法的骨架定下來，留幾個步驟讓子類別填。

<!--CODE-->

`JdbcTemplate`、`RestTemplate` 與 `AbstractList` 都是這樣做的 —— 骨架固定，變動的
那一步留給你。它在函數式世界的表親 —— 把變動的那一步當 lambda 傳進去 —— 在今天
通常是更好的選擇。

---

<!-- fd475075d2a2 -->
## 10) 這些模式在真實函式庫裡的樣子

在函式庫裡認出它們，是記住它們最快的方法：

| 模式 | JDK | Spring / MyBatis |
|---------|-----|------------------|
| Factory | `Calendar.getInstance()`、`List.of` | `BeanFactory`、`SqlSessionFactory` |
| Builder | `StringBuilder`、`Stream.Builder` | `SqlSessionFactoryBuilder` |
| Singleton | `Runtime.getRuntime()` | bean 的預設 scope |
| Decorator | `BufferedReader(new FileReader(...))` | `HttpServletRequestWrapper` |
| Proxy | `java.lang.reflect.Proxy` | AOP、`MapperProxy` |
| Adapter | `Arrays.asList`、`Collections.enumeration` | `HandlerAdapter` |
| Template method | `AbstractList`、`InputStream` | `JdbcTemplate`、MyBatis `BaseExecutor` |
| Observer | `PropertyChangeListener`、`Flow` | `ApplicationEvent` |
| Strategy | `Comparator` | `Resource` loader |
| Iterator | `Iterator` | `Cursor` |

<p align="center"><img src="../../pic/mybatis_design_pattern.jpeg"></p>

---

<!-- 8ebda21dfdad -->
## 11) 什麼時候用哪個 — 快速對照

| 需求 | 模式 |
|------|---------|
| 全系統只要一個共用實例 | Singleton |
| 具體型別要在執行期才決定 | Factory |
| 建構參數很多而且大多選填 | Builder |
| 演算法要能在執行期抽換 | Strategy |
| 狀態一變要通知很多人 | Observer |
| 不靠繼承就加上行為 | Decorator |
| 讓不相容的 API 對得上 | Adapter |
| 控制或量測存取 | Proxy |
| 步驟固定、細節各異 | Template Method |

> **模式是你本來就需要的那個形狀的名字** —— 需求還沒出現就先套一個，就是兩個類別
> 的問題變成六個介面的來由。先用最簡單能跑的做法，等第二種變化出現時再往模式重構。

> **DI（依賴注入）** —— 一個類別的協作者由外部提供，而不是自己在裡面 new 出來；
> 這讓它更好測試、也更鬆耦合。
> - https://github.com/ChaoLiou/Blog/issues/74
> - https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/
