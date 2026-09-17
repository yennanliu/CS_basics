<!-- cfd41f352f43 -->
# 現代 Java FAQ（Java 9 – 21）

> **範圍** — Java 8 之後改了什麼：`var`、record、sealed 型別、模式比對、text block、模組系統，以及虛擬執行緒。
> **另見**：[`java_functional.md`](./java_functional.md) — Java 8 的 lambda／stream
> 工具箱；[`java_multi_thread.md`](./java_multi_thread.md) — 平台執行緒與執行緒池。

多數說自己「還在 Java 8」的團隊，其實早就在 11 或 17 上了；而面試官想問的是：如果
不是 8，你會用什麼。下面這份短清單，是真正改變寫法的那些東西。

---

<!-- 76f902124f3b -->
## 1) 版本地圖 ⭐⭐⭐

| 版本 | 狀態 | 代表性功能 |
|---------|--------|-------------------|
| **8**（2014） | LTS，到處都還在用 | Lambda、stream、`Optional`、新的日期時間 API |
| **9** | — | 模組系統（JPMS）、`List.of`、`Stream.takeWhile` |
| **10** | — | 區域變數的 `var` |
| **11**（2018） | **LTS** | `String.strip/repeat/lines`、`HttpClient`、單檔原始碼直接執行 |
| **14–16** | — | `switch` 運算式、record、text block、`instanceof` 模式 |
| **17**（2021） | **LTS** | Sealed class，上述功能正式定案 |
| **21**（2023） | **LTS** | **虛擬執行緒**、`switch` 的模式比對、record pattern、sequenced collection |

Java 9 之後改成每半年一個版本，所以版本號能說明的能力比以前少得多；真正重要的是
你鎖定哪一個 LTS。

---

<!-- c9dab6357d72 -->
## 2) `var` —— 區域變數型別推論 ⭐⭐⭐

<!--CODE-->

- 只能用在區域變數、`for` 的變數與 `try`-with-resources —— 不能用在欄位、參數或
  回傳型別。
- 型別依然是**靜態且固定的**；這是推論，不是動態型別。
- 右邊已經寫出型別時就用它。會把讀者需要的型別藏起來時就不要用
  （`var result = service.process(x);`）。

---

<!-- 2cf6680a0ce0 -->
## 3) Record ⭐⭐⭐⭐⭐

Record 是不可變資料的透明載體 —— 就是你以前靠 IDE 幫你產生的那種類別。

<!--CODE-->

編譯器會幫你生成：`private final` 欄位、正規建構子、名為 `cents()` / `currency()`
的存取子（沒有 `get` 前綴），以及 `equals`、`hashCode` 和 `toString`。

| 性質 | 後果 |
|----------|-------------|
| 隱含 `final`，不能繼承類別 | 它不是用來取代類別階層的 |
| 欄位都是 `final` | 值語意，可以安全地跨執行緒共享 |
| `equals`/`hashCode` 由所有成分算出 | 可以直接當 map 的 key |
| 成分只是「淺層」不可變 | `record Team(List<Player> players)` 還是可以透過那個 list 被改動 —— 在 compact 建構子裡複製一份 |

適合 DTO、API 的 request/response 主體、值物件，以及回傳多個值的場合。
需要身分（identity）或可變性的地方就不要用（例如 JPA entity）。

---

<!-- 7f89375f64a6 -->
## 4) Sealed 型別與模式比對 ⭐⭐⭐⭐

`sealed` 把允許的子型別固定下來 —— 一個編譯器推論得動的**封閉階層**。

<!--CODE-->

再配上（21 的）`switch` 模式比對，編譯器能證明這個 switch 是**窮盡的**，
於是「新增一個子型別後執行期靜悄悄地做錯事」就變成了編譯錯誤：

<!--CODE-->

同一組功能裡比較小的幾塊：

<!--CODE-->

這就是*代數資料型別*的寫法：sealed interface + 用 record 當各個變體 +
對它們 `switch`。它讓你不用 visitor 也能表達「N 種情況中的一種」。

---

<!-- c3bbdde73a82 -->
## 5) Text block 與字串的新方法 ⭐⭐

<!--CODE-->

---

<!-- 791c814a46c3 -->
## 6) 集合與 API 的新東西 ⭐⭐⭐

<!--CODE-->

`List.of` 回傳的是**真正不可變**的 list（add/remove/set 全都丟例外），
不像 `Arrays.asList`（長度固定但可以 `set`）或
`Collections.unmodifiableList`（只是某個別人還改得動的 list 的視圖）。

---

<!-- 370f132ef610 -->
## 7) 模組系統（JPMS）⭐⭐

<!--CODE-->

它提供 package 層級的強封裝，加上明確宣告的依賴；接著 `jlink` 就能只用你會用到的
模組建出一個 runtime image。實務上多數應用還是留在 classpath 上，只在
「illegal reflective access」警告與 `--add-opens` 旗標上感受到 JPMS 的存在。
知道它是什麼、以及 JDK 自己為什麼要模組化，就夠了。

---

<!-- 6b2d9e416e09 -->
## 8) 虛擬執行緒（Project Loom，21）⭐⭐⭐⭐⭐

**虛擬執行緒**是一個由 JVM 排程到少量 OS carrier 執行緒上的 `Thread`。當它因為 I/O
阻塞時會被*卸載（unmount）*，把 carrier 釋放出來 —— 所以阻塞式的程式碼不必重寫，
就能有非同步程式碼的擴展性。

<!--CODE-->

| | 平台執行緒 | 虛擬執行緒 |
|---|-----------------|----------------|
| 背後是 | 一條 OS 執行緒 | 配置在 heap 上、跑在 carrier 執行緒上的 stack |
| 成本 | 約 1 MB stack，建立很貴 | 約幾 KB，很便宜 —— 每個任務開一條 |
| 合理的數量 | 數百條 | 數百萬條 |
| 阻塞 I/O | 白白佔著 OS 執行緒 | 卸載，carrier 去做別的事 |
| 池化 | 必要 | **反模式** —— 每個任務開一條就好 |

實務上會改變的事：

- **執行緒池不再是限制並發的手段。**要限制對資料庫或下游服務的存取，改用
  `Semaphore`。
- **在 21 上，`synchronized` 會把**虛擬執行緒釘（pin）在它的 carrier 上，只要它在
  區塊裡阻塞，就可能把一池 carrier 榨乾；`ReentrantLock` 不會，這就是 21 上建議在
  阻塞呼叫外圍用它的原因。（21 之後的補充：Java 24 的 JEP 491 移除了這個 pinning，
  所以 24 以上直接用 `synchronized` 又沒問題了。）
- `ThreadLocal` 還能用，但「建立很貴，所以每條執行緒快取一份」這個理由不成立了；
  scoped value 是設計上的替代品。
- CPU 密集的工作一點好處也沒有 —— Loom 講的是**並發**（很多在等待的任務），
  不是平行。

**結構化並發（structured concurrency）**把一個任務的子任務做成一個範圍，整組一起
失敗、一起取消 —— 和 Python 的 `TaskGroup` 是同一個想法。它目前還是**預覽 API**：
在 21 上編譯與執行都要加 `--enable-preview`，而且它的形狀在各版本之間改過，
所以別把它放進你沒辦法重新編譯的正式程式碼裡。

<!--CODE-->

---

<!-- 95fb6927feef -->
## 9) 面試常見問答

**Q：為什麼要從 8 升到 17/21？**
record 與 sealed 型別去掉樣板碼，也讓不合法的狀態根本表達不出來；`switch` 運算式與
模式比對消掉一整類 fall-through 的 bug；G1/ZGC 與 JIT 的改進壓低了停頓時間；21 再加上
虛擬執行緒。另外，8 在多數廠商那邊已經沒有免費的公開支援了。

**Q：Record 和 Lombok 的 `@Value` 差在哪？**
Record 是語言功能 —— 不需要 annotation processor、不需要 IDE 外掛，而且編譯器懂它
（模式、窮盡性檢查）。如果你需要可變性或 builder，而那個類別又不能做成 record，
Lombok 還是贏。

**Q：sealed、`final` 與 package-private 建構子怎麼選？**
`final` 完全禁止繼承；package-private 建構子把繼承限制在同一個 package，但對編譯器
什麼都沒說。`sealed` 明確列出允許的子型別，這才是窮盡性檢查能成立的前提。

**Q：虛擬執行緒會取代 reactive 框架嗎？**
對大多數「一個請求一個任務」的服務來說，是的 —— 用普通、可除錯、有完整 stack trace
的阻塞式程式碼就能有同樣的擴展性。需要串流背壓語意的地方，reactive 仍然勝出。

**Q：`var` 會讓可讀性變差嗎？**
只有在右邊看不出型別的時候。`var list = new ArrayList<String>()` 很清楚；
`var x = compute()` 就不清楚。

---

<!-- 51c56a91eed5 -->
## 10) 重點檢查表

<!--CODE-->

---

<!-- c54accf33dfb -->
## 參考資料

- [JDK 21 release notes](https://openjdk.org/projects/jdk/21/)
- [JEP 444 — Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 395 — Records](https://openjdk.org/jeps/395)
- [`java_functional.md`](./java_functional.md) — lambda、stream、`Optional`
