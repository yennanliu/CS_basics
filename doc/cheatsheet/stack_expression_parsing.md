# Stack — Expression Parsing

> **Scope** — The stack-based expression family: the calculators (LC 224 / 227 / 772), decode-string style nesting (LC 394) and postfix / sequential-operand evaluation (LC 150, 682), with the `pre_op` delay-insert trick that makes operator precedence fall out of a plain stack.
> **See also**: [stack.md](./stack.md) — the parent sheet: LIFO fundamentals and the canonical stack templates; [stack_examples.md](./stack_examples.md) — the worked-solution archive for the rest of the stack family; [string.md](./string.md) — general string scanning; [recursion.md](./recursion.md) — the recursive-descent view of the same parenthesis handling.

## LeetCode Problem Lists

- [Stack](https://leetcode.com/problem-list/stack/)
- [String](https://leetcode.com/problem-list/string/)
- [Math](https://leetcode.com/problem-list/math/)

## Overview

Every problem here is one left-to-right scan with a stack, and every one of them is some
combination of exactly **three** sub-problems:

| Sub-problem | Mechanism | Shows up in |
|---|---|---|
| **Multi-digit numbers** | `num = num * 10 + int(ch)` — never commit a digit on sight | all of them |
| **Precedence** (`*` `/` bind tighter than `+` `-`) | **delay-insert on `pre_op`**: push `±num` for `+`/`-`, pop-and-combine for `*`/`/`, answer = `sum(stack)` | LC 227, 772 |
| **Nesting** (parentheses, `k[...]`) | a `(` opens a scope — either **recurse** on the shared input, or **push** the outer state and reset | LC 224, 772, 394 |

**Postfix (LC 150) is the easy case**: the token order already encodes precedence and
nesting, so nothing needs deferring — number pushes, operator pops two.

### Key Properties
- **Complexity**: every algorithm here is O(n) time, O(n) space (stack depth = nesting depth); the per-operation costs of the structure are in the [Time Complexity](./stack.md#time-complexity) table in the parent sheet
- **Core Idea**: a stack turns "I cannot decide yet" into "I will decide when the *next* token arrives"
- **When to Use**: any single-pass evaluation of a string whose meaning depends on what comes next, or on which scope you are inside

## 1) Delay-Insert on `pre_op` — the Precedence Engine

**Key Insight**: When scanning an expression left-to-right, we can't decide what to do with the current number until we see the **next** operator — so we **delay** the push until then, acting on `pre_op`.

**Why?**
- `+` / `-` (low precedence): push `±num` directly, defer to final `sum(stack)`
- `*` / `/` (high precedence): pop last value and combine immediately — but we only know this **after** `num` is fully built and the next operator arrives

**Setup**:
- `pre_op = '+'` (init) — makes the first number push as positive automatically
- `num` accumulates digits; trigger fires on operator or end-of-string

**Visual trace — `"3+2*2"` → 7**:
```text
char  num  trigger?  pre_op  action               stack
'3'   3    no        '+'     —                    []
'+'   3    YES       '+'     push(3)  → pre_op='+' [3]
'2'   2    no        '+'     —                    [3]
'*'   2    YES       '+'     push(2)  → pre_op='*' [3, 2]
'2'   2    YES(end)  '*'     pop()→2, push(2*2=4)  [3, 4]
sum([3, 4]) = 7 ✓
```

The branch ladder itself is not repeated here — it is the inner `if/elif` chain of the
**universal calculator** in section 2, which is the canonical implementation of this idea.

**Related problems using this pattern**:

| LC | Problem | Variation |
|----|---------|-----------|
| 227 | Basic Calculator II | `+-*/`, no parentheses |
| 224 | Basic Calculator I | `+-()`, no `*/` |
| 772 | Basic Calculator III | `+-*/()` combined |
| 394 | Decode String | `pre_op` tracks repeat count before `[` |

## 2) Universal Calculator — LC 224 / 227 / 772 ⭐⭐⭐⭐⭐

> **One algorithm for all three calculator problems**: `+-` only (224), `+-*/` no parens (227), and `+-*/()` combined (772). Handles operator **precedence** with a stack and **parentheses** with recursion.

**Core Idea** — combine two independent tricks that each solve half the problem:

| Sub-problem | Trick | How it shows up |
|-------------|-------|-----------------|
| **Precedence** (`*` / `/` bind tighter than `+` / `-`) | **Delay-Insert on `pre_op`** (see [section 1](#1-delay-insert-on-pre_op--the-precedence-engine)) | `+`/`-` push the signed number onto the stack (defer); `*`/`/` immediately pop-and-combine with the top. Final answer = `sum(stack)`. |
| **Parentheses** (a sub-expression evaluated first) | **Recursion** — a `(` opens a fresh scope, a `)` closes it | On `(`, recurse on the *same* queue; the recursive call consumes up to its matching `)` and returns the sub-total, which is treated as a plain `curr_num`. |

**Why the stack handles precedence for free:** additive terms are deferred as signed values (`+num` → push `num`, `-num` → push `-num`), while multiplicative operators eat the previous term on the spot (`stack[-1] *= num`). Because `*`/`/` mutate the top *before* it is ever summed, `sum(stack)` at the end naturally respects precedence — e.g. `2 + 3 * 4` builds `[2, 12]` → `14`, not `20`.

**Why we act on `pre_op`, not the current char:** when we read an operator (or hit `)` / end-of-input) it marks the *end* of the number we were building, so we apply the operator that came *before* that number. `pre_op` is initialized to `'+'` so the very first number is simply pushed.

**Why a `deque` + recursion cleanly handles parens:** `popleft()` consumes characters left-to-right and the queue is **shared across recursive calls**. When `helper` recurses on `(`, the child keeps popping from the *same* queue and `break`s on `)`, so the parent resumes exactly after the matching `)`. This is what upgrades the LC 227 delay-insert solution into a full LC 772 solver.

```python
# python
# LC 224 / 227 / 772 — universal basic calculator
# IDEA: deque + recursion (parentheses) + delay-insert on pre_op (precedence)
# time = O(n), space = O(n)  (stack + recursion depth)
import collections

class Solution(object):
    def calculate(self, s):
        # strip spaces, scan left-to-right with a shared queue
        queue = collections.deque(s.replace(" ", ""))

        def helper(q):
            stack = []
            curr_num = 0
            op = '+'                     # operator that precedes curr_num; '+' by default

            while q:
                char = q.popleft()

                if char.isdigit():
                    curr_num = curr_num * 10 + int(char)   # build multi-digit number
                elif char == '(':
                    curr_num = helper(q)   # RECURSE: fully evaluate the parenthesised scope

                # flush when we see an operator, a ')', or run out of input
                if char in "+-*/" or char == ')' or not q:
                    if op == '+':
                        stack.append(curr_num)
                    elif op == '-':
                        stack.append(-curr_num)
                    elif op == '*':
                        stack.append(stack.pop() * curr_num)
                    elif op == '/':
                        # truncate toward zero (Python // floors, so divide as float)
                        stack.append(int(float(stack.pop()) / curr_num))
                    curr_num = 0
                    op = char            # remember this operator for the next number

                if char == ')':
                    break                # end of this scope → return sub-total to caller

            return sum(stack)

        return helper(queue)
```

**How it degrades to each problem:**

| LC | Chars present | What the algo does |
|----|---------------|--------------------|
| 224 | `+ - ( )` | recursion + push/negate only; `*`/`/` branches never fire |
| 227 | `+ - * / ` | never recurses (no `(`); pure delay-insert precedence |
| 772 | `+ - * / ( )` | both mechanisms active — the general case |

**Gotcha — integer division truncates toward zero:** Python's `//` floors (`-7 // 2 == -4`), but these problems require truncation toward zero (`-7 / 2 == -3`). Using `int(float(stack.pop()) / curr_num)` gives the correct behavior for negative intermediates.

### Variation — Running-Result Form (no operand stack)

> **Kept as a second solution to LC 224 on purpose**: it is a *different* formulation, not a
> different spelling. Instead of an operand stack it carries a running `res` plus a `sign`, and
> the stack holds only the **suspended `(res, sign)` of each open parenthesis** — so it needs no
> recursion, and its stack depth is the parenthesis depth alone. It does not generalise to
> `*` / `/`, which is exactly why the universal form above is the one to memorise.

```python
# LC 224 Basic Calculator
# V0'
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator/solution/
class Solution:
    def calculate(self, s):

        stack = []
        operand = 0
        res = 0 # For the on-going result
        sign = 1 # 1 means positive, -1 means negative  

        for ch in s:
            if ch.isdigit():

                # Forming operand, since it could be more than one digit
                operand = (operand * 10) + int(ch)

            elif ch == '+':

                # Evaluate the expression to the left,
                # with result, sign, operand
                res += sign * operand

                # Save the recently encountered '+' sign
                sign = 1

                # Reset operand
                operand = 0

            elif ch == '-':

                res += sign * operand
                sign = -1
                operand = 0

            elif ch == '(':

                # Push the result and sign on to the stack, for later
                # We push the result first, then sign
                stack.append(res)
                stack.append(sign)

                # Reset operand and result, as if new evaluation begins for the new sub-expression
                sign = 1
                res = 0

            elif ch == ')':

                # Evaluate the expression to the left
                # with result, sign and operand
                res += sign * operand

                # ')' marks end of expression within a set of parenthesis
                # Its result is multiplied with sign on top of stack
                # as stack.pop() is the sign before the parenthesis
                res *= stack.pop() # stack pop 1, sign

                # Then add to the next operand on the top.
                # as stack.pop() is the result calculated before this parenthesis
                # (operand on stack) + (sign on stack * (result from parenthesis))
                res += stack.pop() # stack pop 2, operand

                # Reset the operand
                operand = 0

        return res + sign * operand
```

## 3) Decode String — LC 394 ⭐⭐⭐⭐

> **The nesting half of the calculator, without arithmetic.** `k[...]` is a scope: on `[` push
> the outer `(string, count)` and reset, on `]` pop them and fold `prev + count * cur` back.
> Same four-case scan (`digit` / `[` / `letter` / `]`) as LC 726 (Number of Atoms) and LC 385
> (Mini Parser).

```python
# LC 394 Decode String
# V0
# IDEA : STACK
# NOTE : treat before cases separately
#        1) isdigit
#        2) isalpha
#        3) "["
#        4) "]"
# and define num = 0 for dealing with "100a[b]", "10abc" cases
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        """
        NOTE : we deal with 4 cases
            1) digit
            2) "["
            3) alphabet
            4) "]"

        NOTE :
            we use pre_num, pre_string for dealing with previous result
        """
        for c in s:
            # case 1) : digit
            if c.isdigit():
                num = num*10 + int(c)
            # case 2) : "["
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            # case 3) : alphabet
            elif c.isalpha():
                string += c
            # case 4) "]"
            elif c == ']':
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string
```

```java
// java
// LC 394 Decode String

/**
 * Problem: Given an encoded string, return its decoded string.
 *
 * Encoding rule: k[encoded_string] means repeat encoded_string k times
 *
 * Examples:
 * - "3[a]2[bc]" → "aaabcbc"
 * - "3[a2[c]]" → "accaccacc"
 * - "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * Key Insight:
 * - Use stack to handle nested brackets
 * - Process 4 cases: digit, '[', letter, ']'
 * - Build number incrementally (e.g., "100" = 1*10 + 0*10 + 0)
 * - On ']': pop count and previous string, build result
 *
 * Time: O(maxK * N) where maxK is max k value and N is length of decoded string
 * Space: O(N) for the stack
 */

// V0
// IDEA: STACK + 4 CASES (digit, '[', letter, ']')
public String decodeString(String s) {
    if (s == null || s.length() == 0) {
        return "";
    }

    /**
     * NOTE !!!
     * Stack stores alternating pattern:
     * - String (previous accumulated string)
     * - Integer (repeat count)
     * - String (next accumulated string)
     * - Integer (next repeat count)
     * ...
     *
     * Example for "3[a2[c]]":
     * When processing '2[c]':
     *   Stack bottom: ["", 3, "a", 2] Stack top
     */
    Stack<Object> stack = new Stack<>();

    int num = 0;              // Current number being built
    String currentString = ""; // Current string being built

    for (char c : s.toCharArray()) {

        /**
         * Case 1: Digit
         * Build multi-digit numbers (e.g., "100")
         */
        if (Character.isDigit(c)) {
            num = num * 10 + (c - '0');
        }

        /**
         * Case 2: '['
         * Push current string and number to stack
         * Reset for new nested level
         */
        else if (c == '[') {
            // Push current string first, then number
            stack.push(currentString);
            stack.push(num);

            // Reset for new level
            currentString = "";
            num = 0;
        }

        /**
         * Case 3: Letter
         * Append to current string
         */
        else if (Character.isLetter(c)) {
            currentString += c;
        }

        /**
         * Case 4: ']'
         * Pop count and previous string
         * Build repeated string and concatenate
         */
        else if (c == ']') {
            // Pop in reverse order of push
            int repeatCount = (int) stack.pop();
            String prevString = (String) stack.pop();

            /**
             * NOTE !!!
             * Repeat current string repeatCount times
             * Then prepend previous string
             */
            StringBuilder temp = new StringBuilder(prevString);
            for (int i = 0; i < repeatCount; i++) {
                temp.append(currentString);
            }

            currentString = temp.toString();
        }
    }

    return currentString;
}

/**
 * Example Walkthrough: s = "3[a2[c]]"
 *
 * Step 1: c='3' (digit)
 *   num = 3
 *
 * Step 2: c='[' (open bracket)
 *   stack.push("") → stack: [""]
 *   stack.push(3)  → stack: ["", 3]
 *   currentString = "", num = 0
 *
 * Step 3: c='a' (letter)
 *   currentString = "a"
 *
 * Step 4: c='2' (digit)
 *   num = 2
 *
 * Step 5: c='[' (open bracket)
 *   stack.push("a") → stack: ["", 3, "a"]
 *   stack.push(2)   → stack: ["", 3, "a", 2]
 *   currentString = "", num = 0
 *
 * Step 6: c='c' (letter)
 *   currentString = "c"
 *
 * Step 7: c=']' (close bracket)
 *   repeatCount = stack.pop() = 2
 *   prevString = stack.pop() = "a"
 *   temp = "a" + "c" * 2 = "acc"
 *   currentString = "acc"
 *   stack: ["", 3]
 *
 * Step 8: c=']' (close bracket)
 *   repeatCount = stack.pop() = 3
 *   prevString = stack.pop() = ""
 *   temp = "" + "acc" * 3 = "accaccacc"
 *   currentString = "accaccacc"
 *   stack: []
 *
 * Result: "accaccacc"
 */

/**
 * Common Mistakes:
 *
 * 1. Not handling multi-digit numbers (e.g., "100[a]")
 *    ✗ num = c - '0'
 *    ✓ num = num * 10 + (c - '0')
 *
 * 2. Wrong stack push/pop order
 *    ✗ push(num, string) → pop(string, num)  // Wrong!
 *    ✓ push(string, num) → pop(num, string)  // Correct LIFO
 *
 * 3. Forgetting to reset num and currentString after '['
 *    ✗ Only reset one of them
 *    ✓ Reset both: num = 0; currentString = "";
 *
 * 4. Not handling strings outside brackets (e.g., "2[abc]3[cd]ef")
 *    ✓ Continue building currentString for letters outside brackets
 *
 * 5. Using Stack<Object> without proper casting
 *    ✓ Use separate stacks (countStack, stringStack) for type safety
 */

/**
 * Interview Tips:
 *
 * 1. Clarify constraints:
 *    - Is input always valid? (no unmatched brackets)
 *    - Max value of k? (affects overflow considerations)
 *
 * 2. Edge cases to test:
 *    - No brackets: "abc" → "abc"
 *    - Nested brackets: "2[a2[b]]" → "abbabb"
 *    - Multi-digit numbers: "100[a]"
 *    - Mixed: "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * 3. Follow-up questions:
 *    - What if string is invalid? (add validation)
 *    - Can we decode in-place? (no, need stack for nesting)
 *    - How to handle very large k values? (streaming approach)
 */
```

## 4) Operand Stack — Postfix / Sequential Ops — LC 150 ⭐⭐⭐⭐

> **Contrast with LC 224 / 227 / 772** (section [2](#2-universal-calculator--lc-224--227--772-)): those parse **infix** and must handle precedence + parentheses. **Postfix (RPN) has no precedence and no parentheses** — the token order already encodes it, so the whole algorithm is *"number → push; operator → pop two, combine, push back"*.

```text
Core Idea:
  - token is a NUMBER   -> push
  - token is an OPERATOR-> pop b (right), pop a (left), push f(a, b)
  - answer = the single value left on the stack

Watch-outs:
  - ORDER MATTERS for `-` and `/`: the FIRST pop is the RIGHT operand
    -> a = second pop, b = first pop, compute a - b / a / b
  - Integer division TRUNCATES TOWARD ZERO ("-7 / 2 == -3", not -4)
    -> Java `/` already does this; Python `//` FLOORS, so use int(a / b)
  - A leading '-' can be part of a number ("-11"), not an operator
    -> test membership in the operator SET, don't test `startswith('-')`

Similar LC:
  - LC 150  Evaluate Reverse Polish Notation (canonical operand stack)
  - LC 682  Baseball Game (same stack, ops act on the LAST 1-2 records)
```

```java
// java
// LC 150 - Evaluate Reverse Polish Notation
// IDEA: OPERAND STACK — number pushes, operator pops two and pushes the result
// time = O(n), space = O(n)
public int evalRPN(String[] tokens) {

    Deque<Integer> st = new ArrayDeque<>();

    for (String t : tokens) {
        if (t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/")) {
            /**
             *  NOTE !!!  the FIRST pop is the RIGHT operand
             *  -> "a - b" and "a / b", NOT "b - a"
             */
            int b = st.pop();
            int a = st.pop();
            if (t.equals("+")) {
                st.push(a + b);
            } else if (t.equals("-")) {
                st.push(a - b);
            } else if (t.equals("*")) {
                st.push(a * b);
            } else {
                st.push(a / b); // java int division truncates toward zero
            }
        } else {
            /** NOTE !!! handles negative literals like "-11" for free */
            st.push(Integer.parseInt(t));
        }
    }

    return st.pop();
}
```

```python
# python
# LC 150 - Evaluate Reverse Polish Notation
# IDEA: OPERAND STACK — number pushes, operator pops two and pushes the result
# time = O(n), space = O(n)
class Solution(object):
    def evalRPN(self, tokens):
        ops = {'+', '-', '*', '/'}
        stack = []
        for t in tokens:
            if t in ops:
                # NOTE !!! first pop = RIGHT operand
                b = stack.pop()
                a = stack.pop()
                if t == '+':
                    stack.append(a + b)
                elif t == '-':
                    stack.append(a - b)
                elif t == '*':
                    stack.append(a * b)
                else:
                    # NOTE !!! truncate toward zero ( // would FLOOR )
                    stack.append(int(a / b))
            else:
                stack.append(int(t))   # int() also parses "-11"
        return stack[-1]
```

### Variation — Operators Act on the Last Records — LC 682

> **Twist**: same operand stack, but the "operators" are record edits — `C` undoes (pop), `D` doubles the top, `+` sums the top two — and the answer is `sum(stack)` rather than the single remaining value.

```java
// java
// LC 682 - Baseball Game
// IDEA: OPERAND STACK — C / D / '+' rewrite the tail of the record list
// time = O(n), space = O(n)
public int calPoints(String[] operations) {
    // NOTE: use a List as the stack — '+' needs the last TWO entries
    List<Integer> scores = new ArrayList<>();
    for (String op : operations) {
        int n = scores.size();
        if (op.equals("C")) {
            scores.remove(n - 1);                       // undo last
        } else if (op.equals("D")) {
            scores.add(2 * scores.get(n - 1));          // double last
        } else if (op.equals("+")) {
            scores.add(scores.get(n - 1) + scores.get(n - 2)); // sum last two
        } else {
            scores.add(Integer.parseInt(op));
        }
    }
    int sum = 0;
    for (int x : scores) {
        sum += x;
    }
    return sum;
}
```

```python
# python
# LC 682 - Baseball Game
# IDEA: OPERAND STACK — C / D / '+' rewrite the tail of the record list
# time = O(n), space = O(n)
class Solution(object):
    def calPoints(self, operations):
        stack = []
        for op in operations:
            if op == 'C':
                stack.pop()                       # undo last
            elif op == 'D':
                stack.append(2 * stack[-1])       # double last
            elif op == '+':
                stack.append(stack[-1] + stack[-2])  # sum last two
            else:
                stack.append(int(op))
        return sum(stack)
```

## 5) Summary & Quick Reference

### Which Formulation for Which Problem?

| LC | Problem | Tokens | Mechanism |
|----|---------|--------|-----------|
| 227 | Basic Calculator II | `+ - * /` | delay-insert on `pre_op`; never recurses |
| 224 | Basic Calculator I | `+ - ( )` | universal form, or the running-result variation |
| 772 | Basic Calculator III | `+ - * / ( )` | universal form — both mechanisms active |
| 394 | Decode String | `k[ ]`, letters | push `(string, count)` per scope, fold on `]` |
| 726 | Number of Atoms | `( )`, digits, names | LC 394's scan with a count map per scope |
| 385 | Mini Parser | `[ ]`, digits, `,` | LC 394's scan, stack holds `NestedInteger` frames |
| 150 | Evaluate RPN | postfix | operand stack: operator pops two |
| 682 | Baseball Game | records + `C` / `D` / `+` | operand stack, ops rewrite the tail |

### Traps Worth Rehearsing

| Trap | Fix |
|---|---|
| Multi-digit numbers | `num = num * 10 + int(ch)`, and reset `num = 0` after every flush |
| The **last** number never flushes | make end-of-input a trigger too (`or not q`, `i == len(s) - 1`) |
| `-7 / 2` must be `-3`, not `-4` | Python `//` **floors** — use `int(a / b)` / `int(float(x) / y)` |
| RPN operand order | the **first** pop is the **right** operand |
| Spaces in the input | strip them up front (`s.replace(" ", "")`) rather than guarding every branch |
| A leading `-` inside a token | test membership in the operator **set**; `int("-11")` parses fine |

