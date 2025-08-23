package dev;

import java.util.*;

public class workspace14 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // LC 297
    // 5.55 - 6.10 pm
    /**
     *
     *
     *
     */
    public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            return null;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {

            return null;
        }
    }

    // LC 430
    // 13.44 - 13.54 pm
    /**
     *
     * IDEA 1) LINKED LIST OP + recursion
     *
     *   1. check `child`
     *      -> connect next to child
     *      -> connect child.prev to node
     *      -> set child.child as null
     *
     *   2. check `next`
     *
     *     -> connect node.next to next
     *     -> connect next to node
     *
     */
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    // IDEA 2) `Stack`
    public Node flatten(Node head) {
        // edge
        if(head == null){
            return head;
        }
        if(head.next == null && head.child == null && head.prev == null){
            return head;
        }

        Node res = null;

        Stack<Node> st = new Stack<>();

        st.add(head);

        while(!st.isEmpty()){

            Node _node = st.pop();

            Node _child = null;
            Node _next = null;
//
//            while(_node.child != null){
//                st.add(_child);
//            }
//
//            _node

        }


        return res.next;
    }


    // IDEA 1) DFS
//    public Node flatten(Node head) {
//        // edge
//        if(head == null){
//            return head;
//        }
//        if(head.next == null && head.child == null && head.prev == null){
//            return head;
//        }
//
//        // helper func ???
//        return flattenHelper(head);
//
//        //return  null;
//    }
//
//    private Node flattenHelper(Node head){
//        // edge
//        if(head == null){
//            return head;
//        }
//
//        // cache next
//        Node _next = head.next;
//        Node _head = head;
//
//        Node _child = head.child;
//
//        // check `child`
//        while(_child != null){
//            head.next = _child;
//            head = _child;
//            _child = _child.next;
//
//            // what if child as `child` ???
//            //_child =  flattenHelper(head.child);
//        }
//
//        // point _child.child to null
//        _child.child = null;
//
//        // point `child` end to next
//        _child.next = _next;
//
//        return head;
//    }

    // LC 1060
    // IDEA 1) BRUTE FORCE
    /**
     *  exp 1)
     *
     *  Input: A = [4,7,9,10], K = 1
     *
     *  -> diff = [3, 2, 1]
     *
     *  -> 3, cnt = 3, 3 >= k,  -> ans = (3 - 3) + k
     *
     *
     *  exp 2)
     *
     *  Input: A = [4,7,9,10], K = 3
     *
     *  -> diff = [2,1,0]
     *
     *  -> 2, cnt = 2
     *  -> 1, cnt = 3, 3 >= k, ans = 4 + diff + (3 - 3)
     *
     *
     */
    // V1
//    public int missingElement(int[] nums, int k) {
//        // edge
//
//        // total `missing` cnt
//        int cnt = 0;
//
//        for(int i = 1; i < nums.length; i++){
//            int diff = nums[i] - nums[i-1] - 1;
//            cnt += diff;
//            if(cnt >= k){
//                return nums[i-1] + (cnt - diff);
//            }
//
//        }
//
//        return -1;
//    }

    // V2
//    public int missingElement(int[] nums, int k) {
//        // edge
//        if (nums == null || nums.length == 0) {
//            return -1;
//        }
//
//        for(int i = 1; i < nums.length; i++){
//
//            int diff = nums[i] - nums[i-1] - 1;
//
//            if(diff >= k){
//                return nums[i-1] + k; // ???
//            }
//
//            k -= diff;
//        }
//
//        // note !!! if the missing element is NOT within
//        // the elements in array
//        return nums[nums.length - 1] + k;
//    }


    // IDEA 2) BINARY SEARCH
    public int missingElement(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return -1;
        }

        List<Integer> diff_list = new ArrayList<>();
        for(int i = 1; i < nums.length; i++){
            int diff = nums[i] - nums[i-1] - 1;
            if(i > 1){
                diff_list.add(diff + diff_list.get(i-1));
            }
        }

        int l = 0;
        int r = diff_list.size() - 1;

        while (r >= l){
            int mid = (l + r) / 2;
            int val = diff_list.get(mid);
            if(mid == k){
                return nums[mid-1] + val;
            }
            if(val < k){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        // note !!! if the missing element is NOT within
        // the elements in array
        return nums[nums.length - 1] + k;
    }

    // LC 438
    // 3.13 - 3.23 pm
    /**
     *
     *  return an array of `all` the `start` indices of
     *  p's anagrams in s.
     *
     *
     *  IDEA 1) 2 POINTER + HASH MAP
     *
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(s == null || p == null || s.isEmpty() || p.isEmpty()){
            return res;
        }
        // ??
        if(s.equals(p)){
            res.add(0);
            return res;
        }

        // init p map
        Map<String, Integer> p_map = new HashMap();
        for(String x: p.split("")){
            p_map.put(x, p_map.getOrDefault(x, 0) + 1);
        }

        System.out.println(">>> p_map = " + p_map);

        String[] s_arr = s.split("");
        // init s map
        Map<String, Integer> s_map = new HashMap();

        // 2 pointer
        int l = 0;
        for(int r = 0; r < s_arr.length; r++){
            String key = s_arr[r];
            System.out.println(">>> key = " + key + ", p_map = " + p_map
            + ", s_map = " + s_map);
            if(!p_map.containsKey(key)){
                l = r + 1;
                s_map = new HashMap();
            }else{
                if(!s_map.containsKey(key) || s_map.get(key) < p_map.get(key)){
                    s_map.put(key, s_map.getOrDefault(key, 0) + 1);
                    if(isEqaual(p_map, s_map)){
                        res.add(l);
                        s_map = new HashMap();
                    }
                }else{
                    l = r + 1;
                    s_map = new HashMap();
                }
            }
        }

        return res;
    }

    private boolean isEqaual(Map<String, Integer> p_map, Map<String, Integer> s_map){
        if(p_map.keySet().size() != s_map.keySet().size()){
            return false;
        }
        for(String k: p_map.keySet()){
            if(p_map.get(k) != s_map.get(k)){
                return false;
            }
        }

        return true;
    }

    // LC 249
    public List<List<String>> groupStrings_1(String[] strings) {
        return null;
    }

}
