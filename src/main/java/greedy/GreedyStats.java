package greedy;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GreedyStats {
    private static double initialAveragePercentage = 0;
    private static PriorityQueue<Bag> updatedPriorityQueue;

    static class Bag {
        int numWorking;
        int total;
        int index;
        double ratio;
        double maxIncrease;

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
        public void setMaxIncrease( ){
            double ratioA = (double) numWorking/total;
            double ratioB = (double) (numWorking + 1)/(total + 1);
            maxIncrease = ratioB - ratioA;
        }
    }

    public static void main(String[] args){
        // Taking the input.
        int n = Integer.parseInt(args[0]), k = 2000;
        int[] numWorking = new int[n];
        int[] total = new int[n];
        double averageSum = 0;
        Random randomGen = new Random();

// Read bag data and compute the initial average percentage.
        for(int i = 0; i < n; i++) {
            total[i] = randomGen.nextInt(1000) + 1;
            int maxAttempts = 1000; // Set a maximum number of attempts
            int attempts = 0;

            do {
                numWorking[i] = randomGen.nextInt(1000) + 1;
                attempts++;
            } while (numWorking[i] >= total[i] && attempts < maxAttempts);

            if (attempts >= maxAttempts) {
                numWorking[i] = i*20;
                total[i] = i*50;
                // Handle the case where it couldn't find a suitable value within the allowed attempts
                // You might choose to break the loop, generate an error message, or take some other action.
            }

            averageSum += (double) numWorking[i] / total[i];
        }


        initialAveragePercentage = (double) averageSum/n * 100; // Calculate the initial average percentage.

        System.out.println("n= "+n+" k="+k);
        System.out.println("=========Strategy 1==========");
        solution1(numWorking, total, n, k);
        print(updatedPriorityQueue, n, "Strat1");
        System.out.println("=========Strategy 2==========");
        solution2(numWorking, total, n, k);
        print(updatedPriorityQueue, n, "Strat2");
        System.out.println("=========Strategy 3==========");
        solution3(numWorking, total, n, k);
        print(updatedPriorityQueue, n, "Strat3");
        System.out.println("=========Strategy 4==========");
        solution4(numWorking, total, n, k);
        print(updatedPriorityQueue, n, "Strat4");

    }

    private static void solution1(int[] numWorking, int[] total, int n, int k){
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++){
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bag.setMaxIncrease();
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
            //System.out.print(bag.index + " ");
            // Increment the working and total devices for the selected bag without changing its ratio.
            priorityQueue.offer(new Bag(bag.numWorking + 1, bag.total + 1, bag.index));
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }

    private static void solution2(int[] numWorking, int[] total, int n, int k) {
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++) {
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bag.setMaxIncrease();
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
            //System.out.print(bag.index + " ");
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
            bag.setMaxIncrease();
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
            //System.out.print(bag.index + " ");
            // Increment the working and total devices for the selected bag without changing its ratio.
            priorityQueue.offer(new Bag(bag.numWorking + 1, bag.total + 1, bag.index, bag.ratio));
        }
        System.out.println();
        updatedPriorityQueue = priorityQueue; // Update the class variable with the final priority queue.
    }


    private static void solution4(int[] numWorking, int[] total, int n, int k) {
        List<Bag> bags = new ArrayList<>();
        // Create Bag objects and populate the 'bags' list with initial data.
        for(int i = 0; i < n; i++) {
            Bag bag = new Bag(numWorking[i], total[i], i, (double) numWorking[i]/total[i]);
            bag.setMaxIncrease();
            bags.add(bag);
        }

        // Create a priority queue that orders Bag objects by their descending maximum increase in ratio.
        PriorityQueue<Bag> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a.maxIncrease == b.maxIncrease) {
                return Integer.compare(a.index, b.index);
            }
            return Double.compare(b.maxIncrease, a.maxIncrease);
        });
        priorityQueue.addAll(bags);

        for(int i = 0; i < k; i++) {
            Bag bag = priorityQueue.poll();
            Bag newBag = new Bag(bag.numWorking + 1, bag.total + 1, bag.index, bag.ratio);
            newBag.setMaxIncrease();
            // Output the index of the bag with the maximum increase in ratio.
            //System.out.print(bag.index + " ");
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
        double average = (averageSum/n) * 100;

        System.out.println("Initial percentage: " + initialAveragePercentage );
        System.out.println("New percentage: " + average );
        System.out.println("Percentage increase for " + strategy + ": " + ((average - initialAveragePercentage) )+"%");
    }
}
