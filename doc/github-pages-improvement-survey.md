# GitHub Pages Build Process — Improvement Survey

**Date:** 2026-04-19  
**Scope:** `.github/workflows/`, root `build-*.js` / `ensure-nav-pages.js`, `_site/`, `script/`

---

## Current Architecture

```
push to master
  └─ deploy-pages.yml (always runs)
       ├─ npm install (markdown-it, markdown-it-anchor)
       ├─ inline build-site.js   ← huge heredoc inside YAML
       ├─ inline style.css       ← ~2000-line CSS heredoc inside YAML
       ├─ create 404.html
       ├─ copy algo_demo/
       ├─ build-leetcode.js      (separate root file, invoked via `node`)
       ├─ ensure-nav-pages.js    (separate root file)
       └─ upload + deploy artifact

  └─ validate-pages.yml (runs on doc/**, README.md, workflow changes only)
       ├─ npm install (same deps, no lockfile)
       ├─ inline build-site.js (DUPLICATE — nearly identical copy)
       ├─ inline validate-site.js
       └─ exits 1 on errors

  └─ java-syntax-check.yml (runs on leetcode_java/** changes only)
       ├─ compile all .java files
       └─ leaked-javadoc python check
```

---

## Improvement Opportunities

### 1. Massive Code Duplication Between `deploy-pages.yml` and `validate-pages.yml`

**Problem:** The entire `build-site.js` logic (~400 lines) is duplicated verbatim inside both workflow files as heredocs. Any change to the build logic must be manually kept in sync across two places.  
**Benefit:** Single source of truth; fixing a bug or adding a feature only requires one edit; eliminates drift risk.  
**Effort:** Low — extract the shared script to a committed file (e.g., `build-site.js`) and have both workflows call `node build-site.js`. `build-site-full.js` already exists at root as a candidate.  

---

### 2. CSS Embedded as a 2000-Line Heredoc in YAML

**Problem:** The entire site stylesheet is inlined as a shell heredoc inside `deploy-pages.yml` (lines 664–2023). YAML heredocs are hard to edit, have no syntax highlighting, and hide the CSS from normal file-based tooling.  
**Benefit:** Easier styling changes; enables linting/formatting of CSS; CSS shows up in `git diff` cleanly.  
**Effort:** Low — move `style.css` to `_site/style.css` tracked in git (or a `src/` directory), and replace the heredoc step with a `cp` command.  

---

### 3. No Dependency Lockfile (`package-lock.json`)

**Problem:** Both workflows generate a `package.json` on-the-fly and run `npm install` without a lockfile. Dependency versions are not pinned beyond the semver range (`^14.0.0`, `^9.0.1`), so a minor version bump in a dependency can silently break the build.  
**Benefit:** Reproducible builds; faster CI (lockfile enables `npm ci`).  
**Effort:** Very low — commit a `package.json` and `package-lock.json` to the repo root; replace the "create package.json" step with `npm ci`.  

---

### 4. Full Rebuild on Every Push (No Incremental Builds)

**Problem:** Every push to master rebuilds all cheatsheets (~77 files), all FAQs, the entire README, and the LeetCode Explorer from scratch, even if only one Java file changed.  
**Benefit:** Faster deploys; less CI compute usage.  
**Effort:** Medium — use `paths` filters in `deploy-pages.yml` to skip the deploy job for pushes that only touch `leetcode_java/**` or `leetcode_python/**`. Alternatively, add a file-hash check inside `build-site.js` to skip re-rendering unchanged markdown files.  

---

### 5. `validate-pages.yml` Path Filter May Miss Workflow-Only Changes

**Problem:** The validate workflow only triggers on changes to `doc/**`, `README.md`, and the two workflow files. Changes to `build-leetcode.js` or `ensure-nav-pages.js` (committed at repo root) do not trigger validation.  
**Benefit:** Catch regressions in root build scripts before deploying.  
**Effort:** Very low — add `build-*.js`, `ensure-nav-pages.js` to the `paths` filter in `validate-pages.yml`.  

---

### 6. `build-site.js` and `build-site-full.js` Have Unclear Relationship

**Problem:** `build-site-full.js` (168 lines) exists at the repo root but is not called by any workflow. Its purpose overlaps with the inline `build-site.js`. This creates confusion about which is canonical.  
**Benefit:** Clearer codebase; fewer orphan scripts.  
**Effort:** Very low — either integrate `build-site-full.js` as the canonical build script called by workflows, or delete it if it is unused.  

---

### 7. `script/` Utilities Are Disconnected from the CI/CD Pipeline

**Problem:** Useful scripts (`categorize_lc_by_type.py`, `get_review_list.py`, `get_lc_per_rating.py`, etc.) only run manually. There is no automated data refresh step in the pipeline.  
**Benefit:** Auto-refreshed data files (e.g., `data/LC_Practice_*_By_Category.md`) could be committed back by CI and surfaced on the GitHub Pages site.  
**Effort:** Medium — add a scheduled workflow (`workflow_dispatch` or cron) that runs the categorization script and commits updated data files; optionally surface the output in a new site section.  

---

### 8. External CDN Dependencies (`highlight.js`, Google Fonts)

**Problem:** The site loads `highlight.js` and Inter/JetBrains Mono from CDNs at runtime. If the CDN is slow or unavailable, code highlighting and fonts degrade.  
**Benefit:** Fully offline-capable site; predictable performance; no third-party privacy concerns.  
**Effort:** Medium — bundle these assets locally during the build step (`npm install highlight.js` and download font files; serve from `_site/`).  

---

### 9. No Caching for `npm install` Steps

**Problem:** Both workflows install npm packages from scratch on every run (no caching configured). Combined, this adds ~20–40 seconds per workflow run.  
**Benefit:** Faster CI runs.  
**Effort:** Very low — add `actions/cache` with `~/.npm` as the cache key and `package-lock.json` hash as the cache key (once lockfile is committed per item 3).  

---

### 10. Java Syntax Check Compiles All Files Individually with No Maven

**Problem:** `java-syntax-check.yml` uses raw `javac` rather than the existing Maven project (`leetcode_java/pom.xml`). This means JUnit and other test dependencies are excluded, and the dev/Sorting package must be manually excluded.  
**Benefit:** Using `mvn compile` and `mvn test-compile` would catch more errors, respect the project's actual dependency graph, and eliminate the manual exclusion list.  
**Effort:** Low-Medium — replace the `javac` step with `mvn compile` (already specified in CLAUDE.md); test that it resolves correctly in the GitHub Actions environment.  

---

## Summary Table

| # | Issue | Benefit | Effort |
|---|-------|---------|--------|
| 1 | Duplicate build-site.js in both workflows | Eliminate drift risk | Low |
| 2 | 2000-line CSS heredoc in YAML | Easier editing, git-diffable | Low |
| 3 | No package-lock.json | Reproducible builds, faster CI | Very Low |
| 4 | Full rebuild on every push | Faster deploys | Medium |
| 5 | validate-pages.yml misses root build scripts | Catch regressions earlier | Very Low |
| 6 | Orphan build-site-full.js | Cleaner codebase | Very Low |
| 7 | script/ utilities not in CI | Auto-refreshed data pages | Medium |
| 8 | CDN dependencies at runtime | Offline resilience | Medium |
| 9 | No npm cache in workflows | Faster CI | Very Low |
| 10 | javac instead of Maven for Java check | Catches more errors | Low–Medium |

**Quick wins (Very Low effort):** 3, 5, 6, 9 — can be done in a single small PR.  
**High impact (Low effort):** 1, 2 — eliminate the biggest maintainability pain points.
