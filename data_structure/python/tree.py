

#################################################################
# DATA STRUCTURE DEMO : Tree 
#################################################################
# build a tree 
# https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Trees/Tree.py


##################################################################################################################################
# OP on tree 
# Hint   
# https://algorithm.yuanbin.me/zh-tw/basics_data_structure/binary_tree.html
#
#
# 1) Pre-order :  root -> left -> right 
# 2) In-order :   left -> root -> right 
# 3) Post-order :  left -> right -> root 
# 4) Breadth first (BFS) :  layer 0 -> layer 1 -> ....layer N 
#
#
##################################################################################################################################



# --------------------------------------------------------------
# CREARTE A TREE 
class Node(object):
    def __init__(self, data = None):
        self.left = None
        self.right = None
        self.data = data

    # for setting left node
    def setLeft(self, node):
        self.left = node

    # for setting right node
    def setRight(self, node):
        self.right = node

    # for getting the left node
    def getLeft(self):
        return self.left

    # for getting right node
    def getRight(self):
        return self.right

    # for setting data of a node
    def setData(self, data):
        self.data = data

    # for getting data of a node
    def getData(self):
        return self.data

# OP ON TREE 
# in this we traverse first to the leftmost node, then print its data and then traverse for rightmost node
def inorder(Tree):
    if Tree:
        inorder(Tree.getLeft())
        print(Tree.getData(), end = ' ')
        inorder(Tree.getRight())
    return

# in this we first print the root node and then traverse towards leftmost node and then to the rightmost node
def preorder(Tree):
    if Tree:
        print(Tree.getData(), end = ' ')
        preorder(Tree.getLeft())
        preorder(Tree.getRight())
    return

# in this we first traverse to the leftmost node and then to the rightmost node and then print the data
def postorder(Tree):
    if Tree:
        postorder(Tree.getLeft())
        postorder(Tree.getRight())
        print(Tree.getData(), end = ' ')
    return


# --------------------------------------------------------------

if __name__ == '__main__':

    # PART 1) : CREATE A TREE 
    root = Node(1)
    root.setLeft(Node(2))
    root.setRight(Node(3))
    root.left.setLeft(Node(4))

    ''' This will a create like this 
                    1
                /       \
            2            3
        /
    4
    '''

    # PART 2) OP ON TREE :   Inorder/Preorder/Postorder
    print('Inorder  Traversal:')
    inorder(root)
    print('\nPreorder Traversal:')
    preorder(root)
    print('\nPostorder Traversal:')
    postorder(root)

    # OUTPUT:
    # Inorder  Traversal:   root.getLeft().getLeft().getData() -> root.getLeft().getData() -> root.getData() -> root.getRight().getData()
    # 4 2 1 3
    # Preorder Traversal:   root.getData() -> root.getLeft().getData() -> root.getLeft().getLeft().getData() -> root.getRight().getData()
    # 1 2 4 3
    # Postorder Traversal: root.getLeft().getLeft().getData() -> root.getLeft().getData() -> root.getRight().getData() -> root.getData()
    # 4 2 3 1


