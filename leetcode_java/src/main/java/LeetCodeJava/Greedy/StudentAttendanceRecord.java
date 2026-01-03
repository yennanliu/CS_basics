package LeetCodeJava.Greedy;

// https://leetcode.com/problems/student-attendance-record-i/description/
/**
 * 551. Student Attendance Record I
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given a string s representing an attendance record for a student where each character signifies whether the student was absent, late, or present on that day. The record only contains the following three characters:
 *
 * 'A': Absent.
 * 'L': Late.
 * 'P': Present.
 * The student is eligible for an attendance award if they meet both of the following criteria:
 *
 * The student was absent ('A') for strictly fewer than 2 days total.
 * The student was never late ('L') for 3 or more consecutive days.
 * Return true if the student is eligible for an attendance award, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "PPALLP"
 * Output: true
 * Explanation: The student has fewer than 2 absences and was never late 3 or more consecutive days.
 * Example 2:
 *
 * Input: s = "PPALLL"
 * Output: false
 * Explanation: The student was late 3 consecutive days in the last 3 days, so is not eligible for the award.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either 'A', 'L', or 'P'.
 *
 */
public class StudentAttendanceRecord {

    // V0
//    public boolean checkRecord(String s) {
//
//    }

    // V1
    // https://leetcode.com/problems/student-attendance-record-i/solutions/2826653/java-100-easiest-solution-by-sharforaz_r-x2ra/
    public boolean checkRecord_1(String s) {
        int A = 0, L = 0, max = 0;
        for (char c : s.toCharArray()) {
            if (c == 'A')
                A++;
        }
        if (A >= 2)
            return false;
        for (char c : s.toCharArray()) {
            if (c == 'L') {
                L++;
            } else {
                L = 0;
            }
            max = Math.max(max, L);
        }
        return max < 3;
    }


    // V2
    // https://leetcode.com/problems/student-attendance-record-i/solutions/7333244/easy-and-concise-solution-explanation-be-el3d/
    public boolean checkRecord_2(String s) {
        int countA = 0, countL = 0;

        for (char ch : s.toCharArray()) {
            if (ch != 'L')
                countL = 0;
            if (ch == 'L')
                countL++;
            if (ch == 'A')
                countA++;
            if (countA == 2 || countL == 3)
                return false;
        }

        return true;
    }




}
