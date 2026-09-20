const test = require('node:test');
const assert = require('node:assert/strict');
const { setupDOM, teardownDOM, click, keydown } = require('./helpers');

// nav.js applies the stored theme at load time, so a DOM has to exist first.
setupDOM();
const CSNav = require('../nav.js');

test.afterEach(() => { setupDOM(); });
test.after(() => { teardownDOM(); });

// ── Markup ────────────────────────────────────────────────────────────────

test('navHTML renders every primary entry inline, in order', () => {
  const html = CSNav.navHTML();
  const labels = [...html.matchAll(/>([a-z-]+)<\/a>/g)].map((m) => m[1]);
  assert.deepEqual(labels.slice(0, CSNav.PRIMARY.length), CSNav.PRIMARY.map((i) => i.label));
});

test('navHTML puts the secondary entries inside the dropdown menu', () => {
  const menu = CSNav.navHTML().split('<div class="nav-more-menu">')[1];
  for (const item of CSNav.MORE) {
    if (item.children) continue; // a group renders as a label, not a link
    assert.ok(menu.includes('>' + item.label + '</a>'), `${item.label} missing from dropdown`);
  }
  // ...and keeps them out of the inline row.
  const inline = CSNav.navHTML().split('<div class="nav-more">')[0];
  assert.ok(!inline.includes('>patterns</a>'));
});

// ── Grouped entries ───────────────────────────────────────────────────────
//
// The two agent skills are one family — same tree, same install, same
// slash-command convention — and were two unrelated dropdown entries, the
// first labelled "coach", which named neither the command (/lc-coach) nor the
// directory it lives in.

test('a MORE entry with children renders as a labelled group, not a link', () => {
  const html = CSNav.moreEntryHTML(
    { id: 'agent-skills', label: 'agent skills', children: [
      { id: 'lc-coach', label: 'lc-coach', href: 'skills.html' }
    ] }, '', '');
  assert.match(html, /<div class="nav-group">/);
  assert.match(html, /<span class="nav-group-label">agent skills<\/span>/);
  assert.match(html, /<a href="skills\.html">lc-coach<\/a>/);
  // The label is a heading: no href, so it is not a tab stop that goes nowhere.
  assert.ok(!html.includes('<a href="">'));
});

test('the agent skills sit under one parent in the dropdown', () => {
  const group = CSNav.MORE.find((i) => i.id === 'agent-skills');
  assert.ok(group, 'the dropdown should declare an agent-skills group');
  assert.deepEqual(group.children.map((c) => c.id),
    ['lc-coach', 'lc-python', 'lc-java', 'lc-cheatsheet', 'lc-log', 'lc-again']);

  const menu = CSNav.navHTML().split('<div class="nav-more-menu">')[1];
  const block = menu.match(/<div class="nav-group[^"]*">[\s\S]*?<\/div>/)[0];
  assert.ok(block.includes('>lc-coach</a>') && block.includes('>lc-python</a>')
    && block.includes('>lc-java</a>') && block.includes('>lc-cheatsheet</a>')
    && block.includes('>lc-log</a>') && block.includes('>lc-again</a>'),
    'every skill belongs to the same group block');
});

test('the coach entry is named after its command, not "coach"', () => {
  const coach = CSNav.links().find((i) => i.href === 'skills.html');
  assert.equal(coach.id, 'lc-coach');
  assert.equal(coach.label, 'lc-coach');
});

test('a group lights up when one of its children is the current page', () => {
  const html = CSNav.navHTML({ currentPage: 'lc-python' });
  assert.match(html, /<div class="nav-group active">/);
  assert.match(html, /class="nav-more-btn active"/);
  assert.match(html, /<a href="lc-python\.html" class="active">lc-python<\/a>/);
});

test('a group is inert when the current page is elsewhere', () => {
  assert.match(CSNav.navHTML({ currentPage: 'home' }), /<div class="nav-group">/);
});

test('links() flattens groups so every entry it returns has an href', () => {
  const flat = CSNav.links();
  for (const item of flat) {
    assert.ok(item.href, `${item.id} has no href`);
    assert.ok(!item.children, `${item.id} is a group, not a link`);
  }
  const ids = flat.map((i) => i.id);
  assert.ok(ids.includes('lc-coach') && ids.includes('lc-python')
    && ids.includes('lc-java') && ids.includes('lc-cheatsheet')
    && ids.includes('lc-log') && ids.includes('lc-again'),
    'group children are missing');
  assert.ok(!ids.includes('agent-skills'), 'the group label is not a destination');
  assert.equal(new Set(ids).size, ids.length);
});

test('navHTML marks the current primary entry active', () => {
  const html = CSNav.navHTML({ currentPage: 'cheatsheets' });
  assert.match(html, /<a href="cheatsheets\.html" class="active">cheatsheets<\/a>/);
  assert.equal((html.match(/class="active"/g) || []).length, 1);
});

test('navHTML activates the "more" button when the page is inside the dropdown', () => {
  const html = CSNav.navHTML({ currentPage: 'lc-review-plan' });
  assert.match(html, /class="nav-more-btn active"/);
  assert.match(html, /<a href="lc-review-plan\.html" class="active">review<\/a>/);
});

test('navHTML leaves the "more" button inactive for a primary page', () => {
  assert.match(CSNav.navHTML({ currentPage: 'home' }), /class="nav-more-btn"/);
});

test('navHTML prefixes internal links with basePath but never external ones', () => {
  const html = CSNav.navHTML({ basePath: '../' });
  assert.match(html, /href="\.\.\/cheatsheets\.html"/);
  assert.match(html, /href="\.\.\/algo_demo\/index\.html"/);
  assert.match(html, /href="https:\/\/github\.com\/yennanliu\/CS_basics"/);
  assert.ok(!html.includes('href="../https://'));
});

test('navHTML opens external entries in a new tab with rel=noopener', () => {
  assert.match(CSNav.navHTML(), /href="https:\/\/github[^"]*" target="_blank" rel="noopener"/);
});

test('navHTML renders the brand, hamburger and theme toggle exactly once', () => {
  const html = CSNav.navHTML();
  for (const needle of ['nav-brand', 'nav-toggle', 'id="theme-toggle"', 'nav-more-menu']) {
    assert.equal(html.split(needle).length - 1, 1, `${needle} should appear once`);
  }
});

test('navHTML defaults to no active entry when the page is unknown', () => {
  const html = CSNav.navHTML({ currentPage: 'not-a-page' });
  assert.ok(!html.includes('class="active"'));
  assert.match(html, /class="nav-more-btn"/);
});

test('esc neutralises characters that would break out of an attribute', () => {
  assert.equal(CSNav.esc('a"b<c>d&e'), 'a&quot;b&lt;c&gt;d&amp;e');
});

test('isMoreEntry distinguishes dropdown entries from primary ones', () => {
  assert.equal(CSNav.isMoreEntry('patterns'), true);
  assert.equal(CSNav.isMoreEntry('home'), false);
});

test('isMoreEntry sees a page nested inside a group', () => {
  assert.equal(CSNav.isMoreEntry('lc-coach'), true);
  assert.equal(CSNav.isMoreEntry('agent-skills'), true);
});

test('every entry has a unique id', () => {
  const ids = [...CSNav.PRIMARY, ...CSNav.MORE,
               ...CSNav.MORE.flatMap((i) => i.children || [])].map((i) => i.id);
  assert.equal(new Set(ids).size, ids.length);
});

// ── Mounting ──────────────────────────────────────────────────────────────

test('mount fills #site-nav and reads its data attributes', () => {
  document.getElementById('site-nav').setAttribute('data-page', 'faqs');
  document.getElementById('site-nav').setAttribute('data-base', '../');
  CSNav.mount();

  assert.ok(document.querySelector('nav.navbar'), 'navbar not rendered');
  assert.equal(document.querySelector('.nav-links a.active').textContent, 'faqs');
  assert.equal(document.querySelector('.nav-brand').getAttribute('href'), '../index.html');
});

test('mount is a no-op when the placeholder is absent', () => {
  setupDOM('<p>no placeholder</p>');
  assert.equal(CSNav.mount(), null);
  assert.equal(document.querySelector('nav.navbar'), null);
});

test('mount accepts an explicit target element', () => {
  setupDOM('<div id="elsewhere" data-page="search"></div>');
  const host = document.getElementById('elsewhere');
  assert.equal(CSNav.mount(host), host);
  assert.equal(document.querySelector('.nav-links a.active').textContent, 'search');
});

// ── Theme ─────────────────────────────────────────────────────────────────

test('applyStoredTheme falls back to dark when nothing is stored', () => {
  CSNav.applyStoredTheme();
  assert.equal(document.documentElement.getAttribute('data-theme'), 'dark');
});

test('applyStoredTheme restores a previously chosen theme', () => {
  localStorage.setItem('theme', 'light');
  CSNav.applyStoredTheme();
  assert.equal(document.documentElement.getAttribute('data-theme'), 'light');
});

test('themeLabel names the theme you would switch to', () => {
  assert.equal(CSNav.themeLabel('dark'), '☀ light');
  assert.equal(CSNav.themeLabel('light'), '● dark');
});

test('clicking the toggle flips the theme, persists it and relabels the button', () => {
  CSNav.mount();
  const btn = document.getElementById('theme-toggle');
  assert.equal(document.documentElement.getAttribute('data-theme'), 'dark');
  assert.equal(btn.textContent, '☀ light');

  click(btn);
  assert.equal(document.documentElement.getAttribute('data-theme'), 'light');
  assert.equal(localStorage.getItem('theme'), 'light');
  assert.equal(btn.textContent, '● dark');

  click(btn);
  assert.equal(document.documentElement.getAttribute('data-theme'), 'dark');
  assert.equal(localStorage.getItem('theme'), 'dark');
  assert.equal(btn.textContent, '☀ light');
});

test('a theme change announces itself so pages can repaint', () => {
  CSNav.mount();
  const seen = [];
  document.addEventListener(CSNav.THEME_EVENT, (e) => seen.push(e.detail.theme));

  click(document.getElementById('theme-toggle'));
  click(document.getElementById('theme-toggle'));

  assert.deepEqual(seen, ['light', 'dark']);
});

test('mount shows the stored theme on the button, not the default', () => {
  localStorage.setItem('theme', 'light');
  CSNav.mount();
  assert.equal(document.getElementById('theme-toggle').textContent, '● dark');
});

// ── Dropdown ──────────────────────────────────────────────────────────────

test('the "more" button toggles the dropdown and keeps aria-expanded in step', () => {
  CSNav.mount();
  const more = document.querySelector('.nav-more');
  const btn = more.querySelector('.nav-more-btn');

  assert.equal(more.classList.contains('open'), false);
  assert.equal(btn.getAttribute('aria-expanded'), 'false');

  click(btn);
  assert.equal(more.classList.contains('open'), true);
  assert.equal(btn.getAttribute('aria-expanded'), 'true');

  click(btn);
  assert.equal(more.classList.contains('open'), false);
  assert.equal(btn.getAttribute('aria-expanded'), 'false');
});

test('a click outside closes the dropdown', () => {
  setupDOM('<div id="site-nav"></div><p id="outside">elsewhere</p>');
  CSNav.mount();
  const more = document.querySelector('.nav-more');

  click(more.querySelector('.nav-more-btn'));
  assert.equal(more.classList.contains('open'), true);

  click(document.getElementById('outside'));
  assert.equal(more.classList.contains('open'), false);
});

test('a click inside the dropdown leaves it open', () => {
  CSNav.mount();
  const more = document.querySelector('.nav-more');
  click(more.querySelector('.nav-more-btn'));

  click(more.querySelector('.nav-more-menu a'));
  assert.equal(more.classList.contains('open'), true);
});

test('Escape closes the dropdown, other keys do not', () => {
  CSNav.mount();
  const more = document.querySelector('.nav-more');
  click(more.querySelector('.nav-more-btn'));

  keydown('a');
  assert.equal(more.classList.contains('open'), true);

  keydown('Escape');
  assert.equal(more.classList.contains('open'), false);
});

// ── Mobile drawer ─────────────────────────────────────────────────────────

test('the hamburger toggles the drawer and keeps aria-expanded in step', () => {
  CSNav.mount();
  const btn = document.querySelector('.nav-toggle');
  const links = document.querySelector('.nav-links');

  assert.equal(links.classList.contains('open'), false);
  assert.equal(btn.getAttribute('aria-expanded'), 'false');

  click(btn);
  assert.equal(links.classList.contains('open'), true);
  assert.equal(btn.getAttribute('aria-expanded'), 'true');

  click(btn);
  assert.equal(links.classList.contains('open'), false);
  assert.equal(btn.getAttribute('aria-expanded'), 'false');
});

// ── Language toggle ───────────────────────────────────────────────────────
// The button is what makes the 繁體中文 cheatsheets reachable at all, and it is
// rendered from a counterpart href the build supplies — never derived in the
// browser, so a page with no translation cannot link into a 404.

test('langToggleHTML renders nothing when the page has no counterpart', () => {
  assert.equal(CSNav.langToggleHTML('en', ''), '');
  assert.equal(CSNav.langToggleHTML('en', undefined), '');
});

test('langToggleHTML names the language you would switch TO, not the one you are in', () => {
  assert.match(CSNav.langToggleHTML('en', 'heap.zh.html'), />中文<\/a>$/);
  assert.match(CSNav.langToggleHTML('zh', 'heap.html'), />EN<\/a>$/);
});

test('langToggleHTML points at the counterpart and tags the button with its own language', () => {
  const html = CSNav.langToggleHTML('en', 'heap.zh.html');
  assert.match(html, /href="heap\.zh\.html"/);
  // The label is Chinese, so the element carries lang="zh-Hant" for screen readers.
  assert.match(html, /lang="zh-Hant"/);
  assert.match(CSNav.langToggleHTML('zh', 'heap.html'), /lang="en"/);
});

test('langToggleHTML escapes the href it is handed', () => {
  assert.ok(!CSNav.langToggleHTML('en', 'a"onmouseover="x').includes('"onmouseover='));
});

test('navHTML only grows a language toggle when a counterpart is supplied', () => {
  assert.ok(!CSNav.navHTML({ currentPage: 'cheatsheets' }).includes('lang-toggle'));
  const html = CSNav.navHTML({ currentPage: 'cheatsheets', lang: 'en', langAlt: 'heap.zh.html' });
  assert.match(html, /class="lang-toggle"/);
  // Sits at the end of the row: after the "more" menu, before the theme toggle.
  assert.ok(html.indexOf('lang-toggle') > html.indexOf('nav-more-menu'));
  assert.ok(html.indexOf('lang-toggle') < html.indexOf('theme-toggle'));
});

test('mount reads the counterpart off the placeholder', () => {
  const host = document.getElementById('site-nav');
  host.setAttribute('data-page', 'cheatsheets');
  host.setAttribute('data-base', '../');
  host.setAttribute('data-lang', 'zh');
  host.setAttribute('data-lang-alt', 'heap.html');
  CSNav.mount();
  const btn = host.querySelector('.lang-toggle');
  assert.equal(btn.getAttribute('href'), 'heap.html');
  assert.equal(btn.textContent, 'EN');
});

test('mount leaves the bar untouched on a page with no translation', () => {
  const host = document.getElementById('site-nav');
  host.setAttribute('data-page', 'cheatsheets');
  CSNav.mount();
  assert.equal(host.querySelector('.lang-toggle'), null);
});

// ── Skip link ─────────────────────────────────────────────────────────────
//
// It ships with the navbar rather than with each page, because nav.js is the one
// file all four page families load.

test('navHTML leads with a skip link', () => {
  assert.ok(CSNav.navHTML().startsWith('<a class="skip-link" href="#main">'));
});

test('mount labels the page content so the skip link has somewhere to land', () => {
  setupDOM('<div id="site-nav"></div><main class="container">body</main>');
  CSNav.mount();
  assert.equal(document.querySelector('main').id, 'main');
});

test('mount falls back to the content container when a page has no <main>', () => {
  // The hand-written LC tools wrap their content in a bare <div class="container">.
  setupDOM('<div id="site-nav"></div><div class="container">body</div>');
  CSNav.mount();
  assert.equal(document.querySelector('.container').id, 'main');
});

test('mount does not relabel a page that already names its own #main', () => {
  setupDOM('<div id="site-nav"></div><main class="nf" id="main">body</main>' +
           '<div class="container">footer</div>');
  CSNav.mount();
  assert.equal(document.querySelector('.container').id, '');
});

// ── Search shortcut ───────────────────────────────────────────────────────

/** Dispatches a keydown carrying modifiers, which the shared helper cannot. */
function press(key, init) {
  const event = new global.window.KeyboardEvent('keydown',
    Object.assign({ key, bubbles: true, cancelable: true }, init));
  global.document.dispatchEvent(event);
  return event;
}

test('"/" focuses the search box when the page has one', () => {
  setupDOM('<div id="site-nav"></div><input id="q">');
  CSNav.mount();
  const event = press('/');
  assert.equal(document.activeElement, document.getElementById('q'));
  assert.ok(event.defaultPrevented);
});

test('cmd-K does the same', () => {
  setupDOM('<div id="site-nav"></div><input id="q">');
  CSNav.mount();
  press('k', { metaKey: true });
  assert.equal(document.activeElement, document.getElementById('q'));
});

test('the shortcut leaves a keystroke alone while the reader is typing', () => {
  setupDOM('<div id="site-nav"></div><input id="filter"><input id="q">');
  CSNav.mount();
  document.getElementById('filter').focus();
  const event = press('/');
  assert.ok(!event.defaultPrevented, 'a "/" typed into a filter box must reach it');
  assert.equal(document.activeElement, document.getElementById('filter'));
});

test('a plain letter is not a shortcut', () => {
  setupDOM('<div id="site-nav"></div><input id="q">');
  CSNav.mount();
  const event = press('k');
  assert.ok(!event.defaultPrevented);
});
