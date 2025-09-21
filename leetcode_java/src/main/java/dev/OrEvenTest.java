package dev;

public class OrEvenTest {
    public static void main(String[] args){
        int res = 2 | 4| 6;
        System.out.println(res);

        int tmp = 0;
        int[] cache = new int[]{2,4,6};
        for(int x: cache){
            tmp = (tmp | x);
        }

        System.out.println(tmp);
    }

}
