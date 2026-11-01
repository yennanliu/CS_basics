package LeetCodeJava.String;

// https://leetcode.com/problems/generate-tag-for-video-caption/description/

/**
 * 3582. Generate Tag for Video Caption
 * Easy
 *
 * You are given a string caption representing the caption for a video.
 *
 * The following actions must be performed in order to generate a valid tag for the video:
 *
 * Combine all words in the string into a single camelCase string prefixed with '#'. A camelCase string is one where the first letter of all words except the first one is capitalized. All the characters in the first word must be in lower case.
 * Remove all characters that are not an English letter, except the leading '#'.
 * Truncate the result to a maximum of 100 characters.
 *
 * Return the tag after performing the actions on caption.
 *
 *
 * Example 1:
 *
 * Input: caption = "Leetcode daily streak achieved"
 * Output: "#leetcodeDailyStreakAchieved"
 * Explanation:
 * The first word is "leetcode" in lowercase, and the remaining words are capitalized: "Daily", "Streak" and "Achieved".
 *
 * Example 2:
 *
 * Input: caption = "can I Go There"
 * Output: "#canIGoThere"
 * Explanation:
 * The first word is "can" in lowercase, and the remaining words are capitalized: "I", "Go" and "There".
 *
 * Example 3:
 *
 * Input: caption = "hhhh...hhhh"   (a single word of 101 'h' characters)
 * Output: "#hhhh...hhhh"           ('#' followed by 99 'h' characters)
 * Explanation:
 * Since the first word has length 101, we need to truncate the last two
 * letters from the word.
 *
 *
 * Constraints:
 *
 * 1 <= caption.length <= 150
 * caption consists only of English letters and ' '.
 *
 */
public class GenerateTagForVideoCaption {

    private static final int MAX_TAG_LENGTH = 100;

    // V0
    // IDEA: SPLIT ON WHITESPACE, FIRST WORD LOWERCASE, LATER WORDS CAPITALISED, CAP AT 100 CHARS
    /**
     * time = O(N)
     * space = O(N)
     */
    public String generateTag(String caption) {
        StringBuilder tag = new StringBuilder("#");
        // trim + split on runs of spaces never yields an empty word in the middle
        String trimmed = caption.trim();
        if (trimmed.isEmpty()) {
            return tag.toString();
        }
        String[] words = trimmed.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                tag.append(word.toLowerCase());
            } else {
                tag.append(Character.toUpperCase(word.charAt(0)));
                tag.append(word.substring(1).toLowerCase());
            }
        }
        // the cap counts the leading '#'
        if (tag.length() > MAX_TAG_LENGTH) {
            tag.setLength(MAX_TAG_LENGTH);
        }
        return tag.toString();
    }
}
