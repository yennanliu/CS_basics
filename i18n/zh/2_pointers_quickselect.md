<!-- ff0f71084ede -->
# QuickSelect（已移到 quickselect.md）

> **範圍** — **僅作轉址。** 這份文件在 2026 年 11 月改名為 [quickselect.md](./quickselect.md)，留著只是為了把舊連結導向正確的章節。
> **另見**：[quickselect.md](./quickselect.md) — 以分割做選擇的正式文件。

> **這份文件現在是 [quickselect.md](./quickselect.md)。**
>
> Quickselect 是選擇演算法，只是剛好用到雙指標式的分割，所以 `2_pointers_` 這個前綴把它歸錯了主題。
> 內容實質上原封不動搬過去，改寫成模式卡。

<!-- ae097cd5a98e -->
## 東西搬去哪了

| 你原本在找的 | 現在在 |
|---|---|
| 模板 1，第 k 大元素 | [quickselect.md → 模板 1：三向分割 Quickselect](./quickselect.md#template-1-three-way-partition-quickselect--lc-215-) |
| 模板 2，最接近的 k 個點 | [quickselect.md → 模板 2：以衍生鍵做 Quickselect](./quickselect.md#template-2-quickselect-on-a-derived-key--lc-973-) |
| 視覺化範例與隨機 pivot | [quickselect.md → 模板 1](./quickselect.md#template-1-three-way-partition-quickselect--lc-215-)，它的試跑與陷阱 |
| Hoare、Lomuto 與三向分割 | [quickselect.md → 分割方式](./quickselect.md#partition-schemes--lomuto-hoare-and-three-way-) |
| Median of medians | [quickselect.md → Median of Medians](./quickselect.md#median-of-medians--where-a-linear-worst-case-comes-from-) — 現在可以執行了 |
| 其他 quickselect 題目、效能比較 | [quickselect.md → 同一副骨架的其他題目](./quickselect.md#other-problems-on-the-same-skeleton) 與 [Quickselect、堆積與排序的比較](./quickselect.md#quickselect-vs-heap-vs-sort) |

<!-- c0f5615e04dc -->
## 另見

- [quickselect.md](./quickselect.md) — quickselect 的正式文件
- [heap.md](./heap.md) — 大小為 k 的堆積替代方案
