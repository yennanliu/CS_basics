package LeetCodeJava.Design;

// https://leetcode.com/problems/longest-uploaded-prefix/

/**
 *  2424. Longest Uploaded Prefix
 *  Medium
 *
 *  You are given a stream of n videos, each represented by a distinct number from 1
 *  to n that you need to "upload" to a server. You need to implement a data
 *  structure that calculates the length of the longest uploaded prefix at various
 *  points in the upload process.
 *
 *  We consider i to be an uploaded prefix if all videos in the range 1 to i
 *  (inclusive) have been uploaded to the server. The longest uploaded prefix is the
 *  maximum value of i that satisfies this definition.
 *
 *  Implement the LUPrefix class:
 *    LUPrefix(int n) Initializes the object for a stream of n videos.
 *    void upload(int video) Uploads video to the server.
 *    int longest() Returns the length of the longest uploaded prefix.
 *
 *  Example 1:
 *    Input
 *      ["LUPrefix","upload","longest","upload","longest","upload","longest"]
 *      [[4],[3],[],[1],[],[2],[]]
 *    Output
 *      [null, null, 0, null, 1, null, 3]
 *    Explanation
 *      upload(3); longest() -> 0  (video 1 is missing)
 *      upload(1); longest() -> 1
 *      upload(2); longest() -> 3  ([1,2,3] are all uploaded)
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    1 <= video <= n
 *    All values of video are distinct.
 *    At most 2 * 10^5 calls in total will be made to upload and longest.
 */
public class LongestUploadedPrefix {

    // V0
    // IDEA: SEEN FLAGS + A CURSOR THAT ONLY EVER MOVES FORWARD
    //
    //       `longest` can never decrease, so keep a cursor at the first MISSING
    //       video and, after each upload, walk it forward while the next number is
    //       already present.
    //       the cursor advances at most n times in TOTAL, so the amortised cost per
    //       call is O(1) even though a single upload can jump it a long way (see the
    //       example, where uploading 2 takes it from 1 to 3).
    /**
     * time = O(1) amortised per upload / longest
     * space = O(n)
     */
    private final boolean[] seen;
    private final int n;
    private int longest; // length of the current longest prefix

    public LongestUploadedPrefix(int n) {
        this.n = n;
        this.seen = new boolean[n + 2];
        this.longest = 0;
    }

    public void upload(int video) {
        seen[video] = true;
        while (longest + 1 <= n && seen[longest + 1]) {
            longest++;
        }
    }

    public int longest() {
        return longest;
    }
}
