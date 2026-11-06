#!/usr/bin/env node

/**
 * Build the complexity-quiz question bank.
 *
 *   data/complexity_quiz.json  (hand-authored snippets and answers)
 * + data/complexity_quiz.zh.json (the 繁體中文 prose, keyed by question id)
 * + README.md                  (the repo's master problem index)
 * → _site/data/complexity-quiz.json      (English page)
 * → _site/data/complexity-quiz.zh.json   (繁體中文 page — same questions, zh prose)
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

// The question sets the page's "Question set" dropdown offers. A question with
// no `pack` is `classic` — the original bank, and what the page shows by
// default. `essentials` is the opt-in set of interview staples whose bound is
// easy to get wrong (amortised stacks, output-sensitive backtracking, a log over
// the answer range rather than n). The order here is the dropdown's order.
const PACKS = ['classic', 'essentials'];

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

  for (const field of ['id', 'topic', 'code', 'time', 'space']) {
    if (!question[field] || !question[field].length) {
      throw new Error(`${where} is missing a ${field}`);
    }
  }
  if (!question.cost || typeof question.cost !== 'object') {
    throw new Error(`${where} is missing a cost`);
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

  // `cost` is what the page highlights: for each bound, the lines that set it
  // and one sentence on why. A line number past the snippet would highlight
  // nothing and leave the reason pointing at air.
  for (const field of ['time', 'space']) {
    const cost = question.cost[field];
    if (!cost || !Array.isArray(cost.lines) || typeof cost.why !== 'string' || !cost.why.trim()) {
      throw new Error(`${where} needs cost.${field} as { lines: [...], why: "..." }`);
    }
    for (const line of cost.lines) {
      if (!Number.isInteger(line) || line < 1 || line > question.code.length) {
        throw new Error(`${where} has cost.${field} line ${line}, outside its ${question.code.length}-line snippet`);
      }
      if (!question.code[line - 1].trim()) {
        throw new Error(`${where} has cost.${field} line ${line}, which is blank`);
      }
    }
  }

  const pack = question.pack === undefined ? 'classic' : question.pack;
  if (!PACKS.includes(pack)) {
    // A typo'd pack would put the question in a set the page never offers.
    throw new Error(`${where} has pack "${pack}", which is not one of ${PACKS.join(', ')}`);
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
    pack: pack,
    vars: question.vars || '',
    note: question.note || '',
    code: question.code.join('\n'),
    time: question.time,
    space: question.space,
    accept: {
      time: (question.accept && question.accept.time) || [],
      space: (question.accept && question.accept.space) || []
    },
    cost: {
      time: { lines: question.cost.time.lines.slice(), why: question.cost.time.why },
      space: { lines: question.cost.space.lines.slice(), why: question.cost.space.why }
    },
    why: question.why || '',
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

// ── 繁體中文 ─────────────────────────────────────────────────────────────────

// The prose fields a translation carries. Everything else — code, answers,
// accept lists, cost line numbers — is read from the English question, so the
// two pages always grade the same thing.
const ZH_FIELDS = ['title', 'vars', 'note', 'why', 'trap'];

/**
 * Composes the zh bank from the built English one and the overlay. Fails on a
 * translation for an id the bank does not have (a renamed question), a
 * question with no translation, a field the English question does not carry
 * (a translated note for a question that lost its note), or one it carries
 * that the translation skipped — the same coverage rule the cheatsheets are
 * gated on, because a half-English explanation is what this exists to stop.
 */
function buildZh(built, overlay) {
  const entries = (overlay && overlay.questions) || {};
  const topics = (overlay && overlay.topics) || {};
  const ids = new Set(built.questions.map(q => q.id));
  for (const id of Object.keys(entries)) {
    if (!ids.has(id)) throw new Error(`complexity_quiz.zh.json translates "${id}", which is not in the bank`);
  }
  for (const topic of built.topics) {
    if (!topics[topic]) throw new Error(`complexity_quiz.zh.json has no name for the topic "${topic}"`);
  }

  const patternNames = (overlay && overlay.patterns) || {};
  const englishPatterns = new Set(built.questions.map(q => q.pattern).filter(Boolean));
  for (const name of englishPatterns) {
    if (!patternNames[name]) throw new Error(`complexity_quiz.zh.json has no name for the pattern "${name}"`);
  }
  for (const name of Object.keys(patternNames)) {
    if (!englishPatterns.has(name)) throw new Error(`complexity_quiz.zh.json names a pattern "${name}" the bank does not have`);
  }

  const questions = built.questions.map(q => {
    const where = `complexity_quiz.zh.json "${q.id}"`;
    const zh = entries[q.id];
    if (!zh) throw new Error(`${where} is missing — every question needs a translation`);
    for (const field of ZH_FIELDS) {
      // title is only ever authored for a question with no lc number.
      const english = field === 'title' ? (q.lc == null ? q.title : '') : q[field];
      if (english && !zh[field]) throw new Error(`${where} has no ${field}`);
      if (!english && zh[field]) throw new Error(`${where} translates a ${field} the English question does not have`);
    }
    const cost = zh.cost || {};
    for (const field of ['time', 'space']) {
      if (!cost[field]) throw new Error(`${where} has no cost.${field}`);
    }
    for (const text of [zh.vars, zh.note, zh.why, zh.trap, cost.time, cost.space]) {
      if (text && (text.match(/`/g) || []).length % 2) {
        throw new Error(`${where} has an unbalanced \` in "${text}"`);
      }
    }
    return Object.assign({}, q, {
      title: zh.title || q.title,
      pattern: q.pattern ? patternNames[q.pattern] : '',
      vars: zh.vars,
      note: zh.note || '',
      why: zh.why || '',
      trap: zh.trap || '',
      cost: {
        time: { lines: q.cost.time.lines, why: cost.time },
        space: { lines: q.cost.space.lines, why: cost.space }
      }
    });
  });

  return { topics: built.topics, packs: built.packs, topicLabels: topics, questions: questions };
}

/**
 * Reads the bank's `patterns` block — { topic: { pattern name: question id } }
 * — into a Map from question id to pattern name, or throws on the first thing
 * that would make the Pattern coverage set lie about what it covers: an id the
 * bank does not have, an id filed under a topic it is not in, a question named
 * for two patterns, or a topic with questions and no patterns at all.
 */
function resolvePatterns(patterns, questions) {
  // A bank with no block has no Pattern coverage set (the unit tests' small
  // banks); site/test/quiz-page.test.js pins that the real bank carries one.
  if (patterns === undefined) return new Map();
  if (!patterns || typeof patterns !== 'object') throw new Error('data/complexity_quiz.json has a patterns block that is not an object');
  const byId = new Map(questions.map(q => [q.id, q]));
  const byQuestion = new Map();
  const names = new Set();
  for (const [topic, entries] of Object.entries(patterns)) {
    for (const [name, id] of Object.entries(entries || {})) {
      const where = `pattern "${name}" (${topic})`;
      const question = byId.get(id);
      if (!question) throw new Error(`${where} points at "${id}", which is not in the bank`);
      if (question.topic !== topic) {
        throw new Error(`${where} points at "${id}", whose topic is "${question.topic}"`);
      }
      if (byQuestion.has(id)) throw new Error(`${where} reuses "${id}", already "${byQuestion.get(id)}"`);
      if (names.has(name)) throw new Error(`${where} is named twice`);
      byQuestion.set(id, name);
      names.add(name);
    }
  }
  for (const topic of new Set(questions.map(q => q.topic))) {
    if (!Object.keys((patterns[topic]) || {}).length) {
      throw new Error(`topic "${topic}" has questions but no patterns`);
    }
  }
  return byQuestion;
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

  // The pattern a question represents, or '' — the page's Pattern coverage set
  // is every question with one, so it cuts across the classic / essentials packs.
  const patternOf = resolvePatterns(bank.patterns, resolved);
  for (const q of resolved) q.pattern = patternOf.get(q.id) || '';

  const topics = [...new Set(resolved.map(q => q.topic))].sort();
  // Only the packs that actually have questions, in PACKS order — the page
  // offers no dropdown entry that would draw from an empty pool.
  const packs = PACKS.filter(p => resolved.some(q => q.pack === p));
  return { topics: topics, packs: packs, questions: resolved };
}

// ── Entry point ──────────────────────────────────────────────────────────────

/** Builds both banks from data/ and README into _site/data/, and prints the per-topic and per-set counts. */
function main() {
  const bank = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.json'), 'utf8'));
  const readme = parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8'));
  const built = buildQuiz(bank, readme);
  const overlay = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/complexity_quiz.zh.json'), 'utf8'));
  const zh = buildZh(built, overlay);

  const outDir = path.join(ROOT, '_site/data');
  fs.mkdirSync(outDir, { recursive: true });
  fs.writeFileSync(path.join(outDir, 'complexity-quiz.json'), JSON.stringify(built));
  fs.writeFileSync(path.join(outDir, 'complexity-quiz.zh.json'), JSON.stringify(zh));

  console.log(`✓ Created _site/data/complexity-quiz.json (${built.questions.length} questions,`
    + ` ${built.topics.length} topics) and complexity-quiz.zh.json`);

  // A topic with one question cannot fill a topic-filtered quiz, and that only
  // shows up here.
  const counts = new Map();
  for (const q of built.questions) counts.set(q.topic, (counts.get(q.topic) || 0) + 1);
  const summary = [...counts.entries()].sort((a, b) => b[1] - a[1])
    .map(([topic, n]) => `${topic} ${n}`).join(', ');
  console.log(`    ${summary}`);
  const byPack = built.packs.map(p => `${p} ${built.questions.filter(q => q.pack === p).length}`).join(', ');
  console.log(`    packs: ${byPack}, pattern coverage ${built.questions.filter(q => q.pattern).length}`);
}

if (require.main === module) {
  try {
    main();
  } catch (err) {
    console.error(`❌ ${err.message}`);
    process.exit(1);
  }
}

module.exports = { PACKS, answerStrings, resolveQuestion, resolvePatterns, buildQuiz, buildZh };
