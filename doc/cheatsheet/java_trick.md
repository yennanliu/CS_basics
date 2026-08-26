# Java Tricks & Idioms

> **Scope** — The Java language semantics that decide whether a correct algorithm produces a correct answer: characters as integers, value vs reference, and integer arithmetic. The library APIs live in two companion sheets.
> **See also**: [java_trick_collections.md](./java_trick_collections.md) — arrays, lists, maps, queues, heaps, stacks and pairs; [java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String and StringBuilder work, and everything comparator-shaped; [python_trick.md](./python_trick.md) — the same ground in Python; [complexity_cheatsheet.md](./complexity_cheatsheet.md) — the Big-O lookup tables for every structure these APIs wrap.

## LeetCode Problem Lists

- [Java](https://leetcode.com/problem-list/java/)

## Overview

Three families of bug account for almost every "the logic was right but the answer was wrong"
in Java interviews, and all three are language semantics rather than algorithm choices:

| Family | The bug | Fixed in |
|---|---|---|
| **Characters are integers** | `charAt(i)` gives you a `char`, and using it where you meant a digit silently gives you its ASCII code | [Characters & Digits](#characters--digits) |
| **Value vs reference** | you mutate an object you thought you had copied, or you copy a primitive you thought you were sharing | [Value vs Reference](#value-vs-reference--the-rule-behind-most-java-bugs-here) |
| **Integer arithmetic** | `/` truncates toward zero, `%` can return a negative, and `int` overflows at 2^31 | [Integer Math & Operators](#integer-math--operators) |

### References

- [Java Documentation](https://docs.oracle.com/en/java/)
- [LeetCode Java Solutions](https://leetcode.com/problemset/all/?languageTags=java)


## Characters & Digits

### `charAt`, comparisons and letter → index


**Key Methods**: `charAt()`, character comparisons, ASCII operations

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

**Performance Note**: `charAt(i)` is O(1) for strings, making it efficient for character-by-character processing.


### Char digit to integer value (`char - '0'`)


**Key Concept**: Convert a character digit ('0'-'9') to its integer value using ASCII subtraction.

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

**Common Pattern: Increment/Decrement Digit Characters (LC 752 - Open the Lock)**

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

**Quick Reference Table:**

| Operation | Code | Example |
|-----------|------|---------|
| Char → Int | `c - '0'` | `'7' - '0'` → `7` |
| Int → Char | `(char)('0' + n)` | `(char)('0' + 7)` → `'7'` |
| Increment (wrap) | `(digit + 1) % 10` | `9 + 1` → `0` |
| Decrement (wrap) | `(digit - 1 + 10) % 10` | `0 - 1` → `9` |

**Why `+ 10` in decrement?**
```java
// Without +10: (0 - 1) % 10 = -1  ❌ (negative!)
// With +10:    (0 - 1 + 10) % 10 = 9  ✅

// The +10 ensures the value is always positive before modulo
int valMinus = (digit - 1 + 10) % 10;
```

**Comparison: Letter vs Digit Mapping**

| Type | Char → Index | Index → Char | Range |
|------|--------------|--------------|-------|
| **Letters** | `c - 'a'` | `(char)('a' + i)` | 'a'-'z' → 0-25 |
| **Digits** | `c - '0'` | `(char)('0' + i)` | '0'-'9' → 0-9 |


### `charAt()` returns a `char`, not the digit value ⭐


> **Pitfall**: `new Integer(s.charAt(i))` gives the ASCII code (e.g. 51), not the digit (e.g. 3).

#### Wrong

```java
int val1 = new Integer(s.charAt(i));
// s.charAt(i) = '3' → '3' is char → ASCII 51
// val1 = 51 ❌
```

#### Correct

```java
int val1 = s.charAt(i) - '0';
// '3' - '0' = 51 - 48 = 3 ✅
```

#### Why: `char` is stored as ASCII/Unicode value

| Expression    | Result       |
|---------------|--------------|
| `'3'`         | 51 (ASCII)   |
| `'0'`         | 48           |
| `'3' - '0'`  | 3            |

#### `new Integer(...)` is also deprecated

```java
new Integer(...)          // ❌ Deprecated, unnecessary object creation, slower
Integer.valueOf(...)      // ✅ If you need an Integer object
int x = ...;             // ✅ Prefer primitives
```

#### For multi-digit substrings, `parseInt` is correct

```java
int val2 = Integer.parseInt(s.substring(i - 2, i));
// ✅ Correct — parsing a String, not a char
```

#### TL;DR

```java
new Integer(s.charAt(i))  // ❌ gives ASCII (e.g. 51)
s.charAt(i) - '0'         // ✅ gives actual digit (e.g. 3)
```

---

### Mapping letters to array indices (`c - 'a'`)

Because `char` is an integer type, subtracting `'a'` turns a lowercase letter into a
**0-based index**, which is what lets a 26-slot array replace a `HashMap<Character, ?>`:

```java
// java
// rank each letter of a custom alphabet -- LC 269 Alien Dictionary
String order = "hlabcdefgijkmnopqrstuvwxyz";
int[] orderMap = new int[26];
for (int i = 0; i < order.length(); i++) {
    orderMap[order.charAt(i) - 'a'] = i;   // 'h' - 'a' == 7  ->  orderMap[7] = 0
}
```

The reason to bother is complexity, not brevity: the array read is O(1), whereas the obvious
alternative `order.indexOf(c)` rescans the string and is **O(n) on every lookup**. Inside a
comparison loop that is the difference between O(n) and O(n²).

The same subtraction applies to digits with `'0'` instead of `'a'` — see the letter-vs-digit
table above, which is the one to memorise.


### Iterating a character range


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


## Value vs Reference — the rule behind most Java bugs here

### `equals()` vs `==` — when to use which ⭐


> **Core Rule**: `==` compares **references** (are they the same object?). `equals()` compares **content** (do they have the same value?).

#### The Rule — `equals()` vs `==`

```text
==        → compares memory addresses (reference identity)
equals()  → compares logical content (value equality)
```

#### Comparing Collections (List, Set, Map)

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

#### Comparing Strings

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

#### Comparing Wrapper Types (Integer, Long, etc.)

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

#### Comparing Primitives

```java
int i = 5;
int j = 5;
i == j          // ✅ correct — primitives have no .equals()
                // Primitives are ALWAYS compared by value with ==
```

#### Summary Table — `equals()` vs `==`

| Type | Use `==` | Use `equals()` | Pitfall |
|------|----------|----------------|---------|
| `int`, `long`, `char`, ... (primitives) | **Yes** | N/A (no method) | None |
| `String` | **No** | **Yes** | Literals may share ref, but don't rely on it |
| `Integer`, `Long`, ... (wrappers) | **No** | **Yes** | `==` works for -128..127 only (cache) |
| `List`, `Set`, `Map` | **No** | **Yes** | `==` checks identity, not content |
| Custom objects | **No** | **Yes** (if overridden) | Default `equals()` is same as `==` |
| `null` check | **Yes** (`x == null`) | **No** (NPE!) | Always use `==` for null check |

#### Interview Quick Rule

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

### Primitive vs reference types in recursion — the backtracking rule ⭐


> **Core Rule**: Primitives are pass-by-value → each call gets its own copy → **no backtracking needed**.
> Reference types (collections, arrays, objects) are pass-by-reference → shared state → **must backtrack**.

#### The Rule — primitives vs references in recursion

```text
Primitive param (int, long, double...)  →  copy per call  →  NO backtrack needed
Reference param (List, int[], HashMap)  →  shared object  →  MUST backtrack (add + remove)
Global / instance variable              →  shared state   →  MUST backtrack
```

#### Case 1: Primitive — No Backtracking Needed (LC 112 Path Sum)

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

**Memory model:**
```text
Stack frame:  getPathHelper(node=4, curSum=5)
              ├── newSum = 9     ← local to THIS frame
              ├── calls getPathHelper(node.left, newSum=9)
              │     └── newSum = 20  ← different frame, isolated
              └── curSum is still 5 when left call returns ✅
```

#### Case 2: Global Variable — Backtracking IS Needed (LC 112 V0-2)

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

#### Case 3: Reference Type (List) — Backtracking IS Needed (LC 113 Path Sum II)

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

#### Case 4: StringBuilder — Backtracking IS Needed (LC 988 Smallest String Starting From Leaf)

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

#### Summary Table — primitives vs references in recursion

| State Type | Example | Backtrack? | Reason |
|---|---|---|---|
| Primitive param | `int curSum` | **No** | Each call gets own copy |
| Wrapper param (autoboxed) | `Integer curSum` | **No** | Autoboxing creates new object |
| Local variable | `int newSum = curSum + val` | **No** | Belongs to current stack frame only |
| Instance/global variable | `this.curSum` | **Yes** | Shared across all calls |
| Collection param | `List<Integer> path` | **Yes** | Reference, mutated in-place |
| Array param | `int[] path` | **Yes** | Reference, mutated in-place |
| StringBuilder param | `StringBuilder sb` | **Yes** | Reference, mutated in-place via `append()`/`deleteCharAt()` |

#### Interview Tips

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

### Recursion parameter passing


```java
// LC 104
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/MaximumDepthOfBinaryTree.java
```

**Important Concept**: In Java, primitives are **passed by value** (creates copies).

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

**Key Takeaway**: When you need to track state across recursive calls, either use instance variables or design the recursion to return and combine values.


### Passing a value out of a method — the mutable-holder pattern


> **Core Concept**: Reference types (StringBuilder, List, int[], Map, etc.) are passed by **reference**, not by value. Changes made inside a function persist after the function returns.

#### Pattern 1: StringBuilder for Path/String Building (LC 694)

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

**Memory Model:**
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

#### Pattern 2: List for Collecting Results (LC 113 Path Sum II)

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

**Key Difference from Primitives:**
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

#### Pattern 3: General Pattern — Create, Pass, Modify, Use

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

#### Common Reference Types for This Pattern

| Type | Modification Methods | Backtrack Required? | Use Case |
|------|----------------------|-------------------|----------|
| `StringBuilder` | `append(x)`, `setCharAt(i, c)`, `deleteCharAt(i)` | ✅ Yes | String building with backtracking |
| `List<T>` | `add(x)`, `remove(i)`, `set(i, x)` | ✅ Yes | Path/result collection |
| `int[]` / `char[]` | `arr[i] = value` | ✅ Yes | Array modification |
| `Map<K,V>` | `put(k, v)`, `remove(k)` | ✅ Yes | Frequency tracking |
| `Queue<T>` | `add(x)`, `poll()`, `offer(x)` | ✅ Maybe | BFS level-by-level |
| `Set<T>` | `add(x)`, `remove(x)` | ✅ Yes | Visited tracking |
| Primitive `int`, `long` | N/A (pass-by-value) | ❌ No | Only for return or instance vars |
| `String` | N/A (immutable) | ❌ No | Use StringBuilder instead |

**When to Backtrack:**
```text
Rule: If the parameter is a reference type that gets MODIFIED, you must UNDO the modification.

Path/List building:  path.add(val) → must do path.remove(...)
StringBuilder:       sb.append(...) → must do sb.deleteCharAt(...)
Array modification:  arr[i] = val   → must do arr[i] = oldVal
Map/Set:             data.add(x)    → must do data.remove(x)

Primitives:          No backtrack needed (they're copied)
```

#### Pattern 4: List Collection (LC 131 Palindrome Partitioning)

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

**Critical Distinction:**
```java
// ❌ WRONG: Adds reference to currentList (not a snapshot)
result.add(currentList);  // All entries point to same list!

// ✅ CORRECT: Add a copy
result.add(new ArrayList<>(currentList));  // Each entry is independent
```

### Primitive types are passed by value


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

### Modifying a custom class's fields directly (`v.field -= 1`) ⭐


> **Core Question**: When can you write `v.cnt -= 1` and when can't you?

#### When you CAN modify directly

All three conditions must hold:

1. **Field is accessible** (not `private`, or you are inside the class)
2. **Field is not `final`**
3. **Reference is not `null`**

```java
class ValCnt {
    char val;
    int cnt;           // package-private, non-final
    ValCnt(char val, int cnt) { this.val = val; this.cnt = cnt; }
}

ValCnt v = new ValCnt('a', 3);
v.cnt -= 1;   // ✅ allowed: accessible + non-final + non-null
```

#### When you CANNOT modify directly

**Case 1 — `private` field** (outside the class)
```java
class ValCnt {
    private int cnt;   // private!
}

v.cnt -= 1;   // ❌ compile error — use a setter/method instead
```

**Case 2 — `final` field**
```java
class ValCnt {
    final int cnt;
}

v.cnt -= 1;   // ❌ compile error — cannot assign to final variable
```

**Case 3 — null reference**
```java
ValCnt v = null;
v.cnt -= 1;   // ❌ NullPointerException at runtime
```

#### Common misconception: `final` reference vs `final` field

```java
final ValCnt v = new ValCnt('a', 3);

v.cnt -= 1;              // ✅ allowed — final only locks the REFERENCE
v = new ValCnt('b', 1);  // ❌ compile error — cannot reassign final reference
```

`final` on the variable means you cannot point `v` to a different object.
It does **not** prevent mutating the object's fields.

#### `Integer` (wrapper) field — works but autoboxes

```java
class Holder { Integer cnt; }

Holder h = new Holder();
h.cnt = 3;
h.cnt -= 1;  // ✅ works, but really: h.cnt = Integer.valueOf(h.cnt.intValue() - 1)
             //    creates a new Integer object; fails if field is final
```

#### Summary table — mutating a custom class in place

| Situation | `v.cnt -= 1` allowed? |
|-----------|----------------------|
| `int cnt` (package-private) | ✅ yes |
| `private int cnt` (outside class) | ❌ compile error |
| `final int cnt` | ❌ compile error |
| `final ValCnt v` (reference is final) | ✅ yes — field still mutable |
| `v == null` | ❌ NullPointerException |
| `Integer cnt` (wrapper) | ✅ works, autoboxes to new object |

#### Interview trap: `v.cnt--` vs `--v.cnt` vs `v.cnt -= 1`

All three decrement `cnt` by 1. The difference is the **returned expression value**:

```java
v.cnt = 3;

int a = v.cnt--;   // a = 3  (returns BEFORE decrement), v.cnt = 2
int b = --v.cnt;   // b = 1  (returns AFTER  decrement), v.cnt = 1
v.cnt -= 1;        // no return value used, v.cnt = 0
```

Use `v.cnt -= 1` when you only care about the side-effect, not the return value.

---

### Assignment copies the reference, not the object


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

### Re-constructing nodes instead of mutating them


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

### Getting a defensive copy of the current instance

```java
// java
// LC 46
List<List<Integer>> ans = new ArrayList<>();
List<Integer> cur = new ArrayList<>();
//...
ans.add(new ArrayList<>(cur));
//...
```

## Integer Math & Operators

### `ceil` vs `floor` — definition


| Operation | Meaning | Example |
|-----------|---------|---------|
| `Math.ceil(x)` | Round **up** to nearest integer | `ceil(7.0/3)` → `3.0` |
| `Math.floor(x)` | Round **down** to nearest integer | `floor(7.0/3)` → `2.0` |
| `(int)(a/b)` | **Truncate toward zero** (same as floor for positives) | `7/3` → `2` |

```java
// Using double division + Math.ceil / floor
System.out.println(Math.ceil(7.0 / 3));   // 3.0  (rounds UP)
System.out.println(Math.floor(7.0 / 3));  // 2.0  (rounds DOWN)
System.out.println(7 / 3);               // 2    (integer truncation = floor for positives)

// Cast result back to int
int ceilVal  = (int) Math.ceil((double) 7 / 3);  // 3
int floorVal = (int) Math.floor((double) 7 / 3); // 2
```

**Key gotcha**: integer division in Java **always truncates toward zero** (= floor for positive numbers).
```java
7  / 3  →  2   // floor (positive)
-7 / 3  → -2   // truncates toward zero (NOT floor, which would be -3)
```

---

### Integer ceiling division — no `double` needed ⭐


**Formula**: `ceil(a / b)` using only integers:

```java
int ceilDiv = (a + b - 1) / b;
```

**Why it works:**
```text
ceil(a / b)  =  (a + b - 1) / b   (integer division, b > 0)

Example: a=7, b=3
  (7 + 3 - 1) / 3  =  9 / 3  =  3  ✅  (Math.ceil(7.0/3) = 3)

Example: a=6, b=3 (exact division)
  (6 + 3 - 1) / 3  =  8 / 3  =  2  ✅  (Math.ceil(6.0/3) = 2)

Example: a=1, b=5
  (1 + 5 - 1) / 5  =  5 / 5  =  1  ✅  (Math.ceil(1.0/5) = 1)
```

**Comparison: two ways to compute ceiling division**

```java
// Method 1: Integer trick (faster, no casting)
int ceil1 = (val + d - 1) / d;

// Method 2: double cast + Math.ceil (clearer intent, slightly slower)
int ceil2 = (int) Math.ceil((double) val / d);

// Both produce identical results for positive val and d
```

**Classic usage — LC 1283 Find the Smallest Divisor Given a Threshold:**
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

### Integer floor division


For positive integers, `/` already gives floor:
```java
int floorDiv = a / b;  // works when a >= 0 and b > 0
```

For **negative numbers**, use `Math.floorDiv`:
```java
Math.floorDiv(-7, 3);   // -3  (true floor)
-7 / 3;                 // -2  (truncation, NOT floor!)

Math.floorDiv(7, 3);    //  2  (same as 7/3 for positives)
```

---

### Ceiling / floor quick reference


| Goal | Code | Notes |
|------|------|-------|
| Ceil (double) | `(int) Math.ceil((double) a / b)` | Readable, casting required |
| Ceil (integer trick) | `(a + b - 1) / b` | Fast, no casting, positive only |
| Floor (positive) | `a / b` | Integer division truncates |
| Floor (any sign) | `Math.floorDiv(a, b)` | Handles negatives correctly |
| Round (half-up) | `(int) Math.round((double) a / b)` | Nearest integer |
| Mid without overflow | `l + (r - l) / 2` | Standard binary search midpoint |

---

### Classic LC problems using ceiling division


| LC | Problem | Ceiling Division Usage |
|----|---------|----------------------|
| **1283** | Find the Smallest Divisor Given a Threshold | `(val + d - 1) / d` per element |
| **1011** | Capacity To Ship Packages Within D Days | `(wt + cap - 1) / cap` days needed |
| **875**  | Koko Eating Bananas | `(pile + k - 1) / k` hours per pile |
| **2064** | Minimized Maximum of Products Distributed | `(n + m - 1) / m` ceiling per group |

**Pattern**: These are all **binary search on the answer** problems where the check function requires ceiling division to count "how many X fit in Y".

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

### Use `long` to avoid `int` overflow


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


### Max and min of three numbers


```java
// java

// LC 152
max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
```

### Counting set bits in an integer

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

### Pre-calculating perfect squares up to N


> **Trick**: Pre-compute all perfect squares ≤ N into a list, then iterate over the list instead of recalculating `i * i` each time. Useful in BFS/DP problems like LC 279 (Perfect Squares).

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

### Random integers with `random.nextInt`

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

## Quick Reference

### Most common patterns


#### Data Structure Initialization
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

#### Essential Conversions
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

#### Common Operations
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

### Performance tips


| Operation | Efficient Approach | Avoid |
|-----------|-------------------|-------|
| **String Building** | `StringBuilder` | String concatenation in loops |
| **Character Access** | `toCharArray()` then iterate | `charAt()` in tight loops |
| **Sorting** | `Arrays.sort()`, `Collections.sort()` | Stream sorting for large data |
| **Array Printing** | `Arrays.toString()`, `Arrays.deepToString()` | Manual iteration |
| **Character Mapping** | `char - 'a'` | `indexOf()` repeated calls |

### Common LeetCode patterns


#### Frequency Counting
```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.put(c, freq.getOrDefault(c, 0) + 1);
}
```

#### Two Pointers with Character Comparison
```java
int left = 0, right = s.length() - 1;
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) return false;
    left++;
    right--;
}
```

#### Priority Queue for Top-K Problems
```java
PriorityQueue<Integer> heap = new PriorityQueue<>();
for (int num : nums) {
    heap.offer(num);
    if (heap.size() > k) heap.poll();
}
```

### Memory management


- **Primitive arrays**: More memory efficient than object arrays
- **ArrayList**: Automatically resizes, initial capacity matters for large datasets  
- **StringBuilder**: Use for string concatenation in loops
- **Character arrays**: More efficient than String manipulation for character processing
