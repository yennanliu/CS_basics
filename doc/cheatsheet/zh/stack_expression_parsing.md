# Stack — 運算式解析

> **範圍** — 用堆疊解運算式的題型家族：計算機三兄弟（LC 224 / 227 / 772）、decode-string 式的巢狀（LC 394），以及後綴／依序運算元求值（LC 150、682），還有讓運算子優先序自然浮現的 `pre_op` 延遲插入技巧。
> **另見**：[stack.md](./stack.md) — 母表：LIFO 基礎與標準堆疊模板；[stack_examples.md](./stack_examples.md) — 堆疊家族其餘題目的解法檔案庫；[string.md](./string.md) — 一般字串掃描；[recursion.md](./recursion.md) — 用遞迴下降的角度看同一套括號處理。

## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [String](https://leetcode.com/problem-list/string/)
- [Math](https://leetcode.com/problem-list/math/)

## 概觀

這裡每一題都是「帶著一個堆疊由左往右掃一趟」，而且每一題都只是這**三個**子問題的排列組合：

| 子問題 | 機制 | 出現在 |
|---|---|---|
| **多位數數字** | `num = num * 10 + int(ch)` — 看到一個數字就馬上定案是不行的 | 全部 |
| **優先序**（`*` `/` 綁得比 `+` `-` 緊） | **在 `pre_op` 上延遲插入**：遇到 `+`/`-` 就 push `±num`，遇到 `*`/`/` 就 pop 出來合併，答案 = `sum(stack)` | LC 227、772 |
| **巢狀**（括號、`k[...]`） | 一個 `(` 開一層新作用域 — 要嘛對同一份輸入**遞迴**，要嘛把外層狀態**push** 起來後重置 | LC 224、772、394 |

**後綴式（LC 150）是最輕鬆的情況**：token 的順序本身已經編碼了優先序與巢狀，什麼都不用延後 — 數字就 push，運算子就 pop 兩個。

### 關鍵性質
- **複雜度**：這裡每個演算法都是 O(n) 時間、O(n) 空間（堆疊深度 = 巢狀深度）；結構本身每個操作的成本請看母表的 [Time Complexity](./stack.md#time-complexity) 表
- **核心想法**：堆疊把「我現在還不能決定」變成「等*下一個* token 進來我再決定」
- **什麼時候用**：任何「字串的意義取決於後面是什麼、或取決於現在在哪一層作用域」的單趟求值

## 1) 在 `pre_op` 上延遲插入 — 優先序引擎

**關鍵洞見**：由左往右掃運算式時，在看到**下一個**運算子之前，根本沒辦法決定當前這個數字要怎麼處理 — 所以把 push **延後**到那時候，改成對 `pre_op` 動作。

**為什麼？**
- `+` / `-`（低優先序）：直接 push `±num`，把帳留到最後的 `sum(stack)` 一起算
- `*` / `/`（高優先序）：立刻 pop 出上一個值合併 — 但這件事要等 `num` 完整拼好、下一個運算子出現**之後**才知道

**初始設定**：
- `pre_op = '+'`（初始值）— 讓第一個數字自動以正號被 push 進去
- `num` 負責累積位數；觸發點是「遇到運算子」或「字串結束」

**視覺追蹤 — `"3+2*2"` → 7**：
```text
char  num  trigger?  pre_op  action               stack
'3'   3    no        '+'     —                    []
'+'   3    YES       '+'     push(3)  → pre_op='+' [3]
'2'   2    no        '+'     —                    [3]
'*'   2    YES       '+'     push(2)  → pre_op='*' [3, 2]
'2'   2    YES(end)  '*'     pop()→2, push(2*2=4)  [3, 4]
sum([3, 4]) = 7 ✓
```

分支階梯本身這裡不重複貼 — 它就是第 2 節**萬用計算機**裡的那串 `if/elif`，那份才是這個想法的標準實作。

**用到這個模式的相關題目**：

| LC | 題目 | 變化 |
|----|---------|-----------|
| 227 | Basic Calculator II | `+-*/`，沒有括號 |
| 224 | Basic Calculator I | `+-()`，沒有 `*/` |
| 772 | Basic Calculator III | `+-*/()` 全都有 |
| 394 | Decode String | `pre_op` 追蹤 `[` 之前的重複次數 |

## 2) 萬用計算機 — LC 224 / 227 / 772 ⭐⭐⭐⭐⭐

> **一套演算法通吃三題計算機**：只有 `+-`（224）、`+-*/` 無括號（227）、`+-*/()` 全有（772）。用堆疊處理運算子**優先序**，用遞迴處理**括號**。

**核心想法** — 把兩個各解一半問題的獨立技巧併起來：

| 子問題 | 技巧 | 怎麼呈現 |
|-------------|-------|-----------------|
| **優先序**（`*` / `/` 綁得比 `+` / `-` 緊） | **在 `pre_op` 上延遲插入**（見[第 1 節](#1-delay-insert-on-pre_op--the-precedence-engine)） | `+`/`-` 把帶號的數字 push 進堆疊（延後結算）；`*`/`/` 立刻 pop 出堆疊頂端合併。最終答案 = `sum(stack)`。 |
| **括號**（先算完的子運算式） | **遞迴** — `(` 開一層新作用域，`)` 收掉它 | 遇到 `(` 就對*同一個* queue 遞迴；子呼叫吃到它配對的 `)` 為止，回傳子小計，外層把它當成一個普通的 `curr_num`。 |

**為什麼堆疊能免費解決優先序：** 加法項是以帶號值延後結算（`+num` → push `num`，`-num` → push `-num`），而乘除運算子當場就把前一項吃掉（`stack[-1] *= num`）。因為 `*`/`/` 在那一項被加總*之前*就先改掉它，最後的 `sum(stack)` 自然就尊重了優先序 — 例如 `2 + 3 * 4` 會堆成 `[2, 12]` → `14`，而不是 `20`。

**為什麼是對 `pre_op` 動作，而不是對當前字元：** 讀到一個運算子（或碰到 `)` / 輸入結束）代表我們正在拼的那個數字*結束*了，所以要套用的是那個數字*之前*的運算子。`pre_op` 初始化成 `'+'`，第一個數字就會單純被 push 進去。

**為什麼 `deque` + 遞迴能乾淨處理括號：** `popleft()` 由左往右消耗字元，而這個 queue 是**跨遞迴呼叫共用的**。`helper` 在 `(` 上遞迴時，子呼叫繼續從*同一個* queue pop，遇到 `)` 就 `break`，於是父層剛好從配對的 `)` 之後接著跑。這正是把 LC 227 的延遲插入解法升級成完整 LC 772 解法的關鍵。

```python
# python
# LC 224 / 227 / 772 — universal basic calculator
# IDEA: deque + recursion (parentheses) + delay-insert on pre_op (precedence)
# time = O(n), space = O(n)  (stack + recursion depth)
import collections

class Solution(object):
    def calculate(self, s):
        # strip spaces, scan left-to-right with a shared queue
        queue = collections.deque(s.replace(" ", ""))

        def helper(q):
            stack = []
            curr_num = 0
            op = '+'                     # operator that precedes curr_num; '+' by default

            while q:
                char = q.popleft()

                if char.isdigit():
                    curr_num = curr_num * 10 + int(char)   # build multi-digit number
                elif char == '(':
                    curr_num = helper(q)   # RECURSE: fully evaluate the parenthesised scope

                # flush when we see an operator, a ')', or run out of input
                if char in "+-*/" or char == ')' or not q:
                    if op == '+':
                        stack.append(curr_num)
                    elif op == '-':
                        stack.append(-curr_num)
                    elif op == '*':
                        stack.append(stack.pop() * curr_num)
                    elif op == '/':
                        # truncate toward zero (Python // floors, so divide as float)
                        stack.append(int(float(stack.pop()) / curr_num))
                    curr_num = 0
                    op = char            # remember this operator for the next number

                if char == ')':
                    break                # end of this scope → return sub-total to caller

            return sum(stack)

        return helper(queue)
```

**它怎麼退化成各題：**

| LC | 出現的字元 | 演算法實際做了什麼 |
|----|---------------|--------------------|
| 224 | `+ - ( )` | 只有遞迴 + push/取負；`*`/`/` 分支永遠不會觸發 |
| 227 | `+ - * / ` | 完全不遞迴（沒有 `(`）；純粹靠延遲插入處理優先序 |
| 772 | `+ - * / ( )` | 兩套機制都上場 — 一般情況 |

**陷阱 — 整數除法要往零截斷：** Python 的 `//` 是向下取整（`-7 // 2 == -4`），但這些題目要求往零截斷（`-7 / 2 == -3`）。用 `int(float(stack.pop()) / curr_num)` 才能在中間值為負時得到正確結果。

### 變化型 — 累加結果形式（不用運算元堆疊）

> **這是刻意保留的 LC 224 第二解**：它是*另一種*表述，不是同一套寫法換個拼法。它不用運算元堆疊，而是帶著一個累加的 `res` 加上一個 `sign`，堆疊只存**每個未閉合括號被暫停的 `(res, sign)`** — 所以完全不需要遞迴，堆疊深度也只等於括號深度。它推不到 `*` / `/`，而這正是上面那個萬用形式才是該背起來那個的原因。

```python
# LC 224 Basic Calculator
# V0'
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator/solution/
class Solution:
    def calculate(self, s):

        stack = []
        operand = 0
        res = 0 # For the on-going result
        sign = 1 # 1 means positive, -1 means negative  

        for ch in s:
            if ch.isdigit():

                # Forming operand, since it could be more than one digit
                operand = (operand * 10) + int(ch)

            elif ch == '+':

                # Evaluate the expression to the left,
                # with result, sign, operand
                res += sign * operand

                # Save the recently encountered '+' sign
                sign = 1

                # Reset operand
                operand = 0

            elif ch == '-':

                res += sign * operand
                sign = -1
                operand = 0

            elif ch == '(':

                # Push the result and sign on to the stack, for later
                # We push the result first, then sign
                stack.append(res)
                stack.append(sign)

                # Reset operand and result, as if new evaluation begins for the new sub-expression
                sign = 1
                res = 0

            elif ch == ')':

                # Evaluate the expression to the left
                # with result, sign and operand
                res += sign * operand

                # ')' marks end of expression within a set of parenthesis
                # Its result is multiplied with sign on top of stack
                # as stack.pop() is the sign before the parenthesis
                res *= stack.pop() # stack pop 1, sign

                # Then add to the next operand on the top.
                # as stack.pop() is the result calculated before this parenthesis
                # (operand on stack) + (sign on stack * (result from parenthesis))
                res += stack.pop() # stack pop 2, operand

                # Reset the operand
                operand = 0

        return res + sign * operand
```

## 3) Decode String — LC 394 ⭐⭐⭐⭐

> **計算機的巢狀那一半，但沒有算術。** `k[...]` 就是一層作用域：遇到 `[` 就 push 外層的 `(string, count)` 然後重置，遇到 `]` 就 pop 回來、把 `prev + count * cur` 折疊回去。
> 這套四種情況的掃描（`digit` / `[` / `letter` / `]`）跟 LC 726（Number of Atoms）與 LC 385
> （Mini Parser）是同一套。

```python
# LC 394 Decode String
# V0
# IDEA : STACK
# NOTE : treat before cases separately
#        1) isdigit
#        2) isalpha
#        3) "["
#        4) "]"
# and define num = 0 for dealing with "100a[b]", "10abc" cases
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        """
        NOTE : we deal with 4 cases
            1) digit
            2) "["
            3) alphabet
            4) "]"

        NOTE :
            we use pre_num, pre_string for dealing with previous result
        """
        for c in s:
            # case 1) : digit
            if c.isdigit():
                num = num*10 + int(c)
            # case 2) : "["
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            # case 3) : alphabet
            elif c.isalpha():
                string += c
            # case 4) "]"
            elif c == ']':
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string
```

```java
// java
// LC 394 Decode String

/**
 * Problem: Given an encoded string, return its decoded string.
 *
 * Encoding rule: k[encoded_string] means repeat encoded_string k times
 *
 * Examples:
 * - "3[a]2[bc]" → "aaabcbc"
 * - "3[a2[c]]" → "accaccacc"
 * - "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * Key Insight:
 * - Use stack to handle nested brackets
 * - Process 4 cases: digit, '[', letter, ']'
 * - Build number incrementally (e.g., "100" = 1*10 + 0*10 + 0)
 * - On ']': pop count and previous string, build result
 *
 * Time: O(maxK * N) where maxK is max k value and N is length of decoded string
 * Space: O(N) for the stack
 */

// V0
// IDEA: STACK + 4 CASES (digit, '[', letter, ']')
public String decodeString(String s) {
    if (s == null || s.length() == 0) {
        return "";
    }

    /**
     * NOTE !!!
     * Stack stores alternating pattern:
     * - String (previous accumulated string)
     * - Integer (repeat count)
     * - String (next accumulated string)
     * - Integer (next repeat count)
     * ...
     *
     * Example for "3[a2[c]]":
     * When processing '2[c]':
     *   Stack bottom: ["", 3, "a", 2] Stack top
     */
    Stack<Object> stack = new Stack<>();

    int num = 0;              // Current number being built
    String currentString = ""; // Current string being built

    for (char c : s.toCharArray()) {

        /**
         * Case 1: Digit
         * Build multi-digit numbers (e.g., "100")
         */
        if (Character.isDigit(c)) {
            num = num * 10 + (c - '0');
        }

        /**
         * Case 2: '['
         * Push current string and number to stack
         * Reset for new nested level
         */
        else if (c == '[') {
            // Push current string first, then number
            stack.push(currentString);
            stack.push(num);

            // Reset for new level
            currentString = "";
            num = 0;
        }

        /**
         * Case 3: Letter
         * Append to current string
         */
        else if (Character.isLetter(c)) {
            currentString += c;
        }

        /**
         * Case 4: ']'
         * Pop count and previous string
         * Build repeated string and concatenate
         */
        else if (c == ']') {
            // Pop in reverse order of push
            int repeatCount = (int) stack.pop();
            String prevString = (String) stack.pop();

            /**
             * NOTE !!!
             * Repeat current string repeatCount times
             * Then prepend previous string
             */
            StringBuilder temp = new StringBuilder(prevString);
            for (int i = 0; i < repeatCount; i++) {
                temp.append(currentString);
            }

            currentString = temp.toString();
        }
    }

    return currentString;
}

/**
 * Example Walkthrough: s = "3[a2[c]]"
 *
 * Step 1: c='3' (digit)
 *   num = 3
 *
 * Step 2: c='[' (open bracket)
 *   stack.push("") → stack: [""]
 *   stack.push(3)  → stack: ["", 3]
 *   currentString = "", num = 0
 *
 * Step 3: c='a' (letter)
 *   currentString = "a"
 *
 * Step 4: c='2' (digit)
 *   num = 2
 *
 * Step 5: c='[' (open bracket)
 *   stack.push("a") → stack: ["", 3, "a"]
 *   stack.push(2)   → stack: ["", 3, "a", 2]
 *   currentString = "", num = 0
 *
 * Step 6: c='c' (letter)
 *   currentString = "c"
 *
 * Step 7: c=']' (close bracket)
 *   repeatCount = stack.pop() = 2
 *   prevString = stack.pop() = "a"
 *   temp = "a" + "c" * 2 = "acc"
 *   currentString = "acc"
 *   stack: ["", 3]
 *
 * Step 8: c=']' (close bracket)
 *   repeatCount = stack.pop() = 3
 *   prevString = stack.pop() = ""
 *   temp = "" + "acc" * 3 = "accaccacc"
 *   currentString = "accaccacc"
 *   stack: []
 *
 * Result: "accaccacc"
 */

/**
 * Common Mistakes:
 *
 * 1. Not handling multi-digit numbers (e.g., "100[a]")
 *    ✗ num = c - '0'
 *    ✓ num = num * 10 + (c - '0')
 *
 * 2. Wrong stack push/pop order
 *    ✗ push(num, string) → pop(string, num)  // Wrong!
 *    ✓ push(string, num) → pop(num, string)  // Correct LIFO
 *
 * 3. Forgetting to reset num and currentString after '['
 *    ✗ Only reset one of them
 *    ✓ Reset both: num = 0; currentString = "";
 *
 * 4. Not handling strings outside brackets (e.g., "2[abc]3[cd]ef")
 *    ✓ Continue building currentString for letters outside brackets
 *
 * 5. Using Stack<Object> without proper casting
 *    ✓ Use separate stacks (countStack, stringStack) for type safety
 */

/**
 * Interview Tips:
 *
 * 1. Clarify constraints:
 *    - Is input always valid? (no unmatched brackets)
 *    - Max value of k? (affects overflow considerations)
 *
 * 2. Edge cases to test:
 *    - No brackets: "abc" → "abc"
 *    - Nested brackets: "2[a2[b]]" → "abbabb"
 *    - Multi-digit numbers: "100[a]"
 *    - Mixed: "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * 3. Follow-up questions:
 *    - What if string is invalid? (add validation)
 *    - Can we decode in-place? (no, need stack for nesting)
 *    - How to handle very large k values? (streaming approach)
 */
```

## 4) 運算元堆疊 — 後綴式／依序運算 — LC 150 ⭐⭐⭐⭐

> **和 LC 224 / 227 / 772 的對比**（見[第 2 節](#2-universal-calculator--lc-224--227--772-)）：那幾題解析的是**中綴式**，必須處理優先序 + 括號。**後綴式（RPN）沒有優先序也沒有括號** — token 順序已經把它編碼進去了，所以整個演算法就是*「數字 → push；運算子 → pop 兩個、合併、再 push 回去」*。

```text
Core Idea:
  - token is a NUMBER   -> push
  - token is an OPERATOR-> pop b (right), pop a (left), push f(a, b)
  - answer = the single value left on the stack

Watch-outs:
  - ORDER MATTERS for `-` and `/`: the FIRST pop is the RIGHT operand
    -> a = second pop, b = first pop, compute a - b / a / b
  - Integer division TRUNCATES TOWARD ZERO ("-7 / 2 == -3", not -4)
    -> Java `/` already does this; Python `//` FLOORS, so use int(a / b)
  - A leading '-' can be part of a number ("-11"), not an operator
    -> test membership in the operator SET, don't test `startswith('-')`

Similar LC:
  - LC 150  Evaluate Reverse Polish Notation (canonical operand stack)
  - LC 682  Baseball Game (same stack, ops act on the LAST 1-2 records)
```

```java
// java
// LC 150 - Evaluate Reverse Polish Notation
// IDEA: OPERAND STACK — number pushes, operator pops two and pushes the result
// time = O(n), space = O(n)
public int evalRPN(String[] tokens) {

    Deque<Integer> st = new ArrayDeque<>();

    for (String t : tokens) {
        if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
            /**
             *  NOTE !!!  the FIRST pop is the RIGHT operand
             *  -> "a - b" and "a / b", NOT "b - a"
             */
            int b = st.pop();
            int a = st.pop();
            if (t.equals("+")) {
                st.push(a + b);
            } else if (t.equals("-")) {
                st.push(a - b);
            } else if (t.equals("*")) {
                st.push(a * b);
            } else {
                st.push(a / b); // java int division truncates toward zero
            }
        } else {
            /** NOTE !!! handles negative literals like "-11" for free */
            st.push(Integer.parseInt(t));
        }
    }

    return st.pop();
}
```

```python
# python
# LC 150 - Evaluate Reverse Polish Notation
# IDEA: OPERAND STACK — number pushes, operator pops two and pushes the result
# time = O(n), space = O(n)
class Solution(object):
    def evalRPN(self, tokens):
        ops = {'+', '-', '*', '/'}
        stack = []
        for t in tokens:
            if t in ops:
                # NOTE !!! first pop = RIGHT operand
                b = stack.pop()
                a = stack.pop()
                if t == '+':
                    stack.append(a + b)
                elif t == '-':
                    stack.append(a - b)
                elif t == '*':
                    stack.append(a * b)
                else:
                    # NOTE !!! truncate toward zero ( // would FLOOR )
                    stack.append(int(a / b))
            else:
                stack.append(int(t))   # int() also parses "-11"
        return stack[-1]
```

### 變化型 — 運算子作用在最後幾筆紀錄上 — LC 682

> **變化點**：一樣是運算元堆疊，但這裡的「運算子」是對紀錄的編輯 — `C` 是撤銷（pop）、`D` 把頂端加倍、`+` 把頂端兩筆相加 — 而且答案是 `sum(stack)`，不是最後剩下的那個值。

```java
// java
// LC 682 - Baseball Game
// IDEA: OPERAND STACK — C / D / '+' rewrite the tail of the record list
// time = O(n), space = O(n)
public int calPoints(String[] operations) {
    // NOTE: use a List as the stack — '+' needs the last TWO entries
    List<Integer> scores = new ArrayList<>();
    for (String op : operations) {
        int n = scores.size();
        if (op.equals("C")) {
            scores.remove(n - 1);                       // undo last
        } else if (op.equals("D")) {
            scores.add(2 * scores.get(n - 1));          // double last
        } else if (op.equals("+")) {
            scores.add(scores.get(n - 1) + scores.get(n - 2)); // sum last two
        } else {
            scores.add(Integer.parseInt(op));
        }
    }
    int sum = 0;
    for (int x : scores) {
        sum += x;
    }
    return sum;
}
```

```python
# python
# LC 682 - Baseball Game
# IDEA: OPERAND STACK — C / D / '+' rewrite the tail of the record list
# time = O(n), space = O(n)
class Solution(object):
    def calPoints(self, operations):
        stack = []
        for op in operations:
            if op == 'C':
                stack.pop()                       # undo last
            elif op == 'D':
                stack.append(2 * stack[-1])       # double last
            elif op == '+':
                stack.append(stack[-1] + stack[-2])  # sum last two
            else:
                stack.append(int(op))
        return sum(stack)
```

## 5) 總結與快速查詢

### 哪一題該用哪種表述？

| LC | 題目 | Tokens | 機制 |
|----|---------|--------|-----------|
| 227 | Basic Calculator II | `+ - * /` | 在 `pre_op` 上延遲插入；完全不遞迴 |
| 224 | Basic Calculator I | `+ - ( )` | 萬用形式，或累加結果變化型 |
| 772 | Basic Calculator III | `+ - * / ( )` | 萬用形式 — 兩套機制都上場 |
| 394 | Decode String | `k[ ]`、字母 | 每層作用域 push `(string, count)`，在 `]` 折疊 |
| 726 | Number of Atoms | `( )`、數字、名稱 | LC 394 那套掃描，每層作用域配一個計數表 |
| 385 | Mini Parser | `[ ]`、數字、`,` | LC 394 那套掃描，堆疊存的是 `NestedInteger` frame |
| 150 | Evaluate RPN | 後綴式 | 運算元堆疊：運算子 pop 兩個 |
| 682 | Baseball Game | 紀錄 + `C` / `D` / `+` | 運算元堆疊，運算改寫尾端 |

### 值得反覆演練的陷阱

| 陷阱 | 解法 |
|---|---|
| 多位數數字 | `num = num * 10 + int(ch)`，而且每次結算後要重置 `num = 0` |
| **最後一個**數字永遠沒被結算 | 把「輸入結束」也做成觸發點（`or not q`、`i == len(s) - 1`） |
| `-7 / 2` 必須是 `-3`，不是 `-4` | Python 的 `//` 是**向下取整** — 改用 `int(a / b)` / `int(float(x) / y)` |
| RPN 運算元順序 | **第一個** pop 出來的是**右**運算元 |
| 輸入裡的空白 | 一開始就整批清掉（`s.replace(" ", "")`），別在每個分支各防一次 |
| token 裡開頭的 `-` | 用運算子**集合**做成員判斷；`int("-11")` 本來就解析得動 |
