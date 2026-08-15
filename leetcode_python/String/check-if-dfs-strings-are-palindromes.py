"""

3327. Check if DFS Strings Are Palindromes
Hard

You are given a tree rooted at node 0, consisting of n nodes numbered from 0 to n - 1. The tree is represented by an array parent, where parent[i] is the parent of node i. Since node 0 is the root, parent[0] == -1.

You are also given a string s of length n, where s[i] is the character assigned to node i.

Consider an empty string dfsStr, and define a recursive function dfs(int x) that takes a node x as a parameter and performs the following steps in order:

Iterate over each child y of x in increasing order of their numbers, and call dfs(y).
Add the character s[x] to the end of the string dfsStr.

Note that dfsStr is shared across all recursive calls of dfs.

You need to find a boolean array answer of size n, where for every index i from 0 to n - 1, you do the following:

Empty the string dfsStr and call dfs(i).
If the resulting string dfsStr is a palindrome, then set answer[i] to true. Otherwise, set answer[i] to false.

Return the array answer.


Example 1:

Input: parent = [-1,0,0,1,1,2], s = "aababa"
Output: [true,true,false,true,true,true]
Explanation:
Calling dfs(0) results in the string dfsStr = "abaaba", which is a palindrome.
Calling dfs(1) results in the string dfsStr = "aba", which is a palindrome.
Calling dfs(2) results in the string dfsStr = "ab", which is not a palindrome.
Calling dfs(3) results in the string dfsStr = "a", which is a palindrome.
Calling dfs(4) results in the string dfsStr = "b", which is a palindrome.
Calling dfs(5) results in the string dfsStr = "a", which is a palindrome.

Example 2:

Input: parent = [-1,0,0,0,0], s = "aabcb"
Output: [true,true,true,true,true]
Explanation:
Every call on dfs(x) results in a palindrome string.


Constraints:

n == parent.length == s.length
1 <= n <= 10^5
0 <= parent[i] <= n - 1 for all i >= 1.
parent[0] == -1
parent represents a valid tree.
s consists only of lowercase English letters.

"""

# V0
# IDEA : ONE GLOBAL DFS STRING — EVERY SUBTREE IS A CONTIGUOUS SLICE OF IT
#
#   the traversal appends a node's character AFTER all of its children, and
#   the children are visited in index order. so running dfs once from the
#   root produces a string in which each node's own dfs result is exactly the
#   block [start[x], end[x]] — its descendants sit together, followed by x.
#
#   that turns n separate traversals into n substring-palindrome tests, and
#   those are answered in O(1) each by hashing the string and its reverse and
#   comparing the two hashes of the same range.
#
#   both the traversal and the hashing are iterative and linear; a 10^5-node
#   path would otherwise blow the recursion limit.
#
# time = O(n), space = O(n)
class Solution(object):
    def findAnswer(self, parent, s):
        n = len(parent)
        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[parent[i]].append(i)
        for kids in children:
            kids.sort()

        # iterative post-order producing the global dfs string
        order = []
        start = [0] * n
        end = [0] * n
        stack = [(0, False)]
        while stack:
            x, done = stack.pop()
            if done:
                order.append(x)
                end[x] = len(order) - 1
                continue
            start[x] = len(order)
            stack.append((x, True))
            for y in reversed(children[x]):
                stack.append((y, False))

        text = ''.join(s[x] for x in order)
        m = len(text)

        MOD = (1 << 61) - 1
        BASE = 131
        pw = [1] * (m + 1)
        for i in range(m):
            pw[i + 1] = pw[i] * BASE % MOD

        fwd = [0] * (m + 1)
        for i, ch in enumerate(text):
            fwd[i + 1] = (fwd[i] * BASE + ord(ch)) % MOD
        bwd = [0] * (m + 1)
        for i in range(m - 1, -1, -1):
            bwd[i] = (bwd[i + 1] * BASE + ord(text[i])) % MOD

        def fwd_hash(lo, hi):                   # text[lo:hi]
            return (fwd[hi] - fwd[lo] * pw[hi - lo]) % MOD

        def bwd_hash(lo, hi):                   # text[lo:hi] read backwards
            return (bwd[lo] - bwd[hi] * pw[hi - lo]) % MOD

        return [fwd_hash(start[x], end[x] + 1) == bwd_hash(start[x], end[x] + 1)
                for x in range(n)]
