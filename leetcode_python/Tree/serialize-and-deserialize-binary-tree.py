"""

297. Serialize and Deserialize Binary Tree
Hard

Serialization is the process of converting a data structure or object into a sequence of bits so that it can be stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer environment.

Design an algorithm to serialize and deserialize a binary tree. There is no restriction on how your serialization/deserialization algorithm should work. You just need to ensure that a binary tree can be serialized to a string and this string can be deserialized to the original tree structure.

Clarification: The input/output format is the same as how LeetCode serializes a binary tree. You do not necessarily need to follow this format, so please be creative and come up with different approaches yourself.

 

Example 1:


Input: root = [1,2,3,null,null,4,5]
Output: [1,2,3,null,null,4,5]
Example 2:

Input: root = []
Output: []
Example 3:

Input: root = [1]
Output: [1]
Example 4:

Input: root = [1,2]
Output: [1,2]
 

Constraints:

The number of nodes in the tree is in the range [0, 104].
-1000 <= Node.val <= 1000

"""

# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root
  
# V0
# IDEA : DFS + tree property + recursive  + queue
class Codec:
    ### DFS
    def serialize(self, root):
        vals = []
        def preOrder(root):
            if not root:
                vals.append('#')
            ### NOTE : we have else logic, and put all other cases under it
            #      -> TODO : check if we can do `if elif...else`
            else:
                vals.append(str(root.val))
                preOrder(root.left)
                preOrder(root.right)
        preOrder(root)
        return ' '.join(vals)

    def deserialize(self, data):
        vals = [val for val in data.split()]
        ### NOTE : recursive
        def build():
            ### NOTE : when there is element in vals, we keep recursive running
            if vals:
                ### NOTE : vals already retrieved via `[val for val in data.split()]`
                #      -> so every time we pop its 1st element, we are able to get all if elements one by one
                #      -> then we can build the tree via recursive (root.left = build(),  root.right = build())
                val = vals.pop(0)
                if val == '#':
                    return None
                ### NOTE : we get root via current val (val = vals.popleft())
                root = TreeNode(int(val))
                ### NOTE  : root.left comes from build()
                root.left = build()
                ### NOTE  : root.right comes from build()
                root.right = build()
                ### NOTE  : we need to return root
                return root
        return build()

# V0'
# IDEA : DFS + tree property + recursive + collections.deque
import collections
class Codec:
    ### DFS
    def serialize(self, root):
        vals = []
        def preOrder(root):
            if not root:
                vals.append('#')
            ### NOTE : we have else logic, and put all other cases under it
            #      -> TODO : check if we can do `if elif...else`
            else:
                vals.append(str(root.val))
                preOrder(root.left)
                preOrder(root.right)
        preOrder(root)
        return ' '.join(vals)

    def deserialize(self, data):
        vals = collections.deque(val for val in data.split())
        ### NOTE : recursive
        def build():
            ### NOTE : when there is element in vals, we keep recursive running
            if vals:
                ### NOTE : we use popleft
                val = vals.popleft()
                if val == '#':
                    return None
                ### NOTE : we get root via current val (val = vals.popleft())
                root = TreeNode(int(val))
                ### NOTE  : root.left comes from build()
                root.left = build()
                ### NOTE  : root.right comes from build()
                root.right = build()
                ### NOTE  : we need to return root
                return root
        return build()

# V1
# IDEA : DFS
# CHECK Solution description !!!
# https://leetcode.com/problems/serialize-and-deserialize-binary-tree/solution/
# Deserialization 
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root

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

# V1
# IDEA : ASCII
# http://zxi.mytechroad.com/blog/tree/leetcode-297-serialize-and-deserialize-binary-tree/
# C++
# class Codec {
# public:
#
#     // Encodes a tree to a single string.
#     string serialize(TreeNode* root) {
#         ostringstream out;
#         serialize(root, out);
#         return out.str();
#     }
#
#     // Decodes your encoded data to tree.
#     TreeNode* deserialize(string data) {
#         istringstream in(data);
#         return deserialize(in);
#     }
# private:
#     void serialize(TreeNode* root, ostringstream& out) {
#         if (!root) {
#             out << "# ";
#             return;
#         }        
#         out << root->val << " ";
#         serialize(root->left, out);
#         serialize(root->right, out);
#     }
#    
#     TreeNode* deserialize(istringstream& in) {
#         string val;
#         in >> val;
#         if (val == "#") return nullptr;        
#         TreeNode* root = new TreeNode(stoi(val));        
#         root->left = deserialize(in);
#         root->right = deserialize(in);        
#         return root;
#     }
# };


# V1
# IDEA : BINARY
# http://zxi.mytechroad.com/blog/tree/leetcode-297-serialize-and-deserialize-binary-tree/
# C++
# class Codec {
# public:
# 
#     // Encodes a tree to a single string.
#     string serialize(TreeNode* root) {
#         ostringstream out;
#         serialize(root, out);
#         return out.str();
#     }
#
#     // Decodes your encoded data to tree.
#     TreeNode* deserialize(string data) {
#         istringstream in(data);
#         return deserialize(in);
#     }
# private:
#     enum STATUS {
#         ROOT_NULL = 0x0,
#         ROOT = 0x1,
#         LEFT = 0x2,
#         RIGHT = 0x4
#     };
#    
#     void serialize(TreeNode* root, ostringstream& out) {
#         char status = 0;
#         if (root) status |= ROOT;
#         if (root && root->left) status |= LEFT;
#         if (root && root->right) status |= RIGHT;
#         out.write(&status, sizeof(char));        
#         if (!root) return;
#         out.write(reinterpret_cast<char*>(&(root->val)), sizeof(root->val));
#         if (root->left) serialize(root->left, out);
#         if (root->right) serialize(root->right, out);
#     }
#   
#     TreeNode* deserialize(istringstream& in) {
#         char status;
#         in.read(&status, sizeof(char));
#         if (!status & ROOT) return nullptr;
#         auto root = new TreeNode(0);
#         in.read(reinterpret_cast<char*>(&root->val), sizeof(root->val));        
#         root->left = (status & LEFT) ? deserialize(in) : nullptr;
#         root->right = (status & RIGHT) ? deserialize(in) : nullptr;
#         return root;
#     }
# };

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