package LeetCodeJava.String;

// https://leetcode.com/problems/count-and-say/description/

public class CountAndSay {

     // V0
//    public String countAndSay(int n) {
//
//        if (n==1){
//            return "1";
//        }
//
//        String res = "1";
//        for (int i = 1; i < n-1; i++){
//            String curRes = help(i, res);
//            res += curRes;
//        }
//        return res;
//    }
//
//    private String help(int n, String cur){
//
//        int cnt = 1;
//
//        if (n<=0){
//            return null;
//        }
//
//        if (n==1){
//            return "1";
//        }
//        for (int i = 0; i < cur.length()-1; i++){
//            System.out.println("i = " + i + " cur = " + cur);
//            String _cur = String.valueOf(cur.charAt(i));
//            String _next = String.valueOf(cur.charAt(i+1));
//            if (_cur.equals(_next)){
//                cnt += 1;
//            }else{
//                _cur += (String.valueOf(cnt));
//                cur += (_cur);
//                cnt = 1;
//            }
//        }
//        return cur;
//    }


    // V1
    // IDEA : ITERATION
    // https://leetcode.com/problems/count-and-say/solutions/3409791/easy-code-java-solution/
    public String countAndSay_1(int n) {
        if(n == 1){
            return "1";
        }
        String st = "11";
        int j = 0;
        for(int i = 0;i<n-2;i++){
            int count = 1;
            String s = "";
            for( j = 0;j<st.length()-1;j++){
                if(st.charAt(j) == st.charAt(j+1)){
                    count++;
                }
                else{
                    s += Integer.toString(count);
                    s += st.charAt(j);
                    count = 1;
                }
            }
            s += Integer.toString(count);
            s += st.charAt(j);
            st = s;
        }
        return st;
    }

    // V2
    // IDEA : RECURSION
    // https://leetcode.com/problems/count-and-say/solutions/3412863/java-recursion-good-to-go/
    int counter;
    public String countAndSay_2(int n) {
        if(n==1) return "1" ;
        String s= countAndSay_2(n-1);

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<s.length();i++){
            counter ++;
            if(i==s.length()-1 ||s.charAt(i)!=s.charAt(i+1)){
                sb.append(counter).append(s.charAt(i));
                counter=0;
            }
        }
        return sb.toString();
    }

}
