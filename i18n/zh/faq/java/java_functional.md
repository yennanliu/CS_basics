<!-- 50cbd6f035ac -->
# Java 函數式程式設計 FAQ（Lambda、Stream、Optional）

> **範圍** — Java 8 的函數式工具箱：lambda、方法參考、內建的函數式介面、Stream API、collector，以及 `Optional`。
> **另見**：[`java_modern.md`](./java_modern.md) — Java 9–21 的語言功能；
> [`java_collection.md`](./java_collection.md) — stream 跑在上面的那些集合。

Stream 是 Java 8 最常被用到的功能，也是面試官探得最深的一個 —— 因為誤用（副作用、
在不對的工作負載上用 `parallelStream()`、明明 `for` 迴圈比較清楚卻硬要用 stream）
一眼就看得出來。

---

<!-- 7f6d4ed3c5d8 -->
## 1) Lambda 與函數式介面 ⭐⭐⭐⭐⭐

**函數式介面**是剛好只有一個抽象方法（SAM）的介面。lambda 就是它的一個實作。

<!--CODE-->

<!-- e2b46df14912 -->
### 該認得的內建介面

| 介面 | 簽章 | 典型用途 |
|-----------|-----------|-------------|
| `Function<T,R>` | `R apply(T)` | `map` |
| `BiFunction<T,U,R>` | `R apply(T,U)` | `merge`、`reduce` |
| `Predicate<T>` | `boolean test(T)` | `filter`、`removeIf` |
| `Consumer<T>` | `void accept(T)` | `forEach` |
| `Supplier<T>` | `T get()` | 延遲取得預設值、`orElseGet` |
| `UnaryOperator<T>` | `T apply(T)` | `replaceAll` |
| `BinaryOperator<T>` | `T apply(T,T)` | `reduce`、`Collectors.toMap` 的合併函式 |

primitive 版本（`IntPredicate`、`ToIntFunction`、`IntUnaryOperator`……）存在的唯一
理由就是避免裝箱 —— 熱路徑上請用它們。

<!-- a44ae77ad592 -->
### 方法參考

四種形式，全都是 lambda 的語法糖：

<!--CODE-->

<!-- ee6cb3adda58 -->
### Lambda 與匿名類別

| | Lambda | 匿名類別 |
|---|--------|-----------------|
| `this` | 指向**外層**實例 | 指向匿名類別自己的實例 |
| 編譯成 | `invokedynamic`（不會每個實例多一個 class 檔） | 真的產生一個 `Outer$1.class` |
| 能宣告欄位嗎 | 不行 —— 不過有捕獲的 lambda 會帶著捕獲到的值，也能改動那些值指向的物件 | 可以 |
| 目標型別 | 只能是函數式介面 | 任何介面／抽象類別 |

兩者都只能捕獲**實質 final（effectively final）**的區域變數 —— lambda 用到的區域變數
不能再被指派，因為被捕獲的是值，不是變數。

---

<!-- f7d4396f147f -->
## 2) Stream：管線 ⭐⭐⭐⭐⭐

Stream **不是**資料結構。它是跑在某個來源上的一次性管線，由**延遲的中間操作**與
一個**急切的終端操作**組成。

<!--CODE-->

**在終端操作出現之前什麼都不會執行**，而且元素是一個一個流過整條管線的（融合），
所以 `filter → map → findFirst` 可能只碰到一個元素。

| 類別 | 操作 |
|------|------|
| 中間（無狀態） | `filter`、`map`、`flatMap`、`peek`、`mapToInt` |
| 中間（有狀態） | `sorted`、`distinct`、`limit`、`skip` —— 可能要緩衝整條 stream |
| 終端 | `collect`、`forEach`、`reduce`、`count`、`min/max`、`anyMatch`、`findFirst`、`toArray` |

- 一條 stream 只能被消費**一次**：重複使用會丟 `IllegalStateException`。
- `map` 是一個變一個；**`flatMap` 則把一個攤平成很多個**：

<!--CODE-->

<!-- 5ac2f7eea500 -->
### Reduce

<!--CODE-->

累加函式必須**符合結合律且沒有副作用**，否則平行的結果會和循序的不一樣。
處理 primitive 時優先用 `IntStream.sum()` / `average()` /
`summaryStatistics()` —— 不用裝箱，而且 `average()` 回傳 `OptionalDouble`，
因為空的 stream 沒有平均值。

---

<!-- 24a9cd80b903 -->
## 3) Collector ⭐⭐⭐⭐

<!--CODE-->

兩個一直有人踩的陷阱：

- **key 重複時 `toMap` 會丟 `IllegalStateException`。**要傳合併函式：
  `toMap(Employee::id, e -> e, (a, b) -> a)`。
- **值是 null 時 `toMap` 會丟 `NullPointerException`**（和 `HashMap.put` 不一樣）。
  先把 null 濾掉，或自己收進一個 `HashMap`。
- `Collectors.toList()` 對回傳清單的型別與可變性**什麼都沒保證**（今天它是
  `ArrayList`，但別依賴這件事）。需要可變清單用 `toCollection(ArrayList::new)`，
  要明確**不可修改**的就用 `stream.toList()`（16+）。

`teeing`（12+）讓兩個 collector 在同一趟裡跑完；`mapping` / `filtering` /
`flatMapping` 則可以當成 `groupingBy` 裡的下游 collector 組合起來。

---

<!-- a7b778046d6e -->
## 4) 平行 Stream ⭐⭐⭐⭐

`list.parallelStream()` 會把來源切開，丟到**共用的 ForkJoinPool**
（`核心數 − 1` 個 worker，整個 JVM 共用）。

只有以下條件**全部**成立時才用它：

1. 每個元素的工作真的很吃 CPU（幾千奈秒等級），而且元素很多。
2. 來源切得便宜 —— `ArrayList`、陣列、`IntStream.range`。`LinkedList`
   或以 `Iterator` 為來源的切得很差。
3. 那些 lambda 是**無狀態、無副作用、符合結合律**的。
4. 管線裡沒有阻塞 I/O —— 阻塞會榨乾共用的池，甚至讓應用裡毫不相干的程式碼卡住。

<!--CODE-->

反模式：`parallelStream().forEach(list::add)`（沒同步的修改）、
對順序敏感的管線（`forEachOrdered` 會重新序列化，把好處還回去），
以及把任何會打資料庫的東西平行化。

---

<!-- 10dd12cd43c0 -->
## 5) Optional ⭐⭐⭐⭐

`Optional<T>` 是在**回傳型別**上表達「這個東西合理地可能不存在」。它不是一個
通用的 null 包裝器。

<!--CODE-->

| 該做 | 不該做 |
|----|-------|
| 從可能查不到東西的查找回傳 `Optional` | 拿它當欄位、參數或集合（集合請回傳空集合） |
| 預設值很貴時用 `orElseGet(this::expensive)` | `orElse(expensive())` —— 那個參數**即使有值也會被算出來** |
| `orElseThrow(...)` | 沒先 `isPresent()` 就 `get()` —— 同樣的 NPE，只是多繞了幾步 |
| `map` / `flatMap` 串起來 | `if (o.isPresent()) { o.get() … }`，那只是換個樣子的 null 檢查 |

`Optional` 不是 `Serializable`，而且在熱迴圈裡包一層每次呼叫都多一次配置 ——
這就是為什麼 entity 的欄位維持原樣。

---

<!-- 43a047cd1e30 -->
## 6) Stream 還是迴圈 —— 什麼時候不要用 Stream

以下情況請用迴圈：主體會改動外部狀態、需要帶副作用的 `break`、要用 checked
例外（lambda 丟不出來），或索引很重要。stream 真正划算的時候，是整條管線讀起來像
在講**你要什麼**而不是**怎麼拿到**，尤其是 `groupBy` 形狀的彙總。

<!--CODE-->

lambda 能丟出它的**目標型別**宣告的任何例外 —— `Callable.call()` 允許任何 checked
例外。問題在於 Stream API 用的那些介面（`Function`、`Predicate`、`Consumer`……）
一個都沒宣告，所以在 stream 裡面 checked 例外必須被包起來：
`.map(f -> { try { return parse(f); } catch (IOException e) { throw new UncheckedIOException(e); } })`。

---

<!-- dc3d03381495 -->
## 7) 面試常見問答

**Q：Stream 比迴圈快嗎？**
單純的工作通常不會 —— stream 多了管線的開銷。它贏在可讀性，以及在可切分的來源上做
CPU 密集工作時的平行版本。要量測，不要想當然。

**Q：延遲求值（lazy evaluation）換到什麼？**
短路（`findFirst`、`anyMatch`、`limit`）與單趟融合：各階段之間不產生中間集合。

**Q：`map` 和 `flatMap` 差在哪？**
`map` 是 1→1；`flatMap` 是 1→多，然後攤平成一條 stream。

**Q：lambda 可以修改區域變數嗎？**
不行 —— 被捕獲的區域變數必須是實質 final。需要計數就用 `AtomicInteger`、陣列，
或改成正規的 reduction，不要用可變計數器。

**Q：這裡的 `invokedynamic` 在做什麼？**
編譯器產生一個呼叫點，由 JVM 在執行期把它連結到動態生成的 lambda 實作，
而不是為每個 lambda 產生一個匿名類別 —— class 檔更少，內聯也更好。

**Q：對無窮 stream 用 `Stream.iterate` 可以嗎？**
只要後面有東西把它短路掉就合法：
`Stream.iterate(1, x -> x * 2).limit(10)`。

**Q：Stream 可以重複使用嗎？**
不行。從來源重新建一條，或收集一次之後重複使用那個集合。

---

<!-- d53e2f2ac0ca -->
## 8) 重點檢查表

<!--CODE-->

---

<!-- 15a5daa2c350 -->
## 參考資料

- [Java SE — the Stream package summary](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)
- [`java_modern.md`](./java_modern.md) — record、sealed 型別、虛擬執行緒
- [`java_collection.md`](./java_collection.md) — stream 背後的那些集合
