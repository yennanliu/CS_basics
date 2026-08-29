# Priority Queue (PQ)（優先佇列）

> **範圍** — **僅作轉址。** 本檔已併入 [heap.md](./heap.md)，留著只是為了把舊連結導向正確的章節。
> **另見**：[heap.md](./heap.md) — 堆積與優先佇列的正式文件。

> **本檔已併入 [heap.md](./heap.md)。**
>
> 優先佇列是抽象資料型別，二元堆積是它的實作方式。
> 拆成兩份檔案的結果，就是同一批題目（LC 215、23、253、295、347、378、621、703、373）
> 被解了兩次，一種語言一次。現在 `heap.md` 兩邊都收。

## 東西搬去哪了

| 你原本在找的 | 現在在 |
|---|---|
| PQ 題型模式 1–8 | [heap.md → Problem Categories](./heap.md#problem-categories) |
| Java `PriorityQueue` 模板 1–12 | [heap.md → Java Template Library](./heap.md#java-template-library-priorityqueue) — 與 Python 模板並列 |
| Python `heapq` 模板 | [heap.md → Specific Pattern Templates](./heap.md#specific-pattern-templates) |
| `PriorityQueue` API／不彈出就取頂 | [heap_language_apis.md](./heap_language_apis.md#java-priorityqueue) — 完整 API 參考；[heap.md → Language APIs](./heap.md#language-apis) 保留一頁式表格 |
| 附 Java 解法的經典 LC 題 | [heap_examples.md → LC Examples](./heap_examples.md#lc-examples) |
| PQ 模式 → 題目對照 | [heap.md → Decision Table](./heap.md#decision-table--which-heap-pattern) |

## 另見

- [heap.md](./heap.md) — 堆積與優先佇列的正式文件
- [Dijkstra.md](./Dijkstra.md) — 以 PQ 為核心的最短路徑演算法
- [monotonic_queue.md](./monotonic_queue.md) — 滑動視窗極值上，雙端佇列勝過堆積的時機
- [streaming_algorithms.md](./streaming_algorithms.md) — 串流上的 top-k
