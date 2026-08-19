package LeetCodeJava.Tree;

// https://leetcode.com/problems/path-in-zigzag-labelled-binary-tree/

import java.util.ArrayList;
import java.util.List;

/**
 *  1104. Path In Zigzag Labelled Binary Tree
 *  Medium
 *
 *  In an infinite binary tree where every node has two children, the nodes are
 *  labelled in row order.
 *
 *  In the odd numbered rows (ie., the first, third, fifth, ...), the labelling
 *  is left to right, while in the even numbered rows (second, fourth, sixth,
 *  ...), the labelling is right to left.
 *
 *  Given the label of a node in this tree, return the labels in the path from
 *  the root of the tree to the node with that label.
 *
 *  Example 1:
 *    Input: label = 14
 *    Output: [1,3,4,14]
 *
 *  Example 2:
 *    Input: label = 26
 *    Output: [1,2,6,10,26]
 *
 *  Constraints:
 *    1 <= label <= 10^6
 */
public class PathInZigzagLabelledBinaryTree {

    // V0
    // IDEA: MATH - MIRROR THE LABEL BACK INTO THE "NORMAL" NUMBERING
    //       level L (1-based) holds labels in [2^(L-1), 2^L - 1].
    //       if the level were labelled left -> right the parent would just be
    //       label / 2. because the directions alternate, first mirror the
    //       label inside its own level:
    //           mirror = (2^(L-1) + 2^L - 1) - label
    //       then parent = mirror / 2 (the parent level is mirrored too, so one
    //       mirror per step is enough).
    /**
     * time = O(log(label))
     * space = O(log(label))
     */
    public List<Integer> pathInZigZagTree(int label) {
        // find the 1-based level that contains `label`
        int level = 1;
        while ((1L << level) <= label) {
            level++;
        }

        Integer[] arr = new Integer[level];
        int cur = label;
        int lv = level;
        while (lv > 0) {
            arr[lv - 1] = cur;
            // mirror within the level, then step up to the parent
            cur = (int) ((((1L << (lv - 1)) + (1L << lv) - 1) - cur) >> 1);
            lv--;
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < level; i++) {
            res.add(arr[i]);
        }
        return res;
    }
}
