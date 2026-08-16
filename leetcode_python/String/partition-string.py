"""

3597. Partition String
Medium

Given a string s, partition it into unique segments according to the following procedure:

Start building a segment beginning at index 0.
Continue extending the current segment character by character until the current segment has not been seen before.
Once the segment is unique, add it to your list of segments, mark it as seen, and begin a new segment from the next index.
Repeat until you reach the end of s.

Return an array of strings segments, where segments[i] is the ith segment created.


Example 1:

Input: s = "abbccccd"
Output: ["a","b","bc","c","cc","d"]
Explanation:

Index | Segment After Adding | Segment Exists Before? | New Segment | Updated Seen Segments
0     | "a"                  | No                     | ""          | ["a"]
1     | "b"                  | No                     | ""          | ["a", "b"]
2     | "b"                  | Yes                    | "b"         | ["a", "b"]
3     | "bc"                 | No                     | ""          | ["a", "b", "bc"]
4     | "c"                  | No                     | ""          | ["a", "b", "bc", "c"]
5     | "c"                  | Yes                    | "c"         | ["a", "b", "bc", "c"]
6     | "cc"                 | No                     | ""          | ["a", "b", "bc", "c", "cc"]
7     | "d"                  | No                     | ""          | ["a", "b", "bc", "c", "cc", "d"]

Hence, the final output is ["a", "b", "bc", "c", "cc", "d"].

Example 2:

Input: s = "aaaa"
Output: ["a","aa"]
Explanation:

Index | Segment After Adding | Segment Exists Before? | New Segment | Updated Seen Segments
0     | "a"                  | No                     | ""          | ["a"]
1     | "a"                  | Yes                    | "a"         | ["a"]
2     | "aa"                 | No                     | ""          | ["a", "aa"]
3     | "a"                  | Yes                    | "a"         | ["a", "aa"]

Hence, the final output is ["a", "aa"].


Constraints:

1 <= s.length <= 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : GREEDY SCAN WITH A SET OF ALREADY-EMITTED SEGMENTS
#
#   the procedure is fully deterministic — no choice is ever made — so a
#   single left-to-right pass reproduces it exactly. a trailing run of
#   characters that never becomes unique before the string ends simply
#   produces no segment, which is why "aaaa" yields only two segments.
#
#   the k-th distinct segment of length L needs L <= O(sqrt(n)) in the worst
#   case because all emitted segments are distinct, so the repeated slicing
#   stays near linear overall.
#
# time = O(n * sqrt(n)) worst case, space = O(n)
class Solution(object):
    def partitionString(self, s):
        n = len(s)
        seen = set()
        res = []
        i = 0
        while i < n:
            j = i
            while j < n:
                seg = s[i:j + 1]
                if seg not in seen:
                    seen.add(seg)
                    res.append(seg)
                    break
                j += 1
            i = j + 1
        return res
