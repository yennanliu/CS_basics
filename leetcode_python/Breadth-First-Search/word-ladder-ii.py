"""

126. Word Ladder II
Hard

A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that:

Every adjacent pair of words differs by a single letter.
Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
sk == endWord
Given two words, beginWord and endWord, and a dictionary wordList, return all the shortest transformation sequences from beginWord to endWord, or an empty list if no such sequence exists. Each sequence should be returned as a list of the words [beginWord, s1, s2, ..., sk].

 

Example 1:

Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
Output: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]
Explanation: There are 2 shortest transformation sequences:
"hit" -> "hot" -> "dot" -> "dog" -> "cog"
"hit" -> "hot" -> "lot" -> "log" -> "cog"
Example 2:

Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
Output: []
Explanation: The endWord "cog" is not in wordList, therefore there is no valid transformation sequence.
 

Constraints:

1 <= beginWord.length <= 5
endWord.length == beginWord.length
1 <= wordList.length <= 1000
wordList[i].length == beginWord.length
beginWord, endWord, and wordList[i] consist of lowercase English letters.
beginWord != endWord
All the words in wordList are unique.

"""

# V0

# V1
# IDEA : BFS + DFS
# https://leetcode.com/problems/word-ladder-ii/discuss/352661/Simple-Python-BFS-solution-(similar-problems-listed)
# IDEA
# Level-by-level BFS visit can be used to solve a lot of problems of finding discrete shortest distance.
# Please see and vote for my solutions for these similar problems
# 102. Binary Tree Level Order Traversal
# 127. Word Ladder
# 126. Word Ladder II
# 301. Remove Invalid Parentheses
# 317. Shortest Distance from All Buildings
# 773. Sliding Puzzle
# 815. Bus Routes
# 854. K-Similar Strings
# 864. Shortest Path to Get All Keys
# 1091. Shortest Path in Binary Matrix
# 1210. Minimum Moves to Reach Target with Rotations
# 1263. Minimum Moves to Move a Box to Their Target Location
# 1293. Shortest Path in a Grid with Obstacles Elimination
class Solution:
    def findLadders(self, beginWord, endWord, wordList):
        wordList = set(wordList)
        if endWord not in wordList:
            return []
        """
        # BFS visit : build parents get relation between cur and next word (for dfs)
        """
        curr_level = {beginWord}
        parents = collections.defaultdict(list)
        while curr_level:
            wordList -= curr_level
            next_level = set()
            for word in curr_level:
                for i in range(len(word)):
                    for c in 'abcdefghijklmnopqrstuvwxyz':
                        new_word = word[:i] + c + word[i+1:]
                        if new_word in wordList:
                            next_level.add(new_word)
                            parents[new_word].append(word)
            if endWord in next_level:
                break
            curr_level = next_level
        print ("parents = " + str(parents))
        """
        # DFS reconstruction : get parents from dfs, and create "path" as res, then return
        """
        res = []
        def dfs(word, path):
            if word == beginWord:
                path.append(word)
                res.append(path[::-1])
            else:
                for p_word in parents[word]:
                    dfs(p_word, path + [word])
        dfs(endWord, [])
        print ("res = " + str(res))
        return res

# V1
# IDEA : BFS
# https://leetcode.com/problems/word-ladder-ii/discuss/254249/Python-BFS
from collections import defaultdict
class Solution:
    def findLadders(self, beginWord, endWord, wordList):
        graph = defaultdict(set)
        for word in wordList + [beginWord]:
            for i in range(len(word)):
                graph[word[:i] + '_' + word[i + 1:]].add(word)
        wordList = set(wordList)
        
        queue = [[beginWord]]
        flag = False
        while queue:
            new_queue = []
            to_remove = set()
            for path in queue:
                for i in range(len(path[-1])):
                    for new_word in graph[path[-1][:i] + '_' + path[-1][i + 1:]] & wordList:
                        new_queue.append(path + [new_word])
                        to_remove.add(new_word)
                        if new_word == endWord: flag = True
            if flag: return [p for p in new_queue if p[-1] == endWord]
            queue = new_queue[:]
            wordList -= to_remove
        return []

# V1'
# IDEA : BFS + DFS
# https://leetcode.com/problems/word-ladder-ii/discuss/241584/Python-solution
# IDEA : 
# Idea: First do a BFS on the word graph. The purpose of the BFS is two-fold. First, we calculates the distance from beginWord to all words in wordList. If endWord is not in the same connected component as beginWord, we return []. We store the result in a dictionary dist. In particular, we know that the distance d from beginWord to endWord (dist[endWord]) is exactly the length of the shortest transformation sequences from beginWord to endWord. Secondly, we can construct the adjacency list representation of the word graph with the BFS, which is a dictionary graph that maps each word to its set of neighbors in the word graph. This facilitates the construction of the shortest transformation sequences using DFS in the next step, because the value corresponding to a particular key will be the set of all the neighbors of the key.
# Next, we do a DFS starting from beginWord. We can use the dictionary dist to prune most of the search spaces, because we already know that each of the shortest transformation sequences is of length dist[endWord] = d, so that the transformation sequence is of the form [beginWord, word1, word2, ..., endWord], where dist[beginWord] = 0, dist[word1] = 1, dist[word2] = 2, ..., dist[endWord] = d. Therefore, we only need to make recursive DFS calls on those neighbors of the current word which are of distance dist[currentWord]+1 to the beginWord. We initialize two lists, res which holds the result, and tmp which holds all the words in the current DFS subtree. Once the DFS call is on endWord, we create a shallow copy of tmp and append it to res, and return.
class Solution(object):
    def findLadders(self, beginWord, endWord, wordList):
        """
        :type beginWord: str
        :type endWord: str
        :type wordList: List[str]
        :rtype: List[List[str]]
        """
        def dfs(word):
            tmp.append(word)
            if word == endWord:
                res.append(list(tmp))
                tmp.pop()
                return 
            if word in graph:
                for nei in graph[word]:
                    if dist[nei] == dist[word]+1:
                        dfs(nei)
            tmp.pop()

        wordSet = set(wordList)
        if endWord not in wordSet:
            return []
        alphabets = 'abcdefghijklmnopqrstuvwxyz'
        q = collections.deque([(beginWord, 0)])
        dist = {}
        graph = collections.defaultdict(set)
        seen = set([beginWord])
        while q:
            u, d = q.popleft()
            dist[u] = d
            for i in range(len(u)):
                for alph in alphabets:
                    if alph != u[i]:
                        new = u[:i]+alph+u[i+1:]
                        if new in wordSet:
                            graph[u].add(new)
                            graph[new].add(u)
                            if new not in seen:
                                q.append((new, d+1))
                                seen.add(new)
        if endWord not in dist:
            return []
        res = []
        tmp = []
        dfs(beginWord)
        return res 

# V1''
# IDEA : BFS backwards from endWord to beginWord
# https://leetcode.com/problems/word-ladder-ii/discuss/241584/Python-solution
class Solution(object):
    def findLadders(self, beginWord, endWord, wordList):
        """
        :type beginWord: str
        :type endWord: str
        :type wordList: List[str]
        :rtype: List[List[str]]
        """ 
        def dfs(word):
            if word == endWord:
                res.append(list(tmp))
                return
            if word in graph:
                for nei in graph[word]:
                    if dist[nei] == dist[word]-1:
                        tmp.append(nei)
                        dfs(nei)
                        tmp.pop()
        
        wordSet = set(wordList)
        if endWord not in wordSet:
            return []
        alphabets = 'abcdefghijklmnopqrstuvwxyz'
        q = collections.deque([(endWord, 0)])
        min_dist = float('inf')
        seen = set([endWord])
        graph = collections.defaultdict(set)
        dist = {}
        while q:
            u, d = q.popleft()
            dist[u] = d
            for i in range(len(u)):
                for alph in alphabets:
                    new = u[:i]+alph+u[i+1:]
                    if new == beginWord:
                        if min_dist > d+1:
                            min_dist = d+1
                        graph[beginWord].add(u)
                    else:                  
                        if new in wordSet:
                            graph[u].add(new)
                            graph[new].add(u)
                            if new not in seen:
                                seen.add(new)
                                q.append((new, d+1))
        
        if min_dist == float('inf'):
            return []
        res = []
        tmp = [beginWord]
        for nei in graph[beginWord]:
            if dist[nei] == min_dist-1:
                tmp.append(nei)
                dfs(nei)
                tmp.pop()
        return res

# V1 
# https://blog.csdn.net/qqxx6661/article/details/78509871
# https://medium.com/@bill800227/leetcode-126-word-ladder-ii-19bc2ff4a6db
# IDEA : BFS 
class Solution(object):
    def findLadders(self, beginWord, endWord, wordList):
        """
        :type beginWord: str
        :type endWord: str
        :type wordList: List[str]
        :rtype: List[List[str]]
        """
        def bfs(front_level, end_level, is_forward, word_set, path_dic):
            if len(front_level) == 0:
                return False
            if len(front_level) > len(end_level):
                return bfs(end_level, front_level, not is_forward, word_set, path_dic)
            for word in (front_level | end_level):
                word_set.discard(word)
            next_level = set()
            done = False
            while front_level:
                word = front_level.pop()
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    for i in range(len(word)):
                        new_word = word[:i] + c + word[i + 1:]
                        if new_word in end_level:
                            done = True
                            add_path(word, new_word, is_forward, path_dic)
                        else:
                            if new_word in word_set:
                                next_level.add(new_word)
                                add_path(word, new_word, is_forward, path_dic)
            return done or bfs(next_level, end_level, is_forward, word_set, path_dic)

        def add_path(word, new_word, is_forward, path_dic):
            if is_forward:
                path_dic[word] = path_dic.get(word, []) + [new_word]
            else:
                path_dic[new_word] = path_dic.get(new_word, []) + [word]

        def construct_path(word, end_word, path_dic, path, paths):
            if word == end_word:
                paths.append(path)
                return
            if word in path_dic:
                for item in path_dic[word]:
                    construct_path(item, end_word, path_dic, path + [item], paths)

        front_level, end_level = {beginWord}, {endWord}
        path_dic = {}
        wordSet = set(wordList)
        if endWord not in wordSet:
            return []
        bfs(front_level, end_level, True, wordSet, path_dic)
        path, paths = [beginWord], []
        # print path_dic
        construct_path(beginWord, endWord, path_dic, path, paths)
        return paths

### Test case : dev 

# V1'
# https://www.cnblogs.com/zuoyuan/p/3697045.html
class Solution:
    # @param start, a string
    # @param end, a string
    # @param dict, a set of string
    # @return a list of lists of string
    def findLadders(self, start, end, dict):
        def buildpath(path, word):
            if len(prevMap[word])==0:
                path.append(word); currPath=path[:]
                currPath.reverse(); result.append(currPath)
                path.pop();
                return
            path.append(word)
            for iter in prevMap[word]:
                buildpath(path, iter)
            path.pop()
        
        result=[]
        prevMap={}
        length=len(start)
        for i in dict:
            prevMap[i]=[]
        candidates=[set(),set()]; current=0; previous=1
        candidates[current].add(start)
        while True:
            current, previous=previous, current
            for i in candidates[previous]: dict.remove(i)
            candidates[current].clear()
            for word in candidates[previous]:
                for i in range(length):
                    part1=word[:i]; part2=word[i+1:]
                    for j in 'abcdefghijklmnopqrstuvwxyz':
                        if word[i]!=j:
                            nextword=part1+j+part2
                            if nextword in dict:
                                prevMap[nextword].append(word)
                                candidates[current].add(nextword)
            if len(candidates[current])==0: return result
            if end in candidates[current]: break
        buildpath([], end)
        return result

# V1''
# https://www.twblogs.net/a/5d17a56abd9eee1ede0563e6
class Solution:
    def findLadders(self, beginWord: str, endWord: str, wordList: List[str]) -> List[List[str]]:
        wordlist=set(wordList)
        res= []
        layer = {}
        layer[beginWord] = [[beginWord]]
        
        while layer:
            newlayer = collections.defaultdict(list)
            for w in layer:
                if w == endWord:
                    res.extend(k for k in layer[w])
                else:
                    for i in range(len(w)):
                        for c in "abcdefghijklmnopqrstuvwxyz":
                            newword=w[:i] + c + w[i+1:]
                            if newword in wordlist:
                                newlayer[newword] += [j+[newword] for j in layer[w]]
            wordlist -=set(newlayer.keys())
            layer = newlayer
            
        return res
# V2