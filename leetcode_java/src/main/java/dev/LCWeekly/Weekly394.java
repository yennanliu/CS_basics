package dev.LCWeekly;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LeetCode weekly contest 394
 * https://leetcode.com/contest/weekly-contest-394/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-394/
 *
 */
public class Weekly394 {

    // Q1
    // LC 3120
    // https://leetcode.com/problems/count-the-number-of-special-characters-i/
    // 15.10 - 20 pm
    /**
     *
     *  -> Return the `number` of `special letters` in `word.`
     *
     *   - word
     *   -  A letter is called special if
     *      it appears both in lowercase and uppercase in word.
     *
     *
     *   -------------
     *
     *  IDEA 1) HASHMAP OR SET
     *
     *
     */
    public int numberOfSpecialChars_1(String word) {
        // edge
        if(word == null || word.isEmpty()){
            return 0;
        }
        if(word.length() == 1){
            return 0;
        }
        //Set<String> set = new HashSet<>();
        // { val : [lower_idx, upper_idx]

        String lowerAlpha = "abcdefghijklmnopqrstuvwxyz";

        Map<Character, Integer[]> map = new HashMap<>();
        int cnt = 0;
        for(char ch: word.toCharArray()){
            if(!map.containsKey(ch)){
                map.put(ch, new Integer[2]);
            }
            String s = String.valueOf(ch);
            Integer[] arr = map.get(ch);
            // check if lower or upper
            if(lowerAlpha.contains(s)){
                arr[0] = 1;
            }else{
                arr[1] = 1;
            }
        }

        // check
        for(Integer[] x: map.values()){
            if(x[0] == 1 && x[1] == 1){
                cnt += 1;
            }
        }

        return cnt;
    }


    // Q2
    // LC 3121
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/
    // 15.15 - 25 pm
    /**
     *
     * -> Return the number of special letters in word.
     *
     *   - word
     *   - c: letter
     *   - is `special` if
     *      1) exists BOTH lower, upper case in `word`
     *      2) EVERY lower case c appears BEFORE
     *          the first UPPERCASE of c
     *
     *
     *  ---------------
     *
     *   IDEA 1)  HASHMAP + SLIDE WINDOW ???
     *
     *   // map: record all idx of occurrence of a letter c
     *   HASHMAP: { val : [idx1, idx2, ...] }
     *   -> and for each val,
     *       we check if it's special
     *
     *
     *   ---------------
     *
     *
     *
     *
     */
    public int numberOfSpecialChars(String word) {

        return 0;
    }




    // Q3
    // LC 3122
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/


    // Q4
    // LC 3123
    // https://leetcode.com/problems/find-edges-in-shortest-paths/description/


}
