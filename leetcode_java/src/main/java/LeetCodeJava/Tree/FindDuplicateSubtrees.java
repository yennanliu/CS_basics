package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-duplicate-subtrees/
/**
 * 652. Find Duplicate Subtrees
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return all duplicate subtrees.
 *
 * For each kind of duplicate subtrees, you only need to return the root node of any one of them.
 *
 * Two trees are duplicate if they have the same structure with the same node values.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,null,2,4,null,null,4]
 * Output: [[2,4],[4]]
 * Example 2:
 *
 *
 * Input: root = [2,1,1]
 * Output: [[1]]
 * Example 3:
 *
 *
 * Input: root = [2,2,2,3,null,3,null]
 * Output: [[2,3],[3]]
 *
 *
 * Constraints:
 *
 * The number of the nodes in the tree will be in the range [1, 5000]
 * -200 <= Node.val <= 200
 *
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicateSubtrees {

    // V0
//    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
//        return null;
//    }

    // V0-1
    // IDEA: HASHMAP + DFS + NODE PATH
    Map<TreeNode, List<String>> pathMap = new HashMap<>();
    public List<TreeNode> findDuplicateSubtrees_0_1(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        System.out.println(">>> (before dfs) pathMap = " + pathMap);
        // dfs
        duplicateSubtreesHelper(root, new HashMap<>());
        System.out.println(">>> (after dfs) pathMap = " + pathMap);

        // check duplicates
        for (TreeNode node : pathMap.keySet()) {
            if (pathMap.get(node).size() > 1) {
                res.add(node);
            }
        }

        return res;
    }

    private String duplicateSubtreesHelper(TreeNode root, Map<String, TreeNode> seen) {
        if (root == null) {
            return "#"; // marker for null
        }

        // build serialization
        String path = root.val + "," +
                duplicateSubtreesHelper(root.left, seen) + "," +
                duplicateSubtreesHelper(root.right, seen);

        // if this path already exists, add it under the original root node
        if (seen.containsKey(path)) {
            TreeNode firstRoot = seen.get(path);
            pathMap.computeIfAbsent(firstRoot, k -> new ArrayList<>()).add(path);
        } else {
            seen.put(path, root);
            pathMap.computeIfAbsent(root, k -> new ArrayList<>()).add(path);
        }

        return path;
    }

    // V1
    // https://leetcode.ca/2017-09-12-652-Find-Duplicate-Subtrees/
    private Map<String, Integer> counter;
    private List<TreeNode> ans;
    public List<TreeNode> findDuplicateSubtrees_1(TreeNode root) {
        counter = new HashMap<>();
        ans = new ArrayList<>();
        dfs(root);
        return ans;
    }

    private String dfs(TreeNode root) {
        if (root == null) {
            return "#";
        }
        String v = root.val + "," + dfs(root.left) + "," + dfs(root.right);
        counter.put(v, counter.getOrDefault(v, 0) + 1);
        if (counter.get(v) == 2) {
            ans.add(root);
        }
        return v;
    }

    // V2
    // IDEA: HASHMAP
    // https://leetcode.com/problems/find-duplicate-subtrees/solutions/3238264/java-easy-hashmap-with-explanation-by-ka-a6tv/
    public List<TreeNode> findDuplicateSubtrees_2(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        HashMap<String, Integer> hm = new HashMap<>();
        helper(res, root, hm);
        return res;
    }

    public String helper(List<TreeNode> res, TreeNode root, HashMap<String, Integer> hm) {
        if (root == null)
            return "";
        String left = helper(res, root.left, hm);
        String right = helper(res, root.right, hm);
        int currroot = root.val;
        String stringformed = currroot + "$" + left + "$" + right;
        if (hm.getOrDefault(stringformed, 0) == 1) {
            res.add(root);
        }
        hm.put(stringformed, hm.getOrDefault(stringformed, 0) + 1);
        return stringformed;
    }

    // V3
    // IDEA: HASHMAP
    // https://leetcode.com/problems/find-duplicate-subtrees/solutions/1370456/java-easy-approach-with-explanation-hash-6hx9/
    HashMap<String, Integer> map = new HashMap<>();//String -- frequency//it store the string at every instant when we visit parent after visiting its children //it also calculates the frequency of the String in the tree
    ArrayList<TreeNode> res = new ArrayList<>();//it contain the list of dublicate nodes

    public List<TreeNode> findDuplicateSubtrees_3(TreeNode root) {
        Mapper(root);
        return res;//returning the list containing a node of dublicate subtree
    }

    public String Mapper(TreeNode root) {//we are doing postorder traversal because we want to first deal with children and then the parent
        if (root == null)//when we reach to the null ,we return N to tell that i am null and unique
            return "N";

        String left = Mapper(root.left);//recursing down the left subtree and knowing about the left child //LEFT
        String right = Mapper(root.right);//recursing down the hright subtree and knowing abou the right child //RIGHT

        //ROOT
        String curr = root.val + " " + left + " " + right;//after knowing about the left and right children//parent forms their own string //space is added to disinguish the string of same reapeatating root value ex- 11 N , 1 1N

        map.put(curr, map.getOrDefault(curr, 0) + 1);//counting the frequency of string

        if (map.get(curr) == 2)//only the dublicate string node are added to the ArrayList
            res.add(root);

        return curr;//returning to the parent to that i am present, and here is my string with the informationn of my left and right child
    }

    // V4
    // https://leetcode.com/problems/find-duplicate-subtrees/solutions/3238342/clean-codes-full-explanation-depth-first-yn8v/
    // IDEA: DFS
    public List<TreeNode> findDuplicateSubtrees_4(TreeNode root) {
        List<TreeNode> ans = new ArrayList<>();
        Map<String, Integer> count = new HashMap<>();

        encode(root, count, ans);
        return ans;
    }

    private String encode(TreeNode root, Map<String, Integer> count, List<TreeNode> ans) {
        if (root == null)
            return "";

        final String encoded = root.val + "#" + encode(root.left, count, ans) + "#" + encode(root.right, count, ans);//# for encoding null left and right childs
        count.merge(encoded, 1, Integer::sum);
        //used to add the encoding to the count map. If the encoding already exists in the map, its count is incremented by 1 using the Integer::sum function. If it doesn't exist, a new entry is added with a count of 1. This ensures that each subtree encoding is counted exactly once in the map.
        if (count.get(encoded) == 2)//duplicate subtree
            ans.add(root);//add the roots
        return encoded;
    }



}
