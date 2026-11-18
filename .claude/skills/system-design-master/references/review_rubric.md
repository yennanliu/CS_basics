# Review rubric

Used by `review` (scoring a design someone wrote) and `mock` (scoring the person who just
designed one). Score each signal **strong / mixed / weak** and quote the evidence from their
design or their words — a verdict without evidence is an opinion.

## The six signals

| Signal | Strong | Weak |
|---|---|---|
| **Requirements** | asks about scale, latency or freshness, consumers and the correctness bar before drawing; states assumptions | draws in the first minute; never asks who reads the output |
| **Estimation** | numbers turn into partition / shard / cache / cost decisions | numbers computed, then never used |
| **Design** | each component traced to a requirement; the simplest design that meets them; the key choice is explained | a tool for every box; no data model; no key choice |
| **Crux and depth** | names what the problem is really testing and goes deep there | spreads time evenly; stays at box level |
| **Trade-offs and failure** | names the option not taken and what flips it; failure modes raised unprompted with an answer | one option; failures only when asked |
| **Communication** | drives, checks in, manages time, summarises | waits for each next step |

## What the level changes

| Level | Expected |
|---|---|
| New grad / L3 | a working end-to-end design with sensible components; reasonable answers when pushed on scale and failure |
| L4 / mid | drives the round; a correct data model and key choice; at least one deep dive done well |
| L5 / senior | finds the crux unprompted; trade-offs with flip conditions; operations, cost and evolution covered; correct on the correctness traps below |

## Correctness traps

Claims that sound right and are not. Each was caught by a reviewer on a real design in this
repo; flag every one a design makes.

| The claim | Why it is wrong | The correct statement |
|---|---|---|
| "At-least-once into an idempotent sink is exactly-once" | true for re-emitting a result; false for a duplicate **input** already added into a count | aggregates also need dedupe on `event_id` before the increment |
| "Checkpointed state handles duplicates" | a restore rolls back the job's own replay; a producer retry is a new record at a new offset | checkpoints for recovery replays, `event_id` dedupe for producer retries — both |
| "Top-K = union of each window's top-N, re-scored" | an item steady across windows can lose every window and win overall | candidates from a summary with a coverage guarantee (mergeable Misra-Gries / Space-Saving), exact recount for the official list |
| "Dedupe with a 24 h TTL" (with clients that retry for days) | a retry after the TTL is counted again | a stated retry contract the TTL covers, plus an upsert on the event id as a second guard |
| "GDPR delete = delete the rows" | snapshots, derived tables, serving stores, backups still hold it, and a replay writes it back | delete everywhere it landed, expire snapshots, and a deletion filter every replay and backfill checks |
| "Write to the DB and publish to Kafka" | a crash between the two leaves them disagreeing forever | CDC from the DB log, or a transactional outbox |
| "Daily uniques sum to weekly uniques" | distinct counts do not add | keep IDs or mergeable sketches (HLL) and merge per range |
| "Cache it" (for a write-heavy or strongly consistent read) | stale reads, invalidation races, low hit ratio | say the invalidation strategy and the consistency the read can tolerate |
| "Shard by user" (with celebrity users) | one shard takes the celebrity's traffic | name the hot key and the fix: split it, salt it, or treat it separately |
