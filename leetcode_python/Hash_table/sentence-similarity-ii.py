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

# V1'
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
