package LeetCodeJava.Design;

// https://leetcode.com/problems/operations-on-tree/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  1993. Operations on Tree
 *  Medium
 *
 *  You are given a tree with n nodes numbered from 0 to n - 1 in the form of a
 *  parent array parent where parent[i] is the parent of the ith node. The root of
 *  the tree is node 0, so parent[0] = -1. You want to design a data structure that
 *  allows users to lock, unlock and upgrade nodes in the tree.
 *
 *    Lock: locks the given node for the given user and prevents other users from
 *      locking the same node. Only possible if the node is unlocked.
 *    Unlock: unlocks the given node for the given user. Only possible if it is
 *      currently locked by the same user.
 *    Upgrade: locks the given node for the given user and unlocks all of its
 *      descendants regardless of who locked them. Only possible if all 3 hold:
 *        - the node is unlocked,
 *        - it has at least one locked descendant (by any user), and
 *        - it does not have any locked ancestors.
 *
 *  Implement the LockingTree class:
 *    LockingTree(int[] parent) initializes the data structure with the parent array.
 *    boolean lock(int num, int user)
 *    boolean unlock(int num, int user)
 *    boolean upgrade(int num, int user)
 *
 *  Example 1:
 *    Input
 *      ["LockingTree","lock","unlock","unlock","lock","upgrade","lock"]
 *      [[[-1,0,0,1,1,2,2]],[2,2],[2,3],[2,2],[4,5],[0,1],[0,1]]
 *    Output
 *      [null, true, false, true, true, true, false]
 *    Explanation
 *      lock(2,2)    -> true  (node 2 was unlocked)
 *      unlock(2,3)  -> false (user 3 did not lock it)
 *      unlock(2,2)  -> true
 *      lock(4,5)    -> true
 *      upgrade(0,1) -> true  (node 0 free, node 4 is a locked descendant)
 *      lock(0,1)    -> false (already locked)
 *
 *  Constraints:
 *    n == parent.length
 *    2 <= n <= 2000
 *    0 <= parent[i] <= n - 1 for i != 0
 *    parent[0] == -1
 *    0 <= num <= n - 1
 *    1 <= user <= 10^4
 *    parent represents a valid tree.
 *    At most 2000 calls in total will be made to lock, unlock and upgrade.
 */
public class OperationsOnTree {

    // V0
    // IDEA: PARENT ARRAY (walk up) + CHILDREN LISTS (walk down)
    //
    //       locked[x] = the user id holding node x, or -1 when free, which makes
    //       lock / unlock O(1) lookups.
    //
    //       upgrade(num, user) needs three checks:
    //         1) num itself free and no locked ANCESTOR -> climb parent[] to the root
    //         2) at least one locked DESCENDANT          -> walk the subtree
    //         3) if found, clear every locked descendant and lock num
    //       (2) and (3) are done in the same subtree pass, with an explicit stack
    //       rather than recursion (the tree can be a 2000-node chain).
    //       cost is O(n) per upgrade, fine for <= 2000 total calls.
    /**
     * time = O(1) for lock / unlock, O(n) for upgrade
     * space = O(n)
     */
    private final int[] parent;
    private final int[] locked; // -1 = free, else the owning user id
    private final List<List<Integer>> children;

    public OperationsOnTree(int[] parent) {
        int n = parent.length;
        this.parent = parent;
        this.locked = new int[n];
        this.children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            locked[i] = -1;
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            children.get(parent[i]).add(i);
        }
    }

    public boolean lock(int num, int user) {
        if (locked[num] != -1) {
            return false;
        }
        locked[num] = user;
        return true;
    }

    public boolean unlock(int num, int user) {
        if (locked[num] != user) {
            return false;
        }
        locked[num] = -1;
        return true;
    }

    public boolean upgrade(int num, int user) {
        // 1) num must be free, and no ancestor may be locked
        if (locked[num] != -1) {
            return false;
        }
        for (int p = parent[num]; p != -1; p = parent[p]) {
            if (locked[p] != -1) {
                return false;
            }
        }
        // 2) collect the locked descendants
        List<Integer> lockedDesc = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int c : children.get(num)) {
            stack.push(c);
        }
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            if (locked[cur] != -1) {
                lockedDesc.add(cur);
            }
            for (int c : children.get(cur)) {
                stack.push(c);
            }
        }
        if (lockedDesc.isEmpty()) {
            return false;
        }
        // 3) unlock them all, then lock num
        for (int d : lockedDesc) {
            locked[d] = -1;
        }
        locked[num] = user;
        return true;
    }
}
