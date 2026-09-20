#!/usr/bin/env python3
"""
Unit tests for script/check_skills.py.

    python3 script/test_check_skills.py           # all of it
    python3 script/test_check_skills.py -v
    python3 script/test_check_skills.py Format

The gate is the only thing that ever looks at a skill, so the gate going quiet
is indistinguishable from every skill being fine. That is the failure these
tests exist for: a tightened regex that stops matching, an extractor that
returns nothing, a check that reports PASS on an empty list. Each group below
asserts a *bad* skill fails as well as a good one passing — a checker that only
ever sees valid input cannot tell you it still works.

The synthetic skills are built in a temp directory so the tests do not move when
a real skill does. `LiveSkills` at the bottom is what holds the real ones, and
it is where the vacuous-pass guards live: the real tree must produce a non-zero
number of format checks and runnable commands, or the extractors have gone
silent. That split mirrors script/test_suggest_review.py and site/test/.
"""
import io
import shutil
import sys
import tempfile
import textwrap
import unittest
from contextlib import redirect_stdout
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import check_skills as cs  # noqa: E402


GOOD = """\
---
name: lc-demo
description: Files a demo into the repo the way the existing ones are filed, so the shape stays one shape. Use when asked to add a demo. Triggers - "add a demo", "/lc-demo 1".
allowed-tools: Read, Bash
---

# Add a demo

**Invocation**: `/lc-demo <thing>` — e.g. `/lc-demo widget`.

## Prime directives

1. **Copy a neighbour.** Never invent the layout.

## The steps

### 1. Look

```bash
ls data/progress.txt
```

### 2. Write

The file goes where the neighbours are.

### 3. Check

```bash
node script/zh.js status
```

## Do not

- ❌ invent the layout
- ❌ hand back untested work
- ❌ commit or push unless asked

## Worked example

| Step | What it produced |
|---|---|
| 1 | looked |
"""


def build_skill(root, name="lc-demo", text=GOOD):
    skill = Path(root) / name
    skill.mkdir(parents=True, exist_ok=True)
    (skill / "SKILL.md").write_text(text, encoding="utf-8")
    return skill


def run_check(fn, skill):
    """-> (ok, report). Output is swallowed; the tally is what is asserted."""
    rep = cs.Report()
    buf = io.StringIO()
    with redirect_stdout(buf):
        ok = fn(rep, skill)
    return ok, rep


class TempSkill(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.mkdtemp()
        self.addCleanup(shutil.rmtree, self.tmp)

    def assertFails(self, fn, text, why):
        ok, rep = run_check(fn, build_skill(self.tmp, text=text))
        self.assertFalse(ok, f"{why}: expected a failure, got a clean pass")
        self.assertGreater(rep.failed, 0)

    def assertPasses(self, fn, text, why):
        ok, rep = run_check(fn, build_skill(self.tmp, text=text))
        self.assertTrue(ok, f"{why}: expected a pass, {rep.failed} check(s) failed")


# ── structure ────────────────────────────────────────────────────────────────

class Structure(TempSkill):
    def test_the_good_skill_passes(self):
        self.assertPasses(cs.check_structure, GOOD, "the fixture itself")

    def test_missing_frontmatter_fence_fails(self):
        # The bug this whole gate was written for: add-time-space shipped with
        # no --- fences and advertised the literal text "name: add-time-space".
        self.assertFails(cs.check_structure, GOOD.replace("---\n", "", 2),
                         "frontmatter with no fences")

    def test_name_must_match_the_directory(self):
        # The directory name IS the slash command, so a name that disagrees
        # documents a command nobody can type.
        self.assertFails(cs.check_structure, GOOD.replace("name: lc-demo", "name: lc-other"),
                         "name disagreeing with the directory")

    def test_indented_frontmatter_is_rejected(self):
        bad = GOOD.replace("allowed-tools: Read, Bash", "allowed-tools:\n  - Read")
        self.assertFails(cs.check_structure, bad, "a block scalar hosts do not read")

    def test_over_long_description_fails(self):
        bad = GOOD.replace("Triggers - ", "Triggers - " + "x" * cs.MAX_DESCRIPTION)
        self.assertFails(cs.check_structure, bad, "a description past the match limit")

    def test_description_that_echoes_the_name_fails(self):
        bad = GOOD.replace(GOOD.split("description: ")[1].split("\n")[0], "lc demo")
        self.assertFails(cs.check_structure, bad, "a description that is just the name")

    def test_unbalanced_fences_fail(self):
        self.assertFails(cs.check_structure, GOOD + "\n```bash\nls\n", "an unclosed fence")


# ── format ───────────────────────────────────────────────────────────────────

class Format(TempSkill):
    def test_the_good_skill_passes(self):
        self.assertPasses(cs.check_format, GOOD, "the fixture itself")

    def test_each_required_section_is_actually_required(self):
        for section in cs.REQUIRED_SECTIONS:
            with self.subTest(section=section):
                self.assertFails(cs.check_format, GOOD.replace(section, "## Something else"),
                                 f"a recipe with no {section!r}")

    def test_steps_must_be_contiguous(self):
        # A gap reads as an instruction to skip one.
        self.assertFails(cs.check_format, GOOD.replace("### 3. Check", "### 4. Check"),
                         "steps numbered 1, 2, 4")
        self.assertFails(cs.check_format, GOOD.replace("### 2. Write", "### 1. Write"),
                         "two steps numbered 1")

    def test_invocation_must_name_its_own_command(self):
        self.assertFails(cs.check_format, GOOD.replace("`/lc-demo <thing>`", "`/lc-other <thing>`"),
                         "an Invocation naming a different command")

    def test_missing_invocation_fails(self):
        self.assertFails(cs.check_format, GOOD.replace("**Invocation**:", "Invocation:"),
                         "no Invocation line")

    def test_untagged_fence_fails_for_a_recipe(self):
        self.assertFails(cs.check_format, GOOD.replace("```bash\nls data", "```\nls data"),
                         "a bare opening fence")

    def test_do_not_must_be_a_list(self):
        prose = GOOD.replace("- ❌ invent the layout\n- ❌ hand back untested work\n",
                             "Do not invent the layout, and do not hand back untested work.\n")
        self.assertFails(cs.check_format, prose, "a Do not section written as prose")

    def test_headings_inside_a_fence_do_not_count(self):
        # lc-faq-add shows a whole FAQ skeleton, `## 1) <First section>` and
        # all, inside a ```markdown block. Those are examples, not structure.
        fenced = GOOD.replace("## Worked example",
                              "```markdown\n## The steps\n### 9. Not a real step\n```\n\n## Worked example")
        self.assertPasses(cs.check_format, fenced, "example headings inside a fence")

    def test_a_non_recipe_skill_is_only_checked_for_an_h1(self):
        # The generic skills pre-date the house shape and are not `lc-` skills.
        skill = build_skill(self.tmp, name="plain-helper",
                            text="---\nname: plain-helper\ndescription: Does a thing when asked to.\n---\n\n# Plain\n\nProse.\n")
        ok, _ = run_check(cs.check_format, skill)
        self.assertTrue(ok, "a non-recipe skill should not need Invocation or steps")

    def test_lc_coach_is_exempt_by_name(self):
        self.assertIn("lc-coach", cs.RECIPE_EXEMPT)
        self.assertFalse(cs.is_recipe(Path("/x/.claude/skills/lc-coach")))
        self.assertTrue(cs.is_recipe(Path("/x/.claude/skills/lc-log")))


# ── referenced paths ─────────────────────────────────────────────────────────

class ReferencedPaths(TempSkill):
    def test_a_real_path_passes(self):
        self.assertPasses(cs.check_referenced_paths, GOOD, "paths that exist")

    def test_a_renamed_script_fails(self):
        # Nothing points at a skill — the skill points at the file — so a rename
        # leaves every other check green and breaks the recipe.
        self.assertFails(cs.check_referenced_paths,
                         GOOD.replace("script/zh.js", "script/zh-renamed.js"),
                         "a command naming a script that no longer exists")

    def test_a_template_path_is_not_resolved(self):
        text = GOOD.replace("ls data/progress.txt", "ls data/<slug>.txt")
        self.assertPasses(cs.check_referenced_paths, text,
                          "a <placeholder> path the agent fills in")

    def test_a_glob_is_not_resolved(self):
        text = GOOD.replace("ls data/progress.txt", "ls doc/faq/*/*.md")
        self.assertPasses(cs.check_referenced_paths, text, "a glob")

    def test_it_finds_the_paths_it_should(self):
        # Guard against the regex quietly matching nothing, which would make
        # every check above pass for the wrong reason.
        found = cs.repo_paths_in(GOOD)
        self.assertIn("data/progress.txt", found)
        self.assertIn("script/zh.js", found)


# ── run ──────────────────────────────────────────────────────────────────────

class Runnable(unittest.TestCase):
    def commands(self, body):
        return cs.runnable_commands("```bash\n" + textwrap.dedent(body) + "```\n")

    def test_a_plain_command_runs(self):
        run, skip = self.commands("ls data/\n")
        self.assertEqual(run, ["ls data/"])
        self.assertEqual(skip, [])

    def test_a_template_is_skipped_with_a_reason(self):
        run, skip = self.commands("ls doc/faq/<dir>/<file>.md\n")
        self.assertEqual(run, [])
        self.assertIn("placeholder", skip[0][1])

    def test_every_mutating_token_is_skipped(self):
        # A write, a network call or a blocking server must never run here, and
        # each is skipped by name rather than by guessing.
        for token, _ in cs.MUTATING:
            with self.subTest(token=token):
                run, skip = self.commands(f"some {token} thing\n")
                self.assertEqual(run, [], f"{token!r} should not have run")
                self.assertEqual(len(skip), 1)

    def test_a_trailing_comment_is_not_part_of_the_command(self):
        run, _ = self.commands("ls data/   # the practice log\n")
        self.assertEqual(run, ["ls data/"])

    def test_a_comment_line_is_ignored(self):
        run, skip = self.commands("# just a note\n")
        self.assertEqual((run, skip), ([], []))

    def test_a_multi_line_quoted_command_is_one_command(self):
        # `node -e "..."` spans lines. Splitting per line hands /bin/sh half a
        # string, which fails for a reason that has nothing to do with the recipe.
        run, _ = self.commands('node -e "\nconsole.log(1);\nconsole.log(2);"\n')
        self.assertEqual(len(run), 1)
        self.assertIn("console.log(2)", run[0])

    def test_an_unterminated_quote_is_reported_not_run(self):
        run, skip = self.commands('node -e "console.log(1)\n')
        self.assertEqual(run, [])
        self.assertIn("heredoc", skip[0][1])

    def test_balanced_is_what_it_says(self):
        self.assertTrue(cs.balanced('echo "hi"'))
        self.assertFalse(cs.balanced('echo "hi'))
        self.assertTrue(cs.balanced("echo 'a' \"b\""))

    def test_a_failing_command_fails_the_check(self):
        with tempfile.TemporaryDirectory() as tmp:
            skill = build_skill(tmp, text=GOOD.replace("ls data/progress.txt",
                                                       "ls data/definitely-not-here.txt"))
            ok, rep = run_check(lambda r, s: cs.check_run(r, s, timeout=30), skill)
        self.assertFalse(ok, "a command that exits non-zero must fail the gate")


# ── the real tree ────────────────────────────────────────────────────────────

class LiveSkills(unittest.TestCase):
    """The checks above prove the rules work. These prove they are still aimed
    at something: a checker that silently matches nothing passes every test
    above and every skill in the repo."""

    @classmethod
    def setUpClass(cls):
        cls.skills = sorted(d for d in cs.SKILLS_DIR.iterdir() if d.is_dir())
        cls.recipes = [d for d in cls.skills if cs.is_recipe(d)]

    def test_there_are_recipes_to_check(self):
        self.assertGreaterEqual(len(self.recipes), 7,
                                "the lc-* recipe family should be found")

    def test_every_real_skill_passes_structure_and_format(self):
        for skill in self.skills:
            with self.subTest(skill=skill.name):
                ok, rep = run_check(cs.check_structure, skill)
                self.assertTrue(ok, f"{skill.name}: {rep.failed} structure check(s) failed")
                ok, rep = run_check(cs.check_format, skill)
                self.assertTrue(ok, f"{skill.name}: {rep.failed} format check(s) failed")

    def test_every_repo_path_a_recipe_names_exists(self):
        for skill in self.recipes:
            with self.subTest(skill=skill.name):
                ok, _ = run_check(cs.check_referenced_paths, skill)
                self.assertTrue(ok, f"{skill.name} names a repo path that is gone")

    def test_the_path_extractor_is_not_silent(self):
        total = sum(len(cs.repo_paths_in((d / "SKILL.md").read_text(encoding="utf-8")))
                    for d in self.recipes)
        self.assertGreater(total, 20, "repo_paths_in matched almost nothing")

    def test_the_command_extractor_is_not_silent(self):
        run = sum(len(cs.runnable_commands((d / "SKILL.md").read_text(encoding="utf-8"))[0])
                  for d in self.recipes)
        self.assertGreater(run, 8, "runnable_commands found almost nothing to run")

    def test_no_recipe_documents_a_mutating_command_as_runnable(self):
        # The safety property `--run` depends on: nothing that writes, reaches
        # the network or blocks may end up in the run list.
        for skill in self.recipes:
            run, _ = cs.runnable_commands((skill / "SKILL.md").read_text(encoding="utf-8"))
            for command in run:
                for token, why in cs.MUTATING:
                    with self.subTest(skill=skill.name, token=token):
                        self.assertNotIn(token, command, f"would run a command that {why}")

    def test_every_recipe_page_is_wired_into_the_gate(self):
        # A skill with a page nobody checks is a page whose links rot silently.
        for skill in self.recipes:
            page = f"site/pages/{skill.name}.html"
            if (cs.ROOT / page).is_file():
                with self.subTest(skill=skill.name):
                    self.assertIn(page, cs.WIRING_SOURCES,
                                  f"{page} exists but is not in WIRING_SOURCES")


if __name__ == "__main__":
    unittest.main(verbosity=1)
