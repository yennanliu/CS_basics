"""

2081. Sum of k-Mirror Numbers
Hard

A k-mirror number is a positive integer without leading zeros that reads the same both forward and backward in base-10 as well as in base-k.

For example, 9 is a 2-mirror number. The representation of 9 in base-10 and base-2 are 9 and 1001 respectively, which read the same both forward and backward.
On the contrary, 4 is not a 2-mirror number. The representation of 4 in base-2 is 100, which does not read the same both forward and backward.

Given the base k and the number n, return the sum of the n smallest k-mirror numbers.


Example 1:

Input: k = 2, n = 5
Output: 25
Explanation:
The 5 smallest 2-mirror numbers and their representations in base-2 are listed as follows:
  base-10    base-2
    1          1
    3          11
    5          101
    7          111
    9          1001
Their sum = 1 + 3 + 5 + 7 + 9 = 25.

Example 2:

Input: k = 3, n = 7
Output: 499
Explanation:
The 7 smallest 3-mirror numbers are and their representations in base-3 are listed as follows:
  base-10    base-3
    1          1
    2          2
    4          11
    7          21
    121        11111
    242        22222
    ...

Example 3:

Input: k = 7, n = 17
Output: 20379000

Constraints:

2 <= k <= 9
1 <= n <= 30

"""

# V0
# IDEA : GENERATE BASE-10 PALINDROMES IN ORDER, THEN TEST BASE-k
#
#   base-10 palindromes are FAR sparser than "all integers", so building them
#   directly is the only tractable direction. for each half-length L build
#   every half `h` in [10^(L-1), 10^L) and mirror it :
#       odd  length  -> h + reverse(h without its last digit)
#       even length  -> h + reverse(h)
#   emitting odd-length before even-length keeps the whole stream ascending.
#
#   then convert the candidate to base k and keep it iff that string is also
#   a palindrome. stop once n of them are collected.
#
# time = O(#candidates * digits), space = O(digits)
class Solution(object):
    def kMirror(self, k, n):
        def to_base(x, base):
            out = []
            while x:
                out.append(str(x % base))
                x //= base
            return ''.join(reversed(out))

        def gen():
            L = 1
            while True:
                # odd total length : 2 * L - 1
                for h in range(10 ** (L - 1), 10 ** L):
                    s = str(h)
                    yield int(s + s[-2::-1])
                # even total length : 2 * L
                for h in range(10 ** (L - 1), 10 ** L):
                    s = str(h)
                    yield int(s + s[::-1])
                L += 1

        res = 0
        left = n
        for x in gen():
            t = to_base(x, k)
            if t == t[::-1]:
                res += x
                left -= 1
                if left == 0:
                    return res
