package dynamicprogramming;

import java.util.*;
import java.util.List;

public class RiverCrossingProb2 {
    //2D Memoization
    private static int[][] steps_memo;
    static List<String> paths = new ArrayList<>();
    private static Map<String,Pair<Integer,List<Integer>>> memos2D = new HashMap<>();
    private static long starttime;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] inputLine = scanner.nextLine().split(" ");
        int n = Integer.parseInt(inputLine[0]), k = Integer.parseInt(inputLine[1]), m = Integer.parseInt(inputLine[2]);
        int[] costs = new int[n];
        inputLine = scanner.nextLine().split(" ");
        int i = 0;

        for(String input : inputLine){
            costs[i] = Integer.parseInt(input);
            i++;
        }

        Pair<Integer,List<Integer>> result;
        if(args.length == 1){
            String taskNumber = args[0];
            switch (taskNumber) {
                case "run5":
                    starttime = System.currentTimeMillis();
                    findMinCost5(costs,k,n,m);
//                    System.out.println();
                    System.out.println((System.currentTimeMillis() - starttime));
                    break;
                case "run6A":
                    starttime = System.currentTimeMillis();
                    steps_memo = new int[n][n];
                    result = findMinCostMemoAndIndiceswithM6A(costs,k,0,0,m,n);
                    List<Integer> indices = result.getIndices();
                    Collections.reverse(indices);
                    for(Integer idx : result.getIndices())
                        System.out.print(idx+" ");
                    System.out.println((System.currentTimeMillis() - starttime));
                    break;

                case "run6B":
                    starttime = System.currentTimeMillis();
                    findMinCostAndIndiceswithM6B(costs,k,m,n);
                    System.out.println((System.currentTimeMillis() - starttime));
                    break;

                case "run7":
                    findMinCostAndIndicesWithM7(costs,k,m,n);
                    break;
                case "run8":
                    findMinCostAndIndicesWithM8(costs,k,m,n);
                    break;
                default:
                    System.out.println("Wrong Choice");


            }
        }

    }




    /*
        5. O(k^n) -- Brute force approach
    */
    public static void findMinCost5(int[] cost, int k, int n,int m) {
        paths = new ArrayList<>();
        findAllPaths(cost,k,0,n, "");
        int nc = Integer.MAX_VALUE;
        String path = "";
        for(String in : paths){
            if(in.length() == m){
                int c = 0;
                for(int j = 0;j<m;j++){
                    c = c + cost[Character.getNumericValue(in.charAt(j))];
                }
                if(c < nc){
                    nc = c;
                    path = in;
                }
            }
        }
        for(int i = 0;i<m;i++){
            System.out.print(path.charAt(i)+" ");
        }
        System.out.println();


    }

    //Find All possible paths with the given K constraint
    public static void findAllPaths(int[] cost, int k, int i, int n,String currPath) {
        if (i >= n) {
            paths.add(currPath);
        }
        String str = "";
        for (int j = 1; j <= k; j++) {
            str = currPath + i;
            if (i + j    <= n) {
                findAllPaths(cost,k,i+j,n,str);
            }
        }

    }


    /*
       The time complexity is better described as O(k * n * m), where:
            k is the maximum number of steps allowed,
            n is the size of the cost array,
            m is the number of unique pairs (i, j) that the function can be called with.
     */
    public static Pair<Integer,List<Integer>>  findMinCostMemoAndIndiceswithM6A(int[] cost, int k, int i , int j , int m, int n) {
        if (i >= n && (j != m)) {
            return new Pair<>(Integer.MAX_VALUE, new ArrayList<>());
        }
        if (i >= n && (m == j )) {
            return new Pair<>(0, new ArrayList<>());
        }
        if(memos2D.containsKey(Integer.toString(i)+Integer.toString(j))){
            return memos2D.get(Integer.toString(i)+Integer.toString(j));
        }
        int minCost = Integer.MAX_VALUE;
        int idx = 0;
        List<Integer> minIndexes = new ArrayList();

        for(int x = 1;x<=k && (i+x <= n);x++ ){
            Pair<Integer,List<Integer>>  next = findMinCostMemoAndIndiceswithM6A(cost,k,i+x,j+1,m,n);
            int tc = Integer.MAX_VALUE;
            if(next.getMinCost() != Integer.MAX_VALUE) tc = next.getMinCost() + cost[i];
            if(minCost > tc){
                minCost = tc;
                idx = j+1;
                minIndexes = new ArrayList<>(next.getIndices());
                minIndexes.add(i);
            }
        }

        steps_memo[i][idx] = minCost;
        memos2D.put(Integer.toString(i)+Integer.toString(j),new Pair<>(minCost,minIndexes));
        return new Pair<>(minCost,minIndexes);
    }

    /*
       6.B O(n*k*m) Bottom up approach
     */

    public static void findMinCostAndIndiceswithM6B(int[] cost, int k, int m, int n) {
        steps_memo = new int[n][n];
        Map<String, String > paths = new HashMap<>();
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                steps_memo[i][j] = -1;
                paths.put(Integer.toString(i)+ j,"");
            }
        }

        // Populate base cases
        for (int j = 0; j < n; j++) {
            steps_memo[0][j] = cost[0];
            paths.put(Integer.toString(0)+ j,""+0);
        }

        for (int i = 1 ; i <n; i++) {
            for (int j = 0; j < m; j++) {
                int minCost = Integer.MAX_VALUE;
                int idx = 0;
                String p = "";
                for (int x = 1; x <= k && (i - x >= 0); x++) {
                    int nextCost = steps_memo[i - x][j + 1];
                    int tc = (nextCost != Integer.MAX_VALUE) ? nextCost + cost[i] : Integer.MAX_VALUE;
                    if (minCost > tc) {
                        minCost = tc;
                        idx = j + 1;
                        p = paths.get(Integer.toString(i-x)+ idx);
                        p = p +  " "+i;

                    }
                }
                steps_memo[i][idx] = minCost;
                paths.put(Integer.toString(i)+ idx,p);
            }
        }

//        System.out.println(steps_memo[n-1][m]);
        System.out.println(paths.get(Integer.toString(n-1)+ m));
    }



    public static void findMinCostAndIndicesWithM7(int[] cost, int k, int m, int n) {
        steps_memo = new int[n][n];
        Map<String, String> paths = new HashMap<>();

        for (int i = 0; i < n; i++) {
           steps_memo[i][n-1] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < n; i++) {
            steps_memo[0][i] = Integer.MAX_VALUE;
        }

        steps_memo[0][n-1] = 0;
        PriorityQueue<Integer[]> priorityQueue = new PriorityQueue<>((a,b)->Integer.compare(a[0],b[0]));
        for(int j = 1;j <= m;j++){
            if(j == 1) priorityQueue.add(new Integer[]{0,n-1});
            else{
                priorityQueue.add(new Integer[]{Integer.MAX_VALUE,n});
            }
            for(int i = n-1;i>= 0;i--){
                while(priorityQueue.peek()[1] > i + k){
                    priorityQueue.poll();
                }
                if(steps_memo[priorityQueue.peek()[1]][j-1] == Integer.MAX_VALUE){
                    priorityQueue.offer(new Integer[]{steps_memo[i][j-1],i});
                    continue;
                }
                int idx = priorityQueue.peek()[1];
                if(steps_memo[idx][j-1] != Integer.MAX_VALUE)
                    steps_memo[i][j]= cost[i] + steps_memo[priorityQueue.peek()[1]][j-1];
                else
                    steps_memo[i][j]= Integer.MAX_VALUE;
                priorityQueue.offer(new Integer[]{steps_memo[i][j-1],i});

            }
        }
        System.out.println(steps_memo[0][m]);



    }




    public static void findMinCostAndIndicesWithM8(int[] cost, int k, int m, int n) {
        steps_memo = new int[n][n];
        Map<String, String> paths = new HashMap<>();

        for (int i = 0; i < n; i++) {
            steps_memo[i][n-1] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < n; i++) {
            steps_memo[0][i] = Integer.MAX_VALUE;
        }

        steps_memo[0][n-1] = 0;
        Deque<Integer[]> deque = new ArrayDeque<>();

        for(int j = 1;j <= m;j++){
            if(j == 1) deque.add(new Integer[]{0,n-1});
            else{
                deque.add(new Integer[]{Integer.MAX_VALUE,n});
            }
            for(int i = n-1;i>= 0;i--){
                while(deque.peek()[1] > i + k){
                    deque.poll();
                }
                if(steps_memo[deque.peek()[1]][j-1] == Integer.MAX_VALUE){
                    deque.offer(new Integer[]{steps_memo[i][j-1],i});
                    continue;
                }
                int idx = deque.peek()[1];
                if(steps_memo[idx][j-1] != Integer.MAX_VALUE)
                    steps_memo[i][j]= cost[i] + steps_memo[deque.peek()[1]][j-1];
                else
                    steps_memo[i][j]= Integer.MAX_VALUE;
                deque.offer(new Integer[]{steps_memo[i][j-1],i});

            }
        }
        System.out.println(steps_memo[0][m]);

    }


    static class Pair<A,B>{
        A cost;
        B indices;

        public Pair(A a1, B b1) {
            this.cost = a1;
            this.indices = b1;
        }

        public A getMinCost() {
            return cost;
        }

        public B getIndices() {
            return indices;
        }
    }


}
