package LeetCodeJava.Greedy;

// https://leetcode.com/problems/non-overlapping-intervals/description/

import java.util.Arrays;
import java.util.Stack;
import java.util.Comparator;

public class NonOverlappingIntervals {

    // V0
    // IDEA : 2 POINTERS + sorting + intervals (modified by GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/non-overlapping-intervals.py
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        int cnt = 0;
        int[] last = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < last[1]) {
                cnt++;
            } else {
                last[1] = Math.max(intervals[i][1], last[1]);
            }
        }
        return cnt;
    }


    // V0'
    // TODO : implement it
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/non-overlapping-intervals.py
//    public int eraseOverlapIntervals(int[][] intervals) {
//        return 0;
//    }

    // V1
    // IDEA : STACK
    // https://leetcode.com/problems/non-overlapping-intervals/solutions/4683650/4-line-solution-easy-to-understand-java-stack/
    public int eraseOverlapIntervals_1(int[][] intervals) {
        Arrays.sort(intervals,(a, b)->a[1]-b[1]);
        Stack<int[]> stk = new Stack<>();
        int res=0;
        for(int[] i : intervals){
            if(!stk.isEmpty() && i[0]< stk.peek()[1]){
                res++;
            }else{
                stk.push(i);
            }
        }

        return res;
    }

    // V2
    // IDEA : SORT
    // https://leetcode.com/problems/non-overlapping-intervals/solutions/3786438/java-easy-solution-using-sorting-explained/
    public int eraseOverlapIntervals_2(int[][] intervals) {
        // Sort by ending time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int prev = 0, count  = 1;
        // if end is same, sort by start, bigger start in front
        for(int i = 0; i < intervals.length; i ++) {
            if(intervals[i][0] >= intervals[prev][1]) {
                prev = i;
                count ++;
            }
        }
        return intervals.length - count;
    }

    // V3
    // https://leetcode.com/problems/non-overlapping-intervals/submissions/1202459919/
    public int eraseOverlapIntervals_3(int[][] in) {
        Arrays.sort(in, (a,b)->a[1]-b[1]);
        int res=-1, p[]=in[0];
        for(int[] i: in)
            if(i[0]<p[1]) res++;
            else p=i;
        return res;
    }

}
