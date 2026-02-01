package LeetCodeJava.String;

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
    // IDEA: STRING OP
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkRecord(String s) {
        // edge
        if (s.isEmpty() || s.length() == 1) {
            return true;
        }
        if (s.length() == 2) {
            if (s.charAt(0) == 'A' && s.charAt(1) == 'A') {
                return false;
            }
            return true;
        }

        int absent_cnt = 0;
        int con_late_cnt = 0;

        for (char c : s.toCharArray()) {
            // 'A'
            if (c == 'A') {
                absent_cnt += 1;
                con_late_cnt = 0;
            }
            // 'C'
            else if (c == 'L') {
                con_late_cnt += 1;
            }
            // 'P'
            else {
                con_late_cnt = 0;
            }

            if (absent_cnt >= 2) {
                return false;
            }
            if (con_late_cnt >= 3) {
                return false;
            }

        }

        return true;
    }

    // V0-1
    // IDEA: BRUTE FORCE (fixed by gemini)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkRecord_0_1(String s) {
        int absentCount = 0;

        // Condition 1: Check total Absent count
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A') {
                absentCount++;
            }
        }

        // Condition 2: Check for 3 or more consecutive Late days
        // Condition 1 check: Strictly fewer than 2 Absences
        return absentCount < 2 && !s.contains("LLL");
    }

    // V0-2
    // IDEA: BRUTE FORCE (fixed by GPT)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkRecord_0_2(String s) {
        if (s.length() <= 1)
            return true;

        int absentCnt = 0;
        char prev = '\0';
        char prevPrev = '\0';

        for (int i = 0; i < s.length(); i++) {
            char status = s.charAt(i);

            if (status == 'A') {
                absentCnt++;
                if (absentCnt >= 2)
                    return false;
            }

            if (status == 'L') {
                if (prev == 'L' && prevPrev == 'L') {
                    return false;
                }
            }

            // update previous characters
            prevPrev = prev;
            prev = status;
        }

        return true;
    }



    // V1
    // https://leetcode.com/problems/student-attendance-record-i/solutions/2826653/java-100-easiest-solution-by-sharforaz_r-x2ra/
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
