# Stack — Worked Examples

> **Scope** — The problem set behind [stack.md](./stack.md): the stack problems that are not a template's anchor, each as a problem card under the template it reduces to; the monotonic problems are worked in [monotonic_stack.md](./monotonic_stack.md) and only pointed to here.
> **See also**: [stack.md](./stack.md) — the parent sheet: the six templates these problems reduce to; [monotonic_stack.md](./monotonic_stack.md) — next greater / previous smaller, spans, histograms and greedy removal, worked in full; [stack_expression_parsing.md](./stack_expression_parsing.md) — calculators, decode string and postfix evaluation, which are their own family; [iterator.md](./iterator.md) — stack-backed iterators (LC 173, LC 341); [queue.md](./queue.md) — the FIFO counterpart, including LC 232.

## LeetCode Problem Lists

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

[stack.md](./stack.md) holds the six templates; this sheet is the problems that apply them. Each
`##` section below mirrors one template, in the core sheet's order, and each card names the one
step its problem adds to that template. A problem worked in another sheet is a row in the
[Index](#index) (and in its section's pointer table) rather than a second copy.

## Index

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 1047 | Remove All Adjacent Duplicates In String | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Easy | anchor, worked in stack.md |
| 844 | Backspace String Compare | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Easy | worked in advanced_simulation.md |
| 946 | Validate Stack Sequences | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Medium | worked in advanced_simulation.md |
| 1910 | Remove All Occurrences of a Substring | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) | Medium | worked in advanced_simulation.md |
| 1209 | Remove All Adjacent Duplicates in String II | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Medium | [card](#remove-all-adjacent-duplicates-in-string-ii--lc-1209-) |
| 735 | Asteroid Collision | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) | Medium | [card](#asteroid-collision--lc-735) |
| 20 | Valid Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Easy | anchor, worked in stack.md |
| 921 | Minimum Add to Make Parentheses Valid | [greedy_examples § 7](./greedy_examples.md#7-minimum-add-to-make-parentheses-valid--lc-921--a-balance-counter) | Medium | worked in greedy_examples.md |
| 1249 | Minimum Remove to Make Valid Parentheses | [string § Template 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) | Medium | index stack in string.md; [two-scan card](#minimum-remove-to-make-valid-parentheses--lc-1249) here |
| 856 | Score of Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Medium | [card](#score-of-parentheses--lc-856) |
| 1963 | Minimum Number of Swaps to Make the String Balanced | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Medium | [card](#minimum-number-of-swaps-to-make-the-string-balanced--lc-1963) |
| 32 | Longest Valid Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | Hard | [card](#longest-valid-parentheses--lc-32-) |
| 496 | Next Greater Element I | [monotonic_stack § 2-3](./monotonic_stack.md#2-3-next-greater-element-i-lc-496--monotonic-stack--hashmap) | Easy | worked in monotonic_stack.md |
| 739 | Daily Temperatures | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) | Medium | anchor, worked in stack.md |
| 503 | Next Greater Element II | [monotonic_stack § 2-5](./monotonic_stack.md#2-5-next-greater-element-ii-lc-503--circular-monotonic-stack) | Medium | worked in monotonic_stack.md |
| 901 | Online Stock Span | [monotonic_stack § 2-6](./monotonic_stack.md#2-6-online-stock-span-lc-901--monotonic-decreasing-stack) | Medium | worked in monotonic_stack.md |
| 907 | Sum of Subarray Minimums | [monotonic_stack § 2-7](./monotonic_stack.md#2-7-sum-of-subarray-minimums-lc-907--monotonic-stack) | Medium | worked in monotonic_stack.md |
| 2104 | Sum of Subarray Ranges | [monotonic_stack § 2-12](./monotonic_stack.md#2-12-sum-of-subarray-ranges-lc-2104--dual-monotonic-stack-contribution-method) | Medium | worked in monotonic_stack.md |
| 402 | Remove K Digits | [monotonic_stack § 2-8](./monotonic_stack.md#2-8-remove-k-digits-lc-402--monotonic-increasing-stack) | Medium | worked in monotonic_stack.md |
| 316 | Remove Duplicate Letters | [monotonic_stack § 2-8-1](./monotonic_stack.md#2-8-1-remove-duplicate-letters--lc-316) | Medium | worked in monotonic_stack.md |
| 84 | Largest Rectangle in Histogram | [monotonic_stack § 2-2](./monotonic_stack.md#2-2-largest-rectangle-in-histogram-lc-84--monotonic-increasing-stack) | Hard | worked in monotonic_stack.md |
| 155 | Min Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | anchor, worked in stack.md |
| 144 | Binary Tree Preorder Traversal | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Easy | anchor, worked in stack.md |
| 145 | Binary Tree Postorder Traversal | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Easy | anchor, worked in stack.md |
| 232 | Implement Queue using Stacks | [queue § Template 5](./queue.md#template-5-queue-using-stacks-pattern--lc-232-) | Easy | worked in queue.md |
| 173 | Binary Search Tree Iterator | [bst_advanced § Template 5b](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) | Medium | worked in bst_advanced.md |
| 341 | Flatten Nested List Iterator | [iterator § 2-2](./iterator.md#2-2-flatten-nested-list-iterator--lc-341) | Medium | worked in iterator.md |
| 445 | Add Two Numbers II | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) | Medium | [card](#add-two-numbers-ii--lc-445) |
| 71 | Simplify Path | [string § Variation 6.1](./string.md#variation-61-token-stack--lc-71-simplify-path) | Medium | worked in string.md |
| 388 | Longest Absolute File Path | [stack § Template 6](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) | Medium | anchor, worked in stack.md |
| 636 | Exclusive Time of Functions | [stack § Template 6](./stack.md#exclusive-time-of-functions--lc-636) | Medium | anchor, worked in stack.md |

## Cancel Against the Top Examples

| LC | Problem | Where it is worked |
|---|---|---|
| 1047 | Remove All Adjacent Duplicates In String | [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — the anchor |
| 844 · 946 · 1910 | Backspace String Compare · Validate Stack Sequences · Remove All Occurrences of a Substring | [advanced_simulation § Template 6](./advanced_simulation.md#variations-of-template-6) |

### Remove All Adjacent Duplicates in String II — LC 1209 ⭐⭐⭐⭐

> **Key point** — each stack entry is a whole **run** `[char, length]`, so the stack is the reduced
> prefix run-length encoded. A matching char lengthens the top run; the moment it reaches `k` the
> run is popped whole, and the run below is exposed to the next char — which is how a removal
> cascades without a second pass.

**Reduces to**: [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — the
cancel needs `k` equal neighbours instead of two, so the top carries a count. LC 1047 is `k = 2`.

```python
# python
# LC 1209 - Remove All Adjacent Duplicates in String II
# IDEA: stack of [char, run length]; a run that reaches k is popped whole
# time = O(n), space = O(n)
class Solution(object):
    def removeDuplicates(self, s, k):
        stack = []                       # [char, run length] — the reduced prefix, run-length encoded
        for ch in s:
            if stack and stack[-1][0] == ch:
                stack[-1][1] += 1
                if stack[-1][1] == k:
                    stack.pop()          # the run reached k: it vanishes, exposing the run below
            else:
                stack.append([ch, 1])
        return ''.join(ch * cnt for ch, cnt in stack)
```

```java
// java
// LC 1209 - Remove All Adjacent Duplicates in String II
// IDEA: stack of {char, run length}; a run that reaches k is popped whole
// time = O(n), space = O(n)
public String removeDuplicates(String s, int k) {
    Deque<int[]> stack = new ArrayDeque<>();      // {char, run length}, top = last run
    for (char ch : s.toCharArray()) {
        if (!stack.isEmpty() && stack.peek()[0] == ch) {
            stack.peek()[1]++;
            if (stack.peek()[1] == k) {
                stack.pop();                      // the run vanishes, exposing the run below
            }
        } else {
            stack.push(new int[]{ch, 1});
        }
    }
    StringBuilder sb = new StringBuilder();
    while (!stack.isEmpty()) {
        int[] run = stack.pollLast();             // the bottom is the leftmost run
        for (int i = 0; i < run[1]; i++) {
            sb.append((char) run[0]);
        }
    }
    return sb.toString();
}
```

**Dry run**

```text
s = "deeedbbcccbdaa", k = 3
ch   stack (runs)                     note
d    [d1]
e    [d1 e1]
e    [d1 e2]
e    [d1]                             e reaches 3 -> popped
d    [d2]                             the pop exposed d, so this d extends it
b    [d2 b1]
b    [d2 b2]
c    [d2 b2 c1]
c    [d2 b2 c2]
c    [d2 b2]                          c reaches 3 -> popped
b    [d2]                             b reaches 3 -> popped (cascade)
d    []                               d reaches 3 -> popped (cascade)
a    [a1]
a    [a2]                             -> "aa"
```

**Traps**
- Pushing single characters and re-counting the top `k` on every step is O(n·k); with
  `k = 10⁴` and n = 10⁵ that times out. Keep the count on the entry.
- Resetting the count to `0` instead of popping (`"deeed"`, k = 3) leaves an empty run on top,
  so the second `d` starts a new run and the cascade to `"dd"` never happens.
- Java: iterating an `ArrayDeque` with a for-each goes top → bottom and reverses the answer;
  drain with `pollLast()`.

**Similar** — LC 1047 (`k = 2`, no count needed) · LC 1544 (cancel on same letter, opposite case)

### Asteroid Collision — LC 735

> **Key point** — the stack holds the survivors so far, and a collision can only happen between a
> newcomer moving **left** (`a < 0`) and a top moving **right** (`top > 0`). The newcomer keeps
> destroying smaller tops in a `while` loop, so one asteroid can pop many; it is pushed only if it
> is still alive when the loop stops.

**Reduces to**: [stack § Template 1](./stack.md#template-1-cancel-against-the-top--lc-1047) — the
cancel test is a size comparison and can destroy either side, not only the top.

```python
# python
# LC 735 - Asteroid Collision
# IDEA: stack of survivors; a left-mover fights right-moving tops until one side is gone
# time = O(n), space = O(n)
class Solution(object):
    def asteroidCollision(self, asteroids):
        stack = []                       # survivors, left to right
        for a in asteroids:
            alive = True
            # a collision needs the newcomer moving left and the top moving right
            while alive and a < 0 and stack and stack[-1] > 0:
                if stack[-1] < -a:
                    stack.pop()          # the top explodes; `a` keeps flying left
                    continue
                if stack[-1] == -a:
                    stack.pop()          # equal size: both explode
                alive = False            # `a` explodes (or already did with the top)
            if alive:
                stack.append(a)
        return stack
```

```java
// java
// LC 735 - Asteroid Collision
// IDEA: stack of survivors; a left-mover fights right-moving tops until one side is gone
// time = O(n), space = O(n)
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> stack = new ArrayDeque<>();
    for (int a : asteroids) {
        boolean alive = true;
        while (alive && a < 0 && !stack.isEmpty() && stack.peek() > 0) {
            if (stack.peek() < -a) {
                stack.pop();                  // the top explodes, `a` keeps flying
                continue;
            }
            if (stack.peek() == -a) {
                stack.pop();                  // both explode
            }
            alive = false;                    // `a` explodes
        }
        if (alive) {
            stack.push(a);
        }
    }
    int[] res = new int[stack.size()];
    for (int i = res.length - 1; i >= 0; i--) {
        res[i] = stack.pop();                 // pop fills from the right end
    }
    return res;
}
```

**Traps**
- Colliding whenever the signs differ: `[-2, 1]` moves apart (left one goes left, right one goes
  right) and must stay `[-2, 1]`. Only *top right, newcomer left* meet.
- Equal sizes destroy **both**: `[8, -8]` is `[]`, not `[8]` or `[-8]`.
- Pushing the newcomer after a single fight: `[10, 2, -5]` must let `-5` destroy `2` and then die
  against `10`, giving `[10]`. The fight is a `while`, not an `if`.

## Bracket Matching Examples

| LC | Problem | Where it is worked |
|---|---|---|
| 20 | Valid Parentheses | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) — the anchor |
| 921 | Minimum Add to Make Parentheses Valid | [greedy_examples § 7](./greedy_examples.md#7-minimum-add-to-make-parentheses-valid--lc-921--a-balance-counter) — one bracket type, so the stack is a counter |
| 1249 | Minimum Remove to Make Valid Parentheses (index stack) | [string § Template 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) — the stack-free form is the card below |

### Minimum Remove to Make Valid Parentheses — LC 1249

> **Key point** — the index-stack answer is [string § Template 7](./string.md#template-7-in-place-char-array--mark-then-rebuild--lc-1249-);
> this is the stack-free one. With one bracket type the stack can shrink to a counter, but a
> counter forgets *where* the surplus `(` sit, so split the job by direction: left → right drops
> every `)` with no `(` open before it, then right → left drops every `(` with no `)` open after
> it. Each pass removes only brackets that cannot be matched, so the removal is minimal.

**Reduces to**: [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) — the stack
degenerates to its size, run once in each direction. It answers the follow-up *"can you do it
without the index stack?"*, and it is the same two-direction trick as LC 32's counter scans below.

```python
# python
# LC 1249 - Minimum Remove to Make Valid Parentheses
# IDEA: 2 SCANS with a balance counter — drop unmatched ')' left->right, unmatched '(' right->left
# time = O(n), space = O(n) for the two output buffers; O(1) beyond them
class Solution(object):
    def minRemoveToMakeValid(self, s):
        # pass 1: left -> right; a ')' with no '(' open before it can never be matched
        kept = []
        open_cnt = 0
        for c in s:
            if c == ')':
                if open_cnt == 0:
                    continue        # unmatched ')' -> drop
                open_cnt -= 1
            elif c == '(':
                open_cnt += 1
            kept.append(c)

        # pass 2: right -> left, the mirror; a '(' with no ')' open after it can never be matched
        res = []
        close_cnt = 0
        for c in reversed(kept):
            if c == '(':
                if close_cnt == 0:
                    continue        # unmatched '(' -> drop
                close_cnt -= 1
            elif c == ')':
                close_cnt += 1
            res.append(c)

        res.reverse()               # pass 2 built it backwards
        return ''.join(res)
```

```java
// java
// LC 1249 - Minimum Remove to Make Valid Parentheses
// IDEA: 2 SCANS with a balance counter — drop unmatched ')' left->right, unmatched '(' right->left
// time = O(n), space = O(n) for the two output buffers; O(1) beyond them
public String minRemoveToMakeValid(String s) {
    StringBuilder kept = new StringBuilder();
    int openCnt = 0;
    for (char c : s.toCharArray()) {
        if (c == ')') {
            if (openCnt == 0) {
                continue;      // unmatched ')' -> drop
            }
            openCnt--;
        } else if (c == '(') {
            openCnt++;
        }
        kept.append(c);
    }
    StringBuilder res = new StringBuilder();
    int closeCnt = 0;
    for (int i = kept.length() - 1; i >= 0; i--) {
        char c = kept.charAt(i);
        if (c == '(') {
            if (closeCnt == 0) {
                continue;      // unmatched '(' -> drop
            }
            closeCnt--;
        } else if (c == ')') {
            closeCnt++;
        }
        res.append(c);
    }
    return res.reverse().toString(); // pass 2 built it backwards
}
```

**Dry run**

```text
s = ")((a)"

pass 1, left -> right   )  open=0 -> drop       (  open=1   (  open=2   a   )  open=1
                        kept = "((a)"
pass 2, right -> left   )  close=1   a   (  close=0   (  close=0 -> drop
                        res  = "(a)"
```

**Traps**
- One left → right pass only: `"(()"` keeps the surplus `(`, because `open_cnt` just ends at 1.
  The second, mirrored pass is what drops it.
- Forgetting to reverse after pass 2: `")((a)"` comes back as `")a("`.

### Score of Parentheses — LC 856

> **Key point** — each stack slot holds the **score collected so far at that depth**. `(` opens a
> new frame (push `0`); `)` closes it and folds it into the parent: an empty frame was `"()"` and
> scores `1`, a non-empty one doubles, so the parent gains `max(2 * inner, 1)`.

**Reduces to**: [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) — the stack
holds a partial **result** per open depth instead of the opener itself.

```python
# python
# LC 856 - Score of Parentheses
# IDEA: STACK OF PARTIAL SCORES — one frame per depth, fold child into parent
# time = O(n), space = O(n)
class Solution(object):
    def scoreOfParentheses(self, s):
        stack = [0]                  # stack[-1] = score collected so far at the current depth
        for c in s:
            if c == '(':
                stack.append(0)      # open a frame for the new depth
            else:
                inner = stack.pop()  # close it: "()" scores 1, "(X)" scores 2 * X
                stack[-1] += max(2 * inner, 1)
        return stack[0]
```

```java
// java
// LC 856 - Score of Parentheses
// IDEA: STACK OF PARTIAL SCORES — one frame per depth, fold child into parent
// time = O(n), space = O(n)
public int scoreOfParentheses(String s) {
    Deque<Integer> st = new ArrayDeque<>();
    st.push(0); // score of the outermost frame
    for (char c : s.toCharArray()) {
        if (c == '(') {
            st.push(0); // open a new (empty) frame
        } else {
            int inner = st.pop();
            st.push(st.pop() + Math.max(2 * inner, 1)); // "()" scores 1, "(X)" scores 2 * X
        }
    }
    return st.pop();
}
```

**Traps**
- No outer `0` frame: `"()()"` pops the stack empty on the first `)` and has nowhere to add
  the score. Seed the stack with `[0]`.
- `2 * inner` alone scores `"()"` as `0`; the `max(..., 1)` is the base case.

### Minimum Number of Swaps to Make the String Balanced — LC 1963

> **Key point** — cancel every matched `[]` pair with the stack and what is left is always
> `"]" * m + "[" * m`: every unmatched `]` sits before every unmatched `[`. One swap of the
> outermost `]` with the outermost `[` repairs **two** of those m pairs, so the answer is
> `ceil(m / 2)`.

**Reduces to**: [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) — used as a
reducer: the scan leaves only the unbalanced core, and the answer is a formula on its size.

```python
# python
# LC 1963 - Minimum Number of Swaps to Make the String Balanced
# IDEA: cancel matched pairs; the residue is "]"*m + "["*m and one swap fixes two pairs
# time = O(n), space = O(n)
class Solution(object):
    def minSwaps(self, s):
        stack = []
        for c in s:
            if c == ']' and stack and stack[-1] == '[':
                stack.pop()              # a matched pair cancels
            else:
                stack.append(c)          # an unmatched ']' or a '[' still open
        # what is left is "]" * m + "[" * m: every ']' sits before every '['
        m = len(stack) // 2
        return (m + 1) // 2              # one swap repairs two of the m pairs
```

**Traps**
- Popping on every `]` without checking that the top is `[`: on `"]]][[["` two `]` cancel each
  other and the residue is `"][[["`, not `"]]][[["`. Match the top before popping.
- `m // 2` instead of `(m + 1) // 2`: `"][]["` has m = 1 and needs one swap, not zero.
- The stack only ever holds `]` below `[`, so a counter of unmatched `]` gives the same `m` in
  O(1) space.

### Longest Valid Parentheses — LC 32 ⭐⭐⭐⭐

> **Key point** — push **indices**, and keep the stack's bottom element as the index just before
> the current valid run (the *base*, seeded with `-1`). On `)` pop first: if the stack became
> empty, this `)` is unmatched and becomes the new base; otherwise `i - stack[-1]` is the length
> of the valid run ending at `i`.

**Reduces to**: [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) — indices
instead of chars, plus a base sentinel that turns a match into a length.

```python
# python
# LC 32 - Longest Valid Parentheses
# IDEA: STACK OF INDICES + `-1` base sentinel; length = i - stack[-1]
# time = O(n), space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
        stack = [-1]         # bottom = index just before the current valid run (the base)
        best = 0
        for i, c in enumerate(s):
            if c == '(':
                stack.append(i)
            else:
                stack.pop()                       # match the top '(' — or pop the base
                if not stack:
                    stack.append(i)               # unmatched ')' -> it becomes the new base
                else:
                    best = max(best, i - stack[-1])
        return best
```

```java
// java
// LC 32 - Longest Valid Parentheses
// IDEA: STACK OF INDICES + `-1` base sentinel; length = i - stack.peek()
// time = O(n), space = O(n)
public int longestValidParentheses(String s) {
    Deque<Integer> st = new ArrayDeque<>();
    st.push(-1);                    // base: the index just before the current valid run
    int best = 0;
    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == '(') {
            st.push(i);
        } else {
            st.pop();               // match the top '(' — or pop the base
            if (st.isEmpty()) {
                st.push(i);         // unmatched ')' -> the new base
            } else {
                best = Math.max(best, i - st.peek());
            }
        }
    }
    return best;
}
```

**Dry run**

```text
s = ")()())"

i  c   action                       stack        best
-  -   init base                    [-1]         0
0  )   pop -1 -> empty -> new base  [0]          0
1  (   push                         [0, 1]       0
2  )   pop 1, i - top = 2 - 0 = 2   [0]          2
3  (   push                         [0, 3]       2
4  )   pop 3, i - top = 4 - 0 = 4   [0]          4  <- answer
5  )   pop 0 -> empty -> new base   [5]          4
```

**Two more solutions worth knowing.** The stack is the one to write first; each of these teaches
something it does not.

| Approach | Time | Space | What it teaches |
|---|---|---|---|
| Index stack + `-1` base (above) | O(n) | O(n) | the base sentinel turns a match into a length |
| 1D DP — `dp[i]` = longest valid run **ending at** `i` | O(n) | O(n) | jump back over the run ending at `i-1` to find `s[i]`'s partner |
| Two counter scans (left→right, then right→left) | O(n) | **O(1)** | one counter cannot see a surplus of `(`, so scan again backwards |

**1D DP.** A valid run can only end on `)`, so `dp[i] = 0` whenever `s[i] == '('`. On `)`: if
`s[i-1] == '('` it is a new pair glued to the run before it, `dp[i] = dp[i-2] + 2`; otherwise the
run ending at `i-1` is `dp[i-1]` long, so `s[i]`'s partner can only sit at
`open_idx = i - dp[i-1] - 1`, and if that is `(` then `dp[i] = dp[i-1] + 2 + dp[open_idx-1]` —
the last term glues on the run that ended right before the partner, the part everyone forgets.

```python
# python
# LC 32 - Longest Valid Parentheses
# IDEA: 1D DP — dp[i] = longest valid substring ENDING at i
# time = O(n), space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
        n = len(s)
        dp = [0] * n                      # dp[i] = longest valid run ENDING at i
        best = 0
        for i in range(1, n):
            if s[i] == '(':
                continue                  # a valid run never ends with '('
            if s[i - 1] == '(':
                # case 1: "...()" -> the new pair + the run ending at i-2
                dp[i] = 2 + (dp[i - 2] if i >= 2 else 0)
            else:
                # case 2: "...))" -> jump over the run ending at i-1 to s[i]'s partner
                open_idx = i - dp[i - 1] - 1
                if open_idx >= 0 and s[open_idx] == '(':
                    dp[i] = dp[i - 1] + 2
                    if open_idx >= 1:
                        dp[i] += dp[open_idx - 1]   # glue on the run that ends before the partner
            best = max(best, dp[i])
        return best
```

```text
s = "()(())"

i  c   case                                             dp
0  (   '(' ends nothing                                 [0,0,0,0,0,0]
1  )   "()": 2 + dp[-1 -> 0]                            [0,2,0,0,0,0]
2  (   -                                                [0,2,0,0,0,0]
3  (   -                                                [0,2,0,0,0,0]
4  )   "()": 2 + dp[2] = 2                              [0,2,0,0,2,0]
5  )   "))": open_idx = 5 - dp[4] - 1 = 2, s[2] = '('
           dp[5] = dp[4] + 2 + dp[1] = 2 + 2 + 2 = 6    [0,2,0,0,2,6]  <- answer
```

**Two counter scans.** Count `opens` and `closes` left → right: `opens == closes` closes a valid
run of length `2 * closes`, and `closes > opens` means this `)` can never be matched, so reset
both. Left → right never sees `opens == closes` on `"(()"`, so run the mirror pass right → left,
resetting when `opens > closes`. Every valid run is caught by at least one of the two passes;
this is the only O(1)-space answer.

```python
# python
# LC 32 - Longest Valid Parentheses
# IDEA: 2 SCANS with open/close counters — left->right, then right->left
# time = O(n), space = O(1)
class Solution(object):
    def longestValidParentheses(self, s):
        best = 0
        # pass 1: left -> right; a surplus ')' can never be matched -> reset
        opens = closes = 0
        for c in s:
            if c == '(':
                opens += 1
            else:
                closes += 1
            if opens == closes:
                best = max(best, 2 * closes)
            elif closes > opens:
                opens = closes = 0
        # pass 2: right -> left; catches "(()", where opens stays ahead in pass 1
        opens = closes = 0
        for c in reversed(s):
            if c == '(':
                opens += 1
            else:
                closes += 1
            if opens == closes:
                best = max(best, 2 * opens)
            elif opens > closes:
                opens = closes = 0
        return best
```

**Traps**
- No `-1` seed: every run that starts at index `0` (`"()"`) pops the stack empty and is never
  measured.
- Pushing characters instead of indices: the whole trick is `i - stack[-1]`, so the stack must
  hold positions.
- DP: dropping the `dp[open_idx - 1]` term scores `"()(())"` as 4 instead of 6.
- Counter scans: one direction only returns 0 on `"(()"`; the mirrored pass is required.

## Monotonic Stack Examples

The monotonic problems are worked in [monotonic_stack.md](./monotonic_stack.md); the core
form is [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-).

| LC | Problem | Where it is worked |
|---|---|---|
| 496 | Next Greater Element I | [monotonic_stack § 2-3](./monotonic_stack.md#2-3-next-greater-element-i-lc-496--monotonic-stack--hashmap) |
| 739 | Daily Temperatures | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) — the anchor |
| 503 | Next Greater Element II | [monotonic_stack § 2-5](./monotonic_stack.md#2-5-next-greater-element-ii-lc-503--circular-monotonic-stack) |
| 901 | Online Stock Span | [monotonic_stack § 2-6](./monotonic_stack.md#2-6-online-stock-span-lc-901--monotonic-decreasing-stack) |
| 907 | Sum of Subarray Minimums | [monotonic_stack § 2-7](./monotonic_stack.md#2-7-sum-of-subarray-minimums-lc-907--monotonic-stack) |
| 2104 | Sum of Subarray Ranges | [monotonic_stack § 2-12](./monotonic_stack.md#2-12-sum-of-subarray-ranges-lc-2104--dual-monotonic-stack-contribution-method) |
| 402 | Remove K Digits | [monotonic_stack § 2-8](./monotonic_stack.md#2-8-remove-k-digits-lc-402--monotonic-increasing-stack) |
| 316 | Remove Duplicate Letters | [monotonic_stack § 2-8-1](./monotonic_stack.md#2-8-1-remove-duplicate-letters--lc-316) |
| 84 | Largest Rectangle in Histogram | [monotonic_stack § 2-2](./monotonic_stack.md#2-2-largest-rectangle-in-histogram-lc-84--monotonic-increasing-stack) |

## Min Stack Examples

| LC | Problem | Where it is worked |
|---|---|---|
| 155 | Min Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — the anchor, with the space-saving second stack of minima |

## Explicit Stack Examples

| LC | Problem | Where it is worked |
|---|---|---|
| 144 · 145 | Binary Tree Preorder / Postorder Traversal | [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) — the anchors |
| 232 | Implement Queue using Stacks | [queue § Template 5](./queue.md#template-5-queue-using-stacks-pattern--lc-232-) |
| 173 | Binary Search Tree Iterator | [bst_advanced § Template 5b](./bst_advanced.md#template-1-lazy-in-order-iterator--lc-173-) — the paused in-order traversal |
| 341 | Flatten Nested List Iterator | [iterator § 2-2](./iterator.md#2-2-flatten-nested-list-iterator--lc-341) — children pushed in reverse, expanded in `hasNext()` |

### Add Two Numbers II — LC 445

> **Key point** — a singly linked list can only be walked forward, but addition runs from the
> least significant digit. Pushing every node's value onto a stack gives that backwards order
> **without mutating the input**; building the answer by prepending reverses a second time, so it
> comes out most significant first.

**Reduces to**: [stack § Template 5](./stack.md#template-5-explicit-stack--iterative-traversal--lc-144-lc-145-) —
the stack holds the work not yet done and hands it back in reverse, here the digits of two lists.

```python
# python
# LC 445 - Add Two Numbers II
# IDEA: TWO STACKS give least-significant-first access without mutating the input
# time = O(n + m), space = O(n + m)
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        s1, s2 = [], []
        while l1:                         # push every digit: popping now yields least significant first
            s1.append(l1.val)
            l1 = l1.next
        while l2:
            s2.append(l2.val)
            l2 = l2.next

        carry = 0
        head = None                       # the answer is built from its tail forwards
        while s1 or s2 or carry:          # a pending carry adds one more digit
            total = carry
            if s1:
                total += s1.pop()
            if s2:
                total += s2.pop()
            carry = total // 10
            node = ListNode(total % 10)
            node.next = head              # prepend: the second reversal puts the digits back in order
            head = node
        return head
```

```java
// java
// LC 445 - Add Two Numbers II
// IDEA: TWO STACKS give least-significant-first access without mutating the input
// time = O(n + m), space = O(n + m)
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    Deque<Integer> s1 = new ArrayDeque<>();
    Deque<Integer> s2 = new ArrayDeque<>();
    for (ListNode p = l1; p != null; p = p.next) {
        s1.push(p.val);
    }
    for (ListNode p = l2; p != null; p = p.next) {
        s2.push(p.val);
    }
    int carry = 0;
    ListNode head = null;                     // built from the tail forwards
    while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
        int sum = carry;
        if (!s1.isEmpty()) {
            sum += s1.pop();
        }
        if (!s2.isEmpty()) {
            sum += s2.pop();
        }
        carry = sum / 10;
        ListNode node = new ListNode(sum % 10);
        node.next = head;                     // prepend: the second reversal restores the order
        head = node;
    }
    return head;
}
```

**Traps**
- Stopping when both stacks are empty: `[9, 9] + [1]` loses the final carry and returns `[0, 0]`
  instead of `[1, 0, 0]`. Loop while `carry` is pending too.
- Converting each list to an integer and adding is what [add_x_sum § 2-7](./add_x_sum.md#2-7-add-two-numbers-ii--lc-445) does;
  it works in Python only because its integers are unbounded — at 100 digits a Java `long`
  overflows.
- Reversing both lists first is O(1) extra space but mutates the input, which the follow-up
  ("do not modify the input lists") rules out.

**Similar** — LC 2 (least significant first: no stack needed) · LC 234 (push all, compare front
against pops)

## Scope Ledger Examples

| LC | Problem | Where it is worked |
|---|---|---|
| 388 | Longest Absolute File Path | [stack § Template 6](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) — the anchor, plus the overwrite-the-depth-slot variant |
| 636 | Exclusive Time of Functions | [stack § Template 6](./stack.md#exclusive-time-of-functions--lc-636) — the second anchor |
| 71 | Simplify Path | [string § Variation 6.1](./string.md#variation-61-token-stack--lc-71-simplify-path) — `..` pops the parent directory |
