# The Stuck 22 — a derivation drill

> **Scope** — The 22 problems that are Google-tagged, on a curated list, and have been marked
> `AGAIN` sixteen or more times in [`data/progress.txt`](../data/progress.txt). This is not a
> cheatsheet and not a solution dump: it is the one page to read *before* the next attempt,
> because the previous twenty-three did not stick.
> **See also**: [cheatsheet-interviewer-review-2026-09.md](cheatsheet-interviewer-review-2026-09.md) — where this list came from (finding L3).

## Why this page exists

```text
LC 91   Decode Ways                    23 attempts   still AGAIN
LC 139  Word Break                     22 attempts   still AGAIN
LC 1143 Longest Common Subsequence     20 attempts   still AGAIN
```

Nobody who has attempted Decode Ways twenty-three times has a knowledge gap about Decode Ways.
They know what the answer looks like. Across these 22 problems the log holds roughly **390
attempts that did not retire a single one of them**, which is a method problem wearing a
knowledge problem's clothes.

The method that produced those attempts was: read the problem, half-remember the shape, write
code, check against the tests, mark `AGAIN`. That loop never exercises the thing a Google
interview actually scores, which is **derivation** — and so it never builds the thing that
would make attempt 24 different from attempt 23.

## The retire test

A problem leaves this list when, **cold, with nothing open**, you can do all four:

1. **State the brute force and its complexity** out loud, before writing anything.
2. **Name what is repeated** in it — the single recomputation the optimal solution removes.
3. **Say the invariant as one sentence**, and only then start typing.
4. **Code it in under 20 minutes and dry-run it on paper** — no interpreter — finding your own
   bugs.

Miss any one and it stays `AGAIN`. That bar is deliberately higher than "the tests passed";
the tests passing is what has happened ~390 times already.

**Steps 1–3 are the whole point.** If you skip to the code because you remember the code, the
attempt does not count — you have practised recall, and recall is what keeps failing.

## How to use it

One problem per sitting. Read only its entry, close the page, then run the retire test. The
entries give you the derivation, never the code — the code is in
[`leetcode_python/`](../leetcode_python/) and looking at it before you have said the invariant
out loud is how the last twenty-three attempts went.

They are ordered by family, not by attempt count, because six of the top seven are one
recurrence wearing different clothes — which is a much smaller problem than a list of 22.

---

## Family 1 — 1-D sequence DP

Six problems, one idea: **`dp[i]` is the answer for the prefix (or suffix) ending at `i`, and
the recurrence only ever looks backwards a bounded distance.** If this family lands, a third
of the list goes with it.

### LC 91 — Decode Ways · 23 attempts

- **Brute force** — at each index, take one digit or two, recurse on the rest. O(2ⁿ).
- **What repeats** — `ways(i)` is recomputed from every path that reaches `i`.
- **Invariant** — say this before typing:
  > `dp[i]` = the number of ways to decode the suffix `s[i:]`.
- **Recurrence** — `dp[i] = dp[i+1]` if `s[i] != '0'`, **plus** `dp[i+2]` if `10 ≤ int(s[i:i+2]) ≤ 26`.
- **Why it keeps failing** — it is never the DP. It is the zeros. `"0"` decodes to nothing,
  `"06"` is not `6`, but `"10"` and `"20"` are single valid letters. Write the three cases
  `"0"`, `"06"`, `"10"` on paper *first* and make the recurrence answer them.

### LC 139 — Word Break · 22 attempts

- **Brute force** — try every prefix that is in the dictionary, recurse on the remainder. O(2ⁿ).
- **What repeats** — `breakable(i)` for the same `i`, reached by different splits.
- **Invariant**
  > `dp[i]` = `True` iff `s[:i]` can be segmented completely.
- **Recurrence** — `dp[i] = any(dp[j] and s[j:i] in words for j in range(i))`, seeded `dp[0] = True`.
- **Why it keeps failing** — the seed. `dp[0] = True` is the empty prefix, and without it every
  entry is `False` and the bug is invisible until the end. Also: loop over **split points**,
  not over dictionary words, or the complexity argument falls apart.

### LC 322 — Coin Change · 18 attempts

- **Brute force** — recurse subtracting each coin. O(cᵃ).
- **What repeats** — `minCoins(a)` for the same remaining amount.
- **Invariant**
  > `dp[a]` = the fewest coins summing to exactly `a`, `∞` if it cannot be done.
- **Recurrence** — `dp[a] = 1 + min(dp[a-c] for c in coins if c <= a)`, seeded `dp[0] = 0`.
- **Why it keeps failing** — returning `dp[amount]` without converting `∞` back to `-1`. Note
  the loop order is *free* here because `min` is order-independent — unlike LC 518, where it
  decides combinations vs permutations. Knowing which of the two you are in is the follow-up.

### LC 53 — Maximum Subarray · 16 attempts

- **Brute force** — every `(i, j)` pair, sum it. O(n²) (or O(n³) naively).
- **What repeats** — re-summing overlapping ranges.
- **Invariant** — the sentence the whole problem hangs on:
  > `cur` = the largest sum of a subarray **ending exactly at `i`**.
- **Recurrence** — `cur = max(nums[i], cur + nums[i])`; `best = max(best, cur)`.
- **Why it keeps failing** — initialising `best = 0`. On an all-negative array the answer is
  the largest single element, and a zero seed silently returns 0. Seed with `nums[0]` or `-∞`.

### LC 300 — Longest Increasing Subsequence · 19 attempts

- **Brute force** — every subsequence. O(2ⁿ).
- **O(n²) DP** — `dp[i] = 1 + max(dp[j] for j < i if nums[j] < nums[i])`. This is the answer
  you should be able to produce in three minutes; the O(n log n) is the follow-up.
- **What repeats in the O(n²)** — the inner scan for "where does this value belong".
- **Invariant** for the O(n log n):
  > `tails[k]` = the **smallest possible tail** of an increasing subsequence of length `k+1`.
  > `tails` is therefore sorted, so the scan becomes a binary search.
- **Why it keeps failing** — believing `tails` *is* the LIS. It is not; it is the right
  *length* with, usually, the wrong contents. Say that out loud and the confusion stops.
  Strict vs non-strict increasing is `bisect_left` vs `bisect_right`.

### LC 416 — Partition Equal Subset Sum · 19 attempts

- **Brute force** — every subset, check its sum. O(2ⁿ).
- **Reduction** — if `total` is odd, return `False` immediately; else target = `total // 2`.
  That one line is half the problem and is the thing to say first.
- **Invariant**
  > `dp[s]` = can a subset of the items seen so far sum to exactly `s`.
- **Recurrence** — for each `num`, iterate `s` from `target` **down to** `num`:
  `dp[s] |= dp[s - num]`.
- **Why it keeps failing** — the backwards loop. Forwards lets one item be spent twice, which
  silently solves *unbounded* knapsack instead — and unbounded gives the right answer on most
  small test cases, so it passes and then fails in the interview.

---

## Family 2 — 2-D string DP

### LC 1143 — Longest Common Subsequence · 20 attempts

- **Brute force** — at `(i, j)`: if the characters match consume both, else branch on skipping
  either. O(2^(m+n)).
- **What repeats** — the `(i, j)` pair, reached by many different paths.
- **Invariant**
  > `dp[i][j]` = the LCS length of the prefixes `a[:i]` and `b[:j]`.
- **Recurrence** — match → `dp[i-1][j-1] + 1`; otherwise `max(dp[i-1][j], dp[i][j-1])`.
- **Why it keeps failing** — the index shift. `dp` is 1-indexed over lengths and the strings
  are 0-indexed, so the compared characters are `a[i-1]` and `b[j-1]`. Write the 3×3 table for
  `"ab"` / `"ba"` by hand once and the off-by-one stops recurring.

### LC 5 — Longest Palindromic Substring · 19 attempts

- **Brute force** — every substring, check it. O(n³).
- **What repeats** — the palindrome check re-walks characters an enclosing substring already
  verified.
- **Invariant** — expand-around-centre, and prefer it to the DP table in an interview
  (O(n²) time but O(1) space, and far less to get wrong):
  > every palindrome has a centre; there are `2n-1` of them, `n` single characters and `n-1`
  > gaps between characters.
- **Why it keeps failing** — only looping the `n` odd centres. `"abba"` has its centre in a
  *gap*, so every even-length palindrome is missed — and half the test cases still pass.

---

## Family 3 — prefix sums and sliding windows

The dividing line: a window works when growing it moves the metric one way and shrinking it
moves it back. **If values can be negative, that is false and the window is the wrong tool.**

### LC 560 — Subarray Sum Equals K · 17 attempts

- **Brute force** — every `(i, j)`, sum it. O(n²).
- **What repeats** — the re-summation of shared prefixes.
- **Invariant**
  > `count[p]` = how many prefixes seen so far have sum `p`; at each index, a subarray ending
  > here sums to `k` exactly when some earlier prefix equals `prefix - k`.
- **Why it keeps failing** — two things, both fatal and both quiet. Seed `count[0] = 1`, or
  every subarray starting at index 0 is missed. And **a sliding window does not work here** —
  the array can contain negatives, so shrinking does not lower the sum. Reaching for the window
  is the single most common wrong start.

### LC 3 — Longest Substring Without Repeating Characters · 16 attempts

- **Brute force** — every substring with a set. O(n²).
- **Invariant**
  > the window `[l, r]` never contains a duplicate; `last[c]` is the most recent index of `c`.
- **Move** — on seeing `c`: `l = max(l, last[c] + 1)`, then record `last[c] = r`.
- **Why it keeps failing** — dropping the `max(...)`. A repeat of a character that fell out of
  the window long ago drags `l` *backwards* and the window silently re-admits duplicates.

### LC 424 — Longest Repeating Character Replacement · 16 attempts

- **Brute force** — every window, count its characters. O(n²·26).
- **Invariant** — the whole problem in one line:
  > a window is valid iff `len(window) - maxFreq <= k` — the characters that are not the
  > majority one are exactly the ones you must replace.
- **Why it keeps failing** — `maxFreq` is never recomputed downward, which looks like a bug and
  is not. The window never shrinks below the best length already found, so a stale `maxFreq`
  can only fail to *grow* the answer, never corrupt it. If you cannot explain that, you will
  "fix" it under pressure and break the complexity.

---

## Family 4 — monotonic stack and stack parsing

### LC 739 — Daily Temperatures · 19 attempts

- **Brute force** — for each day scan forward to the first warmer one. O(n²).
- **What repeats** — the forward scan re-walks days a previous scan already rejected.
- **Invariant**
  > the stack holds **indices** whose answer is still unknown, with strictly decreasing
  > temperatures.
- **Move** — while the stack is non-empty and `T[i] > T[stack[-1]]`: pop `j`, set `ans[j] = i - j`.
- **Why it keeps failing** — pushing temperatures instead of indices. The answer is a
  *distance*, so the index is the payload; the value is only the comparison key.

### LC 394 — Decode String · 19 attempts

- **Brute force** — repeatedly find the innermost `[...]` and expand it. O(n²) or worse.
- **Invariant**
  > on `[`, push `(string so far, repeat count)` and reset both; on `]`, pop and set
  > `cur = prev + num * cur`.
- **Why it keeps failing** — multi-digit counts. `100[a]` needs `num = num * 10 + digit`
  accumulated across characters; reading one digit works on every small example and then
  quietly fails.

---

## Family 5 — binary search, tree and list invariants

### LC 153 — Find Minimum in Rotated Sorted Array · 16 attempts

- **Brute force** — linear scan. O(n). Say it, then improve it.
- **Invariant**
  > the minimum always lies in the **unsorted** half — and you detect which half that is by
  > comparing `nums[mid]` to `nums[hi]`, never to `nums[lo]`.
- **Move** — `nums[mid] > nums[hi]` → `lo = mid + 1`; else `hi = mid`. Loop `while lo < hi`,
  answer `nums[lo]`.
- **Why it keeps failing** — two classics. Comparing against `nums[lo]` breaks on an array that
  was not rotated at all. And `hi = mid`, not `mid - 1`, because `mid` may *be* the minimum.

### LC 98 — Validate Binary Search Tree · 18 attempts

- **The wrong solution** — comparing each node only to its parent. The counterexample to have
  ready: `[10, 5, 15, null, null, 6, 20]` — `6` is a legitimate left child of `15`, but it sits
  in the right subtree of `10` while being smaller than `10`.
- **Invariant**
  > every node must lie strictly inside an open interval `(low, high)` that narrows on the way
  > down — go left and `high` becomes the node's value, go right and `low` does.
- **Alternative** — an in-order walk must be strictly increasing; carry `prev`.
- **Why it keeps failing** — using `INT_MIN` / `INT_MAX` as the initial bounds, when LeetCode's
  tests include nodes holding exactly those values. Use `None` / `±inf`.

### LC 24 — Swap Nodes in Pairs · 16 attempts

- **Invariant**
  > `prev` always points at the node immediately before the pair being swapped; a dummy head
  > removes the special case for the first pair.
- **Move** — `first = prev.next`, `second = first.next`, then rewire
  `prev.next = second`, `first.next = second.next`, `second.next = first`, and advance
  `prev = first`.
- **Why it keeps failing** — pointer order. Every rewrite must read a pointer *before* anything
  overwrites it. Draw the three arrows on paper and number them; that is a 60-second exercise
  that removes the bug permanently.

### LC 31 — Next Permutation · 18 attempts

- **Brute force** — generate all permutations, sort, take the next. O(n!) — unusable, but say it.
- **Invariant** — four steps, and the suffix property is the one that makes it O(n):
  > scan from the right for the first `i` with `a[i] < a[i+1]` (the **pivot**). Everything to
  > the right of the pivot is non-increasing.
- **Move** — find the rightmost `j > i` with `a[j] > a[i]`, swap them, then **reverse** the
  suffix.
- **Why it keeps failing** — *sorting* the suffix instead of reversing it. It is already
  non-increasing, so reversing gives ascending order in O(n) rather than O(n log n) — and if
  no pivot exists the array is the last permutation, so reverse the whole thing.

---

## Family 6 — BFS and graph bookkeeping

### LC 207 — Course Schedule · 16 attempts

- **Invariant** (Kahn's)
  > the queue holds exactly the nodes with in-degree 0; a cycle exists iff fewer than `n` nodes
  > are ever dequeued.
- **Why it keeps failing** — edge direction. `[a, b]` means *take `b` before `a`*, so the edge
  runs `b → a` and it is `a`'s in-degree that increments. Getting this backwards produces a
  solution that is right on symmetric test cases and wrong in the interview.

### LC 133 — Clone Graph · 16 attempts

- **Invariant**
  > a `{original: clone}` map, and the clone is inserted into the map **before** recursing into
  > neighbours.
- **Why it keeps failing** — creating the clone after the recursion. On any cycle that never
  terminates, and every interesting test case has a cycle.

### LC 127 — Word Ladder · 16 attempts (Hard)

- **Brute force** — compare every pair of words to build the graph. O(N²·L).
- **What repeats** — the pairwise character comparisons.
- **Better** — bucket words by wildcard pattern (`hot` → `*ot`, `h*t`, `ho*`), giving
  O(N·L·26) neighbours without ever comparing two words directly.
- **Invariant** — BFS level = number of transformations; **mark visited at enqueue, not at
  dequeue** (see [bfs.md](cheatsheet/bfs.md) Pattern 3.1).
- **Why it keeps failing** — marking at dequeue. The same word is enqueued once per path that
  reaches it, and the queue explodes on the larger test cases.

---

## Family 7 — backtracking and greedy

### LC 78 — Subsets · 16 attempts

- **Invariant**
  > at index `i`, `path` holds a decided subset of `nums[:i]` — and **every node of the tree is
  > an answer**, not just the leaves.
- **Why it keeps failing** — `res.append(path)` instead of `res.append(path[:])`. You store a
  reference to a list that backtracking then empties, so the output is N copies of `[]`. This
  is the single most common backtracking bug in any language with mutable lists.

### LC 435 — Non-overlapping Intervals · 16 attempts

- **Brute force** — try every subset of intervals. O(2ⁿ).
- **Invariant**
  > sort by **end**, keep the interval that finishes earliest, and count the ones that overlap
  > what you kept.
- **The exchange argument** — say it out loud, because Google asks for it: swapping your
  earliest-finishing pick for any interval that ends later leaves strictly less room for what
  follows, so it can never improve the result.
- **Why it keeps failing** — sorting by **start**. It is the intuitive choice, it is wrong, and
  it passes a surprising number of tests before it does not.

---

## Tracking

Re-run the query that produced this list after each session:

```bash
python3 script/eval_lc_readiness.py     # the profile-side view
```

The list is correct as of **2026-09-13**. It shrinks when a problem passes the four-step retire
test, not when it is attempted again — which is the only difference between this page and the
previous ~390 attempts.
