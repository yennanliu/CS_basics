"""

2585. Number of Ways to Earn Points
Hard

There is a test that has n types of questions. You are given an integer target and a 0-indexed
2D integer array types where types[i] = [counti, marksi] indicates that there are counti questions
of the ith type, and each one of them is worth marksi points.

Return the number of ways you can earn exactly target points in the exam.
Since the answer may be too large, return it modulo 10^9 + 7.

Note that questions of the same type are indistinguishable.

For example, if there are 3 questions of the same type, then solving the 1st and 2nd questions is
the same as solving the 1st and 3rd questions, or the 2nd and 3rd questions.


Example 1:

Input: target = 6, types = [[6,1],[3,2],[2,3]]
Output: 7
Explanation: You can earn 6 points in one of the seven ways:
- Solve 6 questions of the 0th type: 1 + 1 + 1 + 1 + 1 + 1 = 6
- Solve 4 questions of the 0th type and 1 question of the 1st type: 1 + 1 + 1 + 1 + 2 = 6
- Solve 2 questions of the 0th type and 2 questions of the 1st type: 1 + 1 + 2 + 2 = 6
- Solve 3 questions of the 0th type and 1 question of the 2nd type: 1 + 1 + 1 + 3 = 6
- Solve 1 question of the 0th type, 1 question of the 1st type and 1 question of the 2nd type: 1 + 2 + 3 = 6
- Solve 3 questions of the 1st type: 2 + 2 + 2 = 6
- Solve 2 questions of the 2nd type: 3 + 3 = 6

Example 2:

Input: target = 5, types = [[50,1],[50,2],[50,5]]
Output: 4
Explanation: You can earn 5 points in one of the four ways:
- Solve 5 questions of the 0th type: 1 + 1 + 1 + 1 + 1 = 5
- Solve 3 questions of the 0th type and 1 question of the 1st type: 1 + 1 + 1 + 2 = 5
- Solve 1 questions of the 0th type and 2 questions of the 1st type: 1 + 2 + 2 = 5
- Solve 1 question of the 2nd type: 5

Example 3:

Input: target = 18, types = [[6,1],[3,2],[2,3]]
Output: 1
Explanation: You can only earn 18 points by answering all questions.


Constraints:

1 <= target <= 1000
n == types.length
1 <= n <= 50
types[i].length == 2
1 <= counti, marksi <= 50

"""

# V0
# IDEA : BOUNDED KNAPSACK DP (group / multiple knapsack)
#
#   questions of the same type are indistinguishable, so a "way" is just the
#   vector (how many of type 0, how many of type 1, ...). That is a BOUNDED
#   knapsack counting problem: for each type we may take k = 0 .. count copies.
#
#   f[j] = number of ways to reach EXACTLY j points using the types seen so far.
#   base : f[0] = 1 (one way to score 0 : answer nothing).
#   transition for a type (count, marks):
#       new_f[j] = sum over k in 0..count of f[j - k * marks]   (j - k*marks >= 0)
#
#   NOTE : the new layer MUST be written into a fresh array `nf`. Updating f in
#          place would let a single type be re-used more than `count` times,
#          turning it into the UNBOUNDED knapsack and over-counting.
#
#   NOTE : the k-loop breaks as soon as k * marks > j, which is what keeps the
#          worst case at n * target * count = 50 * 1000 * 50 = 2.5 * 10^6.
#
# time = O(n * target * count), space = O(target)
class Solution(object):
    def waysToReachTarget(self, target, types):
        MOD = 10 ** 9 + 7
        f = [0] * (target + 1)
        f[0] = 1
        for count, marks in types:
            nf = [0] * (target + 1)
            for j in range(target + 1):
                total = 0
                for k in range(count + 1):
                    prev = j - k * marks
                    if prev < 0:
                        break
                    total += f[prev]
                nf[j] = total % MOD
            f = nf
        return f[target]
