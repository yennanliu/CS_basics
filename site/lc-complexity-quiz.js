/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — Complexity Quiz

   One script behind two shells: lc-complexity-quiz.html (English) and
   lc-complexity-quiz.zh.html (繁體中文). Every string it writes comes out of
   STRINGS below, picked by <html lang>, and the question bank it fetches is
   the language's own build (data/complexity-quiz.json or .zh.json) — so a fix
   to the quiz lands in both languages at once. site/test/quiz-page.test.js
   boots both shells and fails on a key one language lacks.

   Grading is complexity.js's, the same normaliser site/build-quiz.js validates
   the bank with, so the answers a build accepts and the answers a user may
   type are the same set by construction.
   ───────────────────────────────────────────────────────────────────────── */
(function () {
  'use strict';

  var STRINGS = {
    en: {
      loading: 'Loading questions…',
      loadFailed: 'Could not load the question bank.',
      all: 'Everything',
      allN: function (n) { return 'all ' + n; },
      // The "Question set" dropdown, keyed by the pack names build-quiz.js
      // emits, plus `patterns` (one question per pattern, across both packs)
      // and `all` for every question at once.
      packs: { classic: 'Classic set', essentials: 'Interview essentials',
               patterns: 'Pattern coverage', all: 'Every question' },
      packOption: function (label, n) { return label + ' (' + n + ')'; },
      pool: function (available, asked) {
        return available + ' question' + (available === 1 ? '' : 's') + ' in this pool · '
          + asked + ' will be drawn · ' + (asked * 2) + ' marks';
      },
      symbols: 'Symbols',
      note: 'Note',
      time: 'Time',
      space: 'Space',
      next: 'Next →',
      results: 'See results →',
      or: 'or',
      youSaid: 'you said',
      blank: 'left blank',
      line: 'line',
      lines: 'lines',
      noLines: 'no line',
      keyIdea: 'Key idea',
      trap: 'Common slip',
      pattern: 'Pattern',
      solution: 'full solution ↗',
      cheatsheet: 'complexity cheatsheet',
      cheatsheetHref: 'cheatsheets/complexity_cheatsheet.html',
      // Keyed by compare()'s verdict, then by the dimension being graded.
      diag: {
        over: { time: 'a true upper bound, but not tight — some step is cheaper than you priced it',
                space: 'a true upper bound, but not tight — less is held at once than that' },
        under: { time: 'too low — the code does more work than that',
                 space: 'too low — the code holds more than that at its peak' },
        different: { time: 'a different shape — check which input each loop actually runs over',
                     space: 'a different shape — check what each structure grows with' }
      },
      unknownSymbol: function (bad, known) {
        return '<code>' + bad.join('</code>, <code>') + '</code> is not a symbol in this question'
          + (known.length ? ' — it uses <code>' + known.join('</code>, <code>') + '</code>' : '');
      },
      unreadable: function (said) {
        return 'Could not read <code>' + said + '</code> as a complexity — try a form like <code>O(n log n)</code>.';
      },
      scoreSub: function (pct, clean, n, t, s) {
        return pct + '% · ' + clean + ' of ' + n + ' fully right · time ' + t + '/' + n + ' · space ' + s + '/' + n;
      },
      missedTime: 'time is',
      missedSpace: 'space is',
      difficulty: { Easy: 'Easy', Medium: 'Medium', Hard: 'Hard' },
      verdicts: [
        [0.9, 'Interview-ready. Complexity is not what will cost you the offer.'],
        [0.75, 'Solid. Tighten the misses below and this stops being a risk.'],
        [0.5, 'Shaky. You can read the algorithm — you are not yet pricing it.'],
        [0, 'Start with the cheatsheet, then come back. This is learnable in an afternoon.']
      ]
    },
    zh: {
      loading: '載入題目中…',
      loadFailed: '無法載入題庫。',
      all: '全部',
      allN: function (n) { return '全部 ' + n; },
      packs: { classic: '經典題組', essentials: '面試必備', patterns: '題型全覆蓋', all: '全部題目' },
      packOption: function (label, n) { return label + '（' + n + '）'; },
      pool: function (available, asked) {
        return '此範圍共 ' + available + ' 題 · 抽出 ' + asked + ' 題 · 共 ' + (asked * 2) + ' 分';
      },
      symbols: '符號',
      note: '附註',
      time: '時間',
      space: '空間',
      next: '下一題 →',
      results: '看結果 →',
      or: '或',
      youSaid: '你的答案',
      blank: '未作答',
      line: '第',
      lines: '第',
      noLines: '無',
      keyIdea: '重點',
      trap: '常見錯誤',
      pattern: '題型',
      solution: '完整解法 ↗',
      cheatsheet: '複雜度速查表',
      cheatsheetHref: 'cheatsheets/complexity_cheatsheet.zh.html',
      diag: {
        over: { time: '是正確的上界，但不夠緊 —— 有些步驟比你估的更便宜',
                space: '是正確的上界，但不夠緊 —— 同時保留的東西沒有那麼多' },
        under: { time: '太低了 —— 程式實際做的工作比這更多',
                 space: '太低了 —— 程式在高峰時保留的比這更多' },
        different: { time: '成長形狀不同 —— 檢查每個迴圈實際跑過的是哪個輸入',
                     space: '成長形狀不同 —— 檢查每個結構是隨什麼而增長' }
      },
      unknownSymbol: function (bad, known) {
        return '<code>' + bad.join('</code>、<code>') + '</code> 不是本題的符號'
          + (known.length ? ' —— 本題使用 <code>' + known.join('</code>、<code>') + '</code>' : '');
      },
      unreadable: function (said) {
        return '無法把 <code>' + said + '</code> 讀成複雜度 —— 試試 <code>O(n log n)</code> 這種寫法。';
      },
      scoreSub: function (pct, clean, n, t, s) {
        return pct + '% · ' + n + ' 題中 ' + clean + ' 題全對 · 時間 ' + t + '/' + n + ' · 空間 ' + s + '/' + n;
      },
      missedTime: '時間為',
      missedSpace: '空間為',
      difficulty: { Easy: '簡單', Medium: '中等', Hard: '困難' },
      verdicts: [
        [0.9, '面試就緒。複雜度不會是讓你錯失 offer 的原因。'],
        [0.75, '穩固。把下面答錯的補強，這就不再是風險。'],
        [0.5, '不太穩。你看得懂演算法 —— 但還不會替它估價。'],
        [0, '先讀速查表再回來。這個一個下午就學得會。']
      ]
    }
  };

  var LANG = /^zh/i.test(document.documentElement.lang || '') ? 'zh' : 'en';
  var S = STRINGS[LANG];
  var DATA_URL = LANG === 'zh' ? './data/complexity-quiz.zh.json' : './data/complexity-quiz.json';

  var COUNTS = [5, 10, 25, 50, 100];
  var STORE_KEY = 'complexity-quiz-prefs';

  var bank = [];                                    // every question, as built
  var topicLabels = {};                             // English topic -> shown label
  var packs = [];                                   // the sets the bank carries, in order
  var prefs = { count: 10, topic: 'all', pack: 'classic' };
  // What gets stored. Only the dropdowns move these: a ?set= link, or a Focus
  // the current set lacks falling back to everything, is for this visit only.
  var savedPack = 'classic';
  var savedTopic = 'all';
  var run = null;                                   // the quiz in progress

  var el = function (id) { return document.getElementById(id); };

  function esc(value) {
    return String(value)
      .replace(/&/g, '&amp;').replace(/</g, '&lt;')
      .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }

  // Inline `code` spans and *emphasis* in the authored prose, and nothing else
  // — the bank is ours, but it is still data, so it is escaped before the two
  // tags go in. Emphasis is applied outside the code spans only, so a stray `*`
  // in a snippet like `O(m*n)` cannot pair up with one in the next span.
  function prose(text) {
    return esc(text).split(/(`[^`]+`)/).map(function (part) {
      if (part.charAt(0) === '`' && part.length > 1) return '<code>' + part.slice(1, -1) + '</code>';
      return part.replace(/\*([^*]+)\*/g, '<em>$1</em>');
    }).join('');
  }

  function show(id, visible) { el(id).hidden = !visible; }
  function topicLabel(topic) { return topicLabels[topic] || topic; }

  // ── Python highlighting ────────────────────────────────────────────────
  //
  // Just enough to make a snippet readable: comments, strings, keywords,
  // builtins, numbers. Pulling in highlight.js for six colours would cost more
  // bytes than every question in the bank put together.

  var PY = new RegExp([
    '(#[^\\n]*)',
    '("(?:[^"\\\\\\n]|\\\\.)*"|\'(?:[^\'\\\\\\n]|\\\\.)*\')',
    '\\b(def|class|return|if|elif|else|for|while|in|not|and|or|is|None|True|False|'
      + 'import|from|lambda|yield|break|continue|pass|with|as|try|except|finally|'
      + 'global|nonlocal|assert|del|raise|self)\\b',
    '\\b(len|range|enumerate|sorted|sum|max|min|abs|any|all|zip|map|filter|reversed|'
      + 'set|dict|list|tuple|int|str|float|print|heapq|deque|Counter|defaultdict|OrderedDict|bisect_left)\\b',
    '(\\b\\d+\\b)'
  ].join('|'), 'g');

  function highlightLine(code) {
    var out = '';
    var last = 0;
    var m;
    PY.lastIndex = 0;
    while ((m = PY.exec(code)) !== null) {
      out += esc(code.slice(last, m.index));
      var cls = m[1] ? 'c' : m[2] ? 's' : m[3] ? 'k' : m[4] ? 'b' : 'n';
      out += '<span class="' + cls + '">' + esc(m[0]) + '</span>';
      last = m.index + m[0].length;
    }
    return out + esc(code.slice(last));
  }

  // One <span class="ln"> per line, numbered, so the verdict can point at the
  // lines that set each bound. The T / S tags are there from the start (empty
  // until graded) so marking a line never reflows the snippet.
  function renderCode(q) {
    var time = q.cost.time.lines;
    var space = q.cost.space.lines;
    return q.code.split('\n').map(function (line, i) {
      var n = i + 1;
      var cls = 'ln' + (time.indexOf(n) >= 0 ? ' hot-t' : '') + (space.indexOf(n) >= 0 ? ' hot-s' : '');
      return '<span class="' + cls + '" data-n="' + n + '">' + (highlightLine(line) || ' ') + '</span>';
    }).join('');
  }

  // [3, 4, 5, 9] -> "3–5, 9"
  function lineRange(lines) {
    var sorted = lines.slice().sort(function (a, b) { return a - b; });
    var parts = [];
    for (var i = 0; i < sorted.length; i++) {
      var start = sorted[i];
      while (i + 1 < sorted.length && sorted[i + 1] === sorted[i] + 1) i++;
      parts.push(start === sorted[i] ? String(start) : start + '–' + sorted[i]);
    }
    return parts.join(', ');
  }

  function linesLabel(lines) {
    if (!lines.length) return S.noLines;
    if (LANG === 'zh') return '第 ' + lineRange(lines) + ' 行';
    return (lines.length === 1 ? S.line : S.lines) + ' ' + lineRange(lines);
  }

  // ── Preferences ────────────────────────────────────────────────────────
  // Wrapped like nav.js does: Safari's private mode throws rather than
  // returning null, and losing a preference must never break the page.

  function loadPrefs() {
    try {
      var raw = localStorage.getItem(STORE_KEY);
      if (raw) {
        var saved = JSON.parse(raw);
        if (saved && typeof saved === 'object') {
          if (saved.count) prefs.count = saved.count;
          if (saved.topic) prefs.topic = saved.topic;
          if (saved.pack) prefs.pack = saved.pack;
        }
      }
    } catch (e) { /* defaults are fine */ }
  }

  function savePrefs() {
    var stored = { count: prefs.count, topic: savedTopic, pack: savedPack };
    try { localStorage.setItem(STORE_KEY, JSON.stringify(stored)); } catch (e) { /* ignore */ }
  }

  // ── Setup screen ───────────────────────────────────────────────────────

  // `?set=essentials` (or classic / all) picks the question set from a link —
  // the flag form of the dropdown. It wins over the stored preference for
  // this visit but is not saved, so a shared link does not rewrite anyone's
  // default.
  function packFromUrl() {
    try {
      var value = new URLSearchParams(window.location.search).get('set');
      return value && sets().indexOf(value) !== -1 ? value : null;
    } catch (e) { return null; }
  }

  // Pattern coverage is not a pack: it is the one question that represents
  // each pattern (build-quiz.js stamps `pattern` on it), drawn from both packs.
  function hasPatterns() { return bank.some(function (q) { return q.pattern; }); }

  // Every choice the dropdown offers, in order.
  function sets() {
    return packs.concat(hasPatterns() ? ['patterns'] : [], packs.length > 1 || hasPatterns() ? ['all'] : []);
  }

  function inSet(set) {
    if (set === 'all') return bank;
    if (set === 'patterns') return bank.filter(function (q) { return q.pattern; });
    return bank.filter(function (q) { return q.pack === set; });
  }

  function packPool() { return inSet(prefs.pack); }

  function pool() {
    var questions = packPool();
    if (prefs.topic === 'all') return questions;
    return questions.filter(function (q) { return q.topic === prefs.topic; });
  }

  // The Focus dropdown lists only topics the chosen set has questions in, so
  // no choice can lead to an empty pool. The Focus shown is always the saved
  // one whenever the set has it, and Everything only while it does not — so a
  // set without the saved topic never erases it, and a set that has it again
  // brings it back rather than leaving a stored Focus the page is not showing.
  function renderTopics() {
    var counts = {};
    packPool().forEach(function (q) { counts[q.topic] = (counts[q.topic] || 0) + 1; });
    var topics = Object.keys(counts).sort();
    prefs.topic = savedTopic === 'all' || counts[savedTopic] ? savedTopic : 'all';
    el('topicSelect').innerHTML = '<option value="all">' + esc(S.all) + '</option>'
      + topics.map(function (t) {
        return '<option value="' + esc(t) + '">' + esc(topicLabel(t)) + '</option>';
      }).join('');
    el('topicSelect').value = prefs.topic;
  }

  function renderPacks() {
    var choices = sets();
    el('packSelect').innerHTML = choices.map(function (p) {
      return '<option value="' + esc(p) + '">' + esc(S.packOption(S.packs[p] || p, inSet(p).length)) + '</option>';
    }).join('');
    el('packSelect').value = prefs.pack;
    // A bank with a single set has nothing to choose between.
    el('packRow').hidden = choices.length < 2;
  }

  function askedCount() {
    return prefs.count === 'all' ? pool().length : prefs.count;
  }

  function renderSetup() {
    var available = pool().length;
    el('countChips').innerHTML = COUNTS.map(function (n) {
      var reachable = n <= available;
      return '<button type="button" class="chip' + (prefs.count === n ? ' active' : '') + '"'
        + (reachable ? '' : ' disabled') + ' data-count="' + n + '">' + n + '</button>';
    }).join('')
      + '<button type="button" class="chip' + (prefs.count === 'all' ? ' active' : '') + '"'
      + ' data-count="all">' + esc(S.allN(available)) + '</button>';

    var asked = Math.min(askedCount(), available);
    el('poolText').textContent = S.pool(available, asked);
    el('startBtn').disabled = available === 0;
  }

  // ── Drawing a quiz ─────────────────────────────────────────────────────

  // Fisher-Yates. `sort(() => Math.random() - 0.5)` is the tempting one-liner
  // and it is not a uniform shuffle — it visibly favours the original order.
  function shuffled(items) {
    var out = items.slice();
    for (var i = out.length - 1; i > 0; i--) {
      var j = Math.floor(Math.random() * (i + 1));
      var swap = out[i];
      out[i] = out[j];
      out[j] = swap;
    }
    return out;
  }

  function startQuiz(questions) {
    run = { questions: questions, at: 0, results: [], checked: false,
            set: prefs.pack, focus: prefs.topic, startedAt: new Date() };
    show('setup', false);
    show('report', false);
    show('quiz', true);
    renderQuestion();
  }

  function startFromSetup() {
    var picked = shuffled(pool()).slice(0, Math.max(1, Math.min(askedCount(), pool().length)));
    if (!picked.length) return;
    startQuiz(picked);
  }

  // ── The question screen ────────────────────────────────────────────────

  function current() { return run.questions[run.at]; }

  function titleText(q) { return (q.lc ? 'LC ' + q.lc + ' · ' : '') + q.title; }

  function renderQuestion() {
    var q = current();
    var total = run.questions.length;

    el('progressBar').style.width = (run.at / total * 100) + '%';
    el('qCounter').textContent = (run.at + 1) + ' / ' + total;
    el('qTitle').innerHTML = q.links.lc
      ? '<a href="' + esc(q.links.lc) + '" target="_blank" rel="noopener">' + esc(titleText(q)) + '</a>'
      : esc(titleText(q));
    el('qDifficulty').textContent = S.difficulty[q.difficulty] || q.difficulty;
    el('qDifficulty').className = 'badge ' + q.difficulty;
    el('qTopic').textContent = topicLabel(q.topic);
    el('qCode').innerHTML = renderCode(q);
    el('qCode').className = 'code';

    el('qVars').innerHTML = q.vars ? '<b>' + S.symbols + '</b> — ' + prose(q.vars) : '';
    el('qVars').hidden = !q.vars;
    el('qNote').innerHTML = q.note ? '<b>' + S.note + '</b> — ' + prose(q.note) : '';
    el('qNote').hidden = !q.note;
    show('legend', false);

    ['time', 'space'].forEach(function (field) {
      var input = el(field + 'Input');
      input.value = '';
      input.className = '';
      input.readOnly = false;
      el(field + 'Msg').innerHTML = '';
    });

    run.checked = false;
    show('verdict', false);
    el('checkBtn').hidden = false;
    el('nextBtn').hidden = true;
    el('nextBtn').textContent = run.at === total - 1 ? S.results : S.next;
    el('timeInput').focus();
  }

  // The symbols a question defines: its answers' variables plus every
  // single-letter name its `vars` line assigns ("n = len(nums), k = …").
  function knownSymbols(q) {
    var known = {};
    var answers = [q.time, q.space].concat(q.accept.time, q.accept.space);
    answers.forEach(function (a) {
      (CSComplexity.variables(a) || []).forEach(function (v) { known[v] = true; });
    });
    var declared = /(?:^|[^a-zA-Z_])([a-zA-Z])\s*=/g;
    var m;
    while ((m = declared.exec(q.vars || '')) !== null) known[m[1].toLowerCase()] = true;
    return Object.keys(known).sort();
  }

  // Why a wrong answer is wrong, in one clause: an unknown symbol first (that
  // is almost always the real mistake), then how its growth compares.
  function diagnose(field, said, expected, q) {
    if (!said.trim()) return '';
    var used = CSComplexity.variables(said) || [];
    var known = knownSymbols(q);
    var unknown = used.filter(function (v) { return known.indexOf(v) < 0; });
    if (unknown.length) return S.unknownSymbol(unknown.map(esc), known.map(esc));
    var how = CSComplexity.compare(said, expected);
    return S.diag[how] ? esc(S.diag[how][field]) : '';
  }

  function dimension(field, ok, said, q) {
    var cls = field === 'time' ? 't' : 's';
    var cost = q.cost[field];
    var alts = q.accept[field];
    var yours = '';
    if (!ok) {
      yours = said.trim()
        ? S.youSaid + ' <s>' + esc(said.trim()) + '</s>'
        : S.blank;
      var diag = diagnose(field, said, q[field], q);
      if (diag) yours += ' — <span class="diag">' + diag + '</span>';
    }
    return '<div class="dim ' + cls + ' ' + (ok ? 'right' : 'wrong') + '" data-dim="' + cls + '">'
      + '<div class="dim-head"><span class="sign">' + (ok ? '✓' : '✗') + '</span>'
      + '<span class="field">' + S[field] + '</span>'
      + '<span class="answer">' + esc(q[field]) + '</span>'
      + (alts.length ? '<span class="alts">(' + S.or + ' ' + alts.map(esc).join(', ') + ')</span>' : '')
      + '</div>'
      + (yours ? '<div class="yours">' + yours + '</div>' : '')
      + '<div class="reason"><span class="lines">' + esc(linesLabel(cost.lines)) + '</span>'
      + '<span>' + prose(cost.why) + '</span></div>'
      + '</div>';
  }

  // Unparseable input is sent back rather than graded: "O(n log n" with a
  // typo'd paren, or "n squared", should cost a retype, not a mark.
  function unreadable(field) {
    var said = el(field + 'Input').value;
    if (!said.trim() || CSComplexity.normalize(said) !== null) {
      el(field + 'Msg').innerHTML = '';
      return false;
    }
    el(field + 'Msg').innerHTML = S.unreadable(esc(said.trim()));
    return true;
  }

  function check() {
    if (run.checked) return;
    var badTime = unreadable('time');
    var badSpace = unreadable('space');
    if (badTime || badSpace) {
      el(badTime ? 'timeInput' : 'spaceInput').focus();
      return;
    }

    var q = current();
    var said = { time: el('timeInput').value, space: el('spaceInput').value };
    var got = CSComplexity.grade(said, q);

    run.checked = true;
    run.results.push({ question: q, said: said, got: got });

    el('timeInput').className = got.time ? 'right' : 'wrong';
    el('spaceInput').className = got.space ? 'right' : 'wrong';
    el('timeInput').readOnly = true;
    el('spaceInput').readOnly = true;
    el('qCode').className = 'code graded';
    show('legend', true);

    // The pattern is named only once the answer is in: shown up front it would
    // be half the answer.
    var html = q.pattern
      ? '<p class="pattern-tag"><b>' + S.pattern + '</b>' + esc(q.pattern) + '</p>'
      : '';
    html += dimension('time', got.time, said.time, q) + dimension('space', got.space, said.space, q);
    if (q.why) html += '<p class="takeaway"><b>' + S.keyIdea + '</b>' + prose(q.why) + '</p>';
    if (q.trap) html += '<p class="takeaway trap"><b>' + S.trap + '</b>' + prose(q.trap) + '</p>';

    var refs = [];
    if (q.links.repo) refs.push('<a href="' + esc(q.links.repo) + '" target="_blank" rel="noopener">' + S.solution + '</a>');
    refs.push('<a href="' + S.cheatsheetHref + '">' + S.cheatsheet + '</a>');
    html += '<div class="refs">' + refs.join('') + '</div>';

    el('verdict').innerHTML = html;
    show('verdict', true);
    el('checkBtn').hidden = true;
    el('nextBtn').hidden = false;
    el('nextBtn').focus();
  }

  function next() {
    run.at += 1;
    if (run.at >= run.questions.length) finish();
    else renderQuestion();
  }

  // ── Report ─────────────────────────────────────────────────────────────

  function finish() {
    show('quiz', false);
    show('report', true);

    var results = run.results;
    var marks = results.reduce(function (n, r) { return n + (r.got.time ? 1 : 0) + (r.got.space ? 1 : 0); }, 0);
    var total = results.length * 2;
    var pct = total ? Math.round(marks / total * 100) : 0;
    var clean = results.filter(function (r) { return r.got.time && r.got.space; }).length;
    var timeRight = results.filter(function (r) { return r.got.time; }).length;
    var spaceRight = results.filter(function (r) { return r.got.space; }).length;

    el('scoreBig').textContent = marks + ' / ' + total;
    el('scoreSub').textContent = S.scoreSub(pct, clean, results.length, timeRight, spaceRight);
    for (var i = 0; i < S.verdicts.length; i++) {
      if (marks / (total || 1) >= S.verdicts[i][0]) { el('scoreWord').textContent = S.verdicts[i][1]; break; }
    }

    renderBreakdown(results);
    renderMissed(results);
  }

  function renderBreakdown(results) {
    var byTopic = {};
    results.forEach(function (r) {
      var t = byTopic[r.question.topic] || (byTopic[r.question.topic] = { n: 0, time: 0, space: 0 });
      t.n += 1;
      if (r.got.time) t.time += 1;
      if (r.got.space) t.space += 1;
    });

    var rows = Object.keys(byTopic).map(function (topic) {
      var t = byTopic[topic];
      return { topic: topic, t: t, rate: (t.time + t.space) / (t.n * 2) };
    }).sort(function (a, b) { return a.rate - b.rate; });   // weakest first — that is the to-do list

    el('breakdownBody').innerHTML = rows.map(function (row) {
      return '<tr><td>' + esc(topicLabel(row.topic)) + '</td>'
        + '<td class="num">' + row.t.time + '/' + row.t.n + '</td>'
        + '<td class="num">' + row.t.space + '/' + row.t.n + '</td>'
        + '<td><span class="bar"><span style="width:' + Math.round(row.rate * 100) + '%"></span></span></td></tr>';
    }).join('');
  }

  function renderMissed(results) {
    var missed = results.filter(function (r) { return !r.got.time || !r.got.space; });
    show('missedPanel', missed.length > 0);
    el('retryBtn').hidden = missed.length === 0;

    el('missedList').innerHTML = missed.map(function (r) {
      var q = r.question;
      var parts = [];
      if (!r.got.time) parts.push(S.missedTime + ' <b>' + esc(q.time) + '</b> — ' + prose(q.cost.time.why));
      if (!r.got.space) parts.push(S.missedSpace + ' <b>' + esc(q.space) + '</b> — ' + prose(q.cost.space.why));
      var name = titleText(q);
      return '<div class="missed-item">'
        + '<div class="missed-head"><span class="name">'
        + (q.links.lc ? '<a href="' + esc(q.links.lc) + '" target="_blank" rel="noopener">' + esc(name) + '</a>' : esc(name))
        + '</span><span class="badge">' + esc(topicLabel(q.topic)) + '</span>'
        + (q.pattern ? '<span class="badge pattern">' + esc(q.pattern) + '</span>' : '')
        + '</div>'
        + parts.map(function (p) { return '<div class="missed-detail">' + p + '</div>'; }).join('')
        + '</div>';
    }).join('');
  }

  // ── Export ─────────────────────────────────────────────────────────────
  //
  // The report is gone the moment the tab closes, and nothing here is stored
  // server-side. A download is the record: JSON to read back in a script, CSV
  // to open in a spreadsheet next to the last attempt.

  function pad2(n) { return (n < 10 ? '0' : '') + n; }

  function localStamp(d) {
    return d.getFullYear() + '-' + pad2(d.getMonth() + 1) + '-' + pad2(d.getDate())
      + 'T' + pad2(d.getHours()) + ':' + pad2(d.getMinutes()) + ':' + pad2(d.getSeconds());
  }

  // One plain object per answered question — the shape both formats share.
  function resultRows(results) {
    return results.map(function (r, i) {
      var q = r.question;
      return {
        n: i + 1,
        id: q.id,
        lc: q.lc,
        title: q.title,
        difficulty: q.difficulty,
        topic: q.topic,
        pattern: q.pattern || '',
        yourTime: r.said.time.trim(),
        yourSpace: r.said.space.trim(),
        time: q.time,
        space: q.space,
        timeCorrect: !!r.got.time,
        spaceCorrect: !!r.got.space
      };
    });
  }

  function exportPayload() {
    var results = run.results;
    var timeRight = results.filter(function (r) { return r.got.time; }).length;
    var spaceRight = results.filter(function (r) { return r.got.space; }).length;
    var total = results.length * 2;
    return {
      quiz: 'complexity',
      lang: LANG,
      takenAt: localStamp(run.startedAt),
      set: run.set,
      focus: run.focus,
      asked: run.questions.length,
      answered: results.length,
      score: {
        marks: timeRight + spaceRight,
        total: total,
        percent: total ? Math.round((timeRight + spaceRight) / total * 100) : 0,
        fullyRight: results.filter(function (r) { return r.got.time && r.got.space; }).length,
        time: timeRight,
        space: spaceRight
      },
      results: resultRows(results)
    };
  }

  var CSV_COLUMNS = ['n', 'id', 'lc', 'title', 'difficulty', 'topic', 'pattern',
    'yourTime', 'yourSpace', 'time', 'space', 'timeCorrect', 'spaceCorrect'];

  // RFC 4180: quote a field holding a comma, quote or newline, doubling quotes.
  function csvField(value) {
    var text = value === null || value === undefined ? '' : String(value);
    return /[",\r\n]/.test(text) ? '"' + text.replace(/"/g, '""') + '"' : text;
  }

  function toCsv(payload) {
    var lines = [CSV_COLUMNS.join(',')];
    payload.results.forEach(function (row) {
      lines.push(CSV_COLUMNS.map(function (c) { return csvField(row[c]); }).join(','));
    });
    return lines.join('\r\n') + '\r\n';
  }

  function fileName(payload, ext) {
    return 'complexity-quiz-' + payload.takenAt.replace(/:/g, '').replace('T', '-') + '-' + payload.set + '.' + ext;
  }

  function download(name, type, text) {
    var blob = new Blob([text], { type: type });
    var url = URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = name;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    setTimeout(function () { URL.revokeObjectURL(url); }, 0);
  }

  function exportAs(format) {
    var payload = exportPayload();
    if (format === 'json') {
      download(fileName(payload, 'json'), 'application/json', JSON.stringify(payload, null, 2) + '\n');
    } else {
      // The BOM is what makes Excel read the file as UTF-8 — without it a 繁中
      // pattern name opens as mojibake.
      download(fileName(payload, 'csv'), 'text/csv;charset=utf-8', '\ufeff' + toCsv(payload));
    }
  }

  // ── Wiring ─────────────────────────────────────────────────────────────

  function wire() {
    el('countChips').addEventListener('click', function (event) {
      var chip = event.target.closest('[data-count]');
      if (!chip || chip.disabled) return;
      var value = chip.getAttribute('data-count');
      prefs.count = value === 'all' ? 'all' : parseInt(value, 10);
      savePrefs();
      renderSetup();
    });

    el('packSelect').addEventListener('change', function () {
      prefs.pack = savedPack = el('packSelect').value;
      savePrefs();
      renderTopics();
      renderSetup();
    });

    el('topicSelect').addEventListener('change', function () {
      prefs.topic = savedTopic = el('topicSelect').value;
      savePrefs();
      renderSetup();
    });

    el('startBtn').addEventListener('click', startFromSetup);
    el('checkBtn').addEventListener('click', check);
    el('nextBtn').addEventListener('click', next);
    el('quitBtn').addEventListener('click', function () {
      // Ending early still scores what was answered; an empty run just goes back.
      if (run.results.length) finish();
      else { show('quiz', false); show('setup', true); }
    });
    el('retryBtn').addEventListener('click', function () {
      startQuiz(shuffled(run.results
        .filter(function (r) { return !r.got.time || !r.got.space; })
        .map(function (r) { return r.question; })));
    });
    el('exportJsonBtn').addEventListener('click', function () { exportAs('json'); });
    el('exportCsvBtn').addEventListener('click', function () { exportAs('csv'); });
    el('restartBtn').addEventListener('click', function () {
      show('report', false);
      show('setup', true);
      renderSetup();
    });

    // Typing clears that box's "could not read" message.
    ['time', 'space'].forEach(function (field) {
      el(field + 'Input').addEventListener('input', function () { el(field + 'Msg').innerHTML = ''; });
    });

    // Hovering one dimension's reason dims every line it does not point at.
    ['mouseover', 'focusin'].forEach(function (type) {
      el('verdict').addEventListener(type, function (event) {
        var dim = event.target.closest('[data-dim]');
        el('qCode').classList.remove('focus-t', 'focus-s');
        if (dim) el('qCode').classList.add('focus-' + dim.getAttribute('data-dim'));
      });
    });
    el('verdict').addEventListener('mouseleave', function () {
      el('qCode').classList.remove('focus-t', 'focus-s');
    });

    // Enter checks, then Enter again moves on — so a whole quiz is typeable
    // without reaching for the mouse.
    document.addEventListener('keydown', function (event) {
      if (event.key !== 'Enter' || el('quiz').hidden) return;
      if (event.target === el('timeInput') && !run.checked && !el('spaceInput').value.trim()) {
        el('spaceInput').focus();
        return;
      }
      event.preventDefault();
      if (run.checked) next();
      else check();
    });
  }

  // ── Boot ───────────────────────────────────────────────────────────────

  function boot(data) {
    bank = data.questions || [];
    topicLabels = data.topicLabels || {};
    // A bank built before packs existed carries none: treat it all as classic.
    bank.forEach(function (q) { if (!q.pack) q.pack = 'classic'; });
    packs = data.packs && data.packs.length ? data.packs : ['classic'];
    loadPrefs();
    if (sets().indexOf(prefs.pack) === -1) prefs.pack = packs[0];
    savedPack = prefs.pack;
    savedTopic = prefs.topic;
    prefs.pack = packFromUrl() || prefs.pack;

    renderPacks();
    renderTopics();

    show('loading', false);
    show('setup', true);
    wire();
    renderSetup();
  }

  el('loading').textContent = S.loading;
  fetch(DATA_URL)
    .then(function (res) { return res.json(); })
    .then(boot)
    .catch(function () {
      el('loading').textContent = S.loadFailed;
    });

  // Exposed for site/test/quiz-page.test.js, which checks both tables carry
  // the same keys.
  // exportPayload / toCsv are reached through it too, so the test can read
  // what a download would hold without a real file.
  window.CSQuiz = {
    STRINGS: STRINGS,
    lineRange: lineRange,
    exportPayload: function () { return exportPayload(); },
    toCsv: toCsv
  };
})();
