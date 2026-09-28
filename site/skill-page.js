/* Shared behaviour for the agent-skill pages — the reveal-on-scroll, the
   worked-run tape, the pipeline stepper, the tab groups and the copy buttons.

   This was pasted into each of those pages: eight byte-identical copies plus
   lc-cheatsheet's, which had drifted 18 lines. It is one file now, copied to
   _site/ by site/build.sh.

   Every page that loads it must also load skill-page.css, and the two are a
   pair: the CSS leaves .reveal visible so a page with no JS is never stuck
   blank, and this file is what hides it.
*/
(function () {
  'use strict';

  var reduced = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  // Sections are visible in the stylesheet and only hidden once `.js` is on the
  // root, so a page with no JS never gets stuck blank. The cost is that adding
  // the class here would blank whatever is already on screen for a frame, so
  // the above-the-fold sections are marked revealed in the *same* synchronous
  // block — one paint, no flash.
  var reveals = [].slice.call(document.querySelectorAll('.reveal'));
  var onscreen = reveals.filter(function (el) {
    return el.getBoundingClientRect().top < (window.innerHeight || 0);
  });
  document.documentElement.classList.add('js');
  onscreen.forEach(function (el) { el.classList.add('in'); });

  // ── Hero tape ─────────────────────────────────────────────────────────
  // The run is written into the <pre> in the HTML, so it is there whatever
  // happens to this script, and it is typed out by clipping that text rather
  // than by assembling it here. Reduced motion keeps the finished frame.
  var tape = document.getElementById('tape');
  if (tape && !reduced) {
    var TAPE = tape.textContent;
    var caret = document.createElement('span');
    caret.className = 'caret';
    var text = document.createTextNode('');
    tape.textContent = '';
    tape.appendChild(text);
    tape.appendChild(caret);
    var i = 0;
    (function tick() {
      // Whole lines at a time once past the first: character-by-character for
      // 700 characters is a long time to make someone watch.
      i += i < 60 ? 1 : 3;
      text.nodeValue = TAPE.slice(0, i);
      if (i < TAPE.length) setTimeout(tick, i < 60 ? 26 : 12);
      else caret.remove();
    })();
  }

  // ── Pipeline stepper ──────────────────────────────────────────────────
  var steps = [].slice.call(document.querySelectorAll('.step'));
  var sd = document.getElementById('sd');

  function showStep(step) {
    steps.forEach(function (s) {
      s.setAttribute('aria-checked', String(s === step));
      s.tabIndex = s === step ? 0 : -1;
    });
    // `data-n` is the rail's big label: a step number on the numbered pages, but
    // the mode key ("example", "variation") on lc-cheatsheet, whose four modes
    // are named rather than ordered. Only a number reads as "3. " in front of a
    // title, so only a number is prefixed.
    sd.querySelector('.sd-name').textContent =
      (/^\d+$/.test(step.dataset.n || '') ? step.dataset.n + '. ' : '') + step.dataset.name;
    sd.querySelector('.sd-body').textContent = step.dataset.body;
    // The rule is the line worth reading twice, so it gets a label. Every
    // dataset value is plain text and is set as text — the label is a real
    // element rather than a string concatenated into innerHTML.
    var rule = sd.querySelector('.sd-rule');
    rule.textContent = '';
    var label = document.createElement('b');
    label.textContent = 'Rule: ';
    rule.appendChild(label);
    rule.appendChild(document.createTextNode(step.dataset.rule));
    sd.querySelector('.sd-code').textContent = step.dataset.code;
  }

  steps.forEach(function (step, idx) {
    step.addEventListener('click', function () { showStep(step); });
    step.addEventListener('keydown', function (e) {
      var delta = e.key === 'ArrowRight' ? 1 : e.key === 'ArrowLeft' ? -1 : 0;
      if (!delta) return;
      e.preventDefault();
      var next = steps[(idx + delta + steps.length) % steps.length];
      showStep(next);
      next.focus();
    });
  });
  if (steps.length) showStep(document.querySelector('.step[aria-checked="true"]') || steps[0]);

  // ── Tabs ──────────────────────────────────────────────────────────────
  // Panels toggle with the `hidden` attribute rather than a class, so a hidden
  // panel is out of the accessibility tree and out of find-in-page too. Each
  // tablist is handled independently, so the two on this page cannot fight.
  [].slice.call(document.querySelectorAll('[role="tablist"]')).forEach(function (list) {
    var tabs = [].slice.call(list.querySelectorAll('.tab'));
    if (!tabs.length) return;

    function select(tab) {
      tabs.forEach(function (t) {
        var on = t === tab;
        t.setAttribute('aria-selected', on ? 'true' : 'false');
        // Roving tabindex: the tablist is ONE Tab stop and the arrows move
        // inside it. Without this, every tab is its own stop, so reaching the
        // content past a five-tab install block costs five presses — and the
        // arrow handler below, which already moves focus, has nothing to
        // return focus order to. The stepper above does the same thing.
        t.tabIndex = on ? 0 : -1;
        document.getElementById(t.getAttribute('aria-controls')).hidden = !on;
      });
    }
    tabs.forEach(function (tab, i) {
      tab.addEventListener('click', function () { select(tab); });
      tab.addEventListener('keydown', function (e) {
        var delta = e.key === 'ArrowRight' ? 1 : e.key === 'ArrowLeft' ? -1 : 0;
        if (!delta) return;
        e.preventDefault();
        var next = tabs[(i + delta + tabs.length) % tabs.length];
        select(next);
        next.focus();
      });
    });
    // The markup marks one tab selected; run it through select() so the
    // tabIndex state matches from the first paint rather than from the first
    // click.
    select(list.querySelector('.tab[aria-selected="true"]') || tabs[0]);
  });

  // ── Copy buttons ──────────────────────────────────────────────────────
  // clipboard.writeText needs a secure context; on plain http it rejects, so
  // the button says so instead of silently doing nothing.
  //
  // The `.ann` spans are margin notes about the code, not part of it, so they
  // are stripped from a clone before the text is read — copying the anatomy
  // block otherwise hands back Python with "<- docstring first" inside it. The
  // strip leaves the padding that positioned each note, hence the trailing
  // whitespace pass. textContent, not innerText: the clone is detached, and in
  // a <pre> the two agree anyway.
  function codeText(block) {
    var node = block.querySelector('code').cloneNode(true);
    [].slice.call(node.querySelectorAll('.ann')).forEach(function (a) {
      a.parentNode.removeChild(a);
    });
    return node.textContent.replace(/[ \t]+$/gm, '');
  }

  [].slice.call(document.querySelectorAll('.copy')).forEach(function (btn) {
    btn.addEventListener('click', function () {
      var code = codeText(btn.parentElement);
      var done = function (label) {
        btn.textContent = label;
        setTimeout(function () { btn.textContent = 'copy'; }, 1400);
      };
      if (!navigator.clipboard) return done('select it');
      navigator.clipboard.writeText(code).then(function () { done('copied'); },
                                               function () { done('select it'); });
    });
  });

  // ── Reveal on scroll ──────────────────────────────────────────────────
  if (reduced || !('IntersectionObserver' in window)) {
    reveals.forEach(function (el) { el.classList.add('in'); });
  } else {
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (!entry.isIntersecting) return;
        entry.target.classList.add('in');
        io.unobserve(entry.target);
      });
    }, { rootMargin: '0px 0px -12% 0px' });
    reveals.forEach(function (el) { io.observe(el); });
  }
})();
