// Shared helpers for the Algorithm Visualizer pages.
//
// The navbar and the theme switch live in the shared site/nav.js, which every
// page loads before this file. Beyond repainting on a theme change, this file
// owns three things every visualization gets for free, without a line of
// per-page code:
//
//   1. the --viz-* palette (VIZ)                — see "Visualization palette"
//   2. crisp canvas rendering + canvas type     — see "Canvas quality"
//   3. the structured step trace (createLogger) — see "Step trace"
//
// Rule of thumb: anything that should look the same on all 36 pages belongs
// here or in style.css, never copy-pasted into a page's inline <script>.

// ── Visualization palette ────────────────────────────────────────────────
// Canvases must never hardcode a colour: read it from VIZ so both themes and
// any future palette change stay in one place (the --viz-* tokens in style.css).
var VIZ = {};

var VIZ_TOKENS = {
  base:       '--viz-base',        // default element (bar, node, cell)
  idle:       '--viz-idle',        // untouched / unvisited / eliminated
  active:     '--viz-active',      // current, comparing, pointer
  swap:       '--viz-swap',        // swapping, queued, active window
  done:       '--viz-done',        // sorted, found, finalized
  alt:        '--viz-alt',         // visited, secondary marker
  alt2:       '--viz-alt2',        // third marker (bidirectional search)
  danger:     '--viz-danger',      // evicted, negative cycle, failure
  wall:       '--viz-wall',        // blocked cell
  label:      '--viz-label',       // text on the page background
  muted:      '--viz-muted',       // secondary canvas text
  dim:        '--viz-dim',         // tertiary canvas text
  line:       '--viz-line',        // edges, axes, grid
  surface:    '--viz-surface',     // empty cell / panel fill
  surfaceAlt: '--viz-surface-alt', // faint state fill
  onFill:     '--viz-on-fill'      // text sitting on an accent fill
};

function cssVar(name) {
  return getComputedStyle(document.documentElement).getPropertyValue(name).trim();
}

function refreshViz() {
  for (var key in VIZ_TOKENS) {
    if (Object.prototype.hasOwnProperty.call(VIZ_TOKENS, key)) {
      VIZ[key] = cssVar(VIZ_TOKENS[key]);
    }
  }
  // Categorical ramp for "N distinct groups" (union-find components etc).
  // Refill in place: consumers capture the array once and hold the reference,
  // so replacing it would leave them drawing with the previous theme's colours.
  VIZ.categorical = VIZ.categorical || [];
  VIZ.categorical.length = 0;
  for (var i = 1; i <= 8; i++) VIZ.categorical.push(cssVar('--viz-cat-' + i));
  return VIZ;
}
refreshViz();

// Ink that stays readable on top of `bg`. Use it wherever the fill underneath
// a label can be either a bright accent or a dim surface — a fixed --viz-on-fill
// would vanish on one of them.
VIZ.on = function(bg) {
  var rgb = parseColor(bg);
  if (!rgb) return VIZ.onFill;
  // Rec. 601 luma is good enough to pick between black and white ink.
  var luma = (rgb[0] * 299 + rgb[1] * 587 + rgb[2] * 114) / 1000;
  return luma > 140 ? '#000000' : '#ffffff';
};

// Translucent variant of a palette colour, for highlight washes.
VIZ.alpha = function(color, a) {
  var rgb = parseColor(color);
  if (!rgb) return color;
  return 'rgba(' + rgb[0] + ',' + rgb[1] + ',' + rgb[2] + ',' + a + ')';
};

function parseColor(c) {
  if (!c) return null;
  c = String(c).trim();
  var m = c.match(/^#([0-9a-f]{3}|[0-9a-f]{6})$/i);
  if (m) {
    var h = m[1];
    if (h.length === 3) h = h[0] + h[0] + h[1] + h[1] + h[2] + h[2];
    return [parseInt(h.slice(0, 2), 16), parseInt(h.slice(2, 4), 16), parseInt(h.slice(4, 6), 16)];
  }
  m = c.match(/^rgba?\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)/i);
  return m ? [+m[1], +m[2], +m[3]] : null;
}

// ── Canvas quality ───────────────────────────────────────────────────────
// Every page sizes its canvas the same way — `canvas.width = <css pixels>` in a
// resize()/draw() function — which on a Retina display paints a 1x bitmap into
// a 2x box: every label and every 1px rule ships blurred. Every page also asks
// for `11px sans-serif`, which renders in whatever Helvetica-alike the platform
// picks rather than in the site's own face.
//
// Both are fixed here rather than in 36 inline scripts, by wrapping the one call
// all of them make first: getContext('2d').
//
//   * HiDPI — `width`/`height` become accessors on that one element. A page
//     still writes and reads CSS pixels; behind them the backing store is
//     devicePixelRatio times bigger and the context is pre-scaled, so existing
//     draw code and existing mouse-coordinate maths are unchanged.
//   * Type — the `font` setter rewrites the generic families to the site's
//     stack and lifts the smallest sizes one step. Digits become tabular, which
//     is what a column of array values wants.
//
// A page that genuinely needs the raw backing store can read canvas.deviceScale.

var VIZ_FONT_STACK = "'SF Mono','JetBrains Mono','IBM Plex Mono',ui-monospace," +
                     "'Cascadia Mono','Fira Code','Menlo','Courier New',monospace";

// Canvas font shorthand in the visualizer's face. `VIZ.font(12, 'bold')`.
VIZ.font = function(px, weight) {
  return (weight ? weight + ' ' : '') + px + 'px ' + VIZ_FONT_STACK;
};

// Bar/cell labels are drawn at 9-11px so they fit; on a sharp canvas one step up
// is still comfortably inside the same box and much easier to read.
function bumpFontSize(px) { return px <= 11 ? px + 1 : px; }

function normalizeCanvasFont(value) {
  var s = String(value);
  // <style?> <weight?> <size>px <family...> — only the trailing generic family
  // is replaced, so a page that names a real face keeps it.
  return s.replace(/(\d+(?:\.\d+)?)px/, function(_, n) {
    return bumpFontSize(parseFloat(n)) + 'px';
  }).replace(/\b(sans-serif|serif|monospace|system-ui)\s*$/, VIZ_FONT_STACK);
}

function upgradeCanvas(canvas, ctx) {
  if (canvas.__vizUpgraded) return;
  canvas.__vizUpgraded = true;

  // --- type ---
  var fontDesc = Object.getOwnPropertyDescriptor(CanvasRenderingContext2D.prototype, 'font');
  if (fontDesc && fontDesc.set) {
    Object.defineProperty(ctx, 'font', {
      configurable: true,
      get: function() { return fontDesc.get.call(ctx); },
      set: function(v) { fontDesc.set.call(ctx, normalizeCanvasFont(v)); }
    });
  }

  // --- resolution ---
  var dpr = Math.min(window.devicePixelRatio || 1, 2);
  canvas.deviceScale = dpr;
  if (dpr === 1) return;

  var wDesc = Object.getOwnPropertyDescriptor(HTMLCanvasElement.prototype, 'width');
  var hDesc = Object.getOwnPropertyDescriptor(HTMLCanvasElement.prototype, 'height');
  if (!wDesc || !hDesc) return;

  var cssW = wDesc.get.call(canvas), cssH = hDesc.get.call(canvas);

  function apply() {
    // Writing either attribute resets the bitmap *and* the transform, so the
    // scale has to be re-applied every time — which is also why both are set
    // together instead of one per accessor.
    wDesc.set.call(canvas, Math.round(cssW * dpr));
    hDesc.set.call(canvas, Math.round(cssH * dpr));
    canvas.style.width = cssW + 'px';
    canvas.style.height = cssH + 'px';
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
  }

  Object.defineProperty(canvas, 'width', {
    configurable: true,
    get: function() { return cssW; },
    set: function(v) { cssW = v; apply(); }
  });
  Object.defineProperty(canvas, 'height', {
    configurable: true,
    get: function() { return cssH; },
    set: function(v) { cssH = v; apply(); }
  });
}

(function patchGetContext() {
  var native = HTMLCanvasElement.prototype.getContext;
  HTMLCanvasElement.prototype.getContext = function(type) {
    var ctx = native.apply(this, arguments);
    if (ctx && (type === '2d' || type === undefined)) {
      try { upgradeCanvas(this, ctx); } catch (e) { /* never block a drawing */ }
    }
    return ctx;
  };
})();

// ── Canvas drawing kit ───────────────────────────────────────────────────
// The array-shaped visualizations each drew their own bars, their own value
// labels and their own pointer letters, which is why no two of them framed the
// same idea the same way. These are the shared parts — used by two-pointers and
// sliding-window, and where the rest are reworked they should use them too.
//
// Everything here draws in CSS pixels and takes colours from VIZ, so a page
// using them inherits both the HiDPI scaling and the palette.

// A bar with its value above it. `state` picks the treatment:
//   'normal'  solid fill
//   'muted'   the eliminated / out-of-window half — visibly out of play
VIZ.bar = function(ctx, x, y, w, h, color, value, state) {
  var muted = state === 'muted';
  ctx.save();
  if (muted) ctx.globalAlpha = 0.28;
  ctx.fillStyle = color;
  ctx.beginPath();
  ctx.roundRect(x, y, w, Math.max(h, 2), 3);
  ctx.fill();
  ctx.restore();

  if (value === undefined || value === null) return;
  ctx.fillStyle = muted ? VIZ.dim : VIZ.label;
  ctx.font = VIZ.font(Math.max(9, Math.min(12, w - 2)), muted ? '' : 'bold');
  ctx.textAlign = 'center';
  ctx.fillText(value, x + w / 2, y - 6);
};

// The rule the bars stand on, plus the index under each one. An array
// visualization without indices asks the reader to count columns.
VIZ.axis = function(ctx, x0, x1, y, n, startX, barW, gap, marks) {
  ctx.strokeStyle = VIZ.line;
  ctx.lineWidth = 1;
  ctx.beginPath();
  ctx.moveTo(x0, y + 0.5);
  ctx.lineTo(x1, y + 0.5);
  ctx.stroke();

  ctx.textAlign = 'center';
  for (var i = 0; i < n; i++) {
    var cx = startX + i * (barW + gap) + barW / 2;
    var mark = marks && marks[i];
    ctx.fillStyle = mark ? mark.color : VIZ.dim;
    ctx.font = VIZ.font(11, mark ? 'bold' : '');
    ctx.fillText(i, cx, y + 15);
  }
};

// A pointer sitting under the axis: a caret at the column and its name beside
// it. Two pointers on the same column are stacked rather than overprinted.
VIZ.pointer = function(ctx, cx, y, label, color, slot) {
  var dy = (slot || 0) * 15;
  ctx.fillStyle = color;
  ctx.beginPath();
  ctx.moveTo(cx, y + dy);
  ctx.lineTo(cx - 5, y + 7 + dy);
  ctx.lineTo(cx + 5, y + 7 + dy);
  ctx.closePath();
  ctx.fill();
  ctx.font = VIZ.font(12, 'bold');
  ctx.textAlign = 'center';
  ctx.fillText(label, cx, y + 19 + dy);
};

// A washed band behind a range of columns, with its own caption. This is what
// carries "the answer is somewhere in here" — the idea every one of these
// algorithms turns on.
VIZ.region = function(ctx, x0, x1, yTop, yBot, color, label, align) {
  ctx.save();
  ctx.fillStyle = VIZ.alpha(color, 0.10);
  ctx.fillRect(x0, yTop, x1 - x0, yBot - yTop);
  ctx.strokeStyle = color;
  ctx.lineWidth = 1.5;
  ctx.setLineDash([4, 3]);
  ctx.strokeRect(x0 + 0.5, yTop + 0.5, x1 - x0 - 1, yBot - yTop - 1);
  ctx.restore();
  if (!label) return;
  ctx.fillStyle = color;
  ctx.font = VIZ.font(11, 'bold');
  // Two regions that start on the same column would print their captions on
  // top of each other, so a caller can push one to the far end instead.
  ctx.textAlign = align === 'right' ? 'right' : 'left';
  ctx.fillText(label, align === 'right' ? x1 - 5 : x0 + 5, yTop - 5);
};

// The readout across the top: the two or three numbers the run is actually
// about, each in its own box so a changing value has a fixed place to change in.
VIZ.readout = function(ctx, x, y, items) {
  ctx.textAlign = 'left';
  ctx.textBaseline = 'middle';
  var h = 26, pad = 8, cx = x;
  for (var i = 0; i < items.length; i++) {
    var it = items[i];
    if (!it) continue;
    ctx.font = VIZ.font(11);
    var lw = ctx.measureText(it.label).width;
    ctx.font = VIZ.font(13, 'bold');
    var vw = ctx.measureText(String(it.value)).width;
    var w = pad + lw + 6 + vw + pad;

    ctx.fillStyle = it.color ? VIZ.alpha(it.color, 0.12) : VIZ.surface;
    ctx.fillRect(cx, y, w, h);
    ctx.strokeStyle = it.color || VIZ.line;
    ctx.lineWidth = 1;
    ctx.strokeRect(cx + 0.5, y + 0.5, w - 1, h - 1);

    ctx.fillStyle = VIZ.muted;
    ctx.font = VIZ.font(11);
    ctx.fillText(it.label, cx + pad, y + h / 2 + 1);
    ctx.fillStyle = it.color || VIZ.label;
    ctx.font = VIZ.font(13, 'bold');
    ctx.fillText(it.value, cx + pad + lw + 6, y + h / 2 + 1);

    cx += w + 8;
  }
  ctx.textBaseline = 'alphabetic';
};

// Wrap a page's draw(...) so the frame the run is on survives a repaint.
//
// Two spellings of the same bug were in the pages: passing `draw` straight to
// addEventListener hands it the resize Event as its first drawing argument,
// and the handlers that avoided that called draw() with reset arguments. Both
// silently threw the highlight away — and a theme switch fires a resize, so
// changing theme mid-run wiped the picture.
//
//   draw = VIZ.repaintable(draw);                       // after the function
//   window.addEventListener('resize', draw.repaint);    // replays the last call
VIZ.repaintable = function(draw) {
  var last = [];
  function wrapped() {
    last = Array.prototype.slice.call(arguments);
    return draw.apply(this, last);
  }
  wrapped.repaint = function() { return draw.apply(null, last); };
  return wrapped;
};

// ── Repaint on theme change ──────────────────────────────────────────────
// Canvases draw with the --viz-* tokens, which change with the theme. Every
// visualization already redraws on resize, so re-reading the palette and
// firing a resize repaints them without each page wiring up its own listener.
function initThemeRepaint() {
  document.addEventListener('cs:themechange', function() {
    refreshViz();
    window.dispatchEvent(new Event('resize'));
  });
}

// ── Step trace ───────────────────────────────────────────────────────────
// createLogger's contract with the pages is two calls, `clear()` and
// `log(html, className)`, and they are kept exactly as they were. What changed
// is what comes out the other end: instead of one flat run of <div>s, the three
// shapes the pages have always written are recognised and rendered as
// structure, so the trace can be skimmed rather than read.
//
//   logger.log('L=4 R=24 sum=28')                   → a numbered step
//   logger.log('&nbsp;&nbsp;sum > target → move R')  → the reason, tucked under
//                                                      the step it explains
//   logger.log('<span class="highlight">Found!</span>') → the outcome, called out
//   logger.log('--- Iteration 2 ---')                → a phase heading
//   logger.log('')                                   → ignored (rows are spaced)
//
// Recognising the leading &nbsp; is what does most of the work: a step and the
// decision it led to stop being two equal-weight lines and become one row.
//
// The outcome test is deliberately the *whole* message being one highlight
// span. Pages also use that span inline to pick out a value mid-sentence
// ("Process node <span class=highlight>1</span> dist=7"), and treating those as
// outcomes flags nine rows in ten — which marks nothing at all.

var LOG_INDENT = /^(?:&nbsp;|&#160;| |\s)+/;
var LOG_PHASE = /^-{2,}\s*(.+?)\s*-{2,}$/;
// The outcome test is deliberately the *whole* message being one highlight
// span. Pages also use that span inline to pick out a value mid-sentence
// ("Process node <span class=highlight>1</span> dist=7"), and counting those as
// outcomes flags nine rows in ten — which marks nothing at all.
var LOG_OUTCOME = /^<span class="highlight">(?:(?!<\/span>)[\s\S])*<\/span>$/;

function createLogger(containerId) {
  var el = document.getElementById(containerId);
  var count = 0;
  var lastStep = null;
  var countEl = null, emptyEl = null;

  function chrome() {
    // The panel's counter and its empty-state live next to the log; both are
    // optional so a page that has not been re-laid-out still works.
    var panel = el.closest ? el.closest('.viz-trace') : null;
    countEl = panel ? panel.querySelector('[data-trace-count]') : null;
    emptyEl = panel ? panel.querySelector('[data-trace-empty]') : null;
  }

  function sync() {
    if (!countEl) chrome();
    if (countEl) countEl.textContent = count === 1 ? '1 step' : count + ' steps';
    if (emptyEl) emptyEl.hidden = count > 0 || el.children.length > 0;
  }

  // Follow the tail only while the reader is already at the tail. Scrolling up
  // to re-read step 3 while a sort runs should not yank you back down.
  function atBottom() {
    return el.scrollHeight - el.scrollTop - el.clientHeight < 40;
  }
  function follow(wasAtBottom) {
    if (wasAtBottom) el.scrollTop = el.scrollHeight;
  }

  // Decorate `name=value` and the arrows so the eye can lock onto the changing
  // numbers. Only text nodes are touched, so a message that already carries
  // markup (every `<span class="highlight">` the pages write) is left intact.
  function decorate(node) {
    var walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT, null);
    var texts = [];
    while (walker.nextNode()) texts.push(walker.currentNode);
    for (var i = 0; i < texts.length; i++) {
      var t = texts[i];
      if (!/[A-Za-z_\]]\s*=|→|←|⇒/.test(t.nodeValue)) continue;
      var frag = document.createElement('span');
      frag.innerHTML = t.nodeValue
        .replace(/[&<>]/g, function(c) { return { '&': '&amp;', '<': '&lt;', '>': '&gt;' }[c]; })
        .replace(/([A-Za-z_][A-Za-z0-9_]*(?:\[[^\]]*\])*)\s*=\s*(-?[\w.]+)/g,
                 '<b class="k">$1</b>=<b class="v">$2</b>')
        .replace(/(→|←|⇒|&rarr;)/g, '<i class="arrow">$1</i>');
      t.parentNode.replaceChild(frag, t);
    }
  }

  function row(cls) {
    var div = document.createElement('div');
    div.className = 'step ' + cls;
    return div;
  }

  return {
    clear: function() {
      el.innerHTML = '';
      count = 0;
      lastStep = null;
      sync();
    },

    log: function(msg, cls) {
      var html = msg == null ? '' : String(msg);
      if (!html.replace(LOG_INDENT, '').trim()) return;   // spacer — rows are spaced already
      var wasAtBottom = atBottom();

      var phase = html.replace(LOG_INDENT, '').match(LOG_PHASE);
      if (phase) {
        var head = row('step-phase');
        head.innerHTML = '<span>' + phase[1] + '</span>';
        el.appendChild(head);
        lastStep = null;
        // A phase spends no step number, but it is still a row on screen:
        // sync() is what takes the empty-state prompt down, and bellman-ford
        // and floyd-warshall both open their run with one of these.
        sync();
        follow(wasAtBottom);
        return;
      }

      // An indented line explains the step above it: attach it there rather
      // than spending a step number on it.
      if (LOG_INDENT.test(html) && lastStep) {
        var note = document.createElement('div');
        note.className = 'step-note';
        var noteHtml = html.replace(LOG_INDENT, '').trim();
        note.innerHTML = noteHtml;
        decorate(note);
        if (LOG_OUTCOME.test(noteHtml)) note.classList.add('is-key');
        lastStep.querySelector('.step-body').appendChild(note);
        follow(wasAtBottom);
        return;
      }

      count++;
      var step = row('step-item' + (cls ? ' ' + cls : ''));
      var num = document.createElement('span');
      num.className = 'step-n';
      num.textContent = count;
      var body = document.createElement('div');
      body.className = 'step-body';
      var main = document.createElement('div');
      main.className = 'step-main';
      var mainHtml = html.replace(LOG_INDENT, '').trim();
      main.innerHTML = mainHtml;
      decorate(main);
      body.appendChild(main);
      step.appendChild(num);
      step.appendChild(body);

      if (LOG_OUTCOME.test(mainHtml)) step.classList.add('is-key');

      // The newest row carries the marker; the previous one gives it up.
      var prev = el.querySelector('.is-latest');
      if (prev) prev.classList.remove('is-latest');
      step.classList.add('is-latest');

      el.appendChild(step);
      lastStep = step;
      sync();
      follow(wasAtBottom);
    }
  };
}

// Wire the trace panel's own buttons. Pure chrome — nothing here is required
// for a page to run, so a panel without them (or a page without a panel) is
// simply skipped.
function initTracePanel() {
  var panel = document.querySelector('.viz-trace');
  if (!panel) return;
  var log = panel.querySelector('.viz-log');
  if (!log) return;

  var copy = panel.querySelector('[data-trace-copy]');
  if (copy) {
    copy.addEventListener('click', function() {
      var lines = [];
      log.querySelectorAll('.step').forEach(function(s) {
        var n = s.querySelector('.step-n');
        var main = s.querySelector('.step-main');
        if (s.classList.contains('step-phase')) { lines.push('\n== ' + s.textContent.trim() + ' =='); return; }
        lines.push((n ? n.textContent.padStart(3, ' ') + '. ' : '     ') + (main ? main.textContent.trim() : ''));
        s.querySelectorAll('.step-note').forEach(function(nt) {
          lines.push('       ' + nt.textContent.trim());
        });
      });
      // navigator.clipboard exists only in a secure context, so this is absent
      // when the built site is previewed over http from another machine.
      if (!navigator.clipboard) return;
      navigator.clipboard.writeText(lines.join('\n')).then(function() {
        var was = copy.textContent;
        copy.textContent = 'Copied';
        setTimeout(function() { copy.textContent = was; }, 1200);
      }, function() { /* clipboard blocked — nothing useful to say */ });
    });
  }

  // "Jump to latest" only earns its place once the reader has scrolled away
  // from the tail, so it shows itself.
  var jump = panel.querySelector('[data-trace-jump]');
  if (jump) {
    var update = function() {
      jump.hidden = log.scrollHeight - log.scrollTop - log.clientHeight < 40;
    };
    log.addEventListener('scroll', update);
    new MutationObserver(update).observe(log, { childList: true, subtree: true });
    jump.addEventListener('click', function() {
      log.scrollTo({ top: log.scrollHeight, behavior: 'smooth' });
    });
    update();
  }
}

// Sleep for animation
function sleep(ms) { return new Promise(function(r) { setTimeout(r, ms); }); }

// Shuffle array
function shuffle(arr) {
  for (var i = arr.length - 1; i > 0; i--) {
    var j = Math.floor(Math.random() * (i + 1));
    var tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
  }
  return arr;
}

// Generate random array
function randomArray(n, max) {
  var arr = [];
  for (var i = 0; i < n; i++) arr.push(Math.floor(Math.random() * max) + 1);
  return arr;
}

document.addEventListener('DOMContentLoaded', function() {
  refreshViz();
  initThemeRepaint();
  initTracePanel();
});
