<!-- 4e4ca64ac805 -->
# Java 字串與排序速查表

> **範圍** — Java 的 String 與 StringBuilder 操作 — 與 `char[]` 互轉、切片、解析、組字串與修改 — 加上所有跟 comparator 有關的東西：陣列、集合與 map 的排序，以及決定順序的回傳值規則。
> **另見**：[java_trick.md](./java_trick.md) — 為什麼 `charAt` 回傳的是數字，以及這些呼叫背後的其他語言語意；[java_trick_collections.md](./java_trick_collections.md) — 被排序的那些容器；[sort.md](./sort.md) — 把排序當演算法而不是當 API 來看；[string.md](./string.md) — 字串演算法而不是字串處理。

<!-- efa5f225c4c6 -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)
- [Sorting](https://leetcode.com/problem-list/sorting/)

<!-- d0102ec65425 -->
## 總覽

從 [java_trick.md](./java_trick.md) 拆出來的。字串與排序會共用一份文件，是因為在 Java 裡它們共用同一種踩雷方式：**`String` 是不可變的，而 `Arrays.sort` 對基本型別不吃 comparator**，所以兩邊都會逼你先做一次轉換（`toCharArray`、`Integer[]`、`StringBuilder`），才能做那件顯而易見的事。

<!-- 4bf3af21e0dc -->
### 關鍵性質
- **複雜度**：`substring` 和 `+` 都會建出一個新的 String — 各是 O(n)，所以在迴圈裡串接就是 O(n²)；解法是 `StringBuilder`
- **核心想法**：改 `char[]` 或 `StringBuilder`，最後再轉換一次
- **什麼時候用**：演算法已經定案，剩下的問題只是「該用哪個 API 把它寫出來」的時候

<!-- 99992e5ed3ef -->
## String ↔ char[]

<!-- 7b6e90e28cfa -->
### String 轉字元陣列

<!--CODE-->

**效能**：逐字元走訪時 `toCharArray()` > `charAt()` > `split("")`

**速查：陣列 vs List**
<!--CODE-->

<!-- 555dace47684 -->
### 字元陣列轉 String


- 這樣才能 1) 存取元素 2) 走訪它

<!--CODE-->

<!-- 9686b542240a -->
### 在 char 陣列裡交換元素


<!--CODE-->

<!-- bf62f84ad925 -->
### 陣列轉 String


<!--CODE-->

<!-- 49b78e1b9e6c -->
### 堆疊轉 String


<!--CODE-->

<!-- b6adcdae8bd7 -->
## 讀取與切片字串

<!-- 83cd3c77f0c5 -->
### 存取 String 裡的元素

<!--CODE-->

<!-- 173fce69ff44 -->
### Substring 操作


<!--CODE-->

<!--CODE-->

**重要**：`substring(start, end)` 用的是 **[start, end)** 區間 — 含頭不含尾。

<!-- bc821527a8a6 -->
### 字串轉整數（`Integer.parseInt`）


**關鍵行為**：`Integer.parseInt()` 會自動去掉前導的 0。

<!--CODE-->

**常見模式：版本號比較（LC 165）**
<!--CODE-->

**關鍵技巧：**
<!--CODE-->

<!-- 8a6b0d326437 -->
### 檢查一個 String 是不是回文

<!--CODE-->

<!-- 5e45ee4f1851 -->
### 檢查一個 String 是不是另一個的子序列

<!--CODE-->

<!-- bfc30f29fbcf -->
## 組字串與修改字串

<!-- 365175ec0928 -->
### 替換 String 中某個索引上的字元


<!--CODE-->

<!-- 535044bd151e -->
### 字串字元替換


<!--CODE-->

<!-- 5f735fbf2fe6 -->
### 反轉 String

<!--CODE-->

<!-- e04f4865b319 -->
### 存取 `StringBuilder` 裡的元素


<!--CODE-->

<!-- 96e2675cd5c1 -->
### 依索引更新 `StringBuilder` 的值


<!--CODE-->

<!-- a65c5640fce6 -->
### 從 `StringBuilder` 移除元素

<!--CODE-->

<!-- b7a2c6483cbd -->
## 排序

<!-- 843274aef615 -->
### 陣列排序

<!-- 6e5cfd8494fe -->
#### 基本陣列排序
<!--CODE-->

<!-- 8b499ce87c7e -->
#### 二維陣列排序
<!--CODE-->

<!-- 5fc5f6e39148 -->
### 原地排序 vs stream 排序


**關鍵差異**：可變性與效能上的影響

| 方法 | 是否修改原物件 | 效能 | 記憶體用量 | 回傳型別 |
|--------|-------------------|-------------|--------------|-------------|
| `Arrays.sort(arr)` | ✅ **會**（原地） | **較快** | **較低** | `void` |
| `Arrays.stream(arr).sorted()` | ❌ **不會**（會複製一份） | **較慢** | **較高** | `Stream<T>` |

<!-- 506b6b48b758 -->
#### 原地排序（推薦）
<!--CODE-->

<!-- 8afd772383ff -->
#### Stream 排序（函數式風格）
<!--CODE-->


**示範：**
<!--CODE-->

<!-- 5e343da36750 -->
### Collections 排序


**核心原則**：
- **`Arrays.sort()`** → 用於陣列（基本型別與物件型別）
- **`Collections.sort()`** → 用於集合（List 等）

<!-- b0441288128f -->
#### 陣列排序（物件型別）
<!--CODE-->

<!-- 2c48baa2cc05 -->
#### List 排序
<!--CODE-->

<!-- 569d3e412282 -->
#### 複雜物件排序
<!--CODE-->

**效能比較：**
<!--CODE-->

<!-- eb6a4f8a78bf -->
### 自訂排序一個 List

<!--CODE-->

<!-- 909a83e61c00 -->
### 自訂排序 — comparator 回傳值規則 ⭐


> **核心規則**：comparator 回傳值的正負號決定元素順序。

| 回傳值 | 意義 | 效果 |
|---|---|---|
| **負數**（例如 -1） | o1 < o2 | o1 排在 o2 **前面** |
| **正數**（例如 +1） | o1 > o2 | o1 排在 o2 **後面** |
| **0** | o1 == o2 | 順序**不變** |

<!--CODE-->

<!-- 11704bda6a6a -->
#### Comparator 的心智模型
<!--CODE-->

<!-- 005089a19c18 -->
#### 常見模式總整理
<!--CODE-->

<!-- a5414af65b6d -->
### 依 HashMap 的 key 與 value 排序



<!--CODE-->

<!-- 6b8d6f89b6ab -->
### 先依 map key 再依 value 排序


<!--CODE-->

<!-- 5de30aaa78f2 -->
### 排序一個 String 裡的字元


<!--CODE-->

<!-- 146be7c5bde6 -->
### 字串的字典序比較


<!--CODE-->
