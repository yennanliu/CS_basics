package LeetCodeJava.Math;

// https://leetcode.com/problems/reconstruct-original-digits-from-english/

/**
 *  423. Reconstruct Original Digits from English
 *  Medium
 *
 *  Given a string s containing an out-of-order English representation of digits 0-9,
 *  return the digits in ascending order.
 *
 *  Example 1:
 *
 *  Input: s = "owoztneoer"
 *  Output: "012"
 *
 *  Example 2:
 *
 *  Input: s = "fviefuro"
 *  Output: "45"
 *
 *  Constraints:
 *
 *  1 <= s.length <= 10^5
 *  s[i] is one of the characters ["e","g","f","i","h","o","n","s","r","u","t","w","v","x","z"].
 *  s is guaranteed to be valid.
 */
public class ReconstructOriginalDigitsFromEnglish {

    // V0
    // IDEA: some letters are unique to a single digit word:
    //         'z' -> zero, 'w' -> two, 'u' -> four, 'x' -> six, 'g' -> eight
    //       once those are fixed, more letters become unique:
    //         'h' -> three (minus eight), 'f' -> five (minus four),
    //         's' -> seven (minus six), 'i' -> nine (minus five, six, eight),
    //         'o' -> one (minus zero, two, four)
    /**
     * time = O(n)   n = s.length()
     * space = O(1)
     */
    public String originalDigits(String s) {
        int[] c = new int[26];
        for (int i = 0; i < s.length(); i++) {
            c[s.charAt(i) - 'a']++;
        }

        int[] cnt = new int[10];
        // unique letters
        cnt[0] = c['z' - 'a'];
        cnt[2] = c['w' - 'a'];
        cnt[4] = c['u' - 'a'];
        cnt[6] = c['x' - 'a'];
        cnt[8] = c['g' - 'a'];
        // now-unique letters
        cnt[3] = c['h' - 'a'] - cnt[8];
        cnt[5] = c['f' - 'a'] - cnt[4];
        cnt[7] = c['s' - 'a'] - cnt[6];
        cnt[9] = c['i' - 'a'] - cnt[5] - cnt[6] - cnt[8];
        cnt[1] = c['o' - 'a'] - cnt[0] - cnt[2] - cnt[4];

        StringBuilder sb = new StringBuilder();
        for (int d = 0; d <= 9; d++) {
            for (int k = 0; k < cnt[d]; k++) {
                sb.append((char) ('0' + d));
            }
        }
        return sb.toString();
    }
}
