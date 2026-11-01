package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-font-to-fit-a-sentence-in-a-screen/description/

/**
 * 1618. Maximum Font to Fit a Sentence in a Screen
 * Medium
 *
 * You are given a string text. We want to display text on a screen of width w and height h. You can
 * choose any font size from array fonts, which contains the available font sizes in ascending order.
 *
 * You can use the FontInfo interface to get the width and height of any character at any available
 * font size.
 *
 * The FontInfo interface is defined as such:
 *
 * interface FontInfo {
 *   // Returns the width of character ch on the screen using font size fontSize.
 *   // O(1) per call
 *   public int getWidth(int fontSize, char ch);
 *
 *   // Returns the height of any character on the screen using font size fontSize.
 *   // O(1) per call
 *   public int getHeight(int fontSize);
 * }
 *
 * The calculated width of text for some fontSize is the sum of every getWidth(fontSize, text[i]) call
 * for each 0 <= i < text.length (0-indexed). The calculated height of text for some fontSize is
 * getHeight(fontSize). Note that text is displayed on a single line.
 *
 * It is guaranteed that FontInfo will return the same value if you call getHeight or getWidth with
 * the same parameters.
 *
 * It is also guaranteed that for any font size fontSize and any character ch:
 *
 * getHeight(fontSize) <= getHeight(fontSize+1)
 * getWidth(fontSize, ch) <= getWidth(fontSize+1, ch)
 *
 * Return the maximum font size you can use to display text on the screen. If text cannot fit on the
 * display with any font size, return -1.
 *
 *
 * Example 1:
 *
 * Input: text = "helloworld", w = 80, h = 20, fonts = [6,8,10,12,14,16,18,24,36]
 * Output: 6
 *
 * Example 2:
 *
 * Input: text = "leetcode", w = 1000, h = 50, fonts = [1,2,4]
 * Output: 4
 *
 * Example 3:
 *
 * Input: text = "easyquestion", w = 100, h = 100, fonts = [10,15,20,25]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 50000
 * text contains only lowercase English letters.
 * 1 <= w <= 10^7
 * 1 <= h <= 10^4
 * 1 <= fonts.length <= 10^5
 * 1 <= fonts[i] <= 10^5
 * fonts is sorted in ascending order and does not contain duplicates.
 *
 */
public class MaximumFontToFitASentenceInAScreen {

    /**
     * // This is the FontInfo's API interface.
     * // You should not implement it, or speculate about its implementation
     * interface FontInfo {
     *     public int getWidth(int fontSize, char ch) {}
     *     public int getHeight(int fontSize) {}
     * }
     */
    public interface FontInfo {
        int getWidth(int fontSize, char ch);

        int getHeight(int fontSize);
    }

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (fits() IS MONOTONE OVER fonts)
    /**
     *  getWidth / getHeight never shrink as the font grows, so "does size s fit?" is true
     *  for a prefix of fonts and false after -> binary search the last true.
     *
     *  text can be 50000 chars, so count the 26 letters once and charge each probe 26
     *  getWidth calls instead of 50000. If even fonts[0] does not fit, the answer is -1.
     */
    /**
     * time = O(T + 26 * log F), T = text length, F = number of fonts
     * space = O(26)
     */
    public int maxFont(String text, int w, int h, int[] fonts, FontInfo fontInfo) {
        // how many times each letter appears in text
        int[] letterCount = new int[26];
        for (int i = 0; i < text.length(); i++) {
            letterCount[text.charAt(i) - 'a']++;
        }

        int best = -1;
        int left = 0;
        int right = fonts.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (fits(fonts[mid], w, h, letterCount, fontInfo)) {
                // this size fits -> record it and try a larger one
                best = fonts[mid];
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return best;
    }

    /** True when the text, at this font size, fits inside a w x h screen. */
    private boolean fits(int fontSize, int w, int h, int[] letterCount, FontInfo fontInfo) {
        if (fontInfo.getHeight(fontSize) > h) {
            return false;
        }
        long totalWidth = 0;
        for (int letter = 0; letter < 26; letter++) {
            if (letterCount[letter] == 0) {
                continue;
            }
            char ch = (char) ('a' + letter);
            totalWidth += (long) fontInfo.getWidth(fontSize, ch) * letterCount[letter];
            // stop early once it is already too wide
            if (totalWidth > w) {
                return false;
            }
        }
        return true;
    }
}
