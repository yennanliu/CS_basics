#!/usr/bin/env bash
#
# Builds the complete GitHub Pages tree into _site/.
#
# This is the ONE build recipe — .github/workflows/{deploy-pages,validate-pages}.yml
# both call this script, so a local build produces exactly what deploys. _site/ is
# generated output and is NOT tracked in git (see .gitignore); edit the markdown in
# doc/ and the static pages in site/pages/ instead.
#
#   bash site/build.sh              # full build (downloads web fonts)
#   SKIP_FONTS=1 bash site/build.sh # skip the font download (offline / faster)
#
# Run from anywhere — the script resolves the repo root from its own location.

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if [ ! -d site/node_modules ]; then
  echo "site/node_modules is missing — run: npm ci --prefix site" >&2
  exit 1
fi

# A clean tree every time. Without this, a page whose source markdown was deleted
# or renamed would survive in _site/ and stay live as an orphan URL.
rm -rf _site
mkdir -p _site/doc _site/faqs _site/vendor/highlight

# ── Doc images (build-site.js rewrites ../pic/x.png -> doc/pic/x.png) ──────────
cp -r doc/pic _site/doc/pic

# ── Generated pages ───────────────────────────────────────────────────────────
node site/build-site.js       # doc/**.md      -> HTML pages + search index
node site/build-leetcode.js   # leetcode_*/**  -> _site/data/lc-problems.json
node site/build-roadmap.js    # data/roadmap.json + README.md -> _site/data/roadmap.json
node site/build-quiz.js       # data/complexity_quiz.json + README.md -> _site/data/complexity-quiz.json
node site/build-review-plan.js # data/progress.txt -> _site/data/progress.json

# ── Shared CSS + JS ───────────────────────────────────────────────────────────
cp site/style.css site/nav.css site/lc-page.css site/nav.js site/site.js site/roadmap.js site/complexity.js _site/

# ── Hand-maintained static pages (LC tools, 404) ──────────────────────────────
cp site/pages/*.html _site/

# ── Vendor assets ─────────────────────────────────────────────────────────────
cp site/node_modules/highlight.js/styles/atom-one-dark.min.css _site/vendor/highlight/atom-one-dark.min.css
cp vendor/fonts.css _site/vendor/fonts.css
# lc-similar.html used to pull d3 straight off d3js.org — the site's only
# third-party runtime dependency, unpinned and without an integrity hash, on a
# page that is unusable if the request fails. It is a build dependency now, so
# the version is locked in package-lock.json like everything else.
cp site/node_modules/d3/dist/d3.min.js _site/vendor/d3.min.js

if [ "${SKIP_FONTS:-0}" = "1" ]; then
  echo "→ SKIP_FONTS=1, not downloading web fonts (vendor/fonts.css declares fallbacks)"
else
  curl -sSfL "https://fonts.gstatic.com/s/inter/v20/UcC73FwrK3iLTeHuS_nVMrMxCp50SjIa1ZL7W0Q5nw.woff2" \
    -o _site/vendor/inter.woff2
  curl -sSfL "https://fonts.gstatic.com/s/jetbrainsmono/v24/tDbv2o-flEEny0FZhsfKu5WU4zr3E_BX0PnT8RD8yKwBNntkaToggR7BYRbKPxDcwgknk-4.woff2" \
    -o _site/vendor/jetbrains-mono.woff2
fi

# ── Algorithm visualizers ─────────────────────────────────────────────────────
if [ -d algo_demo ]; then
  cp -r algo_demo _site/algo_demo
fi

# ── Finishing passes ──────────────────────────────────────────────────────────
# Both need the *whole* tree in place, so they run last: the hand-written pages
# above are copied, not generated, and no generator can see them.
node site/finalize-pages.js  # canonical / og / twitter tags, sitemap.xml, robots.txt
node site/prune-images.js    # drop doc/pic images no page references

echo "✓ _site built ($(find _site -type f | wc -l | tr -d ' ') files, $(du -sh _site | cut -f1))"
