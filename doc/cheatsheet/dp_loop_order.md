# DP Loop Order & Dependency

> **Scope** — Why a bottom-up DP's loop *nesting* and *direction* are forced by its own transition, the four ways a wrong order fails, and LC 139 Word Break worked in five orders — including the one that looks right, passes `"leetcode"`, and silently fails the problem's own Example 2.
> **See also**: [dp.md](./dp.md) — [Template 1b](./dp.md#template-1b-prefix-partition-dp---lc-139) is the Word Break template itself, [Template 2a](./dp.md#template-2a-reading-a-2-d-dp-table--shape-dependency-fill-order-) is the same rule for a 2-D table; [knapsack.md](./knapsack.md) — [the four knapsack loop orders](./knapsack.md#loop-order-combinations-vs-permutations) side by side; [dp_advanced.md](./dp_advanced.md) — the LC 518 vs LC 377 counting traces; [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包倒序的中文詳解; [recursion_to_dp.md](./recursion_to_dp.md) — top-down memoisation, the formulation with no loop order to get wrong.

- **Core idea**: a bottom-up DP is a topological sweep of its own dependency graph. Write the transition down, and the legal loop orders are exactly the ones that finish every arrow's tail before its head.
- **When to use it**: whenever you turn a recurrence into a table and must decide what loops outside what — or when a tabulated solution returns a wrong answer that the memoised version of the *same* recurrence gets right.
- **Key LeetCode problems**: LC 139, LC 279, LC 322, LC 518, LC 377, LC 416, LC 516
- **Typical symptom of getting it wrong**: not a crash. A plausible, slightly-too-small answer on one input.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

## 0) Concept

### 0-1) The rule, in one line ⭐⭐⭐⭐⭐

> **When `dp[X]` is computed, every state its transition reads must already hold its final value.**

> 在動態規劃（DP）中，迴圈的巢狀順序取決於一個核心原則：
> **在計算當前狀態 `dp[i]` 時，它所依賴的更小狀態必須已經被計算完畢。**

There is nothing else on this page. Every table, trace and counterexample below is that sentence
applied to one more problem.

### 0-2) The table is a DAG — a loop order is a topological sort

A transition is a set of **arrows**: `dp[i] ← dp[i - len(word)]` says *"the cell `i - len(word)`
must be finished before cell `i` is read."* Collect every arrow and you have a directed acyclic
graph over the cells. A bottom-up loop nest is just a **hard-coded topological order** of that DAG.

```text
LC 139, s = "leetcode", dict = {"leet", "code"}

  dp[0]   dp[1] ... dp[4] ............ dp[8]
    │                ▲                   ▲
    └──── "leet" ────┘                   │
                     └────── "code" ─────┘

  every arrow points RIGHT  (i - len(word) < i, because len(word) >= 1)
      → any order that fills dp[] left to right is legal
      → an order that finishes "all of word w, then all of word w'" is NOT a
        left-to-right order at all — it is an order over WORDS, and the arrows
        do not care about words
```

The distinction in that last line is the whole of §1-4 and §2, and it is where LC 139 actually
bites.

### 0-3) The four ways a wrong order fails ⭐⭐⭐⭐

| # | What the loop did | What breaks | Symptom | Canonical case |
|---|---|---|---|---|
| **1** | Read a cell **not yet written** | the dependency rule | answer too small / `False` / `inf` — never a crash | [LC 139 Order C](#2-4-order-c--dictionary-outer-index-inner-) |
| **2** | Read a cell **already rewritten this pass** | the "final value" half of the rule | item silently reused | [LC 416 forward inner loop](#3-3-lc-416--when-direction-not-nesting-is-the-problem-) |
| **3** | Read only finished cells, but **grouped the enumeration differently** | nothing — both are valid DPs | a *different question* is answered | [LC 518 vs LC 377](#3-2-lc-518-vs-lc-377--when-the-order-changes-the-question-not-the-correctness-) |
| **4** | Asked for an order that **does not exist** | the DAG has a cycle | you cannot write the loop at all | a grid with 4-way movement → Dijkstra, not DP ([dp.md](./dp.md)) |

Failure **1** is the dangerous one: it is not a crash, not a TLE, and not wrong on the example in
the problem statement. It is one wrong `False` on an input you did not try.

### 0-4) Why top-down memoisation never has this bug ⭐⭐⭐⭐

A memoised recursion computes each dependency **on demand**: the call stack discovers a valid
topological order at run time, for free. Bottom-up asks you to *precompute that order by hand* and
bake it into the loop nest — which is the only reason this page needs to exist.

> **Diagnostic**: if a tabulated DP is wrong and the memoised version of the *same* recurrence is
> right, you have a loop-order bug, not a recurrence bug. Stop re-reading the transition.

See [recursion_to_dp.md](./recursion_to_dp.md) for converting between the two deliberately.

## 1) General form

### 1-1) The decision procedure ⭐⭐⭐⭐⭐

```text
Step 1.  Write the transition. Underline every dp[...] on the RIGHT-hand side.
Step 2.  For each one, name the index it reads relative to the one being written.
             -> this fixes the DIRECTION of the state loop              (§1-2)
Step 3.  Does the transition also range over a set of ITEMS
         (coins, words, squares, intermediate vertices)?
             no  -> you are done, one loop
             yes -> may that item loop sit OUTSIDE the state loop?      (§1-4)
```

Step 2, for a 1-D table — the sibling of [Template 2a](./dp.md#template-2a-reading-a-2-d-dp-table--shape-dependency-fill-order-)'s 2-D table:

| The transition reads | Example | So the state loop runs |
|---|---|---|
| `dp[i-1]`, `dp[i-2]` — a fixed offset back | LC 70, LC 198 | `i` **ascending** |
| `dp[i - len(word)]`, `dp[i - coin]` — a variable offset back | LC 139, LC 322, LC 279 | `i` **ascending** |
| `dp[j]` for every `j < i` — all shorter prefixes | LC 139 Order B, LC 132 | `i` ascending, `j` anywhere in `[0, i)` |
| `dp[i+1]`, `dp[i+2]` — a suffix state | suffix/"from here on" DP | `i` **descending** |
| `dp[w - weight]`, and this pass **must not** see its own writes | LC 416 (0/1) | `w` **descending** |
| `dp[w - coin]`, and this pass **must** see its own writes | LC 322, LC 518 (unbounded) | `w` **ascending** |

The last two rows are the same expression with opposite intent — which is why direction, not the
recurrence, is what separates 0/1 from unbounded knapsack.

### 1-2) Axis 1 — direction: what "already final" means in a reused array

Once a 2-D table is collapsed to one row, "already computed" splits into two meanings — *computed
in a previous pass* vs *computed in this pass*. Direction is how you choose between them:

| Want `dp[w - x]` to mean | Direction | Effect | Family |
|---|---|---|---|
| the value **before** this item | `w` descending | each item used **at most once** | 0/1 knapsack — LC 416, 494 |
| the value **including** this item | `w` ascending | each item reusable **without limit** | unbounded — LC 322, 518, 279 |

The step-by-step `nums = [3], target = 6` trace that shows the forward pass counting `3 + 3 = 6`
lives in [dp.md](./dp.md)'s *Why Must the Inner Loop Go Backward?* section; the Chinese walkthrough
is in [knapsack_01_zh.md](./knapsack_01_zh.md). Not repeated here.

### 1-3) Axis 2 — nesting: state-outer vs item-outer ⭐⭐⭐⭐⭐

Every "prefix + a menu of items" DP can be spelled two ways, and they are **not** interchangeable:

```python
# python
# IDEA: the two nestings of one transition -- dp[i] <- dp[i - size(item)]

# (A) STATE-outer: "to reach state i, which item was placed LAST?"
for i in range(1, n + 1):          # state
    for item in items:             # item
        if i >= size(item):
            dp[i] = combine(dp[i], dp[i - size(item)])

# (B) ITEM-outer: "introduce item 1 everywhere, then item 2 everywhere, ..."
for item in items:                 # item
    for i in range(size(item), n + 1):   # state
        dp[i] = combine(dp[i], dp[i - size(item)])
```

**(A) is always legal** when the arrows point strictly backwards, because when `i` is reached every
smaller index is finished — for *every* item, not just some of them.

**(B) is a claim.** At the moment item `t`'s pass reads `dp[j]`, that cell only knows about items
`1..t`. So form (B) computes:

> the states reachable using items **in the order the item loop visits them** — a solution may use
> item 3 after item 1, never item 1 after item 3.

Whether that restriction is harmless is the next section.

### 1-4) The commutativity test — when item-outer is legal ⭐⭐⭐⭐⭐

> **Item-outer is legal exactly when any solution can be re-ordered into the item loop's order and
> still be a solution.**

Two questions answer it every time:

```text
Q1. How do the chosen items compose into the answer?
Q2. Is that composition free to be re-ordered?

    coins / squares:  they are ADDED        3 + 1 + 3 == 1 + 3 + 3     ✅ commutes
    words:            they are CONCATENATED "apple"+"pen" != "pen"+"apple"  ❌ does not
```

| Problem | Items compose by | Re-orderable? | Item-outer? |
|---|---|---|---|
| **LC 322** Coin Change (min) | `+` into a sum | ✅ | legal — same answer either way |
| **LC 279** Perfect Squares (min) | `+` into a sum | ✅ | legal — same answer either way |
| **LC 518** Coin Change II (count) | `+` into a sum | ✅ | legal, and **required** — it is what makes each multiset count once |
| **LC 377** Combination Sum IV (count) | `+`, but orders are distinct answers | ✅ but the answer counts them | **must** be state-outer, or orderings are lost |
| **LC 416** Partition Equal Subset Sum | `+`, each item once | ✅ | legal, with a **descending** inner loop (§1-2) |
| **LC 139** Word Break | concatenation at positions `s` pins down | ❌ | **illegal** — §2-4 |
| **Floyd–Warshall** | a path's set of intermediates | ✅ (sort by index) | legal, and the `k` loop *must* be outermost — §3-4 |

Word Break is the odd one out in every DP catalogue for exactly this reason: `s` already decided
what order the words appear in, so the item loop is not free to decide it again.

### 1-5) The shuffle test — a 10-second check ⭐⭐⭐⭐

> Shuffle the item list and run again. **A correct bottom-up DP is invariant to it.** If the answer
> moves, an item loop is sitting outside a state loop that needed it inside.

```text
s = "codeleet", dict order ["code", "leet"]  -> item-outer returns True   ✅ (correct)
s = "codeleet", dict order ["leet", "code"]  -> item-outer returns False  ❌ (same input!)
```

An algorithm whose answer depends on the order a dictionary happened to be listed in is not an
algorithm. (Python makes this easy to miss: iterating `set(wordDict)` hides the order you are
depending on — see [python_gotchas.md](./python_gotchas.md).)

The test is one-directional: passing it is not proof. `"applepenapple"` in §2-5 is wrong under
**every** dictionary order.

## 2) LC 139 Word Break — one recurrence, five orders ⭐⭐⭐⭐⭐

### 2-1) The recurrence and its arrows

| Aspect | Detail |
|---|---|
| **State** | `dp[i]` = can `s[:i]` — the first `i` characters — be cut into dictionary words? |
| **Base** | `dp[0] = True` (the empty prefix) |
| **Transition** | `dp[i] = OR over words w of ( dp[i - len(w)] AND s[i-len(w):i] == w )` |
| **Answer** | `dp[n]` |
| **Arrows** | `dp[i] ← dp[i - len(w)]`, and `len(w) >= 1`, so **every arrow points strictly backwards** |

Because every arrow points backwards, *filling `dp` left to right is legal*. Everything that goes
wrong below goes wrong by not filling it left to right.

The template, the `n+1` sizing and the `set(wordDict)` trap are in
[dp.md Template 1b](./dp.md#template-1b-prefix-partition-dp---lc-139); this section is only about
the loop nest. Source: [`word-break.py`](https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Backtracking/word-break.py).

### 2-2) Order A — index outer, dictionary inner ✅

```python
# python
# LC 139 - Word Break
# IDEA: state-outer -- "to end a segmentation at i, which dictionary word was placed LAST?"
# time = O(n * k * L), space = O(n)    n = len(s), k = len(wordDict), L = max word length
class Solution(object):
    def wordBreak(self, s, wordDict):
        n = len(s)
        d_set = set(wordDict)

        dp = [False] * (n + 1)
        dp[0] = True

        # NOTE !!! index OUTSIDE, dictionary INSIDE
        #   -> dp[i - w] is a strictly smaller index, so it is already final
        for i in range(1, n + 1):
            for word in d_set:
                w = len(word)
                if i >= w and dp[i - w] and s[i - w:i] == word:
                    dp[i] = True
                    break          # it is an OR -- one witness is enough
        return dp[n]
```

**Why it is correct**: when the body reads `dp[i - w]`, the outer loop has already finished every
index below `i`, for every word. No word's contribution can arrive late.

```text
s = "leetcode", dict = {"leet", "code"}

i=1..3   no word ends here                          dp[1..3] = False
i=4      "leet": dp[0]=True and s[0:4]=="leet"   ->  dp[4] = True
i=5..7   "leet"/"code" do not match ending here      dp[5..7] = False
i=8      "code": dp[4]=True and s[4:8]=="code"   ->  dp[8] = True   ← answer
```

The inner loop iterating a **set** is fine here, and that is a property worth noticing: the words
are read from an already-final row, so their order cannot matter. §2-4 is the order where it does.

### 2-3) Order B — index outer, cut point inner ✅

```python
# python
# LC 139 - Word Break
# IDEA: state-outer, driven by CUT POINTS instead of by words -- "where did the last word start?"
# time = O(n^2) cuts * O(L) slice+hash, space = O(n)
for i in range(1, n + 1):
    for j in range(i):                 # j = start of the candidate last word
        if dp[j] and s[j:i] in d_set:
            dp[i] = True
            break
```

Same arrows, same direction, different way of enumerating the same last segment: Order A asks
*"which word?"*, Order B asks *"which cut?"*. Both only ever read finished cells. The worked trace
is in [dp.md Template 1b](./dp.md#template-1b-prefix-partition-dp---lc-139).

Capping the inner loop at the longest word — `for j in range(max(0, i - L), i)` — is what makes
this the fastest of the two on LC 139's constraints (§2-9).

### 2-4) Order C — dictionary outer, index inner ❌

This is the one that looks like unbounded knapsack, and it is **wrong**:

```python
# python
# LC 139 - Word Break -- ❌ WRONG, do not write this
# IDEA: item-outer, copied from the coin-change shape. One pass per word.
for word in wordDict:                  # ❌ dictionary OUTSIDE
    w = len(word)
    for i in range(w, n + 1):
        if dp[i - w] and s[i - w:i] == word:
            dp[i] = True
```

It even passes Example 1 (`"leetcode"`), which is why it survives a quick test.

**What it actually computes**: `dp[i]` is true iff `s[:i]` splits into words whose **dictionary
indices are non-decreasing**. Word 1's pass has closed by the time word 2's pass creates the cell
word 1 needed — the dependency `dp[i] ← dp[i - len(w)]` is satisfied by *index*, but the value sitting
in `dp[i - len(w)]` is not yet final: later passes will still change it.

That is failure mode **1** from §0-3, and it is the entire reason the repo solution nests the loops
the way it does.

### 2-5) The case it cannot survive — `"applepenapple"`

LC 139's own Example 2. The answer is `True` (`apple | pen | apple`):

```text
s = "applepenapple"  (n = 13),  dict = ["apple", "pen"]

pass 1 — word = "apple" (len 5)
    i=5   dp[0]=True,  s[0:5]  == "apple"   -> dp[5]  = True
    i=10  dp[5]=True,  s[5:10] == "penap"   ✗
    i=13  dp[8]=False                       ✗   ← dp[8] does not exist YET
    state: dp true at {0, 5}

pass 2 — word = "pen" (len 3)
    i=8   dp[5]=True,  s[5:8]  == "pen"     -> dp[8]  = True   ← too late for pass 1
    i=13  dp[10]=False                      ✗
    state: dp true at {0, 5, 8}

    dp[13] = False   ❌   (correct answer: True)
```

> `"apple"` is needed **both before and after** `"pen"`, and a single pass over the dictionary can
> only use a word at one moment in time.

And no re-ordering rescues it — `["pen", "apple"]` fails too — which is the §1-4 test failing
outright, not the §1-5 shuffle test being unlucky:

| Input | Order A / B | Order C, dict `[apple, pen]` | Order C, dict `[pen, apple]` |
|---|---|---|---|
| `"leetcode"`, `[leet, code]` | True ✅ | True ✅ | True ✅ |
| `"codeleet"`, `[leet, code]` | True ✅ | **False** ❌ | True ✅ |
| `"applepenapple"`, `[apple, pen]` | True ✅ | **False** ❌ | **False** ❌ |
| `"catsandog"`, `[cats, dog, sand, and, cat]` | False ✅ | False ✅ | False ✅ |

### 2-6) Salvaging Order C — iterate to a fixpoint

Order C is one relaxation round of what is really a reachability closure. Repeat it until nothing
changes and it becomes correct — the Bellman–Ford trick:

```python
# python
# LC 139 - Word Break -- item-outer made correct by iterating to a fixpoint
# IDEA: each round can only add True cells; stop when a round adds none
# time = O(rounds * n * k * L), rounds <= n / min_word_len + 1, space = O(n)
changed = True
while changed:
    changed = False
    for word in wordDict:
        w = len(word)
        for i in range(w, n + 1):
            if not dp[i] and dp[i - w] and s[i - w:i] == word:
                dp[i] = True
                changed = True
```

`"applepenapple"` needs **3 rounds** (two productive, one to prove it is done) — versus one pass for
Order A. Correct, strictly slower, and worth knowing only because it names what Order C was missing:
one pass is not a closure.

### 2-7) Order D — BFS over boundaries: let the queue choose the order

```python
# python
# LC 139 - Word Break
# IDEA: same DAG, but the topological order is discovered at run time instead of hard-coded
# time = O(n^2) (or O(n * k * L) scanning words), space = O(n)
from collections import deque

q = deque([0])
visited = {0}
while q:
    idx = q.popleft()
    if idx == n:
        return True
    for word in wordDict:
        end = idx + len(word)
        if end <= n and s[idx:end] == word and end not in visited:
            visited.add(end)
            q.append(end)
return False
```

`idx` is the same **boundary** `dp[i]` indexes — the frontier between "already segmented" and "not
yet looked at", which is why it legitimately reaches `n`. BFS cannot have a loop-order bug, because
it never reads a cell: it only ever pushes forward from a boundary already proven reachable.
`visited` is not an optimisation here, it is what keeps the queue from re-expanding a boundary
exponentially.

### 2-8) Order E — top-down memo: no loop order at all

```python
# python
# LC 139 - Word Break
# IDEA: recursion discovers the dependency order itself; memo makes it O(n^2)
# time = O(n^2 * L), space = O(n) memo + O(n) stack
from functools import lru_cache

@lru_cache(None)
def can(start):
    if start == n:
        return True
    return any(s.startswith(word, start) and can(start + len(word)) for word in wordDict)

return can(0)
```

Nothing above it can be mis-ordered — which is exactly the §0-4 diagnostic: this is the version to
reach for when a tabulated attempt disagrees with expectation and you want to know which of the two
is wrong.

### 2-9) Which one to write, and what each costs

With LC 139's constraints — `n ≤ 300`, `k ≤ 1000`, `L ≤ 20`:

| Order | Nesting | Correct? | Complexity | Worst case here | Write it when |
|---|---|---|---|---|---|
| **A** | index → word | ✅ | O(n·k·L) | ~6 × 10⁶ char compares | the dictionary is small relative to `s` |
| **B** | index → cut | ✅ | O(n²) cuts × O(L) hash | ~1.8 × 10⁶ | **the default answer** |
| **B-capped** | index → cut, `j ≥ i - L` | ✅ | O(n·L²) | ~1.2 × 10⁵ | the dictionary is large; the fastest here |
| **C** | word → index | ❌ | — | — | never |
| **C-fixpoint** | word → index, repeated | ✅ | O(rounds·n·k·L) | rounds ≤ 16 | never, for this problem |
| **D** | BFS | ✅ | O(n²) | ~1.8 × 10⁶ | you find reachability easier to reason about |
| **E** | memo | ✅ | O(n²·L) | — | you want the recurrence checked, or top-down is more natural |

> **Interview answer**: write **B**, mention the `L` cap as the optimisation, and say out loud
> *"the dictionary loop has to be inside, because the words are pinned to positions in `s` — this is
> not coin change."* That sentence is the signal; the code is not.

## 3) The same question on four other problems

### 3-1) LC 279 Perfect Squares — the shape where item-outer *is* legal ⭐⭐⭐⭐

Identical skeleton to LC 139: a prefix state, a menu of items, `dp[i] ← dp[i - size]`. Only now the
items are **added**, so §1-4's test passes and both nestings are correct:

```python
# python
# LC 279 - Perfect Squares
# IDEA: unbounded knapsack (min). Squares are summed, so they re-order freely -> both nestings work
# time = O(n * sqrt(n)), space = O(n)
sq = [k * k for k in range(1, int(n ** 0.5) + 1)]
dp = [0] + [float("inf")] * n

for val in sq:                       # item-outer -- legal here, illegal in LC 139
    for j in range(val, n + 1):
        dp[j] = min(dp[j], dp[j - val] + 1)
return dp[n]
```

Swap the two loops and every answer is unchanged (verified for `n = 1..399`). The one-line reason:

```text
LC 279   12 = 4 + 4 + 4   -- the multiset can be walked in ANY order,      so a single pass per square suffices
LC 139   "applepenapple"  -- the words must appear in the order s says,    so a single pass per word does not
```

The min-vs-count distinction of §3-2 applies here too: LC 279 minimises, so even the *counting*
subtlety is moot.

### 3-2) LC 518 vs LC 377 — when the order changes the question, not the correctness ⭐⭐⭐⭐⭐

Both are legal DPs. They answer different questions, and the loop nest is the only difference:

```python
# python
# LC 518 - Coin Change II -- COMBINATIONS: coins outer
for coin in coins:
    for i in range(coin, amount + 1):
        dp[i] += dp[i - coin]

# python
# LC 377 - Combination Sum IV -- PERMUTATIONS: amount outer
for i in range(1, target + 1):
    for num in nums:
        if i >= num:
            dp[i] += dp[i - num]
```

```text
coins = [1, 2], amount = 3

coin-outer   -> 2 : {1,1,1}, {1,2}            each multiset counted ONCE
amount-outer -> 3 : {1,1,1}, {1,2}, {2,1}     every ORDERING counted
```

Item-outer's restriction — "items in the loop's order" — is not a bug here, it is the *feature*: it
is precisely what collapses `{1,2}` and `{2,1}` into one. LC 322 (minimise) is indifferent to both.
Full traces and the decision tree: [knapsack.md](./knapsack.md#loop-order-combinations-vs-permutations)
and [dp_advanced.md](./dp_advanced.md).

> Do not carry this lesson to LC 139. Here item-outer under-counts *by design*; there it
> under-answers *by accident*.

### 3-3) LC 416 — when direction, not nesting, is the problem ⭐⭐⭐⭐

```python
# python
# LC 416 - Partition Equal Subset Sum
# IDEA: 0/1 knapsack -- each number once, so the capacity loop must NOT see its own writes
# time = O(n * target), space = O(target)
for num in nums:
    for s_ in range(target, num - 1, -1):    # ❗ descending
        dp[s_] = dp[s_] or dp[s_ - num]
```

Here item-outer is fine (numbers are summed) — the trap is the inner **direction**. Ascending, the
pass reads cells it just wrote and reuses `num`, turning 0/1 into unbounded. That is failure mode
**2**, and it is the mirror image of LC 139: same nesting, different axis.

### 3-4) Floyd–Warshall — item-outer that *must* be outermost ⭐⭐⭐

```python
# python
# IDEA: k = "may paths route through vertex k?" -- an ITEM loop, and it belongs outside
# time = O(V^3), space = O(V^2)
for k in range(V):                 # ❗ intermediate vertex -- OUTERMOST
    for i in range(V):
        for j in range(V):
            dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
```

The 3-D recurrence is `dp[k][i][j] = min(dp[k-1][i][j], dp[k-1][i][k] + dp[k-1][k][j])`; collapsing
away the `k` dimension is only sound while `k` is the outer sweep. Put `k` innermost and the code
still runs, still terminates, and computes something that is not all-pairs shortest path.

It is legal for §1-4's reason: the set of intermediates on a path can always be re-ordered by index.
Details in [Floyd-Warshall.md](./Floyd-Warshall.md).

### 3-5) Side by side

| Problem | Items | Compose by | Legal nestings | Inner direction |
|---|---|---|---|---|
| **LC 139** Word Break | words | concatenation, positions pinned | **state-outer only** | ascending |
| **LC 279** Perfect Squares | squares | sum (min) | either | ascending |
| **LC 322** Coin Change | coins | sum (min) | either | ascending |
| **LC 518** Coin Change II | coins | sum (count multisets) | **item-outer** — it defines the answer | ascending |
| **LC 377** Combination Sum IV | nums | sum (count sequences) | **state-outer** — it defines the answer | ascending |
| **LC 416** Subset Sum | nums | sum, each once | either | **descending** |
| **Floyd–Warshall** | vertices | path concatenation, re-orderable | **item-outer, outermost** | either |

## 4) Debugging a suspected loop-order bug

```text
1.  Shuffle the item list, re-run.            answer moved  -> item loop is wrongly outside (§1-5)
2.  Write the memoised version (§0-4).        they disagree -> the LOOP is wrong, not the recurrence
3.  Print dp[] after every outer iteration.   a cell flips True LATE -> something read it too early
4.  Re-run the item-outer loop to a fixpoint. answer changes -> one pass was not a closure (§2-6)
5.  Ask §1-4 out loud: "can a solution's items be re-ordered into my loop's order?"
```

Steps 1 and 4 are ten lines each and settle it without any reasoning at all — worth running before
staring at the transition again.

## 5) Common mistakes

- ❌ **Porting the coin-change nest to Word Break.** The single most common form of this bug, and
  the reason for §2. Coins are summed; words are concatenated at positions `s` already fixed.
- ❌ **Testing only Example 1.** `"leetcode"` passes under the broken nesting. `"applepenapple"` —
  the *next* example in the same problem statement — does not.
- ❌ **Letting a `set`'s iteration order reach the answer.** If shuffling changes the result, the
  nesting is wrong; `set` only hides which order you were relying on.
- ❌ **Assuming "both orders work" because someone said so about LC 322.** It is true of LC 322,
  because it minimises over sums. It is false for LC 518/377 (different answers) and for LC 139
  (one is wrong).
- ❌ **Fixing a nesting bug by flipping the inner direction** (or vice versa). Nesting and direction
  are independent axes — §1-3 and §1-2 — and a wrong diagnosis usually swaps one correct answer for
  a different wrong one.
- ❌ **Reaching for a loop order at all when the dependency graph has a cycle.** No topological order
  exists; that problem wants BFS/Dijkstra/Bellman–Ford.

## 6) 中文速記

> **核心原則**：計算 `dp[i]` 時，它所依賴的更小狀態必須已經被計算完畢。

| 要問的問題 | 決定什麼 |
|---|---|
| 轉移式右邊讀了哪些 `dp[...]`？ | 迴圈**方向**（正序 / 倒序） |
| 同一個 item 可不可以在這一輪被重複使用？ | 內層方向：不可以 → **倒序**（0/1）；可以 → **正序**（完全背包） |
| 解答中的 items 可不可以**任意重排**？ | item 迴圈可不可以放外層 |

- **可以重排**（硬幣、平方數：它們是「相加」）→ item 放外層合法。LC 322 / 279 兩種寫法答案相同；
  LC 518 更是**必須**把 coin 放外層，才會把 `{1,2}` 和 `{2,1}` 算成同一種。
- **不可以重排**（LC 139 的單字：它們是「接起來」，而且位置早就被 `s` 決定了）→ item **只能放內層**。
  把 word 放外層會得到「單字必須照字典順序出現」的錯誤答案：`"applepenapple"` 會回傳 `False`，
  因為 `"apple"` 在 `"pen"` 的前後都要用到，但一輪只能用一次。

> 一句話：**`for i: for word:` 正確，`for word: for i:` 錯誤** —— 不是風格問題，是 `s` 已經把單字的
> 先後順序決定了，item 迴圈沒有權利再決定一次。

## 7) Summary

- A bottom-up DP is a topological sort you wrote by hand. Every loop-order rule is that one
  constraint wearing a different hat.
- **Direction** answers *"may this pass see its own writes?"* — descending for 0/1, ascending for
  unbounded.
- **Nesting** answers *"may the item loop decide the order the items are used in?"* — yes when the
  items commute (sums), no when the input already fixed the order (strings).
- LC 139 is the family's one hard no, and it is worth knowing as a sentence rather than as a shape:
  *coins are added, words are concatenated.*
- When in doubt, memoise. Top-down cannot have this bug, and disagreeing with it localises the bug
  in one run.
