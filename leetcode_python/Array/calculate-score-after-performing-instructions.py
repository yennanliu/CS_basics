"""

3522. Calculate Score After Performing Instructions
Medium

You are given two arrays, instructions and values, both of size n.

You need to simulate a process based on the following rules:

You start at the first instruction at index i = 0 with an initial score of 0.

If instructions[i] is "add":

Add values[i] to your score.

Move to the next instruction (i + 1).

If instructions[i] is "jump":

Move to the instruction at index (i + values[i]) without modifying your score.

The process ends when you either:

Go out of bounds (i.e., i < 0 or i >= n), or

Attempt to revisit an instruction that has been previously executed. The
revisited instruction is not executed.

Return your score at the end of the process.

Example 1:

Input: instructions = ["jump","add","add","jump","add","jump"], values =
[2,1,3,1,-2,-3]

Output: 1

Explanation:

Simulate the process starting at instruction 0:

At index 0: Instruction is "jump", move to index 0 + 2 = 2.

At index 2: Instruction is "add", add values[2] = 3 to your score and move to
index 3. Your score becomes 3.

At index 3: Instruction is "jump", move to index 3 + 1 = 4.

At index 4: Instruction is "add", add values[4] = -2 to your score and move to
index 5. Your score becomes 1.

At index 5: Instruction is "jump", move to index 5 + (-3) = 2.

At index 2: Already visited. The process ends.

Example 2:

Input: instructions = ["jump","add","add"], values = [3,1,1]

Output: 0

Explanation:

Simulate the process starting at instruction 0:

At index 0: Instruction is "jump", move to index 0 + 3 = 3.

At index 3: Out of bounds. The process ends.

Example 3:

Input: instructions = ["jump"], values = [0]

Output: 0

Explanation:

Simulate the process starting at instruction 0:

At index 0: Instruction is "jump", move to index 0 + 0 = 0.

At index 0: Already visited. The process ends.

Constraints:

n == instructions.length == values.length

1 <= n <= 10^5

instructions[i] is either "add" or "jump".

-10^5 <= values[i] <= 10^5

"""

# V0
# IDEA : SIMULATION WITH A VISITED SET
#
#   the walk is deterministic, so once an index repeats the process would loop
#   forever — that is exactly why the problem stops there.  marking visited
#   indices bounds the walk at n steps.
#
#   note the visit is marked when the instruction is *executed*, so the very
#   first index counts as visited before any jump can land on it again.
#
# time = O(n), space = O(n)
class Solution(object):
    def calculateScore(self, instructions, values):
        n = len(instructions)
        seen = [False] * n
        i = 0
        score = 0
        while 0 <= i < n and not seen[i]:
            seen[i] = True
            if instructions[i] == "add":
                score += values[i]
                i += 1
            else:
                i += values[i]
        return score


# V0-1
# IDEA : RECURSION (DFS down the single outgoing edge)
#
#   every index has exactly one successor, so the walk reads naturally as a
#   recursion :
#       "add"  -> values[i] + go(i + 1)
#       "jump" -> go(i + values[i])
#   and the base cases are "off the board" or "already visited", both worth 0.
#
#   CAVEAT : the path can be n nodes long, so the recursion depth is O(n) and
#   the limit has to be raised for the largest inputs -- the iterative V0 is
#   what you would actually ship.  kept here because it is the clearest
#   statement of the walk.
#
# time  = O(n)
# space = O(n) for the visited set + O(n) recursion depth
import sys

class Solution(object):
    def calculateScore(self, instructions, values):
        n = len(instructions)
        sys.setrecursionlimit(max(2000, 2 * n + 100))
        seen = set()

        def go(i):
            if not (0 <= i < n) or i in seen:
                return 0
            seen.add(i)
            if instructions[i] == "add":
                return values[i] + go(i + 1)
            return go(i + values[i])

        return go(0)


# V0-2
# IDEA : FLOYD CYCLE DETECTION -> O(1) EXTRA SPACE
#
#   the successor of i depends only on i, so the walk lives in a functional
#   graph and has the classic "rho" shape : a tail of mu nodes leading into a
#   cycle of length lambda -- unless it drops off the board first.  the process
#   stops at the FIRST repeated index, i.e. after exactly mu + lambda distinct
#   nodes, so the whole answer is "sum the add-values over the first
#   mu + lambda nodes".
#
#     phase 1 : tortoise/hare -- either a pointer leaves the board (no cycle
#               at all, so the plain walk terminates on its own) or they meet
#     phase 2 : restart one pointer at 0 and step both -> they meet at the
#               cycle entry, mu steps in
#     phase 3 : go round once from there -> lambda
#
#   this trades V0's O(n) visited array for a handful of integers.
#
# time  = O(n)
# space = O(1)
class Solution(object):
    def calculateScore(self, instructions, values):
        n = len(instructions)

        def nxt(i):
            """the single successor of i, or None once we leave the board"""
            j = i + 1 if instructions[i] == "add" else i + values[i]
            return j if 0 <= j < n else None

        def walk_sum(steps):
            """sum the add-values of the first `steps` nodes, starting at 0"""
            total = 0
            i = 0
            for _ in range(steps):
                if instructions[i] == "add":
                    total += values[i]
                i = nxt(i)
                if i is None:
                    break
            return total

        # phase 1 : is there a cycle at all ?
        slow = fast = 0
        while True:
            slow = nxt(slow)
            if slow is None:
                return walk_sum(n)          # no cycle : at most n nodes
            fast = nxt(fast)
            if fast is None:
                return walk_sum(n)
            fast = nxt(fast)
            if fast is None:
                return walk_sum(n)
            if slow == fast:
                break

        # phase 2 : mu = distance from the start to the cycle entry
        mu = 0
        a, b = 0, slow
        while a != b:
            a = nxt(a)
            b = nxt(b)
            mu += 1

        # phase 3 : lam = cycle length
        lam = 1
        c = nxt(a)
        while c != a:
            c = nxt(c)
            lam += 1

        return walk_sum(mu + lam)
