/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — Big-O answer normaliser and grader

   The complexity quiz asks for a *typed* answer rather than offering a menu,
   because recognising "O(n log n)" in a list is a different (and much easier)
   skill than producing it. That only works if the grader accepts every
   spelling a human actually writes:

     O(n log n)   O(nlogn)   O(N·logN)   n log n   O(n * log(n))   Θ(n lg n)

   …all mean the same thing, and none of them should cost a point.

   The approach is to parse the answer into a small expression tree and expand
   it to a canonical sum-of-products, so equivalence is a string compare:

     "O(m*n)"        -> "m*n"          "O(n*m)"      -> "m*n"
     "O(n log n)"    -> "log(n)*n"     "O(nlogn)"    -> "log(n)*n"
     "O((n+m)log n)" -> "log(n)*m+log(n)*n"
     "O(n^2)"        -> "n^2"          "O(n*n)"      -> "n^2"

   Constant factors are dropped (Big-O), and a lone `+1` term is dropped
   (`O(n+1)` is `O(n)`), but *dominated* terms are kept: `O(n + n^2)` does not
   collapse, because once there are two variables (`O(m + n^2)`) dominance is
   not decidable and a grader that guesses is worse than one that says no.
   Genuinely debatable answers are handled per question by an `accept` list in
   data/complexity_quiz.json, not by loosening the algebra here.

   Loaded both by site/build-quiz.js (to validate the bank at build time) and
   by lc-complexity-quiz.html (to grade in the browser), so the answers a build
   accepts and the answers a user may type are the same set by construction.
   ───────────────────────────────────────────────────────────────────────── */
(function (root, factory) {
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.CSComplexity = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // No honest complexity answer is longer than this; a runaway paste is
  // rejected rather than parsed, so the expander can never be handed a bomb.
  var MAX_INPUT = 160;

  var SUPERSCRIPT = {
    '⁰': '0', '¹': '1', '²': '2', '³': '3', '⁴': '4',
    '⁵': '5', '⁶': '6', '⁷': '7', '⁸': '8', '⁹': '9'
  };

  // Prose answers, in the order they must be tried: "linearithmic" before
  // "linear", or the longer word loses its tail to the shorter rule.
  var WORD_FORMS = [
    [/\bconstants?\b/g, '1'],
    [/\blinearithmic\b/g, 'n log n'],
    [/\blog[-\s]*linear\b/g, 'n log n'],
    [/\blinear\b/g, 'n'],
    [/\bquadratic\b/g, 'n^2'],
    [/\bcubic\b/g, 'n^3'],
    [/\blogarithmic\b/g, 'log n'],
    [/\bexponential\b/g, '2^n'],
    [/\bfactorial\b/g, 'n!'],
    // Only as whole words: once whitespace is stripped, "lgn" is genuinely
    // ambiguous with the variables l and g, so it is left alone.
    [/\blg\b/g, 'log'],
    [/\bln\b/g, 'log']
  ];

  // Hedges and units that carry no algebra. Only multi-letter words are
  // stripped — a single letter is always a variable, never noise.
  var NOISE = new RegExp('\\b(?:amortized|amortised|average|avg|expected|worst|best|case'
    + '|time|space|complexity|about|approximately|approx|roughly|per|operation|operations'
    + '|ops|the|total|overall|extra|auxiliary|steps|calls)\\b', 'g');

  // ── Lexing ────────────────────────────────────────────────────────────────

  /**
   * Rewrites a human answer into bare algebra: prose to symbols, `O(` and its
   * relatives to plain parens, whitespace gone.
   *
   * Dropping the `O(` head rather than unwrapping it is deliberate — it leaves
   * the parens balanced, so "O(n) + O(m)" parses as readily as "O(n + m)".
   */
  function preprocess(raw) {
    var s = String(raw == null ? '' : raw);
    if (s.length > MAX_INPUT) return '';
    s = s.toLowerCase();

    // A trailing gloss ("O(n log k) where k is the heap size") is commentary.
    s = s.split(/\bwhere\b/)[0];

    s = s.replace(/[⁰¹²³⁴⁵⁶⁷⁸⁹]+/g, function (run) {
      return '^' + run.split('').map(function (ch) { return SUPERSCRIPT[ch]; }).join('');
    });
    s = s.replace(/[₀-₉]/g, '');                 // log₂ n → log n
    s = s.replace(/[×·⋅∙]/g, '*');
    s = s.replace(/√/g, 'sqrt');
    s = s.replace(/\*\*/g, '^');                 // Python's power operator

    WORD_FORMS.forEach(function (rule) { s = s.replace(rule[0], rule[1]); });
    s = s.replace(NOISE, ' ');
    // An explicit base is noise — but only when it is written attached to the
    // `log`. A detached "log e" is `log` applied to the variable e, which in a
    // graph answer (O(E log E)) is exactly what was meant.
    s = s.replace(/\blog_\s*(?:2|10|e)\b/g, 'log');
    s = s.replace(/\blog(?:2|10)\b/g, 'log');

    // The `\b` before `o` is what keeps this from eating the o in "log(".
    s = s.replace(/\b(?:big[-\s]*o|bigo|o|theta|omega)\s*\(/g, '(');
    // Θ and Ω are not word characters, so `\b` never matches beside them.
    s = s.replace(/[θω]\s*\(/g, '(');
    s = s.replace(/[.,;:=]/g, ' ');
    return s.replace(/\s+/g, '');
  }

  // Identifiers are single letters (so "mn" reads as m*n), which on its own
  // would make any word a valid product — "dunno" would parse as d*n^2*o*u,
  // and an authored answer of "O(amount * n)" would silently become
  // a*m*n^2*o*t*u and pass the build as gradeable. No real answer runs more
  // than three bare variables together, so a longer run is prose, not algebra.
  var MAX_LETTER_RUN = 3;

  function tokenize(s) {
    var tokens = [];
    var letterRun = 0;
    var i = 0;
    while (i < s.length) {
      var ch = s.charAt(i);
      if (ch >= '0' && ch <= '9') {
        var j = i;
        while (j < s.length && /[0-9.]/.test(s.charAt(j))) j++;
        tokens.push({ t: 'num', v: parseFloat(s.slice(i, j)) });
        letterRun = 0;
        i = j;
        continue;
      }
      if (s.substr(i, 4) === 'sqrt') { tokens.push({ t: 'sqrt' }); letterRun = 0; i += 4; continue; }
      if (s.substr(i, 3) === 'log') { tokens.push({ t: 'log' }); letterRun = 0; i += 3; continue; }
      // "nlogn" is n*log(n) — the spelling people type when they skip the
      // operators — but "noidea" is not a six-variable product.
      if (/[a-zα-ω]/.test(ch)) {
        if (++letterRun > MAX_LETTER_RUN) return null;
        tokens.push({ t: 'var', v: ch });
        i++;
        continue;
      }
      if ('+*^()!'.indexOf(ch) >= 0) { tokens.push({ t: ch }); letterRun = 0; i++; continue; }
      return null;
    }
    return tokens;
  }

  // ── Parsing ───────────────────────────────────────────────────────────────
  //
  //   expr    := term ('+' term)*
  //   term    := power (('*' | ε) power)*      -- ε is implicit multiplication
  //   power   := postfix ('^' power)?          -- right associative
  //   postfix := atom '!'*
  //   atom    := number | var | '(' expr ')' | ('log'|'sqrt') ('^' power)? postfix

  function parse(tokens) {
    var pos = 0;

    function peek() { return tokens[pos]; }
    function eat(t) {
      if (tokens[pos] && tokens[pos].t === t) { pos++; return true; }
      return false;
    }
    function startsFactor() {
      var tk = peek();
      if (!tk) return false;
      return tk.t === 'num' || tk.t === 'var' || tk.t === 'log' || tk.t === 'sqrt' || tk.t === '(';
    }

    function expr() {
      var first = term();
      if (!first) return null;
      var xs = [first];
      while (eat('+')) {
        var next = term();
        if (!next) return null;
        xs.push(next);
      }
      return xs.length === 1 ? xs[0] : { t: 'add', xs: xs };
    }

    function term() {
      var first = power();
      if (!first) return null;
      var xs = [first];
      for (;;) {
        var explicit = eat('*');
        if (!explicit && !startsFactor()) break;
        var next = power();
        if (!next) return null;
        xs.push(next);
      }
      return xs.length === 1 ? xs[0] : { t: 'mul', xs: xs };
    }

    function power() {
      var base = postfix();
      if (!base) return null;
      if (!eat('^')) return base;
      var exponent = power();
      if (!exponent) return null;
      return { t: 'pow', b: base, e: exponent };
    }

    function postfix() {
      var node = atom();
      if (!node) return null;
      while (eat('!')) node = { t: 'fact', x: node };
      return node;
    }

    function atom() {
      var tk = peek();
      if (!tk) return null;
      if (tk.t === 'num') { pos++; return { t: 'num', v: tk.v }; }
      if (tk.t === 'var') { pos++; return { t: 'var', v: tk.v }; }
      if (tk.t === '(') {
        pos++;
        var inner = expr();
        if (!inner || !eat(')')) return null;
        return inner;
      }
      if (tk.t === 'log' || tk.t === 'sqrt') {
        pos++;
        // `log^2 n` — the exponent sits on the function, not on its argument.
        var exponent = null;
        if (eat('^')) {
          exponent = power();
          if (!exponent) return null;
        }
        var arg = postfix();
        if (!arg) return null;
        var node = { t: tk.t, x: arg };
        return exponent ? { t: 'pow', b: node, e: exponent } : node;
      }
      return null;
    }

    var ast = expr();
    if (!ast || pos !== tokens.length) return null;
    return ast;
  }

  // ── Canonical form ────────────────────────────────────────────────────────
  //
  // A term is `{ coef, f }` where `f` maps a factor key to its exponent, so
  // n * n * log n is `{ coef: 1, f: { n: 2, 'log(n)': 1 } }`.

  function unitTerm() { return { coef: 1, f: {} }; }

  function multiply(a, b) {
    var out = [];
    for (var i = 0; i < a.length; i++) {
      for (var j = 0; j < b.length; j++) {
        var f = {};
        var key;
        for (key in a[i].f) f[key] = a[i].f[key];
        for (key in b[j].f) f[key] = (f[key] || 0) + b[j].f[key];
        out.push({ coef: a[i].coef * b[j].coef, f: f });
      }
    }
    return out;
  }

  function single(key) {
    var f = {};
    f[key] = 1;
    return [{ coef: 1, f: f }];
  }

  // Repeating the expansion is only worth it for the exponents that show up in
  // real answers; anything larger stays symbolic (and `2^n` always does).
  var MAX_EXPAND = 6;

  function expand(node) {
    switch (node.t) {
      case 'num': return [{ coef: node.v, f: {} }];
      case 'var': return single(node.v);
      case 'add': return node.xs.reduce(function (acc, x) { return acc.concat(expand(x)); }, []);
      case 'mul': return node.xs.reduce(function (acc, x) { return multiply(acc, expand(x)); }, [unitTerm()]);
      case 'fact': return single(canonical(node.x) + '!');
      case 'log': return single('log(' + canonical(node.x) + ')');
      case 'sqrt': return single('sqrt(' + canonical(node.x) + ')');
      case 'pow': return expandPow(node);
      default: return [unitTerm()];
    }
  }

  function expandPow(node) {
    var e = expand(node.e);
    var constant = e.length === 1 && Object.keys(e[0].f).length === 0 ? e[0].coef : null;
    if (constant !== null && constant >= 0 && constant <= MAX_EXPAND && constant === Math.floor(constant)) {
      var acc = [unitTerm()];
      var base = expand(node.b);
      for (var i = 0; i < constant; i++) acc = multiply(acc, base);
      return acc;
    }
    return single(canonical(node.b) + '^' + canonical(node.e));
  }

  function render(terms, keepCoef) {
    var seen = Object.create(null);
    var out = [];
    terms.forEach(function (term) {
      var parts = Object.keys(term.f).sort().map(function (key) {
        return term.f[key] === 1 ? key : key + '^' + term.f[key];
      });
      var coef = keepCoef ? term.coef : 1;
      if (coef !== 1 || parts.length === 0) parts.unshift(String(coef));
      var text = parts.join('*');
      if (!seen[text]) { seen[text] = true; out.push(text); }
    });
    out.sort();
    // O(n + 1) is O(n): a constant term never survives beside another one.
    if (out.length > 1) out = out.filter(function (text) { return text !== '1'; });
    return out.join('+');
  }

  function canonical(node) { return render(expand(node), true); }

  /**
   * Canonical form of a complexity answer, or null when it is not an
   * expression at all ("dunno", "n squared-ish"). Null is a *rejected*
   * answer — never one that quietly passes.
   */
  function normalize(raw) {
    var text = preprocess(raw);
    if (!text) return null;
    var tokens = tokenize(text);
    if (!tokens || !tokens.length) return null;
    var ast = parse(tokens);
    if (!ast) return null;
    return render(expand(ast), false);
  }

  function equivalent(a, b) {
    var left = normalize(a);
    return left !== null && left === normalize(b);
  }

  /** True when `input` matches `expected` or any of the also-accepted answers. */
  function matches(input, expected, accepted) {
    var got = normalize(input);
    if (got === null) return false;
    if (got === normalize(expected)) return true;
    var alts = accepted || [];
    for (var i = 0; i < alts.length; i++) {
      if (got === normalize(alts[i])) return true;
    }
    return false;
  }

  /** Grades one question's two fields. `answer` is `{ time, space }` as typed. */
  function grade(answer, question) {
    var accept = question.accept || {};
    return {
      time: matches(answer.time, question.time, accept.time),
      space: matches(answer.space, question.space, accept.space)
    };
  }

  return {
    MAX_INPUT: MAX_INPUT,
    preprocess: preprocess,
    tokenize: tokenize,
    parse: parse,
    normalize: normalize,
    equivalent: equivalent,
    matches: matches,
    grade: grade
  };
});
