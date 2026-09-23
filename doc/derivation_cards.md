# Derivation Cards

> **Scope** — One card per chronic problem: the invariant, the contract of the recursion or
> the loop state, the one line that sets the complexity, the edges that keep failing, and a
> skeleton short enough to re-derive from — written once, so the problem is reviewed instead
> of re-solved. Forty cards, chosen by cost (see below). Not a cheatsheet: the pattern's full
> template and its variants live in [`cheatsheet/`](./cheatsheet/); a card only holds what
> *this* problem turns on.
> **See also**: [`lc-readiness-guide.md`](./lc-readiness-guide.md) — the cost curve these
> cards answer to; the September 2026 project review (PR #172), §1.3 — why cards and not
> another pass.

## How to use a card

A problem on this page has been logged up to thirty-eight times and is still marked
`again`, or has been marked `AGAIN` in README after twelve or more passes (LC 1248 is here on
that second path, at seven logged attempts). The pattern is not the problem — the pattern is on a cheatsheet the reader already
knows. What is missing is the *derivation*: the one observation that turns the brute force
into the template, and the one line where the complexity is decided. That is what a card
holds, and nothing else.

1. **Read the card, not the solution.** Two minutes. Say the invariant out loud; name the
   complexity line; name the two edges.
2. **Write the skeleton from memory.** If the invariant is right, the code follows. If the
   code does not follow, the invariant is the thing to fix, not the code.
3. **Log the verdict.** `ok` only if step 2 came out clean and unaided; otherwise `again`.
   The card is reviewed again in a week either way — the schedule is `/lc-log`'s job.

A card is short on purpose. The moment one needs a second template or a second page, the
problem has more than one idea in it and belongs on a cheatsheet.

## How the forty were chosen

Two records, one score. `data/progress.txt` gives each problem its attempt count and its
latest verdict; README's status column gives its recorded pass count (the `*` run). A
problem qualified if its latest log verdict is `again` after **8+ attempts**, or README
shows **12+ passes** with `AGAIN` in an interview-core section. Ranked by `2 × attempts +
passes`, top forty, two already `OK` in README dropped (LC 235, 572). Sep 2026 numbers.

| # | Problem | Section | Log attempts | README passes |
|---|---|---|---|---|
| 300 | [Longest Increasing Subsequence](#300-longest-increasing-subsequence) | Binary Search | 38 | 19 |
| 207 | [Course Schedule](#207-course-schedule) | BFS | 26 | 16 |
| 139 | [Word Break](#139-word-break) | Backtracking | 20 | 22 |
| 322 | [Coin Change](#322-coin-change) | DP | 20 | 18 |
| 91 | [Decode Ways](#91-decode-ways) | DP | 17 | 23 |
| 394 | [Decode String](#394-decode-string) | Stack | 17 | 19 |
| 153 | [Find Minimum in Rotated Sorted Array](#153-find-minimum-in-rotated-sorted-array) | Binary Search | 17 | 16 |
| 128 | [Longest Consecutive Sequence](#128-longest-consecutive-sequence) | Sort | 17 | 15 |
| 133 | [Clone Graph](#133-clone-graph) | BFS | 16 | 16 |
| 1143 | [Longest Common Subsequence](#1143-longest-common-subsequence) | DP | 14 | 20 |
| 97 | [Interleaving String](#97-interleaving-string) | DP | 17 | 12 |
| 79 | [Word Search](#79-word-search) | Backtracking | 15 | 15 |
| 2289 | [Steps to Make Array Non-decreasing](#2289-steps-to-make-array-non-decreasing) | Stack | 16 | 12 |
| 104 | [Maximum Depth of Binary Tree](#104-maximum-depth-of-binary-tree) | Recursion | 16 | 12 |
| 323 | [Number of Connected Components in an Undirected Graph](#323-number-of-connected-components-in-an-undirected-graph) | Graph | 15 | 14 |
| 739 | [Daily Temperatures](#739-daily-temperatures) | Stack | 12 | 19 |
| 261 | [Graph Valid Tree](#261-graph-valid-tree) | BFS | 15 | 13 |
| 416 | [Partition Equal Subset Sum](#416-partition-equal-subset-sum) | DP | 12 | 19 |
| 53 | [Maximum Subarray](#53-maximum-subarray) | DP | 13 | 16 |
| 5 | [Longest Palindromic Substring](#5-longest-palindromic-substring) | String | 11 | 19 |
| 525 | [Contiguous Array](#525-contiguous-array) | Hash Table | 11 | 18 |
| 435 | [Non-overlapping Intervals](#435-non-overlapping-intervals) | Greedy | 12 | 16 |
| 560 | [Subarray Sum Equals K](#560-subarray-sum-equals-k) | Hash Table | 11 | 17 |
| 926 | [Flip String to Monotone Increasing](#926-flip-string-to-monotone-increasing) | DP | 12 | 15 |
| 3 | [Longest Substring Without Repeating Characters](#3-longest-substring-without-repeating-characters) | Hash Table | 11 | 16 |
| 424 | [Longest Repeating Character Replacement](#424-longest-repeating-character-replacement) | Hash Table | 11 | 16 |
| 227 | [Basic Calculator II](#227-basic-calculator-ii) | Stack | 12 | 14 |
| 143 | [Reorder List](#143-reorder-list) | Two Pointers | 12 | 14 |
| 776 | [Split BST](#776-split-bst) | BST | 13 | 12 |
| 127 | [Word Ladder](#127-word-ladder) | BFS | 11 | 16 |
| 32 | [Longest Valid Parentheses](#32-longest-valid-parentheses) | Stack | 13 | 10 |
| 652 | [Find Duplicate Subtrees](#652-find-duplicate-subtrees) | Tree | 11 | 14 |
| 253 | [Meeting Rooms II](#253-meeting-rooms-ii) | Sort | 12 | 12 |
| 767 | [Reorganize String](#767-reorganize-string) | Greedy | 9 | 18 |
| 1248 | [Count Number of Nice Subarrays](#1248-count-number-of-nice-subarrays) | Array | 7 | 21 |
| 450 | [Delete Node in a BST](#450-delete-node-in-a-bst) | BST | 9 | 17 |
| 213 | [House Robber II](#213-house-robber-ii) | DP | 10 | 15 |
| 402 | [Remove K Digits](#402-remove-k-digits) | Greedy | 11 | 13 |
| 269 | [Alien Dictionary](#269-alien-dictionary) | Graph | 10 | 15 |
| 110 | [Balanced Binary Tree](#110-balanced-binary-tree) | Recursion | 10 | 14 |

Each README row above carries a `[card]` link in its Note column pointing back here.

---

## Arrays, hashing and windows

### 3. Longest Substring Without Repeating Characters

`Hash Table` · Medium · 11 attempts · 16 passes

- **Pattern** — variable window with a *last-seen index* map.
- **Invariant** — `s[l..r]` has no repeated character; `last[c]` is the most recent index of `c` anywhere in `s[..r]`.
- **Contract** — when `s[r]` was last seen at `j >= l`, the window cannot contain both, so `l` jumps to `j + 1` — a jump, not a step. `l` never moves left, so `max(l, last[c] + 1)` is the guard.
- **The line that sets the complexity** — `l = max(l, last[c] + 1)`: each pointer moves right at most `n` times, O(n). Shrinking one character at a time in a `while` is also O(n) but is the version that gets miscounted as O(n²).
- **Edges** — empty string → 0; all one character → 1; the repeat is *before* `l` (`last[c] < l`) and must be ignored — that is what the `max` is for.
- **Skeleton**

  ```python
  last, l, best = {}, 0, 0
  for r, c in enumerate(s):
      if c in last: l = max(l, last[c] + 1)
      last[c] = r
      best = max(best, r - l + 1)
  return best
  ```

- **Say out loud** — "The left pointer jumps to just past the previous copy, and never goes backwards."

### 424. Longest Repeating Character Replacement

`Hash Table` · Medium · 11 attempts · 16 passes

- **Pattern** — variable window with a count table and a *monotone* `maxf`.
- **Invariant** — a window is valid iff `len − maxf ≤ k`: every non-majority character is replaced. `maxf` is the largest count *any* window so far has had for one letter.
- **Contract** — `maxf` is never decreased on shrink. That looks wrong and is correct: the answer can only improve when `maxf` improves, so a stale `maxf` only keeps a window that was already the best length, never overstates a new one.
- **The line that sets the complexity** — `while (r - l + 1) - maxf > k: count[s[l]] -= 1; l += 1` — amortised O(n) over 26 letters. Recomputing `max(count.values())` inside the loop makes it O(26n); still linear, but say so.
- **Edges** — `k ≥ n` → `n`; `k = 0` → longest run; a single character.
- **Skeleton**

  ```python
  count, l, maxf, best = defaultdict(int), 0, 0, 0
  for r, c in enumerate(s):
      count[c] += 1
      maxf = max(maxf, count[c])
      while (r - l + 1) - maxf > k:
          count[s[l]] -= 1; l += 1
      best = max(best, r - l + 1)
  return best
  ```

- **Say out loud** — "Valid means window length minus the majority count fits in k; `maxf` only ever grows and that is safe."

### 560. Subarray Sum Equals K

`Hash Table` · Medium · 11 attempts · 17 passes

- **Pattern** — prefix sum + count map.
- **Invariant** — `seen[p]` = how many prefixes so far have sum `p`; a subarray ending at `r` with sum `k` exists once for every earlier prefix equal to `prefix − k`.
- **Contract** — seed `seen = {0: 1}`: the empty prefix, so a subarray starting at index 0 counts. Query **before** inserting the current prefix, or a zero-length subarray counts itself.
- **The line that sets the complexity** — `ans += seen[prefix - k]` — one hash lookup per element, O(n). Negative numbers are why sliding window fails and why this is the template.
- **Edges** — `k = 0` with zeros in the array (counts every zero run); all negatives; the single-element answer.
- **Skeleton**

  ```python
  seen, prefix, ans = defaultdict(int, {0: 1}), 0, 0
  for x in nums:
      prefix += x
      ans += seen[prefix - k]
      seen[prefix] += 1
  return ans
  ```

- **Say out loud** — "Count earlier prefixes equal to current-minus-k; look up before you insert."

### 525. Contiguous Array

`Hash Table` · Medium · 11 attempts · 18 passes

- **Pattern** — prefix sum with `0 → −1`, *first-occurrence* map.
- **Invariant** — after mapping zeros to −1, a balanced subarray has sum 0, so two prefixes with the **same** running sum bracket one; `first[p]` is the earliest index with sum `p`.
- **Contract** — seed `first = {0: -1}` for a balanced prefix from the start. Store only the *first* index for a sum (`setdefault`), because the longest bracket uses the earliest left end.
- **The line that sets the complexity** — `best = max(best, i - first[prefix])` — one lookup per element, O(n).
- **Edges** — all zeros / all ones → 0; the whole array balanced (`first[0] = -1` makes it `n`); length 1 → 0.
- **Skeleton**

  ```python
  first, prefix, best = {0: -1}, 0, 0
  for i, x in enumerate(nums):
      prefix += 1 if x else -1
      if prefix in first: best = max(best, i - first[prefix])
      else: first[prefix] = i
  return best
  ```

- **Say out loud** — "Zeros become −1; equal prefix sums bracket a balanced run; keep the first index, not the last."

### 1248. Count Number of Nice Subarrays

`Array` · Medium · 7 attempts · 21 passes

- **Pattern** — *exactly k* = *at most k* − *at most k−1*, each an at-most window.
- **Invariant** — in `atMost(k)`, the window `l..r` holds ≤ k odd numbers, and every subarray ending at `r` and starting in `[l, r]` is valid — there are `r − l + 1` of them.
- **Contract** — the subtraction works because "at most" is monotone in `k`. The alternative is a prefix count of odds with a map, exactly LC 560 with `odd_count` as the prefix.
- **The line that sets the complexity** — `ans += r - l + 1` inside the window; `l` moves right at most `n` times, so two passes of O(n).
- **Edges** — `k` larger than the number of odds → 0; all odd; `k = 1`.
- **Skeleton**

  ```python
  def at_most(k):
      l = odd = ans = 0
      for r, x in enumerate(nums):
          odd += x & 1
          while odd > k:
              odd -= nums[l] & 1; l += 1
          ans += r - l + 1
      return ans
  return at_most(k) - at_most(k - 1)
  ```

- **Say out loud** — "Exactly k is at-most k minus at-most k−1; each at-most is one sliding window counting `r − l + 1`."

### 128. Longest Consecutive Sequence

`Sort` · Medium · 17 attempts · 15 passes

- **Pattern** — hash set, start a run only at its left end.
- **Invariant** — a run is walked only from a number `x` with `x − 1` absent; every number is therefore walked at most once across the whole loop.
- **Contract** — the outer loop is over the *set*, not the list, so duplicates cost nothing; the inner `while` extends by `+1` lookups.
- **The line that sets the complexity** — `if x - 1 not in seen:` — without it, the inner walk restarts from every member of a run and the algorithm is O(n²). With it, O(n).
- **Edges** — empty → 0; duplicates (`[1,1,1]` → 1); negative numbers; a single element.
- **Skeleton**

  ```python
  seen, best = set(nums), 0
  for x in seen:
      if x - 1 in seen: continue
      n = x
      while n + 1 in seen: n += 1
      best = max(best, n - x + 1)
  return best
  ```

- **Say out loud** — "Only start counting at a number whose predecessor is missing — that is what makes it linear."

### 53. Maximum Subarray

`DP` · Medium · 13 attempts · 16 passes

- **Pattern** — Kadane: best subarray *ending here*.
- **Invariant** — `cur` = the maximum sum of a subarray ending at `i`; `best` = the maximum over all `i`.
- **Contract** — `cur = max(x, cur + x)`: either extend the previous run or start fresh at `x`. Starting fresh is the right move exactly when `cur < 0`.
- **The line that sets the complexity** — that single `max`, once per element, O(n) O(1). The prefix-sum form (`prefix − min_prefix_so_far`) is the same idea and generalises to LC 560.
- **Edges** — all negative → the largest single element, not 0 (initialise `best = nums[0]`, never 0); a single element.
- **Skeleton**

  ```python
  cur = best = nums[0]
  for x in nums[1:]:
      cur = max(x, cur + x)
      best = max(best, cur)
  return best
  ```

- **Say out loud** — "Best ending here is either just this element or the previous best plus it; never initialise the answer to zero."

## Stack

### 394. Decode String

`Stack` · Medium · 17 attempts · 19 passes

- **Pattern** — one stack of `(prefix, repeat)` frames; the current string and number live outside it.
- **Invariant** — `cur` is the decoded text *inside the innermost open bracket*; the stack holds, for each enclosing bracket, the text before it and the multiplier waiting for it.
- **Contract** — on `[`: push `(cur, num)`, reset both. On `]`: pop `(prev, k)`, `cur = prev + k * cur`. Digits accumulate (`num = num * 10 + d`) because counts are multi-digit.
- **The line that sets the complexity** — `cur = prev + k * cur` — O(output length); the input scan is O(n).
- **Edges** — nested (`3[a2[c]]`), adjacent (`2[a]3[b]`), a multi-digit count (`10[a]`), text outside any bracket.
- **Skeleton**

  ```python
  stack, cur, num = [], "", 0
  for c in s:
      if c.isdigit(): num = num * 10 + int(c)
      elif c == "[": stack.append((cur, num)); cur, num = "", 0
      elif c == "]": prev, k = stack.pop(); cur = prev + k * cur
      else: cur += c
  return cur
  ```

- **Say out loud** — "A bracket saves what came before it and how many times; the close bracket pays it back."

### 739. Daily Temperatures

`Stack` · Medium · 12 attempts · 19 passes

- **Pattern** — monotonic *decreasing* stack of indices, resolved on pop.
- **Invariant** — indices on the stack have temperatures non-increasing bottom to top (equal temperatures stay, because `<` does not pop them), and none has yet seen a warmer day.
- **Contract** — the answer for an index is written when it is **popped**: the popper is the first warmer day. Anything still on the stack at the end stays 0.
- **The line that sets the complexity** — `while stack and T[stack[-1]] < T[i]: j = stack.pop(); ans[j] = i - j` — each index pushed and popped once, O(n).
- **Edges** — strictly decreasing input → all zeros; equal temperatures are *not* warmer (`<`, not `<=`); the last day is always 0.
- **Skeleton**

  ```python
  ans, stack = [0] * len(T), []
  for i, t in enumerate(T):
      while stack and T[stack[-1]] < t:
          j = stack.pop(); ans[j] = i - j
      stack.append(i)
  return ans
  ```

- **Say out loud** — "Keep indices waiting for a warmer day; a warmer day pops and answers them."

### 227. Basic Calculator II

`Stack` · Medium · 12 attempts · 14 passes

- **Pattern** — stack of signed terms with a *delayed* operator.
- **Invariant** — the stack holds the terms of a sum; `*` and `/` are applied to the top term immediately, so precedence is handled by the stack's shape, not by parsing.
- **Contract** — apply the *previous* operator when the next operator (or end of input) arrives: `+` pushes `num`, `-` pushes `-num`, `*` pushes `pop() * num`, `/` pushes `int(pop() / num)` — `int()` truncates toward zero; `//` floors and is wrong for `-7 / 2`.
- **The line that sets the complexity** — one pass, one stack op per token, O(n).
- **Edges** — the final number (flush on `i == n − 1`); spaces; multi-digit numbers; `14-3/2` → 13, `-7/2` → −3.
- **Skeleton**

  ```python
  stack, num, op = [], 0, "+"
  for i, c in enumerate(s):
      if c.isdigit(): num = num * 10 + int(c)
      if c in "+-*/" or i == len(s) - 1:
          if op == "+": stack.append(num)
          elif op == "-": stack.append(-num)
          elif op == "*": stack.append(stack.pop() * num)
          else: stack.append(int(stack.pop() / num))
          op, num = c, 0
  return sum(stack)
  ```

- **Say out loud** — "Push terms; multiply and divide eat the top; the operator is applied one token late."

### 32. Longest Valid Parentheses

`Stack` · Hard · 13 attempts · 10 passes

- **Pattern** — stack of *indices* with a sentinel base.
- **Invariant** — the stack top is the index of the last unmatched character (or the sentinel `−1`); everything after it is a valid run.
- **Contract** — `(` pushes its index. `)` pops; if the stack is then empty, this `)` is the new base and is pushed; otherwise the valid run is `i − stack[-1]`.
- **The line that sets the complexity** — `ans = max(ans, i - stack[-1])` — one push or pop per character, O(n) O(n). The O(1)-space version is two counter passes (left-to-right, then right-to-left).
- **Edges** — `")()())"` → 4; `"()(()"` → 2 (the unmatched `(` splits runs); all `(`.
- **Skeleton**

  ```python
  stack, ans = [-1], 0
  for i, c in enumerate(s):
      if c == "(": stack.append(i)
      else:
          stack.pop()
          if not stack: stack.append(i)
          else: ans = max(ans, i - stack[-1])
  return ans
  ```

- **Say out loud** — "The stack top is the last place a valid run cannot cross; the run length is the distance to it."

### 2289. Steps to Make Array Non-decreasing

`Stack` · Medium · 16 attempts · 12 passes

- **Pattern** — monotonic stack carrying a DP value per element.
- **Invariant** — the stack holds, left to right, elements not yet removed, strictly decreasing; with each, the round in which it gets removed. An element is removed by a strictly greater left neighbour once everything between them is gone.
- **Contract** — for a new `x`: pop every element `≤ x` (they are removed before `x`, by something to *their* left), taking the max of their rounds; if something remains on the stack, `x` will be removed in `max + 1`; if nothing remains, `x` is never removed (`0`).
- **The line that sets the complexity** — `cur = cur + 1 if stack else 0` after the pop loop — each element pushed and popped once, O(n).
- **Edges** — already non-decreasing → 0; strictly decreasing → 1 (all removed in one round); equal neighbours are *not* removed (`≤` pops, so equal elements do not remove each other).
- **Skeleton**

  ```python
  stack, ans = [], 0          # (value, round it is removed)
  for x in nums:
      cur = 0
      while stack and stack[-1][0] <= x:
          cur = max(cur, stack.pop()[1])
      cur = cur + 1 if stack else 0
      stack.append((x, cur))
      ans = max(ans, cur)
  return ans
  ```

- **Say out loud** — "An element dies one round after the last thing it outlives; nothing dies if there is no larger element to its left."

## Binary search

### 300. Longest Increasing Subsequence

`Binary Search` · Medium · 38 attempts · 19 passes

- **Pattern** — patience sorting: `tails[k]` = the smallest possible tail of an increasing subsequence of length `k + 1`.
- **Invariant** — `tails` is strictly increasing, and after processing `x` it holds, for every length, the best (smallest) tail achievable so far. `tails` is **not** a subsequence.
- **Contract** — `bisect_left(tails, x)` is the length of the longest IS that `x` can extend; replace that slot with `x` (a smaller tail for the same length), or append if `x` beats every tail. Strictly increasing means `bisect_left`; `bisect_right` would allow equal elements.
- **The line that sets the complexity** — `i = bisect_left(tails, x)` — O(log n) per element, O(n log n). The O(n²) form is `dp[i] = 1 + max(dp[j] for j < i if nums[j] < nums[i])`; say which one you are writing.
- **Edges** — empty → 0; all equal → 1; strictly increasing → n; a decreasing tail followed by a big value.
- **Skeleton**

  ```python
  tails = []
  for x in nums:
      i = bisect_left(tails, x)
      if i == len(tails): tails.append(x)
      else: tails[i] = x
  return len(tails)
  ```

- **Say out loud** — "`tails[k]` is the smallest tail of any length-k+1 run; each element replaces the first tail not smaller than it."

### 153. Find Minimum in Rotated Sorted Array

`Binary Search` · Medium · 17 attempts · 16 passes

- **Pattern** — binary search on which half is sorted.
- **Invariant** — the minimum is in `[l, r]`. Compare `mid` with `r`: if `nums[mid] > nums[r]`, the rotation point (and the minimum) is strictly right of `mid`; otherwise the minimum is at `mid` or left of it.
- **Contract** — `l = mid + 1` in the first case, `r = mid` (not `mid − 1`) in the second, because `mid` may *be* the minimum. Compare with `r`, not `l`: comparing with `l` is ambiguous when the array is not rotated.
- **The line that sets the complexity** — the halving; O(log n). Distinct elements are what make the `mid` vs `r` comparison decisive — LC 154 (duplicates) needs `r -= 1` on ties.
- **Edges** — not rotated (`[1,2,3]`); rotated by `n − 1`; length 1 and 2.
- **Skeleton**

  ```python
  l, r = 0, len(nums) - 1
  while l < r:
      mid = (l + r) // 2
      if nums[mid] > nums[r]: l = mid + 1
      else: r = mid
  return nums[l]
  ```

- **Say out loud** — "If mid is above the right end, the drop is to the right; otherwise mid could be it, so keep it."

## Linked list

### 143. Reorder List

`Two Pointers` · Medium · 12 attempts · 14 passes

- **Pattern** — three named steps: find the middle, reverse the second half, merge alternately.
- **Invariant** — after step 1, `slow` is the last node of the first half (fast/slow with `fast.next and fast.next.next`); after step 2 the second half is reversed and detached (`slow.next = None`); step 3 interleaves two lists of length `⌈n/2⌉` and `⌊n/2⌋`.
- **Contract** — the cut (`slow.next = None`) is mandatory or the merge cycles. Merge by saving both `next`s before rewiring.
- **The line that sets the complexity** — three linear passes, O(n) O(1). Copying nodes into an array and indexing from both ends is O(n) space and the version to mention, not write.
- **Edges** — 1 or 2 nodes (nothing to do); odd vs even length (the first half is the longer one).
- **Skeleton**

  ```python
  slow, fast = head, head
  while fast.next and fast.next.next: slow, fast = slow.next, fast.next.next
  second, slow.next, prev = slow.next, None, None
  while second: second.next, prev, second = prev, second, second.next
  a, b = head, prev
  while b:
      a.next, b.next, a, b = b, a.next, a.next, b.next
  ```

- **Say out loud** — "Middle, reverse, merge — and cut the list before the merge or it loops."

## Trees and recursion

Every skeleton in this section recurses along the tree's height, so its stack is O(h) —
O(n) on a skewed tree, and LC 104 allows 10⁴ nodes. That is within the LeetCode judge's
raised recursion limit but not CPython's default of 1000; run one of these locally on a
path-shaped tree and it raises `RecursionError`. Say so when naming the space cost, and
know the conversion: the same post-order becomes an explicit stack of
`(node, visited)` pairs without changing the contract.

### 104. Maximum Depth of Binary Tree

`Recursion` · Easy · 16 attempts · 12 passes

- **Pattern** — post-order recursion returning a value.
- **Invariant / contract** — `depth(node)` returns the height of the subtree rooted at `node`, with `depth(None) = 0`. The parent's answer is `1 + max(children)`; nothing else is needed from below.
- **The line that sets the complexity** — every node is visited once, O(n); the stack is O(h), which is O(n) on a degenerate tree — say both.
- **Edges** — empty tree → 0; a single node → 1; a path (height = n).
- **Skeleton**

  ```python
  def depth(node):
      if not node: return 0
      return 1 + max(depth(node.left), depth(node.right))
  ```

- **Say out loud** — "The function returns the height of its own subtree; the base case is the empty tree, not the leaf."

### 110. Balanced Binary Tree

`Recursion` · Easy · 10 attempts · 14 passes

- **Pattern** — post-order recursion with a *sentinel* in the return value.
- **Invariant / contract** — `height(node)` returns the height if the subtree is balanced, or `−1` if any subtree below is not; `−1` propagates up unchanged. This is LC 104 with one extra check, and it turns the O(n²) "call `depth` for every node" into one pass.
- **The line that sets the complexity** — `if l == -1 or r == -1 or abs(l - r) > 1: return -1` — one visit per node, O(n).
- **Edges** — empty → balanced; a subtree balanced at the root but not below (`[1,2,2,3,null,null,3,4,null,null,4]`) — the sentinel is what catches it.
- **Skeleton**

  ```python
  def height(node):
      if not node: return 0
      l, r = height(node.left), height(node.right)
      if l == -1 or r == -1 or abs(l - r) > 1: return -1
      return 1 + max(l, r)
  return height(root) != -1
  ```

- **Say out loud** — "Return the height, or −1 meaning 'already broken below'; the −1 rides up untouched."

### 652. Find Duplicate Subtrees

`Tree` · Medium · 11 attempts · 14 passes

- **Pattern** — post-order serialisation with a count map.
- **Invariant** — `key(node)` is a string that is equal for two nodes iff their subtrees are structurally identical with the same values; post-order with explicit null markers makes it injective.
- **Contract** — add a node to the answer exactly when its key's count reaches **2**, so each duplicate shape is reported once.
- **The line that sets the complexity** — `key = f"{node.val},{left},{right}"` — string concatenation makes each key O(size of subtree), O(n²) in total; assigning each distinct key a small integer id (`ids.setdefault(key, len(ids))`) and building keys from child *ids* brings it to O(n).
- **Edges** — a single node; duplicates that are themselves inside a larger duplicate (report both); `None` must serialise as a marker, not the empty string, or `[1,2]`-shaped trees collide with `[1,null,2]`.
- **Skeleton**

  ```python
  count, ans = defaultdict(int), []
  def key(node):
      if not node: return "#"
      k = f"{node.val},{key(node.left)},{key(node.right)}"
      count[k] += 1
      if count[k] == 2: ans.append(node)
      return k
  key(root); return ans
  ```

- **Say out loud** — "Serialise bottom-up with null markers; the second time a serialisation appears, that node is a duplicate."

### 450. Delete Node in a BST

`BST` · Medium · 9 attempts · 17 passes

- **Pattern** — recursive descent returning the (possibly new) subtree root.
- **Invariant / contract** — `delete(node, key)` returns the root of `node`'s subtree with `key` removed and the BST property intact. Three cases at the match: no left → return right; no right → return left; both → copy the **in-order successor** (min of the right subtree) into `node`, then delete that successor from the right subtree.
- **The line that sets the complexity** — `node.right = delete(node.right, node.val)` after copying the successor — the walk is O(h), and the second descent is inside the right subtree, still O(h).
- **Edges** — key absent (return the tree unchanged); deleting the root; deleting a leaf; the successor is `node.right` itself (no left child).
- **Skeleton**

  ```python
  def delete(node, key):
      if not node: return None
      if key < node.val: node.left = delete(node.left, key)
      elif key > node.val: node.right = delete(node.right, key)
      else:
          if not node.left: return node.right
          if not node.right: return node.left
          s = node.right
          while s.left: s = s.left
          node.val = s.val
          node.right = delete(node.right, s.val)
      return node
  ```

- **Say out loud** — "Reattach whatever the recursive call returns; with two children, borrow the successor's value and delete it from the right."

### 776. Split BST

`BST` · Medium · 13 attempts · 12 passes

- **Pattern** — recursion returning a *pair* of roots.
- **Invariant / contract** — `split(node, V)` returns `(small, large)`: two valid BSTs partitioning `node`'s subtree into values `≤ V` and `> V`. If `node.val ≤ V`, the whole left subtree and `node` are small; only `node.right` needs splitting, and its small part becomes `node.right`. Symmetric otherwise.
- **The line that sets the complexity** — one recursive call per level, `node.right = small_of_right` — O(h).
- **Edges** — empty tree → `(None, None)`; every value ≤ V (right part empty); `V` smaller than the minimum.
- **Skeleton**

  ```python
  def split(node, V):
      if not node: return None, None
      if node.val <= V:
          small, large = split(node.right, V)
          node.right = small
          return node, large
      small, large = split(node.left, V)
      node.left = large
      return small, node
  ```

- **Say out loud** — "The node keeps the side that agrees with it and takes back the matching half of the split of the other side."

## Graphs

### 207. Course Schedule

`BFS` · Medium · 26 attempts · 16 passes

- **Pattern** — Kahn's algorithm (BFS topological sort).
- **Invariant** — the queue holds every node whose in-degree is currently 0; a node is dequeued exactly once, after all its prerequisites.
- **Contract** — `[a, b]` means `b → a` (take `b` before `a`): add `a` to `adj[b]`, increment `indeg[a]`. A cycle is detected by *count*: fewer than `n` nodes processed means some in-degree never reached 0.
- **The line that sets the complexity** — `indeg[nxt] -= 1; if indeg[nxt] == 0: queue.append(nxt)` — each edge relaxed once, O(V + E).
- **Edges** — no prerequisites (trivially true); a self-loop `[0, 0]`; two disconnected components, one cyclic.
- **Skeleton**

  ```python
  adj, indeg = defaultdict(list), [0] * n
  for a, b in prereqs: adj[b].append(a); indeg[a] += 1
  q, done = deque(i for i in range(n) if indeg[i] == 0), 0
  while q:
      u = q.popleft(); done += 1
      for v in adj[u]:
          indeg[v] -= 1
          if indeg[v] == 0: q.append(v)
  return done == n
  ```

- **Say out loud** — "Start from everything with no prerequisites; peel; if the count comes up short, there was a cycle."

### 133. Clone Graph

`BFS` · Medium · 16 attempts · 16 passes

- **Pattern** — traversal with an *old → new* map that doubles as the visited set.
- **Invariant** — `clone[old]` exists iff `old` has been discovered; a node's neighbours are wired using `clone[...]`, creating the copy on first sight.
- **Contract** — create the copy when you *see* a node (as a neighbour), not when you *process* it, or a node seen twice gets two copies. BFS or DFS; the map is the whole idea.
- **The line that sets the complexity** — `if nb not in clone: clone[nb] = Node(nb.val); q.append(nb)` — each node and edge once, O(V + E).
- **Edges** — `None` → `None`; a single node with no neighbours; a cycle (the map is what stops it).
- **Skeleton**

  ```python
  if not node: return None
  clone = {node: Node(node.val)}
  q = deque([node])
  while q:
      u = q.popleft()
      for nb in u.neighbors:
          if nb not in clone: clone[nb] = Node(nb.val); q.append(nb)
          clone[u].neighbors.append(clone[nb])
  return clone[node]
  ```

- **Say out loud** — "One dictionary from old node to new node; it is the visited set and the wiring table at once."

### 261. Graph Valid Tree

`BFS` · Medium · 15 attempts · 13 passes

- **Pattern** — union-find (or one BFS) with the edge-count shortcut.
- **Invariant** — a tree on `n` nodes has exactly `n − 1` edges **and** is connected. With `n − 1` edges, connected ⇔ acyclic, so checking either suffices.
- **Contract** — check `len(edges) == n − 1` first; then union every edge and fail on the first edge whose endpoints already share a root (a cycle), or BFS from 0 and check all `n` were reached.
- **The line that sets the complexity** — `if find(a) == find(b): return False` — near-O(1) per edge with path compression, O(n α(n)).
- **Edges** — `n = 1`, no edges → true; `n − 1` edges but disconnected (impossible without a cycle — which is why the count check makes one test enough); duplicate edges.
- **Skeleton**

  ```python
  if len(edges) != n - 1: return False
  parent = list(range(n))
  def find(x):
      while parent[x] != x: parent[x] = parent[parent[x]]; x = parent[x]
      return x
  for a, b in edges:
      ra, rb = find(a), find(b)
      if ra == rb: return False
      parent[ra] = rb
  return True
  ```

- **Say out loud** — "n−1 edges plus no cycle is a tree; with the edge count checked, one cycle test is enough."

### 323. Number of Connected Components in an Undirected Graph

`Graph` · Medium · 15 attempts · 14 passes

- **Pattern** — union-find with a component counter.
- **Invariant** — `count` = the number of disjoint sets; every successful union (two different roots) reduces it by exactly one.
- **Contract** — start at `n`; union each edge; return `count`. No traversal needed.
- **The line that sets the complexity** — `if ra != rb: parent[ra] = rb; count -= 1` — O(E α(n)) for the unions, O(n + E α(n)) in total with `parent = list(range(n))`. The DFS version is O(V + E) with an adjacency list and a visited set; both are fine, name the one you write.
- **Edges** — no edges → `n`; a self-loop (roots equal, no decrement); duplicate edges.
- **Skeleton**

  ```python
  parent, count = list(range(n)), n
  def find(x):
      while parent[x] != x: parent[x] = parent[parent[x]]; x = parent[x]
      return x
  for a, b in edges:
      ra, rb = find(a), find(b)
      if ra != rb: parent[ra] = rb; count -= 1
  return count
  ```

- **Say out loud** — "Start with n islands; every merge of two different roots removes one."

### 127. Word Ladder

`BFS` · Hard · 11 attempts · 16 passes

- **Pattern** — BFS over words, neighbours generated by *single-letter substitution*.
- **Invariant** — level `d` of the BFS holds every word reachable in exactly `d` steps; the first time `endWord` is dequeued, `d` is the answer. Remove a word from the set when it is *enqueued*, not dequeued, so it is never enqueued twice.
- **Contract** — neighbours by trying 26 letters at each position and checking the set: `O(26 · L²)` per word (each candidate `w[:i] + c + w[i+1:]` is an O(L) string to build and hash) versus `O(N · L)` for comparing against every other word — that is the choice that makes it pass.
- **The line that sets the complexity** — the neighbour loop `for i in range(L): for c in ascii_lowercase:` — O(26 · N · L²) total, since each word is enqueued once and each of its 26·L candidates costs O(L) to build and hash.
- **Edges** — `endWord` not in the list → 0; `beginWord == endWord`; the answer counts *words on the path*, so start at `1`.
- **Skeleton**

  ```python
  words = set(wordList)
  if endWord not in words: return 0
  q, steps = deque([beginWord]), 1
  while q:
      for _ in range(len(q)):
          w = q.popleft()
          if w == endWord: return steps
          for i in range(len(w)):
              for c in ascii_lowercase:
                  nw = w[:i] + c + w[i+1:]
                  if nw in words: words.remove(nw); q.append(nw)
      steps += 1
  return 0
  ```

- **Say out loud** — "BFS by levels; generate neighbours by changing one letter and look them up; remove on enqueue."

### 269. Alien Dictionary

`Graph` · Hard · 10 attempts · 15 passes

- **Pattern** — build a graph from adjacent-word comparisons, then Kahn's algorithm.
- **Invariant** — for each adjacent pair, the **first** differing character gives exactly one edge `w1[i] → w2[i]`; later characters say nothing. Every character that appears is a node, even with no edges.
- **Contract** — two failure modes: a prefix violation (`w1` longer than `w2` with no differing char, e.g. `["abc", "ab"]`) is invalid *before* the sort; a cycle shows as fewer characters emitted than exist. Do not add duplicate edges to the in-degree count, or the count never reaches 0.
- **The line that sets the complexity** — the pairwise scan `for w1, w2 in zip(words, words[1:])` is O(total letters); Kahn's is O(V + E) with `V ≤ 26`.
- **Edges** — a single word; all words identical; characters that never appear in an edge (must still be emitted); the prefix case.
- **Skeleton**

  ```python
  adj = {c: set() for w in words for c in w}
  indeg = {c: 0 for c in adj}
  for w1, w2 in zip(words, words[1:]):
      for a, b in zip(w1, w2):
          if a != b:
              if b not in adj[a]: adj[a].add(b); indeg[b] += 1
              break
      else:
          if len(w1) > len(w2): return ""
  q, out = deque(c for c in adj if indeg[c] == 0), []
  while q:
      c = q.popleft(); out.append(c)
      for d in adj[c]:
          indeg[d] -= 1
          if indeg[d] == 0: q.append(d)
  return "".join(out) if len(out) == len(adj) else ""
  ```

- **Say out loud** — "Adjacent words, first difference, one edge; a longer word before its own prefix is already invalid; a short output means a cycle."

## Backtracking

### 79. Word Search

`Backtracking` · Medium · 15 attempts · 15 passes

- **Pattern** — DFS with in-place marking, restored on the way back.
- **Invariant** — `dfs(r, c, k)` answers "does `word[k:]` start at `(r, c)` using unvisited cells?"; cells on the current path are marked (`#`) so a path cannot reuse them, and unmarked before returning so sibling paths can.
- **Contract** — check bounds and the character match *first*, then mark, recurse in four directions, unmark. Success is `k == len(word)`, checked before any board access.
- **The line that sets the complexity** — the 4-way recursion to depth `L`: O(m · n · 3^L) (three directions after the first, since you never go back). Pruning by character frequency (a letter in `word` that the board lacks) is the standard follow-up.
- **Edges** — a one-character word; a word longer than the number of cells; the word needs a cell twice (must fail); the mark must be a character that cannot appear in `word`.
- **Skeleton**

  ```python
  def dfs(r, c, k):
      if k == len(word): return True
      if not (0 <= r < m and 0 <= c < n) or board[r][c] != word[k]: return False
      board[r][c], ch = "#", board[r][c]
      found = any(dfs(r + dr, c + dc, k + 1) for dr, dc in ((1,0),(-1,0),(0,1),(0,-1)))
      board[r][c] = ch
      return found
  return any(dfs(r, c, 0) for r in range(m) for c in range(n))
  ```

- **Say out loud** — "Mark the cell before recursing and restore it after; the check for a full match comes before the bounds check."

### 139. Word Break

`Backtracking` · Medium · 20 attempts · 22 passes

- **Pattern** — 1-D DP over prefixes (memoised recursion over the same states).
- **Invariant** — `dp[i]` is true iff `s[:i]` can be segmented; `dp[0]` is true (the empty prefix). `dp[i]` is true if some `j < i` has `dp[j]` and `s[j:i]` in the dictionary.
- **Contract** — the memoised DFS `can(i)` = "can `s[i:]` be segmented" is the same table from the other end; the state is the index only, so there are `n + 1` states, which is why naive backtracking (exponential) becomes polynomial the moment you memoise.
- **The line that sets the complexity** — `dp[i] = any(dp[j] and s[j:i] in words for j in range(i))` — O(n²) substring checks, O(n² · L) with hashing; bound `j` by the longest word to tighten it.
- **Edges** — a word used twice (`"aaaa"`, `["a"]`); the dictionary contains the whole string; no segmentation possible.
- **Skeleton**

  ```python
  words, dp = set(wordDict), [True] + [False] * len(s)
  for i in range(1, len(s) + 1):
      dp[i] = any(dp[j] and s[j:i] in words for j in range(i))
  return dp[-1]
  ```

- **Say out loud** — "Prefix i is breakable if some earlier breakable prefix plus one dictionary word reaches it; the state is just the index."

## Dynamic programming

### 322. Coin Change

`DP` · Medium · 20 attempts · 18 passes

- **Pattern** — unbounded knapsack, minimising count, 1-D over the amount.
- **Invariant** — `dp[a]` is the fewest coins making exactly `a`, or `∞`; `dp[0] = 0`. Each `dp[a]` depends only on smaller amounts.
- **Contract** — iterate amounts ascending and coins inside (or coins outside and amounts ascending — both are correct here because order does not matter for a *min*; it matters for *counting*, LC 518). `∞` propagates and is turned into `−1` only at the end.
- **The line that sets the complexity** — `dp[a] = min(dp[a], dp[a - c] + 1)` — O(amount · coins), O(amount). The BFS on amounts is the same complexity and the memoised top-down is the same table.
- **Edges** — `amount = 0` → 0; unreachable → −1 (check `dp[amount] == inf`); a coin larger than the amount.
- **Skeleton**

  ```python
  dp = [0] + [inf] * amount
  for a in range(1, amount + 1):
      for c in coins:
          if c <= a: dp[a] = min(dp[a], dp[a - c] + 1)
  return dp[amount] if dp[amount] != inf else -1
  ```

- **Say out loud** — "Fewest coins for amount a is one plus the fewest for a minus some coin; infinity means impossible."

### 91. Decode Ways

`DP` · Medium · 17 attempts · 23 passes

- **Pattern** — 1-D DP with a two-step lookback (Fibonacci-shaped).
- **Invariant** — `dp[i]` = ways to decode `s[:i]`; `dp[0] = 1`. A one-digit step is allowed iff `s[i−1] != '0'`; a two-digit step iff `10 ≤ int(s[i−2:i]) ≤ 26`.
- **Contract** — the zero is the whole problem: `'0'` alone contributes nothing, `'06'` is not 6, `'10'` and `'20'` are only reachable by the two-digit step. Both conditions are checked independently and summed.
- **The line that sets the complexity** — `dp[i] = (dp[i-1] if s[i-1] != '0' else 0) + (dp[i-2] if 10 <= int(s[i-2:i]) <= 26 else 0)` — O(n), O(1) with two variables.
- **Edges** — leading `'0'` → 0; `"10"` → 1; `"27"` → 1; `"100"` → 0; `"2101"` → 1.
- **Skeleton**

  ```python
  if s[0] == "0": return 0
  dp = [1, 1] + [0] * (len(s) - 1)
  for i in range(2, len(s) + 1):
      if s[i-1] != "0": dp[i] += dp[i-1]
      if 10 <= int(s[i-2:i]) <= 26: dp[i] += dp[i-2]
  return dp[-1]
  ```

- **Say out loud** — "Ways to here is ways from one back if this digit stands alone, plus ways from two back if the pair is 10–26."

### 1143. Longest Common Subsequence

`DP` · Medium · 14 attempts · 20 passes

- **Pattern** — 2-D DP over prefixes.
- **Invariant** — `dp[i][j]` = LCS of `a[:i]` and `b[:j]`; row and column 0 are zero.
- **Contract** — if `a[i−1] == b[j−1]`, extend the diagonal: `dp[i−1][j−1] + 1`. Otherwise drop one character from either side: `max(dp[i−1][j], dp[i][j−1])`. The diagonal is the only place the answer grows.
- **The line that sets the complexity** — the double loop, O(m · n), O(m · n); two rows bring space to O(min(m, n)).
- **Edges** — an empty string → 0; identical strings → their length; no common character → 0.
- **Skeleton**

  ```python
  dp = [[0] * (len(b) + 1) for _ in range(len(a) + 1)]
  for i in range(1, len(a) + 1):
      for j in range(1, len(b) + 1):
          dp[i][j] = dp[i-1][j-1] + 1 if a[i-1] == b[j-1] else max(dp[i-1][j], dp[i][j-1])
  return dp[-1][-1]
  ```

- **Say out loud** — "Match: diagonal plus one. Mismatch: the better of dropping a character from either string."

### 97. Interleaving String

`DP` · Medium · 17 attempts · 12 passes

- **Pattern** — 2-D DP where the *sum of indices* fixes the position in the target.
- **Invariant** — `dp[i][j]` is true iff `s1[:i]` and `s2[:j]` interleave to form `s3[:i+j]`. The next character of `s3` is `s3[i+j]`, and it must come from `s1[i]` or `s2[j]`.
- **Contract** — `dp[i][j] = (dp[i−1][j] and s1[i−1] == s3[i+j−1]) or (dp[i][j−1] and s2[j−1] == s3[i+j−1])`, with `dp[0][0]` true. Check `len(s1) + len(s2) == len(s3)` first or the indexing is wrong.
- **The line that sets the complexity** — that recurrence, O(m · n); one row suffices for O(n) space. The greedy two-pointer is wrong when both strings offer the same next character — that is the whole reason it is DP.
- **Edges** — one string empty; all three empty; the tie case `s1 = "aa", s2 = "ab", s3 = "aaba"`.
- **Skeleton**

  ```python
  m, n = len(s1), len(s2)
  if m + n != len(s3): return False
  dp = [[False] * (n + 1) for _ in range(m + 1)]
  dp[0][0] = True
  for i in range(m + 1):
      for j in range(n + 1):
          if i and dp[i-1][j] and s1[i-1] == s3[i+j-1]: dp[i][j] = True
          if j and dp[i][j-1] and s2[j-1] == s3[i+j-1]: dp[i][j] = True
  return dp[m][n]
  ```

- **Say out loud** — "The position in s3 is i plus j; the cell is true if either string can supply that character from a true neighbour."

### 416. Partition Equal Subset Sum

`DP` · Medium · 12 attempts · 19 passes

- **Pattern** — 0/1 knapsack, boolean, 1-D over the target.
- **Invariant** — `dp[t]` is true iff some subset of the items *seen so far* sums to `t`; `dp[0]` is true.
- **Contract** — the target is `sum / 2` (odd sum → false immediately). For each item, iterate `t` **descending** so each item is used at most once; ascending would let an item be reused (that is LC 518 / unbounded).
- **The line that sets the complexity** — `for t in range(target, x - 1, -1): dp[t] = dp[t] or dp[t - x]` — O(n · sum), O(sum).
- **Edges** — odd total; a single element; an element larger than the target (skip it); `[1, 1]`.
- **Skeleton**

  ```python
  total = sum(nums)
  if total % 2: return False
  target, dp = total // 2, [True] + [False] * (total // 2)
  for x in nums:
      for t in range(target, x - 1, -1):
          dp[t] = dp[t] or dp[t - x]
  return dp[target]
  ```

- **Say out loud** — "Reachable sums, one item at a time, filled from the top down so the item is not counted twice."

### 213. House Robber II

`DP` · Medium · 10 attempts · 15 passes

- **Pattern** — the linear robber, run twice on a circle.
- **Invariant** — in the linear version, `prev2, prev1` are the best takes ending before house `i−1` and `i`; `cur = max(prev1, prev2 + x)`. On a circle, house 0 and house `n−1` cannot both be taken, so the answer is the better of robbing `nums[:-1]` and `nums[1:]`.
- **Contract** — the circle is handled by *excluding one end*, not by special-casing inside the loop.
- **The line that sets the complexity** — two linear passes, O(n) O(1).
- **Edges** — one house (return it — both slices are empty); two houses; three houses (`[2,3,2]` → 3).
- **Skeleton**

  ```python
  def rob(a):
      p2 = p1 = 0
      for x in a: p2, p1 = p1, max(p1, p2 + x)
      return p1
  if len(nums) == 1: return nums[0]
  return max(rob(nums[:-1]), rob(nums[1:]))
  ```

- **Say out loud** — "A circle is two lines: one without the first house, one without the last; take the better."

### 926. Flip String to Monotone Increasing

`DP` · Medium · 12 attempts · 15 passes

- **Pattern** — single-pass DP with two running values.
- **Invariant** — after `i` characters, `ones` = count of `1`s so far, `flips` = the fewest flips making `s[:i]` monotone. The boundary between the zero-block and the one-block is placed by the recurrence.
- **Contract** — a `1` never needs flipping *now* (it can be in the one-block): `ones += 1`. A `0` either gets flipped to join the one-block (`flips + 1`) or every `1` before it gets flipped to keep it in the zero-block (`ones`): `flips = min(flips + 1, ones)`.
- **The line that sets the complexity** — that `min`, once per character, O(n) O(1). The prefix-sum version (`for each split, zeros_left... ones_right`) is the same answer in O(n) space.
- **Edges** — all zeros or all ones → 0; `"010110"` → 2; `"00011000"` → 2.
- **Skeleton**

  ```python
  ones = flips = 0
  for c in s:
      if c == "1": ones += 1
      else: flips = min(flips + 1, ones)
  return flips
  ```

- **Say out loud** — "A one is free for now; a zero either flips itself or forces every earlier one to flip — take the cheaper."

## Strings

### 5. Longest Palindromic Substring

`String` · Medium · 11 attempts · 19 passes

- **Pattern** — expand around every centre; two centres per index.
- **Invariant** — `expand(l, r)` grows while `s[l] == s[r]` and returns the palindrome bounded by the first mismatch; odd palindromes have centre `(i, i)`, even ones `(i, i+1)`.
- **Contract** — run both centres for every `i` and keep the longer; the even centre is the one that gets forgotten.
- **The line that sets the complexity** — the expansion loop: O(n) per centre, `2n` centres, O(n²) O(1). The O(n²) DP table is the same time with O(n²) space; Manacher is O(n) and is the *name* to know, not the code.
- **Edges** — length 1; all the same character (`"aaaa"` → whole string); the answer is even-length (`"cbbd"` → `"bb"`).
- **Skeleton**

  ```python
  def expand(l, r):
      while l >= 0 and r < len(s) and s[l] == s[r]: l -= 1; r += 1
      return s[l+1:r]
  best = ""
  for i in range(len(s)):
      for cand in (expand(i, i), expand(i, i + 1)):
          if len(cand) > len(best): best = cand
  return best
  ```

- **Say out loud** — "Every index is a centre twice — once alone, once with its right neighbour — and each centre expands until it breaks."

### 767. Reorganize String

`Greedy` · Medium · 9 attempts · 18 passes

- **Pattern** — greedy by frequency with a max-heap, holding the last-used letter out for one turn.
- **Invariant** — at each step the letter placed is the most frequent one that is *not* the letter just placed; the previous letter is re-inserted only after the next one is chosen.
- **Contract** — feasibility first: if any count exceeds `(n + 1) // 2`, return `""` — the heap would otherwise get stuck and report it late. Python's heap is a min-heap, so push `(-count, ch)`.
- **The line that sets the complexity** — `heapq.heappop` / `heappush` per character, O(n log 26) = O(n). The O(n) no-heap version fills even indices with the most frequent letter first, then odd indices.
- **Edges** — a single character; `"aab"` → `"aba"`; `"aaab"` → `""`; exactly `(n+1)//2` copies (feasible).
- **Skeleton**

  ```python
  count = Counter(s)
  if max(count.values()) > (len(s) + 1) // 2: return ""
  heap, out, prev = [(-c, ch) for ch, c in count.items()], [], None
  heapify(heap)
  while heap:
      c, ch = heappop(heap)
      out.append(ch)
      if prev: heappush(heap, prev)
      prev = (c + 1, ch) if c + 1 < 0 else None
  return "".join(out)
  ```

- **Say out loud** — "Always place the most frequent letter that is not the one just placed; hold the last letter out for exactly one turn."

### 402. Remove K Digits

`Greedy` · Medium · 11 attempts · 13 passes

- **Pattern** — monotonic *non-decreasing* stack of digits.
- **Invariant** — the stack is the smallest number buildable from the digits seen so far with the removals spent so far; a digit on the stack larger than the incoming digit is removed while removals remain, because a smaller digit earlier beats anything later.
- **Contract** — after the scan, if `k` removals remain, drop them from the **end** (the stack is non-decreasing, so the end holds the largest). Then strip leading zeros; the empty result is `"0"`.
- **The line that sets the complexity** — `while k and stack and stack[-1] > d: stack.pop(); k -= 1` — each digit pushed and popped once, O(n).
- **Edges** — `k == len(num)` → `"0"`; `"10200", k=1` → `"200"` (leading zero stripped); already non-decreasing input (`"12345", k=2` → `"123"`, removed from the end).
- **Skeleton**

  ```python
  stack = []
  for d in num:
      while k and stack and stack[-1] > d: stack.pop(); k -= 1
      stack.append(d)
  stack = stack[:len(stack) - k] if k else stack
  return "".join(stack).lstrip("0") or "0"
  ```

- **Say out loud** — "Pop a bigger digit when a smaller one arrives; leftover removals come off the tail; strip zeros; empty means zero."

## Intervals and sorting

### 253. Meeting Rooms II

`Sort` · Medium · 12 attempts · 12 passes

- **Pattern** — sort by start, min-heap of end times.
- **Invariant** — the heap holds the end times of the meetings currently occupying a room; its size is the number of rooms in use; the top is the room that frees first.
- **Contract** — for each meeting in start order, if the earliest-ending room is free (`heap[0] <= start`), reuse it (pop); then push this meeting's end. The answer is the maximum heap size, which equals its final size when you only pop one per push.
- **The line that sets the complexity** — `if heap and heap[0] <= start: heappop(heap)` — O(n log n) for the sort and the heap. The sweep-line version (sort starts and ends separately, two pointers) is the same complexity.
- **Edges** — a meeting ending exactly when another starts (`<=` frees the room); all overlapping → `n`; one meeting.
- **Skeleton**

  ```python
  intervals.sort()
  heap = []
  for start, end in intervals:
      if heap and heap[0] <= start: heappop(heap)
      heappush(heap, end)
  return len(heap)
  ```

- **Say out loud** — "Rooms are end times in a min-heap; if the earliest one is free, reuse it, otherwise open a new one."

### 435. Non-overlapping Intervals

`Greedy` · Medium · 12 attempts · 16 passes

- **Pattern** — activity selection: sort by **end**, keep the interval that ends earliest.
- **Invariant** — `prev_end` is the end of the last kept interval; among all intervals that overlap a kept one, keeping the one that ends first leaves the most room for the rest.
- **Contract** — count what you *keep*; the answer is `n − kept`. Sorting by start and keeping the shorter of two overlapping intervals also works but is the version that gets argued about; sort by end and the proof is one sentence.
- **The line that sets the complexity** — the sort, O(n log n); the scan is O(n).
- **Edges** — touching intervals `[1,2],[2,3]` do not overlap (`start >= prev_end` keeps); all identical → `n − 1`; one interval → 0.
- **Skeleton**

  ```python
  intervals.sort(key=lambda x: x[1])
  kept, prev_end = 0, -inf
  for start, end in intervals:
      if start >= prev_end: kept += 1; prev_end = end
  return len(intervals) - kept
  ```

- **Say out loud** — "Sort by end; keep everything that starts after the last kept end; remove the rest."
