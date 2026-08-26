/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — study roadmap

   Behaviour for lc-roadmap.html: a topic DAG where each box tracks how many
   of its problems you have solved, and a topic unlocks once its prerequisites
   are finished. `_site/data/roadmap.json` (built by site/build-roadmap.js) is
   the only input; progress lives in localStorage and never leaves the browser.

   Loaded as a plain script next to nav.js, and required directly by
   site/test/roadmap.test.js — everything above `init` is a pure function of
   (nodes, solved) so the unlock and counting rules can be tested without a
   rendered page.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSRoadmap = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // Progress is keyed by LeetCode id, not by (topic, id): a problem that two
  // topics both list is one problem to solve, so ticking it in one place must
  // tick it in the other.
  var STORE_KEY = 'cs:roadmap:solved';

  var state = {
    roadmap: null, byId: {}, solved: Object.create(null),
    openId: null,
    // The element focus should go back to when the drawer closes.
    returnFocus: null
  };

  // ── Storage ─────────────────────────────────────────────────────────────

  // Wrapped because Safari's private mode throws on localStorage access rather
  // than returning null, which would otherwise break the whole page.
  function readSolved() {
    var solved = Object.create(null);
    try {
      var raw = typeof localStorage !== 'undefined' && localStorage.getItem(STORE_KEY);
      var ids = raw ? JSON.parse(raw) : [];
      if (Array.isArray(ids)) {
        for (var i = 0; i < ids.length; i++) solved[String(ids[i])] = true;
      }
    } catch (e) { /* unreadable or corrupt — start from empty */ }
    return solved;
  }

  function writeSolved(solved) {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(STORE_KEY, JSON.stringify(Object.keys(solved)));
      }
    } catch (e) { /* storage unavailable — the page still works for this visit */ }
  }

  // ── Derived state ───────────────────────────────────────────────────────

  function esc(value) {
    return String(value).replace(/[&<>"']/g, function (c) {
      return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c];
    });
  }

  function indexNodes(nodes) {
    var byId = {};
    for (var i = 0; i < nodes.length; i++) byId[nodes[i].id] = nodes[i];
    return byId;
  }

  function statsFor(node, solved) {
    var done = 0;
    for (var i = 0; i < node.problems.length; i++) {
      if (solved[node.problems[i].id]) done++;
    }
    return { done: done, total: node.problems.length };
  }

  function isDone(node, solved) {
    var s = statsFor(node, solved);
    return s.total > 0 && s.done === s.total;
  }

  function percent(s) { return s.total ? Math.round((s.done / s.total) * 100) : 0; }

  // A topic is unlocked once every prerequisite is finished. An unknown prereq
  // id counts as met so a data slip cannot strand a branch — build-roadmap.js
  // already fails the build on one.
  function isUnlocked(node, byId, solved) {
    var prereqs = node.prereqs || [];
    for (var i = 0; i < prereqs.length; i++) {
      var parent = byId[prereqs[i]];
      if (parent && !isDone(parent, solved)) return false;
    }
    return true;
  }

  function unmetPrereqs(node, byId, solved) {
    return (node.prereqs || []).filter(function (id) {
      return byId[id] && !isDone(byId[id], solved);
    });
  }

  // Counted distinctly: a problem two topics share is one tick, so summing the
  // per-topic totals would overstate both the numerator and the denominator.
  function distinctSolved(nodes, solved) {
    var seen = Object.create(null);
    for (var i = 0; i < nodes.length; i++) {
      var problems = nodes[i].problems;
      for (var j = 0; j < problems.length; j++) {
        if (solved[problems[j].id]) seen[problems[j].id] = true;
      }
    }
    return Object.keys(seen).length;
  }

  /**
   * The shallowest unlocked, unfinished topic and its first unsolved problem —
   * an answer to "what do I do next?" that does not require reading the graph.
   * Ties go to the topic authored first, which is the left-most box in its row.
   */
  function nextUp(nodes, byId, solved) {
    var best = null;
    for (var i = 0; i < nodes.length; i++) {
      var node = nodes[i];
      if (isDone(node, solved) || !isUnlocked(node, byId, solved)) continue;
      if (best && node.row >= best.row) continue;
      best = node;
    }
    if (!best) return null;
    var problem = null;
    for (var j = 0; j < best.problems.length; j++) {
      if (!solved[best.problems[j].id]) { problem = best.problems[j]; break; }
    }
    return { node: best, problem: problem };
  }

  // ── Markup ──────────────────────────────────────────────────────────────

  /**
   * The edges already say what a topic waits on, so the box itself carries no
   * "after X" line — printing it on all 29 boxes buried the graph in repeated
   * labels. The waiting list lives in the tooltip and in the drawer instead.
   */
  function lockLabel(node, byId, solved) {
    var unmet = unmetPrereqs(node, byId, solved);
    if (!unmet.length) return '';
    return 'Finish first: ' + unmet.map(function (id) { return byId[id].title; }).join(', ');
  }

  function nodeHTML(node, byId, solved) {
    var s = statsFor(node, solved);
    var lock = lockLabel(node, byId, solved);
    var cls = 'node' + (isDone(node, solved) ? ' done' : '') + (lock ? ' locked' : '');
    var label = node.title + ' — ' + s.done + ' of ' + s.total + ' solved' + (lock ? '. ' + lock : '');

    return '<button type="button" class="' + cls + '" data-id="' + esc(node.id) + '"' +
      ' title="' + esc(label) + '" aria-label="' + esc(label) + '">' +
      '<span class="node-head">' +
        '<span class="node-title">' + esc(node.title) + '</span>' +
        '<span class="node-count">' + s.done + '/' + s.total + '</span>' +
      '</span>' +
      '<span class="node-bar"><i style="width:' + percent(s) + '%"></i></span>' +
    '</button>';
  }

  function graphHTML(nodes, byId, solved) {
    var rows = {};
    for (var i = 0; i < nodes.length; i++) {
      (rows[nodes[i].row] = rows[nodes[i].row] || []).push(nodes[i]);
    }
    return Object.keys(rows).map(Number).sort(function (a, b) { return a - b; })
      .map(function (row) {
        return '<div class="row" data-row="' + row + '">' + rows[row].map(function (node) {
          return nodeHTML(node, byId, solved);
        }).join('') + '</div>';
      }).join('');
  }

  function problemHTML(problem, solved) {
    var links = Object.keys(problem.solutions || {}).map(function (lang) {
      return '<a href="' + esc(problem.solutions[lang]) + '" target="_blank" rel="noopener" ' +
        'title="' + esc(lang) + ' solution in this repo">' + esc(lang.slice(0, 2)) + '</a>';
    }).join('');

    return '<div class="prob' + (solved[problem.id] ? ' solved' : '') + '">' +
      '<input type="checkbox" data-check="' + esc(problem.id) + '"' +
        (solved[problem.id] ? ' checked' : '') +
        ' aria-label="Mark ' + esc('#' + problem.id + ' ' + problem.title) + ' as solved">' +
      '<span class="prob-id">#' + esc(problem.id) + '</span>' +
      '<a class="prob-title" href="' + esc(problem.url) + '" target="_blank" rel="noopener">' +
        esc(problem.title) + '</a>' +
      '<span class="diff-badge ' + esc(problem.difficulty) + '">' + esc(problem.difficulty) + '</span>' +
      '<span class="prob-links">' + links + '</span>' +
    '</div>';
  }

  function drawerBodyHTML(node, byId, solved) {
    var s = statsFor(node, solved);
    var html = '';

    if ((node.prereqs || []).length) {
      html += '<div class="drawer-section"><h3>Prerequisites</h3><div class="chips">' +
        node.prereqs.map(function (id) {
          var parent = byId[id];
          if (!parent) return '';
          var met = isDone(parent, solved);
          return '<button type="button" class="chip' + (met ? ' met' : '') + '" data-open="' +
            esc(id) + '">' + (met ? '✓ ' : '') + esc(parent.title) + '</button>';
        }).join('') + '</div></div>';
    }

    if ((node.sheets || []).length) {
      html += '<div class="drawer-section"><h3>Cheatsheets</h3><div class="chips">' +
        node.sheets.map(function (sheet) {
          return '<a class="chip" href="' + esc(sheet.url) + '">' + esc(sheet.title) + '</a>';
        }).join('') + '</div></div>';
    }

    return html +
      '<div class="drawer-section">' +
        '<h3>Problems — ' + s.done + ' / ' + s.total + '</h3>' +
        '<div class="chips bulk">' +
          '<button type="button" class="chip" data-bulk="all">tick all</button>' +
          '<button type="button" class="chip" data-bulk="none">clear all</button>' +
        '</div>' +
        node.problems.map(function (problem) {
          return problemHTML(problem, solved);
        }).join('') +
      '</div>';
  }

  // A cubic curve that leaves the parent box downward and enters the child box
  // downward, so edges read as flowing top-to-bottom even when they cross.
  function edgePath(x1, y1, x2, y2) {
    var mid = (y1 + y2) / 2;
    return 'M' + x1 + ' ' + y1 + ' C' + x1 + ' ' + mid + ' ' + x2 + ' ' + mid + ' ' + x2 + ' ' + y2;
  }

  // ── DOM ─────────────────────────────────────────────────────────────────

  function $(id) { return document.getElementById(id); }

  // CSS.escape is missing on older Safari, and node ids are kebab-case, so only
  // the two characters that could break out of the attribute selector matter.
  function selectorFor(id) { return '.node[data-id="' + String(id).replace(/["\\]/g, '\\$&') + '"]'; }

  /**
   * Draws one curve per prereq edge, measured from where the boxes actually
   * landed. Reading real geometry — rather than deriving it from row/col —
   * keeps the lines attached when the canvas is scrolled or the rows reflow.
   */
  function drawEdges() {
    var graph = $('graph');
    var svg = $('edges');
    if (!graph || !svg || !state.roadmap) return;

    var base = graph.getBoundingClientRect();
    svg.setAttribute('width', graph.offsetWidth);
    svg.setAttribute('height', graph.offsetHeight);
    svg.setAttribute('viewBox', '0 0 ' + graph.offsetWidth + ' ' + graph.offsetHeight);

    var paths = '';
    state.roadmap.nodes.forEach(function (node) {
      var childEl = graph.querySelector(selectorFor(node.id));
      if (!childEl) return;
      var child = childEl.getBoundingClientRect();
      (node.prereqs || []).forEach(function (prereqId) {
        var parentEl = graph.querySelector(selectorFor(prereqId));
        if (!parentEl) return;
        var parent = parentEl.getBoundingClientRect();
        var live = state.byId[prereqId] && isDone(state.byId[prereqId], state.solved);
        paths += '<path class="' + (live ? 'live' : '') + '"' +
          ' data-from="' + esc(prereqId) + '" data-to="' + esc(node.id) + '" d="' + edgePath(
            parent.left - base.left + parent.width / 2, parent.bottom - base.top,
            child.left - base.left + child.width / 2, child.top - base.top
          ) + '"/>';
      });
    });
    svg.innerHTML = paths;
  }

  /** Dims every edge except the ones entering or leaving `id`. */
  function highlightEdges(id) {
    var svg = $('edges');
    if (!svg) return;
    svg.classList.toggle('focused', Boolean(id));
    var paths = svg.querySelectorAll('path');
    for (var i = 0; i < paths.length; i++) {
      var touches = id && (paths[i].getAttribute('data-from') === id ||
                           paths[i].getAttribute('data-to') === id);
      paths[i].classList.toggle('hot', Boolean(touches));
    }
  }

  function renderSummary() {
    var nodes = state.roadmap.nodes;
    var done = distinctSolved(nodes, state.solved);
    var total = state.roadmap.stats.problems;
    var topicsDone = nodes.filter(function (n) { return isDone(n, state.solved); }).length;
    var pct = total ? Math.round((done / total) * 100) : 0;

    $('statProblems').textContent = done + ' / ' + total;
    $('statTopics').textContent = topicsDone + ' / ' + nodes.length;
    $('summaryFill').style.width = pct + '%';
    $('summaryLabel').textContent = pct + '% of the roadmap solved';

    var hint = $('nextUp');
    var next = nextUp(nodes, state.byId, state.solved);
    hint.hidden = false;
    hint.innerHTML = next
      ? '<span class="label">next up</span>' +
        '<button class="btn btn-primary" type="button" data-open="' + esc(next.node.id) + '">' +
          esc(next.node.title) + '</button>' +
        (next.problem
          ? '<span class="where">start with <a href="' + esc(next.problem.url) + '" ' +
            'target="_blank" rel="noopener">#' + esc(next.problem.id) + ' ' +
            esc(next.problem.title) + '</a></span>'
          : '')
      : '<span class="label">next up</span><span>Every topic is complete — ' +
        'nothing left on the roadmap.</span>';
  }

  // Repaints the boxes in place instead of rebuilding the graph, so nothing
  // under the pointer moves while you are ticking problems off.
  function refreshNodes() {
    state.roadmap.nodes.forEach(function (node) {
      var el = document.querySelector(selectorFor(node.id));
      if (!el) return;
      var s = statsFor(node, state.solved);
      var lock = lockLabel(node, state.byId, state.solved);
      var label = node.title + ' — ' + s.done + ' of ' + s.total + ' solved' + (lock ? '. ' + lock : '');
      el.classList.toggle('done', isDone(node, state.solved));
      el.classList.toggle('locked', lock !== '');
      el.setAttribute('title', label);
      el.setAttribute('aria-label', label);
      el.querySelector('.node-count').textContent = s.done + '/' + s.total;
      el.querySelector('.node-bar > i').style.width = percent(s) + '%';
    });
  }

  function afterChange() {
    writeSolved(state.solved);
    refreshNodes();
    renderSummary();
    drawEdges();
    if (state.openId) {
      $('drawerBody').innerHTML = drawerBodyHTML(state.byId[state.openId], state.byId, state.solved);
    }
  }

  /**
   * Shows `node`'s problems, prerequisites and cheatsheets.
   *
   * `opener` is the control that asked for it, remembered so closing can hand
   * focus back. It is passed in rather than read off `document.activeElement`
   * because a click does not reliably leave focus on the thing clicked. Only
   * the first open records it: hopping prereq → prereq inside the drawer should
   * still return you to the box you started from, not to a chip the next render
   * has already thrown away.
   */
  function openDrawer(id, opener) {
    var node = state.byId[id];
    if (!node) return;
    if (!state.openId) state.returnFocus = opener || null;
    state.openId = id;
    $('drawerTitle').textContent = node.title;
    $('drawerBlurb').textContent = node.blurb || '';
    $('drawerBody').innerHTML = drawerBodyHTML(node, state.byId, state.solved);
    $('drawer').classList.add('open');
    $('drawer').setAttribute('aria-hidden', 'false');
    $('overlay').classList.add('open');
    $('drawerClose').focus();
    setHash(id);
  }

  function closeDrawer() {
    if (!state.openId) return;
    state.openId = null;
    $('drawer').classList.remove('open');
    $('drawer').setAttribute('aria-hidden', 'true');
    $('overlay').classList.remove('open');
    setHash(null);
    // The close button we were sitting on is now inside an aria-hidden panel
    // parked off-screen. Without this a keyboard or screen-reader user is left
    // focused on nothing and has to tab in from the top of the document.
    var opener = state.returnFocus;
    state.returnFocus = null;
    if (opener && opener.isConnected && typeof opener.focus === 'function') opener.focus();
  }

  // replaceState rather than assigning location.hash: the drawer is not a
  // separate page, so it should not stack up history entries you have to
  // back out of one by one — but the URL still names the open topic so it
  // can be shared.
  function setHash(id) {
    if (typeof history === 'undefined' || !history.replaceState) return;
    var target = id ? '#' + id : location.pathname + location.search;
    if (id ? location.hash !== '#' + id : location.hash) history.replaceState(null, '', target);
  }

  function setSolved(id, on) {
    if (on) state.solved[id] = true;
    else delete state.solved[id];
  }

  function wire() {
    $('graph').addEventListener('click', function (event) {
      var el = event.target.closest('.node');
      if (el) openDrawer(el.getAttribute('data-id'), el);
    });

    // Twenty-nine boxes make for a lot of crossing lines. Hovering a topic
    // pulls out just the edges that touch it, which is the only way to read
    // "what feeds this, and what does it feed" out of the tangle.
    $('graph').addEventListener('mouseover', function (event) {
      var el = event.target.closest('.node');
      if (el) highlightEdges(el.getAttribute('data-id'));
    });
    $('graph').addEventListener('mouseout', function (event) {
      if (event.target.closest('.node')) highlightEdges(null);
    });

    $('overlay').addEventListener('click', closeDrawer);
    $('drawerClose').addEventListener('click', closeDrawer);
    document.addEventListener('keydown', function (event) {
      if (event.key === 'Escape') closeDrawer();
    });

    $('drawerBody').addEventListener('change', function (event) {
      var box = event.target.closest('input[data-check]');
      if (!box) return;
      setSolved(box.getAttribute('data-check'), box.checked);
      afterChange();
    });

    $('drawerBody').addEventListener('click', function (event) {
      var bulk = event.target.closest('[data-bulk]');
      if (bulk && state.openId) {
        var on = bulk.getAttribute('data-bulk') === 'all';
        state.byId[state.openId].problems.forEach(function (p) { setSolved(p.id, on); });
        afterChange();
        return;
      }
      var jump = event.target.closest('[data-open]');
      if (jump) openDrawer(jump.getAttribute('data-open'));
    });

    $('nextUp').addEventListener('click', function (event) {
      var jump = event.target.closest('[data-open]');
      if (jump) openDrawer(jump.getAttribute('data-open'), jump);
    });

    $('resetBtn').addEventListener('click', function () {
      if (!Object.keys(state.solved).length) return;
      if (typeof confirm === 'function' && !confirm('Clear all roadmap progress in this browser?')) return;
      state.solved = Object.create(null);
      afterChange();
    });

    window.addEventListener('resize', drawEdges);
    if (typeof CSNav !== 'undefined') document.addEventListener(CSNav.THEME_EVENT, drawEdges);
  }

  // ── Boot ────────────────────────────────────────────────────────────────

  /** Renders the roadmap from already-fetched data and wires up the page. */
  function render(roadmap) {
    state.roadmap = roadmap;
    state.byId = indexNodes(roadmap.nodes);
    state.solved = readSolved();
    // A browser renders once per page load, but `state` outlives a re-render.
    // Carrying an open topic — or a focus target belonging to the previous
    // document — over into a fresh render leaves both pointing at dead nodes.
    state.openId = null;
    state.returnFocus = null;

    if (roadmap.meta && roadmap.meta.title) {
      $('pageTitle').textContent = roadmap.meta.title;
      document.title = roadmap.meta.title + ' - CS Basics';
    }
    $('pageIntro').textContent = (roadmap.meta && roadmap.meta.intro) || '';

    $('graph').insertAdjacentHTML('beforeend', graphHTML(roadmap.nodes, state.byId, state.solved));
    $('loading').hidden = true;
    $('summary').hidden = false;
    $('note').hidden = false;

    renderSummary();
    drawEdges();
    wire();

    var hash = location.hash.replace(/^#/, '');
    if (hash && state.byId[hash]) openDrawer(hash);
    return state;
  }

  function init(url) {
    return fetch(url || './data/roadmap.json')
      .then(function (res) { return res.json(); })
      .then(render)
      .catch(function () {
        $('loading').textContent = 'Failed to load the roadmap data (data/roadmap.json).';
      });
  }

  return {
    STORE_KEY: STORE_KEY,
    esc: esc,
    indexNodes: indexNodes,
    statsFor: statsFor,
    isDone: isDone,
    percent: percent,
    isUnlocked: isUnlocked,
    lockLabel: lockLabel,
    highlightEdges: highlightEdges,
    unmetPrereqs: unmetPrereqs,
    distinctSolved: distinctSolved,
    nextUp: nextUp,
    nodeHTML: nodeHTML,
    graphHTML: graphHTML,
    problemHTML: problemHTML,
    drawerBodyHTML: drawerBodyHTML,
    edgePath: edgePath,
    readSolved: readSolved,
    writeSolved: writeSolved,
    drawEdges: drawEdges,
    openDrawer: openDrawer,
    closeDrawer: closeDrawer,
    render: render,
    init: init,
    state: state
  };
});
