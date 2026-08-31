#!/usr/bin/env node
/**
 * Drop the doc images that no generated page references.
 *
 * build.sh copies doc/pic wholesale, because that is the only way to have the
 * images in place before the pages that reference them exist. The result was an
 * 82 MB directory of which 62 MB was never referenced by anything the site
 * serves — images that belong to the repo's markdown as read on GitHub, or to
 * sheets that have since been rewritten, shipped to every visitor's CDN edge and
 * counted against the Pages artifact on every deploy.
 *
 * So: copy everything, build, then delete what nothing points at. Deleting from
 * _site/ only — doc/pic in the repo is untouched, because those files are still
 * correct for anyone reading the markdown on GitHub.
 */
const fs = require('fs');
const path = require('path');

const SITE = process.argv[2] || '_site';
const PIC_DIR = path.join(SITE, 'doc', 'pic');

if (!fs.existsSync(PIC_DIR)) {
  console.log('✓ No _site/doc/pic to prune');
  process.exit(0);
}

function walk(dir, test, acc = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) walk(full, test, acc);
    else if (test(entry.name)) acc.push(full);
  }
  return acc;
}

// Referenced from any attribute on any page — src, href, srcset, and the inline
// scripts too, since a page may name an image in JS.
//
// Matched case-insensitively on purpose. Deleting is the irreversible half of
// this pass, so it errs towards keeping: a reference whose case does not match
// the file (doc/pic has one, `checkpoint3.png` vs `Checkpoint3.png`) is a broken
// link on a case-sensitive server, and that is e2e-check's finding to report —
// not a reason for this script to quietly delete the file and turn a fixable
// typo into a missing asset.
const referenced = new Set();
for (const page of walk(SITE, name => /\.(html|js|css|json)$/.test(name))) {
  const text = fs.readFileSync(page, 'utf8');
  for (const m of text.matchAll(/doc\/pic\/([A-Za-z0-9._%-]+)/g)) {
    referenced.add(decodeURIComponent(m[1]).toLowerCase());
  }
}

const all = walk(PIC_DIR, () => true);
let removedBytes = 0;
let removedCount = 0;
let keptBytes = 0;

for (const file of all) {
  const name = path.basename(file);
  const size = fs.statSync(file).size;
  if (referenced.has(name.toLowerCase())) { keptBytes += size; continue; }
  fs.unlinkSync(file);
  removedBytes += size;
  removedCount++;
}

// Directories left empty by the pass above.
for (const dir of fs.readdirSync(PIC_DIR, { withFileTypes: true })) {
  if (!dir.isDirectory()) continue;
  const full = path.join(PIC_DIR, dir.name);
  if (fs.readdirSync(full).length === 0) fs.rmdirSync(full);
}

const mb = n => (n / 1048576).toFixed(1);
console.log(`✓ Pruned _site/doc/pic: kept ${all.length - removedCount} images (${mb(keptBytes)} MB), ` +
  `dropped ${removedCount} unreferenced (${mb(removedBytes)} MB)`);
