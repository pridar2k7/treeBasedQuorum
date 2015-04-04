package trial;

/**
 * Created by priyadarshini on 3/29/15.
 */
public class ServerTree {
    private final int nodeNumber;

    int leftChild, rightChild;
    int parent;
    public ServerTree(int nodeNumber) {
        this.rightChild = 0;
        this.leftChild = 0;
        this.parent = 0;
        this.nodeNumber = nodeNumber;
        identifyTreeNeighbours();
    }

    private void identifyTreeNeighbours() {
        if (2 * nodeNumber <= 7) {
            leftChild = 2 * nodeNumber;
        }
        if (((2 * nodeNumber) + 1) <= 7) {
            rightChild = (2 * nodeNumber) + 1;
        }
        parent = (int) Math.floor(nodeNumber / 2);
    }

    public int getNodeNumber() {
        return nodeNumber;
    }
}
