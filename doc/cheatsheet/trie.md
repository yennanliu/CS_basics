# Trie 

> **Scope** — The prefix tree — insert/search/startsWith, node layout choices, and the problems where sharing prefixes is the whole trick (autocomplete, word search, XOR trie).
> **See also**: [string.md](./string.md) — string handling without a trie; [hash_map.md](./hash_map.md) — when a set of whole words is enough; [dfs.md](./dfs.md) — the traversal trie problems run on top; [advanced_string_algorithms.md](./advanced_string_algorithms.md) — suffix-based alternatives.

> Whenever we come across questions with multiple strings, it is best to think if Trie can help us.
- https://leetcode.com/problems/search-suggestions-system/solution/

## LeetCode Problem Lists

- [Trie](https://leetcode.com/problem-list/trie/)
- [String](https://leetcode.com/problem-list/string/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Trie           | O(L)     | O(L)     | O(L)     | O(L)     |

> **L = length of the key (word)** — complexities are independent of the number of stored keys. Min/Max = lexicographically smallest / largest key.

## 0) Concept 
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- tree + dict
    - `put Node into dict` (e.g. defaultdict(Node))

<p align="center"><img src="../pic/trie_1.png"></p>

<p align="center"><img src="../pic/trie_2.png"></p>

### 0-1) Types

- **HashMap-based trie** — `children` is a dict/`Map`; flexible alphabet (see Pattern below).
- **Array-based trie** — `children` is a fixed `[None] * 26` / `TrieNode[26]`; fastest for lowercase-only (Template 2).
- **Wildcard trie** — supports `.` matching via DFS over all children (Template 3, LC 211).
- **Binary (XOR) trie** — children are bits `0/1`; used for max-XOR / bitwise problems (Template 5, LC 421).

### 0-2) Pattern

```python
class TrieNode:
    def __init__(self):
        self.children = {}  # HashMap for flexible alphabet
        self.is_end = False
        self.word = None    # Store complete word (optional)

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        """Insert word into trie. Time: O(m), Space: O(m)"""
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
        node.word = word  # Store for easy retrieval
    
    def search(self, word: str) -> bool:
        """Search for exact word. Time: O(m)"""
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end
    
    def startsWith(self, prefix: str) -> bool:
        """Check if any word starts with prefix. Time: O(p)"""
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

```java
// Java version
class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEnd;
    String word;
    
    public TrieNode() {
        children = new HashMap<>();
        isEnd = false;
        word = null;
    }
}

class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
        node.word = word;
    }
    
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
            if (node == null) return false;  // null-check guard
        }
        return node.isEnd;
    }
    
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
            if (node == null) return false;  // null-check guard
        }
        return true;
    }
}
```

### Template 2: Trie with Array (Fixed Alphabet)
```python
class TrieNode:
    def __init__(self):
        self.children = [None] * 26  # For lowercase letters only
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            idx = ord(char) - ord('a')
            if not node.children[idx]:
                node.children[idx] = TrieNode()
            node = node.children[idx]
        node.is_end = True
    
    def search(self, word: str) -> bool:
        node = self._search_prefix(word)
        return node is not None and node.is_end
    
    def startsWith(self, prefix: str) -> bool:
        return self._search_prefix(prefix) is not None
    
    def _search_prefix(self, prefix: str) -> TrieNode:
        node = self.root
        for char in prefix:
            idx = ord(char) - ord('a')
            if not node.children[idx]:
                return None
            node = node.children[idx]
        return node
```

```java
// Java version
class TrieNode {
    TrieNode[] children;
    boolean isEnd;
    
    public TrieNode() {
        children = new TrieNode[26];
        isEnd = false;
    }
}
```

### Template 3: Trie with Wildcard Support — LC 211
```python
class WildcardTrie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word: str) -> bool:
        """Search with '.' as wildcard for any character"""
        return self._dfs_search(word, 0, self.root)
    
    def _dfs_search(self, word: str, index: int, node: TrieNode) -> bool:
        if index == len(word):
            return node.is_end
        
        char = word[index]
        if char == '.':
            # Try all possible children
            if not node.children:
                return False
            for child in node.children.values():
                if self._dfs_search(word, index + 1, child):
                    return True
            return False
        else:
            if char not in node.children:
                return False
            return self._dfs_search(word, index + 1, node.children[char])
```

```java
// Java version
public boolean search(String word) {
    return dfsSearch(word, 0, root);
}

private boolean dfsSearch(String word, int index, TrieNode node) {
    if (index == word.length()) {
        return node.isEnd;
    }
    
    char c = word.charAt(index);
    if (c == '.') {
        for (TrieNode child : node.children.values()) {
            if (dfsSearch(word, index + 1, child)) {
                return true;
            }
        }
        return false;
    } else {
        if (!node.children.containsKey(c)) {
            return false;
        }
        return dfsSearch(word, index + 1, node.children.get(c));
    }
}
```

### Template 4: Autocomplete Trie — LC 1268
```python
class AutocompleteTrie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
        node.word = word
    
    def search_prefix(self, prefix: str, limit: int = 3) -> List[str]:
        """Return up to 'limit' words with given prefix"""
        node = self.root
        
        # Navigate to prefix end
        for char in prefix:
            if char not in node.children:
                return []
            node = node.children[char]
        
        # Collect all words with this prefix
        results = []
        self._dfs_collect(node, results, limit)
        return results
    
    def _dfs_collect(self, node: TrieNode, results: List[str], limit: int):
        if len(results) >= limit:
            return
        
        if node.is_end:
            results.append(node.word)
        
        # Traverse in lexicographical order
        for char in sorted(node.children.keys()):
            self._dfs_collect(node.children[char], results, limit)
```

```java
// Java version with priority queue for top suggestions
class AutocompleteTrie {
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();  // Word -> frequency
    }
    
    public List<String> getTopSuggestions(String prefix, int k) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }
        
        // Use heap to get top k
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
            (a, b) -> b.getValue() - a.getValue()
        );
        pq.addAll(node.counts.entrySet());
        
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty() && result.size() < k) {
            result.add(pq.poll().getKey());
        }
        return result;
    }
}
```

### Template 5: Binary Trie (XOR Problems) — LC 421 ⭐⭐⭐
```python
class BinaryTrie:
    class Node:
        def __init__(self):
            self.children = [None, None]  # 0 and 1
            self.count = 0
    
    def __init__(self):
        self.root = self.Node()
    
    def insert(self, num: int) -> None:
        """Insert number as 32-bit binary"""
        node = self.root
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if not node.children[bit]:
                node.children[bit] = self.Node()
            node = node.children[bit]
            node.count += 1
    
    def find_max_xor(self, num: int) -> int:
        """Find maximum XOR with num"""
        node = self.root
        max_xor = 0
        
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            # Try to go opposite direction for max XOR
            toggled = 1 - bit
            
            if node.children[toggled] and node.children[toggled].count > 0:
                max_xor |= (1 << i)
                node = node.children[toggled]
            else:
                node = node.children[bit]
        
        return max_xor
    
    def remove(self, num: int) -> None:
        """Remove number from trie"""
        node = self.root
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            node = node.children[bit]
            node.count -= 1
```

```java
// Java version
class BinaryTrie {
    class Node {
        Node[] children = new Node[2];
        int count = 0;
    }
    
    private Node root = new Node();
    
    public void insert(int num) {
        Node node = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new Node();
            }
            node = node.children[bit];
            node.count++;
        }
    }
    
    public int findMaxXor(int num) {
        Node node = root;
        int maxXor = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int toggled = 1 - bit;
            
            if (node.children[toggled] != null && node.children[toggled].count > 0) {
                maxXor |= (1 << i);
                node = node.children[toggled];
            } else {
                node = node.children[bit];
            }
        }
        
        return maxXor;
    }
}
```

### Template 6: Trie with Delete Operation

**Delete algorithm — 3-step recursive logic:**
1. Navigate to the end of the word; if the word doesn't exist, return `False`.
2. Unmark `is_end` at the terminal node.
3. During backtracking, remove any child node that is now both a non-terminal leaf (no children, not end of another word) — this cleans up dangling nodes.

**Key invariant**: only delete a node if it has no remaining children AND is not the end of a different word. Shared prefixes must be preserved.

```text
Example: trie contains "apple" and "app"

delete("apple"):
  Unmark 'e'.is_end  →  'e' has no children, not is_end → delete 'e'
                         'l' now has no children, not is_end → delete 'l'
                         second 'p' now has no children BUT is_end ("app" ends here) → STOP

  Result: "app" is still intact.
```

```python
# python — complete Trie with delete
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word: str) -> None:
        node = self.root
        for ch in word:
            if ch not in node.children:
                node.children[ch] = TrieNode()
            node = node.children[ch]
        node.is_end = True

    def search(self, word: str) -> bool:
        node = self.root
        for ch in word:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return node.is_end

    def startsWith(self, prefix: str) -> bool:
        node = self.root
        for ch in prefix:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return True

    def delete(self, word: str) -> bool:
        """
        Delete word from trie. Returns True if word existed and was deleted.
        Cleans up leaf nodes that are no longer needed.
        """
        def _delete(node: TrieNode, word: str, depth: int) -> bool:
            if depth == len(word):
                if not node.is_end:
                    return False          # word not in trie
                node.is_end = False
                # This node can be removed if it has no children
                return len(node.children) == 0

            ch = word[depth]
            if ch not in node.children:
                return False              # word not in trie

            should_delete_child = _delete(node.children[ch], word, depth + 1)

            if should_delete_child:
                del node.children[ch]
                # Propagate deletion upward only if this node is also a bare leaf
                return len(node.children) == 0 and not node.is_end

            return False

        return _delete(self.root, word, 0)
```

```java
// java — complete Trie with delete
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class Trie {
    private TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) return false;
            node = node.children.get(c);
        }
        return node.isEnd;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return false;
            node = node.children.get(c);
        }
        return true;
    }

    public boolean delete(String word) {
        return _delete(root, word, 0);
    }

    // Returns true if the current node can be safely removed by its parent.
    private boolean _delete(TrieNode node, String word, int depth) {
        if (depth == word.length()) {
            if (!node.isEnd) return false;   // word not in trie
            node.isEnd = false;
            // Safe to delete this node if it has no children
            return node.children.isEmpty();
        }

        char ch = word.charAt(depth);
        TrieNode child = node.children.get(ch);
        if (child == null) return false;     // word not in trie

        boolean shouldDeleteChild = _delete(child, word, depth + 1);

        if (shouldDeleteChild) {
            node.children.remove(ch);
            // Propagate upward only if this node is also a bare leaf
            return node.children.isEmpty() && !node.isEnd;
        }

        return false;
    }
}
```

**Trace — `delete("apple")` when trie also contains `"app"`:**
```text
depth=0  ch='a'  → recurse
depth=1  ch='p'  → recurse
depth=2  ch='p'  → recurse
depth=3  ch='l'  → recurse
depth=4  ch='e'  → recurse
depth=5  (end)   isEnd=true → set isEnd=false, children={} → return true (delete 'e')
depth=4  remove 'e', children={}, isEnd=false             → return true (delete 'l')
depth=3  remove 'l', children={}, isEnd=false             → return true (delete 2nd 'p')
depth=2  remove 2nd 'p', children={}, BUT isEnd=true ("app" ends here) → return false ✓
depth=1,0  no deletion propagated upward

Result: "app" intact, "apple" gone ✓
```

## 1) General form

### 1-1) Basic OP

See the `insert` / `search` / `startsWith` skeletons in [0-2) Pattern](#0-2-pattern) and the
variant templates above (array-based, wildcard, autocomplete, binary/XOR, delete).

## 2) LC Example

### 2-1) Implement Trie (Prefix Tree) — LC 208 ⭐⭐⭐⭐⭐
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

### 2-2) Add and Search Word - Data structure design — LC 211 ⭐⭐⭐⭐

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

### 2-3) Search Suggestions System — LC 1268 ⭐⭐⭐⭐
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

### 2-4) Word Search — LC 79
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

### 2-5) Word Search II — LC 212 ⭐⭐⭐⭐
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

## Advanced Trie Variants — XOR Trie, Stream Matching, Delete

### XOR Trie (Binary Trie) — LC 421 Maximum XOR
Use a binary trie (bits 0/1 as children) to find the maximum XOR between any two numbers.

```python
class XORTrie:
    def __init__(self):
        self.root = {}

    def insert(self, num):
        node = self.root
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            node = node.setdefault(bit, {})

    def max_xor(self, num):
        node = self.root
        xor = 0
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            want = 1 - bit          # prefer the opposite bit
            if want in node:
                xor |= (1 << i)
                node = node[want]
            else:
                node = node[bit]
        return xor

# LC 421
def findMaximumXOR(nums):
    trie = XORTrie()
    for n in nums:
        trie.insert(n)
    return max(trie.max_xor(n) for n in nums)
```

### Trie + DP (Stream Matching) — LC 1032 Stream of Characters
Combine a trie with a state machine to match words in a character stream in O(1) per query.

```python
class StreamChecker:
    def __init__(self, words):
        self.trie = {}
        self.stream = []
        # Insert reversed words — query from end of stream
        for w in words:
            node = self.trie
            for c in reversed(w):
                node = node.setdefault(c, {})
            node['#'] = True

    def query(self, letter: str) -> bool:
        self.stream.append(letter)
        node = self.trie
        # Walk stream backwards through trie
        for c in reversed(self.stream):
            if c not in node:
                return False
            node = node[c]
            if '#' in node:
                return True
        return False
```

### Trie Delete (Clean Leaf Removal)

```python
def delete(self, word: str) -> bool:
    def _delete(node, word, depth):
        if depth == len(word):
            if not node.is_end:
                return False
            node.is_end = False
            return len(node.children) == 0  # safe to delete if leaf
        ch = word[depth]
        if ch not in node.children:
            return False
        should_delete = _delete(node.children[ch], word, depth + 1)
        if should_delete:
            del node.children[ch]
            return len(node.children) == 0 and not node.is_end
        return False
    return _delete(self.root, word, 0)
```

### Prefix-Suffix Trie — LC 745
For problems requiring both prefix and suffix matching, wrap each word as `suffix#word` and insert into one trie.

```python
# For word "apple", insert: "apple#apple", "pple#apple", "ple#apple", "le#apple", "e#apple"
def buildIndex(words):
    trie = {}
    for weight, word in enumerate(words):
        for i in range(len(word)):
            key = word[i:] + '#' + word
            node = trie
            for c in key:
                node = node.setdefault(c, {})
            node['weight'] = weight  # store latest (highest) weight
    return trie
```

### Interview tips — trie
| Signal | Pattern |
|--------|---------|
| "prefix matching", "autocomplete" | Standard trie |
| "maximum XOR", "bitwise optimization" | Binary XOR trie |
| "stream of characters", "real-time matching" | Reversed-word trie + state |
| "both prefix AND suffix" | Suffix#word trie |
| "wildcard `.` matching" | DFS at `.` nodes |
| "count words with prefix" | Add `count` field to TrieNode |

---

## 3) More Trie Templates

Patterns not covered by Templates 1-6. Each one is a different way of *walking* the trie —
the trie itself barely changes.

| Signal in the problem | Template | Worked example |
|-----------------------|----------|----------------|
| "split a string into dictionary words" | Template 7 — Trie + DP | LC 139, LC 472 |
| "concatenate two words into a palindrome" | Template 8 — Reversed-word trie | LC 336 |
| "lexicographic / dictionary order of numbers" | Template 9 — Implicit 10-ary digit trie | LC 386 |
| "replace a word by its shortest root/prefix" | Template 1 variation — stop at first `is_end` | LC 648 |

### Template 7: Trie + DP (Word Segmentation) — LC 139 ⭐⭐⭐⭐⭐

**Key Idea**: `dp[i] = "s[0..i) can be segmented"`. From every reachable index `i`, walk the trie
forward one char at a time; every time you land on an `is_end` node at position `j`, set `dp[j+1] = True`.
The trie replaces the inner "try every dictionary word" loop — you break out the moment the
prefix leaves the trie, so no hashing of substrings is needed.

```python
# python
# LC 139 - Word Break
# IDEA: trie of wordDict + dp over s; from each reachable index walk the trie forward
# time = O(n^2 + M), space = O(n + M)   # n = len(s), M = total chars in wordDict
class TrieNode(object):
    def __init__(self):
        self.children = {}
        self.is_end = False

class Solution(object):
    def wordBreak(self, s, wordDict):
        root = TrieNode()
        for w in wordDict:
            node = root
            for ch in w:
                if ch not in node.children:
                    node.children[ch] = TrieNode()
                node = node.children[ch]
            node.is_end = True

        n = len(s)
        dp = [False] * (n + 1)
        dp[0] = True   # empty prefix is always segmentable

        for i in range(n):
            if not dp[i]:
                continue          # unreachable start -> skip
            node = root
            for j in range(i, n):
                ch = s[j]
                ### NOTE : the moment s[i..j] leaves the trie, no longer prefix can match
                if ch not in node.children:
                    break
                node = node.children[ch]
                if node.is_end:
                    dp[j + 1] = True
        return dp[n]
```

```java
// java
// LC 139 - Word Break
// IDEA: trie of wordDict + dp[i] = "s[0..i) is segmentable"; walk trie from every reachable i
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        // time = O(n^2 + M), space = O(n + M)  // n = s.length(), M = total chars in wordDict
        TrieNode root = new TrieNode();
        for (String w : wordDict) {
            TrieNode node = root;
            for (char c : w.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isEnd = true;
        }

        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;   // empty prefix is always segmentable

        for (int i = 0; i < n; i++) {
            if (!dp[i]) continue;          // unreachable start -> skip
            TrieNode node = root;
            for (int j = i; j < n; j++) {
                node = node.children.get(s.charAt(j));
                if (node == null) break;   // prefix left the trie
                if (node.isEnd) dp[j + 1] = true;
            }
        }
        return dp[n];
    }
}
```

**Variation — LC 472 Concatenated Words**: same trie + DP, but the answer needs **at least 2 pieces**
and a word must not be built from itself. Twist: **sort words by length and insert lazily** — when
testing `w`, the trie holds only *strictly shorter* words, so any successful segmentation
automatically uses ≥ 2 of them.

```python
# python
# LC 472 - Concatenated Words
# IDEA: LC 139's trie+dp, but test each word against a trie of only the STRICTLY SHORTER words
# time = O(N * L^2), space = O(total chars)   # N = #words, L = max word length
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        root = TrieNode()

        def can_form(w):
            n = len(w)
            dp = [False] * (n + 1)
            dp[0] = True
            for i in range(n):
                if not dp[i]:
                    continue
                node = root
                for j in range(i, n):
                    if w[j] not in node.children:
                        break
                    node = node.children[w[j]]
                    if node.is_end:
                        dp[j + 1] = True
            return dp[n]

        def insert(w):
            node = root
            for ch in w:
                if ch not in node.children:
                    node.children[ch] = TrieNode()
                node = node.children[ch]
            node.is_end = True

        res = []
        ### NOTE : sort by length -> trie only ever holds shorter words -> >= 2 pieces guaranteed
        for w in sorted(words, key=len):
            if not w:
                continue
            if can_form(w):
                res.append(w)
            insert(w)
        return res
```

### Template 8: Reversed-Word Trie (Palindrome Pairing) — LC 336

**Key Idea**: `words[i] + words[j]` is a palindrome iff one word "covers" the reverse of the other and
the *leftover* middle part is itself a palindrome. So insert every **reversed** word into a trie and
store two things per node:
- `word_index` — a reversed word ends exactly here
- `palindrome_below` — indices of words whose remaining suffix *below* this node is a palindrome

Then walking `words[i]` down the trie splits into exactly 2 cases:

```text
case 1 (words[j] is SHORTER): hit a node with word_index = j after k chars
        words[i] = [ reverse(words[j]) ][ leftover ]   -> valid iff leftover is a palindrome

case 2 (words[j] is LONGER/equal): consumed all of words[i], now at node `node`
        reverse(words[j]) = [ words[i] ][ leftover ]   -> valid iff leftover is a palindrome
                                                          (exactly node.palindrome_below)
```

The two cases are disjoint by length, so no pair is emitted twice.

```python
# python
# LC 336 - Palindrome Pairs
# IDEA: trie of REVERSED words; node stores the word ending there + words with palindromic remainder
# time = O(N * L^2), space = O(N * L)   # N = #words, L = max word length
class PairNode(object):
    def __init__(self):
        self.children = {}
        self.word_index = -1        # a reversed word ends here
        self.palindrome_below = []  # words whose remaining suffix below here is a palindrome

class Solution(object):
    def palindromePairs(self, words):

        def is_pal(s, i, j):
            while i < j:
                if s[i] != s[j]:
                    return False
                i += 1
                j -= 1
            return True

        root = PairNode()
        # build : insert reverse(word)
        for idx, word in enumerate(words):
            n = len(word)
            node = root
            for k in range(n):
                ### NOTE : remaining reversed suffix == reverse(word[0 .. n-1-k])
                if is_pal(word, 0, n - 1 - k):
                    node.palindrome_below.append(idx)
                ch = word[n - 1 - k]
                if ch not in node.children:
                    node.children[ch] = PairNode()
                node = node.children[ch]
            node.palindrome_below.append(idx)   # empty remainder is a palindrome
            node.word_index = idx

        res = []
        for idx, word in enumerate(words):
            node = root
            fell_off = False
            for k, ch in enumerate(word):
                # CASE 1 : a shorter reversed word ends here
                if node.word_index != -1 and node.word_index != idx \
                        and is_pal(word, k, len(word) - 1):
                    res.append([idx, node.word_index])
                if ch not in node.children:
                    fell_off = True
                    break
                node = node.children[ch]
            if fell_off:
                continue
            # CASE 2 : word is a prefix of these reversed words
            for j in node.palindrome_below:
                if j != idx:
                    res.append([idx, j])
        return res
```

```java
// java
// LC 336 - Palindrome Pairs
// IDEA: trie of REVERSED words; node stores the word ending there + words with palindromic remainder
class Solution {
    // time = O(N * L^2), space = O(N * L)   // N = #words, L = max word length
    class PairNode {
        Map<Character, PairNode> children = new HashMap<>();
        int wordIndex = -1;                                // a reversed word ends here
        List<Integer> palindromeBelow = new ArrayList<>(); // palindromic remainder below here
    }

    private PairNode root;

    public List<List<Integer>> palindromePairs(String[] words) {
        root = new PairNode();   // rebuild per call — LeetCode reuses one Solution object
        for (int i = 0; i < words.length; i++) insertReversed(words[i], i);

        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) search(words[i], i, res);
        return res;
    }

    private void insertReversed(String word, int idx) {
        PairNode node = root;
        int n = word.length();
        for (int k = 0; k < n; k++) {
            // remaining reversed suffix == reverse(word[0 .. n-1-k])
            if (isPalindrome(word, 0, n - 1 - k)) node.palindromeBelow.add(idx);
            char c = word.charAt(n - 1 - k);
            node.children.putIfAbsent(c, new PairNode());
            node = node.children.get(c);
        }
        node.palindromeBelow.add(idx);   // empty remainder is a palindrome
        node.wordIndex = idx;
    }

    private void search(String word, int idx, List<List<Integer>> res) {
        PairNode node = root;
        for (int k = 0; k < word.length(); k++) {
            // CASE 1 : a shorter reversed word ends here -> rest of `word` must be a palindrome
            if (node.wordIndex != -1 && node.wordIndex != idx
                    && isPalindrome(word, k, word.length() - 1)) {
                res.add(Arrays.asList(idx, node.wordIndex));
            }
            PairNode next = node.children.get(word.charAt(k));
            if (next == null) return;
            node = next;
        }
        // CASE 2 : `word` is a prefix of these reversed words
        for (int j : node.palindromeBelow) {
            if (j != idx) res.add(Arrays.asList(idx, j));
        }
    }

    private boolean isPalindrome(String s, int i, int j) {
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) return false;
        }
        return true;
    }
}
```

> The `""` (empty word) case is handled for free: it terminates at the **root**, so `root.palindromeBelow`
> is exactly "every word that is itself a palindrome".

### Template 9: Implicit Digit Trie (Lexicographic Numbers) — LC 386

**Key Idea**: `1..n` is a **10-ary trie** you never have to build — node `x` has children
`x*10 .. x*10+9`, and roots are `1..9`. A **pre-order DFS** of that trie emits numbers in
lexicographic order. Doing the DFS iteratively gives O(1) extra space.

```text
n = 13         1              2 ... 9
             / | \
           10 11 12 13        pre-order -> 1, 10, 11, 12, 13, 2, 3, ... 9
```

**Move rules** (the whole algorithm):
- go **deeper**: `cur * 10` (if `cur * 10 <= n`)
- else go to **next sibling**: `cur + 1`
- if the sibling doesn't exist (`cur % 10 == 9` or `cur + 1 > n`), **backtrack** with `cur //= 10` first

```python
# python
# LC 386 - Lexicographical Numbers
# IDEA: 1..n is an implicit 10-ary trie; pre-order DFS of it == lexicographic order
# time = O(n), space = O(1) extra (excluding output)
class Solution(object):
    def lexicalOrder(self, n):
        res = []
        cur = 1
        for _ in range(n):
            res.append(cur)
            if cur * 10 <= n:
                cur *= 10           # go DEEPER (append digit 0)
            else:
                ### NOTE : climb up while there is no next sibling
                while cur % 10 == 9 or cur + 1 > n:
                    cur //= 10
                cur += 1            # next SIBLING
        return res
```

```java
// java
// LC 386 - Lexicographical Numbers
// IDEA: 1..n is an implicit 10-ary trie; pre-order DFS of it == lexicographic order
class Solution {
    public List<Integer> lexicalOrder(int n) {
        // time = O(n), space = O(1) extra (excluding output)
        List<Integer> res = new ArrayList<>();
        int cur = 1;
        for (int i = 0; i < n; i++) {
            res.add(cur);
            if ((long) cur * 10 <= n) {
                cur *= 10;                 // go DEEPER
            } else {
                // climb up while there is no next sibling
                while (cur % 10 == 9 || cur + 1 > n) {
                    cur /= 10;
                }
                cur += 1;                  // next SIBLING
            }
        }
        return res;
    }
}
```

### Template 1 variation — Shortest Root Replacement — LC 648

**Twist**: while walking a word down a normal trie, **stop at the FIRST `is_end` node** — that is the
shortest dictionary root, which is exactly what LC 648 asks to substitute in.

```python
# python
# LC 648 - Replace Words
# IDEA: standard trie; when walking each word, return at the FIRST is_end node (shortest root)
# time = O(M + S), space = O(M)   # M = total chars in dictionary, S = total chars in sentence
class Solution(object):
    def replaceWords(self, dictionary, sentence):
        root = TrieNode()
        for w in dictionary:
            node = root
            for ch in w:
                if ch not in node.children:
                    node.children[ch] = TrieNode()
                node = node.children[ch]
            node.is_end = True

        def shortest_root(word):
            node = root
            for i, ch in enumerate(word):
                if ch not in node.children:
                    break
                node = node.children[ch]
                if node.is_end:
                    return word[:i + 1]   ### NOTE : first is_end wins
            return word

        return " ".join(shortest_root(w) for w in sentence.split())
```

```java
// java
// LC 648 - Replace Words
// IDEA: standard trie; when walking each word, return at the FIRST isEnd node (shortest root)
class Solution {
    public String replaceWords(List<String> dictionary, String sentence) {
        // time = O(M + S), space = O(M)  // M = total dict chars, S = total sentence chars
        TrieNode root = new TrieNode();
        for (String w : dictionary) {
            TrieNode node = root;
            for (char c : w.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.isEnd = true;
        }

        StringBuilder sb = new StringBuilder();
        for (String word : sentence.split(" ")) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(shortestRoot(root, word));
        }
        return sb.toString();
    }

    private String shortestRoot(TrieNode root, String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            node = node.children.get(word.charAt(i));
            if (node == null) break;
            if (node.isEnd) return word.substring(0, i + 1);  // first isEnd wins
        }
        return word;
    }
}
```

### Also solvable with a trie (no new template)

- **LC 14 Longest Common Prefix** — insert all words, then walk down from the root while a node has
  exactly 1 child and is not `is_end`; the path walked is the answer. (The plain vertical scan is
  simpler in an interview — mention the trie only if asked for repeated queries.)