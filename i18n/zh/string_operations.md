<!-- d6d02021bbca -->
# 字串操作與語言 API

> **範圍** — 字串的語言層機制：Python 的切片與方法、Java 的 `String` 與 `StringBuilder`、字元分類與大小寫轉換、字元算術、split/join 的陷阱，以及建構字串的效能守則——不談使用它們的演算法。
> **另見**：[string.md](./string.md) — 母表，收錄字串題型目錄與模板；[string_examples.md](./string_examples.md) — 從同一份檔案拆出來的 LC 解題實作庫；[python_trick.md](./python_trick.md) 與 [java_trick.md](./java_trick.md) — 一般語言慣用法；[Collection.md](./Collection.md) — 怎麼在 Java 的各種集合之間做選擇。

<!-- 4a0eabd6b81f -->
## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)

<!-- 82cf6759fa7c -->
## 總覽

這裡沒有任何演算法，講的是底下那一層：你想都不用想就會用的那些呼叫，加上少數幾個會悄悄害你送出失敗的——Java 的 `split` 吃的是正規表達式、迴圈裡的 `+=` 是平方級、`Counter` 會留下值為零的鍵、`isalpha()` 對非 ASCII 字母也回傳真。

<!-- c7e07d42ac5b -->
### 關鍵性質
- **不可變**：`String` 在 Python 和 Java 都是不可變的——每次「修改」都是重新配置一份
- **成本模型**：串接是 O(len)，放進迴圈就變成 O(n²)。收進 list／`StringBuilder`，最後一次 join
- **字元算術**：Python 用 `ord(c) - ord('a')`、Java 用 `c - 'a'`，是查 26 格表的標準索引寫法
- **什麼時候該看這頁**：面試前，以及任何時候某個「很簡單」的字串呼叫行為跟你想的不一樣

<!-- d0cff7e29223 -->
## Python 字串操作
<!--CODE-->

<!-- bce560f16c5b -->
## Java 字串操作
<!--CODE-->

<!-- 1c23a15aa42a -->
## 字元分類與大小寫

[string.md](./string.md) 裡雙指標與剖析模板所依賴的判斷式與大小寫轉換。

<!--CODE-->

<!--CODE-->

- ⚠️ Python 的 `isalpha()`／`isdigit()` 是**認得 Unicode** 的：`'²'.isdigit()` 是 `True`，`'é'.isalpha()` 也是 `True`。題目說「只有小寫英文字母」時，要精確就用 `'a' <= c <= 'z'` 來判斷。
- ⚠️ Java 的 `Character.isLetter` 同樣認得 Unicode；LC 125 那類「跳過非英數字元」的掃描要的是 `Character.isLetterOrDigit`。
- ⚠️ 大小寫轉換**不保證**可以來回還原——某些 locale 下 `toLowerCase` 會改變長度。面試輸入都是 ASCII，但要把這個假設講出來，不要默默當成理所當然。

<!-- d70991d83d9a -->
## 字串操作技巧
<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- 7e7e6f7f81a3 -->
## 建構字串的效能
<!--CODE-->

<!--CODE-->

- ⚠️ 迴圈裡的 `s += x` 在**兩種語言**都是 O(n²)。CPython 有時會對 refcount 為 1 的情況做最佳化，但絕對不要依賴它。
- ⚠️ `StringBuilder` 不是執行緒安全的（那是 `StringBuffer`）——面試時不重要，當作追問的答案倒是值得知道。
- ⚠️ 已知答案長度時就先配置好：`new StringBuilder(n)` 可以省掉一路加倍複製的成本。

<!-- 0acbe700e9d9 -->
## 摘要與速查

| 任務 | Python | Java |
|---|---|---|
| 字串 → 字元 | `list(s)` | `s.toCharArray()` |
| 字元 → 字串 | `"".join(chars)` | `new String(chars)` |
| 反轉 | `s[::-1]` | `new StringBuilder(s).reverse().toString()` |
| 取子字串 | `s[i:j]` | `s.substring(i, j)` |
| 每隔一個字元 | `s[::2]` | 自己寫 `i += 2` 的迴圈 |
| 依空白切分 | `s.split()` | `s.trim().split("\\s+")` |
| 依一個點字元切分 | `s.split(".")` | `s.split("\\.")` — 參數是**正規表達式** |
| 切分並保留結尾空字串 | `s.split(",")` | `s.split(",", -1)` |
| 用分隔符接起來 | `",".join(parts)` | `String.join(",", parts)` |
| 去掉頭尾空白 | `s.strip()` | `s.trim()`／`s.strip()`（Java 11+） |
| 大小寫轉換 | `s.lower()`／`s.upper()` | `s.toLowerCase()`／`s.toUpperCase()` |
| 取代 | `s.replace(a, b)` | `s.replace(a, b)` |
| 尋找子字串 | `s.find(p)`（找不到回 `-1`） | `s.indexOf(p)`（找不到回 `-1`） |
| 字元 → 碼位／碼位 → 字元 | `ord(c)`／`chr(n)` | `(int) c`／`(char) n` |
| 對應到 26 個字母的索引 | `ord(c) - ord('a')` | `c - 'a'` |
| 是否為字母／數字／英數 | `c.isalpha()`／`c.isdigit()`／`c.isalnum()` | `Character.isLetter(c)`／`isDigit(c)`／`isLetterOrDigit(c)` |
| 次數表 | `Counter(s)` | `int[26]` 或 `HashMap<Character,Integer>` |
| 逐步建構 | list `append` 之後 `join` | `StringBuilder.append` 之後 `toString` |

<!-- 354df2966916 -->
### 值得背起來的坑
1. **Java 的 `split` 是正規表達式。** `"a.b".split(".")` 回傳的是一堆空字串。
2. **Java 的 `split` 會丟掉結尾的空字串。** `"a,,".split(",")` 長度是 1；傳 `-1` 才會保留。
3. **Python 切片永遠不會拋例外。** 3 個字元的字串取 `s[5:99]` 得到 `""` 而不是錯誤——方便，也是悶不吭聲的 bug 來源。
4. **`Counter` 會保留 0。** 減到 `0` 之後要 `del` 掉那個鍵，否則跟新建的 `Counter` 做 `==` 比較會失敗。
5. **絕對不要在迴圈裡串接字串。** 收集起來，最後一次接。
