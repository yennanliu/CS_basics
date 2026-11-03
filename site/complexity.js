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

   Constant factors are dropped (Big-O), and so is any term that is
   *provably* dominated by another: `O(n + n log n)` is `O(n log n)`,
   `O(m*n + n)` is `O(m*n)`, `O(2^n + n^3)` is `O(2^n)`. "Provably" is the
   point — the test is per variable, so `O(m + n^2)` keeps both terms (neither
   bounds the other for every m and n), and a factor the grader cannot rank
   (`log(m+n)`, `min(m,n)`) only cancels against an identical one. A grader
   that guesses is worse than one that says no. See `dominated()`.

   It also reads `max(m, n)` (as `m + n`, the same bound), `min(m, n)`, and
   division (`n^2 / 2`, `n / k`), simplifies a log of a product or power
   (`log(n^2)` is `log n`, `log(n!)` is `n log n`), and closes the parens a
   typist forgot (`O(n log n`).

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
    // Commas are kept for max(a, b) / min(a, b); tokenize() drops the rest.
    // A decimal point survives (n^1.5); any other full stop is punctuation.
    s = s.replace(/(\d)\.(\d)/g, '$1\u0000$2');
    s = s.replace(/[.;:=]/g, ' ').replace(/\u0000/g, '.');
    s = s.replace(/\s+/g, '');
    // A forgotten closing paren is a typo, not a different answer.
    var open = (s.match(/\(/g) || []).length - (s.match(/\)/g) || []).length;
    for (var k = 0; k < open; k++) s += ')';
    return s;
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
    // One flag per open paren: does it belong to max( / min(? A comma is an
    // argument separator only there; anywhere else it is punctuation
    // ("O(n), amortized") and is dropped.
    var parens = [];
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
      if ((s.substr(i, 4) === 'max(' || s.substr(i, 4) === 'min(')) {
        tokens.push({ t: s.substr(i, 3) });
        tokens.push({ t: '(' });
        parens.push(true);
        letterRun = 0;
        i += 4;
        continue;
      }
      if (ch === ',') {
        if (parens.length && parens[parens.length - 1]) tokens.push({ t: ',' });
        letterRun = 0;
        i++;
        continue;
      }
      if (ch === '(') parens.push(false);
      else if (ch === ')') parens.pop();
      // "nlogn" is n*log(n) — the spelling people type when they skip the
      // operators — but "noidea" is not a six-variable product.
      if (/[a-zα-ω]/.test(ch)) {
        if (++letterRun > MAX_LETTER_RUN) return null;
        tokens.push({ t: 'var', v: ch });
        i++;
        continue;
      }
      if ('+*/^()!'.indexOf(ch) >= 0) { tokens.push({ t: ch }); letterRun = 0; i++; continue; }
      return null;
    }
    return tokens;
  }

  // ── Parsing ───────────────────────────────────────────────────────────────
  //
  //   expr    := term ('+' term)*
  //   term    := power (('*' | '/' | ε) power)* -- ε is implicit multiplication
  //   power   := postfix ('^' power)?          -- right associative
  //   postfix := atom '!'*
  //   atom    := number | var | '(' expr ')' | ('log'|'sqrt') ('^' power)? postfix
  //            | ('max'|'min') '(' expr (',' expr)* ')'

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
      return tk.t === 'num' || tk.t === 'var' || tk.t === 'log' || tk.t === 'sqrt'
        || tk.t === 'max' || tk.t === 'min' || tk.t === '(';
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
        var divide = eat('/');
        var explicit = divide || eat('*');
        if (!explicit && !startsFactor()) break;
        var next = power();
        if (!next) return null;
        xs.push(divide ? { t: 'inv', x: next } : next);
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
      if (tk.t === 'max' || tk.t === 'min') {
        pos++;
        if (!eat('(')) return null;
        var args = [];
        do {
          var a = expr();
          if (!a) return null;
          args.push(a);
        } while (eat(','));
        if (!eat(')')) return null;
        return args.length === 1 ? args[0] : { t: tk.t, xs: args };
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
        for (key in b[j].f) {
          f[key] = (f[key] || 0) + b[j].f[key];
          if (f[key] === 0) delete f[key];       // n / n is 1, not n^0
        }
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
      case 'log': return expandLog(node);
      // max(a, b) and a + b are the same bound, up to a factor of two.
      case 'max': return node.xs.reduce(function (acc, x) { return acc.concat(expand(x)); }, []);
      case 'min': return single('min(' + node.xs.map(canonical).sort().join(',') + ')');
      case 'inv': return invert(expand(node.x));
      case 'sqrt': return monomialPow(expand(node.x), 0.5) || single('sqrt(' + canonical(node.x) + ')');
      case 'pow': return expandPow(node);
      default: return [unitTerm()];
    }
  }

  // Division by a monomial (`n / 2`, `n^2 / k`) is multiplication by its
  // reciprocal. Dividing by a sum has no sum-of-products form, so the answer is
  // rejected rather than mangled.
  function Unreadable() {}

  function invert(terms) {
    if (terms.length !== 1 || terms[0].coef === 0) throw new Unreadable();
    var f = {};
    for (var key in terms[0].f) f[key] = -terms[0].f[key];
    return [{ coef: 1 / terms[0].coef, f: f }];
  }

  // log of a product is a sum of logs, and a power comes down as a constant:
  // log(n^2) = 2 log n, log(m*n) = log m + log n, log(2^n) = n, log(n!) ~ n log n.
  // Anything else (log of a sum, log log n) stays a symbolic factor.
  function expandLog(node) {
    var inner = expand(node.x);
    if (inner.length !== 1) return single('log(' + canonical(node.x) + ')');
    var keys = Object.keys(inner[0].f);
    if (!keys.length) return [unitTerm()];                // log of a constant
    var out = [];
    keys.forEach(function (key) {
      var e = inner[0].f[key];
      var m;
      if (/^[a-zα-ω]$/.test(key)) out = out.concat(scale(single('log(' + key + ')'), e));
      else if ((m = /^\d+(?:\.\d+)?\^([a-zα-ω])$/.exec(key))) out = out.concat(scale(single(m[1]), e));
      else if ((m = /^([a-zα-ω])!$/.exec(key))) {
        var nlogn = {};
        nlogn[m[1]] = 1;
        nlogn['log(' + m[1] + ')'] = 1;
        out.push({ coef: e, f: nlogn });
      } else out = out.concat(scale(single('log(' + key + ')'), e));
    });
    return out;
  }

  function scale(terms, c) {
    return terms.map(function (t) { return { coef: t.coef * c, f: t.f }; });
  }

  // A single term to a constant power: (n^2 k)^1.5 = n^3 k^1.5. That is what
  // makes sqrt(n), n^0.5 and n^(1/2) one answer, and n*sqrt(n) equal n^1.5.
  function monomialPow(terms, c) {
    if (terms.length !== 1) return null;
    var f = {};
    for (var key in terms[0].f) f[key] = terms[0].f[key] * c;
    return [{ coef: Math.pow(terms[0].coef, c), f: f }];
  }

  function expandPow(node) {
    var e = expand(node.e);
    var constant = e.length === 1 && Object.keys(e[0].f).length === 0 ? e[0].coef : null;
    if (constant !== null) {
      var raised = monomialPow(expand(node.b), constant);
      if (raised) return raised;
    }
    if (constant !== null && constant >= 0 && constant <= MAX_EXPAND && constant === Math.floor(constant)) {
      var acc = [unitTerm()];
      var base = expand(node.b);
      for (var i = 0; i < constant; i++) acc = multiply(acc, base);
      return acc;
    }
    return single(group(canonical(node.b)) + '^' + group(canonical(node.e)));
  }

  // A composite operand of a symbolic power keeps its parentheses, or
  // (n^2)^n and n^(2^n) — wildly different growth — would both render as
  // n^2^n and compare equal. Simple operands (n, 2, log(n), n!) stay bare,
  // so the everyday keys (2^n, 4^l, k^n) are unchanged.
  function group(text) {
    var depth = 0;
    for (var i = 0; i < text.length; i++) {
      var ch = text.charAt(i);
      if (ch === '(') depth++;
      else if (ch === ')') depth--;
      else if (depth === 0 && (ch === '+' || ch === '*' || ch === '^')) return '(' + text + ')';
    }
    return text;
  }

  // ── Dominance ───────────────────────────────────────────────────────────
  //
  // Term A is dominated by term B when A <= c·B for *every* assignment of the
  // variables, so dropping A cannot change the bound. That is decided per
  // variable: each term is read as, for each variable v, a growth profile
  //
  //     v!  >  c^v (larger c first)  >  poly power of v  >  power of log v
  //
  // and A <= B when every variable's profile in A is at most its profile in B.
  // A factor that cannot be ranked this way — log(m+n), min(m,n), k^n with a
  // variable base — has to appear in B at least as often as in A. Anything
  // with a negative exponent (from division) is left alone entirely.

  var VAR = /^[a-zα-ω]$/;

  function profile(term) {
    var vars = Object.create(null);
    var other = Object.create(null);
    function v(name) {
      // fact: the power of v!; exp: log2 of the c in c^v; p, q: as above.
      return vars[name] || (vars[name] = { fact: 0, exp: 0, p: 0, q: 0 });
    }
    for (var key in term.f) {
      var e = term.f[key];
      if (e < 0) return null;
      var m;
      if (VAR.test(key)) v(key).p += e;
      else if ((m = /^log\(([a-zα-ω])\)$/.exec(key))) v(m[1]).q += e;
      else if ((m = /^sqrt\(([a-zα-ω])\)$/.exec(key))) v(m[1]).p += e / 2;
      else if ((m = /^([a-zα-ω])!$/.exec(key))) v(m[1]).fact += e;
      else if ((m = /^(\d+(?:\.\d+)?)\^([a-zα-ω])$/.exec(key)) && parseFloat(m[1]) > 1) {
        v(m[2]).exp += e * Math.log2(parseFloat(m[1]));
      } else other[key] = e;
    }
    return { vars: vars, other: other };
  }

  var NONE = { fact: 0, exp: 0, p: 0, q: 0 };

  // Is variable profile a <= b? Lexicographic: the faster-growing part decides.
  function varLeq(a, b) {
    var order = ['fact', 'exp', 'p', 'q'];
    for (var i = 0; i < order.length; i++) {
      var k = order[i];
      if (Math.abs(a[k] - b[k]) > 1e-9) return a[k] < b[k];
    }
    return true;
  }

  function leq(a, b) {
    for (var key in a.other) if (!(b.other[key] >= a.other[key])) return false;
    var names = Object.keys(a.vars).concat(Object.keys(b.vars));
    for (var i = 0; i < names.length; i++) {
      if (!varLeq(a.vars[names[i]] || NONE, b.vars[names[i]] || NONE)) return false;
    }
    return true;
  }

  /** The terms of `terms` that no other term bounds. Order is preserved. */
  function dominated(terms) {
    var profiles = terms.map(profile);
    if (profiles.some(function (p) { return p === null; })) return terms;
    return terms.filter(function (term, i) {
      for (var j = 0; j < terms.length; j++) {
        if (j === i || !leq(profiles[i], profiles[j])) continue;
        // Two terms that bound each other are the same growth; keep the first.
        if (!leq(profiles[j], profiles[i]) || j < i) return false;
      }
      return true;
    });
  }

  function render(terms, keepCoef) {
    if (!keepCoef) terms = dominated(terms);
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
  function read(raw) {
    var text = preprocess(raw);
    if (!text) return null;
    var tokens = tokenize(text);
    if (!tokens || !tokens.length) return null;
    var ast = parse(tokens);
    if (!ast) return null;
    try {
      var terms = expand(ast);
      return { ast: ast, terms: terms, key: render(terms, false) };
    } catch (err) {
      if (err instanceof Unreadable) return null;
      throw err;
    }
  }

  function normalize(raw) {
    var r = read(raw);
    return r ? r.key : null;
  }

  /**
   * How a wrong answer relates to the right one, so the feedback can say more
   * than "no": 'equal', 'over' (a true upper bound, but not tight), 'under'
   * (too optimistic), 'different' (neither bounds the other — usually the
   * wrong variable), or null when either side does not parse.
   */
  function compare(said, expected) {
    var a = read(said);
    var b = read(expected);
    if (!a || !b) return null;
    if (a.key === b.key) return 'equal';
    var aTerms = dominated(a.terms).map(profile);
    var bTerms = dominated(b.terms).map(profile);
    if (aTerms.concat(bTerms).some(function (p) { return p === null; })) return 'different';
    function covered(xs, ys) {
      return xs.every(function (x) { return ys.some(function (y) { return leq(x, y); }); });
    }
    var under = covered(aTerms, bTerms);
    var over = covered(bTerms, aTerms);
    if (under && over) return 'equal';
    if (under) return 'under';
    if (over) return 'over';
    return 'different';
  }

  /** The single-letter variables an answer uses, sorted. */
  function variables(raw) {
    var r = read(raw);
    if (!r) return null;
    var found = Object.create(null);
    (function walk(node) {
      if (!node) return;
      if (node.t === 'var') found[node.v] = true;
      [node.x, node.b, node.e].forEach(walk);
      (node.xs || []).forEach(walk);
    })(r.ast);
    return Object.keys(found).sort();
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

  // ── Evaluation ────────────────────────────────────────────────────────────
  //
  // The Big-O chart (lc-big-o.html) plots the same expressions the quiz
  // grades, so it evaluates the same tree rather than a second parser.
  //
  // Everything is computed as log2 of the value. n! at n = 10^6 is a number
  // with 5.5 million digits, and 2^n leaves double range at n = 1024; in log
  // space both are ordinary floats, and "how many operations" comparisons are
  // still exact in order. A value of 0 (log 1, a literal 0) is -Infinity.
  //
  // Every variable takes the same value: on a chart whose x-axis is n,
  // O(m + n) is drawn as O(n). Logarithms are base 2, which is the base the
  // interview arithmetic uses (log2 10^6 ≈ 20); Big-O does not care.

  var LN2 = Math.LN2;

  // Lanczos approximation of ln Γ(z), good to ~15 digits for z > 0.
  var LANCZOS = [0.99999999999980993, 676.5203681218851, -1259.1392167224028,
    771.32342877765313, -176.61502916214059, 12.507343278686905,
    -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7];
  function lnGamma(z) {
    if (z < 0.5) return Math.log(Math.PI / Math.sin(Math.PI * z)) - lnGamma(1 - z);
    z -= 1;
    var x = LANCZOS[0];
    for (var i = 1; i < 9; i++) x += LANCZOS[i] / (z + i);
    var t = z + 7.5;
    return 0.5 * Math.log(2 * Math.PI) + (z + 0.5) * Math.log(t) - t + Math.log(x);
  }

  // log2(2^a + 2^b) without leaving log space.
  function log2Add(a, b) {
    if (a === -Infinity) return b;
    if (b === -Infinity) return a;
    var hi = Math.max(a, b);
    var lo = Math.min(a, b);
    if (hi === Infinity) return Infinity;
    return hi + Math.log2(1 + Math.pow(2, lo - hi));
  }

  /** log2 of the expression's value with every variable set to `x` (> 0). */
  function log2Eval(node, x) {
    switch (node.t) {
      case 'num': return node.v > 0 ? Math.log2(node.v) : -Infinity;
      case 'var': return Math.log2(x);
      case 'add': return node.xs.reduce(function (acc, k) { return log2Add(acc, log2Eval(k, x)); }, -Infinity);
      case 'mul': {
        var sum = 0;
        for (var i = 0; i < node.xs.length; i++) {
          var part = log2Eval(node.xs[i], x);
          if (part === -Infinity) return -Infinity;   // anything times 0 is 0
          sum += part;
        }
        return sum;
      }
      case 'pow': {
        var base = log2Eval(node.b, x);
        var exponent = Math.pow(2, log2Eval(node.e, x));
        if (exponent === 0) return 0;                  // b^0 = 1
        if (base === -Infinity) return -Infinity;      // 0^e = 0
        return exponent * base;
      }
      case 'sqrt': return log2Eval(node.x, x) / 2;
      case 'inv': return -log2Eval(node.x, x);
      case 'max': return node.xs.reduce(function (acc, k) { return Math.max(acc, log2Eval(k, x)); }, -Infinity);
      case 'min': return node.xs.reduce(function (acc, k) { return Math.min(acc, log2Eval(k, x)); }, Infinity);
      case 'log': {
        // log2(v) is the inner exponent itself; a log at or below 0 is 0 work.
        var inner = log2Eval(node.x, x);
        return inner > 0 ? Math.log2(inner) : -Infinity;
      }
      case 'fact': {
        var v = Math.pow(2, log2Eval(node.x, x));
        if (v === Infinity) return Infinity;
        return lnGamma(v + 1) / LN2;
      }
      default: return NaN;
    }
  }

  /**
   * Parses a typed complexity for plotting: `{ ast, key }`, or null when it is
   * not an expression. `key` is the canonical form, so "n log n" and
   * "O(N·logN)" are recognised as the same curve.
   */
  function compile(raw) {
    var r = read(raw);
    return r ? { ast: r.ast, key: r.key } : null;
  }

  return {
    MAX_INPUT: MAX_INPUT,
    preprocess: preprocess,
    tokenize: tokenize,
    parse: parse,
    normalize: normalize,
    equivalent: equivalent,
    matches: matches,
    compare: compare,
    variables: variables,
    grade: grade,
    compile: compile,
    log2Eval: log2Eval
  };
});
