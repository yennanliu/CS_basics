package LeetCodeJava.Heap;

// https://leetcode.com/problems/longest-happy-string/description/

import java.util.PriorityQueue;

/**
 * 1405. Longest Happy String
 * Medium
 * Topics
 * Companies
 * Hint
 * A string s is called happy if it satisfies the following conditions:
 *
 * s only contains the letters 'a', 'b', and 'c'.
 * s does not contain any of "aaa", "bbb", or "ccc" as a substring.
 * s contains at most a occurrences of the letter 'a'.
 * s contains at most b occurrences of the letter 'b'.
 * s contains at most c occurrences of the letter 'c'.
 * Given three integers a, b, and c, return the longest possible happy string. If there are multiple longest happy strings, return any of them. If there is no such string, return the empty string "".
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 *
 * Example 1:
 *
 * Input: a = 1, b = 1, c = 7
 * Output: "ccaccbcc"
 * Explanation: "ccbccacc" would also be a correct answer.
 * Example 2:
 *
 * Input: a = 7, b = 1, c = 0
 * Output: "aabaa"
 * Explanation: It is the only correct answer in this case.
 *
 *
 * Constraints:
 *
 * 0 <= a, b, c <= 100
 * a + b + c > 0
 *
 */
public class LongestHappyString {

    // V0
//    public String longestDiverseString(int a, int b, int c) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=8u-H6O_XQKE
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F1405-longest-happy-string.java
//    public String longestDiverseString_1(int a, int b, int c) {
//        String res = "";
//        PriorityQueue<Pair<Integer, Character>> maxHeap = new PriorityQueue<>(
//                (i1, i2) -> i2.getKey() - i1.getKey());
//        if (a > 0) {
//            maxHeap.add(new Pair<>(a, 'a'));
//        }
//        if (b > 0) {
//            maxHeap.add(new Pair<>(b, 'b'));
//        }
//        if (c > 0) {
//            maxHeap.add(new Pair<>(c, 'c'));
//        }
//
//        while (!maxHeap.isEmpty()) {
//            Pair<Integer, Character> item = maxHeap.poll();
//            Integer count = item.getKey();
//            Character ch = item.getValue();
//            if (res.length() > 1
//                    && ch.equals(res.charAt(res.length() - 1))
//                    && ch.equals(res.charAt(res.length() - 2))) {
//                if (maxHeap.isEmpty()) {
//                    break;
//                }
//                Pair<Integer, Character> item2 = maxHeap.poll();
//                Integer count2 = item2.getKey();
//                Character ch2 = item2.getValue();
//                res += ch2;
//                count2 -= 1;
//                if (count2 > 0) {
//                    maxHeap.add(new Pair<>(count2, ch2));
//                }
//            } else {
//                res += ch;
//                count -= 1;
//            }
//            if (count > 0) {
//                maxHeap.add(new Pair<>(count, ch));
//            }
//        }
//        return res;
//    }


    // V2-1
    // https://leetcode.com/problems/longest-happy-string/editorial/
    // IDEA: PQ
    public String longestDiverseString_2_1(int a, int b, int c) {
        PriorityQueue<Pair> pq = new PriorityQueue<Pair>((x, y) -> (y.count - x.count));
        // Add the counts of a, b and c in priority queue.
        if (a > 0) {
            pq.add(new Pair(a, 'a'));
        }

        if (b > 0) {
            pq.add(new Pair(b, 'b'));
        }

        if (c > 0) {
            pq.add(new Pair(c, 'c'));
        }

        StringBuilder ans = new StringBuilder();
        while (!pq.isEmpty()) {
            Pair p = pq.poll();
            int count = p.count;
            char character = p.character;
            // If three consecutive characters exists, pick the second most
            // frequent character.
            if (ans.length() >= 2 &&
                    ans.charAt(ans.length() - 1) == p.character &&
                    ans.charAt(ans.length() - 2) == p.character) {
                if (pq.isEmpty())
                    break;

                Pair temp = pq.poll();
                ans.append(temp.character);
                if (temp.count - 1 > 0) {
                    pq.add(new Pair(temp.count - 1, temp.character));
                }
            } else {
                count--;
                ans.append(character);
            }

            // If count is greater than zero, add it to priority queue.
            if (count > 0) {
                pq.add(new Pair(count, character));
            }
        }
        return ans.toString();
    }

    class Pair {

        int count;
        char character;

        Pair(int count, char character) {
            this.count = count;
            this.character = character;
        }
    }


    // V2-2
    // https://leetcode.com/problems/longest-happy-string/editorial/
    // IDEA: Using Counters
    public String longestDiverseString_2_2(int a, int b, int c) {
        int curra = 0, currb = 0, currc = 0;
        // Maximum total iterations possible is given by the sum of a, b and c.
        int totalIterations = a + b + c;
        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < totalIterations; i++) {
            if ((a >= b && a >= c && curra != 2) ||
                    (a > 0 && (currb == 2 || currc == 2))) {
                // If 'a' is maximum and it's streak is less than 2, or if streak of 'b' or 'c' is 2, then 'a' will be the next character.
                ans.append('a');
                a--;
                curra++;
                currb = 0;
                currc = 0;
            } else if ((b >= a && b >= c && currb != 2) ||
                    (b > 0 && (currc == 2 || curra == 2))) {
                // If 'b' is maximum and it's streak is less than 2, or if streak of 'a' or 'c' is 2, then 'b' will be the next character.
                ans.append('b');
                b--;
                currb++;
                curra = 0;
                currc = 0;
            } else if ((c >= a && c >= b && currc != 2) ||
                    (c > 0 && (curra == 2 || currb == 2))) {
                // If 'c' is maximum and it's streak is less than 2, or if streak of 'a' or 'b' is 2, then 'c' will be the next character.
                ans.append('c');
                c--;
                currc++;
                curra = 0;
                currb = 0;
            }
        }
        return ans.toString();
    }

    // V3
    // https://leetcode.com/problems/longest-happy-string/solutions/5918628/how-to-easily-solve-the-longest-happy-st-alwj/
    // IDEA: PQ
    public String longestDiverseString_3(int a, int b, int c) {
        // Priority queue to store the characters and their counts.
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] - x[0]);
        if (a > 0)
            pq.offer(new int[] { a, 'a' });
        if (b > 0)
            pq.offer(new int[] { b, 'b' });
        if (c > 0)
            pq.offer(new int[] { c, 'c' });

        StringBuilder result = new StringBuilder();

        while (!pq.isEmpty()) {
            int[] first = pq.poll();

            // Check if last two characters are the same.
            if (result.length() >= 2 && result.charAt(result.length() - 1) == first[1] &&
                    result.charAt(result.length() - 2) == first[1]) {

                if (pq.isEmpty())
                    break; // No more valid characters.

                // Pick the second character.
                int[] second = pq.poll();
                result.append((char) second[1]);
                second[0]--;

                if (second[0] > 0)
                    pq.offer(second);
                pq.offer(first);
            } else {
                result.append((char) first[1]);
                first[0]--;

                if (first[0] > 0)
                    pq.offer(first);
            }
        }

        return result.toString();
    }

    // V4
    // https://leetcode.com/problems/longest-happy-string/solutions/5919175/understand-greed-once-beats-100-with-add-i9l9/
    public String longestDiverseString_4(int a, int b, int c) {
        int currA = 0, currB = 0, currC = 0;
        int maxLen = a + b + c, i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < (maxLen)) {
            if (currA != 2 && a >= b && a >= c || a > 0 && (currB == 2 || currC == 2)) {
                sb.append('a');
                currA++;
                currB = 0;
                currC = 0;
                a--;
            }

            else if (currB != 2 && b >= a && b >= c || b > 0 && (currA == 2 || currC == 2)) {
                sb.append('b');
                currB++;
                currA = 0;
                currC = 0;
                b--;
            }

            else if (currC != 2 && c >= a && c >= b || c > 0 && (currA == 2 || currB == 2)) {
                sb.append('c');
                currC++;
                currA = 0;
                currB = 0;
                c--;
            }
            i++;
        }
        return sb.toString();
    }

}
