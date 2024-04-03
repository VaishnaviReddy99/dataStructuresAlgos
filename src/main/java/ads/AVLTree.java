package ads;

import java.util.*;


public class AVLTree {
    AVLNode root;

    public AVLTree() {
        this.root = null;
    }
    public void insert(double key,Order value){
        root = insert(root,key,value);

    }

    public AVLNode insert(AVLNode root, double key, Order value) {
        if (root == null) {
            return new AVLNode(key, value);
        } else if (key < root.key) {
            root.left = insert(root.left, key, value);
        } else {
            root.right = insert(root.right, key, value);
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);

        if (balance > 1 && key < root.left.key) {
            return rightRotate(root);
        }

        if (balance < -1 && key > root.right.key) {
            return leftRotate(root);
        }

        if (balance > 1 && key > root.left.key) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && key < root.right.key) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public AVLNode leftRotate(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        y.left = z;
        z.right = T2;

        z.height = 1 + Math.max(getHeight(z.left), getHeight(z.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    public AVLNode rightRotate(AVLNode z) {
        AVLNode y = z.left;
        AVLNode T3 = y.right;

        y.right = z;
        z.left = T3;

        z.height = 1 + Math.max(getHeight(z.left), getHeight(z.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));

        return y;
    }

    public int getHeight(AVLNode root) {
        if (root == null) {
            return 0;
        }
        return root.height;
    }

    public int getBalance(AVLNode root) {
        if (root == null) {
            return 0;
        }
        return getHeight(root.left) - getHeight(root.right);
    }

    public AVLNode search(AVLNode root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        if (key < root.key) {
            return search(root.left, key);
        }
        return search(root.right, key);
    }

    public AVLNode findMin(AVLNode root) {
        if (root == null || root.left == null) {
            return root;
        }
        return findMin(root.left);
    }

    public void delete(double key){
        root = delete(root,key);
    }




    public AVLNode delete(AVLNode root, double key) {
        if (root == null) {
            return root;
        } else if (key < root.key) {
            root.left = delete(root.left, key);
        } else if (key > root.key) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) {
                AVLNode temp = root.right;
                root = null;
                return temp;
            } else if (root.right == null) {
                AVLNode temp = root.left;
                root = null;
                return temp;
            }
            AVLNode temp = findMin(root.right);
            root.key = temp.key;
            root.right = delete(root.right, temp.key);
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public List<Object> preOrder(AVLNode root) {
        List<Object> result = new ArrayList<>();
        if (root != null) {
            result.add(root.order);
            result.addAll(preOrder(root.left));
            result.addAll(preOrder(root.right));
        }
        return result;
    }

    public List<AVLNode> getNodesInRange(double minKey, double maxKey){
        List<AVLNode> nodesInRange = new ArrayList<>();
        getKeysInRangeHelper(root,minKey,maxKey,nodesInRange);
        return nodesInRange;
    }

    private void getKeysInRangeHelper(AVLNode node, double minKey, double maxKey, List<AVLNode> keysInRange) {
        if (node == null) {
            return;
        }
        if (node.key >= minKey) {
            getKeysInRangeHelper(node.left, minKey, maxKey, keysInRange);
        }
        if (node.key >= minKey && node.key <= maxKey) {
            keysInRange.add(node);
        }
        if (node.key <= maxKey) {
            getKeysInRangeHelper(node.right, minKey, maxKey, keysInRange);
        }
    }

    public int getRank(double key) {
        return getRankHelper(root, key, 0);
    }

    private int getRankHelper(AVLNode node, double key, int rank) {
        if (node == null) {
            return 0;
        }
        if (key < node.key) {
            return getRankHelper(node.left, key, rank);
        } else if (key > node.key) {
            int leftSize = (node.left != null) ? node.left.height : 0;
            return getRankHelper(node.right, key, rank + leftSize + 1);
        } else {
            int leftSize = (node.left != null) ? node.left.height : 0;
            return rank + leftSize;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    void inorderTraversal(AVLNode root,Map<Integer,Double> map) {
        if (root != null) {
            inorderTraversal(root.left,map);
            map.put(root.order.orderId,root.key);
            inorderTraversal(root.right,map);
        }
    }

    Map<Integer,Double> getOrderKeyMap() {
        Map<Integer,Double> orderKeyMap = new HashMap<>();
        inorderTraversal(root,orderKeyMap);
        return orderKeyMap;
    }

    public List<AVLNode> findNodesLessThan(double key) {
        List<AVLNode> nodes = new ArrayList<>();
        findNodesLessThanHelper(root, key, nodes);
        return nodes;
    }

    public List<AVLNode> findNodesLessThanOrEquals(double key){
        List<AVLNode> nodes = new ArrayList<>();
        findNodesLessThanOrEqualsHelper(root,key,nodes);
        return nodes;
    }

    public List<AVLNode> findNodesLessThan(double key,boolean reverse) {
        List<AVLNode> nodes = new ArrayList<>();
        findNodesLessThanHelper(root, key, nodes);
        if(reverse){
            Collections.sort(nodes, new Comparator<AVLNode>() {
                @Override
                public int compare(AVLNode o1, AVLNode o2) {
                    return Double.compare(o1.key,o2.key);
                }
            });
        }
        return nodes;
    }


    private void findNodesLessThanHelper(AVLNode node, double key, List<AVLNode> nodes) {
        if (node == null) {
            return;
        }
        findNodesLessThanHelper(node.left, key, nodes);
        if (node.key < key) {
            nodes.add(0,node);
        }
        findNodesLessThanHelper(node.right, key, nodes);
    }

    private void findNodesLessThanOrEqualsHelper(AVLNode node, double key, List<AVLNode> nodes) {
        if (node == null) {
            return;
        }
        findNodesLessThanOrEqualsHelper(node.left, key, nodes);
        if (node.key <= key) {
            nodes.add(0,node);
        }
        findNodesLessThanOrEqualsHelper(node.right, key, nodes);
    }

    public AVLNode findMaxKeyNode(AVLNode root) {
        if (root == null) {
            return null; // If tree is empty
        }
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    public List<AVLNode> deleteAllNodesInOrder() {
        List<AVLNode> deletedNodes = new ArrayList<>();
        deleteAllNodesInOrder(root, deletedNodes);
        root = null; // Set the root to null after deleting all nodes
        return deletedNodes;
    }

    private void deleteAllNodesInOrder(AVLNode node, List<AVLNode> deletedNodes) {
        if (node == null) {
            return;
        }
        deleteAllNodesInOrder(node.left, deletedNodes); // Delete left subtree
        deletedNodes.add(node); // Add the current node to the list of deleted nodes
        deleteAllNodesInOrder(node.right, deletedNodes); // Delete right subtree

    }




}
