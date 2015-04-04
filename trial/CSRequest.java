package trial;

/**
 * Created by priyadarshini on 4/1/15.
 */
public class CSRequest {
    int nodeNumber;
    int sequenceNumber;

    public CSRequest(int fromNode, int sequenceNumber) {
        this.nodeNumber = fromNode;
        this.sequenceNumber = sequenceNumber;
    }
}
