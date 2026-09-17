<!-- 3ff603a8f5e7 -->
# JVM FAQ

> **範圍** — JVM 怎麼存放、執行與回收：執行期記憶體區域、物件的建立、垃圾回收與各種收集器、參考強度、類別載入、JIT，以及診斷一台生病的 JVM 要用的旗標與工具。
> **另見**：[`jmm.md`](./jmm.md) — *記憶體模型*（跨執行緒的可見性與順序性，和下面的
> 記憶體*區域*是兩回事）；
> [`faq_performance_tune.md`](../faq_performance_tune.md) — 調校的方法論。

---

<!-- 80c5c3a898fc -->
## 1) 執行期記憶體區域 ⭐⭐⭐⭐⭐

<!--CODE-->

<p align="center"><img src="../../pic/jvm_storage_1.jpeg"></p>

| 區域 | 共享？ | 放什麼 | 失敗形式 |
|------|---------|-------|---------|
| PC 暫存器 | 每執行緒一份 | 目前指令的位址 | — |
| JVM stack | 每執行緒一份 | Stack frame | 某條執行緒的 stack 長不下去時是 `StackOverflowError`；`OutOfMemoryError: unable to create new native thread` 是*另一種*失敗 —— OS 拒絕再開執行緒（原生記憶體或 OS／cgroup 的執行緒上限），不是呼叫鏈太深 |
| 原生 stack | 每執行緒一份 | 原生 frame | 同上 |
| **Heap** | 共享 | 物件、陣列 | `OutOfMemoryError: Java heap space` |
| **Metaspace** | 共享 | 類別中繼資料、static、常數池 | `OutOfMemoryError: Metaspace` |
| 直接記憶體 | 共享 | `ByteBuffer.allocateDirect`、NIO | `OutOfMemoryError: Direct buffer memory` |

**Stack 與 heap** —— 大部分記憶體問題背後其實在問這個：

| | Stack | Heap |
|---|-------|------|
| 範圍 | 一條執行緒、一個 frame | 整個 JVM |
| 生命週期 | 方法返回就彈掉 | 到不可達為止，然後由 GC 回收 |
| 內容 | primitive 與**參考** | 那些參考指向的物件 |
| 大小 | 小（`-Xss`，約 512 KB–1 MB） | 大（`-Xms`/`-Xmx`） |
| 由誰管理 | push/pop，自動 | 垃圾收集器 |

**Java 8 拿掉了 PermGen。**類別中繼資料搬到 **Metaspace**，它住在原生記憶體裡、
按需成長（上限由 `-XX:MaxMetaspaceSize` 決定），所以反覆重新部署 web 應用造成的
經典 `OutOfMemoryError: PermGen space` 不見了 ——
不過 classloader 洩漏現在改成把原生記憶體吃光。

---

<!-- 9562a878fd02 -->
## 2) 一個物件是怎麼被建立的 ⭐⭐⭐

`new Foo()` 會跑這幾步：

1. **解析**常數池裡的符號參考；如果類別還沒被載入／連結／初始化就先做（見 §6）。
2. **配置**記憶體。可能是*指標碰撞*（會壓實的收集器讓 heap 保持連續），
   也可能是從*空閒串列*拿。並發由每條執行緒各自的配置緩衝（**TLAB**）處理，
   所以配置就是移一下指標，不用鎖。
3. **歸零**那塊記憶體 —— 這就是為什麼欄位有預設值（`0`、`false`、`null`）。
4. **設定物件標頭**：mark word（雜湊、GC 年齡、鎖狀態）與類別指標。
5. **執行 `<init>`**：在 `super()` 之後，跑欄位初始化與建構子本體。

<p align="center"><img src="../../pic/new_class_step.jpeg"></p>

所以一個物件就是*標頭 + 實例欄位 + 填充*（對齊到 8 位元組）。
`-XX:+UseCompressedOops`（heap 小於 32 GB 時的預設）把 64 位元參考存成 32 位元
位移，縮小物件大小與快取壓力 —— 這也是為什麼 31 GB 的 heap 裝得下的物件，
可能比 33 GB 的還多。

**逃逸分析**可能證明某個物件從未離開它的方法，於是整個配置就被消掉
（純量替換），或把它的鎖省略掉。

---

<!-- 34f6b9d0ccee -->
## 3) 垃圾回收 ⭐⭐⭐⭐⭐

<p align="center"><img src="../../pic/gc1.png"></p>

<!-- d86b9ee5197a -->
### 什麼是垃圾？

不是「沒有被參考」，而是**不可達**。JVM 從 **GC root**（stack 上的區域變數、
static 欄位、JNI 參考、活著的 monitor）開始走訪；走不到的就可以回收。

Java *不用*參考計數：它收不掉循環（`a.b = b; b.a = a`），
這正是 Java 改用追蹤式回收的原因。

<!-- 47604c405a41 -->
### 三種基本演算法

| 演算法 | 做法 | 優點 | 缺點 |
|-----------|-----|------|------|
| **標記-清除 Mark-Sweep** | 標出活的，其餘就地釋放 | 簡單、不搬動物件 | 會**碎片化**；要跑兩趟 |
| **標記-整理 Mark-Compact** | 標記後把存活者滑到一起 | 不碎片化、配置快 | 搬動物件要花時間 |
| **複製 Copying** | 把存活者複製到空的另一半 | 存活者少時非常快，不碎片化 | 浪費一半空間 |

<p align="center"><img src="../../pic/mark_sweep.png"></p>
<p align="center"><img src="../../pic/mark_compact.png"></p>
<p align="center"><img src="../../pic/mark_copy.png"></p>

<!-- b68e0d96db78 -->
### 分代回收

兩個經驗事實撐起了這個設計：**大多數物件都早夭**，而且**很少有老物件參考年輕物件**。
於是 heap 被切開，每一塊各用最適合它的演算法 —— 新生代用複製（存活者少），
老年代用整理。

<!--CODE-->

1. 配置發生在 **Eden**（在 TLAB 裡）。
2. Eden 滿了 → **minor GC**：存活物件被複製到空的 survivor 區，年齡全部 +1；
   Eden 與另一個 survivor 整片清掉。
3. 物件夠老之後就被**晉升**到老年代 —— 撐過 `-XX:MaxTenuringThreshold` 次回收
   （15 是*上限*，收集器會自適應地調低），或是 survivor 區裝不下就提早晉升。
   大到 Eden 放不下的物件會直接配置在老年代／humongous region。
4. 老年代滿了 → **major / full GC**，那就貴多了。

<!-- 5718d0235f47 -->
### Stop-the-world

為了在物件圖不被改動的情況下觀察它，收集器會把應用執行緒帶到一個
**safepoint**。差別在於那段停頓裡*做了多少事*。Serial 與 Parallel 全都在裡面做完。
G1 讓好幾個階段並行，但疏散（evacuation）仍然在停頓裡做。ZGC 與 Shenandoah 靠
load barrier 把標記、參考處理與搬遷都做成並行的，只留下固定成本的短停頓
（而 Epsilon 根本不回收）。

停頓是 p99 延遲尖峰的經典成因 —— 吞吐量看起來好好的，但每一千個請求就有一個
在等回收 —— 不過它不是唯一成因，所以怪 GC 之前先讀 GC log（§8）。

<!-- a043493ce327 -->
### 各種收集器

| 收集器 | 新生代／老年代 | 旗標 | 特性 |
|-----------|-------------|------|------|
| **Serial** | 複製／標記-整理 | `-XX:+UseSerialGC` | 單執行緒、全程 STW。小 heap、只有 1 顆 CPU 的容器 |
| **Parallel（吞吐量）** | 複製／標記-整理 | `-XX:+UseParallelGC` | 多執行緒 STW。吞吐量最大，但停頓沒有上限。Java 8 之前的預設 |
| **CMS**（14 移除） | 複製／並行標記-清除 | `-XX:+UseConcMarkSweepGC` | 歷史上的低停頓選項；會碎片化，需要 fallback 的 full GC |
| **G1** | 以 region 為單位，兩代都管 | `-XX:+UseG1GC` | **Java 9 起的預設。**heap 切成等大的 region（`G1HeapRegionSize`，由 ergonomics 在 1–32 MB 間挑一個 2 的次方，目標約 2048 個 region）；優先收垃圾最多的 region（garbage first）以命中停頓目標（`-XX:MaxGCPauseMillis=200`） |
| **ZGC** | 以 region 為單位，並行 | `-XX:+UseZGC` | 多 TB heap 上仍是次毫秒停頓；幾乎全部並行（著色指標、load barrier） |
| **Shenandoah** | 以 region 為單位，並行 | `-XX:+UseShenandoahGC` | 目標和 ZGC 一樣，靠 Brooks 指標做並行壓實 |
| **Epsilon** | 沒有 | `-XX:+UseEpsilonGC` | 什麼都不收，用來做基準測試 |

怎麼選：批次／吞吐量導向的工作 → Parallel。一般服務 → G1（除非停頓目標沒達到，
否則別去動它）。大 heap 又有嚴格延遲 SLO → ZGC/Shenandoah。

> `System.gc()` 只是*建議*做一次 full GC。多數正式環境會用
> `-XX:+DisableExplicitGC` 把它關掉 —— 應用程式碼裡永遠不要呼叫它。

---

<!-- 1d42c198d77e -->
## 4) 參考強度 ⭐⭐⭐

| 型別 | 什麼時候可以被清掉 | 用途 |
|------|---------------------------|-----|
| **強參考** `Object o = new Object()` | 只要還可達就永遠不會 | 一般程式 |
| **軟參考** `SoftReference<T>` | 只剩軟可達*而且*收集器認為記憶體壓力大到該清了 —— 時機完全由它決定 | 對記憶體敏感的快取 |
| **弱參考** `WeakReference<T>` | 只剩弱可達（沒有強／軟路徑）。會被*某一次*後續的 GC 清掉，不保證是下一次 | `WeakHashMap`、正規化映射、listener 註冊表 —— 那些不該讓值活著的 key |
| **虛參考** `PhantomReference<T>` | 虛可達，在 finalize 之後；`get()` 永遠回 `null` | 透過 `ReferenceQueue` 做善後清理（`Cleaner` 就是用它取代 `finalize`） |

這些都不是對時程的承諾：可達性只決定一個 referent *有沒有資格*被清，
收集器則在下次跑到它時才真的清。

---

<!-- 6863e6b54e93 -->
## 5) Java 的記憶體洩漏 ⭐⭐⭐⭐

有 GC 的語言一樣會洩漏 —— 洩漏就是**一個一直可達、但再也不會被用到的物件**。
常見來源：

| 洩漏 | 它為什麼抓著不放 |
|------|-----------------|
| 拿 `static` 集合當快取 | static 欄位是 GC root，活得和 classloader 一樣久 |
| 沒移除的 listener／callback | 發布者對每個訂閱者都握著強參考 |
| 池化執行緒裡的 `ThreadLocal` | 執行緒活得比請求久；一定要在 `finally` 裡 `remove()` |
| 沒關的串流、連線、`ExecutorService` | 原生 handle 與執行緒讓物件活著 |
| 插入後 `hashCode` 才變的 key | 那筆項目再也找不到，也永遠移不掉 |
| 對超大字串做 `substring`（Java 7u6 之前） | 子字串共用了母字串的 char 陣列 |
| 應用伺服器裡的 classloader 洩漏 | 一個被抓住的類別就留住整個 classloader —— 以及它載入過的每一個類別 |

**怎麼診斷**：盯著 full GC 之後老年代的佔用量（`jstat -gcutil`）。如果它一路往上、
再也回不到基準線，就抓一份 heap dump（`jmap -dump:live,format=b,file=heap.hprof <pid>`
或 `-XX:+HeapDumpOnOutOfMemoryError`）丟進 **Eclipse MAT** ——
它的「dominator tree」與「leak suspects」報告會直接把抓著不放的 root 點名。

---

<!-- e1fa4c3de3be -->
## 6) 類別載入 ⭐⭐⭐⭐

<!-- 402e32ea16a2 -->
### 生命週期

<!--CODE-->

- **載入 Load**：讀進 `.class` 位元組，建立執行期表示，產生 `Class<?>` 物件。
- **驗證 Verify**：擋掉會弄壞 JVM 的 bytecode（錯誤的 stack map、非法轉型）。
- **準備 Prepare**：配置 static 欄位，並設成**預設**值（還不是初始化子寫的值）。
- **解析 Resolve**：把符號參考換成直接參考（可以延遲做）。
- **初始化 Initialise**：在父類別初始化之後，依原始碼順序執行 `<clinit>` ——
  static 初始化區塊與 static 欄位的指派。它由第一次**主動使用**延遲觸發
  （JLS §12.4.1）：`new`、呼叫 static 方法、讀或寫 static 欄位、執行該類別的
  `main`，或是會要求初始化的反射呼叫（`Class.forName(name)` 會；
  `Class.forName(name, false, loader)` 與 `getDeclaredMethod` 不會）。兩個值得知道的
  例外：讀取**編譯期常數**（`static final int X = 42`）會被編譯器內聯，
  什麼都不會初始化；而透過只宣告在父類別的*欄位*去碰子類別，並不會初始化子類別。

<p align="center"><img src="../../pic/class_load_step.jpeg"></p>

一個類別只有在它的 classloader 變成不可達時才會被**卸載** —— 這就是為什麼
一個被留住的 classloader 會讓它載入過的每一個類別都活著（§5）。

<p align="center"><img src="../../pic/class_life_cycle.jpeg"></p>

<!-- 5efbaa4a964c -->
### Loader 階層與雙親委派

| Loader | 載入什麼 |
|--------|-------|
| **Bootstrap**（原生，沒有 Java 物件） | JDK 核心類別（`java.*`） |
| **Platform / Extension** | 核心之外的 JDK 模組 |
| **Application（System）** | 你的 classpath／modulepath |
| **自訂** | 外掛、熱更新、應用伺服器的隔離、加密或動態生成的 bytecode |

**雙親委派**：一個 loader 在自己動手之前會先問它的父 loader。所以你自己寫一個叫
`java.lang.String` 的類別永遠蓋不掉真的那個 —— 這既是安全性，也是唯一性的保證。
需要隔離的框架（Tomcat 每個 webapp 一個 loader、OSGi）會刻意打破委派。

**類別的身分是（loader, 名稱）**：同一份 bytecode 被兩個 loader 載入就是兩個不同的
類別，互相轉型會丟 `ClassCastException`。這個驚喜是大多數「不可能發生」的類別載入
bug 的根源。

`ClassLoader.loadClass()` 實作委派，`findClass()` 是自訂 loader 要覆寫的那個，
`defineClass()` 則把位元組變成 `Class`。

<p align="center"><img src="../../pic/classloader1.png"></p>

---

<!-- 8872eb8a79b1 -->
## 7) 執行：直譯器、JIT 與 AOT ⭐⭐⭐

JVM 一開始**直譯** bytecode，同時一路收集剖析資料。熱門的方法與迴圈會被 **JIT**
編譯成原生碼（分層：C1 編得快、最佳化較輕，C2 再把最熱的程式碼積極重編）。
因為 JIT 手上有執行期的剖析資料，它做得到靜態編譯器做不到的事：對觀察到是單型的
虛擬呼叫做內聯、展開迴圈、消掉邊界檢查、做逃逸分析，並在假設被打破時
**去最佳化**回到直譯器。

值得知道的後果：

- **暖機很重要**：一個基準測試的前一千次迭代量到的是直譯器。請用 JMH，
  不要自己手寫 `System.nanoTime()` 迴圈。
- 編出來的程式碼住在 **code cache**（`-XX:ReservedCodeCacheSize`）；
  把它用光，JVM 會靜悄悄地退回直譯。
- `-XX:+PrintCompilation` 與 JITWatch 可以看到什麼被編譯、什麼被內聯。
- **CDS**（`-XX:SharedArchiveFile`，以及 AppCDS）把預先解析好的類別中繼資料封存檔
  記憶體映射進來，讓啟動時省下重複的類別載入。它仍然是一般的 JVM，仍然有 JIT ——
  對峰值吞吐量幾乎沒有代價。
- **AOT 原生映像**（GraalVM `native-image`）把整個應用預先編成原生可執行檔：
  毫秒級啟動、佔用很小，但沒有 JIT 靠剖析導引達到的峰值吞吐量，
  而且反射與動態代理必須在建置期就宣告好。這就是為什麼 serverless 的 Java 往原生
  映像靠，而長跑的服務通常不會。

**JRE / JDK / JIT**：JRE 是執行環境（JVM + 核心函式庫），JDK 是 JRE 再加上開發工具
（`javac`、`jstack`、`jmap`），而 JIT 是 JVM *裡面*的編譯器。Java 11 起沒有獨立的
JRE 可下載 —— 要的話用 `jlink` 自己做一個。

---

<!-- 45956bae264c -->
## 8) 旗標、工具與診斷 ⭐⭐⭐⭐

<!-- 1905cef608f5 -->
### 你真的會設的旗標

| 旗標 | 意義 |
|------|---------|
| `-Xms` / `-Xmx` | 初始／最大 heap。正式環境請**設成一樣**，避免調整大小造成的停頓 |
| `-Xss` | 執行緒 stack 大小 |
| `-XX:MaxMetaspaceSize` | 類別中繼資料的上限 |
| `-XX:+UseG1GC` / `-XX:+UseZGC` | 選收集器 |
| `-XX:MaxGCPauseMillis` | G1 的停頓目標 |
| `-XX:NewRatio` / `-XX:SurvivorRatio` | 新生代:老年代、Eden:survivor 的比例（用 G1 時很少需要動） |
| `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=…` | **永遠打開。**OOM 之後那份 dump 是唯一的證據 |
| `-Xlog:gc*:file=gc.log:time,uptime` | 統一 GC log（9+；更早是 `-XX:+PrintGCDetails`） |
| `-XX:+UseContainerSupport` `-XX:MaxRAMPercentage=75` | 依**容器**的限制決定 heap 大小，而不是依主機的 RAM |

<!-- f9904ba1bef0 -->
### 命令列工具（全都隨 JDK 附上）

| 工具 | 回答什麼 |
|------|---------|
| `jps` | 有哪些 JVM 在跑，以及它們的 PID |
| `jstat -gcutil <pid> 1s` | 即時的 GC：各代佔用率、回收次數與時間 |
| `jmap -histo:live <pid>` / `-dump:...` | heap 上有什麼／完整 dump |
| `jstack <pid>` | Thread dump —— 卡住與死鎖時**就是**這個工具（它會直接把死鎖點名） |
| `jcmd <pid> <command>` | 現代的超集合：`GC.heap_info`、`Thread.print`、`VM.flags`、`JFR.start` |
| `jinfo` | 執行期讀取／修改可管理的旗標 |
| **JFR + JMC** | 低開銷、可以常時開著的正式環境剖析器（`-XX:StartFlightRecording`） |
| **VisualVM**、**MAT**、**async-profiler** | 圖形化監控、heap dump 分析、CPU／配置火焰圖 |

<!-- 355a857afc7a -->
### 從程式碼讀 runtime 的狀態

<!--CODE-->

<!-- ce0504fa637c -->
### 分診速查表

| 症狀 | 第一步做什麼 |
|---------|-----------|
| `OutOfMemoryError: Java heap space` | Heap dump → MAT 的 dominator tree。是洩漏，還是真的開太小？ |
| `OutOfMemoryError: Metaspace` | 數一下載入的類別數（`jcmd GC.class_stats`）；懷疑 classloader 洩漏，或某個代理／bytecode 生成器 |
| `OutOfMemoryError: unable to create native thread` | 執行緒洩漏 —— 用 `jstack` 數一數；檢查 `-Xss` 與 OS 上限 |
| `StackOverflowError` | 沒有終止條件的遞迴（看 trace 裡重複的那幾個 frame） |
| 延遲有尖峰、CPU 正常 | GC log → 看停頓分布；是收集器選錯，還是停頓目標設錯？ |
| CPU 很高 | `top -H -p <pid>` → 把執行緒 id 轉成十六進位 → 在 `jstack` 裡找它；或用 async-profiler |
| 應用卡住 | 相隔 10 秒抓兩次 `jstack`；看 `BLOCKED` 的執行緒與死鎖那一段 |

---

<!-- 9669fc9fa144 -->
## 9) 面試常見問答

**Q：物件住在哪 —— 一定是 heap 嗎？**
幾乎都是，但逃逸分析可以讓 JIT 把證明不會逃出方法的物件配置在 stack 上，
或做純量替換。

**Q：64 位元 JVM 上 `int` 多大？**
32 位元。Java 的 primitive 寬度由語言規格固定，不看平台。會變的是**參考**的寬度
（以及壓縮 oop）。

**Q：minor、major、full GC 差在哪？**
Minor 只收新生代（頻繁、便宜）。Major 收老年代。Full 連 Metaspace 都收，最貴。

**Q：Java 會有記憶體洩漏嗎？**
會 —— 見 §5。GC 釋放的是*不可達*的物件，不是*沒在用*的物件。

**Q：什麼是 safepoint？**
一個執行緒狀態對 JVM 而言是已知的位置，於是可以為了 GC、thread dump 或去最佳化
把它暫停。「到 safepoint 的時間」本身也可能是延遲來源 —— 一個長的計數迴圈
可能根本沒去輪詢 safepoint。

**Q：PermGen 為什麼變成 Metaspace？**
PermGen 是固定大小的 heap 區域，難抓大小，而且重新部署時老是 OOM。
Metaspace 用原生記憶體，而且按需成長。

**Q：反射的成本是什麼？**
它繞過編譯期檢查、擋掉一些內聯，而且每次呼叫本來有實際開銷
（現代 JIT 與 method handle 已經大幅降低）。框架會快取 `Method` 物件、
或生成 bytecode，讓熱路徑上避開它。

**Q：一個服務每幾分鐘就停 2 秒，你會怎麼查？**
先打開 GC log，把停頓和 full GC 對起來；如果 GC 是無辜的，檢查 safepoint 時間
（`-Xlog:safepoint`），接著往 JVM 外面看（page cache、容器裡的 CPU throttling、
某個會 stop-the-world 的依賴）。

---

<!-- 51c56a91eed5 -->
## 10) 重點檢查表

<!--CODE-->

---

<!-- a549caf5d5ae -->
## 參考資料

- [`jmm.md`](./jmm.md) — Java 記憶體模型（可見性、順序性、happens-before）
- [`java_multi_thread.md`](./java_multi_thread.md) — 執行緒、鎖與執行緒池
- [JavaGuide — JVM memory areas](https://javaguide.cn/java/jvm/memory-area.html)
- [Oracle — HotSpot GC tuning guide](https://docs.oracle.com/en/java/javase/21/gctuning/)
- [Baeldung — Java classloaders](https://www.baeldung.com/java-classloaders)
