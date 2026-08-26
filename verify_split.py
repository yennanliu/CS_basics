# -*- coding: utf-8 -*-
"""Content-preservation check for one cheatsheet split.

usage: verify_split.py <orig.md> <out1.md> [out2.md ...]
Runs the three inventories the batch contract requires -- LC numbers, code
statements, headings -- plus fence and anchor checks over the produced family.
"""
import io, re, sys, collections

orig = io.open(sys.argv[1], encoding='utf-8').read()
outs = collections.OrderedDict((p.split('/')[-1], io.open(p, encoding='utf-8').read())
                               for p in sys.argv[2:])
both = '\n'.join(outs.values())
ok = True

lc = lambda t: set(int(n) for n in re.findall(r'\bLC[ _]?0*(\d{1,4})\b', t))
missing = sorted(lc(orig) - lc(both))
print('LC numbers      : %d -> %d ; lost: %s' % (len(lc(orig)), len(lc(both)), missing or 'none'))
if missing: ok = False

def blocks(t):
    return re.findall(r'^[ \t]*```(\w*)[ \t]*\n(.*?)^[ \t]*```[ \t]*$', t, re.M | re.S)
def body(lang, code):
    out = []
    for ln in code.split('\n'):
        if lang in ('java', 'python', 'js', 'javascript'):
            ln = re.sub(r'//.*$|#.*$', '', ln)
        ln = ln.strip()
        if ln and not ln.startswith(('/*', '*', '*/')):
            out.append(re.sub(r'\s+', ' ', ln))
    return out
def stmts(t):
    c = collections.Counter()
    for l, code in blocks(t):
        for ln in body(l, code): c[ln] += 1
    return c
so, sn = stmts(orig), stmts(both)
lost = sorted(x for x in so if x not in sn)
print('code statements : %d -> %d distinct ; %d only in the source' % (len(so), len(sn), len(lost)))
for x in lost[:15]: print('     only-in-source >>', x[:92])

def heads(t):
    out, fence = [], False
    for ln in t.split('\n'):
        if re.match(r'^\s*```', ln): fence = not fence; continue
        if not fence and re.match(r'^#{1,6} ', ln): out.append(ln.strip())
    return out
def key(h): return re.sub(r'[^a-z0-9]+', '', re.sub(r'^#+\s*', '', h.lower()))
ho, hn = heads(orig), heads(both)
kn = collections.Counter(key(h) for h in hn)
gone = [h for h in ho if not kn[key(h)]]
print('headings        : %d -> %d ; %d with no same-text counterpart' % (len(ho), len(hn), len(gone)))
for h in gone: print('     no counterpart >>', h[:92])

for name, txt in outs.items():
    f = [l for l in txt.split('\n') if re.match(r'^\s*```', l)]
    bad_fence = len(f) % 2
    bare = [l for l in f[::2] if re.match(r'^\s*```\s*$', l)]
    skips, lvl, fence = [], 0, False
    dup = collections.Counter()
    parent = None
    stack = {}
    for ln in txt.split('\n'):
        if re.match(r'^\s*```', ln): fence = not fence; continue
        m = re.match(r'^(#{1,6}) ', ln)
        if not fence and m:
            n = len(m.group(1))
            if lvl and n > lvl + 1: skips.append(ln.strip())
            # the parent is the nearest heading one level up, not the nearest h2
            stack[n] = ln.strip()
            parent = stack.get(n - 1)
            if n > 2: dup[(parent, key(ln))] += 1
            lvl = n
    sib = [k for k, v in dup.items() if v > 1]
    print('  %-26s %3d fences | untagged %d | level skips %d | duplicate sibling headings %d'
          % (name, len(f) // 2, len(bare), len(skips), len(sib)))
    for s_ in skips[:4]: print('       skip >>', s_[:88])
    for s_ in sib[:4]:   print('       dup sibling >>', s_[1][:60], 'under', (s_[0] or '')[:40])
    if bad_fence or bare or skips: ok = False

def gh_slug(h):
    # GitHub: lowercase, drop everything that is not a word char / hyphen / space,
    # then space -> '-'. It does NOT trim the result, which is why a heading ending
    # in a star run keeps a trailing '-' (74 such anchors already exist in doc/).
    t = re.sub(r'^#{1,6}\s+', '', h).rstrip()
    t = re.sub(r'[^\w\- ]', '', t.lower(), flags=re.U)
    return t.replace(' ', '-')
def site_slug(h):
    t = re.sub(r'^#{1,6}\s+', '', h)
    t = re.sub(r'⭐+', '', t).replace('`', '').replace('*', '')
    return re.sub(r'[^a-z0-9]+', '-', t.lower()).strip('-')
slugs = {n: set(gh_slug(h) for h in heads(t)) for n, t in outs.items()}
site  = {n: set(site_slug(h) for h in heads(t)) for n, t in outs.items()}
bad = []
for n, t in outs.items():
    for tgt, anc in re.findall(r'\]\(\./([\w.]+\.md)#([\w-]+)\)', t):
        if tgt in slugs and anc not in slugs[tgt]: bad.append((n, tgt, anc))
    for anc in re.findall(r'\]\(#([\w-]+)\)', t):
        if anc not in slugs[n]: bad.append((n, n, anc))
print('anchors         : %d broken inside the family (GitHub slug rule)' % len(bad))
for b in bad: print('     BROKEN >>', b)
if bad: ok = False
dis = sum(len(slugs[n] ^ site[n]) // 2 for n in outs)
print('slug divergence : %d headings where site/build-lib.js disagrees with GitHub' % dis)
print('RESULT          :', 'ok' if ok else 'FAILED')
sys.exit(0 if ok else 1)
