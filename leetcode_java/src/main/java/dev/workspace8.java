package dev;

import java.util.HashMap;
import java.util.Map;

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

}
