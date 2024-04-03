package dynamicprogramming;

import java.util.*;
import java.util.List;

public class RiverCrossingProb1 {

    // 1D Path and Cost Tracking for Index
    private static Map<Integer,Pair<Integer,List<Integer>>> memos = new HashMap<>();
    private static long startTime;


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] inputLine = scanner.nextLine().split(" ");
        int n = Integer.parseInt(inputLine[0]), k = Integer.parseInt(inputLine[1]);
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
                case "run1":
                    startTime = System.currentTimeMillis();
                    result = findMinCost1(costs,k,0,n);
                    List<Integer> path = result.getIndices();
                    Collections.reverse(path);
                    for(Integer idx : result.getIndices())
                        System.out.print(idx+" ");
//                    System.out.println(result.getMinCost());
                    System.out.println((System.currentTimeMillis() - startTime));
                    break;
                case "run2A":
                    startTime = System.currentTimeMillis();
                    result = findMinCostMemoAndIndices2A(costs,k,0,n);
//                    System.out.println(result.getMinCost());
                    path = result.getIndices();
                    Collections.reverse(path);
                    for(Integer idx : result.getIndices())
                        System.out.print(idx+" ");
//                    System.out.println();
                    System.out.println((System.currentTimeMillis() - startTime));
                    break;

                case "run2B":
                    startTime = System.currentTimeMillis();
                    finMinCost2B(costs,k,n);
//                    System.out.println();
                    System.out.println((System.currentTimeMillis() - startTime));
                    break;

                case "run3":
                    startTime = System.currentTimeMillis();
                    finMinCost3(costs,k,n);
//                    System.out.println();
                    System.out.println((System.currentTimeMillis() - startTime));
                    break;
                case "run4":
                    startTime = System.currentTimeMillis();
                    findMinCost4(costs,k,n);
//                    System.out.println();
                    System.out.println((System.currentTimeMillis() - startTime));
                    break;

                default:
                    System.out.println("Wrong choice");


            }
        }

    }



    /*
        1. O(K^N) Approach Brute force
     */
    public static Pair<Integer, List<Integer>> findMinCost1(int[] cost, int k, int i, int n) {
        if (i >= n) {
            List<Integer> indexes = new ArrayList<>();
            return new Pair<>(0, indexes);
        }
        int minCost = Integer.MAX_VALUE;
        List<Integer> minIndexes = new ArrayList();
        for (int j = 1; j <= k; j++) {
            if (i + j    <= n) {
                Pair<Integer, List<Integer>> nextCostAndIndexes = findMinCost1(cost, k, i + j, n);
                int nc =  nextCostAndIndexes.getMinCost() + cost[i];
                if(nc<minCost){
                    minCost = nc;
                    minIndexes = new ArrayList<>(nextCostAndIndexes.getIndices());
                    minIndexes.add(i);
                }
            }
        }

        return new Pair<>(minCost, minIndexes);
    }



    /*
        2A. O(N*K) Approach -- Memoization
     */
    public static Pair<Integer,List<Integer>> findMinCostMemoAndIndices2A(int[] cost, int k, int i, int n) {
        if (i >= n) {
            return new Pair<>(0,new ArrayList<>());
        }
        if(memos.containsKey(i)){
            return memos.get(i);
        }
        int minCost = Integer.MAX_VALUE;
        List<Integer> minIndexes = new ArrayList();
        for (int j = 1; j <= k; j++) {
            if (i + j    <= n) {
                Pair<Integer, List<Integer>> nextCostAndIndexes = findMinCostMemoAndIndices2A(cost, k, i + j, n);
                int nc =  nextCostAndIndexes.getMinCost() + cost[i];
                if(nc<minCost){
                    minCost = nc;
                    minIndexes = new ArrayList<>(nextCostAndIndexes.getIndices());
                    minIndexes.add(i);
                }

            }
        }
        memos.put(i, new Pair<>(minCost,minIndexes));
        return memos.get(i);
    }

    /*
        2B. O(N*k) -- Bottom up
    */
    public static void finMinCost2B(int[] cost, int k, int n) {
        for (int idx = n - 1; idx >= 0; idx--) {
            int minCost = Integer.MAX_VALUE;
            List<Integer> minIndexes = new ArrayList<>();
            for (int j = 1; j <= k && (idx + j <= n); j++) {
                Pair<Integer, List<Integer>> nextCostAndIndexes = memos.getOrDefault(idx + j, new Pair<>(0, new ArrayList<>()));
                int nc = nextCostAndIndexes.getMinCost() + cost[idx];
                if (nc < minCost) {
                    minCost = nc;
                    minIndexes = new ArrayList<>(nextCostAndIndexes.getIndices());
                    minIndexes.add(idx);
                }
            }
            memos.put(idx, new Pair<>(minCost, minIndexes));
        }

        //System.out.println(memos.get(0).getMinCost());
        List<Integer> li = memos.get(0).getIndices();
        Collections.reverse(li);
        for(Integer num : li){
            System.out.print(num+" ");
        }
    }



    /*
        3. O(NlogN) Approach
     */
    public static int finMinCost3(int[] c,int k,int n){
        int[] memo = new int[n];
        Arrays.fill(memo,-1);
        memo[0] = c[0];
        PriorityQueue<Integer> minCosts = new PriorityQueue<>();
        minCosts.add(0);

        for (int i = 1; i < n; i++) {
            while (i - minCosts.peek() > k) {
                minCosts.poll();
            }
            memo[i] = c[i] + memo[minCosts.peek()];
            minCosts.add(i);

            if (i == n - 1) {
                break;
            }
        }
//        System.out.println(memo[n-1]); -- MinCost

        for(Integer i : getMinIndicesFromDP(c,memo,k,n))
            System.out.print(i+" ");

        return memo[n-1];
    }

    /*
        Tracing the path of the jumps from the given DP array
     */

    private static List<Integer> getMinIndicesFromDP(int[] c, int[] memo, int k, int n) {
        List<Integer> minIndexes = new ArrayList<>();
        int currentIndex = n - 1;

        while (currentIndex >= 0) {
            minIndexes.add(currentIndex);
            int previousIndex = currentIndex - 1;

            if (previousIndex >= 0) {
                if (memo[currentIndex] == c[currentIndex] + memo[previousIndex]) {
                    currentIndex = previousIndex;
                } else {
                    int jumpIndex = -1;

                    for (int j = 1; j <= k && currentIndex - j >= 0; j++) {
                        if (c[currentIndex] + memo[currentIndex - j] == memo[currentIndex]) {
                            jumpIndex = j;
                            break;
                        }
                    }

                    if (jumpIndex != -1) {
                        currentIndex -= jumpIndex;
                    } else {
                        // Handle the case where none of the jumps lead to the current index
                        break;
                    }
                }
            } else {
                break;
            }
        }

        Collections.reverse(minIndexes);
        return minIndexes;
    }

    /*
      O(N)
     */
    public static int findMinCost4(int[] c,int k,int n){
        int[] memo = new int[n];
        Arrays.fill(memo,-1);
        memo[0] = c[0];
        Deque<Integer> minCosts = new ArrayDeque<>();
        minCosts.add(0);

        for (int i = 1; i < n; i++) {
            while (i - minCosts.peek() > k) {
                minCosts.poll();
            }
            memo[i] = c[i] + memo[minCosts.peek()];
            minCosts.add(i);

            if (i == n - 1) {
                break;
            }
        }

        for(Integer i : getMinIndicesFromDP(c,memo,k,n))
            System.out.print(i+" ");

        return memo[n-1];
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
