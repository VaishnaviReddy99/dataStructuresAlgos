package backup;

import ads.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAVLTree {
    private OrderAVLNode root;

    OrderAVLNode rotateRight(OrderAVLNode y) {
        OrderAVLNode x = y.left;
        OrderAVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    OrderAVLNode rotateLeft(OrderAVLNode x) {
        OrderAVLNode y = x.right;
        OrderAVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int height(OrderAVLNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int getBalance(OrderAVLNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    private OrderAVLNode insert(OrderAVLNode node , Order order) {
        if (node == null)
            return new OrderAVLNode(order);
        if ( calculatePriority(order) < calculatePriority(node.order))
            node.left = insert(node.left, order);
        else
            node.right = insert(node.right, order);

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        int balance = getBalance(node);

        if (balance > 1 && calculatePriority(order) < calculatePriority(node.left.order ))
            return rotateRight(node);

        if (balance < -1 && calculatePriority(order) > calculatePriority(node.right.order))
            return rotateLeft(node);

        if (balance > 1 &&  calculatePriority(order) > calculatePriority(node.left.order)) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && calculatePriority(order) < calculatePriority(node.right.order)) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }


        return node;
    }




    public void insertOrder(Order order) {
        root = insert(root, order);

    }

    public void deleteNode(Order order){
        root = deleteNode(root,order);
    }

    private OrderAVLNode deleteNode(OrderAVLNode node, Order order) {
        if (node == null)
            return null;

        // If the order to be deleted is less than the current node's order,
        // move to the left subtree
        if (Double.compare(calculatePriority(order),calculatePriority(node.order)) < 0)
            node.left = deleteNode(node.left, order);
            // If the order to be deleted is greater than the current node's order,
            // move to the right subtree
        else if (Double.compare(calculatePriority(order),calculatePriority(node.order))> 0)
            node.right = deleteNode(node.right, order);
            // If the current node's order is equal to the order to be deleted
        else {
            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                OrderAVLNode temp = null;
                if (temp == node.left)
                    temp = node.right;
                else
                    temp = node.left;

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else // One child case
                    node = temp;
            } else {
                // Node with two children: Get the inorder successor (smallest
                // in the right subtree)
                OrderAVLNode temp = minValueNode(node.right);

                // Copy the inorder successor's data to this node
                node.order = temp.order;

                // Delete the inorder successor
                node.right = deleteNode(node.right, temp.order);
            }
        }

        // If the tree had only one node then return
        if (node == null)
            return null;

        // Update the height of the current node
        node.height = Math.max(height(node.left), height(node.right)) + 1;

        // Get the balance factor of this node to check whether this node became unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are four cases

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }



    private OrderAVLNode minValueNode(OrderAVLNode node) {
        OrderAVLNode current = node;

        // Loop down to find the leftmost leaf
        while (current.left != null) {
            current = current.left;
        }

        return current;
    }
    public List<Order> findLessThan(double thresholdPriority) {
        List<Order> results = new ArrayList<>();
        findLessThan(root, thresholdPriority, results);
        return results;
    }

    private void findLessThan(OrderAVLNode node, double thresholdPriority, List<Order> results) {
        if (node == null) {
            return;
        }

        // Process nodes with priority less than the threshold
        if (node.priority < thresholdPriority) {
            results.add(node.order);
        }

        // Recursively traverse the left subtree first (lower priorities)
        findLessThan(node.left, thresholdPriority, results);

        // Then traverse the right subtree
        findLessThan(node.right, thresholdPriority, results);
    }


    OrderAVLNode search(OrderAVLNode root, int orderId) {
        if (root == null || root.order.orderId == orderId)
            return root;

        if (orderId < root.order.orderId)
            return search(root.left, orderId);

        return search(root.right, orderId);
    }

    OrderAVLNode searchOrder(int orderId) {
        return search(root, orderId);
    }


   void inorderTraversal(OrderAVLNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            System.out.println(root.toString());
            inorderTraversal(root.right);
        }
    }




    void printOrders() {
        inorderTraversal(root);
    }

    public double calculatePriority(Order order){
        double valueWeight = 0.3;
        double timeWeight = 0.7;
//        double normalizedWeight = order.orderValue / 50.0;
//        return valueWeight * normalizedWeight - timeWeight * order.currentSystemTime;
        return 0.0;
    }

}