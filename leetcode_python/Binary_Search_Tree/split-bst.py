# V0
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root

# V0'
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        elif root.val > V:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root

# V0''
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        left, right = self.splitBST(root.right, V)
        root.right = left
        return [root, right]

# V1 
# http://bookshadow.com/weblog/2018/02/04/leetcode-split-bst/
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: TreeNode
        :type V: int
        :rtype: List[TreeNode]
        """
        if not root: return [None, None]
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        left, right = self.splitBST(root.right, V)
        root.right = left
        return [root, right]

### Test case : dev 

# V1'
# https://blog.csdn.net/magicbean2/article/details/79679927
# JAVA
# /**
#  * Definition for a binary tree node.
#  * struct TreeNode {
#  *     int val;
#  *     TreeNode *left;
#  *     TreeNode *right;
#  *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
#  * };
#  */
# class Solution {
# public:
#     vector<TreeNode*> splitBST(TreeNode* root, int V) {
#         vector<TreeNode *> res(2, NULL);
#         if(root == NULL) {
#             return res;
#         }
#         if(root->val > V) {         // the right child is retained
#             res[1] = root;
#             auto res1 = splitBST(root->left, V);
#             root->left = res1[1];
#             res[0]=res1[0];
#         }
#         else {                      // the left child is retained
#             res[0] = root;
#             auto res1 = splitBST(root->right, V);
#             root->right = res1[0];
#             res[1] = res1[1];
#         }
#         return res;
#     }
# };

# V1''
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
# C++
# class Solution {
# public:
#     vector<TreeNode*> splitBST(TreeNode* root, int V) {
#         vector<TreeNode*> res{NULL, NULL};
#         if (!root) return res;
#         if (root->val <= V) {
#             res = splitBST(root->right, V);
#             root->right = res[0];
#             res[0] = root;
#         } else {
#             res = splitBST(root->left, V);
#             root->left = res[1];
#             res[1] = root;
#         }
#         return res;
#     }
# };

# V1'''
# https://www.acwing.com/solution/LeetCode/content/208/
# IDEA : JAVA
# class Solution {
#     public TreeNode[] splitBST(TreeNode root, int V) {
#         TreeNode[] ans = new TreeNode[2];
#         return dfs(root, V);
#     }

#     public TreeNode[] dfs(TreeNode node, int v) {
#         TreeNode[] ans = new TreeNode[2];
#         if(node==null) return ans;

#         if(node.val>v) {
#             ans[1] = node;
#             TreeNode left = node.left;
#             node.left = null;
#             TreeNode[] nodes = dfs(left, v);
#             node.left = nodes[1];
#             ans[0] = nodes[0];
#         }else {
#             ans[0] = node;
#             TreeNode right = node.right;
#             node.right = null;
#             TreeNode[] nodes = dfs(right, v);
#             node.right = nodes[0];
#             ans[1]=nodes[1];
#         }
#         return ans;
#     }
#
# }

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def splitBST(self, root, V):
        """
        :type root: TreeNode
        :type V: int
        :rtype: List[TreeNode]
        """
        if not root:
            return None, None
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root