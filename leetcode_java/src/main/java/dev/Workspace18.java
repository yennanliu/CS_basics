package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Workspace18 {

    ///WorkSpace17.MyTrie trie = new WorkSpace17.MyTrie();
    //WorkSpace17.MyTrie.MyTrieNode.


    // LC 2871
    // 6.21 - 31 am
    /**
     *   IDEA: BRUTE FORCE + AND bit op
     *
     *
     */
    public int maxSubarrays(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        int cnt = 0;
        int cur = -1; // ???? // nums[0];

        for(int i = 0; i < nums.length; i++){
            if(cur == -1){
                cur = nums[i];
            }else{
                cur = cur & nums[i];
            }
            if(cur == 0){
                cnt += 1;
                // reset cur
                cur = -1;
            }
        }

        return cnt == 0 ? 1: cnt;
    }

    // LC 2870
    // 6.32 - 42 am
    /**
     *   IDEA: hashmap + BRUTE FORCE + math
     *
     *   KEY:
     *
     *    x % 3 = 0 or 1 or 2
     *    -> 0, ans unchanged
     *    -> 1, ans += 1
     *    -> 2, ans += 1
     *
     *    -> since x % 3 = 1 or 2
     *    -> we will the other `remove 2 op` to empty the arr
     *
     */
    public int minOperations(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int ops = 0;

        for (int x : nums) {

            // edge
            if(map.get(x) == 1){
                return -1;
            }

            //map.put(x, map.getOrDefault(x, 0) + 1);
            if(map.containsKey(x)){
                int cnt = map.get(x);
                // case 1) cnt % 3 == 0
                if(cnt % 3 == 0){
                    map.remove(x);
                    ops += 1; // ???
                }
                // case 2) cnt % 3 == 1
                if(cnt % 3 == 1){
                    if(map.get(x) < 4){
                        return -1; // ???
                    }
                    map.put(x, map.get(x) - 4);
                }
                // case 3) cnt % 3 == 2
                else{
                    if(map.get(x) < 5){
                        return -1; // ???
                    }
                    map.put(x, map.get(x) - 5);
                }
            }

        }


        return ops;
    }


    // LC 653
    /**
     *
     * Given the root of a BST and an integer k, r
     * -> return true if there exist two elements in the BST such that
     * their sum is equal to k, or false otherwise.
     *
     *  -> return true if ANY node in BST sum equals as k, otherwise return false
     *
     *
     *
     */
    public boolean findTarget(TreeNode root, int k) {
        // edge
        if(root == null){
            return false;
        }
        if(root.left == null && root.right == null){
            return false;
        }
        // map : { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        // bfs
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode cur = q.poll();
            map.put(cur.val, 1);  // node val should be unique
            // x + y = k
            // -> x = k - y
            // node val should be unique
            if(map.containsKey(k - cur.val) && k - cur.val != cur.val){
                return true;
            }
            if(cur.left != null){
                q.add(cur.left);
            }
            if(cur.right != null){
                q.add(cur.right);
            }
        }

        return false;
    }



}
