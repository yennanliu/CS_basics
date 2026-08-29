# Java 技巧與慣用寫法

> **範圍** — 決定「正確的演算法會不會算出正確答案」的那些 Java 語言語意：字元其實是整數、傳值與傳參考的差別，以及整數運算。函式庫 API 則放在另外兩份姊妹速查表。
> **另見**：[java_trick_collections.md](./java_trick_collections.md) — 陣列、list、map、佇列、堆積(heap)、堆疊與 pair；[java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String 與 StringBuilder 的操作，以及所有跟 comparator 有關的東西；[python_trick.md](./python_trick.md) — 同一片領域的 Python 版；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 這些 API 所包裝的每種結構的 Big-O 查表。

## LeetCode 題目清單

- [Java](https://leetcode.com/problem-list/java/)

## 總覽

在 Java 面試裡，幾乎所有「邏輯明明是對的，答案卻錯了」都出自三類 bug，而這三類全都是語言語意問題，不是演算法選錯：

| 類別 | 這個 bug 長什麼樣 | 在哪裡解決 |
|---|---|---|
| **字元就是整數** | `charAt(i)` 給你的是 `char`，你把它當數字用時，拿到的其實是它的 ASCII 碼，而且不會報錯 | [字元與數字](#characters--digits) |
| **傳值 vs 傳參考** | 你以為複製了一份物件卻改到原本那個，或以為在共用某個基本型別卻只是複製了一份 | [傳值 vs 傳參考](#value-vs-reference--the-rule-behind-most-java-bugs-here) |
| **整數運算** | `/` 會朝零截斷、`%` 可能回傳負數，而 `int` 在 2^31 就溢位 | [整數運算與運算子](#integer-math--operators) |

### 參考資料

- [Java Documentation](https://docs.oracle.com/en/java/)
- [LeetCode Java Solutions](https://leetcode.com/problemset/all/?languageTags=java)


## 字元與數字

### `charAt`、字元比較與「字母 → 索引」


**關鍵方法**：`charAt()`、字元比較、ASCII 運算

```java
// Basic character access
String s = "www.google.com";
char result = s.charAt(6);  // Returns 'g'

// Character comparison in palindrome check (LC 647)
while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
    l--;
    r++;
    count++;
}

// Character to index mapping (lowercase a-z)
char c = 'c';
int index = c - 'a';  // Returns 2 (c is 3rd letter, 0-indexed)
```

```java
// java
String x = "abcxyz";
for ( Character c: x.toCharArray()){
    /** 
     *  c = a, a - ? = 0
     *  c = b, a - ? = -1
     *  c = c, a - ? = -2
     *  c = x, a - ? = -23
     *  c = y, a - ? = -24
     *  c = z, a - ? = -25
     */
    System.out.println(" c = " + c +
            ", a - ? = " + ('a' - c)
    );
}
```

**效能備註**：對字串而言 `charAt(i)` 是 O(1)，所以逐字元處理很有效率。


### 數字字元轉整數值（`char - '0'`）


**關鍵概念**：用 ASCII 相減，把數字字元（'0'-'9'）轉成它的整數值。

```java
// Basic conversion: char digit → int value
char c = '3';
int value = c - '0';  // value = 3

// Why it works:
// '0' = 48 (ASCII)
// '1' = 49
// '2' = 50
// '3' = 51  →  '3' - '0' = 51 - 48 = 3
// ...
// '9' = 57  →  '9' - '0' = 57 - 48 = 9
```

**常見模式：對數字字元做遞增／遞減（LC 752 - Open the Lock）**

```java
// LC 752 - Open the Lock
// Rotate lock wheel: '0' → '1' → '2' ... '9' → '0' (wrap around)

String cur = "0000";

for (int i = 0; i < 4; i++) {
    char c = cur.charAt(i);

    // Get integer value from char
    int digit = c - '0';  // e.g., '3' → 3

    // Calculate rotations with wrap-around
    int valPlus = (digit + 1) % 10;   // 9 → 0 (wrap)
    int valMinus = (digit - 1 + 10) % 10;  // 0 → 9 (wrap)

    // Convert back to char for new string
    char charPlus = (char) ('0' + valPlus);   // 4 → '4'
    char charMinus = (char) ('0' + valMinus); // 2 → '2'

    // Build new lock combinations
    String str1 = cur.substring(0, i) + charPlus + cur.substring(i + 1);
    String str2 = cur.substring(0, i) + charMinus + cur.substring(i + 1);
}
```

**速查表：**

| 操作 | 程式碼 | 範例 |
|-----------|------|---------|
| Char → Int | `c - '0'` | `'7' - '0'` → `7` |
| Int → Char | `(char)('0' + n)` | `(char)('0' + 7)` → `'7'` |
| 遞增（繞回） | `(digit + 1) % 10` | `9 + 1` → `0` |
| 遞減（繞回） | `(digit - 1 + 10) % 10` | `0 - 1` → `9` |

**遞減時為什麼要 `+ 10`？**
```java
// Without +10: (0 - 1) % 10 = -1  ❌ (negative!)
// With +10:    (0 - 1 + 10) % 10 = 9  ✅

// The +10 ensures the value is always positive before modulo
int valMinus = (digit - 1 + 10) % 10;
```

**對照：字母 vs 數字的對映**

| 類型 | Char → 索引 | 索引 → Char | 範圍 |
|------|--------------|--------------|-------|
| **字母** | `c - 'a'` | `(char)('a' + i)` | 'a'-'z' → 0-25 |
| **數字** | `c - '0'` | `(char)('0' + i)` | '0'-'9' → 0-9 |


### `charAt()` 回傳的是 `char`，不是那個數字的值 ⭐


> **陷阱**：`new Integer(s.charAt(i))` 拿到的是 ASCII 碼（例如 51），不是數字本身（例如 3）。

#### 錯誤寫法

```java
int val1 = new Integer(s.charAt(i));
// s.charAt(i) = '3' → '3' is char → ASCII 51
// val1 = 51 ❌
```

#### 正確寫法

```java
int val1 = s.charAt(i) - '0';
// '3' - '0' = 51 - 48 = 3 ✅
```

#### 原因：`char` 是以 ASCII/Unicode 值儲存的

| 運算式    | 結果       |
|---------------|--------------|
| `'3'`         | 51 (ASCII)   |
| `'0'`         | 48           |
| `'3' - '0'`  | 3            |

#### `new Integer(...)` 也已經被棄用

```java
new Integer(...)          // ❌ Deprecated, unnecessary object creation, slower
Integer.valueOf(...)      // ✅ If you need an Integer object
int x = ...;             // ✅ Prefer primitives
```

#### 多位數的子字串要用 `parseInt`

```java
int val2 = Integer.parseInt(s.substring(i - 2, i));
// ✅ Correct — parsing a String, not a char
```

#### 一句話總結

```java
new Integer(s.charAt(i))  // ❌ gives ASCII (e.g. 51)
s.charAt(i) - '0'         // ✅ gives actual digit (e.g. 3)
```

---

### 把字母對映成陣列索引（`c - 'a'`）

因為 `char` 本身是整數型別，減掉 `'a'` 就把一個小寫字母變成
**0 起始的索引**，這正是能用 26 格陣列取代 `HashMap<Character, ?>` 的原因：

```java
// java
// rank each letter of a custom alphabet -- LC 269 Alien Dictionary
String order = "hlabcdefgijkmnopqrstuvwxyz";
int[] orderMap = new int[26];
for (int i = 0; i < order.length(); i++) {
    orderMap[order.charAt(i) - 'a'] = i;   // 'h' - 'a' == 7  ->  orderMap[7] = 0
}
```

值得這麼做的理由是複雜度，不是寫起來比較短：讀陣列是 O(1)，而看似等價的
`order.indexOf(c)` 每次都要重掃字串，是**每次查詢 O(n)**。放在一個比較迴圈裡，
差別就是 O(n) 和 O(n²)。

同樣的相減對數字也成立，只是把 `'a'` 換成 `'0'` — 見上面那張字母 vs 數字的表，
那張才是要背起來的。


### 走訪一段字元範圍


```java
// Iterate through lowercase alphabet
for (char c = 'a'; c <= 'z'; c++) {
    System.out.print(c + " ");  // Output: a b c d e f g h i j k l m n o p q r s t u v w x y z
}

// Iterate through uppercase alphabet  
for (char c = 'A'; c <= 'Z'; c++) {
    System.out.print(c + " ");  // Output: A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
}

// Generate all single-character replacements (LC 127)
String word = "hit";
for (int i = 0; i < word.length(); i++) {
    for (char c = 'a'; c <= 'z'; c++) {
        if (c != word.charAt(i)) {
            String newWord = word.substring(0, i) + c + word.substring(i + 1);
            // Process newWord
        }
    }
}
```


## 傳值 vs 傳參考 — 這裡多數 Java bug 背後的那條規則

### `equals()` vs `==` — 什麼時候用哪一個 ⭐


> **核心規則**：`==` 比的是**參考**（是不是同一個物件？）。`equals()` 比的是**內容**（值一不一樣？）。

#### 規則本身 — `equals()` vs `==`

```text
==        → compares memory addresses (reference identity)
equals()  → compares logical content (value equality)
```

#### 比較集合（List、Set、Map）

```java
// LC 872 — Leaf-Similar Trees
List<Integer> list1 = new ArrayList<>();
List<Integer> list2 = new ArrayList<>();

getLeafSeq(root1, list1);
getLeafSeq(root2, list2);

/** NOTE !!!
 *
 *  Use `equals()` to compare two lists by content.
 *  `==` would check if they are the SAME object (always false here).
 */
list1 == list2        // ❌ false — different objects, even if same content
list1.equals(list2)   // ✅ true  — compares element-by-element
```

#### 比較字串

```java
String a = new String("hello");
String b = new String("hello");

a == b          // ❌ false — different objects
a.equals(b)     // ✅ true  — same content

// BUT: string literals from pool may share reference
String c = "hello";
String d = "hello";
c == d          // ✅ true (same pooled object) — but DON'T rely on this!
c.equals(d)     // ✅ true — always use this
```

#### 比較包裝型別（Integer、Long 等）

```java
Integer x = 127;
Integer y = 127;
x == y          // ✅ true (cached: -128 to 127)

Integer a = 128;
Integer b = 128;
a == b          // ❌ false — outside cache range, different objects!
a.equals(b)     // ✅ true  — always correct

// SAFE alternative for null-safe comparison
Objects.equals(a, b)  // ✅ handles null without NPE
```

#### 比較基本型別

```java
int i = 5;
int j = 5;
i == j          // ✅ correct — primitives have no .equals()
                // Primitives are ALWAYS compared by value with ==
```

#### 總結表 — `equals()` vs `==`

| 類型 | 用 `==` | 用 `equals()` | 陷阱 |
|------|----------|----------------|---------|
| `int`、`long`、`char`……（基本型別） | **是** | 不適用（沒有這個方法） | 無 |
| `String` | **否** | **是** | 字面值可能共用參考，但別依賴它 |
| `Integer`、`Long`……（包裝型別） | **否** | **是** | `==` 只在 -128..127 有效（快取） |
| `List`、`Set`、`Map` | **否** | **是** | `==` 比的是身分，不是內容 |
| 自訂物件 | **否** | **是**（若有覆寫） | 預設的 `equals()` 等同於 `==` |
| null 檢查 | **是**（`x == null`） | **否**（會 NPE！） | 檢查 null 一律用 `==` |

#### 面試速記規則

```text
Q: "Should I use == or equals()?"

→ Is it a primitive (int, long, boolean, char, double)?
  YES → use ==

→ Is it checking for null?
  YES → use ==  (x == null)

→ Everything else (String, Integer, List, Object...)?
  → use equals()
  → use Objects.equals(a, b) if either could be null
```

### 遞迴中的基本型別 vs 參考型別 — 回溯的判斷規則 ⭐


> **核心規則**：基本型別是傳值 → 每次呼叫都拿到自己的副本 → **不需要回溯**。
> 參考型別（集合、陣列、物件）是傳參考 → 狀態共用 → **必須回溯**。

#### 規則本身 — 遞迴中的基本型別 vs 參考型別

```text
Primitive param (int, long, double...)  →  copy per call  →  NO backtrack needed
Reference param (List, int[], HashMap)  →  shared object  →  MUST backtrack (add + remove)
Global / instance variable              →  shared state   →  MUST backtrack
```

#### 情況 1：基本型別 — 不需要回溯（LC 112 Path Sum）

```java
// LC 112 - Path Sum
// curSum is a primitive int → each recursive call gets its OWN COPY
// → no backtracking needed

public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;
    if (root.left == null && root.right == null) return root.val == targetSum;
    getPathHelper(root, 0);
    return pathSumMap.containsValue(targetSum);
}

private void getPathHelper(TreeNode root, Integer curSum) {
    if (root == null) return;

    int newSum = curSum + root.val;  // new local variable — does NOT affect parent's curSum

    if (root.left == null && root.right == null) {
        pathSumMap.put(newSum, newSum);
    }

    /** NOTE !!!
     *
     *  No backtrack on curSum / newSum needed!
     *
     *  Reason:
     *   - curSum is a primitive (int / Integer with autoboxing creates a new object).
     *   - Each recursive call receives its OWN COPY of curSum.
     *   - Modifying newSum inside the call does NOT affect the parent's curSum.
     *   - When the call returns, the parent's curSum is completely unchanged.
     *
     *  Stack visualization for tree 5 -> 4 -> 11 -> 7:
     *
     *    getPathHelper(5,   curSum=0)    newSum=5
     *      getPathHelper(4,  curSum=5)   newSum=9
     *        getPathHelper(11, curSum=9) newSum=20
     *          getPathHelper(7, curSum=20) newSum=27  ← leaf, store 27
     *          ← returns, parent curSum still 20 ✅
     *        ← returns, parent curSum still 9 ✅
     *      ← returns, parent curSum still 5 ✅
     */
    getPathHelper(root.left, newSum);
    getPathHelper(root.right, newSum);
}
```

**記憶體模型：**
```text
Stack frame:  getPathHelper(node=4, curSum=5)
              ├── newSum = 9     ← local to THIS frame
              ├── calls getPathHelper(node.left, newSum=9)
              │     └── newSum = 20  ← different frame, isolated
              └── curSum is still 5 when left call returns ✅
```

#### 情況 2：全域變數 — 需要回溯（LC 112 V0-2）

```java
// BAD pattern: using an instance variable for path sum
// → ALL recursive calls share the SAME curSum → must backtrack!

private int curSum = 0;  // shared across ALL calls ← danger!

public boolean hasPathSum_0_2(TreeNode root, int targetSum) {
    curSum += root.val;  // modifies shared state

    if (root.left == null && root.right == null) {
        if (curSum == targetSum) {
            curSum -= root.val;  // MUST backtrack before returning
            return true;
        }
    }

    if (hasPathSum_0_2(root.left, targetSum)) {
        curSum -= root.val;  // MUST backtrack
        return true;
    }
    if (hasPathSum_0_2(root.right, targetSum)) {
        curSum -= root.val;  // MUST backtrack
        return true;
    }

    curSum -= root.val;  // MUST backtrack on failure path too
    return false;
}
```

#### 情況 3：參考型別（List）— 需要回溯（LC 113 Path Sum II）

```java
// List is passed by reference → shared object → must backtrack
private void dfs(TreeNode node, int remain, List<Integer> path, List<List<Integer>> res) {
    if (node == null) return;

    path.add(node.val);          // ← mutates shared list

    if (node.left == null && node.right == null && remain == node.val) {
        res.add(new ArrayList<>(path));  // snapshot before backtrack
    } else {
        dfs(node.left,  remain - node.val, path, res);
        dfs(node.right, remain - node.val, path, res);
    }

    path.remove(path.size() - 1);  // ← MUST backtrack: undo the add
}
```

#### 情況 4：StringBuilder — 需要回溯（LC 988 Smallest String Starting From Leaf）

```java
// LC 988 - Smallest String Starting From Leaf
// StringBuilder is a reference type → shared object → MUST backtrack

private String smallest = "";

public String smallestFromLeaf(TreeNode root) {
    dfs(root, new StringBuilder());
    return smallest;
}

private void dfs(TreeNode node, StringBuilder sb) {
    if (node == null)
        return;

    /** NOTE !!!
     *
     *  PRE-ORDER DFS: process current node first
     */
    // 1. Add current character (0 -> 'a', 1 -> 'b', etc.)
    sb.append((char) ('a' + node.val));

    /** NOTE !!!
     *
     *  ONLY treat as result when reach `leaf`
     */
    // 2. Leaf check: If we reach a leaf, we have a candidate path
    if (node.left == null && node.right == null) {

        /** NOTE !!!
         *
         *  We reverse current StringBuilder to fit the requirement
         *  (string from leaf to root)
         */
        String currentStr = new StringBuilder(sb).reverse().toString();

        /** NOTE !!!
         *
         *  How we get the `lexicographically smaller` one:
         *  currentStr.compareTo(smallest) < 0
         */
        if (smallest.equals("") || currentStr.compareTo(smallest) < 0) {
            smallest = currentStr;
        }
    }

    // 3. Standard DFS
    dfs(node.left, sb);
    dfs(node.right, sb);

    /** NOTE !!!
     *
     *  For StringBuilder (NOT a primitive type),
     *  we MUST do BACKTRACK (undo)
     *
     *  Reason:
     *   - StringBuilder is a reference type (object)
     *   - All recursive calls share the SAME StringBuilder instance
     *   - After exploring subtrees, we must remove the current char
     *     to restore sb to its state before this call
     *   - Without this, the path would keep growing incorrectly
     *
     *  Memory model:
     *
     *    dfs(node=5, sb="e")
     *      ├── sb.append('a') → sb="ea"
     *      ├── dfs(left, sb="ea")
     *      │     └── sb.append('b') → sb="eab"
     *      │     └── sb.deleteCharAt() → sb="ea"  ← backtrack!
     *      ├── dfs(right, sb="ea")  ← sb is correctly "ea", not "eab"
     *      │     └── ...
     *      └── sb.deleteCharAt() → sb="e"  ← backtrack to parent state
     */
    // 4. BACKTRACK: Remove the last character before returning to parent
    sb.deleteCharAt(sb.length() - 1);
}
```

#### 總結表 — 遞迴中的基本型別 vs 參考型別

| 狀態類型 | 範例 | 要回溯嗎？ | 原因 |
|---|---|---|---|
| 基本型別參數 | `int curSum` | **否** | 每次呼叫都有自己的副本 |
| 包裝型別參數（自動裝箱） | `Integer curSum` | **否** | 自動裝箱(autoboxing)會建出新物件 |
| 區域變數 | `int newSum = curSum + val` | **否** | 只屬於當前的堆疊框架 |
| 實例／全域變數 | `this.curSum` | **是** | 所有呼叫共用 |
| 集合參數 | `List<Integer> path` | **是** | 是參考，會就地被修改 |
| 陣列參數 | `int[] path` | **是** | 是參考，會就地被修改 |
| StringBuilder 參數 | `StringBuilder sb` | **是** | 是參考，透過 `append()`/`deleteCharAt()` 就地被修改 |

#### 面試提示

```text
Q: "Do I need to backtrack this variable?"

Decision tree:
1. Is it a primitive (int, long, boolean, char...)?
   → Passed as value → NO backtrack needed

2. Is it a local variable inside the current stack frame?
   → NOT shared → NO backtrack needed

3. Is it an instance/class variable?
   → Shared → YES, must backtrack

4. Is it a collection or array passed as parameter?
   → Reference = shared → YES, must backtrack

Quick mental model:
  "If I change this variable, will the CALLER see the change?"
  YES → backtrack required
  NO  → no backtrack needed
```

---

### 遞迴的參數傳遞


```java
// LC 104
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/MaximumDepthOfBinaryTree.java
```

**重要觀念**：在 Java 裡，基本型別是**傳值**的（會建立副本）。

```java
// ❌ WRONG: Primitive parameter won't persist changes across recursive calls
public int wrongDepth(TreeNode root, int depth) {
    if (root == null) return depth;
    depth++;  // This increment is lost after recursion returns
    return Math.max(wrongDepth(root.left, depth), wrongDepth(root.right, depth));
}

// ✅ CORRECT: Use instance variables for state that needs to persist
class Solution {
    private int maxDepth = 0;  // Instance variable persists across calls
    
    public int maxDepth(TreeNode root) {
        depthHelper(root, 0);
        return maxDepth;
    }
    
    private void depthHelper(TreeNode root, int currentDepth) {
        if (root == null) return;
        
        maxDepth = Math.max(maxDepth, currentDepth + 1);  // Update global state
        depthHelper(root.left, currentDepth + 1);
        depthHelper(root.right, currentDepth + 1);
    }
}

// ✅ ALTERNATIVE: Return and combine values (functional approach)
public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

**重點帶走**：需要跨遞迴呼叫追蹤狀態時，要嘛用實例變數，要嘛把遞迴設計成回傳值再合併。


### 把值傳出方法之外 — 可變容器（mutable holder）模式


> **核心觀念**：參考型別（StringBuilder、List、int[]、Map 等）是以**參考**傳遞的，不是傳值。在函式內做的修改，在函式回傳之後依然存在。

#### 模式 1：用 StringBuilder 組路徑／字串（LC 694）

```java
// LC 694 - Number of Distinct Islands
// Pattern: Create placeholder → Pass to DFS → Use modified result

Set<String> uniqueIslands = new HashSet<>();

for (int r = 0; r < rows; r++) {
    for (int c = 0; c < cols; c++) {
        if (grid[r][c] == 1) {
            // Step 1: Create empty StringBuilder
            StringBuilder pathSignature = new StringBuilder();

            // Step 2: Pass to DFS — it will modify pathSignature in place
            /** NOTE !!!
             *  We pass `pathSignature` as a reference.
             *  DFS will call pathSignature.append(...)
             *  These changes PERSIST after DFS returns
             */
            dfs(grid, r, c, pathSignature, 'S');

            // Step 3: After DFS returns, pathSignature is populated
            if (pathSignature.length() > 0) {
                uniqueIslands.add(pathSignature.toString());
            }
        }
    }
}

private void dfs(int[][] grid, int r, int c, StringBuilder path, char direction) {
    // Base case
    if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
        return;
    }

    grid[r][c] = 0;

    // ✅ MODIFY the passed reference
    // This directly modifies the caller's StringBuilder object
    path.append(direction);

    // Explore neighbors
    dfs(grid, r + 1, c, path, 'D');
    dfs(grid, r - 1, c, path, 'U');
    dfs(grid, r, c + 1, path, 'R');
    dfs(grid, r, c - 1, path, 'L');

    // Backtrack: undo the append
    path.append('O');
}
```

**記憶體模型：**
```text
Main thread:
pathSignature = StringBuilder{} at memory address 0x1000

    Call dfs(..., pathSignature, 'S')
    ├── path parameter = reference to 0x1000
    ├── path.append('S')  → modifies object at 0x1000 → "S"
    │
    ├── Call dfs(..., path, 'D')
    │   ├── path parameter = reference to 0x1000 (SAME object!)
    │   ├── path.append('D')  → modifies object at 0x1000 → "SD"
    │   ├── path.append('O')  → modifies object at 0x1000 → "SDO"
    │   └── return
    │
    ├── path.append('O')  → modifies object at 0x1000 → "SDOO"
    └── return

Back in main:
pathSignature = StringBuilder{"SDOO"}  ✅ (MODIFIED!)
```

#### 模式 2：用 List 收集結果（LC 113 Path Sum II）

```java
// LC 113 - Path Sum II
// Similar pattern but with List<Integer> for path collection

public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    dfs(root, targetSum, path, result);
    return result;
}

private void dfs(TreeNode node, int remain, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    // Modify the passed List
    path.add(node.val);

    if (node.left == null && node.right == null && remain == node.val) {
        // Snapshot the path before backtracking
        result.add(new ArrayList<>(path));
    } else {
        dfs(node.left, remain - node.val, path, result);
        dfs(node.right, remain - node.val, path, result);
    }

    // Backtrack: undo the add
    path.remove(path.size() - 1);
}
```

**和基本型別的關鍵差異：**
```java
// ❌ PRIMITIVE: Changes don't persist
private void addToSum(int currentSum) {
    currentSum += 5;  // Only affects local copy
}
int mySum = 10;
addToSum(mySum);
System.out.println(mySum);  // Still 10, NOT 15!

// ✅ REFERENCE: Changes persist
private void addToList(List<Integer> list) {
    list.add(5);  // Affects the original list object
}
List<Integer> myList = new ArrayList<>();
addToList(myList);
System.out.println(myList);  // [5] ✅ MODIFIED!
```

#### 模式 3：通用模式 — 建立、傳入、修改、使用

```java
// Generic template for this pattern:

public Type method() {
    // 1. Create placeholder (reference type)
    SomeRefType placeholder = new SomeRefType();

    // 2. Pass to helper function
    helperFunction(placeholder, otherParams);

    // 3. Use modified result
    return placeholder;  // or use directly
}

private void helperFunction(SomeRefType data, OtherParams...) {
    // Modify the reference — changes persist in caller
    data.modify(...);

    // Recurse if needed
    helperFunction(data, newParams);

    // Undo if backtracking required
    data.undo(...);
}
```

#### 這個模式常用到的參考型別

| 類型 | 修改方法 | 需要回溯嗎？ | 使用情境 |
|------|----------------------|-------------------|----------|
| `StringBuilder` | `append(x)`、`setCharAt(i, c)`、`deleteCharAt(i)` | ✅ 是 | 帶回溯的字串組建 |
| `List<T>` | `add(x)`、`remove(i)`、`set(i, x)` | ✅ 是 | 路徑／結果收集 |
| `int[]` / `char[]` | `arr[i] = value` | ✅ 是 | 陣列修改 |
| `Map<K,V>` | `put(k, v)`、`remove(k)` | ✅ 是 | 次數統計 |
| `Queue<T>` | `add(x)`、`poll()`、`offer(x)` | ✅ 看情況 | BFS 逐層走訪 |
| `Set<T>` | `add(x)`、`remove(x)` | ✅ 是 | 已訪記錄 |
| 基本型別 `int`、`long` | 不適用（傳值） | ❌ 否 | 只能靠回傳值或實例變數 |
| `String` | 不適用（不可變） | ❌ 否 | 改用 StringBuilder |

**什麼時候要回溯：**
```text
Rule: If the parameter is a reference type that gets MODIFIED, you must UNDO the modification.

Path/List building:  path.add(val) → must do path.remove(...)
StringBuilder:       sb.append(...) → must do sb.deleteCharAt(...)
Array modification:  arr[i] = val   → must do arr[i] = oldVal
Map/Set:             data.add(x)    → must do data.remove(x)

Primitives:          No backtrack needed (they're copied)
```

#### 模式 4：List 收集（LC 131 Palindrome Partitioning）

```java
// LC 131
public List<List<String>> partition(String s) {
    /**
     * NOTE: we can init result, pass it to method,
     * modify it, and return as ans
     */
    List<List<String>> result = new ArrayList<>();
    dfs(0, result, new ArrayList<String>(), s);
    return result;
}

private void dfs(int start, List<List<String>> result, List<String> currentList, String s) {
    // Base case: reached end of string
    if (start == s.length()) {
        // Snapshot: add a copy of currentList (don't add reference)
        result.add(new ArrayList<>(currentList));
        return;
    }

    // Try all palindromes starting from 'start'
    for (int end = start; end < s.length(); end++) {
        if (isPalindrome(s, start, end)) {
            // Add to current partition
            currentList.add(s.substring(start, end + 1));

            // Recurse
            dfs(end + 1, result, currentList, s);

            // Backtrack: remove what we added
            currentList.remove(currentList.size() - 1);
        }
    }
}

private boolean isPalindrome(String s, int l, int r) {
    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) return false;
        l++;
        r--;
    }
    return true;
}
```

**關鍵區別：**
```java
// ❌ WRONG: Adds reference to currentList (not a snapshot)
result.add(currentList);  // All entries point to same list!

// ✅ CORRECT: Add a copy
result.add(new ArrayList<>(currentList));  // Each entry is independent
```

### 基本型別是以傳值方式傳遞的


```java
// java
// LC 695

/**
 *  NOTE !!!!
 *
 *   DON'T pass `area` as parameter to the DFS func (getBiggestArea)
 *
 *   Reason:
 *
 *   1) in java, primitives pass by value.
 *      `int` is one of the primitives
 *
 *   2) meaning when we pass `area` to dfs,
 *      it actually sent as a new COPY everytime,
 *      which makes us CAN'T track/persist the new area value
 */

// ...

 private int _getArea(int[][] grid, boolean[][] seen, int x, int y){

    // ...
    return 1 + _getArea(grid, seen, x+1, y) +
                _getArea(grid, seen, x-1, y) +
                _getArea(grid, seen, x, y+1) +
                _getArea(grid, seen, x, y-1);

}
```

### 直接修改自訂類別的欄位（`v.field -= 1`）⭐


> **核心問題**：什麼時候可以寫 `v.cnt -= 1`，什麼時候不行？

#### 什麼時候「可以」直接修改

三個條件必須同時成立：

1. **欄位可存取**（不是 `private`，或者你就在該類別內部）
2. **欄位不是 `final`**
3. **參考不是 `null`**

```java
class ValCnt {
    char val;
    int cnt;           // package-private, non-final
    ValCnt(char val, int cnt) { this.val = val; this.cnt = cnt; }
}

ValCnt v = new ValCnt('a', 3);
v.cnt -= 1;   // ✅ allowed: accessible + non-final + non-null
```

#### 什麼時候「不能」直接修改

**情況 1 — `private` 欄位**（在類別外部）
```java
class ValCnt {
    private int cnt;   // private!
}

v.cnt -= 1;   // ❌ compile error — use a setter/method instead
```

**情況 2 — `final` 欄位**
```java
class ValCnt {
    final int cnt;
}

v.cnt -= 1;   // ❌ compile error — cannot assign to final variable
```

**情況 3 — null 參考**
```java
ValCnt v = null;
v.cnt -= 1;   // ❌ NullPointerException at runtime
```

#### 常見誤解：`final` 的參考 vs `final` 的欄位

```java
final ValCnt v = new ValCnt('a', 3);

v.cnt -= 1;              // ✅ allowed — final only locks the REFERENCE
v = new ValCnt('b', 1);  // ❌ compile error — cannot reassign final reference
```

變數上的 `final` 代表你不能讓 `v` 指向另一個物件，
它**並不會**阻止你修改那個物件的欄位。

#### `Integer`（包裝型別）欄位 — 可以用，但會自動裝箱

```java
class Holder { Integer cnt; }

Holder h = new Holder();
h.cnt = 3;
h.cnt -= 1;  // ✅ works, but really: h.cnt = Integer.valueOf(h.cnt.intValue() - 1)
             //    creates a new Integer object; fails if field is final
```

#### 總結表 — 就地修改自訂類別

| 情境 | 允許 `v.cnt -= 1` 嗎？ |
|-----------|----------------------|
| `int cnt`（package-private） | ✅ 可以 |
| `private int cnt`（在類別外部） | ❌ 編譯錯誤 |
| `final int cnt` | ❌ 編譯錯誤 |
| `final ValCnt v`（參考是 final） | ✅ 可以 — 欄位仍然可變 |
| `v == null` | ❌ NullPointerException |
| `Integer cnt`（包裝型別） | ✅ 可以，但會自動裝箱成新物件 |

#### 面試陷阱：`v.cnt--` vs `--v.cnt` vs `v.cnt -= 1`

三者都會把 `cnt` 減 1，差別在於**運算式回傳的值**：

```java
v.cnt = 3;

int a = v.cnt--;   // a = 3  (returns BEFORE decrement), v.cnt = 2
int b = --v.cnt;   // b = 1  (returns AFTER  decrement), v.cnt = 1
v.cnt -= 1;        // no return value used, v.cnt = 0
```

當你只在意副作用、不在意回傳值時，就用 `v.cnt -= 1`。

---

### 賦值複製的是參考，不是物件


- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/LinkedList/ReverseLinkedList.java


```java

    public ListNode reverseList_0_0_1(ListNode head) {
            // edge
            if(head == null || head.next == null) {
                return head;
            }

            ListNode _prev = null;
            /**
             *  NOTE !!!
             *
             *   we CAN'T just assign `_prev` init val to `res`
             *   and return `res` as result
             *   e.g. this is WRONG: return res;
             *
             *  Reason:
             *   - At this point, res is just a reference to null.
             *   - As you update _prev during the loop,
             *     res DOES NOT magically follow _prev.
             *     It stays stuck at the value it was assigned
             *     when you created it → null.
             *
             *   So by the end:
             *    - _prev points to the new head of the reversed list ✅
             *    - res is still null ❌
             *
             *
             *  -> Java references don’t “track” each other after assignment.
             *    res = _prev copies the reference value `at that moment`
             *    If _prev later changes, res won’t update.
             *
             */
            ListNode res = _prev;
            while(head != null){
                ListNode _next = head.next;
                head.next = _prev;
                //_prev.next = head;
                _prev = head;
                head = _next;
            }

            //return res;
            return _prev;
        }

```

### 重新建構節點，而不是就地修改


- LC 116
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/PopulatingNextRightPointersInEachNode.java

```java
public Node connect_0_1(Node root) {
    if (root == null)
        return null;

    Queue<Node> q = new LinkedList<>();
    q.add(root);

    while (!q.isEmpty()) {
        int size = q.size();
        /**
         *  NOTE !!!!
         *
         *   via `prev` node,
         *   we can easily re-connect left - right node in each layer
         *
         *   pattern as below:
         *
         *     Node prev = null;
         *     for(int i = 0; i < size; i++){
         *         TreeNode cur = q.poll();
         *         // NOTE !!!! this
         *         if(prev == null){
         *             prev.next = cur;
         *         }
         *         // NOTE !!!! this
         *         prev = cur;
         *
         *         // ....
         *     }
         *
         */
        Node prev = null; // prev tracks previous node in this level

        for (int i = 0; i < size; i++) {
            Node cur = q.poll();

            if (prev != null) {
                prev.next = cur; // link previous node to current
            }
            prev = cur;

            if (cur.left != null)
                q.add(cur.left);
            if (cur.right != null)
                q.add(cur.right);
        }

        // last node in level should point to null
        if (prev != null)
            prev.next = null;
    }

    return root;
}
```

### 取得目前實例的防禦性複本

```java
// java
// LC 46
List<List<Integer>> ans = new ArrayList<>();
List<Integer> cur = new ArrayList<>();
//...
ans.add(new ArrayList<>(cur));
//...
```

## 整數運算與運算子

### `ceil` vs `floor` — 定義


| 操作 | 意義 | 範例 |
|-----------|---------|---------|
| `Math.ceil(x)` | **向上**取到最近的整數 | `ceil(7.0/3)` → `3.0` |
| `Math.floor(x)` | **向下**取到最近的整數 | `floor(7.0/3)` → `2.0` |
| `(int)(a/b)` | **朝零截斷**（正數時等同 floor） | `7/3` → `2` |

```java
// Using double division + Math.ceil / floor
System.out.println(Math.ceil(7.0 / 3));   // 3.0  (rounds UP)
System.out.println(Math.floor(7.0 / 3));  // 2.0  (rounds DOWN)
System.out.println(7 / 3);               // 2    (integer truncation = floor for positives)

// Cast result back to int
int ceilVal  = (int) Math.ceil((double) 7 / 3);  // 3
int floorVal = (int) Math.floor((double) 7 / 3); // 2
```

**關鍵地雷**：Java 的整數除法**一律朝零截斷**（正數時等於 floor）。
```java
7  / 3  →  2   // floor (positive)
-7 / 3  → -2   // truncates toward zero (NOT floor, which would be -3)
```

---

### 整數版的天花板除法 — 不需要 `double` ⭐


**公式**：只用整數算出 `ceil(a / b)`：

```java
int ceilDiv = (a + b - 1) / b;
```

**為什麼會成立：**
```text
ceil(a / b)  =  (a + b - 1) / b   (integer division, b > 0)

Example: a=7, b=3
  (7 + 3 - 1) / 3  =  9 / 3  =  3  ✅  (Math.ceil(7.0/3) = 3)

Example: a=6, b=3 (exact division)
  (6 + 3 - 1) / 3  =  8 / 3  =  2  ✅  (Math.ceil(6.0/3) = 2)

Example: a=1, b=5
  (1 + 5 - 1) / 5  =  5 / 5  =  1  ✅  (Math.ceil(1.0/5) = 1)
```

**對照：計算天花板除法的兩種寫法**

```java
// Method 1: Integer trick (faster, no casting)
int ceil1 = (val + d - 1) / d;

// Method 2: double cast + Math.ceil (clearer intent, slightly slower)
int ceil2 = (int) Math.ceil((double) val / d);

// Both produce identical results for positive val and d
```

**經典用法 — LC 1283 Find the Smallest Divisor Given a Threshold：**
```java
// ceil(val / divisor) without using double
private boolean canDivide(int[] nums, int threshold, int d) {
    int sum = 0;
    for (int val : nums) {
        sum += (val + d - 1) / d;  // ← integer ceiling division
        if (sum > threshold) return false; // early exit
    }
    return sum <= threshold;
}
```

---

### 整數版的地板除法


對正整數來說，`/` 本身就是 floor：
```java
int floorDiv = a / b;  // works when a >= 0 and b > 0
```

遇到**負數**時，要用 `Math.floorDiv`：
```java
Math.floorDiv(-7, 3);   // -3  (true floor)
-7 / 3;                 // -2  (truncation, NOT floor!)

Math.floorDiv(7, 3);    //  2  (same as 7/3 for positives)
```

---

### 天花板／地板速查

| 目標 | 程式碼 | 備註 |
|------|------|-------|
| Ceil（用 double） | `(int) Math.ceil((double) a / b)` | 好讀，但必須轉型 |
| Ceil（整數技巧） | `(a + b - 1) / b` | 快、不用轉型，但僅限正數 |
| Floor（正數） | `a / b` | 整數除法會截斷 |
| Floor（任意正負） | `Math.floorDiv(a, b)` | 能正確處理負數 |
| 四捨五入（.5 進位） | `(int) Math.round((double) a / b)` | 最接近的整數 |
| 取中點且不溢位 | `l + (r - l) / 2` | 標準的二分搜尋中點寫法 |

---

### 用到天花板除法的經典 LC 題


| LC | 題目 | 天花板除法的用途 |
|----|---------|----------------------|
| **1283** | Find the Smallest Divisor Given a Threshold | 每個元素算 `(val + d - 1) / d` |
| **1011** | Capacity To Ship Packages Within D Days | 需要的天數 `(wt + cap - 1) / cap` |
| **875**  | Koko Eating Bananas | 每堆要吃的小時數 `(pile + k - 1) / k` |
| **2064** | Minimized Maximum of Products Distributed | 每組的天花板 `(n + m - 1) / m` |

**模式**：這些全都是**對答案做二分搜尋**的題目，而檢查函式都需要天花板除法來數「Y 裡面塞得下幾個 X」。

```java
// General template for this binary search pattern
// (Binary search on divisor/capacity/speed)
int l = 1, r = MAX_VAL;
while (l <= r) {
    int mid = l + (r - l) / 2;
    if (check(nums, threshold, mid)) {
        r = mid - 1;  // valid, try smaller
    } else {
        l = mid + 1;  // too small, try larger
    }
}
return l;

// Inside check(): use ceiling division
private boolean check(int[] nums, int threshold, int d) {
    int sum = 0;
    for (int val : nums) {
        sum += (val + d - 1) / d;  // ceil(val / d)
    }
    return sum <= threshold;
}
```

---

### 用 `long` 避免 `int` 溢位


```java
// java
// LC 98

// ...

/**
*  NOTE !!!
*
*  Use long to handle edge cases for Integer.MIN_VALUE and Integer.MAX_VALUE
*  -> use long to handle overflow issue (NOT use int type)
*/
long smallest_val = Long.MIN_VALUE;
long biggest_val = Long.MAX_VALUE;

return check_(root, smallest_val, biggest_val);

// ...
```


### 三個數的最大值與最小值


```java
// java

// LC 152
max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
```

### 計算整數中設為 1 的位元數

```java
// java

/**
*  Integer.bitCount
*
*  -> java default get number of "1" from binary representation of a 10 based integer
*
*  -> e.g.
*      Integer.bitCount(0) = 0
*      Integer.bitCount(1) = 1
*      Integer.bitCount(2) = 1
*      Integer.bitCount(3) = 2
*
*  Ref
*      - https://blog.csdn.net/weixin_42092787/article/details/106607426
*/

// LC 338

```

### 預先算好 N 以內的完全平方數


> **技巧**：先把所有 ≤ N 的完全平方數算進一個 list，之後就走訪這個 list，而不是每次重算 `i * i`。在 LC 279（Perfect Squares）這類 BFS/DP 題裡很好用。

```java
// Pre-calculate perfect squares up to n
List<Integer> squares = new ArrayList<>();
for (int i = 1; i * i <= n; i++) {
    squares.add(i * i);
}

// Usage: iterate over pre-computed squares
for (int square : squares) {
    int nextVal = remaining - square;
    if (nextVal < 0) break; // squares are sorted, early termination
    // ...
}
```

### 用 `random.nextInt` 取隨機整數

```java
// java
// LC 528

/** bound : range of random int can be returned */
//  @param bound the upper bound (exclusive).  Must be positive.
Random random = new Random();

//  * @param bound the upper bound (exclusive).  Must be positive.
System.out.println(random.nextInt(10));
System.out.println(random.nextInt(10));
System.out.println(random.nextInt(100));
```


### `k++` vs `++k`

```java
// java
// LC 78

/** NOTE HERE !!!
*
*  ++i : i+1 first,  then do op
*  i++ : do op first, then i+1
*
*/
```

## 速查表

### 最常見的寫法


#### 資料結構初始化
```java
// Arrays
int[] arr = new int[n];                    // Fixed size
int[][] matrix = new int[rows][cols];      // 2D array

// Collections
List<Integer> list = new ArrayList<>();   // Dynamic list
Map<String, Integer> map = new HashMap<>(); // Key-value store
Set<Integer> set = new HashSet<>();        // Unique elements
Queue<Integer> queue = new LinkedList<>(); // FIFO operations

// Priority Queues
PriorityQueue<Integer> minHeap = new PriorityQueue<>();              // Min-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // Max-heap
```

#### 必備的型別轉換
```java
// String ↔ Character Array
String str = "hello";
char[] chars = str.toCharArray();          // String to char array
String newStr = new String(chars);         // Char array to String

// Array ↔ List  
Integer[] arr = {1, 2, 3};
List<Integer> list = new ArrayList<>(Arrays.asList(arr));  // Array to List
Integer[] newArr = list.toArray(new Integer[0]);           // List to Array

// Character to Index (for a-z)
int index = character - 'a';               // 'a'→0, 'b'→1, ..., 'z'→25
```

#### 常見操作
```java
// HashMap with default values
map.getOrDefault(key, defaultValue);
map.putIfAbsent(key, value);

// Sorting  
Arrays.sort(array);                        // In-place array sort
Collections.sort(list);                    // In-place list sort
list.sort(Collections.reverseOrder());     // Reverse order

// String operations
s.charAt(i);                               // Get character at index
s.substring(start, end);                   // [start, end) substring
StringBuilder sb = new StringBuilder();     // Mutable string
```

### 效能提示


| 操作 | 有效率的做法 | 該避免的做法 |
|-----------|-------------------|-------|
| **字串組建** | `StringBuilder` | 在迴圈裡做字串串接 |
| **字元存取** | 先 `toCharArray()` 再走訪 | 在密集迴圈裡用 `charAt()` |
| **排序** | `Arrays.sort()`、`Collections.sort()` | 對大量資料用 stream 排序 |
| **印出陣列** | `Arrays.toString()`、`Arrays.deepToString()` | 手動走訪 |
| **字元對映** | `char - 'a'` | 重複呼叫 `indexOf()` |

### 常見的 LeetCode 模式


#### 次數統計
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}
```

#### 雙指標搭配字元比較
```java
int left = 0, right = s.length() - 1;
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) return false;
    left++;
    right--;
}
```

#### 用優先佇列處理 Top-K 問題
```java
PriorityQueue<Integer> heap = new PriorityQueue<>();
for (int num : nums) {
    heap.offer(num);
    if (heap.size() > k) heap.poll();
}
```

### 記憶體管理


- **基本型別陣列**：比物件陣列更省記憶體
- **ArrayList**：會自動擴充，資料量大時初始容量很重要
- **StringBuilder**：迴圈中做字串串接時請用它
- **字元陣列**：處理字元時比直接操作 String 更有效率
