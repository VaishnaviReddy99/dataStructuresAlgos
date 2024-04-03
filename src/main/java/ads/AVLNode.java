package ads;


public class AVLNode {
    double key;
    Order order;
    AVLNode left;
    AVLNode right;
    int height;

    @Override
    public String toString() {
        return "AVLNode{" +
                "key=" + key +
                ", value=" + order.toString() +
                ", height=" + height +
                '}';
    }

    public AVLNode(double key, Order value) {
        this.key = key;
        this.order = value;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
