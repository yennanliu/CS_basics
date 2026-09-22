<h1 align="center"><a href="https://yennanliu.github.io/CS_basics/">CS_BASICS</a></h1>

<p align="center">
Algorithms, data structures, system design and LeetCode solutions in Python, Java, SQL and Scala —
with cheatsheets, interview FAQs, a study roadmap, algorithm visualizers and a
spaced-repetition review plan.
</p>

<p align="center">
<a href="https://yennanliu.github.io/CS_basics/">Site</a> ·
<a href="https://yennanliu.github.io/CS_basics/search.html">Search</a> ·
<a href="https://yennanliu.github.io/CS_basics/problems.html">Problem index</a> ·
<a href="https://yennanliu.github.io/CS_basics/cheatsheets.html">Cheatsheets</a> ·
<a href="https://yennanliu.github.io/CS_basics/faqs.html">FAQs</a> ·
<a href="https://yennanliu.github.io/CS_basics/lc-roadmap.html">Roadmap</a> ·
<a href="https://yennanliu.github.io/CS_basics/lc-review-plan.html">Review plan</a>
</p>

[![Star History Chart](https://api.star-history.com/svg?repos=yennanliu/CS_basics&type=Date)](https://star-history.com/#yennanliu/CS_basics&Date)


## Looking for a problem?

The problem index — every LeetCode number, the solutions committed here, the complexity,
the tags and the `OK` / `AGAIN` status — is [`PROBLEMS.md`](./PROBLEMS.md).

**Do not search it on GitHub.** GitHub renders only the first **512,000 bytes** of a
markdown file, and the index is over 1 MB. It stops mid-table, with no warning, so
find-in-page here misses about two thirds of the rows. Use the site, which renders it
in full:

| Looking for | Go to |
|---|---|
| one problem, by number / title / tag | **[Search](https://yennanliu.github.io/CS_basics/search.html)** — or press <kbd>/</kbd> on any page |
| the whole index, by topic | **[Problem index](https://yennanliu.github.io/CS_basics/problems.html)** |
| problems filtered by tag and difficulty | **[LC Explorer](https://yennanliu.github.io/CS_basics/lc-explorer.html)** |
| what to revise today | **[Review plan](https://yennanliu.github.io/CS_basics/lc-review-plan.html)** |
| what to learn next, and in what order | **[Study roadmap](https://yennanliu.github.io/CS_basics/lc-roadmap.html)** |
| a problem like the one you just did | **[LC Similar](https://yennanliu.github.io/CS_basics/lc-similar.html)** |

The index is still plain markdown in the repo, so `grep` works fine on a clone:

```bash
grep -n "^| *2071 " PROBLEMS.md
```


## The site

[**yennanliu.github.io/CS_basics**](https://yennanliu.github.io/CS_basics/) is built from
this repo by CI on every push to `master`.

| | |
|---|---|
| [Cheatsheets](https://yennanliu.github.io/CS_basics/cheatsheets.html) | pattern sheets, tiered by interview weight ([繁體中文](https://yennanliu.github.io/CS_basics/cheatsheets.zh.html)) |
| [Interview FAQs](https://yennanliu.github.io/CS_basics/faqs.html) | Java, SQL, backend and system-design questions ([繁體中文](https://yennanliu.github.io/CS_basics/faqs.zh.html)) |
| [Study roadmap](https://yennanliu.github.io/CS_basics/lc-roadmap.html) | topics in dependency order, with Blind 75 / NeetCode / Top 100 Liked filed onto them |
| [Review plan](https://yennanliu.github.io/CS_basics/lc-review-plan.html) | a spaced-repetition session picked from [`data/progress.txt`](data/progress.txt) |
| [Complexity quiz](https://yennanliu.github.io/CS_basics/lc-complexity-quiz.html) | name the time and space bound of a snippet |
| [Algorithm visualizers](https://yennanliu.github.io/CS_basics/algo_demo/) | animated, step-by-step algorithm pages |
| [Pattern recognition](https://yennanliu.github.io/CS_basics/patterns.html) | map a problem statement to the technique it wants |
| [Agent skills](https://yennanliu.github.io/CS_basics/skills.html) | the `/lc-*` commands below, as installable skills |


## What's in the repo

| Path | What it holds |
|------|---------------|
| [`PROBLEMS.md`](./PROBLEMS.md) | **the problem index** — every solved problem, by topic |
| [`leetcode_python/`](./leetcode_python) | Python solutions, in directories by pattern |
| [`leetcode_java/`](./leetcode_java) | Java solutions (Maven + JUnit) |
| [`leetcode_SQL/`](./leetcode_SQL), [`leetcode_scala/`](./leetcode_scala) | SQL and Scala solutions |
| [`algorithm/`](./algorithm), [`data_structure/`](./data_structure) | algorithm and data-structure implementations, several languages |
| [`doc/cheatsheet/`](./doc/cheatsheet) | the pattern cheatsheets |
| [`doc/faq/`](./doc/faq) | interview FAQs |
| [`system_design/`](./system_design) | system-design patterns, templates and case studies |
| [`algo_demo/`](./algo_demo) | the algorithm visualizers |
| [`script/`](./script) | the planners and maintenance scripts — see [`doc/utility-scripts.md`](doc/utility-scripts.md) |
| [`site/`](./site) | the GitHub Pages build |
| [`data/progress.txt`](data/progress.txt) | the daily practice log, and the only source for the review plan |


## Agent skills

Markdown skills you can install into Claude Code (or Codex / Gemini — see each
[`INSTALL.md`](.claude/skills/lc-coach/INSTALL.md)). Each directory name **is** the slash
command. Details on [skills.html](https://yennanliu.github.io/CS_basics/skills.html).

| Command | What it does |
|---|---|
| [`/lc-coach`](.claude/skills/lc-coach) | scores a solution the way a FAANG interviewer does, and writes the debrief packet |
| [`/lc-python`](.claude/skills/lc-python) | files a Python solution in the house layout and inserts its `PROBLEMS.md` row |
| [`/lc-java`](.claude/skills/lc-java) | the same for Java, adding the `[Java]` link to the row that already exists |
| [`/lc-log`](.claude/skills/lc-log) | appends today's practice to [`data/progress.txt`](data/progress.txt) |
| [`/lc-again`](.claude/skills/lc-again) | moves a problem's `OK` / `AGAIN` status after a re-solve |
| [`/lc-cheatsheet`](.claude/skills/lc-cheatsheet) | files what a problem taught you into [`doc/cheatsheet/`](./doc/cheatsheet) |
| [`/lc-algo-demo`](.claude/skills/lc-algo-demo) | adds a visualizer to [`algo_demo/`](./algo_demo) against the shared contract |
| [`/lc-site-data`](.claude/skills/lc-site-data) | adds a roadmap topic or a complexity-quiz question |
| [`/lc-faq-add`](.claude/skills/lc-faq-add) | files an interview question into [`doc/faq/`](./doc/faq), English + 繁中 |
| [`/lc-zh-translate`](.claude/skills/lc-zh-translate) | works the 繁體中文 translation backlog |
| [`/add-time-space`](.claude/skills/add-time-space) | adds the `time =` / `space =` javadoc to a Java package |


## Plan a session from the command line

```bash
python3 script/suggest_review.py              # what to revise today, from the practice log
python3 script/eval_lc_readiness.py           # score a LeetCode profile against a Google SWE bar
python3 script/extract_must_lc.py             # regenerate doc/must_lc_list.md from the index
```

Full usage for every script: [`doc/utility-scripts.md`](doc/utility-scripts.md).


## Build the site locally

`_site/` is generated output and is **gitignored** — CI builds it. To preview the same
tree CI serves:

```bash
npm ci --prefix site               # first time only
bash site/build.sh                 # SKIP_FONTS=1 to skip the web-font download
node site/e2e-check.js _site       # the gate CI runs — run it before you push
npm test --prefix site             # the unit tests
python3 -m http.server -d _site 8000
```

To change the site, edit source only: the markdown under [`doc/`](./doc), the static pages
in [`site/pages/`](./site/pages), or the build tooling in [`site/`](./site).


## Big-O at a glance

<h5 align="center"><a href="https://www.bigocheatsheet.com/">pic_source</a></h5>

<p align="center"><img src ="./doc/pic/bigO_complexity_chart.png"></p>

<p align="center"><img src ="./doc/pic/common_ds_op_cost.png" ></p>

<p align="center"><img src ="./doc/pic/sort_algorithm_complexity.png" ></p>

<p align="center"><img src ="./doc/pic/sort_algorithm_complexity_2.jpeg" ></p>

<p align="center"><img src ="./doc/pic/big-o-cheat-sheet-poster.png" ></p>

<p align="center"><img src ="./doc/pic/data_structure_2.png" ></p>

<p align="center"><img src ="./doc/pic/common_ds.png" ></p>

<p align="center"><img src ="./doc/pic/needcode_roadmap.png" ></p>


## Resource

* LC classics problems
	- [Blind Curated 75](https://leetcode.com/list/xoqag3yj/)
	- [Grind 75](https://www.techinterviewhandbook.org/grind75/)
	- [Grind 169](https://www.techinterviewhandbook.org/grind75/?weeks=28&hours=6)
	- [leetcode wiki repo](https://github.com/doocs/leetcode)
	- [leetcode wiki](https://leetcode.doocs.org/)
	- [LC 官神Github題目分類整理](https://github.com/wisdompeak/LeetCode/tree/master)
	- [LC top 100 likes](https://leetcode.com/studyplan/top-100-liked/)
	- [neetcode 150 LC list](https://neetcode.io/practice)
		- [My Brain after 569 Leetcode Problems](https://youtu.be/8wysIxzqgPI)
	- [jiakaobo LC](https://www.jiakaobo.com/leetcode.html) : LC code & video
	- [LC pattern @ blind](https://www.teamblind.com/post/New-Year-Gift---Curated-List-of-Top-100-LeetCode-Questions-to-Save-Your-Time-OaM1orEU) : Curated-List-of-Top-100-LeetCode-Questions-to-Save-Your-Time
	- [LC Algorithm Problem Classification](https://www.programcreek.com/2013/08/leetcode-problem-classification/)
	- [cheatsheet-leetcode-a4](https://cheatsheet.dennyzhang.com/cheatsheet-leetcode-a4)
	- [14-patterns-to-ace-any-coding-interview-question](https://hackernoon.com/14-patterns-to-ace-any-coding-interview-question-c5bb3357f6ed)
	- [grokking-the-coding-interview](https://www.educative.io/courses/grokking-the-coding-interview)


* LC experiences
	- [LC難度表](https://zerotrac.github.io/leetcode_problem_rating/#/)
		- 數字越高, 題目越難, 挑與自己LC排名接近的題庫
		- (e.g. LC rank ~= 1600, pick 1600 problem)
	- [代碼隨想錄](https://github.com/youngyangyang04/leetcode-master)
	- [Leetcode cookbook](https://github.com/halfrost/LeetCode-Go)
	- [fucking-algorithm](https://github.com/labuladong/fucking-algorithm)
	- [fucking-algorithm website](https://labuladong.github.io/algo/)
	- [FAANG 面試準備經驗與建議（一）](https://arthur-lin.medium.com/faang-%E9%9D%A2%E8%A9%A6%E6%BA%96%E5%82%99%E7%B6%93%E9%A9%97%E8%88%87%E5%BB%BA%E8%AD%B0-%E4%B8%80-b7add6a7b9a6)
	- [FAANG 面試準備經驗與建議（二）](https://arthur-lin.medium.com/faang-%E9%9D%A2%E8%A9%A6%E6%BA%96%E5%82%99%E7%B6%93%E9%A9%97%E8%88%87%E5%BB%BA%E8%AD%B0-%E4%BA%8C-%E6%A8%A1%E6%93%AC%E9%9D%A2%E8%A9%A6%E8%88%87%E8%B3%87%E6%BA%90%E4%BB%8B%E7%B4%B9-b06cc097b665)
	- [LC 小知識](https://ithelp.ithome.com.tw/articles/10299626)
	- [Meta SWE isnterview prep](https://www.metacareers.com/profile/trial/?redirect=job_details&chooseView=Arrays)
	- [0到100的軟體工程師面試之路](https://ithelp.ithome.com.tw/users/20152262/ironman/5615?page=2)
	- 來和大家聊聊我是如何刷題的 : pt1, pt2, pt3
		- https://blog.csdn.net/azl397985856/article/details/110358828
		- https://mp.weixin.qq.com/s/guCR2DCTGoWf4ojeqq2M8A
		- https://mp.weixin.qq.com/s/P_RMRmugmxvIHGyn2EHl7g

* LC Flow
	- high level idea : data structure, algorithm
	- offer time & space complexity
	- code implementation
	- offer test case (consider edge case)
	- discussion & follow up

- [Resource.md](./doc/Resource.md) - `Resource` for coding interview (**keep updating**)
- [Teach yourself CS](https://teachyourselfcs.com/)
- [MindMapCodeInterview](./doc/cheatsheet/mind_map_code_interview.png) - Mind map for coding interview 
- [CodeInterviewCheatsheet](./doc/cheatsheet/code_interview_cheatsheet.pdf) - Coding interview cheetsheet
- [repl.it](https://repl.it/) - Coding online!

* Visualization
	- [Algorithms viz](https://www.cs.usfca.edu/~galles/visualization/Algorithms.html)
	- [visualgo - DFS / BFS](https://visualgo.net/en/dfsbfs?slide=1) - DFS, BFS visualization
	- [visualgo - linkedlist](https://visualgo.net/en/list) - Linkedlist visualization
	- [visualgo - BST](https://visualgo.net/bn/bst) - binary search tree visualization
	- [toptal-sorting-algorithms](https://www.toptal.com/developers/sorting-algorithms)- sorting algorithms online

- [How to: Work at Google](https://www.youtube.com/watch?v=XKu_SEDAykw) — Example Coding/Engineering Interview
- [bit_manipulation.md](./doc/bit_manipulation.md) - Bit Manipulation Cheat Sheet
- [Py TimeComplexity](https://wiki.python.org/moin/TimeComplexity) - Py basic data structure `Time Complexity` ref
- [Py data model](https://docs.python.org/3/reference/datamodel.html) - Python data model doc 
- [pgexercises](https://pgexercises.com/questions/aggregates/) - Postgre exercises
- [sqlservertutorial](https://www.sqlservertutorial.net/)
- [Books](https://github.com/yennanliu/data_science_repo/tree/master/book)
- [freecodecamp - data-structures](https://www.freecodecamp.org/news/tag/data-structures/)
- [LC interview-experience](https://leetcode.com/discuss/interview-experience?currentPage=1&orderBy=hot&query=)
- [Cheatsheet](https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet)

* Data structure
	- [py core data structure interview](https://python.plainenglish.io/python-for-interviewing-an-overview-of-the-core-data-structures-666abdf8b698)

* System design
	- [system_design readme](https://github.com/yennanliu/CS_basics/tree/master/system_design)
	- [sys_design_resource](https://github.com/yennanliu/CS_basics/blob/master/system_design/sys_design_resource.md)

* Tools
	- https://coderpad.io/
	- https://codeshare.io/
	- http://sqlfiddle.com/

* LC SQL resources
	- [LC SQL resource](./doc/lc_sql_resource.md)
