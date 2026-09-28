/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — the problem index's filter

   problems.html is README rendered whole: 60 tables and ~2,800 problem rows
   in one 1.8 MB document. That shape is the point — GitHub stops rendering
   markdown at 512 KB and README passed 1.1 MB, so this page is the only
   place the whole index can actually be read. What the shape costs is
   finding anything in it: before this, locating LC 239 meant Ctrl-F through
   1.8 MB and landing on whichever of its four rows came first.

   So the rows are filtered in place rather than paged, split or re-rendered
   from a second copy of the data. The page still *is* the README — a query
   only hides the rows that do not match, along with any heading and prose
   left with nothing under it. Clear the box and the document is byte-for-byte
   what it was.

   Everything above boot() is pure and takes its DOM as an argument, so
   site/test/problems-filter.test.js can drive it against the real built page
   in jsdom rather than against a fixture that agrees with it by construction.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSProblems = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // One class, one rule. Toggling `display` inline instead would fight the
  // `tbody tr:nth-child(even)` striping that style.css puts on these tables.
  var HIDDEN = 'pf-off';

  // ── Indexing ───────────────────────────────────────────────────────────

  // Which column holds what, read off <thead> rather than assumed. 57 of
  // README's 60 tables share one 8-column shape (# / Title / Solution / Time /
  // Space / Difficulty / Note / Status) and three do not, so a fixed index
  // would silently read "Use case" as a difficulty on those three.
  function columnMap(table) {
    var map = { num: -1, difficulty: -1, status: -1, note: -1 };
    var ths = table.querySelectorAll('thead th');
    for (var i = 0; i < ths.length; i++) {
      var name = ths[i].textContent.trim().toLowerCase();
      if (name === '#') map.num = i;
      else if (name === 'difficulty') map.difficulty = i;
      else if (name === 'status') map.status = i;
      else if (name === 'note' || name === 'comment') map.note = i;
    }
    return map;
  }

  function readRow(tr, map, trail) {
    var cells = tr.children;
    var cell = function (i) { return i >= 0 && cells[i] ? cells[i].textContent : ''; };

    var status = cell(map.status).toUpperCase();
    var note = cell(map.note).toUpperCase();
    var again = status.indexOf('AGAIN') !== -1;
    var digits = cell(map.num).replace(/\D/g, '');

    return {
      el: tr,
      // The heading trail is part of what a row matches on, so "sliding window"
      // returns that section's rows and not only the one problem carrying the
      // words in its title.
      text: (trail + ' ' + tr.textContent).replace(/\s+/g, ' ').toLowerCase(),
      // README writes its numbers zero-padded ("0239"). Stripped here so a
      // digit query can be matched exactly — see matchRow.
      num: digits ? String(Number(digits)) : '',
      difficulty: cell(map.difficulty).trim().toLowerCase(),
      // "AGAIN (not start)", "AGAIN****** (2)(MUST)", "OK* (4)" — the word is
      // the signal, the star run is the pass count, and an empty cell is a row
      // nobody has reached yet.
      status: again ? 'again' : status.indexOf('OK') !== -1 ? 'ok' : 'todo',
      // MUST is written into the Note column on some rows and the Status column
      // on others, and LC 27 carries it in both. Either one counts.
      must: status.indexOf('MUST') !== -1 || note.indexOf('MUST') !== -1
    };
  }

  // Splits the rendered README into sections — a heading plus the blocks under
  // it — because hiding rows alone leaves a page of headings standing over
  // nothing, which reads as "no results" repeated 60 times.
  function indexContent(scope) {
    var sections = [];
    var stack = [];
    var current = { heading: null, level: 0, blocks: [], rows: [] };
    sections.push(current);

    var kids = scope.children;
    for (var i = 0; i < kids.length; i++) {
      var el = kids[i];
      // The filter bar and the empty-state line are rendered inside .content
      // too, and are not part of the document being filtered — without this
      // the first query hides the box you just typed into.
      if (el.hasAttribute('data-pf-chrome')) continue;
      var heading = /^H[1-6]$/.test(el.tagName);

      if (heading) {
        var level = Number(el.tagName.charAt(1));
        while (stack.length && stack[stack.length - 1].level >= level) stack.pop();
        stack.push({ level: level, text: el.textContent });
        // The whole outline path, so a row under "Newly Added" → "Array"
        // answers to either word.
        var trail = stack.map(function (s) { return s.text; }).join(' ');
        current = { heading: el, level: level, trail: trail, blocks: [], rows: [] };
        sections.push(current);
        continue;
      }

      current.blocks.push(el);
      var tables = el.tagName === 'TABLE' ? [el] : el.querySelectorAll('table');
      for (var t = 0; t < tables.length; t++) {
        var map = columnMap(tables[t]);
        var trs = tables[t].querySelectorAll('tbody tr');
        for (var r = 0; r < trs.length; r++) {
          var row = readRow(trs[r], map, current.trail || '');
          // The top-level block the row sits in, resolved once. Asking
          // `block.contains(tr)` per row per keystroke instead is 2,800
          // tree walks for every character typed.
          row.block = el;
          current.rows.push(row);
        }
      }
    }
    return sections;
  }

  // ── State ──────────────────────────────────────────────────────────────

  function emptyState() {
    return { terms: [], difficulty: [], status: [], must: false };
  }

  function isActive(state) {
    return state.terms.length > 0 || state.difficulty.length > 0 ||
           state.status.length > 0 || state.must;
  }

  function terms(query) {
    return String(query || '').toLowerCase().split(/\s+/).filter(Boolean);
  }

  function matchRow(row, state) {
    if (state.difficulty.length && state.difficulty.indexOf(row.difficulty) === -1) return false;
    if (state.status.length && state.status.indexOf(row.status) === -1) return false;
    if (state.must && !row.must) return false;

    for (var i = 0; i < state.terms.length; i++) {
      var term = state.terms[i];
      // A bare number is a problem number. Substring-matching it instead would
      // make "239" return LC 1239, LC 2390 and every row whose time bound
      // happens to read O(239) — 26 rows for "26" is not a search result.
      if (/^\d+$/.test(term)) {
        if (row.num !== String(Number(term))) return false;
      } else if (row.text.indexOf(term) === -1) {
        return false;
      }
    }
    return true;
  }

  // ── Applying ───────────────────────────────────────────────────────────

  // Only writes to an element whose visibility actually changed. Every
  // keystroke re-evaluates ~2,800 rows; re-setting the class on all of them
  // each time is what turns a filter into a stutter.
  function setHidden(el, hide) {
    if (el.classList.contains(HIDDEN) === hide) return;
    if (hide) el.classList.add(HIDDEN);
    else el.classList.remove(HIDDEN);
  }

  // style.css stripes these tables with `tbody tr:nth-child(even)`, which counts
  // the hidden rows too — filtering it leaves stripes in a random-looking
  // pattern that reads as a rendering bug. So the stripe is carried by a class
  // over the rows still showing, and style.css hands nth-child over to it for
  // as long as the script is running.
  var ALT = 'pf-alt';
  function setAlt(el, alt) {
    if (el.classList.contains(ALT) === alt) return;
    if (alt) el.classList.add(ALT);
    else el.classList.remove(ALT);
  }

  function apply(sections, state) {
    var active = isActive(state);
    var shown = 0, total = 0;

    for (var i = 0; i < sections.length; i++) {
      var section = sections[i];
      var visibleRows = 0;
      var live = [];
      var stripe = [];

      for (var r = 0; r < section.rows.length; r++) {
        var row = section.rows[r];
        total++;
        var keep = !active || matchRow(row, state);
        if (keep) {
          visibleRows++;
          shown++;
          // Per table, not per section: the stripe restarts with each <tbody>.
          var at = live.indexOf(row.block);
          if (at === -1) { live.push(row.block); stripe.push(0); at = live.length - 1; }
          setAlt(row.el, stripe[at]++ % 2 === 1);
        } else {
          setAlt(row.el, false);
        }
        setHidden(row.el, !keep);
      }
      section.visible = !active || visibleRows > 0;
      section.parentOnly = false;
      section.live = live;
    }

    // A parent heading survives on its children. "## Newly Added (kamyu104
    // gap)" owns no rows of its own — hiding it would file its 22 sub-tables
    // under nothing and make them look like part of the main index.
    var keptBelow = [];
    for (var s = sections.length - 1; s >= 0; s--) {
      var sec = sections[s];
      if (!sec.visible) {
        for (var l = sec.level + 1; l <= 6; l++) {
          if (keptBelow[l]) { sec.visible = true; sec.parentOnly = true; break; }
        }
      }
      for (var c = sec.level; c <= 6; c++) keptBelow[c] = false;
      if (sec.visible) keptBelow[sec.level] = true;
    }

    for (var j = 0; j < sections.length; j++) {
      var sj = sections[j];
      if (sj.heading) setHidden(sj.heading, !sj.visible);
      for (var b = 0; b < sj.blocks.length; b++) {
        var block = sj.blocks[b];
        // While filtering, a block earns its place by containing a matching
        // row. The prose and the code fences between the tables are context
        // for reading the index top to bottom, not answers to a query.
        var keepBlock = !active || (sj.visible && sj.live.indexOf(block) !== -1);
        setHidden(block, !keepBlock);
      }
    }

    return { shown: shown, total: total, active: active };
  }

  // ── URL ────────────────────────────────────────────────────────────────

  // A filtered view is worth linking to — "the Hard graph problems still
  // marked AGAIN" is a message you send yourself, and it survives a reload.
  function readQuery(search) {
    var params = new URLSearchParams(search || '');
    var list = function (key) {
      return (params.get(key) || '').split(',').map(function (v) {
        return v.trim().toLowerCase();
      }).filter(Boolean);
    };
    return {
      query: params.get('q') || '',
      terms: terms(params.get('q')),
      difficulty: list('difficulty'),
      status: list('status'),
      must: params.get('must') === '1'
    };
  }

  function writeQuery(state, query) {
    var params = new URLSearchParams();
    if (query) params.set('q', query);
    if (state.difficulty.length) params.set('difficulty', state.difficulty.join(','));
    if (state.status.length) params.set('status', state.status.join(','));
    if (state.must) params.set('must', '1');
    var s = params.toString();
    return s ? '?' + s : '';
  }

  // ── Boot ───────────────────────────────────────────────────────────────

  function boot(doc) {
    doc = doc || document;
    var bar = doc.getElementById('problem-filter');
    var scope = doc.querySelector('.content');
    if (!bar || !scope) return null;

    var sections = indexContent(scope);
    var input = doc.getElementById('q');
    var count = doc.getElementById('pf-count');
    var empty = doc.getElementById('pf-empty');
    var clear = doc.getElementById('pf-clear');
    var chips = bar.querySelectorAll('[data-facet]');

    var initial = readQuery(doc.defaultView ? doc.defaultView.location.search : '');
    var state = emptyState();
    state.difficulty = initial.difficulty;
    state.status = initial.status;
    state.must = initial.must;
    state.terms = initial.terms;
    if (input) input.value = initial.query;

    for (var i = 0; i < chips.length; i++) {
      var chip = chips[i];
      var facet = chip.getAttribute('data-facet');
      var value = (chip.getAttribute('data-value') || '').toLowerCase();
      var on = facet === 'must' ? state.must : state[facet].indexOf(value) !== -1;
      chip.setAttribute('aria-pressed', on ? 'true' : 'false');
    }

    // The bar ships hidden so that a reader with no JavaScript gets the plain
    // full index rather than a search box that does nothing.
    bar.hidden = false;
    // Two things style.css hangs off this class, both of which only make sense
    // once the script is running: the table striping the filter maintains, and
    // the `overflow-x` the sticky bar needs. See style.css for why.
    if (doc.body) doc.body.classList.add('pf-page');

    function render() {
      var result = apply(sections, state);
      if (count) {
        count.textContent = result.active
          ? result.shown.toLocaleString('en-US') + ' of ' + result.total.toLocaleString('en-US') + ' rows'
          : result.total.toLocaleString('en-US') + ' rows';
      }
      if (empty) empty.hidden = !(result.active && result.shown === 0);
      if (clear) clear.hidden = !result.active;
      var view = doc.defaultView;
      if (view && view.history && view.history.replaceState) {
        var qs = writeQuery(state, input ? input.value.trim() : '');
        view.history.replaceState(null, '', view.location.pathname + qs + view.location.hash);
      }
      return result;
    }

    if (input) {
      input.addEventListener('input', function () {
        state.terms = terms(input.value);
        render();
      });
      // Escape clears the box before it clears the focus — the same thing the
      // browser's own find bar does.
      input.addEventListener('keydown', function (event) {
        if (event.key === 'Escape' && input.value) {
          event.stopPropagation();
          input.value = '';
          state.terms = [];
          render();
        }
      });
    }

    for (var c = 0; c < chips.length; c++) {
      chips[c].addEventListener('click', function (event) {
        var el = event.currentTarget;
        var facet = el.getAttribute('data-facet');
        var value = (el.getAttribute('data-value') || '').toLowerCase();
        if (facet === 'must') {
          state.must = !state.must;
          el.setAttribute('aria-pressed', state.must ? 'true' : 'false');
        } else {
          var at = state[facet].indexOf(value);
          if (at === -1) state[facet].push(value);
          else state[facet].splice(at, 1);
          el.setAttribute('aria-pressed', at === -1 ? 'true' : 'false');
        }
        render();
      });
    }

    if (clear) {
      clear.addEventListener('click', function () {
        if (input) input.value = '';
        state.terms = [];
        state.difficulty = [];
        state.status = [];
        state.must = false;
        for (var k = 0; k < chips.length; k++) chips[k].setAttribute('aria-pressed', 'false');
        render();
        if (input) input.focus();
      });
    }

    render();
    return { sections: sections, state: state, render: render };
  }

  if (typeof document !== 'undefined') {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', function () { boot(document); });
    } else {
      boot(document);
    }
  }

  return {
    HIDDEN: HIDDEN,
    columnMap: columnMap,
    indexContent: indexContent,
    emptyState: emptyState,
    isActive: isActive,
    terms: terms,
    matchRow: matchRow,
    apply: apply,
    readQuery: readQuery,
    writeQuery: writeQuery,
    boot: boot
  };
});
