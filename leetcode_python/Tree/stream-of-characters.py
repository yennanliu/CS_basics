"""

1032. Stream of Characters
Hard

Design an algorithm that accepts a stream of characters and checks if a suffix
of these characters is a string of a given array of strings words.

For example, if words = ["abc", "xyz"] and the stream added the four characters
(one by one) 'a', 'x', 'y', and 'z', your algorithm should detect that the
suffix "xyz" of the characters "axyz" matches "xyz" from words.

Implement the StreamChecker class:

StreamChecker(String[] words) Initializes the object with the strings array words.
boolean query(char letter) Accepts a new character from the stream and returns
true if any non-empty suffix from the stream forms a word that is in words.

Example 1:

Input
["StreamChecker", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query"]
[[["cd", "f", "kl"]], ["a"], ["b"], ["c"], ["d"], ["e"], ["f"], ["g"], ["h"], ["i"], ["j"], ["k"], ["l"]]

Output
[null, false, false, false, true, false, true, false, false, false, false, false, true]

Explanation
StreamChecker streamChecker = new StreamChecker(["cd", "f", "kl"]);
streamChecker.query("a"); // return False
streamChecker.query("b"); // return False
streamChecker.query("c"); // return False
streamChecker.query("d"); // return True, because 'cd' is in the wordlist
streamChecker.query("e"); // return False
streamChecker.query("f"); // return True, because 'f' is in the wordlist
streamChecker.query("g"); // return False
streamChecker.query("h"); // return False
streamChecker.query("i"); // return False
streamChecker.query("j"); // return False
streamChecker.query("k"); // return False
streamChecker.query("l"); // return True, because 'kl' is in the wordlist

Constraints:

1 <= words.length <= 2000
1 <= words[i].length <= 200
words[i] consists of lowercase English letters.
letter is a lowercase English letter.
At most 4 * 10^4 calls will be made to query.

"""

# V0
class StreamChecker(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        pass
        

    def query(self, letter):
        """
        :type letter: str
        :rtype: bool
        """
        pass
        

# V0-0-1
# IDEA: TRIE + REVERSE processing / build (gpt)
class MyNode(object):
    def __init__(self):
        self.child = {}
        self.is_end = False


class StreamChecker(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.node = MyNode()

        # Store the `entire` stream `without` limiting its length.
        self.stream = []

        # Build a Reverse Trie.
        for word in words:
            self.add_word(word)

    def query(self, letter):
        """
        :type letter: str
        :rtype: bool
        """
        # Keep every character received so far.
        self.stream.append(letter)

        # NOTE !!!
        # -> search backwards.
        # -> 每次查詢都從最新字元開始，沿著 Reverse Trie 搜尋。
        #
        # Search backwards, starting from the newest character.
        node = self.node

        for ch in reversed(self.stream):
            # No Trie path exists for this suffix.
            if ch not in node.child:
                return False

            node = node.child[ch]

            # Found a complete word matching the stream's suffix.
            if node.is_end:
                return True

        return False

    def add_word(self, word):
        # Reverse the word before inserting it into the Trie.
        word = word[::-1]
        node = self.node

        for ch in word:
            if ch not in node.child:
                node.child[ch] = MyNode()

            node = node.child[ch]

        node.is_end = True




# V0-1
# IDEA: TRIE + REVERSE processing / build + max_len optimization (gpt)
from collections import deque

class MyNode(object):
    def __init__(self):
        self.child = {}
        self.is_end = False


class StreamChecker(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.node = MyNode()

        """
        NOTE !!!


        the `self.max_len` is NOT necessary; just an optimization way.


        ->

        self.max_len 的主要用途是：限制我們需要記住的歷史字元數量，`避免儲存整個 stream。`


            -> 在 LC 1032 中，我們只需要檢查目前 stream 的最後一段是否符合某個單字，
               因此不需要保留所有歷史字元。


            ->

            補充：self.max_len 並不是 Trie 搜尋正確性的必要條件，
                 因為 Trie 本身也會在路徑不存在時停止搜尋；但它能明確限制歷史資料的大小，
                 讓記憶體使用量保持在 O(W)



            ->
                面試重點：
                     當問題只需要檢查最近 W 個字元時，就不必儲存整段歷史。
                     使用 deque(maxlen=W) 可以自動保留最近的 W 個字元，讓程式更有效率。


        """

        # Find the maximum word length.
        # Use a loop for compatibility with older Python versions.
        self.max_len = 0
        for word in words:
            self.max_len = max(self.max_len, len(word))

        # Keep the newest character at the front.
        # We only need to remember max_len characters.
        self.stream = deque(maxlen=self.max_len)

        # Build a reverse Trie.
        for word in words:
            self.add_word(word)

    def query(self, letter):
        """
        :type letter: str
        :rtype: bool
        """
        if self.max_len == 0:
            return False

        # Add the newest character to the front.
        self.stream.appendleft(letter)

        return self.is_matched()

    def is_matched(self):
        node = self.node

        # Traverse from newest character to oldest.
        for ch in self.stream:
            if ch not in node.child:
                return False

            node = node.child[ch]

            # A complete word has been matched.
            if node.is_end:
                return True

        return False

    def add_word(self, word):
        if not word:
            return

        # Reverse the word before inserting it.
        word = word[::-1]
        node = self.node

        for ch in word:
            if ch not in node.child:
                node.child[ch] = MyNode()

            node = node.child[ch]

        node.is_end = True



# V0-3
# IDEA: TRIE (gemini)
class MyNode(object):

  def __init__(self):
    self.child = {}
    self.is_end = False


class StreamChecker(object):

  def __init__(self, words):
    """:type words: List[str]"""
    self.root = MyNode()
    # 使用集合記錄當前所有進行中的 Trie 節點
    self.active_nodes = set()

    # 1. 建立「正向」Trie
    for word in words:
      node = self.root
      for w in word:
        if w not in node.child:
          node.child[w] = MyNode()
        node = node.child[w]
      node.is_end = True

  def query(self, letter):
    """:type letter: str

    :rtype: bool
    """
    next_nodes = set()
    found = False

    # 2. 每個新字元都可以作為一個新單字的起點（從根節點出發）
    if letter in self.root.child:
      nxt = self.root.child[letter]
      next_nodes.add(nxt)
      if nxt.is_end:
        found = True

    # 3. 推進所有現有的活躍路徑
    for node in self.active_nodes:
      if letter in node.child:
        nxt = node.child[letter]
        next_nodes.add(nxt)
        if nxt.is_end:
          found = True

    # 更新下一次的活躍節點集合
    self.active_nodes = next_nodes
    return found



# V0-5
# IDEA : TRIE OF REVERSED WORDS + WALK THE STREAM BACKWARD (claude)
#
#   a query asks "does the stream END with some word?" -> that is a SUFFIX test.
#   a trie answers PREFIX tests, so reverse both sides:
#     store every word reversed, and read the stream from its newest char back.
#   a suffix of the stream matches a word  <=>  the reversed suffix is a path
#   from the root that ends on an is_end node.
#
#   only the last max_len chars can ever be part of a match, so the history is
#   capped at max_len -> memory stays bounded however long the stream runs.
#
#   e.g. words = ["cd", "f", "kl"] -> trie holds "dc", "f", "lk"
#        stream "abcd" -> read back: d -> c (is_end) -> True
#
# time = ctor O(t), query O(l), space = O(t + l)
#   (t = total chars in words, l = longest word)
from collections import deque


class TrieNode(object):
    def __init__(self):
        # letter -> child node
        self.children = {}
        # True if a (reversed) word ends exactly here
        self.is_end = False


class StreamChecker(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        self.root = TrieNode()
        max_len = 0

        for word in words:
            max_len = max(max_len, len(word))

            # insert the word REVERSED: last char first
            node = self.root
            for i in range(len(word) - 1, -1, -1):
                ch = word[i]
                if ch not in node.children:
                    node.children[ch] = TrieNode()
                node = node.children[ch]
            node.is_end = True

        # NOTE !!! keep only the last max_len chars; older ones can never
        #          be inside a matching suffix. deque(maxlen=...) drops the
        #          oldest char automatically when a new one is appended.
        self.history = deque(maxlen=max_len)

    def query(self, letter):
        """
        :type letter: str
        :rtype: bool
        """
        self.history.append(letter)

        # walk from the newest char back towards the oldest one kept
        node = self.root
        for i in range(len(self.history) - 1, -1, -1):
            ch = self.history[i]

            # the path breaks: no longer suffix can match either
            if ch not in node.children:
                return False

            node = node.children[ch]

            # some word equals the suffix read so far
            if node.is_end:
                return True

        return False


# V1
# IDEA : AHO-CORASICK (FORWARD TRIE + FAILURE LINKS) (claude)
#
#   keep ONE pointer "state" = the trie node for the longest suffix of the
#   stream that is still a prefix of some word. a new letter either extends it
#   (follow the child) or, if no child exists, falls back along the failure
#   link to the next-longest such suffix and tries again.
#
#   fail[node]  = node of the longest proper suffix of node's string that is
#                 also a prefix in the trie (built level by level with BFS).
#   output[node] = True if node's string, OR any suffix of it, is a word
#                 -> output[node] |= output[fail[node]], so a query is one lookup.
#
#   e.g. words = ["abc", "bc"]; stream "ab" -> state "ab"; letter 'c' -> "abc"
#        output["abc"] is True (and would be even if only "bc" were a word)
#
# NOTE : amortised O(1) per query, but the failure links are hard to derive
#        live -> learn V0 first; this is the follow-up answer.
#
# time = ctor O(t * 26), query O(1) amortised, space = O(t)
class StreamChecker2(object):

    def __init__(self, words):
        """
        :type words: List[str]
        """
        # node 0 is the root; children[node] maps letter -> node id
        self.children = [{}]
        self.output = [False]

        # 1) build the forward trie
        for word in words:
            node = 0
            for ch in word:
                if ch not in self.children[node]:
                    self.children.append({})
                    self.output.append(False)
                    self.children[node][ch] = len(self.children) - 1
                node = self.children[node][ch]
            self.output[node] = True

        # 2) BFS from the root to set each node's failure link.
        #    a parent's link is always ready before its children's, because
        #    the link points to a strictly shorter string (a lower level).
        self.fail = [0] * len(self.children)
        queue = deque()
        for ch in self.children[0]:
            child = self.children[0][ch]
            self.fail[child] = 0    # a single char's only proper suffix is ""
            queue.append(child)

        while queue:
            node = queue.popleft()
            for ch in self.children[node]:
                child = self.children[node][ch]

                # longest suffix of node's string that can be extended by ch
                fallback = self.fail[node]
                while fallback != 0 and ch not in self.children[fallback]:
                    fallback = self.fail[fallback]
                if ch in self.children[fallback]:
                    self.fail[child] = self.children[fallback][ch]
                else:
                    self.fail[child] = 0

                # NOTE !!! a word ending at a suffix also ends here
                if self.output[self.fail[child]]:
                    self.output[child] = True

                queue.append(child)

        # the stream's current position in the automaton
        self.state = 0

    def query(self, letter):
        """
        :type letter: str
        :rtype: bool
        """
        # fall back until the current suffix can be extended by letter
        while self.state != 0 and letter not in self.children[self.state]:
            self.state = self.fail[self.state]

        if letter in self.children[self.state]:
            self.state = self.children[self.state][letter]
        else:
            self.state = 0

        return self.output[self.state]
