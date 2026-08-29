package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/utf-8-validation/

/**
 *  393. UTF-8 Validation
 *  Medium
 *
 *  Given an integer array data representing the data, return whether it is a
 *  valid UTF-8 encoding.
 *
 *  A character in UTF8 can be from 1 to 4 bytes long, subjected to the rules:
 *
 *   - For a 1-byte character, the first bit is a 0, followed by its Unicode code.
 *   - For an n-bytes character, the first n bits are all one's, the n + 1 bit is
 *     0, followed by n - 1 bytes with the most significant 2 bits being 10.
 *
 *     Char. number range  |        UTF-8 octet sequence
 *        (hexadecimal)    |              (binary)
 *     --------------------+---------------------------------------------
 *     0000 0000-0000 007F | 0xxxxxxx
 *     0000 0080-0000 07FF | 110xxxxx 10xxxxxx
 *     0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx
 *     0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
 *
 *  Note: only the least significant 8 bits of each integer is used.
 *
 *  Example 1:
 *  Input: data = [197,130,1]
 *  Output: true
 *
 *  Example 2:
 *  Input: data = [235,140,4]
 *  Output: false
 *
 *  Constraints:
 *  1 <= data.length <= 2 * 10^4
 *  0 <= data[i] <= 255
 */
public class UTF8Validation {

    // V0
    // IDEA: read the leading byte to know how many continuation bytes follow,
    //       then check each of them starts with `10`
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean validUtf8(int[] data) {
        int i = 0;
        int n = data.length;
        while (i < n) {
            int b = data[i] & 0xFF;
            int cnt;
            if ((b >> 7) == 0b0) {
                cnt = 0;
            } else if ((b >> 5) == 0b110) {
                cnt = 1;
            } else if ((b >> 4) == 0b1110) {
                cnt = 2;
            } else if ((b >> 3) == 0b11110) {
                cnt = 3;
            } else {
                return false;
            }
            if (i + cnt >= n) {
                return false;
            }
            for (int j = i + 1; j <= i + cnt; j++) {
                if (((data[j] & 0xFF) >> 6) != 0b10) {
                    return false;
                }
            }
            i += cnt + 1;
        }
        return true;
    }

    // V1
    // IDEA: single pass state machine - carry a counter of how many continuation bytes
    //       are still expected. A leading byte is only legal when that counter is 0,
    //       and the stream is valid only if the counter ends at 0.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean validUtf8_1(int[] data) {
        int remaining = 0;
        for (int d : data) {
            int b = d & 0xFF;
            if (remaining == 0) {
                if ((b & 0b10000000) == 0) {
                    remaining = 0;
                } else if ((b & 0b11100000) == 0b11000000) {
                    remaining = 1;
                } else if ((b & 0b11110000) == 0b11100000) {
                    remaining = 2;
                } else if ((b & 0b11111000) == 0b11110000) {
                    remaining = 3;
                } else {
                    return false;
                }
            } else {
                if ((b & 0b11000000) != 0b10000000) {
                    return false;
                }
                remaining--;
            }
        }
        return remaining == 0;
    }

    // V2
    // IDEA: readable reference - render each byte as a zero padded 8 char binary string
    //       and validate the spec with plain string prefix checks instead of bit masks
    /**
     * time = O(n)
     * space = O(1) (8 char scratch string per byte)
     */
    public boolean validUtf8_2(int[] data) {
        int i = 0;
        int n = data.length;
        while (i < n) {
            String s = toBinary_2(data[i]);
            int cnt;
            if (s.startsWith("0")) {
                cnt = 0;
            } else if (s.startsWith("110")) {
                cnt = 1;
            } else if (s.startsWith("1110")) {
                cnt = 2;
            } else if (s.startsWith("11110")) {
                cnt = 3;
            } else {
                return false;
            }
            if (i + cnt >= n) {
                return false;
            }
            for (int j = i + 1; j <= i + cnt; j++) {
                if (!toBinary_2(data[j]).startsWith("10")) {
                    return false;
                }
            }
            i += cnt + 1;
        }
        return true;
    }

    private String toBinary_2(int x) {
        String s = Integer.toBinaryString(x & 0xFF);
        StringBuilder sb = new StringBuilder();
        for (int k = s.length(); k < 8; k++) {
            sb.append('0');
        }
        return sb.append(s).toString();
    }
}
