package LeetCodeJava.Stack;

// https://leetcode.com/problems/tag-validator/description/

import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 591. Tag Validator
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given a string representing a code snippet, implement a tag validator to parse the code and return whether it is valid.
 *
 * A code snippet is valid if all the following rules hold:
 *
 * The code must be wrapped in a valid closed tag. Otherwise, the code is invalid.
 * A closed tag (not necessarily valid) has exactly the following format : <TAG_NAME>TAG_CONTENT</TAG_NAME>. Among them, <TAG_NAME> is the start tag, and </TAG_NAME> is the end tag. The TAG_NAME in start and end tags should be the same. A closed tag is valid if and only if the TAG_NAME and TAG_CONTENT are valid.
 * A valid TAG_NAME only contain upper-case letters, and has length in range [1,9]. Otherwise, the TAG_NAME is invalid.
 * A valid TAG_CONTENT may contain other valid closed tags, cdata and any characters (see note1) EXCEPT unmatched <, unmatched start and end tag, and unmatched or closed tags with invalid TAG_NAME. Otherwise, the TAG_CONTENT is invalid.
 * A start tag is unmatched if no end tag exists with the same TAG_NAME, and vice versa. However, you also need to consider the issue of unbalanced when tags are nested.
 * A < is unmatched if you cannot find a subsequent >. And when you find a < or </, all the subsequent characters until the next > should be parsed as TAG_NAME (not necessarily valid).
 * The cdata has the following format : <![CDATA[CDATA_CONTENT]]>. The range of CDATA_CONTENT is defined as the characters between <![CDATA[ and the first subsequent ]]>.
 * CDATA_CONTENT may contain any characters. The function of cdata is to forbid the validator to parse CDATA_CONTENT, so even it has some characters that can be parsed as tag (no matter valid or invalid), you should treat it as regular characters.
 *
 *
 * Example 1:
 *
 * Input: code = "<DIV>This is the first line <![CDATA[<div>]]></DIV>"
 * Output: true
 * Explanation:
 * The code is wrapped in a closed tag : <DIV> and </DIV>.
 * The TAG_NAME is valid, the TAG_CONTENT consists of some characters and cdata.
 * Although CDATA_CONTENT has an unmatched start tag with invalid TAG_NAME, it should be considered as plain text, not parsed as a tag.
 * So TAG_CONTENT is valid, and then the code is valid. Thus return true.
 * Example 2:
 *
 * Input: code = "<DIV>>>  ![cdata[]] <![CDATA[<div>]>]]>]]>>]</DIV>"
 * Output: true
 * Explanation:
 * We first separate the code into : start_tag|tag_content|end_tag.
 * start_tag -> "<DIV>"
 * end_tag -> "</DIV>"
 * tag_content could also be separated into : text1|cdata|text2.
 * text1 -> ">>  ![cdata[]] "
 * cdata -> "<![CDATA[<div>]>]]>", where the CDATA_CONTENT is "<div>]>"
 * text2 -> "]]>>]"
 * The reason why start_tag is NOT "<DIV>>>" is because of the rule 6.
 * The reason why cdata is NOT "<![CDATA[<div>]>]]>]]>" is because of the rule 7.
 * Example 3:
 *
 * Input: code = "<A>  <B> </A>   </B>"
 * Output: false
 * Explanation: Unbalanced. If "<A>" is closed, then "<B>" must be unmatched, and vice versa.
 *
 *
 * Constraints:
 *
 * 1 <= code.length <= 500
 * code consists of English letters, digits, '<', '>', '/', '!', '[', ']', '.', and ' '.
 *
 *
 */
public class TagValidator {

    // V0
//    public boolean isValid(String code) {
//
//    }

    // V1-1
    // IDEA: STACK
    // https://leetcode.com/problems/tag-validator/editorial/
    Stack<String> stack = new Stack<>();
    boolean contains_tag = false;

    public boolean isValidTagName(String s, boolean ending) {
        if (s.length() < 1 || s.length() > 9)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isUpperCase(s.charAt(i)))
                return false;
        }
        if (ending) {
            if (!stack.isEmpty() && stack.peek().equals(s))
                stack.pop();
            else
                return false;
        } else {
            contains_tag = true;
            stack.push(s);
        }
        return true;
    }

    public boolean isValidCdata(String s) {
        return s.indexOf("[CDATA[") == 0;
    }

    public boolean isValid_1_1(String code) {
        if (code.charAt(0) != '<' || code.charAt(code.length() - 1) != '>')
            return false;
        for (int i = 0; i < code.length(); i++) {
            boolean ending = false;
            int closeindex;
            if (stack.isEmpty() && contains_tag)
                return false;
            if (code.charAt(i) == '<') {
                if (!stack.isEmpty() && code.charAt(i + 1) == '!') {
                    closeindex = code.indexOf("]]>", i + 1);
                    if (closeindex < 0 || !isValidCdata(code.substring(i + 2, closeindex)))
                        return false;
                } else {
                    if (code.charAt(i + 1) == '/') {
                        i++;
                        ending = true;
                    }
                    closeindex = code.indexOf('>', i + 1);
                    if (closeindex < 0 || !isValidTagName(code.substring(i + 1, closeindex), ending))
                        return false;
                }
                i = closeindex;
            }
        }
        return stack.isEmpty() && contains_tag;
    }


    // V1-2
    // IDEA: REGEX
    // https://leetcode.com/problems/tag-validator/editorial/
    Stack<String> stack_1_2 = new Stack<>();
    boolean contains_tag_1_2 = false;

    public boolean isValidTagName_1_2(String s, boolean ending) {
        if (ending) {
            if (!stack_1_2.isEmpty() && stack_1_2.peek().equals(s))
                stack_1_2.pop();
            else
                return false;
        } else {
            contains_tag = true;
            stack_1_2.push(s);
        }
        return true;
    }

    public boolean isValid_1_2(String code) {
        String regex = "<[A-Z]{0,9}>([^<]*(<((\\/?[A-Z]{1,9}>)|(!\\[CDATA\\[(.*?)]]>)))?)*";
        if (!Pattern.matches(regex, code))
            return false;
        for (int i = 0; i < code.length(); i++) {
            boolean ending = false;
            if (stack_1_2.isEmpty() && contains_tag_1_2)
                return false;
            if (code.charAt(i) == '<') {
                if (code.charAt(i + 1) == '!') {
                    i = code.indexOf("]]>", i + 1);
                    continue;
                }
                if (code.charAt(i + 1) == '/') {
                    i++;
                    ending = true;
                }
                int closeindex = code.indexOf('>', i + 1);
                if (closeindex < 0 || !isValidTagName_1_2(code.substring(i + 1, closeindex), ending))
                    return false;
                i = closeindex;
            }
        }
        return stack_1_2.isEmpty();
    }




    // V2



}
