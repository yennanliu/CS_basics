/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — pure rendering helpers shared by site/build-site.js

   Everything here is a plain string → string (or data → string) transform with
   no filesystem, git or markdown-it dependency, so it can be unit-tested. The
   priority-badge helpers in particular earn their tests: the badge carries a
   screen-reader sentence, and a blanket tag-strip once leaked that sentence into
   58 pages of TOC labels and 148 search records.

   site/build-site.js keeps the IO — reading files, running git, invoking
   markdown-it, writing _site.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSBuildLib = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // GitHub's heading-slug rule, which is the one the markdown in doc/ is written
  // against. Three details matter and the old implementation got all three wrong:
  //   - each dropped character leaves its surrounding spaces behind, so ' — ' becomes
  //     '--' rather than '-';
  //   - the result is NOT trimmed, so a heading ending in a ⭐ run keeps a trailing '-'
  //     (74 anchors under doc/cheatsheet already rely on that);
  //   - non-ASCII letters survive, so a heading like '前缀和' keeps its characters
  //     instead of collapsing to ''.
  // Star runs are lifted out of headings only AFTER ids are assigned (see
  // annotatePriorityHeadings), so the id is built from the star-bearing text.
  function slugify(text) {
    return text
      .replace(/<[^>]*>/g, '')
      .toLowerCase()
      .trim()
      .replace(/[^\p{L}\p{N}_\- ]+/gu, '')
      .replace(/ /g, '-');
  }

  // ── Priority (⭐) markers ─────────────────────────────────────────────────────
  // Cheatsheets mark interview-critical sections with a trailing ⭐…⭐⭐⭐⭐⭐ run
  // (see the style guide in CLAUDE.md). Raw emoji in a heading is easy to miss and
  // impossible to filter, so the run is lifted out of the heading text into a
  // badge and recorded as data-prio, which the TOC then reuses.
  const TIER_LABELS = {
    5: 'Must know — expect it in almost every loop',
    4: 'High value — a gap here costs you rounds',
    3: 'Worth knowing — usually a variant of a must-know pattern',
    2: 'Niche — read once, revisit only if a company is known to ask',
    1: 'Nice to have'
  };

  function prioBadge(level, extraClass = '') {
    const n = Math.max(1, Math.min(5, level));
    const stars = '★'.repeat(n) + '☆'.repeat(5 - n);
    return `<span class="prio prio-${n}${extraClass ? ' ' + extraClass : ''}" title="${TIER_LABELS[n]}">` +
      `<span class="prio-stars" aria-hidden="true">${stars}</span>` +
      `<span class="sr-only">Priority ${n} of 5 — ${TIER_LABELS[n]}</span></span>`;
  }

  // Matches exactly what prioBadge() emits. Anything reading a heading's *text*
  // (the TOC, the search index) has to drop the badge first, or the stars and the
  // screen-reader sentence end up in the label.
  const PRIO_BADGE_RE = /<span class="prio prio-\d[^"]*" title="[^"]*"><span class="prio-stars" aria-hidden="true">[^<]*<\/span><span class="sr-only">[^<]*<\/span><\/span>/g;

  function headingText(inner) {
    return inner
      .replace(PRIO_BADGE_RE, '')
      .replace(/<[^>]*>/g, '')
      .replace(/^[\s#]+/, '')
      .trim();
  }

  // Rewrites ⭐ runs in h2–h4 into a badge. Heading ids are left untouched (they
  // were slugified from the star-bearing text, so existing deep links keep working).
  function annotatePriorityHeadings(htmlContent) {
    let maxLevel = 0;
    const html = htmlContent.replace(/<h([2-4])([^>]*)>([\s\S]*?)<\/h\1>/g, (full, level, attrs, inner) => {
      const stars = inner.match(/⭐{1,5}/);
      if (!stars) return full;
      const n = Math.min(5, stars[0].length);
      if (n > maxLevel) maxLevel = n;
      const cleaned = inner.replace(/⭐{1,5}/g, '').replace(/\s{2,}/g, ' ').trim();
      return `<h${level}${attrs} data-prio="${n}">${cleaned}${prioBadge(n, 'prio-heading')}</h${level}>`;
    });
    return { html, hasPriority: maxLevel > 0 };
  }

  const PRIORITY_LEGEND =
    '<div class="prio-legend"><span class="prio-legend-label">Section priority</span>' +
    [5, 4, 3, 2].map(n =>
      `<span class="prio-legend-item">${prioBadge(n)}<span class="prio-legend-text">${TIER_LABELS[n].split(' — ')[0]}</span></span>`
    ).join('') +
    '<span class="prio-legend-note">Marked on the sections that carry it — unmarked sections are background/reference.</span></div>';

  // Nested TOC: h2 → h3, plus any h4 that carries a priority marker (those are the
  // per-pattern templates people actually navigate to). Rendered as a sticky rail
  // on wide screens and a collapsed <details> panel on narrow ones.
  const TOC_LABELS = {
    contents: 'Contents',
    // Given a section count, the word that follows it in the summary line.
    sections: n => `${n} section${n === 1 ? '' : 's'}`
  };

  function generateTOC(htmlContent, labels = {}) {
    const L = Object.assign({}, TOC_LABELS, labels);
    const headingRegex = /<h([234])(\s[^>]*)>([\s\S]*?)<\/h\1>/g;
    const headings = [];
    let match;
    while ((match = headingRegex.exec(htmlContent)) !== null) {
      const attrs = match[2];
      const idMatch = attrs.match(/id="([^"]*)"/);
      if (!idMatch) continue;
      const prioMatch = attrs.match(/data-prio="(\d)"/);
      const level = Number(match[1]);
      const prio = prioMatch ? Number(prioMatch[1]) : 0;
      // h4 only earns a TOC slot when it is flagged as interview-critical.
      if (level === 4 && prio < 4) continue;
      headings.push({
        level,
        prio,
        id: idMatch[1],
        text: headingText(match[3])
      });
    }
    if (headings.length < 3) return '';

    const entry = h =>
      `<li class="toc-item toc-l${h.level}${h.prio >= 4 ? ' toc-hot' : ''}">` +
      `<a href="#${h.id}">${h.text}` +
      (h.prio ? `<span class="toc-prio prio-${h.prio}" title="${TIER_LABELS[h.prio]}" aria-hidden="true">${'★'.repeat(h.prio)}</span>` : '') +
      '</a>';

    let toc = '';
    let openDepth = 0; // how many nested <ul> are currently open
    for (const h of headings) {
      const depth = h.level - 2; // 0 for h2, 1 for h3, 2 for h4
      while (openDepth > depth) { toc += '</li></ul>'; openDepth--; }
      if (openDepth < depth) {
        toc += `<ul class="toc-sublist toc-sublist-${depth}">`;
        openDepth = depth;
      } else if (toc) {
        toc += '</li>';
      }
      toc += entry(h);
    }
    while (openDepth > 0) { toc += '</li></ul>'; openDepth--; }
    if (toc) toc += '</li>';

    const sections = headings.filter(h => h.level === 2).length;
    return '<aside class="toc-rail">' +
      '<details class="toc" open data-toc>' +
      `<summary class="toc-summary"><span class="toc-summary-label">${L.contents}</span>` +
      `<span class="toc-count">${L.sections(sections)}</span></summary>` +
      `<nav class="toc-nav" aria-label="On this page"><ul class="toc-list">${toc}</ul></nav>` +
      '</details></aside>';
  }

  // ── Cross-language anchors ────────────────────────────────────────────────
  //
  // A translated sheet keeps its links verbatim, so `[見 §3](#two-pointers)` still
  // names the *English* heading slug — an anchor that does not exist on the
  // translated page, so the link silently lands at the top of the doc.
  //
  // The two files are the same document in two languages: same headings, same
  // order. So the Nth heading id in one maps to the Nth in the other. That is an
  // assumption, not a guarantee — a translator can drop or add a heading — so it
  // is verified before use and the whole map is abandoned if the shapes differ.
  // Abandoning it leaves the links exactly as broken as they were, never worse.

  function headingIds(htmlContent) {
    const ids = [];
    const re = /<h[1-4]\b[^>]*\bid="([^"]*)"/g;
    let m;
    while ((m = re.exec(htmlContent)) !== null) ids.push(m[1]);
    return ids;
  }

  function anchorMap(fromIds, toIds) {
    if (!fromIds || !toIds || fromIds.length !== toIds.length || !fromIds.length) return null;
    const map = new Map();
    for (let i = 0; i < fromIds.length; i++) {
      if (fromIds[i] !== toIds[i]) map.set(fromIds[i], toIds[i]);
    }
    return map;
  }

  /**
   * Rewrites every `#fragment` in `htmlContent` through the map for the page it
   * points at. `mapFor(page)` returns the map for a sibling page ('' for this
   * one); anything it cannot resolve is left untouched.
   */
  function retargetAnchors(htmlContent, mapFor) {
    return htmlContent.replace(/href="([^"]*#[^"]*)"/g, (full, href) => {
      const hash = href.indexOf('#');
      const page = href.slice(0, hash);
      // An absolute or parent-relative URL belongs to another site or section.
      if (/^[a-z]+:|^\/|\.\./i.test(page)) return full;
      const map = mapFor(page);
      if (!map) return full;
      const target = map.get(href.slice(hash + 1));
      return target ? `href="${page}#${target}"` : full;
    });
  }

  function extractHeadings(htmlContent) {
    const headingRegex = /<h([1-4])\s[^>]*?>(.*?)<\/h\1>/g;
    const headings = [];
    let match;
    while ((match = headingRegex.exec(htmlContent)) !== null) {
      const text = headingText(match[2]);
      if (text) headings.push(text);
    }
    return headings;
  }

  function ensureHeadingIds(htmlContent) {
    return htmlContent.replace(/<h([2-4])(?![^>]*\bid=)([^>]*)>(.*?)<\/h\1>/g, (_, level, attrs, text) =>
      `<h${level}${attrs} id="${slugify(text)}">${text}</h${level}>`
    );
  }

  function groupByCategory(items) {
    const grouped = {};
    for (const item of items) {
      if (!grouped[item.category]) grouped[item.category] = [];
      grouped[item.category].push(item);
    }
    return grouped;
  }

  function buildPrevNext(items, idx) {
    const prev = idx > 0 ? items[idx - 1] : null;
    const next = idx < items.length - 1 ? items[idx + 1] : null;
    return '<nav class="prev-next">' +
      (prev ? `<a href="${prev.file}.html" class="prev-link">← ${prev.title}</a>` : '<span></span>') +
      (next ? `<a href="${next.file}.html" class="next-link">${next.title} →</a>` : '<span></span>') +
      '</nav>';
  }

  function buildIndexGrid(grouped, categoryOrder, subFolder) {
    let html = '';
    for (const category of categoryOrder) {
      if (!grouped[category] || grouped[category].length === 0) continue;
      const count = grouped[category].length;
      html += `<h2 class="cat-heading">${category}` +
        `<span class="cat-count">${count} doc${count === 1 ? '' : 's'}</span></h2>` +
        '<div class="cheatsheet-grid sheet-grid">';
      for (const item of grouped[category]) {
        html += `\n        <article class="cheatsheet-card sheet-card">` +
          '<div class="card-top">' +
          `<h3 class="card-title"><a href="${subFolder}/${item.file}.html">${item.title}</a></h3></div>` +
          (item.description
            ? `<p class="card-desc">${item.description}</p>`
            : `<p><a href="${subFolder}/${item.file}.html" class="read-more">Read more →</a></p>`) +
          '</article>';
      }
      html += '</div>';
    }
    return html;
  }

  // A one-line summary for a doc that has no Scope line — used for the FAQ cards,
  // which were still title-plus-"Read more" long after the cheatsheet cards grew
  // descriptions. Preference order: an explicit Scope line, the lead paragraph
  // before the first heading, then the first few section headings. Never invents
  // prose that is not in the file.
  const SUMMARY_MAX = 240;

  // "Covers: 目錄 · REF" tells a reader nothing — skip the headings that are
  // navigation or boilerplate rather than subject matter.
  const NAVIGATIONAL_HEADING =
    /^(目錄|目录|contents?|table of contents|toc|index|ref|refs?|reference[s]?|參考(資料)?|参考(资料)?|summary|overview|leetcode problem lists?|備註|note[s]?)$/i;

  function truncateSummary(text) {
    if (text.length <= SUMMARY_MAX) return text;
    const window = text.slice(0, SUMMARY_MAX);
    const sentenceEnd = Math.max(window.lastIndexOf('. '), window.lastIndexOf('? '), window.lastIndexOf('! '));
    if (sentenceEnd >= SUMMARY_MAX * 0.5) return window.slice(0, sentenceEnd + 1);
    const wordEnd = window.lastIndexOf(' ');
    return (wordEnd > 0 ? window.slice(0, wordEnd) : window).replace(/[,;:]$/, '') + '…';
  }

  function flattenInlineMarkdown(text) {
    return text
      .replace(/!\[[^\]]*\]\([^)]*\)/g, '')
      .replace(/\[([^\]]+)\]\([^)]*\)/g, '$1')
      .replace(/<[^>]+>/g, '')
      .replace(/`([^`]*)`/g, '$1')
      .replace(/\*\*([^*]*)\*\*/g, '$1')
      .replace(/\*([^*]*)\*/g, '$1')
      .replace(/\s+/g, ' ')
      .trim();
  }

  function summariseDoc(rawMarkdown, headings = []) {
    const scope = extractScope(rawMarkdown);
    if (scope) return truncateSummary(scope);

    const lines = rawMarkdown.split('\n');
    const lead = [];
    for (let i = lines[0] && lines[0].startsWith('# ') ? 1 : 0; i < lines.length; i++) {
      const line = lines[i].trim();
      if (!line) { if (lead.length) break; continue; }
      // A heading, list, table, quote or fence means the lead paragraph is over
      // (or there never was one).
      if (/^(#{1,6} |[-*+] |\d+\. |> |\||```|<)/.test(line)) break;
      lead.push(line);
    }
    const paragraph = flattenInlineMarkdown(lead.join(' '));
    if (paragraph.length >= 40) return truncateSummary(paragraph);

    const sections = headings
      // Two letters is a real heading ("GC", "IO"); one is noise from a numbered list.
      .filter(hd => hd && hd.trim().length >= 2 && !NAVIGATIONAL_HEADING.test(hd))
      .slice(0, 3);
    return sections.length ? truncateSummary('Covers: ' + sections.join(' · ')) : null;
  }

  // Fixed chrome for the cheatsheet index, per language. Anything that is *data*
  // — category names, blurbs, tier labels, the "start here" reasons — stays in
  // data/cheatsheet_meta.json (under `zh` for the translation); only the sentences
  // that belong to this template live here.
  const INDEX_TEXT = {
    en: {
      h1: 'Algorithm &amp; Data Structure Cheat Sheets',
      intro: n => `${n} sheets, grouped by topic and ranked by how often the pattern actually shows up in a ` +
        'FAANG software-engineering loop. Read the <a href="#start-here">Start here</a> ladder first; ' +
        'the catalogue below is for lookup.',
      starsHeading: 'What the stars mean',
      starsAria: 'What the star ratings mean',
      starsFoot: 'The same stars appear on individual sections inside each sheet, so you can skim a ' +
        '4,000-line doc and still see which templates are the ones to memorise.',
      startHere: 'Start here',
      startBlurb: n => `${n} sheets in reading order. Together they cover the large majority of what a ` +
        'coding round will actually ask.',
      catalogue: 'Full catalogue',
      filterLabel: 'Filter',
      filterPlaceholder: 'Title, topic or description — e.g. window, dijkstra, knapsack',
      filterAria: 'Filter by interview priority',
      filterAll: 'All',
      filterFour: '★★★★ and up',
      filterFive: '★★★★★ only',
      sheetCount: n => `${n} sheet${n === 1 ? '' : 's'}`,
      stub: 'redirect',
      reference: 'imported reference',
      empty: 'No sheet matches that filter. ',
      emptyReset: 'Clear it',
      howTo: '<strong>How to use this:</strong> pick the sheet, read its Scope line to confirm it owns your ' +
        'problem, then jump straight to the starred sections. Every sheet links to its neighbours rather ' +
        'than repeating them.',
      source: 'Source: <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">doc/cheatsheet on GitHub</a> — ' +
        'ratings and grouping live in <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>.'
    },
    zh: {
      h1: '演算法與資料結構速查表',
      intro: n => `共 ${n} 份速查表，依主題分組，並以「在 FAANG 軟體工程面試中實際出現的頻率」排序。` +
        '建議先讀 <a href="#start-here">從這裡開始</a> 的閱讀順序，下方的完整目錄則供查閱用。',
      starsHeading: '星等代表什麼',
      starsAria: '星等評分說明',
      starsFoot: '每份速查表內部的章節也標了同一套星等，所以即使面對四千行的文件，' +
        '也能一眼看出哪些模板是非背不可的。',
      startHere: '從這裡開始',
      startBlurb: n => `${n} 份速查表，依閱讀順序排列。讀完這一串，就涵蓋了程式面試絕大多數會問到的內容。`,
      catalogue: '完整目錄',
      filterLabel: '篩選',
      filterPlaceholder: '標題、主題或描述 — 例如 window、dijkstra、knapsack',
      filterAria: '依面試重要度篩選',
      filterAll: '全部',
      filterFour: '★★★★ 以上',
      filterFive: '僅 ★★★★★',
      sheetCount: n => `${n} 份`,
      stub: '轉址',
      reference: '外部索引',
      empty: '沒有符合該條件的速查表。',
      emptyReset: '清除篩選',
      howTo: '<strong>使用方式：</strong>先挑主題，讀它的「範圍」那行確認這份文件確實涵蓋你的問題，' +
        '再直接跳到標星的章節。每份速查表只連向相鄰主題，不重複它們的內容。',
      source: '原始檔：<a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet/zh">GitHub 上的 doc/cheatsheet/zh</a> — ' +
        '星等與分組定義在 <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>。'
    }
  };

  // The cheatsheet index: a curated "start here" ladder, then every sheet grouped
  // by category and ordered by interview weight, each carrying its Scope line as a
  // description so the reader can tell 74 cards apart.
  //
  // `lang` picks both the chrome above and the `zh` overrides in meta, and decides
  // whether a card points at `<slug>.html` or `<slug>.zh.html`. Categories stay
  // keyed by their English name everywhere — grouping, anchors, data-category — so
  // the two indexes stay row-for-row comparable and a deep link works in either.
  function buildCheatsheetIndex(sheets, meta, lang = 'en') {
    const t = INDEX_TEXT[lang] || INDEX_TEXT.en;
    const zh = lang === 'zh' ? (meta.zh || {}) : {};
    const pick = (map, key, fallback) => (map && map[key] != null ? map[key] : fallback);

    const byFile = new Map(sheets.map(s => [s.file, s]));
    const tierLabel = n => pick(zh.tierLabels, String(n), meta.tierLabels[String(n)]).label;
    const tierNote = n => pick(zh.tierLabels, String(n), meta.tierLabels[String(n)]).note;
    const catName = c => pick(zh.categories, c, c);
    const catBlurb = c => pick(zh.categoryBlurbs, c, meta.categoryBlurbs[c]);
    const href = file => `cheatsheets/${file}${lang === 'zh' ? '.zh' : ''}.html`;

    const startHere = meta.startHere
      .map(s => ({ ...byFile.get(s.file), why: pick(zh.startHere, s.file, s.why) }))
      .filter(s => s.file);
    let html = `<h1>${t.h1}</h1><p class="intro">${t.intro(sheets.length)}</p>`;

    html += `<section class="tier-key" aria-label="${t.starsAria}">` +
      `<h2 class="key-heading">${t.starsHeading}</h2><ul class="tier-key-list">` +
      [5, 4, 3, 2].map(n =>
        `<li class="tier-key-item">${prioBadge(n)}<span class="tier-key-label">${tierLabel(n)}</span>` +
        `<span class="tier-key-note">${tierNote(n)}</span></li>`
      ).join('') +
      `</ul><p class="tier-key-foot">${t.starsFoot}</p></section>`;

    html += `<section class="start-here" id="start-here"><h2>${t.startHere}</h2>` +
      `<p class="cat-blurb">${t.startBlurb(startHere.length)}</p><ol class="start-list">`;
    for (const s of startHere) {
      html += `<li class="start-item"><a class="start-title" href="${href(s.file)}">${s.title}</a>` +
        `${prioBadge(s.tier, 'prio-compact')}<span class="start-why">${s.why}</span></li>`;
    }
    html += '</ol></section>';

    html += `<h2 class="catalogue-heading" id="catalogue">${t.catalogue}</h2>`;

    // Filter bar. It ships with everything visible and is wired up by site.js, so
    // the catalogue still works with JS off.
    html += '<div class="index-filter" data-sheet-filter>' +
      `<label class="filter-label" for="sheet-filter">${t.filterLabel}</label>` +
      '<input type="search" id="sheet-filter" class="filter-input" autocomplete="off" ' +
      `placeholder="${t.filterPlaceholder}">` +
      `<div class="filter-tiers" role="group" aria-label="${t.filterAria}">` +
      `<button type="button" class="filter-chip is-on" data-min-tier="0">${t.filterAll}</button>` +
      `<button type="button" class="filter-chip" data-min-tier="4">${t.filterFour}</button>` +
      `<button type="button" class="filter-chip" data-min-tier="5">${t.filterFive}</button>` +
      '</div>' +
      `<p class="filter-status" role="status" aria-live="polite" data-total="${sheets.length}"></p>` +
      '</div>';

    const grouped = groupByCategory(sheets);
    for (const category of meta.categoryOrder) {
      const items = grouped[category];
      if (!items || !items.length) continue;
      const anchor = slugify(category);
      html += `<section class="cat-section" data-category="${category}">`;
      html += `<h3 class="cat-heading" id="${anchor}">${catName(category)}` +
        `<span class="cat-count">${t.sheetCount(items.length)}</span></h3>`;
      if (catBlurb(category)) {
        html += `<p class="cat-blurb">${catBlurb(category)}</p>`;
      }
      html += '<div class="cheatsheet-grid sheet-grid">';
      for (const item of items) {
        const kindChip = item.kind === 'stub'
          ? `<span class="kind-chip kind-stub">${t.stub}</span>`
          : item.kind === 'reference'
            ? `<span class="kind-chip kind-reference">${t.reference}</span>`
            : '';
        // data-search is what the filter matches on: title, category and the Scope
        // line, so "window" finds Sliding Window and "dag" finds toposort. The
        // English category and slug ride along in both languages, so a reader who
        // only knows the English term can still find the 中文 card.
        const haystack = [item.title, category, catName(category), item.description || '',
          item.file.replace(/_/g, ' ')].join(' ').toLowerCase().replace(/"/g, '');
        html += `\n        <article class="cheatsheet-card sheet-card tier-${item.tier}"` +
          ` data-tier="${item.tier}" data-search="${haystack}">` +
          '<div class="card-top">' +
          `<h4 class="card-title"><a href="${href(item.file)}">${item.title}</a></h4>` +
          `${prioBadge(item.tier, 'prio-compact')}</div>` +
          (item.description ? `<p class="card-desc">${item.description}</p>` : '') +
          (kindChip ? `<p class="card-tags">${kindChip}</p>` : '') +
          '</article>';
      }
      html += '</div></section>';
    }
    html += `<p class="filter-empty" hidden>${t.empty}` +
      `<button type="button" class="filter-reset">${t.emptyReset}</button></p>`;

    html += '<div class="index-foot">' +
      `<p>${t.howTo}</p>` +
      `<p>${t.source}</p>` +
      '</div>';
    return html;
  }

  // The markdown H1 is the real, hand-written title; the page header used to show a
  // filename-derived one *above* it at a smaller size. Pull the H1 out of the body
  // so the page has exactly one, at the top, in the header.
  function splitLeadingH1(htmlContent) {
    const match = htmlContent.match(/^\s*<h1([^>]*)>([\s\S]*?)<\/h1>/);
    if (!match) return { title: null, titleId: null, html: htmlContent };
    const title = match[2].replace(/<[^>]*>/g, '').replace(/^[\s#]+/, '').trim();
    const idMatch = match[1].match(/id="([^"]*)"/);
    return {
      title: title || null,
      // Kept so links to the doc's top-level anchor still resolve.
      titleId: idMatch ? idMatch[1] : null,
      html: htmlContent.slice(match[0].length)
    };
  }

  // The page chrome around a rendered doc. `labels` lets a translated page carry
  // translated chrome without a second copy of this template — a 繁體中文 sheet
  // should not open with an English breadcrumb.
  const PAGE_LABELS = {
    home: 'Home',
    updated: 'Updated',
    // Takes the index label: Chinese wants no space between verb and object.
    backTo: label => `Back to ${label}`,
    edit: 'Edit on GitHub'
  };

  function buildPageContent({
    title, htmlContent, toc, lastMod, indexHref, indexLabel, githubHref,
    meta = '', legend = '', titleId = null, labels = {}
  }) {
    const L = Object.assign({}, PAGE_LABELS, labels);
    return `
        <nav class="breadcrumbs"><a href="../index.html">${L.home}</a> <span class="sep">›</span> <a href="../${indexHref}">${indexLabel}</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
        <div class="page-layout">
          ${toc}
          <div class="page-main">
            <div class="cheatsheet-header">
              <h1${titleId ? ` id="${titleId}"` : ''}>${title}</h1>
              <div class="header-meta">
                ${meta}
                ${lastMod ? `<span class="last-updated">${L.updated} ${lastMod}</span>` : ''}
              </div>
            </div>
            ${legend}
            <div class="cheatsheet-content">
              ${htmlContent}
            </div>
            <div class="cheatsheet-footer">
              <a href="../${indexHref}" class="back-link">← ${L.backTo(indexLabel)}</a>
              <a href="${githubHref}" class="github-edit" target="_blank">${L.edit} →</a>
            </div>
          </div>
        </div>
      `;
  }

  // Pulls the `> **Scope** — …` line out of a cheatsheet for use as its card
  // description. Markdown emphasis and links are flattened to plain text.
  // `範圍` is the same line on a 繁體中文 translation — matching both keeps a
  // translated sheet's card and search summary in its own language.
  const SCOPE_LINE = /^>\s*\*\*(?:Scope|範圍)\*\*\s*[—:：-]?\s*/;

  function extractScope(rawMarkdown) {
    const line = rawMarkdown.split('\n').slice(0, 12).find(l => SCOPE_LINE.test(l));
    if (!line) return null;
    return line
      .replace(SCOPE_LINE, '')
      .replace(/\[([^\]]+)\]\([^)]*\)/g, '$1')   // links → their text
      .replace(/`([^`]*)`/g, '$1')
      .replace(/\*\*([^*]*)\*\*/g, '$1')
      .replace(/\*([^*]*)\*/g, '$1')
      .trim();
  }

  function titleCaseFromFile(baseName) {
    return baseName.replace(/_/g, ' ').split(' ')
      .map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
  }

  return {
    slugify,
    TIER_LABELS,
    prioBadge,
    PRIO_BADGE_RE,
    headingText,
    annotatePriorityHeadings,
    PRIORITY_LEGEND,
    generateTOC,
    extractHeadings,
    headingIds,
    anchorMap,
    retargetAnchors,
    ensureHeadingIds,
    groupByCategory,
    buildPrevNext,
    buildIndexGrid,
    buildCheatsheetIndex,
    splitLeadingH1,
    buildPageContent,
    extractScope,
    titleCaseFromFile,
    summariseDoc,
  };
});
