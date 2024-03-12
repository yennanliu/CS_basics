package LeetCodeJava.Math;

// https://leetcode.com/problems/counting-bits/

public class CountingBits {

    // V0
    // IDEA : MATH
    public int[] countBits(int n) {
        /** NOTE !!! we init ans as below */
        int[] ans = new int[n+1];
        for (int x = 0; x < n+1; x++){
            // https://stackoverflow.com/questions/2406432/converting-an-int-to-a-binary-string-representation-in-java
            String binary = Integer.toBinaryString(x);
            ans[x] = countElement(binary, '1');
        }

        return ans;
    }

    // https://www.baeldung.com/java-count-chars
    private int countElement(String s, char x){
        int count = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == x){
                count += 1;
            }
        }
        return count;
    }

    // V1
    // https://leetcode.com/problems/counting-bits/solutions/3987279/java-solution-using-right-shift-operator/
    public int countOnes(int n) {
        int count = 0;
        while(n != 0) {
            count += (n&1);
            n = n>>1;
        }
        return count;
    }
    public int[] countBits_1(int n) {
        int ans[] = new int[n+1];
        ans[0] = 0;
        for(int i=1; i<ans.length; i++) {
            ans[i] = countOnes(i);
        }
        return ans;
    }

    // V2
    // IDEA : BIT OP
    // https://leetcode.com/problems/counting-bits/solutions/79539/three-line-java-solution/
    public int[] countBits_2(int num) {
        int[] f = new int[num + 1];
        for (int i=1; i<=num; i++) f[i] = f[i >> 1] + (i & 1);
        return f;
    }
    
}
