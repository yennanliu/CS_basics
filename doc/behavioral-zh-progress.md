# 繁體中文 Behavioral guides — Translation Progress

The behavioral-interview guides under [`doc/behavioral/`](./behavioral/) are the
only markdown tree.
A translation is a *sparse overlay* of translated sections in
`i18n/zh/behavioral/<id>.md`, and the site composes the two into a full Chinese
document at build time — see the *Traditional Chinese docs* section of
[CLAUDE.md](../CLAUDE.md).

**This file is generated. Do not edit it by hand:**

```bash
node script/zh.js status --write
```

## How a translation is stored

These guides are prose end to end, with no fenced code today, so nearly every
line is translated. A fence added later is still never stored twice:

```text
doc/behavioral/<id>.md
   │  every fence lifts out to a one-line <!--CODE--> marker
   │  the prose is cut into sections at each heading
   ▼
i18n/zh/behavioral/<id>.md
   │  <!-- hash --> + the translated section
   │  compose — English structure, translated prose, original code
   ▼
_site/behavioral/<page>.zh.html
```

Each section is keyed by a hash of **its English text**. Edit one section of an
English document and only that section's translation goes missing; the rest of
the file stays current. A section with no entry falls back to English, so a
half-translated document renders with English gaps rather than failing.

## Known limitations

- **Principle and rubric names stay in English** — `Customer Obsession`,
  `STAR`, `Bar Raiser`, `Strong hire` — with a 中文 gloss beside them, because
  they are the words the interviewer will use. The questions themselves are translated.


## Status — 82 / 82 sections (100%)

| Behavioral guide | Sections | 繁體中文 |
|---|---:|:---:|
| [amazon_leadership_principles](./behavioral/amazon_leadership_principles.md) | 27 | [✅](../i18n/zh/behavioral/amazon_leadership_principles.md) |
| [googleyness](./behavioral/googleyness.md) | 18 | [✅](../i18n/zh/behavioral/googleyness.md) |
| [mock_question_bank](./behavioral/mock_question_bank.md) | 37 | [✅](../i18n/zh/behavioral/mock_question_bank.md) |
