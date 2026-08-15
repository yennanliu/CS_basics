"""

3331. Find Subtree Sizes After Changes
Medium

You are given a tree rooted at node 0 that consists of n nodes numbered from 0 to n - 1. The tree is represented by an array parent of size n, where parent[i] is the parent of node i. Since node 0 is the root, parent[0] == -1.

You are also given a string s of length n, where s[i] is the character assigned to node i.

We make the following changes on the tree one time simultaneously for all nodes x from 1 to n - 1:

Find the closest node y to node x such that y is an ancestor of x, and s[x] == s[y].
If node y does not exist, do nothing.
Otherwise, remove the edge between x and its current parent and make node y the new parent of x by adding an edge between them.

Return an array answer of size n where answer[i] is the size of the subtree of node i in the final tree.


Example 1:

Input: parent = [-1,0,0,1,1,1], s = "abaabc"
Output: [6,3,1,1,1,1]
Explanation:
The parent of node 3 will change from node 1 to node 0.

Example 2:

Input: parent = [-1,0,4,0,1], s = "abbba"
Output: [5,2,1,1,1]
Explanation:
The following changes will happen at the same time:
The parent of node 4 will change from node 1 to node 0.
The parent of node 2 will change from node 4 to node 1.


Constraints:

n == parent.length == s.length
1 <= n <= 10^5
0 <= parent[i] <= n - 1 for all i >= 1.
parent[0] == -1
parent represents a valid tree.
s consists only of lowercase English letters.

"""

# V0
# IDEA : ONE DFS CARRYING A STACK OF ANCESTORS PER LETTER
#
#   "the closest ancestor sharing x's character" is exactly the top of a
#   stack that holds, for each letter, the ancestors currently on the path
#   from the root. so a single traversal decides every node's new parent :
#   push a node's letter on entry, pop it on exit.
#
#   note the changes are described as simultaneous, but the new parent is
#   always an ANCESTOR in the ORIGINAL tree, so reading the original ancestor
#   stack gives the right answer without any ordering subtlety.
#
#   with the new parents in hand, subtree sizes come from a second bottom-up
#   pass over the same traversal order.
#
#   both passes are iterative — the tree may be a 10^5-node path.
#
# time = O(n), space = O(n)
class Solution(object):
    def findSubtreeSizes(self, parent, s):
        n = len(parent)
        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[parent[i]].append(i)

        new_parent = list(parent)
        stacks = [[] for _ in range(26)]

        order = []
        st = [(0, False)]
        while st:
            x, done = st.pop()
            c = ord(s[x]) - 97
            if done:
                stacks[c].pop()
                continue
            if stacks[c]:
                new_parent[x] = stacks[c][-1]
            stacks[c].append(x)
            order.append(x)
            st.append((x, True))
            for y in reversed(children[x]):
                st.append((y, False))

        size = [1] * n
        for x in reversed(order):               # children settle before parents
            p = new_parent[x]
            if p != -1 and x != 0:
                size[p] += size[x]
        return size
