package LeetCodeJava.Heap;

// https://leetcode.com/problems/last-stone-weight/

import java.util.*;

public class LastStoneWeight {

    // V0
    // IDEA : PQ (MAX HEAP)
    public int lastStoneWeight(int[] stones) {

        if (stones.length == 0){
            return 0;
        }

        if (stones.length == 1){
            return stones[0];
        }

        // init a Max pq
        /** NOTE !!!
         *
         *  init MAX PQ via Comparator.reverseOrder()
         */
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int x : stones){
            pq.add(x);
        }

       // System.out.println("pq = " + pq);

        // pop 2 biggest elements
        while (pq.size() >= 2){
            int first = pq.poll();
            int second = pq.poll();
            int tmp = 0;
            if (first > second){
                tmp = first - second;
            }
            pq.add(tmp);
        }

        return pq.size() == 0 ? 0 : pq.peek();
    }

    // V0'
    // IDEA : MAX HEAP (PRIORITY QUEUE)
    public int lastStoneWeight_0_1(int[] stones) {

        if (stones.length == 0 || stones.equals(null)){
            return 0;
        }

        if (stones.length == 1){
            return stones[0];
        }

        /** NOTE !!! we can define Max-heap via this
         *
         *  PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder());
         */
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : stones){
            pq.add(-1 * x);
        }

        while (!pq.isEmpty() && pq.size() > 1){
            int _first = pq.remove() * -1;
            int _second = pq.remove() * -1;
            int diff = Math.abs(_first - _second);
            //System.out.println(">>> _first = " + _first + " second = " + _second + " diff = " + diff);
            pq.add(-1 * diff);
        }

        if (pq.size() > 1){
            return 0;
        }
        int ans = -1 * pq.remove();
        return ans;
    }

    // V1
    // IDEA : Array-Based Simulation
    // https://leetcode.com/problems/last-stone-weight/editorial/
    private int removeLargest(List<Integer> stones) {
        int indexOfLargest = stones.indexOf(Collections.max(stones));
        int result = stones.get(indexOfLargest);
        stones.set(indexOfLargest, stones.get(stones.size() - 1));
        stones.remove(stones.size() - 1);
        return result;
    }

    public int lastStoneWeight_2(int[] stones) {
        List<Integer> stoneList = new ArrayList<>();
        for (int weight : stones) {
            stoneList.add(weight);
        }

        while (stoneList.size() > 1) {
            int stone1 = removeLargest(stoneList);
            int stone2 = removeLargest(stoneList);
            if (stone1 != stone2) {
                stoneList.add(stone1 - stone2);
            }
        }

        return !stoneList.isEmpty() ? stoneList.remove(0) : 0;
    }

    // V2
    // IDEA : Sorted Array-Based Simulation
    // https://leetcode.com/problems/last-stone-weight/editorial/
    public int lastStoneWeight_3(int[] stones) {
        List<Integer> stoneList = new ArrayList<>();
        for (int stone : stones) {
            stoneList.add(stone);
        }
        Collections.sort(stoneList);

        while (stoneList.size() > 1) {
            int stone1 = stoneList.remove(stoneList.size() - 1);
            int stone2 = stoneList.remove(stoneList.size() - 1);

            if (stone1 != stone2) {
                int newStone = stone1 - stone2;
                int index = Collections.binarySearch(stoneList, newStone);
                if (index < 0) {
                    stoneList.add(-index - 1, newStone);
                } else {
                    stoneList.add(index, newStone);
                }
            }
        }

        return !stoneList.isEmpty() ? stoneList.remove(0) : 0;
    }

    // V3
    // IDEA : Heap-Based Simulation
    // https://leetcode.com/problems/last-stone-weight/editorial/
    public int lastStoneWeight_4(int[] stones) {

        /** NOTE !!! we can define Max-heap via below */
        // Insert all the stones into a Max-Heap.
        PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder());
        for (int stone: stones) {
            heap.add(stone);
        }

        // While there is more than one stone left, we need to remove the two largest
        // and smash them together. If there is a resulting stone, we need to put into
        // the heap.
        while (heap.size() > 1) {
            int stone1 = heap.remove();
            int stone2 = heap.remove();
            if (stone1 != stone2) {
                heap.add(stone1 - stone2);
            }
        }

        // Check whether or not there is a stone left to return.
        return heap.isEmpty() ? 0 : heap.remove();
    }

}
