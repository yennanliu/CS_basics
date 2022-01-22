#---------------------------------------------------------------
# TREE
#---------------------------------------------------------------

# # build a tree 
# # https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Trees/Tree.py


# ##################################################################################################################################
# # OP on tree 
# # Hint   
# # https://algorithm.yuanbin.me/zh-tw/basics_data_structure/binary_tree.html
# #
# #
# # 1) Pre-order :  root -> left -> right 
# # 2) In-order :   left -> root -> right 
# # 3) Post-order :  left -> right -> root 
# # 4) Breadth first (BFS) :  layer 0 -> layer 1 -> ....layer N 
# #
# #
# ##################################################################################################################################



# # --------------------------------------------------------------
# # CREARTE A TREE 
# class Node(object):
#     def __init__(self, data = None):
#         self.left = None
#         self.right = None
#         self.data = data

#     # for setting left node
#     def setLeft(self, node):
#         self.left = node

#     # for setting right node
#     def setRight(self, node):
#         self.right = node

#     # for getting the left node
#     def getLeft(self):
#         return self.left

#     # for getting right node
#     def getRight(self):
#         return self.right

#     # for setting data of a node
#     def setData(self, data):
#         self.data = data

#     # for getting data of a node
#     def getData(self):
#         return self.data

# # OP ON TREE 
# # in this we traverse first to the leftmost node, then print its data and then traverse for rightmost node
# def inorder(Tree):
#     if Tree:
#         inorder(Tree.getLeft())
#         print(Tree.getData(), end = ' ')
#         inorder(Tree.getRight())
#     return

# # in this we first print the root node and then traverse towards leftmost node and then to the rightmost node
# def preorder(Tree):
#     if Tree:
#         print(Tree.getData(), end = ' ')
#         preorder(Tree.getLeft())
#         preorder(Tree.getRight())
#     return

# # in this we first traverse to the leftmost node and then to the rightmost node and then print the data
# def postorder(Tree):
#     if Tree:
#         postorder(Tree.getLeft())
#         postorder(Tree.getRight())
#         print(Tree.getData(), end = ' ')
#     return


# # --------------------------------------------------------------

# if __name__ == '__main__':

#     # PART 1) : CREATE A TREE 
#     root = Node(1)
#     root.setLeft(Node(2))
#     root.setRight(Node(3))
#     root.left.setLeft(Node(4))

#     ''' This will a create like this 
#                     1
#                 /       \
#             2            3
#         /
#     4
#     '''

#     # PART 2) OP ON TREE :   Inorder/Preorder/Postorder
#     print('Inorder  Traversal:')
#     inorder(root)
#     print('\nPreorder Traversal:')
#     preorder(root)
#     print('\nPostorder Traversal:')
#     postorder(root)

#     # OUTPUT:
#     # Inorder  Traversal:   root.getLeft().getLeft().getData() -> root.getLeft().getData() -> root.getData() -> root.getRight().getData()
#     # 4 2 1 3
#     # Preorder Traversal:   root.getData() -> root.getLeft().getData() -> root.getLeft().getLeft().getData() -> root.getRight().getData()
#     # 1 2 4 3
#     # Postorder Traversal: root.getLeft().getLeft().getData() -> root.getLeft().getData() -> root.getRight().getData() -> root.getData()
#     # 4 2 3 1


# V1 
# https://stackoverflow.com/questions/2358045/how-can-i-implement-a-tree-in-python
class Node:
    """
    Class Node
    """
    def __init__(self, value):
        self.left = None
        self.data = value
        self.right = None

class Tree:
    """
    Class tree will provide a tree as well as utility functions.
    """
    def createNode(self, data):
        """
        Utility function to create a node.
        """
        return Node(data)

    def insert(self, node , data):
        """
        Insert function will insert a node into tree.
        Duplicate keys are not allowed.
        """
        #if tree is empty , return a root node
        if node is None:
            return self.createNode(data)
        # if data is smaller than parent , insert it into left side
        if data < node.data:
            node.left = self.insert(node.left, data)
        elif data > node.data:
            node.right = self.insert(node.right, data)

        return node

    def search(self, node, data):
        """
        Search function will search a node into tree.
        """
        # if root is None or root is the search data.
        if node is None or node.data == data:
            return node

        if node.data < data:
            return self.search(node.right, data)
        else:
            return self.search(node.left, data)

    def deleteNode(self,node,data):
        """
        Delete function will delete a node into tree.
        Not complete , may need some more scenarion that we can handle
        Now it is handling only leaf.
        """

        # Check if tree is empty.
        if node is None:
            return None

        # searching key into BST.
        if data < node.data:
            node.left = self.deleteNode(node.left, data)
        elif data > node.data:
            node.right = self.deleteNode(node.right, data)
        else: # reach to the node that need to delete from BST.
            if node.left is None and node.right is None:
                del node
            if node.left == None:
                temp = node.right
                del node
                return  temp
            elif node.right == None:
                temp = node.left
                del node
                return temp
        return node

    def traverseInorder(self, root):
        """
        traverse function will print all the node in the tree.
        """
        if root is not None:
            self.traverseInorder(root.left)
            print root.data
            self.traverseInorder(root.right)

    def traversePreorder(self, root):
        """
        traverse function will print all the node in the tree.
        """
        if root is not None:
            print root.data
            self.traversePreorder(root.left)
            self.traversePreorder(root.right)

    def traversePostorder(self, root):
        """
        traverse function will print all the node in the tree.
        """
        if root is not None:
            self.traversePostorder(root.left)
            self.traversePostorder(root.right)
            print root.data

def main():
    root = None
    tree = Tree()
    root = tree.insert(root, 10)
    print root
    tree.insert(root, 20)
    tree.insert(root, 30)
    tree.insert(root, 40)
    tree.insert(root, 70)
    tree.insert(root, 60)
    tree.insert(root, 80)

    print ("Traverse Inorder")
    tree.traverseInorder(root)

    print ("Traverse Preorder")
    tree.traversePreorder(root)

    print ("Traverse Postorder")
    tree.traversePostorder(root)

# if __name__ == "__main__":
#     main()
