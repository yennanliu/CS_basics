# Talk track — what to actually say

An interviewer scores what they hear, not what you thought. These are the sentences that
carry signal, phase by phase. Say them in your own words — but say *something* in each slot,
because an empty slot reads as an absent skill.

---

## Phase 1 — Clarify (2–4 min)

Restate first. It is free credit and it catches misread problems before they cost 20 minutes.

> "Let me play it back: given `nums` and `k`, return the max of every window of size `k`. Right?"

Then ask only what changes the code:

> "What's the range of `n`? …10⁵ — so I'm aiming for O(n) or O(n log n), not O(n²)."
> "Can the array be empty, or `k` be larger than the array?"
> "Negative numbers? Duplicates?"
> "Ties — any particular one to return, or is any of them fine?"
> "Can I mutate the input, or should I treat it as read-only?"

**Do not ask** what you can decide yourself ("should I use Python?"). Asking wastes a signal slot.

## Phase 2 — Approach (5–10 min)

Always start from the brute force. It is your safety net and your baseline.

> "The brute force is: for each window, scan its `k` elements for the max. That's O(n·k) —
>  correct, but too slow for 10⁵. Let me find what it repeats."

Then name the repetition, not the template:

> "Consecutive windows overlap in `k-1` elements, so I rescan almost the same range every step.
>  I want a structure that keeps the max of a set that grows on the right and shrinks on the left."

Then the structure, the invariant, and the cost — before writing a line:

> "A deque of indices, kept decreasing by value. Invariant: everything in it is inside the
>  window, and the front is the maximum. Each index is pushed once and popped once, so O(n)
>  time and O(k) space. Does that sound reasonable to start coding?"

That last question matters. It converts a monologue into a check-in, and it lets the
interviewer redirect you before you have sunk 15 minutes.

**When you are stuck**, narrate the search — silence scores as nothing, a search scores as
problem solving:

> "Two-pointers doesn't fit because the max doesn't move monotonically. Let me try what a
>  heap gives me: I'd get the max in O(log k), but stale entries are the problem — I'd need
>  lazy deletion. Let me see whether a deque avoids that."

## Phase 3 — Code (10–15 min)

Narrate decisions, never keystrokes. "Now I write a for loop" is noise; this is signal:

> "I'll store indices rather than values, because I need to know when an element leaves the window."
> "This `while` — not an `if` — because more than one element can fall out of order at once."
> "I'll handle the empty input up front so the main loop stays clean."

Say it when you defer something, so it does not read as forgotten:

> "I'll assume valid input for now and add the guard at the end if there's time."

And when you notice a bug mid-write, say so out loud and fix it. Catching your own bug is a
**Strong Hire** verification signal; quietly patching it is worth nothing.

> "Wait — that comparison should be strict. With `>=` I'd drop equal elements that are still
>  in the window. Fixing it."

## Phase 4 — Verify (3–5 min)

Never say "it works". Trace, out loud, from the smallest input that hits the interesting branch.

> "Let me trace `[2,1,5], k = 2` — small, but it does exercise a pop.
>  i=0: push 0, deque `[0]`, window not full yet.
>  i=1: `1 < 2` so it goes behind, deque `[0,1]`, window full → emit `nums[0] = 2`.
>  i=2: `5` pops both, deque `[2]`; index 0 has also left the window → emit `5`.
>  Output `[2,5]`, which matches. The invariant held on every step."

Then volunteer the edges before you are asked:

> "Edge cases: `k = 1` degenerates to the array itself, which works. `k = n` gives a single
>  window and one answer. Empty input returns empty — the guard covers it. Strictly decreasing
>  input is the worst case for deque size, which is where the O(k) space comes from."

Then the complexity, derived rather than recalled:

> "Every index is pushed exactly once and popped at most once, so the inner `while` is O(n)
>  in total, not O(n) per step — O(n) time overall. Space is O(k) for the deque, plus the
>  output, which the problem asks for."

## Phase 5 — Follow-up

Expect it, and answer at the level of the change, not with a rewrite:

> "If it were a stream instead of an array, the deque still works — nothing needs the future."
> "If I needed both max and min, I'd run two deques with mirrored comparisons; same O(n)."
> "If `k` changed at every step, the deque invariant breaks and I'd move to a balanced BST or
>  a heap with lazy deletion — O(n log n)."

---

## Phrases that cost you

| Say this instead | Not this |
|---|---|
| "Let me trace it on `[3,3]`" | "I think it works" |
| "O(n) — each index is pushed and popped once" | "It's linear-ish" |
| "That's a bug, here's the fix" | *silently editing* |
| "The brute force is O(n²); let me improve it" | *staring in silence* |
| "I've seen this shape before — let me re-derive why the deque works" | "Oh, I've done this one" |
| "I'm between two approaches; here's the trade-off" | "Um… maybe… I don't know" |

## The two-minute rule

If you have said nothing for two minutes, you are losing communication points *and* the
interviewer's ability to help you. Say where you are, even if where you are is stuck:

> "I'm stuck on how to evict the element that leaves the window. I know I need the max of a
>  moving range; a heap gives me the max but not the eviction. Can I think out loud for a minute?"

Interviewers hint far more readily when they can see exactly which joint is jammed.
