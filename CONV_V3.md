# Task: bring every listed Java LC solution up to at least 3 versions (V0, V1, V2)

Work ONLY inside the git worktree: /Users/jliu/CS_basics_wt_v3
Paths in your batch file are relative to that worktree root.

## What to add

Each listed file already has V0 (and sometimes V1). Add versions until the file has
**at least three**: `// V0`, `// V1`, `// V2`. Keep the existing V0 as-is unless it is
actually wrong — do not renumber or reorder what is already there.

Format each added version exactly like the existing ones:

```java
    // V1
    // IDEA: <one-line description of the APPROACH, e.g. "top-down memoization">
    /**
     * time = O(...)
     * space = O(...)
     */
    public <ret> <methodName>_1(<args>) {
        ...
    }
```

- Method-name suffixes: V0 = bare name, V1 = `_1`, V2 = `_2`. If the file already uses
  `_1`, your new one is `_2`. Never two methods with the same signature in one class.
- Helper methods for a variant get the same suffix (`dfs_1`, `helper_2`) so they never collide.
- Signature of every version must match the official LeetCode signature (same return type
  and parameter types as V0). Only the method NAME gets the suffix.

## The bar for a "version" — this is the important part

Each version must be a **materially different algorithm**, not a re-spelling. Good axes:

- different complexity (O(n^2) brute force vs O(n) one-pass vs O(1) math formula)
- different paradigm (recursion vs iteration vs DP table vs bit manipulation)
- different data structure (HashMap vs sorting vs heap vs two pointers vs prefix sum)
- top-down memoization vs bottom-up tabulation vs space-optimized rolling array
- brute force written deliberately as the readable reference, with the optimal one elsewhere

BAD (do not do this): renaming variables, swapping a for-loop for a while-loop, converting
a loop to a stream, extracting a helper, or reordering independent statements.

If a problem genuinely only supports two sensible approaches, a legitimate third is an
explicit **brute-force / reference implementation** clearly labelled as such:
`// IDEA: brute force O(n^2) — kept as a readable correctness reference`. That is honest.
Use that escape hatch rather than inventing a fake distinction.

If even that is impossible, leave the file at 2 versions and report it as SKIPPED with the
reason. Do not pad.

## Design / data-structure problems (LRU cache, iterators, stacks, etc.)

The top-level class IS the required API, so you cannot suffix its methods. Instead add
alternate whole implementations as nested static classes with the same public API:

```java
    // V1
    // IDEA: TreeMap-backed alternative to the two-stack V0
    /**
     * time = O(log n) per op, space = O(n)
     */
    public static class <ClassName>V1 {
        ... same constructor + method names as the outer class ...
    }
```

## Verify before you finish — REQUIRED

1. Compile: from /Users/jliu/CS_basics_wt_v3/leetcode_java run
   `javac -nowarn -d /tmp/v3_<BATCH> -sourcepath src/main/java <your files...>`
   Fix every error.

2. Cross-check the versions agree. Write a THROWAWAY harness OUTSIDE the repo
   (use /tmp/, never add files to the worktree) that runs V0, V1 and V2 on the
   LeetCode examples plus a handful of edge cases, and asserts they return the same
   answer. Where the problem allows cheap random inputs, also fuzz a few hundred cases
   comparing the versions against each other. If they disagree, the new version is wrong —
   fix it. Delete the harness when done.

This cross-check is the point of the task: three versions that disagree are worse than one.

Do not modify README.md. Do not create new .java files in the repo. Do not commit.
