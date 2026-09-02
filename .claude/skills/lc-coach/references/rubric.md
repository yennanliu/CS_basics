# The Rubric

Four signals, six levels. An interviewer writes a paragraph of evidence per signal and picks a
level; the debrief is that document, not a feeling. Score the same way.

**Evidence, not adjectives.** "Weak communication" is not a score. "Coded for 8 minutes in
silence, then could not say what `dp[i][j]` meant" is.

## The six levels

| Code | Level | The one-line test |
|---|---|---|
| **SH** | Strong Hire | I would spend capital arguing for this person. |
| **H** | Hire | Clear yes, nothing to argue about. |
| **LH** | Lean Hire | Yes, but the packet needs a second positive to survive. |
| **LNH** | Lean No Hire | No, but I could be talked out of it by a strong second packet. |
| **NH** | No Hire | No. |
| **SNH** | Strong No Hire | No, and I would flag it if committee leaned otherwise. |

`LH` and `LNH` are where most real candidates land, and the gap between them is the whole
game. Practice feedback that only offers "hire / no hire" hides exactly the rung you are
standing on.

---

## Communication

| Level | Anchor |
|---|---|
| **SH** | Clarifies input range, types and tie-breaks before touching code. States the approach and its complexity, gets a nod, *then* codes. Narrates decisions, not keystrokes. Takes an L1 hint and runs with it. |
| **H** | Asks the important clarifiers. States the approach first. Goes quiet in hard stretches but resurfaces with where they got to. |
| **LH** | Codes first, explains when asked. Answers are correct but have to be pulled out. One unscoreable silence. |
| **LNH** | Long silences I could not write anything against. Explanation arrives only after two prompts, and describes the code rather than the idea. |
| **NH** | Silent coding. Cannot explain their own line. |
| **SNH** | Argues with a failing test case instead of running it. Talks over the hint. Cannot be interrupted. |

## Problem solving

| Level | Anchor |
|---|---|
| **SH** | Brute force stated and costed within 2 minutes, names the exact repeated work, derives the structure that removes it. Handles the follow-up variant without restarting. |
| **H** | Reaches the optimal approach unaided, or after L1/L2. Can justify why the structure fits. |
| **LH** | Reaches optimal after L3 — I pointed at the repeated work, they took it from there. Or reaches it alone but cannot say why it beats the alternative. |
| **LNH** | Working but sub-optimal, with no route to better when asked. Or optimal only after L4 — I handed over the invariant. |
| **NH** | Pattern-matches to a template that does not fit, and cannot say why it should. No brute force to fall back on. |
| **SNH** | Refuses the hint and keeps pushing a dead approach for the rest of the round. |

## Coding

| Level | Anchor |
|---|---|
| **SH** | Compiles as written. Boundaries right the first time. Names carry meaning. A helper extracted where it earns its place. |
| **H** | One small bug, found by their own trace. Clean structure, readable. |
| **LH** | Two or three bugs, found when prompted. Works, but reads as a first draft — flags where a `return` belongs, deep nesting. |
| **LNH** | Bugs I had to point at. Structure I would send back in review. Unfinished, with the remaining shape unclear. |
| **NH** | Does not run. Mutates while iterating. Half-finished with nothing to show what the rest would be. |
| **SNH** | Cannot write a terminating loop or index an array without help. |

## Verification

| Level | Anchor |
|---|---|
| **SH** | Picks the adversarial input unprompted, traces it as a table, catches their own bug, states tight complexity with variables defined. |
| **H** | Traces the given example carefully. Names the edge cases. Complexity right, derived on request. |
| **LH** | Traces when asked and does it properly. Complexity right but recalled rather than derived. |
| **LNH** | Traces only under instruction, and skips rows. Complexity roughly right, defended vaguely. |
| **NH** | "It works." Complexity guessed, or wrong and defended. Never tries an edge case. |
| **SNH** | Claims a trace they did not do. |

---

## What a hint costs

The ladder is numbered so the cost is legible. Reaching the same code by a different route is
a different packet, and the interviewer writes down which route it was.

| Highest rung used | Ceiling on *problem solving* | The sentence in the packet |
|---|---|---|
| none | `SH` | "Found the bottleneck unaided." |
| L1 — a question about their code | `SH` | "Needed one nudge to re-read their own loop." |
| L2 — a failing input | `H` | "I supplied the counterexample; the diagnosis was theirs." |
| L3 — the category of the fix | `LH` | "Got there after I pointed at the recomputation." |
| L4 — the invariant or template name | `LNH` | "I named the structure." |
| L5 — code | `NH` | "Did not reach a solution." |

Two rules on top:

- The clock is not a hint. Taking 25 minutes unaided beats 12 minutes after L4.
- A hint that is *offered and declined*, then solved alone, scores as unaided. Say so.

## Combining signals into a round

- The round is **not an average**. One signal at `NH` caps the round at `LNH`; anything at
  `SNH` makes the round `SNH` regardless of the rest.
- **Optimal-but-broken loses to sub-optimal-but-correct.** Interviewers hire people whose code
  runs.
- **Unfinished is not automatically negative.** A candidate who states the approach, codes 80%,
  and knows exactly what the last 20% is, is usually `LH` or `H`. A candidate who finishes
  wrong code and claims it works is `NH`.
- **The follow-up separates `H` from `SH`.** The main problem mostly filters; the variant is
  where the ceiling shows.

## What happens to the packet

Worth knowing, because it changes what is worth doing in the room:

- Nobody in committee reads the code. They read the interviewer's notes.
- Ambiguity in the notes resolves **downward**. "Seemed to understand the invariant" is read as
  "did not state the invariant".
- Quotable evidence survives verbatim — a stated invariant, a named edge case, a complexity
  derived out loud. Impressions do not.
- Silence produces no notes at all, and a stretch with no notes is scored as a stretch with
  nothing in it. This is the single cheapest thing to fix.
- Interviewers calibrate against a bar, not against the other candidates that week. A hard
  problem does not lower the bar; it lowers how far you are expected to get.

## Self-score card

Fill this in after every practice problem, before looking at any solution. It takes 90 seconds
and is the highest-yield habit in the whole loop.

```text
LC ___  ____________________          time: __ min (target: 25 medium / 35 hard)

Did I state the brute force and its cost first?             Y / N
Did I state the approach + complexity before coding?        Y / N
Did I code it in one pass without going back to fix logic?  Y / N
Did I find my own bug, or did the test find it?             me / test
Did I state complexity with the variables defined?          Y / N
Highest hint rung I needed (none / L1..L5):                 ____
Which edge case would have failed if not caught?            ____________

Communication ____  Problem solving ____  Coding ____  Verification ____
Round: SH / H / LH / LNH / NH / SNH
The one rung-mover for next time: ____________________________________
```

Two `N`s on the same line across three consecutive problems is a pattern, not an accident —
drill that line specifically, not more problems.
