"""

1258. Synonymous Sentences
Medium

You are given a list of equivalent string pairs synonyms where
synonyms[i] = [si, ti] indicates that si and ti are equivalent strings.
You are also given a sentence text.

Return all possible synonymous sentences sorted lexicographically.


Example 1:

Input: synonyms = [["happy","joy"],["sad","sorrow"],["joy","cheerful"]], text = "I am happy today but was sad yesterday"
Output: ["I am cheerful today but was sad yesterday","I am cheerful today but was sorrow yesterday","I am happy today but was sad yesterday","I am happy today but was sorrow yesterday","I am joy today but was sad yesterday","I am joy today but was sorrow yesterday"]

Example 2:

Input: synonyms = [["happy","joy"],["cheerful","glad"]], text = "I am happy today but was sad yesterday"
Output: ["I am happy today but was sad yesterday","I am joy today but was sad yesterday"]


Constraints:

0 <= synonyms.length <= 10
synonyms[i].length == 2
1 <= si.length, ti.length <= 10
si != ti
text consists of at most 10 words.
All the pairs of synonyms are unique.
The words of text are separated by single spaces.

"""

# V0
# IDEA : UNION FIND + BACKTRACK (DFS)
#        1) union all synonym pairs -> each group = a set of interchangeable words
#        2) DFS over the words of `text`, branching on every word of its group
# time = O(m ^ w * w)
# m = max group size, w = number of words (both <= ~20 by constraints)
# space = O(n + w), n = number of distinct synonym words
class Solution(object):
    def generateSentences(self, synonyms, text):
        # union find
        parent = {}

        def find(x):
            parent.setdefault(x, x)
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        for a, b in synonyms:
            union(a, b)

        # group : root -> sorted list of its words
        group = {}
        for w in parent:
            group.setdefault(find(w), []).append(w)
        for k in group:
            group[k].sort()

        words = text.split()
        res = []
        cur = []

        def dfs(i):
            if i == len(words):
                res.append(" ".join(cur))
                return
            w = words[i]
            # word has no synonym -> keep it as it is
            if w not in parent:
                cur.append(w)
                dfs(i + 1)
                cur.pop()
            else:
                for cand in group[find(w)]:
                    cur.append(cand)
                    dfs(i + 1)
                    cur.pop()

        dfs(0)
        res.sort()
        return res
