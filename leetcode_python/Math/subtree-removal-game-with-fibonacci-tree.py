"""

2005. Subtree Removal Game with Fibonacci Tree
Hard

A Fibonacci tree is a binary tree created using the order function order(n):

order(0) is the empty tree.
order(1) is a binary tree with only one node.
order(n) is a binary tree that consists of a root node with the left subtree as order(n - 2) and the right subtree as order(n - 1).

Alice and Bob are playing a game with a Fibonacci tree with Alice staring first. On each turn, a player selects a node and removes that node and its subtree. The player that is forced to delete root loses.

Given the integer n, return true if Alice wins the game or false if Bob wins, assuming both players play optimally.

A subtree of a binary tree tree is a tree that consists of a node in tree and all of this node's descendants. The tree tree could also be considered as a subtree of itself.


Example 1:

Input: n = 3
Output: true
Explanation:
Alice takes the node 1 in the right subtree.
Bob takes either the 1 in the left subtree or the 2 in the right subtree.
Alice takes whichever node Bob doesn't take.
Bob is forced to take the root node 3, so Bob will lose.
Return true because Alice wins.

Example 2:

Input: n = 1
Output: false
Explanation:
Alice is forced to take the root node 1, so Alice will lose.
Return false because Alice loses.

Example 3:

Input: n = 2
Output: true
Explanation:
Alice takes the node 1.
Bob is forced to take the root node 2, so Bob will lose.
Return true because Alice wins.


Constraints:

1 <= n <= 100

"""

# V0
# IDEA : SPRAGUE-GRUNDY / COLON PRINCIPLE ON A ROOTED TREE
#
#   removing a node + its subtree == cutting the edge that hangs it under its
#   parent, and "forced to delete the root" == no edge left to cut. so this is
#   plain green-hackenbush on a rooted tree, whose Grundy value is
#
#       g(tree) = XOR over children c of ( g(c) + 1 )
#
#   for the Fibonacci tree : g(1) = 0 (lone node, no edge), and for k >= 2 the
#   root has children order(k-2) and order(k-1), so
#
#       g(k) = (g(k-2) + 1) ^ (g(k-1) + 1)     with the empty tree contributing 0
#
#   Alice (first to move) wins iff g(n) != 0.
#   NOTE : g(2) uses an EMPTY left subtree -> only the right child contributes.
#   NOTE : the resulting pattern is g(n) == 0 exactly when n % 6 == 1.
#
# time = O(n), space = O(n)
class Solution(object):
    def findGameWinner(self, n):
        if n == 1:
            return False

        g = [0] * (n + 1)
        g[1] = 0
        for k in range(2, n + 1):
            left = 0 if k - 2 == 0 else g[k - 2] + 1
            g[k] = left ^ (g[k - 1] + 1)

        return g[n] != 0
