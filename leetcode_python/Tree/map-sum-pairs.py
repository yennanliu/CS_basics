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