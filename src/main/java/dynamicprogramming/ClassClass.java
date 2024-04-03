package dynamicprogramming;

import java.util.PriorityQueue;

public class ClassClass {

    public static void main(String[] args){

    }

    public static void findMinCostAndIndicesWithMPQ(int[] cost, int k, int m, int n) {
        int[][] dp = new int[n + 1][m];

        for (int i = 0; i < n; i++) {
            dp[i][0] = Integer.MAX_VALUE;
        }

        for (int i = 1; i < m; i++) {
            PriorityQueue<PathInfo> pq = new PriorityQueue<>();
            pq.offer(new PathInfo(0, 0, ""));

            for (int j = 0; j < n; j++) {
                PathInfo minPath = pq.poll();
                if(minPath.cost != Integer.MAX_VALUE)
                    dp[j][i] = minPath.cost + cost[j];
                else
                    dp[j][i]= minPath.cost;

                if (j < n - 1) {
                    pq.offer(new PathInfo(dp[j][i], j + 1, minPath.path + " " + j));
                }

                if (j - k >= 0) {
                    pq.remove(new PathInfo(dp[j - k][i - 1], j - k, ""));
                }
            }
        }

        int minCost = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            minCost = Math.min(minCost, dp[j][m - 1]);
        }

        System.out.println(minCost);
    }

    static class PathInfo implements Comparable<PathInfo> {
        int cost;
        int idx;
        String path;

        public PathInfo(int cost, int idx, String path) {
            this.cost = cost;
            this.idx = idx;
            this.path = path;
        }

        @Override
        public int compareTo(PathInfo other) {
            return Integer.compare(this.cost, other.cost);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PathInfo pathInfo = (PathInfo) obj;
            return idx == pathInfo.idx;
        }

        @Override
        public int hashCode() {
            return idx;
        }
    }

}
