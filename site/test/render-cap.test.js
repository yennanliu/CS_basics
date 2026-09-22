/**
 * GitHub renders only the first 512,000 bytes of a markdown file. Past that it
 * stops mid-element and says nothing — no notice, no truncation warning, the
 * tables simply end.
 *
 * README.md crossed that line and nobody noticed for months. At 1,137,734 bytes
 * the cut fell inside the LC 1480 row, so 1,929 of the 3,287 problem rows and
 * every heading after them were not on the repo's front page at all, and
 * find-in-page failed on two thirds of the index without any sign that it had.
 *
 * That is why the index moved to PROBLEMS.md and README.md became a landing
 * page. `site/build-site.js` fails the build on the same rule; this pins it here
 * too, so `npm test` catches it without a full build.
 *
 * PROBLEMS.md is deliberately exempt: it is over the cap by design, says so in
 * its own header, and the site renders it in full at problems.html.
 */
const test = require('node:test');
const assert = require('node:assert');
const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..', '..');
const GITHUB_RENDER_CAP = 512000;

test('README.md renders in full on GitHub', () => {
  const size = fs.statSync(path.join(ROOT, 'README.md')).size;
  assert.ok(
    size <= GITHUB_RENDER_CAP,
    `README.md is ${size.toLocaleString('en-US')} bytes; GitHub renders only the first ` +
    `${GITHUB_RENDER_CAP.toLocaleString('en-US')} and drops the rest with no warning`
  );
});

test('the problem index is still where the readers expect it', () => {
  const { PROBLEM_INDEX } = require('../build-roadmap.js');
  assert.equal(PROBLEM_INDEX, 'PROBLEMS.md');
  assert.ok(fs.existsSync(path.join(ROOT, PROBLEM_INDEX)));
});

// The index cannot render on GitHub, so its header has to say so — a reader who
// lands there and uses find-in-page gets no other warning.
test('the problem index warns that GitHub truncates it', () => {
  const head = fs.readFileSync(path.join(ROOT, 'PROBLEMS.md'), 'utf8').slice(0, 2000);
  assert.match(head, /512,000 bytes/);
  assert.match(head, /problems\.html/);
});

// The README's job after the move is to route a reader to the index. A front
// page that forgets to mention it is the failure this whole change was about.
test('README.md points at the index and at the site search', () => {
  const readme = fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8');
  assert.match(readme, /PROBLEMS\.md/);
  assert.match(readme, /search\.html/);
});
