# Schema Design for Data Engineers

> **Scope** — physical schema design for data systems: how to answer a schema-design question, OLTP tables, constraints and indexes, keys and ID generation, warehouse partitioning / clustering / distribution, data types, NULLs and conventions, soft deletes and multi-tenancy, schema evolution, event schemas, and worked DDL in PostgreSQL, BigQuery and Spark / Delta. It does not cover logical dimensional modeling (star schema, SCD) or NoSQL query-first modeling.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`data_modeling.md`](./data_modeling.md) — facts, dimensions, grain and SCD; [`sql_vs_nosql.md`](./sql_vs_nosql.md) — query-first NoSQL modeling; [`sql_for_data.md`](./sql_for_data.md), [`batch_pipeline.md`](./batch_pipeline.md), [`stream_pipeline.md`](./stream_pipeline.md), [`spark.md`](./spark.md), [`kafka.md`](./kafka.md), [`hadoop.md`](./hadoop.md) — the other deep-dives.

Every DDL block names its dialect in its first comment. Syntax differs between engines more
than anywhere else in SQL, and an interviewer will notice `CLUSTERED BY` written where
`CLUSTER BY` was meant.

---

## 1) How to Answer a Schema-Design Question ⭐⭐⭐⭐⭐

### 1-1) The method

**Schema design is answered from the queries backwards: what one row means, how the data
is read and written, and how it will change — the DDL comes last.**

```text
1 Requirements      what is the system for? who reads, who writes, how much, how fresh?
2 Access patterns   the top 3-5 queries and writes, with their filters and latency needs
3 Grain             what exactly one row of each table represents
4 Entities          the nouns, their relationships and cardinalities (1:1, 1:N, M:N)
5 Keys              primary keys, natural vs surrogate, how ids are generated
6 Physical layout   indexes (OLTP) or partitioning / clustering / distribution (OLAP)
7 Evolution         what changes next quarter, and how the schema absorbs it
```

Say the steps out loud. An interviewer grades the order as much as the tables: a candidate
who writes `CREATE TABLE` before asking how the data is queried has designed for nothing.

### 1-2) The questions to ask before any DDL

| Ask | Why it changes the design |
|---|---|
| OLTP or analytics? | normalised + indexes vs denormalised + partitions |
| Read / write ratio and volume | index count, partitioning, whether to shard |
| The hottest query and its filter | the primary key, the partition and clustering columns |
| Freshness | batch tables vs streaming upserts vs append-only events |
| Does history matter? | overwrite vs history table vs SCD2 (see [`data_modeling.md`](./data_modeling.md)) |
| Who else reads it? | how strict the evolution rules must be |
| Retention and privacy | partition expiry, PII columns, deletion requests |

---

## 2) OLTP Schema Design ⭐⭐⭐⭐⭐

### 2-1) Normalisation in practice

**Normalise to third normal form so each fact is stored once, then denormalise deliberately
where a measured read path needs it.**

- **1NF** — atomic columns, no repeating groups (`phone1, phone2, phone3` → a `phones` table).
- **2NF** — no non-key column depends on part of a composite key.
- **3NF** — no non-key column depends on another non-key column (`zip → city`).
- **Deliberate denormalisation** — copy a value when it must be frozen at write time
  (`order_line.unit_price`, because the product price changes later) or when a hot read path
  pays for a join on every request (a cached `orders.item_count`).

The frozen copy is not redundancy: an order's price is a different fact from the product's
current price.

### 2-2) Constraints

**Constraints are the schema's executable documentation: the database rejects a bad row
instead of a pipeline discovering it a week later.**

| Constraint | Guarantees | Note |
|---|---|---|
| `PRIMARY KEY` | unique + not null, one per table | creates a unique index |
| `FOREIGN KEY` | the referenced row exists | PostgreSQL does **not** index the referencing column for you |
| `UNIQUE` | no duplicate values | PostgreSQL allows several `NULL`s unless `NULLS NOT DISTINCT` (15+) |
| `NOT NULL` | a value is always present | the cheapest constraint; use it by default |
| `CHECK` | a row-level predicate holds | `amount >= 0`, `status IN (...)` |
| `DEFAULT` | a value when none is given | `created_at DEFAULT now()` |

Warehouses differ: BigQuery, Snowflake and Redshift accept `PRIMARY KEY` / `FOREIGN KEY`
declarations but **do not enforce** them (they are hints for the optimiser), so uniqueness
there is a data-quality test, not a guarantee.

### 2-3) Indexing ⭐⭐⭐⭐⭐

**An index is a sorted copy of some columns plus a pointer to the row; it turns a full scan
into a tree lookup, and every write pays to keep it up to date.**

| Index | Use for |
|---|---|
| **B-tree** (default) | equality, range, `ORDER BY`, prefix `LIKE 'abc%'` |
| **Composite** `(a, b, c)` | queries filtering on a leading prefix: `a`, `a + b`, `a + b + c` |
| **Covering** (`INCLUDE`) | answering a query from the index alone (index-only scan) |
| **Partial** (`WHERE ...`) | indexing only the rows a hot query touches |
| **Hash** | equality only |
| **GIN** | `jsonb` containment, arrays, full-text search |
| **BRIN** | very large, naturally ordered tables (append-only time series) |

**Composite column order** — the leftmost-prefix rule: an index on `(customer_id, created_at)`
serves `WHERE customer_id = ?` and `WHERE customer_id = ? AND created_at > ?`, but not
`WHERE created_at > ?` alone. Put equality columns first, the range or sort column last.

```sql
-- PostgreSQL
-- "a customer's latest orders": equality on customer_id, then sorted by created_at
CREATE INDEX idx_orders_customer_created
    ON orders (customer_id, created_at DESC);

-- covering: the query reads status and total without touching the table
CREATE INDEX idx_orders_customer_cover
    ON orders (customer_id, created_at DESC) INCLUDE (status, total_amount);

-- partial: only the small set of orders still waiting for payment
CREATE INDEX idx_orders_pending
    ON orders (created_at) WHERE status = 'pending';
```

Check that an index is used with `EXPLAIN (ANALYZE, BUFFERS)`; a plan showing `Seq Scan` on
a selective filter means the index is missing, unusable (a function on the column), or the
statistics are stale.

### 2-4) When indexes hurt

- **Every write maintains every index** — an insert into a table with eight indexes is nine
  writes. Write-heavy tables keep few indexes.
- **Low selectivity** — an index on a boolean or a three-value `status` rarely helps unless it
  is partial over the rare value.
- **Functions defeat indexes** — `WHERE lower(email) = ?` needs an expression index on
  `lower(email)`; `WHERE created_at::date = ?` should be rewritten as a range.
- **Duplicate prefixes** — `(a)` is redundant next to `(a, b)`.
- **Building on a live table** — plain `CREATE INDEX` blocks writes; use
  `CREATE INDEX CONCURRENTLY` in PostgreSQL.
- **Bulk loads** — drop secondary indexes, load, recreate; often faster than maintaining them row by row.

---

## 3) Keys and ID Generation ⭐⭐⭐⭐

### 3-1) Natural vs surrogate keys

**Use a surrogate key as the primary key and keep the natural key as a unique column — the
surrogate never changes, the natural key is what people look things up by.**

| | Natural key | Surrogate key |
|---|---|---|
| What | a business identifier (`email`, `order_no`, `isbn`) | a meaningless generated id |
| Pro | meaningful, no lookup, dedupe for free | stable, compact, survives business changes |
| Con | can change (`email`), can collide across source systems | needs a generation step; a duplicate needs a separate unique constraint |
| Fits | small reference tables (`country_code`, `currency_code`) | almost everything else |

In a warehouse dimension the surrogate key also versions the row for SCD2, which is why the
fact table stores `customer_sk` rather than `customer_id` — see [`data_modeling.md`](./data_modeling.md).

### 3-2) ID generation strategies

**Sequential ids are compact and index-friendly but need one coordinator; random UUIDs need
no coordinator but scatter inserts across the index; time-ordered ids give you both.**

| Strategy | Size | Ordered | Coordination | Watch out for |
|---|---|---|---|---|
| Auto-increment / `IDENTITY` | 8 bytes (`BIGINT`) | yes | the database | one writer; ids leak business volume; merges across shards collide |
| **UUIDv4** | 16 bytes | no | none | random inserts fragment a B-tree and hurt cache locality |
| **UUIDv7** (RFC 9562) | 16 bytes | by time (ms) | none | leaks the creation time |
| **ULID** | 16 bytes | by time (ms) | none | not a native type in most databases |
| **Snowflake id** | 8 bytes | by time | a unique worker id per generator | clock going backwards; worker-id assignment |
| **Hash of natural key** | 8–32 bytes | no | none | collisions at short widths; changes if the natural key does |

- A Snowflake id packs a millisecond timestamp, a worker id and a per-millisecond sequence
  into a 64-bit integer (Twitter's layout: 41 / 10 / 12 bits plus a sign bit).
- **Deterministic ids** — a hash of the natural key (`md5(source || ':' || order_no)`) lets a
  re-run produce the same key, which keeps a pipeline idempotent.
- PostgreSQL has `gen_random_uuid()` (v4) built in since 13 and `uuidv7()` since 18; store
  either in the native `uuid` type, never as `text`.

---

## 4) Warehouse Physical Design ⭐⭐⭐⭐⭐

### 4-1) Partitioning

**Partition by the column nearly every query filters on — usually the event date — so a
query reads only the partitions it needs and old data can be dropped a partition at a time.**

- **Pick** a low-cardinality, always-filtered column: `event_date`, sometimes `region`.
- **Size** — partitions should stay large. Thousands of tiny partitions cost more in metadata
  and file listing than they save in pruning.
- **Pruning needs a direct filter** — `WHERE event_date = '2026-11-01'` prunes;
  `WHERE DATE(event_ts) = ...` on a table partitioned by `event_date` does not.
- **Lifecycle** — expire or archive whole partitions (`partition_expiration_days` in BigQuery,
  `DROP PARTITION` in PostgreSQL / Hive).
- **Guard rails** — BigQuery's `require_partition_filter = TRUE` rejects a query that would
  scan every partition.
- **Iceberg hidden partitioning** — `PARTITIONED BY (days(event_ts))` derives the partition
  from a timestamp, so writers and readers never handle a separate date column.

### 4-2) Clustering and sort keys

**Clustering orders the data inside each partition so the engine can skip blocks using
min/max statistics; choose the columns queries filter or join on after the partition column.**

| Engine | Feature | Limit / note |
|---|---|---|
| BigQuery | `CLUSTER BY` | up to 4 columns; order matters (first is most significant) |
| Snowflake | automatic micro-partitions + optional clustering key | reclustering costs credits |
| Redshift | `SORTKEY` (compound or interleaved) | compound favours its leading column |
| Delta Lake | `OPTIMIZE ... ZORDER BY`, or liquid clustering `CLUSTER BY` | liquid clustering replaces `PARTITIONED BY` on that table |
| Iceberg | sort order (`WRITE ORDERED BY`) | applied when data is written or rewritten |

Rule of thumb: partition by time, cluster by the next one to three most-filtered columns
(`customer_id`, `country`), highest-value filter first.

### 4-3) Bucketing, Z-order and liquid clustering

- **Bucketing** (Hive / Spark) — hashes rows into a fixed number of files by key
  (`CLUSTERED BY (user_id) INTO 64 BUCKETS`). Two tables bucketed the same way on the join key
  can be joined without a shuffle. The bucket count is fixed at creation.
- **Z-order** — interleaves several columns' bits so rows close in all of them land in the same
  files; good when queries filter on different columns.
- **Liquid clustering** (Delta) — incremental clustering whose keys can be changed later
  without rewriting the table; it cannot be combined with `PARTITIONED BY` on the same table.

Note the naming collision: `CLUSTERED BY ... INTO n BUCKETS` in Hive / Spark is bucketing;
`CLUSTER BY` in BigQuery or Delta is sort-based clustering.

### 4-4) Redshift distribution styles

**In an MPP warehouse the distribution key decides which node holds each row; distribute two
big tables on their join key so the join happens locally instead of over the network.**

| `DISTSTYLE` | Rows go to | Use for |
|---|---|---|
| `AUTO` (default) | Redshift chooses and adjusts | the starting point |
| `KEY` | the node chosen by hashing `DISTKEY` | large fact + large dimension joined on that key |
| `ALL` | a full copy on every node | small, slowly changing dimensions |
| `EVEN` | round-robin | tables with no dominant join |

A skewed `DISTKEY` (most rows share one value) puts most of the work on one node — the same
failure as data skew in Spark.

---

## 5) Data Types ⭐⭐⭐⭐

### 5-1) Money and numbers

**Store money as an exact decimal or as integer minor units — never as a binary float, which
cannot represent 0.10 exactly.**

| Choice | Example | Note |
|---|---|---|
| `NUMERIC(12, 2)` / `DECIMAL` | `19.99` | exact; the default answer |
| Integer minor units | `1999` cents in `BIGINT` | fast and exact; minor units differ by currency (JPY has 0 decimals, BHD has 3) |
| `FLOAT` / `DOUBLE` | — | for measurements and ratios, never money |
| PostgreSQL `money` | — | locale-dependent formatting; avoid |

Always store the currency next to the amount (`currency_code CHAR(3)`, ISO 4217).

### 5-2) Timestamps and time zones ⭐⭐⭐⭐⭐

**Store instants in UTC with a time-zone-aware type, and derive local dates for reporting; a
timestamp without a zone is a wall-clock reading, not a point in time.**

| Engine | Instant (absolute) | Wall-clock (no zone) |
|---|---|---|
| PostgreSQL | `timestamptz` — stored as UTC, shown in the session zone | `timestamp` |
| BigQuery | `TIMESTAMP` | `DATETIME` |
| Spark 3.4+ | `TIMESTAMP` (= `TIMESTAMP_LTZ`, session zone) | `TIMESTAMP_NTZ` |
| Snowflake | `TIMESTAMP_TZ` / `TIMESTAMP_LTZ` | `TIMESTAMP_NTZ` |

- Keep **event time** (when it happened) and **ingest time** (when it arrived) as two columns.
- A "daily" report needs a time zone: `DATE(event_ts, 'America/New_York')` in BigQuery. Store
  the user's zone if reports are per user.
- A future appointment ("9:00 local next March") is a wall-clock time plus a zone name, because
  zone rules can change before it happens.

### 5-3) Strings, booleans and enums

- **Strings** — PostgreSQL `text` and `varchar(n)` perform the same; `varchar(n)` is only a
  length check. Use it when the limit is a real rule.
- **Booleans** — a real `BOOLEAN`, named as a statement (`is_active`, `has_paid`), not a `'Y'/'N'`
  string. A three-state flag is not a boolean; make it an enum.
- **Enums** — a `CHECK (status IN (...))` or a lookup table. A native PostgreSQL `ENUM` can
  gain values but cannot drop them without recreating the type; magic integers (`status = 3`)
  document nothing.
- **Identifiers from other systems** — store as strings even when they look numeric
  (`'00123'` loses its zeros as an integer).

### 5-4) Semi-structured and nested data

**Land JSON as-is in a semi-structured column, then promote the fields queries actually use
to typed columns.**

| Engine | Type |
|---|---|
| PostgreSQL | `jsonb` (binary, indexable with GIN) — prefer it over `json` |
| BigQuery | `JSON`, or typed `STRUCT` / `ARRAY` (nested, repeated fields) |
| Snowflake | `VARIANT` |
| Spark / Delta | `STRUCT`, `ARRAY`, `MAP`; `VARIANT` in Spark 4.0 |

BigQuery's nested and repeated fields store an order with its lines in one row, so the
common read needs no join; `UNNEST` flattens them when it does.

```sql
-- BigQuery
CREATE TABLE shop.orders_nested (
  order_id     STRING    NOT NULL,
  order_ts     TIMESTAMP NOT NULL,
  customer_id  STRING    NOT NULL,
  line_items   ARRAY<STRUCT<sku STRING, quantity INT64, unit_price NUMERIC>>,
  attributes   JSON
)
PARTITION BY DATE(order_ts);

-- revenue per SKU: flatten the repeated field
SELECT li.sku, SUM(li.quantity * li.unit_price) AS revenue
FROM   shop.orders_nested, UNNEST(line_items) AS li
WHERE  DATE(order_ts) = '2026-11-01'
GROUP  BY li.sku;
```

Here `PARTITION BY DATE(order_ts)` is BigQuery's time-unit partitioning on a `TIMESTAMP`
column, so the `DATE(order_ts)` filter does prune.

---

## 6) NULLs, Conventions and Table Patterns ⭐⭐⭐

### 6-1) NULL handling and defaults

**`NULL` means unknown, and it propagates: `NULL = NULL` is unknown, `NULL + 1` is `NULL`, and
`NOT IN` over a list containing `NULL` matches nothing.**

- `NOT NULL` by default; allow `NULL` only when "unknown" is a real state.
- Do not encode unknown as a magic value (`'1900-01-01'`, `-1`) in a typed column — aggregates
  silently include it. The exception is a dimension's explicit "unknown" member row, which
  keeps fact foreign keys `NOT NULL`.
- Compare null-safely when a change detector must see `NULL → value`: `IS DISTINCT FROM`.
- In PostgreSQL 11+, `ADD COLUMN ... DEFAULT <constant>` is a metadata-only change; a volatile
  default (`random()`) still rewrites the table.

### 6-2) Naming and audit columns

| Convention | Example |
|---|---|
| `snake_case`, singular or plural — pick one | `order_line`, `customer` |
| layer / role prefixes in a warehouse | `stg_`, `fact_`, `dim_`, `agg_` |
| key suffixes | `_id` natural / source key, `_sk` surrogate key |
| time suffixes | `_at` / `_ts` for timestamps, `_date` for dates |
| booleans as statements | `is_deleted`, `has_discount` |
| units in the name | `duration_ms`, `distance_km`, `amount_usd` |

Audit columns on every table: `created_at`, `updated_at`; in a pipeline also `_ingested_at`,
`_source`, `_batch_id` (or `_run_id`), so any row can be traced to the load that wrote it.

### 6-3) Soft deletes and history tables

**A soft delete marks a row deleted instead of removing it — keeping it for audit and undo, at
the cost of every query having to exclude it.**

- `deleted_at TIMESTAMPTZ NULL` beats `is_deleted BOOLEAN`: it records when.
- Uniqueness must ignore deleted rows — a **partial unique index**.
- A view (`active_customer`) hides the filter from readers.
- Privacy deletion ("delete my data") needs a **hard** delete or anonymisation; a soft delete
  does not satisfy it.
- **History table** — an append-only `customer_history` written by the application or a
  trigger keeps every version without cluttering the main table; CDC into the lake does the
  same downstream.

```sql
-- PostgreSQL
CREATE TABLE customer (
  customer_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email        TEXT        NOT NULL,
  full_name    TEXT        NOT NULL,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  deleted_at   TIMESTAMPTZ
);

-- an email is unique among live customers; a deleted one can sign up again
CREATE UNIQUE INDEX uq_customer_email_live
    ON customer (lower(email)) WHERE deleted_at IS NULL;

CREATE VIEW active_customer AS
SELECT * FROM customer WHERE deleted_at IS NULL;
```

### 6-4) Multi-tenant schemas

| Model | Isolation | Cost / ops | Fits |
|---|---|---|---|
| **Shared tables + `tenant_id`** | logical — every query filters | cheapest, one migration | many small tenants (SaaS default) |
| **Schema per tenant** | namespace | migrations run N times | tens to hundreds of tenants |
| **Database per tenant** | strong | most expensive | few large or regulated tenants |

- With shared tables, lead every index and primary key with `tenant_id`, and enforce the
  filter in the database with PostgreSQL **row-level security** rather than trusting every query.
- Watch for a **noisy neighbour**: one huge tenant can dominate a shared table — the reason
  large tenants are often moved to their own database.

```sql
-- PostgreSQL row-level security on a shared table
ALTER TABLE invoice ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation ON invoice
    USING (tenant_id = current_setting('app.tenant_id')::BIGINT);
-- the application runs SET app.tenant_id = '42' per connection / transaction
-- the table owner bypasses policies unless: ALTER TABLE invoice FORCE ROW LEVEL SECURITY;
```

---

## 7) Schema Evolution ⭐⭐⭐⭐⭐

### 7-1) Which changes are safe

**Additive changes are safe; renames, type narrowing and dropping a field someone reads are
breaking — so every change is classified before it ships.**

| Change | Usually safe? | How to do it |
|---|---|---|
| Add a nullable column / field with a default | yes | just add it |
| Widen a type (`INT` → `BIGINT`) | usually | check each engine and serialisation format |
| Rename a column | no | expand-and-contract (7-5) |
| Drop a column | breaks its readers | deprecate, confirm no readers, then drop |
| Narrow a type / change units or meaning | no | a new column or a new versioned table |
| Make a nullable column `NOT NULL` | breaks writers that send `NULL` | backfill, then add the constraint |

### 7-2) Avro and Protobuf rules

**Avro resolves the writer's schema against the reader's by field name and fills missing
fields from defaults; Protobuf identifies fields by number, so a number is forever.**

| | Avro | Protobuf (proto3) |
|---|---|---|
| Field identity | name (aliases for renames) | field number |
| Add a field | give it a default | any new number; absent fields read as the type's default |
| Remove a field | safe for readers that have a default for it | reserve its number and name with `reserved` |
| Rename | via `aliases` | wire-safe (binary carries numbers), breaks JSON mapping |
| Change type | only listed promotions (`int` → `long`, `float` → `double`, ...) | only wire-compatible pairs (`int32` ↔ `int64`, ...); otherwise a new field |

```text
// proto3 — never reuse a deleted field's number
message Order {
  reserved 4;                 // was: string coupon = 4;
  reserved "coupon";
  string order_id   = 1;
  int64  amount_cents = 2;
  string currency   = 3;
  string promo_code = 5;      // the replacement gets a NEW number
}
```

### 7-3) Schema Registry compatibility modes

**The registry checks each new schema version against the old ones and rejects an incompatible
one, so a bad producer deploy fails at registration instead of breaking every consumer.**

| Mode | Guarantees | Allowed changes (Avro) | Upgrade first |
|---|---|---|---|
| `BACKWARD` (default) | new schema reads data written with the previous one | delete fields, add fields with defaults | consumers |
| `FORWARD` | previous schema reads data written with the new one | add fields, delete fields that had defaults | producers |
| `FULL` | both | add / delete fields with defaults only | either |
| `*_TRANSITIVE` | the same, against **all** earlier versions, not just the last | — | — |
| `NONE` | nothing checked | anything | — |

For a Kafka topic that consumers replay from the beginning, the transitive modes are the safe
choice: a consumer may meet a record written by any old version.

### 7-4) Table-format evolution

| Format | Add | Rename / reorder | Drop | Note |
|---|---|---|---|---|
| Plain Parquet files | yes (readers merge schemas) | breaks (by name) | readers see `NULL` | no table-level schema at all |
| Hive Metastore table | `ADD COLUMNS` | by position or name, depending on format | limited | metadata says one thing, files another |
| **Iceberg** | yes | yes, metadata-only (columns tracked by id) | yes | also partition evolution without rewriting data |
| **Delta Lake** | yes (`mergeSchema` on write) | metadata-only **only with column mapping** | with column mapping | column mapping upgrades the table protocol |

```sql
-- Spark SQL with Delta Lake: enable column mapping, then rename without rewriting data
ALTER TABLE lake.events SET TBLPROPERTIES (
  'delta.columnMapping.mode' = 'name',
  'delta.minReaderVersion'   = '2',
  'delta.minWriterVersion'   = '5'
);
ALTER TABLE lake.events RENAME COLUMN usr_id TO user_id;
```

Upgrading the protocol means older Delta readers can no longer read the table — check every
reader's version first.

### 7-5) Expand and contract

**Rename or retype a column without downtime in three releases: add the new shape, move
everyone to it, then remove the old one.**

```text
1 EXPAND    add new column (nullable); writers write BOTH old and new
2 BACKFILL  copy old -> new for existing rows, in batches
3 MIGRATE   readers switch to the new column; verify nothing reads the old one
4 CONTRACT  stop writing the old column; drop it in a later release
```

The same pattern versions a whole table or topic (`orders_v2`) when the change is too large
for one column, with a **data contract** stating who owns the schema and how much notice a
breaking change gets.

---

## 8) Event and Log Schemas ⭐⭐⭐⭐

### 8-1) The event envelope

**Every event carries the same envelope — a unique id, two timestamps, its type and schema
version — around a payload that varies by type.**

| Field | Why |
|---|---|
| `event_id` | unique per event (UUIDv7) — consumers dedupe on it |
| `event_type` | `order_placed`, `page_viewed` — routes and filters |
| `schema_version` | which payload schema to parse with |
| `event_time` | when it happened (producer clock) — windows and partitions use it |
| `ingest_time` | when the pipeline received it — measures lateness, drives incremental loads |
| `producer` / `source` | which service emitted it |
| `entity_id` | the key the stream is partitioned and ordered by (`order_id`, `user_id`) |
| `payload` | the type-specific body |

```json
{
  "event_id": "0190f5c2-7b1e-7cc3-9a2f-3c5e8d1b4a77",
  "event_type": "order_placed",
  "schema_version": 3,
  "event_time": "2026-11-01T14:03:22.418Z",
  "producer": "checkout-service",
  "entity_id": "ord_8812",
  "payload": { "customer_id": "cus_19", "amount_cents": 4599, "currency": "USD" }
}
```

`ingest_time` is usually added by the pipeline, not the producer.

### 8-2) Event design rules

- **Facts, not commands** — name events in the past tense (`order_placed`), describing what
  happened, so any number of consumers can react.
- **Self-contained enough** — include the fields consumers need to avoid a lookup at
  processing time, but not a full copy of every related entity.
- **Immutable** — a correction is a new event (`order_amount_corrected`), not an edit.
- **One topic per entity or domain, not per consumer** — consumers filter.
- **Partition key = the entity whose order matters** — all of an order's events go to one
  partition, so they are read in order (see [`kafka.md`](./kafka.md)).
- **No PII you do not need** — events are copied everywhere and retained for a long time.

---

## 9) Worked Case Studies ⭐⭐⭐⭐⭐

### 9-1) E-commerce OLTP (PostgreSQL)

**Requirements**: customers place orders of several products; show a customer's recent
orders; never oversell; keep the price paid.

```sql
-- PostgreSQL
CREATE TABLE product (
  product_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  sku          TEXT          NOT NULL UNIQUE,
  name         TEXT          NOT NULL,
  price        NUMERIC(12,2) NOT NULL CHECK (price >= 0),
  stock_qty    INTEGER       NOT NULL CHECK (stock_qty >= 0),   -- never oversell
  created_at   TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE orders (
  order_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  customer_id  BIGINT        NOT NULL REFERENCES customer (customer_id),
  status       TEXT          NOT NULL DEFAULT 'pending'
               CHECK (status IN ('pending', 'paid', 'shipped', 'cancelled')),
  total_amount NUMERIC(12,2) NOT NULL CHECK (total_amount >= 0),
  currency     CHAR(3)       NOT NULL,
  created_at   TIMESTAMPTZ   NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE order_line (
  order_id     BIGINT        NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE,
  line_no      SMALLINT      NOT NULL,
  product_id   BIGINT        NOT NULL REFERENCES product (product_id),
  quantity     INTEGER       NOT NULL CHECK (quantity > 0),
  unit_price   NUMERIC(12,2) NOT NULL,          -- frozen at purchase time
  PRIMARY KEY (order_id, line_no)
);

-- the hot read: a customer's latest orders
CREATE INDEX idx_orders_customer_created ON orders (customer_id, created_at DESC);
-- foreign keys are not indexed automatically
CREATE INDEX idx_order_line_product ON order_line (product_id);
```

The `stock_qty >= 0` check makes the database reject an oversell: a decrement
`UPDATE product SET stock_qty = stock_qty - 2 WHERE product_id = ?` fails rather than going
negative. It uses the `customer` table from 6-3.

### 9-2) Ride-sharing analytics (BigQuery)

**Requirements**: trips and revenue per city per day; driver performance; history of a
driver's city and rating.

1. **Process** — trip completed.
2. **Grain** — `fact_trip`: one row per completed trip.
3. **Dimensions** — rider, driver (SCD2), date, zone (zones roll up to cities).
4. **Measures** — fare, distance, duration, surge multiplier.
5. **Layout** — partition by `trip_date`, cluster by `pickup_zone_sk`, `driver_sk`.

```sql
-- BigQuery
CREATE TABLE analytics.fact_trip (
  trip_id          INT64          NOT NULL,     -- degenerate dimension
  trip_date        DATE           NOT NULL,     -- partition column
  rider_sk         INT64          NOT NULL,
  driver_sk        INT64          NOT NULL,     -- the driver version at trip time
  pickup_zone_sk   INT64          NOT NULL,
  dropoff_zone_sk  INT64          NOT NULL,
  requested_at     TIMESTAMP      NOT NULL,     -- BigQuery TIMESTAMP is an absolute instant
  completed_at     TIMESTAMP      NOT NULL,
  distance_km      NUMERIC(8, 2),
  duration_s       INT64,
  surge_multiplier NUMERIC(4, 2),
  fare_amount      NUMERIC(12, 2),              -- money: NUMERIC, never FLOAT64
  currency_code    STRING(3)
)
PARTITION BY trip_date
CLUSTER BY pickup_zone_sk, driver_sk
OPTIONS (require_partition_filter = TRUE);
```

`trip_date` is the trip's **local** date in its city, computed at load time — so "per city per
day" means the city's own day, not UTC's. Say which one you picked.

### 9-3) Clickstream event table (Spark SQL with Delta Lake)

**Requirements**: billions of events a day; queries by day and by user; replays from Kafka
must not duplicate.

```sql
-- Spark SQL with Delta Lake
CREATE TABLE lake.events (
  event_id        STRING    NOT NULL,
  event_type      STRING    NOT NULL,
  schema_version  INT       NOT NULL,
  user_id         STRING,
  event_time      TIMESTAMP NOT NULL,
  ingest_time     TIMESTAMP NOT NULL,
  event_date      DATE      NOT NULL,          -- derived from event_time by the writer
  properties      MAP<STRING, STRING>
)
USING DELTA
PARTITIONED BY (event_date);

-- idempotent load from a staging batch: a replayed event_id is skipped
MERGE INTO lake.events AS t
USING staging_events AS s
ON    t.event_id = s.event_id AND t.event_date = s.event_date
WHEN NOT MATCHED THEN INSERT *;

-- co-locate each user's events inside the day's files
OPTIMIZE lake.events WHERE event_date = '2026-11-01' ZORDER BY (user_id);
```

Putting `event_date` in the `MERGE` condition limits the match to the partitions in the batch
instead of the whole table — which assumes a replayed event keeps its original `event_time`.

---

## 10) Anti-Patterns

| Anti-pattern | Why it hurts | Instead |
|---|---|---|
| Entity-attribute-value table (`entity, key, value`) | no types, no constraints, every query a pivot | real columns, or a `jsonb` column for the truly variable part |
| `FLOAT` for money | `0.1 + 0.2 ≠ 0.3` | `NUMERIC` or integer minor units |
| `TIMESTAMP` without a zone for instants | ambiguous across zones and DST | `timestamptz` / UTC |
| Comma-separated lists in a column | cannot index, join or constrain | a child table or an `ARRAY` type |
| Partitioning by a high-cardinality column | millions of tiny partitions | partition by date, cluster by the id |
| One table with `type` and 40 mostly-null columns | sparse, unclear | a table per type, or a shared core + extension tables |
| No `NOT NULL` anywhere | the schema documents nothing | `NOT NULL` by default |
| Reusing a column for a new meaning | old data silently reinterpreted | a new column |
| Unindexed foreign keys on big child tables | slow joins and slow parent deletes | index the referencing column |

---

## 11) Quick Q → A

**Q: UUID or auto-increment primary key?**
A: Auto-increment `BIGINT` for a single database where ids are not exposed; UUIDv7 when ids
are generated in many places or exposed in URLs — it is time-ordered, so it does not fragment
the index like UUIDv4.

**Q: How do you choose the partition column?**
A: The column almost every query filters on, with low enough cardinality that partitions stay
large — usually the event date. Then cluster by the next most-filtered columns.

**Q: A column needs renaming in a table 30 jobs read. How?**
A: Expand and contract: add the new column, dual-write, backfill, move readers, then drop the
old one — or, on Iceberg or Delta with column mapping, a metadata-only rename once every
reader supports it.

**Q: Do you put foreign keys in a warehouse?**
A: Declare them for documentation and the optimiser, but most warehouses do not enforce them,
so referential integrity is checked by data-quality tests in the pipeline.

**Q: Where does JSON belong?**
A: In a semi-structured column at the raw layer; fields that are filtered, joined or
aggregated get promoted to typed columns in the cleaned layer.

**Q: What breaks a Kafka consumer, schema-wise?**
A: Removing or retyping a field it reads, or a producer registering an incompatible schema —
which a Schema Registry in `BACKWARD` or `FULL` mode rejects at registration time.
