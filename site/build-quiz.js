#!/usr/bin/env node

/**
 * Build the complexity-quiz question bank.
 *
 *   data/complexity_quiz.json  (hand-authored snippets and answers)
 * + README.md                  (the repo's master problem index)
 * → _site/data/complexity-quiz.json
 *
 * As with the roadmap, a question carries only the LeetCode number; its title,
 * difficulty and links to this repo's solutions are resolved from README at
 * build time so they live in exactly one place.
 *
 * Everything inconsistent is a hard failure rather than a dropped question: an
 * answer the grader cannot parse would be unscoreable no matter what the user
 * typed, and an lc number README does not know would render as a blank card.
 *
 * Run via site/build.sh; exported helpers are unit-tested in site/test.
 */

const fs = require('fs');
const path = require('path');

const { parseReadmeProblems } = require('./build-roadmap.js');
const CSComplexity = require('./complexity.js');

const ROOT = path.join(__dirname, '..');
const LC_URL = 'https://leetcode.com/problems/';

// ── Validation ───────────────────────────────────────────────────────────────

/**
 * Every answer string a question can be graded against — the canonical one and
 * every alternative it also accepts.
 */
function answerStrings(question) {
  const accept = question.accept || {};
  return [question.time, question.space]
    .concat(accept.time || [], accept.space || []);
}

/**
 * Resolves one authored question against README, or throws explaining why it
 * cannot be shown.
 *
 * `readme` is the Map from parseReadmeProblems, keyed by the id as a string.
 */
function resolveQuestion(question, readme) {
  const where = `question "${question.id || '(missing id)'}"`;

  for (const field of ['id', 'topic', 'code', 'time', 'space', 'why']) {
    if (!question[field] || !question[field].length) {
      throw new Error(`${where} is missing a ${field}`);
    }
  }
  if (!Array.isArray(question.code)) {
    throw new Error(`${where} has a code field that is not an array of lines`);
  }

  // `accept: { time: "O(n)" }` instead of `["O(n)"]` would survive validation —
  // `concat` folds a bare string in as one element — and then reach the page,
  // where rendering the feedback calls `.map()` on it and throws.
  for (const field of ['time', 'space']) {
    const alternatives = question.accept && question.accept[field];
    if (alternatives !== undefined && !Array.isArray(alternatives)) {
      throw new Error(`${where} has an accept.${field} that is not an array`);
    }
  }

  for (const answer of answerStrings(question)) {
    if (CSComplexity.normalize(answer) === null) {
      // Either a typo or a shape the grader has never seen. Both mean nobody
      // could have scored a point on it.
      throw new Error(`${where} has an answer the grader cannot parse: "${answer}"`);
    }
  }

  const resolved = {
    id: question.id,
    lc: question.lc == null ? null : question.lc,
    topic: question.topic,
    vars: question.vars || '',
    note: question.note || '',
    code: question.code.join('\n'),
    time: question.time,
    space: question.space,
    accept: {
      time: (question.accept && question.accept.time) || [],
      space: (question.accept && question.accept.space) || []
    },
    why: question.why,
    trap: question.trap || '',
    links: {}
  };

  if (question.lc == null) {
    // A pure algorithm or Python drill: it has no README row to borrow from,
    // so it has to carry its own label.
    for (const field of ['title', 'difficulty']) {
      if (!question[field]) {
        throw new Error(`${where} has no lc number, so it must set its own ${field}`);
      }
    }
    resolved.title = question.title;
    resolved.difficulty = question.difficulty;
    return resolved;
  }

  const problem = readme.get(String(question.lc));
  if (!problem) {
    throw new Error(`${where} points at LC ${question.lc}, which is not in README.md`);
  }
  // README is the single source for these, so an authored one is not merely
  // redundant — it is a second copy free to drift. Rejecting beats ignoring:
  // a silently-dropped title is exactly the failure this builder exists to
  // make loud.
  for (const field of ['title', 'difficulty']) {
    if (question[field]) {
      throw new Error(`${where} sets its own ${field}, but LC ${question.lc} takes that from README.md`);
    }
  }
  resolved.title = problem.title;
  resolved.difficulty = problem.difficulty || 'Medium';
  resolved.links.lc = problem.url || LC_URL;
  if (problem.solutions && problem.solutions.Python) {
    resolved.links.repo = problem.solutions.Python;
  }
  return resolved;
}

/** Resolves the whole bank, rejecting duplicate ids. */
function buildQuiz(bank, readme) {
  const questions = (bank && bank.questions) || [];
  if (!questions.length) throw new Error('data/complexity_quiz.json has no questions');

  const seen = new Set();
  const resolved = questions.map(question => {
    if (seen.has(question.id)) {
      // Two questions with one id would collide in the per-question state the
      // page keeps, so one of them would silently take the other's answer.
      throw new Error(`duplicate question id "${question.id}"`);
    }
    seen.add(question.id);
    return resolveQuestion(question, readme);
  });

  const topics = [...new Set(resolved.map(q => q.topic))].sort();
  return { topics: topics, questions: resolved };
}

// ── Entry point ──────────────────────────────────────────────────────────────

function main() {
  const bank = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.json'), 'utf8'));
  const readme = parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8'));
  const built = buildQuiz(bank, readme);

  const outDir = path.join(ROOT, '_site/data');
  fs.mkdirSync(outDir, { recursive: true });
  fs.writeFileSync(path.join(outDir, 'complexity-quiz.json'), JSON.stringify(built));

  console.log(`✓ Created _site/data/complexity-quiz.json (${built.questions.length} questions,`
    + ` ${built.topics.length} topics)`);

  // A topic with one question cannot fill a topic-filtered quiz, and that only
  // shows up here.
  const counts = new Map();
  for (const q of built.questions) counts.set(q.topic, (counts.get(q.topic) || 0) + 1);
  const summary = [...counts.entries()].sort((a, b) => b[1] - a[1])
    .map(([topic, n]) => `${topic} ${n}`).join(', ');
  console.log(`    ${summary}`);
}

if (require.main === module) {
  try {
    main();
  } catch (err) {
    console.error(`❌ ${err.message}`);
    process.exit(1);
  }
}

module.exports = { answerStrings, resolveQuestion, buildQuiz };
