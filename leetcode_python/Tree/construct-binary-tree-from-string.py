# V0
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
            deg += {'(' : 1, ')' : -1}.get(s[0], 0)
            part += s[0]
            s = s[1:]
            if deg == 0: break
        return part, s

### Test case : dev

# V1'
# https://www.jiuzhang.com/solution/construct-binary-tree-from-string/#tag-highlight-lang-python
# IDEA : RECURSION 
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
# Time:  O(n)
# Space: O(h)
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