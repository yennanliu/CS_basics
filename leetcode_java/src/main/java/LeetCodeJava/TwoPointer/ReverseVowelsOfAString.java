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
//    public String reverseVowels(String s) {
//
//        if (s == null || s.length() == 0){
//            return s;
//        }
//
//        char[] _array = s.toCharArray();
//
//        String vowels = "aeiou";
//
//        int j = s.length()-1;
//
//        for (int i = 0; i < _array.length; i++){
//            String r = String.valueOf(_array[j]);
//            String l = String.valueOf(_array[i]);
//
//            if (vowels.contains(l) && vowels.contains(r)){
//                char _tmp = _array[i];
//                _array[j] = _tmp;
//                _array[i] = l;
//            }
//        }
//
//        return _array.toString();
//    }

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
    public String reverseVowels(String s) {
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
    public class Solution {
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




}

