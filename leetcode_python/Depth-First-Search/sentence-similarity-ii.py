"""

737. Sentence Similarity II
Medium

We can represent a sentence as an array of words, for example, the sentence "I am happy with leetcode" can be represented as arr = ["I","am",happy","with","leetcode"].

Given two sentences sentence1 and sentence2 each represented as a string array and given an array of string pairs similarPairs where similarPairs[i] = [xi, yi] indicates that the two words xi and yi are similar.

Return true if sentence1 and sentence2 are similar, or false if they are not similar.

Two sentences are similar if:

They have the same length (i.e., the same number of words)
sentence1[i] and sentence2[i] are similar.
Notice that a word is always similar to itself, also notice that the similarity relation is transitive. For example, if the words a and b are similar, and the words b and c are similar, then a and c are similar.

 

Example 1:

Input: sentence1 = ["great","acting","skills"], sentence2 = ["fine","drama","talent"], similarPairs = [["great","good"],["fine","good"],["drama","acting"],["skills","talent"]]
Output: true
Explanation: The two sentences have the same length and each word i of sentence1 is also similar to the corresponding word in sentence2.
Example 2:

Input: sentence1 = ["I","love","leetcode"], sentence2 = ["I","love","onepiece"], similarPairs = [["manga","onepiece"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
Output: true
Explanation: "leetcode" --> "platform" --> "anime" --> "manga" --> "onepiece".
Since "leetcode is similar to "onepiece" and the first two words are the same, the two sentences are similar.
Example 3:

Input: sentence1 = ["I","love","leetcode"], sentence2 = ["I","love","onepiece"], similarPairs = [["manga","hunterXhunter"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
Output: false
Explanation: "leetcode" is not similar to "onepiece".
 

Constraints:

1 <= sentence1.length, sentence2.length <= 1000
1 <= sentence1[i].length, sentence2[i].length <= 20
sentence1[i] and sentence2[i] consist of lower-case and upper-case English letters.
0 <= similarPairs.length <= 2000
similarPairs[i].length == 2
1 <= xi.length, yi.length <= 20
xi and yi consist of English letters.

"""

# V0
# https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
# IDEA : DFS
# CONCEPT : 
# -> 1) MAKE A GRAPH
# -> 2) PUT ALL word belong to "the same" "group" into the same connected components
# -> 3) GO THROUGH EVERY WORD IN words1, words2 AND CHECK IF THEY ARE IN THE SAME connected components (use DFS TO this)
# STEPS :
# -> 1) MAKE A GRAPH (w1 -> w2, w2 -> w1)
# -> 2) DFS GO THROUGH EVERY WORD IN words1, words2 AND CHECK IF THEY ARE IN THE SAME connected components
import collections
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)
        ### NOTICE HERE : use DFS to check if 2 words is in the SAME "word cluster"
        def dfs(words1, words2, visits):
            for similar in similars[words2]:
                if words1 == similar:
                    return True
                elif similar not in visits:
                    visits.add(similar)
                    if dfs(words1, similar, visits):
                        return True
            return False

        for w1, w2 in zip(words1, words2):
            if w1 != w2 and not dfs(w1, w2, set([w2])):
                return False
        return True

# V1
# IDEA : DFS
# https://leetcode.com/problems/sentence-similarity-ii/solution/
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        graph = collections.defaultdict(list)
        for w1, w2 in pairs:
            graph[w1].append(w2)
            graph[w2].append(w1)

        for w1, w2 in zip(words1, words2):
            stack, seen = [w1], {w1}
            while stack:
                word = stack.pop()
                if word == w2: break
                for nei in graph[word]:
                    if nei not in seen:
                        seen.add(nei)
                        stack.append(nei)
            else:
                return False
        return True

# V1'
# IDEA : Union-Find
# https://leetcode.com/problems/sentence-similarity-ii/solution/
class DSU:
    def __init__(self, N):
        self.par = range(N)
    def find(self, x):
        if self.par[x] != x:
            self.par[x] = self.find(self.par[x])
        return self.par[x]
    def union(self, x, y):
        self.par[self.find(x)] = self.find(y)

class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2): return False

        index = {}
        count = itertools.count()
        dsu = DSU(2 * len(pairs))
        for pair in pairs:
            for p in pair:
                if p not in index:
                    index[p] = next(count)
            dsu.union(index[pair[0]], index[pair[1]])

        return all(w1 == w2 or
                   w1 in index and w2 in index and
                   dsu.find(index[w1]) == dsu.find(index[w2])
                   for w1, w2 in zip(words1, words2))

# V1'' 
# http://bookshadow.com/weblog/2017/11/26/leetcode-sentence-similarity-ii/
# https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
# IDEA : DICT + HASH TABLE + BFS/DFS 
import collections
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        if len(words1) != len(words2): return False
        similars = collections.defaultdict(set)
        for w1, w2 in pairs:
            similars[w1].add(w2)
            similars[w2].add(w1)

        def dfs(words1, words2, visits):
            for similar in similars[words2]:
                if words1 == similar:
                    return True
                elif similar not in visits:
                    visits.add(similar)
                    if dfs(words1, similar, visits):
                        return True
            return False

        for w1, w2 in zip(words1, words2):
            if w1 != w2 and not dfs(w1, w2, set([w2])):
                return False
        return True

### Test case : dev 

# V1''''
# https://www.jiuzhang.com/solution/sentence-similarity-ii/#tag-highlight-lang-python
# IDEA : UNION FIND
class Solution():
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2): 
            return False
        graph = collections.defaultdict(list)
        for w1, w2 in pairs:
            graph[w1].append(w2)
            graph[w2].append(w1)

        for w1, w2 in zip(words1, words2):
            stack, seen = [w1], {w1}
            while stack:
                word = stack.pop()
                if word == w2: break
                for nei in graph[word]:
                    if nei not in seen:
                        seen.add(nei)
                        stack.append(nei)
            else:
                return False
        return True

# V2
# Time:  O(n + p)
# Space: O(p)
# IDEA : UnionFind
import itertools
class UnionFind(object):
    def __init__(self, n):
        self.set = range(n)

    def find_set(self, x):
        if self.set[x] != x:
            self.set[x] = self.find_set(self.set[x])  # path compression.
        return self.set[x]

    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root == y_root:
            return False
        self.set[min(x_root, y_root)] = max(x_root, y_root)
        return True

class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        if len(words1) != len(words2): return False

        lookup = {}
        union_find = UnionFind(2 * len(pairs))
        for pair in pairs:
            for p in pair:
                if p not in lookup:
                    lookup[p] = len(lookup)
            union_find.union_set(lookup[pair[0]], lookup[pair[1]])

        return all(w1 == w2 or
                   w1 in lookup and w2 in lookup and
                   union_find.find_set(lookup[w1]) == union_find.find_set(lookup[w2])
                   for w1, w2 in itertools.izip(words1, words2))