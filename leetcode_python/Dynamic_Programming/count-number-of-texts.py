"""

2266. Count Number of Texts
Medium

Alice is texting Bob using her phone. The mapping of digits to letters is shown in the figure below.

In order to add a letter, Alice has to press the key of the corresponding digit i times, where i is the position of the letter in the key.

For example, to add the letter 's', Alice has to press '7' four times. Similarly, to add the letter 'k', Alice has to press '5' twice.

Note that the digits '0' and '1' do not map to any letters, so Alice does not use them.

However, due to an error in transmission, Bob did not receive Alice's text message but received a string of pressed keys instead.

For example, when Alice sent the message "bob", Bob received the string "2266622".

Given a string pressedKeys representing the string received by Bob, return the total number of possible text messages Alice could have sent.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: pressedKeys = "22233"
Output: 8
Explanation:
The possible text messages Alice could have sent are:
"aaadd", "abdd", "badd", "cdd", "aaae", "abe", "bae", and "ce".
Since there are 8 possible messages, we return 8.

Example 2:

Input: pressedKeys = "222222222222222222222222222222222222"
Output: 82876089
Explanation:
There are 2082876103 possible text messages Alice could have sent.
Since we need to return the answer modulo 10^9 + 7, we return 2082876103 % (10^9 + 7) = 82876089.


Constraints:

1 <= pressedKeys.length <= 10^5
pressedKeys only consists of digits from '2' - '9'.

"""

# V0
# IDEA : DP ON EACH EQUAL-DIGIT RUN (tribonacci / tetranacci), multiply the runs
#
#   two different digits can never be merged into one letter, so the answer is
#   the product of the counts of each maximal run of the same digit.
#
#   inside a run of length m the question is "how many ways to cut m presses
#   into groups of size <= L", where L = 4 for keys '7' and '9' (four letters)
#   and L = 3 for every other key.
#
#     f[m] = f[m-1] + f[m-2] + f[m-3]            (L = 3)
#     g[m] = g[m-1] + g[m-2] + g[m-3] + g[m-4]   (L = 4)
#     f[0] = g[0] = 1
#
#   NOTE : tables are built lazily up to the longest run only, so no fixed
#          100000-entry precompute is needed.
#
"""

DP def
    two DIFFERENT digits can never merge into one letter, so the answer is
    the PRODUCT over each maximal run of equal digits

    f[m]: ways to cut a run of m presses into groups of size <= 3
          (keys with 3 letters)

    g[m]: ways to cut a run of m presses into groups of size <= 4
          (keys '7' and '9')

DP eq

     f[m] = f[m-1] + f[m-2] + f[m-3]              # tribonacci

     g[m] = g[m-1] + g[m-2] + g[m-3] + g[m-4]     # tetranacci


    -> e.g. f[m-t] = "the last letter used t presses"

     init: f[0] = g[0] = 1
     ans = product over runs (ch, m) of ( g[m] if ch in '79' else f[m] )

"""
# time = O(n), space = O(n)
from itertools import groupby
class Solution(object):
    def countTexts(self, pressedKeys):
        MOD = 10 ** 9 + 7
        n = len(pressedKeys)

        f = [0] * (n + 1)          # groups of size <= 3
        g = [0] * (n + 1)          # groups of size <= 4
        f[0] = g[0] = 1
        for i in range(1, n + 1):
            f[i] = (f[i - 1] + (f[i - 2] if i >= 2 else 0) +
                    (f[i - 3] if i >= 3 else 0)) % MOD
            g[i] = (g[i - 1] + (g[i - 2] if i >= 2 else 0) +
                    (g[i - 3] if i >= 3 else 0) +
                    (g[i - 4] if i >= 4 else 0)) % MOD

        res = 1
        for ch, grp in groupby(pressedKeys):
            m = len(list(grp))
            res = res * (g[m] if ch in ('7', '9') else f[m]) % MOD
        return res
