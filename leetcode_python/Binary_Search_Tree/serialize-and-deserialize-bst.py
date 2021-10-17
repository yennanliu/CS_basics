"""

449. Serialize and Deserialize BST
Medium

Serialization is converting a data structure or object into a sequence of bits so that it can be stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer environment.

Design an algorithm to serialize and deserialize a binary search tree. There is no restriction on how your serialization/deserialization algorithm should work. You need to ensure that a binary search tree can be serialized to a string, and this string can be deserialized to the original tree structure.

The encoded string should be as compact as possible.

 
Example 1:

Input: root = [2,1,3]
Output: [2,1,3]
Example 2:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 104].
0 <= Node.val <= 104
The input tree is guaranteed to be a binary search tree.

"""

# V0
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'
            
        p = [root]
        result = [root.val]

        while p:
            new_p = []
            for n in p:
                if n.left:
                    new_p.append(n.left)
                    result.extend([n.left.val])
                else:
                    result.extend('#')
                    
                if n.right:
                    new_p.append(n.right)
                    result.extend([n.right.val])
                else:
                    result.extend('#')

            p = new_p
        
        while result and result[-1] == '#':
            result.pop()
     
        return '{' + ','.join(map(str, result)) + '}' 

    def deserialize(self, data):
        if data == '{}':
            return 

        nodes = collections.deque([TreeNode(n) for n in data[1:-1].split(',')])
        root = nodes.popleft()
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.popleft()
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.popleft()
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V0 
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None

# V1'
# IDEA : BST property
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/212043/Python-solution
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        def dfs(root):
            if not root:
                return 
            res.append(str(root.val) + ",")
            dfs(root.left)
            dfs(root.right)
            
        res = []
        dfs(root)
        return "".join(res)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        lst = data.split(",")
        lst.pop()
        stack = []
        head = None
        for n in lst:
            n = int(n)
            if not head:
                head = TreeNode(n)
                stack.append(head)
            else:
                node = TreeNode(n)
                if n < stack[-1].val:
                    stack[-1].left = node
                else:
                    while stack and stack[-1].val < n: 
                        u = stack.pop()
                    u.right = node
                stack.append(node)
        return head

# V1''
# IDEA : BST property
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/212043/Python-solution
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        def dfs(root):
            if not root:
                return 
            res.append(str(root.val) + ",")
            dfs(root.left)
            dfs(root.right)
            
        res = []
        dfs(root)
        return "".join(res)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        def helper(arr, i, j):
            if i > j:
                return
            if i == j:
                node = TreeNode(arr[i])
                return node
            for k in range(i+1, j+1):
                if arr[k] > arr[i]:
                    l = helper(arr, i+1, k-1)
                    r = helper(arr, k, j)
                    root = TreeNode(arr[i])
                    root.left = l
                    root.right = r
                    return root
            l = helper(arr, i+1, j)
            root = TreeNode(arr[i])
            root.left = l
            return root
        
        arr = data.split(",")
        arr.pop()
        data = [int(x) for x in arr]
        return helper(data, 0, len(data)-1)

# V1''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
class Codec:
    def serialize(self, root):
        def doit(node):
            if node:
                vals.append(str(node.val))
                doit(node.left)
                doit(node.right)
            else:
                vals.append('#')
        vals = []
        doit(root)
        return ' '.join(vals)

    def deserialize(self, data):
        def doit():
            val = next(vals)
            if val == '#':
                return None
            node = TreeNode(int(val))
            node.left = doit()
            node.right = doit()
            return node
        vals = iter(data.split())
        return doit()

# V1'''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        if not root:
            return '[]'
        res = [root.val]
        q = collections.deque([root])
        while q:
            front = q.popleft()
            if front.left:
                q.append(front.left)
            if front.right:
                q.append(front.right)
            res.append(front.left.val if front.left else 'null')
            res.append(front.right.val if front.right else 'null')
        while res and res[-1] == 'null':
            res.pop()
        return '[' + ','.join(map(str, res)) + ']'

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        if data == '[]':
            return None
        nodes = collections.deque([[TreeNode(o), None][o == 'null']
                                    for o in data[1:-1].split(',')])
        q = collections.deque([nodes.popleft()]) if nodes else None
        root = q[0] if q else None
        while q:
            parent = q.popleft()
            left = nodes.popleft() if nodes else None
            right = nodes.popleft() if nodes else None
            parent.left, parent.right = left, right
            if left:
                q.append(left)
            if right:
                q.append(right)
        return root
# Your Codec object will be instantiated and called as such:
# codec = Codec()
# codec.deserialize(codec.serialize(root))

# V1'''''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
import json
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        if not root:
            return 'null'
        nodes = collections.deque([root])
        maps = collections.deque([{'v' : root.val}])
        tree = maps[0]
        while nodes:
            frontNode = nodes.popleft()
            frontMap = maps.popleft()
            if frontNode.left:
                frontMap['l'] = {'v' : frontNode.left.val}
                nodes.append(frontNode.left)
                maps.append(frontMap['l'])
            if frontNode.right:
                frontMap['r'] = {'v' : frontNode.right.val}
                nodes.append(frontNode.right)
                maps.append(frontMap['r'])
        return json.dumps(tree)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        tree = json.loads(data)
        if not tree:
            return None
        root = TreeNode(tree['v'])
        maps = collections.deque([tree])
        nodes = collections.deque([root])
        while nodes:
            frontNode = nodes.popleft()
            frontMap = maps.popleft()
            left, right = frontMap.get('l'), frontMap.get('r')
            if left:
                frontNode.left = TreeNode(left['v'])
                maps.append(left)
                nodes.append(frontNode.left)
            if right:
                frontNode.right = TreeNode(right['v'])
                maps.append(right)
                nodes.append(frontNode.right)
        return root
# Your Codec object will be instantiated and called as such:
# codec = Codec()
# codec.deserialize(codec.serialize(root))

# V1''''''
# https://www.jiuzhang.com/solution/serialize-and-deserialize-bst/#tag-highlight-lang-python
class Codec:
    def serialize(self, root):
        # write your code here
        if not root:
            return '{}'
            
        p = [root]
        result = [root.val]
        while p:
            new_p = []
            for n in p:
                if n.left:
                    new_p.append(n.left)
                    result.extend([n.left.val])
                else:
                    result.extend('#')
                    
                if n.right:
                    new_p.append(n.right)
                    result.extend([n.right.val])
                else:
                    result.extend('#')

            p = new_p
        
        while result and result[-1] == '#':
            result.pop()
     
        return '{' + ','.join(map(str, result)) + '}' 

    def deserialize(self, data):
        if data == '{}':
            return 

        nodes = collections.deque([TreeNode(n) for n in data[1:-1].split(',')])
        root = nodes.popleft()
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.popleft()
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.popleft()
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p  
        return root

# V2 
# Time:  O(n)
# Space: O(h)
import collections
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Codec(object):

    def serialize(self, root):
        """Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def serializeHelper(node, vals):
            if node:
                vals.append(node.val)
                serializeHelper(node.left, vals)
                serializeHelper(node.right, vals)

        vals = []
        serializeHelper(root, vals)

        return ' '.join(map(str, vals))


    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def deserializeHelper(minVal, maxVal, vals):
            if not vals:
                return None

            if minVal < vals[0] < maxVal:
                val = vals.popleft()
                node = TreeNode(val)
                node.left = deserializeHelper(minVal, val, vals)
                node.right = deserializeHelper(val, maxVal, vals)
                return node
            else:
                return None

        vals = collections.deque([int(val) for val in data.split()])
        return deserializeHelper(float('-inf'), float('inf'), vals)