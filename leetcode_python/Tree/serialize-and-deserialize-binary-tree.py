# V0
import collections
class Codec:
    def serialize(self, root):
        vals = []
        def preOrder(root):
            if not root:
                vals.append('#')
            else:
                vals.append(str(root.val))
                preOrder(root.left)
                preOrder(root.right)
        preOrder(root)
        return ' '.join(vals)

    def deserialize(self, data):
        vals = collections.deque(val for val in data.split())
        def build():
            if vals:
                val = vals.popleft()
                if val == '#':
                    return None
                root = TreeNode(int(val))
                root.left = build()
                root.right = build()
                return root
        return build()

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79571892
# IDEA : DEQUE (collections.deque)
# Deque DEMO 
# -> A double-ended queue, or deque, supports adding and removing elements from either end. The more commonly used stacks and queues are degenerate forms of deques, where the inputs and outputs are restricted to a single end.
# https://pymotw.com/2/collections/deque.html
#
# In [30]: import collections
#     ...: 
#     ...: d = collections.deque('abcdefg')
#     ...: print (d)
#     ...: print (len(d))
#     ...: print ('left end :', d[0])
#     ...: print ('right end :', d[-1])
#     ...: print ('pop :' , d.pop())
#     ...: print (d)
#     ...: print ('pop_left:' , d.popleft())
#     ...: print (d)
#     ...: 
# deque(['a', 'b', 'c', 'd', 'e', 'f', 'g'])
# 7
# left end : a
# right end : g
# pop : g
# deque(['a', 'b', 'c', 'd', 'e', 'f'])
# pop_left: a
# deque(['b', 'c', 'd', 'e', 'f'])
import collections
class Codec:
    def serialize(self, root):
        """Encodes a tree to a single string.

        :type root: TreeNode
        :rtype: str
        """
        vals = []
        def preOrder(root):
            if not root:
                vals.append('#')
            else:
                vals.append(str(root.val))
                preOrder(root.left)
                preOrder(root.right)
        preOrder(root)
        return ' '.join(vals)

    def deserialize(self, data):
        """Decodes your encoded data to tree.

        :type data: str
        :rtype: TreeNode
        """
        vals = collections.deque(val for val in data.split())
        def build():
            if vals:
                val = vals.popleft()
                if val == '#':
                    return None
                root = TreeNode(int(val))
                root.left = build()
                root.right = build()
                return root
        return build()

### Test case : dev

# V1'
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# IDEA : BINARY TREE TRANVERSE
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

# V1''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# IDEA : BINARY TREE TRANVERSE (BY LAYER)
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

# V1'''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# IDEA : JSON + DICT
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

# V1''''
# http://bookshadow.com/weblog/2015/10/26/leetcode-serialize-and-deserialize-binary-tree/
# IDEA : JSON + TUPLE 
import json
class Codec:
    def serialize(self, root):
        def tuplify(root):
            return root and (root.val, tuplify(root.left), tuplify(root.right))
        return json.dumps(tuplify(root))

    def deserialize(self, data):
        def detuplify(t):
            if t:
                root = TreeNode(t[0])
                root.left = detuplify(t[1])
                root.right = detuplify(t[2])
                return root
        return detuplify(json.loads(data))

# V1'''''
# https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/74259/Recursive-preorder-Python-and-C%2B%2B-O(n)
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

# V1''''''
# IDEA : BFS 
# https://leetcode.com/problems/serialize-and-deserialize-binary-tree/discuss/166904/Python-or-BFS-tm
class Codec:
    def serialize(self, root):    
        if not root: return ""
        q = collections.deque([root])
        res = []
        while q:
            node = q.popleft()
            if node:
                q.append(node.left)
                q.append(node.right)
            res.append(str(node.val) if node else '#')
        return ','.join(res)
                
    def deserialize (self, data):
        if not data: return None
        nodes = data.split(',')
        root = TreeNode(int(nodes[0]))
        q = collections.deque([root])
        index = 1
        while q:
            node = q.popleft()
            if nodes[index] is not '#':
                node.left = TreeNode(int(nodes[index]))
                q.append(node.left)
            index += 1
        
            if nodes[index] is not '#':
                node.right = TreeNode(int(nodes[index]))
                q.append(node.right)
            index += 1
        return root

# V2