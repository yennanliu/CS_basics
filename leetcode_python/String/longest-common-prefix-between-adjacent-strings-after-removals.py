"""

3598. Longest Common Prefix Between Adjacent Strings After Removals
Medium

You are given an array of strings words. For each index i in the range [0, words.length - 1], perform the following steps:

Remove the element at index i from the words array.
Compute the length of the longest common prefix among all adjacent pairs in the modified array.

Return an array answer, where answer[i] denotes the length of the longest common prefix between the adjacent pairs after removing the element at index i. If the words array contains fewer than 2 strings after removing element i, or if no adjacent pair exists, then answer[i] should be 0.


Example 1:

Input: words = ["jump","run","run","jump","run"]
Output: [3,0,0,3,3]
Explanation:
Removing index 0:
words becomes ["run", "run", "jump", "run"]
Longest adjacent pair prefix: "run" and "run" -> 3
Removing index 1:
words becomes ["jump", "run", "jump", "run"]
Longest adjacent pair prefix: none -> 0
Removing index 2:
words becomes ["jump", "run", "jump", "run"]
Longest adjacent pair prefix: none -> 0
Removing index 3:
words becomes ["jump", "run", "run", "run"]
Longest adjacent pair prefix: "run" and "run" -> 3
Removing index 4:
words becomes ["jump", "run", "run", "jump"]
Longest adjacent pair prefix: "run" and "run" -> 3

Example 2:

Input: words = ["dog","racer","car"]
Output: [0,0,0]
Explanation:
Removing any index results in an answer of 0.


Constraints:

1 <= words.length <= 10^5
1 <= words[i].length <= 10^4
words[i] consists of lowercase English letters.
The sum of words[i].length is smaller than or equal 10^5.

"""

# V0
# IDEA : PREFIX / SUFFIX MAXIMA OVER THE ORIGINAL ADJACENT PAIRS
#
#   deleting index i destroys at most two of the original adjacent pairs
#   (i-1,i) and (i,i+1) and creates exactly one brand new pair (i-1,i+1).
#   every other pair survives untouched, so the answer is the max of
#   - the best pair entirely left of i,
#   - the best pair entirely right of i,
#   - the freshly stitched pair.
#
#   precomputed running maxima of the pairwise lcp array make each query
#   O(1) apart from the single stitched lcp.
#
# time = O(total length), space = O(n)
class Solution(object):
    def longestCommonPrefix(self, words):
        n = len(words)
        if n <= 2:
            return [0] * n

        def lcp(a, b):
            m = min(len(a), len(b))
            i = 0
            while i < m and a[i] == b[i]:
                i += 1
            return i

        pairs = [lcp(words[i], words[i + 1]) for i in range(n - 1)]
        m = n - 1
        pre = [0] * m           # pre[j] = max(pairs[0..j])
        run = 0
        for j in range(m):
            if pairs[j] > run:
                run = pairs[j]
            pre[j] = run
        suf = [0] * m           # suf[j] = max(pairs[j..m-1])
        run = 0
        for j in range(m - 1, -1, -1):
            if pairs[j] > run:
                run = pairs[j]
            suf[j] = run

        ans = [0] * n
        for i in range(n):
            best = 0
            if i - 2 >= 0:
                best = pre[i - 2]
            if i + 1 <= m - 1 and suf[i + 1] > best:
                best = suf[i + 1]
            if 0 < i < n - 1:
                v = lcp(words[i - 1], words[i + 1])
                if v > best:
                    best = v
            ans[i] = best
        return ans
