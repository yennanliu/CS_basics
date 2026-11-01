package LeetCodeJava.String;

// https://leetcode.com/problems/partition-string/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 3597. Partition String
 * Medium
 *
 * Given a string s, partition it into unique segments according to the following procedure:
 *
 * Start building a segment beginning at index 0.
 * Continue extending the current segment character by character until the current segment has not been seen before.
 * Once the segment is unique, add it to your list of segments, mark it as seen, and begin a new segment from the next index.
 * Repeat until you reach the end of s.
 *
 * Return an array of strings segments, where segments[i] is the ith segment created.
 *
 *
 * Example 1:
 *
 * Input: s = "abbccccd"
 * Output: ["a","b","bc","c","cc","d"]
 * Explanation:
 *
 * Index | Segment After Adding | Segment Exists Before? | New Segment | Updated Seen Segments
 * 0     | "a"                  | No                     | ""          | ["a"]
 * 1     | "b"                  | No                     | ""          | ["a", "b"]
 * 2     | "b"                  | Yes                    | "b"         | ["a", "b"]
 * 3     | "bc"                 | No                     | ""          | ["a", "b", "bc"]
 * 4     | "c"                  | No                     | ""          | ["a", "b", "bc", "c"]
 * 5     | "c"                  | Yes                    | "c"         | ["a", "b", "bc", "c"]
 * 6     | "cc"                 | No                     | ""          | ["a", "b", "bc", "c", "cc"]
 * 7     | "d"                  | No                     | ""          | ["a", "b", "bc", "c", "cc", "d"]
 *
 * Hence, the final output is ["a", "b", "bc", "c", "cc", "d"].
 *
 * Example 2:
 *
 * Input: s = "aaaa"
 * Output: ["a","aa"]
 * Explanation:
 *
 * Index | Segment After Adding | Segment Exists Before? | New Segment | Updated Seen Segments
 * 0     | "a"                  | No                     | ""          | ["a"]
 * 1     | "a"                  | Yes                    | "a"         | ["a"]
 * 2     | "aa"                 | No                     | ""          | ["a", "aa"]
 * 3     | "a"                  | Yes                    | "a"         | ["a", "aa"]
 *
 * Hence, the final output is ["a", "aa"].
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s contains only lowercase English letters.
 *
 */
public class PartitionString {

    // V0
    // IDEA: GREEDY SIMULATION WITH A SET OF SEGMENTS ALREADY EMITTED
    //   grow the current segment one char at a time; the moment it is new, emit it and start
    //   over. A trailing segment that never becomes new is dropped ("aaaa" -> ["a", "aa"]).
    //   All emitted segments are distinct, so each has length O(sqrt(n)).
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public List<String> partitionString(String s) {
        Set<String> seen = new HashSet<>();
        List<String> segments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            current.append(s.charAt(i));
            String segment = current.toString();
            if (!seen.contains(segment)) {
                seen.add(segment);
                segments.add(segment);
                current.setLength(0);
            }
        }
        return segments;
    }
}
