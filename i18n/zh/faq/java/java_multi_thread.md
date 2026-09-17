<!-- a113175a4f4d -->
# Java 多執行緒 FAQ

> **範圍** — 怎麼寫出正確的並發 Java：執行緒生命週期、`synchronized` 與 `ReentrantLock`、CAS 與 atomic、執行緒池、`CompletableFuture`、`ThreadLocal` 以及並發集合。
> **另見**：[`jmm.md`](./jmm.md) — *為什麼*可見性與順序性需要這些工具；
> [`jvm.md`](./jvm.md) — safepoint 與 thread dump；
> [`java_modern.md`](./java_modern.md) — 虛擬執行緒。

---

<!-- 76193bbd9392 -->
## 1) 並發與平行 ⭐⭐⭐

- **並發 Concurrency** —— 多個任務在同一段時間內*都在進行中*；在單核上它們是切片
  交錯執行。它講的是**結構**：同時處理很多件事。
- **並行 Parallelism** —— 多個任務*在同一瞬間*跑在不同核心上。
  它講的是**執行**：同時做很多件事。

<p align="center"><img src="../../pic/concurrent.png"></p>
<p align="center"><img src="../../pic/parallel.png"></p>

**同步 Sync 與非同步 Async** 是另一個軸：同步呼叫在結果出來之前不會返回；
非同步呼叫立刻返回，結果稍後才到（callback、`Future`、事件）。
阻塞／非阻塞則是在講那條*執行緒*有沒有在等。

---

<!-- bf7ad5b15445 -->
## 2) 執行緒：生命週期與基礎 ⭐⭐⭐⭐

<!--CODE-->

Java 的六個執行緒狀態（`Thread.State`）：

<!--CODE-->

注意 `RUNNABLE` 同時涵蓋「正在 CPU 上跑」與「準備好但還沒被排到」—— JVM 不區分這兩者。
`BLOCKED` 專指*正在等著進入 `synchronized` 區塊*，這也是 thread dump 讀得懂的關鍵。

| 方法 | 會釋放鎖嗎？ | 備註 |
|--------|-------------------|-------|
| `Object.wait()` | **會** | 必須持有 monitor；永遠放在檢查條件的 `while` 迴圈裡呼叫 |
| `Thread.sleep(ms)` | **不會** | 純粹延遲 |
| `Thread.join()` | 不適用 | 等另一條執行緒結束 |
| `Thread.yield()` | 不會 | 給排程器的提示；很少有用 |
| `LockSupport.park()` | 不會 | `ReentrantLock` 底下的原語 |

**中斷是協作式的。**`t.interrupt()` 只是設一個旗標（並讓阻塞中的呼叫丟出
`InterruptedException`）；它不會強制停下任何東西。`Thread.stop()` 已棄用且不安全 ——
它可能讓物件停在改到一半的狀態。在 `catch (InterruptedException e)` 裡，
要嘛重新丟出，要嘛呼叫 `Thread.currentThread().interrupt()` 把旗標補回去。

---

<!-- 37141e0c7a86 -->
## 3) `synchronized` ⭐⭐⭐⭐⭐

互斥，外加 [`jmm.md`](./jmm.md) §4 講的那些可見性保證。

<!--CODE-->

- 同一個類別的實例方法與 static 方法鎖的是**不同**的 monitor，所以它們不會互斥。
  這是常見的 bug。
- 它是**可重入**的：已經持有 monitor 的執行緒可以再進去。
- 千萬不要鎖在 `String` 字面值或裝箱的 `Integer` 上 —— 常數池與 integer 快取
  會讓毫不相干的程式碼跟你共用同一把鎖。用一個 `private final Object`。
- 鎖住能守住不變條件的**最小**臨界區，而且持鎖時絕對不要呼叫不明或會阻塞的方法。

**底層**：對物件 mark word 操作的 `monitorenter`/`monitorexit` bytecode。
HotSpot 的鎖只會單向升級 —— *偏向鎖*（單一執行緒、不用 CAS；JDK 15 起預設關閉，
18 移除）→ *輕量級鎖*（CAS 自旋，低競爭）→
*重量級鎖*（OS mutex，執行緒被 park）。這就是為什麼沒有競爭的 `synchronized`
幾乎免費，而有競爭的 `synchronized` 很貴。

---

<!-- 44f3012c8ede -->
## 4) `ReentrantLock` 與 AQS ⭐⭐⭐⭐

<!--CODE-->

| | `synchronized` | `ReentrantLock` |
|---|----------------|-----------------|
| 層級 | JVM 關鍵字 | `java.util.concurrent` 的 API |
| 釋放 | 離開或丟例外時自動釋放 | **你必須**在 `finally` 裡 `unlock()` |
| 試著拿／逾時 | 沒有 | `tryLock()`、`tryLock(t, unit)` |
| 等待可被中斷 | 不行 | `lockInterruptibly()` |
| 公平性 | 只有不公平 | `new ReentrantLock(true)` 走 FIFO（較慢） |
| 條件佇列 | 只有一個（`wait`/`notify`） | 可以很多個（每個條件一個 `newCondition()`） |
| 可重入 | 是 | 是 |

單純的互斥優先用 `synchronized`（能寫錯的地方比較少，JVM 也會最佳化它）；
需要逾時、可中斷、公平性或多個條件佇列時，才拿出 `ReentrantLock`。

相關的還有：`ReentrantReadWriteLock`（多讀或一寫 —— 只有在讀取為主而且讀取很慢時
才划算），以及 `StampedLock`（多了樂觀讀模式；不可重入）。

**AQS**（`AbstractQueuedSynchronizer`）是 `ReentrantLock`、
`Semaphore`、`CountDownLatch` 與 `ReadWriteLock` 共同的引擎：一個用 CAS 操作的
`int` state，加上一條用 `LockSupport` park 起來的 FIFO 執行緒佇列。
知道這一個類別撐起了全部，是「`java.util.concurrent` 怎麼運作的？」的好答案。

---

<!-- 86a42924e768 -->
## 5) 樂觀鎖與悲觀鎖、CAS ⭐⭐⭐⭐

| | 悲觀鎖 Pessimistic | 樂觀鎖 Optimistic |
|---|-------------------|------------------|
| 假設 | 衝突很可能發生 | 衝突很少見 |
| 機制 | 先拿鎖（`synchronized`、`ReentrantLock`、`SELECT … FOR UPDATE`） | 先讀、算完再檢查並交換（CAS，或用 version 欄位） |
| 成本 | 阻塞、context switch | 有競爭時要重試 |
| 最適合 | 寫多、競爭激烈 | 讀多、競爭少 |

**CAS**（compare-and-swap）是單一個原子的 CPU 指令：*如果那個記憶體位置還是預期的值，
就換掉它*。atomic 類別就是靠它避開鎖的。

<!--CODE-->

面試時談 CAS 要講的兩件事：

- **ABA 問題**：在你讀取與交換之間，值可能 A → B → A 變回來，於是 CAS 成功了，
  但世界其實已經動過。用版本戳記來修 —— `AtomicStampedReference`。
- **自旋成本**：競爭很激烈時，執行緒會燒 CPU 一直重試。熱門計數器上 `LongAdder`
  勝過 `AtomicLong`，正是因為它把競爭分散到多個 cell，只在讀取時才加總。

Atomic 家族：`AtomicInteger/Long/Boolean/Reference`、`AtomicIntegerArray`、
`AtomicReferenceFieldUpdater`、`LongAdder`/`LongAccumulator`。

---

<!-- 09e9d3e5d1ff -->
## 6) 執行緒池 ⭐⭐⭐⭐⭐

每個任務開一條執行緒是撐不住的 —— 每條要約 1 MB 的 stack 加上一次系統呼叫。
池會重複使用執行緒，並給你一條佇列、一個上限與一個拒絕策略。

<!--CODE-->

**任務是怎麼被收下的** —— 這個順序常讓人意外：

<!--CODE-->

所以如果佇列是**無界**的，`maximumPoolSize` 永遠不會被用到，任務會一直堆到 heap
掛掉 —— 而 `Executors.newFixedThreadPool` 與 `newCachedThreadPool` 做的正是這件事
（分別是無界佇列／無界執行緒）。請自己用有界佇列建 `ThreadPoolExecutor`。

| 拒絕策略 | 行為 |
|------------------|-----------|
| `AbortPolicy`（預設） | 丟出 `RejectedExecutionException` |
| `CallerRunsPolicy` | 由送出任務的那條執行緒自己跑 —— 天然的背壓 |
| `DiscardPolicy` / `DiscardOldestPolicy` | 安靜地丟掉 —— 只適合可以掉的工作 |

**大小怎麼抓**：CPU 密集 → 大約 `核心數 + 1`。I/O 密集 →
`核心數 × (1 + 等待時間/服務時間)`；實務上要量測。而且永遠**幫執行緒命名** ——
正式環境的 stack trace 裡出現一個沒名字的 `pool-1-thread-7`，什麼也告訴不了你。

其他規則：關閉時先 `shutdown()` 再 `awaitTermination()`（`shutdownNow()` 會中斷並
回傳還沒跑的任務）；絕對不要在*同一個*有界池裡送出一個會等另一個任務的任務
（自我死鎖）；還要記得 `submit()` 的任務丟出的例外會被裝進 `Future`，
在你呼叫 `get()` 之前都是靜悄悄的。

---

<!-- 0c61be27089e -->
## 7) `CompletableFuture` ⭐⭐⭐⭐

`Future.get()` 會阻塞，那等於把非同步的意義丟掉。
`CompletableFuture` 改成用組合的。

<!--CODE-->

| 方法 | 做什麼 |
|--------|------|
| `supplyAsync` / `runAsync` | 啟動工作（記得傳**你自己的** executor —— 預設是共用的 ForkJoinPool） |
| `thenApply` / `thenCompose` | map／flatMap（函式本身回傳 future 時用 `thenCompose`） |
| `thenCombine` / `allOf` / `anyOf` | 匯流 |
| `exceptionally` / `handle` / `whenComplete` | 錯誤處理；`handle` 兩種結果都看得到 |
| `*Async` 版本 | 讓 callback 跑在 executor 上，而不是完成那一階段的執行緒上 |

兩個陷阱。第一，**`supplyAsync`/`runAsync` 以及每一個沒指定 executor 的 `*Async`
方法都用共用的 ForkJoinPool**（大小是 `核心數 − 1`，和 parallel stream 共用 ——
在那裡阻塞會把所有東西餓死）；而非 `Async` 的那些方法（`thenApply`、`thenCombine`……）
根本不挑 executor，會跑在完成上一階段的那條執行緒上，若上一階段早就完成了就跑在
呼叫端的執行緒上 —— 當那條執行緒是 Netty 的 event loop 時，這本身就是個驚喜。
第二，沒有掛上終端的 `exceptionally`/`whenComplete` 的話，未處理的例外是看不見的。

---

<!-- f507db343381 -->
## 8) 並發集合與協調工具 ⭐⭐⭐⭐

| 需求 | 用 | 不要用 |
|------|-----|-----|
| 共享 map | `ConcurrentHashMap`（CAS + 每個 bucket 各自 `synchronized`，讀取無鎖） | `Hashtable`、`Collections.synchronizedMap`（一把全域鎖） |
| 生產者／消費者交棒 | `ArrayBlockingQueue`，或**明確指定容量的** `LinkedBlockingQueue` —— 它的無參數版本容量是 `Integer.MAX_VALUE`，也就是會把 heap 塞爆而不是阻塞 | 一個 `List` 加上 `wait`/`notify` |
| 幾乎只讀的 list | `CopyOnWriteArrayList` | `synchronizedList` |
| 有競爭的計數器 | `LongAdder` | `AtomicLong`、`synchronized` |
| 有序的並發 map | `ConcurrentSkipListMap` | `TreeMap` + 鎖 |

`ConcurrentHashMap` 的原子輔助方法（`computeIfAbsent`、`merge`、`putIfAbsent`）
才是讓「先檢查再動作」安全的方式；`map.get(k)` 之後接 `map.put(k, v)` 就是競態，
不管那個 map 有多執行緒安全都一樣。另外注意它不接受 `null` 的 key 與 value，
這點和 `HashMap` 不同。

協調用的原語：

| 類別 | 用途 |
|-------|-----|
| `CountDownLatch` | 等 N 件事發生。**一次性** |
| `CyclicBarrier` | N 條執行緒會合後一起繼續。**可重複使用** |
| `Semaphore` | 限制同時存取某項資源的數量 |
| `Phaser` | 參與者數量可動態變化的 barrier |
| `Exchanger` | 兩條執行緒交換物件 |

更多實例 —— 加上執行緒池監控、優雅關閉與背壓 —— 在
[`../backend/be_programming_notes_pt2.md`](../backend/be_programming_notes_pt2.md)。

---

<!-- abd7670917a2 -->
## 9) `ThreadLocal` ⭐⭐⭐

不用在每個方法簽章裡傳來傳去，就能有每條執行緒各自的狀態：request id、使用者情境、
或像 `SimpleDateFormat` 這種不執行緒安全的輔助物件。

<!--CODE-->

每條執行緒持有一個 `ThreadLocalMap`，它的 **key 是對 `ThreadLocal` 物件的弱參考**，
但 **value 是強參考**。在執行緒池裡執行緒是永生的，所以一個你沒有 `remove()` 的值
就會洩漏 —— 更糟的是，下一個被這條執行緒服務的請求會讀到上一個請求的資料。

`InheritableThreadLocal` 會把值複製給子執行緒（但不會給池裡的執行緒）。
在虛擬執行緒下，「每條執行緒快取一份」失去了意義；*scoped value* 是它的後繼者。

---

<!-- 123a7ef2ea1d -->
## 10) 死鎖、活鎖與飢餓 ⭐⭐⭐⭐

<!--CODE-->

四個 Coffman 條件必須同時成立：互斥、持有並等待、不可搶占、
循環等待（見 [`../cs_basic.md`](../cs_basic.md)）。打破任何一個即可：

- **全域統一鎖的順序** —— 這是能擴展的修法。依一個*穩定、唯一*的次序排
  （帳號 id、建立時指派的單調序號）。`hashCode()` 不是全序：碰撞會讓兩把鎖無法比較，
  而且算在可變狀態上的雜湊還會變。如果不得不退回
  `System.identityHashCode`，就照《Java Concurrency in Practice》的做法，
  在取這一對鎖時再握一把第三方的「排序鎖」來打破平手。
- 一次把全部拿齊，或用 `tryLock(timeout)` 然後退讓。
- 縮小臨界區；更好的是避開共享的可變狀態，那就根本不需要鎖。

**活鎖**：執行緒不斷互相回應卻沒有進展（兩個人在走廊上一直互相讓路）——
加上隨機退避就能解。**飢餓**：某條執行緒始終拿不到資源；公平鎖或調整優先序會有幫助。

**在正式環境怎麼抓到它**：`jstack <pid>` 會印出一段
「Found one Java-level deadlock」，把兩條執行緒與兩個 monitor 都點名。
通常整個調查就到此為止。

---

<!-- 5ffbf184ac93 -->
## 11) 面試常見問答

**Q：`start()` 和 `run()` 差在哪？**
`start()` 是向 JVM 要一條新執行緒，由它去呼叫 `run()`。直接呼叫 `run()`
就只是在目前這條執行緒上執行它 —— 根本沒有並發。

**Q：`Runnable` 和 `Callable` 差在哪？**
`Callable<V>` 會回傳值，而且可以丟 checked 例外；`Runnable` 什麼都不回，也不能丟。
`ExecutorService.submit` 兩者都收，並回傳一個 `Future`。

**Q：為什麼 `volatile` 對計數器不夠？**
它給了可見性與順序性，但沒有原子性 —— `count++` 是讀-改-寫。
請用 `AtomicInteger` 或鎖。見 [`jmm.md`](./jmm.md)。

**Q：為什麼 `wait()` 一定要放在 `while` 迴圈裡？**
因為會有假喚醒（spurious wakeup），也因為在 `notify` 與你重新拿到鎖之間，
其他執行緒可能又把條件改掉了。醒來之後要重新檢查那個條件。

**Q：`notify()` 和 `notifyAll()` 差在哪？**
`notify()` 隨便叫醒一個等待者 —— 如果等待者們在等*不同*的條件，你可能叫醒錯的那個
而讓程式卡住。`notifyAll()` 是安全的預設；更精準的替代方案是在 `ReentrantLock` 上
用多個 `Condition`。

**Q：`ConcurrentHashMap` 是怎麼不鎖整個 map 就保持執行緒安全的？**
Java 7 用分段；Java 8 以後用 CAS 裝上 bucket 的頭節點，並對那個頭節點
`synchronized` 來做該 bucket 內的更新，所以只有打到同一個 bucket 的寫入才會競爭。
讀取則是無鎖的（對 table 做 `volatile` 讀取）。

**Q：怎麼讓一個類別變成執行緒安全？**
依優先順序：讓它**不可變**；把狀態限制在單一執行緒裡
（`ThreadLocal`、actor 風格的佇列）；委派給執行緒安全的集合；真的都不行了，
才用鎖守住可變狀態 —— 並且寫清楚*哪一把鎖*守*哪一個欄位*。

**Q：虛擬執行緒會改變這些嗎？**
它改變的是*成本模型*，不是正確性規則：競態、死鎖與可見性問題全都還在。
執行緒池不再是限制並發的手段，`Semaphore` 才是。見
[`java_modern.md`](./java_modern.md)。

---

<!-- 20bc65528a0b -->
## 12) 重點檢查表

<!--CODE-->

---

<!-- 297b6c48e940 -->
## 參考資料

- [`jmm.md`](./jmm.md) — happens-before、`volatile`、`final` 的語意
- [`java_collection.md`](./java_collection.md) — 集合的內部結構
- [`../backend/be_programming_notes_pt2.md`](../backend/be_programming_notes_pt2.md) — 上線形態的執行緒池、`CompletableFuture` 與協調
- [JavaGuide — concurrency questions](https://javaguide.cn/java/concurrent/java-concurrent-questions-01.html)
- 《Java Concurrency in Practice》，Goetz 等著
