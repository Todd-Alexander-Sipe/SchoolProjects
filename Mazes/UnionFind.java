public class UnionFind {
    private final int[] SIZE;
    private final int[] POINT_TO_PARENT;

    public UnionFind(int size) {
        this.SIZE = new int[size];
        POINT_TO_PARENT = new int[size];

        for (int i = 0; i < size; i++) {
            POINT_TO_PARENT[i] = i;
            this.SIZE[i] = 1;
        }
    }

    public int find(int p) {
        int root = p;

        while (root != POINT_TO_PARENT[root]){
            root = POINT_TO_PARENT[root];
        }

        while (p != root){
            int next = POINT_TO_PARENT[p];
            POINT_TO_PARENT[p] = root;
            p = next;
        }

        return root;
    }

    public boolean connected(int p, int q){
        return find(p) == find(q);
    }

    public void union(int p, int q){
        int root1 = find(p);
        int root2 = find(q);
        if ( root1 == root2){
            return;
        }
        if (SIZE[root1] < SIZE[root2]){
            SIZE[root2] += SIZE[root1];
            POINT_TO_PARENT[root1] = root2;
        }
        else{
            SIZE[root1] += SIZE[root2];
            POINT_TO_PARENT[root2] = root1;
        }
    }
}
