package dev;

import javax.print.attribute.EnumSyntax;
import java.util.*;

public class workspace14 {
//
//    public class TreeNode {
//        int val;
//        TreeNode left;
//        TreeNode right;
//
//        TreeNode(int x) {
//            val = x;
//        }
//    }

    // LC 297
    // 5.55 - 6.10 pm
    /**
     *
     *
     *
     */
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//            return null;
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//
//            return null;
//        }
//    }

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
  // 18.45 - 18.55 pm
  // IDEA: PQ + HASHMAP
  public String reorganizeString(String s) {

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

      // custom PQ: sort with cnt_map val (val : big -> small)
      PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
              int diff = cnt_map.get(o2) - cnt_map.get(o1);
              return diff;
          }
      });

      //StringBuilder sb = new StringBuilder();
      String res = "";

      // NOTE !!! add `key` to PQ
      pq.addAll(cnt_map.keySet());

      // NOTE !!! we define prev
      String prev = null;

      while (!pq.isEmpty()){

          String val = pq.poll();

          // case 1) prev has val and prev == cur val
          if(prev != null && val == prev){
              // edge case ???
              if(pq.isEmpty()){
                  return "";
              }

              String next = pq.poll();
              res += next;
              cnt_map.put(next, cnt_map.get(next) - 1);
              if(cnt_map.get(next) > 0){
                  pq.add(next);
              }
              pq.add(val);
              // NOTE !!! update `prev`
              prev = next;
          }
          // case 1) prev is null or prev != cur val
          else{
              res += val;
              cnt_map.put(val, cnt_map.get(val) - 1);
              if(cnt_map.get(val) > 0){
                  pq.add(val);
              }
              // NOTE !!! update `prev`
              prev = val;
          }

      }

      return res;
  }




//  public String reorganizeString(String s) {
//
//      // edge
//      if(s.isEmpty()){
//          return "";
//      }
//      if(s.length() <= 2){
//          return s;
//      }
//
//      // { val : cnt }
//      Map<String, Integer> cnt_map = new HashMap<>();
//
//      for(String x: s.split("")){
//          cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
//      }
//
//      // custom PQ: sort with cnt_map val (val : big -> small)
//      PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
//          @Override
//          public int compare(String o1, String o2) {
//              int diff = cnt_map.get(o2) - cnt_map.get(o1);
//              return diff;
//          }
//      });
//
//      //StringBuilder sb = new StringBuilder();
//      String res = "";
//
//      // NOTE !!! add `key` to PQ
//      pq.addAll(cnt_map.keySet());
//
//      while (!pq.isEmpty()){
//          String val = pq.poll();
//          String val2 = null;
//          String toUpdate = null;
//          // case 1) val is NOT same as its neighbor
//          if(!res.isEmpty() && val.equals(res.charAt(res.length()-1))){
//              // pop another `prev` element as well
//              val2 = pq.poll();
//          }
//          if(val2 != null){
//              toUpdate = val2;
//              pq.add(val);
//          }
//          res += toUpdate;
//          cnt_map.put(toUpdate, cnt_map.get(toUpdate) - 1);
//          if(cnt_map.get(toUpdate) == 0){
//              cnt_map.remove(toUpdate);
//          }else{
//              pq.add(toUpdate);
//          }
//
//      }
//
//
//      //return sb.toString();
//      return res;
//  }


//  public String reorganizeString(String s) {
//      //StringBuilder sb = new StringBuilder();
//      // edge
//      if(s.isEmpty()){
//          return "";
//      }
//      if(s.length() <= 2){
//          return s;
//      }
//
//      // { val : cnt }
//      Map<String, Integer> cnt_map = new HashMap<>();
//
//      for(String x: s.split("")){
//          cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
//      }
//
//      // custom PQ: sort on map val  (big -> small)
//      PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
//          @Override
//          public int compare(String o1, String o2) {
//              int diff = cnt_map.get(o2) - cnt_map.get(o1);
//              return diff;
//          }
//      });
//
//      for(String key: cnt_map.keySet()){
//          pq.add(key);
//      }
//
//      String res = "";
//
//      while(!pq.isEmpty()){
//          // get val with `max` cnt
//          String cur_val = pq.poll();
//          // check if `adjacent` are NOT the same
//          if(!res.isEmpty()){
//              String prevVal = String.valueOf(res.charAt(res.length() - 1));
//              if(prevVal.equals(cur_val)){
//                  return "";
//              }
//          }
//
//          res += cur_val;
//
//          // update hashmap
//          cnt_map.put(cur_val, cnt_map.get(cur_val) - 1);
//          // if `cnt` == 0
//          // remove key from hashmap
//          if(cnt_map.get(cur_val) == 0){
//              cnt_map.remove(cur_val);
//          }else{
//              // update PQ: the key still exists, add it back to
//              pq.add(cur_val);
//          }
//      }
//
//      return res;
//    }


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

    // IDEA 2) HASHMAP + SLIDING WINDOW
    public int longestSubstring(String s, int k) {

        // edge
        if (s == null || s.isEmpty() || k > s.length()) {
            return 0;
        }

        Map<String, Integer> cnt_map = new HashMap<>();
        for(int i = 0; i <  s.length(); i++){
           String val = String.valueOf(s.charAt(i));
           cnt_map.put(val, cnt_map.getOrDefault(val, 0) + 1);
        }

        //Map<Character, Integer> map = new HashMap<>();
        int res = 0;

        for (int i = 0; i < s.length(); i++) {
            // reset map
            Map<Character, Integer> map = new HashMap<>();
            int j = i;
            //boolean x = cnt_map.get(s.charAt(j)) > k;
            System.out.println("s.charAt(j) = " + s.charAt(j));
            while (  j < s.length() && cnt_map.get(String.valueOf(s.charAt(j))) >= k ) {
                //Character c = m
                map.put(s.charAt(j), map.getOrDefault(s.charAt(j), 0) + 1);
                if (isValid(map, k)) {
                    res = Math.max(res, j - i + 1);
                }
                j += 1;
            }
        }

        return res;
    }

    public boolean isValid(Map<Character, Integer> map, int k) {
        for (Character c : map.keySet()) {
            if (map.get(c) < k) {
                return false;
            }
        }
        return true;
    }

    // brute force (double loop)
//    public int longestSubstring(String s, int k) {
//
//        // edge
//        if(s == null || s.isEmpty()){
//            return 0;
//        }
//        if(s.length() == 1){
//            if(k <= 1){
//                return 1;
//            }
//            return 0;
//        }
//
//        int res = 0;
//        Map<String, Integer> cnt_map = new HashMap<>();
//        String[] s_arr = s.split("");
//
////        for(int i = 0; i <  s_arr.length; i++){
////            String val = String.valueOf(s.charAt(i));
////            cnt_map.put(val, cnt_map.getOrDefault(val, 0) + 1);
////        }
////
////        Set<String> candidates = new HashSet<>();
////        for(String key: cnt_map.keySet()){
////            System.out.println(" cnt_map.get(key) ?" + cnt_map.get(key) + ", k = " + k);
////            if(cnt_map.get(key) >= k){
////                candidates.add(key);
////            }
////        }
////
////        System.out.println(">>> cnt_map = " + cnt_map);
////        System.out.println(">>> candidates = " + candidates);
//
//
//        for(int i = 0; i < s_arr.length; i++){
//            Map<String, Integer> tmp_map = new HashMap<>();
//            for(int j = i; j < s_arr.length; j++){
//                String val = String.valueOf(s.charAt(j));
////                if(!candidates.contains(val)){
////                    break;
////                }
//                tmp_map.put(val, tmp_map.getOrDefault(val, 0) + 1);
//                if(isValid(tmp_map, k)){
//                    res = Math.max(res, j - i + 1);
//                }
//            }
//        }
//
//        return res;
//    }
//
//    private boolean isValid(Map<String, Integer> tmp_map, int k){
//        for(Integer val: tmp_map.values()){
//            if(val < k){
//                return false;
//            }
//        }
//
//        return true;
//    }




//    public int longestSubstring(String s, int k) {
//        // edge
//        if(s == null || s.isEmpty()){
//            return 0;
//        }
//        if(s.length() == 1){
//            if(k <= 1){
//                return 1;
//            }
//            return 0;
//        }
//
//        // cnt map
//        Map<String, Integer> cnt_map = new HashMap<>();
//        Map<String, Integer> cnt_map2 = new HashMap<>();
//        // last idx map
//        Map<String, Integer> last_idx_map = new HashMap<>();
//
//        String[] s_arr = s.split("");
//        for(int i = 0; i <  s_arr.length; i++){
//            String val = String.valueOf(s.charAt(i));
//            cnt_map.put(val, cnt_map.getOrDefault(val, 0) + 1);
//            last_idx_map.put(val, i);
//        }
//
//        // only keep `cnt > k` key
//        for(String key: cnt_map.keySet()){
//            if(cnt_map.get(key) >= k){
//                cnt_map2.put(key, cnt_map.get(key));
//            }
//        }
//
//        int res = 0;
//
//        // sliding window
//        int l = 0;
//        int r = 0;
//
//        Map<String, Integer> tmp_map = new HashMap<>();
//
//        while(r < s_arr.length){
//
//            String right_val = String.valueOf(s.charAt(r));
//
//            while(!cnt_map.containsKey(right_val) && l < r){
//                String left_val = String.valueOf(s.charAt(l));
//                tmp_map.put(left_val, tmp_map.get(left_val) - 1);
//                if(tmp_map.get(left_val) == 0){
//                    tmp_map.remove(left_val);
//                }
//                l += 1;
//            }
//
//            tmp_map.put(right_val, tmp_map.get(right_val) + 1);
//
//            if(isValid(tmp_map, k)){
//                res = Math.max(res, r - l + 1);
//            }
//
//            r += 1;
//        }
//
//        return res;
//    }
//
//    private boolean isValid(Map<String, Integer> tmp_map, int k){
//        for(Integer val: tmp_map.values()){
//            if(val < k){
//                return false;
//            }
//        }
//
//        return true;
//    }

    // LC 1157
    // 17.32 - 17.42 pm
    /**
     * Your MajorityChecker object will be instantiated and called as such:
     * MajorityChecker obj = new MajorityChecker(arr);
     * int param_1 = obj.query(left,right,threshold);
     */
    /**
     *  Design a data structure that
     *  `efficiently` finds the `majority` element of a given subarray.
     *
     *  -> The majority element of a subarray is an element
     *    that occurs threshold times or more in the subarray.
     *
     *
     *  -> so find # of element in subarray that count >= threshold
     *
     *
     *  IDEA 1) HASHMAP + SLIDE WINDOW
     *
     *
     *  IDEA 2) sub array + hashmap + cnt check
     *
     *
     */
    class MajorityChecker {

        // attr
        int[] cur_arr;
        // { val: cnt}
        Map<Integer, Integer> cnt_map;

        public MajorityChecker(int[] arr) {
            this.cur_arr = arr;
            this.cnt_map = new HashMap<>();
        }

        public int query(int left, int right, int threshold) {
            //int ans = -1;
            // copy sub array
            int[] subArr = Arrays.copyOfRange(this.cur_arr, left, right + 1);
            System.out.println(">>> (query) subArr = " + Arrays.toString(Arrays.stream(subArr).toArray())
                    + ", left = " + left + ", right = " + right);
            // update map
            updateCnt(subArr);
            // get `validated` elements
            return getMajorElement(threshold);
        }

        private void updateCnt(int[] inputArr){
            if(inputArr == null || inputArr.length == 0){
                return;
            }
            // reset map // ???
            this.cnt_map = new HashMap<>();
            System.out.println(">>> (before updateCnt) this.cnt_map = " + this.cnt_map);

            for(int x: inputArr){
                this.cnt_map.put(x, this.cnt_map.getOrDefault(x, 0) + 1);
            }
            System.out.println(">>> (after updateCnt) this.cnt_map = " + this.cnt_map);
        }

        private int getMajorElement(int threshold){
            int ans = -1;
            System.out.println(">>> (getMajorElement) this.cnt_map = " + this.cnt_map);
            for(int key: this.cnt_map.keySet()){
                System.out.println(">>> key = " +  key);
                if(this.cnt_map.get(key) >= threshold){
                    return key;
                }
            }
            return ans;
        }

    }




    // LC 394
    // 9.42 - 9.52 am
    /**  IDEA 1) STACK
     *
     *  STACK (FILO)
     *
     *  ex 1)
     *
     *  Input: s = "3[a]2[bc]"
     *  Output: "aaabcbc"
     *
     *  -> "3[a]2[bc]"
     *      x             st = [3]
     *
     *  -> "3[a]2[bc]"
     *       x         st = [3]
     *
     *  -> "3[a]2[bc]"
     *        x        st = [3], cache = a
     *
     *  -> "3[a]2[bc]"
     *         x       st = [], cache = "", res = "aaa"
     *
     *
     *  -> "3[a]2[bc]"
     *          x    st = [2], cache = ""
     *
     *   -> "3[a]2[bc]"
     *              x    st = [2], cache = "bc"
     *
     *   -> "3[a]2[bc]"
     *               x     cache = "", res = aaabcbc
     *
     *
     *  -------------------------------------
     *
     *    ex 2)
     *
     *    Input: s = "3[a2[c]]"
     *    Output: "accaccacc"
     *
     *
     *   ->
     *
     *    s = "3[a2[c]]"
     *         x          st = [3]
     *
     *   s = "3[a2[c]]"   st = [3, a2]
     *           x
     *
     *   s = "3[a2[c]]"   st = [3, a2, c], res = ""
     *             x
     *
     *   s = "3[a2[c]]"   st = [3], res = "acc"
     *              x
     *
     *   s = "3[a2[c]]"   st = [], res = "accaccacc"
     *               x
     *
     */
    public String decodeString(String s) {
        // edge
        if(s.isEmpty() || s.length() == 1){
            return s;
        }

        StringBuilder sb = new StringBuilder();

        Stack<String> st = new Stack<>();
        //Stack<String> st_num = new Stack<>();

        Stack<String> st_ans = new Stack<>();

        String[] s_arr = s.split("");
        String nums = "0123456789";

        //String cache = "";
        Stack<String> cache = new Stack<>();

        for(int i = 0; i < s_arr.length; i++) {
            String val = s_arr[i];
            if(val.equals("[")){
                while(!cache.isEmpty()){
                    st.add(cache.pop());
                }
                // cut off
                st.add("|");
                cache = new Stack<>();
            }
            else if (val.equals("]")) {
                String prev = st.pop();
                if(!nums.contains(prev)){
                    st_ans.add(prev);
                }else{
                    String tmp = mulitiply(prev, Integer.parseInt(val));
                }

            }else{
                cache.add(val);
            }

        }

        return sb.toString();
    }

    private String mulitiply(String s, int mutiplier){
        String res = "";
        for(int i = 0; i < mutiplier; i++){
            res += s;
        }
        return res;
    }

    // LC 535
    // 16.39 - 16.49 pm
    /**
     *  IDEA 1) decode, encode  + hash
     *
     *
     */
//    public class Codec {
//
//        // attr
//        String baseLong = "https://leetcode.com/problems";
//        String baseShort = "http://tinyurl.com";
//
//        // { val : encode_val }
//        //Map<String, Integer> map = new HashMap<>(); // ???
//
//        // Encodes a URL to a shortened URL.
//        public String encode(String longUrl) {
//
//            // edge
//            if(longUrl == null){
//                return baseShort;
//            }
//            String input = longUrl.split(baseLong)[1];
//            StringBuilder output = new StringBuilder();
//
//            System.out.println(">>> (encode) input = " + input);
//
//            for(String x: input.split("")){
//                // ???
//                int diff =  x.charAt(0) - 'a'; // ???
//                output.append(String.valueOf(diff));
//               // map.put(x, diff);
//            }
//
//            System.out.println(">>> (encode) res = " + baseShort + "/" + output);
//            return baseShort + "/" + output;
//        }
//
//        // Decodes a shortened URL to its original URL.
//        public String decode(String shortUrl) {
//            // edge
//            if(shortUrl.equals(baseShort)){
//                return null;
//            }
//
//            String input = shortUrl.split(baseShort)[1];
//            StringBuilder output = new StringBuilder();
//
//            System.out.println(">>> (decode) input = " + input);
//
//            // fix below
//            for(String x: input.split("")){
//
//                // ???
//                // x - a = diff
//                // -> x = diff + a
//                char val = (char)('a' + Integer.parseInt(x));  //
//                output.append(String.valueOf(val));
//            }
//
//            System.out.println(">>> (decode) res = " + baseShort + "/" + output);
//            return baseLong + "/" + output;
//        }
//    }

    // LC 297
    // Definition for a binary tree node.
    // 4.21 - 4.32 pm
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     *  IDEA 1) DFS
     *
     *  IDEA 2) DFS
     *
     *
     */
    // IDEA 2) BFS
    public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            // edge
            if(root == null){
                return "|"; // ??
            }

            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);

            while(!q.isEmpty()){
                // root -> left sub -> right sub
                TreeNode node = q.poll();
                // NOTE !!! below
                if(node == null){
                    sb.append("|");
                    continue; // NOTE !!!
                }
                sb.append(node.val);
                sb.append(","); // ??
//                // ??
//                if(node.left != null){
//                    q.add(node.left);
//                }
//                if(node.right != null){
//                    q.add(node.right);
//                }
                q.add(node.left);
                q.add(node.right);
            }

            return sb.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            // edge
            if(data.equals("|")){
                return null;
            }
            String[] arr = data.split(",");
            Queue<String> q = new LinkedList<>();
            // ???
            for(String x: arr){
                q.add(x);
            }

            // ???
            TreeNode res = new TreeNode(Integer.parseInt(q.poll()));
            while(!q.isEmpty()){
                String parent = q.poll();
//                if(parent.equals("|")){
//                }
//                res.left = new TreeNode(Integer.parseInt(val));

                // left
              //  if()

                // right
            }


            return res;
        }
    }

    // IDEA 1) DFS
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//            // edge
//            if(root == null){
//                return "|"; // ??
//            }
//            return root + ","
//                    + serialize(root.left) + ","
//                    + serialize(root.right);
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//            // edge
//            if(data.equals("|")){
//                return null;
//            }
//            Queue<String> q = new LinkedList<>();
//            for(String x: data.split(",")){
//                q.add(x);
//            }
//            return deserializeHelper(q);
//        }
//
//        public TreeNode deserializeHelper(Queue<String> queue) {
//            // root -> left sub node -> right sub node
//            String val = queue.poll();
////            if(queue.isEmpty()){
////                return null; // ??
////            }
//            if(val.equals("|")){
//                return null; // ???
//            }
//            TreeNode node = new TreeNode(Integer.parseInt(val));
//            node.left = deserializeHelper(queue);
//            node.right = deserializeHelper(queue);
//
//            // ???
//            return node;
//        }
//
//    }



//    public class Codec {
//
//        StringBuilder sb;
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//            // edge
//            if(root == null){
//                return ",";
//            }
//            if(root.left == null && root.right == null){
//                return String.valueOf(root.val);
//            }
//
//            sb = new StringBuilder();
//            // bfs
//            // save tree `layer by layer`
//            // node -> left-sub-node -> right->sub-node
//            Queue<TreeNode> q = new LinkedList<>();
//            q.add(root);
//
//            int q_size = 0;
//
//            while (!q.isEmpty()){
//                // cache cur queue size
//                q_size = q.size();
//
//                for(int i = 0; i < q_size; i++){
//                    // loop over all nodes in `current layer`
//                    TreeNode node = q.poll();
//                    sb.append(node.val);
//                    sb.append(",");  // split each node val
//
//                    if(node.left != null){
//                        q.add(node.left);
//                    }
//                    if(node.right != null){
//                        q.add(node.right);
//                    }
//
//                  //  sb.append("|"); // end of layer
//                }
//
//            }
//
//            return sb.toString();
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//            // edge
//            if(data.equals(",")){
//                return null;
//            }
//            if(data.length() == 1){
//                // ???
//                return new TreeNode(Integer.parseInt(data));
//            }
//
//
//            // ???
//            Queue<String> tmpQ = new LinkedList<>();
//            String str = sb.toString();
//            for(String x: str.split(",")){
//                tmpQ.add(x);
//            }
//
//            // TreeNode res = new TreeNode(0);
//            return deserialHelper(tmpQ); /// ????
//        }
//    }
//
//    public TreeNode deserialHelper(Queue<String> q){
//        // edge
//        if(q.isEmpty()){
//            return null;
//        }
//
//        // queue: [ "a,b,c", "d,e,f",  ...)
//        String val = q.poll();
//        //String[] val_arr = val.split(",");
//        // ???
//        //TreeNode node = new TreeNode(Integer.parseInt(val_arr[0]));
//        TreeNode node = new TreeNode(Integer.parseInt(val));
////        node.left = new TreeNode(Integer.parseInt(val_arr[1]));
////        node.right = new TreeNode(Integer.parseInt(val_arr[2]));
//        node.left = deserialHelper(q);
//        node.right =  deserialHelper(q);
//
////        for(String x: val.split(",")){
////            TreeNode node = new TreeNode(Integer.parseInt(x));
////            node.left =
////        }
//
//        return node;
//    }

    // Your Codec object will be instantiated and called as such:
    // Codec ser = new Codec();
    // Codec deser = new Codec();
    // TreeNode ans = deser.deserialize(ser.serialize(root));


    // LC 772
    // 9.01 - 9.11 am
    /**
     *  IDEA 1) STACK
     *
     *    1. () first
     *    2. if "+", "-", pop prev, combine with it
     *    3. if *, /, pop prev, combine with it
     *    4.
     *
     *  exp 1)
     *
     *   6 - 4  / 2
     *
     *   6 - 4  / 2 ,  st = [6]
     *   x
     *
     *   6 - 4  / 2 ,  st = [6, -]
     *     x
     *
     *   6 - 4  / 2 ,  st = [6, -4]
     *       x
     *
     *    ....
     *
     *    6 - 4  / 2 ,  st = [6, -4 , /]
     *           x
     *
     *   6 - 4  / 2 ,  st = [6, -2]
     *            x
     *
     *   -> ans = 6 + (-2) = 4
     *
     *
     *  exp 2)  "2*(5+5*2)/3+(6/2+8)"
     *
     *    2*(5+5*2)/3+(6/2+8),  st = [2]
     *    x
     *
     *    2*(5+5*2)/3+(6/2+8),  st = [2, *]
     *     x
     *
     *   2*(5+5*2)/3+(6/2+8),  st = [2, *, ( ]
     *     x
     *
     *    2*(5+5*2)/3+(6/2+8),  st = [2, *, ( , 5, ]
     *       x
     *
     *      ...
     *     2*(5+5*2)/3+(6/2+8),  st = [2, *, ( , 5, ]
     *
     */
    public int calculate(String s) {

        return 0;
    }

    // LC 451
    // 3.06 - 3.16 pm
    /**
     * IDEA: HASHMAP + PQ
     *
     */
    public String frequencySort(String s) {
        // edge
        if(s.isEmpty()){
            return null;
        }
        if(s.length() == 1){
            return s;
        }

        // map : {val : cnt}
        Map<String, Integer> cnt_map = new HashMap<>();
        for(String x: s.split("")){
            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);
        }

        // pq [v1, v2..] : sort on map val, big -> small
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = cnt_map.get(o2) - cnt_map.get(o1);
                return diff;
            }
        });

        for(String k: cnt_map.keySet()){
            pq.add(k);
        }

        StringBuilder sb = new StringBuilder();

        while(!pq.isEmpty()){
            String key = pq.poll();
            sb.append(mutiply(key, cnt_map.get(key)));
        }

        return sb.toString();
    }

    private String mutiply(String s, int times){
        String res = "";
        for(int i = 0; i < times; i++){
            res += s;
        }
        return res;
    }

    // LC 973
    // 3.32 - 3.42 pm
    /**
     *
     *  Given an array of points where points[i] = [xi, yi]
     *  represents a point on the X-Y plane and an integer k,
     *  -> `return the "k closest points" to the origin (0, 0).`
     *
     *  IDEA 1) PQ
     *   -> step 1) calculate all point dis (point VS (0,0)),
     *      save as ( dist : [[x1,y1], [x2,y2], ....] )  in hashmap
     *
     *      step 2) sort above (small -> big)
     *
     */
    public int[][] kClosest(int[][] points, int k) {
        // edge
        if(points == null || points.length == 0){
            return null;
        }
        if(points.length == 1){
            return points; // ???
        }

        // map : {dist : [[x1,y1], [x2,y2], ....] }
        //Map<String, Integer> dist_map = new HashMap<>();
        Map<Integer, List<Integer[]>> dist_map = new HashMap<>();
        for(int[] p: points){
            int dist = p[0] * p[0] + p[1] * p[1];
            List<Integer[]> tmp = new ArrayList<>();
            if(dist_map.containsKey(dist)){
                tmp = dist_map.get(dist);
            }
            tmp.add(dist, new Integer[]{p[0], p[1]});
            dist_map.put(dist, tmp);
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for(Integer key: dist_map.keySet()){
            pq.add(key);
        }

        System.out.println(">>> pq = " + pq);

        int[][] res = new int[k][2]; // ???
        int i = 0;
        while(i < k && !pq.isEmpty()){
            Integer val = pq.poll();
            List<Integer[]> list = dist_map.get(val);
            for(int j = 0; i < list.size(); j++){
                res[i][0] = list.get(j)[0];
                res[i][1] = list.get(j)[1];
                i += 1;
            }
        }

        return res;
    }

    // LC 003
    // 3.00 - 3.10 pm
    /**
     *
     *  Given a string s,
     *  find the `length` of the `longest` substring
     *  without `duplicate` characters.
     *
     *  NOTE:
     *   A substring is a contiguous non-empty sequence
     *   of characters within a string.
     *
     *
     *
     *  IDEA 1) hashset + slide window
     *
     *
     *
     */
    public int lengthOfLongestSubstring(String s) {
        // edge
        if(s.isEmpty()){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        if(s.length() == 2){
            if(s.charAt(0) == s.charAt(1)){
                return 1;
            }
            return 2;
        }

        int max_len = 0;
        HashSet<String> set = new HashSet<>();
        String[] s_arr = s.split("");

        int l = 0;
        int r = 0;
        while(r < s_arr.length && r >= l){
            String x = s_arr[r];
            // NOTE!!!
            while(!set.isEmpty() && set.contains(x)){
                //String to_remove = s_arr[l];
                set.remove(s_arr[l]); // ???
                l += 1;
            }

            set.add(x);
            max_len = Math.max(max_len, r - l + 1);
            r += 1;
        }

        return max_len;
    }


    // LC 329
    // 3.19 - 3.29 pm
    /**
     *
     *
     *
     */
    public int longestIncreasingPath(int[][] matrix) {

        return 0;
    }

    // LC 125
    // 3.30 - 3.40 pm
    /**
     *
     *  IDEA 1) 2 POINTERS
     */
    public boolean isPalindrome(String s) {
        // edge
        if(s.isEmpty()){
            return true;
        }
        // ???
//        if(s.equals("")){
//            return true;
//        }
        if(s.length() == 1){
            return true;
        }

        StringBuilder sb = new StringBuilder();
        String alpha = "abcdefghijklmonpqrstuvwxyz"; // ??
        String nums = "0123456789";
        for(String x: s.split("")){
            boolean shouldAdd = alpha.contains(x.toLowerCase()) || nums.contains(x);
            if(shouldAdd){
                if(alpha.contains(x.toLowerCase())){
                    sb.append(x.toLowerCase());
                }else{
                    sb.append(x);
                }
            }
        }

        System.out.println(">>> str = " + sb.toString());
        return sb.toString().contentEquals(sb.reverse());
    }

    // LC 680
    // 3.45 - 3.55 pm
    /**
     * Given a string s, return `true` if the s can be
     * `palindrome` after deleting `at most` one character from it.
     *
     *
     *  IDEA 1) 2 POINTER ??
     *
     *   ->
     *   step 1) check if already `palindrome`
     *   step 2) left, right pointer
     *      keep moving, and check if can `remove` a single element make it as palindrome
     *      else, return false directly ??
     *
     */
    public boolean validPalindrome(String s) {
        // edge
        if(s.isEmpty()){
            return true;
        }
        if(s.length() == 1){
            return true;
        }
        // ??
//        if(s.length() == 3){
//            if(s.charAt(0) != s.charAt(2)){
//                return false;
//            }
//        }
        // check if already `palindrome`
        if(checkIsPalindrome(s)){
            return true;
        }

        int l = 0;
        int r = s.length() - 1;
        int cnt = 0;
        while(r > l){
            // ???
            if(cnt > 1){
                return false;
            }
            if(s.charAt(l) != s.charAt(r)){
                /**  NOTE !!!
                 *
                 *  the ONLY case that we can remove `1` element
                 *  make string to palindrome is as below:
                 *
                 *   input = "abca"
                 *
                 *   -> so ONLY l, r pointer diff is 1
                 *     otherwise, we need to remove both
                 *     l, and r pointer element than
                 *     be able to make string to palindrome
                 *
                 */
                if(r - l > 1){
                    return false;
                }
                cnt += 1;
            }
            r -= 1;
            l += 1;
        }

        return true;
    }

    private boolean checkIsPalindrome(String s){
        // edge
        if(s.isEmpty()){
            return true;
        }
        int l = 0;
        int r = s.length() - 1;
        while(r > l){
            if(s.charAt(l) != s.charAt(r)){
                return false;
            }
            r -= 1;
            l += 1;
        }
        return true;
    }

    // LC 005
    // 7.46 - 7.56 AM
    /**
     *  Given a string s,
     *  return the longest `palindromic` (回文) `substring` in s.
     *
     *  substr: A substring is a contiguous non-empty sequence of characters within a string.
     *
     *
     *  IDEA 1)  1 pointer (as middle, expand left, right) + odd, even sub string check
     *
     *  IDEA 2)  2 pointer, l, r ???
     *
     *  IDEA 3) recursion ????
     *
     *
     */
    // IDEA 1)  1 pointer + odd, even sub string check
    public String longestPalindrome(String s) {

        // edge
        if(s.isEmpty()){
            return null;
        }
        if(s.length() == 1){
            return s;
        }
        String res = "";
        int[] tmp1 = new int[2];
        int[] tmp2 = new int[2];
        for(int i = 0; i < s.length(); i++){
            // single
            String cur = String.valueOf(s.charAt(i));
            System.out.println(">>> cur = " + cur);
            if(cur.length() > res.length()){
                res = cur;
            }

            // odd
            /**
             *    bab
             *     x
             */
            tmp1 = longestPalindromeHelper(s, i-1, i+1);
            String str1 = String.copyValueOf(s.toCharArray(), tmp1[0], tmp1[1]);
            System.out.println(">>> tmp1 = " + tmp1 + ", str1 = " + str1);
            if(str1.length() > res.length()){
                res = str1;
            }
            // even
            /**
             *   bb
             */
            tmp2 = longestPalindromeHelper(s, i, i+1);
            String str2 = String.copyValueOf(s.toCharArray(), tmp2[0], tmp2[1]);
            System.out.println(">>> tmp2 = " + tmp2 + ", str2 = " + str2);
            if(str2.length() > res.length()){
                res = str2;
            }
        }

        return res;
    }

    private int[] longestPalindromeHelper(String x, int l, int r){
        if(x.isEmpty()){
            return null;
        }
        int[] res = new int[2];
        while(r > l && l >= 0 && r < x.length()){
            if(x.charAt(l) != x.charAt(r)){
                return res;
            }
            res[0] = l;
            res[1] = r;
            l -= 1;
            r += 1;
        }

        return res;
    }







    // LC 167
    // 7.23 - 7.33 am
    /**
     *  IDEA 1) HASH MAP (same as 2 sum )
     *
     *  IDEA 2) BINARY SEARCH
     *
     *
     */
    // IDEA 3) 2 POINTERS
    public int[] twoSum(int[] numbers, int target) {
        // edge
        if (numbers == null || numbers.length == 0) {
            return null;
        }
        if (numbers.length == 1) {
            return null;
        }
        // {val : idx}
        //Map<Integer, Integer> map = new HashMap<>();

        int[] res = new int[2];

        int l = 0;
        int r = numbers.length - 1;

        // 2 pointer
        // use the fact that `arr is in INCREASING ORDER`
        while(r >= l){
            int _sum = numbers[l] + numbers[r];
            if(_sum == target){
                // ????
                res[0] = l + 1;
                res[1] =  r + 1;
                return res;
            }else if(_sum < target){
                l += 1;
            }else{
                r -= 1;
            }
        }

        return res;
    }

    // IDEA 2) BINARY SEARCH
//    public int[] twoSum(int[] numbers, int target) {
//        if (numbers == null || numbers.length == 0) {
//            return null;
//        }
//        if (numbers.length == 1) {
//            return null;
//        }
//
//        int[] res = new int[2];
//
//        // {val : idx}
//        Map<Integer, Integer> map = new HashMap<>();
////        for (int i = 0; i < numbers.length; i++) {
////            int x = numbers[i];
////            map.put(x, i + 1);
////        }
//
//        // binary search
//        int l = 0;
//        int r = numbers.length - 1;
//        while(r >= l){
//            int mid = ( l + r ) / 2;
//            int val = numbers[mid];
//            // x + y = t
//            // y = t - x
//            int diff = target - val;
//            if(map.containsKey(diff)){
//                res[0] = map.get(diff);
//                res[1] = mid + 1;
//                return res;
//            }
//            // too big
//            else if(diff < 0){
//                r = mid - 1;
//            }
//            // too small
//            else{
//                l = mid + 1;
//            }
//
//            // update visited element
//            map.put(val, mid + 1);
//        }
//
//        return res;
//    }






    // IDEA 1) HASH MAP (same as 2 sum )
    public int[] twoSum_(int[] numbers, int target) {
        // edge
        if (numbers == null || numbers.length == 0) {
            return null;
        }
        if (numbers.length == 1) {
            return null;
        }
        // {val : idx}
        //Map<Integer, Integer> map = new HashMap<>();

        int[] res = new int[2];

        // {val : idx}
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            // x + y = t
            // y = t - x
            int x = numbers[i];
            int diff = target - x;
            if (map.containsKey(diff)) {
                res[0] = map.get(diff);
                res[1] = i + 1;
                return res;
            }
            map.put(x, i + 1);
        }

        return res;
    }

    // LC 35
    // 10.14 - 10.24 am
    /**
     *  IDEA 1) BINARY SEARCH
     *
     */
    public int searchInsert(int[] nums, int target) {
        // edge
        if(nums == null){
            return 0; // ??
        }
        if(nums.length == 1){
            if(target > nums[0]){
                return 1;
            }
            return 0;
        }
        if(target < nums[0]){
            return 0;
        }
        if(target > nums[nums.length - 1]){
            return nums.length;
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;
        // r >= l
        while( r >= l ){
            int mid = (r + l) / 2;
            int val = nums[mid];

            System.out.println(">>> l = " + l + ", r = " + r + ", mid = " + mid);

            if(val == target){
                return mid;
            }
            // if target NOT in nums
            // and is in `mid + 1 ` index
            else if(mid +1 <= nums.length - 1 && target < nums[mid+1] && target > nums[mid]){
                // ??? if target NOT in nums
               // return mid;
                return mid + 1;
            }
            // if target NOT in nums
            // and is in `mid - 1 ` index
            else if(mid - 1 >= 0 && target < nums[mid] && target > nums[mid - 1]){
                // ??? if target NOT in nums
                return mid;
            }
            else if (val < target){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        return -1; //
    }

    // LC 278
    /**
     *  IDEA 1) BINARY SEATCH
     *
     *  -> r > l ??
     *  -> find `first xxx`
     *  -> so l, r is NOT going to overlap
     *  -> so we can scan
     *
     */
    public int firstBadVersion(int n) {
        // edge
        if(n <= 1){
            return n; // ??
        }
        int l = 1; // 0;
        int r = n;

        while (r > l){
            int mid = (l + r) / 2;
            System.out.println(">>> l = " + l + ", r = " + r + " mid = " + mid);
            // [0, mid] are `OK`
            if(!isBadVersion(mid)){
                if(isBadVersion(mid + 1)){
                    return mid + 1;
                }
                l = mid + 1;
            }
            // mid or some idx within mid - end are `bad`
            else{
                r = mid; // /??
            }
        }

        return l; //???
    }

    private Boolean isBadVersion(int n) {
        return true;
    }


    // LC 34
    // 14,09 - 14.19 pm
    /**
     *  Given an array of integers nums `sorted` in `non-decreasing` order,
     * find the starting and ending position of a given target value.
     *
     * If target is not found in the array, return [-1, -1].
     *
     * You must write an algorithm with O(log n) runtime complexity.
     *
     *
     * 1. increasing arr
     * 2. find the start, end idx with target val
     * 3. return [-1, -1] if NOT found
     *
     *
     *   IDEA 1) BINARY SEARCH
     *
     *   IDEA 2) BRUTE FORCE + HASHMAP
     *
     */
    // IDEA 1) BINARY SEARCH
    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[] {-1, -1};
        // edge
        if(nums == null){
            return res;
        }
        if(nums.length == 1){
            if(nums[0] == target){
                res[0] = 0;
                res[1] = 1;
            }
            return res;
        }

        // binary search
        // r > l ???
        int l = 0;
        int r = nums.length - 1;
        while(r > l){
            int mid =  (l + r) / 2;
            int val = nums[mid];
            // case 1) target reached
            if(val == target){
                // ???
                int idx1 = mid - 1;
                int idx2 = mid + 1;
                boolean shouldMoveLeft = idx1 >= 0 && nums[idx1] == target;
                boolean shouldMoveRight = idx2 <= nums.length -1 && nums[idx2] == target;
                while(shouldMoveLeft || shouldMoveRight){
                    if(shouldMoveLeft){
                        idx1 -= 1;
                    }
                    if (shouldMoveRight) {
                        idx2 += 1;
                    }
                }
                res[0] = idx1;
                res[1] = idx2;
                return res;
            }else if (val < target){
                l = mid + 1;
            }else{
                r = mid;
            }
        }

        return res; // ???
    }

    // LC 42
    // 9.06 - 9.20 am
    /**
     *  IDEA 1) 2 pointers + STACK
     *
     *   -> left, right pointer
     *   -> get `area` when `right > left`
     *   -> sum over all `area` as ans
     *   -> slide window ???
     *   -> maintain a window while
     *     - left <= right
     *     - otherwise, get `all areas` within window
     *   - NOT get area at `0 idx`
     *
     *   exp 1)
     *
     *   Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
     *   Output: 6
     *
     *
     *   [0,1,0,2,1,0,1,3,2,1,2,1]  ans = 0
     *    x
     *
     *   [0,1,0,2,1,0,1,3,2,1,2,1]  ans = 0, st = [1], max_l = 1
     *      x
     *
     *   [0,1,0,2,1,0,1,3,2,1,2,1]  ans = 0, st = [1,0], max_l = 1
     *        x
     *
     *  [0,1,0,2,1,0,1,3,2,1,2,1]  ans = 0,  2 > max_l, st = [], ans = (left_l - cur_hight * 1 ) = 1
     *         x
     *
     */
    public int trap(int[] height) {
        // edge
        if(height.length <= 2){
            return 0;
        }
//        // ??
//        if(height.length == 3){
//            return 0;
//        }

        int ans = 0;
        Stack<Integer> st = new Stack<>();
        //int l = 0;
        //int r = 0;

        //int l_height = Integer.MAX_VALUE; // ???
        Stack<Integer> st_left_height = new Stack<>();

        // NOTE !!! idx starts from 1
        for(int r = 1; r < height.length; r++){
            int val = height[r];
            // case 1) st is empty
            if(st.isEmpty()){
                //l_height = val;
                st_left_height.add(val);
                st.add(val);
            }
            // case 2) cur val < l_height
            else if (val < st_left_height.peek()){
                st_left_height.add(val);
                st.add(val);
            }
            // case 3) cur val > l_height
            else{
                // get `area`
                while(!st.isEmpty()){
                    // pop `1st element` from st_left_height
                    int l_height = st_left_height.pop();
                    //int tmp = st.pop() * l_height;
                    ans += (st.pop() * l_height);
                }
                // add cur val to st
                st.add(val);
               // l_height = val;
                st_left_height.add(val);
            }
        }

        return ans;
    }


}
