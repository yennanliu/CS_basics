# Amazon Leadership Principles Interview

> **Scope** — How Amazon uses its sixteen Leadership Principles in an interview loop: what each principle probes, the classic questions for it, the STAR answer the interviewer is trained to expect, and how the Bar Raiser and the debrief read your stories.
> **See also**: [googleyness.md](./googleyness.md) — the Google behavioral round, which scores many of the same stories against a different rubric; [mock_question_bank.md](./mock_question_bank.md) — twenty-five classic questions with sample answers, for mock practice.

## 1. How the Loop Uses the Principles

| | |
|---|---|
| **What is asked** | Past-behavior questions, almost always "Tell me about a time…", each tied to a Leadership Principle (LP) |
| **Where** | Every round. In an SDE loop a coding round typically opens with one or two LP questions |
| **Who owns what** | Before the loop, each interviewer is assigned a few LPs, so the loop as a whole covers most of them without repeating |
| **Bar Raiser** | One interviewer from outside the hiring team, trained to hold the hiring bar for the whole company; they lead the debrief and can block a hire |
| **The decision** | Each interviewer writes up the evidence for their LPs and votes before the debrief; the debrief compares notes and decides |

Amazon's own interview guidance recommends the **STAR** format, and interviewers are trained to pull a story into that shape with follow-up questions if you do not.

> **Key point** — The LPs are not a culture quiz. Each one is a lens for reading a past decision, and the interviewer is collecting **data points**: specific actions you took that show, or fail to show, the principle.

## 2. The Sixteen Principles at a Glance

| # | Principle | In one line | What the interviewer probes |
|---|---|---|---|
| 1 | **Customer Obsession** | Start from the customer and work backwards | Who the customer was and how you learned what they needed |
| 2 | **Ownership** | Act for the long term and beyond your own scope; never "that's not my job" | Something you took on that nobody assigned you |
| 3 | **Invent and Simplify** | Find new, simpler ways to do things | A simplification you designed, and the result |
| 4 | **Are Right, A Lot** | Strong judgment; seek out views that disagree with yours | A call you made with incomplete data, and how you tested it |
| 5 | **Learn and Be Curious** | Keep learning and explore new possibilities | Something you taught yourself, and why |
| 6 | **Hire and Develop the Best** | Raise the bar with every hire and coach others | Someone you mentored, and how they grew |
| 7 | **Insist on the Highest Standards** | Raise the bar continually; fix defects so they stay fixed | A time you refused to ship something not good enough |
| 8 | **Think Big** | Create and communicate a bold direction | A vision bigger than the task you were given |
| 9 | **Bias for Action** | Speed matters; many decisions are reversible | A calculated risk you took without waiting for certainty |
| 10 | **Frugality** | Accomplish more with less | A constraint that made you more resourceful |
| 11 | **Earn Trust** | Listen, speak candidly, admit mistakes | A mistake you admitted, or hard feedback you gave or took |
| 12 | **Dive Deep** | Stay close to the details; audit often | A root cause you found by going into the data |
| 13 | **Have Backbone; Disagree and Commit** | Challenge decisions respectfully, then commit fully | A disagreement you raised, and how you committed afterwards |
| 14 | **Deliver Results** | Deliver on the key inputs, on time, despite setbacks | A goal you met despite an obstacle |
| 15 | **Strive to be Earth's Best Employer** | Make the work environment better for the people in it | How you made a team a better place to work |
| 16 | **Success and Scale Bring Broad Responsibility** | Consider the wider effects of your work | A time you weighed impact beyond your own project |

The last two were added in 2021 and come up mostly for people managers and senior roles. For an entry-level SDE loop, the first fourteen carry nearly all the questions, and **Customer Obsession, Ownership, Dive Deep, Deliver Results, Bias for Action, Earn Trust and Have Backbone** come up most often.

## 3. The Answer Pattern

### STAR, weighted toward Action and Result

| Part | Share of the answer | What to say |
|---|---|---|
| **Situation** | ~15% | The context in one or two sentences |
| **Task** | ~10% | Your responsibility, and the goal |
| **Action** | ~50% | What *you* did, step by step, and why |
| **Result** | ~25% | The outcome **in numbers**, and what you learned |

- **Lead with the headline** if the story is long: "I cut our build time from 20 minutes to 6 — here is how."
- **Numbers** are the currency of an Amazon result: latency, error rate, hours saved, users affected, days early. If you do not have an exact figure, give an honest estimate and say it is one.
- **"I", not "we"**. The interviewer will ask "what did *you* do?" until they find out; save them the question.
- **One story per question**, told for two or three minutes, then stop and let them probe.

### The follow-ups to prepare for

Interviewers are trained to dig two or three layers into a story. Prepare these for every story in your bank:

- What exactly did you do, and what did others do?
- What data did you look at? How did you know it worked?
- What alternatives did you consider, and why did you reject them?
- What would you do differently now?
- What did your manager or the other person think?
- What was the hardest part?

## 4. The Interviewer's Perspective

- **They take notes close to verbatim** and later tag each note with the LP it supports. The debrief reads the notes, so a vague answer produces a vague note and no data point.
- **They are assigned LPs**, so a question about "a time you disagreed" may be scoring Have Backbone *or* Earn Trust depending on who asks. Answer the question asked, and the LP follows.
- **They read every other interviewer's notes in the debrief.** Telling the same story to three interviewers looks like a candidate with one story. Use a different story in each round.
- **They ask for another example** when the first one does not show the principle. That is not a failure, but it costs time you cannot get back.
- **A hypothetical answer scores weakly.** "I would…" to a "tell me about a time" question gives them nothing to write down. If you truly have no example, say so, then give the closest real one.
- **The Bar Raiser asks whether you raise the bar**: would you be better than half the people already in the role? Strong, specific, recent stories are how that question gets answered yes.

What the notes usually show:

| Outcome | What the notes usually show |
|---|---|
| **Strong yes** | Several clear data points for each assigned LP, numbers in the results, honest reflection under probing |
| **Leaning yes** | The right instincts, but some answers needed a second example or stayed at "we" |
| **Leaning no** | Generic or hypothetical answers, results without numbers, the same story reused across rounds |
| **Strong no** | Blaming others, a story that changes under probing, or an action that contradicts the principle (cutting corners for Highest Standards, hiding a mistake for Earn Trust) |

## 5. Classic Questions by Principle

### Customer Obsession

- Tell me about a time you went above and beyond for a customer.
- Tell me about a time you had to balance a customer's request against what you thought was right for them.

**What they want to know** — who your customer was (for an engineer it may be another team, or the user of your API), how you found out what they needed, and whether you changed course for them.

**Strong signal** — you gathered evidence of the need (a ticket, a metric, a conversation) and the result is stated in the customer's terms.

### Ownership

- Tell me about a time you took on something outside your responsibilities.
- Tell me about a time you saw a problem nobody owned and fixed it.

**What they want to know** — whether you act like an owner when nobody is watching, including the boring follow-through after launch.

**Strong signal** — you noticed it, took it on without being asked, and made sure it stayed fixed.

### Invent and Simplify

- Tell me about a time you found a simpler way to do something.
- Tell me about the most innovative thing you have built.

**What they want to know** — whether you remove complexity rather than add it, and whether the new way was adopted.

### Are Right, A Lot

- Tell me about a decision you made with incomplete information.
- Tell me about a time you were wrong.

**What they want to know** — how you form judgment, whether you look for people who disagree, and how you update when you are wrong.

### Learn and Be Curious

- Tell me about something you taught yourself recently.
- Tell me about a time curiosity led you to a better solution.

**What they want to know** — whether you learn because the work needs it and because you want to, and whether it changed what you built.

### Hire and Develop the Best

- Tell me about a time you helped a teammate improve.
- Tell me about a time you gave someone difficult feedback.

**What they want to know** — at entry level: mentoring an intern or a classmate, onboarding someone, or writing the doc that helped the next person.

### Insist on the Highest Standards

- Tell me about a time you were not satisfied with the status quo.
- Tell me about a time you refused to compromise on quality.

**What they want to know** — whether you hold a bar when it is inconvenient, and whether you fix the cause, not only the symptom.

### Think Big

- Tell me about a time you proposed a bigger idea than the task required.
- Tell me about a long-term vision you set for a project.

**What they want to know** — whether you can see past the immediate ticket and bring others along with the bigger picture.

### Bias for Action

- Tell me about a time you made a decision quickly without all the data.
- Tell me about a time you took a calculated risk.

**What they want to know** — whether you can tell a reversible decision from an irreversible one, and move fast on the first.

**Strong signal** — you name why the decision was reversible and what you did to limit the downside.

### Frugality

- Tell me about a time you accomplished something with limited resources.
- Tell me about a time you saved time or money for the team.

**What they want to know** — whether constraints make you inventive rather than stuck.

### Earn Trust

- Tell me about a time you made a mistake and how you handled it.
- Tell me about a time you received critical feedback.

**What they want to know** — whether you admit mistakes quickly and openly, and treat others with respect when it is hard.

**Strong signal** — you raised the mistake yourself, before anyone found it, and fixed the process that allowed it.

### Dive Deep

- Tell me about a time you used data to find the root cause of a problem.
- Tell me about the hardest bug you have debugged.

**What they want to know** — whether you go below the surface metric, and whether you can explain the details when probed.

**Strong signal** — the interviewer can ask three "why"s and you still have specifics.

### Have Backbone; Disagree and Commit

- Tell me about a time you disagreed with your manager or team.
- Tell me about a time you committed to a decision you disagreed with.

**What they want to know** — whether you speak up with evidence, and whether you commit fully once the decision is made.

**Strong signal** — you disagreed respectfully, with data, and after the decision went against you, you made it succeed.

### Deliver Results

- Tell me about a time you delivered a project under a tight deadline.
- Tell me about a goal you did not meet.

**What they want to know** — whether you focus on what matters most, get past setbacks, and deliver the result rather than the activity.

**Strong signal** — the obstacle is real, the trade-off you made is explicit, and the outcome has a number.

### Strive to be Earth's Best Employer

- Tell me about a time you made your team a better place to work.

**What they want to know** — whether you look after the people around you: inclusion, workload, growth.

### Success and Scale Bring Broad Responsibility

- Tell me about a time you considered the wider impact of a decision.

**What they want to know** — whether you think about effects beyond your project: users, other teams, the community.

## 6. A Worked Example

**Question** — "Tell me about a time you used data to find the root cause of a problem." (*Dive Deep*)

> **Situation** — During my internship, our team's nightly data export started failing about one night in five, and the on-call engineer was re-running it by hand.
>
> **Task** — I was asked to make the job reliable before the end of my internship, six weeks out.
>
> **Action** — The failures all logged a timeout, so the obvious fix was a longer timeout. Instead, I pulled thirty days of job logs into a spreadsheet and lined them up against the database's load metrics. Every failure overlapped a weekly reindex job that someone had moved to the same hour. I confirmed it by running the export by hand during a reindex, which reproduced the timeout. I proposed moving the export an hour later instead of raising the timeout, wrote up the evidence for my manager and the reindex job's owner, and added an alert on export duration so a new overlap would show up before it failed.
>
> **Result** — The export ran for the remaining five weeks without a failure, which saved the on-call engineer about two hours a week. I learned to treat an obvious fix as a hypothesis and check the data before applying it.

Why it scores: the candidate did not stop at the symptom, the actions are specific and their own, the evidence is shown, the result has numbers, and a guard keeps it fixed (also a data point for **Ownership** and **Insist on the Highest Standards**).

## 7. Building a Story Bank

Eight to ten stories cover the loop, because each good story shows two or three principles. Map them before the interview so you never reuse a story across rounds.

| Story | Principles it can show |
|---|---|
| A bug or incident you traced to its root cause | Dive Deep, Ownership, Insist on the Highest Standards |
| A deadline you met despite a setback | Deliver Results, Bias for Action |
| A disagreement where you were overruled | Have Backbone; Disagree and Commit, Earn Trust |
| A mistake you admitted | Earn Trust, Ownership |
| A feature changed because of user feedback | Customer Obsession, Learn and Be Curious |
| A tool or script that removed manual work | Invent and Simplify, Frugality, Ownership |
| Someone you helped learn or onboard | Hire and Develop the Best, Earn Trust |
| A quick decision on a reversible choice | Bias for Action, Are Right, A Lot |
| A larger idea you proposed beyond the task | Think Big, Invent and Simplify |
| A goal you missed | Deliver Results, Earn Trust, Learn and Be Curious |

Your Google stories transfer: a Googleyness "disagreement" story is an Amazon **Have Backbone** story, and a "failure" story is an **Earn Trust** story. Amazon's probes go deeper into numbers and your individual actions, so add the metrics before you reuse one.

## 8. Checklist Before the Loop

- Eight to ten stories, mapped to principles, with no story planned for two rounds.
- Every result has a number; every story has a "what I would do differently".
- At least one failure, one disagreement, one customer story and one deep-dive story.
- The follow-ups in section 3 rehearsed for each story.
- Stories told in the first person, in two to three minutes each.
- Two questions for the interviewer about how their team works.
