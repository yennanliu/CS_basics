package LeetCodeJava.String;

// https://leetcode.com/problems/html-entity-parser/description/

import java.util.*;

/**
 * 1410. HTML Entity Parser
 * Medium
 *
 * HTML entity parser is the parser that takes HTML code as input and replace all
 * the entities of the special characters by the characters itself.
 *
 * The special characters and their entities for HTML are:
 *
 * Quotation Mark: the entity is &quot; and symbol character is ".
 * Single Quote Mark: the entity is &apos; and symbol character is '.
 * Ampersand: the entity is &amp; and symbol character is &.
 * Greater Than Sign: the entity is &gt; and symbol character is >.
 * Less Than Sign: the entity is &lt; and symbol character is <.
 * Slash: the entity is &frasl; and symbol character is /.
 *
 * Given the input text string to the HTML parser, you have to implement the entity
 * parser.
 *
 * Return the text after replacing the entities by the special characters.
 *
 *
 * Example 1:
 *
 * Input: text = "&amp; is an HTML entity but &ambassador; is not."
 * Output: "& is an HTML entity but &ambassador; is not."
 * Explanation: The parser will replace the &amp; entity by &
 *
 * Example 2:
 *
 * Input: text = "and I quote: &quot;...&quot;"
 * Output: "and I quote: \"...\""
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 10^5
 * The string may contain any possible characters out of all the 256 ASCII
 * characters.
 *
 */
public class HTMLEntityParser {

    // V0
    // IDEA: HASH MAP + LEFT-TO-RIGHT SCAN
    //       at each '&' try every entity; the first match is emitted and skipped over, so a
    //       produced '&' is never parsed again ("&amp;quot;" -> "&quot;", NOT '"').
    /**
     * time = O(N * L), L = 7 (longest entity)
     * space = O(N)
     */
    public String entityParser(String text) {
        Map<String, String> entities = new LinkedHashMap<>();
        entities.put("&quot;", "\"");
        entities.put("&apos;", "'");
        entities.put("&amp;", "&");
        entities.put("&gt;", ">");
        entities.put("&lt;", "<");
        entities.put("&frasl;", "/");

        StringBuilder sb = new StringBuilder();
        int i = 0;
        int n = text.length();
        while (i < n) {
            boolean matched = false;
            if (text.charAt(i) == '&') {
                for (Map.Entry<String, String> entry : entities.entrySet()) {
                    // startsWith(prefix, offset) checks the entity in place, no substring copy
                    if (text.startsWith(entry.getKey(), i)) {
                        sb.append(entry.getValue());
                        i += entry.getKey().length();
                        matched = true;
                        break;
                    }
                }
            }
            if (!matched) {
                sb.append(text.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }

    // V1
    // IDEA: SEQUENTIAL REPLACE, WITH &amp; LAST
    //       the other five entities never produce a '&', so they cannot create a new entity;
    //       replacing &amp; last means the '&' it produces is never rescanned.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String entityParser_1(String text) {
        String result = text;
        result = result.replace("&quot;", "\"");
        result = result.replace("&apos;", "'");
        result = result.replace("&gt;", ">");
        result = result.replace("&lt;", "<");
        result = result.replace("&frasl;", "/");
        return result.replace("&amp;", "&");
    }
}
