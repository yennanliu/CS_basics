# Google SWE Final Week Review Strategy

**Role:** Forward Deployed Engineer  
**Format:** 60 mins, 1-2 coding questions  
**Key note from HR:** Coding is 80% of the job

---

## TL;DR

Neither Blind 75 alone nor "read all cheatsheets" alone. Do a targeted combo — **60% active practice, 40% pattern review.**

---

## Why Not Just Blind 75?

- 75 problems in 7 days = ~11/day. Too many — you'll rush and retain nothing.
- Blind 75 has problems you likely already solved.
- New problems this late create anxiety more than confidence.

## Why Not Just Read Cheatsheets?

- Passive reading ≠ ability to produce code under pressure.
- Google expects production-grade code + verbal walkthrough simultaneously — that's a skill you practice, not read.

---

## Priority Data Structures (from JD)

**Tier 1 — cover 100%:** HashMap/HashSet, Array/String, Tree (Binary Tree + BST), Graph, Stack/Queue

**Tier 2 — cover patterns only:** Heap, Trie, DP (just patterns — no obscure DP)

---

## Day-by-Day Plan

| Day | Focus | What to do |
|-----|-------|-----------|
| **Day 1** | Arrays + HashMap | Read `array.md` + `hash_map.md`. Do 3 Blind 75: Two Sum, Contains Duplicate, Product of Array Except Self |
| **Day 2** | Strings + Sliding Window | Read `string.md` + `sliding_window.md`. Do: Longest Substring Without Repeat, Valid Anagram, Minimum Window Substring |
| **Day 3** | Trees (BT + BST) | Read `binary_tree.md` + `bst.md`. Do: Invert Tree, Max Depth, Validate BST, LCA, Serialize/Deserialize |
| **Day 4** | Graph + BFS/DFS | Read `graph.md` + `bfs.md` + `dfs.md`. Do: Number of Islands, Clone Graph, Course Schedule, Word Ladder |
| **Day 5** | Stack + Heap + Trie | Read `stack.md` + `heap.md` + `trie.md`. Do: Valid Parentheses, Min Stack, Top K Frequent Elements, Implement Trie |
| **Day 6** | Mock interviews | Pick 2 unsolved problems. **Full simulation: verbal algo first → code → test.** Time yourself at 30 min each. |
| **Day 7** | Light review only | Read `lc_pattern.md` + `complexity_cheatsheet.md`. No new problems. Sleep well. |

---

## The 5-Step Interview Process (Critical)

HR explicitly said: **ask clear question → share algo → code → prod-grade → manual test**

Practice saying it out loud every time:

1. **Clarify:** "Let me clarify — the input is... edge cases are... can input be empty/negative?"
2. **Algo:** "My approach is [X], which is O(n) time, O(n) space because..."
3. **Code:** "Let me code that up..."
4. **Quality:** Write clean, production-grade code (proper naming, no magic numbers)
5. **Test:** "Let me trace through: input=[2,7,11], expected=[0,1], got=[0,1] ✓"

> Most candidates skip steps 1 and 5. Google interviewers notice.

---

## Blind 75 Priority Problems

~20 high-signal problems — one deep pass beats 75 rushed ones.

| Category | Problems |
|----------|----------|
| Arrays | Two Sum, 3Sum, Container With Most Water |
| Strings | Longest Substring Without Repeating, Valid Anagram, Minimum Window Substring |
| Trees | Invert Tree, Max Depth, LCA, Serialize/Deserialize Binary Tree |
| Graphs | Number of Islands, Course Schedule, Alien Dictionary |
| Heap | Top K Frequent, Merge K Sorted Lists |
| Trie | Implement Trie, Word Search II |
| DP | Climbing Stairs, Coin Change, Longest Common Subsequence (pattern only) |

---

## Cheatsheet Usage Guide

| Cheatsheet | When to read |
|-----------|-------------|
| `lc_pattern.md` | Day 1 morning — orientation |
| `complexity_cheatsheet.md` | Day 7 — final refresh |
| `binary_tree.md`, `graph.md`, `hash_map.md`, `stack.md` | Before each day's topic |
| `dp_pattern.md` | Day 5 — skim patterns only, don't deep-dive |
| `bfs.md`, `dfs.md` | Day 4 together |
| `string.md`, `sliding_window.md` | Day 2 |
| `heap.md`, `trie.md` | Day 5 |

**Skip for now** (low probability for this role):
- `segment_tree.md`
- `binary_indexed_tree.md`
- `scanning_line.md`
- `streaming_algorithms.md`

---

## Reference

- NeetCode Blind 75: https://neetcode.io/practice/practice/blind75
- JD: Forward Deployed Engineer @ Google
