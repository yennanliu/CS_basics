# Analytics for Data Engineers FAQ

> **Scope** — the analytics knowledge a data engineering interview expects, and how to talk about analytics experience: what "analytics experience" questions probe, defining metrics precisely, ratio metrics and the averaging trap, the common product metrics, building metric tables people trust (additive vs non-additive measures, sketches, one definition for every consumer), A/B testing from the data engineer's side (assignment, exposure logging, sample ratio mismatch, reading a result), root-causing a metric change, and serving dashboards fast. The SQL for cohorts, funnels and sessions is [`sql_for_data.md`](./sql_for_data.md)'s; star schemas and the semantic layer are [`data_modeling.md`](./data_modeling.md)'s.
> **See also**: [`./sql_for_data.md`](./sql_for_data.md) — retention, funnel, sessionisation and percentile queries; [`./data_modeling.md`](./data_modeling.md) — grain, facts and metric definitions in the semantic layer; [`./data_performance.md`](./data_performance.md) — making the dashboard queries fast; [`./batch_pipeline.md`](./batch_pipeline.md) — data quality checks on the tables behind the metrics.

A data engineer is rarely asked to run a regression in the interview, but is almost always
asked whether the numbers their pipelines produce can be trusted, what a metric means, and
what they would do when one moves. This file is the vocabulary and the reasoning behind those
answers.

---

## 0) The Short Answer ⭐⭐⭐⭐⭐

**"Analytics, for me, is turning events into numbers people make decisions on. That means a
precise definition for every metric — numerator, denominator, unit, time window and filters —
tables built at the right grain so the number is computed one way everywhere, data quality
checks so a pipeline bug never looks like a business change, and, when a metric moves, a
fixed order of checks: is the data right, is it external, is it a release or an experiment,
and which segment moved."**

---

## 1) What "Analytics Experience" Means in a DE Interview ⭐⭐⭐⭐⭐

### 1-1) What the interviewer is probing

**Behind "tell me about your analytics experience" are four questions: do you understand
the business numbers, can you define them precisely, can you build tables that compute them
correctly, and can you reason about a change without guessing.**

| What they ask | What they are checking | Where it is answered |
|---|---|---|
| "What metrics did your team track?" | you know why each metric exists, not just its name | [§2](#2-metrics-) |
| "How was DAU defined?" | precision: timezone, dedup, bots, which events count | [§2-2](#2-2-defining-a-metric-precisely) |
| "How did you build the reporting tables?" | grain, additivity, incremental builds | [§3](#3-building-metric-tables-people-trust-) |
| "Have you supported experiments?" | assignment and exposure data, SRM | [§4](#4-ab-testing-for-data-engineers-) |
| "Revenue dropped 10% yesterday — what do you do?" | a method, starting with "is the data right?" | [§5](#5-root-causing-a-metric-change-) |
| "Dashboards are slow" | serving layer and pre-aggregation | [§6](#6-dashboards-and-the-serving-layer-) |

### 1-2) Telling an analytics story

**Use STAR, but make the Result a number and make the Action about the data: which
definition you fixed, which table you built, which check you added.**

```text
Situation  Marketing and finance reported different "new customer" counts for the same month
           (12.4k vs 10.9k), and both numbers were in board slides.
Task       Own one definition and make every dashboard use it.
Action     - Traced both: marketing counted sign-ups; finance counted first paid orders,
             and excluded refunds within 14 days.
           - Agreed a written definition with both teams: first paid order, net of refunds.
           - Built fct_first_order at one row per customer, incremental by order date,
             and moved both dashboards onto it.
           - Added a daily reconciliation check against the billing system (alert at > 0.5%).
Result     One number in every report; the monthly reconciliation meeting was cancelled;
           the check caught a duplicated payment-webhook load two months later.
```

What makes it land:

- The **conflict was in the definition**, not the SQL — that is the usual root cause.
- There is a **number** in the Situation and in the Result.
- The Action names a **table, a grain and a check** — data engineering, not slide-making.

---

## 2) Metrics ⭐⭐⭐⭐⭐

### 2-1) Kinds of metric

**Every metric belongs to a role: a north-star metric says whether the product is winning,
driver (input) metrics are the levers that move it, and guardrail metrics make sure a win
on one does not quietly break something else.**

| Role | Example (a marketplace) | Used for |
|---|---|---|
| **North star** | weekly completed orders | long-term direction |
| **Driver / input** | search-to-order conversion, sellers with stock, delivery time | what a team can move |
| **Guardrail** | refund rate, page latency, support tickets per order | stop a "win" that harms users |
| **Diagnostic** | events per session, crash rate by app version | explaining a change |

And by shape, which decides how it is aggregated:

| Shape | Example | Aggregates by |
|---|---|---|
| **Count / sum** | orders, revenue | adding — fully additive |
| **Distinct count** | daily active users | recomputing or merging sketches — **not** additive |
| **Ratio** | conversion rate, average order value | dividing **sums**, never averaging ratios |
| **Distribution** | p50 / p95 delivery time | recomputing from detail or merging sketches |

### 2-2) Defining a metric precisely

**A metric is not defined until someone else could compute exactly the same number from the
definition alone; most "the numbers don't match" incidents are two definitions, not a bug.**

The checklist every definition answers:

| Element | Question | Example for DAU |
|---|---|---|
| **Entity** | what is counted? | distinct `user_id` (logged-in) — not devices |
| **Qualifying event** | which actions count? | any foreground app event; push receipts do **not** count |
| **Time window** | which day? | calendar day in **UTC** (or the business's reporting timezone — state which) |
| **Filters** | who is excluded? | internal accounts, test users, known bots |
| **Deduplication** | what is one? | a user counted once per day across platforms |
| **Late data** | when is the number final? | provisional at 06:00, final after 3 days of late events |
| **Owner** | who decides changes? | product analytics, versioned in the metrics layer |

```sql
-- DAU, written so the definition is visible in the query
SELECT
  DATE(event_ts)              AS activity_date,      -- event_ts stored in UTC
  COUNT(DISTINCT user_id)     AS dau
FROM   events
WHERE  is_foreground = TRUE                                     -- foreground app events only
  AND  user_id IS NOT NULL                                       -- logged-in users only
  AND  NOT EXISTS (SELECT 1 FROM internal_users i                -- staff and test accounts
                   WHERE i.user_id = events.user_id)             -- (NOT IN breaks on a NULL id)
  AND  NOT EXISTS (SELECT 1 FROM known_bots b                    -- known bots
                   WHERE b.user_id = events.user_id)
GROUP  BY DATE(event_ts);
```

### 2-3) Ratio metrics and the averaging trap

**A ratio across groups is the sum of numerators over the sum of denominators; the average of
each group's ratio weights a tiny group the same as a huge one and gives a different, usually
wrong, answer.**

```text
                orders   visits   conversion
Mobile             900   30,000       3.0%
Desktop            100      500      20.0%

Average of ratios:  (3.0% + 20.0%) / 2      = 11.5%   ← wrong
Ratio of sums:      (900 + 100) / 30,500    =  3.3%   ← the site's conversion rate
```

```sql
-- Store the numerator and denominator; divide only at the end
SELECT
  SUM(orders) * 1.0 / NULLIF(SUM(visits), 0) AS conversion_rate
FROM   daily_platform_stats
WHERE  stat_date BETWEEN DATE '2026-11-01' AND DATE '2026-11-07';
```

- Tables should store **numerators and denominators**, not precomputed rates, so any rollup
  stays correct — as long as both are **additive** across the rollup. A ratio of distinct
  counts (stickiness, users who converted ÷ active users over a week) needs each count
  recomputed at the target grain first (see [§3-2](#3-2-non-additive-metrics-distinct-users-across-days)).
- **Simpson's paradox**: a rate can rise in every segment while falling overall, because the
  **mix** shifted towards a lower-rate segment — always check the mix when a ratio moves (see
  [§5-2](#5-2-decomposing-the-change-rate-vs-mix)).

### 2-4) The common product metrics

| Metric | Definition | Watch out for |
|---|---|---|
| **DAU / WAU / MAU** | distinct active users in a day / 7 days / 28–30 days | MAU is not the sum of DAUs; window must be fixed |
| **Stickiness** | DAU ÷ MAU | a ratio of two distinct counts — compute each, then divide |
| **N-day retention** | of users who started on day 0, the share active on day N | "on day N" (classic) vs "on or after day N" (unbounded) give different curves |
| **Churn** | share of last period's active users not active this period | define "active" and the period first |
| **Conversion rate** | users (or sessions) completing the goal ÷ users entering the step | user-level vs session-level counts differ |
| **ARPU** | revenue ÷ active users in the period | which users are in the denominator? |
| **AOV** | revenue ÷ orders | heavily skewed — report the median alongside |
| **LTV** | expected revenue per user over their lifetime | a model, not a count — state the horizon and the assumptions |

The SQL for retention cohorts and funnels is
[`./sql_for_data.md` §7-8](./sql_for_data.md#7-8-retention-cohorts) and
[§7-9](./sql_for_data.md#7-9-funnels).

---

## 3) Building Metric Tables People Trust ⭐⭐⭐⭐⭐

### 3-1) The layers from events to metrics

**Metrics are computed from facts at a declared grain, and rolled up only along paths that
are mathematically safe — so the same number comes out whichever dashboard asks.**

```text
raw events ──► cleaned events ──► facts at a grain ──► daily aggregates ──► metrics layer ──► dashboards
 (bronze)        (silver)          fct_orders           agg_daily_country    one definition     BI, notebooks
                                   one row per order    one row per          per metric
                                                        day × country
```

- Declare each table's **grain** in its name or docs: one row per order, per user per day.
- Store **additive** measures (counts, sums) and the **parts** of ratios in aggregates.
- Build incrementally by date partition, so late data reprocesses only the affected days.

### 3-2) Non-additive metrics: distinct users across days

**Distinct counts do not add up: the users active this week is not the sum of each day's
DAU, because the same user is counted once per day. Recompute from detail, or store a
mergeable sketch per day.**

```sql
-- Wrong: sums daily distinct counts — a user active on 5 days counts 5 times
SELECT SUM(dau) AS wau_wrong
FROM   agg_daily_active
WHERE  activity_date BETWEEN DATE '2026-11-01' AND DATE '2026-11-07';

-- Exact: recompute from the detail table
SELECT COUNT(DISTINCT user_id) AS wau
FROM   fct_user_activity_daily
WHERE  activity_date BETWEEN DATE '2026-11-01' AND DATE '2026-11-07';

-- Approximate and cheap (BigQuery): store a HyperLogLog++ sketch per day, merge any range
CREATE TABLE agg_daily_active_sketch AS
SELECT activity_date, HLL_COUNT.INIT(user_id) AS users_sketch
FROM   fct_user_activity_daily
GROUP  BY activity_date;

SELECT HLL_COUNT.MERGE(users_sketch) AS wau_approx
FROM   agg_daily_active_sketch
WHERE  activity_date BETWEEN DATE '2026-11-01' AND DATE '2026-11-07';
```

The same holds for medians and percentiles: a median of daily medians is not the week's
median. Keep the detail, or a mergeable quantile sketch (t-digest, KLL).

### 3-3) One definition, many consumers

**When every dashboard writes its own SQL for "revenue", the numbers drift; define each
metric once — in a semantic layer, dbt metrics or a governed view — and make dashboards read
it.**

- Version metric definitions; a definition change is announced and dated, and history is
  either restated or annotated.
- Name the **owner** of each metric — the person who decides an edge case.
- Keep the definition next to the code (YAML in the metrics layer, a doc string on the view),
  not in a slide.

The modelling side of this — conformed dimensions and the semantic layer — is
[`./data_modeling.md` §9-3](./data_modeling.md#9-3-the-semantic-layer-and-metric-definitions).

### 3-4) Data quality checks for metrics

**A pipeline bug must never be reported as a business change, so the tables behind a metric
carry checks that fail or warn before the dashboard updates.**

| Check | Catches |
|---|---|
| **Freshness** — latest partition loaded by 06:00 | a stuck job reported as "zero sales" |
| **Volume** — today's row count within the usual range for this weekday | a partial load, a duplicated load |
| **Uniqueness** of the grain key | a fan-out join doubling revenue |
| **Reconciliation** with the system of record (billing, ledger) | silent drops or double counting |
| **Distribution** — share of `NULL` / `unknown` per dimension | a broken upstream field |

The patterns for writing these checks as SQL are
[`./sql_for_data.md` §7-16](./sql_for_data.md#7-16-data-quality-checks-as-sql-assertions-).

---

## 4) A/B Testing for Data Engineers ⭐⭐⭐⭐

### 4-1) The data path of an experiment

**An experiment is a data pipeline: assignment decides the variant, exposure logging records
who actually saw it, the metric tables compute outcomes per user, and the analysis compares
the groups — and most broken experiments are broken in the first two steps, not the
statistics.**

```text
assignment ──► exposure log ──► join to metric facts ──► per-user outcomes ──► analysis
 hash(user,     user saw the      by user_id, from        one row per user      lift, CI,
 experiment)    variant at ts     exposure time onward    per variant           SRM check
```

### 4-2) What the data engineer owns

**Deterministic assignment, a reliable exposure log, and metrics computed per randomisation
unit from the moment of exposure.**

- **Assignment** is a hash of the user and the experiment, so it is stable across sessions and
  needs no lookup table:

```python
import hashlib

def assign_variant(user_id: str, experiment: str, treatment_share: float = 0.5) -> str:
    """Same user + same experiment always gives the same variant."""
    key = f"{experiment}:{user_id}".encode("utf-8")
    bucket = int(hashlib.sha256(key).hexdigest()[:8], 16) % 10_000   # 0..9999
    return "treatment" if bucket < treatment_share * 10_000 else "control"

print(assign_variant("user_42", "checkout_v2"))   # stable on every call
```

- Including the experiment name in the hash keeps different experiments **independent** —
  otherwise the same users are always in treatment together.
- **Assignment first, exposure with care**: the safe overall comparison is everyone
  *assigned*. Restricting to users who reached the changed screen removes the dilution from
  users who never saw it, but is unbiased only if the trigger is the same in both groups —
  log the trigger in control too (counterfactual logging) and check that its rate matches
  across variants. If the variant changes who reaches the screen, analyse by assignment.
- **Randomisation unit = analysis unit**: randomise by user, then compute one value per user.
  Treating each page view as independent when users were randomised overstates significance.
- Metrics start at a **common start time** for everyone in the analysis: **assignment time**
  for an assignment analysis — every assigned user, including those never exposed, who count
  with zero activity — or the **trigger time** (logged in both groups) for a triggered one.
  Events before that cannot have been caused by the variant.

### 4-3) Sample ratio mismatch

**If the split was 50/50 and the groups are 50.8/49.2 on a large sample, something is broken —
assignment, logging, a redirect or a bot filter — and the result cannot be trusted until it
is explained.**

```python
def srm_chi_square(control: int, treatment: int, expected_treatment_share: float = 0.5) -> float:
    """Chi-square statistic for observed vs expected group sizes (1 degree of freedom)."""
    total = control + treatment
    expected_treatment = total * expected_treatment_share
    expected_control = total - expected_treatment
    return ((control - expected_control) ** 2 / expected_control
            + (treatment - expected_treatment) ** 2 / expected_treatment)

stat = srm_chi_square(control=50_800, treatment=49_200)
print(round(stat, 1))   # 25.6
# Critical values at 1 df: 3.84 (p = 0.05), 10.83 (p = 0.001).
# Teams flag SRM at a strict threshold such as p < 0.001 — 25.6 is far past it.
```

Common causes: one variant crashes or loads slower so fewer exposures are logged; a bot filter
removes more users from one side; exposure logged only on one variant's code path.

### 4-4) Reading the result

**Report the effect with a confidence interval, not only a p-value; decide the sample size
before starting; and do not stop the test the first day it looks significant.**

```python
from math import erf, sqrt

def two_proportion_z_test(conv_a: int, n_a: int, conv_b: int, n_b: int) -> tuple:
    """Return (absolute lift, z, two-sided p-value) for conversion B vs A."""
    p_a, p_b = conv_a / n_a, conv_b / n_b
    pooled = (conv_a + conv_b) / (n_a + n_b)
    standard_error = sqrt(pooled * (1 - pooled) * (1 / n_a + 1 / n_b))
    z = (p_b - p_a) / standard_error
    p_value = 2 * (1 - 0.5 * (1 + erf(abs(z) / sqrt(2))))
    return p_b - p_a, z, p_value

lift, z, p = two_proportion_z_test(conv_a=1_000, n_a=20_000, conv_b=1_100, n_b=20_000)
print(f"lift={lift:.4f} z={z:.2f} p={p:.3f}")   # lift=0.0050 z=2.24 p=0.025
```

| Concept | One line |
|---|---|
| **p-value** | how surprising the data would be if there were no real difference |
| **Confidence interval** | the range of effects consistent with the data — the thing to report |
| **Power / MDE** | the smallest effect the test can reliably detect at its sample size; fix it up front |
| **Peeking** | checking daily and stopping at the first p < 0.05 inflates false positives |
| **Multiple comparisons** | 20 metrics at p < 0.05 will show about one false "win" |
| **Novelty effect** | users react to anything new; early lifts often fade |
| **Network effects** | in a marketplace or social graph, treatment users affect control users — randomise by cluster or region |

---

## 5) Root-Causing a Metric Change ⭐⭐⭐⭐⭐

### 5-1) The order to check

**"Revenue dropped 10% yesterday": first prove the data is right, then rule out the calendar,
then look for something you shipped, then find which segment moved — and only then form a
hypothesis about users.**

| Step | Question | How |
|---|---|---|
| 1 | **Is the data right?** | freshness and volume checks; did a job fail, run twice or load partially? compare with the source system |
| 2 | **Did the definition change?** | metric or code change, a new filter, a timezone switch |
| 3 | **Is it the calendar?** | same weekday last week, holiday, payday, season |
| 4 | **Is it external?** | outage at a payment provider, a competitor's sale, news |
| 5 | **Is it us?** | releases, experiments ramped, marketing campaigns ended, pricing changes |
| 6 | **Which segment?** | split by platform, app version, country, channel, new vs returning |
| 7 | **Which part of the metric?** | decompose: revenue = users × conversion × orders per buyer × AOV |

The trap interviewers wait for: jumping straight to step 6 with a story about user behaviour
when the cause was a half-loaded partition.

### 5-2) Decomposing the change: rate vs mix

**A total moves because a segment's rate changed, or because the mix of segments changed —
split the change into both before blaming any one segment.**

```sql
-- Conversion by platform, this week vs last week: did a rate move, or the mix?
WITH by_platform AS (
  SELECT
    platform,
    SUM(CASE WHEN week = 'this' THEN visits ELSE 0 END) AS visits_this,  -- 0, not NULL, when a
    SUM(CASE WHEN week = 'last' THEN visits ELSE 0 END) AS visits_last,  -- platform is new or gone
    SUM(CASE WHEN week = 'this' THEN orders ELSE 0 END) AS orders_this,
    SUM(CASE WHEN week = 'last' THEN orders ELSE 0 END) AS orders_last
  FROM weekly_platform_stats
  GROUP BY platform
)
SELECT
  platform,
  orders_last * 1.0 / NULLIF(visits_last, 0)                       AS rate_last,
  orders_this * 1.0 / NULLIF(visits_this, 0)                       AS rate_this,
  visits_last * 1.0 / NULLIF(SUM(visits_last) OVER (), 0)          AS mix_last,
  visits_this * 1.0 / NULLIF(SUM(visits_this) OVER (), 0)          AS mix_this
FROM by_platform
ORDER BY platform;
```

Reading it:

- **Rates flat, mix shifted** (say, a campaign brought many low-converting mobile visitors) —
  the total fell with no segment getting worse. Nothing is broken.
- **One rate fell, mix flat** — look at that segment: a release on that platform, a broken
  payment method in that country.

---

## 6) Dashboards and the Serving Layer ⭐⭐⭐⭐

### 6-1) Where dashboards read from

**Match the store to the freshness and concurrency the dashboard needs: a warehouse for
internal daily reporting, an extract or cache for many viewers, and a real-time OLAP store
for sub-second, user-facing analytics.**

| Option | Latency / freshness | Concurrency | Fits |
|---|---|---|---|
| **BI directly on the warehouse** (BigQuery, Snowflake, Redshift) | seconds; as fresh as the last load | tens of users | internal daily reporting |
| **BI extracts / cache** | fast; stale until refresh | hundreds | executive dashboards |
| **Pre-aggregated tables** in the warehouse | fast; per-load freshness | hundreds | the standard fix for a slow dashboard |
| **Real-time OLAP** (Druid, Pinot, ClickHouse) | sub-second; seconds of freshness | thousands | user-facing analytics, live operations |
| **Key-value cache** (Redis) of computed results | milliseconds | very high | a fixed set of numbers in a product |

### 6-2) Making dashboards fast and cheap

- Point each dashboard at a **pre-aggregated table at the grain it displays** — daily ×
  country, not raw events.
- **Partition by date** and make the dashboard's date filter required, so every query prunes.
- Refresh **incrementally** — today's partition plus the days late data can still change
  (three days, for the DAU definition in [§2-2](#2-2-defining-a-metric-precisely)), not the
  whole history.
- Use **approximate distinct counts** or sketches for user counts across arbitrary ranges.
- Watch the most expensive dashboard queries (warehouse query history) — a few tiles usually
  account for most of the cost.

The engine-side levers are [`./data_performance.md` §3](./data_performance.md#3-optimising-processing-).

---

## 7) The Analytical SQL Toolkit ⭐⭐⭐⭐

**The analytics questions in a DE interview are usually a handful of SQL patterns; each is
worked out in full in [`sql_for_data.md`](./sql_for_data.md).**

| Business question | Pattern | Where |
|---|---|---|
| "Top 3 products per category" | `ROW_NUMBER()` per group | [§7-1](./sql_for_data.md#7-1-top-n-per-group) |
| "Running revenue this month" | window `SUM` with a frame | [§7-3](./sql_for_data.md#7-3-running-totals-and-moving-averages) |
| "Month-over-month growth" | `LAG()` over months | [§7-4](./sql_for_data.md#7-4-month-over-month-and-year-over-year-growth) |
| "Longest login streak" | gaps and islands | [§7-5](./sql_for_data.md#7-5-gaps-and-islands-consecutive-streaks-) |
| "Sessions from raw clicks" | sessionisation with a 30-minute gap | [§7-7](./sql_for_data.md#7-7-sessionisation) |
| "Weekly retention by signup cohort" | cohort × period matrix | [§7-8](./sql_for_data.md#7-8-retention-cohorts) |
| "Where do users drop off?" | funnel with ordered steps | [§7-9](./sql_for_data.md#7-9-funnels) |
| "Median delivery time" | `PERCENTILE_CONT` | [§7-10](./sql_for_data.md#7-10-median-and-percentiles) |

---

## 8) Quick Q → A ⭐⭐⭐⭐

| Question | Answer |
|---|---|
| Can you sum daily active users to get weekly active users? | no — distinct counts are not additive; recompute or merge sketches |
| Average of conversion rates across countries? | no — divide the summed numerators by the summed denominators |
| Rate up in every segment, down overall — how? | mix shift (Simpson's paradox): traffic moved towards lower-rate segments |
| Metric dropped — first step? | check the data: freshness, volume, failed or duplicated loads |
| What does a data engineer own in an A/B test? | deterministic assignment, exposure logging, per-user metric tables, the SRM check |
| What is sample ratio mismatch? | group sizes that differ from the planned split by more than chance — a sign of broken assignment or logging |
| Why include the experiment name in the assignment hash? | so assignments to different experiments are independent |
| Why not stop a test as soon as p < 0.05? | peeking inflates false positives; fix the sample size up front |
| How do you stop dashboards disagreeing? | one governed definition per metric, with an owner, read by every dashboard |
| How do you make a slow dashboard fast? | a pre-aggregated table at its grain, partitioned by date, refreshed incrementally |
| North star vs guardrail? | the north star says whether you are winning; guardrails catch a win that harms something else |
