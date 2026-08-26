# Trie — Worked Examples

> **Scope** — The worked-solution archive behind [trie.md](./trie.md): five problems, from building the structure to the grid search where a trie turns "once per word" into one walk.
> **See also**: [trie.md](./trie.md) — the parent sheet: the nine templates, the advanced variants and the interview notes; [string.md](./string.md) — string algorithms that do not need a trie; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — the single-pattern alternative; [dfs.md](./dfs.md) — the grid walk inside LC 79 and LC 212; [backtrack.md](./backtrack.md) — the un-choose step that makes LC 212 correct.

## LeetCode Problem Lists

- [Trie](https://leetcode.com/problem-list/trie/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

This is the long tail of [trie.md](./trie.md), which was 67% example tail. The parent keeps the
nine templates and the variants; this file keeps the problems that *apply* them.

### Key Properties
- **Complexity**: O(L) per insert or query in the word length, independent of how many words are stored — which is the entire reason to build one
- **Core Idea**: a trie turns "check this prefix against every word" into "walk one path", so it pays off exactly when the same prefix is asked about repeatedly
- **When to Use**: after the parent's template table has told you which walk the problem needs


## Building the Structure

### 1) Implement Trie (Prefix Tree) — LC 208 ⭐⭐⭐⭐⭐

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

```java
// java
// LC 208
// V1
// https://leetcode.com/problems/implement-trie-prefix-tree/editorial/
class TrieNode {

    // R links to node children
    private TrieNode[] links;

    private final int R = 26;

    private boolean isEnd;

    public TrieNode() {
        links = new TrieNode[R];
    }

    public boolean containsKey(char ch) {
        return links[ch -'a'] != null;
    }
    public TrieNode get(char ch) {
        return links[ch -'a'];
    }
    public void put(char ch, TrieNode node) {
        links[ch -'a'] = node;
    }
    public void setEnd() {
        isEnd = true;
    }
    public boolean isEnd() {
        return isEnd;
    }
}

class Trie2 {
    private TrieNode root;

    public Trie2() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new TrieNode());
            }
            node = node.get(currentChar);
        }
        node.setEnd();
    }


    // search a prefix or whole key in trie and
    // returns the node where search ends
    private TrieNode searchPrefix(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char curLetter = word.charAt(i);
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
            } else {
                return null;
            }
        }
        return node;
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }

}
```

### 2) Add and Search Word — LC 211 — wildcard walk ⭐⭐⭐⭐


**Key Idea**: plain trie for `addWord`, then a `helper(word, idx, node)` recursion for `search` that
splits on `ch == "."` (try every child) vs `ch != "."` (walk the single matching child).

```python
# python
# LC 211 Add and Search Word - Data structure design
# V0
# IDEA: TRIE + recursion
# time = O(m) add / O(26^m) search worst  # m = word length
# space = O(N)  # N = total chars stored in trie
class myNode(object):
    def __init__(self):
        self.child = {}
        self.is_end = False


class WordDictionary(object):

    def __init__(self):
        self.node = myNode()

    def addWord(self, word):
        node = self.node

        for ch in word:
            if ch not in node.child:
                node.child[ch] = myNode()
            node = node.child[ch]

        node.is_end = True

    def search(self, word):
        """
        NOTE !!!

        we simply call the helper func
        """
        return self.helper(word, 0, self.node)

    def helper(self, word, idx, node):
        if idx == len(word):
            return node.is_end

        ch = word[idx]

        """
        NOTE !!

        1. 2 cases
            - ch == "."
            - ch != "."

        2. do `ch == "."` first,
           via recursion way

        3. then do `ch != "."` case,
            - if ch NOT in child, return False directly
            - still do `recursion` call in the final stage
        """

        if ch == ".":
            for next_node in node.child.values():
                if self.helper(word, idx + 1, next_node):
                    return True
            return False

        if ch not in node.child:
            return False

        return self.helper(word, idx + 1, node.child[ch])
```

**Second idiom — `defaultdict(Node)` instead of an explicit dict.** Same trie, same recursion;
the difference is that `children` auto-creates a child on access, so `addWord` has no
`if ch not in ...` branch and the wildcard walk can hand `node.children[ch]` straight to the
recursion. Worth seeing because it is the form most Python solutions on the site use, and
because the auto-creation is also its trap: a *lookup* on a missing key silently inserts one,
so `search` must never index `children` outside a guarded branch.

```python
# python
# LC 211
# V0-1
# IDEA: TRIE + defaultdict (helper handles `node == None`)
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
            # NOTE : defaultdict auto-creates the child node
            current = current.children[w]
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
            # NOTE : use children.get() -> may return None, handled by the check above
            return self.match(word, index + 1, root.children.get(word[index]))
        # CASE 2: word[index] == '.'
        for child in root.children.values():
            if self.match(word, index + 1, child):
                return True
        return False
```

```java
// java
// LC 211

// V1
// IDEA : TRIE
// https://leetcode.com/problems/design-add-and-search-words-data-structure/editorial/
class TrieNode {
    Map<Character, TrieNode> children = new HashMap();
    boolean word = false;
    public TrieNode() {}
}

class WordDictionary2 {
    TrieNode trie;

    /** Initialize your data structure here. */
    public WordDictionary2() {
        trie = new TrieNode();
    }

    /** Adds a word into the data structure. */
    public void addWord(String word) {
        TrieNode node = trie;

        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.word = true;
    }

    /** Returns if the word is in the node. */
    public boolean searchInNode(String word, TrieNode node) {
        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);
            if (!node.children.containsKey(ch)) {
                // if the current character is '.'
                // check all possible nodes at this level
                if (ch == '.') {
                    for (char x : node.children.keySet()) {
                        TrieNode child = node.children.get(x);
                        /** NOTE !!!
                         *  -> if ".", we HAVE to go through all nodes in next levels
                         *  -> and check if any of them is valid
                         *  -> so we need to RECURSIVELY call searchInNode method with "i+1" sub string
                         */
                        if (searchInNode(word.substring(i + 1), child)) {
                            return true;
                        }
                    }
                }
                // if no nodes lead to answer
                // or the current character != '.'
                return false;
            } else {
                // if the character is found
                // go down to the next level in trie
                node = node.children.get(ch);
            }
        }
        return node.word;
    }

    /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
    public boolean search(String word) {
        return searchInNode(word, trie);
    }
}
```

## Searching With It

### 3) Search Suggestions System — LC 1268 ⭐⭐⭐⭐

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

### 4) Word Search — LC 79 — grid DFS, no trie yet

```python
# LC 79. Word Search
# V0
# IDEA : DFS + backtracking
class Solution(object):
 
    def exist(self, board, word):
        ### NOTE : construct the visited matrix
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        """
        NOTE !!!! : we visit every element in board and trigger the dfs
        """
        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.dfs(board, word, 0, i, j, visited):
                    return True

        return False

    def dfs(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        ### NOTE this condition
        # 1) if idx out of range
        # 2) if already visited
        # 3) if board[i][j] != word[cur] -> not possible to be as same as word
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # NOTE THIS !! : mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.dfs(board, word, cur + 1, i + 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i - 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i, j + 1, visited) or\
                 self.dfs(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
```
```java
// java
// LC 79 - Word Search
// IDEA: DFS + backtracking. Mark the cell in place with a sentinel instead of a visited[][]
//       -- one fewer allocation, and the undo is a single assignment.
// time = O(m * n * 4^L), space = O(L)   L = word.length()
public boolean exist(char[][] board, String word) {
    int m = board.length, n = board[0].length;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (dfs(board, word, 0, i, j)) return true;
        }
    }
    return false;
}

private boolean dfs(char[][] board, String word, int cur, int i, int j) {
    /** NOTE !!! the success test comes FIRST -- otherwise a word ending on the last
     *  cell is rejected by the bounds check on the following call. */
    if (cur == word.length()) return true;
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return false;
    if (board[i][j] != word.charAt(cur)) return false;

    char c = board[i][j];
    board[i][j] = '#';                       // mark visited in place
    boolean found = dfs(board, word, cur + 1, i + 1, j)
                 || dfs(board, word, cur + 1, i - 1, j)
                 || dfs(board, word, cur + 1, i, j + 1)
                 || dfs(board, word, cur + 1, i, j - 1);
    board[i][j] = c;                         // un-choose

    return found;
}
```

### 5) Word Search II — LC 212 — the trie is what makes it tractable ⭐⭐⭐⭐

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
```

```java
// java
// LC 212 - Word Search II
// IDEA: one trie over ALL words, then ONE grid walk instead of a separate LC 79 run per word.
//       The node stores the whole word at its terminal, so a hit needs no string building.
// time = O(m * n * 4^L), space = O(total chars in words)   L = longest word
class TrieNode {
    TrieNode[] next = new TrieNode[26];
    String word;                             // non-null only at a word end
}

public List<String> findWords(char[][] board, String[] words) {
    TrieNode root = new TrieNode();
    for (String w : words) {
        TrieNode node = root;
        for (char c : w.toCharArray()) {
            int k = c - 'a';
            if (node.next[k] == null) node.next[k] = new TrieNode();
            node = node.next[k];
        }
        node.word = w;
    }

    List<String> res = new ArrayList<>();
    for (int i = 0; i < board.length; i++)
        for (int j = 0; j < board[0].length; j++)
            dfs(board, i, j, root, res);
    return res;
}

private void dfs(char[][] board, int i, int j, TrieNode node, List<String> res) {
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return;
    char c = board[i][j];
    if (c == '#' || node.next[c - 'a'] == null) return;

    node = node.next[c - 'a'];
    if (node.word != null) {
        res.add(node.word);
        /** NOTE !!! null it out rather than de-duplicating later -- the same word can be
         *  reached by several paths, and this also prunes the branch that just matched. */
        node.word = null;
    }

    board[i][j] = '#';
    dfs(board, i + 1, j, node, res);
    dfs(board, i - 1, j, node, res);
    dfs(board, i, j + 1, node, res);
    dfs(board, i, j - 1, node, res);
    board[i][j] = c;                         // un-choose
}
```

```python

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
            if letter not in parent:
                return
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

---
