const test = require('node:test');
const assert = require('node:assert/strict');

const CSComplexity = require('../complexity.js');
const { normalize, matches, grade } = CSComplexity;

// The grader's whole job is to accept every spelling of one answer and no
// spelling of a different one, so the tests come in those two shapes:
// `same` for things that must collapse together, `differ` for things that
// must not.

function same(canonical, spellings) {
  const expected = normalize(canonical);
  assert.notEqual(expected, null, `${canonical} did not parse at all`);
  for (const spelling of spellings) {
    assert.equal(normalize(spelling), expected, `${spelling} should equal ${canonical}`);
  }
}

function differ(...answers) {
  const seen = new Map();
  for (const answer of answers) {
    const form = normalize(answer);
    assert.notEqual(form, null, `${answer} did not parse at all`);
    assert.ok(!seen.has(form), `${answer} collided with ${seen.get(form)}`);
    seen.set(form, answer);
  }
}

// ── Spellings that must collapse ──────────────────────────────────────────

test('accepts the ways people write n log n', () => {
  same('O(n log n)', [
    'O(nlogn)', 'n log n', 'O(N log N)', 'O(N·logN)', 'Θ(n lg n)',
    'O(n * log(n))', 'O( n  log  n )', 'O(n ln n)', 'O(log(n) * n)',
    'O(n log2 n)', 'linearithmic', 'O(n log n) average', 'o(nlogn)',
  ]);
});

test('multiplication is commutative and the separator is optional', () => {
  same('O(m * n)', ['O(n * m)', 'O(mn)', 'O(m·n)', 'O(n×m)', 'O(m*n) time']);
});

test('powers survive every notation', () => {
  same('O(n^2)', ['O(n²)', 'O(n*n)', 'O(n**2)', 'quadratic', 'O(N^2)']);
  same('O(n^3)', ['O(n³)', 'O(n*n*n)', 'cubic']);
});

test('a symbolic exponent stays symbolic', () => {
  same('O(2^n)', ['exponential', 'O(2**n)', 'O(2^N)']);
  same('O(n * 2^n)', ['O(2^n * n)', 'O(2^n·n)']);
});

test('constants collapse and a lone +1 term drops out', () => {
  same('O(1)', ['constant', 'O(1) amortized', 'O(2)', 'O(1) per operation']);
  same('O(n)', ['O(n + 1)', 'O(2n)', 'linear', 'O(n) expected']);
});

test('sums are order-insensitive', () => {
  same('O(V + E)', ['O(E + V)', 'O(v+e)', 'O(E) + O(V)']);
});

test('a product over a sum is distributed', () => {
  same('O((n + m) log n)', ['O(n log n + m log n)', 'O(log n * (m + n))']);
});

test('hedges, units and trailing glosses are ignored', () => {
  same('O(n log k)', [
    'O(n log k) where k is the heap size',
    'O(n log k), amortized',
    'worst case O(n log k)',
  ]);
});

// ── Distinctions that must survive ────────────────────────────────────────

test('the answers a candidate confuses stay distinct', () => {
  differ('O(1)', 'O(log n)', 'O(n)', 'O(n log n)', 'O(n^2)', 'O(n^3)', 'O(2^n)', 'O(n!)');
});

test('a sum is not a product and the variables are not interchangeable', () => {
  differ('O(m + n)', 'O(m * n)', 'O(m)', 'O(n)');
  differ('O(n log k)', 'O(n log n)', 'O(k log n)');
});

test('log n squared is not log of n squared… but n^2 log n is its own thing', () => {
  differ('O(log^2 n)', 'O(log n)', 'O(n^2 log n)');
});

test('E log V and E log E are different bounds', () => {
  // The `log e` here is log applied to the edge count, not a natural-log base.
  differ('O(E log V)', 'O(E log E)', 'O(V log E)');
});

// ── Rejection ─────────────────────────────────────────────────────────────

test('answers that are not expressions are rejected, not guessed at', () => {
  for (const junk of ['', '   ', 'no idea', '???', 'O(', 'n +', 'n log', '(n))']) {
    assert.equal(normalize(junk), null, `${JSON.stringify(junk)} should be rejected`);
  }
});

test('an over-long paste is rejected rather than parsed', () => {
  assert.equal(normalize('O(' + 'n*'.repeat(200) + 'n)'), null);
});

// ── matches() and grade() ─────────────────────────────────────────────────

test('matches accepts the canonical answer and the listed alternatives', () => {
  assert.ok(matches('O(h)', 'O(h)', ['O(n)']));
  assert.ok(matches('O(n)', 'O(h)', ['O(n)']));
  assert.ok(matches('  o(N)  ', 'O(h)', ['O(n)']));
  assert.ok(!matches('O(log n)', 'O(h)', ['O(n)']));
  assert.ok(!matches('', 'O(h)', ['O(n)']));
});

test('matches works with no alternatives at all', () => {
  assert.ok(matches('nlogn', 'O(n log n)'));
  assert.ok(!matches('O(n)', 'O(n log n)'));
});

test('grade scores time and space independently', () => {
  const question = { time: 'O(n)', space: 'O(1)', accept: { space: ['O(n)'] } };
  assert.deepEqual(grade({ time: 'O(n)', space: 'O(1)' }, question), { time: true, space: true });
  assert.deepEqual(grade({ time: 'O(n)', space: 'O(n)' }, question), { time: true, space: true });
  assert.deepEqual(grade({ time: 'O(n^2)', space: 'O(1)' }, question), { time: false, space: true });
  assert.deepEqual(grade({ time: '', space: '' }, question), { time: false, space: false });
});

test('grade tolerates a question with no accept block', () => {
  assert.deepEqual(grade({ time: 'O(n)', space: 'O(1)' }, { time: 'O(n)', space: 'O(1)' }),
    { time: true, space: true });
});

// ── Evaluation (the Big-O chart) ──────────────────────────────────────────
//
// lc-big-o.html plots and ranks typed expressions with compile() + log2Eval(),
// so the numbers it prints are only as right as these.

function ops(expr, n) {
  const c = CSComplexity.compile(expr);
  assert.ok(c, `${expr} did not compile`);
  return Math.pow(2, CSComplexity.log2Eval(c.ast, n));
}

function near(actual, expected, rel = 1e-9) {
  assert.ok(Math.abs(actual - expected) <= rel * Math.max(1, Math.abs(expected)),
    `${actual} is not ${expected}`);
}

test('log2Eval gives the textbook values for the nine classes', () => {
  near(ops('O(1)', 1e6), 1);
  near(ops('O(log n)', 1024), 10);
  near(ops('O(√n)', 1e6), 1000);
  near(ops('O(n)', 1e6), 1e6);
  near(ops('O(n log n)', 1024), 10240);
  near(ops('O(n²)', 1000), 1e6);
  near(ops('O(n^3)', 100), 1e6);
  near(ops('O(2^n)', 20), 1048576);
  near(ops('O(n!)', 10), 3628800, 1e-12);
});

test('log2Eval stays finite where the value itself would overflow', () => {
  const fact = CSComplexity.compile('n!');
  const exp = CSComplexity.compile('2^n');
  // 2^5000 and 5000! are far past Number.MAX_VALUE; their logs are not.
  near(CSComplexity.log2Eval(exp.ast, 5000), 5000);
  const lf = CSComplexity.log2Eval(fact.ast, 5000);
  assert.ok(Number.isFinite(lf) && lf > 5000, 'n! must outgrow 2^n');
});

test('log2Eval handles sums, products, nested logs and zeros', () => {
  near(ops('n + n^2', 10), 110);
  near(ops('n * 2^n', 10), 10240);
  near(ops('n log^2 n', 16), 256);
  near(ops('m + n', 10), 20);                         // every variable is n
  assert.equal(CSComplexity.log2Eval(CSComplexity.compile('log n').ast, 1), -Infinity);
  assert.equal(CSComplexity.log2Eval(CSComplexity.compile('n log n').ast, 1), -Infinity);
});

test('compile keys equal spellings of one curve the same way', () => {
  assert.equal(CSComplexity.compile('O(N·logN)').key, CSComplexity.compile('n log n').key);
  assert.equal(CSComplexity.compile('dunno'), null);
});

test('nested symbolic powers keep their grouping', () => {
  // Both used to render as n^2^n and compare equal.
  differ('O((n^2)^n)', 'O(n^(2^n))');
  differ('O((n+1)^n)', 'O(n+1^n)');
  same('O(2^n)', ['O(2^(n))', 'O((2)^n)']);
});

// ── Dominated terms, max/min, division, log rules ─────────────────────────

test('a provably dominated term drops out', () => {
  same('O(n log n)', ['O(n log n + n)', 'O(n + n log n)', 'O(n log n) + O(n)', 'O(nlogn + log n)']);
  same('O(n^2)', ['O(n^2 + n)', 'O(n + n^2 + 1)', 'O(n^2 + n log n)']);
  same('O(m * n)', ['O(m*n + m + n)', 'O(mn + n)']);
  same('O(2^n)', ['O(2^n + n^3)']);
  same('O(n * 2^n)', ['O(n * 2^n + 2^n)']);
  same('O(n!)', ['O(n! + 2^n)']);
});

test('dominance is per variable, so incomparable terms survive', () => {
  differ('O(m + n^2)', 'O(n^2)', 'O(m)');
  differ('O(n + k log n)', 'O(n)', 'O(k log n)');
  differ('O(V + E)', 'O(E)', 'O(V)');
  differ('O(E log V)', 'O(E log V + V)');
  // A factor the grader cannot rank only cancels against itself.
  differ('O(log(m + n))', 'O(log m)');
});

test('max, min and division read the way people write them', () => {
  same('O(m + n)', ['O(max(m, n))', 'O(max(n,m))']);
  same('O(min(m, n))', ['O(min(n, m))']);
  differ('O(min(m, n))', 'O(m + n)', 'O(m * n)');
  same('O(n^2)', ['O(n^2 / 2)', 'O(n(n+1)/2)']);
  same('O(n)', ['O(n / k * k)']);
  assert.equal(normalize('O(n / (m + k))'), null, 'division by a sum is rejected, not mangled');
});

test('a log of a product or power simplifies', () => {
  same('O(log n)', ['O(log(n^2))', 'O(log(2n))']);
  same('O(log m + log n)', ['O(log(m*n))', 'O(log(mn))']);
  same('O(n)', ['O(log(2^n))']);
  same('O(n log n)', ['O(log(n!))']);
  differ('O(log log n)', 'O(log n)');
});

test('a forgotten closing paren is forgiven', () => {
  same('O(n log n)', ['O(n log n', 'O(n * log(n']);
  same('O(n)', ['O(n), amortized']);
});

test('compare says how a wrong answer misses', () => {
  const { compare } = CSComplexity;
  assert.equal(compare('O(n^2)', 'O(n log n)'), 'over');
  assert.equal(compare('O(n)', 'O(n log n)'), 'under');
  assert.equal(compare('O(k)', 'O(n)'), 'different');
  assert.equal(compare('O(n + n log n)', 'O(n log n)'), 'equal');
  assert.equal(compare('O(m * n)', 'O(m + n)'), 'over');
  assert.equal(compare('dunno', 'O(n)'), null);
});

test('variables lists the symbols an answer uses', () => {
  assert.deepEqual(CSComplexity.variables('O(E log V)'), ['e', 'v']);
  assert.deepEqual(CSComplexity.variables('O(1)'), []);
  assert.equal(CSComplexity.variables('???'), null);
});

test('fractional exponents and square roots are one notation', () => {
  same('O(n^1.5)', ['O(n * sqrt(n))', 'O(n sqrt n)', 'O(n^(3/2))', 'O(n√n)']);
  same('O(sqrt(n))', ['O(n^0.5)', 'O(n^(1/2))', 'O(√n)']);
  differ('O(n^1.5)', 'O(n^15)', 'O(n^2)', 'O(n)');
  same('O(4^n / sqrt(n))', ['O(4^n / n^0.5)', 'O(4^n / √n)']);
  differ('O(4^n / sqrt(n))', 'O(4^n)');
  same('O(n^2)', ['O(n^2 + n^1.5)']);
});

test('review regressions: quotients in a log, constants beside a reciprocal, zero powers', () => {
  differ('O(log(n/k))', 'O(log(n*k))');
  differ('O(1 + n/k)', 'O(n/k)');
  same('O(1)', ['O(n^0)', 'O((m*n)^0)']);
  same('O(n)', ['O(n + 1)']);
});
