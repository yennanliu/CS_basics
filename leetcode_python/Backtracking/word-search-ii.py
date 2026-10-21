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
class Solution(object):
    def findWords(self, board, words):
        """
        :type board: List[List[str]]
        :type words: List[str]
        :rtype: List[str]
        """
        pass



# V0-1
# IDEA : DFS + trie  + BACKTRACK (GPT)
class MyNode(object):
    def __init__(self):
        self.child = {}
        self.is_end = False

        """
        # NOTE !!!
        
            setup an extra attr: `word`
                -> so we can append `cur word` from cur node to res
        """
        self.word = None


class Solution(object):

    def __init__(self):
        self.node = MyNode()
        self.res = []
        self.board = None
        self.rows = 0
        self.cols = 0

    def findWords(self, board, words):
        """
        :type board: List[List[str]]
        :type words: List[str]
        :rtype: List[str]
        """
        if not board or not board[0] or not words:
            return []

        self.node = MyNode()
        self.res = []
        self.board = board
        self.rows = len(board)
        self.cols = len(board[0])

        # Build Trie
        for word in words:
            self.add_word(word)

        """
        NOTE !!!


        still loop over every cell,
        for building the word (with the support from trie)

        """
        # DFS from every cell
        for r in range(self.rows):
            for c in range(self.cols):

                # NOTE !!! use `dfs` helper func
                self.dfs(r, c, self.node)

        return self.res

    def add_word(self, word):
        node = self.node

        for ch in word:
            if ch not in node.child:
                node.child[ch] = MyNode()
            node = node.child[ch]

        node.is_end = True
        node.word = word

    def dfs(self, r, c, node):
        ch = self.board[r][c]


        """
        NOTE !!!

        edge case:

            -> if ch NOT in trie
            -> quit early

            -> the `trie` property.

        """
        # Trie pruning: no word has this prefix
        if ch not in node.child:
            return

        """
        NOTE !!!

        get `next_node` via use `ch` as key from our trie
        """
        next_node = node.child[ch]

        # Found a complete word
        if next_node.is_end:
            self.res.append(next_node.word)


            """
            NOTE !!!

            mark `is_end` to avoid duplicated append
                
            """
            # Avoid adding the same word more than once
            next_node.is_end = False

        # Mark current cell as visited
        self.board[r][c] = "#"

        # Explore four directions
        for dr, dc in ((0, 1), (0, -1), (1, 0), (-1, 0)):
            nr = r + dr
            nc = c + dc

            if (0 <= nr < self.rows and
                    0 <= nc < self.cols and
                    self.board[nr][nc] != "#"):
                self.dfs(nr, nc, next_node)

        """
        NOTE !!!

        undo (Backtrack)

        -> undo `self.board[r][c] = "#"`
            
        """
        # Backtrack: restore original character
        self.board[r][c] = ch


# V0-2
# IDEA : DFS + trie + BACKTRACK (GEMINI)
class TrieNode(object):

  def __init__(self):
    self.child = {}
    # 直接在節點上儲存完整的單字，當到達結尾時可以秒速取得，不需回溯重建字串
    self.word = None


class Solution(object):

  def findWords(self, board, words):
    """:type board: List[List[str]]

    :type words: List[str]
    :rtype: List[str]
    """
    # 1. 建立 Trie 前綴樹
    root = TrieNode()
    for word in words:
      node = root
      for char in word:
        if char not in node.child:
          node.child[char] = TrieNode()
        node = node.child[char]
      node.word = word

    rows = len(board)
    cols = len(board[0])
    res = set()

    # 2. 定義 DFS (Backtracking) 函式
    def dfs(r, c, node):
      char = board[r][c]

      # 若當前字元不在 Trie 的子節點中，直接返回（剪枝）
      if char not in node.child:
        return

      next_node = node.child[char]

      # 若找到一個完整的單字
      if next_node.word:
        res.add(next_node.word)
        # 找到後將其設為 None，避免重複加入同一單字
        next_node.word = None

      # 將當前格子暫時標記為已訪問，防止原地打轉
      board[r][c] = "#"

      # 四個方向探索
      for dr, dc in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
        nr, nc = r + dr, c + dc
        if 0 <= nr < rows and 0 <= nc < cols and board[nr][nc] != "#":
          dfs(nr, nc, next_node)

      # 恢復當前格子的字元（回溯）
      board[r][c] = char

      # 優化剪枝 (Pruning)：若子節點已經空了，從 Trie 中刪除它，加速後續搜尋
      if not next_node.child and not next_node.word:
        del node.child[char]

    # 3. 從網格中的每一個格子出發進行 DFS
    for r in range(rows):
      for c in range(cols):
        if board[r][c] in root.child:
          dfs(r, c, root)

    return list(res)


# V0-3
# IDEA : DFS + trie
# DEMO
# >>> words = ['oath', 'pea', 'eat', 'rain'], trie = {'o': {'a': {'t': {'h': {'#': None}}}}, 'p': {'e': {'a': {'#': None}}}, 'e': {'a': {'t': {'#': None}}}, 'r': {'a': {'i': {'n': {'#': None}}}}}
# time = O(m*n*4^L), L = max word length
# space = O(N*L), N = num words (trie)
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
        # https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md
        trie = {}
        rList = set()
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
# time = O(m*n*4^L), L = max word length
# space = O(N*L), N = num words (trie)
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

# V1'
# IDEA : DFS + trie
# https://leetcode.com/problems/word-search-ii/discuss/59808/Python-DFS-362ms
# time = O(m*n*4^L), L = max word length
# space = O(N*L), N = num words (trie)
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

# V1''
# IDEA : DFS + trie
# https://leetcode.com/problems/word-search-ii/discuss/59905/Python-AC-solution
# time = O(m*n*4^L), L = max word length
# space = O(N*L), N = num words (trie)
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