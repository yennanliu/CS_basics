# Crux catalog

The one thing each classic is really testing, the decision a strong answer makes, and where in
this repo it is worked. `consult` and `mock` push on the crux; `case` builds the case around
it; `gaps` compares this list with `system_design/`.

## Backend classics

| Problem | Crux | The decision to make out loud | In the repo |
|---|---|---|---|
| URL shortener | ID generation + a read-heavy path | counter ranges → base62 over hashing; cache-first reads; 301 vs 302 | `system_design/url_shortener/` |
| News feed / Twitter timeline | fan-out | push on write for normal users, pull at read for celebrities (hybrid) | `system_design/design_twitter/` |
| Chat / messaging | connections + ordering + delivery | per-conversation sequence numbers; WebSocket gateways with a presence store; at-least-once with client dedupe | `doc/faq/backend/web_long_connections.md` |
| Rate limiter | shared counters under concurrency | token bucket vs sliding window; atomic check-and-decrement in Redis; fail open vs closed | `doc/faq/backend/be_programming_notes.md`, `doc/faq/backend/api_design.md` |
| Distributed KV store | partitioning + replication | consistent hashing with virtual nodes; quorum `R + W > N`; hinted handoff | `system_design/key_value_store_search_engine/`, `doc/faq/de/sql_vs_nosql.md` |
| Web crawler | frontier + politeness + dedupe | per-host queues; URL and content fingerprints; robots.txt | `system_design/web_crawler/` |
| Ride hailing / nearby search | geo index under constant updates | geohash / S2 cells in memory, location updates not written to the main DB | `system_design/design_uber/` |
| File sync (Dropbox) | chunking + sync conflicts | content-addressed chunks dedupe uploads; metadata DB separate from blob store | `system_design/design_dropbox_google_drive/` |
| Video streaming | upload → transcode pipeline + delivery | async transcoding DAG; adaptive bitrate segments on a CDN | `system_design/design_netflix/` |
| Matching / swiping (Tinder) | candidate generation + mutual match | precomputed candidate queues per user; a match is a two-sided write checked atomically | `system_design/design_tinder/` |
| Ticket booking / inventory | no double-sell under contention | conditional update / row lock vs a reservation with TTL; idempotency keys | `doc/faq/backend/overbooking_prevention.md` |
| Payments | exactly-once money movement | idempotency keys, a double-entry ledger, reconciliation with the processor | `doc/faq/backend/api_design.md` (idempotency keys) |
| Notifications | fan-out to many channels reliably | a queue per channel, retries with backoff, user preferences, dedupe | — |
| Leaderboard | ranked reads at write speed | Redis sorted set; sharded ranges for huge boards | `doc/faq/redis/redis_leaderboard.md` |
| Typeahead | prefix lookup at low latency | a trie / precomputed top-k per prefix, rebuilt offline, cached at the edge | — |
| Webhook delivery / receipt | at-least-once across a network you do not own | signed payloads, fast ACK, dedupe on event id, retry + DLQ | `doc/faq/backend/webhook_integration.md` |

## Data-engineering classics

| Problem | Crux | The decision to make out loud | In the repo |
|---|---|---|---|
| OLTP → warehouse daily | incremental, idempotent loads with history | CDC from a replica; overwrite by date; SCD2 | `doc/faq/de/batch_pipeline.md` §11 |
| Real-time metrics per city | event time + revisable results | watermark + allowed lateness; upsert by `(city, window)` | `doc/faq/de/stream_pipeline.md` §13 |
| Clickstream analytics | duplicates + lateness from clients | client `event_id` + a retry contract the dedupe TTL covers | `doc/faq/de/de_system_design.md` §6-1 |
| Ad clicks for billing | provisional vs billable numbers | stream for the dashboard, reconciled batch for the bill | `doc/faq/de/de_system_design.md` §6-2 |
| Top-K trending | candidates in bounded memory | two-stage aggregation; mergeable summaries, not per-window top-N | `doc/faq/de/de_system_design.md` §6-3 |
| Distinct counts | counts do not add | HyperLogLog sketches merged per range | `doc/faq/de/de_system_design.md` §6-4 |
| Feature store | train/serve skew + leakage | one definition, offline + online stores, point-in-time joins | `doc/faq/de/de_system_design.md` §6-5 |
| Fraud detection | a synchronous decision on async features | stream keeps features warm in a KV; fail open vs closed | `doc/faq/de/de_system_design.md` §6-6 |
| Company data platform | many sources, many readers, one truth | open table format + one catalog; medallion with ownership; contracts | `doc/faq/de/de_system_design.md` §6-7 |
| Hadoop → cloud migration | moving without breaking consumers | storage first; migrate by domain; dual-run and compare | `doc/faq/de/de_system_design.md` §6-8 |
| CDC into a lakehouse | ordering, deletes and schema change | key by primary key; `MERGE` with tombstones; registry compatibility | `doc/faq/de/batch_pipeline.md` §2-3 |
| Log / metrics pipeline | volume at low value per event | sampling and tiering; rollups for old data; cardinality limits | `system_design/real_time_pipeline/` |
