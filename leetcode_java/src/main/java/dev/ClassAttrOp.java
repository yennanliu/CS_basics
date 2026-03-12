package dev;

class ValCnt2 {
    char val;

    // if using `final` will cause below error:
    // java: cannot assign a value to final variable cnt

   // final int cnt;
    // /Users/yennanliu/CS_basics/leetcode_java/src/main/java/dev/ClassAttrOp.java:31:10
    //java: cnt has private access in dev.ValCnt2
    //private int cnt;

    int cnt;

    ValCnt2(char val, int cnt){
        this.val = val;
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "val = " + this.val + ", cnt = " + this.cnt;
        //return super.toString();
    }
}

public class ClassAttrOp {



    public static void main(String[] args) {

        ValCnt2 v = new ValCnt2('s', 3);
        System.out.println(">>> (before) v = "  + v);

        v.cnt -= 1;
        System.out.println(">>> (after) v = "  + v);
    }




}
