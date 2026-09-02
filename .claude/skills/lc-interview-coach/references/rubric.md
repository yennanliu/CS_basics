# The Rubric

Four signals, four levels. An interviewer writes one paragraph of evidence per signal and
picks a level; the debrief is that document, not a feeling. Score the same way.

**Evidence, not adjectives.** "Weak communication" is not a score. "Coded for 8 minutes in
silence, then could not say what `dp[i][j]` meant" is.

---

## Communication

| Level | Anchor |
|---|---|
| **Strong Hire** | Clarifies input range, types and tie-breaks before touching code. States the approach and its complexity, gets a nod, *then* codes. Narrates decisions, not keystrokes. Hears a hint at L1 and runs with it. |
| **Hire** | Asks the important clarifiers. States the approach first. Goes quiet in hard stretches but resurfaces with where they got to. |
| **Lean Hire** | Codes first, explains when asked. Answers are correct but have to be pulled out. |
| **No Hire** | Silent coding. Cannot explain their own line. Argues with a failing test case instead of running it. |

## Problem solving

| Level | Anchor |
|---|---|
| **Strong Hire** | Brute force stated and costed within 2 minutes, names the exact repeated work, derives the structure that removes it. Handles a follow-up variant without restarting. |
| **Hire** | Reaches the optimal approach, possibly after one L2/L3 hint. Can justify why the structure fits. |
| **Lean Hire** | Reaches a working but sub-optimal solution, or needs L4 to reach the optimal one. Recognises the pattern only once named. |
| **No Hire** | Pattern-matches to a template that does not fit, and cannot say why it should. No brute force to fall back to. |

## Coding

| Level | Anchor |
|---|---|
| **Strong Hire** | Compiles as written. Boundaries right the first time. Names carry meaning. Helper extracted when it earns its place. |
| **Hire** | One small bug, found by their own trace. Clean structure, readable. |
| **Lean Hire** | Two or three bugs, found only when prompted. Works, but reads as a first draft — flags where a `return` belongs, deep nesting. |
| **No Hire** | Does not run. Mutates while iterating. Half-finished at time, with no structure to show what the rest would be. |

## Verification

| Level | Anchor |
|---|---|
| **Strong Hire** | Picks the adversarial input unprompted, traces it as a table, catches their own bug, states tight complexity with variables defined. |
| **Hire** | Traces the given example carefully. Names the edge cases. Complexity right, derived on request. |
| **Lean Hire** | Traces only when asked. Complexity right but recalled rather than derived. |
| **No Hire** | "It works." Complexity guessed, or wrong and defended. Never tries an edge case. |

---

## Combining

- One `No Hire` on any signal caps the round at **Lean Hire**. Say this out loud when it happens.
- Two signals at `Strong Hire` and none below `Hire` is a **Strong Hire** round.
- **Optimal-but-broken loses to sub-optimal-but-correct.** Interviewers hire people whose code runs.
- Unfinished ≠ No Hire. A candidate who states the approach, codes 80%, and knows exactly
  what the remaining 20% is, is usually a `Hire`. A candidate who finishes wrong code and
  claims it works is not.

## Self-score card

Fill this in after every practice problem, before looking at any solution. It takes 90
seconds and is the single highest-yield habit in the whole loop.

```text
LC ___  ____________________          time: __ min (target: 25 medium / 35 hard)

Did I state the brute force and its cost first?             Y / N
Did I state the approach + complexity before coding?        Y / N
Did I code it in one pass without going back to fix logic?  Y / N
Did I find my own bug, or did the test find it?             me / test
Did I state complexity with the variables defined?          Y / N
Which edge case would have failed if not caught?            ____________

Communication ____  Problem solving ____  Coding ____  Verification ____
The one thing to fix next time: ______________________________________
```

Two `N`s on the same line across three consecutive problems is a pattern, not an accident —
drill that line specifically, not more problems.
