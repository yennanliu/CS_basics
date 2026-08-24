"""

1987. Number of Unique Good Subsequences
Hard

You are given a binary string binary. A subsequence of binary is considered good if it is not empty and has no leading zeros (with the exception of "0").

Find the number of unique good subsequences of binary.

For example, if binary = "001", then all the good subsequences are ["0", "0", "1"], so the unique good subsequences are "0" and "1". Note that subsequences "00", "01", and "001" are not good because they have leading zeros.

Return the number of unique good subsequences of binary. Since the answer may be very large, return it modulo 10^9 + 7.

A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: binary = "001"
Output: 2
Explanation: The good subsequences of binary are ["0", "0", "1"].
The unique good subsequences are "0" and "1".

Example 2:

Input: binary = "11"
Output: 2
Explanation: The good subsequences of binary are ["1", "1", "11"].
The unique good subsequences are "1" and "11".

Example 3:

Input: binary = "101"
Output: 5
Explanation: The good subsequences of binary are ["1", "0", "1", "10", "11", "101"].
The unique good subsequences are "0", "1", "10", "11", and "101".


Constraints:

1 <= binary.length <= 10^5
binary consists of only '0's and '1's.

"""

# V0
# IDEA : DP ON THE LAST CHARACTER (count DISTINCT strings, not positions)
#
#   every good subsequence except the single "0" starts with '1'.
#   track two counts of DISTINCT strings that start with '1' :
#     f = those ending in '1'
#     g = those ending in '0'
#
#   scanning left to right, at character c :
#     c == '0' : append '0' to every existing string -> g = f + g
#                (also record that a lone "0" is available)
#     c == '1' : append '1' to every existing string, plus the new "1"
#                -> f = f + g + 1
#
#   NOTE : assigning (not adding) is what removes duplicates - two equal
#          strings built from different positions collapse into one count.
#   NOTE : add 1 at the end only if the string contains at least one '0',
#          to account for the standalone "0".
#
"""

DP def
    every good subsequence except the single "0" starts with '1'. count
    DISTINCT STRINGS (not positions):

    f: distinct good subsequences (starting '1') ending in '1'
    g: distinct good subsequences (starting '1') ending in '0'
    zero: 1 once a standalone "0" is possible

DP eq

     c == '0' : g = f + g          # append '0' to every existing string
                zero = 1

     c == '1' : f = f + g + 1      # append '1' to every existing string,
                                   #   plus the brand new "1"


    -> e.g. NOTE !!! ASSIGNING (not adding) is what removes duplicates -
              two equal strings built from different positions collapse into
              one count

     ans = (f + g + zero) % (10^9 + 7)

"""
# time = O(n), space = O(1)
class Solution(object):
    def numberOfUniqueGoodSubsequences(self, binary):
        MOD = 10 ** 9 + 7
        f = 0        # distinct good subsequences ending in '1'
        g = 0        # distinct good subsequences ending in '0' (starting '1')
        zero = 0     # 1 once a standalone "0" is possible
        for c in binary:
            if c == "0":
                g = (f + g) % MOD
                zero = 1
            else:
                f = (f + g + 1) % MOD
        return (f + g + zero) % MOD
