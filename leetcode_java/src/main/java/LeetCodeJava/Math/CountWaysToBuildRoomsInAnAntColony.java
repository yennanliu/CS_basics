package LeetCodeJava.Math;

// https://leetcode.com/problems/count-ways-to-build-rooms-in-an-ant-colony/description/

import java.util.*;

/**
 * 1916. Count Ways to Build Rooms in an Ant Colony
 * Hard
 *
 * You are an ant tasked with adding n new rooms numbered 0 to n-1 to your colony. You are given the expansion plan as a 0-indexed integer array of length n, prevRoom, where prevRoom[i] indicates that you must build room prevRoom[i] before building room i, and these two rooms must be connected directly. Room 0 is already built, so prevRoom[0] = -1. The expansion plan is given such that once all the rooms are built, every room will be reachable from room 0.
 *
 * You can only build one room at a time, and you can travel freely between rooms you have already built only if they are connected. You can choose to build any room as long as its previous room is already built.
 *
 * Return the number of different orders you can build all the rooms in. Since the answer may be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: prevRoom = [-1,0,1]
 * Output: 1
 * Explanation: There is only one way to build the additional rooms: 0 -> 1 -> 2
 *
 * Example 2:
 *
 * Input: prevRoom = [-1,0,0,1,2]
 * Output: 6
 * Explanation: The 6 ways are:
 * 0 -> 1 -> 3 -> 2 -> 4
 * 0 -> 2 -> 4 -> 1 -> 3
 * 0 -> 1 -> 2 -> 3 -> 4
 * 0 -> 1 -> 2 -> 4 -> 3
 * 0 -> 2 -> 1 -> 3 -> 4
 * 0 -> 2 -> 1 -> 4 -> 3
 *
 *
 * Constraints:
 *
 * n == prevRoom.length
 * 2 <= n <= 10^5
 * prevRoom[0] == -1
 * 0 <= prevRoom[i] < n for all 1 <= i < n
 * Every room is reachable from room 0.
 *
 */
public class CountWaysToBuildRoomsInAnAntColony {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: HOOK LENGTH FORMULA FOR TREES -> n! / PRODUCT(subtree size of every node)
    //  a build order is a topological order of the tree rooted at 0; node v must come
    //  first within its own subtree, which a random order does with probability 1/size(v).
    /**
     * time = O(N + log MOD)
     * space = O(N)
     */
    public int waysToBuildRooms(int[] prevRoom) {
        int n = prevRoom.length;

        // children lists of the tree
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            children.get(prevRoom[i]).add(i);
        }

        // BFS from the root: a parent always appears before its children (iterative, no deep recursion)
        int[] order = new int[n];
        int tail = 0;
        order[tail++] = 0;
        for (int head = 0; head < tail; head++) {
            int room = order[head];
            for (int child : children.get(room)) {
                order[tail++] = child;
            }
        }

        // subtree sizes, accumulated from the leaves up (reverse BFS order)
        int[] subtreeSize = new int[n];
        Arrays.fill(subtreeSize, 1);
        for (int i = n - 1; i > 0; i--) {
            int room = order[i];
            subtreeSize[prevRoom[room]] += subtreeSize[room];
        }

        long numerator = 1;
        for (int i = 2; i <= n; i++) {
            numerator = numerator * i % MOD;
        }
        long denominator = 1;
        for (int size : subtreeSize) {
            denominator = denominator * size % MOD;
        }

        // division under a prime modulus = multiply by the modular inverse (Fermat)
        return (int) (numerator * modPow(denominator, MOD - 2) % MOD);
    }

    /** Computes base^exp mod MOD by binary exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
