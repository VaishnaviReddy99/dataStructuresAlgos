package backup;

import ads.Order;

public class ETANode {
    int eta; // Key value (Estimated Time of Arrival)
    Order order; // Reference to the corresponding order
    ETANode left;
    ETANode right;

    @Override
    public String toString() {
        return "ETANode{" +
                "eta=" + eta +
                ", order=" + order +
                '}';
    }

    int height;

    public ETANode(int eta, Order order) {
        this.eta = eta;
        this.order = order;
        this.height = 1;
    }
}
