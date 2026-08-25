# https://leetcode.ca/all/269.html


"""

269. Alien Dictionary
Hard

There is a new alien language that uses the English alphabet. However, the order among the letters is unknown to you.

You are given a list of strings words from the alien language's dictionary, where the strings in words are sorted lexicographically by the rules of this new language.

Return a string of the unique letters in the new alien language sorted in lexicographically increasing order by the new language's rules. If there is no solution, return "". If there are multiple solutions, return any of them.

A string s is lexicographically smaller than a string t if at the first letter where they differ, the letter in s comes before the letter in t in the alien language. If the first min(s.length, t.length) letters are the same, then s is smaller if and only if s.length < t.length.

 

Example 1:

Input: words = ["wrt","wrf","er","ett","rftt"]
Output: "wertf"
Example 2:

Input: words = ["z","x"]
Output: "zx"
Example 3:

Input: words = ["z","x","z"]
Output: ""
Explanation: The order is invalid, so return "".
 

Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 100
words[i] consists of only lowercase English letters.

"""

# V0
# IDEA: TOPOLOGICAL SORT (gpt)
from collections import deque

class Solution(object):
    def alienOrder(self, words):
        # {char: [next_char_1, next_char_2, ...]}
        graph = {c: [] for word in words for c in word}

        # indegree[char] = number of prerequisites
        in_degree = {c: 0 for c in graph}

        # Build graph from adjacent words
        for i in range(1, len(words)):
            prev_w = words[i - 1]
            cur_w = words[i]

            # Find the first different character
            found_diff = False

            for j in range(min(len(prev_w), len(cur_w))):
                prev = prev_w[j]
                cur = cur_w[j]

                if prev != cur:
                    # prev must come before cur
                    #
                    # prev -> cur
                    if cur not in graph[prev]:
                        graph[prev].append(cur)
                        in_degree[cur] += 1

                    found_diff = True
                    break

            # Invalid case:
            # ["abc", "ab"]
            #
            # Longer word comes before its prefix.
            if not found_diff and len(prev_w) > len(cur_w):
                return ""

        # BFS Topological Sort
        q = deque()

        for char in graph:
            if in_degree[char] == 0:
                q.append(char)

        res = []

        while q:
            node = q.popleft()
            res.append(node)

            for next_char in graph[node]:
                in_degree[next_char] -= 1

                if in_degree[next_char] == 0:
                    q.append(next_char)

        # If not all characters are processed,
        # there is a cycle.
        if len(res) != len(graph):
            return ""

        return "".join(res)


# V0-1
# IDEA: TOPOLOGICAL SORT (gpt)
from collections import deque


class Solution(object):

    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """

        # -----------------------------------
        # 1. Initialize graph and in_degree
        # -----------------------------------

        # NOTE !!! we init `graph`, and `in_degree` as dict
        graph = {ch: [] for word in words for ch in word}
        in_degree = {ch: 0 for ch in graph}

        # -----------------------------------
        # 2. Build graph
        # -----------------------------------
        #
        # Only compare adjacent words.
        #
        # Example:
        #     "wrt"
        #     "wrf"
        #
        # First different character:
        #
        #     t -> f
        #
        # Meaning:
        #     t must appear before f
        #

        for i in range(len(words) - 1):

            word1 = words[i]
            word2 = words[i + 1]

            # Invalid case:
            # ["abc", "ab"]
            #
            # A longer word cannot come before
            # its own prefix.
            if len(word1) > len(word2) and word1.startswith(word2):
                return ""

            # Find first different character
            min_len = min(len(word1), len(word2))

            for j in range(min_len):

                if word1[j] != word2[j]:

                    prev = word1[j]
                    cur = word2[j]

                    # Avoid duplicate edge
                    if cur not in graph[prev]:
                        graph[prev].append(cur)
                        in_degree[cur] += 1

                    # Only the FIRST different character matters
                    break

        # -----------------------------------
        # 3. BFS - Topological Sort
        # -----------------------------------

        q = deque()

        for ch in in_degree:
            if in_degree[ch] == 0:
                q.append(ch)

        res = []

        while q:

            node = q.popleft()
            res.append(node)

            for next_node in graph[node]:

                in_degree[next_node] -= 1

                if in_degree[next_node] == 0:
                    q.append(next_node)

        # -----------------------------------
        # 4. Detect cycle
        # -----------------------------------

        if len(res) != len(graph):
            return ""

        return "".join(res)


# V0-2
# IDEA: TOPOLOGICAL SORT (Gemini)
from collections import defaultdict, deque

class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        # 1. Initialize graph and in-degree map for all unique characters
        graph = defaultdict(set)
        in_degree = {char: 0 for word in words for char in word}

        # 2. Build graph by comparing adjacent words
        for i in range(len(words) - 1):
            w1, w2 = words[i], words[i + 1]
            min_len = min(len(w1), len(w2))

            # Edge Case: Invalid prefix order (e.g., ["abc", "ab"])
            if len(w1) > len(w2) and w1[:min_len] == w2[:min_len]:
                return ""

            # Find the first character mismatch
            for j in range(min_len):
                if w1[j] != w2[j]:
                    # Add edge w1[j] -> w2[j]
                    if w2[j] not in graph[w1[j]]:
                        graph[w1[j]].add(w2[j])
                        in_degree[w2[j]] += 1
                    break  # Only the first differing character matters

        # 3. BFS (Kahn's Algorithm) starting with in-degree 0
        queue = deque([char for char in in_degree if in_degree[char] == 0])
        res = []

        while queue:
            curr = queue.popleft()
            res.append(curr)

            for neighbor in graph[curr]:
                in_degree[neighbor] -= 1
                if in_degree[neighbor] == 0:
                    queue.append(neighbor)

        # 4. Check for cycles: if result doesn't contain all unique chars, return ""
        if len(res) < len(in_degree):
            return ""

        return "".join(res)


# V1-1
# IDEA: TOPOLOGICAL SORT
# https://leetcode.ca/2016-08-25-269-Alien-Dictionary/
from collections import defaultdict, deque
from typing import Dict, List, Set

class Solution:
    def alienOrder(self, words: List[str]) -> str:
        graph = defaultdict(set)
        inDegree = defaultdict(int)

        self._buildGraph(graph, words, inDegree)
        return self._topology(graph, inDegree)

    def _buildGraph(self, graph: Dict[str, Set[str]], words: List[str], inDegree: Dict[str, int]) -> None:
        # Create a node for each character in each word
        for word in words:
            for c in word:
                inDegree[c] = 0  # necessary for final char counting

        for first, second in zip(words, words[1:]): # or pairwise(words)
            length = min(len(first), len(second))
            for j in range(length):
                u = first[j]
                v = second[j]
                if u != v:
                    if v not in graph[u]:
                        graph[u].add(v)
                        inDegree[v] += 1
                    break  # Later characters' order is meaningless
                if j == length - 1 and len(first) > len(second):
                    # If 'ab' comes before 'a', it's an invalid order
                    graph.clear()
                    return

    def _topology(self, graph: Dict[str, Set[str]], inDegree: Dict[str, int]) -> str:
        result = ''
        q = deque([c for c in inDegree if inDegree[c] == 0])

        while q:
            u = q.popleft()
            result += u
            for v in graph[u]:
                inDegree[v] -= 1
                if inDegree[v] == 0:
                    q.append(v)

        # If there are remaining characters in inDegree, it means there's a cycle
        if any(inDegree.values()):
            return ''

        # Words = ['z', 'x', 'y', 'x']
        return result if len(result) == len(indegree) else ''



# V1-2
# IDEA:
# https://leetcode.ca/2016-08-25-269-Alien-Dictionary/

import collections


class Node(object):
  def __init__(self, val):
    self.val = val
    self.neighbors = []

  def connect(self, node):
    self.neighbors.append(node)

  def getNbrs(self):
    return self.neighbors


class Solution(object):
  def alienOrder(self, words):
    """
    :type words: List[str]
    :rtype: str
    """

    def dfs(root, graph, visited):
      visited[root] = 1
      for nbr in graph[root].getNbrs():
        if visited[nbr.val] == 0:
          if not dfs(nbr.val, graph, visited):
            return False
        elif visited[nbr.val] == 1:
          return False

      visited[root] = 2
      self.ans += root
      return True

    self.ans = ""
    graph = {}
    visited = collections.defaultdict(int)
    self.topNum = 0
    for i in range(0, len(words) - 1):
      a = words[i]
      b = words[i + 1]
      i = 0
      while i < len(a) and i < len(b):
        if a[i] != b[i]:
          nodeA = nodeB = None
          if a[i] not in graph:
            nodeA = Node(a[i])
            graph[a[i]] = nodeA
          else:
            nodeA = graph[a[i]]
          if b[i] not in graph:
            nodeB = Node(b[i])
            graph[b[i]] = nodeB
          else:
            nodeB = graph[b[i]]
          nodeA.connect(nodeB)
          break
        i += 1
      if i < len(a) and i >= len(b):
        return ""

    for c in graph:
      if visited[c] == 0:
        if not dfs(c, graph, visited):
          return ""

    unUsedSet = set()
    for word in words:
      for c in word:
        unUsedSet.add(c)

    for c in unUsedSet:
      if c not in graph:
        self.ans += c
    return self.ans[::-1]


# V1-3
# IDEA:
# https://leetcode.ca/2016-08-25-269-Alien-Dictionary/
class Solution:
    def alienOrder(self, words: List[str]) -> str:
        g = [[False] * 26 for _ in range(26)]
        s = [False] * 26
        cnt = 0
        n = len(words)
        for i in range(n - 1):
            for c in words[i]:
                if cnt == 26:
                    break
                o = ord(c) - ord('a')
                if not s[o]:
                    cnt += 1
                    s[o] = True
            m = len(words[i])
            for j in range(m):
                if j >= len(words[i + 1]):
                    return ''
                c1, c2 = words[i][j], words[i + 1][j]
                if c1 == c2:
                    continue
                o1, o2 = ord(c1) - ord('a'), ord(c2) - ord('a')
                if g[o2][o1]:
                    return ''
                g[o1][o2] = True
                break
        for c in words[n - 1]:
            if cnt == 26:
                break
            o = ord(c) - ord('a')
            if not s[o]:
                cnt += 1
                s[o] = True

        indegree = [0] * 26
        for i in range(26):
            for j in range(26):
                if i != j and s[i] and s[j] and g[i][j]:
                    indegree[j] += 1
        q = deque()
        ans = []
        for i in range(26):
            if s[i] and indegree[i] == 0:
                q.append(i)
        while q:
            t = q.popleft()
            ans.append(chr(t + ord('a')))
            for i in range(26):
                if s[i] and i != t and g[t][i]:
                    indegree[i] -= 1
                    if indegree[i] == 0:
                        q.append(i)
        return '' if len(ans) < cnt else ''.join(ans)


# V1
# IDEA : BFS
# https://leetcode.com/problems/alien-dictionary/solution/
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
from collections import defaultdict, Counter, deque
def alienOrder(self, words: List[str]) -> str:
    
    # Step 0: create data structures + the in_degree of each unique letter to 0.
    adj_list = defaultdict(set)
    in_degree = Counter({c : 0 for word in words for c in word})
            
    # Step 1: We need to populate adj_list and in_degree.
    # For each pair of adjacent words...
    for first_word, second_word in zip(words, words[1:]):
        for c, d in zip(first_word, second_word):
            if c != d:
                if d not in adj_list[c]:
                    adj_list[c].add(d)
                    in_degree[d] += 1
                break
        else: # Check that second word isn't a prefix of first word.
            if len(second_word) < len(first_word): return ""
    
    # Step 2: We need to repeatedly pick off nodes with an indegree of 0.
    output = []
    queue = deque([c for c in in_degree if in_degree[c] == 0])
    while queue:
        c = queue.popleft()
        output.append(c)
        for d in adj_list[c]:
            in_degree[d] -= 1
            if in_degree[d] == 0:
                queue.append(d)
                
    # If not all letters are in output, that means there was a cycle and so
    # no valid ordering. Return "" as per the problem description.
    if len(output) < len(in_degree):
        return ""
    # Otherwise, convert the ordering we found into a string and return it.
    return "".join(output)

# V1
# IDEA : DFS
# https://leetcode.com/problems/alien-dictionary/solution/
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
def alienOrder(self, words: List[str]) -> str:

    # Step 0: Put all unique letters into the adj list.
    reverse_adj_list = {c : [] for word in words for c in word}

    # Step 1: Find all edges and put them in reverse_adj_list.
    for first_word, second_word in zip(words, words[1:]):
        for c, d in zip(first_word, second_word):
            if c != d: 
                reverse_adj_list[d].append(c)
                break
        else: # Check that second word isn't a prefix of first word.
            if len(second_word) < len(first_word): 
                return ""

    # Step 2: Depth-first search.
    seen = {} # False = grey, True = black.
    output = []
    def visit(node):  # Return True iff there are no cycles.
        if node in seen:
            return seen[node] # If this node was grey (False), a cycle was detected.
        seen[node] = False # Mark node as grey.
        for next_node in reverse_adj_list[node]:
            result = visit(next_node)
            if not result: 
                return False # Cycle was detected lower down.
        seen[node] = True # Mark node as black.
        output.append(node)
        return True

    if not all(visit(node) for node in reverse_adj_list):
        return ""

    return "".join(output)

# V1
# https://blog.csdn.net/qq_37821701/article/details/108807236
# IDEA : Topological sorting (official solution)
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
class Solution:
    def alienOrder(self, words):
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


# V1''''
# https://leetcode.jp/leetcode-269-alien-dictionary-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
# JAVA
# // 用于统计排在每种字母后面的所有字母
# Map<Character, List<Character>> map = new HashMap<>();
# // 拓扑排序用的访问数组
# int[] visited = new int[26];
# // 用于统计words中存在哪些字母
# boolean[] has = new boolean[26];
# public String alienOrder(String[] words) {
#     // 统计words中存在哪些字母
#     for(int i=0;i<words.length;i++){
#         String current=words[i];
#         for(int j=0;j<current.length();j++){
#             has[current.charAt(j)-'a']=true;
#         }
#     }
#     // 相邻2单词比较，统计排在每种字母后面的所有字母
#     for(int i=1;i<words.length;i++){
#         // 前单词
#         String pre = words[i-1];
#         // 当前单词
#         String current=words[i];
#         // 单词下标
#         int index=0;
#         // 比较2单词同一下标
#         while(index<pre.length() && index<current.length()){
#             // 前单词当前字符
#             char p = pre.charAt(index);
#             // 当前单词当前字符
#             char c = current.charAt(index);
#             // 2字符不同
#             if(p!=c){
#                 // 将当前字母放入前字母的后续列表中
#                 List<Character> l=map.getOrDefault(p,new ArrayList<>());
#                 l.add(c);
#                 map.put(p, l);
#                 break;
#             }
#             index++;
#         }
#     }
#     // 返回结果
#     String res="";
#     // 循环dfs每种字符
#     for(int i=0;i<26;i++){
#         // 如果该字母没有出现过，跳过
#         if(!has[i]) continue;
#         // 如果存在非法排序，返回空
#         if(!dfs((char)(i+'a'))) return res;
#     }
#     // 因为拓扑排序是反向遍历，所以将结果倒序打印出来。
#     for(int i=resList.size()-1;i>=0;i--){
#         res+=resList.get(i);
#     }
#     return res;
# }
# List<Character> resList = new ArrayList<>();
# // 拓扑排序（dfs）
# boolean dfs(char c){
#     if(visited[c-'a']==1) return false;
#     if(visited[c-'a']==2) return true;
#     visited[c-'a']=1;
#     List<Character> list = map.get(c);
#     if(list!=null) {
#         for(Character next : list){
#             if(!dfs(next)) return false;
#         }
#     }
#     visited[c-'a']=2;
#     resList.add(c);
#     return true;
# }


# V1
# https://www.jiuzhang.com/solution/alien-dictionary/
# IDEA : Topological sorting
# time = O(n)  # n = total chars; heap over <=26 nodes is O(1) factor
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
class Solution:
    def alienOrder(self, words):
        # Write your code here
        from collections import defaultdict
        from collections import deque
        import heapq
        
        graph = {}

        # initial graph
        for w in words:
            for c in w:
                graph[c] = set()
        
        for i in range(1, len(words)):
            for j in range(min(len(words[i]), len(words[i-1]))):
                if words[i-1][j] != words[i][j]:
                    graph[words[i-1][j]].add(words[i][j])
                    break

        indegree = defaultdict(int)
        for g in graph:
            for ne in graph[g]:
                indegree[ne] += 1

        q = [w for w in graph if indegree[w] == 0]
        heapq.heapify(q)

        order = []
        visited = set()
        while q:
            # n = q.pop()
            n = heapq.heappop(q)

            if n in visited:
                continue
            visited.add(n)
            order.append(n)

            for ne in graph[n]:
                indegree[ne] -= 1
                if indegree[ne] == 0:
                    # q.appendleft(ne)
                    heapq.heappush(q, ne)
        return ''.join(order) if len(order) == len(graph) else ''

# V1'
# https://www.cnblogs.com/lightwindy/p/8531872.html
# IDEA :BFS 
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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
# time = O(n)  # n = total chars across all words
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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
import collections
# BFS solution.
# time = O(n)
# space = O(|V|+|E|) = O(26 + 26^2) = O(1)
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
# time = O(n)  # n = total chars across all words (ancestor DFS search)
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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
# time = O(n)  # n = total chars across all words
# space = O(|V|+|E|) = O(26 + 26^2) = O(1)
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
# time = O(n)  # n = total chars across all words (ancestor DFS search)
# space = O(1)  # graph nodes/edges bounded by alphabet (<=26)
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