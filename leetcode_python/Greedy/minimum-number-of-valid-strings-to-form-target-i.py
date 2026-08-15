"""

3291. Minimum Number of Valid Strings to Form Target I
Medium

You are given an array of strings words and a string target.

A string x is called valid if x is a prefix of any string in words.

Return the minimum number of valid strings that can be concatenated to form target. If it is not possible to form target, return -1.


Example 1:

Input: words = ["abc","aaaaa","bcdef"], target = "aabcdabc"
Output: 3
Explanation:
The target string can be formed by concatenating:
Prefix of length 2 of words[1], i.e. "aa".
Prefix of length 3 of words[2], i.e. "bcd".
Prefix of length 3 of words[0], i.e. "abc".

Example 2:

Input: words = ["abababab","ab"], target = "ababaababa"
Output: 2
Explanation:
The target string can be formed by concatenating:
Prefix of length 5 of words[0], i.e. "ababa".
Prefix of length 5 of words[0], i.e. "ababa".

Example 3:

Input: words = ["abcdef"], target = "xyz"
Output: -1


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 5 * 10^3
The input is generated such that sum(words[i].length) <= 10^5.
words[i] consists only of lowercase English letters.
1 <= target.length <= 5 * 10^3
target consists only of lowercase English letters.

"""

# V0
# IDEA : LONGEST REACH PER START, THEN THE JUMP-GAME GREEDY
#
#   a valid piece starting at position i can be any prefix of a word that
#   matches target there — and if a piece of length L fits, so does every
#   shorter one. so all that matters is
#
#       reach[i] = i + (longest prefix of some word matching target at i)
#
#   which a trie over the words answers by walking target from i until the
#   trie runs out.
#
#   with those reaches the problem becomes exactly Jump Game II : sweep left
#   to right keeping the furthest position reachable with the current number
#   of pieces, and add a piece when the sweep passes the current frontier.
#
# time = O(|target| * max word length + total word length), space = O(total word length)
class Solution(object):
    def minValidStrings(self, words, target):
        n = len(target)

        # trie of the words
        children = [{}]
        for w in words:
            node = 0
            for ch in w:
                nxt = children[node].get(ch)
                if nxt is None:
                    children.append({})
                    nxt = len(children) - 1
                    children[node][ch] = nxt
                node = nxt

        reach = [0] * n
        for i in range(n):
            node = 0
            j = i
            while j < n:
                nxt = children[node].get(target[j])
                if nxt is None:
                    break
                node = nxt
                j += 1
            reach[i] = j                        # exclusive end

        res = 0
        cur_end = 0                             # frontier of the current piece
        farthest = 0
        i = 0
        while cur_end < n:
            while i < n and i <= cur_end:
                if reach[i] > farthest:
                    farthest = reach[i]
                i += 1
            if farthest <= cur_end:
                return -1
            cur_end = farthest
            res += 1
        return res
