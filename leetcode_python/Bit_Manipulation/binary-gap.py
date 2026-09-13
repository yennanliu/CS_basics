"""

868. Binary Gap
Easy

Given a positive integer n, find and return the longest distance between any two adjacent 1's in the binary representation of n. If there are no two adjacent 1's, return 0.

Two 1's are adjacent if there are only 0's separating them (possibly no 0's). The distance between two 1's is the absolute difference between their bit positions. For example, the two 1's in "1001" have a distance of 3.

Example 1:

Input: n = 22
Output: 2
Explanation: 22 in binary is "10110".
The first adjacent pair of 1's is "10110" with a distance of 2.
The second adjacent pair of 1's is "10110" with a distance of 1.
The answer is the largest of these two distances, which is 2.
Note that "10110" is not a valid pair since there is a 1 separating the two 1's underlined.

Example 2:

Input: n = 8
Output: 0
Explanation: 8 in binary is "1000".
There are not any adjacent pairs of 1's in the binary representation of 8, so we return 0.

Example 3:

Input: n = 5
Output: 2
Explanation: 5 in binary is "101".

Constraints:

1 <= n <= 10^9

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079495
# IDEA :  LINEAR SCAN 
# time = O(log n)  # n = value N; bin(N) has O(log n) bits
# space = O(log n)
class Solution(object):
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        binary = bin(N)[2:]
        dists = [0] * len(binary)
        left = 0
        for i, b in enumerate(binary):
            if b == '1':
                dists[i] = i - left
                left = i
        return max(dists)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079495
# time = O(log n)  # n = value N; bin(N) has O(log n) bits
# space = O(log n)  # bin(N) string
class Solution:
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        nbins = bin(N)[2:]
        index = -1
        res = 0
        for i, b in enumerate(nbins):
            if b == "1":
                if index != -1:
                    res = max(res, i - index)
                index = i
        return res

# V2 
# time = O(logn) = O(1) due to n is a 32-bit number
# space = O(1)
class Solution(object):
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        result = 0
        last = None
        for i in range(32):
            if (N >> i) & 1:
                if last is not None:
                    result = max(result, i-last)
                last = i
        return result