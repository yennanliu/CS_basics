"""

1562. Find Latest Group of Size M
Medium

Given an array arr that represents a permutation of numbers from 1 to n.

You have a binary string of size n that initially has all its bits set to zero. At each step i (assuming both the binary string and arr are 1-indexed) from 1 to n, the bit at position arr[i] is set to 1.

You are also given an integer m. Find the latest step at which there exists a group of ones of length m. A group of ones is a contiguous substring of 1's such that it cannot be extended in either direction.

Return the latest step at which there exists a group of ones of length exactly m. If no such group exists, return -1.

Example 1:

Input: arr = [3,5,1,2,4], m = 1
Output: 4
Explanation:
Step 1: "00100", groups: ["1"]
Step 2: "00101", groups: ["1", "1"]
Step 3: "10101", groups: ["1", "1", "1"]
Step 4: "11101", groups: ["111", "1"]
Step 5: "11111", groups: ["11111"]
The latest step at which there exists a group of size 1 is step 4.

Example 2:

Input: arr = [3,1,5,4,2], m = 2
Output: -1
Explanation:
Step 1: "00100", groups: ["1"]
Step 2: "10100", groups: ["1", "1"]
Step 3: "10101", groups: ["1", "1", "1"]
Step 4: "10111", groups: ["1", "111"]
Step 5: "11111", groups: ["11111"]
No group of size 2 exists during any step.

Constraints:

n == arr.length
1 <= m <= n <= 10^5
1 <= arr[i] <= n
All integers in arr are distinct.

"""

# V0
# IDEA : INTERVAL MERGING (track the length stored at each group's two ends)
#
#   keep length[p] = size of the group of ones whose END is p (only the
#   two boundary cells of a group are kept up to date, that is all we
#   ever read).
#   setting bit x merges the group ending at x-1 with the one starting
#   at x+1 :
#     total = left + right + 1 ; write it at x-left and x+right.
#   cnt[L] counts how many groups currently have length L -> answer is
#   the last step at which cnt[m] > 0.
#
# time = O(n), space = O(n)
class Solution(object):
    def findLatestStep(self, arr, m):
        n = len(arr)
        length = [0] * (n + 2)
        cnt = [0] * (n + 2)
        res = -1
        for step, x in enumerate(arr, 1):
            left, right = length[x - 1], length[x + 1]
            total = left + right + 1
            length[x - left] = total
            length[x + right] = total
            cnt[left] -= 1
            cnt[right] -= 1
            cnt[total] += 1
            if cnt[m] > 0:
                res = step
        return res
