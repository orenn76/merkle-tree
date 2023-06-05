package ninyo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MerkleTree {

    public static void main(String[] args) {
        ArrayList<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("Guns and Roses");
        dataBlocks.add("Secret Man");
        dataBlocks.add("Rock and Roll");
        dataBlocks.add("Take a shot");
        Node root = createMerkleTree(dataBlocks);
        printLevelOrderTraversal(root);
    }

    public static Node createMerkleTree(ArrayList<String> dataBlocks) {
        ArrayList<Node> childNodes = new ArrayList<>();
        for (String data : dataBlocks) {
            childNodes.add(new Node(null, null, HashAlgorithm.getCryptoHashMD5(data)));
        }
        return buildTree(childNodes);
    }

    private static Node buildTree(ArrayList<Node> children) {
        //Return the Merkle Root
        if (children.size() == 1) {
            return children.get(0);
        }
        ArrayList<Node> parents = new ArrayList<>();
        //Hash the leaf transaction pair to get parent transaction
        for (int i = 0; i < children.size(); i += 2) {
            if ((i + 1) < children.size()) {
                Node leftChild = children.get(i);
                Node rightChild = children.get(i + 1);
                String parentHash = HashAlgorithm.getCryptoHashMD5(leftChild.getHash().concat(rightChild.getHash()));
                parents.add(new Node(leftChild, rightChild, parentHash));
            }
        }
        // If odd number of transactions , add the last transaction again
        if (children.size() % 2 == 1) {
            Node lastChild = children.get(children.size() - 1);
            String parentHash = HashAlgorithm.getCryptoHashMD5(lastChild.getHash().concat(lastChild.getHash()));
            parents.add(new Node(lastChild, lastChild, parentHash));
        }
        return buildTree(parents);
    }

    private static void printLevelOrderTraversal(Node root) {
        if (root == null) {
            return;
        }
        if ((root.getLeft() == null && root.getRight() == null)) {
            System.out.println(root.getHash());
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                System.out.println(node.getHash());
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }
            if (node != null && node.getLeft() != null) {
                queue.add(node.getLeft());
            }
            if (node != null && node.getRight() != null) {
                queue.add(node.getRight());
            }
        }
    }
}