# Math & Logic Puzzles

> **Scope** — The four moves that solve brainteaser-style questions: counting how much information one test buys, balancing the worst case, finding the invariant, and using linearity of expectation. It teaches the moves, not a riddle list.
> **See also**: [combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — the counting formulas and the pigeonhole principle; [dp_advanced.md](./dp_advanced.md) — egg drop solved as a general DP (LC 887); [binary_search_on_answer.md](./binary_search_on_answer.md) — the same "minimise the worst case" idea on a monotone predicate.

## LeetCode Problem Lists

- [Math](https://leetcode.com/problem-list/math/)
- [Game Theory](https://leetcode.com/problem-list/game-theory/)
- [Probability and Statistics](https://leetcode.com/problem-list/probability-and-statistics/)

## 0) Concept

A puzzle question is not testing whether you have heard the puzzle. It is testing whether
you can turn a woolly scenario into something countable and then optimise it out loud. Four
moves cover almost every one of them:

| Move | The question it answers | Signal in the wording |
|---|---|---|
| **Count the information** (§1-1) | How few tests *could* possibly work? | "how many weighings / tests / questions" |
| **Balance the worst case** (§1-2) | Where do I put the first probe? | "guarantee", "in the worst case", "minimise the maximum" |
| **Find the invariant** (§1-3) | What never changes, whatever I do? | "is it always possible", "who wins", "prove you cannot" |
| **Linearity of expectation** (§1-4) | What happens on average? | "expected", "on average", "what is the ratio" |

Start with the information count even when you are not asked for it: it gives a lower
bound, and if your construction matches the bound you are provably optimal — the single
strongest thing you can say in one of these rounds.

## 1) General form

### 1-1) Count the information a test buys ⭐⭐⭐⭐⭐

A test with `k` distinguishable outcomes, repeated `t` times, separates at most `k^t`
cases. To identify one of `N` possibilities you therefore need

```text
k^t >= N        ->        t >= log_k(N)
```

- A yes/no test: `k = 2`, and `log2(1000) = 9.97`, so 1,000 cases need at least 10 tests.
- A balance scale: `k = 3` (left heavier, right heavier, level). Naming which of 12 balls is
  the odd one **and** whether it is heavy or light is 24 cases, and `3^3 = 27 >= 24`, so
  three weighings can just do it — which is why the puzzle says 12 and never 14 (28 cases,
  more than 27, provably impossible).
- A comparison sort: `k = 2`, `N = n!` possible orderings, so `t >= log2(n!) = O(n log n)`.
  The famous sorting lower bound *is* this same count.

**The construction that hits the bound: each test becomes one bit of the answer's id.**
CtCI 6.10 — 1,000 bottles, one of them poisoned, 10 test strips, results take a week, and
you get one round of testing. `2^10 = 1024 >= 1000`, so 10 strips are exactly enough:
number the bottles, let strip `j` taste every bottle whose id has bit `j` set, and read the
pattern of positives back as a binary number.

```python
# python
# CtCI 6.10 - 1000 bottles, 1 poisoned, 10 test strips, ONE round of testing
# IDEA: strip j tastes every bottle whose id has bit j set, so the vector of
#       positive strips IS the poisoned bottle's id, written in binary
# time = O(n_bottles * n_strips) drops, space = O(n_strips)
def find_poisoned(n_bottles, n_strips, is_poisoned):
    assert (1 << n_strips) >= n_bottles          # 2^10 = 1024 >= 1000 -> solvable
    positive = [False] * n_strips

    for bottle in range(n_bottles):
        for j in range(n_strips):
            if (bottle >> j) & 1:                # bit j set -> a drop goes on strip j
                positive[j] |= is_poisoned(bottle)

    poisoned_id = 0
    for j in range(n_strips):
        if positive[j]:
            poisoned_id |= 1 << j                # read the pattern back as an id
    return poisoned_id
```

**More rounds multiply the outcomes, they do not add.** If the strips can be reused over
`r` rounds, each strip reports *which round it first turned positive*, or never — that is
`r + 1` outcomes, not 2. So `s` strips over `r` rounds separate `(r + 1)^s` bottles, and
the encoding changes from binary to base `r + 1`: digit `j` of the bottle's id in base
`r + 1` says which round strip `j` should taste it. Ten strips over four rounds reach
`5^10 = 9.7` million bottles.

### 1-2) Balance the worst case ⭐⭐⭐⭐⭐

When a wrong probe forces you into a linear scan, the cost of a plan is
`probes spent so far + length of the scan you fall back to`. The best plan makes that sum
**the same for every outcome** — if one branch is cheaper than another, you can afford to
be greedier there, which means you were not optimal.

CtCI 6.5 — two eggs, 100 floors, find the floor where an egg starts to break, minimising
the worst-case number of drops. Egg 1 jumps, and when it breaks, egg 2 must scan the gap
one floor at a time. Drop egg 1 at floor `x`, then `x + (x-1)`, then `x + (x-1) + (x-2)`:
after the `i`-th jump you have spent `i` drops and the gap left to scan is `x - i`, so the
total is `x` no matter where it breaks. Cover all the floors and you get

```text
x + (x-1) + ... + 1 = x(x+1)/2 >= 100    ->    x = 14   (14*15/2 = 105)
```

```python
# python
# CtCI 6.5 - 2 eggs, n floors: find the breaking floor in at most x drops,
# where x is the smallest integer with x(x+1)/2 >= n  (n = 100 -> x = 14)
# IDEA: shrink the jump by 1 after each survived drop, so (drops used + worst
#       remaining scan) stays constant at x for every outcome
# time = O(sqrt(n)) drops, space = O(1)
def find_breaking_floor(floors, breaks_at):      # breaks_at(f) -> True if the egg breaks
    step = 1
    while step * (step + 1) // 2 < floors:       # derive x from the building, not from 100
        step += 1

    floor, prev, broke = step, 0, False
    while step > 0 and floor <= floors:          # egg 1: jumps that shrink by 1
        if breaks_at(floor):
            broke = True
            break
        step -= 1
        prev, floor = floor, floor + step

    top = floor - 1 if broke else min(floor, floors)   # never re-test the floor that broke
    for f in range(prev + 1, top + 1):                 # egg 2: scan the gap, bottom-up
        if breaks_at(f):
            return f
    return floor if broke else -1                # nothing lower broke, so it is `floor`
```

The same balancing act, in other clothes:

- **Binary search** is this with an unlimited supply of eggs — every probe halves the
  space, so both branches cost the same.
- **k eggs, n floors** is the DP in [dp_advanced.md](./dp_advanced.md) (LC 887): with `d`
  drops and `k` eggs you can clear `f(d, k) = f(d-1, k-1) + f(d-1, k) + 1` floors. The
  two-egg closed form above is that recurrence's special case.
- **"Minimise the maximum X"** on a monotone predicate is
  [binary_search_on_answer.md](./binary_search_on_answer.md) — guess the answer, verify it.

### 1-3) Find the invariant ⭐⭐⭐⭐

"Can this always be done?" and "who wins?" are almost never solved by search. Look for a
quantity that no legal move can change, then show the goal state has a different value.

**100 lockers.** All 100 lockers start closed; pass `k` toggles every `k`-th locker. Which
are open at the end? Locker `n` is toggled once per divisor of `n`, so it ends open exactly
when `n` has an **odd** number of divisors. Divisors pair up as `d` with `n/d`, and the
only unpaired case is `d == n/d` — so the open lockers are the perfect squares.

```python
# python
# The 100 lockers puzzle: pass k toggles every k-th locker; which end up open?
# IDEA: locker n is toggled once per divisor of n; divisors pair up as (d, n/d),
#       so the count is odd only when d == n/d — that is, n is a perfect square
# time = O(sqrt(n)), space = O(sqrt(n))
def open_lockers(n):
    out, k = [], 1
    while k * k <= n:
        out.append(k * k)
        k += 1
    return out                                   # n=100 -> [1, 4, 9, ..., 100]
```

**Parity as a colouring.** Remove two opposite corners of a chessboard — can 31 dominoes
tile the remaining 62 squares? No: every domino covers one black and one white square, so
any tiling needs equal counts, and the two corners you removed were the same colour. The
invariant is "black minus white", which starts at ±2 and no move can change.

**On LeetCode the invariant is usually a modulus:**

| # | Problem | The invariant |
|---|---|---|
| 292 | Nim Game | You lose exactly when `n % 4 == 0` — the opponent can restore that state |
| 1025 | Divisor Game | Parity of `n`; every move flips it and 1 is a loss |
| 877 | Stone Game | Fixed parity of the pile count hands player 1 a whole colour class |
| 794 | Valid Tic-Tac-Toe State | Counts of X and O must satisfy `0 <= x - o <= 1` |
| 1041 | Robot Bounded In Circle | A net rotation ≠ 0 is invariant under repetition, so the path is bounded |

### 1-4) Linearity of expectation ⭐⭐⭐⭐

Expectations add even when the events are dependent, which lets you avoid the joint
distribution entirely.

**CtCI 6.7 — the apocalypse.** Every family keeps having children until a girl is born,
then stops. What is the boy-to-girl ratio? Tempting to sum a series; unnecessary. Every
*birth* is an independent 50/50 coin, and no stopping rule changes the coin. The stopping
rule decides how many births happen, not what any of them is, so the ratio is **1:1**.
(Per family: exactly 1 girl, and an expected `1/0.5 - 1 = 1` boy.)

**Expected number of tries.** If each attempt succeeds independently with probability `p`,
the expected number of attempts is `1/p`. That single fact prices every rejection-sampling
loop — including `rand7()` from `rand5()`, where 21 of 25 draws are usable, so the expected
cost is `25/21 ~= 1.19` rounds. The construction lives in
[combinatorics_math_patterns.md](./combinatorics_math_patterns.md) (LC 470).

**LC bridges:** LC 470 (Implement Rand10 Using Rand7), LC 688 (Knight Probability in
Chessboard) and LC 837 (New 21 Game) are all "expectation over states" — a DP where the
value of a state is the average of its successors.

### 1-5) Pigeonhole ⭐⭐⭐

`n + 1` items in `n` boxes force a box with two. In interviews it shows up as *a state
space that must repeat*: iterate a deterministic map over a finite set and a cycle is
guaranteed within `|states| + 1` steps. Worked examples — LC 957 and the cycle-detection
family — are in
[combinatorics_math_patterns.md](./combinatorics_math_patterns.md).

## 2) LC Example

### 2-1) Nim Game — LC 292 ⭐⭐⭐

The cleanest invariant question on LeetCode, and worth being able to derive rather than
recall: 1–3 stones may be taken and whoever takes the last one wins.

```python
# python
# LC 292 - Nim Game
# IDEA: multiples of 4 are the losing states. From any other pile you can move TO a
#       multiple of 4; from a multiple of 4 every move (1..3) leaves one, so the
#       opponent hands it straight back.
# time = O(1), space = O(1)
class Solution(object):
    def canWinNim(self, n):
        return n % 4 != 0
```

```java
// java
// LC 292 - Nim Game
// IDEA: n % 4 == 0 is invariant under "opponent restores it" — those states lose
// time = O(1), space = O(1)
class Solution {
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }
}
```

The derivation is the part to say out loud: hand-check `n = 1, 2, 3` (wins), `n = 4`
(loss), `n = 5, 6, 7` (win by moving to 4), and the pattern is forced from there.

## 3) How to attack a puzzle you have not seen

1. **Restate it as a search over a finite set.** How many possible answers are there? That
   number is `N` in §1-1.
2. **Count what one probe buys.** `k` outcomes, so you need at least `log_k(N)` probes.
   Say the bound before you have a construction — it frames everything after it.
3. **Try to hit the bound.** Usually by making each probe answer one digit of the answer's
   id, in base `k`.
4. **If the answer must be guaranteed, balance the branches** (§1-2). If it only has to be
   good on average, compute the expectation (§1-4).
5. **If the question smells impossible, look for the invariant** (§1-3) — a parity, a
   modulus, a colouring, a conserved sum.
6. **Sanity-check with the smallest case.** `n = 1, 2, 3` catches a wrong pattern faster
   than any amount of algebra.

## 4) Common Pitfalls

- **Chasing the clever trick before the bound.** The information count takes ten seconds
  and tells you whether the trick you are hunting can exist at all.
- **Confusing average with worst case.** "13 drops on average" does not answer "guarantee
  the fewest drops". Which one the question wants is usually one word in the prompt.
- **Assuming a stopping rule changes a distribution.** It changes how many samples you
  take, not what each sample is — the apocalypse trap.
- **Adding outcomes instead of multiplying them.** Two rounds of a 3-outcome test give
  `3^2 = 9` cases, not 6.
- **Reasoning about "an odd number" without checking the pairing.** The lockers answer is
  perfect squares precisely because divisors pair up; state the pairing, not the result.
- **Treating a puzzle as trivia.** Even when you know the answer, narrate the derivation —
  the score comes from the reasoning, and a memorised number reads exactly like a memorised
  number.

## 5) Summary

| Puzzle shape | Move | Worked example above |
|---|---|---|
| "How few tests?" | `k^t >= N`, then encode the id in base `k` | Poison bottles (§1-1) |
| "Guarantee the fewest probes" | Equalise probes-spent + scan-left | Egg drop (§1-2) |
| "Is it always possible / who wins" | Invariant: parity, modulus, colouring | Lockers, Nim (§1-3, §2-1) |
| "On average / what ratio" | Linearity of expectation, `E[tries] = 1/p` | Apocalypse (§1-4) |
| "Must something repeat" | Pigeonhole over a finite state space | §1-5 |
