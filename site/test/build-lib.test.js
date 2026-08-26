const test = require('node:test');
const assert = require('node:assert/strict');
const { JSDOM } = require('jsdom');

const lib = require('../build-lib.js');

// Parses a fragment so a test can assert on structure rather than on string
// shape — the TOC in particular has to come out as well-formed nested lists.
function parse(html) {
  return new JSDOM(`<!DOCTYPE html><html><body>${html}</body></html>`).window.document;
}

// ── slugify ───────────────────────────────────────────────────────────────

// These pin GitHub's rule, which is the one the markdown under doc/ is written
// against: drop the character, keep the spaces around it, do not trim the result.
test('slugify lowercases, strips tags, and drops punctuation without eating its spaces', () => {
  assert.equal(lib.slugify('Template 4: 0/1 Knapsack — LC 416'), 'template-4-01-knapsack--lc-416');
  assert.equal(lib.slugify('<span>Two Pointers</span>'), 'two-pointers');
  // ' — ' is space + dropped char + space, so it yields TWO dashes, not one
  assert.equal(lib.slugify('Reorder List — LC 143'), 'reorder-list--lc-143');
  // '/' has no spaces around it, so its neighbours join
  assert.equal(lib.slugify('Fast/Slow Cycle Detection'), 'fastslow-cycle-detection');
  // '&' behaves the same way
  assert.equal(lib.slugify('Templates & Algorithms'), 'templates--algorithms');
});

test('slugify does not trim the result, so a trailing star run leaves a dash', () => {
  // 74 anchors under doc/cheatsheet depend on this: the space before the star run
  // survives the run's removal and becomes a trailing '-'
  assert.equal(lib.slugify('Sort List — LC 148 ⭐⭐⭐⭐⭐'), 'sort-list--lc-148-');
  assert.equal(lib.slugify('⭐⭐⭐ Overview ⭐'), '-overview-');
});

test('slugify trims surrounding whitespace before slugifying', () => {
  assert.equal(lib.slugify('  (LC 53) '), 'lc-53');
});

test('slugify keeps non-ASCII letters instead of collapsing them', () => {
  assert.equal(lib.slugify('Prefix Sum (前缀和)'), 'prefix-sum-前缀和');
});

// ── prioBadge ─────────────────────────────────────────────────────────────

test('prioBadge fills one star per level out of five', () => {
  assert.match(lib.prioBadge(5), /★★★★★/);
  assert.match(lib.prioBadge(3), /★★★☆☆/);
  assert.match(lib.prioBadge(1), /★☆☆☆☆/);
});

test('prioBadge clamps out-of-range levels instead of emitting a broken badge', () => {
  assert.match(lib.prioBadge(9), /prio-5/);
  assert.match(lib.prioBadge(0), /prio-1/);
});

test('prioBadge carries the tier wording in both the tooltip and a screen-reader span', () => {
  const d = parse(lib.prioBadge(4));
  assert.equal(d.querySelector('.prio').getAttribute('title'), lib.TIER_LABELS[4]);
  assert.match(d.querySelector('.sr-only').textContent, /^Priority 4 of 5 — /);
  // The visual stars must not be read out twice.
  assert.equal(d.querySelector('.prio-stars').getAttribute('aria-hidden'), 'true');
});

test('prioBadge appends an extra class without dropping the tier class', () => {
  const cls = parse(lib.prioBadge(2, 'prio-compact')).querySelector('.prio').className;
  assert.equal(cls, 'prio prio-2 prio-compact');
});

// ── headingText ───────────────────────────────────────────────────────────

// This is the regression the module exists for: the badge's screen-reader
// sentence used to end up inside TOC labels and search records.
test('headingText drops the priority badge, stars and screen-reader text included', () => {
  const inner = 'Template 4: 0/1 Knapsack — LC 416' + lib.prioBadge(5, 'prio-heading');
  const text = lib.headingText(inner);
  assert.equal(text, 'Template 4: 0/1 Knapsack — LC 416');
  assert.ok(!text.includes('★'), 'stars leaked into the label');
  assert.ok(!text.includes('Priority'), 'screen-reader sentence leaked into the label');
});

test('headingText strips the anchor markup markdown-it wraps headings in', () => {
  const inner = '<a class="header-anchor" href="#overview"><span>Overview</span></a>';
  assert.equal(lib.headingText(inner), 'Overview');
});

test('headingText removes a leading permalink hash and surrounding whitespace', () => {
  assert.equal(lib.headingText('  # Key Properties  '), 'Key Properties');
});

// ── annotatePriorityHeadings ──────────────────────────────────────────────

test('annotatePriorityHeadings moves a star run out of the text into a badge', () => {
  const { html, hasPriority } = lib.annotatePriorityHeadings(
    '<h3 id="t4" tabindex="-1">Template 4 ⭐⭐⭐⭐⭐</h3>'
  );
  const h = parse(html).querySelector('h3');
  assert.equal(hasPriority, true);
  assert.equal(h.getAttribute('data-prio'), '5');
  assert.equal(h.getAttribute('id'), 't4', 'existing attributes must survive');
  assert.equal(h.getAttribute('tabindex'), '-1');
  assert.ok(!h.firstChild.textContent.includes('⭐'));
  assert.match(h.querySelector('.prio-stars').textContent, /★★★★★/);
});

test('annotatePriorityHeadings leaves unmarked headings byte-identical', () => {
  const input = '<h2 id="overview">Overview</h2><h3 id="key">Key Properties</h3>';
  const { html, hasPriority } = lib.annotatePriorityHeadings(input);
  assert.equal(html, input);
  assert.equal(hasPriority, false);
});

test('annotatePriorityHeadings rates by the first run but clears every run in the heading', () => {
  const { html } = lib.annotatePriorityHeadings('<h3 id="x">⭐⭐⭐ Pattern ⭐ note</h3>');
  const h = parse(html).querySelector('h3');
  assert.equal(h.getAttribute('data-prio'), '3');
  assert.ok(!h.textContent.includes('⭐'));
});

test('annotatePriorityHeadings ignores h1 and h5 — only h2–h4 carry section priority', () => {
  const input = '<h1 id="a">Title ⭐⭐⭐⭐⭐</h1><h5 id="b">Aside ⭐⭐</h5>';
  assert.equal(lib.annotatePriorityHeadings(input).html, input);
});

test('annotatePriorityHeadings reports hasPriority for a page with any marked section', () => {
  const input = '<h2 id="a">Plain</h2><h3 id="b">Marked ⭐⭐</h3>';
  assert.equal(lib.annotatePriorityHeadings(input).hasPriority, true);
});

// ── generateTOC ───────────────────────────────────────────────────────────

const h = (level, id, text, prio) =>
  `<h${level} id="${id}"${prio ? ` data-prio="${prio}"` : ''}>${text}${prio ? lib.prioBadge(prio, 'prio-heading') : ''}</h${level}>`;

test('generateTOC returns nothing for a page too short to need one', () => {
  assert.equal(lib.generateTOC(h(2, 'a', 'One') + h(2, 'b', 'Two')), '');
});

test('generateTOC nests h3 entries inside their h2 as well-formed lists', () => {
  const d = parse(lib.generateTOC(
    h(2, 'sec1', 'Section 1') + h(3, 'sub1', 'Sub 1') + h(3, 'sub2', 'Sub 2') + h(2, 'sec2', 'Section 2')
  ));
  const tops = d.querySelectorAll('.toc-list > .toc-l2');
  assert.equal(tops.length, 2);
  assert.equal(tops[0].querySelectorAll('.toc-sublist > .toc-l3').length, 2);
  assert.equal(tops[1].querySelectorAll('.toc-l3').length, 0);
});

test('generateTOC counts only top-level sections in its summary', () => {
  const d = parse(lib.generateTOC(
    h(2, 'a', 'A') + h(3, 'a1', 'A1') + h(3, 'a2', 'A2') + h(2, 'b', 'B')
  ));
  assert.equal(d.querySelector('.toc-count').textContent, '2 sections');
});

test('generateTOC singularises the section count', () => {
  const d = parse(lib.generateTOC(h(2, 'a', 'A') + h(3, 'a1', 'A1') + h(3, 'a2', 'A2')));
  assert.equal(d.querySelector('.toc-count').textContent, '1 section');
});

test('generateTOC admits an h4 only when it is flagged 4-star or higher', () => {
  const d = parse(lib.generateTOC(
    h(2, 'a', 'A') + h(3, 'a1', 'A1') +
    h(4, 'hot', 'Critical template', 5) +
    h(4, 'warm', 'Useful template', 4) +
    h(4, 'cool', 'Variant', 3) +
    h(4, 'plain', 'Background note')
  ));
  const l4 = [...d.querySelectorAll('.toc-l4 a')].map(a => a.getAttribute('href'));
  assert.deepEqual(l4, ['#hot', '#warm']);
});

test('generateTOC labels entries without the badge text and adds a compact star run', () => {
  const d = parse(lib.generateTOC(
    h(2, 'a', 'A') + h(3, 'b', 'B') + h(3, 'starred', 'Knapsack', 5)
  ));
  const link = d.querySelector('.toc-item.toc-hot a');
  assert.equal(link.firstChild.textContent, 'Knapsack');
  assert.ok(!link.textContent.includes('Priority 5 of 5'));
  assert.equal(link.querySelector('.toc-prio').textContent, '★★★★★');
  assert.equal(link.querySelector('.toc-prio').getAttribute('aria-hidden'), 'true');
});

test('generateTOC marks 4-star and 5-star rows as hot, lower tiers not', () => {
  const d = parse(lib.generateTOC(
    h(2, 'a', 'A') + h(3, 'five', 'Five', 5) + h(3, 'four', 'Four', 4) + h(3, 'three', 'Three', 3)
  ));
  assert.deepEqual(
    [...d.querySelectorAll('.toc-hot a')].map(a => a.getAttribute('href')),
    ['#five', '#four']
  );
});

test('generateTOC skips headings that have no id, since it cannot link to them', () => {
  const html = h(2, 'a', 'A') + '<h3>Unanchored</h3>' + h(3, 'b', 'B') + h(3, 'c', 'C');
  const d = parse(lib.generateTOC(html));
  assert.equal(d.querySelectorAll('.toc-item').length, 3);
  assert.ok(!d.body.textContent.includes('Unanchored'));
});

test('generateTOC ships expanded inside a details element so it works without JS', () => {
  const d = parse(lib.generateTOC(h(2, 'a', 'A') + h(3, 'b', 'B') + h(3, 'c', 'C')));
  const details = d.querySelector('details.toc');
  assert.ok(details.hasAttribute('open'));
  assert.ok(details.hasAttribute('data-toc'));
  assert.ok(d.querySelector('.toc-rail'));
});

// ── splitLeadingH1 ────────────────────────────────────────────────────────

test('splitLeadingH1 lifts the title out of the body and keeps its anchor id', () => {
  const { title, titleId, html } = lib.splitLeadingH1(
    '<h1 id="dynamic-programming-dp" tabindex="-1">Dynamic Programming (DP)</h1>\n<p>Body</p>'
  );
  assert.equal(title, 'Dynamic Programming (DP)');
  assert.equal(titleId, 'dynamic-programming-dp');
  assert.ok(!html.includes('<h1'));
  assert.match(html, /<p>Body<\/p>/);
});

test('splitLeadingH1 unwraps the permalink anchor inside the heading', () => {
  const { title } = lib.splitLeadingH1(
    '<h1 id="bst-binary-search-tree"><a class="header-anchor" href="#x"><span>BST (Binary Search Tree)</span></a></h1>'
  );
  assert.equal(title, 'BST (Binary Search Tree)');
});

test('splitLeadingH1 leaves the content alone when there is no leading h1', () => {
  const input = '<p>No title here</p><h1 id="late">Later</h1>';
  const { title, titleId, html } = lib.splitLeadingH1(input);
  assert.equal(title, null);
  assert.equal(titleId, null);
  assert.equal(html, input);
});

// ── extractScope ──────────────────────────────────────────────────────────

test('extractScope flattens links, code and emphasis into plain prose', () => {
  const md = [
    '# Heap',
    '',
    '> **Scope** — Both the *heap* and the `PriorityQueue` ADT. See [heap.md](./heap.md).',
    '> **See also**: [sort.md](./sort.md) — heap sort.'
  ].join('\n');
  assert.equal(
    lib.extractScope(md),
    'Both the heap and the PriorityQueue ADT. See heap.md.'
  );
});

test('extractScope returns null when the file has no Scope line', () => {
  assert.equal(lib.extractScope('# Topic\n\n## Overview\n'), null);
});

test('extractScope only looks at the header, not at a Scope line buried mid-file', () => {
  const md = '# Topic\n' + '\n'.repeat(20) + '> **Scope** — too late to count.\n';
  assert.equal(lib.extractScope(md), null);
});

// ── titleCaseFromFile ─────────────────────────────────────────────────────

test('titleCaseFromFile is the last-resort title when a file has no usable H1', () => {
  assert.equal(lib.titleCaseFromFile('sliding_window'), 'Sliding Window');
  assert.equal(lib.titleCaseFromFile('2_pointers'), '2 Pointers');
});

// ── groupByCategory / buildPrevNext ───────────────────────────────────────

test('groupByCategory buckets items and preserves their order within a bucket', () => {
  const grouped = lib.groupByCategory([
    { file: 'a', category: 'Graphs' },
    { file: 'b', category: 'Trees' },
    { file: 'c', category: 'Graphs' }
  ]);
  assert.deepEqual(Object.keys(grouped), ['Graphs', 'Trees']);
  assert.deepEqual(grouped.Graphs.map(i => i.file), ['a', 'c']);
});

test('buildPrevNext links both neighbours in the middle of the list', () => {
  const items = [
    { file: 'a', title: 'Alpha' }, { file: 'b', title: 'Beta' }, { file: 'c', title: 'Gamma' }
  ];
  const d = parse(lib.buildPrevNext(items, 1));
  assert.equal(d.querySelector('.prev-link').getAttribute('href'), 'a.html');
  assert.equal(d.querySelector('.next-link').getAttribute('href'), 'c.html');
});

test('buildPrevNext leaves a placeholder rather than a dead link at either end', () => {
  const items = [{ file: 'a', title: 'Alpha' }, { file: 'b', title: 'Beta' }];
  assert.equal(parse(lib.buildPrevNext(items, 0)).querySelector('.prev-link'), null);
  assert.equal(parse(lib.buildPrevNext(items, 1)).querySelector('.next-link'), null);
});

// ── buildPageContent ──────────────────────────────────────────────────────

const pageArgs = (over = {}) => Object.assign({
  title: 'Heap & Priority Queue',
  htmlContent: '<h2 id="overview">Overview</h2>',
  toc: '<aside class="toc-rail">toc</aside>',
  lastMod: 'Aug 13, 2026',
  indexHref: 'cheatsheets.html',
  indexLabel: 'Cheat Sheets',
  githubHref: 'https://github.com/x/y',
  meta: '<span class="cat-chip">Trees &amp; Heaps</span>',
  legend: '<div class="prio-legend">key</div>',
  titleId: 'heap-priority-queue'
}, over);

test('buildPageContent emits exactly one h1, in the header, carrying its anchor id', () => {
  const d = parse(lib.buildPageContent(pageArgs()));
  const h1s = d.querySelectorAll('h1');
  assert.equal(h1s.length, 1);
  assert.equal(h1s[0].getAttribute('id'), 'heap-priority-queue');
  assert.ok(d.querySelector('.cheatsheet-header h1'));
});

test('buildPageContent puts the TOC rail beside the content, not above it', () => {
  const d = parse(lib.buildPageContent(pageArgs()));
  const layout = d.querySelector('.page-layout');
  assert.deepEqual(
    [...layout.children].map(el => el.className),
    ['toc-rail', 'page-main']
  );
});

test('buildPageContent omits the updated line when the date is unknown', () => {
  const d = parse(lib.buildPageContent(pageArgs({ lastMod: null })));
  assert.equal(d.querySelector('.last-updated'), null);
});

test('buildPageContent omits the legend on a page with no marked sections', () => {
  const d = parse(lib.buildPageContent(pageArgs({ legend: '' })));
  assert.equal(d.querySelector('.prio-legend'), null);
});

test('buildPageContent works for a page with no heading id to preserve', () => {
  const d = parse(lib.buildPageContent(pageArgs({ titleId: null })));
  assert.equal(d.querySelector('h1').hasAttribute('id'), false);
});

// ── buildCheatsheetIndex ──────────────────────────────────────────────────

const META = {
  tierLabels: {
    5: { label: 'Must know', note: 'Every loop.' },
    4: { label: 'High value', note: 'Costs you rounds.' },
    3: { label: 'Worth knowing', note: 'A variant.' },
    2: { label: 'Niche', note: 'Rare.' }
  },
  categoryOrder: ['Arrays & Strings', 'Graphs', 'Empty Category'],
  categoryBlurbs: { 'Arrays & Strings': 'Highest-frequency surface.' },
  startHere: [{ file: 'array', why: 'Start with the operations.' }]
};

const SHEETS = [
  { file: 'array', title: 'Array', category: 'Arrays & Strings', tier: 5, kind: 'sheet', description: 'Array fundamentals.' },
  { file: 'difference_array', title: 'Difference Array', category: 'Arrays & Strings', tier: 3, kind: 'sheet', description: 'Range update.' },
  { file: 'graph', title: 'Graph Algorithms', category: 'Graphs', tier: 5, kind: 'sheet', description: 'Representation and traversal.' },
  { file: 'priority_queue', title: 'Priority Queue → see Heap', category: 'Graphs', tier: 2, kind: 'stub', description: 'Redirect only.' }
];

test('buildCheatsheetIndex gives every card a description and a tier badge', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  const cards = d.querySelectorAll('.sheet-card');
  assert.equal(cards.length, 4);
  for (const card of cards) {
    assert.ok(card.querySelector('.card-desc'), 'card without a description');
    assert.ok(card.querySelector('.prio'), 'card without a tier badge');
  }
});

test('buildCheatsheetIndex renders categories in the configured order and skips empty ones', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  assert.deepEqual(
    [...d.querySelectorAll('.cat-heading')].map(el => el.firstChild.textContent.trim()),
    ['Arrays & Strings', 'Graphs']
  );
});

test('buildCheatsheetIndex counts the sheets in each category', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  assert.deepEqual(
    [...d.querySelectorAll('.cat-count')].map(el => el.textContent),
    ['2 sheets', '2 sheets']
  );
});

test('buildCheatsheetIndex carries the tier class onto the card for its emphasis stripe', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  const array = [...d.querySelectorAll('.sheet-card')].find(c => /Array$/.test(c.querySelector('.card-title').textContent));
  assert.ok(array.classList.contains('tier-5'));
});

test('buildCheatsheetIndex flags a redirect stub so it is not mistaken for a real sheet', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  const stub = d.querySelector('.kind-stub');
  assert.equal(stub.textContent, 'redirect');
  assert.match(stub.closest('.sheet-card').querySelector('.card-title').textContent, /Priority Queue/);
});

test('buildCheatsheetIndex builds the start-here ladder from the metadata, with reasons', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  const items = d.querySelectorAll('.start-item');
  assert.equal(items.length, 1);
  assert.equal(items[0].querySelector('.start-title').getAttribute('href'), 'cheatsheets/array.html');
  assert.equal(items[0].querySelector('.start-why').textContent, 'Start with the operations.');
});

test('buildCheatsheetIndex drops a start-here entry whose sheet no longer exists', () => {
  const meta = Object.assign({}, META, {
    startHere: [{ file: 'array', why: 'Real.' }, { file: 'deleted_sheet', why: 'Gone.' }]
  });
  const d = parse(lib.buildCheatsheetIndex(SHEETS, meta));
  assert.equal(d.querySelectorAll('.start-item').length, 1);
});

test('buildCheatsheetIndex explains every tier in its key', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  assert.deepEqual(
    [...d.querySelectorAll('.tier-key-label')].map(el => el.textContent),
    ['Must know', 'High value', 'Worth knowing', 'Niche']
  );
});

test('buildCheatsheetIndex states the sheet total in its intro', () => {
  const d = parse(lib.buildCheatsheetIndex(SHEETS, META));
  assert.match(d.querySelector('.intro').textContent, /^4 sheets, /);
});

// ── buildIndexGrid (the plain grid the FAQ index shares) ──────────────────

test('buildIndexGrid links each card into the given subfolder', () => {
  const grouped = { Java: [{ file: 'java_jvm', title: 'JVM FAQ' }] };
  const d = parse(lib.buildIndexGrid(grouped, ['Java'], 'faqs'));
  assert.equal(d.querySelector('.cheatsheet-card h3 a').getAttribute('href'), 'faqs/java_jvm.html');
});

// ── extractHeadings (feeds the search index) ──────────────────────────────

test('extractHeadings collects heading text without the priority badge', () => {
  const html = h(2, 'a', 'Overview') + h(3, 'b', 'Knapsack', 5);
  assert.deepEqual(lib.extractHeadings(html), ['Overview', 'Knapsack']);
});

test('extractHeadings ignores headings that render empty', () => {
  assert.deepEqual(lib.extractHeadings('<h2 id="a"></h2><h3 id="b">Real</h3>'), ['Real']);
});

// ── ensureHeadingIds ──────────────────────────────────────────────────────

test('ensureHeadingIds only fills in ids that are missing', () => {
  const out = lib.ensureHeadingIds('<h2 id="kept">A</h2><h3>Needs An Id</h3>');
  assert.match(out, /<h2 id="kept">/);
  assert.match(out, /<h3 id="needs-an-id">/);
});

// ── summariseDoc (FAQ cards, which have no Scope line) ────────────────────

test('summariseDoc prefers an explicit Scope line when the doc has one', () => {
  const md = '# Heap\n\n> **Scope** — Heaps and priority queues.\n\nBody text here.\n';
  assert.equal(lib.summariseDoc(md, ['Overview']), 'Heaps and priority queues.');
});

test('summariseDoc falls back to the lead paragraph before the first heading', () => {
  const md = [
    '# Redis for Backend Engineers',
    '',
    'Redis is an in-memory data structure store widely used in modern backend',
    'systems for caching, sessions and real-time analytics.',
    '',
    '## 1. What is Redis?'
  ].join('\n');
  assert.equal(
    lib.summariseDoc(md, ['1. What is Redis?']),
    'Redis is an in-memory data structure store widely used in modern backend systems for caching, sessions and real-time analytics.'
  );
});

test('summariseDoc lists the sections when the doc opens straight into headings', () => {
  const md = '# JVM FAQ\n\n### 1) JVM internal storage ?\n\n- notes\n';
  assert.equal(
    lib.summariseDoc(md, ['JVM FAQ', '1) JVM internal storage ?', 'GC', 'Class loading', 'Tuning']),
    'Covers: JVM FAQ · 1) JVM internal storage ? · GC'
  );
});

test('summariseDoc skips navigational headings that describe nothing', () => {
  const md = '# CS Basics\n\n## 目錄\n';
  assert.equal(
    lib.summariseDoc(md, ['目錄', 'Contents', 'REF', 'Processes vs Threads', 'Memory']),
    'Covers: Processes vs Threads · Memory'
  );
});

test('summariseDoc returns null rather than inventing a summary for a link dump', () => {
  const md = '# Airflow FAQ\n\n1. How can you scale Airflow?\n\t- https://example.test/a\n';
  assert.equal(lib.summariseDoc(md, []), null);
});

test('summariseDoc ignores a list or table that follows the H1 directly', () => {
  const md = '# Topic\n\n- bullet one\n- bullet two\n\n## Real Section\n';
  assert.equal(lib.summariseDoc(md, ['Real Section']), 'Covers: Real Section');
});

test('summariseDoc flattens links and code out of the lead paragraph', () => {
  const md = '# T\n\nUses `heapq` and the [heap docs](https://example.test) for **top-k** selection over a stream of events.\n';
  assert.equal(
    lib.summariseDoc(md, []),
    'Uses heapq and the heap docs for top-k selection over a stream of events.'
  );
});

test('summariseDoc truncates a long lead paragraph at a sentence boundary', () => {
  const first = 'A'.repeat(120) + '.';
  const md = `# T\n\n${first} ${'B'.repeat(200)}.\n`;
  const summary = lib.summariseDoc(md, []);
  assert.equal(summary, first);
  assert.ok(summary.length <= 240);
});

test('summariseDoc ellipsises when there is no sentence break to cut at', () => {
  const md = '# T\n\n' + Array.from({ length: 60 }, (_, i) => `word${i}`).join(' ') + '\n';
  const summary = lib.summariseDoc(md, []);
  assert.ok(summary.endsWith('…'));
  assert.ok(summary.length <= 241);
});

// ── buildIndexGrid with descriptions ─────────────────────────────────────

test('buildIndexGrid shows a description when the doc has one', () => {
  const grouped = { Java: [{ file: 'java_jvm', title: 'JVM FAQ', description: 'Memory areas and GC.' }] };
  const d = parse(lib.buildIndexGrid(grouped, ['Java'], 'faqs'));
  assert.equal(d.querySelector('.card-desc').textContent, 'Memory areas and GC.');
  assert.equal(d.querySelector('.read-more'), null);
});

test('buildIndexGrid keeps a read-more link for a doc it could not summarise', () => {
  const grouped = { Java: [{ file: 'java_jvm', title: 'JVM FAQ', description: null }] };
  const d = parse(lib.buildIndexGrid(grouped, ['Java'], 'faqs'));
  assert.equal(d.querySelector('.card-desc'), null);
  assert.equal(d.querySelector('.read-more').getAttribute('href'), 'faqs/java_jvm.html');
});

test('buildIndexGrid counts the docs in each category', () => {
  const grouped = { Java: [{ file: 'a', title: 'A' }, { file: 'b', title: 'B' }], SQL: [{ file: 'c', title: 'C' }] };
  const d = parse(lib.buildIndexGrid(grouped, ['Java', 'SQL'], 'faqs'));
  assert.deepEqual([...d.querySelectorAll('.cat-count')].map(el => el.textContent), ['2 docs', '1 doc']);
});
