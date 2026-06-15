# GitHub Pages — Build & Deploy Guide

## Overview

The site converts markdown docs in `doc/` into a static HTML site deployed via GitHub Pages. Build is automated through GitHub Actions on every push to `master`.

**Live site:** `https://yennanliu.github.io/CS_basics/`

---

## Directory Layout

```
site/
  build-site.js       # Converts doc/**/*.md → _site/**/*.html
  build-leetcode.js   # Generates LeetCode JSON data
  style.css           # Site stylesheet
  package.json        # Node dependencies (markdown-it, highlight.js)

vendor/
  fonts.css           # Font declarations (Inter, JetBrains Mono)

_site/                # Build output (gitignored); uploaded as Pages artifact
  index.html
  cheatsheets/
  faqs/
  doc/pic/
  style.css
  vendor/

.github/workflows/
  deploy-pages.yml    # Main deploy pipeline
  validate-pages.yml  # Pre-deploy HTML validation
```

---

## Local Build

```bash
# Install dependencies (once)
npm ci --prefix site

# Build HTML from markdown
mkdir -p _site/doc
cp -r doc/pic _site/doc/pic
mkdir -p _site/faqs
node site/build-site.js
cp site/style.css _site/style.css

# (Optional) Build LeetCode JSON data
node site/build-leetcode.js
```

Output lands in `_site/`. Open `_site/index.html` in a browser to preview locally.

---

## CI / CD Pipeline

### Trigger conditions (`deploy-pages.yml`)

Runs on push to `master` when any of these paths change:

- `doc/**`, `README.md`, `site/**`, `vendor/**`, `algo_demo/**`
- `.github/workflows/deploy-pages.yml`

Code-only changes (`leetcode_java/`, `leetcode_python/`, etc.) skip the deploy.  
Manual trigger: **Actions → Deploy to GitHub Pages → Run workflow**.

### Build steps (in order)

| Step | What it does |
|------|-------------|
| Checkout | Full repo clone |
| Setup Node 20 | Runtime for build scripts |
| `npm ci --prefix site` | Install locked dependencies |
| `node site/build-site.js` | Markdown → HTML (cheatsheets, FAQs, index pages) |
| Copy CSS + vendor assets | `style.css`, highlight.js theme, font files |
| Download fonts | Inter + JetBrains Mono woff2 from Google Fonts |
| Create `404.html` | Custom 404 page |
| Copy `algo_demo/` | Interactive algorithm visualizations (if present) |
| `node site/build-leetcode.js` | Generates LeetCode explorer JSON |
| Upload artifact | `actions/upload-pages-artifact@v3` bundles `_site/` |
| Deploy | `actions/deploy-pages@v4` pushes artifact to Pages |

### Validation pipeline (`validate-pages.yml`)

Runs the same build steps, then executes `validate-site.js` which checks:

- Required top-level HTML files exist and are non-empty
- Key cheatsheet pages exist (`heap.html`, `array.html`, `dp.html`, etc.)
- Every HTML page has `<!DOCTYPE html>`, charset, viewport, navbar, footer
- No unconverted GitHub blob image URLs or broken relative `../pic/` paths
- Internal `.html` links resolve to real files
- `_site/doc/pic/` contains image assets

---

## GitHub Pages Setup (one-time)

1. **Repo Settings → Pages**
   - Source: `GitHub Actions` (not a branch)
2. **Required permissions** (set in `deploy-pages.yml`):
   ```yaml
   permissions:
     contents: read
     pages: write
     id-token: write
   ```
3. **Concurrency** is set to `cancel-in-progress: false` so a slow deploy is never interrupted by a follow-up push.

---

## Adding Content

| Content type | Where to add |
|-------------|-------------|
| Cheatsheet | `doc/cheatsheet/<name>.md` |
| FAQ page | `doc/faq/<name>.md` |
| Images | `doc/pic/<filename>` |
| Algorithm visualization | `algo_demo/<name>/` |

The build scripts auto-discover files in those directories — no manual registration needed.

---

## Other Workflows

| Workflow | Trigger | Purpose |
|---------|---------|---------|
| `java-syntax-check.yml` | Push to `leetcode_java/**/*.java` | Compiles all Java files; catches syntax errors and leaked Javadoc |
| `refresh-lc-data.yml` | Every Monday 02:00 UTC | Runs `script/categorize_lc_by_type.py` and commits updated category data |
| `validate-pages.yml` | Push/PR touching docs or site files | Dry-run build + HTML validation before deploy |
