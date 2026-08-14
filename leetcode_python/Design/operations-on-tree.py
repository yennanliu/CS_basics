"""

1993. Operations on Tree
Medium

You are given a tree with n nodes numbered from 0 to n - 1 in the form of a parent array parent where parent[i] is the parent of the ith node. The root of the tree is node 0, so parent[0] = -1 since it has no parent. You want to design a data structure that allows users to lock, unlock, and upgrade nodes in the tree.

The data structure should support the following functions:

Lock: Locks the given node for the given user and prevents other users from locking the same node. You may only lock a node using this function if the node is unlocked.
Unlock: Unlocks the given node for the given user. You may only unlock a node using this function if it is currently locked by the same user.
Upgrade: Locks the given node for the given user and unlocks all of its descendants regardless of who locked it. You may only upgrade a node if all 3 conditions are true:
- The node is unlocked,
- It has at least one locked descendant (by any user), and
- It does not have any locked ancestors.

Implement the LockingTree class:

LockingTree(int[] parent) initializes the data structure with the parent array.
lock(int num, int user) returns true if it is possible for the user with id user to lock the node num, or false otherwise. If it is possible, the node num will become locked by the user with id user.
unlock(int num, int user) returns true if it is possible for the user with id user to unlock the node num, or false otherwise. If it is possible, the node num will become unlocked.
upgrade(int num, int user) returns true if it is possible for the user with id user to upgrade the node num, or false otherwise. If it is possible, the node num will be upgraded.


Example 1:

Input
["LockingTree", "lock", "unlock", "unlock", "lock", "upgrade", "lock"]
[[[-1, 0, 0, 1, 1, 2, 2]], [2, 2], [2, 3], [2, 2], [4, 5], [0, 1], [0, 1]]
Output
[null, true, false, true, true, true, false]

Explanation
LockingTree lockingTree = new LockingTree([-1, 0, 0, 1, 1, 2, 2]);
lockingTree.lock(2, 2); // return true because node 2 is unlocked.
// Node 2 will now be locked by user 2.
lockingTree.unlock(2, 3); // return false because user 3 cannot unlock a node locked by user 2.
lockingTree.unlock(2, 2); // return true because node 2 was previously locked by user 2.
// Node 2 will now be unlocked.
lockingTree.lock(4, 5); // return true because node 4 is unlocked.
// Node 4 will now be locked by user 5.
lockingTree.upgrade(0, 1); // return true because node 0 is unlocked and has at least one locked descendant (node 4).
// Node 0 will now be locked by user 1 and node 4 will now be unlocked.
lockingTree.lock(0, 1); // return false because node 0 is already locked.


Constraints:

n == parent.length
2 <= n <= 2000
0 <= parent[i] <= n - 1 for i != 0
parent[0] == -1
0 <= num <= n - 1
1 <= user <= 10^4
parent represents a valid tree.
At most 2000 calls in total will be made to lock, unlock, and upgrade.

"""

# V0
# IDEA : DESIGN - parent array (walk up) + children lists (walk down)
#
#   locked[x] = the user id holding node x, or -1 when free.
#
#   lock / unlock are O(1) lookups on locked[].
#
#   upgrade(num, user) needs three checks :
#     1) no locked ANCESTOR (and num itself free) -> climb parent[] to the root
#     2) at least one locked DESCENDANT           -> walk the subtree
#     3) if found, clear every locked descendant and lock num
#   both walks are done in the same pass over the subtree.
#
#   NOTE : the subtree walk uses an explicit stack, not recursion - the tree
#          can be a 2000-node chain, deeper than python's default limit.
#   NOTE : cost is O(n) per upgrade, fine for <= 2000 total calls.
#
# time = O(1) lock / unlock, O(n) upgrade ; space = O(n)
class LockingTree(object):

    def __init__(self, parent):
        n = len(parent)
        self.parent = parent
        self.locked = [-1] * n
        self.children = [[] for _ in range(n)]
        for node in range(1, n):
            self.children[parent[node]].append(node)

    def lock(self, num, user):
        if self.locked[num] != -1:
            return False
        self.locked[num] = user
        return True

    def unlock(self, num, user):
        if self.locked[num] != user:
            return False
        self.locked[num] = -1
        return True

    def upgrade(self, num, user):
        # 1) num itself and all its ancestors must be unlocked
        x = num
        while x != -1:
            if self.locked[x] != -1:
                return False
            x = self.parent[x]

        # 2) + 3) sweep the subtree, unlocking whatever we find
        found = False
        stack = list(self.children[num])
        while stack:
            y = stack.pop()
            if self.locked[y] != -1:
                self.locked[y] = -1
                found = True
            stack.extend(self.children[y])

        if not found:
            return False
        self.locked[num] = user
        return True


# Your LockingTree object will be instantiated and called as such:
# obj = LockingTree(parent)
# param_1 = obj.lock(num,user)
# param_2 = obj.unlock(num,user)
# param_3 = obj.upgrade(num,user)
