package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-students-unable-to-eat-lunch/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1700. Number of Students Unable to Eat Lunch
 * Easy
 *
 * The school cafeteria offers circular and square sandwiches at lunch break, referred to by numbers
 * 0 and 1 respectively. All students stand in a queue. Each student either prefers square or
 * circular sandwiches.
 *
 * The number of sandwiches in the cafeteria is equal to the number of students. The sandwiches are
 * placed in a stack. At each step:
 *
 * - If the student at the front of the queue prefers the sandwich on the top of the stack, they will
 *   take it and leave the queue.
 * - Otherwise, they will leave it and go to the queue's end.
 *
 * This continues until none of the queue students want to take the top sandwich and are thus unable
 * to eat.
 *
 * You are given two integer arrays students and sandwiches where sandwiches[i] is the type of the
 * ith sandwich in the stack (i = 0 is the top of the stack) and students[j] is the preference of the
 * jth student in the initial queue (j = 0 is the front of the queue). Return the number of students
 * that are unable to eat.
 *
 *
 * Example 1:
 *
 * Input: students = [1,1,0,0], sandwiches = [0,1,0,1]
 * Output: 0
 *
 * Example 2:
 *
 * Input: students = [1,1,1,0,0,1], sandwiches = [1,0,0,0,1,1]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= students.length, sandwiches.length <= 100
 * students.length == sandwiches.length
 * sandwiches[i] is 0 or 1
 * students[i] is 0 or 1
 *
 */
public class NumberOfStudentsUnableToEatLunch {

    // V0
    // IDEA: COUNTING (the queue ORDER is irrelevant, only how many students want each type)
    // a student who does not want the top sandwich just cycles to the back, so the top sandwich is
    // taken as long as ANY remaining student wants its type. The process stalls at the first
    // sandwich nobody left wants, and everyone still waiting is unable to eat.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countStudents(int[] students, int[] sandwiches) {
        // wantCount[t] = students still in the queue who prefer sandwich type t
        int[] wantCount = new int[2];
        for (int preference : students) {
            wantCount[preference]++;
        }

        for (int sandwich : sandwiches) {
            if (wantCount[sandwich] == 0) {
                // stuck: nobody takes this sandwich; the remaining students all want the other type
                return wantCount[1 - sandwich];
            }
            wantCount[sandwich]--;
        }
        return 0;
    }

    // V1
    // IDEA: QUEUE SIMULATION (literal transcription of the statement)
    // rotate the queue; if a full rotation passes without anyone eating, the rest are stuck.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int countStudents_1(int[] students, int[] sandwiches) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int preference : students) {
            queue.offerLast(preference);
        }

        int top = 0;
        // number of consecutive students who refused the current top sandwich
        int refusals = 0;
        while (!queue.isEmpty() && refusals < queue.size()) {
            int student = queue.pollFirst();
            if (student == sandwiches[top]) {
                top++;
                refusals = 0;
            } else {
                queue.offerLast(student);
                refusals++;
            }
        }
        return queue.size();
    }
}
