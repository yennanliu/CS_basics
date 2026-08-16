package LeetCodeJava.Array;

// https://leetcode.com/problems/read-n-characters-given-read4-ii-call-multiple-times/description/
/**
 * 158. Read N Characters Given read4 II - Call Multiple Times
 * Hard
 * Lock: Prime
 *
 * Given a file and assume that you can only read the file using a given method read4,
 * implement a method read to read n characters. Your method read may be called multiple times.
 *
 * Method read4:
 *
 * The API read4 reads four consecutive characters from file, then writes those characters
 * into the buffer array buf4.
 *
 * The return value is the number of actual characters read.
 *
 * Note that read4() has its own file pointer, much like FILE *fp in C.
 *
 * Definition of read4:
 *
 *     Parameter:  char[] buf4
 *     Returns:    int
 *
 * buf4[] is a destination, not a source. The results from read4 will be copied to buf4[].
 *
 * Below is a high-level example of how read4 works:
 *
 * File file("abcde");  // File is "abcde", initially file pointer (fp) points to 'a'
 * char[] buf4 = new char[4];
 * read4(buf4);  // read4 returns 4. Now buf4 = "abcd", fp points to 'e'
 * read4(buf4);  // read4 returns 1. Now buf4 = "e", fp points to end of file
 * read4(buf4);  // read4 returns 0. Now buf4 = "", fp points to end of file
 *
 * Method read:
 *
 * By using the read4 method, implement the method read that reads n characters from file and
 * store it in the buffer array buf. Consider that you cannot manipulate file directly.
 *
 * The return value is the number of actual characters read.
 *
 * Definition of read:
 *
 *     Parameters: char[] buf, int n
 *     Returns:    int
 *
 * buf[] is a destination, not a source. You will need to write the results to buf[].
 *
 * Note:
 *
 * - Consider that you cannot manipulate the file directly. The file is only accessible
 *   for read4 but not for read.
 * - The read function may be called multiple times.
 * - Please remember to RESET your class variables declared in Solution, as static/class
 *   variables are persisted across multiple test cases.
 * - You may assume the destination buffer array, buf, is guaranteed to have enough space
 *   for storing n characters.
 * - It is guaranteed that in a given test case the same buffer buf is called by read.
 *
 *
 * Example 1:
 *
 * Input: file = "abc", queries = [1,2,1]
 * Output: [1,2,0]
 * Explanation:
 * sol.read(buf, 1);  // buf contains "a", return 1
 * sol.read(buf, 2);  // buf contains "bc", return 2
 * sol.read(buf, 1);  // end of file reached, return 0
 *
 * Example 2:
 *
 * Input: file = "abc", queries = [4,1]
 * Output: [3,0]
 * Explanation:
 * sol.read(buf, 4);  // buf contains "abc", return 3
 * sol.read(buf, 1);  // end of file reached, return 0
 *
 *
 * Constraints:
 *
 * 1 <= file.length <= 500
 * file consists of English letters and digits.
 * 1 <= queries.length <= 10
 * 1 <= queries[i] <= 500
 *
 */
public class ReadNCharactersGivenRead4II {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    // (on LC the Solution class `extends Reader4`)
    private int read4(char[] buf4) {
        return 0;
    }

    // V0
    // IDEA: KEEP AN INTERNAL 4-CHAR BUFFER ACROSS CALLS
    /**
     *  The whole difficulty vs LC 157 is that read4 hands back up to 4 chars, but the
     *  caller may only want 1. The leftover chars MUST survive until the next read()
     *  call
     *
     *  -> so we store them in INSTANCE state (buf4 / i / cnt), NOT in a local var.
     *
     *  time  = O(n) per read() call
     *  space = O(1)
     */

    /** NOTE !!!
     *
     *  below 3 fields are the `cross-call` state.
     *  they are what makes LC 158 different from LC 157.
     */
    private char[] buf4 = new char[4]; // internal scratch buffer
    private int i = 0;                 // next index to consume inside buf4
    private int cnt = 0;               // number of valid chars currently in buf4

    /**
     * @param buf Destination buffer
     * @param n   Number of characters to read
     * @return    The number of actual characters read
     */
    public int read(char[] buf, int n) {
        int j = 0; // how many chars we have written into buf so far

        while (j < n) {

            /** NOTE !!!
             *
             *  internal buffer drained -> refill it from the file
             */
            if (this.i == this.cnt) {
                this.cnt = read4(this.buf4);
                this.i = 0;
                if (this.cnt == 0) {
                    break; // EOF, nothing more to give
                }
            }

            buf[j] = this.buf4[this.i];
            this.i += 1;
            j += 1;
        }

        return j;
    }

}
