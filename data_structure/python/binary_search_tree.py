# V0 

# V1
# class Node:
#     def __init__(self, data):
#         self.left = None
#         self.right = None
#         self.value = data

# class BinarySearchTree:

#     def __init__(self):
#         #self.node = Node()
#         pass

#     def insert(self, data):
#         if self.value:
#             if data < self.value:
#                 if self.left is None:
#                     self.left = Node(data)
#                 else:
#                     self.left.insert(data)
#             elif data > self.value:
#                 if self.right is None:
#                     self.right = Node(data)
#                 else:
#                     self.right.insert(data)
#         else:
#             self.data = data

#     def lookup(self, data):
#         if self.value == data:
#             return ">>> find the value : {}".format(data)
#         elif data < self.data:
#             self.lookup(self.value.left)
#         elif data > self.data:
#             self.lookup(self.value.right)
#         else:
#             return ">>> Can't find the value {}".format(data)

# root = Node(12)
# root.insert(6)
# root.insert(14)
# root.insert(3)
# print(root.lookup(7))
# print(root.lookup(14))

# # V2
# # https://www.tutorialspoint.com/python/python_binary_search_tree.htm
# class Node:

#     def __init__(self, data):

#         self.left = None
#         self.right = None
#         self.data = data

# # Insert method to create nodes
#     def insert(self, data):

#         if self.data:
#             if data < self.data:
#                 if self.left is None:
#                     self.left = Node(data)
#                 else:
#                     self.left.insert(data)
#             elif data > self.data:
#                 if self.right is None:
#                     self.right = Node(data)
#                 else:
#                     self.right.insert(data)
#         else:
#             self.data = data
# # findval method to compare the value with nodes
#     def findval(self, lkpval):
#         if lkpval < self.data:
#             if self.left is None:
#                 return str(lkpval)+" Not Found"
#             return self.left.findval(lkpval)
#         elif lkpval > self.data:
#             if self.right is None:
#                 return str(lkpval)+" Not Found"
#             return self.right.findval(lkpval)
#         else:
#             print(str(self.data) + ' is found')
# # Print the tree
#     def PrintTree(self):
#         if self.left:
#             self.left.PrintTree()
#         print( self.data),
#         if self.right:
#             self.right.PrintTree()
# # root = Node(12)
# # root.insert(6)
# # root.insert(14)
# # root.insert(3)
# # print(root.findval(7))
# # print(root.findval(14))


# # V3 
# # https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Trees/BinarySearchTree.py
# class Node(object):
#     def __init__(self, data):
#         self.data = data
#         self.leftChild = None
#         self.rightChild = None

#     def insert(self, data):
#         ''' For inserting the data in the Tree '''
#         if self.data == data:
#             return False        # As BST cannot contain duplicate data

#         elif data < self.data:
#             ''' Data less than the root data is placed to the left of the root '''
#             if self.leftChild:
#                 return self.leftChild.insert(data)
#             else:
#                 self.leftChild = Node(data)
#                 return True

#         else:
#             ''' Data greater than the root data is placed to the right of the root '''
#             if self.rightChild:
#                 return self.rightChild.insert(data)
#             else:
#                 self.rightChild = Node(data)
#                 return True

#     def minValueNode(self, node):
#         current = node

#         # loop down to find the leftmost leaf
#         while(current.leftChild is not None):
#             current = current.leftChild

#         return current

#     def delete(self, data):
#         ''' For deleting the node '''
#         if self is None:
#             return None

#         # if current node's data is less than that of root node, then only search in left subtree else right subtree
#         if data < self.data:
#             self.leftChild = self.leftChild.delete(data)
#         elif data > self.data:
#             self.rightChild = self.rightChild.delete(data)
#         else:
#             # deleting node with one child
#             if self.leftChild is None:
#                 temp = self.rightChild
#                 self = None
#                 return temp
#             elif self.rightChild is None:
#                 temp = self.leftChild
#                 self = None
#                 return temp

#             # deleting node with two children
#             # first get the inorder successor
#             temp = self.minValueNode(self.rightChild)
#             self.data = temp.data
#             self.rightChild = self.rightChild.delete(temp.data)

#         return self

#     def find(self, data):
#         ''' This function checks whether the specified data is in tree or not '''
#         if(data == self.data):
#             return True
#         elif(data < self.data):
#             if self.leftChild:
#                 return self.leftChild.find(data)
#             else:
#                 return False
#         else:
#             if self.rightChild:
#                 return self.rightChild.find(data)
#             else:
#                 return False

#     def preorder(self):
#         '''For preorder traversal of the BST '''
#         if self:
#             print(str(self.data), end = ' ')
#             if self.leftChild:
#                 self.leftChild.preorder()
#             if self.rightChild:
#                 self.rightChild.preorder()

#     def inorder(self):
#         ''' For Inorder traversal of the BST '''
#         if self:
#             if self.leftChild:
#                 self.leftChild.inorder()
#             print(str(self.data), end = ' ')
#             if self.rightChild:
#                 self.rightChild.inorder()

#     def postorder(self):
#         ''' For postorder traversal of the BST '''
#         if self:
#             if self.leftChild:
#                 self.leftChild.postorder()
#             if self.rightChild:
#                 self.rightChild.postorder()
#             print(str(self.data), end = ' ')

# class Tree(object):
#     def __init__(self):
#         self.root = None

#     def insert(self, data):
#         if self.root:
#             return self.root.insert(data)
#         else:
#             self.root = Node(data)
#             return True

#     def delete(self, data):
#         if self.root is not None:
#             return self.root.delete(data)

#     def find(self, data):
#         if self.root:
#             return self.root.find(data)
#         else:
#             return False

#     def preorder(self):
#         if self.root is not None:
#             print()
#             print('Preorder: ')
#             self.root.preorder()

#     def inorder(self):
#         print()
#         if self.root is not None:
#             print('Inorder: ')
#             self.root.inorder()

#     def postorder(self):
#         print()
#         if self.root is not None:
#             print('Postorder: ')
#             self.root.postorder()

# # if __name__ == '__main__':
# #     tree = Tree()
# #     tree.insert(10)
# #     tree.insert(12)
# #     tree.insert(5)
# #     tree.insert(4)
# #     tree.insert(20)
# #     tree.insert(8)
# #     tree.insert(7)
# #     tree.insert(15)
# #     tree.insert(13)
# #     print(tree.find(1))
# #     print(tree.find(12))
# #     ''' Following tree is getting created:
# #                     10
# #                  /      \
# #                5         12
# #               / \           \
# #             4     8          20
# #                  /          /
# #                 7         15
# #                          /
# #                        13
# #     '''

# #     tree.preorder()
# #     tree.inorder()
# #     tree.postorder()
# #     print('\n\nAfter deleting 20')
# #     tree.delete(20)
# #     tree.inorder()
# #     tree.preorder()
# #     print('\n\nAfter deleting 10')
# #     tree.delete(10)
# #     tree.inorder()
# #     tree.preorder()
