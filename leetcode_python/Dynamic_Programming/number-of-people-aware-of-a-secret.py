"""

2327. Number of People Aware of a Secret
Medium

On day 1, one person discovers a secret.

You are given an integer delay, which means that each person will share the secret with a new person every day, starting from delay days after discovering the secret. You are also given an integer forget, which means that each person will forget the secret forget days after discovering it. A person cannot share the secret on the same day they forgot it, or on any day afterwards.

Given an integer n, return the number of people who know the secret at the end of day n. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 6, delay = 2, forget = 4
Output: 5
Explanation:
Day 1: Suppose the first person is named A. (1 person)
Day 2: A is the only person who knows the secret. (1 person)
Day 3: A shares the secret with a new person, B. (2 people)
Day 4: A shares the secret with a new person, C. (3 people)
Day 5: A forgets the secret, and B shares the secret with a new person, D. (3 people)
Day 6: B shares the secret with E, and C shares the secret with F. (5 people)

Example 2:

Input: n = 4, delay = 1, forget = 3
Output: 6
Explanation:
Day 1: The first person is named A. (1 person)
Day 2: A shares the secret with B. (2 people)
Day 3: A and B share the secret with 2 new people, C and D. (4 people)
Day 4: A, B, C, and D share the secret with 2 new people, E and F. (6 people)


Constraints:

2 <= n <= 1000
1 <= delay < forget <= n

"""

# V0
# IDEA : DP ON "HOW MANY PEOPLE LEARN THE SECRET ON DAY i"
#
#   let dp[i] be the number of people who FIRST hear it on day i, with
#   dp[1] = 1. someone who learned on day j is actively sharing on days
#       j + delay  ..  j + forget - 1
#   so, looking backwards from day i, its new listeners come from
#       dp[i] = sum of dp[j] for j in [i - forget + 1, i - delay]
#
#   maintaining that as a SLIDING WINDOW sum keeps it O(n) instead of O(n^2):
#   day i adds dp[i - delay] to the window and retires dp[i - forget].
#
#   at the end, the people who still remember are those who learned within
#   the last `forget` days : sum of dp[n - forget + 1 .. n].
#
"""

DP def
    dp[i]: number of people who FIRST hear the secret on day i

           -> dp[1] = 1

    someone who learned on day j is actively SHARING on days
    j + delay .. j + forget - 1

DP eq

     dp[i] = sum of dp[j]   for j in [i - forget + 1, i - delay]


    -> e.g. maintain that as a SLIDING WINDOW sum -> O(n) instead of O(n^2):
              day i ADDS dp[i - delay] and RETIRES dp[i - forget]

     at the end, the people who still REMEMBER are those who learned within
     the last `forget` days:

     ans = sum of dp[max(1, n - forget + 1) .. n]    mod 10^9 + 7

"""
# time = O(n), space = O(n)
class Solution(object):
    def peopleAwareOfSecret(self, n, delay, forget):
        MOD = 10 ** 9 + 7
        dp = [0] * (n + 1)
        dp[1] = 1

        window = 0                       # sum of dp[j] for the currently sharing j
        for i in range(2, n + 1):
            if i - delay >= 1:
                window = (window + dp[i - delay]) % MOD
            if i - forget >= 1:
                window = (window - dp[i - forget]) % MOD
            dp[i] = window

        return sum(dp[max(1, n - forget + 1):n + 1]) % MOD
