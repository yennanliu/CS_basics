# Cheatsheet Template

A cheatsheet exists to make one thing faster in a coding interview: **recognise the pattern, recall the template, avoid the trap.** Every rule below serves one of those three. Why each rule exists is in [`cheatsheet-review-2026-11.md`](../cheatsheet-review-2026-11.md).

**Contents** — [Pick a skeleton](#pick-a-skeleton) · [The header](#every-cheatsheet-starts-with-this-header) · [Register the file](#then-register-the-file) · [The pattern card](#the-pattern-card--every-template-section) · [The problem card](#the-problem-card--every-worked-example) · [Recognition table](#the-recognition-table) · [Drill path](#the-drill-path) · [Skeleton A](#skeleton-a--short-doc) · [Skeleton B](#skeleton-b--reference-doc) · [Stars](#mark-the-interview-critical-sections) · [Formatting](#formatting-rules) · [Code](#code-conventions) · [Do not](#do-not)

---

## Pick a skeleton

Two skeletons are in use. **Pick by size, not by topic.**

| | **Skeleton A — short doc** | **Skeleton B — reference doc** |
|---|---|---|
| Use when | < ~800 lines; one technique, few patterns | > ~800 lines; a data structure or a whole pattern family |
| Examples | `patience_sorting.md`, `kadane_algorithm.md`, `n_sum.md`, `iterator.md` | `heap.md`, `dp.md`, `bfs.md`, `graph.md`, `minimum_spanning_tree.md` |
| Shape | `0) Concept` → `1) General form` → `2) LC Example` | `Overview` → `Recognition` → `Templates` → `LC Examples` → `Pattern Selection` → `Drill Path` → `Summary` |

Do **not** mix the two in one file. If a Skeleton A doc grows past ~800 lines, convert it to B rather than bolting B's sections onto the end. Past ~1,500 lines, split the long tail into `X_examples.md` and rare techniques into `X_advanced.md` (see [`cheatsheet-split-plan-2026-08.md`](../cheatsheet-split-plan-2026-08.md)).

Whichever skeleton you pick, **what goes inside a template section is the same**: a [pattern card](#the-pattern-card--every-template-section). A worked example is a [problem card](#the-problem-card--every-worked-example).

---

## Every cheatsheet starts with this header

```markdown
# <Topic Name>

> **Scope** — <one sentence: what this file owns, and what it deliberately does not>.
> **See also**: [other.md](./other.md) — <why you'd go there>; [third.md](./third.md) — <why>.

## LeetCode Problem Lists

<!-- One bullet per relevant LC topic tag, most relevant first.
     Slugs must be real LC topic tags — verify with:
       python3 script/add_lc_problem_lists.py --verify -->
- [Topic Name](https://leetcode.com/problem-list/topic-slug/)
```

Both lines are **mandatory on every file**. `node site/build-site.js` fails without them:

- The **H1** is the page title on the site and the card title on `cheatsheets.html`. Write a name a reader would recognise ("Dynamic Programming (DP)"), not a filename echo ("Dp").
- The **Scope** line keeps two files from silently growing into the same doc, *and* is lifted verbatim into the sheet's card description on the index. One sentence of plain prose, no lead-in.
- The **See also** line is what stops a reader landing on the wrong file of an overlapping family (`tree` vs `tree2` vs `binary_tree` vs `bst`). A parent names every satellite; each satellite points back.

---

## Then register the file

Every `doc/cheatsheet/*.md` needs an entry in [`data/cheatsheet_meta.json`](../../data/cheatsheet_meta.json). The build **throws** if one is missing, so nothing can land in an unsorted bucket:

```json
"my_topic": { "category": "Arrays & Strings", "tier": 4, "title": "Optional override" }
```

| Field | Meaning |
|---|---|
| `category` | Must be one of that file's `categoryOrder` entries. |
| `tier` | FAANG interview weight: `5` must-know, `4` high value, `3` worth knowing, `2` niche. Drives the card's stars, its ordering inside the category, and its emphasis stripe. |
| `title` | Only when the H1 is too long or too literal for a card. |
| `kind` | `"stub"` for a redirect file, `"reference"` for an imported index. Omit for a normal sheet. |

Add it to `startHere` only if it belongs in the beginner reading ladder.

---

## The pattern card — every template section

A **heading** and eight slots, **always in this order**. A reader who knows the order can find "what's the trap?" in any sheet without reading the rest of it. Two slots are *optional*: **Dry run** is written whenever a step is non-obvious, and **Not this if** whenever a look-alike exists. An optional slot is left out only when it would be empty, and the order of what remains does not change.

The heading names the **pattern** and its **anchor problem**: `### Template N: <Pattern name> — LC <n>`. The pattern name is narrower than the sheet's topic. `hash_map.md` is the topic, and *Complement Lookup — LC 1* is one card in it. It is the name you would say out loud once you recognise the problem. The anchor problem is the one the template solves as written; **Drill** lists what to practise after it.

````markdown
### Template 2: Variable-Size Window — LC 3 ⭐⭐⭐⭐⭐

**Recognise** — "longest / shortest **contiguous** subarray or substring such that …", all
values non-negative or the condition is monotone in window size. n ≤ 10⁵ → O(n) expected.

**Key point** — `s[left..right]` is always valid; `right` only grows, `left` only grows,
so each index enters and leaves the window once.

```python
# python
# IDEA: grow right; while the window is invalid, shrink left; record after it is valid again
# time = O(n), space = O(k)   k = distinct items in the window
def longest_valid(s):
    count = {}
    left = 0
    best = 0
    for right, ch in enumerate(s):
        count[ch] = count.get(ch, 0) + 1
        while count[ch] > 1:              # invalid -> shrink until valid
            out = s[left]
            count[out] -= 1
            if count[out] == 0:
                del count[out]            # so count holds only the window's chars: space is O(k)
            left += 1
        best = max(best, right - left + 1)  # valid here, so measure here
    return best
```

```java
// java
// LC 3 - Longest Substring Without Repeating Characters
// IDEA: grow right; while the window has a duplicate, shrink left; record after it is valid
// time = O(n), space = O(k)
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> count = new HashMap<>();
    int left = 0;
    int best = 0;
    for (int right = 0; right < s.length(); right++) {
        char ch = s.charAt(right);
        count.put(ch, count.getOrDefault(ch, 0) + 1);
        while (count.get(ch) > 1) {           // invalid -> shrink until valid
            char out = s.charAt(left);
            count.put(out, count.get(out) - 1);
            if (count.get(out) == 0) {
                count.remove(out);             // only the window's chars stay: O(k)
            }
            left++;
        }
        best = Math.max(best, right - left + 1);  // valid here, so measure here
    }
    return best;
}
```

**Complexity**
- **Time O(n)**: the inner `while` looks nested, but `left` only moves forward, so across the
  whole run it advances at most n times. Each index is added once and removed once.
- **Space O(k)**: `del count[out]` keeps only the window's distinct characters in `count`.

**Dry run** *(optional — required when one step is non-obvious)*

```text
s = "abba"
right=0 'a'  window "a"    best=1
right=1 'b'  window "ab"   best=2
right=2 'b'  dup -> drop 'a','b' -> "b"   best=2
right=3 'a'  window "ba"   best=2
```

**Traps**
- Recording `best` *before* the shrink loop measures an invalid window. LC 3 `"abba"` returns 3.
- `while`, not `if`: one new char can require several shrinks.

**Not this if** — negatives can make a sum shrink as the window grows → prefix sum + hash map
([prefix_sum § Template 2](./prefix_sum.md#template-2-hashmap--prefix-sum-subarray-target-sum--lc-560)).

**Drill** — LC 3 ⭐ · LC 424 · LC 340 · LC 992 *(exactly-k = atMost(k) − atMost(k−1))*
````

| Slot | Rule |
|---|---|
| **Heading** | `### Template N: <Pattern name> — LC <n>`, plus a `⭐` run when it is interview-critical. A pattern, not a topic ("Complement Lookup", not "Hash Table"), and the problem the template solves as written. |
| **Recognise** | The *wording* in a problem statement that should make you reach for this, plus a **constraint cue** where one exists (n ≤ 20 → bitmask, n ≤ 10⁵ → n log n). Never restate the title. "Maximum path sum problems" under *Maximum Path Sum* teaches nothing. |
| **Key point** | One or two lines, **before** the code: the reason this works, as a fact the code keeps true at every step (an invariant). "Use a hash map" is not a key point; "`seen` holds every number before index i, so one lookup answers *has `target − x` appeared?*" is. For DP, the five lines *state / recurrence / base / order / answer* replace it. |
| **Template** | One canonical solution, **Python first, then Java**. Runnable, with real names, no `pass`, no `...` where the logic should be. Comments open with `# IDEA:`, then `# time = … , space = …` on the next line. |
| **Complexity** | One bullet for time and one for space, each naming **the line or loop that sets it**. The code comment states the bound; this slot says why, which is what you say out loud in the interview. Amortised bounds (a nested loop that is still O(n)) are where it matters most. |
| **Dry run** *(optional)* | A ` ```text ` state table on the smallest input that shows the non-obvious step. Required when a reader could not dry-run the template in their head. |
| **Traps** | Each trap names **the input that springs it** and what goes wrong. "Forgetting to handle edge cases" is not a trap; "`[]` returns `None` instead of `0`" is. |
| **Not this if** *(optional)* | The look-alike, and the tell that separates them. It comes after Traps because it is for a reader who already knows the pattern. The model is [`difference_array.md`](./difference_array.md)'s "looks like a diff array, but is not". |
| **Drill** | 3–6 LC numbers, easy → hard, must-know ones starred. Say in a few words what changes from the template. The anchor problem from the heading comes first. |

Exemplars to copy: [`minimum_spanning_tree.md`](./minimum_spanning_tree.md) (why greedy is safe before the code, the same graph traced through both algorithms), [`dp.md`](./dp.md) Template 1b (the DP table before the code, "three traps"), [`prefix_sum_advanced.md`](./prefix_sum_advanced.md) Template 14 (the smallest failing case, "when not to use it").

---

## The problem card — every worked example

A worked example is a lesson about **one** step that is not in the template. Raw solution files already live in `leetcode_python/` and `leetcode_java/`; the sheet links to them rather than pasting a second copy.

````markdown
### Longest Repeating Character Replacement — LC 424

> **Key point** — a window is valid when `len − maxFreq ≤ k`. Keep `maxFreq` as the highest
> count *ever* seen and never lower it: the window may then sit on an invalid stretch, but its
> length never exceeds `best`, and it only grows past `best` once a higher `maxFreq` makes a
> longer valid window possible. So the **length** is always right, even when the window is not.

**Reduces to**: [Template 2](#template-2-variable-size-window--lc-3-) — with a different validity test, and a window that is allowed to be stale.

```python
# python
# IDEA: shrink while (length - max_freq) > k; max_freq is never lowered, so only the length is trusted
# time = O(n), space = O(26)
def characterReplacement(s, k):
    count = {}
    left = 0
    max_freq = 0          # highest count ever seen: may be stale for the current window
    best = 0
    for right, ch in enumerate(s):
        count[ch] = count.get(ch, 0) + 1
        max_freq = max(max_freq, count[ch])
        while (right - left + 1) - max_freq > k:   # too many letters to replace
            count[s[left]] -= 1
            left += 1
        best = max(best, right - left + 1)
    return best
```

**Traps**
- With a stale `max_freq` the window itself can be invalid: `"AABB"`, k = 0, leaves `"AB"` after
  the third character. Return the length, never the window. If the interviewer wants the
  substring, recompute `max(count.values())`. That is correct but costs O(26·n).
- Explaining why the stale value is safe is the interview question; have the sentence ready.

**Similar** — LC 1004 (k flips of 0s) · LC 2024 (two letters)
````

Rules:

- The **first thing under the heading is prose**, the `> **Key point** —` line, never a code fence. It is the same slot as the pattern card's **Key point**: one name for one idea on both cards.
- **`Reduces to`** links the card this is a case of. If no card fits, the sheet is missing a template. Add one rather than an orphan example.
- **V0 is the canonical answer**, the one to write in the interview, not the first one written. A second solution stays only with its reason beside it: different complexity, a distinct trick, or a language idiom.
- Java is welcome as the second block. A Java-only example in a Python-first sheet is a gap to fill, not a style.
- **One home per problem.** Before adding a worked example, `grep -rn "LC <n>\b" doc/cheatsheet/`. If another sheet already works the problem, add a one-row pointer here instead of a second solution.

---

## The recognition table

Every tier ≥ 4 main sheet has one: right after the Overview in Skeleton B, at the end of `0) Concept` in Skeleton A. It is how a reader holding a problem statement finds the card:

```markdown
## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "next greater / previous smaller element" | n ≤ 10⁵ | [Template 1](#…) monotonic stack | window max over a sliding range → [monotonic_queue](./monotonic_queue.md) |
| "sum of min over all subarrays" | | [Template 3](#…) contribution counting | |
```

- The **Signal** column quotes statement wording, not technique names.
- The **Constraint cue** column comes from the constraint ruler in [`complexity_cheatsheet.md`](./complexity_cheatsheet.md). Link to it; do not copy the ruler.
- **Reach for** links to a pattern card in this sheet, or to another sheet's card when the signal routes out. Routing out is a correct answer.

---

## The drill path

Every main sheet ends its teaching content with an ordered practice list: just before `Summary` in Skeleton B, as `3) Drill Path` in Skeleton A:

```markdown
## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 739 · LC 496 · LC 84 | the three templates, once each |
| **Practice** | LC 503 · LC 901 · LC 907 | circular array, online stream, contribution counting |
| **Stretch** | LC 2104 · LC 1944 | two stacks at once; visibility counting |
```

- **Core** = the problems from [`data/l3_core.json`](../../data/l3_core.json) that this sheet owns, plus at most a few others the sheet cannot be learned without. Star them.
- Order is easy → hard **within** a stage.
- No hand-typed totals ("Seventeen problems"). They go stale the first week.

---

## Skeleton A — short doc

```markdown
## 0) Concept

<one paragraph> + **Recognise** / **Key point** lines for the whole technique

### Recognition                   <!-- the recognition table, at the end of 0) Concept -->

## 1) General form

### Template 1: <Template name>   <!-- a pattern card; Template 1 … N, no gaps -->

## 2) LC Example

### <Problem Name> — LC <number>  <!-- a problem card -->

## 3) Drill Path
```

The model short sheet is [`patience_sorting.md`](./patience_sorting.md): three rules → trace → two-line proof → "when it applies" → a one-line discriminator.

---

## Skeleton B — reference doc

```markdown
## Time Complexity          <!-- data-structure docs only; omit for algorithm docs -->

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| <Name>         | O(...)   | O(...)   | O(...)   | O(...)   |

> One-line caveat: what the table hides (amortised? average vs worst? space?).
> This note is the ONLY place complexity is stated in the header — do not repeat
> it as a bullet under Key Properties.

## Overview

<one paragraph>

### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Idea**: ...
- **When to Use**: ...

### References
- [Name](url)

## Recognition                <!-- the recognition table -->

## Templates & Algorithms

### Template Comparison Table

| Template | Use Case | Complexity | When to Use |
|---|---|---|---|

### Template 1: <Name> — LC <number>     <!-- a pattern card each -->

## LC Examples                <!-- problem cards, grouped by the template they reduce to -->

## Pattern Selection Strategy  <!-- ONE decision section per sheet -->

## Drill Path

## Summary & Quick Reference   <!-- ONE summary per sheet; nothing after it except See also -->
```

### Section rules for Skeleton B

- **The canonical template is in the first ~300 lines.** A reader skimming top-down should hit runnable code before the end of the first screenful of Templates. The best section goes first, not last.
- **`LC Examples` is not an append-log.** Before adding a problem there, check whether a template above already solves it *in the same language*. If it does, extend the template's Drill line instead.
- **`LC Examples`, `Problems by Pattern` and any other example section come before `Summary`**, never after it.
- **One** decision section and **one** summary section per sheet. A second flowchart restating the first decision table is deleted, not kept.
- **No `Missing Google Patterns`-style catch-all sections.** New material goes under the pattern it belongs to. If it fits nowhere, it needs its own named `##` section.
- Every `##` heading is unique within the file, and every `###` under a shared parent is unique too. If you need "Summary" twice, qualify both.

---

## Mark the interview-critical sections

A trailing `⭐` run on a heading says how interview-critical that section is. The site strips the run out of the heading text, renders it as a star badge, weights the heading's left rule by it, and pulls 4★/5★ `h4`s into the page's table of contents.

```markdown
### Template 4: 0/1 Knapsack — LC 416 ⭐⭐⭐⭐⭐
### Template 7: Pairwise D&C Merge (k-way) — LC 23 ⭐⭐⭐⭐
```

- Put the run **on the heading**, not in the prose beneath it. Only headings are picked up.
- `⭐⭐⭐⭐⭐` = write it from memory; `⭐⭐⭐⭐` = expect it; `⭐⭐⭐` = a known variant.
- A tier-4 or tier-5 sheet should mark roughly **3–8 sections**. Leave background and reference sections unmarked. If everything is starred, nothing is.
- A star run moves the heading's anchor (a trailing `-`). Move every link with it.

---

## Formatting rules

- **Bold** key terms and the card slot labels: `**Recognise**`, `**Key point**`, `**Complexity**`, `**Traps**`.
- Category headers: `#### **Category Name**`
- Code blocks: **always** tag the language: `python`, `java`, or `text` for ASCII traces, diagrams and program output. Never leave a bare ` ``` `.
- Complexity: inside code, on the line right after `# IDEA:`, as `# time = O(...), space = O(...)`. The order is always the language label, then `IDEA`, then `time`.
- Images: `<p align="center"><img src="../pic/filename.png"></p>`
- Heading levels never skip: an `h2` is followed by `h3`, not `h4`.
- State each LC number **once** per heading: `### Top K Frequent Elements — LC 347`, not `### Top K Frequent Elements (LC 347) — LC 347`.
- Number templates `Template 1 … N` **without gaps**. Worked examples need no number; the heading is `<Problem Name> — LC <n>`. Do not start new `1-1-3)`-style numbering; it cannot be said aloud and breaks when a section moves.
- **No emoji in headings** other than the trailing `⭐` run the site renders. ✅ / ❌ are fine as table cell values.
- English only in an English sheet. A translation goes in the `i18n/zh/` overlay (see CLAUDE.md § Traditional Chinese docs), never inline, where the 100% zh gate cannot see it.

## Code conventions

- Open with `# IDEA: brief description` (Python) or `// IDEA: ...` (Java).
- **Python first, then Java**, for every pair. Provide both for a template; for a worked example, Java is the optional second block.
- Label each block: `# python` / `// java`.
- Include `// LC <number> - Problem Name` above the class/function.
- **One canonical solution per problem.** A second variant needs a reason stated beside it (different complexity, different language idiom, teaches a distinct trick), not just a different spelling of the same loop.
- **Readable over clever**, the same rule as `/lc-python` and `/lc-java`: explicit loops, names that say what they hold, a comment on each non-obvious step.
- **Every snippet runs.** Smoke-test it against the problem's own examples before committing. A template that is wrong gets memorised.
- Java comparators use `Integer.compare(a, b)`, never `a - b` (overflow). Python index arithmetic uses `//`, never `/`.

## Common section patterns

| Pattern | Use |
|---------|-----|
| Recognition Table | After the Overview: signal → constraint cue → card → not-this-if |
| Template Comparison Table | Side-by-side comparison of loop conditions / update rules |
| Family Table | One row per variant: what changed vs the baseline → which template → complexity. Model: [`tree_lca_distance.md`](./tree_lca_distance.md)'s LCA family |
| Failure Table | What broke → symptom → canonical case. Model: [`dp_loop_order.md`](./dp_loop_order.md) |
| Trap → Fix Table | At the end of a card or sheet. Model: [`stack_expression_parsing.md`](./stack_expression_parsing.md) |
| Visual Trace | ASCII walkthrough of algorithm steps on a concrete example (tag it ` ```text `) |
| Decision Matrix | `Minimize vs Maximize`, `Memoization vs Tabulation`, etc. |

---

## Do not

Each of these is in the corpus today. The [Nov 2026 review](../cheatsheet-review-2026-11.md) counts them.

- ❌ `NOTE !!!` comment walls inside code. Write the point as a **Traps** bullet.
- ❌ IPython `In [n]:` transcripts as a template. Use a ` ```python ` block with the output as a `# -> …` comment.
- ❌ A "Universal Template" of pseudocode with undefined names, or a proof whose code is `pass`.
- ❌ Generic filler sections ("Common Mistakes: forgetting edge cases", "Best Practices", "Related Topics") with no concrete input in them.
- ❌ Invented numbers: benchmarks, "usage frequency", hand-typed problem counts.
- ❌ Pasted chat ("Great — here is…"), "fixed by gpt", blog URLs inside code.
- ❌ A worked example that is only a heading over code.
- ❌ The same problem worked in a second sheet. Point to its home instead.
