const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');

const { answerStrings, resolveQuestion, buildQuiz } = require('../build-quiz.js');
const { parseReadmeProblems } = require('../build-roadmap.js');
const CSComplexity = require('../complexity.js');

const ROOT = path.join(__dirname, '..', '..');

// A stand-in README, so the unit tests do not move whenever a row in the real
// one does. The corpus test at the bottom is what holds the real file.
const README = parseReadmeProblems([
  '## Array',
  '| 1 | [Two Sum](https://leetcode.com/problems/two-sum/) |'
    + ' [Python](./leetcode_python/Array/two-sum.py) | _O(n)_ | _O(n)_ | Easy | `blind75` | OK |',
].join('\n'));

function question(overrides) {
  return Object.assign({
    id: 'sample',
    lc: 1,
    topic: 'Arrays & Hashing',
    code: ['def f(nums):', '    return len(nums)'],
    time: 'O(n)',
    space: 'O(1)',
    why: 'One pass.',
  }, overrides);
}

// ── answerStrings ─────────────────────────────────────────────────────────

test('answerStrings collects both fields and every alternative', () => {
  assert.deepEqual(
    answerStrings(question({ accept: { time: ['O(n log n)'], space: ['O(n)', 'O(log n)'] } })),
    ['O(n)', 'O(1)', 'O(n log n)', 'O(n)', 'O(log n)']
  );
});

test('answerStrings works with no accept block', () => {
  assert.deepEqual(answerStrings(question()), ['O(n)', 'O(1)']);
});

// ── resolveQuestion ───────────────────────────────────────────────────────

test('an LC question takes its title, difficulty and links from README', () => {
  const resolved = resolveQuestion(question(), README);
  assert.equal(resolved.title, 'Two Sum');
  assert.equal(resolved.difficulty, 'Easy');
  assert.equal(resolved.links.lc, 'https://leetcode.com/problems/two-sum/');
  assert.match(resolved.links.repo, /leetcode_python\/Array\/two-sum\.py$/);
});

test('code lines are joined into one snippet', () => {
  assert.equal(resolveQuestion(question(), README).code, 'def f(nums):\n    return len(nums)');
});

test('accept always resolves to a pair of arrays', () => {
  assert.deepEqual(resolveQuestion(question(), README).accept, { time: [], space: [] });
  assert.deepEqual(
    resolveQuestion(question({ accept: { space: ['O(n)'] } }), README).accept,
    { time: [], space: ['O(n)'] }
  );
});

test('a non-LC question carries its own title and difficulty', () => {
  const resolved = resolveQuestion(question({
    id: 'drill', lc: null, title: 'Doubling loop', difficulty: 'Easy',
  }), README);
  assert.equal(resolved.title, 'Doubling loop');
  assert.equal(resolved.difficulty, 'Easy');
  assert.deepEqual(resolved.links, {});
});

test('a non-LC question without its own title is rejected', () => {
  assert.throws(
    () => resolveQuestion(question({ lc: null, difficulty: 'Easy' }), README),
    /must set its own title/
  );
});

test('an lc number README does not know is rejected', () => {
  assert.throws(() => resolveQuestion(question({ lc: 99999 }), README), /not in README/);
});

test('a missing required field is rejected, and named', () => {
  for (const field of ['id', 'topic', 'code', 'time', 'space', 'why']) {
    assert.throws(
      () => resolveQuestion(question({ [field]: undefined }), README),
      new RegExp(`missing a ${field}`),
      `${field} should be required`
    );
  }
});

test('an unparseable answer is rejected wherever it appears', () => {
  // This is the guard that matters: "O(amount * n)" reads as a product of six
  // one-letter variables, so nothing a user could type would ever match it.
  assert.throws(() => resolveQuestion(question({ time: 'O(amount * n)' }), README), /cannot parse/);
  assert.throws(() => resolveQuestion(question({ space: 'linear-ish' }), README), /cannot parse/);
  assert.throws(
    () => resolveQuestion(question({ accept: { time: ['O(n)', 'no idea'] } }), README),
    /cannot parse/
  );
});

// ── buildQuiz ─────────────────────────────────────────────────────────────

test('buildQuiz lists the distinct topics, sorted', () => {
  const built = buildQuiz({ questions: [
    question({ id: 'a', topic: 'Heap' }),
    question({ id: 'b', topic: 'Arrays & Hashing' }),
    question({ id: 'c', topic: 'Heap' }),
  ] }, README);
  assert.deepEqual(built.topics, ['Arrays & Hashing', 'Heap']);
  assert.equal(built.questions.length, 3);
});

test('buildQuiz rejects a duplicate id', () => {
  assert.throws(
    () => buildQuiz({ questions: [question(), question()] }, README),
    /duplicate question id "sample"/
  );
});

test('buildQuiz rejects an empty bank', () => {
  assert.throws(() => buildQuiz({ questions: [] }, README), /no questions/);
});

// ── The real bank ─────────────────────────────────────────────────────────

test('data/complexity_quiz.json builds against the real README', () => {
  const bank = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.json'), 'utf8'));
  const readme = parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8'));
  const built = buildQuiz(bank, readme);

  assert.ok(built.questions.length >= 25, 'the bank should be big enough to draw 25 without repeats');
  for (const q of built.questions) {
    assert.ok(q.title, `${q.id} has no title`);
    assert.ok(q.code.includes('\n'), `${q.id} has a one-line snippet`);
  }
});

test('no question in the bank accepts an answer that contradicts its own', () => {
  // An `accept` entry that normalises to the canonical answer is dead weight;
  // one that normalises to a *different* topic answer would make the question
  // unfailable. Both are worth catching before they ship.
  const bank = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.json'), 'utf8'));
  for (const q of bank.questions) {
    for (const field of ['time', 'space']) {
      const alts = (q.accept && q.accept[field]) || [];
      const canonical = CSComplexity.normalize(q[field]);
      const seen = new Set([canonical]);
      for (const alt of alts) {
        const form = CSComplexity.normalize(alt);
        assert.ok(!seen.has(form), `${q.id}: accepted ${field} "${alt}" repeats an answer it already takes`);
        seen.add(form);
      }
    }
  }
});

test('the questions the roadmap-style drills lean on stay in the bank', () => {
  // The three Python traps are the reason this page exists rather than a
  // markdown drill sheet; losing them silently would gut it.
  const bank = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.json'), 'utf8'));
  const ids = new Set(bank.questions.map(q => q.id));
  for (const id of ['string-concat-loop', 'pop-front-loop', 'list-membership-loop', 'binary-search-slicing']) {
    assert.ok(ids.has(id), `${id} is missing from the bank`);
  }
});
