package LeetCodeJava.String;

// https://leetcode.com/problems/ransom-note/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 383. Ransom Note
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given two strings ransomNote and magazine, return true if ransomNote can be constructed by using the letters from magazine and false otherwise.
 *
 * Each letter in magazine can only be used once in ransomNote.
 *
 *
 *
 * Example 1:
 *
 * Input: ransomNote = "a", magazine = "b"
 * Output: false
 * Example 2:
 *
 * Input: ransomNote = "aa", magazine = "ab"
 * Output: false
 * Example 3:
 *
 * Input: ransomNote = "aa", magazine = "aab"
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= ransomNote.length, magazine.length <= 105
 * ransomNote and magazine consist of lowercase English letters.
 */
public class RansomNote {

    // V0
    // IDEA: HASH MAP
    public boolean canConstruct(String ransomNote, String magazine) {
        // edge
        if(ransomNote == null){
            return true;
        }
        // ???
        if(magazine == null){
            return false;
        }

        Map<Character, Integer> map1 = new HashMap<>();
        Map<Character, Integer> map2 = new HashMap<>();

        for(char ch: ransomNote.toCharArray()){
            map1.put(ch, map1.getOrDefault(ch, 0) + 1);
        }

        for(char ch: magazine.toCharArray()){
            map2.put(ch, map2.getOrDefault(ch, 0) + 1);
        }

//        System.out.println(">>> map1 = " + map1 +
//                ", map2 = " + map2);

        // if ransomNote can be constructed by using the
        // letters from magazine and false otherwise.
        for(char ch: map1.keySet()){
            if(!map2.containsKey(ch) || map2.get(ch) < map1.get(ch)){
                return false;
            }
        }

        return true;
    }

    // V1
    // https://leetcode.com/problems/ransom-note/solutions/1671552/1ms-100-easy-explanation-java-solution-b-a286/
    public boolean canConstruct_1(String ransomNote, String magazine) {
        if (ransomNote.length() > magazine.length()) return false;
        int[] alphabets_counter = new int[26];

        for (char c : magazine.toCharArray())
            alphabets_counter[c-'a']++;

        for (char c : ransomNote.toCharArray()){
            if (alphabets_counter[c-'a'] == 0) return false;
            alphabets_counter[c-'a']--;
        }
        return true;
    }

    // V2
    // https://leetcode.com/problems/ransom-note/solutions/6743962/video-counting-each-character-2-solution-3x0h/
    public boolean canConstruct_2(String ransomNote, String magazine) {
        HashMap<Character, Integer> magaHash = new HashMap<>();

        for (char c : magazine.toCharArray()) {
            magaHash.put(c, magaHash.getOrDefault(c, 0) + 1);
        }

        for (char c : ransomNote.toCharArray()) {
            if (!magaHash.containsKey(c) || magaHash.get(c) <= 0) {
                return false;
            }
            magaHash.put(c, magaHash.get(c) - 1);
        }

        return true;
    }



}
