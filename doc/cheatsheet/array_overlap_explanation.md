# Explanation of Overlapping Booking Logic in MyCalendar

> **Scope** — **One idea only** — the interval-overlap predicate: closed `[a,b]` vs half-open `[a,b)`, and which one each LC problem needs.
> **See also**: [intervals.md](./intervals.md) — what to do once you can test overlap; [scanning_line.md](./scanning_line.md); [difference_array.md](./difference_array.md).

- (LC 729)

This document explains the logic used in the following code snippet from the `MyCalendar` class:

```java
if (start < date.get(1) && end > date.get(0)) {
    return false;  // There's an overlap
}
```

## LeetCode Problem Lists

- [Design](https://leetcode.com/problem-list/design/)
- [Ordered Set](https://leetcode.com/problem-list/ordered-set/)
- [Segment Tree](https://leetcode.com/problem-list/segment-tree/)

## Purpose
The purpose of this logic is to check whether a new booking overlaps with an existing booking in the calendar. If the new booking overlaps with an existing booking, the function will return `false`, indicating that the booking cannot be made.

## Visual Explanation

Imagine a timeline where each event has a start and an end time. The new event is represented by `start` and `end`, while an existing event is represented by `date.get(0)` (start) and `date.get(1)` (end).

### Cases:

### 1. **No Overlap - New Event is Completely Before the Existing Event**

```text
New:      |-----|   
Existing:          |-----|
```

- **Condition**: `end <= date.get(0)`
- Explanation: The new event ends before the existing event starts, so no overlap.

### 2. **No Overlap - New Event is Completely After the Existing Event**

```text
New:              |-----|
Existing:  |-----|
```

- **Condition**: `start >= date.get(1)`
- Explanation: The new event starts after the existing event ends, so no overlap.

### 3. **Overlap - New Event Partially Overlaps with the Existing Event**

```text
New:       |-------|
Existing:     |------|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: The new event starts before the existing event ends and ends after the existing event starts, causing a partial overlap.

### 4. **Complete Overlap - New Event Starts and Ends Inside the Existing Event**

```text
New:         |---|
Existing:   |-------|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: Both conditions are true, meaning the new event is entirely within the bounds of the existing event.

### 5. **New Event Engulfs the Existing Event (Starts Before and Ends After)**

```text
New:     |-----------|
Existing:   |-----|
```

- **Condition**: `start < date.get(1)` and `end > date.get(0)`
- Explanation: The new event starts before and ends after the existing event, completely overlapping it.

## Breakdown of the Condition:

- **`start < date.get(1)`**: This checks if the new event starts before the existing event ends.
- **`end > date.get(0)`**: This checks if the new event ends after the existing event starts.

If **both conditions are true**, there is an overlap, and the function returns `false` to indicate that the booking cannot be made.

---

## Overlap Predicate Reference

> Scope: this section fixes the **predicate** (is there an overlap? how big? which convention?).
> Full interval **algorithms** live elsewhere — see
> [`intervals.md`](./intervals.md) (merge / greedy scheduling / two-pointer intersection),
> [`scanning_line.md`](./scanning_line.md) (max concurrent, sweep events),
> [`difference_array.md`](./difference_array.md) (bulk range add).

### 0) Concept — the two interval conventions

Every interval problem silently picks one of two conventions. Getting it wrong produces
**off-by-one bugs that only show up on the "touching endpoints" test case**.

| Convention | Written | Endpoint `a1 == b0` means | Typical wording in the problem |
|---|---|---|---|
| **Closed** `[a0, a1]` | both ends **included** | **IS** an overlap (they share the point `a1`) | "inclusive", array indices `left..right`, `\|x - y\| <= t` |
| **Half-open** `[a0, a1)` | end **excluded** | **NOT** an overlap (back-to-back is fine) | "a booking ending at 10 and one starting at 10 do not conflict" |

```text
Closed:      A = [1, 3]        B = [3, 7]
             |-----●           ●-------|         ● shared point 3  ->  OVERLAP

Half-open:   A = [1, 3)        B = [3, 7)
             |-----○           ●-------|         ○ excluded        ->  NO OVERLAP
```

### 0-1) Canonical overlap tests ⭐⭐⭐⭐⭐

Memorize these two lines. Everything else in interval problems is built on top of them.

```text
closed     [a0, a1] ∩ [b0, b1] ≠ ∅   <=>   a0 <= b1 && b0 <= a1
half-open  [a0, a1) ∩ [b0, b1) ≠ ∅   <=>   a0 <  b1 && b0 <  a1
```

**Why this form (and not a case analysis)**: it is the *negation of "disjoint"*.
Two ranges are disjoint iff one finishes before the other starts —
`a1 < b0 || b1 < a0` (closed) / `a1 <= b0 || b1 <= a0` (half-open).
De Morgan that and you get the one-liners above. This is why you never need the
5-case breakdown listed earlier in this doc — the 5 cases collapse into one predicate.

**Properties worth stating out loud in an interview**:
- The predicate is **symmetric** — argument order does not matter.
- It needs **no sorting**. (If the intervals *are* sorted by start, `b0 <= a1` alone suffices.)
- The doc's `MyCalendar` snippet `start < date.get(1) && end > date.get(0)` is exactly the
  **half-open** form, with the two comparisons written in the other order.

### 0-2) Touching endpoints — how to decide which convention the problem wants

Ask these in order; the first one that answers wins:

1. **Does the statement say it?** "`[left, right)`", "end exclusive", "half-open" -> half-open.
   "inclusive", "`0 <= left <= right < n`" -> closed.
2. **Is it a booking / meeting / timeline?** Almost always **half-open** — real calendars let a
   09:00–10:00 meeting sit next to a 10:00–11:00 one. (LC 729 / 731 / 732 / 715 / 218 / 699.)
3. **Is it an array index range or an inclusive value window?** **Closed** — you must count the
   endpoint, so lengths carry a `+ 1`. (LC 303 / 304 / 307 / 220 / 1438 / 497 / 1348.)
4. **Still unsure? Probe the degenerate case.** Feed the interviewer `(a, b)` and `(b, c)`:
   "should these two be treated as conflicting?" One sentence of clarification removes the
   whole class of off-by-one bugs.

**Normalization trick (recommended)**: for integers, `[a, b]` closed is *identical* to
`[a, b + 1)` half-open. Convert once at input, then use the half-open predicate everywhere.
This kills the `+ 1` scattered across your merge/sweep/length code.

### 0-3) Intersection, union, length and gap formulas

```text
intersection      = [ max(a0, b0),  min(a1, b1) ]        # valid iff the overlap test passes
overlap length    = max(0, min(a1, b1) - max(a0, b0))    # continuous / half-open measure
overlap int count = max(0, min(a1, b1) - max(a0, b0) + 1) # closed, counting integer points
union (bounding)  = [ min(a0, b0),  max(a1, b1) ]        # a TRUE union only if they overlap/touch
gap (if disjoint) = max(0, max(a0, b0) - min(a1, b1))
```

**Trap**: `union` is only the real union when the ranges overlap **or touch**. For two disjoint
ranges it returns the bounding box and silently swallows the hole between them — that is exactly
the bug that breaks a merge loop when you forget the overlap guard.

**Trap**: mixing the two length formulas. `[1, 3]` closed contains **3** integers (1, 2, 3);
`[1, 3)` half-open has measure **2**. Pick one convention per problem and stay in it.

---

## 1) General form

### 1-1) Basic OP — overlap predicate toolkit

```java
// java
// IDEA: canonical overlap / intersection / union predicates for 1D ranges.
//       Closed    [a0, a1] -> both ends INCLUDED, so "touching" (a1 == b0) IS an overlap
//       Half-open [a0, a1) -> end EXCLUDED,       so "touching" (a1 == b0) is NOT an overlap
public class RangeOps {

    // time = O(1), space = O(1)
    // closed [a0, a1] vs [b0, b1]  -> touching endpoints count as overlap
    static boolean overlapClosed(int a0, int a1, int b0, int b1) {
        return a0 <= b1 && b0 <= a1;
    }

    // time = O(1), space = O(1)
    // half-open [a0, a1) vs [b0, b1)  -> touching endpoints are NOT an overlap
    static boolean overlapHalfOpen(int a0, int a1, int b0, int b1) {
        return a0 < b1 && b0 < a1;
    }

    // time = O(1), space = O(1)
    // intersection range; meaningful only when the matching overlap test above is true
    static int[] intersection(int a0, int a1, int b0, int b1) {
        return new int[] { Math.max(a0, b0), Math.min(a1, b1) };
    }

    // time = O(1), space = O(1)
    // overlap MEASURE for half-open / continuous ranges (0 when disjoint or only touching)
    static int overlapLength(int a0, int a1, int b0, int b1) {
        return Math.max(0, Math.min(a1, b1) - Math.max(a0, b0));
    }

    // time = O(1), space = O(1)
    // overlap COUNT of integer points for closed ranges (note the "+ 1")
    static int overlapCountClosed(int a0, int a1, int b0, int b1) {
        return Math.max(0, Math.min(a1, b1) - Math.max(a0, b0) + 1);
    }

    // time = O(1), space = O(1)
    // union bounding range; a true union only when the two ranges overlap or touch
    static int[] union(int a0, int a1, int b0, int b1) {
        return new int[] { Math.min(a0, b0), Math.max(a1, b1) };
    }

    // time = O(1), space = O(1)
    // gap between two disjoint ranges (0 when they overlap or touch)
    static int gap(int a0, int a1, int b0, int b1) {
        return Math.max(0, Math.max(a0, b0) - Math.min(a1, b1));
    }
}
```

```python
# python
# IDEA: canonical overlap / intersection / union predicates for 1D ranges.
#       Closed    [a0, a1] -> both ends INCLUDED, so "touching" (a1 == b0) IS an overlap
#       Half-open [a0, a1) -> end EXCLUDED,       so "touching" (a1 == b0) is NOT an overlap

# time = O(1), space = O(1)
def overlap_closed(a0, a1, b0, b1):
    return a0 <= b1 and b0 <= a1

# time = O(1), space = O(1)
def overlap_half_open(a0, a1, b0, b1):
    return a0 < b1 and b0 < a1

# time = O(1), space = O(1)
# meaningful only when the matching overlap test above is true
def intersection(a0, a1, b0, b1):
    return (max(a0, b0), min(a1, b1))

# time = O(1), space = O(1)
# continuous / half-open measure (0 when disjoint or only touching)
def overlap_length(a0, a1, b0, b1):
    return max(0, min(a1, b1) - max(a0, b0))

# time = O(1), space = O(1)
# closed ranges: number of shared integer points (note the "+ 1")
def overlap_count_closed(a0, a1, b0, b1):
    return max(0, min(a1, b1) - max(a0, b0) + 1)

# time = O(1), space = O(1)
# a TRUE union only when the two ranges overlap or touch
def union(a0, a1, b0, b1):
    return (min(a0, b0), max(a1, b1))

# time = O(1), space = O(1)
def gap(a0, a1, b0, b1):
    return max(0, max(a0, b0) - min(a1, b1))
```

### 1-2) 2D — rectangle overlap is just the 1D test twice

**Key Idea**: two axis-aligned rectangles overlap **iff their x-ranges overlap AND their
y-ranges overlap**. There is no separate 2D formula to memorize — reuse the 1D predicate per axis.
The same decomposition extends to any number of dimensions.

```java
// java
// IDEA: 2D overlap = AND of two independent 1D tests, one per axis.
//       rect = [x1, y1, x2, y2] (bottom-left, top-right), half-open on both axes.

// time = O(1), space = O(1)
static boolean rectOverlap(int[] r, int[] s) {
    return r[0] < s[2] && s[0] < r[2]      // x-ranges overlap
        && r[1] < s[3] && s[1] < r[3];     // y-ranges overlap
}

// time = O(1), space = O(1)
// intersection area; 0 when the rectangles only touch along an edge or a corner
static long rectOverlapArea(int[] r, int[] s) {
    long w = Math.max(0, Math.min(r[2], s[2]) - Math.max(r[0], s[0]));
    long h = Math.max(0, Math.min(r[3], s[3]) - Math.max(r[1], s[1]));
    return w * h;
}
```

```python
# python
# IDEA: 2D overlap = AND of two independent 1D tests, one per axis.
#       rect = (x1, y1, x2, y2) (bottom-left, top-right), half-open on both axes.

# time = O(1), space = O(1)
def rect_overlap(r, s):
    return r[0] < s[2] and s[0] < r[2] \
       and r[1] < s[3] and s[1] < r[3]

# time = O(1), space = O(1)
# 0 when the rectangles only touch along an edge or a corner
def rect_overlap_area(r, s):
    w = max(0, min(r[2], s[2]) - max(r[0], s[0]))
    h = max(0, min(r[3], s[3]) - max(r[1], s[1]))
    return w * h
```

**Note (LC 497)**: when a rectangle is a set of **integer lattice points** with the border
included, it is *closed* on both axes, so its point count is `(x2 - x1 + 1) * (y2 - y1 + 1)` —
not the area `(x2 - x1) * (y2 - y1)`. Choosing the wrong one biases the random sampling.

---

## 2) LC Example — which predicate does each problem need?

Grouped by the convention the problem statement forces on you.

### **Half-open `[start, end)` — touching is allowed**

| LC | Title | Predicate you need | Why half-open |
|---|---|---|---|
| 729 | My Calendar I | `s < e2 && s2 < e` | a booking ending at 20 and one starting at 20 do not conflict |
| 731 | My Calendar II | same test, but allow overlap **depth ≤ 2** | triple booking is what's banned, not contact |
| 732 | My Calendar III | max overlap **depth** over all points | k-booking count, back-to-back adds nothing |
| 715 | Range Module | half-open overlap, but **merge when touching** | `[1,3)` and `[3,5)` must fuse into `[1,5)` for `queryRange` to be correct |
| 218 | The Skyline Problem | half-open per building | buildings sharing an x edge are not simultaneously present |
| 699 | Falling Squares | `[left, left + side)` | squares that only touch at an edge do **not** stack |
| 850 | Rectangle Area II | 2D half-open (AND of two axes) | edge contact contributes 0 area |

### **Closed `[start, end]` — endpoints count, lengths carry `+ 1`**

| LC | Title | Predicate you need | Why closed |
|---|---|---|---|
| 1348 | Tweet Counts Per Frequency | `t >= start && t <= end` | chunk boundaries are inclusive on both sides |
| 497 | Random Point in Non-overlapping Rectangles | closed on both axes | points **on the border** are valid picks |
| 303 | Range Sum Query - Immutable | closed index range `[left, right]` | `prefix[right + 1] - prefix[left]` |
| 304 | Range Sum Query 2D - Immutable | closed on both axes | inclusive row/col bounds; 2D prefix with `+ 1` offsets |
| 307 | Range Sum Query - Mutable | closed index range | same inclusive bounds, backed by BIT / segment tree |
| 220 | Contains Duplicate III | value window `[x - t, x + t]`, closed | `\|nums[i] - nums[j]\| <= t` is inclusive on both ends |
| 1438 | Longest Continuous Subarray With Absolute Diff ≤ Limit | window `max - min <= limit`, closed | the limit itself is allowed |

### 2-1) Variation notes on the `MyCalendar` family

The snippet at the top of this doc is the **depth-1** case. The family scales by asking for a
larger allowed overlap depth, and the predicate never changes — only what you do with it:

- **LC 729 (depth 1)** — reject if the new booking overlaps *any* stored booking.
  Brute force `O(n)` per booking with the half-open test; `O(log n)` with an ordered map.
- **LC 731 (depth 2)** — keep a second list of the *intersections* seen so far. Reject only if the
  new booking overlaps an intersection. Note the intersection is exactly
  `[max(s, s2), min(e, e2)]` from the formula above.
- **LC 732 (max depth)** — stop testing pairs; sweep `+1` at `start`, `-1` at `end` and track the
  running maximum. This is the point where the pairwise predicate stops scaling
  (`O(n^2)` -> `O(n log n)`).

See [`scanning_line.md`](./scanning_line.md) for the sweep and
[`intervals.md`](./intervals.md) for the calendar-booking template.

### 2-2) References

- LC 729 My Calendar I / LC 731 My Calendar II / LC 732 My Calendar III — booking depth 1 / 2 / max
- LC 715 Range Module — half-open ranges that must merge on contact
- LC 218 The Skyline Problem / LC 699 Falling Squares — half-open sweep over an ordered set
- LC 850 Rectangle Area II — 2D overlap via per-axis 1D tests
- LC 497 Random Point in Non-overlapping Rectangles — closed lattice, count `(x2-x1+1)*(y2-y1+1)`
- LC 1348 Tweet Counts Per Frequency — closed time chunks
- LC 303 / 304 / 307 Range Sum Query (Immutable / 2D / Mutable) — closed index ranges
- LC 220 Contains Duplicate III / LC 1438 Longest Continuous Subarray With Absolute Diff ≤ Limit — closed value windows

