/* ─────────────────────────────────────────────────────────────────────────
   lc-big-o.js — the Big-O comparison, shared by lc-big-o.html (English) and
   lc-big-o.zh.html (繁體中文).

   The two pages are thin shells: each holds its own static prose, and both
   load this one script, so the chart, the ops table and the compare tool can
   never drift apart between languages. Every string the script writes comes
   out of STRINGS below, picked by <html lang>; site/test/big-o-page.test.js
   boots both shells and fails on a key one language is missing.
   ───────────────────────────────────────────────────────────────────────── */
(function () {
  'use strict';

  var STRINGS = {
    en: {
      names: { c1: 'constant', logn: 'logarithmic', sqrtn: 'square root', n: 'linear', nlogn: 'linearithmic',
               n2: 'quadratic', n3: 'cubic', exp: 'exponential', fact: 'factorial' },
      shapes: {
        c1: 'Index into an array, a hash-map get/put, push / pop a stack.',
        logn: 'Halve the search space each step: binary search, balanced BST, heap push / pop.',
        sqrtn: 'Loop while i·i ≤ n: trial-division primality, enumerating divisors.',
        n: 'One pass: hash map, two pointers, sliding window, prefix sums, BFS / DFS (V + E).',
        nlogn: 'Sort, then scan. A heap fed n items. Merge sort. n binary searches.',
        n2: 'Two nested loops over the input; a DP table indexed by two positions.',
        n3: 'Three nested loops: interval DP with a split point, Floyd–Warshall.',
        exp: 'Every subset: include / exclude recursion, naive two-branch recursion.',
        fact: 'Every ordering: permutations, brute-force travelling salesman.'
      },
      lcs: {
        c1: 'LC 155 Min Stack · LC 380 Insert Delete GetRandom O(1)',
        logn: 'LC 704 Binary Search · LC 33 Search in Rotated Sorted Array · LC 153',
        sqrtn: 'Divisor / prime checks · LC 279 Perfect Squares runs O(n√n)',
        n: 'LC 1 Two Sum · LC 3 Longest Substring · LC 121 · LC 200 Number of Islands',
        nlogn: 'LC 56 Merge Intervals · LC 215 Kth Largest · LC 300 LIS (patience)',
        n2: 'LC 15 3Sum · LC 5 Longest Palindromic Substring · LC 1143 LCS',
        n3: 'LC 312 Burst Balloons · LC 1039 · Floyd–Warshall',
        exp: 'LC 78 Subsets · LC 90 Subsets II · naive LC 509 Fibonacci',
        fact: 'LC 46 Permutations · LC 51 N-Queens (bounded by n!)'
      },
      yours: 'yours',
      yoursLabel: 'Yours',
      emptyHint: 'Type a complexity in the box — e.g. n log n — then press Plot, or pick one of the examples.',
      decimal: 'Decimals are not supported — write n^1.5 as n sqrt n.',
      unreadable: function (t) { return 'Could not read "' + t + '". Use n, log, sqrt, ^, ! and * — e.g. n log^2 n.'; },
      dupe: function (l) { return 'That is ' + l + ', already on the chart.'; },
      tooMany: function (k) { return 'Up to ' + k + ' custom curves — remove one first.'; },
      plotted: function (l) { return 'Plotted ' + l + ' — dashed, so it stands apart from the nine classes.'; },
      remove: function (l) { return 'Remove ' + l; },
      axisN: 'n (input size)',
      axisOps: 'operations',
      axisOpsLog: 'operations (log scale)',
      captionStart: function (n) { return 'Operations against n from 1 to ' + n + ', '; },
      captionLog: 'on a log scale, where each gridline is 10× the one below — the only view that keeps 2ⁿ and n! on the page. ',
      captionLinear: 'on a linear scale sized so O(n log n) just reaches the top; anything steeper leaves the chart. ',
      captionEnd: 'Hover for the exact counts at any n. Logs are base 2.',
      captionNone: 'Every curve is hidden — click a class above to show it.',
      scales: { linear: 'linear', log: 'log' },
      thClass: 'Class', thName: 'Name', thShape: 'Code shape', thLc: 'Typical problems',
      thMaxN: 'largest n in ~1 s',
      any: 'any',
      opsEmpty: 'Show a class above to fill the table.',
      cmpUnreadable: function (t) {
        return 'Could not read ' + t + '. Use n, log, sqrt, ^, ! and * — e.g. <code>n log n</code>, <code>2^n</code>.';
      },
      empty: '(empty)',
      at6: function (la, va, lb, vb, f) {
        return 'At n = 10⁶: ' + la + ' ≈ <b>' + va + '</b> ops, ' + lb + ' ≈ <b>' + vb + '</b> ops — a factor of <b>' + f + '</b>.';
      },
      sameExact: function (a, b) { return 'Same class: ' + a + ' = ' + b; },
      sameExactWhy: 'They differ only by a constant factor, which Big-O drops. Pick whichever is clearer to say out loud.',
      sameNum: function (a, b) { return 'Same class, as far as can be measured: ' + a + ' ≈ ' + b; },
      sameNumRatio: function (r, n) {
        return 'Their ratio settles at about <b>' + r + '</b> by n = ' + n
          + ' — a constant factor, which Big-O drops (e.g. <code>log n</code> vs <code>log(n²)</code>).';
      },
      sameNumCaveat: 'This is a numerical check: a factor that grows as slowly as <code>log log n</code> or slower can hide from it, '
        + 'so argue it algebraically before relying on it.',
      unknown: 'Cannot tell these apart numerically',
      unknownWhy: 'Their difference is below what double-precision arithmetic can resolve at the input sizes it can evaluate. '
        + 'Compare them by structure instead: cancel the common factors and see what is left.',
      stillBehind: function (big, n) {
        return 'But only eventually — ' + big + ' is still the cheaper of the two at n = ' + n
          + ', so the crossover is <b>beyond ' + n + '</b>. For any real input the "worse" class wins here: constants matter.';
      },
      crossAt: function (big, approx, n) {
        return 'But not from the start — ' + big + ' only stays ahead once n passes <b>' + (approx ? 'about ' : '') + n + '</b>. '
          + 'Before that the "worse" class is cheaper, which is why constants still matter for small inputs.';
      },
      always: function (big) { return big + ' costs at least as much at every n ≥ 1 — there is no input size where it is cheaper.'; },
      slower: function (s, b) { return s + ' grows slower than ' + b; },
      better: function (s, b) { return 'So <b>' + s + '</b> is the better bound: ' + s + ' ⊂ ' + b + '.'; }
    },
    zh: {
      names: { c1: '常數', logn: '對數', sqrtn: '平方根', n: '線性', nlogn: '線性對數',
               n2: '平方', n3: '立方', exp: '指數', fact: '階乘' },
      shapes: {
        c1: '陣列索引、hash map 的 get / put、stack 的 push / pop。',
        logn: '每一步把搜尋範圍砍半：binary search、平衡 BST、heap 的 push / pop。',
        sqrtn: '迴圈跑到 i·i ≤ n 為止：試除法判斷質數、列舉因數。',
        n: '掃一遍：hash map、two pointers、sliding window、prefix sum、BFS / DFS（V + E）。',
        nlogn: '先排序再掃描。把 n 個元素丟進 heap。Merge sort。做 n 次 binary search。',
        n2: '對輸入跑兩層巢狀迴圈；用兩個位置當索引的 DP 表。',
        n3: '三層巢狀迴圈：帶切分點的區間 DP、Floyd–Warshall。',
        exp: '列舉每個子集：選 / 不選的遞迴、兩個分支的暴力遞迴。',
        fact: '列舉每種排列：permutation、暴力解旅行推銷員問題。'
      },
      lcs: {
        c1: 'LC 155 Min Stack · LC 380 Insert Delete GetRandom O(1)',
        logn: 'LC 704 Binary Search · LC 33 Search in Rotated Sorted Array · LC 153',
        sqrtn: '因數 / 質數判斷 · LC 279 Perfect Squares 是 O(n√n)',
        n: 'LC 1 Two Sum · LC 3 Longest Substring · LC 121 · LC 200 Number of Islands',
        nlogn: 'LC 56 Merge Intervals · LC 215 Kth Largest · LC 300 LIS（patience）',
        n2: 'LC 15 3Sum · LC 5 Longest Palindromic Substring · LC 1143 LCS',
        n3: 'LC 312 Burst Balloons · LC 1039 · Floyd–Warshall',
        exp: 'LC 78 Subsets · LC 90 Subsets II · 暴力版 LC 509 Fibonacci',
        fact: 'LC 46 Permutations · LC 51 N-Queens（上界為 n!）'
      },
      yours: '自訂',
      yoursLabel: '自訂曲線',
      emptyHint: '在輸入框打一個複雜度（例如 n log n）再按「繪製」，或點下方的範例。',
      decimal: '不支援小數 —— 請把 n^1.5 寫成 n sqrt n。',
      unreadable: function (t) { return '看不懂「' + t + '」。可用 n、log、sqrt、^、! 和 * —— 例如 n log^2 n。'; },
      dupe: function (l) { return l + ' 已經在圖上了。'; },
      tooMany: function (k) { return '最多 ' + k + ' 條自訂曲線 —— 請先移除一條。'; },
      plotted: function (l) { return '已繪製 ' + l + ' —— 以虛線顯示，和九個基本類別區分開來。'; },
      remove: function (l) { return '移除 ' + l; },
      axisN: 'n（輸入大小）',
      axisOps: '運算次數',
      axisOpsLog: '運算次數（對數刻度）',
      captionStart: function (n) { return 'n 從 1 到 ' + n + ' 的運算次數，'; },
      captionLog: '使用對數刻度，每條格線是下一條的 10 倍 —— 只有這個視角能讓 2ⁿ 和 n! 留在圖上。',
      captionLinear: '使用線性刻度，O(n log n) 剛好碰到頂端；比它陡的都會衝出圖外。',
      captionEnd: '滑鼠移到圖上可看任一 n 的精確次數。log 以 2 為底。',
      captionNone: '所有曲線都隱藏了 —— 點上方任一類別即可顯示。',
      scales: { linear: '線性', log: '對數' },
      thClass: '類別', thName: '名稱', thShape: '程式碼樣貌', thLc: '典型題目',
      thMaxN: '~1 秒內最大的 n',
      any: '不限',
      opsEmpty: '在上方顯示任一類別即可填入此表。',
      cmpUnreadable: function (t) {
        return '看不懂 ' + t + '。可用 n、log、sqrt、^、! 和 * —— 例如 <code>n log n</code>、<code>2^n</code>。';
      },
      empty: '（空白）',
      at6: function (la, va, lb, vb, f) {
        return '在 n = 10⁶ 時：' + la + ' ≈ <b>' + va + '</b> 次，' + lb + ' ≈ <b>' + vb + '</b> 次 —— 相差 <b>' + f + '</b> 倍。';
      },
      sameExact: function (a, b) { return '同一類別：' + a + ' = ' + b; },
      sameExactWhy: '兩者只差一個常數倍，Big-O 會把它丟掉。挑口頭上比較好講的那個就好。',
      sameNum: function (a, b) { return '就可測量的範圍而言是同一類別：' + a + ' ≈ ' + b; },
      sameNumRatio: function (r, n) {
        return '到 n = ' + n + ' 時，兩者的比值穩定在約 <b>' + r + '</b> '
          + '—— 是常數倍，Big-O 會丟掉（例如 <code>log n</code> 對 <code>log(n²)</code>）。';
      },
      sameNumCaveat: '這是數值檢查：成長得像 <code>log log n</code> 一樣慢（或更慢）的因子可能躲過它，'
        + '下結論前請先用代數論證。',
      unknown: '用數值無法分辨這兩者',
      unknownWhy: '在可計算的輸入大小下，兩者的差距小於雙精度浮點數能分辨的程度。'
        + '請改從結構比較：消掉共同因子，看剩下什麼。',
      stillBehind: function (big, n) {
        return '但只是「最終」—— 到 n = ' + n + ' 時 ' + big + ' 仍是兩者中比較便宜的，'
          + '所以交叉點<b>在 ' + n + ' 之後</b>。對任何實際的輸入，這裡「比較差」的類別反而贏：常數很重要。';
      },
      crossAt: function (big, approx, n) {
        return '但不是一開始就如此 —— ' + big + ' 要等 n 超過<b>' + (approx ? '約 ' : '') + n + '</b>才一直比較大。'
          + '在那之前「比較差」的類別反而比較便宜，這就是小輸入時常數仍然重要的原因。';
      },
      always: function (big) { return big + ' 在每個 n ≥ 1 都至少一樣貴 —— 不存在它比較便宜的輸入大小。'; },
      slower: function (s, b) { return s + ' 成長得比 ' + b + ' 慢'; },
      better: function (s, b) { return '所以 <b>' + s + '</b> 是更好的上界：' + s + ' ⊂ ' + b + '。'; }
    }
  };
  var LANG = /^zh/i.test(document.documentElement.lang || '') ? 'zh' : 'en';
  var S = STRINGS[LANG];

  // ── The nine classes, slowest to fastest growing ───────────────────────
  //
  // `slot` is the colour token. O(1) takes the neutral --s0: it is the
  // baseline, not a contender, and the eight categorical slots are then
  // enough for the rest without a ninth generated hue. Name, code shape and
  // example problems are per language, from STRINGS.
  var PRESETS = [
    { id: 'c1',     expr: '1',       label: 'O(1)',       slot: 0 },
    { id: 'logn',   expr: 'log n',   label: 'O(log n)',   slot: 1 },
    { id: 'sqrtn',  expr: 'sqrt n',  label: 'O(√n)',      slot: 2 },
    { id: 'n',      expr: 'n',       label: 'O(n)',       slot: 3 },
    { id: 'nlogn',  expr: 'n log n', label: 'O(n log n)', slot: 4 },
    { id: 'n2',     expr: 'n^2',     label: 'O(n²)',      slot: 5 },
    { id: 'n3',     expr: 'n^3',     label: 'O(n³)',      slot: 6 },
    { id: 'exp',    expr: '2^n',     label: 'O(2ⁿ)',      slot: 7 },
    { id: 'fact',   expr: 'n!',      label: 'O(n!)',      slot: 8 }
  ];
  PRESETS.forEach(function (p) {
    p.name = S.names[p.id];
    p.shape = S.shapes[p.id];
    p.lc = S.lcs[p.id];
  });

  var MAX_CUSTOM = 3;
  var DASHES = ['7 4', '2 3', '10 3 2 3'];
  var RANGES = [10, 20, 50, 100, 1000, 1e6];
  var TABLE_NS = [10, 100, 1e3, 1e4, 1e5, 1e6];
  var STORE_KEY = 'big-o-chart';

  var state = { hidden: {}, custom: [], nMax: 20, scale: 'linear', budgetExp: 8 };

  function el(id) { return document.getElementById(id); }
  function esc(v) {
    return String(v).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }

  // ── Persistence (a convenience only; the page works without it) ────────
  function load() {
    try {
      var saved = JSON.parse(localStorage.getItem(STORE_KEY) || 'null');
      if (!saved) return;
      if (saved.hidden) state.hidden = saved.hidden;
      if (RANGES.indexOf(saved.nMax) >= 0) state.nMax = saved.nMax;
      if (saved.scale === 'log' || saved.scale === 'linear') state.scale = saved.scale;
      if ([7, 8, 9].indexOf(saved.budgetExp) >= 0) state.budgetExp = saved.budgetExp;
      (saved.custom || []).slice(0, MAX_CUSTOM).forEach(function (raw) { addCustom(raw, true); });
    } catch (e) { /* storage blocked or corrupt: start fresh */ }
  }
  function save() {
    try {
      localStorage.setItem(STORE_KEY, JSON.stringify({
        hidden: state.hidden, nMax: state.nMax, scale: state.scale, budgetExp: state.budgetExp,
        custom: state.custom.map(function (c) { return c.raw; })
      }));
    } catch (e) { /* ignore */ }
  }

  // ── Curves ─────────────────────────────────────────────────────────────

  PRESETS.forEach(function (p) {
    p.ast = CSComplexity.compile(p.expr).ast;
    p.key = CSComplexity.compile(p.expr).key;
    p.stroke = 'var(--s' + p.slot + ')';
  });

  function allCurves() { return PRESETS.concat(state.custom); }
  function visibleCurves() {
    return allCurves().filter(function (c) { return !state.hidden[c.id]; });
  }

  // log2 of the operation count; see CSComplexity.log2Eval.
  function L(curve, n) { return CSComplexity.log2Eval(curve.ast, n); }

  // A typed expression, checked. Returns null on success, or { text, level }
  // where level is 'err' for a real problem and 'info' for a nudge that is not
  // the user's mistake (an empty box, a curve that is already plotted).
  function addCustom(raw, quiet) {
    var text = String(raw || '').trim();
    // An empty box is not an error — the Plot button simply has nothing to
    // plot yet. It used to answer in red ("Type an expression first."), right
    // under a row of curves that had just been plotted, which read as if the
    // plot had failed.
    if (!text) return { text: S.emptyHint, level: 'info' };
    // The shared preprocessor treats "." as a separator (so "O(n). " grades),
    // which would read n^1.5 as n^15. Say so instead of plotting a wrong curve.
    if (/\d\.\d/.test(text)) return { text: S.decimal, level: 'err' };
    var compiled = CSComplexity.compile(text);
    if (!compiled) return { text: S.unreadable(text), level: 'err' };
    var dupe = allCurves().filter(function (c) { return c.key === compiled.key; })[0];
    if (dupe) {
      delete state.hidden[dupe.id];
      return quiet ? null : { text: S.dupe(dupe.label), level: 'info' };
    }
    if (state.custom.length >= MAX_CUSTOM) return { text: S.tooMany(MAX_CUSTOM), level: 'err' };
    var used = state.custom.map(function (c) { return c.dash; });
    var dash = DASHES.filter(function (d) { return used.indexOf(d) === -1; })[0];
    var label = /^\s*[oOθΘ]\s*\(/.test(text) ? text : 'O(' + text + ')';
    state.custom.push({
      // Keyed by the canonical form, so the id — and with it the saved
      // hidden/shown state — survives a reload.
      id: 'u:' + compiled.key, raw: text, label: label,
      name: S.yours, ast: compiled.ast, key: compiled.key, stroke: 'var(--text)', dash: dash, custom: true
    });
    return null;
  }

  // ── Number formatting ──────────────────────────────────────────────────
  var SUP = { '0': '⁰', '1': '¹', '2': '²', '3': '³', '4': '⁴', '5': '⁵', '6': '⁶', '7': '⁷', '8': '⁸', '9': '⁹', '-': '⁻', ',': ',' };
  function sup(s) { return String(s).split('').map(function (ch) { return SUP[ch] || ch; }).join(''); }

  // An operation count, given as log2. Small numbers in full, large ones as
  // a × 10^k, and the truly enormous (n! at n = 10^6) as 10^k alone.
  function fmtOps(l2) {
    if (l2 === -Infinity) return '0';
    if (!isFinite(l2)) return '∞';
    var log10 = l2 * Math.LOG10E / Math.LOG2E;
    if (log10 < 6) {
      var v = Math.pow(2, l2);
      return v < 10 ? String(Math.round(v * 10) / 10) : Math.round(v).toLocaleString('en-US');
    }
    var k = Math.floor(log10);
    if (k > 99999) return '10' + sup(Math.round(log10).toLocaleString('en-US'));
    var mant = Number(Math.pow(10, log10 - k).toFixed(1));
    if (mant >= 10) { mant = 1; k += 1; }          // 9.96 rounds to 1.0×10^(k+1), not 10.0×10^k
    return mant.toFixed(1) + '×10' + sup(k);
  }
  function fmtN(n) {
    // Bisection lands a hair under an exact answer (n² ≤ 10⁸ gives 9999.9999…),
    // so nudge before flooring.
    n = Math.floor(n * (1 + 1e-9));
    if (n < 1e5) return n.toLocaleString('en-US');
    var k = Math.floor(Math.log10(n));
    var mant = Number((n / Math.pow(10, k)).toFixed(1));
    if (mant >= 10) { mant = 1; k += 1; }
    return (mant < 1.05 ? '' : mant.toFixed(1) + '×') + '10' + sup(k);
  }

  // Largest n whose operation count stays within 2^budgetL, by bisection on
  // log2 n (every curve here is non-decreasing for n ≥ 1).
  // Infinity means "still fits at n = 2^62", the edge of the search — not
  // "no limit"; the table prints it as a lower bound unless the curve is flat.
  var MAX_LOG_N = 62;
  function maxN(curve, budgetL) {
    if (L(curve, Math.pow(2, MAX_LOG_N)) <= budgetL) return Infinity;
    if (L(curve, 1) > budgetL) return 0;
    var lo = 0, hi = MAX_LOG_N;
    for (var i = 0; i < 80; i++) {
      var mid = (lo + hi) / 2;
      if (L(curve, Math.pow(2, mid)) <= budgetL) lo = mid; else hi = mid;
    }
    return Math.pow(2, lo);
  }

  // ── Chart ──────────────────────────────────────────────────────────────

  var SVGNS = 'http://www.w3.org/2000/svg';
  var geom = null;   // the last layout, for the hover layer

  function niceStep(span, target) {
    var raw = span / target;
    var mag = Math.pow(10, Math.floor(Math.log10(raw)));
    var norm = raw / mag;
    return (norm < 1.5 ? 1 : norm < 3.5 ? 2 : norm < 7.5 ? 5 : 10) * mag;
  }
  function axisNum(v) {
    if (v >= 1e5) return fmtN(v);
    return (Math.round(v * 100) / 100).toLocaleString('en-US');
  }

  function draw() {
    var svg = el('chart');
    var W = Math.max(300, el('chartWrap').clientWidth);
    var H = Math.round(Math.min(460, Math.max(300, W * 0.58)));
    var narrow = W < 520;
    var m = { l: narrow ? 46 : 60, r: narrow ? 74 : 92, t: 30, b: 40 };
    var pw = W - m.l - m.r, ph = H - m.t - m.b;
    svg.setAttribute('viewBox', '0 0 ' + W + ' ' + H);
    svg.setAttribute('width', W);
    svg.setAttribute('height', H);

    var nMax = state.nMax;
    var curves = visibleCurves();
    var isLog = state.scale === 'log';

    // y range. Linear: tall enough that n log n reaches the top at n = nMax —
    // the classic picture, where everything steeper visibly leaves the chart.
    // Log: from 1 operation up to the biggest visible curve, capped at 10^30 so
    // n! at n = 10^6 does not flatten every polynomial into the floor.
    var yTop;
    if (isLog) {
      var maxL = 4;
      curves.forEach(function (c) { var v = L(c, nMax); if (v > maxL) maxL = v; });
      yTop = Math.min(maxL, 100) * 1.04;
    } else {
      yTop = nMax * Math.log2(nMax) * 1.15;
    }
    function xPix(n) { return m.l + (n - 1) / (nMax - 1) * pw; }
    function yPix(l2) {
      if (isLog) return m.t + ph - (Math.max(0, l2) / yTop) * ph;
      var v = l2 === -Infinity ? 0 : Math.pow(2, l2);
      return m.t + ph - (v / yTop) * ph;
    }

    var out = [];
    out.push('<defs><clipPath id="plotClip"><rect x="' + m.l + '" y="' + m.t + '" width="' + pw + '" height="' + ph + '"/></clipPath></defs>');

    // Grid + axes
    var xs = niceStep(nMax - 1, narrow ? 4 : 7);
    for (var xv = Math.ceil(1 / xs) * xs; xv <= nMax + 1e-9; xv += xs) {
      if (xv < 1) continue;
      var px = xPix(xv);
      out.push('<line class="gridline" x1="' + px + '" x2="' + px + '" y1="' + m.t + '" y2="' + (m.t + ph) + '"/>');
      out.push('<text class="axis-text" x="' + px + '" y="' + (m.t + ph + 16) + '" text-anchor="middle">' + esc(axisNum(xv)) + '</text>');
    }
    if (isLog) {
      var top10 = yTop * Math.LOG10E / Math.LOG2E;
      var step10 = Math.max(1, Math.ceil(top10 / 6));
      for (var k = 0; k <= top10; k += step10) {
        var py = yPix(k / Math.log10(2));
        out.push('<line class="gridline" x1="' + m.l + '" x2="' + (m.l + pw) + '" y1="' + py + '" y2="' + py + '"/>');
        out.push('<text class="axis-text" x="' + (m.l - 6) + '" y="' + (py + 4) + '" text-anchor="end">' + (k === 0 ? '1' : '10' + sup(k)) + '</text>');
      }
    } else {
      var ys = niceStep(yTop, 6);
      for (var yv = 0; yv <= yTop; yv += ys) {
        var py2 = yPix(Math.log2(yv || 0));
        if (yv === 0) py2 = m.t + ph;
        out.push('<line class="gridline" x1="' + m.l + '" x2="' + (m.l + pw) + '" y1="' + py2 + '" y2="' + py2 + '"/>');
        out.push('<text class="axis-text" x="' + (m.l - 6) + '" y="' + (py2 + 4) + '" text-anchor="end">' + esc(axisNum(yv)) + '</text>');
      }
    }
    out.push('<line class="axisline" x1="' + m.l + '" x2="' + (m.l + pw) + '" y1="' + (m.t + ph) + '" y2="' + (m.t + ph) + '"/>');
    out.push('<line class="axisline" x1="' + m.l + '" x2="' + m.l + '" y1="' + m.t + '" y2="' + (m.t + ph) + '"/>');
    out.push('<text class="axis-title" x="' + (m.l + pw / 2) + '" y="' + (H - 6) + '" text-anchor="middle">' + esc(S.axisN) + '</text>');
    out.push('<text class="axis-title" x="' + m.l + '" y="' + (m.t - 12) + '" text-anchor="start">' + esc(isLog ? S.axisOpsLog : S.axisOps) + '</text>');

    // Curves
    var SAMPLES = Math.min(600, Math.max(200, pw));
    var rightLabels = [];
    var topLabels = [];
    curves.forEach(function (c) {
      var d = '';
      var exitN = null;
      for (var i = 0; i <= SAMPLES; i++) {
        var n = 1 + (nMax - 1) * i / SAMPLES;
        var l2 = L(c, n);
        var y = yPix(l2);
        if (!isFinite(y) || y < m.t - ph) y = m.t - ph;   // far above: clipped anyway
        if (exitN === null && y < m.t) exitN = n;
        d += (i ? 'L' : 'M') + xPix(n).toFixed(1) + ' ' + y.toFixed(1);
      }
      out.push('<path d="' + d + '" fill="none" clip-path="url(#plotClip)" style="stroke:' + c.stroke + '" stroke-width="2"'
        + (c.dash ? ' stroke-dasharray="' + c.dash + '"' : '') + ' stroke-linejoin="round"/>');
      if (exitN !== null) topLabels.push({ x: xPix(exitN), text: c.label });
      else rightLabels.push({ y: yPix(L(c, nMax)), text: c.label });
    });

    // Direct labels: right edge for curves that finish inside the plot,
    // along the top for those that leave it. Nudged apart so they never stack.
    rightLabels.sort(function (a, b) { return b.y - a.y; });
    var floorY = m.t + ph + 4;
    rightLabels.forEach(function (lb) {
      lb.y = Math.min(lb.y + 4, floorY);
      floorY = lb.y - 13;
    });
    rightLabels.forEach(function (lb) {
      out.push('<text class="curve-label" x="' + (m.l + pw + 6) + '" y="' + lb.y.toFixed(1) + '">' + esc(lb.text) + '</text>');
    });
    topLabels.sort(function (a, b) { return a.x - b.x; });
    var rows = [];   // last right edge used on each row
    topLabels.forEach(function (lb) {
      var w = lb.text.length * 6.8;
      var x = Math.max(m.l, Math.min(lb.x - w / 2, m.l + pw + m.r - w - 2));
      var r = 0;
      while (rows[r] !== undefined && rows[r] > x - 6) r++;
      rows[r] = x + w;
      var y = m.t + 12 + r * 13;
      out.push('<text class="curve-label" x="' + x.toFixed(1) + '" y="' + y + '" style="paint-order:stroke;stroke:var(--bg);stroke-width:3px">' + esc(lb.text) + '</text>');
    });

    // Hover layer
    out.push('<line id="hoverLine" class="axisline" y1="' + m.t + '" y2="' + (m.t + ph) + '" x1="-10" x2="-10" stroke-dasharray="3 3" style="stroke:var(--text-light)"/>');
    out.push('<g id="hoverDots"></g>');
    out.push('<rect id="hitArea" x="' + m.l + '" y="' + m.t + '" width="' + pw + '" height="' + ph + '" fill="transparent"/>');

    svg.innerHTML = out.join('');
    geom = { m: m, pw: pw, ph: ph, W: W, nMax: nMax, xPix: xPix, yPix: yPix, curves: curves };

    el('chartDesc').textContent = curves.length
      ? S.captionStart(fmtN(nMax)) + (isLog ? S.captionLog : S.captionLinear) + S.captionEnd
      : S.captionNone;
  }

  function hover(evt) {
    if (!geom || !geom.curves.length) return;
    var rect = el('chart').getBoundingClientRect();
    var scale = geom.W / rect.width;
    var px = (evt.clientX - rect.left) * scale;
    var frac = Math.min(1, Math.max(0, (px - geom.m.l) / geom.pw));
    var n = 1 + frac * (geom.nMax - 1);
    if (geom.nMax <= 100) n = Math.max(1, Math.round(n));
    var x = geom.xPix(n);
    var line = el('hoverLine');
    line.setAttribute('x1', x); line.setAttribute('x2', x);

    var rows = geom.curves.map(function (c) { return { c: c, l: L(c, n) }; });
    rows.sort(function (a, b) { return b.l - a.l; });
    var dots = '';
    rows.forEach(function (r) {
      var y = geom.yPix(r.l);
      if (y >= geom.m.t && y <= geom.m.t + geom.ph) {
        dots += '<circle cx="' + x + '" cy="' + y + '" r="4" style="fill:' + r.c.stroke + ';stroke:var(--bg)" stroke-width="2"/>';
      }
    });
    el('hoverDots').innerHTML = dots;

    var tip = el('tip');
    tip.innerHTML = '<div class="tn">n = ' + fmtN(n) + '</div>' + rows.map(function (r) {
      return '<div class="row"><span class="swatch' + swatchClass(r.c) + '" style="border-color:' + r.c.stroke + '"></span>'
        + '<span class="nm">' + esc(r.c.label) + '</span><span class="v">' + fmtOps(r.l) + '</span></div>';
    }).join('');
    tip.hidden = false;
    var left = x / scale + 14;
    var tipW = tip.offsetWidth;
    if (left + tipW > rect.width) left = x / scale - tipW - 14;
    tip.style.left = Math.max(0, left) + 'px';
    tip.style.top = (geom.m.t / scale + 4) + 'px';
  }
  function unhover() {
    el('tip').hidden = true;
    var line = el('hoverLine');
    if (line) { line.setAttribute('x1', -10); line.setAttribute('x2', -10); }
    var dots = el('hoverDots');
    if (dots) dots.innerHTML = '';
  }

  function swatchClass(c) {
    if (!c.dash) return '';
    return c.dash === DASHES[1] ? ' dash2' : ' dash1';
  }

  // ── Ladder (legend + toggles) ──────────────────────────────────────────
  function renderLadder() {
    var html = [];
    PRESETS.forEach(function (p, i) {
      if (i) html.push('<span class="lt">&lt;</span>');
      html.push(chipHTML(p));
    });
    el('ladder').innerHTML = html.join('');
    el('customList').innerHTML = state.custom.length
      ? '<span class="label" style="margin-top:0.9rem">' + esc(S.yoursLabel) + '</span><div class="ladder">'
        + state.custom.map(chipHTML).join('') + '</div>'
      : '';
  }
  function chipHTML(c) {
    var toggle = '<button type="button" class="curve-chip' + (state.hidden[c.id] ? ' off' : '') + '" data-id="' + esc(c.id) + '"'
      + ' aria-pressed="' + (!state.hidden[c.id]) + '" title="' + esc(c.name) + '">'
      + '<span class="swatch' + swatchClass(c) + '" style="border-color:' + c.stroke + '"></span>' + esc(c.label)
      + '</button>';
    if (!c.custom) return toggle;
    // Removal is its own button, so it is reachable by keyboard and Enter on
    // the chip still means "toggle".
    return '<span class="chip-pair">' + toggle
      + '<button type="button" class="chip-remove" data-remove="' + esc(c.id) + '" aria-label="' + esc(S.remove(c.label)) + '">×</button></span>';
  }

  // ── Ops table ──────────────────────────────────────────────────────────
  function renderOps() {
    var budgetL = state.budgetExp / Math.log10(2);
    var curves = visibleCurves().slice().sort(function (a, b) { return L(a, 1e6) - L(b, 1e6); });
    el('budgetText').textContent = '10' + sup(state.budgetExp);
    var head = '<thead><tr><th>' + esc(S.thClass) + '</th>' + TABLE_NS.map(function (n) {
      return '<th style="text-align:right">n = ' + fmtN(n) + '</th>';
    }).join('') + '<th style="text-align:right">' + esc(S.thMaxN) + '</th></tr></thead>';
    var body = curves.map(function (c) {
      var cells = TABLE_NS.map(function (n) {
        var l2 = L(c, n);
        var cls = l2 <= budgetL ? 'fit' : l2 <= budgetL + Math.log2(10) ? 'tight' : 'slow';
        return '<td class="num ' + cls + '">' + fmtOps(l2) + '</td>';
      }).join('');
      var mx = maxN(c, budgetL);
      var mxText = mx === Infinity
        ? (L(c, Math.pow(2, MAX_LOG_N)) === L(c, 1) ? esc(S.any) : '&gt; ' + fmtN(Math.pow(2, MAX_LOG_N)))
        : mx < 1 ? '—' : fmtN(mx);
      return '<tr><td class="cls"><span class="swatch' + swatchClass(c) + '" style="border-color:' + c.stroke + '"></span>'
        + esc(c.label) + '</td>' + cells + '<td class="num maxn">' + mxText + '</td></tr>';
    }).join('');
    el('opsTable').innerHTML = head + '<tbody>' + (body || '<tr><td colspan="8">' + esc(S.opsEmpty) + '</td></tr>') + '</tbody>';
  }

  function renderClasses() {
    el('classTable').innerHTML = '<thead><tr><th>' + esc(S.thClass) + '</th><th>' + esc(S.thName) + '</th><th>' + esc(S.thShape)
      + '</th><th>' + esc(S.thLc) + '</th></tr></thead><tbody>'
      + PRESETS.map(function (p) {
        return '<tr><td class="cls"><span class="swatch" style="border-color:' + p.stroke + '"></span>' + esc(p.label) + '</td>'
          + '<td>' + esc(p.name) + '</td><td>' + esc(p.shape) + '</td><td>' + esc(p.lc) + '</td></tr>';
      }).join('') + '</tbody>';
  }

  // ── Compare ────────────────────────────────────────────────────────────
  //
  // Asymptotic order is read off the log2-ratio d(n) = L(a) - L(b) at three
  // far-apart n. If d has stopped moving, the ratio tends to a constant: same
  // class. If it keeps moving, its sign says which one wins.
  //
  // Two things make this numerical, and the page says so rather than guess:
  //  - Precision. L is a double, so at n = 2^512, 2^n's L is 2^512 and the
  //    extra log2 n of n·2^n is lost in rounding. Probes are kept small enough
  //    that L stays exact to well under one bit, and a difference smaller than
  //    the rounding error is reported as undecided, never as a winner.
  //  - Factors like sqrt(log log n) grow so slowly that no feasible probe sees
  //    them move. Only identical canonical forms (n vs 2n) are certain to be
  //    the same class; a flat numerical ratio is reported as "as far as can be
  //    measured".
  //
  // The first set is far out on purpose: an additive constant as large as
  // 10^15 (n vs n + 10^15) still moves the ratio at 2^16..2^48, and only
  // probes well past it see the two flatten into one class. It is used only
  // when every value there stays exact (|L| ≤ 2^36, where a double still
  // resolves 10⁻⁵ of a bit); n·2^n vs 2^n is not, and falls through to the
  // smaller sets that can see its extra factor of n.
  var EXACT_L = Math.pow(2, 36);
  var PROBE_SETS = [
    [Math.pow(2, 64), Math.pow(2, 128), Math.pow(2, 256)],
    [Math.pow(2, 16), Math.pow(2, 32), Math.pow(2, 48)],
    [Math.pow(2, 8), Math.pow(2, 12), Math.pow(2, 16)],
    [16, 32, 64],
    [4, 6, 8]
  ];
  var CROSS_LIMIT = 1e18;

  // Additive constants never change the class (O(n + 10^15) = O(n)), but one
  // big enough can outlast every probe: log n only reaches 256 at n = 2^256,
  // nowhere near 10^15. So they are removed structurally before measuring.
  // Only through +, *, log and sqrt, where that is sound — every remaining
  // term grows without bound, so a constant beside it is a lower-order term.
  // Not inside a power or a factorial: (n+10)! is n^10 times n!.
  function hasVar(node) {
    if (node.t === 'var') return true;
    if (node.t === 'num') return false;
    var kids = node.xs || [node.x, node.b, node.e].filter(Boolean);
    return kids.some(hasVar);
  }
  function dropConstants(node) {
    switch (node.t) {
      case 'add': {
        var kept = node.xs.filter(hasVar).map(dropConstants);
        if (!kept.length) return node;                  // all constant: leave it
        return kept.length === 1 ? kept[0] : { t: 'add', xs: kept };
      }
      case 'mul': return { t: 'mul', xs: node.xs.map(dropConstants) };
      case 'log': case 'sqrt': return { t: node.t, x: dropConstants(node.x) };
      default: return node;
    }
  }

  function asymptotic(A, B) {
    A = { ast: dropConstants(A.ast) };
    B = { ast: dropConstants(B.ast) };
    for (var s = 0; s < PROBE_SETS.length; s++) {
      var probes = PROBE_SETS[s];
      var la = probes.map(function (n) { return L(A, n); });
      var lb = probes.map(function (n) { return L(B, n); });
      var finite = la.concat(lb).every(function (v) { return isFinite(v); });
      if (!finite) continue;   // too steep for these n: try smaller ones
      var exact = la.concat(lb).every(function (v) { return Math.abs(v) <= EXACT_L; });
      if (s === 0 && !exact) continue;   // the far set only decides when it is exact
      var d = la.map(function (v, i) { return v - lb[i]; });
      // Rounding error of a difference of two doubles of this size, in bits.
      var tol = probes.map(function (n, i) { return (Math.abs(la[i]) + Math.abs(lb[i])) * Math.pow(2, -50); });
      // Out at 2^64..2^256 a same-class pair has flattened to hundredths of a
      // bit, while log log n still moves 0.2 a step — so the far set needs a
      // much tighter bar than the near ones, or n log log n would pass as n.
      var bar = s === 0 ? 0.02 : 0.25;
      var flat = Math.abs(d[2] - d[1]) < bar + tol[2] + tol[1] && Math.abs(d[1] - d[0]) < bar + tol[1] + tol[0];
      if (flat) return { kind: 'same', ratio: Math.pow(2, d[2]), upTo: probes[2] };
      // The *trend* of d decides, not its sign at one n: n² vs 10¹⁵·n is
      // still negative at 2^48 but rising, and n² is the one that wins.
      var trend = d[2] - d[1];
      if (Math.abs(trend) <= 4 * (tol[2] + tol[1])) return { kind: 'unknown' };
      return { kind: 'order', faster: trend > 0 ? 'a' : 'b' };
    }
    return { kind: 'unknown' };
  }

  function compare() {
    var a = el('cmpA').value.trim(), b = el('cmpB').value.trim();
    var ca = CSComplexity.compile(a), cb = CSComplexity.compile(b);
    var out = el('cmpOut');
    var badA = !ca || /\d\.\d/.test(a);
    var badB = !cb || /\d\.\d/.test(b);
    if (badA || badB) {
      // Name the field that actually failed (a decimal compiles, so !ca alone
      // is not enough), and show "(empty)" for either blank field.
      out.innerHTML = '<p class="msg err">' + S.cmpUnreadable(esc((badA ? a : b) || S.empty)) + '</p>';
      return;
    }
    var A = { ast: ca.ast }, B = { ast: cb.ast };
    var la = 'O(' + a.replace(/^\s*[oOθΘ]\s*\((.*)\)\s*$/, '$1') + ')';
    var lb = 'O(' + b.replace(/^\s*[oOθΘ]\s*\((.*)\)\s*$/, '$1') + ')';
    var at6 = L(A, 1e6) - L(B, 1e6);
    var atLine = isFinite(at6)
      ? '<p>' + S.at6(esc(la), fmtOps(L(A, 1e6)), esc(lb), fmtOps(L(B, 1e6)), fmtOps(Math.abs(at6))) + '</p>'
      : '';

    // Identical canonical forms differ only by constant factors: certain.
    if (ca.key === cb.key) {
      out.innerHTML = '<div class="big">' + S.sameExact(esc(la), esc(lb)) + '</div>'
        + '<p>' + S.sameExactWhy + '</p>';
      return;
    }

    var result = asymptotic(A, B);
    if (result.kind === 'same') {
      out.innerHTML = '<div class="big">' + S.sameNum(esc(la), esc(lb)) + '</div>'
        + '<p>' + S.sameNumRatio(Math.round(result.ratio * 100) / 100, fmtN(result.upTo)) + '</p>'
        + '<p>' + S.sameNumCaveat + '</p>' + atLine;
      return;
    }
    if (result.kind === 'unknown') {
      out.innerHTML = '<div class="big">' + S.unknown + '</div>'
        + '<p>' + S.unknownWhy + '</p>' + atLine;
      return;
    }

    var big = result.faster === 'a' ? la : lb, small = result.faster === 'a' ? lb : la;
    var Big = result.faster === 'a' ? A : B, Small = result.faster === 'a' ? B : A;
    // The last n (on a fine grid up to CROSS_LIMIT) where the faster one is
    // not yet ahead. If it is still behind at the limit, the crossover is
    // somewhere past it — say so rather than present the limit as the answer.
    // The scan also stops once L passes 2^45: beyond that a double cannot
    // resolve a few bits of difference (2^n vs n·2^n), and a rounding tie
    // would read as "still behind".
    // `cross` is the last n where the faster one costs no more (a tie counts:
    // n² and 100·n meet at n = 100, so n² is ahead only past 100). `cheaper`
    // records whether it was ever strictly cheaper — a tie alone (both 1 op
    // at n = 1) is not a range where the "worse" class wins.
    var cross = 0, cheaper = false, lastN = 1, n;
    for (n = 1; n <= CROSS_LIMIT; n = n < 1000 ? n + 1 : n * 1.01) {
      var lBig = L(Big, n), lSmall = L(Small, n);
      // -Infinity is a cost of 0 (log 1), exact and fine; only +Infinity or
      // a huge finite L ends the scan.
      if (lBig === Infinity || lSmall === Infinity
          || Math.max(isFinite(lBig) ? lBig : 0, isFinite(lSmall) ? lSmall : 0) > Math.pow(2, 45)) break;
      lastN = n;
      if (lBig <= lSmall) cross = n;
      if (lBig < lSmall) cheaper = true;
    }
    var stillBehind = cheaper && cross === lastN && lastN > 1;
    var crossLine;
    if (stillBehind) {
      crossLine = '<p>' + S.stillBehind(esc(big), fmtN(lastN)) + '</p>';
    } else if (cheaper) {
      crossLine = '<p>' + S.crossAt(esc(big), cross >= 1000, fmtN(cross)) + '</p>';
    } else {
      crossLine = '<p>' + S.always(esc(big)) + '</p>';
    }
    out.innerHTML = '<div class="big">' + S.slower(esc(small), esc(big)) + '</div>'
      + '<p>' + S.better(esc(small), esc(big)) + '</p>'
      + crossLine + atLine;
  }

  // ── Wiring ─────────────────────────────────────────────────────────────
  function renderControls() {
    el('rangeChips').innerHTML = RANGES.map(function (r) {
      return '<button type="button" class="chip' + (r === state.nMax ? ' active' : '') + '" data-range="' + r + '">' + fmtN(r) + '</button>';
    }).join('');
    el('scaleChips').innerHTML = ['linear', 'log'].map(function (s) {
      return '<button type="button" class="chip' + (s === state.scale ? ' active' : '') + '" data-scale="' + s + '">' + esc(S.scales[s]) + '</button>';
    }).join('');
    el('budgetSelect').value = String(state.budgetExp);
  }

  function renderAll() {
    renderLadder();
    renderControls();
    draw();
    renderOps();
    save();
  }

  function onLadderClick(evt) {
    var rm = evt.target.closest('[data-remove]');
    if (rm) {
      var id = rm.getAttribute('data-remove');
      state.custom = state.custom.filter(function (c) { return c.id !== id; });
      delete state.hidden[id];
      renderAll();
      return;
    }
    var chip = evt.target.closest('[data-id]');
    if (!chip) return;
    var cid = chip.getAttribute('data-id');
    if (state.hidden[cid]) delete state.hidden[cid]; else state.hidden[cid] = true;
    renderAll();
  }

  function submitCustom(raw) {
    var res = addCustom(raw, false);
    var msg = el('addMsg');
    msg.className = 'msg' + (res && res.level === 'err' ? ' err' : '');
    msg.textContent = res ? res.text : S.plotted(state.custom[state.custom.length - 1].label);
    if (!res) el('addInput').value = '';
    else if (!String(raw || '').trim()) el('addInput').focus();
    renderAll();
  }

  el('ladder').addEventListener('click', onLadderClick);
  el('customList').addEventListener('click', onLadderClick);
  el('addForm').addEventListener('submit', function (evt) { evt.preventDefault(); submitCustom(el('addInput').value); });
  // A message answers the last Plot; once the box is being edited it is about
  // something that is no longer there, so it goes.
  el('addInput').addEventListener('input', function () {
    var msg = el('addMsg');
    msg.textContent = '';
    msg.className = 'msg';
  });
  document.querySelector('.examples').addEventListener('click', function (evt) {
    var b = evt.target.closest('[data-ex]');
    if (b) submitCustom(b.getAttribute('data-ex'));
  });
  el('rangeChips').addEventListener('click', function (evt) {
    var b = evt.target.closest('[data-range]');
    if (!b) return;
    state.nMax = Number(b.getAttribute('data-range'));
    renderAll();
  });
  el('scaleChips').addEventListener('click', function (evt) {
    var b = evt.target.closest('[data-scale]');
    if (!b) return;
    state.scale = b.getAttribute('data-scale');
    renderAll();
  });
  el('budgetSelect').addEventListener('change', function () {
    state.budgetExp = Number(el('budgetSelect').value);
    renderOps();
    save();
  });
  el('cmpForm').addEventListener('submit', function (evt) { evt.preventDefault(); compare(); });

  var chart = el('chart');
  chart.addEventListener('mousemove', hover);
  chart.addEventListener('mouseleave', unhover);
  chart.addEventListener('touchstart', function (evt) { hover(evt.touches[0]); }, { passive: true });
  chart.addEventListener('touchmove', function (evt) { hover(evt.touches[0]); }, { passive: true });

  var resizeTimer = null;
  window.addEventListener('resize', function () {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(draw, 80);
  });

  load();
  renderClasses();
  renderAll();
  compare();
})();
