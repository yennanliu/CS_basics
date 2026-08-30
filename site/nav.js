/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — shared navbar

   The single source of truth for the site navigation: the entry list, the
   markup, and the behaviour (theme toggle, mobile drawer, "more" dropdown).
   Every page family loads this same file, so adding a nav entry is a one-line
   change here rather than an edit across four hand-maintained copies:

     - generated doc pages   (site/build-site.js emits the placeholder)
     - algo_demo/*.html      (visualizer pages)
     - _site/lc-*.html       (hand-maintained LeetCode tools)

   Usage — load in <head> so the stored theme lands before first paint, then
   drop a placeholder where the navbar belongs and mount it synchronously:

     <head> <script src="nav.js"></script> </head>
     <body>
       <div id="site-nav" data-page="cheatsheets" data-base="../"></div>
       <script>CSNav.mount();</script>

   `data-page` is an entry id (see PRIMARY/MORE below) and marks it active;
   `data-base` is the prefix that gets a page back to the site root.

   A page that exists in both languages also sets `data-lang` (the language of
   the page you are on) and `data-lang-alt` (the href of its counterpart). That
   is the only trigger for the 中文/EN button — pages with no translation simply
   omit the attributes and get no button, so nothing can link into a 404.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSNav = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  var THEME_KEY = 'theme';
  var DEFAULT_THEME = 'dark';

  // Fired on `document` after every theme change. Pages that paint their own
  // colours (canvas visualizations, SVG charts) listen for this to repaint.
  var THEME_EVENT = 'cs:themechange';

  // Entries rendered inline in the bar.
  var PRIMARY = [
    { id: 'home',        label: 'home',        href: 'index.html' },
    { id: 'search',      label: 'search',      href: 'search.html' },
    { id: 'lc-roadmap',  label: 'roadmap',     href: 'lc-roadmap.html' },
    { id: 'cheatsheets', label: 'cheatsheets', href: 'cheatsheets.html' },
    { id: 'faqs',        label: 'faqs',        href: 'faqs.html' },
    { id: 'lc-explorer', label: 'lc-explorer', href: 'lc-explorer.html' },
    { id: 'visualizer',  label: 'visualizer',  href: 'algo_demo/index.html' }
  ];

  // Secondary entries, collapsed behind the "more" dropdown. The button lights
  // up when the current page is one of them, so the trail survives collapsing.
  //
  // The inline row is capacity-bound, not preference-bound: eight entries
  // overflow the bar between 768px and 1024px. The roadmap took the slot the
  // random picker held, since the picker's siblings (similar, review) already
  // live down here.
  var MORE = [
    { id: 'patterns',           label: 'patterns',   href: 'patterns.html' },
    { id: 'lc-similar',         label: 'similar',    href: 'lc-similar.html' },
    { id: 'lc-review-plan',     label: 'review',     href: 'lc-review-plan.html' },
    { id: 'lc-random-picker',   label: 'random',     href: 'lc-random-picker.html' },
    { id: 'lc-complexity-quiz', label: 'complexity', href: 'lc-complexity-quiz.html' },
    { id: 'resources',          label: 'resources',  href: 'resources.html' },
    { id: 'github',             label: 'github',     href: 'https://github.com/yennanliu/CS_basics', external: true }
  ];

  // ── Markup ──────────────────────────────────────────────────────────────

  function esc(value) {
    return String(value)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  function hrefFor(item, basePath) {
    return item.external ? item.href : (basePath || '') + item.href;
  }

  function isMoreEntry(id) {
    for (var i = 0; i < MORE.length; i++) if (MORE[i].id === id) return true;
    return false;
  }

  function linkHTML(item, currentPage, basePath) {
    var attrs = ' href="' + esc(hrefFor(item, basePath)) + '"';
    if (item.external) attrs += ' target="_blank" rel="noopener"';
    if (item.id === currentPage) attrs += ' class="active"';
    return '<a' + attrs + '>' + esc(item.label) + '</a>';
  }

  // The button names the language you would switch TO — the same rule the theme
  // toggle follows. It is a plain <a>, so it works with JS off and middle-clicks
  // into a new tab like any other link.
  function langToggleHTML(lang, altHref) {
    if (!altHref) return '';
    var toZh = lang !== 'zh';
    var label = toZh ? '中文' : 'EN';
    var title = toZh ? '切換到繁體中文版' : 'Read this page in English';
    return '<a class="lang-toggle" href="' + esc(altHref) + '" ' +
      'lang="' + (toZh ? 'zh-Hant' : 'en') + '" ' +
      'title="' + esc(title) + '" aria-label="' + esc(title) + '">' + label + '</a>';
  }

  function navHTML(options) {
    options = options || {};
    var currentPage = options.currentPage || '';
    var basePath = options.basePath || '';
    var links = function (item) { return linkHTML(item, currentPage, basePath); };

    return '<nav class="navbar">' +
      '<div class="nav-inner">' +
        '<a href="' + esc(basePath + 'index.html') + '" class="nav-brand">' +
          '<span class="nav-title">CS_basics</span>' +
        '</a>' +
        '<button type="button" class="nav-toggle" aria-label="Toggle menu" ' +
          'aria-controls="nav-links" aria-expanded="false">' +
          '<span></span><span></span><span></span>' +
        '</button>' +
        '<div class="nav-links" id="nav-links">' +
          PRIMARY.map(links).join('') +
          '<div class="nav-more">' +
            '<button type="button" class="nav-more-btn' +
              (isMoreEntry(currentPage) ? ' active' : '') + '" ' +
              'aria-haspopup="true" aria-expanded="false">' +
              'more <span class="nav-more-caret">▾</span>' +
            '</button>' +
            '<div class="nav-more-menu">' + MORE.map(links).join('') + '</div>' +
          '</div>' +
          langToggleHTML(options.lang, options.langAlt) +
          '<button type="button" id="theme-toggle" class="theme-toggle" ' +
            'aria-label="Toggle theme">' + esc(themeLabel(DEFAULT_THEME)) + '</button>' +
        '</div>' +
      '</div>' +
    '</nav>';
  }

  // ── Theme ───────────────────────────────────────────────────────────────

  // Wrapped because Safari's private mode throws on localStorage access
  // rather than returning null, which would otherwise break the whole navbar.
  function storedTheme() {
    try {
      return (typeof localStorage !== 'undefined' && localStorage.getItem(THEME_KEY)) || DEFAULT_THEME;
    } catch (e) {
      return DEFAULT_THEME;
    }
  }

  function persistTheme(theme) {
    try {
      if (typeof localStorage !== 'undefined') localStorage.setItem(THEME_KEY, theme);
    } catch (e) { /* storage unavailable — theme still applies for this page */ }
  }

  function currentTheme() {
    return document.documentElement.getAttribute('data-theme') || DEFAULT_THEME;
  }

  // The label names the theme you would switch TO, not the one you are in.
  function themeLabel(theme) {
    return theme === 'dark' ? '☀ light' : '● dark';
  }

  function syncThemeLabel() {
    var btn = document.getElementById('theme-toggle');
    if (btn) btn.textContent = themeLabel(currentTheme());
  }

  // Called at load, before <body> is parsed, so no flash of the wrong theme.
  function applyStoredTheme() {
    document.documentElement.setAttribute('data-theme', storedTheme());
  }

  function setTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    persistTheme(theme);
    syncThemeLabel();
    if (typeof CustomEvent === 'function') {
      document.dispatchEvent(new CustomEvent(THEME_EVENT, { detail: { theme: theme } }));
    }
  }

  function toggleTheme() {
    setTheme(currentTheme() === 'dark' ? 'light' : 'dark');
  }

  // ── Behaviour ───────────────────────────────────────────────────────────

  function initTheme() {
    applyStoredTheme();
    syncThemeLabel();
    var btn = document.getElementById('theme-toggle');
    if (btn) btn.addEventListener('click', toggleTheme);
  }

  // Bound here rather than inline so aria-expanded stays in step with the
  // drawer — a screen reader otherwise cannot tell whether it is open.
  function initNavToggle(scope) {
    scope = scope || document;
    var btn = scope.querySelector('.nav-toggle');
    var links = scope.querySelector('.nav-links');
    if (!btn || !links) return;
    btn.addEventListener('click', function () {
      var open = links.classList.toggle('open');
      btn.setAttribute('aria-expanded', open ? 'true' : 'false');
    });
  }

  function initNavMore(scope) {
    scope = scope || document;
    var more = scope.querySelector('.nav-more');
    if (!more) return;
    var btn = more.querySelector('.nav-more-btn');
    if (!btn) return;

    var setOpen = function (open) {
      more.classList.toggle('open', open);
      btn.setAttribute('aria-expanded', open ? 'true' : 'false');
    };

    btn.addEventListener('click', function () {
      setOpen(!more.classList.contains('open'));
    });
    document.addEventListener('click', function (event) {
      if (!more.contains(event.target)) setOpen(false);
    });
    document.addEventListener('keydown', function (event) {
      if (event.key === 'Escape') setOpen(false);
    });
  }

  // Renders the navbar into `#site-nav` (or the given element) and wires it up.
  function mount(target) {
    var host = target || document.getElementById('site-nav');
    if (!host) return null;
    host.innerHTML = navHTML({
      currentPage: host.getAttribute('data-page') || '',
      basePath: host.getAttribute('data-base') || '',
      lang: host.getAttribute('data-lang') || 'en',
      langAlt: host.getAttribute('data-lang-alt') || ''
    });
    initTheme();
    initNavToggle(host);
    initNavMore(host);
    return host;
  }

  if (typeof document !== 'undefined' && document.documentElement) applyStoredTheme();

  return {
    PRIMARY: PRIMARY,
    MORE: MORE,
    THEME_EVENT: THEME_EVENT,
    esc: esc,
    hrefFor: hrefFor,
    isMoreEntry: isMoreEntry,
    navHTML: navHTML,
    langToggleHTML: langToggleHTML,
    themeLabel: themeLabel,
    storedTheme: storedTheme,
    currentTheme: currentTheme,
    applyStoredTheme: applyStoredTheme,
    setTheme: setTheme,
    toggleTheme: toggleTheme,
    initTheme: initTheme,
    initNavToggle: initNavToggle,
    initNavMore: initNavMore,
    mount: mount
  };
});
