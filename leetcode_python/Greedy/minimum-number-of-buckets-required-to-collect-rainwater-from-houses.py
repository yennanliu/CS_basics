"""

2086. Minimum Number of Buckets Required to Collect Rainwater from Houses
Medium

You are given a 0-indexed string street. Each character in street is either 'H' representing a house or '.' representing an empty space.

You can place buckets on the empty spaces to collect rainwater that falls from the adjacent houses. The rainwater from a house at index i is collected if a bucket is placed at index i - 1 and/or index i + 1. A single bucket, if placed adjacent to two houses, can collect the rainwater from both houses.

Return the minimum number of buckets needed so that for every house, there is at least one bucket collecting rainwater from it, or -1 if it is impossible.


Example 1:

Input: street = "H..H"
Output: 2
Explanation:
We can put buckets at index 1 and index 2.
"H..H" -> "HBBH" ('B' denotes where a bucket is placed).
The house at index 0 has a bucket to its right, and the house at index 3 has a bucket to its left.
Thus, for every house, there is at least one bucket collecting rainwater from it.

Example 2:

Input: street = ".H.H."
Output: 1
Explanation:
We can put a bucket at index 2.
".H.H." -> ".HBH." ('B' denotes where a bucket is placed).
The house at index 1 has a bucket to its right, and the house at index 3 has a bucket to its left.
Thus, for every house, there is at least one bucket collecting rainwater from it.

Example 3:

Input: street = ".HHH."
Output: -1
Explanation:
There is no empty space adjacent to the house at index 2, so we cannot collect its rainwater.
Thus, it is impossible to collect the rainwater from every house.


Constraints:

1 <= street.length <= 10^5
street[i] is either 'H' or '.'.

"""

# V0
# IDEA : GREEDY — ALWAYS PREFER THE BUCKET ON THE RIGHT OF A HOUSE
#
#   scan left to right. for a house at i :
#     - if a bucket already sits at i - 1, it is covered, move on
#     - else prefer placing at i + 1 : that bucket may ALSO serve a house at
#       i + 2, which a bucket at i - 1 never could
#     - else fall back to i - 1
#     - if neither side is free -> impossible, return -1
#
#   placing on the right is never worse (exchange argument), which makes the
#   single greedy pass optimal.
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumBuckets(self, street):
        s = list(street)
        n = len(s)
        res = 0
        for i in range(n):
            if s[i] != 'H':
                continue
            if i > 0 and s[i - 1] == 'B':
                continue
            if i + 1 < n and s[i + 1] == '.':
                s[i + 1] = 'B'
                res += 1
            elif i > 0 and s[i - 1] == '.':
                s[i - 1] = 'B'
                res += 1
            else:
                return -1
        return res
