# Trie 
> Whenever we come across questions with multiple strings, it is best to think if Trie can help us.
- https://leetcode.com/problems/search-suggestions-system/solution/

## 0) Concept 
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- tree + dict
    - `put Node into dict` (e.g. defaultdict(Node))

<p><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/trie_1.png" ></p>

<p><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/trie_2.png" ></p>

### 0-1) Types

### 0-2) Pattern
```python
# python

#--------------------------
# V1
#--------------------------

from collections import defaultdict
### NOTE : need to define our Node
class Node():

    def __init__(self):
        """
        NOTE : we use defaultdict(Node) as our trie data structure

        -> use defaultdict
            - key : every character from word
            - value : Node (Node type)

        and link children with parent Node via defaultdict
        """
        self.children = defaultdict(Node)
        self.isword = False

# implement basic methods
class Trie():

    def __init__(self):
        self.root = Node()

    def insert(self, word):
        ### NOTE : we always start from below
        cur = self.root
        for w in word:
            cur = cur.children[w] # same as self.root.defaultdict[w]
        cur.isword = True

    def search(self, word):
        ### NOTE : we always start from below
        cur = self.root
        for w in word:
            cur = cur.children.get(w)
            if not cur:
                return False
        ### NOTE : need to check if isword
        return cur.isword

    def startsWith(self, prefix):
        ### NOTE : we always start from below
        cur = self.root
        for p in prefix:
            cur = cur.children.get(p)
            if not cur:
                return False
        return True
```

```python
#  python
#--------------------------
# V2
#--------------------------
# LC 1268
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.words = []

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.words.append(word)
            node.words.sort()
            while len(node.words) > 3:
                node.words.pop()
    
    def search(self, word):
        res = []
        node = self.root
        for char in word:
            if char not in node.children:
                break
            node = node.children[char]
            res.append(node.words[:])
        l_remain = len(word) - len(res)
        for _ in range(l_remain):
            res.append([])
        return res
```

```java
// java

// Custom class Trie with function to get 3 words starting with given prefix
// LC 1268
// https://leetcode.com/problems/search-suggestions-system/
class Trie {

    // Node definition of a trie
    class Node {
        boolean isWord = false;
        List<Node> children = Arrays.asList(new Node[26]);
    };
    Node Root, curr;
    List<String> resultBuffer;

    // Runs a DFS on trie starting with given prefix and adds all the words in the resultBuffer, limiting result size to 3
    void dfsWithPrefix(Node curr, String word) {
        if (resultBuffer.size() == 3)
            return;
        if (curr.isWord)
            resultBuffer.add(word);

        // Run DFS on all possible paths.
        for (char c = 'a'; c <= 'z'; c++)
            if (curr.children.get(c - 'a') != null)
                dfsWithPrefix(curr.children.get(c - 'a'), word + c);
    }
    Trie() {
        Root = new Node();
    }

    // Inserts the string in trie.
    void insert(String s) {

        // Points curr to the root of trie.
        curr = Root;
        for (char c : s.toCharArray()) {
            if (curr.children.get(c - 'a') == null)
                curr.children.set(c - 'a', new Node());
            curr = curr.children.get(c - 'a');
        }

        // Mark this node as a completed word.
        curr.isWord = true;
    }
    List<String> getWordsStartingWith(String prefix) {
        curr = Root;
        resultBuffer = new ArrayList<String>();
        // Move curr to the end of prefix in its trie representation.
        for (char c : prefix.toCharArray()) {
            if (curr.children.get(c - 'a') == null)
                return resultBuffer;
            curr = curr.children.get(c - 'a');
        }
        dfsWithPrefix(curr, prefix);
        return resultBuffer;
    }
};
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Implement Trie (Prefix Tree)
```python
# 208. Implement Trie (Prefix Tree)
# V0
# IDEA : trie concept :  dict + tree
# https://blog.csdn.net/fuxuemingzhu/article/details/79388432
### NOTE : we need implement Node class
from collections import defaultdict
class Node(object):
    def __init__(self):
        ### NOTE : we use defaultdict as dict
        # TODO : make a default py dict version
        self.children = defaultdict(Node)
        self.isword = False
        
class Trie(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        ### NOTE : we use the Node class we implement above
        self.root = Node()

    def insert(self, word):
        current = self.root
        for w in word:
            current = current.children[w]
        ### NOTE : if insert OP completed, mark isword attr as true
        current.isword = True

    def search(self, word):
        current = self.root
        for w in word:
            current = current.children.get(w)
            if current == None:
                return False
        ### NOTE : we need to check if isword atts is true (check if word terminated here as well)
        return current.isword

    def startsWith(self, prefix):
        current = self.root
        for w in prefix:
            current = current.children.get(w)
            if current == None:
                return False
        ### NOTE : we don't need to check isword here, since it is "startsWith"
        return True
```

### 2-2) Add and Search Word - Data structure design
```python
# LC 211 Add and Search Word - Data structure design
# V0
from collections import defaultdict
class Node(object):
    def __init__(self):
        self.children = defaultdict(Node)
        self.isword = False
        
class WordDictionary(object):

    def __init__(self):
        self.root = Node()

    def addWord(self, word):
        current = self.root
        for w in word:
            _next = current.children[w]
            current = _next
        current.isword = True

    def search(self, word):
        return self.match(word, 0, self.root)
    
    def match(self, word, index, root):
        """
        NOTE : match is a helper func (for search)

          - deal with 2 cases
          -   1) word[index] != '.'
          -   2) word[index] == '.'
        """
        # note the edge cases
        if root == None:
            return False
        if index == len(word):
            return root.isword
        # CASE 1: word[index] != '.'
        if word[index] != '.':
            return root != None and self.match(word, index + 1, root.children.get(word[index]))
        # CASE 2: word[index] == '.'
        else:
            for child in root.children.values():
                if self.match(word, index + 1, child):
                    return True
        return False
```

### 2-3) Search Suggestions System
```python
# LC 1268. Search Suggestions System
# V1
# IDEA : TRIE
# https://leetcode.com/problems/search-suggestions-system/discuss/436183/Python-Trie-Solution
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.words = []

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.words.append(word)
            node.words.sort()
            while len(node.words) > 3:
                node.words.pop()
    
    def search(self, word):
        res = []
        node = self.root
        for char in word:
            if char not in node.children:
                break
            node = node.children[char]
            res.append(node.words[:])
        l_remain = len(word) - len(res)
        for _ in range(l_remain):
            res.append([])
        return res

class Solution:
    def suggestedProducts(self, products: List[str], searchWord: str):
        trie = Trie()
        for prod in products:
            trie.insert(prod)
        return trie.search(searchWord)
```

```java
// IDEA : trie + dfs
// https://leetcode.com/problems/search-suggestions-system/solution/
// Custom class Trie with function to get 3 words starting with given prefix
class Trie {

    // Node definition of a trie
    class Node {
        boolean isWord = false;
        List<Node> children = Arrays.asList(new Node[26]);
    };
    Node Root, curr;
    List<String> resultBuffer;

    // Runs a DFS on trie starting with given prefix and adds all the words in the resultBuffer, limiting result size to 3
    void dfsWithPrefix(Node curr, String word) {
        if (resultBuffer.size() == 3)
            return;
        if (curr.isWord)
            resultBuffer.add(word);

        // Run DFS on all possible paths.
        for (char c = 'a'; c <= 'z'; c++)
            if (curr.children.get(c - 'a') != null)
                dfsWithPrefix(curr.children.get(c - 'a'), word + c);
    }
    Trie() {
        Root = new Node();
    }

    // Inserts the string in trie.
    void insert(String s) {

        // Points curr to the root of trie.
        curr = Root;
        for (char c : s.toCharArray()) {
            if (curr.children.get(c - 'a') == null)
                curr.children.set(c - 'a', new Node());
            curr = curr.children.get(c - 'a');
        }

        // Mark this node as a completed word.
        curr.isWord = true;
    }
    List<String> getWordsStartingWith(String prefix) {
        curr = Root;
        resultBuffer = new ArrayList<String>();
        // Move curr to the end of prefix in its trie representation.
        for (char c : prefix.toCharArray()) {
            if (curr.children.get(c - 'a') == null)
                return resultBuffer;
            curr = curr.children.get(c - 'a');
        }
        dfsWithPrefix(curr, prefix);
        return resultBuffer;
    }
};
class Solution {
    List<List<String>> suggestedProducts(String[] products,
                                         String searchWord) {
        Trie trie = new Trie();
        List<List<String>> result = new ArrayList<>();
        // Add all words to trie.
        for (String w : products)
            trie.insert(w);
        String prefix = new String();
        for (char c : searchWord.toCharArray()) {
            prefix += c;
            result.add(trie.getWordsStartingWith(prefix));
        }
        return result;
    }
};
```

### 2-4) Word Search
```python
# LC 79. Word Search
```

### 2-5) Word Search II
```python
# LC 212. Word Search II

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

# V1'
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
```