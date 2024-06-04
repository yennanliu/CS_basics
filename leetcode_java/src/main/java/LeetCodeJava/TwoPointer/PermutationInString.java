package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/permutation-in-string/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PermutationInString {

    // V0
    // TODO implement

    // V0'
    // IDEA : COUNTER : count element in string (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Two_Pointers/permutation-in-string.py
    public boolean checkInclusion_0(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }

        // Create frequency maps for s1 and the sliding window in s2
        Map<Character, Integer> mapS1 = new HashMap<>();
        Map<Character, Integer> mapS2 = new HashMap<>();

        for (char c : s1.toCharArray()) {
            mapS1.put(c, mapS1.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        for (int right = 0; right < s2.length(); right++) {
            char rightChar = s2.charAt(right);
            mapS2.put(rightChar, mapS2.getOrDefault(rightChar, 0) + 1);

            /** NOTE !!!
             *
             *  we simply check permutation with below trick
             *  -> e.g. if 2 map are equal
             */
            // Check if the current window matches the frequency map of s1
            if (mapS1.equals(mapS2)) {
                return true;
            }

            // If the window size exceeds the size of s1, move the left pointer
            if (right - left + 1 >= s1.length()) {
                char leftChar = s2.charAt(left);
                mapS2.put(leftChar, mapS2.get(leftChar) - 1);
                if (mapS2.get(leftChar) == 0) {
                    mapS2.remove(leftChar);
                }
                left++;
            }
        }

        return false;
    }

    // V1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/permutation-in-string/editorial/
    boolean flag = false;

    public boolean checkInclusion_2(String s1, String s2) {
        permute(s1, s2, 0);
        return flag;
    }

    public String swap(String s, int i0, int i1) {
        if (i0 == i1)
            return s;
        String s1 = s.substring(0, i0);
        String s2 = s.substring(i0 + 1, i1);
        String s3 = s.substring(i1 + 1);
        return s1 + s.charAt(i1) + s2 + s.charAt(i0) + s3;
    }

    void permute(String s1, String s2, int l) {
        if (l == s1.length()) {
            if (s2.indexOf(s1) >= 0)
                flag = true;
        } else {
            for (int i = l; i < s1.length(); i++) {
                s1 = swap(s1, l, i);
                permute(s1, s2, l + 1);
                s1 = swap(s1, l, i);
            }
        }
    }

    // V2
    // IDEA : SORTING
    // https://leetcode.com/problems/permutation-in-string/editorial/
    public boolean checkInclusion_3(String s1, String s2) {
        s1 = sort(s1);
        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            if (s1.equals(sort(s2.substring(i, i + s1.length()))))
                return true;
        }
        return false;
    }

    public String sort(String s) {
        char[] t = s.toCharArray();
        Arrays.sort(t);
        return new String(t);
    }

    // V3
    // IDEA : Hashmap
    // https://leetcode.com/problems/permutation-in-string/editorial/
    public boolean checkInclusion_4(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        HashMap<Character, Integer> s1map = new HashMap<>();

        for (int i = 0; i < s1.length(); i++)
            s1map.put(s1.charAt(i), s1map.getOrDefault(s1.charAt(i), 0) + 1);

        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            HashMap<Character, Integer> s2map = new HashMap<>();
            for (int j = 0; j < s1.length(); j++) {
                s2map.put(s2.charAt(i + j), s2map.getOrDefault(s2.charAt(i + j), 0) + 1);
            }
            if (matches(s1map, s2map))
                return true;
        }
        return false;
    }

    public boolean matches(HashMap<Character, Integer> s1map, HashMap<Character, Integer> s2map) {
        for (char key : s1map.keySet()) {
            if (s1map.get(key) - s2map.getOrDefault(key, -1) != 0)
                return false;
        }
        return true;
    }

    // V4
    // IDEA : Array
    // https://leetcode.com/problems/permutation-in-string/editorial/
    public boolean checkInclusion_5(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        int[] s1map = new int[26];
        for (int i = 0; i < s1.length(); i++)
            s1map[s1.charAt(i) - 'a']++;
        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            int[] s2map = new int[26];
            for (int j = 0; j < s1.length(); j++) {
                s2map[s2.charAt(i + j) - 'a']++;
            }
            if (matches(s1map, s2map))
                return true;
        }
        return false;
    }

    public boolean matches(int[] s1map, int[] s2map) {
        for (int i = 0; i < 26; i++) {
            if (s1map[i] != s2map[i])
                return false;
        }
        return true;
    }

    // V5
    // IDEA : Sliding Window
    // https://leetcode.com/problems/permutation-in-string/editorial/
    public boolean checkInclusion_6(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        int[] s1map = new int[26];
        int[] s2map = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1map[s1.charAt(i) - 'a']++;
            s2map[s2.charAt(i) - 'a']++;
        }
        for (int i = 0; i < s2.length() - s1.length(); i++) {
            if (matches(s1map, s2map))
                return true;
            s2map[s2.charAt(i + s1.length()) - 'a']++;
            s2map[s2.charAt(i) - 'a']--;
        }
        return matches_6(s1map, s2map);
    }

    public boolean matches_6(int[] s1map, int[] s2map) {
        for (int i = 0; i < 26; i++) {
            if (s1map[i] != s2map[i])
                return false;
        }
        return true;
    }

    // V6
    // IDEA : OPTIMIZED SLIDING WINDOW
    // https://leetcode.com/problems/permutation-in-string/editorial/
    public boolean checkInclusion_7(String s1, String s2) {
        if (s1.length() > s2.length())
            return false;
        int[] s1map = new int[26];
        int[] s2map = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1map[s1.charAt(i) - 'a']++;
            s2map[s2.charAt(i) - 'a']++;
        }

        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (s1map[i] == s2map[i])
                count++;
        }

        for (int i = 0; i < s2.length() - s1.length(); i++) {
            int r = s2.charAt(i + s1.length()) - 'a', l = s2.charAt(i) - 'a';
            if (count == 26)
                return true;
            s2map[r]++;
            if (s2map[r] == s1map[r]) {
                count++;
            } else if (s2map[r] == s1map[r] + 1) {
                count--;
            }
            s2map[l]--;
            if (s2map[l] == s1map[l]) {
                count++;
            } else if (s2map[l] == s1map[l] - 1) {
                count--;
            }
        }
        return count == 26;
    }


    // V5
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0567-permutation-in-string.java
    public boolean checkInclusion_8(String s1, String s2) {
        int n = s1.length();
        int[] freq = new int[26];
        int m = s2.length();
        for (int i = 0; i < n; i++) {
            freq[s1.charAt(i) - 'a']++;
        }
        int[] freq2 = new int[26];
        for (int i = 0; i < m; i++) {
            freq2[s2.charAt(i) - 'a']++;
            if (i >= n) {
                freq2[s2.charAt(i - n) - 'a']--;
            }
            if (Arrays.equals(freq, freq2))
                return true;
        }
        return false;
    }

}
