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

  function slugify(text) {
    return text.toLowerCase().replace(/<[^>]*>/g, '').replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
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
  function generateTOC(htmlContent) {
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
      `<summary class="toc-summary"><span class="toc-summary-label">Contents</span>` +
      `<span class="toc-count">${sections} section${sections === 1 ? '' : 's'}</span></summary>` +
      `<nav class="toc-nav" aria-label="On this page"><ul class="toc-list">${toc}</ul></nav>` +
      '</details></aside>';
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

  // The cheatsheet index: a curated "start here" ladder, then every sheet grouped
  // by category and ordered by interview weight, each carrying its Scope line as a
  // description so the reader can tell 74 cards apart.
  function buildCheatsheetIndex(sheets, meta) {
    const byFile = new Map(sheets.map(s => [s.file, s]));
    const tierLabel = tier => meta.tierLabels[String(tier)].label;

    const startHere = meta.startHere.map(s => ({ ...byFile.get(s.file), why: s.why })).filter(s => s.file);
    let html = '<h1>Algorithm &amp; Data Structure Cheat Sheets</h1>' +
      `<p class="intro">${sheets.length} sheets, grouped by topic and ranked by how often the pattern ` +
      'actually shows up in a FAANG software-engineering loop. Read the ' +
      '<a href="#start-here">Start here</a> ladder first; the catalogue below is for lookup.</p>';

    html += '<section class="tier-key" aria-label="What the star ratings mean">' +
      '<h2 class="key-heading">What the stars mean</h2><ul class="tier-key-list">' +
      [5, 4, 3, 2].map(t =>
        `<li class="tier-key-item">${prioBadge(t)}<span class="tier-key-label">${tierLabel(t)}</span>` +
        `<span class="tier-key-note">${meta.tierLabels[String(t)].note}</span></li>`
      ).join('') +
      '</ul><p class="tier-key-foot">The same stars appear on individual sections inside each sheet, so you can ' +
      'skim a 4,000-line doc and still see which templates are the ones to memorise.</p></section>';

    html += '<section class="start-here" id="start-here"><h2>Start here</h2>' +
      `<p class="cat-blurb">${startHere.length} sheets in reading order. Together they cover the large majority ` +
      'of what a coding round will actually ask.</p><ol class="start-list">';
    for (const s of startHere) {
      html += `<li class="start-item"><a class="start-title" href="cheatsheets/${s.file}.html">${s.title}</a>` +
        `${prioBadge(s.tier, 'prio-compact')}<span class="start-why">${s.why}</span></li>`;
    }
    html += '</ol></section>';

    html += '<h2 class="catalogue-heading" id="catalogue">Full catalogue</h2>';

    // Filter bar. It ships with everything visible and is wired up by site.js, so
    // the catalogue still works with JS off.
    html += '<div class="index-filter" data-sheet-filter>' +
      '<label class="filter-label" for="sheet-filter">Filter</label>' +
      `<input type="search" id="sheet-filter" class="filter-input" autocomplete="off" ` +
      `placeholder="Title, topic or description — e.g. window, dijkstra, knapsack">` +
      '<div class="filter-tiers" role="group" aria-label="Filter by interview priority">' +
      '<button type="button" class="filter-chip is-on" data-min-tier="0">All</button>' +
      '<button type="button" class="filter-chip" data-min-tier="4">★★★★ and up</button>' +
      '<button type="button" class="filter-chip" data-min-tier="5">★★★★★ only</button>' +
      '</div>' +
      `<p class="filter-status" role="status" aria-live="polite" data-total="${sheets.length}"></p>` +
      '</div>';

    const grouped = groupByCategory(sheets);
    for (const category of meta.categoryOrder) {
      const items = grouped[category];
      if (!items || !items.length) continue;
      const anchor = slugify(category);
      html += `<section class="cat-section" data-category="${category}">`;
      html += `<h3 class="cat-heading" id="${anchor}">${category}` +
        `<span class="cat-count">${items.length} sheet${items.length === 1 ? '' : 's'}</span></h3>`;
      if (meta.categoryBlurbs[category]) {
        html += `<p class="cat-blurb">${meta.categoryBlurbs[category]}</p>`;
      }
      html += '<div class="cheatsheet-grid sheet-grid">';
      for (const item of items) {
        const kindChip = item.kind === 'stub'
          ? '<span class="kind-chip kind-stub">redirect</span>'
          : item.kind === 'reference'
            ? '<span class="kind-chip kind-reference">imported reference</span>'
            : '';
        // data-search is what the filter matches on: title, category and the
        // Scope line, so "window" finds Sliding Window and "dag" finds toposort.
        const haystack = [item.title, category, item.description || '', item.file.replace(/_/g, ' ')]
          .join(' ').toLowerCase().replace(/"/g, '');
        html += `\n        <article class="cheatsheet-card sheet-card tier-${item.tier}"` +
          ` data-tier="${item.tier}" data-search="${haystack}">` +
          '<div class="card-top">' +
          `<h4 class="card-title"><a href="cheatsheets/${item.file}.html">${item.title}</a></h4>` +
          `${prioBadge(item.tier, 'prio-compact')}</div>` +
          (item.description ? `<p class="card-desc">${item.description}</p>` : '') +
          (kindChip ? `<p class="card-tags">${kindChip}</p>` : '') +
          '</article>';
      }
      html += '</div></section>';
    }
    html += '<p class="filter-empty" hidden>No sheet matches that filter. ' +
      '<button type="button" class="filter-reset">Clear it</button></p>';

    html += '<div class="index-foot">' +
      '<p><strong>How to use this:</strong> pick the sheet, read its Scope line to confirm it owns your problem, ' +
      'then jump straight to the starred sections. Every sheet links to its neighbours rather than repeating them.</p>' +
      '<p>Source: <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">doc/cheatsheet on GitHub</a> — ' +
      'ratings and grouping live in <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>.</p>' +
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

  function buildPageContent({
    title, htmlContent, toc, lastMod, indexHref, indexLabel, githubHref,
    meta = '', legend = '', titleId = null
  }) {
    return `
        <nav class="breadcrumbs"><a href="../index.html">Home</a> <span class="sep">›</span> <a href="../${indexHref}">${indexLabel}</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
        <div class="page-layout">
          ${toc}
          <div class="page-main">
            <div class="cheatsheet-header">
              <h1${titleId ? ` id="${titleId}"` : ''}>${title}</h1>
              <div class="header-meta">
                ${meta}
                ${lastMod ? `<span class="last-updated">Updated ${lastMod}</span>` : ''}
              </div>
            </div>
            ${legend}
            <div class="cheatsheet-content">
              ${htmlContent}
            </div>
            <div class="cheatsheet-footer">
              <a href="../${indexHref}" class="back-link">← Back to ${indexLabel}</a>
              <a href="${githubHref}" class="github-edit" target="_blank">Edit on GitHub →</a>
            </div>
          </div>
        </div>
      `;
  }

  // Pulls the `> **Scope** — …` line out of a cheatsheet for use as its card
  // description. Markdown emphasis and links are flattened to plain text.
  function extractScope(rawMarkdown) {
    const line = rawMarkdown.split('\n').slice(0, 12).find(l => l.startsWith('> **Scope**'));
    if (!line) return null;
    return line
      .replace(/^>\s*\*\*Scope\*\*\s*—?\s*/, '')
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
