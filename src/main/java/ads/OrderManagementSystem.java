package ads;



import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.*;

public class OrderManagementSystem {

    AVLTree etaTree = new AVLTree();
    AVLTree orderAVLTree = new AVLTree();
    int etaStatues = 1;
    int prev_deliveryTime = 0;
    int started_time = 0;
    Map<Integer, AVLNode> etaMap = new HashMap<>();
    Stack<Integer[]> delieveredOrders = new Stack<>();
    String deliveredMessage = "Order %d has been delivered at time %d";

    public String createOrInsertOrder(int orderId, int currentSystemTime, int orderValue, int deliveryTime){
        StringBuilder ouputString = new StringBuilder();
        String deliveredMessage = checkAndDeliverOrders(currentSystemTime);
        Order order = new Order(orderId,currentSystemTime,orderValue,deliveryTime);
        orderAVLTree.insert(order.calculatePriority(),order); //insert order
        boolean reCaculate = true;
        if(currentSystemTime > started_time){
            reCaculate = false;
        }
        //Recalculating etas for lesser priority Orders
        List<AVLNode> orderList = orderAVLTree.findNodesLessThan(order.calculatePriority(),true);
        boolean orderPopped = false;
        boolean useStartTime = false;
//        System.out.println("Order "+orderId+" Priority: "+order.calculatePriority());

        //Deleting all the lesser priority orders
        if(orderList.size() > 0 && reCaculate) {
            orderPopped = true;
            //Updating the etas
            double eta = etaStatues;
            int pd = -1;
            for(AVLNode popOrder : orderList){
                etaTree.delete(etaMap.get(popOrder.order.orderId).key); // Delete from tree
                started_time = (int) (etaMap.get(popOrder.order.orderId).key - popOrder.order.deliveryTime);
            }
            AVLNode etaNode = etaTree.findMaxKeyNode(etaTree.root);
            if (etaNode == null) {
                etaStatues = started_time;
                useStartTime = true;
            } else{
                etaStatues = (int) etaNode.key;
                prev_deliveryTime = (int) etaNode.order.deliveryTime;
            }

        }
        //Now Adding current order to etaTree
        createETANodes(order,useStartTime);
        ouputString.append("Order "+order.orderId+" has been created - ETA: "+etaStatues).append("\n");

        //Re inserting the popped orders
        if(orderPopped){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for(AVLNode popOrder : orderList){
                createETANodes(popOrder.order);
                stringBuilder.append(popOrder.order.orderId+": "+etaStatues+", ");
                etaMap.put(popOrder.order.orderId,new AVLNode(etaStatues,popOrder.order));
            }
            stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length()-1);
            stringBuilder.append("]");
//            System.out.println("Updated ETAs: "+stringBuilder.toString());
            ouputString.append("Updated ETAs: "+stringBuilder.toString()).append("\n");
        }

        ouputString.append(deliveredMessage);

        return ouputString.toString();



    }

    private void createETANodes(Order order,boolean startTime){
       if(etaStatues == 1){
           etaStatues = order.currentSystemTime + order.deliveryTime;
           started_time = order.currentSystemTime;
       }else {
           if (startTime) {
               etaStatues = started_time + order.deliveryTime;
               started_time = etaStatues;
           } else {
               etaStatues = etaStatues + prev_deliveryTime + order.deliveryTime;
               started_time = etaStatues + prev_deliveryTime;
           }
       }
        etaTree.insert(etaStatues,order);
        etaMap.put(order.orderId,new AVLNode(etaStatues,order));
        prev_deliveryTime = order.deliveryTime;
    }

    private void createETANodes(Order order){
        if(etaStatues == 1){
            etaStatues = order.currentSystemTime + order.deliveryTime;
            started_time = order.currentSystemTime;
        }else{
            etaStatues = etaStatues + prev_deliveryTime + order.deliveryTime;
            started_time = etaStatues+prev_deliveryTime;
        }
        etaTree.insert(etaStatues,order);
        etaMap.put(order.orderId,new AVLNode(etaStatues,order));
        prev_deliveryTime = order.deliveryTime;
    }

    private String checkAndDeliverOrders(int currentSystemTime) {
        //Check for any pending deliveries
        StringBuilder str = new StringBuilder();
        List<AVLNode> etaNodes = etaTree.findNodesLessThanOrEquals(currentSystemTime);
        if(etaNodes.size() != 0) {
            for(AVLNode etaNode : etaNodes){
                etaTree.delete(etaNode.key);
                orderAVLTree.delete(etaNode.order.calculatePriority());
                etaMap.remove(etaNode.order.orderId);
                String message = String.format(deliveredMessage,etaNode.order.orderId,(int)etaNode.key);
                str.append(message).append("\n");
                delieveredOrders.push(new Integer[]{etaNode.order.orderId,(int)etaNode.key,etaNode.order.deliveryTime});

            }

        }
        return str.toString();
    }


    public String updateTime(int orderId, int currentSystemTime, int newDelivieryTime){
        StringBuilder outputMessage = new StringBuilder();
        outputMessage.append(checkAndDeliverOrders(currentSystemTime));
        if(!etaMap.containsKey(orderId)){
            outputMessage.append("Cannot update. Order").append(orderId).append(" has already been ").append("delivered.");
            return outputMessage.toString();
        }

        List<Order> ordersToAdd = new ArrayList<>();
        Order prevorder = etaMap.get(orderId).order;

        List<AVLNode> nodesWithLowerPriorities = orderAVLTree.findNodesLessThan(prevorder.calculatePriority(),true);


        orderAVLTree.delete(prevorder.calculatePriority());
        Order updatedOrder = new Order(orderId,currentSystemTime,prevorder.orderValue,newDelivieryTime);
        orderAVLTree.insert(updatedOrder.calculatePriority(),updatedOrder);


        etaTree.delete(etaMap.get(orderId).key);

        for(AVLNode avlNode : nodesWithLowerPriorities){
            int ord_id = avlNode.order.orderId;
            etaTree.delete(etaMap.get(ord_id).key);

            ordersToAdd.add(avlNode.order);
        }

        // Re calculate and add the etas
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Updated ETAs: [");
        AVLNode maxNode =  etaTree.findMaxKeyNode(etaTree.root);
        boolean useStartTime = false;
        if (maxNode == null) {
            started_time = (int) (etaMap.get(orderId).key - (prevorder.deliveryTime));
            useStartTime = true;
        } else{
            etaStatues = (int) maxNode.key;
            prev_deliveryTime = (int) maxNode.order.deliveryTime;
        }

        createETANodes(updatedOrder,useStartTime);
        etaMap.put(orderId,new AVLNode(etaStatues,updatedOrder));
        stringBuilder.append(orderId + ": " + etaStatues + ", ");

        for (Order op: ordersToAdd){
            createETANodes(op);
            etaMap.put(op.orderId,new AVLNode(etaStatues,op));
            stringBuilder.append(op.orderId + ": " + etaStatues + ", ");
        }

        stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length()-1);
        stringBuilder.append("]");
        outputMessage.append(stringBuilder.toString()).append("\n");


        return outputMessage.toString();



    }


    public String cancelOrder(Integer orderId, Integer currentSystemTime) {
        StringBuilder stringBuilder = new StringBuilder();
        if(!etaMap.containsKey(orderId)){
            stringBuilder.append("Cannot cancel. Order ").append(orderId).append(" has already been delivered").append("\n");
            return stringBuilder.toString();
        }
        Order cancelledOrder = etaMap.get(orderId).order;
        List<AVLNode> lowPriorityOrders = orderAVLTree.findNodesLessThan(cancelledOrder.calculatePriority(),true);
        etaTree.delete(etaMap.get(orderId).key);
        orderAVLTree.delete(cancelledOrder.calculatePriority());

        for(AVLNode avlNode: lowPriorityOrders){
            etaTree.delete(etaMap.get(avlNode.order.orderId).key); // Deletes each order
        }

        AVLNode avlNode = etaTree.findMaxKeyNode(etaTree.root);

        if(avlNode == null){
            AVLNode prevNode = null;
            for(Map.Entry<Integer,AVLNode> entry: etaMap.entrySet()){
                if(entry.getKey() == orderId) break;
                prevNode = entry.getValue();
            }

            if(prevNode == null){
                etaStatues = delieveredOrders.peek()[1];
                prev_deliveryTime = delieveredOrders.peek()[2];
            }else {
                etaStatues = (int) prevNode.key;
                prev_deliveryTime = prevNode.order.deliveryTime;
            }

        }else{
            etaStatues = (int) avlNode.key;
            prev_deliveryTime = avlNode.order.deliveryTime;
        }
        StringBuilder opMessage = new StringBuilder();
        stringBuilder = new StringBuilder();
        opMessage.append("Order ").append(orderId).append(" has been cancelled").append("\n");
        for (AVLNode op: lowPriorityOrders){
            createETANodes(op.order);
            stringBuilder.append(op.order.orderId+": "+etaStatues+", ");
            //System.out.println("Order updated "+ op.order.orderId+" eta "+etaStatues);
        }
        if(lowPriorityOrders.size() != 0){
            stringBuilder.insert(0,"[");
            stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length()-1);
            stringBuilder.append("]");
            opMessage.append("Updated ETAs: ").append(stringBuilder.toString()).append("\n");
        }


        //Inserted all nodes
        etaMap.remove(orderId);
        return opMessage.toString();

    }

    public String printOrder(Integer orderId) {
        AVLNode etaNode = etaMap.get(orderId);
        return String.format("[%d, %d, %d, %d, %d]",orderId,etaNode.order.currentSystemTime,etaNode.order.orderValue,
                etaNode.order.deliveryTime,(int)etaNode.key);

    }

    public String printOrdersInRange(Integer startTime, Integer endTime) {
        List<AVLNode> avlNodes = etaTree.getNodesInRange(startTime,endTime);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        if(avlNodes.size() > 0) {
            avlNodes.forEach(node -> {
                stringBuilder.append(node.order.orderId + ", ");
            });
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }else{
            return "There are no orders in that time period";
        }
    }

    public String flushOutAll() {
        List<AVLNode> nodes = etaTree.deleteAllNodesInOrder();
        Collections.sort(nodes, new Comparator<AVLNode>() {
            @Override
            public int compare(AVLNode o1, AVLNode o2) {
                return Double.compare(o1.key,o2.key);
            }
        });
        String res = "";
        StringBuilder stringBuilder = new StringBuilder();
        for(AVLNode avlNode: nodes){
            stringBuilder.append (String.format(deliveredMessage,avlNode.order.orderId,(int)avlNode.key)).append("\n");
        }
        return stringBuilder.toString();
    }

    public String getRank(Integer orderId) {
        if(etaMap.containsKey(orderId)) {
            int rank = etaTree.getRank(etaMap.get(orderId).key);
            return new String("Order " + orderId + " will be delivered after " + rank + " Orders");
        }
        return "";
    }


}
