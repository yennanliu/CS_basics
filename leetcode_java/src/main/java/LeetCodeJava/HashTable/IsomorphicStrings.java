package LeetCodeJava.HashTable;

// https://leetcode.com/problems/isomorphic-strings/

import java.util.HashMap;

/**
 * 205. Isomorphic Strings
 * Easy
 *
 * Given two strings s and t, determine if they are isomorphic.
 *
 * Two strings s and t are isomorphic if the characters in s can be replaced to get t.
 *
 * All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters may map to the same character, but a character may map to itself.
 *
 * Example 1:
 *
 * Input: s = "egg", t = "add"
 * Output: true
 * Explanation:
 *
 * The strings s and t can be made identical by:
 *
 * Mapping 'e' to 'a'.
 * Mapping 'g' to 'd'.
 *
 * Example 2:
 *
 * Input: s = "f11", t = "b23"
 * Output: false
 * Explanation:
 *
 * The strings s and t can not be made identical as '1' needs to be mapped to both '2' and '3'.
 *
 * Example 3:
 *
 * Input: s = "paper", t = "title"
 * Output: true
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 * 10^4
 * t.length == s.length
 * s and t consist of any valid ascii character.
 *
 */
public class IsomorphicStrings {
    // V0

    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        // NOTE : we have to do both case
        return check(s, t) && check(t, s);
    }

    private boolean check(String s, String t){
        HashMap<String, String> map = new HashMap();
        for (int i = 0; i < s.length(); i++){
            String sValue = String.valueOf(s.charAt(i));
            String tValue = String.valueOf(t.charAt(i));
            if (!map.containsKey(sValue)){
                map.put(sValue, tValue);
            }else{
                if (! tValue.equals(map.get(sValue))){
                    //System.out.println("tValue =  " + tValue + " map.get(sValue) = " + map.get(sValue));
                    return false;
                }
            }
        }
        return true;
    }

}
