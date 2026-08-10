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

  function init() {
    wrapTables(document);
    initReadingProgress();
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
    init: init
  };
});
