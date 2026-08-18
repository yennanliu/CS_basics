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

  // Cheatsheet index filter: a text box plus priority chips over 74 cards. The
  // markup ships fully expanded, so with JS off the catalogue is unchanged.
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
    // The ladder and the key are orientation for a first visit; once someone is
    // filtering they are looking for one sheet, so get them out of the way.
    var intro = [scope.querySelector('.tier-key'), scope.querySelector('.start-here')];
    var total = Number(status && status.getAttribute('data-total')) || cards.length;
    var minTier = 0;

    function apply() {
      var terms = (input && input.value || '').toLowerCase().split(/\s+/).filter(Boolean);
      var shown = 0;
      for (var i = 0; i < cards.length; i++) {
        var card = cards[i];
        var hay = card.getAttribute('data-search') || '';
        var tier = Number(card.getAttribute('data-tier')) || 0;
        var match = tier >= minTier;
        for (var t = 0; match && t < terms.length; t++) {
          if (hay.indexOf(terms[t]) === -1) match = false;
        }
        card.hidden = !match;
        if (match) shown++;
      }
      // A category with nothing left in it should not leave a stray heading, and
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
      if (empty) empty.hidden = shown !== 0;
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
    return { cards: cards.length, apply: apply };
  }

  function init() {
    wrapTables(document);
    initReadingProgress();
    initTOC(document);
    initSheetFilter(document);
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
    init: init
  };
});
