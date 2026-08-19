package LeetCodeJava.Array;

// https://leetcode.com/problems/read-n-characters-given-read4/

/**
 *  157. Read N Characters Given Read4
 *  Easy
 *
 *  Given a file and assume that you can only read the file using a given
 *  method read4, implement a method to read n characters.
 *
 *  Method read4:
 *   int read4(char[] buf4)
 *  reads four consecutive characters from file, then writes those
 *  characters into the buffer array buf4. It returns the number of actual
 *  characters read (< 4 only when there are fewer than 4 chars left).
 *
 *  Method read:
 *   int read(char[] buf, int n)
 *  reads n characters from file and stores them in the buffer buf.
 *  Return the number of actual characters read.
 *
 *  Note: read may only be called once per test case.
 *
 *  Example 1:
 *   Input: file = "abc", n = 4
 *   Output: 3  (buf = "abc")
 *
 *  Example 2:
 *   Input: file = "abcde", n = 5
 *   Output: 5  (buf = "abcde")
 *
 *  Constraints:
 *   1 <= file.length <= 500
 *   0 <= n <= 1000
 *
 *  NOTE: on LeetCode the class extends `Reader4` which provides read4().
 *        Here read4() is declared locally so the file compiles standalone.
 */
public class ReadNCharactersGivenRead4 {

    /** placeholder for the API provided by the parent class `Reader4` on LeetCode */
    protected int read4(char[] buf4) {
        return 0;
    }

    // V0
    // IDEA: KEEP CALLING read4 INTO A TEMP BUFFER, COPY OVER UNTIL n CHARS OR EOF
    /**
     * time = O(n)
     * space = O(1)
     */
    public int read(char[] buf, int n) {
        char[] buf4 = new char[4];
        int total = 0;

        while (total < n) {
            int cnt = read4(buf4);
            // NOTE !!! don't write more than n chars in total
            int toCopy = Math.min(cnt, n - total);
            for (int i = 0; i < toCopy; i++) {
                buf[total + i] = buf4[i];
            }
            total += toCopy;

            // EOF : read4 returned fewer than 4 chars
            if (cnt < 4) {
                break;
            }
        }

        return total;
    }
}
