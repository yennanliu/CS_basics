package LeetCodeJava.String;

// https://leetcode.com/problems/expressive-words/
/**
 * 809. Expressive Words
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Sometimes people repeat letters to represent extra feeling. For example:
 *
 * "hello" -> "heeellooo"
 * "hi" -> "hiiii"
 * In these strings like "heeellooo", we have groups of adjacent letters that are all the same: "h", "eee", "ll", "ooo".
 *
 * You are given a string s and an array of query strings words. A query word is stretchy if it can be made to be equal to s by any number of applications of the following extension operation: choose a group consisting of characters c, and add some number of characters c to the group so that the size of the group is three or more.
 *
 * For example, starting with "hello", we could do an extension on the group "o" to get "hellooo", but we cannot get "helloo" since the group "oo" has a size less than three. Also, we could do another extension like "ll" -> "lllll" to get "helllllooo". If s = "helllllooo", then the query word "hello" would be stretchy because of these two extension operations: query = "hello" -> "hellooo" -> "helllllooo" = s.
 * Return the number of query strings that are stretchy.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "heeellooo", words = ["hello", "hi", "helo"]
 * Output: 1
 * Explanation:
 * We can extend "e" and "o" in the word "hello" to get "heeellooo".
 * We can't extend "helo" to get "heeellooo" because the group "ll" is not size 3 or more.
 * Example 2:
 *
 * Input: s = "zzzzzyyyyy", words = ["zzyy","zy","zyy"]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= s.length, words.length <= 100
 * 1 <= words[i].length <= 100
 * s and words[i] consist of lowercase letters.
 *
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressiveWords {

    // V0
//    public int expressiveWords(String s, String[] words) {
//
//    }


    /** NOTE !!!
     *
     *  CAN'T use below logic, because:
     *
     *   - character counts alone are insufficient
     *   - order/grouping matters
     *
     */
//    //  IDEA 1) STRING OP ???? + HASHMAP ???
//    public int expressiveWords(String s, String[] words) {
//        // edge
//
//
//        Map<String, Integer> mapS = getCnt(s); // ??
//        int cnt = 0;
//        for(String x: words){
//            // ???
//            Map<String, Integer> map = getCnt(x);
//            boolean isStretchy= false;
//
//            for(String k: map.keySet()){
//                if(!mapS.containsKey(k)){
//                    break;
//                }
//                // /??
//                if(mapS.get(k) - map.get(k) == 0){
//                    continue;
//                }
//                // ???
//                if(mapS.get(k) - map.get(k) < 2){
//                    break;
//                }
//            }
//            cnt += 1;
//        }
//
//
//        return cnt;
//    }
//
//
//    private Map<String, Integer> getCnt(String s){
//        Map<String, Integer> map = new HashMap<>();
//        for(char ch: s.toCharArray()){
//            String str = String.valueOf(ch); // ???
//            map.put(str, map.getOrDefault(str, 0) + 1 );
//        }
//        return map;
//    }


    // V0-1
    // IDEA: two-pointer/group-count (GPT)
    public int expressiveWords_0_1(String s, String[] words) {

        int cnt = 0;

        for (String word : words) {
            /** NOTE !!
             *
             *  call helper func here
             */
            if (isStretchy_0_1(s, word)) {
                cnt++;
            }
        }

        return cnt;
    }


    /** NOTE !!
     *
     *  helper func here
     *
     *   -> check if s, and word are `stretchy`)
     */
    private boolean isStretchy_0_1(String s, String word) {

        int i = 0;
        int j = 0;

        while (i < s.length() && j < word.length()) {

            // chars must match
            if (s.charAt(i) != word.charAt(j)) {
                return false;
            }

            char ch = s.charAt(i);

            /** NOTE !!
             *
             *  count `group length` in `s`
             */
            // count group length in s
            int cntS = 0;
            while (i < s.length() && s.charAt(i) == ch) {
                cntS++;
                i++;
            }

            /** NOTE !!
             *
             *  count `group length` in `word`
             */
            // count group length in word
            int cntW = 0;
            while (j < word.length() && word.charAt(j) == ch) {
                cntW++;
                j++;
            }

            // invalid cases
            if (cntS < cntW) {
                return false;
            }

            if (cntS != cntW && cntS < 3) {
                return false;
            }
        }

        // both must finish together
        return i == s.length() && j == word.length();
    }



    // V0-2
    // IDEA: two-pointer/group-count (gemini)
    public int expressiveWords_0_2(String s, String[] words) {
        if (s == null || words == null)
            return 0;

        // Compress the target string into ordered groups
        List<Group> targetGroups = getGroups(s);
        int expressiveCount = 0;

        for (String word : words) {
            if (isStretchy(targetGroups, getGroups(word))) {
                expressiveCount++;
            }
        }

        return expressiveCount;
    }

    // Custom helper class to store an ordered character block
    private static class Group {
        char ch;
        int count;

        Group(char ch, int count) {
            this.ch = ch;
            this.count = count;
        }
    }

    // Compresses a string into a list of character groups preserving order
    private List<Group> getGroups(String str) {
        List<Group> groups = new ArrayList<>();
        if (str.isEmpty())
            return groups;

        char[] chars = str.toCharArray();
        char current = chars[0];
        int count = 1;

        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == current) {
                count++;
            } else {
                groups.add(new Group(current, count));
                current = chars[i];
                count = 1;
            }
        }
        groups.add(new Group(current, count)); // Don't forget the last group!
        return groups;
    }

    // Validates if the query word groups can stretch into the target groups
    private boolean isStretchy(List<Group> target, List<Group> query) {
        // Condition 1: They must have the exact same number of distinct character blocks
        if (target.size() != query.size()) {
            return false;
        }

        for (int i = 0; i < target.size(); i++) {
            Group tHead = target.get(i);
            Group qHead = query.get(i);

            // Condition 2: The characters must match in order
            if (tHead.ch != qHead.ch) {
                return false;
            }

            // Condition 3: Cannot reduce characters from the query word
            if (qHead.count > tHead.count) {
                return false;
            }

            // Condition 4: If counts differ, target must have a group size >= 3 to allow extension
            if (qHead.count < tHead.count && tHead.count < 3) {
                return false;
            }
        }

        return true;
    }



    // V1
    // IDEA : (gpt)
    /**
     * 	2.	Stretchy Check:
     * 	•	The isStretchy method checks if a word can be stretched to match the string s.
     * 	•	It uses two pointers i and j to traverse through s and the word respectively.
     * 	•	It compares the characters at i and j. If they don’t match, it returns false.
     * 	•	If they match, it gets the length of the group of repeated characters starting from i in s (len1) and starting from j in the word (len2).
     * 	•	It checks if len1 is less than len2 or if len1 is greater than len2 but less than 3, in which case it returns false.
     * 	•	It moves the pointers i and j by len1 and len2 respectively.
     *
     *
     * 	3.	Getting Repeated Length:
     * 	•	The getRepeatedLength method calculates the length of the group of repeated characters starting from the given index.
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int expressiveWords_1(String s, String[] words) {
        int count = 0;
        for (String word : words) {
            if (isStretchy(s, word)) {
                count++;
            }
        }
        return count;
    }

    private boolean isStretchy(String s, String word) {
        int n = s.length();
        int m = word.length();
        int i = 0, j = 0;

        while (i < n && j < m) {
            if (s.charAt(i) != word.charAt(j)) {
                return false;
            }

            int len1 = getRepeatedLength(s, i);
            int len2 = getRepeatedLength(word, j);

            if ((len1 < len2) || (len1 > len2 && len1 < 3)) {
                return false;
            }

            i += len1;
            j += len2;
        }

        return i == n && j == m;
    }

    private int getRepeatedLength(String str, int index) {
        int start = index;
        while (index < str.length() && str.charAt(index) == str.charAt(start)) {
            index++;
        }
        return index - start;
    }

    // V1_1 (modified by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int expressiveWords_1_1(String s, String[] words) {
        if (words == null || words.length == 0) {
            return 0;
        }

        int res = 0;
        for (String x : words) {
            System.out.println("x = " + x + ", canForm(x, s) = " + canForm(x, s));
            if (canForm(x, s)) {
                res += 1;
            }
        }

        return res;
    }

    private boolean canForm(String x, String target) {
        int i = 0, j = 0;
        int n = x.length(), m = target.length();

        while (i < n && j < m) {
            if (x.charAt(i) != target.charAt(j)) {
                return false;
            }

            int len1 = getRepeatedLen(x, i);
            int len2 = getRepeatedLen(target, j);

            if (len1 > len2 || (len2 < 3 && len1 != len2)) {
                return false;
            }

            i += len1;
            j += len2;
        }

        // Both strings should be fully traversed for a match
        return i == n && j == m;
    }

    private int getRepeatedLen(String input, int index) {
        int count = 0;
        char currentChar = input.charAt(index);
        while (index + count < input.length() && input.charAt(index + count) == currentChar) {
            count++;
        }
        return count;
    }

    // V2
    // https://leetcode.com/problems/expressive-words/solutions/4729707/java-easy-100-solution-easy-to-understand/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int expressiveWords_2(final String s, final String[] words) {
        int count = 0;

        for(final String word : words)
            if(helper(s, word))
                count++;

        return count;
    }

    private boolean helper(final String s, final String word) {
        if(s.length() < word.length())
            return false;

        int i = 0, j = 0;

        while(i < s.length() && j < word.length()) {
            if(s.charAt(i) != word.charAt(j))
                return false;

            final char curr = word.charAt(j);
            int sCount = 0;

            while(i < s.length() && s.charAt(i) == curr) {
                sCount++;
                i++;
            }

            int wordCount = 0;

            while(j < word.length() && word.charAt(j) == curr) {
                wordCount++;
                j++;
            }

            if(sCount - wordCount < 0 || (sCount - wordCount != 0 && sCount < 3))
                return false;
        }

        return i >= s.length() && j >= word.length();
    }

    // V3
    // https://leetcode.com/problems/expressive-words/solutions/1850024/java-solution-using-frequency-lists/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int expressiveWords_3(String s, String[] words) {

        int expressive = 0;

        List<List<Integer>> sFreq = countOrder(s);

        for(String w: words) {
            List<List<Integer>> wFreq = countOrder(w);
            if(sFreq.size() != wFreq.size())
                continue;
            else {
                boolean flag = true;
                for(int i = 0 ; i < sFreq.size(); i++) {
                    if(sFreq.get(i).get(0) != wFreq.get(i).get(0)) {
                        flag = false;
                        break;
                    }
                    else if(sFreq.get(i).get(0) == wFreq.get(i).get(0) &&
                            sFreq.get(i).get(1) != wFreq.get(i).get(1)) {
                        if(sFreq.get(i).get(1) > wFreq.get(i).get(1) &&
                                sFreq.get(i).get(1) >= 3)
                            flag = true;
                        else {
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag)
                    expressive++;
            }
        }

        return expressive;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public List<List<Integer>> countOrder(String str) {

        List<List<Integer>> countList = new ArrayList<>();
        char prev = str.charAt(0);
        int cnt = 1;
        for(int i = 1; i <= str.length(); i++) {
            if(i < str.length() && str.charAt(i) == str.charAt(i - 1))
                cnt++;
            else {
                List<Integer> temp = new ArrayList<>();
                temp.add((int)(prev - 'a'));
                temp.add(cnt);
                countList.add(temp);
                cnt = 1;
                if(i < str.length())
                    prev = str.charAt(i);
                else
                    break;
            }
        }
        return countList;
    }






}
