package dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  UnionFind
 *
 *  given input,
 *
 *  1) check if 2 nodes are connected
 *  2) find one nodes' parent
 *
 */

public class UnionFindTest1 {

    // attr
    Integer[] parents;
    Map<Integer, List<Integer>> graph;
    int nodeCnt;

    int connectCnt;

    List<List<Integer>> dependency;

    // constructor
    public UnionFindTest1(Integer[] elements, List<List<Integer>> dependency, int nodeCnt){

        // init parent
        this.parents = new Integer[nodeCnt];
        for(int i = 0; i < nodeCnt; i++){
            this.parents[i] = i; // at first, every element is its own parent
        }

        // init graph
        this.dependency = dependency;
        this.graph = new HashMap<>();
        for(List<Integer> x: dependency){
            Integer parent = x.get(0);
            Integer child = x.get(1);

            this.graph.putIfAbsent(parent, new ArrayList<>());

            List<Integer> childList = this.graph.get(parent);
            childList.add(child);

            this.graph.put(parent, childList); // ??
        }

        // init nodecnt
        this.nodeCnt = nodeCnt;
        this.connectCnt = nodeCnt;
    }

    // method
    /**
     *  union
     *
     *  find
     *
     *  isConnected
     */
    public void union(Integer node1, Integer node2){

        if (node1.equals(node2)){
            return;
        }

        Integer parent1 = this.find(node1);
        Integer parent2 = this.find(node2);

        // can also do `path compression` for performance optimization
        this.connectCnt -= 1; // ??
        this.parents[node2] = parent1; // or this.parents[node1] = parent1
    }

    // find node parent
    public Integer find(Integer node){

        // ?
        if (this.parents[node].equals(node)){
            return node;
        }else{
            this.parents[node] = this.find(node);
            return this.parents[node]; // ??
        }
//
//        if(this.graph.containsKey(node)){
//            for(int x: this.graph.get(node)){
//                 this.find(x);
//            }
//        }
//
//        return node; // ??
    }

    public boolean isConnected(Integer node1, Integer node2){

        if (node1.equals(node2)){
            return true;
        }
        return this.find(node1).equals(this.find(node2));
    }

    public int connectCnt(){
        return this.connectCnt;
    }
}
