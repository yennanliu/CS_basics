package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/reverse-vowels-of-a-string/
/**
 * 345. Reverse Vowels of a String
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given a string s, reverse only all the vowels in the string and return it.
 *
 * The vowels are 'a', 'e', 'i', 'o', and 'u', and they can appear in both lower and upper cases, more than once.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "IceCreAm"
 *
 * Output: "AceCreIm"
 *
 * Explanation:
 *
 * The vowels in s are ['I', 'e', 'e', 'A']. On reversing the vowels, s becomes "AceCreIm".
 *
 * Example 2:
 *
 * Input: s = "leetcode"
 *
 * Output: "leotcede"
 *
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3 * 105
 * s consist of printable ASCII characters.
 *
 */
import java.util.HashSet;
import java.util.Set;

public class ReverseVowelsOfAString {


    // V0
    // IDEA: LC 151, 917, 2 POINTERS, CHAR OP
    public String reverseVowels(String s) {
        // edge

        int n = s.length();
        char[] chars = s.toCharArray();

        int l = 0;
        int r = n - 1;
        while (r > l) {
            while (r > l && !isVowels(s.charAt(r))) {
                r -= 1;
            }
            while (r > l && !isVowels(s.charAt(l))) {
                l += 1;
            }
            char tmp = chars[r];
            chars[r] = chars[l];
            chars[l] = tmp;

            r -= 1;
            l += 1;
        }

        return String.valueOf(chars); // ??
    }

    private boolean isVowels(char ch) {
        String vowel = "aeiou";
        return vowel.contains(String.valueOf(ch).toLowerCase());
    }


    // V0-1
    // IDEA: CHAR OP + 2 POINTERS (gemini)
    public String reverseVowels_0_1(String s) {
        if (s == null || s.length() == 0)
            return s;

        int n = s.length();
        char[] chars = s.toCharArray();

        int l = 0;
        int r = n - 1;

        while (l < r) {
            // 1. Move left pointer until it finds a vowel
            while (l < r && !isVowel_0_1(chars[l])) {
                l++;
            }
            // 2. Move right pointer until it finds a vowel
            while (l < r && !isVowel_0_1(chars[r])) {
                r--;
            }

            // 3. Swap the vowels
            char tmp = chars[l];
            chars[l] = chars[r];
            chars[r] = tmp;

            // 4. Important: Move pointers after swap to avoid infinite loop
            l++;
            r--;
        }

        return new String(chars);
    }

    private boolean isVowel_0_1(char ch) {
        // Efficiently check both cases without String allocation
        return "aeiouAEIOU".indexOf(ch) != -1;
    }


    // V0-2
    // IDEA: CHAR OP + 2 POINTERS (GPT)
    public String reverseVowels_0_2(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }

        char[] chars = s.toCharArray();
        int l = 0;
        int r = chars.length - 1;

        while (l < r) {
            while (l < r && !isVowel_0_2(chars[l])) {
                l++;
            }

            while (l < r && !isVowel_0_2(chars[r])) {
                r--;
            }

            // swap vowels
            char temp = chars[l];
            chars[l] = chars[r];
            chars[r] = temp;

            l++;
            r--;
        }

        return new String(chars);
    }

    private boolean isVowel_0_2(char ch) {
        ch = Character.toLowerCase(ch);
        return ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u';
    }



    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/reverse-vowels-of-a-string/editorial/
    // Return true if the character is a vowel (case-insensitive)
    boolean isVowel(char c) {
        return c == 'a' || c == 'i' || c == 'e' || c == 'o' || c == 'u'
                || c == 'A' || c == 'I' || c == 'E' || c == 'O' || c == 'U';
    }

    // Function to swap characters at index x and y
    // NOTE !!! this function
    void swap(char[] chars, int x, int y) {
        char temp = chars[x];
        chars[x] = chars[y];
        chars[y] = temp;
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    public String reverseVowels_1(String s) {
        int start = 0;
        int end = s.length() - 1;
        // Convert String to char array as String is immutable in Java
        char[] sChar = s.toCharArray();

        // NOTE !!! below tricks
        // While we still have characters to traverse
        while (start < end) {
            // Find the leftmost vowel
            while (start < s.length () && !isVowel(sChar[start])) {
                start++;
            }
            // Find the rightmost vowel
            while (end >= 0 && !isVowel(sChar[end])) {
                end--;
            }
            // Swap them if start is left of end
            if (start < end) {
                swap(sChar, start++, end--);
            }
        }

        // Converting char array back to String
        return new String(sChar);
    }

    // V2
    // https://leetcode.com/problems/reverse-vowels-of-a-string/solutions/81221/one-pass-java-solution-13ms/
    /**
     * time = O(N)
     * space = O(1)
     */
    public String reverseVowels_2(String s) {
        char[] list=s.toCharArray();
        Set<Character> set=new HashSet<>();
        set.add('a');
        set.add('e');
        set.add('i');
        set.add('o');
        set.add('u');
        set.add('A');
        set.add('E');
        set.add('I');
        set.add('O');
        set.add('U');
        for (int i=0, j=list.length-1; i<j; ) {
            if (!set.contains(list[i])) {
                i++;
                continue;
            }
            if (!set.contains(list[j])) {
                j--;
                continue;
            }
            char temp=list[i];
            list[i]=list[j];
            list[j]=temp;
            i++;
            j--;
        }
        return String.valueOf(list);
    }




}

