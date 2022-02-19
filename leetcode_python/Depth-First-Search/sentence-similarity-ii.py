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
# IDEA : DFS
from collections import defaultdict
class Solution(object):
    def areSentencesSimilarTwo(self, sentence1, sentence2, similarPairs):
        # helper func
        def dfs(w1, w2, visited):
            for j in d[w2]:
                if w1 == w2:
                    return True
                elif j not in visited:
                    visited.add(j)
                    if dfs(w1, j, visited):
                        return True
            return False
        
        # edge case
        if len(sentence1) != len(sentence2):
            return False
      
        d = defaultdict(list)
        for a, b in similarPairs:
            d[a].append(b)
            d[b].append(a)
            
        for i in range(len(sentence1)):
            visited =  set([sentence2[i]])
            if sentence1[i] != sentence2[i] and not dfs(sentence1[i],  sentence2[i], visited):
                return False
        return True

# V0'
# IDEA : BFS
class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        # Problem tells us this case will mean not similar. 
        if len(words1) != len(words2):
            return False
            
        # Create a bidirectional graph of similar words.
        graph = collections.defaultdict(set)
        for w1, w2 in pairs:
            graph[w1].add(w2)
            graph[w2].add(w1)
        # Now for every w1, w2 combo, see if we can traverse the graph and go from one to the other.
        for w1, w2 in zip(words1, words2):
            # Use a queue for processing neighboring words.
            q = collections.deque([])
            q.append(w1)
            # Keep track of the words we vitsit (so we dont get stuck in a cycle).
            seen = set()
            while q:
                wrd = q.popleft()
                # If the current word is our w2 we made it through the graph, on to the next w1, w2.
                if wrd == w2:
                    break
                # Otherwise keep traversing.
                for nw in graph[wrd]:
                    if nw not in seen:
                        q.append(nw)
                        seen.add(nw)
            # Python break syntax here, if we don't break, aka. we don't make it to w2
            # we know there is no similarity, therefore we can end here and return False.
            else:
                return False
        # If we work through all words we make it here, and we know they are similar.
        return True

# V0'''
# IDEA : UNION FIND
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2):
            return False
        
        parent = dict()
        
        def add(x):
            if x not in parent:
                parent[x] = x
                
        def find(x):
            if x == parent[x]:
                return x
            parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            parentX = find(x)
            parentY = find(y)
            if parentX == parentY:
                return
            parent[parentY] = parentX
            
        for a, b in pairs:
            add(a)
            add(b)
            union(a, b)
            
        # print parent
        for word1, word2 in zip(words1, words2):
            # print word1, word2
            if word1 == word2:
                continue
            if word1 not in parent or word2 not in parent:
                return False
            if find(word1) != find(word2):
                return False
        return True

# V0''''
# IDEA : DFS
# https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
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
# IDEA : BFS
# https://leetcode.com/problems/sentence-similarity-ii/discuss/928878/Easy-%2B-Straightforward-Python-BFS-with-Explaination-and-Comments!
class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        # Problem tells us this case will mean not similar. 
        if len(words1) != len(words2):
            return False
            
        # Create a bidirectional graph of similar words.
        graph = collections.defaultdict(set)
        for w1, w2 in pairs:
            graph[w1].add(w2)
            graph[w2].add(w1)
        # Now for every w1, w2 combo, see if we can traverse the graph and go from one to the other.
        for w1, w2 in zip(words1, words2):
            # Use a queue for processing neighboring words.
            q = collections.deque([])
            q.append(w1)
            # Keep track of the words we vitsit (so we dont get stuck in a cycle).
            seen = set()
            while q:
                wrd = q.popleft()
                # If the current word is our w2 we made it through the graph, on to the next w1, w2.
                if wrd == w2:
                    break
                # Otherwise keep traversing.
                for nw in graph[wrd]:
                    if nw not in seen:
                        q.append(nw)
                        seen.add(nw)
            # Python break syntax here, if we don't break, aka. we don't make it to w2
            # we know there is no similarity, therefore we can end here and return False.
            else:
                return False
        # If we work through all words we make it here, and we know they are similar.
        return True

# V1''
# IDEA : DICT + HASH TABLE + BFS/DFS 
# http://bookshadow.com/weblog/2017/11/26/leetcode-sentence-similarity-ii/
# https://zxi.mytechroad.com/blog/hashtable/leetcode-737-sentence-similarity-ii/
import collections
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
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


# V1'''
# IDEA : DFS (queue format)
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

# V1''''
# IDEA : UnionFind
# https://leetcode.com/problems/sentence-similarity-ii/discuss/725681/Python-Union-Find-solution
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2):
            return False
        
        parent = dict()
        
        def add(x):
            if x not in parent:
                parent[x] = x
                
        def find(x):
            if x == parent[x]:
                return x
            parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            parentX = find(x)
            parentY = find(y)
            if parentX == parentY:
                return
            parent[parentY] = parentX
            
        for a, b in pairs:
            add(a)
            add(b)
            union(a, b)
            
        # print parent
        for word1, word2 in zip(words1, words2):
            # print word1, word2
            if word1 == word2:
                continue
            if word1 not in parent or word2 not in parent:
                return False
            if find(word1) != find(word2):
                return False
        return True

# V1''''''
# IDEA : UnionFind
# https://leetcode.com/problems/sentence-similarity-ii/discuss/304988/Python-Solution%3A-standard-union-find
class UnionFind:

    def __init__(self, capacity):
        self.capacity = capacity
        self.array = [i for i in range(capacity)]
        self.size = [1 for i in range(capacity)]
        self.maxedges = 0

    def find(self, x):
        path = x
        while path != self.array[path]:
            path = self.array[path]

        while x != self.array[x]:
            temp = self.array[x]
            self.array[x] = path
            x = temp

        return path

    def union(self, x, y):
        rootx = self.find(x)
        rooty = self.find(y)

        if rootx == rooty:
            return

        if self.size[rootx] > self.size[rooty]:
            self.size[rootx] += self.size[rooty]
            self.array[rooty] = self.array[rootx]
            self.maxedges = max(self.maxedges, self.size[rootx])
        else:
            self.size[rooty] += self.size[rootx]
            self.array[rootx] = self.array[rooty]
            self.maxedges = max(self.maxedges, self.size[rooty])


class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        uf = UnionFind(len(pairs) * 2)

        word_dict = {}
        counter = 0

        for pair in pairs:
            for word in pair:
                if word not in word_dict:
                    word_dict[word] = counter
                    counter += 1

            uf.union(word_dict[pair[0]], word_dict[pair[1]])

        if len(words1) != len(words2):
            return False

        return words1 == words2 or all(w1 == w2 or w1 in word_dict and w2 in word_dict and uf.find(word_dict[w1]) == uf.find(word_dict[w2]) for w1, w2 in zip(words1, words2))

# V1''''''''
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


# V1''''''''
# IDEA : UNION FIND
# https://leetcode.com/problems/sentence-similarity-ii/discuss/304988/Python-Solution%3A-standard-union-find
class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        def make_set(x):
            if x not in parent:
                parent[x], rank[x] = x, 0
                
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            x_root = find(x)
            y_root = find(y)
            if x_root != y_root:
                if rank[x_root] > rank[y_root]:
                    x_root, y_root = y_root, x_root
                parent[x_root] = y_root
                if rank[x_root] == rank[y_root]:
                    rank[y_root] += 1
                    
        if len(words1) != len(words2):
            return False
        parent, rank = dict(), dict()
        for p1, p2 in pairs:
            make_set(p1)
            make_set(p2)
            union(p1, p2)  
        for w1, w2 in zip(words1, words2):
            if w1 == w2:
                pass
            else:
                if w1 not in parent or w2 not in parent or find(w1) != find(w2):
                    return False
        return True

# V1''''''''
# IDEA : UNION FIND
# https://leetcode.com/problems/sentence-similarity-ii/discuss/574395/Python-Union-Find
class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2): return False
        UF = {}
        def find(x):
            if UF[x] != x:
                UF[x] = find(UF[x])
            return UF[x]
        def union(x, y):
            UF.setdefault(x, x)
            UF.setdefault(y, y)
            UF[find(x)] = find(y)
        for w1, w2 in pairs:
            union(w1, w2)
        return all(w1 == w2 or (w1 in UF and w2 in UF and find(w1) == find(w2)) for w1, w2 in zip(words1, words2))

# V1'''''''''''
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

# V1'''''''''''''''
# IDEA : DFS
# https://leetcode.com/problems/sentence-similarity-ii/discuss/109755/SHORT-Python-DFS-with-explanation
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        from collections import defaultdict
        if len(words1) != len(words2): return False
        words, similar_words = defaultdict(set), {}
        [(words[w1].add(w2), words[w2].add(w1)) for w1, w2 in pairs]
        def dfs(word, root_word):
            if word in similar_words: return
            similar_words[word] = root_word
            [dfs(synonym, root_word) for synonym in words[word]]
        [dfs(word, word) for word in words]
        return all(similar_words.get(w1, w1) == similar_words.get(w2, w2) for w1, w2 in zip(words1, words2))

# V1'''''''''''''''
# IDEA : DFS
# https://leetcode.com/problems/sentence-similarity-ii/discuss/109755/SHORT-Python-DFS-with-explanation
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        from collections import defaultdict
        if len(words1) != len(words2):
            return False
        words = defaultdict(set)
        # Build the graph from pairs.
        for w1, w2 in pairs:
            words[w1].add(w2)
            words[w2].add(w1)

        similar_words = {}
        def dfs(word, root_word):
            if word in similar_words:
                return
            similar_words[word] = root_word
            [dfs(synonym, root_word) for synonym in words[word]]

        # Assign root words.
        [dfs(word, word) for word in words]

        # Compare words.
        return all(similar_words.get(w1, w1) == similar_words.get(w2, w2) for w1, w2 in zip(words1, words2))

# V1''''''''''''''
# IDEA : DFS
# https://leetcode.com/problems/sentence-similarity-ii/discuss/221015/Python-solution
class Solution:
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        """
        :type words1: List[str]
        :type words2: List[str]
        :type pairs: List[List[str]]
        :rtype: bool
        """
        def dfs(word, i):
            part[word] = i
            if word in graph:
                for nei in graph[word]:
                    if nei not in part:
                        dfs(nei, i)
            
        if len(words1) != len(words2):
            return False
        
        # construct word graph using pairs. O(P) time. 
        n = len(words1)
        graph = collections.defaultdict(set)
        word_set = set()
        for pair in pairs:
            graph[pair[0]].add(pair[1])
            graph[pair[1]].add(pair[0])
            word_set.add(pair[0])
            word_set.add(pair[1])
            
        # use DFS to map each word to the connected component
        # in the word graph it belongs to. O(P) time.
        part = {}
        count = 0
        for word in word_set:
            if word not in part:
                dfs(word, count)
                count += 1
        
        # words in the same connected component of the word graph are similar. O(N) time.
        for i in range(n):
            if words1[i] != words2[i]:
                if words1[i] not in part or words2[i] not in part or part[words1[i]] != part[words2[i]]:
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