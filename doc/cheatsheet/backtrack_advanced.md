# Advanced Backtracking

> **Scope** — Hard-tier backtracking that carries extra state through the recursion — a Trie node, the previous operand, a deletion budget — plus constraint propagation, memoised search and the generic partitioning templates a first pass should skip; none of the must-know shapes are repeated here.
> **See also**: [backtrack.md](./backtrack.md) — the must-know templates and the decision table; [backtrack_examples.md](./backtrack_examples.md) — worked solutions for those templates; [trie.md](./trie.md) — the Trie itself; [dp.md](./dp.md) / [knapsack.md](./knapsack.md) — where memoised partitioning ends up.

## LeetCode Problem Lists

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Trie](https://leetcode.com/problem-list/trie/)

## Overview

Three templates that do **not** reduce to "pick / skip over an index". Each one carries an
extra piece of state through the recursion — a **Trie node**, the **previous operand**, or a
**deletion budget** — and that extra state is the whole trick.

Everything on this page assumes the [choose → explore → un-choose skeleton](./backtrack.md#template-1-choose--explore--un-choose-)
is already automatic. Come here when the basic shape is not enough: when re-running the simple
template per input is too slow, when a partial solution has to be *scored* rather than
collected, or when the search only becomes tractable after constraints are propagated.

### Key Properties

- **Complexity**: exponential in the worst case, same as any backtrack — see the [Time Complexity by Problem Type](./backtrack.md#time-complexity-by-problem-type) table
- **Core Idea**: the extra state carried through the recursion *is* the trick
- **When to Use**: hard-tier interviews; a first pass at the topic should skip this page

## Problem Categories

| Template | Extra state carried | Worked example |
|----------|--------------------|----------------|
| Trie-pruned grid search | current `TrieNode` | LC 212 Word Search II |
| Expression building | `prev` operand (for `*` precedence) | LC 282 Expression Add Operators |
| Deletion-budget backtracking | `(l, r)` chars still removable | LC 301 Remove Invalid Parentheses |

Plus the partitioning family, whose generic templates and memoised form live here rather than
on the main sheet:

**Definition**: Divide input into groups or segments based on certain criteria.

**Common Partitioning Types**:

**1. Equal Sum Partitioning**
- Divide array into groups with equal sums
- Examples: LC 416 (Partition Equal Subset Sum), LC 698 (K Equal Sum Subsets)

**2. Palindromic Partitioning**
- Split string into palindromic substrings
- Examples: LC 131 (Palindrome Partitioning), LC 132 (Palindrome Partitioning II)

**3. Subset Partitioning**
- Group elements based on constraints
- Examples: LC 90 (Subsets II), LC 47 (Permutations II)


## Templates & Algorithms

### Template 1: Trie + Grid Backtracking — LC 212 Word Search II ⭐⭐⭐⭐


**Key Idea**: LC 79 asks for *one* word — re-running it per word is `O(W · M · N · 4^L)`.
Instead push **all words into a Trie** and walk the grid **once**, carrying the current Trie
node alongside `(r, c)`. A cell branch dies the moment the Trie has no child for that letter.

**Three moves that matter**
1. **Trie node as the "index"** — replaces `idx` into a single word; one DFS covers all words.
2. **In-place marking** (`board[r][c] = '#'`, restore after) — no `visited` matrix needed.
3. **Leaf pruning** — after recursing, if a node has no children left, unlink it from its
   parent. This keeps the Trie shrinking and is what makes the worst case tolerable.

**Dedup twist**: set `node.word = null` right after collecting it, instead of using a `Set`.

```java
// java
// LC 212 - Word Search II
// time = O(M*N*4^(L-1)), space = O(K) where K = total chars in words, L = max word len
// IDEA: build a Trie of all words, then ONE DFS over the grid carrying the Trie node.
//       in-place '#' marking for visited + prune dead Trie leaves after backtracking.
class TrieNode {
    TrieNode[] next = new TrieNode[26];
    String word = null;   // non-null ONLY at the end of a word
}

class Solution {
    private List<String> res = new ArrayList<>();
    private char[][] board;

    public List<String> findWords(char[][] board, String[] words) {
        this.board = board;

        /** NOTE !!! build Trie first -> all words share one traversal */
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode node = root;
            for (char ch : w.toCharArray()) {
                int i = ch - 'a';
                if (node.next[i] == null) node.next[i] = new TrieNode();
                node = node.next[i];
            }
            node.word = w;
        }

        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                dfs(r, c, root);

        return res;
    }

    private void dfs(int r, int c, TrieNode parent) {
        char ch = board[r][c];

        /** NOTE !!! double exit: already visited ('#') OR Trie has no such branch */
        if (ch == '#' || parent.next[ch - 'a'] == null) return;

        TrieNode node = parent.next[ch - 'a'];
        if (node.word != null) {
            res.add(node.word);
            node.word = null;   // dedup: collect each word only once (no Set needed)
        }

        board[r][c] = '#';      // mark (in-place, saves the visited matrix)
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < board.length && nc >= 0 && nc < board[0].length)
                dfs(nr, nc, node);
        }
        board[r][c] = ch;       // undo (backtrack)

        /** NOTE !!! prune: a fully-consumed leaf can never match again -> unlink it */
        boolean dead = node.word == null;
        for (TrieNode t : node.next) if (t != null) { dead = false; break; }
        if (dead) parent.next[ch - 'a'] = null;
    }
}
```

```python
# python
# LC 212 - Word Search II
# time = O(M*N*4^(L-1)), space = O(K) where K = total chars in words, L = max word len
# IDEA: Trie of all words + ONE grid DFS carrying the Trie node.
#       in-place '#' marking + drop dead Trie leaves after backtracking.
class TrieNode:
    def __init__(self):
        self.children = {}
        self.word = None      # non-None ONLY at the end of a word

class Solution:
    def findWords(self, board, words):
        root = TrieNode()
        for w in words:
            node = root
            for ch in w:
                node = node.children.setdefault(ch, TrieNode())
            node.word = w

        rows, cols = len(board), len(board[0])
        res = []

        def dfs(r, c, parent):
            ch = board[r][c]

            ### NOTE !!! Trie decides whether this branch is alive
            node = parent.children.get(ch)
            if not node:
                return

            if node.word:
                res.append(node.word)
                node.word = None          # dedup without a set

            board[r][c] = '#'             # mark visited (in-place)
            for nr, nc in ((r+1, c), (r-1, c), (r, c+1), (r, c-1)):
                if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] != '#':
                    dfs(nr, nc, node)
            board[r][c] = ch              # undo (backtrack)

            ### NOTE !!! prune dead leaf -> Trie shrinks as words are found
            if not node.children:
                parent.children.pop(ch)

        for r in range(rows):
            for c in range(cols):
                dfs(r, c, root)
        return res
```

> **Contrast with LC 79**: LC 79 returns `True` up the stack the instant it matches (early
> exit). LC 212 must keep exploring after a hit, because a longer word may extend the same path.

### Template 2: Expression Building (operator insertion) — LC 282 Expression Add Operators ⭐⭐⭐⭐


**Key Idea**: at every gap between digits, branch on `+ | - | *` (and on how many digits the
current operand eats). The only hard part is `*` **precedence**: you cannot just multiply into
the running total, because `2 + 3 * 2` must be `8`, not `10`.

**The `prev` trick** — carry the *last operand as it was applied*:

```text
choose '+' v :   cur = cur + v            prev = +v
choose '-' v :   cur = cur - v            prev = -v
choose '*' v :   cur = cur - prev + prev*v   prev = prev*v
                       ^^^^^^^^^ undo the last operand, re-apply it multiplied
```

**Two guards you must not forget**
- **Leading zero**: `if j > idx and num[idx] == '0': break` → `"05"` is never a valid operand.
- **Overflow**: use `long` in Java — intermediate products blow past `int`.

```java
// java
// LC 282 - Expression Add Operators
// time = O(4^N * N), space = O(N) recursion depth (+ output)
// IDEA: at each split point try every operand length, then branch on + - * .
//       carry `prev` (last applied operand) so '*' can UNDO it and re-apply multiplied.
class Solution {
    private List<String> res = new ArrayList<>();
    private String num;
    private long target;

    public List<String> addOperators(String num, int target) {
        this.num = num;
        this.target = target;
        if (num == null || num.isEmpty()) return res;
        dfs(0, new StringBuilder(), 0L, 0L);
        return res;
    }

    // cur  = value of the expression built so far
    // prev = last operand AS APPLIED (already signed / already multiplied)
    private void dfs(int idx, StringBuilder expr, long cur, long prev) {
        if (idx == num.length()) {
            if (cur == target) res.add(expr.toString());
            return;
        }

        for (int j = idx; j < num.length(); j++) {

            /** NOTE !!! no leading zero -> "0" ok, "05" not */
            if (j > idx && num.charAt(idx) == '0') break;

            String s = num.substring(idx, j + 1);
            long v = Long.parseLong(s);   // NOTE !!! long, int overflows
            int len = expr.length();      // remember length -> cheap backtrack

            if (idx == 0) {
                // first operand: no operator in front of it
                dfs(j + 1, expr.append(s), v, v);
                expr.setLength(len);
            } else {
                dfs(j + 1, expr.append('+').append(s), cur + v, v);
                expr.setLength(len);

                dfs(j + 1, expr.append('-').append(s), cur - v, -v);
                expr.setLength(len);

                /** NOTE !!! '*' : remove prev from cur, then add prev*v back */
                dfs(j + 1, expr.append('*').append(s), cur - prev + prev * v, prev * v);
                expr.setLength(len);
            }
        }
    }
}
```

```python
# python
# LC 282 - Expression Add Operators
# time = O(4^N * N), space = O(N) recursion depth (+ output)
# IDEA: try every operand length at each split, branch on + - * .
#       carry `prev` (last applied operand) so '*' can UNDO it and re-apply multiplied.
class Solution:
    def addOperators(self, num, target):
        res = []
        n = len(num)

        # cur  : value of expression so far
        # prev : last operand AS APPLIED (already signed / already multiplied)
        def dfs(idx, expr, cur, prev):
            if idx == n:
                if cur == target:
                    res.append(expr)
                return

            for j in range(idx, n):

                ### NOTE !!! no leading zero -> "0" ok, "05" not
                if j > idx and num[idx] == '0':
                    break

                s = num[idx:j+1]
                v = int(s)

                if idx == 0:
                    dfs(j + 1, s, v, v)                 # first operand: no operator
                else:
                    dfs(j + 1, expr + '+' + s, cur + v, v)
                    dfs(j + 1, expr + '-' + s, cur - v, -v)
                    ### NOTE !!! '*' : undo prev, re-apply as prev*v
                    dfs(j + 1, expr + '*' + s, cur - prev + prev * v, prev * v)

        if num:
            dfs(0, "", 0, 0)
        return res
```

> **Same shape, different problem**: LC 679 (24 Game) is the other "build an expression"
> backtrack — there you pick **two operands out of the list**, apply an op, recurse on the
> shrunken list (and use a float epsilon compare instead of `==`).

### Template 3: Deletion-Budget Backtracking — LC 301 Remove Invalid Parentheses ⭐⭐⭐⭐


**Key Idea**: "remove the **minimum** number of chars" → don't search all removals. First
**count** exactly how many `(` and `)` are surplus in one pass, then backtrack with that count
as a **budget**. Any string reaching the end with `budget == 0` is automatically minimal.

**Counting the surplus** (one pass):
```text
'('  -> l++
')'  -> if l > 0: l--   (matched)   else: r++   (unmatched close)
end  -> l = surplus '(' , r = surplus ')'
```

**Per char, exactly two branches**: *delete it* (only if its budget is > 0) or *keep it*
(keep `)` only while `open > 0`, else the prefix is already invalid → prune).

```java
// java
// LC 301 - Remove Invalid Parentheses
// time = O(2^N), space = O(N) recursion depth (+ output)
// IDEA: 1st pass counts surplus '(' = l and ')' = r  ->  that is the DELETION BUDGET.
//       then per char: branch "delete" (budget--) vs "keep"; a full string with
//       l == r == open == 0 is guaranteed minimal. HashSet dedups equal results.
class Solution {
    private Set<String> res = new HashSet<>();
    private String s;

    public List<String> removeInvalidParentheses(String s) {
        this.s = s;

        /** NOTE !!! count surplus brackets FIRST -> that fixes the removal count */
        int l = 0, r = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '(') l++;
            else if (ch == ')') {
                if (l > 0) l--;   // matched
                else r++;         // unmatched ')'
            }
        }

        dfs(0, l, r, 0, new StringBuilder());
        return new ArrayList<>(res);
    }

    // l, r  = '(' and ')' still allowed to be DELETED
    // open  = unmatched '(' currently kept in path
    private void dfs(int i, int l, int r, int open, StringBuilder path) {
        if (i == s.length()) {
            if (l == 0 && r == 0 && open == 0) res.add(path.toString());
            return;
        }

        char ch = s.charAt(i);

        // ---- branch 1 : DELETE current char (only if budget remains) ----
        if (ch == '(' && l > 0) dfs(i + 1, l - 1, r, open, path);
        else if (ch == ')' && r > 0) dfs(i + 1, l, r - 1, open, path);

        // ---- branch 2 : KEEP current char ----
        int len = path.length();
        path.append(ch);
        if (ch != '(' && ch != ')') dfs(i + 1, l, r, open, path);
        else if (ch == '(') dfs(i + 1, l, r, open + 1, path);
        /** NOTE !!! keep ')' ONLY when it can be matched -> prunes invalid prefixes */
        else if (open > 0) dfs(i + 1, l, r, open - 1, path);
        path.setLength(len);   // undo (backtrack)
    }
}
```

```python
# python
# LC 301 - Remove Invalid Parentheses
# time = O(2^N), space = O(N) recursion depth (+ output)
# IDEA: count surplus '(' = l and ')' = r first -> DELETION BUDGET.
#       per char branch "delete" (budget--) vs "keep"; end state l==r==open==0 is minimal.
class Solution:
    def removeInvalidParentheses(self, s):

        ### NOTE !!! step 1 : how many brackets MUST be removed
        l = r = 0
        for ch in s:
            if ch == '(':
                l += 1
            elif ch == ')':
                if l > 0:
                    l -= 1      # matched
                else:
                    r += 1      # unmatched ')'

        res = set()             # set -> dedup identical strings

        # l, r : '(' and ')' still allowed to be DELETED
        # open : unmatched '(' currently kept in path
        def dfs(i, l, r, open_cnt, path):
            if i == len(s):
                if l == 0 and r == 0 and open_cnt == 0:
                    res.add(path)
                return

            ch = s[i]

            # ---- branch 1 : DELETE current char (only if budget remains) ----
            if ch == '(' and l > 0:
                dfs(i + 1, l - 1, r, open_cnt, path)
            elif ch == ')' and r > 0:
                dfs(i + 1, l, r - 1, open_cnt, path)

            # ---- branch 2 : KEEP current char ----
            if ch not in '()':
                dfs(i + 1, l, r, open_cnt, path + ch)
            elif ch == '(':
                dfs(i + 1, l, r, open_cnt + 1, path + ch)
            ### NOTE !!! keep ')' ONLY when matchable -> prunes invalid prefixes early
            elif open_cnt > 0:
                dfs(i + 1, l, r, open_cnt - 1, path + ch)

        dfs(0, l, r, 0, "")
        return list(res)
```

> **Set-free variant**: instead of a `HashSet`, when you delete a char skip **all identical
> consecutive chars** at once (`while i+1 < n and s[i+1] == s[i]: i++`) — the same

### Template 4: Constraint Propagation (Early Termination)

Beyond simple bound-checking, propagate constraints forward before recursing. This is the key insight separating O(n!) brute force from practical backtracking.

```text
Standard backtracking:   try → recurse → undo
With propagation:        try → propagate constraints → if valid: recurse → undo
```

Example: In Sudoku, after placing a digit, immediately eliminate it from peer cells. If any cell has zero candidates, backtrack immediately without reaching deeper levels.

> The plain row/col/box-set version of Sudoku is
> [backtrack.md Template 11](./backtrack.md#template-11-sudoku-solver--lc-37); this is the
> optimisation on top of it.


### Template 5: Generic Partitioning Templates

Two shape-only templates — pass the validity predicate in. Useful when a problem partitions
something that is neither a string of palindromes nor `k` equal-sum buckets.

**1. String Partitioning Template**:
```python
def partition_string(s, is_valid_partition):
    def backtrack(start, current_partition):
        if start == len(s):
            result.append(current_partition[:])
            return

        for end in range(start + 1, len(s) + 1):
            substring = s[start:end]
            if is_valid_partition(substring):
                current_partition.append(substring)
                backtrack(end, current_partition)
                current_partition.pop()

    result = []
    backtrack(0, [])
    return result
```

**2. Array Partitioning Template**:
```python
def partition_array(nums, k, target_sum):
    def backtrack(index, groups):
        if index == len(nums):
            return all(sum(group) == target_sum for group in groups)

        for i in range(k):
            if sum(groups[i]) + nums[index] <= target_sum:
                groups[i].append(nums[index])
                if backtrack(index + 1, groups):
                    return True
                groups[i].pop()

                # Pruning: if current group is empty, no need to try other empty groups
                if not groups[i]:
                    break

        return False

    return backtrack(0, [[] for _ in range(k)])
```

### Template 6: Equal-Subset-Sum Partitioning — LC 416

Pure include/exclude recursion with no path to collect. This is the backtracking *ancestor* of
0/1 knapsack — the same call tree, before memoisation. For the DP form see
[knapsack.md](./knapsack.md).

```python
# python
# LC 416 - Partition Equal Subset Sum
def canPartition(nums):
    total = sum(nums)
    if total % 2 != 0:
        return False

    target = total // 2

    def backtrack(index, current_sum):
        if current_sum == target:
            return True
        if index >= len(nums) or current_sum > target:
            return False

        # Include current number
        if backtrack(index + 1, current_sum + nums[index]):
            return True

        # Exclude current number
        return backtrack(index + 1, current_sum)

    return backtrack(0, 0)
```

### Template 7: Memoised Backtracking

The moment the recursion is keyed by a **state** rather than a path, the search collapses into
DP. This is the mechanical halfway house: keep the backtrack shape, add a `memo` on the state
tuple. Outline only — `check_valid_partition`, `get_choices` and `update_state` are yours to fill in.

```python
def partition_with_memo(nums):
    memo = {}

    def backtrack(index, state_tuple):
        if index == len(nums):
            return check_valid_partition(state_tuple)

        if state_tuple in memo:
            return memo[state_tuple]

        result = False
        for choice in get_choices(index, state_tuple):
            new_state = update_state(state_tuple, choice)
            if backtrack(index + 1, new_state):
                result = True
                break

        memo[state_tuple] = result
        return result

    return backtrack(0, initial_state)
```

> Once every branch is decided by the state alone, drop the recursion entirely — see
> [recursion_to_dp.md](./recursion_to_dp.md).


### Template 8: Bucket Partitioning with Early Termination

The `if len(groups[i]) == 0: break` line is the load-bearing one: trying a second *empty*
bucket only relabels a partition you already rejected.

```python
def optimized_partition(nums, k):
    def backtrack(index, groups, remaining_sum):
        if index == len(nums):
            return remaining_sum == 0

        # Pruning: if remaining sum is too small
        if remaining_sum < 0:
            return False

        for i in range(len(groups)):
            groups[i].append(nums[index])
            if backtrack(index + 1, groups, remaining_sum - nums[index]):
                return True
            groups[i].pop()

            # Important pruning: don't try other empty groups
            if len(groups[i]) == 0:
                break

        return False

    return backtrack(0, [[] for _ in range(k)], sum(nums))
```

### Framework Variants (pseudo-code)

The validity-gated spelling of the skeleton — `is_valid` before `place`, rather than a check
at the top of the recursive call. Outline, not runnable code.

```python
# python pseudo code 1
# https://leetcode.com/explore/learn/card/recursion-ii/472/backtracking/2793/
def backtrack(candidate):
    if find_solution(candidate):
        output(candidate)
        return
    
    # iterate all possible candidates.
    for next_candidate in list_of_candidates:
        if is_valid(next_candidate):
            # try this partial candidate solution
            place(next_candidate)
            # given the candidate, explore further.
            backtrack(next_candidate)
            # backtrack
            remove(next_candidate)
```

## Summary & Quick Reference

### Pruning and Partitioning Comparison

| Technique | Purpose | When to Use | Complexity Impact |
|-----------|---------|-------------|-------------------|
| **Constraint Pruning** | Early termination | Invalid states | Reduces branches significantly |
| **Bound Pruning** | Eliminate suboptimal paths | Optimization problems | O(2^n) → O(n!) potential |
| **Symmetry Pruning** | Avoid duplicates | Permutation problems | Eliminates factorial duplicates |
| **Equal Sum Partition** | Divide into equal groups | Subset sum problems | Exponential to polynomial |
| **String Partition** | Split by criteria | String segmentation | O(2^n) worst case |

### Which advanced template?

| Signal in the problem | Template |
|---|---|
| many words / patterns against one grid or stream | [Template 1](#template-1-trie--grid-backtracking--lc-212-word-search-ii-) |
| build an expression / insert operators | [Template 2](#template-2-expression-building-operator-insertion--lc-282-expression-add-operators-) |
| "remove the **minimum** number of characters" | [Template 3](#template-3-deletion-budget-backtracking--lc-301-remove-invalid-parentheses-) |
| a valid board still times out | [Template 4](#template-4-constraint-propagation-early-termination) |
| split into `k` groups under a predicate | [Template 5](#template-5-generic-partitioning-templates) / [Template 8](#template-8-bucket-partitioning-with-early-termination) |
| the same *state* keeps recurring | [Template 7](#template-7-memoised-backtracking) → [dp.md](./dp.md) |

### Related Topics

- [backtrack.md](./backtrack.md) — the must-know templates
- [backtrack_examples.md](./backtrack_examples.md) — worked solutions for those templates
- [trie.md](./trie.md) — Trie construction and search
- [dfs_advanced.md](./dfs_advanced.md) — Trie + DFS wildcard search (LC 211), Tarjan, Hierholzer
- [knapsack.md](./knapsack.md) / [recursion_to_dp.md](./recursion_to_dp.md) — where memoised backtracking lands
