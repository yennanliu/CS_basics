<!-- 15508304d036 -->
# Java 例外 FAQ

> **範圍** — `Throwable` 階層、checked 與 unchecked 的差別、`try`/`catch`/`finally` 的運作細節、try-with-resources，以及應用程式裡處理例外的設計準則。
> **另見**：[`java_basic.md`](./java_basic.md) — 語言基礎；
> [`java_tdd.md`](./java_tdd.md) — 在測試裡對例外做斷言。

---

<!-- ecf11b5488d9 -->
## 1) 階層 ⭐⭐⭐⭐⭐

<!--CODE-->

| | Checked | Unchecked（`RuntimeException`） | `Error` |
|---|---------|-------------------------------|---------|
| 編譯器強制處理 | 是 —— 要 catch 或宣告 | 不會 | 不會 |
| 代表 | 呼叫端可以反應的、預期會發生的外部失敗 | 程式碼裡的 bug | JVM 自己出事了 |
| 例子 | 檔案不存在、網路斷線 | 存取 null、參數錯誤 | 記憶體不足 |
| 你該做的 | 處理它或包裝它 | 去把程式修好 | 讓它把行程帶走 |

`Error` 和 `Exception` 的差別是例外題裡最常被問的一題：**`Error` 本來就不是設計來被
catch 的**，因為通常沒有任何合理的處置 —— 一個 `OutOfMemoryError` 的處理器往往還得
配置記憶體。

---

<!-- 85c9687a3e64 -->
## 2) `throw` 與 `throws` ⭐⭐⭐

<!--CODE-->

- `throws` 是方法**宣告**的一部分 —— 是 API 契約。（但它不是*簽章*的一部分：不能靠它多載。）
- `throw` 是一個**陳述句**，丟出某個例外實例。
- 覆寫的方法能丟的 checked 例外只能**更少或更窄**，不能比被覆寫的方法更多 ——
  否則用基底型別的呼叫端會被殺個措手不及。

---

<!-- 265a95a96bc9 -->
## 3) `try` / `catch` / `finally` 的運作細節 ⭐⭐⭐⭐

<!--CODE-->

會被考的規則：

- **`finally` 一定會跑** —— 包括 `try` 區塊裡已經 `return` 之後：回傳值先算出來，
  接著跑 `finally`，最後才真正回傳。
- **在 `finally` 裡 `return`（或 `throw`）會把待丟的例外或回傳值吃掉。**
  千萬不要這樣寫；靜態分析工具都會警告。
- Java 層面能跳過 `finally` 的方式只有 `System.exit()`、`Runtime.getRuntime().halt()`，
  或永遠離不開 `try`（無窮迴圈、死鎖）。JVM 管不到的則是：崩潰、`kill -9`、機器斷電。
- 把父型別的 catch 放在子型別前面是**編譯錯誤**（永遠到不了的 catch）。
- catch `Exception` 會把每一個 `RuntimeException` 也一起接住 —— 包括你本來希望它
  大聲失敗的那些 NPE。

<!--CODE-->

---

<!-- 22a23dc74666 -->
## 4) try-with-resources ⭐⭐⭐⭐⭐

宣告在 header 裡的任何 `AutoCloseable` 都會自動關閉，而且是**反序**關閉，
不管區塊是正常結束還是丟了例外。

<!--CODE-->

它為什麼比 `try/finally` 好：手寫 `finally` 時，`close()` 丟出的例外會**取代**原本的
例外，真正的原因就不見了。try-with-resources 會保留原本的例外，並把關閉失敗
掛成**被抑制（suppressed）**的例外（`e.getSuppressed()`）。

---

<!-- 62bcb7fb5d15 -->
## 5) 用例外做設計 ⭐⭐⭐⭐

<!-- ac25e16b8465 -->
### 包起來，不要外洩

讓每一層丟自己抽象層次的例外，並把 cause 串起來，這樣 stack trace 才不會斷：

<!--CODE-->

要傳 cause **物件**，不要只傳它的文字：`throw new X("context", e)` 保留了原本的
stack trace，而 `throw new X(e.getMessage())` 則把它丟掉 —— 這是根因從 log 裡消失
最常見的原因。

<!-- a7f7fcba2bd8 -->
### 經驗法則

- **程式錯誤要快速失敗**：`Objects.requireNonNull(x, "x")`、輸入不對用
  `IllegalArgumentException`、呼叫順序不對用 `IllegalStateException`。
- **不要用例外做流程控制。**一個本來就常常查不到東西的查找，應該回傳 `Optional`，
  而不是丟例外。
- **絕對不要吞掉**：空的 `catch` 區塊，或 `catch (Exception e) { e.printStackTrace(); }`，
  都是把真正的失敗藏起來。記 log 要**連例外物件一起**（`log.error("...", e)`）——
  只傳 `e.getMessage()` 等於把 stack trace 丟掉。
- **保住中斷旗標。**`catch (InterruptedException e)` 必須重新丟出，或呼叫
  `Thread.currentThread().interrupt()`；否則取消的訊號就失傳了。
- **無法復原的失敗優先用 unchecked。**現代 API（Spring、JPA）會把 checked 例外包成
  runtime 例外，免得呼叫端被逼著寫空的 `catch`。
- **訊息要帶上下文**：識別碼與實際值，而不是「發生錯誤」。
- 自訂例外：每個模組一個基底類別（`OrderException`），呼叫端需要分流的情況再開子類別。
  不要一個訊息就發明一個類別。

<!-- fddbb2e6569c -->
### 成本

建立例外時會擷取 stack trace，那才是貴的部分 —— 熱路徑上每秒幾千個，在 profile 裡
一定看得見。只有在真的很熱、而且形同流程控制的訊號上，才考慮覆寫 `fillInStackTrace()`
（或用一個共享、不帶 stack 的實例）。

---

<!-- c925db9b5060 -->
## 6) 面試常見問答

**Q：checked 還是 unchecked，你偏好哪個？為什麼？**
程式錯誤、以及呼叫端根本無法有意義處理的失敗（也就是大多數情況）用 unchecked；
呼叫端真的有另一條路可走時才用 checked。checked 例外會沿著每一層往外漏，也鼓勵
大家寫空的 catch，這就是為什麼多數現代框架避開它。

**Q：`final`、`finally`、`finalize`？**
`final` 是關鍵字（常數／不可覆寫／不可繼承）；`finally` 是一定會跑的區塊；
`finalize()` 是 `Object` 上那個已棄用、以前由 GC 呼叫的鉤子 ——
永遠不要依賴它，用 try-with-resources 或 `Cleaner`。

**Q：`finally` 有可能被跳過嗎？**
有：`System.exit()` / `Runtime.halt()`、永遠離不開 `try` 區塊的執行緒，或任何
直接把行程弄死的事（JVM 崩潰、`kill -9`）。

**Q：什麼是被抑制（suppressed）的例外？**
在關閉 try-with-resources 的資源時丟出的例外；它會被掛在主要例外上，而不是取代它。

**Q：為什麼最外層的 `catch (Exception e)` 還是可以接受？**
在**邊界**上（請求處理器、執行緒的 `run`、`main`），廣泛地 catch 起來記 log、回 500
是正確的 —— 重點是你上面已經沒有人會做這件事了。但在商業邏輯裡面，它是壞味道。

**Q：執行緒裡丟出的例外會怎樣？**
它只會終止那條執行緒，然後交給該執行緒的 `UncaughtExceptionHandler`
（預設是印到 `stderr`）。在 `ExecutorService` 裡則看你是怎麼交任務的：`submit()`
會把例外裝進回傳的 `Future`，所以在**你呼叫 `Future.get()` 之前它是靜悄悄的**；
`execute()` 沒有 `Future`，所以它會像其他執行緒一樣走到 worker 執行緒的
`UncaughtExceptionHandler`。

**Q：`NoClassDefFoundError` 和 `ClassNotFoundException` 差在哪？**
`ClassNotFoundException` 是 checked 的，來自明確的
`Class.forName`／`loadClass`。`NoClassDefFoundError` 表示這個類別在編譯期存在，
但執行期不見了（或初始化失敗）—— 通常是打包的問題。

---

<!-- ef160d7245e4 -->
## 7) 重點檢查表

<!--CODE-->

---

<!-- df1b67a95265 -->
## 參考資料

- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [`java_basic.md`](./java_basic.md) — 語言基礎
- [`java_tdd.md`](./java_tdd.md) — 測試「該丟的例外有丟出來」
