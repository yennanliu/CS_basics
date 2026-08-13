"""

1177. Can Make Palindrome from Substring
Medium

You are given a string s and array queries where queries[i] = [left_i, right_i, k_i].
We may rearrange the substring s[left_i...right_i] for each query and then choose up to k_i of them
to replace with any lowercase English letter.

If the substring is possible to be a palindrome string after the operations above, the result of the
query is true. Otherwise, the result is false.

Return a boolean array answer where answer[i] is the result of the i-th query queries[i].

Note that each letter is counted individually for replacement, so if, for example
s[left_i...right_i] = "aaa", and k_i = 2, we can only replace two of the letters.
Also, note that no query modifies the initial string s.

Example 1:

Input: s = "abcda", queries = [[3,3,0],[1,2,0],[0,3,1],[0,3,2],[0,4,1]]
Output: [true,false,false,true,true]
Explanation:
queries[0]: substring = "d", is palindrome.
queries[1]: substring = "bc", is not palindrome.
queries[2]: substring = "abcd", is not palindrome after replacing only 1 character.
queries[3]: substring = "abcd", could be changed to "abba" which is palindrome.
Also this can be changed to "baab" first rearrange it "bacd" then replace "cd" with "ab".
queries[4]: substring = "abcda", could be changed to "abcba" which is palindrome.

Example 2:

Input: s = "lyb", queries = [[0,1,0],[2,2,1]]
Output: [false,true]

Constraints:

1 <= s.length, queries.length <= 10^5
0 <= left_i <= right_i < s.length
0 <= k_i <= s.length
s consists of lowercase English letters.

"""

# V0
# IDEA : PREFIX XOR BITMASK (parity of each letter count)
#
#  after a free rearrange, the only obstacle is letters with an ODD count.
#  if `odd` letters have odd counts, we can pair them up 2-by-2 by rewriting
#  one of each pair -> odd // 2 replacements are needed
#  (the possibly leftover single odd letter sits in the middle for free).
#
#  answer[i] = (odd // 2 <= k)
#
#  parity is tracked with a 26-bit mask, prefix[i] = xor of masks of s[:i],
#  so the substring parity is prefix[r+1] ^ prefix[l].
#
# time = O(n + m)
# space = O(n)
class Solution(object):
    def canMakePaliQueries(self, s, queries):
        n = len(s)
        pre = [0] * (n + 1)
        for i, c in enumerate(s):
            pre[i + 1] = pre[i] ^ (1 << (ord(c) - ord('a')))

        res = []
        for l, r, k in queries:
            mask = pre[r + 1] ^ pre[l]
            odd = bin(mask).count('1')
            res.append(odd // 2 <= k)
        return res


# V1
# IDEA : PREFIX SUM of the 26 letter counts
# (same idea, keeps the actual counts instead of only the parity)
# time = O((n + m) * 26)
# space = O(n * 26)
class Solution(object):
    def canMakePaliQueries(self, s, queries):
        n = len(s)
        ss = [[0] * 26 for _ in range(n + 1)]
        for i, c in enumerate(s):
            for j in range(26):
                ss[i + 1][j] = ss[i][j]
            ss[i + 1][ord(c) - ord('a')] += 1

        res = []
        for l, r, k in queries:
            odd = sum((ss[r + 1][j] - ss[l][j]) & 1 for j in range(26))
            res.append(odd // 2 <= k)
        return res
