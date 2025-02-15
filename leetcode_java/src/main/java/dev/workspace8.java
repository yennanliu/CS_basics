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

  // LC 567
  // 4.04 - 4.19 pm
  /**
   *
   * Given two strings s1 and s2, return true if s2 contains a
   * permutation of s1, or false otherwise.
   *
   * In other words, return true if one of
   * s1's permutations is the substring of s2.
   *
   * -> A permutation is a rearrangement of all the characters of a string.
   *
   */
  public boolean checkInclusion(String s1, String s2) {
      if(!s1.isEmpty() && s2.isEmpty()){
          return false;
      }
      if(s1.equals(s2)){
          return true;
      }
      Map<String, Integer> map1 = new HashMap<>();
      Map<String, Integer> map2 = new HashMap<>();
      for(String x: s1.split("")){
          String k = String.valueOf(x);
          map1.put(x, map1.getOrDefault(k, 0 ) + 1);
      }

      // 2 pointers (for s2)
      int l = 0;
      //int r = 0;
      for(int r = 0; r < s2.length(); r++){
          String val = String.valueOf(s2.charAt(r));
          map2.put(val, map2.getOrDefault(val, 0) + 1);

          // NOTE !!! below trick
          if(map2.equals(map1)){
              return true;
          }
          // NOTE !!! if reach below, means a permutation string is NOT FOUND YET
          if( (r - l + 1) >= s1.length() ){
              // update map
              String leftVal = String.valueOf(s2.charAt(l));
              map2.put(leftVal, map2.get(leftVal) - 1);
              l += 1; // ?
              if(map2.get(leftVal) == 0){
                  map2.remove(leftVal);
              }
          }
      }

      return false;
  }

  //  public boolean checkInclusion(String s1, String s2) {
  //      // edge
  //      if(s1.equals(s2)){
  //          return true;
  //      }
  //      if(s2.contains(s1) || s2.isEmpty()){
  //          return true;
  //      }
  //      if(s1.isEmpty() && !s2.isEmpty()){
  //          return false;
  //      }
  //      // ??
  //      if(s1.length() > s2.length()){
  //          return false;
  //      }
  //
  //      // map: {val, cnt}
  //      Map<String, Integer> map_s1 = new HashMap<>();
  //      Map<String, Integer> map = new HashMap<>();
  //
  //      for(String x: s1.split("")){
  //          map_s1.put(x, map_s1.getOrDefault(x, 0) + 1);
  //      }
  //      System.out.println(">>> map_s1 = " + map_s1);
  //
  //      int s_1_idx = 0;
  //      int s_2_r_idx = 0;
  //      int s_2_l_idx = 0;
  //      String cache = "";
  //      while (s_2_r_idx < s2.length() && s_1_idx < s1.length()){
  //          String s2Val = String.valueOf(s2.charAt(s_2_r_idx));
  //          if(!map_s1.containsKey(s2Val)){
  //              cache = "";
  //              map = new HashMap<>();
  //              //s_1_idx = s_2_idx;
  //              s_2_l_idx += 1;
  //              s_2_r_idx = s_2_l_idx;
  //          }else{
  //              map.put(s2Val, map.getOrDefault(s2Val, 0) + 1);
  //              if(map.get(s2Val) <= map_s1.get(s2Val)){
  //                  cache += s2Val;
  //                  if(cache.length() == s1.length()){
  //                      return true;
  //                  }
  //                  s_1_idx += 1;
  //              }else{
  //                  cache = "";
  //                  map = new HashMap<>();
  //              }
  //          }
  //          s_2_r_idx += 1;
  //      }
  //
  //      return false;
  //    }

  // LC 76
  // 5.25 - 5.35 pm
  /**
   * Given two strings s and t of lengths m and n respectively,
   * return the minimum window substring
   * of s such that every character in t
   * (including duplicates) is included in the window.
   * If there is no such substring, return the empty string "".
   *
   * The testcases will be generated such that the answer is unique.
   *
   *
   * A substring ->
   * is a contiguous non-empty sequence of characters within a string.
   *
   *
   *
   * IDEA : 2 POINTERS
   *
   * -> l, r
   * -> once l `reach 1st char in t`, then we stop l,
   * -> then we start r, till `all sub str` covers char in t, record the len
   * -> then we move l to `next char in t in s`, move r
   * -> ... repeat above steps, and get min len
   * -> return min len
   */
  public String minWindow(String s, String t) {

      return null;
    }

}
