package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/sum-of-two-integers/description/

public class SumOfTwoIntegers {

    // V0
    // IDEA: BIT OP (half adder: XOR gives the `sum without carry`, AND gives the carry)
    // LC 371 (NOTE !!! `+` and `-` are NOT allowed)
    /**
     *  NOTE !!!
     *
     *   a ^ b  -> sum of the bits where EXACTLY one of a, b has a 1 (no carry)
     *   a & b  -> the bits where BOTH a, b have a 1  -> those produce a carry
     *   (a & b) << 1 -> the carry, moved to its correct (next) position
     *
     *   -> so we keep doing `a = a ^ b`, `b = carry << 1`
     *      until there is NO carry left (b == 0), then a is the answer
     *
     *  example: a = 3 (011), b = 5 (101)
     *
     *   round 1: carry = 011 & 101 = 001 -> a = 011 ^ 101 = 110, b = 001 << 1 = 010
     *   round 2: carry = 110 & 010 = 010 -> a = 110 ^ 010 = 100, b = 010 << 1 = 100
     *   round 3: carry = 100 & 100 = 100 -> a = 100 ^ 100 = 000, b = 100 << 1 = 1000
     *   round 4: carry = 000 & 1000 = 0  -> a = 1000 (= 8), b = 0  -> return 8
     */
    /**
     * time = O(1)  (at most 32 rounds)
     * space = O(1)
     */
    public int getSum(int a, int b) {
        while (b != 0) {
            /** NOTE !!! get the carry BEFORE we overwrite a */
            int carry = a & b;
            // `sum without carry`
            a = a ^ b;
            // move the carry to the next bit position, and keep adding it in
            b = carry << 1;
        }
        return a;
    }

    // V1
    // IDEA : BIT OP
    // https://leetcode.com/problems/sum-of-two-integers/solutions/4623531/java-best-easy-solution-100-beats-0ms/
    public int getSum_1(int a, int b) {
        while(b!=0)
        {
            int carry=a&b;
            a=a ^ b;
            b=carry<<1;
        }
        return a;
    }

    // V2
    // https://leetcode.com/problems/sum-of-two-integers/solutions/84290/java-simple-easy-understand-solution-with-explanation/
    // Iterative
    public int getSum_2(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;

        while (b != 0) {
            int carry = a & b;
            a = a ^ b;
            b = carry << 1;
        }

        return a;
    }

    // V3
    // https://leetcode.com/problems/sum-of-two-integers/solutions/84290/java-simple-easy-understand-solution-with-explanation/
    // Iterative
    public int getSubtract_3(int a, int b) {
        while (b != 0) {
            int borrow = (~a) & b;
            a = a ^ b;
            b = borrow << 1;
        }

        return a;
    }

}
