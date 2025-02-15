package dev;

import java.awt.*;
import java.util.*;
import java.util.List;

public class workspace8 {

  // LC 003
  // https://leetcode.com/problems/longest-substring-without-repeating-characters/
  // 2.31 - 2.40 pm
  /**
   * Given a string s, find the length of the longest
   * `substring` without duplicate characters.
   *
   * -> A substring is a contiguous non-empty
   *    sequence of characters within a string.
   *
   *
   * IDEA: hashmap + sliding window
   *
   */
  public int lengthOfLongestSubstring(String s) {
      // edge
      if(s == null || s.length() == 0){
          return 0;
      }
      if(s.length() == 1){
          return 1;
      }
      /**
       * map : {val: min_idx}
       */
      Map<String, Integer> map = new HashMap<>();
      int res = 1;
      int l = 0;
      for(int r = 0; r < s.length(); r++){
          System.out.println(">>> l = " + l + ", r = " + r + ", map = " + map + ", res = " + res);
          String k = s.split("")[r];
          // NOTE !!! if `duplicated val is meet`
          while (map.containsKey(k)){
              // keep moving `left` idx, till NO duplicated val is found
              map.put(k, map.get(k) - 1);
              if (map.get(k) == 0){
                  map.remove(k);
              }
              l += 1;
          }
          if (map.isEmpty() || !map.containsKey(k)){
              map.put(k, r);
              res = Math.max(res, r - l + 1);
          }
      }

      return res;
    }

    // LC 424
    // IDEA: HASHMAP + 2 POINTERS
    public int characterReplacement(String s, int k) {
        // edge
        if(s == null || s.length() == 0){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        // map: {val, cnt}
        Map<String, Integer> map = new HashMap<>();
        int l = 0;
        int res = 1;
        for(int r = 0; r < s.length(); r++){
            String key = s.split("")[r];
            map.put(key, map.getOrDefault(key, 0) + 1);
            if(map.containsKey(key)){
                // NOTE !!!
                // if (r - l  + 1)  > k
                // -> there are still `more than k different val
                // -> so we CAN'T  get `same character sub string` via k times modification
                while ( (r - l  + 1) - getMaxCnt(map) > k ){
                   String pevK = s.split("")[l];
                   map.put(pevK, map.get(pevK) - 1);
                   if(map.get(pevK) == 0){
                       map.remove(pevK);
                   }
                   l += 1;
                }
            }
            res = Math.max(res, r - l + 1);
        }

        return res;
    }

    private int getMaxCnt(Map<String, Integer> map){
        int res = 0;
        for(Integer x: map.values()){
            res = Math.max(res, x);
        }
        return res;
    }
//    public int characterReplacement(String s, int k) {
//      // edge
//      if(s == null || s.length() == 0){
//          return 0;
//      }
//      if(s.length() == 1){
//         return 1;
//      }
//      // map: {val, cnt}
//      Map<String, Integer> map = new HashMap<>();
//      int res = 0;
//      int l = 0;
//      for(int r = 0; r < s.length(); r++){
//          String key = s.split("")[r];
//          String prev = s.split("")[r];
//          map.put(key, map.getOrDefault(key, 1)+1);
//          if(map.keySet().size() > 1 && getMapSecondCnt(map) > k){
//              // if most cnt > move l
//              //l = Math.max(map.get(key), r);
//              l = r;
//              // reset map
//              map = new HashMap<>();
//          }
//          res = Math.max(res, r - l + 1);
//      }
//      return res;
//    }
//
//    private int getMapSecondCnt(Map<String, Integer> map){
//        List<Integer> values = new ArrayList<>();
//        for(Integer x: map.values()){
//            values.add(x);
//        }
//
//        System.out.println(">>> (before)  values = " + values);
//        Collections.sort(values, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o2 - o1;
//            }
//        });
//
//        System.out.println(">>> (after)  values = " + values);
//        return values.get(1); // get 2nd biggest
//    }

}
