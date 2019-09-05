# V0 

# V1 
# http://bookshadow.com/weblog/2017/09/17/leetcode-map-sum-pairs/
class TrieNode:
    # Initialize your data structure here.
    def __init__(self):
        self.children = dict()
        self.sum = 0
        self.val = 0

class MapSum(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.root = TrieNode()

    def insert(self, key, val):
        """
        :type key: str
        :type val: int
        :rtype: void
        """
        node = self.root
        nodes = []
        for letter in key:
            child = node.children.get(letter)
            if child is None:
                child = TrieNode()
                node.children[letter] = child
            nodes.append(child)
            node = child
        if node.val != val:
            delta = val - node.val
            node.val = val
            for node in nodes:
                node.sum += delta

    def sum(self, prefix):
        """
        :type prefix: str
        :rtype: int
        """
        node = self.root
        for letter in prefix:
            node = node.children.get(letter)
            if node is None: return 0
        return node.sum       
# Your MapSum object will be instantiated and called as such:
# obj = MapSum()
# obj.insert(key,val)
# param_2 = obj.sum(prefix)

# V1'
# https://www.jiuzhang.com/solution/map-sum-pairs/#tag-highlight-lang-python
class TrieNode:
    def __init__(self):
        self.son = {}
        self.val = 0

class Trie:
    root = TrieNode()
    
    def insert(self, s, val):
        cur = self.root
        for i in range(0, len(s)):
            if s[i] not in cur.son:
                cur.son[s[i]] = TrieNode()
            cur = cur.son[s[i]]
            cur.val += val
            
    def find(self, s):
        cur = self.root
        for i in range(0, len(s)):
            if s[i] not in cur.son:
                return 0
            cur = cur.son[s[i]]
        return cur.val
    
    
class MapSum:
    
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.d = {}
        self.trie = Trie()
        

    def insert(self, key, val):
        """
        :type key: str
        :type val: int
        :rtype: void
        """
        if key in self.d:
            self.trie.insert(key, val - self.d[key])
        else:
            self.trie.insert(key, val)
        self.d[key] = val
        

    def sum(self, prefix):
        """
        :type prefix: str
        :rtype: int
        """
        return self.trie.find(prefix)
# Your MapSum object will be instantiated and called as such:
# obj = MapSum()
# obj.insert(key,val)
# param_2 = obj.sum(prefix)

# V2 
# Time:  O(n), n is the length of key
# Space: O(t), t is the number of nodes in trie
import collections
class MapSum(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        _trie = lambda: collections.defaultdict(_trie)
        self.__root = _trie()


    def insert(self, key, val):
        """
        :type key: str
        :type val: int
        :rtype: void
        """
        # Time: O(n)
        curr = self.__root
        for c in key:
            curr = curr[c]
        delta = val
        if "_end" in curr:
            delta -= curr["_end"]

        curr = self.__root
        for c in key:
            curr = curr[c]
            if "_count" in curr:
                curr["_count"] += delta
            else:
                curr["_count"] = delta
        curr["_end"] = val


    def sum(self, prefix):
        """
        :type prefix: str
        :rtype: int
        """
        # Time: O(n)
        curr = self.__root
        for c in prefix:
            if c not in curr:
                return 0
            curr = curr[c]
        return curr["_count"]