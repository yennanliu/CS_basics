package LeetCodeJava.Greedy;

// https://leetcode.com/problems/assign-cookies/description/

import java.util.Arrays;

/**
 *  455. Assign Cookies
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Assume you are an awesome parent and want to give your children some cookies. But, you should give each child at most one cookie.
 *
 * Each child i has a greed factor g[i], which is the minimum size of a cookie that the child will be content with; and each cookie j has a size s[j]. If s[j] >= g[i], we can assign the cookie j to the child i, and the child i will be content. Your goal is to maximize the number of your content children and output the maximum number.
 *
 *
 *
 * Example 1:
 *
 * Input: g = [1,2,3], s = [1,1]
 * Output: 1
 * Explanation: You have 3 children and 2 cookies. The greed factors of 3 children are 1, 2, 3.
 * And even though you have 2 cookies, since their size is both 1, you could only make the child whose greed factor is 1 content.
 * You need to output 1.
 * Example 2:
 *
 * Input: g = [1,2], s = [1,2,3]
 * Output: 2
 * Explanation: You have 2 children and 3 cookies. The greed factors of 2 children are 1, 2.
 * You have 3 cookies and their sizes are big enough to gratify all of the children,
 * You need to output 2.
 *
 *
 * Constraints:
 *
 * 1 <= g.length <= 3 * 104
 * 0 <= s.length <= 3 * 104
 * 1 <= g[i], s[j] <= 231 - 1
 *
 *
 * Note: This question is the same as 2410: Maximum Matching of Players With Trainers.
 *
 *
 */
public class AssignCookies {

    // V0
    public int findContentChildren(int[] g, int[] s) {
        // edge

        //  sort (small -> big)
        Arrays.sort(g);
        Arrays.sort(s);

        int cnt = 0;

        // g pointer
        int i = 0;
        // s pointer
        int j = 0;

        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) {
                i += 1;
                cnt += 1;
            }

            j += 1;
        }

        return cnt;
    }


    // V0-1
    // IDEA: SORT + 2 POINTERS (GEMINI)
    public int findContentChildren_0_1(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);

        int i = 0; // child pointer (start at smallest)
        int j = 0; // cookie pointer (start at smallest)

        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) {
                i++; // child satisfied
            }
            j++; // move to next cookie regardless
        }
        return i;
    }


    // V0-2
    // IDEA: SORT + 2 POINTERS (GPT)
    public int findContentChildren_0_2(int[] g, int[] s) {

        Arrays.sort(g);
        Arrays.sort(s);

        int cnt = 0;

        int i = g.length - 1; // largest child
        int j = s.length - 1; // largest cookie

        while (i >= 0 && j >= 0) {

            if (s[j] >= g[i]) { // cookie can satisfy child
                cnt++;
                j--; // use this cookie
            }

            i--; // move to next largest child
        }

        return cnt;
    }



    // V1
    // IDEA
    // https://leetcode.com/problems/assign-cookies/editorial/
    public int findContentChildren_1(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int contentChildren = 0;
        int cookieIndex = 0;
        while (cookieIndex < s.length && contentChildren < g.length) {
            if (s[cookieIndex] >= g[contentChildren]) {
                contentChildren++;
            }
            cookieIndex++;
        }
        return contentChildren;
    }


    // V2
    // https://leetcode.com/problems/assign-cookies/solutions/4485308/on-log-n-time-o1-space-cjavapythonjs-exp-a8qu/
    public int findContentChildren_2(int[] g, int[] s) {
        int cookiesNums = s.length;
        if (cookiesNums == 0)
            return 0;
        Arrays.sort(g);
        Arrays.sort(s);

        int maxNum = 0;
        int cookieIndex = cookiesNums - 1;
        int childIndex = g.length - 1;
        while (cookieIndex >= 0 && childIndex >= 0) {
            if (s[cookieIndex] >= g[childIndex]) {
                maxNum++;
                cookieIndex--;
                childIndex--;
            } else {
                childIndex--;
            }
        }

        return maxNum;
    }




}
