package backup;

import ads.Order;

public class OrderAVLNode {
    Order order;
    double priority;
    int height = 1;
    OrderAVLNode left,right;


    OrderAVLNode(Order order){
        this.order = order;
        this.left = null;
        this.right = null;
        this.priority = calculatePriority(order);
        this.height = 1;

    }

    @Override
    public String toString() {
        return "AVLNode{" +
                "order=" + order +
                ", priority=" + priority +
                ", height=" + height +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public double calculatePriority(Order order){
        double valueWeight = 0.3;
        double timeWeight = 0.7;
        double normalizedWeight = order.orderValue / 50.0;
        return valueWeight * normalizedWeight - timeWeight * order.currentSystemTime;
    }


}
