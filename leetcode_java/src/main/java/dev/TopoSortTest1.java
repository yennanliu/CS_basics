package dev;

import java.util.*;

/**
 *  Offer the global sorting based on the `prev, next relations` info.
 *
 *  Example 1:
 *
 *  input = [a,b], [b,c], [c,d]
 *
 *  -> output:  [a,b,c,d]
 *
 *  Example 2:
 *
 *  input = [1,2], [2,4], [4,5]
 *
 *  -> output: [1,2,4,5]
 *
 *
 */
public class TopoSortTest1 {

    // attr
    List<Integer> degrees;
    List<Integer> sortedRes;
    Map<Integer, List<Integer>> graph;
    Queue<Integer> queue;

    //List<List<String>> dependencies;

    // constructor
    public TopoSortTest1(){
        this.degrees = new ArrayList<>();
        this.sortedRes = new ArrayList<>();
        this.graph = new HashMap<>();
        this.queue = new LinkedList<>();
        //this.dependencies = new ArrayList<>();
    }

    // method
    /**
     *
     * List<List<String>> dependencies
     *
     * {prev: next}
     *
     */
    public List<Integer> topoSort(List<List<Integer>> dependencies, int size) throws Exception {
        // edge case
        if (dependencies.isEmpty() || size == 0) {
            return this.sortedRes;
        }

        // init degree
        //int N = size; //dependencies.size(); // ??
        for(int i = 0; i < size; i++){
            this.degrees.add(0);
        }

        // build graph
        for(List<Integer> x: dependencies){
            Integer prev = x.get(0);
            Integer next = x.get(1);
            // update degree
            if(this.graph.containsKey(prev)){
               this.degrees.add(prev, this.degrees.get(prev)+1);
            }
            List<Integer> curNext = this.graph.getOrDefault(prev, new ArrayList<>());
            curNext.add(next);
            this.graph.put(prev, curNext);
        }

        // add `degree = 0` elements to queue
        for(Integer x: degrees){
            if(x==0){
                this.queue.add(x);
            }
        }

        // bfs ??
        while(!this.queue.isEmpty()){
            Integer curVal = queue.poll();
            this.sortedRes.add(curVal); // ???
            // go through `next` nodes
            if(this.graph.containsKey(curVal)){
                List<Integer> nextList = this.graph.get(curVal);
                for(Integer x: nextList){
                    //this.queue.add(x);
                    this.degrees.add(this.degrees.get(x), this.degrees.get(x)-1);

                    // NOTE !!! if degree = 0, put into queue
                    if(this.degrees.get(x) == 0){
                        this.queue.add(x);
                    }
                }
            }
        }

        System.out.println(">>> this.sortedRes = " + this.sortedRes);

        // validate
        if(this.sortedRes.size() != size){
            throw new Exception("Not able to do topological sort, sorted size not equals to input size");
        }

        return this.sortedRes;
    }

    // testing
    public static void main(String[] args) throws Exception {
        System.out.println(">>> 123");
        TopoSortTest1 topoSortTest1 = new TopoSortTest1();
        List<List<Integer>> deps = new ArrayList<>();
        List<Integer> tmp1 = new ArrayList<>();
        List<Integer> tmp2 = new ArrayList<>();
        List<Integer> tmp3 = new ArrayList<>();

        tmp1.add(1);
        tmp1.add(2);

        tmp2.add(2);
        tmp2.add(4);

        tmp3.add(4);
        tmp3.add(5);

        deps.add(tmp1);
        deps.add(tmp2);
        deps.add(tmp3);

       // topoSortTest1.topoSort(deps, 4);

        System.out.println(topoSortTest1.topoSort(deps, 4));

    }
}
