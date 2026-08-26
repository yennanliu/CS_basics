# -*- coding: utf-8 -*-
"""Ground truth: do the in-repo markdown anchors resolve against the ids the site
actually emits?  Reads _site/*.html, not a re-implementation of slugify."""
import io, re, glob, os, sys, collections
ids = {}
for p in glob.glob('_site/**/*.html', recursive=True):
    n = os.path.basename(p)
    ids.setdefault(n, set()).update(re.findall(r'\sid="([^"]+)"', io.open(p, encoding='utf-8', errors='replace').read()))
bad = collections.Counter(); total = 0; broken = []
for p in glob.glob('doc/**/*.md', recursive=True):
    t = io.open(p, encoding='utf-8').read()
    for tgt, anc in re.findall(r'\]\((?:\./)?([\w.\-]+)\.md#([\w%\-]+)\)', t):
        html = tgt + '.html'
        if html not in ids: continue
        total += 1
        if anc not in ids[html]:
            bad[tgt] += 1; broken.append((os.path.basename(p), tgt, anc))
print('in-repo md anchors pointing at a built page : %d' % total)
print('  NOT resolving against the emitted ids     : %d' % len(broken))
for t, c in bad.most_common(8): print('     %-34s %d' % (t, c))
if len(sys.argv) > 1:
    io.open(sys.argv[1], 'w', encoding='utf-8').write('\n'.join('%s -> %s#%s' % b for b in sorted(broken)))
