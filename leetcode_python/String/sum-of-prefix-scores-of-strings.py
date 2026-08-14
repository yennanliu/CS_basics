"""

2416. Sum of Prefix Scores of Strings
Hard

You are given an array words of size n consisting of non-empty strings.

We define the score of a string term as the number of strings words[i] such that term is a prefix of words[i].

For example, if words = ["a", "ab", "abc", "cab"], then the score of "ab" is 2, since "ab" is a prefix of both "ab" and "abc".

Return an array answer of size n where answer[i] is the sum of scores of every non-empty prefix of words[i].

Note that a string is considered as a prefix of itself.


Example 1:

Input: words = ["abc","ab","bc","b"]
Output: [5,4,3,2]
Explanation: The answer for each string is the following:
- "abc" has 3 prefixes: "a", "ab", and "abc".
- There are 2 strings with the prefix "a", 2 strings with the prefix "ab", and 1 string with the prefix "abc".
The total is answer[0] = 2 + 2 + 1 = 5.
- "ab" has 2 prefixes: "a" and "ab".
- There are 2 strings with the prefix "a", and 2 strings with the prefix "ab".
The total is answer[1] = 2 + 2 = 4.
- "bc" has 2 prefixes: "b" and "bc".
- There are 2 strings with the prefix "b", and 1 string with the prefix "bc".
The total is answer[2] = 2 + 1 = 3.
- "b" has 1 prefix: "b".
- There are 2 strings with the prefix "b".
The total is answer[3] = 2.

Example 2:

Input: words = ["abcd"]
Output: [4]
Explanation:
"abcd" has 4 prefixes: "a", "ab", "abc", and "abcd".
Each prefix has a score of one, so the total is answer[0] = 1 + 1 + 1 + 1 = 4.


Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 1000
words[i] consists of lowercase English letters.

"""

# V0
# IDEA : TRIE WHERE EACH NODE COUNTS HOW MANY WORDS PASS THROUGH IT
#
#   the score of a prefix p is exactly the number of words having p as a
#   prefix — i.e. the visit count of p's trie node.
#
#   pass 1 : insert every word, incrementing `count` on each node along its
#            path.
#   pass 2 : walk each word again and sum the counts of the nodes it visits;
#            that is the sum over all its non-empty prefixes.
#
#   the trie is stored as plain dicts of children plus an integer count,
#   which keeps it short without a node class.
#
# time = O(total characters), space = O(total characters)
class Solution(object):
    def sumPrefixScores(self, words):
        children = [{}]      # children[node][char] -> node index
        count = [0]

        for w in words:
            node = 0
            for c in w:
                if c not in children[node]:
                    children.append({})
                    count.append(0)
                    children[node][c] = len(children) - 1
                node = children[node][c]
                count[node] += 1

        res = []
        for w in words:
            node = 0
            total = 0
            for c in w:
                node = children[node][c]
                total += count[node]
            res.append(total)
        return res
