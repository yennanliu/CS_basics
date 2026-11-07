/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — shared page behaviour for generated doc pages

   Content-level behaviour that used to be inlined into every generated page
   by site/build-site.js: copy-to-clipboard on code blocks, horizontal scroll
   wrappers around wide tables, and the reading progress bar.

   Navbar behaviour lives in nav.js, not here.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSSite = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // Called from the inline onclick that build-site.js emits on each copy
  // button, so it has to reach the page as a global (see the export below).
  function copyCode(btn) {
    var wrapper = btn.closest('.code-block-wrapper');
    var pre = wrapper ? wrapper.querySelector('pre') : null;
    var text = pre ? pre.innerText : '';
    if (!navigator.clipboard) return;
    navigator.clipboard.writeText(text).then(function () {
      btn.textContent = 'copied';
      btn.classList.add('copied');
      setTimeout(function () {
        btn.textContent = 'copy';
        btn.classList.remove('copied');
      }, 2000);
    });
  }

  // Markdown tables are emitted bare; wrapping them lets a wide table scroll
  // inside its own box instead of forcing the whole page sideways.
  function wrapTables(scope) {
    scope = scope || document;
    var tables = scope.querySelectorAll('table');
    for (var i = 0; i < tables.length; i++) {
      var table = tables[i];
      if (table.parentElement && table.parentElement.classList.contains('table-wrap')) continue;
      var wrapper = document.createElement('div');
      wrapper.className = 'table-wrap';
      table.parentNode.insertBefore(wrapper, table);
      wrapper.appendChild(table);
    }
    return tables.length;
  }

  function readingProgress(doc) {
    doc = doc || document.documentElement;
    var height = doc.scrollHeight - doc.clientHeight;
    return height > 0 ? (doc.scrollTop / height) * 100 : 0;
  }

  function initReadingProgress() {
    var bar = document.getElementById('reading-progress');
    if (!bar) return;
    var update = function () { bar.style.width = readingProgress() + '%'; };
    window.addEventListener('scroll', update);
    update();
  }

  // The TOC ships open so it works without JS. On narrow screens it would then
  // push the whole doc down, so collapse it there; on wide screens it is a
  // sticky rail and the current section is highlighted as you scroll.
  var TOC_WIDE_MIN = 1025;

  function initTOC(scope) {
    scope = scope || document;
    var toc = scope.querySelector('.toc[data-toc]');
    if (!toc) return null;

    var isWide = function () {
      return typeof window !== 'undefined' && window.innerWidth >= TOC_WIDE_MIN;
    };
    if (!isWide()) toc.open = false;

    var links = toc.querySelectorAll('.toc-item > a');
    var byId = {};
    var targets = [];
    for (var i = 0; i < links.length; i++) {
      var href = links[i].getAttribute('href') || '';
      if (href.charAt(0) !== '#') continue;
      var id = href.slice(1);
      var heading = scope.getElementById ? scope.getElementById(id) : document.getElementById(id);
      if (!heading) continue;
      byId[id] = links[i].parentElement;
      targets.push(heading);
    }
    if (!targets.length) return { toc: toc, sections: 0 };

    // A 90-entry rail is as unreadable as a 90-entry list. Past a threshold,
    // show only the top-level sections and expand the one being read. Applied
    // from JS so that without it the full nested list still renders.
    var list = toc.querySelector('.toc-list');
    var dense = list && list.querySelectorAll('.toc-sublist .toc-item').length > 24;
    if (dense) list.classList.add('toc-dense');

    var active = null;
    var openGroup = null;
    var setActive = function (id) {
      var item = byId[id];
      if (!item || item === active) return;
      if (active) active.classList.remove('is-active');
      item.classList.add('is-active');
      active = item;
      if (!dense) return;
      var group = item.classList.contains('toc-l2')
        ? item
        : item.closest('.toc-l2');
      if (group && group !== openGroup) {
        if (openGroup) openGroup.classList.remove('is-open');
        group.classList.add('is-open');
        openGroup = group;
      }
    };

    // Open the first group so a dense TOC is never fully collapsed on load.
    setActive(targets[0].id);

    if (typeof IntersectionObserver === 'function') {
      // A band near the top of the viewport decides which heading is "current".
      var observer = new IntersectionObserver(function (entries) {
        var visible = entries.filter(function (e) { return e.isIntersecting; });
        if (!visible.length) return;
        visible.sort(function (a, b) { return a.boundingClientRect.top - b.boundingClientRect.top; });
        setActive(visible[0].target.id);
      }, { rootMargin: '-72px 0px -70% 0px', threshold: 0 });
      for (var j = 0; j < targets.length; j++) observer.observe(targets[j]);
    }

    return { toc: toc, sections: targets.length };
  }

  // Cheatsheet index filter: a text box plus priority chips over the catalogue.
  // The markup ships fully expanded, so with JS off the catalogue is unchanged.
  // A sheet appears in both catalogue views (by priority and by topic), so the
  // count is of distinct sheets (data-file) in the views on screen.
  function initSheetFilter(scope) {
    scope = scope || document;
    var bar = scope.querySelector('[data-sheet-filter]');
    if (!bar) return null;

    var input = bar.querySelector('.filter-input');
    var chips = bar.querySelectorAll('.filter-chip');
    var status = bar.querySelector('.filter-status');
    var cards = scope.querySelectorAll('.sheet-card[data-search]');
    var sections = scope.querySelectorAll('.cat-section');
    var empty = scope.querySelector('.filter-empty');
    // Orientation for a first visit; once someone is filtering they are looking
    // for one sheet, so get it out of the way.
    var intro = [scope.querySelector('.tier-key'), scope.querySelector('.start-here'),
      scope.querySelector('.cat-jump')];
    var minTier = 0;

    function inHiddenView(el) {
      var panel = el.closest ? el.closest('[data-view-panel]') : null;
      return !!(panel && panel.hidden);
    }
    function distinct(list) {
      var seen = {};
      var n = 0;
      for (var i = 0; i < list.length; i++) {
        if (inHiddenView(list[i])) continue;
        var key = list[i].getAttribute('data-file') || ('#' + i);
        if (!seen[key]) { seen[key] = true; n++; }
      }
      return n;
    }
    var total = Number(status && status.getAttribute('data-total')) || distinct(cards);

    function apply() {
      var terms = (input && input.value || '').toLowerCase().split(/\s+/).filter(Boolean);
      var matched = [];
      for (var i = 0; i < cards.length; i++) {
        var card = cards[i];
        var hay = card.getAttribute('data-search') || '';
        var tier = Number(card.getAttribute('data-tier')) || 0;
        var match = tier >= minTier;
        for (var t = 0; match && t < terms.length; t++) {
          if (hay.indexOf(terms[t]) === -1) match = false;
        }
        card.hidden = !match;
        if (match) matched.push(card);
      }
      var shown = distinct(matched);
      // A group with nothing left in it should not leave a stray heading, and
      // its count has to describe what is on screen rather than the full group.
      for (var s = 0; s < sections.length; s++) {
        var visible = sections[s].querySelectorAll('.sheet-card:not([hidden])').length;
        sections[s].hidden = visible === 0;
        var count = sections[s].querySelector('.cat-count');
        if (count) {
          if (!count.getAttribute('data-full')) count.setAttribute('data-full', count.textContent);
          var full = count.getAttribute('data-full');
          count.textContent = visible === sections[s].querySelectorAll('.sheet-card').length
            ? full
            : visible + ' of ' + full;
        }
      }
      var filtering = terms.length > 0 || minTier > 0;
      for (var n = 0; n < intro.length; n++) if (intro[n]) intro[n].hidden = filtering;
      if (empty) empty.hidden = shown !== 0 || bar.hidden;
      if (status) {
        status.textContent = filtering
          ? shown + ' of ' + total + ' sheets'
          : total + ' sheets';
      }
      return shown;
    }

    function setTier(value, chosen) {
      minTier = value;
      for (var i = 0; i < chips.length; i++) chips[i].classList.toggle('is-on', chips[i] === chosen);
      apply();
    }

    if (input) input.addEventListener('input', apply);
    for (var c = 0; c < chips.length; c++) {
      (function (chip) {
        chip.addEventListener('click', function () {
          setTier(Number(chip.getAttribute('data-min-tier')) || 0, chip);
        });
      })(chips[c]);
    }
    var reset = empty && empty.querySelector('.filter-reset');
    if (reset) {
      reset.addEventListener('click', function () {
        if (input) input.value = '';
        setTier(0, chips[0]);
        if (input) input.focus();
      });
    }

    apply();
    bar._apply = apply;
    return { cards: cards.length, apply: apply };
  }

  // ── Small storage helpers: every access can throw (private mode, blocked site
  // data), and the page has to work the same without it.
  function readStore(storage, key, fallback) {
    try {
      var raw = storage && storage.getItem(key);
      return raw ? JSON.parse(raw) : fallback;
    } catch (e) { return fallback; }
  }
  function writeStore(storage, key, value) {
    try { if (storage) storage.setItem(key, JSON.stringify(value)); } catch (e) { /* ignore */ }
  }
  // A malformed hash (`#%E0%A4%A`) makes decodeURIComponent throw; that must not
  // stop the rest of init, so fall back to the raw string.
  function safeDecode(s) {
    try { return decodeURIComponent(s); } catch (e) { return s; }
  }
  function defaultStorage() {
    try { return typeof localStorage !== 'undefined' ? localStorage : null; } catch (e) { return null; }
  }

  // Cheatsheet index views: the server renders the review path and both
  // catalogue views one after another; this turns them into tabs. The view comes
  // from the URL hash first (so #review-path and #arrays-strings both land
  // somewhere visible), then the reader's last choice, then "by priority".
  var VIEW_KEY = 'cs-index-view';

  function initIndexViews(scope, opts) {
    scope = scope || document;
    opts = opts || {};
    var bar = scope.querySelector('[data-cs-tabs]');
    if (!bar) return null;
    var storage = opts.storage !== undefined ? opts.storage : defaultStorage();
    var tabs = bar.querySelectorAll('[data-view]');
    var panels = scope.querySelectorAll('[data-view-panel]');
    var filter = scope.querySelector('[data-sheet-filter]');

    // The server ships plain links (with JS off the views are simply stacked);
    // the tab pattern's roles only make sense once the views really are tabs.
    bar.setAttribute('role', 'tablist');
    for (var r = 0; r < tabs.length; r++) {
      var ref = (tabs[r].getAttribute('href') || '').replace(/^#/, '');
      tabs[r].setAttribute('role', 'tab');
      if (ref) tabs[r].setAttribute('aria-controls', ref);
    }
    for (var q = 0; q < panels.length; q++) panels[q].setAttribute('role', 'tabpanel');

    function panelFor(id) {
      if (!id) return null;
      var el = scope.getElementById ? scope.getElementById(id) : scope.querySelector('#' + id);
      if (!el) return null;
      return el.hasAttribute('data-view-panel') ? el : el.closest('[data-view-panel]');
    }

    function show(view, remember) {
      var found = false;
      for (var i = 0; i < panels.length; i++) {
        var on = panels[i].getAttribute('data-view-panel') === view;
        panels[i].hidden = !on;
        if (on) found = true;
      }
      if (!found) return false;
      for (var j = 0; j < tabs.length; j++) {
        var active = tabs[j].getAttribute('data-view') === view;
        tabs[j].classList.toggle('is-on', active);
        tabs[j].setAttribute('aria-selected', active ? 'true' : 'false');
      }
      // The filter narrows the catalogue; the review path is a fixed list.
      if (filter) {
        filter.hidden = view === 'path';
        if (filter._apply) filter._apply();
      }
      if (remember) writeStore(storage, VIEW_KEY, view);
      current = view;
      return true;
    }

    var current = null;
    var hash = (opts.hash !== undefined ? opts.hash
      : (typeof location !== 'undefined' ? location.hash : '')).replace(/^#/, '');
    var fromHash = panelFor(hash ? safeDecode(hash) : '');
    var initial = (fromHash && fromHash.getAttribute('data-view-panel')) ||
      readStore(storage, VIEW_KEY, null) || 'priority';
    if (!show(initial, false)) show('priority', false);

    for (var k = 0; k < tabs.length; k++) {
      (function (tab) {
        tab.addEventListener('click', function (e) {
          if (e && e.preventDefault) e.preventDefault();
          var view = tab.getAttribute('data-view');
          show(view, true);
          if (typeof history !== 'undefined' && history.replaceState && tab.getAttribute('href')) {
            try { history.replaceState(null, '', tab.getAttribute('href')); } catch (err) { /* ignore */ }
          }
        });
      })(tabs[k]);
    }

    if (typeof window !== 'undefined' && window.addEventListener && !opts.noHashListener) {
      window.addEventListener('hashchange', function () {
        var id = safeDecode(location.hash.replace(/^#/, ''));
        var p = panelFor(id);
        if (p && p.hidden) {
          show(p.getAttribute('data-view-panel'), false);
          var target = scope.getElementById(id);
          if (target && target.scrollIntoView) target.scrollIntoView();
        }
      });
    }

    return { show: show, current: function () { return current; } };
  }

  // The review path: per-browser "reviewed on" marks over the core sheets. A
  // mark comes due again after the tier's interval (data-intervals, in days),
  // and "next up" is the most overdue sheet, else the first never reviewed, in
  // path order. The practice log stays the record of problems solved — this is
  // only a reading checklist, so it lives in localStorage like the roadmap's
  // unjudged ticks.
  var REVIEW_KEY = 'cs-review-path';
  var DAY = 86400000;

  function dayNumber(d) {
    return Math.floor(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()) / DAY);
  }
  function isoDay(d) {
    var m = d.getMonth() + 1, day = d.getDate();
    return d.getFullYear() + '-' + (m < 10 ? '0' : '') + m + '-' + (day < 10 ? '0' : '') + day;
  }
  function parseIsoDay(s) {
    var m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(s || '');
    return m ? dayNumber(new Date(+m[1], +m[2] - 1, +m[3])) : null;
  }
  function fill(template, vars) {
    return String(template).replace(/\{(\w+)\}/g, function (all, k) { return vars[k] != null ? vars[k] : all; });
  }

  function initReviewPath(scope, opts) {
    scope = scope || document;
    opts = opts || {};
    var root = scope.querySelector('[data-review-path]');
    if (!root) return null;
    var storage = opts.storage !== undefined ? opts.storage : defaultStorage();
    var now = function () { return opts.now ? opts.now() : new Date(); };
    var S = readJSONAttr(root, 'data-strings', {});
    var intervals = readJSONAttr(root, 'data-intervals', {});
    var steps = root.querySelectorAll('.path-step[data-file]');
    var summary = root.querySelector('.path-summary');
    var nextBox = root.querySelector('.path-next');
    var fillBar = root.querySelector('.path-meter-fill');
    var countEl = root.querySelector('.path-count');
    var resetBtn = root.querySelector('.path-reset');
    var marks = readStore(storage, REVIEW_KEY, {});
    // Anything but a plain object (an array, a string, null) is not ours to edit.
    if (!marks || typeof marks !== 'object' || Array.isArray(marks)) marks = {};

    function readJSONAttr(el, name, fallback) {
      try { return JSON.parse(el.getAttribute(name) || 'null') || fallback; } catch (e) { return fallback; }
    }

    function stateOf(step) {
      var file = step.getAttribute('data-file');
      var tier = step.getAttribute('data-tier');
      var interval = Number(intervals[tier]) || 14;
      var day = parseIsoDay(marks[file]);
      if (day == null) return { file: file, reviewed: false, due: false, overdue: 0 };
      var age = dayNumber(now()) - day;
      return { file: file, reviewed: true, age: age, due: age >= interval,
        overdue: age - interval, left: interval - age };
    }

    // Buttons are created here rather than shipped, so a reader without JS
    // never sees a control that does nothing.
    for (var i = 0; i < steps.length; i++) {
      (function (step) {
        var slot = step.querySelector('.step-mark');
        if (!slot || slot.querySelector('.step-toggle')) return;
        var btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'step-toggle';
        btn.addEventListener('click', function () { toggle(step.getAttribute('data-file')); });
        slot.appendChild(btn);
      })(steps[i]);
    }

    function toggle(file) {
      var st = null;
      for (var i = 0; i < steps.length; i++) {
        if (steps[i].getAttribute('data-file') === file) st = stateOf(steps[i]);
      }
      // Reviewed and not yet due: the button is an undo. Otherwise it records today.
      if (st && st.reviewed && !st.due) delete marks[file];
      else marks[file] = isoDay(now());
      writeStore(storage, REVIEW_KEY, marks);
      render();
    }

    function render() {
      var done = 0, next = null, overdue = null;
      for (var i = 0; i < steps.length; i++) {
        var step = steps[i];
        var st = stateOf(step);
        var status = step.querySelector('.step-status');
        var btn = step.querySelector('.step-toggle');
        var fresh = st.reviewed && !st.due;
        step.classList.toggle('is-done', fresh);
        step.classList.toggle('is-due', st.due);
        step.classList.remove('is-next');
        if (fresh) done++;
        if (status) {
          status.textContent = !st.reviewed ? S.never
            : st.due ? S.due
            : (st.age === 0 ? S.today : fill(S.daysAgo, { n: st.age })) + ' · ' + fill(S.dueIn, { n: st.left });
        }
        if (btn) {
          btn.textContent = fresh ? S.unmark : S.mark;
          btn.setAttribute('aria-pressed', fresh ? 'true' : 'false');
        }
        if (st.due && (!overdue || st.overdue > overdue.st.overdue)) overdue = { step: step, st: st };
        if (!st.reviewed && !next) next = step;
      }
      var pick = overdue ? overdue.step : next;
      if (pick) pick.classList.add('is-next');

      if (summary) summary.hidden = false;
      if (fillBar) fillBar.style.width = (steps.length ? (100 * done / steps.length) : 0).toFixed(1) + '%';
      if (countEl) countEl.textContent = fill(S.progress || '{n} / {total}', { n: done, total: steps.length });
      if (nextBox) {
        nextBox.textContent = '';
        if (!pick) {
          var p = document.createElement('p');
          p.className = 'path-all-done';
          p.textContent = S.allDone || '';
          nextBox.appendChild(p);
        } else {
          var link = pick.querySelector('.step-title');
          var why = pick.querySelector('.step-why');
          var label = document.createElement('span');
          label.className = 'path-next-label';
          label.textContent = (S.nextUp || 'Next up') + ' · #' + pick.querySelector('.step-no').textContent;
          var title = document.createElement('a');
          title.className = 'path-next-title';
          title.href = link.getAttribute('href');
          title.textContent = link.textContent;
          nextBox.appendChild(label);
          nextBox.appendChild(title);
          if (why) {
            var w = document.createElement('p');
            w.className = 'path-next-why';
            w.textContent = why.textContent;
            nextBox.appendChild(w);
          }
          var actions = document.createElement('div');
          actions.className = 'path-next-actions';
          var open = document.createElement('a');
          open.className = 'path-btn path-btn-primary';
          open.href = link.getAttribute('href');
          open.textContent = S.open || 'Open';
          var mark = document.createElement('button');
          mark.type = 'button';
          mark.className = 'path-btn';
          mark.textContent = S.mark;
          var file = pick.getAttribute('data-file');
          mark.addEventListener('click', function () { toggle(file); });
          actions.appendChild(open);
          actions.appendChild(mark);
          nextBox.appendChild(actions);
        }
      }
      return { done: done, total: steps.length, next: pick ? pick.getAttribute('data-file') : null };
    }

    if (resetBtn) {
      resetBtn.addEventListener('click', function () {
        if (typeof confirm === 'function' && !opts.noConfirm && !confirm(resetBtn.textContent + '?')) return;
        marks = {};
        writeStore(storage, REVIEW_KEY, marks);
        render();
      });
    }

    var first = render();
    return { render: render, toggle: toggle, state: first };
  }

  function init() {
    wrapTables(document);
    initReadingProgress();
    initTOC(document);
    initSheetFilter(document);
    initIndexViews(document);
    initReviewPath(document);
  }

  if (typeof document !== 'undefined' && typeof document.addEventListener === 'function') {
    document.addEventListener('DOMContentLoaded', init);
    // The copy buttons are wired with inline onclick attributes, which resolve
    // against the global scope rather than this module.
    if (typeof self !== 'undefined') self.copyCode = copyCode;
  }

  return {
    copyCode: copyCode,
    wrapTables: wrapTables,
    readingProgress: readingProgress,
    initReadingProgress: initReadingProgress,
    initTOC: initTOC,
    initSheetFilter: initSheetFilter,
    initIndexViews: initIndexViews,
    initReviewPath: initReviewPath,
    init: init
  };
});
