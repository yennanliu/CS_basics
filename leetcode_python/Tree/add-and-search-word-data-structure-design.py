"""

211. Design Add and Search Words Data Structure
Medium

Design a data structure that supports adding new words and finding if a string matches any previously added string.

Implement the WordDictionary class:

WordDictionary() Initializes the object.
void addWord(word) Adds word to the data structure, it can be matched later.
bool search(word) Returns true if there is any string in the data structure that matches word or false otherwise. word may contain dots '.' where dots can be matched with any letter.
 

Example:

Input
["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
[[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]
Output
[null,null,null,null,false,true,true,true]

Explanation
WordDictionary wordDictionary = new WordDictionary();
wordDictionary.addWord("bad");
wordDictionary.addWord("dad");
wordDictionary.addWord("mad");
wordDictionary.search("pad"); // return False
wordDictionary.search("bad"); // return True
wordDictionary.search(".ad"); // return True
wordDictionary.search("b.."); // return True
 

Constraints:

1 <= word.length <= 500
word in addWord consists lower-case English letters.
word in search consist of  '.' or lower-case English letters.
At most 50000 calls will be made to addWord and search.

"""


# V0
# IDEA: TRIE + recursion (gemini)
class MyNode(object):
    def __init__(self):
        # Maps characters to their corresponding child MyNode objects
        self.next = {}
        self.is_end = False

class WordDictionary(object):
    def __init__(self):
        # Create a fresh root node for our Trie
        self.root = MyNode()

    def addWord(self, word):
        """
        :type word: str
        :rtype: None
        """
        node = self.root
        for char in word:
            if char not in node.next:
                node.next[char] = MyNode()
            node = node.next[char]  # Step down into the next character node
        node.is_end = True

    def search(self, word):
        """
        :type word: str
        :rtype: bool
        """
        # Kick off the recursive search helper starting at index 0 from the root
        return self.search_helper(word, 0, self.root)
        
    def search_helper(self, word, index, node):
        # Base Case: If we've matched every character, check if this forms a complete word
        if index == len(word):
            return node.is_end
            
        char = word[index]

        """
        NOTE !!!

        for both cases (char == or != '.'),
        we ALL proceed with recursion call !!!

         - return self.search_helper(word, index + 1, node.next[char])

         - if self.search_helper(word, index + 1, child_node)
            ...
        """
        
        if char != ".":
            # Standard exact match lookup
            if char not in node.next:
                return False
            return self.search_helper(word, index + 1, node.next[char])
        else:
            # Wildcard match: Try exploring EVERY available child branch path!
            for child_node in node.next.values():
                if self.search_helper(word, index + 1, child_node):
                    return True  # If any branch successfully matches the rest, return True
            return False



# V0-1
# IDEA: TRIE + recursion (GPT)
class MyNode(object):
    def __init__(self):
        # children:
        # {
        #   'a': MyNode(),
        #   'b': MyNode(),
        #   ...
        # }
        self.next = {}

        # True if a word ends at this node
        self.is_end = False

    def build(self, word):
        # Start from the current node (root)
        node = self

        # Insert each character into the Trie
        for ch in word:
            if ch not in node.next:
                node.next[ch] = MyNode()

            # Move to the child
            node = node.next[ch]

        # Mark the last node as the end of a word
        node.is_end = True


class WordDictionary(object):

    def __init__(self):
        # Root of the Trie
        self.node = MyNode()

    def addWord(self, word):
        self.node.build(word)

    def search(self, word):
        # Start DFS from the root
        return self.search_helper(self.node, word, 0)

    def search_helper(self, node, word, i):
        """
        node : current Trie node
        word : search string
        i    : current index in word
        """

        # Base case:
        # We've matched every character in the search word.
        # Return True only if this Trie node marks the end of a word.
        if i == len(word):
            return node.is_end

        ch = word[i]

        # --------------------------------------------------
        # Normal character
        # --------------------------------------------------
        if ch != ".":

            # Character doesn't exist
            if ch not in node.next:
                return False

            # Continue DFS with the matching child
            return self.search_helper(node.next[ch], word, i + 1)

        # --------------------------------------------------
        # Wildcard '.'
        # '.' can match ANY single character.
        # Try every child.
        # --------------------------------------------------
        for child in node.next.values():

            if self.search_helper(child, word, i + 1):
                # Found one valid path
                return True

        # None of the children worked
        return False


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

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79390052
class Node(object):
    def __init__(self):
        self.children = collections.defaultdict(Node)
        self.isword = False
        
class WordDictionary(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.root = Node()

    def addWord(self, word):
        """
        Adds a word into the data structure.
        :type word: str
        :rtype: void
        """
        current = self.root
        for w in word:
            current = current.children[w]
        current.isword = True

    def search(self, word):
        """
        Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter.
        :type word: str
        :rtype: bool
        """
        return self.match(word, 0, self.root)
    
    def match(self, word, index, root):
        if root == None:
            return False
        if index == len(word):
            return root.isword
        if word[index] != '.':
            return root != None and self.match(word, index + 1, root.children.get(word[index]))
        else:
            for child in root.children.values():
                if self.match(word, index + 1, child):
                    return True
        return False

# Your WordDictionary object will be instantiated and called as such:
# obj = WordDictionary()
# obj.addWord(word)

# V1'
# https://www.jiuzhang.com/solution/add-and-search-word-data-structure-design/#tag-highlight-lang-python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_word = False

class WordDictionary:
    
    def __init__(self):
        self.root = TrieNode()
        
    """
    @param: word: Adds a word into the data structure.
    @return: nothing
    """
    def addWord(self, word):
        node = self.root
        for c in word:
            if c not in node.children:
                node.children[c] = TrieNode()
            node = node.children[c]
        node.is_word =True

    """
    @param: word: A word could contain the dot character '.' to represent any one letter.
    @return: if the word is in the data structure.
    """
    def search(self, word):
        if word is None:
            return False
        return self.search_helper(self.root, word, 0)
        
    def search_helper(self, node, word, index):
        if node is None:
            return False
            
        if index >= len(word):
            return node.is_word
        
        char = word[index]
        if char != '.':
            return self.search_helper(node.children.get(char), word, index + 1)
            
        for child in node.children:
            if self.search_helper(node.children[child], word, index + 1):
                return True
                
        return False

# V1''
# https://www.jiuzhang.com/solution/add-and-search-word-data-structure-design/#tag-highlight-lang-python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_word = False

class WordDictionary:
    
    def __init__(self):
        self.root = TrieNode()
        
    """
    @param: word: Adds a word into the data structure.
    @return: nothing
    """
    def addWord(self, word):
        node = self.root
        for c in word:
            if c not in node.children:
                node.children[c] = TrieNode()
            node = node.children[c]
        node.is_word =True

    """
    @param: word: A word could contain the dot character '.' to represent any one letter.
    @return: if the word is in the data structure.
    """
    def search(self, word):
        if word is None:
            return False
        return self.search_helper(self.root, word, 0)
        
    def search_helper(self, node, word, index):
        if node is None:
            return False
            
        if index >= len(word):
            return node.is_word
        
        char = word[index]
        if char != '.':
            return self.search_helper(node.children.get(char), word, index + 1)
            
        for child in node.children:
            if self.search_helper(node.children[child], word, index + 1):
                return True
                
        return False

# V2 
# Time:  O(min(n, h)), per operation
# Space: O(min(n, h))
class TrieNode(object):
    # Initialize your data structure here.
    def __init__(self):
        self.is_string = False
        self.leaves = {}

class WordDictionary(object):
    def __init__(self):
        self.root = TrieNode()

    # @param {string} word
    # @return {void}
    # Adds a word into the data structure.
    def addWord(self, word):
        curr = self.root
        for c in word:
            if c not in curr.leaves:
                curr.leaves[c] = TrieNode()
            curr = curr.leaves[c]
        curr.is_string = True

    # @param {string} word
    # @return {boolean}
    # Returns if the word is in the data structure. A word could
    # contain the dot character '.' to represent any one letter.
    def search(self, word):
        return self.searchHelper(word, 0, self.root)

    def searchHelper(self, word, start, curr):
        if start == len(word):
            return curr.is_string
        if word[start] in curr.leaves:
            return self.searchHelper(word, start+1, curr.leaves[word[start]])
        elif word[start] == '.':
            for c in curr.leaves:
                if self.searchHelper(word, start+1, curr.leaves[c]):
                    return True

        return False
