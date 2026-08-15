"""

3023. Find Pattern in Infinite Stream I
Medium
🔒 (premium)

You are given a binary array pattern and an object stream of class InfiniteStream representing a 0-indexed infinite stream of bits.

The class InfiniteStream contains the following function:

int next(): Reads a single bit (which is either 0 or 1) from the stream and returns it.

Return the first starting index where the pattern matches the bits read from the stream. For example, if the pattern is [1, 0], the first timestamp where the pattern matches is when the stream reads [1, 0] consecutively.


Example 1:

Input: stream = [1,1,1,0,1,1,1,...], pattern = [0,1]
Output: 3
Explanation: The first occurrence of the pattern [0,1] is at index 3, as the stream reads 0 at index 3 and 1 at index 4.

Example 2:

Input: stream = [0,0,0,0,...], pattern = [0]
Output: 0
Explanation: The first occurrence of the pattern [0] is at index 0.

Example 3:

Input: stream = [1,0,1,1,0,1,1,0,1,...], pattern = [1,1,0,1]
Output: 2
Explanation: The first occurrence of the pattern [1,1,0,1] is at index 2.


Constraints:

1 <= pattern.length <= 100
pattern consists only of 0 and 1.
stream consists only of 0 and 1.
The input is generated such that the pattern's start index exists in the first 10^5 bits of the stream.

"""

# V0
# IDEA : ROLLING WINDOW OF THE LAST len(pattern) BITS
#
#   the stream can only be consumed forwards, one bit at a time, so keep a
#   deque holding the most recent m = len(pattern) bits. after each read,
#   drop the oldest bit once the window is over-full and compare.
#
#   the comparison is O(m) and m <= 100, so the total stays comfortable for
#   the 10^5-bit guarantee. the sequel (LC 3037) has a much longer pattern
#   and needs KMP instead of the direct compare.
#
#   the answer is the index where the window STARTS, i.e. read_count - m.
#
# time = O(N * m) over N read bits, space = O(m)
from collections import deque


# Definition for an infinite stream.
# class InfiniteStream(object):
#     def next(self):
#         """
#         :rtype: int
#         """
class Solution(object):
    def findPattern(self, stream, pattern):
        m = len(pattern)
        target = list(pattern)
        window = deque()
        read = 0

        while True:
            window.append(stream.next())
            read += 1
            if len(window) > m:
                window.popleft()
            if len(window) == m and list(window) == target:
                return read - m
