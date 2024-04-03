package backup;

import ads.Order;

public class ETATree {
    private ETANode root;

    // Method to get the height of a node
    private int height(ETANode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    // Method to get the balance factor of a node
    private int getBalance(ETANode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    // Method to perform right rotation
    private ETANode rightRotate(ETANode y) {
        ETANode x = y.left;
        ETANode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Method to perform left rotation
    private ETANode leftRotate(ETANode x) {
        ETANode y = x.right;
        ETANode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public void insertNode(Order order, int eta){
        ETANode node = null;
        if(root == null){
            node = insert(null,eta,order,null);
            root = node;
        }else{
            root = insert(root,eta,order,null);
        }

    }

    // Method to insert a node into the AVL tree
    private ETANode insert(ETANode node, int eta, Order order, ETANode prevNode) {
        if (node == null) {
            return new ETANode(eta, order);
        }

        if (eta < node.eta)
            node.left = insert(node.left, eta, order,node.left);
        else if (eta > node.eta)
            node.right = insert(node.right, eta, order,node.right);
        else // Duplicate keys not allowed
            return node;

        // Update height of current node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Get the balance factor of this node to check for rotation
        int balance = getBalance(node);

        // If the node becomes unbalanced, perform rotations
        if (balance > 1 && eta < node.left.eta)
            return rightRotate(node);

        if (balance < -1 && eta > node.right.eta)
            return leftRotate(node);

        if (balance > 1 && eta > node.left.eta) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && eta < node.right.eta) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }



    // Method to find the node with the minimum ETA
    private ETANode minValueNode(ETANode node) {
        ETANode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public void deleteNode(int eta){
        deleteNode(root,eta);
    }
    // Method to delete a node from the AVL tree
    private ETANode deleteNode(ETANode root, int eta) {
        if (root == null)
            return root;

        if (eta < root.eta)
            root.left = deleteNode(root.left, eta);
        else if (eta > root.eta)
            root.right = deleteNode(root.right, eta);
        else {
            if ((root.left == null) || (root.right == null)) {
                ETANode temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                ETANode temp = minValueNode(root.right);
                root.eta = temp.eta;
                root.right = deleteNode(root.right, temp.eta);
            }
        }

        if (root == null)
            return root;

        root.height  = Math.max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public ETANode findMaxEta() {
        ETANode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    public ETANode findNodeLessThan(int key) {
        return findNodeLessThan(root, key, null);
    }

    private ETANode findNodeLessThan(ETANode node, int key, ETANode prev) {
        if (node == null) {
            return prev;
        }

        prev = findNodeLessThan(node.left, key, prev);

        if (node.eta < key) {
            prev = node; // Update the last visited node with key less than the given key
        } else {
            return prev;
        }

        // Traverse right subtree
        return findNodeLessThan(node.right, key, prev);
    }

    public void printETAs(){
        inorderTraversal(root);
    }

    void inorderTraversal(ETANode root) {
        if (root != null) {
            inorderTraversal(root.left);
            System.out.println("ETA "+root.eta+"  " +root.order.toString());
            inorderTraversal(root.right);
        }
    }
}
