package greedy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomTester {

    public static void main(String args[]) {
        int n = 1000;
        int k = 2000;

        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("numbers.txt"))) {
            //writer.write(n + " " );
            //writer.newLine();

            for (int i = 0; i < n; i++) {
                int a = random.nextInt(100) + 1; // Random number between 1 and k
                while(a == 0) a = random.nextInt();
                writer.write(a + " ");
                //writer.newLine();
            }

            System.out.println("Data written to number.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
