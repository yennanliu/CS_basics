// Shared helpers for the Algorithm Visualizer pages.
//
// Theme is shared with the main CS_basics site: same `theme` localStorage key,
// same dark-by-default, so a choice made on either side carries across.

(function() {
  var saved = localStorage.getItem('theme') || 'dark';
  document.documentElement.setAttribute('data-theme', saved);
})();

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

// ── Theme toggle ─────────────────────────────────────────────────────────
function initThemeToggle() {
  var btn = document.getElementById('theme-toggle');
  if (!btn) return;
  var updateLabel = function() {
    btn.textContent = document.documentElement.getAttribute('data-theme') === 'dark'
      ? '☀ light' : '● dark';
  };
  updateLabel();
  btn.addEventListener('click', function() {
    var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    var next = isDark ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('theme', next);
    updateLabel();
    refreshViz();
    // Every visualization redraws on resize, so this repaints the canvas
    // with the new palette without each page wiring up its own listener.
    window.dispatchEvent(new Event('resize'));
  });
}

// ── Mobile nav ───────────────────────────────────────────────────────────
// Bound here rather than inline so the button's aria-expanded stays in step
// with the menu — a screen reader otherwise can't tell whether it's open.
function initNavToggle() {
  var btn = document.querySelector('.nav-toggle');
  var links = document.getElementById('nav-links');
  if (!btn || !links) return;
  btn.addEventListener('click', function() {
    var open = links.classList.toggle('open');
    btn.setAttribute('aria-expanded', open ? 'true' : 'false');
  });
}

// Logging helper
function createLogger(containerId) {
  var el = document.getElementById(containerId);
  return {
    clear: function() { el.innerHTML = ''; },
    log: function(msg, cls) {
      var div = document.createElement('div');
      div.className = 'step' + (cls ? ' ' + cls : '');
      div.innerHTML = msg;
      el.appendChild(div);
      el.scrollTop = el.scrollHeight;
    }
  };
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
  initThemeToggle();
  initNavToggle();
});
