package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/koko-eating-bananas/

import java.util.Arrays;

public class KokoEatingBananas {

    // V0
    // IDEA : BINARY SEARCH (close boundary)
    public int minEatingSpeed(int[] piles, int h) {

        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

        int l = 1; //Arrays.stream(piles).min().getAsInt();
        int r = Arrays.stream(piles).max().getAsInt();

        while (r >= l){
            int mid = (l + r) / 2;
            int _hour = getCompleteTime(piles, mid);
            if (_hour <= h){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        return l;
    }

    // V0
    // IDEA : BINARY SEARCH (open boundary)
    public int minEatingSpeed_2(int[] piles, int h) {

        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

        // https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
        int l = 1; //Arrays.stream(piles).min().getAsInt();
        int r = Arrays.stream(piles).max().getAsInt();

        while (r > l){
            int mid = (l + r) / 2;
            int _hour = getCompleteTime(piles, mid);
            //System.out.println("l = " + l + " r = " + r + " mid = " + mid + " _hour = " + _hour);
            if (_hour <= h){
                r = mid;
            }else{
                l = mid + 1;
            }
         }

        return r;
    }

    private int getCompleteTime(int[] piles, int speed){

        int _hour  = 0;
        for (int pile : piles) {
            _hour += Math.ceil((double) pile / speed);
        }

        return _hour;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/koko-eating-bananas/editorial/
    public int minEatingSpeed_3(int[] piles, int h) {
        // Start at an eating speed of 1.
        int speed = 1;

        while (true) {
            // hourSpent stands for the total hour Koko spends with
            // the given eating speed.
            int hourSpent = 0;

            // Iterate over the piles and calculate hourSpent.
            // We increase the hourSpent by ceil(pile / speed)
            for (int pile : piles) {
                hourSpent += Math.ceil((double) pile / speed);
                if (hourSpent > h) {
                    break;
                }
            }

            // Check if Koko can finish all the piles within h hours,
            // If so, return speed. Otherwise, let speed increment by
            // 1 and repeat the previous iteration.
            if (hourSpent <= h) {
                return speed;
            } else {
                speed += 1;
            }
        }
    }

    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/koko-eating-bananas/editorial/
    public int minEatingSpeed_4(int[] piles, int h) {
        // Initialize the left and right boundaries
        int left = 1, right = 1;
        for (int pile : piles) {
            right = Math.max(right, pile);
        }

        while (left < right) {
            // Get the middle index between left and right boundary indexes.
            // hourSpent stands for the total hour Koko spends.
            int middle = (left + right) / 2;
            int hourSpent = 0;

            // Iterate over the piles and calculate hourSpent.
            // We increase the hourSpent by ceil(pile / middle)
            for (int pile : piles) {
                hourSpent += Math.ceil((double) pile / middle);
            }

            // Check if middle is a workable speed, and cut the search space by half.
            if (hourSpent <= h) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }

        // Once the left and right boundaries coincide, we find the target value,
        // that is, the minimum workable eating speed.
        return right;
    }

}
