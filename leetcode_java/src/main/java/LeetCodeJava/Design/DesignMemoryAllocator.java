package LeetCodeJava.Design;

// https://leetcode.com/problems/design-memory-allocator/

/**
 *  2502. Design Memory Allocator
 *  Medium
 *
 *  You are given an integer n representing the size of a 0-indexed memory array. All
 *  memory units are initially free.
 *
 *  You have a memory allocator with the following functionalities:
 *   1. Allocate a block of size consecutive free memory units and assign it the id mID.
 *   2. Free all memory units with the given id mID.
 *
 *  Note that multiple blocks can be allocated to the same mID, and you should free all
 *  the memory units with mID even if they were allocated in different blocks.
 *
 *  Implement the Allocator class:
 *
 *   - Allocator(int n) Initializes an Allocator object with a memory array of size n.
 *   - int allocate(int size, int mID) Find the LEFTMOST block of size consecutive free
 *     memory units and allocate it with the id mID. Return the block's first index. If
 *     such a block does not exist, return -1.
 *   - int freeMemory(int mID) Free all memory units with the id mID. Return the number
 *     of memory units you have freed.
 *
 *  Example 1:
 *    Input
 *      ["Allocator","allocate","allocate","allocate","freeMemory","allocate","allocate",
 *       "allocate","freeMemory","allocate","freeMemory"]
 *      [[10],[1,1],[1,2],[1,3],[2],[3,4],[1,1],[1,1],[1],[10,2],[7]]
 *    Output
 *      [null,0,1,2,1,3,1,6,3,-1,0]
 *    Explanation
 *      allocate(1,1) -> 0, allocate(1,2) -> 1, allocate(1,3) -> 2  => [1,2,3,_,...]
 *      freeMemory(2) -> 1                                          => [1,_,3,_,...]
 *      allocate(3,4) -> 3, allocate(1,1) -> 1, allocate(1,1) -> 6
 *      freeMemory(1) -> 3
 *      allocate(10,2) -> -1 (no 10 consecutive free units)
 *      freeMemory(7)  -> 0  (nothing owns mID 7)
 *
 *  Constraints:
 *    1 <= n, size, mID <= 1000
 *    At most 1000 calls will be made to allocate and freeMemory.
 */
public class DesignMemoryAllocator {

    // V0
    // IDEA: DIRECT SIMULATION ON A PER-UNIT ARRAY
    //
    //   n <= 1000 and at most 1000 calls, so a plain per-unit array is enough
    //   (<= 10^6 unit visits overall) -- no interval tree needed.
    //
    //   mem[i] = mID owning unit i, or 0 when free (mID >= 1, so 0 is a safe sentinel).
    //
    //   - allocate: sweep left to right keeping a run length of free units; the moment
    //               the run reaches `size` we are at the LEFTMOST valid block, so stamp
    //               it and return its start index. the run counter resets to 0 on any
    //               occupied unit -- the block has to be CONSECUTIVE.
    //   - freeMemory: sweep and zero out every unit tagged with mID, counting them.
    //               blocks of the same mID may be scattered, so the whole array is scanned.
    /**
     * time = O(N) per call
     * space = O(N)
     */
    private final int[] mem;

    public DesignMemoryAllocator(int n) {
        this.mem = new int[n];
    }

    public int allocate(int size, int mID) {
        int run = 0;
        for (int i = 0; i < this.mem.length; i++) {
            if (this.mem[i] != 0) {
                run = 0;
                continue;
            }
            run++;
            if (run == size) {
                int start = i - size + 1;
                for (int j = start; j <= i; j++) {
                    this.mem[j] = mID;
                }
                return start;
            }
        }
        return -1;
    }

    public int freeMemory(int mID) {
        int res = 0;
        for (int i = 0; i < this.mem.length; i++) {
            if (this.mem[i] == mID) {
                this.mem[i] = 0;
                res++;
            }
        }
        return res;
    }
}
