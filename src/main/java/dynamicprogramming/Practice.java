package dynamicprogramming;
import java.util.Random;

public class Practice {
    public static void main(String[] args) {
        int[] nums = {2,7,11,15};
        int idx = search(nums,0,nums.length-1,-1);
        System.out.println(idx);

    }

    public static int search(int[] nums, int startidx, int endIdx, int num){
        int mid = (startidx+endIdx)/2;
        int res = -1;
        while(startidx <= endIdx ){
            if(nums[mid] == num){
                res = mid;
                break;
            }else if(nums[mid] > num){
                endIdx = mid - 1;
            }else if(nums[mid] < num){
                startidx = mid + 1;
            }
            mid = (startidx+endIdx)/2;
        }
        return res;
    }

    public static void generateRandomIntegers() {
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            int randomNumber = random.nextInt(101);
            while(randomNumber ==  0){
                randomNumber = random.nextInt(100);// Generates a random integer in the range [0, 100]
            }
            System.out.print(randomNumber);

            // Add space after each number, except for the last one
            if (i < 99) {
                System.out.print(" ");
            }
        }
    }
}
