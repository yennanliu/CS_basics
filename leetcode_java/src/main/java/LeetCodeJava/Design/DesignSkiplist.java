package LeetCodeJava.Design;

// https://leetcode.com/problems/design-skiplist/

import java.util.Random;

/**
 *  1206. Design Skiplist
 *  Hard
 *
 *  Design a Skiplist without using any built-in libraries.
 *
 *  A skiplist is a data structure that takes O(log(n)) time to add, erase and search.
 *  It is a stack of sorted linked lists: each layer is a sorted linked list and the
 *  upper layers let a search skip over many nodes at once.
 *
 *  Implement the Skiplist class:
 *   - Skiplist() Initializes the object of the skiplist.
 *   - boolean search(int target) Returns true if target exists in the Skiplist.
 *   - void add(int num) Inserts num into the Skiplist.
 *   - boolean erase(int num) Removes ONE occurrence of num and returns true; if num
 *     does not exist, do nothing and return false.
 *
 *  Note that duplicates may exist in the Skiplist.
 *
 *  Example 1:
 *    Input
 *      ["Skiplist","add","add","add","search","add","search","erase","erase","search"]
 *      [[],[1],[2],[3],[0],[4],[1],[0],[1],[1]]
 *    Output
 *      [null,null,null,null,false,null,true,false,true,false]
 *
 *  Constraints:
 *    0 <= num, target <= 2 * 10^4
 *    At most 5 * 10^4 calls will be made to search, add, and erase.
 */
public class DesignSkiplist {

    // V0
    // IDEA: classic skip list. Every node carries a `next` array; a node is promoted to
    //       the next level with probability P = 0.25, so there are O(log n) levels and a
    //       search walks O(log n) nodes. All 3 ops share the same primitive: from the
    //       sentinel head at the TOP level, walk right while next.val < target, then
    //       drop a level - collecting the last node before the target on each level.
    /**
     * time = O(log n) expected per op
     * space = O(n)
     */
    private static final int MAX_LEVEL = 16;
    private static final double P = 0.25;

    private static class SkipNode {
        int val;
        SkipNode[] next;

        SkipNode(int val, int level) {
            this.val = val;
            this.next = new SkipNode[level];
        }
    }

    private final SkipNode head;
    private final Random rand;
    private int level;

    public DesignSkiplist() {
        // sentinel : value smaller than any legal input (0 <= num)
        this.head = new SkipNode(-1, MAX_LEVEL);
        this.rand = new Random();
        this.level = 1;
    }

    /**
     * time = O(log n)
     * space = O(1)
     */
    public boolean search(int target) {
        SkipNode cur = this.head;
        for (int i = this.level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < target) {
                cur = cur.next[i];
            }
        }
        cur = cur.next[0];
        return cur != null && cur.val == target;
    }

    /**
     * time = O(log n)
     * space = O(log n)
     */
    public void add(int num) {

        SkipNode[] prev = new SkipNode[MAX_LEVEL];
        SkipNode cur = this.head;
        for (int i = this.level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            prev[i] = cur;
        }

        int newLevel = randomLevel();
        if (newLevel > this.level) {
            for (int i = this.level; i < newLevel; i++) {
                prev[i] = this.head;
            }
            this.level = newLevel;
        }

        SkipNode node = new SkipNode(num, newLevel);
        for (int i = 0; i < newLevel; i++) {
            node.next[i] = prev[i].next[i];
            prev[i].next[i] = node;
        }
    }

    /**
     * time = O(log n)
     * space = O(log n)
     */
    public boolean erase(int num) {

        SkipNode[] prev = new SkipNode[MAX_LEVEL];
        SkipNode cur = this.head;
        for (int i = this.level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            prev[i] = cur;
        }

        SkipNode target = prev[0].next[0];
        if (target == null || target.val != num) {
            return false;
        }

        // unlink the FIRST matching node on every level it appears on
        for (int i = 0; i < this.level; i++) {
            if (prev[i].next[i] == target) {
                prev[i].next[i] = target.next[i];
            }
        }

        // shrink the level if the top ones went empty
        while (this.level > 1 && this.head.next[this.level - 1] == null) {
            this.level--;
        }

        return true;
    }

    private int randomLevel() {
        int lvl = 1;
        while (lvl < MAX_LEVEL && this.rand.nextDouble() < P) {
            lvl++;
        }
        return lvl;
    }
}
