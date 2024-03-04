package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/decode-ways/description/

public class DecodeWays {

    // V0
    // IDEA : DP
    // TODO : implement

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/decode-ways/solutions/4456554/character-state-machine-video-solution-java-c/
    public int numDecodings(String encodedString) {
        char s[] = encodedString.toCharArray();
        if(s[0] == '0')return 0;
        int sz = s.length;
        int noWaysAtIndx[] = new int[sz];
        noWaysAtIndx[0] = 1;
        for(int indx = 1; indx < sz; indx++){
            char currC = s[indx], prevC = s[indx-1];
            if(currC == '0' && !(prevC == '1' || prevC == '2')){
                return  0;
            }
            int onesDigit = currC - '0', tensDigit = prevC - '0';
            int number = tensDigit * 10 + onesDigit;
            if(number >= 10 && number <= 26){
                if(indx >= 2)
                    noWaysAtIndx[indx] += noWaysAtIndx[indx-2];
                else
                    noWaysAtIndx[indx] = 1;
            }
            if(number != 10 && number != 20){
                noWaysAtIndx[indx] += noWaysAtIndx[indx-1];
            }
        }
        return noWaysAtIndx[sz - 1];
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/decode-ways/solutions/4454539/decode-ways-java/
    public int numDecodings_1(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        if(n == 0 || s.charAt(0) == '0') return 0;
        dp[0] = 1; dp[1] = 1;

        for(int i = 2; i <= n; i++){
            // check for single-digit
            if(s.charAt(i - 1) >= '1' && s.charAt(i - 1) <= '9') dp[i] = dp[i - 1];

            // check for two-digits
            if(s.charAt(i - 2) == '1') dp[i] += dp[i - 2];
            else if(s.charAt(i - 2) == '2' && s.charAt(i - 1) >= '0' && s.charAt(i - 1) <= '6') dp[i] += dp[i - 2];
        }
        return dp[n];
    }



}
