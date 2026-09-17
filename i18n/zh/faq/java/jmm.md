<!-- 2b5be70051e5 -->
# JMM FAQ（Java 記憶體模型）

> **範圍** — 一條執行緒寫的值什麼時候會被另一條看到：原子性、可見性、順序性、happens-before，以及 `volatile`、`synchronized`、`final` 各自保證了什麼。
> **另見**：[`java_multi_thread.md`](./java_multi_thread.md) — 套用這些規則的工具；[`jvm.md`](./jvm.md) — 記憶體*區域*（和這裡講的記憶體*模型*是兩件事）。

<!-- 43dec05c9cab -->
## 總覽

**Java 記憶體模型（JMM）**定義執行緒透過記憶體互動的規則 —— 具體說就是：一條執行緒
寫下的值*什麼時候*會被另一條執行緒的讀取看到，以及編譯器／CPU *可以*做哪些重排序。
它是一份抽象的契約，不是在描述實體的 RAM。

它為什麼存在：編譯器與 CPU 為了效能會重排指令，也會把值快取在暫存器或 CPU cache
裡。沒有規則的話，多執行緒程式在不同硬體上的行為就無法預測。

---

<!-- 7a4c975aa04a -->
## 1) 三個保證

| 性質 | 它回答的問題 | 什麼會破壞它 |
|----------|---------------------|-----------|
| **原子性** | 一個操作是全有全無嗎？ | `count++`（讀-改-寫其實是 3 步） |
| **可見性** | 執行緒 B 看得到執行緒 A 寫的值嗎？ | 值被各執行緒各自快取 |
| **順序性** | 操作看起來是按程式順序發生的嗎？ | 編譯器／CPU 重排序 |

- 除非加上 `volatile`，`long`/`double` 的讀寫在所有平台上都不保證是原子的
  （64 位元的值可能被拆成兩個 32 位元操作）。

---

<!-- 7ac0bb1946f3 -->
## 2) happens-before

JMM 的核心概念。如果動作 A **happens-before** 動作 B，那麼 A 的效果（記憶體寫入）
對 B 來說是**可見且有序**的。如果兩個動作之間沒有 happens-before 關係，JVM 就可以
自由重排它們。

主要的 happens-before 規則：
- **程式順序**：同一條執行緒內，每個動作 happens-before 下一個動作。
- **monitor 鎖**：解鎖某個 monitor happens-before 後續對同一個 monitor 的上鎖。
- **volatile**：對 volatile 欄位的寫入 happens-before 之後每一次對它的讀取。
- **執行緒啟動**：`Thread.start()` happens-before 被啟動的執行緒裡的任何動作。
- **執行緒 join**：某條執行緒裡的動作 happens-before 另一條執行緒從對它的
  `join()` 返回。
- **遞移性**：若 A hb B 且 B hb C，則 A hb C。

> happens-before 講的是**可見性與順序的保證**，不是實際時間先後。它不代表 A 在物理上
> 一定比 B 先執行。

---

<!-- e25425340302 -->
## 3) volatile

<!--CODE-->

它保證：
- **可見性**：寫入會刷回主記憶體；讀取一定拿到最新值。
- **順序性**：插入記憶體屏障，禁止跨越這次存取的重排序。

它**不**提供：
- **複合動作的原子性** —— `volatile int x; x++;` 一樣有競態。
  讀-改-寫要用 `synchronized` 或 `AtomicInteger`。

經典用法：**雙重檢查鎖（double-checked locking）**的單例需要 `volatile`，
才不會有人看到一個只建構到一半的物件：
<!--CODE-->

---

<!-- 44d9e615ebb0 -->
## 4) synchronized

- 同時提供**互斥**（臨界區的原子性）**和**可見性（monitor 的 release/acquire 語意）。
- 進入 `synchronized` 區塊就取得 monitor，離開時釋放；而釋放 happens-before 之後
  對同一個 monitor 的取得。
- 所以在同步區塊裡寫的變數，對下一條進入同一把鎖的執行緒是可見的 —— 這些變數
  不需要再加 `volatile`。

---

<!-- 7f2ddc34c723 -->
## 5) final 欄位的語意

- 一個正確建構完成的物件，它的 `final` 欄位保證對其他執行緒可見，**不需要額外同步**，
  前提是建構過程中 `this` 沒有外洩。
- 這就是為什麼不可變物件（所有欄位都是 `final`、`this` 沒有外洩）可以放心在
  執行緒之間共享。

---

<!-- 561420165532 -->
## 總結

| 工具 | 原子性 | 可見性 | 順序性 |
|------|-----------|-----------|----------|
| `volatile` | ✗（只保證單次讀或寫） | ✓ | ✓ |
| `synchronized` | ✓ | ✓ | ✓ |
| `final`（不可變） | ✓（值不會變） | ✓（安全建構之後） | ✓ |
| Atomic 類別 | ✓（CAS） | ✓ | ✓ |

**有競態的讀取不是「讀到稍微舊一點的值」，而是根本沒有順序保證。**Java 和 C/C++ 不同，
即使程式有競態仍然維持記憶體安全，並且明確定義 JMM 允許哪些結果；但少了 happens-before
這條邊，編譯器可以把讀取整個提到迴圈外面 —— 於是一條輪詢非 `volatile` 旗標的執行緒，
可能在寫入端早就設好值之後還無限轉圈。這就是「寧可用上面那四列之一，也不要靠運氣」
的理由。

---

<!-- 140fe2cb3596 -->
## 參考資料

- [JavaGuide — JMM](https://javaguide.cn/java/concurrent/jmm.html)
- [pdai — Java JVM JMM](https://pdai.tech/md/java/jvm/java-jvm-jmm.html)
- [`java_multi_thread.md`](./java_multi_thread.md) — 鎖、atomic、執行緒池
- [`jvm.md`](./jvm.md) — 執行期記憶體區域與 GC
