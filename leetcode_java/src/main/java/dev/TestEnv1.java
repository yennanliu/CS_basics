package dev;

public class TestEnv1 {

    // custom UF
    class UF1{

        // attr
        int[] degree;
        int[] parents; // ???
        int cnt;

        // constructor
        UF1(int n){
            this.degree = new int[n];
            this.parents = new int[n];
            this.cnt = cnt;

            // init values
            for(int i = 0; i < n; i ++){
                this.degree[i] = 0;
                this.parents[i] = i;// node's parent is itself at beginning
            }
        }
        // method
        /**
         * find(x): Get root parent of x with path compression
         * union(x, y): Connect two nodes, return false if already connected
         * connected(x, y): Check if two nodes are in same component
         */


        // ok
        public int find(int x){
            // ???? while or if ??
            // -> yes. while
            while(this.parents[x] != x){
                /**
                 *  parent[x] = parent[parent[x]];
                 *  x = parent[x];
                 *
                 */
                // correct
                this.parents[x] = find(x);
            }
            return this.parents[x]; // ???
        }

        // ok
        public void union(int x, int y){
            if(x == y){
                return;
            }
            int parentX = this.find(x);
            int parentY = this.find(y);

            // need to do `path relaxation` ??? (via `rank`)

            // ???
            this.parents[x] = parentY; // ???
            this.cnt -= 1;
        }

        // ok
        public boolean connected(int x, int y){
            if(x == y){
                return true;
            }
            return this.find(x) == this.find(y);
        }

    }


}
