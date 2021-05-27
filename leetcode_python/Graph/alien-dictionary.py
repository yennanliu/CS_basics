"""

There is a new alien language which uses the latin alphabet. 
However, the order among letters are unknown to you. 
You receive a list of non-empty words from the dictionary, 
where words are sorted lexicographically by the rules of this new language. 
Derive the order of letters in this language.

Example 1:

Input:
[
  "wrt",
  "wrf",
  "er",
  "ett",
  "rftt"
]

Output: "wertf"

Example 2:

Input:
[
  "z",
  "x"
]

Output: "zx"

Example 3:

Input:
[
  "z",
  "x",
  "z"
] 

Output: "" 

Explanation: The order is invalid, so return "".
Note:

You may assume all letters are in lowercase.
You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
If the order is invalid, return an empty string.
There may be multiple valid order of letters, return any one of them is fine.

"""

# V0

# V1
# https://blog.csdn.net/qq_37821701/article/details/108807236
# IDEA : Topological sorting (official solution)
class Solution:
    def alienOrder(self, words: List[str]) -> str:
        # create adject matrx of the graph
        adj_list = collections.defaultdict(set)
        # create initial indegrees 0 for all distinct words
        indegrees = {}
        for word in words:
            for c in word:
                if c in indegrees:
                    continue
                indegrees[c] = 0
        
        # construct the graph and indegrees
        for first_word,second_word in zip(words,words[1:]):
            for c,d in zip(first_word,second_word):
                if c!=d:
                    # this line is needed, otherwise the indegrees of d will be repeatedly added
                    if d not in adj_list[c]:
                        adj_list[c].add(d)
                        indegrees[d]+=1
                    break
            # this 'else' will still match with the 'if' inside the for loop, 
            # it means if after any zip pairs c and d is not equal, 
            # codes in 'else' won't be runned. only when all pairs are equal, 
            # then codes in 'else' will be runned. 
            # In other words, the 'else' match to the final 'if' of the for loop
                else:
                    # check if the second word is a prefix of the first word
                    if len(second_word)<len(first_word):
                        return ''
        
        # pick all nodes with zero indegree and put it into queue
        q = collections.deque()
        for k,v in indegrees.items():
            if v==0:
                q.append(k)
        
        # pick off zero indegree nodes level by level,and add to the output
        ans = []
        while q:
            c = q.popleft()
            ans.append(c)
            for d in adj_list[c]:
                indegrees[d] -= 1
                if indegrees[d]==0:
                    q.append(d)
        
        # if there are letter that not appear in the output, means there is a cycle in the graph, because on the indegrees of nodes in a cycle will all be non-zero
        if len(ans)<len(indegrees):
            return ''
        
        return "".join(ans)

# V1'
# https://www.cnblogs.com/lightwindy/p/8531872.html
# IDEA :BFS 
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, zero_in_degree_queue, in_degree, out_degree = [], collections.deque(), {}, {}
        nodes = sets.Set()
        for word in words:
            for c in word:
                nodes.add(c)
         
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)
         
        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)
         
        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)
             
            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)
             
                del out_degree[precedence]
         
        if out_degree:
            return ""
 
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in range(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = sets.Set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = sets.Set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break　　

### Test case : dev 

# V1'
# https://www.cnblogs.com/lightwindy/p/8531872.html
# IDEA : DFS
class Solution(object):
    def alienOrder(self, words):
        # Find ancestors of each node by DFS.
        nodes, ancestors = sets.Set(), {}
        for i in range(len(words)):
            for c in words[i]:
                nodes.add(c)
        for node in nodes:
            ancestors[node] = []
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], ancestors)
 
        # Output topological order by DFS.
        result = []
        visited = {}
        for node in nodes:
            if self.topSortDFS(node, node, ancestors, visited, result):
                return ""
         
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, ancestors):
        min_len = min(len(word1), len(word2))
        for i in range(min_len):
            if word1[i] != word2[i]:
                ancestors[word2[i]].append(word1[i])
                break
 
 
    # Topological sort, return whether there is a cycle.
    def topSortDFS(self, root, node, ancestors, visited, result):
        if node not in visited:
            visited[node] = root
            for ancestor in ancestors[node]:
                if self.topSortDFS(root, ancestor, ancestors, visited, result):
                    return True
            result.append(node)
        elif visited[node] == root:
            # Visited from the same root in the DFS path.
            # So it is cyclic.
            return True
        return False

# V1''
# https://yao.page/posts/alien-dictionary-python/
class Solution(object):
    def alien_dictionary(self, words):
        n = len(words)

        # Init nodes
        nodes = {}
        for word in words:
            for c in word:
                if c not in nodes:
                    nodes[c] = Node(c)

        # Build graph
        for i in range(n-1):
            for c1, c2 in zip(words[i], words[i+1]):
                if c1 == c2:
                    continue
                elif c1 != c2:
                    nodes[c2].to.add(c1)
                    break

        for label in nodes:
            print(label, nodes[label].to)

        # Run topo sort
        visiting = set()
        res = []

        def topo_sort(label):
            visiting.add(label)

            node = nodes[label]
            for v_label in node.to:
                if v_label in visiting:
                    return False

                if v_label in nodes:
                    if topo_sort(v_label) is False:
                        return False

            res.append(label)
            del nodes[label]

            visiting.remove(label)

        while nodes:
            label = next(iter(nodes))
            if topo_sort(label) is False:
                return ""

        return ''.join(res)

# V1'''
# https://medium.com/@dimko1/alien-dictionary-6cf2da24bf3c
class Solution(object):
    def alienOrder(self, words):
        pre = collections.defaultdict(set)
        suc = collections.defaultdict(set)

        for pair in zip(words, words[1:]):
            for a, b in zip(*pair):
                if a != b:
                    suc[a].add(b)
                    pre[b].add(a)
                    break
        chars = set(''.join(words))
        charToProcess = chars - set(pre)
        order = ''
        while charToProcess:
            ch = charToProcess.pop()
            order += ch
            for b in suc[ch]:
                pre[b].discard(ch)
                if not pre[b]:
                    charToProcess.add(b)
        return order * (set(order) == chars)

# V1''''
# https://blog.csdn.net/a921122/article/details/60407972
# IDEA : topology sort
# IDEA : JAVA


# V1'''''
# https://www.jianshu.com/p/19b5459c53e2
# IDEA : topology sort
# IDEA : C++


# V2
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/alien-dictionary.py
# Time:  O(n)
# Space: O(|V|+|E|) = O(26 + 26^2) = O(1)
import collections
# BFS solution.
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, in_degree, out_degree = [], {}, {}
        zero_in_degree_queue = collections.deque()
        nodes = set()
        for word in words:
            for c in word:
                nodes.add(c)

        for i in xrange(1, len(words)):
            if (len(words[i-1]) > len(words[i]) and
                    words[i-1][:len(words[i])] == words[i]):
                return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)

        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)

        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)

            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)

                del out_degree[precedence]

        if out_degree:
            return ""

        return "".join(result)

    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in xrange(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break

# DFS solution.
class Solution2(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        # Find ancestors of each node by DFS.
        nodes, ancestors = set(), {}
        for i in xrange(len(words)):
            for c in words[i]:
                nodes.add(c)
        for node in nodes:
            ancestors[node] = []
        for i in xrange(1, len(words)):
            if (len(words[i-1]) > len(words[i]) and
                    words[i-1][:len(words[i])] == words[i]):
                return ""
            self.findEdges(words[i - 1], words[i], ancestors)

        # Output topological order by DFS.
        result = []
        visited = {}
        for node in nodes:
            if self.topSortDFS(node, node, ancestors, visited, result):
                return ""

        return "".join(result)

    # Construct the graph.
    def findEdges(self, word1, word2, ancestors):
        min_len = min(len(word1), len(word2))
        for i in xrange(min_len):
            if word1[i] != word2[i]:
                ancestors[word2[i]].append(word1[i])
                break

    # Topological sort, return whether there is a cycle.
    def topSortDFS(self, root, node, ancestors, visited, result):
        if node not in visited:
            visited[node] = root
            for ancestor in ancestors[node]:
                if self.topSortDFS(root, ancestor, ancestors, visited, result):
                    return True
            result.append(node)
        elif visited[node] == root:
            # Visited from the same root in the DFS path.
            # So it is cyclic.
            return True
        return False
    
# V3
# https://shareablecode.com/snippets/alien-dictionary-python-solution-leetcode-E6Er-DiZ3
import collections

# BFS solution.
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, in_degree, out_degree = [], {}, {}
        zero_in_degree_queue = collections.deque()
        nodes = set()
        for word in words:
            for c in word:
                nodes.add(c)

        for i in xrange(1, len(words)):
            if (len(words[i-1]) > len(words[i]) and
                    words[i-1][:len(words[i])] == words[i]):
                return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)

        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)

        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)

            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)

                del out_degree[precedence]

        if out_degree:
            return ""

        return "".join(result)

    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in xrange(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break


# DFS solution.
class Solution2(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        # Find ancestors of each node by DFS.
        nodes, ancestors = set(), {}
        for i in xrange(len(words)):
            for c in words[i]:
                nodes.add(c)
        for node in nodes:
            ancestors[node] = []
        for i in xrange(1, len(words)):
            if (len(words[i-1]) > len(words[i]) and
                    words[i-1][:len(words[i])] == words[i]):
                return ""
            self.findEdges(words[i - 1], words[i], ancestors)

        # Output topological order by DFS.
        result = []
        visited = {}
        for node in nodes:
            if self.topSortDFS(node, node, ancestors, visited, result):
                return ""

        return "".join(result)

    # Construct the graph.
    def findEdges(self, word1, word2, ancestors):
        min_len = min(len(word1), len(word2))
        for i in xrange(min_len):
            if word1[i] != word2[i]:
                ancestors[word2[i]].append(word1[i])
                break

    # Topological sort, return whether there is a cycle.
    def topSortDFS(self, root, node, ancestors, visited, result):
        if node not in visited:
            visited[node] = root
            for ancestor in ancestors[node]:
                if self.topSortDFS(root, ancestor, ancestors, visited, result):
                    return True
            result.append(node)
        elif visited[node] == root:
            # Visited from the same root in the DFS path.
            # So it is cyclic.
            return True
        return False

