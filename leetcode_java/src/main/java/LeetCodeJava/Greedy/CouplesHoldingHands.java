package LeetCodeJava.Greedy;

// https://leetcode.com/problems/couples-holding-hands/description/

import java.util.HashMap;
import java.util.Map;

/**
 *  765. Couples Holding Hands
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are n couples sitting in 2n seats arranged in a row and want to hold hands.
 *
 * The people and seats are represented by an integer array row where row[i] is the ID of the person sitting in the ith seat. The couples are numbered in order, the first couple being (0, 1), the second couple being (2, 3), and so on with the last couple being (2n - 2, 2n - 1).
 *
 * Return the minimum number of swaps so that every couple is sitting side by side. A swap consists of choosing any two people, then they stand up and switch seats.
 *
 *
 *
 * Example 1:
 *
 * Input: row = [0,2,1,3]
 * Output: 1
 * Explanation: We only need to swap the second (row[1]) and third (row[2]) person.
 * Example 2:
 *
 * Input: row = [3,2,0,1]
 * Output: 0
 * Explanation: All couples are already seated side by side.
 *
 *
 * Constraints:
 *
 * 2n == row.length
 * 2 <= n <= 30
 * 0 <= row[i] < 2n
 * All the elements of row are unique.
 *
 */
public class CouplesHoldingHands {

    // V0
//    public int minSwapsCouples(int[] row) {
//
//    }

    // V1-1
    // IDEA: HASHMAP + IDX (GPT)
    public int minSwapsCouples_1_1(int[] row) {
        int n = row.length;

        /** NOTE !!!
         *
         *  map: { val : idx }
         *
         */
        // map: person -> index
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(row[i], i);
        }

        int swaps = 0;

        /** NOTE !!!
         *
         *  jump 2 idx in every loop
         */
        for (int i = 0; i < n; i += 2) {
            int first = row[i];

            // compute partner without bit trick
            /** NOTE !!!
             *
             *  If x is even → partner = x + 1
             *  If x is odd → partner = x - 1
             *
             */
            int partner = (first % 2 == 0) ? first + 1 : first - 1;

            /** NOTE !!!
             *
             *  if next val is `partner`,
             *  we neglect this loop
             *
             */
            if (row[i + 1] == partner){
                continue;
            }


            /** NOTE !!!
             *
             *  get partner `idx` via map
             */
            int partnerIdx = map.get(partner);

            /** NOTE !!!
             *
             *   we swap
             *     `next element` (row[i + 1])
             *      with
             *     `partner element`
             */
            // swap
            int temp = row[i + 1];
            row[i + 1] = partner;
            row[partnerIdx] = temp;

            /** NOTE !!!
             *
             *  don't forget to update the map
             */
            // update map
            map.put(temp, partnerIdx);
            map.put(partner, i + 1);

            swaps++;
        }

        return swaps;
    }


    // V1-2
    // IDEA: HASHMAP + IDX (GEMINI)
    public int minSwapsCouples_1_2(int[] row) {
        int n = row.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(row[i], i);
        }

        int swaps = 0;
        for (int i = 0; i < n; i += 2) {
            int firstPerson = row[i];

            // Standard math instead of bitwise XOR
            int partner;
            if (firstPerson % 2 == 0) {
                partner = firstPerson + 1;
            } else {
                partner = firstPerson - 1;
            }

            if (row[i + 1] != partner) {
                swaps++;

                int currentNeighbor = row[i + 1];
                int partnerIdx = map.get(partner);

                // Swap in array
                row[partnerIdx] = currentNeighbor;
                row[i + 1] = partner;

                // Update the map for the neighbor we just moved
                map.put(currentNeighbor, partnerIdx);
            }
        }
        return swaps;
    }



    // V2-1
    // IDEA: Greedy + Index Map
    // https://leetcode.com/problems/couples-holding-hands/solutions/7489338/beats-100-2-solutions-greedy-dsu-union-f-rsml/
    public int minSwapsCouples_2_1(int[] row) {
        int swaps = 0;
        Map<Integer, Integer> index = new HashMap<>();

        // Store current index of each person
        for (int i = 0; i < row.length; i++) {
            index.put(row[i], i);
        }

        // Process seats in pairs
        for (int i = 0; i < row.length; i += 2) {
            int first = row[i];
            int partner = first ^ 1; // correct partner

            // If already seated correctly, continue
            if (row[i + 1] == partner)
                continue;

            // Otherwise swap partner into correct place
            int partnerIndex = index.get(partner);
            int second = row[i + 1];

            row[i + 1] = partner;
            row[partnerIndex] = second;

            // Update indices after swap
            index.put(partner, i + 1);
            index.put(second, partnerIndex);

            swaps++;
        }

        return swaps;
    }



    // V2-2
    // IDEA: UNION FIND (DSU)
    // https://leetcode.com/problems/couples-holding-hands/solutions/7489338/beats-100-2-solutions-greedy-dsu-union-f-rsml/
    /**
     * Disjoint Set Union (Union-Find) data structure
     * Used to group couples that must be together.
     */
    class DSU {
        int[] parent; // parent[i] = parent of node i
        int components; // number of connected components

        /**
         * Initialize DSU with n components
         * Initially, every couple is its own parent
         */
        DSU(int n) {
            parent = new int[n];
            components = n;

            // Each couple is initially in its own set
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        /**
         * Find the representative (root) of a set
         * Uses path compression for optimization
         */
        int find(int x) {
            // If x is not the root, recursively find root
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }

        /**
         * Union two sets if they are different
         * Decreases component count when merge happens
         */
        void union(int a, int b) {
            int pa = find(a); // root of first set
            int pb = find(b); // root of second set

            // Only merge if they are in different sets
            if (pa != pb) {
                parent[pb] = pa; // merge sets
                components--; // one less connected component
            }
        }
    }

    /**
     * Main function to calculate minimum swaps
     */
    public int minSwapsCouples_2_2(int[] row) {

        // Number of couples
        int n = row.length / 2;

        // Initialize DSU for all couples
        DSU dsu = new DSU(n);

        /**
         * Traverse seats pairwise.
         * Each adjacent pair forms a relationship between two couples.
         */
        for (int i = 0; i < row.length; i += 2) {

            // Find couple number for both people
            int c1 = row[i] / 2; // couple of first person
            int c2 = row[i + 1] / 2; // couple of second person

            // Union the two couples
            dsu.union(c1, c2);
        }

        /**
         * If there are k connected components,
         * we need (total couples - k) swaps.
         */
        return n - dsu.components;
    }



    // V3
    // https://leetcode.com/problems/couples-holding-hands/solutions/6966129/easy-explanation-using-arrays-cyclic-sor-xrkb/
    public int minSwapsCouples_3(int[] row) {
        int swaps = 0;
        int n = row.length;
        int[] position = new int[n];

        // Map each person to their seat index
        for (int i = 0; i < n; i++) {
            position[row[i]] = i;
        }

        // Process pairs of seats
        for (int i = 0; i < n; i += 2) {
            int first = row[i];
            int second = row[i + 1];
            int expectedSecond = first ^ 1; // Partner of first person

            if (second != expectedSecond) {
                swaps++;
                int partnerIndex = position[expectedSecond];

                // Swap second person with the correct partner
                swap(row, i + 1, partnerIndex);

                // Update position map after swap
                position[second] = partnerIndex;
                position[expectedSecond] = i + 1;
            }
        }
        return swaps;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    // V4
    // https://leetcode.com/problems/couples-holding-hands/solutions/7513220/minimum-swaps-to-seat-couples-together-g-6fsr/
    public int minSwapsCouples_4(int[] row) {
        return answer(row);
    }

    public int answer(int[] row) {
        int n = row.length;
        Map<Integer, Integer> map = new HashMap<>();
        //key : person , value : position(index)

        for (int i = 0; i < n; i++) {
            map.put(row[i], i);
        }

        int swap = 0;

        for (int i = 0; i < n - 1; i += 2) {
            int person = row[i];
            int partner = row[i] ^ 1;

            if (row[i + 1] == partner) {
                continue;
            }

            int partner_idx = map.get(partner);

            //update position in map

            map.put(partner, i + 1);
            map.put(row[i + 1], partner_idx);

            //swap persons
            int temp = row[partner_idx];
            row[partner_idx] = row[i + 1];
            row[i + 1] = temp;
            swap++;
        }

        return swap;
    }





}
