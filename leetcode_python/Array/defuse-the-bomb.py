"""

1652. Defuse the Bomb
Easy

You have a bomb to defuse, and your time is running out! Your informer will provide you with a circular array code of length of n and a key k.

To decrypt the code, you must replace every number. All the numbers are replaced simultaneously.

- If k > 0, replace the ith number with the sum of the next k numbers.
- If k < 0, replace the ith number with the sum of the previous -k numbers.
- If k == 0, replace the ith number with 0.

As code is circular, the next element of code[n-1] is code[0], and the previous element of code[0] is code[n-1].

Given the circular array code and an integer key k, return the decrypted code to defuse the bomb!


Example 1:

Input: code = [5,7,1,4], k = 3
Output: [12,10,16,13]
Explanation: Each number is replaced by the sum of the next 3 numbers. The decrypted code is [7+1+4, 1+4+5, 4+5+7, 5+7+1]. Notice that the numbers wrap around.

Example 2:

Input: code = [1,2,3,4], k = 0
Output: [0,0,0,0]
Explanation: When k is zero, the numbers are replaced by 0.

Example 3:

Input: code = [2,4,9,3], k = -2
Output: [12,5,6,13]
Explanation: The decrypted code is [3+9, 2+3, 4+2, 9+4]. Notice that the numbers wrap around again. If k is negative, the sum is of the previous numbers.


Constraints:

n == code.length
1 <= n <= 100
1 <= code[i] <= 100
-(n - 1) <= k <= n - 1

"""

# V0
# IDEA : SLIDING WINDOW over the circular array
#
#   every output entry is the sum of a fixed-length window of |k|
#   consecutive elements, only the window's offset differs:
#     k > 0 -> window is [i+1, i+k]
#     k < 0 -> window is [i+k, i-1]
#   so compute the window sum once for i = 0, then slide it: drop the
#   element leaving on the left, add the one entering on the right.
#
#   NOTE : index everything modulo n to wrap around.
#   NOTE : k == 0 returns all zeros, handle before the loop.
#
# time = O(n), space = O(n) for the output
class Solution(object):
    def decrypt(self, code, k):
        n = len(code)
        res = [0] * n
        if k == 0:
            return res

        # window for i = 0
        if k > 0:
            lo, hi = 1, k               # inclusive, may exceed n -> mod
        else:
            lo, hi = n + k, n - 1

        total = 0
        for j in range(lo, hi + 1):
            total += code[j % n]
        res[0] = total

        for i in range(1, n):
            total -= code[(lo + i - 1) % n]
            total += code[(hi + i) % n]
            res[i] = total
        return res
