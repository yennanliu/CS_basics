package LeetCodeJava.Array;

// https://leetcode.com/problems/beautiful-array/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 932. Beautiful Array
 * Medium
 * Topics
 * Companies
 * An array nums of length n is beautiful if:
 *
 * nums is a permutation of the integers in the range [1, n].
 * For every 0 <= i < j < n, there is no index k with i < k < j where 2 * nums[k] == nums[i] + nums[j].
 * Given the integer n, return any beautiful array nums of length n. There will be at least one valid answer for the given n.
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: [2,1,4,3]
 * Example 2:
 *
 * Input: n = 5
 * Output: [3,1,2,5,4]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 *
 */
public class BeautifulArray {

    // V0
//    public int[] beautifulArray(int n) {
//
//    }

    // V1
    // IDEA : Divide and Conquer
    // https://leetcode.com/problems/beautiful-array/editorial/
    Map<Integer, int[]> memo;
    public int[] beautifulArray_1(int N) {
        memo = new HashMap();
        return f(N);
    }

    public int[] f(int N) {
        if (memo.containsKey(N))
            return memo.get(N);

        int[] ans = new int[N];
        if (N == 1) {
            ans[0] = 1;
        } else {
            int t = 0;
            for (int x: f((N+1)/2))  // odds
                ans[t++] = 2*x - 1;
            for (int x: f(N/2))  // evens
                ans[t++] = 2*x;
        }
        memo.put(N, ans);
        return ans;
    }


    // V2
    // IDEA : Divide and Conquer
    // https://leetcode.com/problems/beautiful-array/solutions/1373064/java-clear-thinking-process-divide-and-conquer/
    public int[] beautifulArray(int n) {
        int[] ans = new int[n];
        for(int i = 0; i  < n; i++){
            ans[i] = i+1;
        }
        recursion(ans, 0, n-1);
        return ans;
    }

    public void recursion(int[] arr, int left, int right){
        if(left >= right)
            return;
        ArrayList<Integer> l = new ArrayList<>();
        ArrayList<Integer> r = new ArrayList<>();

        boolean alt = true;// Not worry about whether the factor of the interval is even or odd too much, they can be grouped by
        // just picking one and skip one

        for(int i = left; i <= right; i++){ // picking the elements and put them into the two groups
            if(alt)
                l.add(arr[i]);
            else
                r.add(arr[i]);
            alt = !alt;
        }

        for(int i = left; i <= right; i++){ // merging them into the final array
            if(!l.isEmpty())
                arr[i] = l.remove(0);
            else
                arr[i] = r.remove(0);
        }
        recursion(arr, left, (right+left)/2);
        recursion(arr, (left+right)/2+1, right);
    }

    // V3-1
    // https://leetcode.com/problems/beautiful-array/solutions/1368607/java-0-ms-100-00-fast-explained-2-solutions/
    public int[] beautifulArray_3_1(int n) {
        int[] answer = new int[n];
        if(n == 1) {
            answer[0] = 1;
            return answer;
        }
        int[] right =beautifulArray(n/2);
        int[] left = beautifulArray((n+1)/2);

        for(int i=left.length; i<n; i++) {      //This loop adds all even elements at end
            answer[i] = right[i-left.length] * 2;
        }
        for(int i=0; i<left.length; i++) {      //This loop adds all odd elements at start
            answer[i] = left[i] * 2 - 1;
        }
        return answer;
    }

    // V3-2
    // https://leetcode.com/problems/beautiful-array/solutions/1368607/java-0-ms-100-00-fast-explained-2-solutions/
    public int[] beautifulArray_3_2(int n) {
        ArrayList<Integer> answer=new ArrayList<>();
        answer.add(1);
        while(answer.size()<n){
            ArrayList<Integer> temp=new ArrayList<>();
            for(int element:answer)
                if(2*element-1<=n)
                    temp.add(element*2-1);

            for(int element:answer)
                if(2*element<=n)
                    temp.add(element*2);

            //in different loops because we want to maintain order
            answer=temp;
        }
        return answer.stream().mapToInt(i -> i).toArray();
    }

}
