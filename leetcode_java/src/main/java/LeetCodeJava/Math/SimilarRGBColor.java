package LeetCodeJava.Math;

// https://leetcode.com/problems/similar-rgb-color/

/**
 *  800. Similar RGB Color
 *  Easy
 *
 *  The red-green-blue color "#AABBCC" can be written as "#ABC" in shorthand.
 *
 *  For example, "#15c" is shorthand for the color "#1155cc".
 *
 *  The similarity between the two colors "#ABCDEF" and "#UVWXYZ" is
 *  -(AB - UV)^2 - (CD - WX)^2 - (EF - YZ)^2.
 *
 *  Given a string color that follows the format "#ABCDEF", return a string
 *  represents the color that is most similar to the given color and has a
 *  shorthand (i.e., it can be represented as "#XYZ").
 *
 *  Any answer which has the same highest similarity as the best answer will be accepted.
 *
 *  Example 1:
 *   Input: color = "#09f166"
 *   Output: "#11ee66"
 *   Explanation: the similarity is -(0x09 - 0x11)^2 -(0xf1 - 0xee)^2 - (0x66 - 0x66)^2
 *                = -64 - 9 - 0 = -73. This is the highest among any shorthand color.
 *
 *  Example 2:
 *   Input: color = "#4e3fe1"
 *   Output: "#5544dd"
 *
 *  Constraints:
 *   - color.length() == 7
 *   - color[0] == '#'
 *   - color[i] is either digit or character in the range ['a', 'f'] for i > 0.
 */
public class SimilarRGBColor {

    // V0
    // IDEA: each shorthand channel value is 17 * q (0x00, 0x11, ... 0xff),
    //       so round each byte to the nearest multiple of 17 independently.
    /**
     * time = O(1)
     * space = O(1)
     */
    public String similarRGB(String color) {
        StringBuilder sb = new StringBuilder("#");
        for (int i = 1; i < 7; i += 2) {
            sb.append(round(color.substring(i, i + 2)));
        }
        return sb.toString();
    }

    private String round(String hex) {
        int v = Integer.parseInt(hex, 16);
        int q = v / 17;
        int r = v % 17;
        if (r > 8) {
            q += 1;
        }
        return String.format("%02x", 17 * q);
    }

    // V1
    // IDEA: brute force over all 16^3 shorthand colors, keep the best similarity.
    /**
     * time = O(1)  (fixed 16^3 iterations)
     * space = O(1)
     */
    public String similarRGB_1(String color) {
        int ir = Integer.parseInt(color.substring(1, 3), 16);
        int ig = Integer.parseInt(color.substring(3, 5), 16);
        int ib = Integer.parseInt(color.substring(5, 7), 16);

        int best = Integer.MAX_VALUE;
        int br = 0, bg = 0, bb = 0;
        for (int r = 0; r < 16; r++) {
            for (int g = 0; g < 16; g++) {
                for (int b = 0; b < 16; b++) {
                    int d = sq(ir - r * 17) + sq(ig - g * 17) + sq(ib - b * 17);
                    if (d < best) {
                        best = d;
                        br = r;
                        bg = g;
                        bb = b;
                    }
                }
            }
        }
        return "#" + String.format("%02x%02x%02x", br * 17, bg * 17, bb * 17);
    }

    private int sq(int x) {
        return x * x;
    }
}
