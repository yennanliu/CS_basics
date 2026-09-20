---
name: lc-faq-add
description: File an interview question and answer into doc/faq/ in the shape the other 49 FAQs use — the sheet its Scope line owns, the numbered section it belongs under, tagged code fences — and write the matching 繁體中文 overlay section in the same pass, because that tree is 100% translated and a new English answer leaves a hole the moment it lands. Use when asked to add an FAQ entry, document an interview question, answer a backend/Java/SQL/Kafka question into the notes, or start a new FAQ file. Triggers - "add a question about virtual threads to the java FAQ", "/lc-faq-add kafka rebalancing", "document this interview question", "start a FAQ for gRPC".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# File a Q&A into the FAQs

Add an answer to `doc/faq/` and its 中文 counterpart under `i18n/zh/faq/`.

**Invocation**: `/lc-faq-add <topic or question>` — e.g.
`/lc-faq-add kafka rebalancing`, `/lc-faq-add java virtual threads`.

## Why the translation is part of the job

`doc/faq/` is **49 documents across 12 sub-trees and it is 100% translated — 902 of 902
sections**. So a new English section does not leave the tree "mostly translated": it leaves
a hole that nothing reports until someone next runs `node script/zh.js status faq`, and the
page renders a Chinese document with an English gap in the middle of it.

That is why this skill writes both halves in one pass. It is also the difference between
this skill and `markdown-doc-writer`, which knows neither the tree nor the overlay.

## Prime directives

1. **The Scope line picks the file, not the topic name.** Every FAQ opens with one, and it
   exists to stop two files growing into the same document. Read the candidates' Scope lines
   before choosing — "connection pooling" could plausibly land in `db/`, `backend/` or
   `java/`, and only the Scope lines say which owns it.
2. **File it under the section it belongs to.** The FAQs are numbered documents
   (`## 1) The Four Pillars of OOP` → `### 1-1) Encapsulation`). A new answer goes under the
   section that owns the concept, never appended to the end as a loose question.
3. **The Chinese section ships in the same change** (see above).
4. **Every fence is tagged.** ` ```java `, ` ```python `, ` ```sql `, ` ```bash `, or
   ` ```text ` for ASCII diagrams and program output. Never a bare ` ``` `.
5. **Answer the question that gets asked.** These are interview notes: the answer leads with
   the thing you would say out loud, then the mechanism, then the code if code helps. A
   tutorial that builds up to the answer is the wrong shape.

## Where a file lives, and what that decides

The directory **is** the index category — `build-site.js` maps the top-level sub-directory:

| Directory | Card category |
|---|---|
| `doc/faq/java/` | Java |
| `doc/faq/backend/` | Backend |
| `doc/faq/db/` | Database |
| `doc/faq/redis/`, `kafka/`, `flink/`, `sql/` | Redis, Kafka, Flink, SQL |
| `doc/faq/spark/` | Spark & Hadoop |
| `doc/faq/stream/` | Streaming |
| a file at `doc/faq/` root | General |

An unmapped sub-directory becomes its own capitalised category rather than failing — so a
new directory is a decision about the index, and worth naming in the report.

The page name folds the sub-directory in: `doc/faq/java/faq_OOP.md` → `faqs/java_faq_OOP.html`.
The card title comes from the **H1**, and the card description is summarised from the lead
paragraph — so both are worth writing as something a reader would recognise.

## The steps

### 1. Find the file whose Scope line owns it

```bash
ls doc/faq/ doc/faq/*/
grep -n "Scope" doc/faq/*/*.md doc/faq/*.md | grep -i "<topic keyword>"
```

Read the Scope lines of every plausible candidate, not just the one whose filename matches.
If the best file is not the obvious one, say so in the report and explain which Scope line
decided it.

**A new FAQ file** is right when no existing Scope line claims the area and the topic is
worth more than a section. It needs its own H1 and Scope block:

```markdown
# gRPC FAQ

> **Scope** — <what this file owns, and what it deliberately does not>.
> **See also**: [`./faq_API_design.md`](./faq_API_design.md) — <why you'd go there>.

---

## 1) <First section>
```

Add the `See also` line to the neighbouring file too, so the pair points both ways.

### 2. Read a neighbour, then place the section

```bash
sed -n '1,60p' doc/faq/<dir>/<file>.md
grep -n '^## \|^### ' doc/faq/<dir>/<file>.md
```

The heading numbering is positional (`## 3)` → `### 3-2)`). Inserting a section in the
middle means renumbering the ones after it — which **moves their anchors**, so every link
aimed at them has to move in the same change. Prefer appending within the owning section,
and when a renumber is genuinely right, do the link sweep:

```bash
grep -rn "#<old-anchor>" doc/ i18n/ site/pages/
```

### 3. Write the answer

- lead with the answer, then the mechanism, then code (directive 5);
- bold the key terms the way the neighbours do — `**Pattern**`, `**Key Idea**`;
- a table when the answer is a comparison; that is what most of these questions are;
- every fence tagged (directive 4);
- headings never skip a level (`h2` → `h3`, never `h2` → `h4`).

### 4. Write the 中文 section in the same pass

```bash
node script/zh.js sync faq/<dir>/<file>
node script/zh.js todo faq/<dir>/<file>      # the new section, with its key
```

Write it into `i18n/zh/faq/<dir>/<file>.md` under the key `todo` printed, keeping **every
`<!--CODE-->` marker in order** — `compose` throws otherwise. Structure and heading order
come from the English document; translate the heading *text* only. Links keep their English
anchor targets. API, class and command names stay in English.

An FAQ has **no Scope line**, so its card is summarised from the *composed Chinese* — if the
lead paragraph changed, translate it, and the card follows.

The full rules, and the campaign workflow for a backlog, are
[`/lc-zh-translate`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-zh-translate/SKILL.md)'s.

```bash
node script/zh.js sync faq/<dir>/<file>
node script/zh.js status --write             # regenerates doc/faq-zh-progress.md
```

`doc/faq-zh-progress.md` is generated — never hand-edit it.

### 5. Gate

```bash
SKIP_FONTS=1 bash site/build.sh
node site/e2e-check.js _site
npm test --prefix site
```

`e2e-check.js` is where a dangling `#fragment` from a renumbered heading, an untagged fence
that rendered wrong, a broken relative link or a missing page description surfaces. Then
open both `faqs/<name>.html` and `faqs/<name>.zh.html` — the navbar's 中文 / EN button swaps
between them.

### 6. Report

Close with: the file and section, which Scope line decided it, whether anything was
renumbered and what links moved with it, the zh key written, the new
`status faq` coverage line, and anything left deliberately in English.

## Do not

- ❌ pick the file by topic name instead of by Scope line (directive 1)
- ❌ append a loose question to the end of a file instead of filing it under its section
- ❌ ship the English section without its 中文 counterpart (directive 3)
- ❌ leave a fence untagged (directive 4)
- ❌ renumber headings without sweeping the links that pointed at them
- ❌ drop, add or reorder a `<!--CODE-->` marker in the translation
- ❌ put `category`, `tier` or `kind` in a translation — FAQs do not carry them at all
- ❌ hand-edit `doc/faq-zh-progress.md`
- ❌ start a new FAQ file when an existing Scope line already claims the area
- ❌ commit or push unless asked

## Worked example

`/lc-faq-add kafka rebalancing`:

| Step | What it produced |
|---|---|
| 1 | three candidates; `doc/faq/kafka/faq_kafka.md`'s Scope line owns consumer-group behaviour, the Streaming one's owns processing semantics — so the Kafka file, named in the report |
| 2 | filed under `## 4) Consumer Groups` as `### 4-3) Rebalancing`, appended within the section so nothing renumbered and no anchor moved |
| 3 | answer leads with *"a rebalance stops the whole group"*, then the trigger list, then a ` ```text ` timeline and a ` ```bash ` of `kafka-consumer-groups.sh` |
| 4 | `todo faq/kafka/faq_kafka` → one key; translated with both `<!--CODE-->` markers in place; `kafka-consumer-groups.sh` and `max.poll.interval.ms` left in English |
| 5 | build clean, e2e 80/80, `status faq` back to 903/903 |
| 6 | flagged: `doc/faq/stream/faq_stream.md` gained a `See also` line pointing here, since its Scope line touches the same area |
