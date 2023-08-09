package dev;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class hello {
    public static void main(String[] args) {

        //System.out.println("hello!");


        Integer[] _array = new Integer[4];
        _array[0] = 10;
        _array[1] = -2;
        _array[2] = 0;
        _array[3] = 99;
        Arrays.stream(_array).forEach(System.out::println);
        Arrays.sort(_array, Collections.reverseOrder());
        System.out.println("---");
        Arrays.stream(_array).forEach(System.out::println);

        System.out.println("--->");

        List<Integer> _list = Arrays.asList(1,2,3,4);
        _list.forEach(System.out::println);
        Collections.sort(_list, Collections.reverseOrder());
        System.out.println("---");
        _list.forEach(System.out::println);

    }
}
