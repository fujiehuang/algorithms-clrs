package _12_BinarySearchTrees;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class BinarySearchTree<T>
{
    private static class Node<T>
    {
        private T key;
        private Node<T> left;
        private Node<T> right;

        public Node(T key) {
            this.key = key;
        }
    }

    private Node<T> root;
    private Comparator<T> comp;
    private int size = 0;

    public BinarySearchTree(Comparator<T> comp) {
        this.comp = comp;
    }

    public void insert(T key) {
        if (contains(key))
            return;

        root = insert(root, key);
        size++;
    }

    private Node<T> insert(Node<T> n, T key) {
        if (n == null)
            return new Node<>(key);

        if (comp.compare(key, n.key) < 0) {
            n.left = insert(n.left, key);
        } else {
            n.right = insert(n.right, key);
        }

        return n;
    }

    public void remove(T key) {
        if (!contains(key))
            return;

        root = remove(root, key);
        size--;
    }

    private Node<T> remove(Node<T> n, T key) {
        if (comp.compare(n.key, key) == 0) {
            if (n.left == null)
                return n.right;
            else if (n.right == null)
                return n.left;
            else {
                T min = minimum(n.right).key;
                n.key = min;
                n.right = remove(n.right, min);
            }
        } else if (comp.compare(key, n.key) < 0) {
            n.left = remove(n.left, key);
        } else {
            n.right = remove(n.right, key);
        }

        return n;
    }

    public boolean contains(T key) {
        return search(key) != null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public T minimum() {
        return isEmpty() ? null : minimum(root).key;
    }

    private Node<T> minimum(Node<T> n) {
        Node<T> t = n;
        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    public T maximum() {
        return isEmpty() ? null : maximum(root).key;
    }

    private Node<T> maximum(Node<T> n) {
        Node<T> t = n;
        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    public T successor(T key) {
        Node<T> n = successor(root, key);
        return n == null ? null : n.key;
    }

    private Node<T> successor(Node<T> n, T key) {
        if (n == null)
            return null;

        if (n.key == key) {
            if (n.right != null)
                return minimum(n.right);
            return null;
        } else if (comp.compare(key, n.key) < 0) {
            Node<T> t = successor(n.left, key);
            return t == null ? n : t;
        } else {
            return successor(n.right, key);
        }
    }

    public T predecessor(T key) {
        Node<T> n = predecessor(root, key);
        return n == null ? null : n.key;
    }

    private Node<T> predecessor(Node<T> n, T key) {
        if (n == null)
            return null;

        if (comp.compare(n.key, key) == 0) {
            if (n.left != null)
                return maximum(n.left);
            return null;
        } else if (comp.compare(key, n.key) > 0) {
            Node<T> t = predecessor(n.right, key);
            return t == null ? n : t;
        } else {
            return predecessor(n.left, key);
        }
    }

    private Node<T> search(T key) {
        Node<T> n = root;
        while (n != null && comp.compare(n.key, key) != 0) {
            if (comp.compare(key, n.key) < 0)
                n = n.left;
            else
                n = n.right;
        }
        return n;
    }

    public List<T> preorderWalk() {
        List<T> l = new LinkedList<>();
        preorderWalk(root, l);
        return l;
    }

    private void preorderWalk(Node<T> n, List<T> l) {
        if (n == null)
            return;
        l.add(n.key);
        preorderWalk(n.left, l);
        preorderWalk(n.right, l);
    }

    public List<T> inOrderWalk() {
        List<T> l = new LinkedList<>();
        inOrderWalk(root, l);
        return l;
    }

    private void inOrderWalk(Node<T> n, List<T> l) {
        if (n == null)
            return;
        inOrderWalk(n.left, l);
        l.add(n.key);
        inOrderWalk(n.right, l);
    }

    public List<T> postOrderWalk() {
        List<T> l = new LinkedList<>();
        postOrderWalk(root, l);
        return l;
    }

    private void postOrderWalk(Node<T> n, List<T> l) {
        if (n == null)
            return;
        postOrderWalk(n.left, l);
        postOrderWalk(n.right, l);
        l.add(n.key);
    }

    private static class Example
    {
        public static void main(String[] args) {
            BinarySearchTree<Integer> bst = new BinarySearchTree<>(Integer::compareTo);
            Integer[] nodes = {10, 6, 13, 4, 8, 11, 14, 5, 7, 9, 12, 15};
            for (Integer i : nodes)
                bst.insert(i);

            System.out.println("Preorder walk: " + bst.preorderWalk());
            System.out.println("Inorder walk: " + bst.inOrderWalk());
            System.out.println("Postorder walk: " + bst.postOrderWalk());

            System.out.println("Successor of 5: " + bst.successor(5));
            System.out.println("Predecessor of 6: " + bst.predecessor(6));
            System.out.println("Successor of 10: " + bst.successor(10));
            System.out.println("Predecessor of 11: " + bst.predecessor(11));

            System.out.println("\nTree min: " + bst.minimum());
            System.out.println("Tree max: " + bst.maximum());

            System.out.println("\nContains 10? - " + bst.contains(10));
            System.out.println("Contains 2? - " + bst.contains(2));
            System.out.println("Size: " + bst.size());

            System.out.println("\nRemoving 10 and 8...");
            bst.remove(10);
            bst.remove(8);

            System.out.println("\nContains 10? - " + bst.contains(10));
            System.out.println("Inorder walk: " + bst.inOrderWalk());
            System.out.println("Size: " + bst.size());
        }
    }
}
