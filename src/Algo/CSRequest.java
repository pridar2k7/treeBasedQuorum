package Algo;

/**
 * Created by priyadarshini on 4/1/15.
 */
public class CSRequest {
    int nodeNumber;
    int sequenceNumber;

    public CSRequest(int fromNode, int sequenceNumber) {
        nodeNumber = fromNode;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CSRequest csRequest = (CSRequest) o;

        if (nodeNumber != csRequest.nodeNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeNumber;
        result = 31 * result + sequenceNumber;
        return result;
    }
}
