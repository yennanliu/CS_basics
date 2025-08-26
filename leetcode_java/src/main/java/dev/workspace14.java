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
    // 3.09 - 3.19 pm
    /**
     *  IDEA 1) HASHMAP + LOOP
     *
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        // edge
        if (s == null || p == null || s.isEmpty() || p.isEmpty() || p.length() > s.length()) {
            return res;
        }

        Map<String, Integer> p_map = new HashMap<>();
        for(String x: p.split("")){
            p_map.put(x, p_map.getOrDefault(x, 0) + 1);
        }

        Map<String, Integer> s_map = new HashMap<>();

        String[] s_arr = s.split("");
        for(int i = 0; i < s_arr.length; i++){
            String val = s_arr[i];
            // case 1) val NOT exists in p_map
            if(!p_map.containsKey(val)){
                s_map = new HashMap<>();
            }else{
                // case 2) val exists in p_map, but NOT in s_map yet
                if(!s_map.containsKey(val)){
                    s_map.put(val, s_map.getOrDefault(val, 0) + 1);
                }else{
                    // case 3) val exists in p_map, but  s_map(cnt) < p_map(cnt)
                    if(s_map.get(val) < p_map.get(val)){
                        s_map.put(val, s_map.getOrDefault(val, 0) + 1);
                    }
                    // case 4) val exists in p_map, but  s_map(cnt) > p_map(cnt)
                    else{
                        //s_map.put(val, s_map.getOrDefault(val, 0) - 1);
//                        s_map = new HashMap<>();
//                        s_map.put(val, 1);
                    }
                }
            }

            // check `if pattern equals`
            if(isEqaual(p_map, s_map)){
                res.add(i);
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




//    public List<Integer> findAnagrams(String s, String p) {
//        List<Integer> res = new ArrayList<>();
//        // edge
//        if(s == null || p == null || s.isEmpty() || p.isEmpty()){
//            return res;
//        }
//        // ??
//        if(s.equals(p)){
//            res.add(0);
//            return res;
//        }
//
//        // init p map
//        Map<String, Integer> p_map = new HashMap();
//        for(String x: p.split("")){
//            p_map.put(x, p_map.getOrDefault(x, 0) + 1);
//        }
//
//        System.out.println(">>> p_map = " + p_map);
//
//        String[] s_arr = s.split("");
//        // init s map
//        Map<String, Integer> s_map = new HashMap();
//
//        // 2 pointer
//        int l = 0;
//        for(int r = 0; r < s_arr.length; r++){
//            String key = s_arr[r];
//            System.out.println(">>> key = " + key + ", p_map = " + p_map
//            + ", s_map = " + s_map);
//            if(!p_map.containsKey(key)){
//                l = r + 1;
//                s_map = new HashMap();
//            }else{
//                if(!s_map.containsKey(key) || s_map.get(key) < p_map.get(key)){
//                    s_map.put(key, s_map.getOrDefault(key, 0) + 1);
//                    if(isEqaual(p_map, s_map)){
//                        res.add(l);
//                        s_map = new HashMap();
//                    }
//                }else{
//                    l = r + 1;
//                    s_map = new HashMap();
//                }
//            }
//        }
//
//        return res;
//    }
//
//    private boolean isEqaual(Map<String, Integer> p_map, Map<String, Integer> s_map){
//        if(p_map.keySet().size() != s_map.keySet().size()){
//            return false;
//        }
//        for(String k: p_map.keySet()){
//            if(p_map.get(k) != s_map.get(k)){
//                return false;
//            }
//        }
//
//        return true;
//    }

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

    // LC 692
    // 1.34 - 1.44 pm
    /**
     *  IDEA 1) HASHMAP + PQ custom sorting
     *
     *
     */
    public List<String> topKFrequent(String[] words, int k) {
        List<String> res = new ArrayList<>();
        // edge
        if(words == null || words.length == 0){
            return res;
        }
        if(words.length == 1){
            if(k > words.length){
                //throw new Exception("k > word size");
                return res; // ???
            }
            res.add(words[0]);
            return res;
        }

        // {val : cnt}
        Map<String, Integer> cnt_map = new HashMap<>();
        for(String w: words){
            cnt_map.put(w, cnt_map.getOrDefault(w, 0) + 1);
        }

        // custom sort: cnt_map val
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = cnt_map.get(o1) - cnt_map.get(o2);
                // Sort on `key ` with `lexicographically` order (increasing order)
                //return y.length() - x.length(); // ?
                if(diff == 0){
                    return o1.compareTo(o2);
                }
                return diff;
            }
        });

        // NOTE !!! below
        // we add map `element` to PQ (instead of val)
        // and if PQ size > k, pop it
        // since PQ is sorting on map val
        // so we automatically pop element with `least cnt`
        for(String key: cnt_map.keySet()){
            pq.add(key);
            // NOTE !!!
            while(pq.size() > k){
                pq.poll();
            }
        }

        while (!pq.isEmpty()){
            res.add(pq.poll());
        }

        return res;
    }

  // LC 767
  // 2.07 - 2.30 pm
  /**
   * Given a string s, `rearrange` the
   * characters of s so that
   * any two adjacent characters are `NOT the same`.
   *
   *
   *  IDEA 1) HASHMAP + POINTER ???
   *
   *    step 1) freq map : {val : cnt}
   *    step 2) loop over `most freq val` in map
   *            and construct the str
   *    step 3) go to `next freq val` in map,
   *            continue construct the str
   *    step 4) redo above ..
   *
   *    NOTE: if can't form the str by different adjacent element
   *          within any iteration -> return "" directly
   *
   *   ex 1)
   *
   *   Input: s = "aab"
   *   Output: "aba"
   *
   *   -> map = {a: 2, b: 1}
   *
   *   ->
   *
   */
  public String reorganizeString(String s) {
      //StringBuilder sb = new StringBuilder();
      // edge
      if(s.isEmpty()){
          return "";
      }
      if(s.length() <= 2){
          return s;
      }

      // { val : cnt }
      Map<String, Integer> cnt_map = new HashMap<>();

      for(String x: s.split("")){
          cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
      }

      // custom PQ: sort on map val  (big -> small)
      PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
              int diff = cnt_map.get(o2) - cnt_map.get(o1);
              return diff;
          }
      });

      for(String key: cnt_map.keySet()){
          pq.add(key);
      }

      String res = "";

      while(!pq.isEmpty()){
          // get val with `max` cnt
          String cur_val = pq.poll();
          // check if `adjacent` are NOT the same
          if(!res.isEmpty()){
              String prevVal = String.valueOf(res.charAt(res.length() - 1));
              if(prevVal.equals(cur_val)){
                  return "";
              }
          }

          res += cur_val;

          // update hashmap
          cnt_map.put(cur_val, cnt_map.get(cur_val) - 1);
          // if `cnt` == 0
          // remove key from hashmap
          if(cnt_map.get(cur_val) == 0){
              cnt_map.remove(cur_val);
          }else{
              // update PQ: the key still exists, add it back to
              pq.add(cur_val);
          }
      }

      return res;
    }


    // LC 395
    // 17.41 - 17.51 pm
    /**
     *
     *  return the length of the `longest` substring
     *  of s such that the frequency of
     *  each character in this substring is
     *  `greater than or equal` to k.
     *
     *  IDEA 1) HASH MAP + SLIDING WINDOW ???
     *
     *   cnt_map : {val : cnt}
     *   last_idx_map : { val : last_idx }
     *
     *
     *   ex 1)
     *
     *   Input: s = "aaabb", k = 3
     *   Output: 3
     *
     *   -> map : { a : 3, b: 2}
     *   -> 3
     *
     *
     *   ex 2)
     *
     *   Input: s = "ababbc", k = 2
     *   Output: 5
     *
     *   -> cnt_map : { a : 2, b : 3, c : 1}
     *      last_idx_map : { a : 2, b: 4, c : 5 }
     *
     *    "ababbc"
     *     x        "a"
     *
     *    "ababbc"
     *      x       "ab"
     *
     *    "ababbc"  "aba"
     *       x
     *
     *     "ababbc"  "abab"    ans=4
     *         x
     *
     *    "ababbc"  "ababb"    ans=5
     *         x
     *
     *    ababbc"  "ababb"    ans=5
     *         x
     */
    public int longestSubstring(String s, int k) {
        // edge
        if(s == null || s.isEmpty()){
            return 0;
        }
        if(s.length() == 1){
            if(k <= 1){
                return 1;
            }
            return 0;
        }

        // cnt map
        Map<String, Integer> cnt_map = new HashMap<>();
        Map<String, Integer> cnt_map2 = new HashMap<>();
        // last idx map
        Map<String, Integer> last_idx_map = new HashMap<>();

        String[] s_arr = s.split("");
        for(int i = 0; i <  s_arr.length; i++){
            String val = String.valueOf(s.charAt(i));
            cnt_map.put(val, cnt_map.getOrDefault(val, 0) + 1);
            last_idx_map.put(val, i);
        }

        // only keep `cnt > k` key
        for(String key: cnt_map.keySet()){
            if(cnt_map.get(key) >= k){
                cnt_map2.put(key, cnt_map.get(key));
            }
        }

        int res = 0;

        // sliding window
        int l = 0;
        int r = 0;

        Map<String, Integer> tmp_map = new HashMap<>();

        while(r < s_arr.length){

            String right_val = String.valueOf(s.charAt(r));

            while(!cnt_map.containsKey(right_val) && l < r){
                String left_val = String.valueOf(s.charAt(l));
                tmp_map.put(left_val, tmp_map.get(left_val) - 1);
                if(tmp_map.get(left_val) == 0){
                    tmp_map.remove(left_val);
                }
                l += 1;
            }

            tmp_map.put(right_val, tmp_map.get(right_val) + 1);

            if(isValid(tmp_map, k)){
                res = Math.max(res, r - l + 1);
            }

            r += 1;
        }

        return res;
    }

    private boolean isValid(Map<String, Integer> tmp_map, int k){
        for(Integer val: tmp_map.values()){
            if(val < k){
                return false;
            }
        }

        return true;
    }

}
