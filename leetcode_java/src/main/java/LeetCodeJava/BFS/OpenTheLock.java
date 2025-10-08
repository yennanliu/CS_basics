package LeetCodeJava.BFS;

// https://leetcode.com/problems/open-the-lock/description/

import dev.workspace6;

import java.util.*;

/**
 * 752. Open the Lock
 * Medium
 * Topics
 * Companies
 * Hint
 * You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'. The wheels can rotate freely and wrap around: for example we can turn '9' to be '0', or '0' to be '9'. Each move consists of turning one wheel one slot.
 *
 * The lock initially starts at '0000', a string representing the state of the 4 wheels.
 *
 * You are given a list of deadends dead ends, meaning if the lock displays any of these codes, the wheels of the lock will stop turning and you will be unable to open it.
 *
 * Given a target representing the value of the wheels that will unlock the lock, return the minimum total number of turns required to open the lock, or -1 if it is impossible.
 *
 *
 *
 * Example 1:
 *
 * Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
 * Output: 6
 * Explanation:
 * A sequence of valid moves would be "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202".
 * Note that a sequence like "0000" -> "0001" -> "0002" -> "0102" -> "0202" would be invalid,
 * because the wheels of the lock become stuck after the display becomes the dead end "0102".
 * Example 2:
 *
 * Input: deadends = ["8888"], target = "0009"
 * Output: 1
 * Explanation: We can turn the last wheel in reverse to move from "0000" -> "0009".
 * Example 3:
 *
 * Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
 * Output: -1
 * Explanation: We cannot reach the target without getting stuck.
 *
 *
 * Constraints:
 *
 * 1 <= deadends.length <= 500
 * deadends[i].length == 4
 * target.length == 4
 * target will not be in the list deadends.
 * target and deadends[i] consist of digits only.
 */
public class OpenTheLock {

    // V0-1
    // IDEA: BFS + String replace (fixed by gpt)
    public int openLock_0_1(String[] deadends, String target) {
        // Edge: starting point is the target
        if (target.equals("0000")) {
            return 0;
        }

        // Deadends set
        Set<String> dead = new HashSet<>();
        for (String x : deadends) {
            dead.add(x);
            if (x.equals("0000")) { // can't start
                return -1;
            }
        }

        // BFS
        Queue<String> q = new LinkedList<>();
        q.add("0000");
        Set<String> visited = new HashSet<>();
        visited.add("0000");
        int layer = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            // process all nodes in current layer
            for (int k = 0; k < size; k++) {
                String cur = q.poll();

                // Found target
                if (cur.equals(target)) {
                    return layer;
                }

                // Move 4 directions (wheel rotations)
                for (int i = 0; i < 4; i++) {
                    int val = cur.charAt(i) - '0';

                    int valMinus = (val == 0) ? 9 : val - 1;
                    int valPlus = (val == 9) ? 0 : val + 1;

                    String str1 = cur.substring(0, i) + valMinus + cur.substring(i + 1);
                    String str2 = cur.substring(0, i) + valPlus + cur.substring(i + 1);

                    if (!dead.contains(str1) && !visited.contains(str1)) {
                        q.add(str1);
                        visited.add(str1);
                    }
                    if (!dead.contains(str2) && !visited.contains(str2)) {
                        q.add(str2);
                        visited.add(str2);
                    }
                }
            }
            layer++;
        }

        return -1; // not found
    }

    // V0-2
    // IDEA: BFS + custom class (fixed by gpt)
    public class LockInfo {
        String lock;
        int moves;

        public LockInfo(String lock, int moves) {
            this.lock = lock;
            this.moves = moves;
        }
    }

    public int openLock_0_2(String[] deadends, String target) {
        // edge case
        if (target.equals("0000")) {
            return 0;
        }

        Set<String> deadendSet = new HashSet<>(Arrays.asList(deadends));
        if (deadendSet.contains("0000"))
            return -1;

        Queue<LockInfo> q = new LinkedList<>();
        /** NOTE !!! we use `visited to re-visit the locks */
        Set<String> visited = new HashSet<>();

        q.add(new LockInfo("0000", 0));
        visited.add("0000");

        while (!q.isEmpty()) {
            LockInfo cur = q.poll();
            String lock = cur.lock;
            int moves = cur.moves;

            if (lock.equals(target)) {
                return moves;
            }

            // deadlock
            if (deadendSet.contains(lock)) {
                continue;
            }

            // move
            for (int i = 0; i < 4; i++) {
                String lock1 = lockMover(lock, i, true);
                String lock2 = lockMover(lock, i, false);

                /** NOTE !!! we use `visited to re-visit the locks */
                if (!visited.contains(lock1) && !deadendSet.contains(lock1)) {
                    q.add(new LockInfo(lock1, moves + 1));
                    visited.add(lock1);
                }

                /** NOTE !!! we use `visited to re-visit the locks */
                if (!visited.contains(lock2) && !deadendSet.contains(lock2)) {
                    q.add(new LockInfo(lock2, moves + 1));
                    visited.add(lock2);
                }
            }
        }

        return -1;
    }

    /**  lockMover is a helper func for moving lock */
    public String lockMover(String lock, int idx, boolean clockwise) {
        char[] chars = lock.toCharArray();
        if (clockwise) {
            chars[idx] = chars[idx] == '9' ? '0' : (char) (chars[idx] + 1);
        } else {
            chars[idx] = chars[idx] == '0' ? '9' : (char) (chars[idx] - 1);
        }
        return new String(chars);
    }

    // V1
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0752-open-the-lock.java
    // https://www.youtube.com/watch?v=Pzg3bCDY87w
    public int openLock_1(String[] deadends, String target) {
        Set<String> visited = new HashSet<>();
        for (String deadend : deadends) {
            if (deadend.equals("0000")) {
                return -1;
            }
            visited.add(deadend);
        }

        Queue<String> queue = new LinkedList<>();
        queue.offer("0000");
        visited.add("0000");

        int turns = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String lock = queue.poll();
                if (lock.equals(target)) {
                    return turns;
                }
                List<String> children = generateChildren(lock);
                for (String child : children) {
                    if (!visited.contains(child)) {
                        visited.add(child);
                        queue.offer(child);
                    }
                }
            }
            turns++;
        }
        return -1;
    }

    private List<String> generateChildren(String lock) {
        List<String> children = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            char[] digits = lock.toCharArray();
            digits[i] = (char) (((digits[i] - '0' + 1) % 10) + '0');
            children.add(new String(digits));
            digits[i] = (char) (((digits[i] - '0' - 2 + 10) % 10) + '0');
            children.add(new String(digits));
        }
        return children;
    }


    // V2
    // https://leetcode.com/problems/open-the-lock/editorial/
    // IDEA: BFS
    public int openLock_2(String[] deadends, String target) {

//        // Map the next slot digit for each current slot digit.
//        Map<Character, Character> nextSlot = Map.of(
//                '0', '1',
//                '1', '2',
//                '2', '3',
//                '3', '4',
//                '4', '5',
//                '5', '6',
//                '6', '7',
//                '7', '8',
//                '8', '9',
//                '9', '0');
//        // Map the previous slot digit for each current slot digit.
//        Map<Character, Character> prevSlot = Map.of(
//                '0', '9',
//                '1', '0',
//                '2', '1',
//                '3', '2',
//                '4', '3',
//                '5', '4',
//                '6', '5',
//                '7', '6',
//                '8', '7',
//                '9', '8');

        // Map the next slot digit for each current slot digit.
        Map<Character, Character> nextSlot = new HashMap<>();
        nextSlot.put('0', '1');
        nextSlot.put('1', '2');
        nextSlot.put('2', '3');
        nextSlot.put('3', '4');
        nextSlot.put('4', '5');
        nextSlot.put('5', '6');
        nextSlot.put('6', '7');
        nextSlot.put('7', '8');
        nextSlot.put('8', '9');
        nextSlot.put('9', '0');

        // Map the previous slot digit for each current slot digit.
        Map<Character, Character> prevSlot = new HashMap<>();
        prevSlot.put('0', '9');
        prevSlot.put('1', '0');
        prevSlot.put('2', '1');
        prevSlot.put('3', '2');
        prevSlot.put('4', '3');
        prevSlot.put('5', '4');
        prevSlot.put('6', '5');
        prevSlot.put('7', '6');
        prevSlot.put('8', '7');
        prevSlot.put('9', '8');

        // Set to store visited and dead-end combinations.
        Set<String> visitedCombinations = new HashSet<>(Arrays.asList(deadends));
        // Queue to store combinations generated after each turn.
        Queue<String> pendingCombinations = new LinkedList<String>();

        // Count the number of wheel turns made.
        int turns = 0;

        // If the starting combination is also a dead-end,
        // then we can't move from the starting combination.
        if (visitedCombinations.contains("0000")) {
            return -1;
        }

        // Start with the initial combination '0000'.
        pendingCombinations.add("0000");
        visitedCombinations.add("0000");

        while (!pendingCombinations.isEmpty()) {
            // Explore all the combinations of the current level.
            int currLevelNodesCount = pendingCombinations.size();
            for (int i = 0; i < currLevelNodesCount; i++) {
                // Get the current combination from the front of the queue.
                String currentCombination = pendingCombinations.poll();

                // If the current combination matches the target,
                // return the number of turns/level.
                if (currentCombination.equals(target)) {
                    return turns;
                }

                // Explore all possible new combinations by turning each wheel in both
                // directions.
                for (int wheel = 0; wheel < 4; wheel += 1) {
                    // Generate the new combination by turning the wheel to the next digit.
                    StringBuilder newCombination = new StringBuilder(currentCombination);
                    newCombination.setCharAt(wheel, nextSlot.get(newCombination.charAt(wheel)));
                    // If the new combination is not a dead-end and was never visited,
                    // add it to the queue and mark it as visited.
                    if (!visitedCombinations.contains(newCombination.toString())) {
                        pendingCombinations.add(newCombination.toString());
                        visitedCombinations.add(newCombination.toString());
                    }

                    // Generate the new combination by turning the wheel to the previous digit.
                    newCombination = new StringBuilder(currentCombination);
                    newCombination.setCharAt(wheel, prevSlot.get(newCombination.charAt(wheel)));
                    // If the new combination is not a dead-end and is never visited,
                    // add it to the queue and mark it as visited.
                    if (!visitedCombinations.contains(newCombination.toString())) {
                        pendingCombinations.add(newCombination.toString());
                        visitedCombinations.add(newCombination.toString());
                    }
                }
            }
            // We will visit next-level combinations.
            turns += 1;
        }
        // We never reached the target combination.
        return -1;
    }

    // V3
    // https://leetcode.com/problems/open-the-lock/solutions/5057217/java-solution-by-archivebizzle-dawj/
    public int openLock_3(String[] deadends, String target) {
        Set<String> seen = new HashSet<>(Arrays.asList(deadends));
        if (seen.contains("0000"))
            return -1;
        if (target.equals("0000"))
            return 0;

        int ans = 0;
        Queue<String> q = new ArrayDeque<>(Arrays.asList("0000"));

        while (!q.isEmpty()) {
            ++ans;
            for (int sz = q.size(); sz > 0; --sz) {
                StringBuilder sb = new StringBuilder(q.poll());
                for (int i = 0; i < 4; ++i) {
                    final char cache = sb.charAt(i);
                    // Increase the i-th digit by 1.
                    sb.setCharAt(i, sb.charAt(i) == '9' ? '0' : (char) (sb.charAt(i) + 1));
                    String word = sb.toString();
                    if (word.equals(target))
                        return ans;
                    if (!seen.contains(word)) {
                        q.offer(word);
                        seen.add(word);
                    }
                    sb.setCharAt(i, cache);
                    // Decrease the i-th digit by 1.
                    sb.setCharAt(i, sb.charAt(i) == '0' ? '9' : (char) (sb.charAt(i) - 1));
                    word = sb.toString();
                    if (word.equals(target))
                        return ans;
                    if (!seen.contains(word)) {
                        q.offer(word);
                        seen.add(word);
                    }
                    sb.setCharAt(i, cache);
                }
            }
        }

        return -1;
    }

    // V4
    // https://leetcode.com/problems/open-the-lock/solutions/5057116/fasterlesserdetailed-explainationbfsstep-vsum/
//    public int openLock_4(String[] deadends, String target) {
//        Set<String> deadendSet = new HashSet<>(Arrays.asList(deadends));
//        if (deadendSet.contains("0000")) {
//            return -1;
//        }
//
//        Queue<Pair<String, Integer>> queue = new LinkedList<>();
//        queue.offer(new Pair<>("0000", 0));
//        Set<String> visited = new HashSet<>();
//        visited.add("0000");
//
//        while (!queue.isEmpty()) {
//            Pair<String, Integer> current = queue.poll();
//            String currentCombination = current.getKey();
//            int moves = current.getValue();
//
//            if (currentCombination.equals(target)) {
//                return moves;
//            }
//
//            for (int i = 0; i < 4; i++) {
//                for (int delta : new int[] { -1, 1 }) {
//                    int newDigit = (currentCombination.charAt(i) - '0' + delta + 10) % 10;
//                    String newCombination = currentCombination.substring(0, i) +
//                            newDigit +
//                            currentCombination.substring(i + 1);
//
//                    if (!visited.contains(newCombination) && !deadendSet.contains(newCombination)) {
//                        visited.add(newCombination);
//                        queue.offer(new Pair<>(newCombination, moves + 1));
//                    }
//                }
//            }
//        }
//
//        return -1; // Target is not reachable
//    }



}
