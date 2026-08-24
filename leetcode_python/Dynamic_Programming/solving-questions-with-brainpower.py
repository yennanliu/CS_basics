"""

2140. Solving Questions With Brainpower
Medium

You are given a 0-indexed 2D integer array questions where questions[i] = [points_i, brainpower_i].

The array describes the questions of an exam, where you have to process the questions in order (i.e., starting from question 0) and make a decision whether to solve or skip each question. Solving question i will earn you points_i points but you will be unable to solve each of the next brainpower_i questions. If you skip question i, you get to make the decision on the next question.

For example, given questions = [[3, 2], [4, 3], [4, 4], [2, 5]]:
If question 0 is solved, you will earn 3 points but you will be unable to solve questions 1 and 2.
If instead, question 0 is skipped and question 1 is solved, you will earn 4 points but you will be unable to solve questions 2 and 3.

Return the maximum points you can earn for the exam.


Example 1:

Input: questions = [[3,2],[4,3],[4,4],[2,5]]
Output: 5
Explanation: The maximum points can be earned by solving questions 0 and 3.
- Solve question 0: Earn 3 points, will be unable to solve the next 2 questions
- Unable to solve questions 1 and 2
- Solve question 3: Earn 2 points
Total points earned: 3 + 2 = 5. There is no other way to earn 5 or more points.

Example 2:

Input: questions = [[1,1],[2,2],[3,3],[4,4],[5,5]]
Output: 7
Explanation: The maximum points can be earned by solving questions 1 and 4.
- Skip question 0
- Solve question 1: Earn 2 points, will be unable to solve the next 2 questions
- Unable to solve questions 2 and 3
- Solve question 4: Earn 5 points
Total points earned: 2 + 5 = 7. There is no other way to earn 7 or more points.


Constraints:

1 <= questions.length <= 10^5
questions[i].length == 2
1 <= points_i, brainpower_i <= 10^5

"""

# V0
# IDEA : SUFFIX DP — dp[i] = BEST SCORE ACHIEVABLE FROM QUESTION i ONWARDS
#
#   at question i there are exactly two choices :
#       skip  -> dp[i + 1]
#       solve -> points[i] + dp[i + brainpower[i] + 1]
#   so
#       dp[i] = max(dp[i + 1], points[i] + dp[min(n, i + brainpower[i] + 1)])
#
#   filling from the back makes both lookups already available. dp has n + 1
#   slots so the "jumped past the end" case reads a clean 0.
#
#   NOTE : scores can exceed 32-bit; python ints handle that for free.
#
"""

DP def
    dp[i]: BEST score achievable from question i onwards

DP eq

     dp[i] = max(
                dp[i+1],                                        # SKIP question i

                points[i] + dp[ min(n, i + brainpower[i] + 1) ] # SOLVE it
             )


    -> e.g. filling from the BACK makes both lookups already available, and
              dp has n + 1 slots so a "jumped past the end" read is a clean 0

     init: dp[n] = 0
     ans = dp[0]

"""
# time = O(n), space = O(n)
class Solution(object):
    def mostPoints(self, questions):
        n = len(questions)
        dp = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            points, brainpower = questions[i]
            nxt = min(n, i + brainpower + 1)
            dp[i] = max(dp[i + 1], points + dp[nxt])
        return dp[0]
