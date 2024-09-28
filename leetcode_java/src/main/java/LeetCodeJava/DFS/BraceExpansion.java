package LeetCodeJava.DFS;

// https://leetcode.com/problems/brace-expansion/description/
// https://leetcode.ca/2018-11-21-1087-Brace-Expansion/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 1087. Brace Expansion
 * Description
 * You are given a string s representing a list of words. Each letter in the word has one or more options.
 *
 * If there is one option, the letter is represented as is.
 * If there is more than one option, then curly braces delimit the options. For example, "{a,b,c}" represents options ["a", "b", "c"].
 * For example, if s = "a{b,c}", the first character is always 'a', but the second character can be 'b' or 'c'. The original list is ["ab", "ac"].
 *
 * Return all words that can be formed in this manner, sorted in lexicographical order.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "{a,b}c{d,e}f"
 * Output: ["acdf","acef","bcdf","bcef"]
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: ["abcd"]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * s consists of curly brackets '{}', commas ',', and lowercase English letters.
 * s is guaranteed to be a valid input.
 * There are no nested curly brackets.
 * All characters inside a pair of consecutive opening and ending curly brackets are different.
 *
 */
public class BraceExpansion {

    // V0
    // TODO : implement, validate
//    public String[] expand(String s) {
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.ca/2018-11-21-1087-Brace-Expansion/
    private List<String> ans;
    private List<String[]> items;

    public String[] expand_1(String s) {
        ans = new ArrayList<>();
        items = new ArrayList<>();
        convert(s);
        dfs(0, new ArrayList<>());
        Collections.sort(ans);
        return ans.toArray(new String[0]);
    }

    private void convert(String s) {
        if ("".equals(s)) {
            return;
        }
        if (s.charAt(0) == '{') {
            int j = s.indexOf("}");
            items.add(s.substring(1, j).split(","));
            convert(s.substring(j + 1));
        } else {
            int j = s.indexOf("{");
            if (j != -1) {
                items.add(s.substring(0, j).split(","));
                convert(s.substring(j));
            } else {
                items.add(s.split(","));
            }
        }
    }

    private void dfs(int i, List<String> t) {
        if (i == items.size()) {
            ans.add(String.join("", t));
            return;
        }
        for (String c : items.get(i)) {
            t.add(c);
            dfs(i + 1, t);
            t.remove(t.size() - 1);
        }
    }


    // V2
    // IDEA : DFS
    // https://walkccc.me/LeetCode/problems/1087/#__tabbed_1_2
    public String[] expand_2(String s) {
        List<String> ans = new ArrayList<>();

        dfs_2(s, 0, new StringBuilder(), ans);
        Collections.sort(ans);
        return ans.toArray(new String[0]);
    }

    private void dfs_2(final String s, int i, StringBuilder sb, List<String> ans) {
        if (i == s.length()) {
            ans.add(sb.toString());
            return;
        }
        if (s.charAt(i) == '{') {
            final int nextRightBraceIndex = s.indexOf("}", i);
            for (final String c : s.substring(i + 1, nextRightBraceIndex).split(",")) {
                sb.append(c);
                dfs_2(s, nextRightBraceIndex + 1, sb, ans);
                sb.deleteCharAt(sb.length() - 1);
            }
        } else { // s[i] != '{'
            sb.append(s.charAt(i));
            dfs_2(s, i + 1, sb, ans);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // V3
    // IDEA : DFS
    // https://blog.csdn.net/qq_46105170/article/details/108840420

    // V4
    // IDEA : DFS
    // https://blog.csdn.net/qq_21201267/article/details/107541722



    // testing
//    public static void main(String[] args) {
//        BraceExpansion be = new BraceExpansion();
//        String input =  "{a,b}c{d,e}f";
//        String[] res = be.expand(input);
//        for(String x : Arrays.asList(res)){
//            System.out.println(x);
//        }
//    }

}
