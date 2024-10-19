package LeetCodeJava.Design;

// https://leetcode.com/problems/snapshot-array/description/

import java.util.*;

/**
 * 1146. Snapshot Array
 * Medium
 * Topics
 * Companies
 * Hint
 * Implement a SnapshotArray that supports the following interface:
 *
 * SnapshotArray(int length) initializes an array-like data structure with the given length. Initially, each element equals 0.
 * void set(index, val) sets the element at the given index to be equal to val.
 * int snap() takes a snapshot of the array and returns the snap_id: the total number of times we called snap() minus 1.
 * int get(index, snap_id) returns the value at the given index, at the time we took the snapshot with the given snap_id
 *
 *
 * Example 1:
 *
 * Input: ["SnapshotArray","set","snap","set","get"]
 * [[3],[0,5],[],[0,6],[0,0]]
 * Output: [null,null,0,null,5]
 * Explanation:
 * SnapshotArray snapshotArr = new SnapshotArray(3); // set the length to be 3
 * snapshotArr.set(0,5);  // Set array[0] = 5
 * snapshotArr.snap();  // Take a snapshot, return snap_id = 0
 * snapshotArr.set(0,6);
 * snapshotArr.get(0,0);  // Get the value of array[0] with snap_id = 0, return 5
 *
 *
 * Constraints:
 *
 * 1 <= length <= 5 * 104
 * 0 <= index < length
 * 0 <= val <= 109
 * 0 <= snap_id < (the total number of times we call snap())
 * At most 5 * 104 calls will be made to set, snap, and get.
 *
 */
public class SnapshotArray {

    /**
     * Your SnapshotArray object will be instantiated and called as such:
     * SnapshotArray obj = new SnapshotArray(length);
     * obj.set(index,val);
     * int param_2 = obj.snap();
     * int param_3 = obj.get(index,snap_id);
     */
    // V0
    // (TLE) : TODO : optimize
//    class SnapshotArray_ {
//
//        Integer[] elements;
//        Map<Integer, Integer[]> snapshotMap;
//        Integer snapshotCount;
//
//        public SnapshotArray_(int length) {
//            this.elements = new Integer[length];
//            this.snapshotMap = new HashMap<>();
//            this.snapshotCount = 0;
//            // Store the initial snapshot (snapshot 0)
//            this.snapshotMap.put(this.snapshotCount, this.elements.clone());
//        }
//
//        public void set(int index, int val) {
//            // Set value in the current snapshot (current version of elements)
//            this.elements[index] = val;
//        }
//
//        public int snap() {
//            // Take a snapshot of the current elements array by creating a new copy
//            snapshotMap.put(snapshotCount, elements.clone());
//            // Increment snapshotCount to prepare for the next snapshot
//            return snapshotCount++;
//        }
//
//        public int get(int index, int snap_id) {
//            // Retrieve the value from the snapshot with the given snap_id
//            return snapshotMap.get(snap_id)[index];
//        }
//    }

    // V0_1
    // IDEA : (fix by GPT)
    /**
     * Key Optimizations
     *
     * 	1.	Sparse Storage:
     * 	    •Instead of copying the whole array for each snapshot,
     * 	    the TreeMap for each index only stores the values that
     * 	    have been set at different snapshot versions.
     * 	    This avoids unnecessary duplication of data.
     *
     * 	2.	Efficient Retrieval:
     * 	    •Using TreeMap.floorEntry() allows us to efficiently
     * 	    retrieve the value of an index at the given snapshot,
     * 	    or the most recent value before the snapshot.
     *
     * 	3.	Memory Efficiency:
     * 	    •The memory usage is optimized because we store
     * 	    only the changes at each snapshot. If no change
     * 	    occurs for an element, we do not store multiple
     * 	    copies of the same value.
     *
     * -> This approach ensures that both set() and get() operations
     * remain efficient, with logarithmic time complexity due to
     * the use of TreeMap, while significantly reducing memory
     * usage compared to the original approach.
     *
     */
    class SnapshotArray_0_1 {

        private int snapId;
        private Map<Integer, TreeMap<Integer, Integer>> snapshots;

        public SnapshotArray_0_1(int length) {
            this.snapId = 0;
            this.snapshots = new HashMap<>();
            for (int i = 0; i < length; i++) {
                snapshots.put(i, new TreeMap<>());
                snapshots.get(i).put(0, 0);  // Initially, every element is 0 at snap_id 0
            }
        }

        public void set(int index, int val) {
            snapshots.get(index).put(snapId, val);
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            // Get the greatest key less than or equal to snap_id
            return snapshots.get(index).floorEntry(snap_id).getValue();
        }
    }

    // V0_2
    // IDEA : HASHMAP + LIST (fixed by gpt) + binary search
    class SnapshotArray_0_2 {

        private int snapId;
        private Map<Integer, List<int[]>> snapshots;

        public SnapshotArray_0_2(int length) {
            this.snapId = 0;
            this.snapshots = new HashMap<>();
            // Initialize each index with a snapshot at snapId 0, where the value is 0
            for (int i = 0; i < length; i++) {
                snapshots.put(i, new ArrayList<>());
                snapshots.get(i).add(new int[]{0, 0});  // {snapId, value}
            }
        }

        public void set(int index, int val) {
            // Store the new value along with the current snapId
            List<int[]> snapshotList = snapshots.get(index);
            // If the last snapshot has the same snapId, just update the value
            if (!snapshotList.isEmpty() && snapshotList.get(snapshotList.size() - 1)[0] == snapId) {
                snapshotList.get(snapshotList.size() - 1)[1] = val;
            } else {
                snapshotList.add(new int[]{snapId, val});
            }
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            List<int[]> snapshotList = snapshots.get(index);

            // Perform a binary search to find the largest snapId <= snap_id
            int low = 0, high = snapshotList.size() - 1;
            while (low < high) {
                int mid = (low + high + 1) / 2;
                if (snapshotList.get(mid)[0] <= snap_id) {
                    low = mid;  // Move right if the current snapId is valid
                } else {
                    high = mid - 1;  // Move left if the current snapId is too large
                }
            }

            return snapshotList.get(low)[1];
        }
    }

    // V1
    // IDEA : Binary Search
    // https://leetcode.com/problems/snapshot-array/editorial/
    class SnapshotArray_1_1 {
        int snapId = 0;
        TreeMap<Integer, Integer>[] historyRecords;

        public SnapshotArray_1_1(int length) {
            historyRecords = new TreeMap[length];
            for (int i = 0; i < length; i++) {
                historyRecords[i] = new TreeMap<Integer, Integer>();
                historyRecords[i].put(0, 0);
            }
        }

        public void set(int index, int val) {
            historyRecords[index].put(snapId, val);
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snapId) {
            return historyRecords[index].floorEntry(snapId).getValue();
        }
    }

    // V2
    // IDEA : HASHMAP
    // https://leetcode.com/problems/snapshot-array/solutions/350574/java-python-3-3-codes-w-analysis-store-difference-by-hashmap-and-treemap-respectively/
    class SnapshotArray_2_1 {

        private List<Map<Integer, Integer>> shot;
        private Map<Integer, Integer> diff;

        public SnapshotArray_2_1(int length) {
            shot = new ArrayList<>(length);
            diff = new HashMap<>(length);
        }

        public void set(int index, int val) {
            diff.put(index, val);
        }

        public int snap() {
            shot.add(diff);
            diff = new HashMap<>();
            return shot.size() - 1;
        }

        public int get(int index, int snap_id) {
            for (int i = snap_id; i >= 0; --i)
                if (shot.get(i).containsKey(index))
                    return shot.get(i).get(index);
            return 0;
        }
    }

    // V2-2
    // https://leetcode.com/problems/snapshot-array/solutions/350574/java-python-3-3-codes-w-analysis-store-difference-by-hashmap-and-treemap-respectively/
    // IDEA : BINARY SEARCH
    class SnapshotArray_2_2 {
        private int snapId;
        private List<List<int[]>> shot;

        public SnapshotArray_2_2(int length) {
            this.snapId = 0;
            this.shot = new ArrayList<>();
            // Initialize the list with [-1, 0] for each index
            for (int i = 0; i < length; i++) {
                List<int[]> snapshotList = new ArrayList<>();
                snapshotList.add(new int[]{-1, 0}); // Add [-1, 0] as the initial state
                this.shot.add(snapshotList);
            }
        }

        public void set(int index, int val) {
            List<int[]> a = this.shot.get(index);
            if (a.get(a.size() - 1)[0] == this.snapId) {
                a.get(a.size() - 1)[1] = val;  // Update if snapId matches the last entry
            } else {
                a.add(new int[]{this.snapId, val});  // Otherwise add new entry
            }
        }

        public int snap() {
            this.snapId++;
            return this.snapId - 1; // Return the current snapId, then increment
        }

        public int get(int index, int snapId) {
            List<int[]> a = this.shot.get(index);
            int low = 0, high = a.size() - 1;

            // Binary search to find the correct snapshot using snapId
            while (low < high) {
                int mid = (low + high + 1) / 2;
                if (a.get(mid)[0] <= snapId) {
                    low = mid;
                } else {
                    high = mid - 1;
                }
            }

            return a.get(low)[1];  // Return the value for the found snapshot
        }
    }
    
}
