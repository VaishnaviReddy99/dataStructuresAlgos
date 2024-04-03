package greedy;

import java.util.*;
public class sample{
    public static void main(String[] args){
//        int[] dist = {1,3,4};
//        int[] speed = {1,1,1};
//        System.out.print(eliminateMaximum(dist,speed));
        countHomogenous("abbcccaa");
    }
    public static int maxOperations(int[] nums, int k) {
        String indexes = "";
        Arrays.sort(nums);
        List<Integer> arrList = new ArrayList<>();
        for(int i : nums) arrList.add(i);
        int count = 0;
        int len = nums.length;
        
        for(int i = 0;i<len;i++){
            int num = arrList.get(i);
            int ind = findNum(arrList,k-num,i+1,arrList.size()-1);
            if(ind > i ){
                count = count +1;
                arrList.remove(ind);
                arrList.remove(i);
            }
        }
        return count;
    }
    public static int findNum(List<Integer> arrList, int k, int low,int high){
        while(low <= high){
            int mid = low  + ((high - low) / 2);
            if(arrList.get(mid) < k)
                low  = mid + 1;
            else if(arrList.get(mid) > k)
                high = mid - 1;
            else if(arrList.get(mid)== k)
                return mid;
        }
        return -1;
    }

    public static int getWinner(int[] arr, int k) {
        List<Integer> arrlist = new ArrayList<>();
        for(int i : arr){
            arrlist.add(i);
        }
        int maxp = -1;
        int max_so_far = 0;
        int num = -1;
        while(true){
            if(max_so_far == k){
                maxp = num;
                break;
            }
            if(arrlist.get(0) > arrlist.get(1) ){
                num = arrlist.get(0);
                arrlist.add(arrlist.remove(1));
                max_so_far = max_so_far + 1;
            }else{
                max_so_far = 1;
                num = arrlist.get(1);
                arrlist.add(arrlist.remove(0));
            }

        }
        return maxp;
    }

    public static int eliminateMaximum(int[] dist, int[] speed) {
        int n = dist.length;
        double[] time = new double[n];
        for(int i = 0;i<n;i++){
            time[i] =(double) dist[i]/(double) speed[i];
        }

        Arrays.sort(time);
        int count = 0;
        for(int i = 0;i<n;i++){
            if(time[i] > i){
                count = count + 1;
            }else{
                break;
            }

        }

        return count;
    }

    public static String simplifyPath(String path) {
        String res = "";
        String[] strs = path.split("/");
        int i = 0;
        int len = strs.length;
        Stack<String> cann = new Stack<>();
        while(i < len){
            if(!strs[i].isEmpty()){
                if(strs[i].equals(".")){

                }else if(strs[i].equals("..") ){
                    if(!cann.isEmpty()){
                        cann.pop();
                    }

                }else{
                    cann.push(strs[i]);
                }

            }
            i++;
        }
        if(cann.isEmpty()) return "/";
        for(String str: cann){
            res = res +"/"+str;
        }
        return res;


    }

    public static int countHomogenous(String s) {
        List<String> cons = new ArrayList<>();
        String r = ""+s.charAt(0);
        for(int i = 1;i<s.length();i++){
            if(s.charAt(i) == s.charAt(i-1)){
                r = r + s.charAt(i);
            }else{
                cons.add(r);
                r = ""+s.charAt(i);
            }
        }
        cons.add(r);
        return -1;
    }
}