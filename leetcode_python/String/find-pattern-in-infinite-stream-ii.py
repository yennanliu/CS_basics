"""

3037. Find Pattern in Infinite Stream II
Hard
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

1 <= pattern.length <= 10^4
pattern consists only of 0 and 1.
stream consists only of 0 and 1.
The input is generated such that the pattern's start index exists in the first 10^7 bits of the stream.

"""

# V0
# IDEA : KMP AS A STREAMING AUTOMATON — NO NEED TO STORE THE STREAM
#
#   LC 3023's window compare is O(m) per bit; with m up to 10^4 and 10^7 bits
#   that is 10^11 operations. KMP fixes it : precompute the failure table
#   once, then keep a single integer j = how many pattern bits currently
#   match the tail of what has been read.
#
#   each new bit updates j in amortised O(1), and reaching j == m means the
#   match ENDED at the bit just read, so it started at read_count - m.
#
#   nothing but j and the failure table is retained, so memory is O(m) no
#   matter how long the stream runs.
#
# time = O(N + m) over N read bits, space = O(m)
# Definition for an infinite stream.
# class InfiniteStream(object):
#     def next(self):
#         """
#         :rtype: int
#         """
class Solution(object):
    def findPattern(self, stream, pattern):
        m = len(pattern)

        fail = [0] * m
        j = 0
        for i in range(1, m):
            while j and pattern[i] != pattern[j]:
                j = fail[j - 1]
            if pattern[i] == pattern[j]:
                j += 1
            fail[i] = j

        j = 0
        read = 0
        while True:
            bit = stream.next()
            read += 1
            while j and bit != pattern[j]:
                j = fail[j - 1]
            if bit == pattern[j]:
                j += 1
            if j == m:
                return read - m
