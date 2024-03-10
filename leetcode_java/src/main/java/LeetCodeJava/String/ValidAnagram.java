package LeetCodeJava.String;

// https://leetcode.com/problems/valid-anagram/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ValidAnagram {

    // V0
    // IDEA : HASHMAP
    public boolean isAnagram(String s, String t) {

        if (s == null && t == null){
            return true;
        }
//        if ((s == null && t != null) || (s != null && t == null) ){
//            return false;
//        }
        Map<String, Integer> sMap = new HashMap<>();
        for (String x : s.split("")){
            if (!sMap.containsKey(x)){
                sMap.put(x, 1);
            }
            else{
                sMap.put(x, sMap.get(x)+1);
            }
        }

        for (String y : t.split("")){
            if (!sMap.containsKey(y)){
                return false;
            }
            sMap.put(y, sMap.get(y)-1);
            if (sMap.get(y)==0){
                sMap.remove(y);
            }
        }

        if (sMap.keySet().size()!= 0){
            return false;
        }
        return true;
    }

    // V1
    // IDEA : ARRAY SORT
    // https://leetcode.com/problems/valid-anagram/solutions/3687854/3-method-s-c-java-python-beginner-friendly/
    public boolean isAnagram_1(String s, String t) {
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();

        Arrays.sort(sChars);
        Arrays.sort(tChars);

        return Arrays.equals(sChars, tChars);
    }

    // V2
    // https://leetcode.com/problems/valid-anagram/solutions/3261552/easy-solutions-in-java-python-javascript-and-c-look-at-once/
    public boolean isAnagram_2(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        int[] freq = new int[26];
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
            freq[t.charAt(i) - 'a']--;
        }

        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0) {
                return false;
            }
        }

        return true;
    }
    
}
