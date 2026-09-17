# 繁體中文 FAQs — Translation Progress

The interview FAQs under [`doc/faq/`](./faq/) are the only markdown tree.
A translation is a *sparse overlay* of translated sections in
`i18n/zh/faq/<id>.md`, and the site composes the two into a full Chinese
document at build time — see the *Traditional Chinese docs* section of
[CLAUDE.md](../CLAUDE.md).

**This file is generated. Do not edit it by hand:**

```bash
node script/zh.js status --write
```

## How a translation is stored

Two fifths of these documents is fenced code — Java, SQL, shell, config — and it
must read identically in both languages, so it is never stored twice:

```text
doc/faq/<id>.md
   │  every fence lifts out to a one-line <!--CODE--> marker
   │  the prose is cut into sections at each heading
   ▼
i18n/zh/faq/<id>.md
   │  <!-- hash --> + the translated section
   │  compose — English structure, translated prose, original code
   ▼
_site/faqs/<page>.zh.html
```

Each section is keyed by a hash of **its English text**. Edit one section of an
English document and only that section's translation goes missing; the rest of
the file stays current. A section with no entry falls back to English, so a
half-translated document renders with English gaps rather than failing.

## Known limitations

- **API, class and command names stay in English** — `ConcurrentHashMap`,
  `SELECT ... FOR UPDATE`, `kafka-topics.sh`. They are the strings you type and
  the terms an interviewer will use.
- **A few FAQs were written in Chinese to begin with**
  ([`後端面試題總整理.md`](./faq/backend/後端面試題總整理.md) among them). Their
  sections still need an entry each to count as translated, and that entry is
  usually the text already there.


## Status — 902 / 902 sections (100%)

| FAQ | Sections | 繁體中文 |
|---|---:|:---:|
| [backend/api_design](./faq/backend/api_design.md) | 14 | [✅](../i18n/zh/faq/backend/api_design.md) |
| [backend/authentication](./faq/backend/authentication.md) | 17 | [✅](../i18n/zh/faq/backend/authentication.md) |
| [backend/be_programming_notes](./faq/backend/be_programming_notes.md) | 36 | [✅](../i18n/zh/faq/backend/be_programming_notes.md) |
| [backend/be_programming_notes_pt2](./faq/backend/be_programming_notes_pt2.md) | 46 | [✅](../i18n/zh/faq/backend/be_programming_notes_pt2.md) |
| [backend/be_programming_notes_pt3](./faq/backend/be_programming_notes_pt3.md) | 33 | [✅](../i18n/zh/faq/backend/be_programming_notes_pt3.md) |
| [backend/db_isolation_demo_mysql](./faq/backend/db_isolation_demo_mysql.md) | 14 | [✅](../i18n/zh/faq/backend/db_isolation_demo_mysql.md) |
| [backend/llm_tool_idempotency](./faq/backend/llm_tool_idempotency.md) | 27 | [✅](../i18n/zh/faq/backend/llm_tool_idempotency.md) |
| [backend/overbooking_prevention](./faq/backend/overbooking_prevention.md) | 9 | [✅](../i18n/zh/faq/backend/overbooking_prevention.md) |
| [backend/web_long_connections](./faq/backend/web_long_connections.md) | 13 | [✅](../i18n/zh/faq/backend/web_long_connections.md) |
| [backend/後端面試題總整理](./faq/backend/後端面試題總整理.md) | 88 | [✅](../i18n/zh/faq/backend/後端面試題總整理.md) |
| [cs_basic](./faq/cs_basic.md) | 38 | [✅](../i18n/zh/faq/cs_basic.md) |
| [db/faq_DB_DW](./faq/db/faq_DB_DW.md) | 19 | [✅](../i18n/zh/faq/db/faq_DB_DW.md) |
| [db/faq_redshift](./faq/db/faq_redshift.md) | 1 | [✅](../i18n/zh/faq/db/faq_redshift.md) |
| [db/postgre](./faq/db/postgre.md) | 15 | [✅](../i18n/zh/faq/db/postgre.md) |
| [faq_Airflow](./faq/faq_Airflow.md) | 1 | [✅](../i18n/zh/faq/faq_Airflow.md) |
| [faq_DE](./faq/faq_DE.md) | 4 | [✅](../i18n/zh/faq/faq_DE.md) |
| [faq_ML](./faq/faq_ML.md) | 2 | [✅](../i18n/zh/faq/faq_ML.md) |
| [faq_data_model](./faq/faq_data_model.md) | 15 | [✅](../i18n/zh/faq/faq_data_model.md) |
| [faq_dev_interview](./faq/faq_dev_interview.md) | 30 | [✅](../i18n/zh/faq/faq_dev_interview.md) |
| [faq_devops](./faq/faq_devops.md) | 76 | [✅](../i18n/zh/faq/faq_devops.md) |
| [faq_performance_tune](./faq/faq_performance_tune.md) | 27 | [✅](../i18n/zh/faq/faq_performance_tune.md) |
| [faq_python](./faq/faq_python.md) | 46 | [✅](../i18n/zh/faq/faq_python.md) |
| [faq_python_concurrency](./faq/faq_python_concurrency.md) | 15 | [✅](../i18n/zh/faq/faq_python_concurrency.md) |
| [faq_software_runtime](./faq/faq_software_runtime.md) | 7 | [✅](../i18n/zh/faq/faq_software_runtime.md) |
| [flink/faq_flink](./faq/flink/faq_flink.md) | 26 | [✅](../i18n/zh/faq/flink/faq_flink.md) |
| [java/cqrs](./faq/java/cqrs.md) | 3 | [✅](../i18n/zh/faq/java/cqrs.md) |
| [java/faq_OOP](./faq/java/faq_OOP.md) | 16 | [✅](../i18n/zh/faq/java/faq_OOP.md) |
| [java/java_basic](./faq/java/java_basic.md) | 21 | [✅](../i18n/zh/faq/java/java_basic.md) |
| [java/java_collection](./faq/java/java_collection.md) | 10 | [✅](../i18n/zh/faq/java/java_collection.md) |
| [java/java_design_pattern](./faq/java/java_design_pattern.md) | 13 | [✅](../i18n/zh/faq/java/java_design_pattern.md) |
| [java/java_exception](./faq/java/java_exception.md) | 12 | [✅](../i18n/zh/faq/java/java_exception.md) |
| [java/java_functional](./faq/java/java_functional.md) | 14 | [✅](../i18n/zh/faq/java/java_functional.md) |
| [java/java_generics](./faq/java/java_generics.md) | 12 | [✅](../i18n/zh/faq/java/java_generics.md) |
| [java/java_modern](./faq/java/java_modern.md) | 12 | [✅](../i18n/zh/faq/java/java_modern.md) |
| [java/java_multi_thread](./faq/java/java_multi_thread.md) | 14 | [✅](../i18n/zh/faq/java/java_multi_thread.md) |
| [java/java_spring](./faq/java/java_spring.md) | 13 | [✅](../i18n/zh/faq/java/java_spring.md) |
| [java/java_tdd](./faq/java/java_tdd.md) | 7 | [✅](../i18n/zh/faq/java/java_tdd.md) |
| [java/jmm](./faq/java/jmm.md) | 9 | [✅](../i18n/zh/faq/java/jmm.md) |
| [java/jvm](./faq/java/jvm.md) | 23 | [✅](../i18n/zh/faq/java/jvm.md) |
| [kafka/faq_kafka](./faq/kafka/faq_kafka.md) | 22 | [✅](../i18n/zh/faq/kafka/faq_kafka.md) |
| [redis/redis_backend](./faq/redis/redis_backend.md) | 5 | [✅](../i18n/zh/faq/redis/redis_backend.md) |
| [redis/redis_info](./faq/redis/redis_info.md) | 11 | [✅](../i18n/zh/faq/redis/redis_info.md) |
| [redis/redis_leaderboard](./faq/redis/redis_leaderboard.md) | 15 | [✅](../i18n/zh/faq/redis/redis_leaderboard.md) |
| [spark/faq_hadoop](./faq/spark/faq_hadoop.md) | 2 | [✅](../i18n/zh/faq/spark/faq_hadoop.md) |
| [spark/faq_mapreduce](./faq/spark/faq_mapreduce.md) | 2 | [✅](../i18n/zh/faq/spark/faq_mapreduce.md) |
| [spark/faq_spark_hadoop](./faq/spark/faq_spark_hadoop.md) | 2 | [✅](../i18n/zh/faq/spark/faq_spark_hadoop.md) |
| [sql/faq_mysql](./faq/sql/faq_mysql.md) | 5 | [✅](../i18n/zh/faq/sql/faq_mysql.md) |
| [sql/faq_sql](./faq/sql/faq_sql.md) | 37 | [✅](../i18n/zh/faq/sql/faq_sql.md) |
| [stream/faq_stream](./faq/stream/faq_stream.md) | 3 | [✅](../i18n/zh/faq/stream/faq_stream.md) |
