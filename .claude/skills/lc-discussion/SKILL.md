---
name: lc-discussion
description: Search recent LeetCode Discuss posts (leetcode.com/discuss/) by keyword — a company, a level, a round — read the ones that describe real interview questions, map each question to an LC number, check every one against README and the practice log, and write the result as a 繁體中文 summary doc under doc/ws/. Use when asked what a company has been asking lately, to scan LC discuss for interview experiences, to turn recent posts into a drill list, or to summarise discussion threads into a doc. Triggers - "/lc-discussion google", "/lc-discussion meta e4 --days 30", "scan LC discuss for recent Google L3 questions", "what is Amazon asking lately?", "summarise the latest interview posts into a doc".
allowed-tools: Read, Write, Glob, Grep, Bash
---

# Recent LeetCode Discuss posts, summarised

Turn the last few weeks of interview write-ups on LeetCode Discuss into one doc in
繁體中文: the posts worth reading, the questions that came up more than once, and
which of those problems this repo has never practised.

**Invocation**: `/lc-discussion <keywords> [--days N]` — e.g. `/lc-discussion google`,
`/lc-discussion google l3 --days 30`, `/lc-discussion amazon oa`.

## Why this exists

`leetcode.com/discuss/` renders in the browser, so fetching the page returns an
empty shell, and most of what the listing shows is noise: team-matching threads,
pay questions, roadmap posts that mention the company name once. In the Oct 2026
Google scan, about 30 of 200 posts described an actual coding question. The
useful part is reading those 30 and lining them up against the log. That turned
up five problems that had come straight from L3/L4 loops and had never been
practised (1235, 3026, 1218, 2812, 1101).

[`script/lc_discussion.py`](https://github.com/yennanliu/CS_basics/blob/master/script/lc_discussion.py)
does the mechanical part. It calls the GraphQL endpoint the page itself uses
(no login), keeps only posts that carry every keyword term, fetches the full
markdown of the ones you pick, and cross-references LC numbers against README and
`data/progress.txt` offline, using the parsers `script/l3_core.py` uses.

## Prime directives

1. **First-hand beats aggregated.** A post saying "I was asked X" counts. A post
   saying "the 8 problems Google asks in 2026" is pattern-level, often built from
   Glassdoor or Blind, and is reported with that caveat. Never treat it as a
   sighting.
2. **Name or match, never guess silently.** An LC number is *named* when the post
   links it or writes it. Anything else is a *match* you inferred from the story
   (the mouse-and-cat grid → LC 2812), and the doc labels it `相近`. A question
   with no good match is listed as `無對應 LC`. Do not force a number onto it.
3. **The log is the record.** Every LC number in the doc gets the `xref` result:
   the log's latest verdict first, README's status cell beside it. A problem is
   never described as solid because README says `OK`.
4. **Summarise, don't republish.** Each post gets a link and a one-line digest of
   the question. Quote a constraint or an example when the question depends on
   it, never a whole post.
5. **Read-only on the record.** This skill writes one file under `doc/ws/`. It
   never writes README or `data/progress.txt`. Drills it suggests are logged with
   `/lc-log`.

## The steps

### 1. Search

```bash
python3 script/lc_discussion.py search google --days 90 --summary
python3 script/lc_discussion.py search google l3 --days 30
```

Each line gives the date, upvotes, topic id, title and URL. Every keyword term
must appear in the title or summary; `--loose` drops that filter, and
`--order MOST_RELEVANT` ranks by match instead of date. `--pages` (default 4
× 50) widens the sweep. `--json FILE` keeps the list for later.

### 2. Shortlist

Keep the posts that describe a **question**: interview experiences, phone
screens, onsites, OAs, compiled question lists. Drop team matching, pay,
timelines, "should I accept", and anything about another role family (SRE,
FDE, data centre) unless the keywords asked for it. Put the target level first
when there is one, and say how many posts were scanned and how many were kept.

### 3. Read

```bash
python3 script/lc_discussion.py fetch 8543506 8527382 8524882
```

The full markdown of each post, with a `named:` line listing any LC links or
numbers it contains. For more than a handful, add `--dir <scratch dir>` and read
the files from there; `--dir` never points inside the repo. Note each question,
its follow-ups and, if the post gives one, the verdict the round got.

### 4. Map to LC numbers

For each question, write down the LC number and whether it is `named` or `相近`
(directive 2). Count repeats across **different** posts: a compiled list that
copies another post is not a second sighting. Patterns that repeat (the same tree
DP from two candidates, three variants of a rate limiter) are the main finding
of the doc.

### 5. Cross-reference

```bash
python3 script/lc_discussion.py xref 1235 3026 963 359 1101 2812
```

Offline. For each number it prints README's title and status, the log's latest
verdict (`ok`, `again`, `none`, or `never`), when it was last attempted, how many
attempts there were, and whether it is in the L3 core set. Sort the doc's
problems into three groups: **never logged**, **`again` or no verdict**, and
**`ok`**.

### 6. Write the doc

Write `doc/ws/lc_discussion_<keywords>_<YYYYMMDD>.md` (keywords joined with `_`,
the date the scan ran) in 繁體中文 with this shape:

```markdown
# LeetCode Discuss 掃描：<keywords>（<YYYY-MM-DD>）

> 掃描範圍：<N> 篇貼文（<from> – <to>），保留 <M> 篇描述實際題目的貼文。

## 值得讀的貼文
| 日期 | 貼文 | 內容 |

## 重複出現的題型
- ...

## 對照你的練習紀錄
### 從沒記錄過
| LC | 題目 | 出處 |
### 有紀錄但最新結果是 again 或沒有結果
### 已經 ok

## 建議的下一輪練習
```

House rules: LC titles, API names and code stay in English; every post is a
link; every LC number has its `xref` status; mark `相近` matches; tag every code
fence. `doc/ws/` is not built into the site, so nothing else needs to change.

### 7. Report

Tell the user the scan size and how many posts were kept, the two or three
patterns that repeated, and the five never-logged problems you would drill first,
each with the post it came from. Give the doc's path. If they drill them, the
next step is `/lc-log`.

## Do not

- ❌ count a compiled or "confirmed sightings" list as a first-hand report
  (directive 1)
- ❌ give a question an LC number without saying whether it was named or
  matched (directive 2)
- ❌ report a problem's state from README alone; `xref` the log (directive 3)
- ❌ paste whole posts into the doc; link them and summarise (directive 4)
- ❌ write `data/progress.txt` or README, or save fetched posts inside the repo
  (directive 5)
- ❌ scrape the HTML page or send a login cookie; the public GraphQL endpoint is
  enough
- ❌ commit or push unless asked

## Worked example

`/lc-discussion google` on 2026-10-02, which produced
[`doc/ws/lc_discussion_google_20261002.md`](https://github.com/yennanliu/CS_basics/blob/master/doc/ws/lc_discussion_google_20261002.md):

| Step | What it produced |
|---|---|
| 1 | `search google --days 90` → 200 posts fetched, about 125 about Google interviews |
| 2 | about 30 describe a coding question; 8 kept as worth reading, with L3 first |
| 3 | `fetch` on those 30; the 75-question compilation (8525983) read as a list, not as sightings |
| 4 | named: 1235, 3026, 963, 359, 2402, 68, 60, 351, 1970; matched (`相近`): 1101, 2812, 1218, 249, 1254, 3453; repeats across posts: 1235 (2 onsites), the leaf-cutting tree DP (2), the right-only grid path count (2), stream variants of the rate limiter (3) |
| 5 | `xref` → never logged: 1235, 3026, 1101, 2812, 1102, 1218, 1970, 1293, 381, 68; again or no verdict: 963, 939, 1254, 2402, 1631, 621, 329, 410, 128, 91; ok: 200, 207, 994, 127 |
| 6 | the doc, in 繁體中文 |
| 7 | suggested drill: 1235, 3026, 1218, 2812, 1101 |
