package LeetCodeJava.HashTable;

// https://leetcode.com/problems/repeated-dna-sequences/description/

import java.util.*;

/**
 * 187. Repeated DNA Sequences
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * The DNA sequence is composed of a series of nucleotides abbreviated as 'A', 'C', 'G', and 'T'.
 *
 * For example, "ACGAATTCCG" is a DNA sequence.
 * When studying DNA, it is useful to identify repeated sequences within the DNA.
 *
 * Given a string s that represents a DNA sequence, return all the 10-letter-long sequences (substrings) that occur more than once in a DNA molecule. You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
 * Output: ["AAAAACCCCC","CCCCCAAAAA"]
 * Example 2:
 *
 * Input: s = "AAAAAAAAAAAAA"
 * Output: ["AAAAAAAAAA"]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s[i] is either 'A', 'C', 'G', or 'T'.
 *
 */
public class RepeatedDNASequences {

    // V0
//    public List<String> findRepeatedDnaSequences(String s) {
//
//    }

    // V1
    // https://leetcode.com/problems/repeated-dna-sequences/solutions/6738307/sliding-window-hashmap-by-pratham_18-0wj0/
    public List<String> findRepeatedDnaSequences_1(String s) {
        List<String> ans = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        if (s.length() < 11)
            return ans;
        int i = 0, j = 10;
        StringBuilder sb = new StringBuilder(s.substring(i, j));
        map.put(sb.toString(), 1);
        while (j < s.length()) {
            sb.deleteCharAt(0);
            sb.append(s.charAt(j));
            i++;
            j++;
            if (map.containsKey(sb.toString())) {
                if (map.get(sb.toString()) == 1) {
                    map.put(sb.toString(), 2);
                    ans.add(sb.toString());
                }
            } else {
                map.put(sb.toString(), 1);
            }
        }
        return ans;
    }

    // V2
    // https://leetcode.com/problems/repeated-dna-sequences/solutions/53855/7-lines-simple-java-on-by-stefanpochmann-4sts/
    public List<String> findRepeatedDnaSequences_2(String s) {
        Set seen = new HashSet(), repeated = new HashSet();
        for (int i = 0; i + 9 < s.length(); i++) {
            String ten = s.substring(i, i + 10);
            if (!seen.add(ten))
                repeated.add(ten);
        }
        return new ArrayList(repeated);
    }

    // V3
    // https://leetcode.com/problems/repeated-dna-sequences/solutions/5704051/solution-by-dare2solve-detailed-explanat-zwtc/
    public List<String> findRepeatedDnaSequences_3(String s) {
        Map<String, Integer> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (int i = 0; i <= s.length() - 10; i++) {
            String substring = s.substring(i, i + 10);
            if (map.containsKey(substring)) {
                if (map.get(substring) == 1) {
                    result.add(substring);
                }
                map.put(substring, map.get(substring) + 1);
            } else {
                map.put(substring, 1);
            }
        }

        return result;
    }


}
