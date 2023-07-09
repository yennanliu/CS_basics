package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-substring-without-repeating-characters/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LongestSubstringWithoutRepeatingCharacters {

    // V0
    // IDEA : SLIDING WINDOW + HASH SET
    public int lengthOfLongestSubstring(String s) {

        if (s.equals("")){
            return 0;
        }

        if (s.equals(" ")){
            return 1;
        }

        if (s.length() == 1){
            return 1;
        }

        int ans = 0;
        char[] s_array = s.toCharArray();
        for (int i = 0; i < s_array.length-1; i++){
            int j = i;
            Set<String> set = new HashSet<String>();
            while (j < s_array.length){
                String cur = String.valueOf(s_array[j]);
                if (set.contains(cur)){
                    ans = Math.max(ans, set.size());
                    break;
                }else{
                    set.add(cur);
                    ans = Math.max(ans, set.size());
                    j += 1;
                }
            }
        }

        return ans;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
    public int lengthOfLongestSubstring_2(String s) {
        int n = s.length();

        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (checkRepetition(s, i, j)) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }

        return res;
    }

    private boolean checkRepetition(String s, int start, int end) {
        Set<Character> chars = new HashSet<>();

        for (int i = start; i <= end; i++) {
            char c = s.charAt(i);
            if(chars.contains(c)){
                return false;
            }
            chars.add(c);
        }

        return true;
    }

    // V2
    // IDEA : SLIDING WINDOW
    // https://leetcode.com/problems/longest-substring-without-repeating-characters/editorial/
    public int lengthOfLongestSubstring_3(String s) {
        Map<Character, Integer> chars = new HashMap();

        int left = 0;
        int right = 0;
        int res = 0;

        while (right < s.length()) {
            char r = s.charAt(right);
            chars.put(r, chars.getOrDefault(r,0) + 1);

            while (chars.get(r) > 1) {
                char l = s.charAt(left);
                chars.put(l, chars.get(l) - 1);
                left++;
            }

            res = Math.max(res, right - left + 1);

            right++;
        }
        return res;
    }

}
