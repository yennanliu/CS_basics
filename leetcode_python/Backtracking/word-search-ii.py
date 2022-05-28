"""

212. Word Search II
Hard

Given an m x n board of characters and a list of strings words, return all words on the board.

Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once in a word.

 

Example 1:


Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]
Example 2:


Input: board = [["a","b"],["c","d"]], words = ["abcb"]
Output: []
 

Constraints:

m == board.length
n == board[i].length
1 <= m, n <= 12
board[i][j] is a lowercase English letter.
1 <= words.length <= 3 * 104
1 <= words[i].length <= 10
words[i] consists of lowercase English letters.
All the strings of words are unique.

"""

# V0
# IDEA : DFS + trie
# DEMO
# >>> words = ['oath', 'pea', 'eat', 'rain'], trie = {'o': {'a': {'t': {'h': {'#': None}}}}, 'p': {'e': {'a': {'#': None}}}, 'e': {'a': {'t': {'#': None}}}, 'r': {'a': {'i': {'n': {'#': None}}}}}
class Solution(object):
    def checkList(self, board, row, col, word, trie, rList):
        if row<0 or row>=len(board) or col<0 or col>=len(board[0]) or board[row][col] == '.' or board[row][col] not in trie:
            return
        c = board[row][col]
        _word= word + c
        if '#' in trie[c]: 
            rList.add(_word)
            if len(trie[c]) == 1:
                return # if next node is empty, return as no there is no need to search further
        board[row][col] = '.'
        self.checkList(board, row-1, col, _word, trie[c], rList) #up
        self.checkList(board, row+1, col, _word, trie[c], rList) #down
        self.checkList(board, row, col-1, _word, trie[c], rList) #left
        self.checkList(board, row, col+1, _word, trie[c], rList) #right
        board[row][col] = c
    
    def findWords(self, board, words):
        if not board or not words:
            return []
        # building Trie
        trie, rList = {}, set()
        for word in words:
            t = trie
            for c in word:
                if c not in t:
                    t[c] = {}
                t = t[c]
            t['#'] = None
        #print (">>> words = {}, trie = {}".format(words, trie))
        for row in range(len(board)):
            for col in range(len(board[0])):
                if board[row][col] in trie:
                    self.checkList(board, row, col, "", trie, rList)
        return list(rList)

# V1
# IDEA : Backtracking with Trie
# https://leetcode.com/problems/word-search-ii/solution/
class Solution:
    def findWords(self, board, words):
        WORD_KEY = '$'
        
        trie = {}
        for word in words:
            node = trie
            for letter in word:
                # retrieve the next node; If not found, create a empty node.
                node = node.setdefault(letter, {})
            # mark the existence of a word in trie node
            node[WORD_KEY] = word
        
        rowNum = len(board)
        colNum = len(board[0])
        
        matchedWords = []
        
        def backtracking(row, col, parent):    
            
            letter = board[row][col]
            currNode = parent[letter]
            
            # check if we find a match of word
            word_match = currNode.pop(WORD_KEY, False)
            if word_match:
                # also we removed the matched word to avoid duplicates,
                #   as well as avoiding using set() for results.
                matchedWords.append(word_match)
            
            # Before the EXPLORATION, mark the cell as visited 
            board[row][col] = '#'
            
            # Explore the neighbors in 4 directions, i.e. up, right, down, left
            for (rowOffset, colOffset) in [(-1, 0), (0, 1), (1, 0), (0, -1)]:
                newRow, newCol = row + rowOffset, col + colOffset     
                if newRow < 0 or newRow >= rowNum or newCol < 0 or newCol >= colNum:
                    continue
                if not board[newRow][newCol] in currNode:
                    continue
                backtracking(newRow, newCol, currNode)
        
            # End of EXPLORATION, we restore the cell
            board[row][col] = letter
        
            # Optimization: incrementally remove the matched leaf node in Trie.
            if not currNode:
                parent.pop(letter)

        for row in range(rowNum):
            for col in range(colNum):
                # starting from each of the cells
                if board[row][col] in trie:
                    backtracking(row, col, trie)
        
        return matchedWords

# V1
# IDEA : DFS + trie
# https://leetcode.com/problems/word-search-ii/discuss/59808/Python-DFS-362ms
class Solution(object):
    def checkList(self, board, row, col, word, trie, rList):
        if row<0 or row>=len(board) or col<0 or col>=len(board[0]) or board[row][col] == '.' or board[row][col] not in trie: return
        c = board[row][col]
        _word= word + c
        if '#' in trie[c]: 
            rList.add(_word)
            if len(trie[c]) == 1: return # if next node is empty, return as no there is no need to search further
        board[row][col] = '.'
        self.checkList(board, row-1, col, _word, trie[c], rList) #up
        self.checkList(board, row+1, col, _word, trie[c], rList) #down
        self.checkList(board, row, col-1, _word, trie[c], rList) #left
        self.checkList(board, row, col+1, _word, trie[c], rList) #right
        board[row][col] = c
    
    def findWords(self, board, words):
        """
        :type board: List[List[str]]
        :type words: List[str]
        :rtype: List[str]
        """
        if not board or not words:
            return []
        # building Trie
        trie, rList = {}, set()
        for word in words:
            t = trie
            for c in word:
                if c not in t: t[c] = {}
                t = t[c]
            t['#'] = None
        for row in range(len(board)):
            for col in range(len(board[0])):
                if board[row][col] not in trie:
                    continue
                self.checkList(board, row, col, "", trie, rList)
        return list(rList)

# V1
# IDEA : DFS + trie
# https://leetcode.com/problems/word-search-ii/discuss/59905/Python-AC-solution
class TrieNode:
    def __init__(self):
        self.children = collections.defaultdict(TrieNode)
        self.flag = False

class Solution:
    def __init__(self):
        self.root = TrieNode()
        self.result = []

    def insert(self, word):
        node = self.root
        for letter in word:
            node = node.children[letter]
        node.flag = True

    def findWords(self, board, words):
        for w in words:
            self.insert(w)
        for j in range(len(board)):
            for i in range(len(board[0])):
                self.dfs(self.root, board, j, i)
        return self.result

    def dfs(self, node, board, j, i, word=''):
        if node.flag:
            self.result.append(word)
            node.flag = False
        if 0 <= j < len(board) and 0 <= i < len(board[0]):
            char = board[j][i]
            child = node.children.get(char)
            if child is not None:
                word += char
                board[j][i] = None
                self.dfs(child, board, j + 1, i, word)
                self.dfs(child, board, j - 1, i, word)
                self.dfs(child, board, j, i + 1, word)
                self.dfs(child, board, j, i - 1, word)
                board[j][i] = char

# V2