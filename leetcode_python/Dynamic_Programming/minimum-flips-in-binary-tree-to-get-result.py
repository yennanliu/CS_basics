"""

2313. Minimum Flips in Binary Tree to Get Result
Hard

You are given the root of a binary tree with the following properties:

Leaf nodes have either the value 0 or 1, representing false and true respectively.
Non-leaf nodes have either the value 2, 3, 4, or 5, representing the boolean operations OR, AND, XOR, and NOT, respectively.

You are also given a boolean result, which is the desired result of the evaluation of the root node.

The evaluation of a node is as follows:

If the node is a leaf node, the evaluation is the value of the node, i.e. true or false.
Otherwise, evaluate the node's children and apply the boolean operation of its value with the children's evaluations.

In one operation, you can flip a leaf node, which causes a false node to become true, and a true node to become false.

Return the minimum number of operations that need to be performed such that the evaluation of root yields result. It can be shown that there is always a way to achieve result.

A leaf node is a node that has zero children.

Note: NOT nodes have either a left child or a right child, but other non-leaf nodes have both a left child and a right child.


Example 1:

Input: root = [3,5,4,2,null,1,1,1,0], result = true
Output: 2
Explanation:
It can be shown that a minimum of 2 nodes have to be flipped to make the root of the tree
evaluate to true. One way to achieve this is shown in the diagram above.

Example 2:

Input: root = [0], result = false
Output: 0
Explanation:
The root of the tree already evaluates to false, so 0 nodes have to be flipped.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
0 <= Node.val <= 5
OR, AND, and XOR nodes have 2 children.
NOT nodes have 1 child.
Leaf nodes have a value of 0 or 1.
Non-leaf nodes have a value of 2, 3, 4, or 5.

"""

# V0
# IDEA : TREE DP, 2 STATES PER NODE (cost to make it False / True)
#
#   f[v] = min flips so that the subtree at v evaluates to False
#   t[v] = min flips so that the subtree at v evaluates to True
#
#   leaf 0 -> (f, t) = (0, 1)      leaf 1 -> (f, t) = (1, 0)
#   OR  (2): f = fL + fR
#            t = min(fL+tR, tL+fR, tL+tR)
#   AND (3): f = min(fL+fR, fL+tR, tL+fR)
#            t = tL + tR
#   XOR (4): f = min(fL+fR, tL+tR)
#            t = min(fL+tR, tL+fR)
#   NOT (5): f = t(child), t = f(child)
#
#   a missing child gets (INF, INF) so it never contributes to a min.
#
#   NOTE : up to 10^5 nodes -> the tree can be a 10^5-deep chain, so the
#          post-order traversal is written ITERATIVELY (explicit stack)
#          rather than recursively.
#
"""

DP def
    (TREE DP, 2 states per node)

    f[v]: MIN flips so that the subtree at v evaluates to FALSE

    t[v]: MIN flips so that the subtree at v evaluates to TRUE

DP eq

     leaf 0 -> (f, t) = (0, 1)        leaf 1 -> (f, t) = (1, 0)

     OR  (2): f = fL + fR
              t = min(fL+tR, tL+fR, tL+tR)

     AND (3): f = min(fL+fR, fL+tR, tL+fR)
              t = tL + tR

     XOR (4): f = min(fL+fR, tL+tR)
              t = min(fL+tR, tL+fR)

     NOT (5): f = t(child),  t = f(child)


    -> e.g. a MISSING child gets (INF, INF) so it never contributes to a min

     NOTE !!! up to 10^5 nodes means the tree can be a 10^5-deep chain, so
              the post-order traversal is written ITERATIVELY

     ans = t[root] if result else f[root]

"""
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def minimumFlips(self, root, result):
        INF = float('inf')
        memo = {}                       # id(node) -> (falseCost, trueCost)
        stack = [(root, False)]
        while stack:
            node, done = stack.pop()
            if node is None:
                continue
            if not done:
                stack.append((node, True))
                if node.left is not None:
                    stack.append((node.left, False))
                if node.right is not None:
                    stack.append((node.right, False))
                continue

            v = node.val
            if v == 0:
                memo[id(node)] = (0, 1)
            elif v == 1:
                memo[id(node)] = (1, 0)
            else:
                lf, lt = memo.get(id(node.left), (INF, INF))
                rf, rt = memo.get(id(node.right), (INF, INF))
                if v == 2:              # OR
                    memo[id(node)] = (lf + rf,
                                      min(lf + rt, lt + rf, lt + rt))
                elif v == 3:            # AND
                    memo[id(node)] = (min(lf + rf, lf + rt, lt + rf),
                                      lt + rt)
                elif v == 4:            # XOR
                    memo[id(node)] = (min(lf + rf, lt + rt),
                                      min(lf + rt, lt + rf))
                else:                   # NOT (exactly one child)
                    memo[id(node)] = (min(lt, rt), min(lf, rf))

        f, t = memo[id(root)]
        return t if result else f
