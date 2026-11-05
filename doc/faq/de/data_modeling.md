# Data Modeling for Data Engineers

> **Scope** — logical data modeling for analytics and the interview questions it draws: modeling levels, ER and normalization, OLTP vs OLAP, Kimball dimensional modeling (grain, facts, dimensions, the bus matrix), fact and dimension patterns, slowly changing dimensions, Inmon, Data Vault, One Big Table, the semantic layer, and a worked case study. Physical table design — partitioning, keys, evolution, types — is [`schema_design.md`](./schema_design.md)'s.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`schema_design.md`](./schema_design.md), [`sql_for_data.md`](./sql_for_data.md), [`batch_pipeline.md`](./batch_pipeline.md) and [`sql_vs_nosql.md`](./sql_vs_nosql.md) — the neighbouring deep-dives; [`../faq_data_model.md`](../faq_data_model.md) and [`../db/faq_DB_DW.md`](../db/faq_DB_DW.md) — older notes on the same ground.

SQL is **PostgreSQL** unless a block says otherwise. Each answer leads with the sentence you
would say out loud, then the mechanism, then the trade-off an interviewer pushes on next.

---

## 1) Modeling Levels and ER ⭐⭐⭐

### 1-1) Conceptual, logical, physical

**A conceptual model names the business entities and how they relate; a logical model adds
attributes, keys and cardinality without committing to a database; a physical model is the
DDL for one engine.**

| Level | Answers | Audience | Contains |
|---|---|---|---|
| **Conceptual** | what does the business talk about? | business, product | entities and relationships, no attributes |
| **Logical** | what exactly is stored, and how is it identified? | analysts, engineers | attributes, primary and foreign keys, cardinality, normal form |
| **Physical** | how is it stored on *this* engine? | engineers | types, partitioning, clustering, indexes, constraints |

In an interview, walk down the levels out loud: name the entities, draw the relationships,
declare the keys, then — and only then — talk about partitioning
(see [`schema_design.md`](./schema_design.md)).

### 1-2) ER modeling and cardinality

**An entity-relationship model is entities (things with an identity), attributes, and
relationships with a cardinality on each end — one-to-one, one-to-many or many-to-many.**

| Cardinality | Example | How it lands in tables |
|---|---|---|
| **1 : 1** | user ↔ user_profile | same table, or a foreign key with a `UNIQUE` constraint |
| **1 : N** | customer → orders | foreign key on the "many" side (`orders.customer_id`) |
| **M : N** | students ↔ courses | an associative (junction) table `enrollment(student_id, course_id, ...)` |

```text
customer ──1────N── order ──1────N── order_line ──N────1── product
                       │
                       N
                       │
                       1
                    address
```

- **Optionality** (zero or more vs one or more) is a separate question from cardinality:
  can an order exist with no lines? It decides `NOT NULL` and inner vs outer joins later.
- A junction table usually grows attributes of its own (`enrolled_at`, `grade`) — that is
  the sign it is a real entity, and in a warehouse it often becomes a fact table.

---

## 2) Normalization ⭐⭐⭐⭐

### 2-1) Why normalize: the three anomalies

**Normalization removes redundancy so each fact is stored once — which is what prevents
update, insert and delete anomalies.**

| Anomaly | Symptom in a denormalised `orders` table that repeats the customer's city |
|---|---|
| **Update** | a customer moves; you must update every one of their order rows, or they disagree |
| **Insert** | you cannot record a new customer until they place an order |
| **Delete** | deleting a customer's only order deletes the only record of the customer |

### 2-2) 1NF to BCNF, worked on one table

**Each normal form removes one kind of dependency: 1NF repeating groups, 2NF partial-key
dependencies, 3NF transitive dependencies, BCNF any dependency whose left side is not a key.**

Start with one wide table, keyed by `(order_id, product_id)`:

```text
order_id | order_date | customer_id | customer_city | product_id | product_name | qty | tags
1001     | 2026-11-01 | C1          | Taipei        | P1         | Mug          | 2   | "gift,red"
1001     | 2026-11-01 | C1          | Taipei        | P2         | Pen          | 1   | "office"
```

| Form | Rule | Violation above | Fix |
|---|---|---|---|
| **1NF** | atomic values, no repeating groups | `tags` holds a list | move to `order_line_tag(order_id, product_id, tag)` |
| **2NF** | no non-key attribute depends on *part* of the key | `order_date`, `customer_id` depend on `order_id` alone; `product_name` on `product_id` alone | split `orders(order_id, order_date, customer_id)` and `product(product_id, product_name)` |
| **3NF** | no non-key attribute depends on another non-key attribute | `customer_city` depends on `customer_id`, not on `order_id` | split `customer(customer_id, customer_city)` |
| **BCNF** | every determinant is a candidate key | (none left here) | — |

The result is four tables — `customer`, `orders`, `product`, `order_line(order_id,
product_id, qty)` — and each fact lives in exactly one row.

BCNF only differs from 3NF when a table has overlapping candidate keys. The classic case is
`(student, course, instructor)` where each instructor teaches one course: `instructor →
course`, but `instructor` is not a key, so the table is 3NF yet not BCNF.

4NF and 5NF (multi-valued and join dependencies) rarely come up in a DE interview; knowing
that they exist is enough.

### 2-3) Denormalization and its trade-offs

**Denormalise when reads vastly outnumber writes and the joins cost more than the
redundancy — which is the normal state of an analytics warehouse.**

| | Normalised (3NF) | Denormalised (star, wide table) |
|---|---|---|
| Write path | one place to update | many rows to update, or rebuild |
| Read path | many joins | few or no joins |
| Storage | smallest | larger, but columnar compression narrows the gap |
| Consistency | enforced by structure | enforced by the pipeline that builds it |
| Fits | OLTP | OLAP, dashboards, ML features |

The pipeline is what keeps a denormalised table correct, so the denormalised copy is
**derived** from a normalised or raw source and can always be rebuilt.

---

## 3) OLTP vs OLAP ⭐⭐⭐⭐

**OLTP systems run the business — many small transactions by key, normalised; OLAP systems
analyse it — few large scans and aggregations, dimensional.**

| | OLTP | OLAP |
|---|---|---|
| Typical query | `SELECT ... WHERE order_id = ?` | `SUM(amount) GROUP BY month, region` over years |
| Rows touched | few | millions to billions |
| Model | 3NF entities | star schemas, wide tables |
| Storage layout | row-oriented, B-tree indexes | column-oriented, compressed, zone maps |
| Writes | constant, concurrent, transactional | batch or micro-batch loads |
| History | current state, overwritten | full history kept |
| Examples | PostgreSQL, MySQL, Oracle | BigQuery, Snowflake, Redshift, ClickHouse, Databricks SQL |

The data engineer's job sits between them: extract from OLTP (ideally by CDC, so the source
is not hammered), and remodel into OLAP shape.

---

## 4) Kimball Dimensional Modeling ⭐⭐⭐⭐⭐

### 4-1) The four-step design process

**Pick the business process, declare the grain, identify the dimensions, identify the facts
— in that order, because the grain decides what the other two can be.**

1. **Business process** — an operational activity that produces events: taking orders,
   shipping, a subscription renewal. Not a department, not a report.
2. **Grain** — exactly what one fact row represents.
3. **Dimensions** — the who, what, where, when, why, how that are true at that grain.
4. **Facts** — the numeric measurements that are true at that grain.

### 4-2) Grain: the most important sentence in the model

**State the grain as a sentence in business terms — "one row per product per order line" —
and reject any dimension or fact that is not true at it.**

- Prefer the **atomic** grain (the lowest the source provides). You can always roll up; you
  can never drill below what you stored.
- Mixed grain in one table is the root of most double-counting bugs: an order-level
  `shipping_fee` copied onto every order line gets summed once per line.
- The fix for a fact at a coarser grain is to **allocate** it down (split shipping by line
  value) or keep it in a separate order-level fact table.

### 4-3) Facts and additivity

**A fact is additive if it can be summed across every dimension, semi-additive if across
some, and non-additive if across none — and the type decides how a BI tool may aggregate it.**

| Type | Example | Sum across | Aggregate instead |
|---|---|---|---|
| **Additive** | sales amount, quantity | every dimension | `SUM` |
| **Semi-additive** | account balance, inventory level | accounts, products — **not time** | the period-end value, or `AVG` over time |
| **Non-additive** | unit price, margin %, ratios | nothing | store numerator and denominator, compute the ratio last |

```sql
-- semi-additive: month-end balance per branch, not the sum of daily balances
SELECT d.year_month,
       b.branch_name,
       SUM(f.balance_amount) AS month_end_balance
FROM   fact_account_balance_daily f
JOIN   dim_date   d ON d.date_key   = f.date_key
JOIN   dim_branch b ON b.branch_key = f.branch_key
WHERE  d.is_month_end
GROUP  BY d.year_month, b.branch_name;
```

### 4-4) Star schema vs snowflake schema

**A star schema joins the fact directly to flat, denormalised dimensions; a snowflake
normalises the dimensions into sub-tables. Default to the star.**

```text
            dim_date                          dim_date
               │                                 │
dim_customer ─ fact_sales ─ dim_product    dim_customer ─ fact_sales ─ dim_product
               │                                 │                         │
            dim_store                         dim_store              dim_subcategory
                                                 │                         │
           (star)                             dim_city                dim_category
                                                       (snowflake)
```

| | Star | Snowflake |
|---|---|---|
| Joins per query | one per dimension | more |
| Dimension storage | redundant (category name repeated per product) | normalised |
| Query simplicity for BI users | high | lower |
| When it is right | almost always | a huge dimension with a large, shared sub-dimension; a tool that requires it |

On a columnar warehouse the redundancy in a star's dimensions costs little, because repeated
values compress well, so the extra joins of a snowflake buy almost nothing.

### 4-5) The bus matrix and conformed dimensions

**A conformed dimension means the same thing, with the same keys and attribute values, in
every fact table that uses it — which is what lets you compare facts from different
processes. The bus matrix is the plan that shows which processes share which dimensions.**

```text
                      Date  Customer  Product  Store  Promotion  Carrier
Orders                 X       X         X       X        X
Shipments              X       X         X                         X
Returns                X       X         X       X
Inventory snapshot     X                 X       X
Web sessions           X       X
```

- Each row becomes one or more fact tables; each column is one conformed dimension, built once.
- **Drill-across**: to compare orders with returns, aggregate each fact separately to the
  shared conformed attributes, then join the two results — never join two fact tables row to
  row (that is a fan-out).

```sql
-- drill-across: return rate per product category per month
WITH o AS (
  SELECT d.year_month, p.category, SUM(f.quantity) AS units_sold
  FROM   fact_order_line f
  JOIN   dim_date d    ON d.date_key = f.order_date_key
  JOIN   dim_product p ON p.product_key = f.product_key
  GROUP  BY d.year_month, p.category
), r AS (
  SELECT d.year_month, p.category, SUM(f.quantity) AS units_returned
  FROM   fact_return f
  JOIN   dim_date d    ON d.date_key = f.return_date_key
  JOIN   dim_product p ON p.product_key = f.product_key
  GROUP  BY d.year_month, p.category
)
SELECT o.year_month,
       o.category,
       o.units_sold,
       COALESCE(r.units_returned, 0)                                   AS units_returned,
       ROUND(COALESCE(r.units_returned, 0)::numeric / NULLIF(o.units_sold, 0), 4) AS return_rate
FROM   o
LEFT JOIN r ON r.year_month = o.year_month AND r.category = o.category;
```

---

## 5) Fact Table Types ⭐⭐⭐⭐

### 5-1) The four types

| Type | One row per | Inserted / updated | Example |
|---|---|---|---|
| **Transaction** | event, at the moment it happens | insert only | one order line, one payment, one click |
| **Periodic snapshot** | entity per regular period | insert per period | daily account balance, monthly inventory |
| **Accumulating snapshot** | instance of a process with a known start and end | inserted once, **updated** as milestones happen | order: placed → paid → shipped → delivered |
| **Factless** | an event or a coverage relationship with no measure | insert | student attended class; product was on promotion |

How to pick: a question about **what happened** is a transaction fact; **what was the state
at the end of each period** is a periodic snapshot; **how long between steps of a pipeline**
is an accumulating snapshot; **what did not happen** (promoted but not sold) needs a factless
coverage table to compare against.

### 5-2) An accumulating snapshot, end to end

**An accumulating snapshot has one date key per milestone and the lags between them as
facts, and its row is updated each time the process moves.**

```sql
CREATE TABLE fact_order_fulfillment (
  order_id             BIGINT       PRIMARY KEY,   -- degenerate dimension
  customer_key         BIGINT       NOT NULL,
  placed_date_key      INT          NOT NULL,
  paid_date_key        INT,                        -- NULL until the milestone happens
  shipped_date_key     INT,
  delivered_date_key   INT,
  order_amount         NUMERIC(12, 2) NOT NULL,
  days_placed_to_paid  INT,
  days_paid_to_ship    INT,
  days_ship_to_deliver INT
);

-- a shipment event arrives: move the row along
UPDATE fact_order_fulfillment f
SET    shipped_date_key  = sd.date_key,
       days_paid_to_ship = s.ship_date - pd.full_date      -- DATE - DATE = days
FROM   stg_shipments s
JOIN   dim_date sd ON sd.full_date = s.ship_date,
       dim_date pd
WHERE  f.order_id = s.order_id
  AND  pd.date_key = f.paid_date_key
  AND  f.shipped_date_key IS NULL;
```

- Milestone date keys can point to a special "not yet" row of `dim_date` instead of `NULL`,
  so inner joins keep working; either is fine if it is consistent.
- Because rows are updated, this table is a poor fit for an append-only store; on a
  lakehouse, use `MERGE` (Delta, Iceberg) and expect rewrites.

### 5-3) Aggregate and consolidated facts

- **Aggregate fact** — a pre-summed copy at a coarser grain (`fact_sales_daily_store`) for
  dashboards. It is derived, must agree with the atomic table, and is rebuilt from it.
- **Consolidated fact** — two processes at the same grain combined into one table (actual vs
  forecast per product per month) because they are almost always analysed together.

---

## 6) Dimension Patterns ⭐⭐⭐⭐

### 6-1) Surrogate keys and the date dimension

**Dimensions are keyed by a surrogate key — a meaningless integer the warehouse assigns —
so that history (SCD2), multiple sources and unknown members all have a key of their own.**

The natural key (`customer_id` from the app) stays in the dimension as an attribute for
lookups. Key generation and key-type choices are [`schema_design.md`](./schema_design.md)'s.

The **date dimension** is the one dimension every warehouse has. It is generated, not
loaded, and it carries the calendar logic no one should re-derive in every query:

```sql
CREATE TABLE dim_date (
  date_key      INT PRIMARY KEY,          -- 20261105, readable and sortable
  full_date     DATE NOT NULL UNIQUE,
  year          INT  NOT NULL,
  quarter       INT  NOT NULL,
  month         INT  NOT NULL,
  year_month    CHAR(7) NOT NULL,         -- '2026-11'
  iso_weekday   INT  NOT NULL,            -- 1 = Monday
  is_weekend    BOOLEAN NOT NULL,
  is_month_end  BOOLEAN NOT NULL
);

INSERT INTO dim_date
SELECT CAST(TO_CHAR(g.d, 'YYYYMMDD') AS INT),
       g.d::date,
       EXTRACT(YEAR    FROM g.d)::int,
       EXTRACT(QUARTER FROM g.d)::int,
       EXTRACT(MONTH   FROM g.d)::int,
       TO_CHAR(g.d, 'YYYY-MM'),
       EXTRACT(ISODOW  FROM g.d)::int,
       EXTRACT(ISODOW  FROM g.d) IN (6, 7),
       g.d::date = (date_trunc('month', g.d) + INTERVAL '1 month - 1 day')::date
FROM   generate_series(DATE '2020-01-01', DATE '2030-12-31', INTERVAL '1 day') AS g(d);
```

Fiscal periods, holidays and "same day last year" belong here too, so they are defined once.

### 6-2) Degenerate and junk dimensions

**A degenerate dimension is a key with no attributes left to describe it — like an order
number — so it lives in the fact table with no dimension table. A junk dimension collects
low-cardinality flags into one small table so the fact does not carry a dozen of them.**

| Pattern | What | Example |
|---|---|---|
| **Degenerate** | an identifier kept on the fact, no dimension table | `order_id`, `invoice_no`, `transaction_id` |
| **Junk** | every observed combination of small flags, one key per combination | `is_gift`, `payment_type`, `order_channel` → `order_profile_key` |

```sql
CREATE TABLE dim_order_profile (
  order_profile_key  INT PRIMARY KEY,
  is_gift            BOOLEAN NOT NULL,
  payment_type       TEXT    NOT NULL,   -- 'card' | 'wallet' | 'cod'
  order_channel      TEXT    NOT NULL,   -- 'web' | 'ios' | 'android'
  UNIQUE (is_gift, payment_type, order_channel)
);
```

The degenerate `order_id` is what lets you group order lines back into orders.

### 6-3) Role-playing dimensions

**One physical dimension joined several times under different roles — the same `dim_date`
as order date, ship date and delivery date.**

```sql
SELECT od.year_month           AS order_month,
       sd.year_month           AS ship_month,
       COUNT(*)                AS orders
FROM   fact_order_fulfillment f
JOIN   dim_date od ON od.date_key = f.placed_date_key
JOIN   dim_date sd ON sd.date_key = f.shipped_date_key
GROUP  BY od.year_month, sd.year_month;
```

In a BI tool or semantic layer, expose each role as its own view (`dim_order_date`,
`dim_ship_date`) so users do not have to alias the join themselves.

### 6-4) Outriggers and mini-dimensions

- **Outrigger** — a dimension that references another dimension, such as `dim_customer` with
  a `first_purchase_date_key` into `dim_date`. Fine in moderation; many of them turn a star
  into a snowflake.
- **Mini-dimension** — for attributes that change often on a big dimension (age band, income
  band, loyalty tier). Tracking them with SCD2 on `dim_customer` would add a row per change
  for millions of customers. Instead, put every combination of the banded values in a small
  `dim_customer_demographics` and give the **fact** both keys.

```text
fact_order_line
  customer_key      ──► dim_customer               (slow attributes: name, signup date)
  demographics_key  ──► dim_customer_demographics  (age_band, income_band, loyalty_tier
                                                    as they were when the order happened)
```

### 6-5) Bridge tables for many-to-many

**When one fact row relates to many members of a dimension — an account with several
holders, a patient visit with several diagnoses — a bridge table sits between them, with a
weighting factor so totals do not double-count.**

```text
fact_account_balance ──► dim_account_group ◄── bridge_account_customer ──► dim_customer
                          (account_group_key)    (account_group_key,
                                                  customer_key,
                                                  weight)       -- weights per group sum to 1
```

```sql
-- balance per customer city without double-counting joint accounts
SELECT c.city,
       SUM(f.balance_amount * b.weight) AS weighted_balance
FROM   fact_account_balance f
JOIN   bridge_account_customer b ON b.account_group_key = f.account_group_key
JOIN   dim_customer c            ON c.customer_key      = b.customer_key
WHERE  f.date_key = 20261031
GROUP  BY c.city;
```

Without the weight, a joint account's balance is counted once per holder. Sometimes that
is what the business wants ("impact" reporting), so the weighted and unweighted queries
should both be explained to users.

### 6-6) Hierarchies

| Hierarchy shape | Example | Model it as |
|---|---|---|
| **Fixed depth** | product → subcategory → category | flattened columns in one dimension |
| **Ragged / variable depth** | org chart, bill of materials, account tree | parent-child column, plus a bridge (closure) table for fast rollups |
| **Multiple hierarchies** | calendar vs fiscal year | separate attribute sets in the same dimension |

```sql
-- every employee under a given manager, any depth (parent-child, recursive CTE)
WITH RECURSIVE reports AS (
  SELECT employee_key, manager_key, 1 AS depth
  FROM   dim_employee
  WHERE  manager_key = 42
  UNION ALL
  SELECT e.employee_key, e.manager_key, r.depth + 1
  FROM   dim_employee e
  JOIN   reports r ON e.manager_key = r.employee_key
)
SELECT * FROM reports;
```

A closure table (`ancestor_key, descendant_key, depth`) precomputes every pair, so a rollup
is a plain join; recursive CTEs are fine at query time for small trees.

### 6-7) Unknown members and late-arriving data

- **Unknown / not-applicable members** — reserve rows such as key `-1` = "Unknown" and `-2` =
  "Not applicable" in every dimension, and point facts there instead of `NULL`. Inner joins
  then never drop facts, and BI users see a labelled bucket.
- **Late-arriving dimension** (the fact arrives before its dimension row) — insert an
  **inferred member**: a row with just the natural key and placeholder attributes, flagged
  `is_inferred`. When the real row arrives, overwrite the inferred row in place (type 1), then
  continue SCD2 normally.
- **Late-arriving fact** (the fact arrives days after the event) — look up the dimension
  version that was valid **on the event date**, not the current one (see 7-4).

---

## 7) Slowly Changing Dimensions ⭐⭐⭐⭐⭐

### 7-1) Types 0 to 7

**SCD Type 1 overwrites and keeps no history; Type 2 closes the old row and inserts a new
one, so each fact joins to the version that was true when it happened. Most interviews only
need 1, 2 and 3, and knowing the others exist.**

| Type | Behaviour | History | Use for |
|---|---|---|---|
| **0** | retain original, never change | original only | birth date, original signup channel |
| **1** | overwrite in place | none | corrections, attributes nobody analyses historically |
| **2** | new row with `valid_from` / `valid_to` / `is_current` | full | address, segment, price tier — anything reported "as it was" |
| **3** | add a `previous_<attr>` column | one prior value | a one-off reorganisation (old region vs new region) |
| **4** | current table + separate history table (or a mini-dimension) | full, split | fast-changing attributes |
| **5** | mini-dimension (4) + a type-1 outrigger to the current mini-dimension row | full + current | demographics analysis both "then" and "now" |
| **6** | 1 + 2 + 3: SCD2 rows that also carry a type-1 `current_<attr>` column | full + current on every row | report history by either the historical or the current value |
| **7** | type-2 dimension, and the fact carries both the surrogate key and the durable natural key | full + current via two joins | the same flexibility as 6 without updating history rows |

### 7-2) SCD2 in SQL

**Close the changed current rows, then insert a new version for every changed or new key —
in one transaction, with a NULL-safe comparison.**

```sql
-- PostgreSQL. dim_customer(customer_sk identity PK, customer_id, address, segment,
--                          valid_from, valid_to, is_current)
BEGIN;

UPDATE dim_customer d
SET    valid_to   = CURRENT_DATE - 1,
       is_current = FALSE
FROM   stg_customer s
WHERE  d.customer_id = s.customer_id
  AND  d.is_current
  AND  (d.address, d.segment) IS DISTINCT FROM (s.address, s.segment);  -- NULL-safe

INSERT INTO dim_customer (customer_id, address, segment, valid_from, valid_to, is_current)
SELECT s.customer_id, s.address, s.segment, CURRENT_DATE, DATE '9999-12-31', TRUE
FROM   stg_customer s
LEFT JOIN dim_customer d
       ON d.customer_id = s.customer_id AND d.is_current
WHERE  d.customer_id IS NULL;   -- brand-new customers, and the ones closed just above

COMMIT;
```

- `<>` would miss a change to or from `NULL`, because `NULL <> 'x'` is unknown, not true.
- Comparing many columns: compare a hash of the tracked columns (`hash_diff`) stored on each row.
- `valid_to = CURRENT_DATE - 1` with an inclusive `BETWEEN` join keeps the date ranges from
  overlapping. With timestamps, use a half-open range (`valid_from <= t AND t < valid_to`)
  instead.

### 7-3) SCD2 with `MERGE` on a lakehouse

**On Delta Lake (and Spark SQL generally), SCD2 is one `MERGE` over a staged source that
lists each changed key twice: once to match and close the old row, once with a `NULL` merge
key so it falls through to the insert.**

```sql
-- Spark SQL on Delta Lake. <=> is Spark's NULL-safe equality.
-- dim_customer.customer_sk is GENERATED ALWAYS AS IDENTITY.
MERGE INTO dim_customer AS t
USING (
  SELECT s.customer_id AS merge_key, s.*
  FROM   stg_customer s
  UNION ALL
  SELECT CAST(NULL AS STRING) AS merge_key, s.*      -- the new version of a changed key
  FROM   stg_customer s
  JOIN   dim_customer d
         ON d.customer_id = s.customer_id AND d.is_current
  WHERE  NOT (d.address <=> s.address AND d.segment <=> s.segment)
) AS u
ON  t.customer_id = u.merge_key AND t.is_current
WHEN MATCHED AND NOT (t.address <=> u.address AND t.segment <=> u.segment) THEN
  UPDATE SET valid_to = date_sub(current_date(), 1), is_current = false
WHEN NOT MATCHED THEN
  INSERT (customer_id, address, segment, valid_from, valid_to, is_current)
  VALUES (u.customer_id, u.address, u.segment, current_date(), DATE '9999-12-31', true);
```

The `CAST(NULL AS STRING)` assumes `customer_id` is a string; match the key's type. Because
`MERGE` is a single atomic commit on Delta, there is no half-applied state. In dbt, a
**snapshot** generates the same logic for you (see 9-2).

### 7-4) Joining facts to an SCD2 dimension

**Resolve the surrogate key when the fact is loaded, by the event date; after that, queries
join on the surrogate key alone and automatically see history "as it was".**

```sql
-- at fact load time: pick the version valid on the order date
INSERT INTO fact_order_line (order_line_id, order_id, order_date_key, customer_sk, product_key, quantity, net_amount)
SELECT o.order_line_id,
       o.order_id,
       CAST(TO_CHAR(o.order_date, 'YYYYMMDD') AS INT),
       COALESCE(c.customer_sk, -1),                 -- unknown member, never NULL
       COALESCE(p.product_key, -1),
       o.quantity,
       o.net_amount
FROM   stg_order_lines o
LEFT JOIN dim_customer c
       ON c.customer_id = o.customer_id
      AND o.order_date BETWEEN c.valid_from AND c.valid_to
LEFT JOIN dim_product p
       ON p.product_id = o.product_id AND p.is_current;
```

| Question | Join |
|---|---|
| revenue by the customer's segment **at the time** | `fact.customer_sk = dim.customer_sk` |
| revenue by the customer's segment **today** | join on the durable key to the current row (`dim.customer_id = ...  AND dim.is_current`) — or type 6 / 7 |

---

## 8) Enterprise Approaches: Inmon, Data Vault, One Big Table ⭐⭐⭐

### 8-1) Inmon's corporate information factory

**Inmon builds a normalised (3NF) enterprise data warehouse first, as the single integrated
source of truth, and derives departmental dimensional marts from it.**

```text
Kimball (bottom-up)                         Inmon (top-down)
sources ─► staging ─► conformed stars       sources ─► staging ─► 3NF EDW ─► dimensional marts
                       = the warehouse                              (integrated)  (per department)
```

| | Kimball | Inmon |
|---|---|---|
| First deliverable | one business process's star schema | the integrated enterprise model |
| Time to first value | short | long |
| Integration | through conformed dimensions | through the 3NF core |
| Risk | marts drift if conformance is not enforced | a long project before anyone gets a report |

### 8-2) Data Vault 2.0

**Data Vault splits the model into hubs (business keys), links (relationships between keys)
and satellites (descriptive attributes over time), so a new source or attribute is a new
table, never a change to an existing one. It is an integration layer — you still build stars
on top for reporting.**

| Table | Holds | Changes when |
|---|---|---|
| **Hub** | one row per business key, its hash key, load time, record source | a new key appears |
| **Link** | one row per relationship between hub keys | a new relationship appears |
| **Satellite** | descriptive attributes of a hub or link, one row per change, with a `hash_diff` | an attribute changes |

```sql
-- PostgreSQL. Hash keys are MD5 of the normalised business key (32 hex chars).
CREATE TABLE hub_customer (
  hub_customer_hk  CHAR(32)    PRIMARY KEY,
  customer_bk      TEXT        NOT NULL,          -- business key from the source
  load_dts         TIMESTAMP   NOT NULL,
  record_source    TEXT        NOT NULL
);

CREATE TABLE hub_order (
  hub_order_hk     CHAR(32)    PRIMARY KEY,
  order_bk         TEXT        NOT NULL,
  load_dts         TIMESTAMP   NOT NULL,
  record_source    TEXT        NOT NULL
);

CREATE TABLE link_order_customer (
  link_order_customer_hk  CHAR(32)  PRIMARY KEY,  -- hash of both business keys
  hub_order_hk            CHAR(32)  NOT NULL REFERENCES hub_order,
  hub_customer_hk         CHAR(32)  NOT NULL REFERENCES hub_customer,
  load_dts                TIMESTAMP NOT NULL,
  record_source           TEXT      NOT NULL
);

CREATE TABLE sat_customer_details (
  hub_customer_hk  CHAR(32)    NOT NULL REFERENCES hub_customer,
  load_dts         TIMESTAMP   NOT NULL,
  hash_diff        CHAR(32)    NOT NULL,          -- hash of the attributes, to detect change
  name             TEXT,
  email            TEXT,
  address          TEXT,
  record_source    TEXT        NOT NULL,
  PRIMARY KEY (hub_customer_hk, load_dts)
);
```

- **Insert-only**: every table is appended to; history is the satellites.
- **Parallel loading**: hash keys are computed from business keys, so hubs, links and
  satellites can load at the same time without looking up a sequence.
- **Cost**: many joins to reassemble anything, so Data Vault needs a presentation layer
  (stars, views) and tooling (dbt packages such as AutomateDV) to stay manageable.

### 8-3) One Big Table and the activity schema

- **One Big Table (OBT)** — the fact pre-joined to every dimension attribute a dashboard
  needs, one wide table. Fast and simple on columnar engines, which only read the columns a
  query touches. The costs: dimension changes mean rebuilding the table, history semantics
  are frozen at build time, and the same attribute is duplicated across many OBTs. Build it
  **from** a star, as a serving table, rather than instead of one.
- **Activity schema** — one narrow, time-ordered stream per entity (`customer_stream`:
  `activity_id`, `ts`, `customer`, `activity`, a few feature columns, `revenue_impact`), with
  every business event as an activity type. Questions become "first / last / in-between
  activity" queries over one table. It trades modeling up front for more complex queries.

### 8-4) Which approach, when

| Situation | Reasonable answer |
|---|---|
| A product team needs dashboards this quarter | Kimball stars over a raw / staging layer |
| Dozens of sources, heavy audit, frequent source change | Data Vault in the integration layer, stars on top |
| One very hot dashboard with fixed questions | an OBT built from the stars |
| An existing 3NF enterprise warehouse | Inmon-style: derive marts from it |
| Event-heavy product analytics | an event fact table, possibly an activity schema |

The modern default most interviewers accept: **raw (bronze) → cleaned and conformed
(silver) → dimensional marts and serving tables (gold)**, with the marts in Kimball shape.

---

## 9) Modeling in the Modern Stack ⭐⭐⭐⭐

### 9-1) Where modeling happens in a medallion lakehouse

| Layer | Content | Modeling style |
|---|---|---|
| **Bronze / raw** | source data as received, append-only, plus load metadata | none — keep it replayable |
| **Silver / cleaned** | deduped, typed, conformed keys, one row per entity version | source-aligned 3NF-ish entities, or Data Vault |
| **Gold / marts** | business processes as facts and dimensions, aggregates, OBTs | Kimball dimensional |

The layers are about **trust and reprocessing**, not about technology: a bug in silver is
fixed by rebuilding silver and gold from bronze. How the layers are loaded is
[`batch_pipeline.md`](./batch_pipeline.md)'s.

### 9-2) A dbt project's model layers

**dbt's convention maps onto the same idea: staging models clean one source table each,
intermediate models combine them, and marts expose facts and dimensions.**

```text
models/
  staging/            stg_<source>__<table>.sql    1:1 with a source table: rename, cast, dedupe
  intermediate/       int_<verb>_<entity>.sql      joins and business logic, not exposed
  marts/
    core/             dim_customer.sql, fct_order_line.sql
    finance/          fct_revenue_daily.sql
snapshots/            customers_snapshot.sql        SCD2 of a mutable source table
```

```sql
-- snapshots/customers_snapshot.sql — dbt builds and maintains the SCD2 table
{% snapshot customers_snapshot %}
{{ config(
    target_schema = 'snapshots',
    unique_key    = 'customer_id',
    strategy      = 'timestamp',
    updated_at    = 'updated_at'
) }}
select * from {{ source('app', 'customers') }}
{% endsnapshot %}
```

dbt adds `dbt_valid_from` and `dbt_valid_to` columns; `dbt_valid_to IS NULL` marks the
current row. Tests (`unique`, `not_null`, `relationships`) on the marts enforce the grain and
the foreign keys that the warehouse itself usually does not.

### 9-3) The semantic layer and metric definitions

**A semantic layer defines each metric once — its measure, aggregation, filters and the
dimensions it can be sliced by — so every dashboard, notebook and API computes "revenue" the
same way.**

- Without one, "active users" ends up defined differently in five dashboards.
- Tools: dbt Semantic Layer (MetricFlow), LookML (Looker), Cube, and the metric views of
  several warehouses.
- The model underneath is still a star: the semantic layer knows the entities (join keys),
  the dimensions and the measures, and generates the SQL.

```yaml
# MetricFlow-style definition (the spec has changed across dbt versions — check yours)
semantic_models:
  - name: order_lines
    model: ref('fct_order_line')
    defaults:
      agg_time_dimension: order_date
    entities:
      - name: order_line
        type: primary
        expr: order_line_id
      - name: customer
        type: foreign
        expr: customer_sk
    dimensions:
      - name: order_date
        type: time
        type_params:
          time_granularity: day
    measures:
      - name: net_revenue
        agg: sum
        expr: net_amount

metrics:
  - name: net_revenue
    label: Net revenue
    type: simple
    type_params:
      measure: net_revenue
```

---

## 10) Case Study: An E-commerce Warehouse ⭐⭐⭐⭐⭐

### 10-1) From requirements to a bus matrix

**Interview prompt: "Design the analytics model for an online store." Ask what questions it
must answer, list the business processes, draw the bus matrix, then model one process in
detail.**

Questions the business gave:

1. revenue and units by product category, by day and month;
2. conversion from session to order, by channel;
3. return rate by category;
4. revenue by customer segment, both as it was and as it is now.

```text
Process          Grain                                   Date  Customer  Product  Channel  Promo
Orders           one row per order line                   X       X         X        X       X
Returns          one row per returned order line          X       X         X        X
Web sessions     one row per session                      X       X                  X
Inventory        one row per product per warehouse per day X                X
```

### 10-2) The order-line star

```sql
CREATE TABLE dim_customer (
  customer_sk     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  customer_id     TEXT    NOT NULL,                 -- natural key
  segment         TEXT    NOT NULL,                 -- SCD2-tracked
  country         TEXT    NOT NULL,                 -- SCD2-tracked
  signup_date     DATE    NOT NULL,                 -- type 0
  valid_from      DATE    NOT NULL,
  valid_to        DATE    NOT NULL,
  is_current      BOOLEAN NOT NULL
);

CREATE TABLE dim_product (
  product_key     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  product_id      TEXT    NOT NULL,
  product_name    TEXT    NOT NULL,
  subcategory     TEXT    NOT NULL,                 -- flattened fixed-depth hierarchy
  category        TEXT    NOT NULL,
  brand           TEXT    NOT NULL,
  is_current      BOOLEAN NOT NULL
);

CREATE TABLE fact_order_line (
  order_line_id      BIGINT       PRIMARY KEY,
  order_id           BIGINT       NOT NULL,         -- degenerate dimension
  order_date_key     INT          NOT NULL REFERENCES dim_date (date_key),
  customer_sk        BIGINT       NOT NULL REFERENCES dim_customer (customer_sk),
  product_key        BIGINT       NOT NULL REFERENCES dim_product (product_key),
  channel_key        INT          NOT NULL,
  promotion_key      INT          NOT NULL,         -- -2 = "no promotion"
  quantity           INT          NOT NULL,
  gross_amount       NUMERIC(12, 2) NOT NULL,
  discount_amount    NUMERIC(12, 2) NOT NULL,
  net_amount         NUMERIC(12, 2) NOT NULL        -- additive
);
```

Design decisions worth saying out loud:

- grain is the **order line**, so product is a valid dimension; order-level shipping is
  allocated to lines by value, or kept in an order-level fact;
- `unit_price` is not stored as a fact (non-additive); it is `net_amount / quantity` when needed;
- customer is SCD2, so "segment at the time" is a plain join, and "segment now" goes through
  `customer_id` to the current row;
- every foreign key is `NOT NULL`, with unknown and not-applicable members instead of `NULL`.

### 10-3) Answering the questions

```sql
-- 1. revenue by category by month
SELECT d.year_month, p.category, SUM(f.net_amount) AS net_revenue, SUM(f.quantity) AS units
FROM   fact_order_line f
JOIN   dim_date d    ON d.date_key    = f.order_date_key
JOIN   dim_product p ON p.product_key = f.product_key
GROUP  BY d.year_month, p.category;

-- 4a. revenue by segment as it was when each order happened
SELECT c.segment, SUM(f.net_amount) AS net_revenue
FROM   fact_order_line f
JOIN   dim_customer c ON c.customer_sk = f.customer_sk
GROUP  BY c.segment;

-- 4b. revenue by segment as it is today
SELECT cur.segment, SUM(f.net_amount) AS net_revenue
FROM   fact_order_line f
JOIN   dim_customer hist ON hist.customer_sk = f.customer_sk
JOIN   dim_customer cur  ON cur.customer_id  = hist.customer_id AND cur.is_current
GROUP  BY cur.segment;
```

Question 3 is the drill-across query in 4-5. Question 2 needs the session fact: count
sessions and sessions with an order per channel, each from its own fact, then divide.

---

## 11) Design Questions and How to Answer Them ⭐⭐⭐⭐⭐

### 11-1) A repeatable approach for "model X"

**Clarify the questions, name the processes, state the grain, then dimensions, facts, SCD
choices, and finally physical concerns — out loud, in that order.**

1. **Clarify**: who uses it, which questions, how fresh, how much history, what volume.
2. **Processes**: list the events the source systems produce; draw a bus matrix.
3. **Grain**: one sentence per fact table, atomic wherever possible.
4. **Dimensions**: conformed across processes; decide SCD type per attribute.
5. **Facts**: additive where possible; note semi- and non-additive ones and how to aggregate.
6. **Edge cases**: late data, unknown members, many-to-many, hierarchies, deletes.
7. **Physical**: partitioning, clustering, load pattern — handed to
   [`schema_design.md`](./schema_design.md) and [`batch_pipeline.md`](./batch_pipeline.md).

### 11-2) Prompts that come up

| Prompt | The key modeling call |
|---|---|
| Ride-sharing (trips) | grain one row per completed trip; driver and rider as SCD2; pickup and dropoff as a role-playing location dimension |
| Subscription business | periodic snapshot of subscription state per month for MRR; transaction fact for plan changes |
| Music or video streaming | play events as a transaction fact; a factless "content available in region" coverage table |
| Banking | daily balance snapshot (semi-additive); bridge table for joint accounts |
| Ads | impression and click facts at different grains, joined only through drill-across |
| Order fulfilment SLA | accumulating snapshot with a date key per milestone and lag facts |
| Social network | users and follows as an edge table; graph questions may justify a graph store (see [`sql_vs_nosql.md`](./sql_vs_nosql.md)) |

---

## 12) Quick Q&A ⭐⭐⭐⭐

- **Q: Star or snowflake?** — Star by default: fewer joins, simpler for BI users, and on a
  columnar store the redundancy compresses away. Snowflake only for a very large shared
  sub-dimension or a tool that needs it.
- **Q: Why surrogate keys instead of the source id?** — They make SCD2 possible (several rows
  per natural key), insulate the warehouse from source key changes and collisions between
  sources, and give unknown members a key.
- **Q: Is price a fact or a dimension attribute?** — The amount paid on a line is a fact.
  A list price that describes the product is a dimension attribute (often SCD2). Unit price
  on the line is non-additive: store the extended amount.
- **Q: What is a fan trap?** — Joining a one-to-many relationship and then summing a measure
  from the "one" side, which repeats it per child row. Aggregate at the right grain first.
- **Q: What is a chasm trap?** — Two one-to-many facts joined through a shared dimension,
  multiplying each other's rows. Drill across instead.
- **Q: SCD2 or a daily snapshot of the whole dimension?** — SCD2 stores only changes and is
  compact; full daily snapshots are trivial to build and query on cheap storage, but grow
  with rows × days. Small dimensions can afford snapshots; large ones want SCD2.
- **Q: How do you handle a fact whose dimension row has not arrived?** — Insert an inferred
  member with the natural key, point the fact at it, and update it when the real data lands.
- **Q: How do you model deletes in the source?** — Do not physically delete history in the
  warehouse: close the SCD2 row and set an `is_deleted` flag; CDC is what makes deletes
  visible in the first place.
- **Q: When is One Big Table the right answer?** — When a fixed set of questions on a
  columnar engine needs the lowest latency, and it is built from a star you can rebuild it from.
- **Q: Normalised or dimensional in the warehouse?** — Both, in different layers: normalised
  or Data Vault for integration, dimensional for consumption.
