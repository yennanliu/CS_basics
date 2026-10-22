"""

211. Design Add and Search Words Data Structure
Medium

Design a data structure that supports adding new words and finding if a
string matches any previously added string.

Implement the WordDictionary class:

WordDictionary() Initializes the object.
void addWord(word) Adds word to the data structure, it can be matched later.
bool search(word) Returns true if there is any string in the data structure
that matches word or false otherwise. word may contain dots '.' where dots
can be matched with any letter.


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

1 <= word.length <= 25
word in addWord consists of lowercase English letters.
word in search consist of '.' or lowercase English letters.
There will be at most 2 dots in word for search queries.
At most 10^4 calls will be made to addWord and search.

"""

# NOTE : every version below is a design class. LC calls it `WordDictionary`,
#        so when pasting V0-1 / V1 back into LC, rename `WordDictionary2` /
#        `WordDictionary3` to `WordDictionary`.


# V0
# IDEA : TRIE + RECURSION (DFS on index)
#
#   addWord : plain trie insert, mark the last node `is_end`.
#   search  : helper(word, idx, node) = "can word[idx:] be matched starting
#             at node?"
#             - idx == len(word)  -> matched every char, but only a WORD if
#                                    node.is_end (".ad" must not match "adx")
#             - ch == "."         -> try EVERY child, True if any branch works
#             - ch is a letter    -> follow that one child, or False
#
#   NOTE : for both cases (ch == or != '.') we proceed with a recursive call;
#          '.' just fans out to all children instead of one.
#
#   e.g. words = [bad, dad, mad], search(".ad")
#        root -'.'-> {b, d, m} -> each -'a'-> -'d'-> is_end -> True on "b"
#
# time = O(L) addWord; search O(L) without dots, O(26^d * L) worst with d dots
# space = O(total chars inserted) for the trie, O(L) recursion stack
class myNode(object):
    def __init__(self):
        self.child = {}
        self.is_end = False


class WordDictionary(object):

    def __init__(self):
        self.node = myNode()

    def addWord(self, word):
        """
        :type word: str
        :rtype: None
        """
        node = self.node

        for ch in word:
            if ch not in node.child:

                """
                NOTE !!!

                the key of hashmap is `myNode` type.

                -> so we should init it as `myNode()`,
                       -> so we have the defined attr
                            - child
                            - is_end


                -> NOTE !!!

                   DON'T do below: (below is WRONG !!!!)

                    ```node.child[ch] = {} ```

                """
                node.child[ch] = myNode()
            
            node = node.child[ch]

        node.is_end = True

    def search(self, word):
        """
        :type word: str
        :rtype: bool
        """
        """
        NOTE !!!


        1. we simply call the helper func


        2. pass 3 param to the helper func:

            ->
                1. word
                2. idx
                3. node

        """
        return self.helper(word, 0, self.node)


    """
    NOTE !!!

    the structure of dfs helper func


    1. DON'T looping over word (explicitly)

    2. pass `node` as param, so we don't need to deal with
        `node = self.node` in helper func


    3. deal with ch == "." case first

        -> and if `can make it`, return True directly
        -> otherwise, return False


    4. finally, 
       deal with ch != "." case  

       -> e.g. ```self.helper(word, idx + 1, node.child[ch])```
       
    """
    def helper(self, word, idx, node):
        """Return True if word[idx:] can be matched starting from node."""
        if idx == len(word):
            return node.is_end


        """
        NOTE !!!


        1. we DON'T loop over word

        2. we use `idx` to get the char from word.

            -> ch = word[idx]


        """
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


        # NOTE !!! below
        # `node.child[ch]` is the `next node` in trie
        return self.helper(word, idx + 1, node.child[ch])


# V0-1
# IDEA : TRIE + ITERATIVE WALK, RECURSE ONLY ON '.'
#
#   Same trie as V0. Letters are followed with a plain loop (no stack frame
#   per char); only a '.' needs branching, so only '.' starts a recursive
#   call on each child. With at most 2 dots per query (constraints), the
#   recursion depth is at most 2.
#
# time = O(L) addWord; search O(L) without dots, O(26^d * L) worst with d dots
# space = O(total chars inserted) for the trie, O(d) recursion stack
class TrieNode(object):
    def __init__(self):
        # char -> child TrieNode
        self.children = {}
        # True if an added word ends exactly at this node
        self.is_end = False


class WordDictionary2(object):

    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word):
        """
        :type word: str
        :rtype: None
        """
        node = self.root
        for ch in word:
            if ch not in node.children:
                node.children[ch] = TrieNode()
            node = node.children[ch]
        node.is_end = True

    def search(self, word):
        """
        :type word: str
        :rtype: bool
        """
        return self._search_from(word, 0, self.root)

    def _search_from(self, word, start, node):
        """Return True if word[start:] can be matched starting from node."""
        for i in range(start, len(word)):
            ch = word[i]

            if ch == ".":
                # wildcard: the rest of the word may continue under ANY child
                for child in node.children.values():
                    if self._search_from(word, i + 1, child):
                        return True
                # every branch failed -> this path is a dead end
                return False

            # normal letter: exactly one child can continue the match
            if ch not in node.children:
                return False
            node = node.children[ch]

        # consumed the whole word: it only counts if a word ENDS here
        return node.is_end


# V1
# IDEA : TRIE + BFS (level = index in word)
#
#   Keep the set of trie nodes that match word[:i] (the "frontier"). For
#   each next char, the new frontier is every child reachable by that char
#   ('.' -> all children). After the last char, the word matches if any
#   frontier node is the end of a word. Same bound as DFS, no recursion,
#   but holds a whole level of nodes at once.
#
# time = O(L) addWord; search O(26^d * L) worst with d dots
# space = O(total chars inserted) for the trie, O(26^d) frontier
class TrieNode(object):
    def __init__(self):
        self.children = {}
        self.is_end = False


class WordDictionary3(object):

    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word):
        """
        :type word: str
        :rtype: None
        """
        node = self.root
        for ch in word:
            if ch not in node.children:
                node.children[ch] = TrieNode()
            node = node.children[ch]
        node.is_end = True

    def search(self, word):
        """
        :type word: str
        :rtype: bool
        """
        # nodes whose root path matches the prefix processed so far
        frontier = [self.root]

        for ch in word:
            next_frontier = []
            for node in frontier:
                if ch == ".":
                    # wildcard: every child continues the match
                    for child in node.children.values():
                        next_frontier.append(child)
                elif ch in node.children:
                    next_frontier.append(node.children[ch])

            # no node matches this prefix -> no word can match
            if not next_frontier:
                return False
            frontier = next_frontier

        for node in frontier:
            if node.is_end:
                return True
        return False
