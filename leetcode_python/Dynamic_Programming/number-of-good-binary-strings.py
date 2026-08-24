"""

2533. Number of Good Binary Strings
Medium

You are given four integers minLength, maxLength, oneGroup and zeroGroup.

A binary string is good if it satisfies the following conditions:

The length of the string is in the range [minLength, maxLength].
The size of each block of consecutive 1's is a multiple of oneGroup.
    For example in a binary string 00110111100 sizes of each block of consecutive ones are [2,4].
The size of each block of consecutive 0's is a multiple of zeroGroup.
    For example, in a binary string 00110111100 sizes of each block of consecutive zeros are [2,1,2].

Return the number of good binary strings. Since the answer may be too large, return it modulo 10^9 + 7.

Note that 0 is considered a multiple of all the numbers.


Example 1:

Input: minLength = 2, maxLength = 3, oneGroup = 1, zeroGroup = 2
Output: 5
Explanation: There are 5 good binary strings in this example: "00", "11", "001", "100", and "111".
It can be proven that there are only 5 good strings satisfying all conditions.

Example 2:

Input: minLength = 4, maxLength = 4, oneGroup = 4, zeroGroup = 3
Output: 1
Explanation: There is only 1 good binary string in this example: "1111".
It can be proven that there is only 1 good string satisfying all conditions.


Constraints:

1 <= minLength <= maxLength <= 10^5
1 <= oneGroup, zeroGroup <= maxLength

"""

# V0
# IDEA : DP OVER "APPEND ONE WHOLE BLOCK"
#
#   a good string is built by appending blocks; since every 1-block length must
#   be a multiple of oneGroup and every 0-block a multiple of zeroGroup, we can
#   think of it as repeatedly appending a chunk of EXACTLY oneGroup ones or
#   EXACTLY zeroGroup zeros. two adjacent chunks of the same symbol just merge
#   into one longer (still valid) block, so this construction is a bijection
#   with the set of good strings.
#
#     f[i] = number of good strings of length i
#     f[0] = 1 (empty string)
#     f[i] = f[i - oneGroup] + f[i - zeroGroup]
#
#   answer = f[minLength] + ... + f[maxLength].
#
#   NOTE : the bijection holds because each chunk is a FIXED size, so a given
#          string decomposes in exactly one way (greedily left to right).
#
"""

DP def
    every 1-block length is a multiple of oneGroup and every 0-block a
    multiple of zeroGroup, so a good string is built by repeatedly appending a
    chunk of EXACTLY oneGroup ones or EXACTLY zeroGroup zeros (two adjacent
    same-symbol chunks just merge into one longer valid block)

    f[i]: number of good strings of length i

DP eq

     f[i] = f[i - oneGroup] + f[i - zeroGroup]


    -> e.g. the construction is a BIJECTION because each chunk has a FIXED
              size, so a given string decomposes in exactly one way
              (greedily, left to right)

     init: f[0] = 1 (the empty string)
     ans = f[minLength] + ... + f[maxLength]    mod 10^9 + 7

"""
# time = O(maxLength), space = O(maxLength)
class Solution(object):
    def goodBinaryStrings(self, minLength, maxLength, oneGroup, zeroGroup):
        MOD = 10 ** 9 + 7
        f = [0] * (maxLength + 1)
        f[0] = 1
        for i in range(1, maxLength + 1):
            if i >= oneGroup:
                f[i] += f[i - oneGroup]
            if i >= zeroGroup:
                f[i] += f[i - zeroGroup]
            f[i] %= MOD
        return sum(f[minLength:]) % MOD
