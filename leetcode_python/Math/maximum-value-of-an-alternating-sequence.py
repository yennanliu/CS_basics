# https://leetcode.com/problems/maximum-value-of-an-alternating-sequence/description/

"""

3993. Maximum Value of an Alternating Sequence
Solved
Medium
premium lock icon
Companies
Hint
You are given three integers n, s, and m.

A sequence seq of integers of length n is considered valid if:

seq[0] = s.
The sequence is alternating, meaning that either:
seq[0] > seq[1] < seq[2] > ..., or
seq[0] < seq[1] > seq[2] < ....
For every adjacent pair, |seq[i] - seq[i - 1]| <= m.
A sequence of length 1 is considered alternating.

Return the maximum possible element that can appear in any valid sequence.

 

Example 1:

Input: n = 4, s = 3, m = 5

Output: 12

Explanation:

One valid sequence is [3, 8, 7, 12].
The maximum element in the sequence is 12.
Example 2:

Input: n = 2, s = 4, m = 3

Output: 7

Explanation:

One valid sequence is [4, 7].
The maximum element in the sequence is 7.
 

Constraints:

1 <= n, s <= 109
1 <= m <= 105

"""

# V0
# IDEA: MATH (gemini)
class Solution(object):
    def maximumValue(self, n, s, m):
        """
        :type n: int
        :type s: int
        :type m: int
        :rtype: int
        """
        # Edge case
        if n == 1:
            return s
            
        # The maximum value is ALWAYS reached right after an addition step.
        # How many total additions happen in a sequence of length n?
        a = n // 2
        
        # How many subtractions happen BEFORE that final addition?
        b = a - 1
        
        # The peak value is the starting value + all additions - all subtractions
        return s + (a * m) - b


# V0-1
# IDEA: MATH (gemini)
class Solution(object):
    def maximumValue(self, n, s, m):
        """
        :type n: int
        :type s: int
        :type m: int
        :rtype: int
        """
        # Edge case: sequence of length 1 is just the starting element
        if n == 1:
            return s
            
        # Formula: start + (number of rises) * (m - 1) + 1
        return s + (n // 2) * (m - 1) + 1

# V1
