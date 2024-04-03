package ads;

import java.io.*;
import java.util.Scanner;

public class MainRunner {
    private static String createOrder = "createOrder";
    private static String updateTime = "updateTime";
    private static String print = "print";
    private static String cancelOrder = "cancelOrder";
    private static String getRank = "getRankOfOrder";

    public static void main(String[] args) throws IOException {
        String inputFileName = args[0];
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        OrderManagementSystem orderManagementSystem = new OrderManagementSystem();
        StringBuilder outputFileContent = new StringBuilder();
        int count = 0;
        while (scanner.hasNextLine()){
//            System.out.println("======================================================================");
            String command = scanner.nextLine().trim();
            if(command.startsWith(createOrder)){
                count++;
                String[] node_args = command.substring(createOrder.length()+1,command.length()-1).split(",");
                Integer orderId = Integer.parseInt(node_args[0].trim());
                Integer currentSystemTime = Integer.parseInt(node_args[1].trim());
                Integer orderValue = Integer.parseInt(node_args[2].trim());
                Integer deliveryTime = Integer.parseInt(node_args[3].trim());
                outputFileContent.append(orderManagementSystem.createOrInsertOrder(orderId,currentSystemTime,orderValue,deliveryTime));

                //avlTree.printOrders();

            }else if(command.startsWith(print)){
                String[] node_args = command.substring(print.length()+1,command.length()-1).split(",");
                if(node_args.length == 1){
                    Integer orderId = Integer.parseInt(node_args[0].trim());
                    outputFileContent.append(orderManagementSystem.printOrder(orderId)).append("\n");
                }else{
                    Integer startTime = Integer.parseInt(node_args[0].trim());
                    Integer endTime= Integer.parseInt(node_args[1].trim());
                    outputFileContent.append(orderManagementSystem.printOrdersInRange(startTime,endTime)).append("\n");
                }


            }else if(command.startsWith(updateTime)){
                String[] node_args = command.substring(updateTime.length()+1,command.length()-1).split(",");
                Integer orderId = Integer.parseInt(node_args[0].trim());
                Integer currentSystemTime = Integer.parseInt(node_args[1].trim());
                Integer updatedDeliveryTIme = Integer.parseInt(node_args[2].trim());
                outputFileContent.append(orderManagementSystem.updateTime(orderId,currentSystemTime,updatedDeliveryTIme));

            }else if(command.startsWith(cancelOrder)){
                String[] node_args = command.substring(cancelOrder.length()+1,command.length()-1).split(", ");
                Integer orderId = Integer.parseInt(node_args[0]);
                Integer currentSystemTime = Integer.parseInt(node_args[1]);
                outputFileContent.append(orderManagementSystem.cancelOrder(orderId,currentSystemTime));
            }else if(command.startsWith(getRank)){
                String[] node_args = command.substring(getRank.length()+1,command.length()-1).split(",");
                Integer orderId = Integer.parseInt(node_args[0].trim());
                outputFileContent.append(orderManagementSystem.getRank(orderId)).append("\n");
            }else if(command.startsWith("Quit")){
                outputFileContent.append(orderManagementSystem.flushOutAll());
            }
        }

        System.out.println(inputFileName);

        String ouputFileName = inputFileName.split(".")[0]+"_"+"output_file.txt";
        FileWriter fileWriter = new FileWriter(ouputFileName);

        // Wrap the FileWriter in a BufferedWriter for efficient writing
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // Write the content to the file
        bufferedWriter.write(String.valueOf(outputFileContent));

        // Close the writer
        bufferedWriter.close();

        //System.out.println(outputFileContent);
    }
}
