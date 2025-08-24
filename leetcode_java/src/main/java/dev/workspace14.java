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
    // 4.03 - 4.13 pm
    /**
     *  -> Given a list of strings
     *    which contains only lowercase alphabets,
     *    group all strings that belong to
     *    the same shifting sequence.
     *
     *
     *   IDEA 1) HASH MAP + `calculate diff within alphabet`
     *
     */
    public List<List<String>> groupStrings_1(String[] strings) {
        List<List<String>> res = new ArrayList<>();
        // edge
        if(strings == null || strings.length == 0){
            return res;
        }

        // map:  { `diff dist`: [word_1, word_2,...] }
        Map<Integer, List<String>> map = new HashMap<>();
        for(String x: strings){
            int dist = getDist(x);
            List<String> list = new ArrayList<>();
            if(map.containsKey(dist)){
                list = map.get(dist);
                list.add(x);
            }
            map.put(dist, list);
        }

        for(Integer i: map.keySet()){
            res.add(map.get(i));
        }

        return res;
    }

    private int getDist(String input){
        if(input.isEmpty() || input.length() == 1){
            return 0;
        }
        //int diff = input.charAt(0) - 'a';
        return input.charAt(1) - input.charAt(0); // ??
    }

  // LC 890
  // 4.28 - 4.38 pm
  /**
   *  IDEA 1) HASHMAP
   *
   *   ex 1) slide window ??
   *
   *   Input:
   *      words = ["abc","deq","mee","aqq","dkd","ccc"],
   *      pattern = "abb"
   *    Output: ["mee","aqq"]
   *
   *   -> pattern:  [1a2b]
   *
   */
  public List<String> findAndReplacePattern(String[] words, String pattern) {
      List<String> res = new ArrayList<>();
      // edge
      if(words == null || words.length == 0){
          return res;
      }
      if(words.length == 1){
          res.add(words[0]);
          return res;
      }

      String _pattern = getPattern(pattern);

      for(String w: words){
          if(isSamePattern(getPattern(w), _pattern)){
              res.add(w);
          }
      }

      return res;
    }

    private boolean isSamePattern(String pattern1, String pattern2){
      if(pattern1.length() != pattern2.length()){
          return false;
      }

      //String alpha = "abcdefghijklmnopqrstuvwxyz";
      String nums = "0123456789";
      for(int i = 0; i < pattern1.length(); i++){
          String p1 = String.valueOf(pattern1.charAt(i));
          String p2 = String.valueOf(pattern2.charAt(i));
          // only compare the `digit` part
          if(nums.contains(p1)){
              if(p1.equals(p2)){
                  return false;
              }
          }
      }
      return true;
    }

    private String getPattern(String pattern){
        StringBuilder sb = new StringBuilder();
        // brute force, to optimize
        int cnt = 0;
        String prev = null;
        for(int i = 0; i < pattern.length(); i++){
            if(pattern.charAt(i) == pattern.charAt(i)){
                cnt += 1;
                prev = String.valueOf(pattern.charAt(i));
            }else{
                sb.append(cnt);
                sb.append(pattern.charAt(i-1));
                cnt = 0; // ??
                prev = String.valueOf(pattern.charAt(i));
                //break; // ???
            }
        }

        return sb.toString();
    }

    // LC 347
    // 12.49 - 12.59 pm
    /**
     *  IDEA 1) HASHMAP + SORT (on map val)
     *
     *  IDEA 2) HASHMAP + PQ
     *
     */
    public int[] topKFrequent(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        if(nums.length == 1){
            return new int[]{nums[0]}; // ??
        }

        // map : { val : cnt }
        Map<Integer, Integer> cnt_map = new HashMap<>();

        // small PQ (sort on map val
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = cnt_map.get(o1) - cnt_map.get(o2);
                return diff;
            }
        });

        for(int x: nums){
            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
        }

        int[] res = new int[k];
        for(int key: cnt_map.keySet()){
            pq.add(key);
        }
        while (pq.size() > k){
            pq.poll();
        }

        int i = 0;
        while(i < k){
            res[i] = pq.poll();
            i += 1;
        }

        return res;
    }


//    public int[] topKFrequent(int[] nums, int k) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return null;
//        }
//        if(nums.length == 1){
//            return new int[]{nums[0]}; // ??
//        }
//
//        // map : { val : cnt }
//        Map<Integer, Integer> map = new HashMap<>();
//
//        // max PQ: save `top k` largest cnt
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1-o2;
//                return diff; // ???
//            }
//        });
//
//        for(int x: nums){
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//
//        Map<Integer, Integer> map2 = new HashMap<>();
//        for(Integer key: map.keySet()){
//            map2.put(map.get(key), key);
//        }
//
//        for(int val: map.values()){
//            pq.add(val);
//        }
//
//        while(pq.size() > k){
//            pq.poll();
//        }
//
//        int[] res = new int[pq.size()];
//        int i = 0;
//        while(!pq.isEmpty()){
//            res[i] = map2.get(pq.poll());
//            i += 1;
//        }
//
//        return res;
//    }

    // LC 215
    // 1.22 - 1.32 pm
    /**
     * IDEA 1) PQ
     *
     *
     */
    public int findKthLargest(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            throw new RuntimeException("null array");
        }
        if(k > nums.length){
            throw new RuntimeException("k bigger than arr size");
        }
        if(nums.length == 1){
            if(k == 1){
                return nums[0];
            }
            throw new RuntimeException("not valid k");
        }

        // small PQ, so we can keep PQ size = k
        // and pop the `k+1` largest element
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for(int x: nums){
            pq.add(x);
            while (pq.size() > k){
                pq.poll();
            }
        }

        if(!pq.isEmpty()){
            return pq.poll();
        }

        return -1;  // should NOT visit here
    }

}
