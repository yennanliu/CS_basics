# V0 

# V1
# https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
class Node: 
    def __init__(self,data=None): 
        self.left = None
        self.right = None
        self.val = data 

# A utility function to insert a new node with the given key 
def insert(root,node): 
    if root is None: 
        root = node 
    else: 
        if root.val < node.val: 
            if root.right is None: 
                root.right = node 
            else: 
                insert(root.right, node) 
        else: 
            if root.left is None: 
                root.left = node 
            else: 
                insert(root.left, node) 

# A utility function to do inorder tree traversal 
def inorder(root): 
    if root: 
        inorder(root.left) 
        print(root.val) 
        inorder(root.right) 


# r = Node(10) 
# insert(r,Node(30)) 
# insert(r,Node(20)) 
# insert(r,Node(40)) 
# insert(r,Node(70)) 
# insert(r,Node(60)) 
# insert(r,Node(80)) 
# inorder(r) 
