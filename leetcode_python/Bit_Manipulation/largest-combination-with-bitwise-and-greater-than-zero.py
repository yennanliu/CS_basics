"""

2275. Largest Combination With Bitwise AND Greater Than Zero
Medium

The bitwise AND of an array nums is the bitwise AND of all integers in nums.

For example, for nums = [1, 5, 3], the bitwise AND is equal to 1 & 5 & 3 = 1.
Also, for nums = [7], the bitwise AND is 7.

You are given an array of positive integers candidates. Evaluate the bitwise AND of every combination of numbers of candidates. Each number in candidates may only be used once in each combination.

Return the size of the largest combination of candidates with a bitwise AND greater than 0.


Example 1:

Input: candidates = [16,17,71,62,12,24,14]
Output: 4
Explanation: The combination [16,17,62,24] has a bitwise AND of 16 & 17 & 62 & 24 = 16 > 0.
The size of the combination is 4.
It can be shown that no combination with a size greater than 4 has a bitwise AND greater than 0.
Note that more numbers have a bitwise AND less than 8 and thus the size of this combination is not 4.

Example 2:

Input: candidates = [8,8]
Output: 2
Explanation: The largest combination [8,8] has a bitwise AND of 8 & 8 = 8 > 0.
The size of the combination is 2.


Constraints:

1 <= candidates.length <= 10^5
1 <= candidates[i] <= 10^7

"""

# V0
# IDEA : THE AND IS NON-ZERO IFF SOME BIT SURVIVES — COUNT PER BIT COLUMN
#
#   a combination's AND is > 0 exactly when at least ONE bit position is set
#   in every member. so for a fixed bit b, the biggest such combination is
#   simply "all candidates having bit b set".
#
#   therefore the answer is
#       max over b of  #{ x : x has bit b set }
#
#   candidates are < 2^24, so 24 columns suffice.
#
# time = O(n * 24), space = O(24)
class Solution(object):
    def largestCombination(self, candidates):
        BITS = 24                       # 10^7 < 2^24
        counts = [0] * BITS
        for x in candidates:
            for b in range(BITS):
                if x >> b & 1:
                    counts[b] += 1
        return max(counts)
