#!/usr/bin/env python3
"""Traditional-Chinese translation pipeline for doc/cheatsheet/.

The cheatsheets are ~70% fenced code by line count, and that code must survive a
translation byte-for-byte. So a translator never sees it: `extract` collapses
every fence to a one-line marker, the prose alone gets translated, and `merge`
splices the original blocks back in.

    python3 script/zh_cheatsheet.py extract [slug ...]   # en  -> work/<slug>.md
    python3 script/zh_cheatsheet.py merge   [slug ...]   # work/<slug>.zh.md -> doc/cheatsheet/zh/<slug>.md
    python3 script/zh_cheatsheet.py verify  [slug ...]   # zh code blocks == en code blocks
    python3 script/zh_cheatsheet.py status  [--write]    # progress table (--write updates the tracker doc)

With no slugs, every sheet is processed. The marker is an HTML comment, so a
half-finished file still renders as valid markdown instead of leaking `⟦…⟧` noise.
"""

import argparse
import os
import re
import subprocess
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
EN_DIR = os.path.join(ROOT, 'doc', 'cheatsheet')
ZH_DIR = os.path.join(EN_DIR, 'zh')
WORK_DIR = os.path.join(ROOT, '.zh-work')
TRACKER = os.path.join(ROOT, 'doc', 'cheatsheet-zh-progress.md')

MARKER = '<!--CODE:%d-->'
MARKER_RE = re.compile(r'^<!--CODE:(\d+)-->\s*$')
FENCE_RE = re.compile(r'^(\s*)(`{3,}|~{3,})(.*)$')

# Not cheatsheets: the template, and the index/README files build-site.js skips.
SKIP = {'00_template', 'README'}


# ── Core: split a sheet into prose + code blocks ─────────────────────────────

def split_blocks(text):
    """Return (prose_with_markers, [code_block, ...]).

    A code block is captured whole, fences included, so re-inserting it is a
    plain substitution. Fence matching follows CommonMark: the closing fence
    must use the same character and be at least as long as the opening one.
    """
    prose, blocks = [], []
    lines = text.split('\n')
    i = 0
    while i < len(lines):
        m = FENCE_RE.match(lines[i])
        if not m:
            prose.append(lines[i])
            i += 1
            continue
        indent, fence, info = m.groups()
        # An info string containing the fence char is not an opener (e.g. ```` ``` ````).
        if fence[0] in info:
            prose.append(lines[i])
            i += 1
            continue
        block = [lines[i]]
        i += 1
        close = re.compile(r'^\s*' + re.escape(fence[0]) + '{' + str(len(fence)) + r',}\s*$')
        while i < len(lines) and not close.match(lines[i]):
            block.append(lines[i])
            i += 1
        if i < len(lines):
            block.append(lines[i])
            i += 1
        prose.append(MARKER % len(blocks))
        blocks.append('\n'.join(block))
    return '\n'.join(prose), blocks


def code_blocks(text):
    return split_blocks(text)[1]


class MergeError(Exception):
    """A translation that cannot be spliced back together — reported, not fatal."""


def restore(prose, blocks, slug):
    """Substitute markers back for their code blocks, insisting on a 1:1 match.

    Raises MergeError rather than exiting: one broken translation should not
    hide the state of the other 128.
    """
    out, seen = [], []
    for line in prose.split('\n'):
        m = MARKER_RE.match(line)
        if not m:
            out.append(line)
            continue
        idx = int(m.group(1))
        if idx >= len(blocks):
            raise MergeError(f'{slug}: marker {MARKER % idx} has no matching code '
                             f'block (the English sheet has {len(blocks)})')
        if idx in seen:
            raise MergeError(f'{slug}: marker {MARKER % idx} appears more than once')
        seen.append(idx)
        out.append(blocks[idx])
    missing = [i for i in range(len(blocks)) if i not in seen]
    if missing:
        raise MergeError(
            f'{slug}: {len(missing)} code marker(s) dropped by the translation: ' +
            ', '.join(MARKER % i for i in missing[:8]) +
            ('…' if len(missing) > 8 else ''))
    if seen != sorted(seen):
        raise MergeError(f'{slug}: code markers are out of order — '
                         'the translation reordered sections')
    return '\n'.join(out)


# ── Helpers ──────────────────────────────────────────────────────────────────

def die(msg):
    print(f'error: {msg}', file=sys.stderr)
    sys.exit(1)


def all_slugs():
    return sorted(
        f[:-3] for f in os.listdir(EN_DIR)
        if f.endswith('.md') and f[:-3] not in SKIP
    )


def resolve(slugs):
    known = set(all_slugs())
    if not slugs:
        return sorted(known)
    unknown = [s for s in slugs if s not in known]
    if unknown:
        die('no such cheatsheet: ' + ', '.join(unknown))
    return slugs


def read(path):
    with open(path, encoding='utf-8') as fh:
        return fh.read()


def write(path, text):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as fh:
        fh.write(text)


# ── Commands ─────────────────────────────────────────────────────────────────

def cmd_extract(slugs):
    for slug in resolve(slugs):
        prose, blocks = split_blocks(read(os.path.join(EN_DIR, slug + '.md')))
        write(os.path.join(WORK_DIR, slug + '.md'), prose)
        print(f'{slug}: {len(prose.splitlines())} prose lines, {len(blocks)} code blocks')


def cmd_merge(slugs):
    merged, failed, pending = 0, [], 0
    for slug in resolve(slugs):
        src = os.path.join(WORK_DIR, slug + '.zh.md')
        if not os.path.exists(src):
            pending += 1
            continue
        try:
            blocks = code_blocks(read(os.path.join(EN_DIR, slug + '.md')))
            out = restore(read(src), blocks, slug)
            if not out.startswith('# '):
                raise MergeError(f'{slug}: the translation must still open with the H1 ("# 標題")')
        except MergeError as exc:
            failed.append(str(exc))
            continue
        write(os.path.join(ZH_DIR, slug + '.md'), out)
        merged += 1
        print(f'✓ doc/cheatsheet/zh/{slug}.md')
    for msg in failed:
        print('✗ ' + msg, file=sys.stderr)
    summary = f'merged {merged} file(s)'
    if failed:
        summary += f', {len(failed)} failed'
    if pending:
        summary += f', {pending} not translated yet'
    print(summary)
    if failed:
        sys.exit(1)


def cmd_verify(slugs):
    problems = []
    for slug in resolve(slugs):
        zh_path = os.path.join(ZH_DIR, slug + '.md')
        if not os.path.exists(zh_path):
            continue
        en, zh = (code_blocks(read(os.path.join(EN_DIR, slug + '.md'))),
                  code_blocks(read(zh_path)))
        if len(en) != len(zh):
            problems.append(f'{slug}: {len(zh)} code blocks, English has {len(en)}')
            continue
        for i, (a, b) in enumerate(zip(en, zh)):
            if a != b:
                problems.append(f'{slug}: code block #{i + 1} was modified by the translation')
                break
    for p in problems:
        print('✗ ' + p, file=sys.stderr)
    if problems:
        sys.exit(1)
    print('✓ every translated sheet carries its English code blocks verbatim')


def last_commit(path):
    """Unix timestamp of the newest commit touching `path`, or 0 if untracked."""
    try:
        out = subprocess.run(['git', 'log', '-1', '--format=%ct', '--', path],
                             cwd=ROOT, capture_output=True, text=True, check=True)
        return int(out.stdout.strip() or 0)
    except (subprocess.CalledProcessError, ValueError, OSError):
        return 0


def survey():
    """One row per sheet: (slug, en_lines, translated, stale)."""
    rows = []
    for slug in all_slugs():
        en_path = os.path.join(EN_DIR, slug + '.md')
        zh_path = os.path.join(ZH_DIR, slug + '.md')
        has = os.path.exists(zh_path)
        # A translation goes stale when the English sheet is edited after it.
        # Both timestamps come from git, so an unrelated checkout cannot fake it.
        stale = has and last_commit(en_path) > last_commit(zh_path) > 0
        rows.append((slug, len(read(en_path).split('\n')), has, stale))
    return rows


TRACKER_HEAD = """# 繁體中文 Cheatsheets — Translation Progress

Every sheet in [`doc/cheatsheet/`](./cheatsheet/) has a Traditional Chinese
counterpart under [`doc/cheatsheet/zh/`](./cheatsheet/zh/) with the same filename.
The site builds both and the navbar carries a **中文 / EN** button that swaps
between the two — see the *Traditional Chinese cheatsheets* section of
[CLAUDE.md](../CLAUDE.md) for the full workflow.

**This file is generated. Do not edit it by hand:**

```bash
python3 script/zh_cheatsheet.py status --write
```

## How a translation is produced

Roughly 70% of these sheets is fenced code, and that code must survive
byte-for-byte. So the translator never sees it:

```text
doc/cheatsheet/<slug>.md
   │  extract  — every fence collapses to a one-line <!--CODE:n--> marker
   ▼
.zh-work/<slug>.md            (prose only, gitignored)
   │  translate — markers must survive, exactly once, in order
   ▼
.zh-work/<slug>.zh.md
   │  merge    — the original code blocks are spliced back in
   ▼
doc/cheatsheet/zh/<slug>.md
```

`python3 script/zh_cheatsheet.py verify` re-extracts both sides and fails if a
translated sheet's code blocks differ from the English original in any way.

## Known limitations

- **Anchor links across sheets** (`./heap.md#overview`) keep the English
  fragment, which does not exist on the translated page — the link lands at the
  top of the right sheet rather than at the right section.
- **The star legend and the priority tooltips** inside a sheet are still English;
  they come from `site/build-lib.js`, not from the markdown.
- A sheet marked **⚠️ stale** below has been edited in English since it was
  translated. Re-run extract → translate → merge for it.

"""


def cmd_status(write_tracker):
    rows = survey()
    total = len(rows)
    done = sum(1 for _, _, has, _ in rows if has)
    stale = sum(1 for _, _, _, st in rows if st)
    pct = (100 * done / total) if total else 0
    print(f'{done}/{total} sheets translated ({pct:.0f}%)' +
          (f', {stale} stale' if stale else ''))
    if not write_tracker:
        for slug, lines, has, st in rows:
            mark = '⚠' if st else ('x' if has else ' ')
            print(f'  [{mark}] {slug} ({lines} lines)')
        return

    body = [TRACKER_HEAD]
    body.append(f'## Status — {done} / {total} sheets ({pct:.0f}%)' +
                (f', {stale} needing a refresh' if stale else ''))
    body.append('')
    body.append('| Sheet | Lines | 繁體中文 |')
    body.append('|---|---:|:---:|')
    for slug, lines, has, st in rows:
        if st:
            state = f'[⚠️ stale](./cheatsheet/zh/{slug}.md)'
        elif has:
            state = f'[✅](./cheatsheet/zh/{slug}.md)'
        else:
            state = '—'
        body.append(f'| [{slug}](./cheatsheet/{slug}.md) | {lines} | {state} |')
    body.append('')
    write(TRACKER, '\n'.join(body))
    print(f'✓ wrote {os.path.relpath(TRACKER, ROOT)}')


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = ap.add_subparsers(dest='cmd', required=True)
    for name in ('extract', 'merge', 'verify'):
        p = sub.add_parser(name)
        p.add_argument('slugs', nargs='*')
    p = sub.add_parser('status')
    p.add_argument('--write', action='store_true', help='rewrite doc/cheatsheet-zh-progress.md')

    args = ap.parse_args()
    if args.cmd == 'extract':
        cmd_extract(args.slugs)
    elif args.cmd == 'merge':
        cmd_merge(args.slugs)
    elif args.cmd == 'verify':
        cmd_verify(args.slugs)
    else:
        cmd_status(args.write)


if __name__ == '__main__':
    main()
