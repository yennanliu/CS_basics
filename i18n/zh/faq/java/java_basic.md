<!-- 5640fe66cb71 -->
# Java 基礎 FAQ

> **範圍** — 語言的核心機制：primitive 與參考型別、字串、初始化順序、`Object` 的方法、巢狀類別、反射、I/O 與日期時間 API。
> **另見**：[`faq_OOP.md`](./faq_OOP.md)（OOP 四大支柱、覆寫、SOLID）·
> [`java_collection.md`](./java_collection.md)（集合）·
> [`java_exception.md`](./java_exception.md)（例外）·
> [`java_generics.md`](./java_generics.md)（泛型）·
> [`jvm.md`](./jvm.md)（類別載入、記憶體、GC）。

---

<!-- a709f8f70523 -->
## 1) Primitive 與參考型別 ⭐⭐⭐⭐⭐

<p align="center"><img src="../../pic/basic_ref_data_type.jpeg" width="500" height="300"></p>

| Primitive | 位元 | 範圍／說明 | 預設值 |
|-----------|------|--------------|---------|
| `byte` | 8 | −128 … 127 | `0` |
| `short` | 16 | ±32 K | `0` |
| `int` | 32 | ±21 億 | `0` |
| `long` | 64 | ±9.2 E18 | `0L` |
| `float` | 32 | IEEE 754 | `0.0f` |
| `double` | 64 | IEEE 754 | `0.0d` |
| `char` | 16 | 一個無號的 UTF-16 code unit | `'\u0000'` |
| `boolean` | 由 JVM 決定 | `true` / `false` | `false` |

**參考型別**是類別、介面、陣列與列舉。變數裡放的是參考；物件本身住在 heap 上。
大小由規格固定，所以在 64 位元的 JVM 上 `int` 一樣是 32 位元。

只有**欄位**有預設值。區域變數必須先指派才能用 —— 編譯器會擋。

<!-- 35d34f7c53eb -->
### `==` 與 `equals`

| | `==` | `equals()` |
|---|------|-----------|
| Primitive | 比較值 | 不適用 |
| 參考型別 | 比較**身分**（是不是同一個物件） | 比較**邏輯相等**，由類別自己定義 |

`Object.equals` 比的是身分，所以沒覆寫它的類別，行為就跟 `==` 一樣。
`String`、包裝類別、`List`/`Map` 等等都覆寫了它。

<!--CODE-->

<!-- c428fda9434e -->
### 自動裝箱與 `Integer` 快取 ⭐⭐⭐⭐

<!--CODE-->

兩個後果：**裝箱的值一律用 `equals` 比較**，以及小心拆箱藏起來的 NPE ——
`Integer count = map.get(k); if (count > 0)` 在 key 不存在時會炸。三元運算子也可能
意外拆箱：`flag ? 1 : someInteger` 會把兩邊都拆箱。

---

<!-- 08144667128c -->
## 2) 字串 ⭐⭐⭐⭐⭐

`String` 是**不可變**而且 `final` 的。每一次「修改」都產生新物件，
這就是為什麼字串可以安全地跨執行緒共享、可以放進字串池快取，也可以安全當作 map 的 key
（它的雜湊永遠不變）。

<!-- f862a03e5fb5 -->
### 字串常數池

字面值會被放進一個位於 heap 的池子裡，所以相同的字面值就是同一個物件。
`new String("x")` 是刻意在池子外面再造一個物件；`"x".intern()` 則回傳池子裡的那個。

<!--CODE-->

<!-- f97ba8db4018 -->
### `String`、`StringBuilder` 與 `StringBuffer`

| | `String` | `StringBuilder` | `StringBuffer` |
|---|----------|-----------------|----------------|
| 可變 | 否 | 是 | 是 |
| 執行緒安全 | 是（不可變） | **否** | 是（`synchronized`） |
| 速度 | 反覆修改時最慢 | 最快 | 比 builder 慢 |
| 用在 | 值、key、常數 | 在單一執行緒裡組字串 —— 一般情況 | 舊程式；共用的 buffer（少見） |

**在迴圈裡**串接是經典的錯誤：每個 `+=` 都配置一個新字串再複製，整體變成 `O(n²)`。
請用 `StringBuilder`。（單一運算式的 `a + b + c` 已經會被編成一次 builder 或
`invokedynamic` 呼叫，所以沒問題。）

---

<!-- 60ff156d6508 -->
## 3) 運算子與數值的坑 ⭐⭐⭐

<!--CODE-->

- **整數除法會截斷**：`5 / 2 == 2`；`5 % -2 == 1`（正負號跟著被除數）。
- **無聲溢位**：`Integer.MAX_VALUE + 1` 會繞回 `Integer.MIN_VALUE`。想讓它丟例外就用
  `Math.addExact`，二分搜尋則寫 `left + (right - left) / 2`。
- **浮點數不精確**：`0.1 + 0.2 != 0.3`。金額請用 `BigDecimal`（而且要用**字串**
  建構，不要用 double）。
- `&&` / `||` 會短路；`&` / `|` 不會（而且它們也能做位元運算）。
- `>>` 保留正負號，`>>>` 補零。

---

<!-- bfc18da2aaf7 -->
## 4) 變數、`static` 與 `final` ⭐⭐⭐⭐

| | 成員變數 | 區域變數 |
|---|---------------------------|--------------------------|
| 宣告位置 | 類別本體 | 方法或區塊內 |
| 存在於 | Heap（跟著實例），`static` 的話在類別的中繼資料 | 執行緒的 stack frame |
| 生命週期 | 和實例／類別一樣長 | 到區塊結束為止 |
| 預設值 | 有 | **沒有** —— 必須指派 |
| 存取修飾詞 | 可以加 | 不行（只能加 `final`） |

**`static`** 綁在類別上而不是實例上：只有一份、大家共用、可以用
`ClassName.member` 存取，在類別初始化時初始化。`static` 方法碰不到實例狀態、
也沒有 `this`，而且在子類別裡是被**隱藏**而不是被覆寫 ——
分派是在編譯期依*參考*型別決定的。

`static` 適合常數（`static final`）、無狀態的輔助方法與工廠方法。可變的 `static`
狀態會被 JVM 裡每一條執行緒共用 —— 競態與記憶體洩漏的常見來源。

**`final`**：`final` 變數不能重新綁定（但它指向的物件還是可以被改）、`final` 方法
不能被覆寫、`final` 類別不能被繼承。`final` 欄位還帶有安全發布的保證 ——
見 [`jmm.md`](./jmm.md)。

<!-- cc441a1c18d9 -->
### 初始化順序

<!--CODE-->

父類別的初始化一定在子類別之前完成。這就是為什麼在建構子裡呼叫可被覆寫的方法很危險：
子類別的覆寫執行時，它自己的欄位都還是預設值。

---

<!-- fdac5920d814 -->
## 5) `Object` 的方法 ⭐⭐⭐⭐

每個類別都繼承這些，而前三個幾乎每次面試都會問：

| 方法 | 契約 |
|--------|----------|
| `equals(Object)` | 自反、對稱、遞移、一致；`x.equals(null)` 為 false |
| `hashCode()` | 相等的物件**必須**有相同的雜湊碼；覆寫 `equals` 就一定要覆寫它 |
| `toString()` | 要覆寫 —— 預設的 `Foo@1b6d3586` 在 log 裡毫無用處 |
| `getClass()` | 執行期的類別（`final`） |
| `clone()` | 淺複製；需要實作 `Cloneable`，否則丟 `CloneNotSupportedException` |
| `wait` / `notify` / `notifyAll` | monitor 的協調 —— 見 [`java_multi_thread.md`](./java_multi_thread.md) |
| `finalize()` | 已棄用；絕對不要依賴它。用 try-with-resources 或 `Cleaner` |

<!--CODE-->

違反契約會讓物件從 `HashMap`/`HashSet` 裡「憑空消失」：查找算出來的 bucket 和插入時
算的不一樣。`record` 會正確地把兩個都生出來 ——
見 [`java_modern.md`](./java_modern.md)。

<!-- 72b8fcc0ed8e -->
### 淺複製與深複製

`clone()` 複製的是欄位的值，所以被參考的物件是**共用的**。深複製會把它們重新造一份：

<!--CODE-->

對值型別而言，**不可變**勝過複製：既然什麼都不會變，就沒有什麼要複製。

---

<!-- adf2b7bc9fe3 -->
## 6) 建立物件的幾種方式 ⭐⭐⭐

| 方式 | 例子 | 會跑建構子嗎？ |
|-----|---------|---------------------|
| `new` | `new Foo()` | 會 |
| 反射 | `Foo.class.getDeclaredConstructor().newInstance()` | 會 |
| `clone()` | `foo.clone()` | 不會 |
| 反序列化 | `ObjectInputStream.readObject()` | 不會 —— 但*會*跑第一個**非**序列化父類別的無參數建構子 |
| 工廠／builder | `List.of()`、`Integer.valueOf(1)` | 間接會 |

會跳過建構子的那兩個，正是為什麼單例還必須防守 `readResolve()` 與 `clone()`。

---

<!-- 04a3659a56c9 -->
## 7) 巢狀類別 ⭐⭐⭐

| 種類 | 宣告 | 會抓著外層實例的參考嗎？ |
|------|-------------|------------------------------------------|
| **靜態巢狀** | `static class Node` | 不會 —— 預設就該用這個 |
| **內部類別** | `class Iter` | **會** —— `Outer.this` |
| **區域類別** | 宣告在方法裡 | 只有在實例情境下才會 —— `static` 方法裡不會 |
| **匿名類別** | `new Comparator<>() { … }` | 只有在實例情境下才會 |

內部類別讓外層實例活著是真實的洩漏來源（一個長壽的 listener 抓著
`Activity` 或 service 不放）。除非真的需要外層實例，巢狀的輔助類別都加 `static`。
lambda **不會**每個實例產生一個類別，而且它的 `this` 是外層的 ——
見 [`java_functional.md`](./java_functional.md)。

---

<!-- 05bfbb7ba75d -->
## 8) 反射與標註 ⭐⭐⭐

反射能在執行期檢視與操作類別 —— Spring、Jackson、JUnit 與所有 DI 容器背後的機制。

<!--CODE-->

成本與注意事項：沒有編譯期檢查（改個名字就變成執行期失敗）、比直接呼叫慢、
JIT 也比較難內聯，而且它會打破封裝。框架會快取 `Method`/`Field` handle，
或直接生成 bytecode，讓這個成本只付一次。

**標註（annotation）**是中繼資料；`@Retention(RUNTIME)` 才能讓反射看得到它
（`SOURCE` 只存在於編譯期，例如 `@Override`）。**動態代理**
（`java.lang.reflect.Proxy` + `InvocationHandler`）在執行期實作一個介面，
把每次呼叫都導到同一個 handler —— `@Transactional` 與 RPC stub 就是這樣運作的；
見 [`java_spring.md`](./java_spring.md)。

---

<!-- 98323fb4ce1a -->
## 9) I/O 與 NIO ⭐⭐⭐

傳統的 `java.io` 是**以串流為基礎、而且是阻塞的**；`java.nio` 多了 buffer、channel
與非阻塞的 selector。

| | 位元組串流 | 字元串流 |
|---|-------------|-------------------|
| 基底類別 | `InputStream` / `OutputStream` | `Reader` / `Writer` |
| 用於 | 二進位資料 | 文字（它們會套用字元集） |
| 帶緩衝的包裝 | `BufferedInputStream` | `BufferedReader` |

<!--CODE-->

規則：**一定要指定字元集**（平台預設在不同機器上不一樣 —— UTF-8 到 Java 18 才變成
預設）、一定要加緩衝（沒緩衝的逐位元組讀取等於每個位元組一次系統呼叫），
而且一定要用 try-with-resources 關閉。

**NIO** 對伺服器很重要：一條執行緒可以透過 `Selector` 盯著上千條連線，
Netty 與非阻塞的 servlet 容器就是這樣擴展的。有了虛擬執行緒（Java 21），
普通的阻塞式程式碼也能達到類似的規模 —— 見 [`java_modern.md`](./java_modern.md)。

<!-- 1109e6f939cc -->
### 序列化與 `transient`

實作 `Serializable` 就能讓 JVM 把一整張物件圖寫成位元組。標成 **`transient`** 的
欄位會被跳過 —— 用它來排除祕密（密碼、token）、快取，以及任何不屬於持久狀態的東西
（開著的連線、logger）。

<!--CODE-->

新程式碼最好避開 Java 原生序列化：它是眾所皆知的反序列化攻擊面，
把你的類別結構和傳輸格式綁死，而且會跳過建構子。請改用有明確 schema 的
JSON／Protobuf。

---

<!-- e24b190415a7 -->
## 10) 日期與時間（`java.time`）⭐⭐⭐

Java 8 的這套 API 取代了可變又不執行緒安全的 `Date`/`Calendar`。

| 類別 | 代表 |
|-------|-----------|
| `LocalDate` / `LocalTime` / `LocalDateTime` | **不帶**時區的日期／時間 —— 生日、營業時間 |
| `Instant` | UTC 時間線上的一個點 —— **要存要記 log 的就是它** |
| `ZonedDateTime` | 在某個時區下呈現的瞬間，會處理日光節約 |
| `Duration` / `Period` | 機器時間（秒）／人類時間（月、日） |

<!--CODE-->

這些全都是不可變且執行緒安全的，和 `SimpleDateFormat` 不同 —— 後者需要
`ThreadLocal`，或更好的做法是直接換成 `DateTimeFormatter`。

---

<!-- 8e2e89184840 -->
## 11) 面試常見問答

**Q：Java 是傳值還是傳參考？**
永遠是**傳值** —— 只是對物件來說，傳的那個值是*參考*。所以方法可以改動你傳進去的
那個物件，但沒辦法讓你的變數指向別的地方。

**Q：`String` 為什麼是不可變的？**
不用同步就有執行緒安全、常數池（否則共享就不安全）、當 map key 時可快取的
`hashCode`，還有安全性 —— 驗證過的檔案路徑或 URL 不會在檢查之後被改掉。

**Q：運算式裡的 `a++` 和 `++a`？**見 §3。

**Q：可以覆寫 `static` 方法嗎？**
不行。在子類別裡重新宣告是**隱藏**它；由參考型別決定跑哪一個。

**Q：把參數或集合加上 `final` 有什麼用？**
只保證那個*繫結*不會變。`final List<String> xs` 還是可以被 add；
要不可修改的請用 `List.copyOf(xs)`。

**Q：`int` 和 `Integer` —— 什麼時候差別會浮現？**
可為 null（`Integer` 可以是 `null`）、身分比較（對裝箱值用 `==`）、
效能（裝箱會配置記憶體），以及集合（它們只能裝物件）。

**Q：`serialVersionUID` 是什麼？**
反序列化會檢查的版本印記。不寫的話，**序列化執行期**會依類別結構自己算一個，
於是幾乎任何改動都會讓舊位元組讀不回來（`InvalidClassException`）。
把它釘死，就能讓*相容的*改動（加欄位、加方法）仍然讀得回來 ——
但救不了像移除欄位或改型別這種不相容的改動。

**Q：`throw` 和 `throws`？**見 [`java_exception.md`](./java_exception.md)。

---

<!-- 20bc65528a0b -->
## 12) 重點檢查表

<!--CODE-->

---

<!-- 507a14eab6d8 -->
## 參考資料

- [JavaGuide — Java basics](https://javaguide.cn/java/basis/java-basic-questions-01.html)
- [`faq_OOP.md`](./faq_OOP.md) · [`java_collection.md`](./java_collection.md) · [`java_exception.md`](./java_exception.md) · [`java_generics.md`](./java_generics.md)
- [`jvm.md`](./jvm.md) — 類別載入、記憶體區域與 GC
- [`java_functional.md`](./java_functional.md) · [`java_modern.md`](./java_modern.md)
