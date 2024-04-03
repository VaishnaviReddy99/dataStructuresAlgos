package greedy;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class GreedyBag {
    private static double initialAveragePercentage = 0;
    private static PriorityQueue<Bag> updatedPriorityQueue;

    /*
    Created an Object Bag, for better readability and coding.
    Bag object contains all the descriptive details of each bag
     */
    static class Bag {
        int numWorking;
        int total;
        int index;
        double ratio;
        double avgIncrease;

        public Bag(int numWorking, int total, int index) {
            this.numWorking = numWorking;
            this.total = total;
            this.index = index;
        }

        @Override
        public String toString() {
            return "Bag {" +
                    "numWorking = " + numWorking +
                    ", total = " + total +
                    ", index = " + index +
                    ", ratio = " + ratio +
                    '}';
        }

        public Bag(int numWorking, int total, int index, double ratio) {
            this.numWorking = numWorking;
            this.total = total;
            this.index = index;
            this.ratio = ratio;
        }

        // Method to calculate and set the maximum increase in ratio when adding an extra working device.
        public void setAvgIncrease( ){
            double ratioA = (double) numWorking/total;
            double ratioB = (double) (numWorking + 1)/(total + 1);
            avgIncrease = ratioB - ratioA;
        }
    }

    public static void main(String[] args){
        // Taking the input using Scanner
        Scanner scanner = new Scanner(System.in);
        String[] inputLine = scanner.nextLine().split(" ");
        int n = Integer.parseInt(inputLine[0]), k = Integer.parseInt(inputLine[1]);
        int[] numWorking = new int[n];
        int[] total = new int[n];
        double averageSum = 0;


        for(int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] numbers = line.split(" ");
            numWorking[i] = Integer.parseInt(numbers[0]);
            total[i] = Integer.parseInt(numbers[1]);
            // Read bag data and compute the initial average percentage of the data given.
            averageSum += (double) numWorking[i]/total[i];
        }

        initialAveragePercentage = (double) averageSum/n; // Calculate the initial average.
        scanner.close();

        /*
            The code takes in two parameters: taskNumber isComputeStats
            taskNumber: this parameter determines which strategy to chose/run
            isComputeStats: this parameter is used to get details on the percentage increase. This is used for the experimental study.
         */
        if(args.length == 2) {
            String taskNumber = args[0];
            String isComputeStats = args[1];
            if (taskNumber.equalsIgnoreCase("task1")) {
                solution1(numWorking, total, n, k);
                if (isComputeStats != null && isComputeStats.equalsIgnoreCase("yes"))
                    print(updatedPriorityQueue, n, "Strat1");
            }
            else if (taskNumber.equalsIgnoreCase("task2")) {
                solution2(numWorking, total, n, k);
                if (isComputeStats != null && isComputeStats.equalsIgnoreCase("yes"))
                    print(updatedPriorityQueue, n, "Strat2");
            }
            else if (taskNumber.equalsIgnoreCase("task3")) {
                solution3(numWorking, total, n, k);
                if (isComputeStats != null && isComputeStats.equalsIgnoreCase("yes"))
                    print(updatedPriorityQueue, n, "Strat3");
            }
            else if (taskNumber.equalsIgnoreCase("task4")) {
                solution4(numWorking, total, n, k);
                if (isComputeStats != null && isComputeStats.equalsIgnoreCase("yes"))
                    print(updatedPriorityQueue, n, "Strat4");
            }

        }
    }

    private static void solution1(int[] numWorking, int[] total, int n, int k){
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++){
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bags.add(bag);
        }

        // Create a priority queue based on the ascending number of working devices.
        PriorityQueue<Bag> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a.numWorking == b.numWorking) {
                return Integer.compare(a.index, b.index);
            }
            return Integer.compare(a.numWorking, b.numWorking);
        });
        priorityQueue.addAll(bags);

        for(int i = 0; i < k; i++) {
            Bag bag = priorityQueue.poll(); // Remove the bag with the minimum number of working devices.
            System.out.print(bag.index + " ");
            // Increment the working and total devices for the selected bag without changing its ratio.
            priorityQueue.offer(new Bag(bag.numWorking + 1, bag.total + 1, bag.index, bag.ratio));
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }

    private static void solution2(int[] numWorking, int[] total, int n, int k) {
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++) {
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bags.add(bag);
        }

        // Create a priority queue that orders Bag objects by their ascending ratio.
        PriorityQueue<Bag> priorityQueue = new PriorityQueue<>((a, b) -> {
            if(a.ratio == b.ratio) {
                return Integer.compare(a.index, b.index);
            }
            return Double.compare(a.ratio, b.ratio);
        });
        priorityQueue.addAll(bags);

        for(int i = 0; i < k; i++) {
            Bag bag = priorityQueue.poll();
            System.out.print(bag.index + " ");
            // Increment the working and total devices for the selected bag, and recalculate its ratio.
            priorityQueue.offer(new Bag(bag.numWorking + 1, bag.total + 1, bag.index, (double) (bag.numWorking + 1)/(bag.total + 1)));
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }

    private static void solution3(int[] numWorking, int[] total, int n, int k) {
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++) {
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bags.add(bag);
        }

        // Create a priority queue based on the ascending number of total devices.
        PriorityQueue<Bag> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a.total == b.total) {
                return Integer.compare(a.index, b.index);
            }
            return Integer.compare(a.total, b.total);
        });
        priorityQueue.addAll(bags);

        for(int i = 0; i < k; i++) {
            Bag bag = priorityQueue.poll();
            System.out.print(bag.index + " ");
            // Increment the working and total devices for the selected bag without changing its ratio.
            priorityQueue.offer(new Bag(bag.numWorking + 1, bag.total + 1, bag.index));
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }


    private static void solution4(int[] numWorking, int[] total, int n, int k) {
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++) {
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bag.setAvgIncrease();
            bags.add(bag);
        }

        // Create a priority queue that orders Bag objects by their descending maximum increase in ratio.
        PriorityQueue<Bag> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a.avgIncrease == b.avgIncrease) {
                return Integer.compare(a.index, b.index);
            }
            return Double.compare(b.avgIncrease, a.avgIncrease);
        });
        priorityQueue.addAll(bags);

        for(int i = 0; i < k; i++) {
            Bag bag = priorityQueue.poll();
            Bag newBag = new Bag(bag.numWorking + 1, bag.total + 1, bag.index, bag.ratio);
            newBag.setAvgIncrease();
            // Output the index of the bag with the maximum increase in ratio.
            System.out.print(bag.index + " ");
            priorityQueue.offer(newBag);
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }

    private static void print(PriorityQueue<Bag> priorityQueue, int n, String strategy) {
        // Calculate and print the relevant statistics.
        double averageSum = 0;
        for(Bag bag: priorityQueue) {
            averageSum += (double) bag.numWorking/bag.total;
        }
        double average = averageSum/n;

        System.out.println("Initial percentage: " + initialAveragePercentage * 100);
        System.out.println("New percentage: " + average * 100);
        System.out.println("Percentage increase for " + strategy + ": " + ((average - initialAveragePercentage) * 100)+"%");
}
}
