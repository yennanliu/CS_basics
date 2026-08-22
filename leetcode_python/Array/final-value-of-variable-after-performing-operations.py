"""

2011. Final Value of Variable After Performing Operations
Easy

There is a programming language with only four operations and one variable X:

++X and X++ increments the value of the variable X by 1.
--X and X-- decrements the value of the variable X by 1.

Initially, the value of X is 0.

Given an array of strings operations containing a list of operations, return the final value of X after performing all the operations.


Example 1:

Input: operations = ["--X","X++","X++"]
Output: 1
Explanation: The operations are performed as follows:
Initially, X = 0.
--X: X is decremented by 1, X =  0 - 1 = -1.
X++: X is incremented by 1, X = -1 + 1 =  0.
X++: X is incremented by 1, X =  0 + 1 =  1.

Example 2:

Input: operations = ["++X","++X","X++"]
Output: 3
Explanation: The operations are performed as follows:
Initially, X = 0.
++X: X is incremented by 1, X = 0 + 1 = 1.
++X: X is incremented by 1, X = 1 + 1 = 2.
X++: X is incremented by 1, X = 2 + 1 = 3.

Example 3:

Input: operations = ["X++","++X","--X","X--"]
Output: 0
Explanation: The operations are performed as follows:
Initially, X = 0.
X++: X is incremented by 1, X = 0 + 1 = 1.
++X: X is incremented by 1, X = 1 + 1 = 2.
--X: X is decremented by 1, X = 2 - 1 = 1.
X--: X is decremented by 1, X = 1 - 1 = 0.


Constraints:

1 <= operations.length <= 100
operations[i] will be either "++X", "X++", "--X", or "X--".

"""

# V0
# IDEA : LOOK AT THE MIDDLE CHARACTER ONLY
#
#   every op is 3 chars and the operator always occupies index 1
#   ("++X", "X++", "--X", "X--"), so op[1] == '+' means +1 and '-' means -1.
#   order is irrelevant, just sum the deltas.
#
# time = O(n), space = O(1)
class Solution(object):
    def finalValueAfterOperations(self, operations):
        res = 0
        for op in operations:
            if op[1] == '+':
                res += 1
            else:
                res -= 1
        return res


# V0-1
# IDEA : TALLY THE FOUR TOKENS WITH A COUNTER, THEN COMBINE THE COUNTS
#
#   the input alphabet is only four strings, so hash the whole tokens once and
#   read the four tallies off the map instead of inspecting characters:
#   (# "++X" + # "X++") - (# "--X" + # "X--").
#
#   the loop moves into Counter's C level and the arithmetic becomes a single
#   expression -- useful shape when the token set is later extended (a new
#   operation is one more term, not a new branch in the scan).
#
# time = O(n), space = O(1)   (at most 4 distinct keys)
import collections

class Solution(object):
    def finalValueAfterOperations(self, operations):
        cnt = collections.Counter(operations)
        return (cnt["++X"] + cnt["X++"]) - (cnt["--X"] + cnt["X--"])


# V0-2
# IDEA : ARITHMETIC -- COUNT ONLY THE DECREMENTS AND DERIVE THE REST
#
#   every operation moves X by exactly +1 or -1, so with n operations of which
#   d are decrements the answer is (n - d) - d = n - 2 * d.  no running total
#   and no per-op branch: count one class and let arithmetic supply the other.
#
# time = O(n), space = O(1)
class Solution(object):
    def finalValueAfterOperations(self, operations):
        d = sum(1 for op in operations if "-" in op)
        return len(operations) - 2 * d
