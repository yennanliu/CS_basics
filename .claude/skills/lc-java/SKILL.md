---
name: lc-java
description: File a LeetCode **Java** solution into this repo the way the existing ones are filed — put it in the package its pattern owns, write the file-level javadoc header (`<number>. <Title>` then difficulty then the statement), mark the solution blocks `// V0` / `// V0-1` / `// V1-1` so the markers match the method names, add the `time =` / `space =` javadoc, compile and run it, write every block as the standard, readable approach with explanatory comments and no clever syntactic sugar, file each other good approach as `// V1`, `// V2`, …, then add the `[Java]` link to the README row the problem already has. Use when asked to "add LC <number> in Java", to file a draft out of `ws/Workspace26.java`, to port an existing Python solution to Java, or to close part of the Java coverage gap. Triggers - "add LC 25 to LinkedList in java", "/lc-java 239 SlideWindow", "file my workspace draft", "port LC 207 to java".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a LeetCode Java solution

Turn an LC number (plus, usually, a draft sitting in `ws/Workspace26.java`) into a
committed-quality Java file **and** the `[Java]` link on its README row, in the shape the
other ~1626 files under `leetcode_java/` already use.

**Invocation**: `/lc-java <LC number> <Package>` — e.g. `/lc-java 25 LinkedList`.
A draft pasted under the command is used as the solution; with no draft, it is usually
already in the workspace scratch file (step 2).

Missing arguments are inferred, not asked about: the package from the technique the solution
actually uses, the class name from the problem title. Ask only if the number itself is
missing or the package is genuinely ambiguous.

**Wrong skill?** This one owns `leetcode_java/` only. A Python solution goes to
[`/lc-python`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-python/SKILL.md).
The two are counterparts and share one README row — see
[directive 4](#prime-directives).

## Prime directives

1. **The marker names the method.** `// V1-2` documents `findMedianSortedArrays_1_2`; `// V0-1`
   documents `<method>_0_1`; bare `// V0` documents the plain LC method name. The two are a
   pair, which is why markers are **never renumbered to close a gap** — renumbering silently
   renames a method. A new block takes the next free marker, and only a genuine collision
   (two blocks answering to one name) is reassigned.
2. **The directory is the `package` line.** `LeetCodeJava/LinkedList/` ⇒
   `package LeetCodeJava.LinkedList;`. A file in the wrong directory compiles nowhere, and
   the mismatch is invisible until a whole-tree `javac`.
3. **The url comment is load-bearing, not decoration.** `script/find_missing_java.py` joins
   Python to Java on three signals — the LC number in the javadoc header, the
   `leetcode.com/problems/<slug>` url, and the title normalised to a class name — because
   LeetCode renames slugs and ~60 files have no url at all. Leave the url out and the file
   is invisible to coverage counting.
4. **The README row already exists — update it, never add a second.** Most problems here are
   Python-first. The Java link goes into that row's Solution cell, after the Python one. A
   duplicate row is the single most expensive mistake this skill can make, because nothing
   in the build catches it.
   **The number can match two rows.** README carries two sets of topic tables — the main
   `## ` ones and the imported `## Newly Added (kamyu104 gap)` / `### ` ones, whose rows
   carry the status `imported`. No number may appear in both any more (`check_readme.py`
   and the site build fail on one), but if a grep ever returns two hits, edit the one
   under a `## ` heading; that is the row `/lc-python` maintains and the one the user reads.
5. **Untested is unfinished.** `javac` it and run it against the javadoc's own examples
   before reporting done.
6. **Readable beats clever.** These files are read back months later by someone who has to
   *say* the solution in an interview. Every block is the **standard, well-known approach**
   written the way a reviewer would approve it in production: explicit loops, named
   intermediate variables, a comment on every step that is not obvious, and no syntactic
   sugar that has to be decoded before the idea is visible (step 4 lists what that means).
   When two spellings run in the same complexity, the plainer one wins.
7. **Every good approach is worth filing.** If the problem has more than one solution that
   passes LC's constraints and is worth knowing — BFS *and* DFS, a heap *and* quickselect,
   memo *and* bottom-up DP — each gets its own block (`// V0`, `// V1`, `// V2`, …), not
   only the one in the draft (step 5).

## The steps

### 1. Settle the problem, and find the row that already exists

```bash
grep -n "^| *0*<number> " README.md          # the existing row: title, difficulty, tags, status
grep -rn "leetcode.com/problems/" leetcode_python/*/*<slug>*.py
```

If README has the row, **it is the source of truth** for the title, the difficulty, the
leetcode url and the complexity columns — they were settled when the Python file was filed,
and this skill does not re-derive or "correct" them.

If there is no row (a Java-first problem), the problem page is the authority on the exact
title, and the row is inserted in ascending LC-number order exactly as
[`/lc-python`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-python/SKILL.md) step 6 describes.

Check whether the file is already there under another spelling before writing a new one —
LC 211 is `add-and-search-word-data-structure-design` in Python and
`design-add-and-search-words-data-structure` on the problem page:

```bash
grep -rln "problems/<slug>\|^ \* <number>\. " leetcode_java/src/main/java/
```

### 2. Pick up the draft from the workspace

Drafts are written in `leetcode_java/src/main/java/ws/Workspace26.java` and filed later —
that is what the `update ws` commits are:

```bash
grep -n "// LC <number>" leetcode_java/src/main/java/ws/Workspace26.java
```

The draft usually carries the user's idea notes above it (`// IDEA: TREE MAP ????`) and
sometimes the bug they hit. **Keep their idea and their notes** — fix the bug, do not
replace the approach with a cleverer one. Keeping the idea is not keeping the spelling: a
dense draft is rewritten to the readable form of step 4, with the same algorithm
underneath. When the draft is filed, remove it from the
workspace in the same change so the scratchpad does not accumulate committed work.

### 3. Read a neighbour before writing

```bash
ls leetcode_java/src/main/java/LeetCodeJava/<Package>/
```

`LeetCodeJava/Array/MedianOfTwoSortedArrays.java` is the reference the whole tree was
normalised onto (commit `db49955`). Open one recent file from the **target package** and
match it; the layout below is the house shape, but the package wins if it has drifted.

Where things go:

| Kind of problem | Package |
|---|---|
| a normal LC problem | `LeetCodeJava/<Pattern>/` — `Array`, `LinkedList`, `DFS`, `DynamicProgramming`, `SlideWindow`, `HashTable`, … (33 of them) |
| a weekly/biweekly contest | `LCWeekly/Weekly<n>.java` — one class per contest, not one per problem |
| a pure algorithm, not an LC problem | `AlgorithmJava/` |
| a shared data structure | `LeetCodeJava/DataStructure/` — and **import** it, never re-declare it |

### 4. Write `leetcode_java/src/main/java/LeetCodeJava/<Package>/<ClassName>.java`

```java
package LeetCodeJava.<Package>;

// https://leetcode.com/problems/<slug>/description/
// https://neetcode.io/problems/<slug>          // only if it is actually on neetcode

import java.util.*;
import LeetCodeJava.DataStructure.ListNode;     // only what is used

/**
 * <number>. <Exact Problem Title>
 * <Difficulty>
 *
 * <the statement, as prose>
 *
 *
 * Example 1:
 *
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 5000
 *
 */
public class <ClassName> {

    // V0
    // IDEA: <ONE LINE — THE TECHNIQUE, IN CAPS LIKE ITS NEIGHBOURS>
    /**
     * time = O(N)
     * space = O(1)
     */
    public <ReturnType> <lcMethodName>(<args>) {
        // edge case: nothing to reverse
        if (head == null || k == 1) {
            return head;
        }

        // dummy sits before the head, so the first group is handled like every other
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            // find the k-th node of this group; fewer than k left -> stop
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) {
                break;
            }
            ...
        }
        return dummy.next;
    }

    /** Walks k steps from node; returns null if the list ends first. */
    private ListNode getKth(ListNode node, int k) {
        ...
    }
}
```

Rules that are not negotiable:

- **`package` first, then the url comment(s), then imports, then the file-level javadoc,
  then the class.** The javadoc sits directly above `public class`.
- The javadoc header is exactly `<number>. <Exact Title>` then the difficulty on its own
  line. Nothing else goes in it: **no leetcode.com page furniture** — no `Solved`,
  `Topics`, `Companies`, `Hint`, premium lock, and no page footer (`Seen this question in a
  real interview before?`, vote counts, `Acceptance Rate`, `Similar Questions`). 1445 files
  had to have that stripped; do not put it back.
- The class name is the problem title in PascalCase (`ReverseNodesInKGroup`), and the file
  name is the class name. It is *not* derived from the method name.
- `// V0` is the canonical solution. If it is not implemented yet, use the placeholder the
  tree already uses — the marker, `// TODO : implement`, and the signature commented out —
  and put the working solution in `// V0-1`.
- Zero, not the letter O: `// V0`, never `// VO`. 14 files had that bug and looked as though
  they had no canonical solution at all.
- `// IDEA: ...` goes under the marker. When a block came from somewhere else, say so in the
  same way the tree already does — `// IDEA: Binary Search Partition  (gpt)`.
- The `time =` / `space =` javadoc block goes directly above the method, in the form
  [`/add-time-space`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/add-time-space/SKILL.md) normalises to — `time = O(N)` and
  `space = O(1)` on their own lines, equals sign, not a colon. Call that skill for a whole
  directory; write the block by hand for a single new file.
- A statement containing `*/` (LC 722 is *about* block comments) is written with the javadoc
  escape `*&#47;`.

**Readability rules** (directive 6) — they apply to every block, `// V0` included:

- **The standard approach first.** `// V0` is the solution a reader would find in a
  textbook or an editorial for the pattern, not a problem-specific trick that only works
  once you have seen it. A trick that genuinely beats the standard bound is filed too, as a
  later version (step 5).
- **No syntactic sugar that hides the logic.** Write it out instead of:
  - `stream()` / `Collectors` pipelines carrying the algorithm — a `for` loop. A stream
    that only converts at the boundary (`list.stream().mapToInt(i -> i).toArray()`) is fine;
  - `Map.merge` / `compute` / `computeIfPresent` with method references — `getOrDefault`
    and `put`, or `computeIfAbsent(key, x -> new ArrayList<>())` for a list-valued map,
    which is idiom enough;
  - nested ternaries, assignment inside a condition (`while ((x = next()) != null)`),
    labelled `break` / `continue`;
  - bit tricks (`x & -x`, `n & (n - 1)`, `>>> 1`) with no comment saying what they compute;
  - a comparator lambda holding real logic — a short `(a, b) -> Integer.compare(a[0], b[0])`
    is fine. **Never** `a[0] - b[0]`: it overflows on large or negative values.
- **Names say what they hold.** `left` / `right`, `windowSum`, `inDegree`, `visited` — not
  `l`, `r`, `s`, `m`, `tmp`, `res2`. Loop indices `i` / `j` and grid coordinates `r` / `c`
  are the exceptions.
- **Comments explain why, not what.** One short comment above each logical step — the
  invariant a loop keeps, why a pointer moves, what a DP cell *means*, why the `+ 1`. Do
  not narrate syntax (`// increment i`). A DP block states the recurrence and base case; a
  graph block states what a node and an edge are.
- **Production-ready.** Handle the edges the constraints allow (null, empty, single
  element, negatives) explicitly and early. `mid = left + (right - left) / 2`; `long` for a
  sum or product that can pass `Integer.MAX_VALUE`; boxed `Integer`s compared with
  `.equals`, never `==`. Declare by interface (`Map<Integer, Integer> count = new
  HashMap<>()`), use `Deque<Integer> stack = new ArrayDeque<>()` rather than `Stack`. No
  `System.out`, no dead code, no commented-out attempts; a helper is a `private` method with
  a one-line javadoc; a magic number becomes a named constant (`private static final int
  MOD = 1_000_000_007;`).

### 5. File every good approach as its own block

When the problem has **several solutions that all pass LC's constraints and are each worth
knowing**, file all of them (directive 7). Before writing, list the approaches the problem
is known for — the editorial's, the standard ones for its pattern — and keep every one that
is a genuinely different idea:

| Marker | Method name | What it holds |
|---|---|---|
| `// V0` | `<method>` | The canonical one — the user's own approach when a draft was given, otherwise the most standard, easiest-to-explain solution. |
| `// V0-1`, `// V0-2` | `<method>_0_1`, `<method>_0_2` | A variation on `V0`'s idea — same technique, a better bound or a cleaner state. |
| `// V1`, `// V2`, … | `<method>_1`, `<method>_2` | A **different approach** that also solves the problem — BFS after DFS, heap after sort, bottom-up DP after memo. Ordered from most standard to least. A block taken from elsewhere says so in its `IDEA` (`(gpt)`, `(editorial)`). |

- Each block has its own `// IDEA:` and its own `time =` / `space =` javadoc, so the
  trade-off between versions is readable from the comments alone. Say in the `IDEA` when a
  version is the one to use in an interview, and when it is not.
- The readability rules of step 4 apply to every block, not only `// V0`.
- A brute force that **times out** on the constraints is not a version — mention it in one
  comment under `V0`'s `IDEA` if it is the natural stepping stone.
- Do **not** pad: two spellings of the same loop are one approach. Most problems end up with
  one to three blocks.
- In a file that already has blocks, a new one takes the **next free marker** — existing
  markers are never renumbered to make room (directive 1).

### 6. Compile and smoke-test before reporting done

The file is not runnable on its own — compile it against the tree's source root:

```bash
cd leetcode_java/src/main/java
javac -d /tmp/lcjava LeetCodeJava/<Package>/<ClassName>.java
```

Then run it against the javadoc's own examples plus the edges (empty, single element,
all-same). A throwaway driver is fine — do **not** add a `main` to the solution file, and do
not leave a test file behind:

```bash
cd leetcode_java/src/main/java
cat > /tmp/Drive.java <<'EOF'
import LeetCodeJava.<Package>.<ClassName>;
public class Drive { public static void main(String[] a) {
    <ClassName> s = new <ClassName>();
    Object v0  = s.<method>(...);         // V0
    Object v01 = s.<method>_0_1(...);     // V0-1, if filed
    Object v1  = s.<method>_1(...);       // V1, if filed
    System.out.println(v0);
    System.out.println(java.util.Objects.equals(v0, v01) && java.util.Objects.equals(v0, v1));  // must print true
} }
EOF
javac -d /tmp/lcjava -cp /tmp/lcjava /tmp/Drive.java && java -cp /tmp/lcjava Drive
```

Every filed block gets one call with the same input — the marker names the method (directive
1), so `// V1` is `<method>_1` — and they must agree: the last line has to print `true`.
`Objects.equals` covers boxed values and collections; an array result needs `Arrays.equals`
(or `Arrays.deepEquals` for a 2-D one) instead. A method that mutates its input (a linked
list reversed in place, an array sorted) gets a **fresh copy** of the input per call, or the
second version runs on the first one's output. When the answer is not unique (any valid
order), compare what the problem pins down rather than raw equality. If a statement example
disagrees, the solution is wrong — say so rather than adjusting the example.

**Known-good baseline**: a whole-tree `javac` reports 8 pre-existing errors, all of them
missing-JUnit errors under `dev/Sorting/`. Everything under `LeetCodeJava/` compiles. If a
run shows 9, the ninth is yours.

### 7. Update the README row

```bash
grep -n "^| *0*<number> " README.md
```

More than one hit means the imported table also carries the problem (directive 4) — print
the heading each candidate sits under and take the `## ` one:

```bash
awk -v L=<hit line> 'NR<=L && /^#{1,3} /{h=$0} NR==L{print h}' README.md   # want "## Stack"
```

Append the Java link to the end of the existing row's **Solution** cell, after the Python
one — `, [Java](<path to the file just written>)`:

```text
| 0015 | [3 Sum](https://leetcode.com/problems/3sum/) | [Python](./leetcode_python/Array/3sum.py), [Java](./leetcode_java/src/main/java/LeetCodeJava/Array/ThreeSum.java) | _O(n^2)_ | _O(1)_ | Medium | ... | ... |
```

- Match the spacing of the rows already there.
- **Do not touch the other cells.** Complexity, difficulty, tags and status belong to the
  problem, not to the language, and the status column in particular is the user's own record
  — never downgrade or "refresh" it.
- Only when there is no row at all: insert one in ascending LC-number order, with `[Java]`
  as the sole solution link.

### 8. Check the coverage join actually sees the file

```bash
python3 script/find_missing_java.py | grep -c "<number>"     # expect 0 — it is no longer missing
```

A file the script still reports as missing has a broken url comment or a header the parser
cannot read (directive 3). Fix it now, not later.

### 9. Report what was assumed

Close with the file, the README line number, the compile and test results, and every
inference: the package chosen and why, complexity columns taken from the existing row rather
than re-derived, a neetcode url deliberately omitted, the workspace draft removed.

## Do not

- ❌ renumber a `// V` marker to close a gap — it renames the method (directive 1)
- ❌ write `// VO` with the letter O
- ❌ paste leetcode.com page furniture into the javadoc header (step 4)
- ❌ add a second README row for a problem that already has one (directive 4)
- ❌ re-declare `ListNode` / `TreeNode` instead of importing `LeetCodeJava.DataStructure`
- ❌ add a `main` method to a solution file, or leave a driver behind
- ❌ hand back code that was not compiled and run (step 6)
- ❌ rewrite the user's approach into your own — but do rewrite its *spelling* when it is
  dense: same algorithm, readable form (directive 6)
- ❌ syntactic sugar that hides the algorithm — stream pipelines, `merge` / `compute` chains,
  nested ternaries, uncommented bit tricks, `a[0] - b[0]` comparators (step 4)
- ❌ single-letter names for anything but an index, or code with no comment on a
  non-obvious step
- ❌ file only one approach when the problem has several good ones, or pad with a second
  spelling of the same loop (step 5)
- ❌ touch `data/progress.txt` — the practice log is the user's own record and gets its own
  commit
- ❌ commit or push unless asked

## Worked example

`/lc-java 25 LinkedList`, with the draft picked up from the workspace:

| Step | What it produced |
|---|---|
| 1 | README row 0025 already there — title *Reverse Nodes in k-Group*, Hard, `_O(n)_` / `_O(1)_`, Python-only |
| 2 | `ws/Workspace26.java` → `// LC 25` draft plus the user's `// IDEA: reverse in iteration` note |
| 3 | read `LinkedList/ReverseLinkedList.java`, matched its shape |
| 4 | wrote `LeetCodeJava/LinkedList/ReverseNodesInKGroup.java` — `package LeetCodeJava.LinkedList`, url comment, `25. Reverse Nodes in k-Group / Hard` javadoc, `import LeetCodeJava.DataStructure.ListNode` |
| 5 | `// V0` the user's iterative pass; `// V0-1` the dummy-node variant — justified: O(1) space instead of O(n/k) recursion frames |
| 6 | `javac` clean; `[1,2,3,4,5] k=2 → [2,1,4,3,5]`, `k=3 → [3,2,1,4,5]`, `k=1 → unchanged`, single node — both variants agreeing |
| 7 | `[Java](...)` appended to row 0025's Solution cell; no other cell touched |
| 8 | `find_missing_java.py` no longer lists 25 |
| 9 | flagged: package chosen as `LinkedList` over `TwoPointer`; complexity columns left as the Python pass set them |
