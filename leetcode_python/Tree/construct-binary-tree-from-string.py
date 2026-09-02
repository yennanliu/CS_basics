# https://leetcode.ca/all/536.html

r"""

Leetcode 536. 
Construct Binary Tree from String


You need to construct a binary tree from a string consisting of parenthesis and integers.
The whole input represents a binary tree. It contains an integer followed by zero, one or two pairs of parenthesis. The integer represents the root's value and a pair of parenthesis contains a child binary tree with the same structure.
You always start to construct the left child node of the parent first if it exists.

Example:
Input: "4(2(3)(1))(6(5))"
Output: return the tree root node representing the following tree:

       4
     /   \
    2     6
   / \   / 
  3   1 5 

Note:
There will only be '(', ')', '-' and '0' ~ '9' in the input string.
An empty tree is represented by "" instead of "()"

"""

# V0
class Solution(object):
    def str2tree(self, s):
        pass


# V0-1
# IDEA: STR OP + DFS (gemini)
# NOTE: validated (neg / multi-digit / leaf / deep-chain cases all OK)
class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: Optional[TreeNode]
        """
        if not s:
            return None

        # 1. Parse the root value (handles negative sign and multi-digit numbers)
        first_paren = s.find('(')
        if first_paren == -1:
            return TreeNode(int(s))

        root_val = int(s[:first_paren])
        root = TreeNode(root_val)

        # 2. Find the closing ')' for the left child using parenthesis balance
        balance = 0
        left_end = -1
        for i in range(first_paren, len(s)):
            if s[i] == '(':
                balance += 1
            elif s[i] == ')':
                balance -= 1

            if balance == 0:
                left_end = i
                break

        # 3. Construct left child from s[first_paren + 1 : left_end]
        root.left = self.str2tree(s[first_paren + 1 : left_end])

        # 4. Construct right child (if present) from s[left_end + 2 : len(s) - 1]
        if left_end + 1 < len(s):
            root.right = self.str2tree(s[left_end + 2 : -1])

        return root


# V0-2
# IDEA: STR OP + DFS (gpt)
# NOTE: validated (neg / multi-digit / leaf / deep-chain cases all OK)
class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: Optional[TreeNode]
        """
        if not s:
            return None

        root, _ = self.helper(s, 0)
        return root

    def helper(self, s, idx):
        n = len(s)

        # Parse number, including negative numbers
        sign = 1

        if s[idx] == '-':
            sign = -1
            idx += 1

        num = 0

        while idx < n and s[idx].isdigit():
            num = num * 10 + int(s[idx])
            idx += 1

        root = TreeNode(sign * num)

        # Parse left subtree
        if idx < n and s[idx] == '(':
            root.left, idx = self.helper(s, idx + 1)

            # Skip ')'
            idx += 1

        # Parse right subtree
        if idx < n and s[idx] == '(':
            root.right, idx = self.helper(s, idx + 1)

            # Skip ')'
            idx += 1

        return root, idx


# V0
# IDEA : tree property + recursive
# time = O(n), each char processed a constant number of times
# space = O(n), recursion stack + string slicing
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None
        n = ''
        while s and s[0] not in ('(', ')'):
            n += s[0]
            s = s[1:]
        ### NOTE this
        node = TreeNode(int(n))
        ### NOTE this
        left, right = self.divide(s)
        node.left = self.str2tree(left[1:-1])
        node.right = self.str2tree(right[1:-1])
        return node

    def divide(self, s):
        part, deg = '', 0
        while s:
            """
            syntax exmaple :

            In [9]: x = {'(' : 1, ')' : -1}

            In [10]: x.get('(')
            Out[10]: 1

            In [11]: x.get('(', 0 )
            Out[11]: 1

            In [12]: x.get('&', 0 )
            Out[12]: 0
            """
            deg += {'(' : 1, ')' : -1}.get(s[0], 0)
            part += s[0]
            s = s[1:]
            if deg == 0:
                break
        return part, s

# V0'
# IDEA : tree property + recursive
# time = O(n)
# space = O(h)  # h = tree height (recursion stack); index-based, no string slicing
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: TreeNode
        """
        def str2treeHelper(s, i):
            start = i
            if s[i] == '-': i += 1
            while i < len(s) and s[i].isdigit():
                i += 1
            node = TreeNode(int(s[start:i]))
            if i < len(s) and s[i] == '(':
                i += 1
                node.left, i = str2treeHelper(s, i)
                i += 1
            if i < len(s) and s[i] == '(':
                i += 1
                node.right, i = str2treeHelper(s, i)
                i += 1
            return node, i

        return str2treeHelper(s, 0)[0] if s else None

# V0'
# time = O(n)
# space = O(n)
class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: TreeNode
        """
        if not s: return None
        n = ''
        while s and s[0] not in ('(', ')'):
            n += s[0]
            s = s[1:]
        node = TreeNode(int(n))
        left, right = self.divide(s)
        node.left = self.str2tree(left[1:-1])
        node.right = self.str2tree(right[1:-1])
        return node

    def divide(self, s):
        part, deg = '', 0
        while s:
            if s[0] == '(':
                deg += 1 
            elif s[0] == ')':
                deg += -1 
            else:
                deg += 0 
            part += s[0]
            s = s[1:]
            if deg == 0: break
        return part, s

# V1 
# http://bookshadow.com/weblog/2017/03/12/leetcode-construct-binary-tree-from-string/
# https://blog.csdn.net/magicbean2/article/details/78850694
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n)
class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: TreeNode
        """
        if not s:
            return None
        n = ''
        while s and s[0] not in ('(', ')'):
            n += s[0]
            s = s[1:]
        node = TreeNode(int(n))
        left, right = self.divide(s)
        node.left = self.str2tree(left[1:-1])
        node.right = self.str2tree(right[1:-1])
        return node

    def divide(self, s):
        part, deg = '', 0
        while s:
            """
            syntax exmaple :

            In [9]: x = {'(' : 1, ')' : -1}

            In [10]: x.get('(')
            Out[10]: 1

            In [11]: x.get('(', 0 )
            Out[11]: 1

            In [12]: x.get('&', 0 )
            Out[12]: 0
            """
            deg += {'(' : 1, ')' : -1}.get(s[0], 0)
            part += s[0]
            s = s[1:]
            if deg == 0:
                break
        return part, s

### Test case : dev

# V1'
# https://www.jiuzhang.com/solution/construct-binary-tree-from-string/#tag-highlight-lang-python
# IDEA : RECURSION
# time = O(n)
# space = O(n)
class Solution:
    """
    @param s: a string
    @return: return a TreeNode
    """

    def str2tree(self, s):
        self.idx = 0
        self.len = len(s)
        if(self.len == 0):
            return None
        root = self.dfs(s)
        return root

    def dfs(self, s):
        if(self.idx >= self.len):
            return None
        sig, k = 1, 0
        if(s[self.idx] == '-'):
            sig = -1
            self.idx += 1
        while(self.idx < self.len and s[self.idx] >= '0' and s[self.idx] <= '9'):
            k = k * 10 + ord(s[self.idx]) - ord('0')
            self.idx += 1
        root = TreeNode(sig * k)
        if(self.idx >= self.len or s[self.idx] == ')'):
            self.idx += 1
            return root
        self.idx += 1
        root.left = self.dfs(s)
        if(self.idx >= self.len or s[self.idx] == ')'):
            self.idx += 1
            return root
        self.idx += 1
        root.right = self.dfs(s)
        if(self.idx >= self.len or s[self.idx] == ')'):
            self.idx += 1
            return root
        return root

# V1''
# time = O(n^2), findIndex scans a range for each node
# space = O(n)
# https://www.geeksforgeeks.org/construct-binary-tree-string-bracket-representation/
# Python3 program to conStruct a 
# binary tree from the given String 

# Helper class that allocates a new node 
class newNode: 
    def __init__(self, data): 
        self.data = data 
        self.left = self.right = None

# This funtcion is here just to test 
def preOrder(node): 
    if (node == None): 
        return
    print(node.data, end = " ") 
    preOrder(node.left) 
    preOrder(node.right) 

# function to return the index of 
# close parenthesis 
def findIndex(Str, si, ei): 
    if (si > ei): 
        return -1

    # Inbuilt stack 
    s = [] 
    for i in range(si, ei + 1): 

        # if open parenthesis, push it 
        if (Str[i] == '('): 
            s.append(Str[i]) 

        # if close parenthesis 
        elif (Str[i] == ')'): 
            if (s[-1] == '('): 
                s.pop(-1) 

                # if stack is empty, this is 
                # the required index 
                if len(s) == 0: 
                    return i 
    # if not found return -1 
    return -1

# function to conStruct tree from String 
def treeFromString(Str, si, ei): 
    
    # Base case 
    if (si > ei): 
        return None

    # new root 
    root = newNode(ord(Str[si]) - ord('0')) 
    index = -1

    # if next char is '(' find the 
    # index of its complement ')' 
    if (si + 1 <= ei and Str[si + 1] == '('): 
        index = findIndex(Str, si + 1, ei) 

    # if index found 
    if (index != -1): 

        # call for left subtree 
        root.left = treeFromString(Str, si + 2, index - 1) 

        # call for right subtree 
        root.right = treeFromString(Str, index + 2, ei - 1) 
    return root 

# V2
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/construct-binary-tree-from-string.py
# time = O(n)
# space = O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def str2tree(self, s):
        """
        :type s: str
        :rtype: TreeNode
        """
        def str2treeHelper(s, i):
            start = i
            if s[i] == '-': i += 1
            while i < len(s) and s[i].isdigit(): i += 1
            node = TreeNode(int(s[start:i]))
            if i < len(s) and s[i] == '(':
                i += 1
                node.left, i = str2treeHelper(s, i)
                i += 1
            if i < len(s) and s[i] == '(':
                i += 1
                node.right, i = str2treeHelper(s, i)
                i += 1
            return node, i

        return str2treeHelper(s, 0)[0] if s else None


# V3
# IDEA : ITERATIVE + STACK (one pass, no recursion) - easiest to trace by hand
#
#  The string is just a "walk" over the tree. Read it left to right :
#
#     a number -> make a node, hang it under the node on TOP of the stack,
#                 then PUSH it (we are now "inside" this node)
#     '('      -> we are about to enter a child   -> nothing to do
#     ')'      -> that child is finished          -> POP
#
#  Trace on s = "4(2(3)(1))(6(5))" :
#
#     char   action                       stack (bottom -> top)
#     4      new node 4 (root)            [4]
#     (      -                            [4]
#     2      4.left  = 2, push            [4,2]
#     (      -                            [4,2]
#     3      2.left  = 3, push            [4,2,3]
#     )      pop                          [4,2]
#     (      -                            [4,2]
#     1      2.right = 1, push            [4,2,1]
#     )      pop                          [4,2]
#     )      pop                          [4]
#     (      -                            [4]
#     6      4.right = 6, push            [4,6]
#     (      -                            [4,6]
#     5      6.left  = 5, push            [4,6,5]
#     )      pop                          [4,6]
#     )      pop                          [4]
#                                         -> stack[0] = root  ✅
#
#  NOTE : the root has NO surrounding '()', so it is never popped
#         -> exactly one node is left on the stack at the end = the root
#
# time = O(n)   # each char is looked at once
# space = O(h)  # h = tree height (the stack)
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None

        stack = []
        i = 0

        while i < len(s):
            if s[i] == '(':
                # just a separator -> the real work happens on the next number
                i += 1
            elif s[i] == ')':
                # current subtree is done -> go back to the parent
                stack.pop()
                i += 1
            else:
                # a number (maybe negative / multi-digit)
                val, i = self.helper(s, i)
                node = TreeNode(val)
                if stack:
                    self.helper_attach(stack[-1], node)
                stack.append(node)

        return stack[0]

    """
    NOTE !!!

    read ONE number starting at index i, and return (value, next_index)

     - a leading '-' is part of the number  -> "-4" is -4, NOT 4
     - keep eating while the char is a digit -> "42" is 42, NOT 4 then 2
    """
    def helper(self, s, i):
        start = i
        if s[i] == '-':
            i += 1
        while i < len(s) and s[i].isdigit():
            i += 1
        return int(s[start:i]), i

    """
    NOTE !!!

    "You always start to construct the LEFT child first if it exists"
    -> so the 1st child we meet is the left one, the 2nd is the right one.
    -> and the input can NEVER be "4()(6)" (an empty tree is "" , not "()" ),
       so "left is still None" always means "this child is the left one".
    """
    def helper_attach(self, parent, child):
        if parent.left is None:
            parent.left = child
        else:
            parent.right = child


# V3'
# IDEA : TOKENIZE first, THEN recursive descent (split the 2 hard things apart)
#
#  The messy part of this problem is doing 2 jobs at once :
#     job A : turn "-42" into the number -42       (character level)
#     job B : figure out the tree shape from ()    (structure level)
#
#  -> so do them SEPARATELY :
#
#     "4(2(3)(1))(6(5))"
#        --helper_tokenize-->  [4,'(',2,'(',3,')','(',1,')',')','(',6,'(',5,')',')']
#        --helper----------->  the tree
#
#  Once tokenized, the grammar is tiny and the recursion reads like the rule :
#
#     node := NUMBER [ '(' node ')' ]  [ '(' node ')' ]
#              ^^^^^^  ^^^^^^^^^^^^^    ^^^^^^^^^^^^^
#              value      left             right
#
# time = O(n)
# space = O(n)  # the token list + recursion stack
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None

        tokens = self.helper_tokenize(s)
        self.idx = 0          # where we are in the token list
        return self.helper(tokens)

    # job A : characters -> tokens ( int | '(' | ')' )
    def helper_tokenize(self, s):
        tokens = []
        i = 0
        while i < len(s):
            if s[i] in ('(', ')'):
                tokens.append(s[i])
                i += 1
            else:
                start = i
                if s[i] == '-':
                    i += 1
                while i < len(s) and s[i].isdigit():
                    i += 1
                tokens.append(int(s[start:i]))
        return tokens

    """
    NOTE !!!

    `self.idx` is SHARED by all the recursive calls (it is the "read cursor").
    every call must leave it pointing JUST AFTER the subtree it consumed,
    otherwise the parent will read the wrong token.
    """
    # job B : tokens -> tree
    def helper(self, tokens):
        # the current token is always the node's value
        node = TreeNode(tokens[self.idx])
        self.idx += 1

        # optional LEFT child : '(' ... ')'
        if self.idx < len(tokens) and tokens[self.idx] == '(':
            self.idx += 1                    # eat '('
            node.left = self.helper(tokens)
            self.idx += 1                    # eat ')'

        # optional RIGHT child : '(' ... ')'
        if self.idx < len(tokens) and tokens[self.idx] == '(':
            self.idx += 1                    # eat '('
            node.right = self.helper(tokens)
            self.idx += 1                    # eat ')'

        return node


# V3''
# IDEA : DIVIDE & CONQUER on a [lo, hi] RANGE (no string slicing)
#
#  Same "cut the string into root / left / right" idea as V0-1, but we pass
#  INDEXES (lo, hi) instead of building new substrings with s[a:b].
#  -> no O(n) copy per call, and it is easy to see which part belongs to whom.
#
#     s = "4(2(3)(1))(6(5))"
#          0123456789...
#
#          4  ( 2(3)(1) ) ( 6(5) )
#          ^   ^^^^^^^^     ^^^^
#        root    left       right
#
#  the ONLY tricky bit is "where does the left subtree end ?"
#  -> helper_find_close() walks with a counter :  '(' +1 , ')' -1
#     the index where the counter hits 0 is the MATCHING ')'
#
# time = O(n * h)  # h = tree height (find_close re-scans its own range)
# space = O(h)
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None
        return self.helper(s, 0, len(s) - 1)

    # build the tree described by s[lo .. hi] (both ends included)
    def helper(self, s, lo, hi):
        if lo > hi:
            return None

        # 1) the number at the front is the root
        j = lo
        if s[j] == '-':
            j += 1
        while j <= hi and s[j].isdigit():
            j += 1
        node = TreeNode(int(s[lo:j]))

        # no '(' after the number -> it is a leaf
        if j > hi:
            return node

        # 2) LEFT child lives inside the 1st (...) block -> strip the parens
        left_close = self.helper_find_close(s, j, hi)
        node.left = self.helper(s, j + 1, left_close - 1)

        # 3) RIGHT child lives inside the 2nd (...) block, if there is one
        if left_close + 1 <= hi:
            right_close = self.helper_find_close(s, left_close + 1, hi)
            node.right = self.helper(s, left_close + 2, right_close - 1)

        return node

    """
    NOTE !!!

    we can NOT just look for the next ')' :
    in "(2(3)(1))" the first ')' at idx 4 closes the INNER "(3)", not the block.

    -> so count the depth :  '(' -> +1 , ')' -> -1
       and stop at the FIRST index where depth is back to 0.
    """
    def helper_find_close(self, s, open_idx, hi):
        depth = 0
        for i in range(open_idx, hi + 1):
            if s[i] == '(':
                depth += 1
            elif s[i] == ')':
                depth -= 1
                if depth == 0:
                    return i
        return -1
