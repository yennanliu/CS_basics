---
name: lc-algo-demo
description: Add or edit an algorithm visualizer under algo_demo/ against the shared contract the other 36 pages already obey — colours read from the VIZ palette and never hardcoded, no re-implementation of the devicePixelRatio canvas wrapper, the step trace written in the three shapes createLogger renders as structure, the trace in its own full-width panel below the canvas, and `draw = VIZ.repaintable(draw)` for any draw() that takes arguments. Use when asked to add a visualizer, animate an algorithm, build a demo page, or change something that should look the same on every visualizer. Triggers - "add a visualizer for Dijkstra", "/lc-algo-demo trie", "animate the sliding window", "the trace is unreadable on the segment tree page".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add an algorithm visualizer

Write a page into `algo_demo/` that behaves like the other 36.

**Invocation**: `/lc-algo-demo <algorithm>` — e.g. `/lc-algo-demo trie`.

## Why the contract matters here specifically

`algo_demo/` is **36 hand-written pages plus `common.js` and `style.css`, with no
generator**. `site/build.sh` copies the directory wholesale. So nothing compiles these
pages, nothing templates them, and every shared behaviour is held together by convention
alone — which means a page that reimplements one of them looks fine in isolation and is
wrong the moment the theme changes, the window resizes, or someone repaints the palette.

**Anything that should look or behave the same on all 37 pages belongs in `common.js` or
`style.css`, never copy-pasted into a page's inline `<script>`.**

`site/test/algo-demo.test.js` evaluates the shipped `common.js` in jsdom and asserts against
it, so a change to the shared files is testable. A change copy-pasted into a page is not.

## Prime directives

1. **Never hardcode a colour.** Canvases read from `VIZ` — the `--viz-*` tokens — so both
   themes and any repaint stay in one place. A literal `#3b82f6` is invisible in review and
   wrong in the other theme.
2. **Do not reimplement what `getContext('2d')` already gives you.** It is wrapped once, for
   every page: the backing store is scaled to `devicePixelRatio` behind `width`/`height`
   accessors (a page still writes and reads CSS pixels), and the `font` setter rewrites the
   generic families to the site's face. Both fixes therefore reach pages written before
   either existed. Re-doing the scaling double-scales it.
3. **`draw = VIZ.repaintable(draw)` for any `draw()` that takes arguments**, plus
   `window.addEventListener('resize', draw.repaint)`. Handing `draw` straight to
   `addEventListener` passes it the resize `Event` as its first drawing argument, and calling
   it with reset arguments throws the highlight away. A **theme switch fires a resize**, so
   either way the picture reverts mid-run.
4. **The trace lives in `.viz-trace`, below the canvas, full width.** It was once in the
   300px control column, where `L=4 R=24 sum=28` wrapped three times and four steps filled
   the box. Put it back there and the trace stops being readable.
5. **Write the trace in the shapes the logger understands** — otherwise it renders as a flat
   run of divs, which is what it was before.

## The step trace's three shapes

`createLogger(id)`'s contract is still `clear()` and `log(html, cls)`. What it does with the
message depends on its shape:

| Written | Rendered as |
|---|---|
| `logger.log('L=4 R=24 sum=28')` | a **numbered step** |
| `logger.log('&nbsp;&nbsp;sum > target → move R')` | **the reason**, indented and tucked under the step above it |
| `logger.log('--- Iteration 2 ---')` | a **phase heading** |
| `logger.log('<span class="highlight">Found!</span>')` | **the run's outcome** |
| `logger.log('')` | ignored — rows are already spaced |

The leading `&nbsp;` does most of the work: it turns a step and the decision it led to from
two equal-weight lines into one row.

**The outcome test is the message being *entirely* one `highlight` span.** Pages also use
that span mid-sentence to pick out a value — `Process node <span class="highlight">1</span>
dist=7` — and treating those as outcomes flags nine rows in ten, which marks nothing. Use
the whole-message form once per run, for the result.

The logger also auto-decorates `name=value` pairs and `→ ← ⇒` arrows inside a step, so write
them plainly rather than marking them up.

## The steps

### 1. Read a neighbour, and `common.js`

```bash
ls algo_demo/
```

Open a page that visualizes the same *shape* of thing — an array walk
(`kadane.html`, `sliding-window.html`, `two-pointers.html`), a grid (`bfs.html`,
`multi-source-bfs.html`), a tree (`binary-tree-traversal.html`, `lca.html`), a graph
(`dijkstra.html`, `topological-sort.html`) — and match it. Then read `common.js`'s header
comment, which names the three things you get for free.

### 2. Check the helper does not already exist

Before writing a drawing helper, look for it. `VIZ` already provides `bar`, `axis`,
`pointer`, `region`, `readout`, `on(bg)`, `alpha(color, a)`, `font(px, weight)` and
`categorical`, plus `sleep`, `shuffle` and `randomArray`. A page-local re-implementation of
any of them is a divergence waiting to happen.

### 3. Write `algo_demo/<name>.html`

Match the page skeleton exactly — it is what the navbar, breadcrumbs and trace panel hang
off:

```html
<script src="../nav.js"></script>          <!-- before common.js, always -->
<script src="common.js"></script>
...
<div id="site-nav" data-page="visualizer" data-base="../"></div>
<script>CSNav.mount();</script>
<main class="container">
  <div class="breadcrumbs">…<span class="current">Your Algorithm</span></div>
  <div class="page-header">…</div>
  <div class="viz-wrapper">
    <div class="viz-canvas-area"><canvas id="canvas"></canvas></div>
    <div class="legend">…</div>                 <!-- legend dots use var(--viz-*) -->
    <div class="viz-controls">…</div>           <!-- 300px column: controls only -->
  </div>
  <section class="viz-trace">…<div class="viz-log" id="log"></div></section>
</main>
```

The legend dots take `style="background:var(--viz-base)"` — the same tokens the canvas
reads, so the legend cannot drift from the drawing.

In the inline script:

```js
var logger = createLogger('log');

function draw(highlight, phase) { … }          // reads colours from VIZ
draw = VIZ.repaintable(draw);                  // AFTER the function (directive 3)
window.addEventListener('resize', draw.repaint);
```

### 4. Register it on the index

`algo_demo/index.html` is hand-maintained. Add a card to the `algo-grid` under the right
`section-label`, in the shape the others use:

```html
<a class="algo-card" href="<name>.html">
  <h3>Kadane's Algorithm</h3>
  <p>Maximum subarray sum in one pass. LC 53, 152, 918.</p>
  <span class="tag">O(n)</span>
</a>
```

The `<p>` names the LC problems the page is worth opening for — that is what makes the index
usable. A page not on the index is a page nobody finds.

### 5. Exercise it, in both themes

The failure modes are all behavioural, so a visual check is the test:

- **run it end to end** — the trace reads as steps with their reasons tucked under them, one
  outcome at the end, and phase headings where the algorithm changes phase;
- **switch the theme mid-run** — the picture must survive. A theme switch fires a resize,
  which is what directive 3 exists for;
- **resize the window mid-run** — same;
- **narrow to phone width** — no horizontal scroll, and the trace still readable.

```bash
SKIP_FONTS=1 bash site/build.sh
node site/e2e-check.js _site
npm test --prefix site
python3 -m http.server -d _site 8000     # then open /algo_demo/<name>.html
```

`e2e-check.js` walks every built page, so it catches a missing title, description, navbar,
footer or canonical URL, a broken link, an eager image and an external `<script>`.

### 6. Report

Close with the page, the index card, which neighbour's shape it was matched to, which `VIZ`
helpers it used rather than reimplemented, and the two-theme / resize check results.

## Do not

- ❌ hardcode a colour instead of reading `VIZ` (directive 1)
- ❌ re-scale the canvas for `devicePixelRatio` — it is already done (directive 2)
- ❌ hand `draw` straight to `addEventListener('resize', …)` (directive 3)
- ❌ move the trace into the 300px control column (directive 4)
- ❌ wrap a whole step in `<span class="highlight">` unless it is the run's outcome
- ❌ copy-paste a shared behaviour into a page's inline `<script>` — it goes in `common.js`
- ❌ add an external `<script>` tag; `e2e-check.js` fails the build on one
- ❌ forget the index card (step 4)
- ❌ commit or push unless asked

## Worked example

`/lc-algo-demo monotonic-stack`:

| Step | What it produced |
|---|---|
| 1 | matched `kadane.html`'s shape — a single array walk with a growing structure beside it |
| 2 | used `VIZ.bar`, `VIZ.pointer` and `VIZ.readout` rather than writing three drawing helpers |
| 3 | `algo_demo/monotonic-stack.html`; `draw(i, stack, popped)` wrapped with `VIZ.repaintable` and bound to `resize` |
| 4 | card added under the **Stack & Queue** section label: *"Next greater element in one pass. LC 496, 503, 739, 84."* |
| 5 | trace reads `i=3 h=2` → indented `h[3] < stack top → pop`; one `<span class="highlight">` outcome; theme switch and resize mid-run both kept the highlight |
| 6 | flagged: the legend reuses `--viz-swap` for "on the stack", matching `sliding-window.html`'s use of it for the active window |
