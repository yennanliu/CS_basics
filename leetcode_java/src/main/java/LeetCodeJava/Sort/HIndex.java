package LeetCodeJava.Sort;

// https://leetcode.com/problems/h-index/description/

import java.util.Arrays;

/**
 *  274. H-Index
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of integers citations where citations[i] is the number of citations a researcher received for their ith paper, return the researcher's h-index.
 *
 * According to the definition of h-index on Wikipedia: The h-index is defined as the maximum value of h such that the given researcher has published at least h papers that have each been cited at least h times.
 *
 *
 *
 * Example 1:
 *
 * Input: citations = [3,0,6,1,5]
 * Output: 3
 * Explanation: [3,0,6,1,5] means the researcher has 5 papers in total and each of them had received 3, 0, 6, 1, 5 citations respectively.
 * Since the researcher has 3 papers with at least 3 citations each and the remaining two with no more than 3 citations each, their h-index is 3.
 * Example 2:
 *
 * Input: citations = [1,3,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == citations.length
 * 1 <= n <= 5000
 * 0 <= citations[i] <= 1000
 *
 */
public class HIndex {

    // V0
//    public int hIndex(int[] citations) {
//
//    }


    // V0-1
    // IDEA: SORT (gemini)
    /**
     *   IDEA:
     *
     *  This solution is based on the formal definition of the H-index: **"The largest number  such that at least  papers have  or more citations."**
     *
     * By sorting the array, we can check this condition very easily without needing any extra data structures.
     *
     * ---
     *
     * ### 1. The Core Idea:
     *
     * When an array is sorted in ascending order (small to big), look at any index :
     *
     * * The papers at index  all have **at least** the number of citations found at `citations[i]`.
     * * How many papers is that? The count is simply ****.
     *
     * ### 2. Step-by-Step Logic
     *
     * Let's use an example: `citations = [3, 0, 6, 1, 5]`
     *
     * #### Step A: Sort the array
     *
     * `citations = [0, 1, 3, 5, 6]`  ()
     *
     * #### Step B: Loop and check the condition
     *
     * We are looking for the first index where the number of citations is  the number of papers remaining.
     *
     * 1. **Index **: `citations[0] = 0`.
     * * Remaining papers () = 5.
     * * Is ? **No.** (We don't have 5 papers with  citations).
     *
     *
     * 2. **Index **: `citations[1] = 1`.
     * * Remaining papers () = 4.
     * * Is ? **No.** (We don't have 4 papers with  citations).
     *
     *
     * 3. **Index **: `citations[2] = 3`.
     * * Remaining papers () = **3**.
     * * Is ? **YES!** * This means there are **3** papers (the ones at index 2, 3, and 4) that all have **at least 3** citations.
     * * We return **3**.
     *
     *
     *
     * ---
     *
     * ### 3. Why it finds the "Largest"
     *
     * Because we iterate from the **left** (the smallest values), the first time we satisfy the condition `citations[i] >= (n - i)`, we have found the maximum possible .
     *
     * If we kept going to the right, the number of papers () would get smaller (e.g., , then ), so the  value would decrease. Since we want the **maximum** , we stop and return the very first valid -candidate we find.
     *
     * ### 4. Comparison to your original idea
     *
     * Your original idea tried to find a value **inside** the array that matched the count.
     *
     * * **Your code logic:** "Is the citation value  equal to the number of papers?"
     * * **Correct logic:** "Is the citation value  **at least** the number of papers?"
     *
     * The H-index doesn't have to be equal to a value in the array; it just has to be a threshold that  papers cross.
     *
     * ---
     *
     * ### 📈 Complexity
     *
     * * **Time Complexity**:  because of the `Arrays.sort()`. The for-loop itself is only .
     * * **Space Complexity**:  (or  depending on the sorting implementation), but we aren't using any HashMaps.
     *
     */
    public int hIndex_0_1(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;

        // We check each possible h-index from the perspective of the sorted array
        for (int i = 0; i < n; i++) {
            /** NOTE !!
             *
             *   The Core Idea: $n - i$
             *
             *   When an array is sorted in ascending order (small to big), look at any index $i$:
             *
             *    1.  The papers at index $i, i+1, i+2, \dots, n-1$ all have at least the number of citations found at citations[i]
             *
             *    2. How many papers is that? The count is simply $n - i$.
             *
             */
            // After sorting, at index i, there are (n - i) papers
            // that have at least citations[i] citations.
            int hCandidate = n - i;

            if (citations[i] >= hCandidate) {
                return hCandidate;
            }
        }

        return 0;
    }

    // V0-2
    // IDEA: Bucket SORT (gpt)
    public int hIndex_0_2(int[] citations) {
        int n = citations.length;
        int[] buckets = new int[n + 1];

        for (int c : citations) {
            if (c >= n) {
                buckets[n]++;
            } else {
                buckets[c]++;
            }
        }

        int count = 0;
        for (int i = n; i >= 0; i--) {
            count += buckets[i];
            if (count >= i) {
                return i;
            }
        }

        return 0;
    }



    // V1-1
    // https://leetcode.com/problems/h-index/solutions/6743916/video-2-solutions-counting-and-sorting-b-nck3/
    public int hIndex_1_1(int[] citations) {
        int papers = citations.length;
        int[] citationBuckets = new int[papers + 1];

        for (int citation : citations) {
            citationBuckets[Math.min(citation, papers)]++;
        }

        int cumulativePapers = 0;
        for (int hIndex = papers; hIndex >= 0; hIndex--) {
            cumulativePapers += citationBuckets[hIndex];
            if (cumulativePapers >= hIndex) {
                return hIndex;
            }
        }
        return 0;
    }


    // V1-2
    // https://leetcode.com/problems/h-index/solutions/6743916/video-2-solutions-counting-and-sorting-b-nck3/
    public int hIndex_1_2(int[] citations) {
        int n = citations.length;
        Arrays.sort(citations);

        for (int i = 0; i < n; i++) {
            if (citations[i] >= n - i) {
                return n - i;
            }
        }

        return 0;
    }


    // V2
    // https://leetcode.com/problems/h-index/solutions/70768/java-bucket-sort-on-solution-with-detail-pjp1/
    public int hIndex_2(int[] citations) {
        int n = citations.length;
        int[] buckets = new int[n + 1];
        for (int c : citations) {
            if (c >= n) {
                buckets[n]++;
            } else {
                buckets[c]++;
            }
        }
        int count = 0;
        for (int i = n; i >= 0; i--) {
            count += buckets[i];
            if (count >= i) {
                return i;
            }
        }
        return 0;
    }




}
