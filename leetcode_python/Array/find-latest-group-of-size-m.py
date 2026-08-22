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


# V0-1
# IDEA : UNION-FIND OVER THE BITS TURNED ON + LIVE COUNT OF SIZE-M GROUPS
#
#   model each group of ones as a DSU component instead of an interval:
#   turning on bit x creates a singleton, then unions it with whichever of
#   x-1 / x+1 is already on.
#   `cnt` = how many components currently have size exactly m. every event
#   changes at most 3 sizes (the new singleton, and the two operands of a
#   union), so cnt is patched in O(1) each time and never rescanned.
#   answer = the last step at which cnt > 0.
#
#   NOTE : sizes are only meaningful at a ROOT, so always compare
#          size[find(v)], never size[v].
#
# time = O(n * alpha(n)) ~ O(n), space = O(n)
class Solution(object):
    def findLatestStep(self, arr, m):
        n = len(arr)
        parent = list(range(n + 2))
        size = [1] * (n + 2)
        on = [False] * (n + 2)

        def find(a):
            while parent[a] != a:
                parent[a] = parent[parent[a]]     # path halving
                a = parent[a]
            return a

        cnt = 0                                   # components of size == m
        res = -1
        for step, x in enumerate(arr, 1):
            on[x] = True
            if m == 1:
                cnt += 1
            for y in (x - 1, x + 1):
                if 1 <= y <= n and on[y]:
                    rx, ry = find(x), find(y)
                    if rx != ry:
                        if size[rx] == m:
                            cnt -= 1
                        if size[ry] == m:
                            cnt -= 1
                        parent[ry] = rx
                        size[rx] += size[ry]
                        if size[rx] == m:
                            cnt += 1
            if cnt > 0:
                res = step
        return res


# V0-2
# IDEA : BRUTE FORCE — REBUILD THE BIT ARRAY AND RESCAN AFTER EVERY STEP
#
#   the definition itself : keep the bit array, and after each step walk it
#   once measuring every maximal run of ones, recording the step whenever some
#   run has length exactly m.
#   O(n^2) so it TLEs at n = 10^5, but it needs no invariant to be believed
#   and makes a convenient oracle for the two linear solutions above.
#
#   NOTE : the run that touches the right border must be checked after the
#          loop, it is never closed by a zero.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def findLatestStep(self, arr, m):
        n = len(arr)
        bits = [0] * (n + 1)
        res = -1
        for step, x in enumerate(arr, 1):
            bits[x] = 1
            run = 0
            for i in range(1, n + 1):
                if bits[i]:
                    run += 1
                else:
                    if run == m:
                        res = step
                    run = 0
            if run == m:
                res = step
        return res
