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
