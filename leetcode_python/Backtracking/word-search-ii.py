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

# V1
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
        if not board or not words: return []
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
                if board[row][col] not in trie: continue
                self.checkList(board, row, col, "", trie, rList)
        return list(rList)

# V1'

# V2