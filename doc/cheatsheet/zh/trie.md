# 字典樹（Trie）

> **範圍** — 前綴樹 — insert/search/startsWith、節點該怎麼擺，以及那些「共用前綴」就是全部訣竅的題目（自動補完、單字搜尋、XOR trie）。
> **另見**：[trie_examples.md](./trie_examples.md) — 這些模板背後的五道實作題；[string.md](./string.md) — 不用 trie 的字串處理；[hash_map.md](./hash_map.md) — 只要存整個單字的集合就夠時；[dfs.md](./dfs.md) — trie 題目底下跑的那套走訪；[advanced_string_algorithms.md](./advanced_string_algorithms.md) — 以後綴為基礎的替代方案。

> 只要題目一次丟出很多個字串，就該先想想 trie 能不能幫上忙。
- https://leetcode.com/problems/search-suggestions-system/solution/

## LeetCode 題目清單

- [Trie](https://leetcode.com/problem-list/trie/)
- [String](https://leetcode.com/problem-list/string/)

## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| Trie           | O(L)     | O(L)     | O(L)     | O(L)     |

> **L = 鍵值（單字）的長度** — 複雜度跟存了幾個鍵值無關。最小／最大 = 字典序最小／最大的鍵值。

## 0) 概念
- https://blog.csdn.net/fuxuemingzhu/article/details/79388432
- 樹 + dict
    - `put Node into dict`（例如 defaultdict(Node)）

<p align="center"><img src="../pic/trie_1.png"></p>

<p align="center"><img src="../pic/trie_2.png"></p>

### 0-1) 分類

- **雜湊表版 trie** — `children` 是 dict／`Map`；字母集合可以任意（見下面的 Pattern）。
- **陣列版 trie** — `children` 是固定的 `[None] * 26` / `TrieNode[26]`；只處理小寫時最快（模板 2）。
- **萬用字元 trie** — 靠對所有子節點做 DFS 來支援 `.` 比對（模板 3，LC 211）。
- **二元（XOR）trie** — 子節點就是 bit `0/1`；用在 max-XOR／位元運算題（模板 5，LC 421）。

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

## 模板與演算法

模板 1 就是 [0-2) Pattern](#0-2-pattern) 裡那個雜湊表版的 trie。底下全部都是同一個結構，
只改動一件事：裝子節點的容器、走訪到某個節點時允許做什麼，或者「字母表」是什麼。

### 模板 2：陣列版 Trie（固定字母集合）
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

### 模板 3：支援萬用字元的 Trie — LC 211
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

### 模板 4：自動補完 Trie — LC 1268
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

### 模板 5：二元 Trie（XOR 題） — LC 421 ⭐⭐⭐
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

### 模板 6：支援刪除的 Trie

**刪除演算法 — 三步驟遞迴邏輯：**
1. 一路走到單字結尾；如果這個單字不存在，回傳 `False`。
2. 把結尾節點的 `is_end` 取消掉。
3. 回溯的過程中，把已經變成「非終端葉節點」（沒有子節點、也不是別的單字的結尾）的子節點移掉 — 這一步負責清掉懸空的節點。

**關鍵不變量**：只有在一個節點「沒有剩下任何子節點」**而且**「不是另一個單字的結尾」時才能刪。共用的前綴必須留著。

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

**追蹤 — trie 裡同時有 `"app"` 時執行 `delete("apple")`：**
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

模板 1-6 沒涵蓋到的模式。每一個都是換一種方式*走*這棵 trie —
trie 本身幾乎沒變。

| 題目裡的關鍵訊號 | 模板 | 實作範例 |
|-----------------------|----------|----------------|
| 「把字串切成字典裡的單字」 | 模板 7 — Trie + DP | LC 139、LC 472 |
| 「把兩個單字接成迴文」 | 模板 8 — 反向單字 trie | LC 336 |
| 「數字的字典序」 | 模板 9 — 隱式的十元數字 trie | LC 386 |
| 「用最短的字根／前綴取代單字」 | 模板 1 的變形 — 在第一個 `is_end` 就停 | LC 648 |

### 模板 7：Trie + DP（單字切分） — LC 139 ⭐⭐⭐⭐⭐

**關鍵想法**：`dp[i] = 「s[0..i) 可以被切開」`。從每個到得了的索引 `i` 出發，一次一個字元往 trie 深處走；
每次落在位置 `j` 的 `is_end` 節點上，就把 `dp[j+1] = True`。
trie 取代了內層那個「把字典裡每個單字都試一遍」的迴圈 — 前綴一離開 trie 你就跳出，
所以完全不需要對子字串做雜湊。

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

**變形 — LC 472 Concatenated Words**：一樣是 trie + DP，但答案需要**至少 2 段**，
而且一個單字不能由它自己組成。訣竅：**依長度排序，邊測邊插入** — 測 `w` 的時候，
trie 裡只有*嚴格更短*的單字，所以任何切得成功的結果自動就用到了 ≥ 2 個單字。

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

### 模板 8：反向單字 Trie（迴文配對） — LC 336

**關鍵想法**：`words[i] + words[j]` 是迴文，若且唯若其中一個單字「蓋住」了另一個的反轉，
而*剩下*的中段本身也是迴文。所以把每個**反轉後**的單字插進 trie，每個節點存兩樣東西：
- `word_index` — 剛好有一個反轉單字結束在這裡
- `palindrome_below` — 那些在這個節點*底下*剩餘後綴是迴文的單字索引

接著拿 `words[i]` 往 trie 下走，情況剛好分成 2 種：

```text
case 1 (words[j] is SHORTER): hit a node with word_index = j after k chars
        words[i] = [ reverse(words[j]) ][ leftover ]   -> valid iff leftover is a palindrome

case 2 (words[j] is LONGER/equal): consumed all of words[i], now at node `node`
        reverse(words[j]) = [ words[i] ][ leftover ]   -> valid iff leftover is a palindrome
                                                          (exactly node.palindrome_below)
```

這兩種情況以長度區分、彼此不重疊，所以不會有同一組配對被輸出兩次。

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

> `""`（空字串）的情況不用特別處理就對了：它會終止在**根節點**，所以 `root.palindromeBelow`
> 剛好就是「所有本身是迴文的單字」。

### 模板 9：隱式數字 Trie（字典序數字） — LC 386

**關鍵想法**：`1..n` 就是一棵你永遠不用真的建出來的**十元 trie** — 節點 `x` 的子節點是
`x*10 .. x*10+9`，根節點則是 `1..9`。對這棵 trie 做**前序 DFS**，吐出來的數字就是字典序。
把 DFS 寫成迭代版，額外空間就是 O(1)。

```text
n = 13         1              2 ... 9
             / | \
           10 11 12 13        pre-order -> 1, 10, 11, 12, 13, 2, 3, ... 9
```

**移動規則**（整個演算法就這樣）：
- 往**深處**走：`cur * 10`（前提是 `cur * 10 <= n`）
- 否則走到**下一個兄弟**：`cur + 1`
- 如果兄弟不存在（`cur % 10 == 9` 或 `cur + 1 > n`），先用 `cur //= 10` **回溯**

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

### 模板 1 的變形 — 用最短字根取代 — LC 648

**訣竅**：拿一個單字在普通 trie 上往下走時，**在第一個 `is_end` 節點就停** — 那就是最短的字典字根，
剛好是 LC 648 要你替換上去的東西。

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

## 1) 一般形式

### 1-1) 基本操作

看 [0-2) Pattern](#0-2-pattern) 裡的 `insert` / `search` / `startsWith` 骨架，
以及上面那些變形模板（陣列版、萬用字元、自動補完、二元／XOR、刪除）。

## 進階 Trie 變形 — XOR Trie、串流比對、刪除

### XOR Trie — LC 421，精簡版參考

> 完整走一遍在上面的 [Template 5](#template-5-binary-trie-xor-problems--lc-421-)；這裡是同一個想法的速查版。
用二元 trie（子節點是 bit 0/1）找出任兩個數字之間的最大 XOR。

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

### Trie + DP（串流比對） — LC 1032 Stream of Characters
把**反轉**單字組成的 trie 跟目前為止的串流結合，然後倒著走這段串流。

> **複雜度**：走訪會停在第一個沒有 trie 子節點的字元，所以一次查詢是
> O(L)，L 是字典裡最長的單字 — **不是** O(1)，只要你有節制地保留資料，也不會是 O(stream)。
> 下面這個版本一直往 `self.stream` 塞東西，所以走訪長度受串流長度限制；只保留最後 L 個字元
> （`deque(maxlen=L)`）才能讓每次查詢是 O(L)、記憶體是 O(L)。

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

### Trie 刪除 — 精簡版參考

> 完整實作在上面的 [Template 6](#template-6-trie-with-delete-operation)；這裡把它維持的不變量單獨拉出來講。

```python
# NOTE !!! two different booleans are in play, and conflating them is the classic bug:
#   - `deleted`  -- was the WORD removed?          (what the caller asked)
#   - `prune`    -- may the parent drop this node? (an internal signal)
# Returning the prune signal as the result reports False for "delete('a')" when "ab"
# also exists, because the node survives as a prefix even though the word is gone.
def delete(self, word: str) -> bool:
    deleted = False

    def _delete(node, depth):
        nonlocal deleted
        if depth == len(word):
            if not node.is_end:
                return False              # word was never here -> nothing to prune
            node.is_end = False
            deleted = True
            return len(node.children) == 0
        ch = word[depth]
        if ch not in node.children:
            return False
        if _delete(node.children[ch], depth + 1):
            del node.children[ch]
            return len(node.children) == 0 and not node.is_end
        return False

    _delete(self.root, 0)
    return deleted
```

### 前綴-後綴 Trie — LC 745
要同時比對前綴和後綴的題目，把每個單字包成 `suffix#word` 再插進同一棵 trie。

```python
# For word "apple", insert: "apple#apple", "pple#apple", ..., "e#apple", and "#apple"
# NOTE !!! range(len(word) + 1) -- the last iteration inserts the EMPTY suffix, which is
#          what makes a query with suffix "" matchable. range(len(word)) silently drops it.
def buildIndex(words):
    trie = {}
    for weight, word in enumerate(words):
        for i in range(len(word) + 1):
            key = word[i:] + '#' + word
            node = trie
            for c in key:
                node = node.setdefault(c, {})
            node['weight'] = weight  # store latest (highest) weight
    return trie
```

### 面試提示 — trie
| 訊號 | 模式 |
|--------|---------|
| 「前綴比對」、「自動補完」 | 標準 trie |
| 「最大 XOR」、「位元最佳化」 | 二元 XOR trie |
| 「字元串流」、「即時比對」 | 反向單字 trie + 狀態 |
| 「前綴**和**後綴都要」 | suffix#word trie |
| 「萬用字元 `.` 比對」 | 在 `.` 節點做 DFS |
| 「數有幾個單字帶這個前綴」 | 在 TrieNode 上加一個 `count` 欄位 |

---

### 也能用 trie 解（不需要新模板）

- **LC 14 Longest Common Prefix** — 把所有單字插進去，然後從根往下走，只要節點剛好只有 1 個子節點
  且不是 `is_end` 就繼續；走過的路徑就是答案。（面試時單純的垂直掃描更簡單 — 只有在對方問「重複查詢很多次呢」時才提 trie。）

## 實作範例

五道題目放在 **[trie_examples.md](./trie_examples.md)**：

| 分組 | 題目 |
|---|---|
| [Building the structure](./trie_examples.md#building-the-structure) | LC 208, 211 |
| [Searching with it](./trie_examples.md#searching-with-it) | LC 1268, 79, 212 |

LC 79 放在那裡而且刻意不用 trie：它是讓 LC 212 變得好懂的基準線 —
同樣的格子 DFS，每個單字各跑一次，直到 trie 把所有單字塌縮成同一趟走訪為止。
