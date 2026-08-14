"""

1868. Product of Two Run-Length Encoded Arrays
Medium

Run-length encoding is a compression algorithm that allows for an integer array nums with many segments of consecutive repeated numbers to be represented by a (generally smaller) 2D array encoded. Each encoded[i] = [vali, freqi] describes the ith segment of repeated numbers in nums where vali is the value that is repeated freqi times.

For example, nums = [1,1,1,2,2,2,2,2] is represented by the run-length encoded array encoded = [[1,3],[2,5]]. Another way to read this is "three 1's followed by five 2's".

The product of two run-length encoded arrays encoded1 and encoded2 can be calculated using the following steps:

Expand both encoded1 and encoded2 into the full arrays nums1 and nums2 respectively.
Create a new array prodNums of length nums1.length and set prodNums[i] = nums1[i] * nums2[i].
Compress prodNums into a run-length encoded array and return it.

You are given two run-length encoded arrays encoded1 and encoded2 representing full arrays nums1 and nums2 respectively. Both nums1 and nums2 have the same length. Each encoded1[i] = [vali, freqi] describes the ith segment of nums1, and each encoded2[j] = [valj, freqj] describes the jth segment of nums2.

Return the product of encoded1 and encoded2.

Note: Compression should be done such that the run-length encoded array has the minimum possible length.


Example 1:

Input: encoded1 = [[1,3],[2,3]], encoded2 = [[6,3],[3,3]]
Output: [[6,6]]
Explanation: encoded1 expands to [1,1,1,2,2,2] and encoded2 expands to [6,6,6,3,3,3].
prodNums = [6,6,6,6,6,6], which is compressed into the run-length encoded array [[6,6]].

Example 2:

Input: encoded1 = [[1,3],[2,1],[3,2]], encoded2 = [[2,3],[3,3]]
Output: [[2,3],[6,1],[9,2]]
Explanation: encoded1 expands to [1,1,1,2,3,3] and encoded2 expands to [2,2,2,3,3,3].
prodNums = [2,2,2,6,9,9], which is compressed into the run-length encoded array [[2,3],[6,1],[9,2]].


Constraints:

1 <= encoded1.length, encoded2.length <= 10^5
encoded1[i].length == 2
encoded2[j].length == 2
1 <= vali, freqi <= 10^4 for each encoded1[i].
1 <= valj, freqj <= 10^4 for each encoded2[j].
The full arrays that encoded1 and encoded2 represent are the same length.

"""

# V0
# IDEA : TWO POINTERS OVER THE RUNS (never expand the arrays)
#
#   walk both encodings at once, keeping how much of the current run of
#   each side is still unconsumed. the overlap of the two current runs is
#   min(rem1, rem2) positions that ALL share the same product v1 * v2.
#
#   emit that block, subtract the overlap from both sides, and advance
#   whichever side just ran out. each step finishes at least one run,
#   so the loop runs at most len(e1) + len(e2) times.
#
#   NOTE : merge with the previous output block when the product repeats,
#          otherwise the result is not minimal (e.g. [[6,3],[6,3]]).
#
# time = O(m + n), space = O(m + n) for the output
class Solution(object):
    def findRLEArray(self, encoded1, encoded2):
        res = []
        i, j = 0, 0
        rem1 = encoded1[0][1]
        rem2 = encoded2[0][1]

        while i < len(encoded1) and j < len(encoded2):
            take = min(rem1, rem2)
            val = encoded1[i][0] * encoded2[j][0]

            if res and res[-1][0] == val:
                res[-1][1] += take
            else:
                res.append([val, take])

            rem1 -= take
            rem2 -= take
            if rem1 == 0:
                i += 1
                if i < len(encoded1):
                    rem1 = encoded1[i][1]
            if rem2 == 0:
                j += 1
                if j < len(encoded2):
                    rem2 = encoded2[j][1]

        return res
