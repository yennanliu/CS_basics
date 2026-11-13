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

  // The badge, legend and TOC wording on a translated page. Keyed by `lang` so
  // a 中文 page does not carry English tooltips and screen-reader text on every
  // starred heading.
  const PRIO_TEXT = {
    en: {
      tiers: TIER_LABELS,
      sr: (n, label) => `Priority ${n} of 5 — ${label}`,
      legendLabel: 'Section priority',
      legendNote: 'Marked on the sections that carry it — unmarked sections are background/reference.'
    },
    zh: {
      tiers: {
        5: '必備 — 幾乎每一輪面試都會出現',
        4: '高價值 — 這裡有缺口就會掉關',
        3: '值得會 — 多半是必備模式的變形',
        2: '冷門 — 讀過一次即可，除非目標公司已知會問',
        1: '加分項'
      },
      sr: (n, label) => `優先度 ${n}／5 — ${label}`,
      legendLabel: '章節優先度',
      legendNote: '只標在真正需要的章節上 —— 沒標的是背景／參考資料。'
    }
  };
  const prioText = lang => PRIO_TEXT[lang] || PRIO_TEXT.en;

  function prioBadge(level, extraClass = '', lang = 'en') {
    const n = Math.max(1, Math.min(5, level));
    const stars = '★'.repeat(n) + '☆'.repeat(5 - n);
    const t = prioText(lang);
    return `<span class="prio prio-${n}${extraClass ? ' ' + extraClass : ''}" title="${t.tiers[n]}">` +
      `<span class="prio-stars" aria-hidden="true">${stars}</span>` +
      `<span class="sr-only">${t.sr(n, t.tiers[n])}</span></span>`;
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
  function annotatePriorityHeadings(htmlContent, lang = 'en') {
    let maxLevel = 0;
    const html = htmlContent.replace(/<h([2-4])([^>]*)>([\s\S]*?)<\/h\1>/g, (full, level, attrs, inner) => {
      const stars = inner.match(/⭐{1,5}/);
      if (!stars) return full;
      const n = Math.min(5, stars[0].length);
      if (n > maxLevel) maxLevel = n;
      const cleaned = inner.replace(/⭐{1,5}/g, '').replace(/\s{2,}/g, ' ').trim();
      return `<h${level}${attrs} data-prio="${n}">${cleaned}${prioBadge(n, 'prio-heading', lang)}</h${level}>`;
    });
    return { html, hasPriority: maxLevel > 0 };
  }

  function priorityLegend(lang = 'en') {
    const t = prioText(lang);
    return `<div class="prio-legend"><span class="prio-legend-label">${t.legendLabel}</span>` +
      [5, 4, 3, 2].map(n =>
        `<span class="prio-legend-item">${prioBadge(n, '', lang)}<span class="prio-legend-text">${t.tiers[n].split(' — ')[0]}</span></span>`
      ).join('') +
      `<span class="prio-legend-note">${t.legendNote}</span></div>`;
  }
  const PRIORITY_LEGEND = priorityLegend('en');

  // Nested TOC: h2 → h3, plus any h4 that carries a priority marker (those are the
  // per-pattern templates people actually navigate to). Rendered as a sticky rail
  // on wide screens and a collapsed <details> panel on narrow ones.
  const TOC_LABELS = {
    contents: 'Contents',
    tiers: TIER_LABELS,
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
      (h.prio ? `<span class="toc-prio prio-${h.prio}" title="${L.tiers[h.prio]}" aria-hidden="true">${'★'.repeat(h.prio)}</span>` : '') +
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

  // The grid's own words. Category names are not in here: they are the English
  // page family's keys, so both languages group a doc identically, and the 中文
  // index passes a `catName` that renames them for display only.
  const GRID_TEXT = {
    en: { docCount: n => `${n} doc${n === 1 ? '' : 's'}`, readMore: 'Read more →' },
    zh: { docCount: n => `${n} 篇`, readMore: '閱讀全文 →' }
  };

  /**
   * The plain category-and-cards grid the FAQ indexes use, in either language.
   * `lang` picks the grid's own words and the `.zh` page suffix; `catName`
   * renames a category for display without changing how docs are grouped.
   */
  function buildIndexGrid(grouped, categoryOrder, subFolder, { lang = 'en', catName = c => c } = {}) {
    const t = GRID_TEXT[lang] || GRID_TEXT.en;
    const href = item => `${subFolder}/${item.file}${lang === 'zh' ? '.zh' : ''}.html`;
    let html = '';
    for (const category of categoryOrder) {
      if (!grouped[category] || grouped[category].length === 0) continue;
      const count = grouped[category].length;
      html += `<h2 class="cat-heading">${catName(category)}` +
        `<span class="cat-count">${t.docCount(count)}</span></h2>` +
        '<div class="cheatsheet-grid sheet-grid">';
      for (const item of grouped[category]) {
        html += `\n        <article class="cheatsheet-card sheet-card">` +
          '<div class="card-top">' +
          `<h3 class="card-title"><a href="${href(item)}">${item.title}</a></h3></div>` +
          (item.description
            ? `<p class="card-desc">${item.description}</p>`
            : `<p><a href="${href(item)}" class="read-more">${t.readMore}</a></p>`) +
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
      intro: n => `${n} sheets, ranked by how often the pattern actually shows up in a FAANG ` +
        'software-engineering loop. Follow the <a href="#review-path">review path</a> to work through ' +
        'the ones that matter in order, or browse by priority or topic.',
      stats: {
        sheets: 'sheets',
        must: 'must know',
        high: 'high value',
        drill: 'drill problems ok'
      },
      views: { path: 'Review path', priority: 'By priority', topic: 'By topic' },
      viewsAria: 'Choose how to browse the cheat sheets',
      tierCount: n => `${n} sheet${n === 1 ? '' : 's'}`,
      jumpTo: 'Jump to',
      // Review path
      pathHeading: 'Review path',
      pathBlurb: (n, total) => `${n} core sheets in study order — the ${total} foundations first, then every ` +
        'other must-know sheet, then the high-value ones. Mark a sheet reviewed when you have re-read its ' +
        'starred templates; it comes due again a week later (two for high value).',
      stages: {
        start: ['Foundations', 'The reading ladder: each one unlocks the next.'],
        must: ['Must know', 'The rest of what shows up in almost every loop.'],
        high: ['High value', 'Common enough that a gap here costs a round.']
      },
      nextUp: 'Next up',
      open: 'Open sheet',
      mark: 'Mark reviewed',
      unmark: 'Undo',
      reset: 'Reset progress',
      drill: (ok, total) => `drill ${ok}/${total} ok`,
      drillNext: 'next',
      drillLink: 'drill path',
      noDrill: 'no drill path yet',
      examples: 'examples',
      // relative-time words the page script uses; kept here so one table holds the chrome
      rel: {
        never: 'not reviewed',
        today: 'reviewed today',
        daysAgo: 'reviewed {n}d ago',
        due: 'due again',
        dueIn: 'next in {n}d',
        allDone: 'Everything on the path is reviewed and nothing is due. Pick any sheet to go deeper.',
        progress: '{n} of {total} reviewed',
        storage: 'Progress is kept in this browser only.'
      },
      // Catalogue
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
        'problem, then jump straight to the starred sections — the same stars mark the sections inside ' +
        'each sheet that are worth memorising. The drill counts come from the practice log.',
      source: 'Source: <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">doc/cheatsheet on GitHub</a> — ' +
        'ratings and grouping live in <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>.'
    },
    zh: {
      h1: '演算法與資料結構速查表',
      intro: n => `共 ${n} 份速查表，依「在 FAANG 軟體工程面試中實際出現的頻率」排序。` +
        '照著 <a href="#review-path">複習路徑</a> 依序讀完最重要的那些，或依優先度、主題瀏覽。',
      stats: {
        sheets: '份速查表',
        must: '份必備',
        high: '份高價值',
        drill: '練習題已 ok'
      },
      views: { path: '複習路徑', priority: '依優先度', topic: '依主題' },
      viewsAria: '選擇瀏覽速查表的方式',
      tierCount: n => `${n} 份`,
      jumpTo: '跳到',
      pathHeading: '複習路徑',
      pathBlurb: (n, total) => `${n} 份核心速查表，依學習順序排列 —— 先是 ${total} 份基礎，接著是其餘必備，` +
        '再來是高價值。重讀完一份的標星模板後就標記為已複習；一週後（高價值為兩週）會再次到期。',
      stages: {
        start: ['基礎', '閱讀階梯：每一份都是下一份的前提。'],
        must: ['必備', '幾乎每一輪面試都會出現的其餘內容。'],
        high: ['高價值', '常見到這裡有缺口就會掉關。']
      },
      nextUp: '下一份',
      open: '開啟',
      mark: '標記已複習',
      unmark: '復原',
      reset: '重設進度',
      drill: (ok, total) => `練習 ${ok}/${total} ok`,
      drillNext: '下一題',
      drillLink: '練習路徑',
      noDrill: '尚無練習路徑',
      examples: '例題',
      rel: {
        never: '尚未複習',
        today: '今天已複習',
        daysAgo: '{n} 天前複習',
        due: '該複習了',
        dueIn: '{n} 天後到期',
        allDone: '路徑上的速查表都已複習，目前沒有到期的。挑任何一份深入讀吧。',
        progress: '已複習 {n} / {total}',
        storage: '進度只存在這個瀏覽器裡。'
      },
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
        '再直接跳到標星的章節 —— 每份速查表內部值得背的章節也標了同一套星等。練習數字來自練習紀錄。',
      source: '原始檔：<a href="https://github.com/yennanliu/CS_basics/tree/master/i18n/zh">GitHub 上的 i18n/zh</a> — ' +
        '星等與分組定義在 <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>。'
    }
  };

  // ── Drill summary ───────────────────────────────────────────────────────────
  // A rendered sheet already carries one stampDrillVerdicts badge per LC number
  // in its Drill Path. Read them back — in order, once each — so the index can
  // say how much of a sheet's drill is solid and which problem is next, without
  // a second parse of the markdown or the log. `anchor` is the id of the heading
  // the first badge sits under, which is the Drill Path heading in either
  // language (the 中文 page's ids come from its translated text).
  function drillSummary(html) {
    const re = /data-lc="(\d+)" data-verdict="(\w+)"/g;
    const seen = new Map();
    let first = -1;
    let m;
    while ((m = re.exec(html || ''))) {
      if (first < 0) first = m.index;
      if (!seen.has(m[1])) seen.set(m[1], m[2]);
    }
    if (!seen.size) return null;
    const ids = [...seen.keys()];
    const count = v => ids.filter(id => seen.get(id) === v).length;
    let anchor = null;
    const heads = /<h[2-4]\b[^>]*\bid="([^"]*)"/g;
    while ((m = heads.exec(html)) && m.index < first) anchor = m[1];
    return {
      total: ids.length,
      ok: count('ok'),
      again: count('again'),
      // The Drill Path runs Core → Practice → Stretch, easy → hard, so the first
      // problems not yet ok are the ones to do next — `again` ones before unseen.
      next: ids.filter(id => seen.get(id) === 'again')
        .concat(ids.filter(id => seen.get(id) === 'none'))
        .slice(0, 3),
      anchor
    };
  }

  // The review path: the curated ladder from meta.startHere, then every other
  // tier-5 sheet, then every tier-4 one, in category order. Companion sheets
  // (`_examples`, `_advanced`), stubs and imported references are not steps —
  // an examples sheet rides along on its core sheet's step instead.
  const REVIEW_INTERVAL_DAYS = { 5: 7, 4: 14 };

  function reviewPath(sheets, meta) {
    const byFile = new Map(sheets.map(s => [s.file, s]));
    const isCore = s => s && (!s.kind || s.kind === 'sheet') && !/_(examples|advanced)$/.test(s.file);
    const taken = new Set();
    const stage = (key, list) => {
      const steps = list.filter(s => isCore(s) && !taken.has(s.file));
      steps.forEach(s => taken.add(s.file));
      return { key, steps };
    };
    const ordered = tier => meta.categoryOrder.flatMap(cat =>
      sheets.filter(s => s.category === cat && s.tier === tier)
        .sort((a, b) => a.title.localeCompare(b.title)));
    return [
      stage('start', (meta.startHere || []).map(s => byFile.get(s.file)).filter(Boolean)),
      stage('must', ordered(5)),
      stage('high', ordered(4))
    ].filter(st => st.steps.length);
  }

  // Sheets in a group, most important first, then alphabetical — the order every
  // list on the index uses, so a sheet never moves between views for no reason.
  const byWeight = (a, b) => (b.tier - a.tier) || a.title.localeCompare(b.title);

  // The priority view's order inside one tier: the start-here ladder first (it is
  // already in teaching order), then by category in categoryOrder, then title —
  // so "Must know" opens with Array and Hash Map, not whatever sorts first.
  function byImportance(meta) {
    const ladder = new Map((meta.startHere || []).map((s, i) => [s.file, i]));
    const cat = new Map((meta.categoryOrder || []).map((c, i) => [c, i]));
    const rank = (m, k) => (m.has(k) ? m.get(k) : Infinity);
    return (a, b) => (b.tier - a.tier) ||
      (rank(ladder, a.file) - rank(ladder, b.file) || 0) ||
      (rank(cat, a.category) - rank(cat, b.category) || 0) ||
      a.title.localeCompare(b.title);
  }

  const escapeAttr = s => String(s).replace(/&/g, '&amp;').replace(/"/g, '&quot;');

  // The cheatsheet index. Three views over the same sheets, chosen by a tab bar:
  //
  //   Review path — the core sheets in study order, each with its drill progress
  //                 and a per-browser "reviewed" mark that comes due again;
  //   By priority — one section per tier, must-know first, shown as feature cards;
  //   By topic    — the category catalogue, each category ordered by tier.
  //
  // Everything is rendered server-side and ships visible, so with JS off the page
  // is the three views stacked; site.js turns them into tabs and wires the filter
  // and the review marks. `lang` picks both the chrome above and the `zh`
  // overrides in meta, and decides whether a card points at `<slug>.html` or
  // `<slug>.zh.html`. Categories stay keyed by their English name everywhere —
  // grouping, anchors, data-category — so the two indexes stay row-for-row
  // comparable and a deep link works in either.
  function buildCheatsheetIndex(sheets, meta, lang = 'en') {
    const t = INDEX_TEXT[lang] || INDEX_TEXT.en;
    const zh = lang === 'zh' ? (meta.zh || {}) : {};
    const pick = (map, key, fallback) => (map && map[key] != null ? map[key] : fallback);

    const fileSet = new Set(sheets.map(s => s.file));
    const tierLabel = n => pick(zh.tierLabels, String(n), meta.tierLabels[String(n)]).label;
    const tierNote = n => pick(zh.tierLabels, String(n), meta.tierLabels[String(n)]).note;
    const catName = c => pick(zh.categories, c, c);
    const catBlurb = c => pick(zh.categoryBlurbs, c, meta.categoryBlurbs[c]);
    const href = file => `cheatsheets/${file}${lang === 'zh' ? '.zh' : ''}.html`;
    const startWhy = new Map((meta.startHere || [])
      .map(s => [s.file, pick(zh.startHere, s.file, s.why)]));

    const drillBar = d => {
      if (!d) return '';
      const pct = n => (100 * n / d.total).toFixed(1);
      return `<span class="drill-bar" aria-hidden="true">` +
        `<span class="drill-ok" style="width:${pct(d.ok)}%"></span>` +
        `<span class="drill-again" style="width:${pct(d.again)}%"></span></span>`;
    };
    const drillLine = (d, file) => {
      if (!d) return '';
      const link = d.anchor ? `${href(file)}#${d.anchor}` : href(file);
      return `<p class="card-drill">${drillBar(d)}` +
        `<span class="drill-text">${t.drill(d.ok, d.total)}` +
        (d.again ? ` · <span class="drill-again-n">${d.again} again</span>` : '') +
        (d.next.length ? ` · ${t.drillNext}: <a href="${link}">${d.next.map(n => 'LC ' + n).join(', ')}</a>` : '') +
        '</span></p>';
    };
    const kindChip = item => item.kind === 'stub'
      ? `<span class="kind-chip kind-stub">${t.stub}</span>`
      : item.kind === 'reference'
        ? `<span class="kind-chip kind-reference">${t.reference}</span>`
        : '';

    // One card, used by both catalogue views. data-search is what the filter
    // matches on: title, category and the Scope line, so "window" finds Sliding
    // Window and "dag" finds toposort. The English category and slug ride along
    // in both languages, so a reader who only knows the English term can still
    // find the 中文 card. data-file lets the filter count a sheet once although it
    // appears in two views.
    const card = (item, { showCategory }) => {
      const haystack = [item.title, item.category, catName(item.category), item.description || '',
        item.file.replace(/_/g, ' ')].join(' ').toLowerCase().replace(/"/g, '');
      const chip = kindChip(item);
      return `\n        <article class="cheatsheet-card sheet-card tier-${item.tier}"` +
        ` data-tier="${item.tier}" data-file="${item.file}" data-search="${haystack}">` +
        '<div class="card-top">' +
        `<h4 class="card-title"><a href="${href(item.file)}">${item.title}</a></h4>` +
        `${prioBadge(item.tier, 'prio-compact', lang)}</div>` +
        (showCategory ? `<p class="card-cat">${catName(item.category)}</p>` : '') +
        (item.description ? `<p class="card-desc">${item.description}</p>` : '') +
        (item.tier >= 4 ? drillLine(item.drill, item.file) : '') +
        (chip ? `<p class="card-tags">${chip}</p>` : '') +
        '</article>';
    };

    // ── Header: title, intro, stat strip ──
    const count = tier => sheets.filter(s => s.tier === tier).length;
    const drills = sheets.map(s => s.drill).filter(Boolean);
    const drillOk = drills.reduce((n, d) => n + d.ok, 0);
    const drillTotal = drills.reduce((n, d) => n + d.total, 0);
    let html = `<h1>${t.h1}</h1><p class="intro">${t.intro(sheets.length)}</p>`;
    html += '<ul class="cs-stats">' +
      `<li><strong>${sheets.length}</strong> ${t.stats.sheets}</li>` +
      `<li><strong>${count(5)}</strong> ${t.stats.must}</li>` +
      `<li><strong>${count(4)}</strong> ${t.stats.high}</li>` +
      (drillTotal ? `<li><strong>${drillOk}/${drillTotal}</strong> ${t.stats.drill}</li>` : '') +
      '</ul>';

    // ── View tabs ──
    const VIEWS = [['path', 'review-path'], ['priority', 'by-priority'], ['topic', 'by-topic']];
    // Plain links here; site.js adds the tablist / tab / tabpanel roles once it
    // has actually turned the stacked views into tabs.
    html += `<nav class="cs-tabs" aria-label="${t.viewsAria}" data-cs-tabs>` +
      VIEWS.map(([key, id], i) =>
        `<a class="cs-tab${i === 1 ? ' is-on' : ''}" href="#${id}" data-view="${key}">${t.views[key]}</a>`
      ).join('') + '</nav>';

    // ── View 1: review path ──
    const stages = reviewPath(sheets, meta);
    const stepTotal = stages.reduce((n, st) => n + st.steps.length, 0);
    const startCount = (stages.find(st => st.key === 'start') || { steps: [] }).steps.length;
    const strings = JSON.stringify(Object.assign({}, t.rel, {
      mark: t.mark, unmark: t.unmark, nextUp: t.nextUp, open: t.open
    }));
    html += `<section class="cs-view review-path" id="review-path" data-view-panel="path" data-review-path ` +
      `data-strings="${escapeAttr(strings)}" data-intervals="${escapeAttr(JSON.stringify(REVIEW_INTERVAL_DAYS))}">` +
      `<h2 class="view-heading">${t.pathHeading}</h2>` +
      `<p class="cat-blurb">${t.pathBlurb(stepTotal, startCount)}</p>` +
      // Filled by site.js; without JS the path is simply an ordered reading list.
      '<div class="path-summary" hidden>' +
      '<div class="path-next"></div>' +
      `<div class="path-progress"><span class="path-meter"><span class="path-meter-fill"></span></span>` +
      `<span class="path-count"></span>` +
      `<button type="button" class="path-reset">${t.reset}</button></div>` +
      `<p class="path-note">${t.rel.storage}</p>` +
      '</div>';
    let stepNo = 0;
    for (const st of stages) {
      const [name, blurb] = t.stages[st.key];
      html += `<div class="path-stage" data-stage="${st.key}">` +
        `<h3 class="stage-heading" id="path-${st.key}">${name}` +
        `<span class="cat-count">${t.tierCount(st.steps.length)}</span></h3>` +
        `<p class="stage-blurb">${blurb}</p><ol class="path-list" start="${stepNo + 1}">`;
      for (const s of st.steps) {
        stepNo++;
        const why = startWhy.get(s.file) || s.description || '';
        const ex = `${s.file}_examples`;
        html += `<li class="path-step tier-${s.tier}" data-file="${s.file}" data-tier="${s.tier}">` +
          `<span class="step-no" aria-hidden="true">${stepNo}</span>` +
          '<div class="step-body"><div class="step-top">' +
          `<a class="step-title" href="${href(s.file)}">${s.title}</a>` +
          `${prioBadge(s.tier, 'prio-compact', lang)}` +
          (fileSet.has(ex) ? `<a class="step-extra" href="${href(ex)}">+ ${t.examples}</a>` : '') +
          '</div>' +
          (why ? `<p class="step-why">${why}</p>` : '') +
          (s.drill ? drillLine(s.drill, s.file) : `<p class="card-drill drill-none">${t.noDrill}</p>`) +
          '</div>' +
          '<div class="step-mark"><span class="step-status"></span></div>' +
          '</li>';
      }
      html += '</ol></div>';
    }
    html += '</section>';

    // ── Filter (applies to both catalogue views) ──
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

    // ── View 2: by priority ──
    html += `<section class="cs-view" id="by-priority" data-view-panel="priority">` +
      `<h2 class="view-heading">${t.views.priority}</h2>`;
    for (const tier of [5, 4, 3, 2]) {
      const items = sheets.filter(s => s.tier === tier).sort(byImportance(meta));
      if (!items.length) continue;
      html += `<section class="cat-section tier-section tier-section-${tier}" data-tier-section="${tier}">` +
        `<h3 class="cat-heading" id="tier-${tier}">${prioBadge(tier, '', lang)}` +
        `<span class="tier-key-label">${tierLabel(tier)}</span>` +
        `<span class="cat-count">${t.sheetCount(items.length)}</span></h3>` +
        `<p class="cat-blurb tier-key-note">${tierNote(tier)}</p>` +
        `<div class="cheatsheet-grid sheet-grid${tier === 5 ? ' feature-grid' : ''}">` +
        items.map(item => card(item, { showCategory: true })).join('') +
        '</div></section>';
    }
    html += '</section>';

    // ── View 3: by topic ──
    const grouped = groupByCategory(sheets);
    const cats = meta.categoryOrder.filter(c => grouped[c] && grouped[c].length);
    html += `<section class="cs-view" id="by-topic" data-view-panel="topic">` +
      `<h2 class="view-heading">${t.views.topic}</h2>` +
      `<nav class="cat-jump" aria-label="${t.jumpTo}">` +
      cats.map(c => `<a href="#${slugify(c)}">${catName(c)}</a>`).join('') + '</nav>';
    for (const category of cats) {
      const items = grouped[category].slice().sort(byWeight);
      html += `<section class="cat-section" data-category="${category}">`;
      html += `<h3 class="cat-heading" id="${slugify(category)}">${catName(category)}` +
        `<span class="cat-count">${t.sheetCount(items.length)}</span></h3>`;
      if (catBlurb(category)) html += `<p class="cat-blurb">${catBlurb(category)}</p>`;
      html += '<div class="cheatsheet-grid sheet-grid">' +
        items.map(item => card(item, { showCategory: false })).join('') +
        '</div></section>';
    }
    html += '</section>';

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
    meta = '', legend = '', titleId = null, labels = {}, contentClass = ''
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
            <div class="cheatsheet-content${contentClass ? ` ${contentClass}` : ''}">
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

  /** 'YYYYMMDD' (the practice log's date shape) -> 'YYYY-MM-DD'; anything else unchanged. */
  function isoDate(compact) {
    return typeof compact === 'string' && /^\d{8}$/.test(compact)
      ? compact.slice(0, 4) + '-' + compact.slice(4, 6) + '-' + compact.slice(6, 8)
      : compact;
  }

  // ── Drill Path verdicts ──────────────────────────────────────────────────────
  // A sheet's `## Drill Path` (Skeleton A: `3) Drill Path`) lists LC numbers; the
  // practice log already says which of them are solid. Stamp each `LC <n>` in
  // that section with the log's latest verdict — the same reading the roadmap
  // renders (build-roadmap.js readLogVerdicts) — so a sheet shows what is left
  // to drill without anyone typing it. Only the Drill Path section is touched,
  // and only text between tags, so links and attributes are never rewritten.
  const VERDICT_TEXT = {
    en: { ok: 'ok', again: 'again', none: 'no verdict',
          title: (v, d) => `practice log: ${v}${d ? ' on ' + d : ''}` },
    zh: { ok: 'ok', again: 'again', none: '未評定',
          title: (v, d) => `練習紀錄：${v}${d ? '（' + d + '）' : ''}` }
  };

  function verdictBadge(id, verdicts, lang) {
    const t = VERDICT_TEXT[lang] || VERDICT_TEXT.en;
    const v = verdicts && verdicts.get(String(id));
    const status = v ? v.status : 'none';
    const label = t[status];
    const title = v ? t.title(v.status, isoDate(v.date)) : t.title(t.none, '');
    return `<span class="lc-verdict lc-verdict-${status}" data-lc="${id}" data-verdict="${status}" title="${title}">${label}</span>`;
  }

  // A Drill Path heading: its id on an English page, its text on a 中文 one,
  // where ids are retargeted from the translated text.
  const DRILL_HEADING = /(^|-)drill-path(-|$)/;
  const DRILL_TEXT = /drill path|練習路徑|練習路線/i;

  function stampDrillVerdicts(html, verdicts, lang = 'en') {
    const open = /<h([1-6])\b[^>]*>/g;
    let m;
    while ((m = open.exec(html))) {
      const level = Number(m[1]);
      const close = html.indexOf(`</h${level}>`, m.index);
      if (close < 0) break;
      const id = (m[0].match(/\bid="([^"]*)"/) || [])[1] || '';
      const text = html.slice(m.index + m[0].length, close).replace(/<[^>]*>/g, '');
      if (!DRILL_HEADING.test(id) && !DRILL_TEXT.test(text)) continue;
      const start = close + `</h${level}>`.length;
      const next = new RegExp(`<h[1-${level}]\\b`, 'g');
      next.lastIndex = start;
      const n = next.exec(html);
      const end = n ? n.index : html.length;
      let inHeading = false;
      const body = html.slice(start, end).split(/(<[^>]+>)/).map(part => {
        if (part.startsWith('<')) {
          if (/^<h[1-6]\b/.test(part)) inHeading = true;
          else if (/^<\/h[1-6]>/.test(part)) inHeading = false;
          return part;
        }
        return inHeading ? part
          : part.replace(/\bLC\s?(\d{1,4})\b/g, (all, lc) => `${all} ${verdictBadge(lc, verdicts, lang)}`);
      }).join('');
      html = html.slice(0, start) + body + html.slice(end);
      open.lastIndex = start + body.length;
    }
    return html;
  }


  // ── Reading layout (pilot) ───────────────────────────────────────────────
  // A pattern card's slots are written as a bold lead-in: `**Traps**` on a line
  // of its own, or `**Drill** — LC 1 · LC 2` opening a paragraph. Rendered, that
  // lead-in was bold body text at body size, so the label and what it labels
  // read as one run. markCardSlots tags the paragraph and its label so the
  // stylesheet can set the label apart; the slot kind is looked up in both
  // languages, and a lead-in that is not one of the seven slots is still tagged
  // as a plain label.
  //
  // READING_V2 lists the sheets that get this layout. It is a pilot list, not a
  // feature flag to keep: once the layout is agreed, every sheet gets it and
  // this set goes away.
  const READING_V2 = new Set(['array']);

  const SLOT_KINDS = {
    recognise: ['recognise', 'recognize', '辨識'],
    key: ['key point', '關鍵', '重點'],
    complexity: ['complexity', '複雜度'],
    dryrun: ['dry run', '試跑', '手動追蹤'],
    traps: ['traps', '陷阱'],
    notthis: ['not this if', '不適用時'],
    drill: ['drill', '練習']
  };
  const SLOT_BY_LABEL = new Map();
  for (const kind of Object.keys(SLOT_KINDS)) {
    for (const label of SLOT_KINDS[kind]) SLOT_BY_LABEL.set(label, kind);
  }
  // A label, not a bolded sentence: short, plain text, no closing punctuation.
  const SLOT_LABEL_MAX = 28;

  function markCardSlots(html) {
    return html.replace(
      /<p><strong>([^<]{1,80})<\/strong>(\s*(?:—|–)\s*|\s*(?=<\/p>))/g,
      (all, label, tail) => {
        const text = label.trim();
        if (text.length > SLOT_LABEL_MAX || /[.。:：!！?？]$/.test(text)) return all;
        const kind = SLOT_BY_LABEL.get(text.toLowerCase()) || 'note';
        // `tail` is the dash that joined label and text; the label is now set
        // apart by its own box, so the dash would be a second separator.
        const inline = /\S/.test(tail);
        return `<p class="slot slot-${kind}${inline ? '' : ' slot-block'}">` +
          `<strong class="slot-label">${text}</strong>${inline ? ' ' : ''}`;
      }
    );
  }

  return {
    markCardSlots,
    READING_V2,
    stampDrillVerdicts,
    verdictBadge,
    slugify,
    isoDate,
    TIER_LABELS,
    prioBadge,
    PRIO_BADGE_RE,
    headingText,
    annotatePriorityHeadings,
    PRIORITY_LEGEND,
    PRIO_TEXT,
    priorityLegend,
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
    drillSummary,
    reviewPath,
    REVIEW_INTERVAL_DAYS,
    splitLeadingH1,
    buildPageContent,
    extractScope,
    titleCaseFromFile,
    summariseDoc,
  };
});
