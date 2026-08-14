"""

2024. Maximize the Confusion of an Exam
Medium

A teacher is writing a test with n true/false questions, with 'T' denoting true and 'F' denoting false. He wants to confuse the students by maximizing the number of consecutive questions with the same answer (multiple trues or multiple falses in a row).

You are given a string answerKey, where answerKey[i] is the original answer to the ith question. In addition, you are given an integer k, the maximum number of times you may perform the following operation:

Change the answer key for any question to 'T' or 'F' (i.e., set answerKey[i] to 'T' or 'F').

Return the maximum number of consecutive 'T's or 'F's in the answer key after performing the operation at most k times.


Example 1:

Input: answerKey = "TTFF", k = 2
Output: 4
Explanation: We can replace both the 'F's with 'T's to make answerKey = "TTTT".
There are four consecutive 'T's.

Example 2:

Input: answerKey = "TFFT", k = 1
Output: 3
Explanation: We can replace the first 'T' with an 'F' to make answerKey = "FFFT".
Alternatively, we can replace the second 'T' with an 'F' to make answerKey = "TFFF".
In both cases, there are three consecutive 'F's.

Example 3:

Input: answerKey = "TTFTTFTT", k = 1
Output: 5
Explanation: We can replace the first 'F' to make answerKey = "TTTTTFTT"
Alternatively, we can replace the second 'F' to make answerKey = "TTFTTTTT".
In both cases, there are five consecutive 'T's.


Constraints:

n == answerKey.length
1 <= n <= 5 * 10^4
answerKey[i] is either 'T' or 'F'
1 <= k <= n

"""

# V0
# IDEA : SLIDING WINDOW, TWICE (same as LC 424 "longest repeating char replacement")
#
#   run the window once with 'T' as the target letter, once with 'F'.
#   for a fixed target, a window is valid iff the count of the OTHER letter
#   is <= k (those are the flips we spend). grow right, shrink left while
#   the window is invalid, track the max width.
#
#   NOTE : doing two independent passes is simpler (and just as fast) as the
#          "max-count" trick, and avoids the subtle non-shrinking-window
#          reasoning of LC 424.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxConsecutiveAnswers(self, answerKey, k):
        def longest(target):
            res = left = bad = 0
            for right, c in enumerate(answerKey):
                if c != target:
                    bad += 1
                while bad > k:
                    if answerKey[left] != target:
                        bad -= 1
                    left += 1
                res = max(res, right - left + 1)
            return res

        return max(longest('T'), longest('F'))
