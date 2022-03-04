"""

1339. Maximum Product of Splitted Binary Tree
Medium

Given the root of a binary tree, split the binary tree into two subtrees by removing one edge such that the product of the sums of the subtrees is maximized.

Return the maximum product of the sums of the two subtrees. Since the answer may be too large, return it modulo 109 + 7.

Note that you need to maximize the answer before taking the mod and not after taking it.

 

Example 1:


Input: root = [1,2,3,4,5,6]
Output: 110
Explanation: Remove the red edge and get 2 binary trees with sum 11 and 10. Their product is 110 (11*10)
Example 2:


Input: root = [1,null,2,3,4,null,null,5,6]
Output: 90
Explanation: Remove the red edge and get 2 binary trees with sum 15 and 6.Their product is 90 (15*6)
 

Constraints:

The number of nodes in the tree is in the range [2, 5 * 104].
1 <= Node.val <= 104

"""

# V0
# IDEA : DFS
class Solution(object):
    def maxProduct(self, root):
        all_sums = []

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            total_sum = left_sum + right_sum + subroot.val
            all_sums.append(total_sum)
            return total_sum

        total = tree_sum(root)
        best = 0
        for s in all_sums:
            best = max(best, s * (total - s))   
        return best % (10 ** 9 + 7)

# V1
# IDEA : DFS
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/discuss/496817/Python-8-lines
class Solution(object):
    def maxProduct(self, root):
        sums = []
        def sum(root):
            if not root:
                return 0
            sums.append(root.val + sum(root.left) + sum(root.right))
            return sums[-1]
        sum(root)
        return max(sum * (sums[-1] - sum) for sum in sums) % (10**9 + 7)

# V1
# IDEA : ONE PASS DFS
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/solution/
class Solution(object):
    def maxProduct(self, root):
        all_sums = []

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            total_sum = left_sum + right_sum + subroot.val
            all_sums.append(total_sum)
            return total_sum

        total = tree_sum(root)
        best = 0
        for s in all_sums:
            best = max(best, s * (total - s))   
        return best % (10 ** 9 + 7)

# V1
# IDEA : TWO PASS DFS
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/solution/
class Solution(object):
    def maxProduct(self, root):

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            return left_sum + right_sum + subroot.val

        def maximum_product(subroot, total):
            best = 0
            def recursive_helper(subroot):
                nonlocal best
                if subroot is None: return 0
                left_sum = recursive_helper(subroot.left)
                right_sum = recursive_helper(subroot.right)
                total_sum = left_sum + right_sum + subroot.val
                product = total_sum * (tree_total_sum - total_sum)
                best = max(best, product)
                return total_sum
            recursive_helper(subroot)
            return best

        tree_total_sum = tree_sum(root)
        return maximum_product(root, tree_total_sum) % (10 ** 9 + 7)

# V1
# IDEA : DFS
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/discuss/768608/Python-solution
class Solution:
    def maxProduct(self, root):
        MOD = 1000000007
        subtree_sum = []
        def dfs(root):
            if not root:
                return 0
            l = dfs(root.left)
            r = dfs(root.right)
            summ = l + r + root.val
            subtree_sum.append(summ)
            return summ
        
        total_sum = dfs(root)
        max_prod = -float('inf')
        for summ in subtree_sum:
            max_prod = max(max_prod, (total_sum - summ) * summ)
        return max_prod % MOD

# V1
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/discuss/1040798/Python
class Solution(object):
    def maxProduct(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        self.total = 0        
        def subtree(root):
            if not root: return 0
            left = subtree(root.left)
            right = subtree(root.right)
            cur_sub = root.val+left+right
            self.res = max(self.res, (self.total-cur_sub)*cur_sub)
            return cur_sub
        
        self.res = -sys.maxsize
        self.total = subtree(root)
        subtree(root)
        return self.res % (10**9 + 7)

# V1
# IDEA : Advanced Strategies for Dealing with 32-Bit Integers
# https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/solution/
# JAVA
# /**
#  * Definition for a binary tree node.
#  * public class TreeNode {
#  *     int val;
#  *     TreeNode left;
#  *     TreeNode right;
#  *     TreeNode(int x) { val = x; }
#  * }
#  */
# class Solution {
#
#     private static final int MOD = 1000000007;
#
#     private List<Integer> allSums = new ArrayList<>();
#
#     public int maxProduct(TreeNode root) {
#         int totalSum = treeSum(root);
#         int nearestToHalf = 0;
#         int smallestDistanceBetween = Integer.MAX_VALUE;
#         for (int sum : allSums) {
#             // We want to do this in a way that doesn't require floats.
#             // One way is to minimise the *distance* between the 2 halves.
#             int distanceBetween = Math.abs(totalSum - sum * 2);
#             if (distanceBetween < smallestDistanceBetween) {
#                 smallestDistanceBetween = distanceBetween;
#                 nearestToHalf = sum;
#             }
#         }
#         return modularMultiplication(nearestToHalf, totalSum - nearestToHalf, MOD);
#     }
#
#
#     private int modularMultiplication(int a, int b, int m) {
#         int product = 0;
#         int currentSum = a;
#         while (b > 0) {
#             int bit = b % 2;
#             b >>= 1;
#             if (bit == 1) {
#                 product += currentSum;
#                 product %= m;
#             }
#             currentSum <<= 1;
#             currentSum %= m;
#         }
#         return product;
#     }    
#
#     private int treeSum(TreeNode subroot) {
#         if (subroot == null) return 0;
#         int leftSum = treeSum(subroot.left);
#         int rightSum = treeSum(subroot.right);
#         int totalSum = leftSum + rightSum + subroot.val;
#         allSums.add(totalSum);
#         return totalSum;
#     }
# }

# V2