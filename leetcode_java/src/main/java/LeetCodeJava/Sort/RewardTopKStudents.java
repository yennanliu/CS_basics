package LeetCodeJava.Sort;

// https://leetcode.com/problems/reward-top-k-students/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  2512. Reward Top K Students
 *  Medium
 *
 *  You are given two string arrays positive_feedback and negative_feedback,
 *  containing the words denoting positive and negative feedback, respectively.
 *  Note that no word is both positive and negative.
 *
 *  Initially every student has 0 points. Each positive word in a feedback report
 *  increases the points of a student by 3, whereas each negative word decreases
 *  the points by 1.
 *
 *  You are given n feedback reports, represented by a 0-indexed string array
 *  report and a 0-indexed integer array student_id, where student_id[i]
 *  represents the ID of the student who has received the feedback report
 *  report[i]. The ID of each student is unique.
 *
 *  Given an integer k, return the top k students after ranking them in
 *  non-increasing order by their points. In case more than one student has the
 *  same points, the one with the lower ID ranks higher.
 *
 *  Example 1:
 *    Input: positive_feedback = ["smart","brilliant","studious"],
 *           negative_feedback = ["not"],
 *           report = ["this student is studious","the student is smart"],
 *           student_id = [1,2], k = 2
 *    Output: [1,2]
 *    Explanation: Both students have 3 points, but student 1 has the lower ID.
 *
 *  Example 2:
 *    Input: positive_feedback = ["smart","brilliant","studious"],
 *           negative_feedback = ["not"],
 *           report = ["this student is not studious","the student is smart"],
 *           student_id = [1,2], k = 2
 *    Output: [2,1]
 *
 *  Constraints:
 *    1 <= positive_feedback.length, negative_feedback.length <= 10^4
 *    1 <= positive_feedback[i].length, negative_feedback[j].length <= 100
 *    No word is present in both positive_feedback and negative_feedback.
 *    n == report.length == student_id.length
 *    1 <= n <= 10^4
 *    1 <= report[i].length <= 100
 *    1 <= student_id[i] <= 10^9
 *    All the values of student_id[i] are unique.
 *    1 <= k <= n
 */
public class RewardTopKStudents {

    // V0
    // IDEA: HASH SET SCORING + CUSTOM COMPARATOR
    //       put both vocabularies in sets so a word lookup is O(1), then score
    //       every report by splitting on spaces (+3 / -1 per hit).
    //       ranking mixes directions (points DESC, id ASC), so the comparator
    //       compares points reversed first, then id normally.
    //       NOTE: use Integer.compare, never `a - b` (ids go up to 1e9).
    /**
     * time = O(P + N + n * L + n log n)
     * space = O(P + N + n)
     */
    public List<Integer> topStudents(String[] positive_feedback,
                                     String[] negative_feedback,
                                     String[] report,
                                     int[] student_id,
                                     int k) {

        Set<String> pos = new HashSet<>(Arrays.asList(positive_feedback));
        Set<String> neg = new HashSet<>(Arrays.asList(negative_feedback));

        int n = report.length;
        int[][] scored = new int[n][2];   // [points, id]
        for (int i = 0; i < n; i++) {
            int point = 0;
            for (String w : report[i].split(" ")) {
                if (pos.contains(w)) {
                    point += 3;
                } else if (neg.contains(w)) {
                    point -= 1;
                }
            }
            scored[i][0] = point;
            scored[i][1] = student_id[i];
        }

        Arrays.sort(scored, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(b[0], a[0]);   // points DESC
            }
            return Integer.compare(a[1], b[1]);       // id ASC
        });

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            res.add(scored[i][1]);
        }
        return res;
    }
}
