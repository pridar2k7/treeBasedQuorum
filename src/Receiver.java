import org.omg.PortableInterceptor.INACTIVE;
import trial.*;

import javax.xml.soap.Node;
import java.util.*;

/**
 * Created by priyadarshini on 3/29/15.
 */
//Receiver class will run a thread which continuously looks into the blocking linked Q and whenever
//a message come for processing from any of the sockets
public class Receiver extends Thread {
    private ServerTree currentNode;

    public Receiver() {
        start();
    }

    @Override
    public void run() {
        System.out.println("enteringggg");
        String receivedMessage;
        try {
            System.out.println("in receiver");
            while (true) {
                receivedMessage = (String) MessageReader.messageQueue.take();
                System.out.println("received msg" + receivedMessage);
                System.out.println("Nodes replylist" + Nodes.replyList.toString());

                String[] keyWords = receivedMessage.split(" ");
                if (keyWords[0].equals("REQUEST")) {
                    System.out.println("Request received from node " + keyWords[2] + "with sequence number" + keyWords[1]);
                    receiveRequest(Integer.parseInt(keyWords[2]), Integer.parseInt(keyWords[1]));
                } else if (keyWords[0].equals("REPLY")) {
                    System.out.println("Reply received from node " + keyWords[2].trim());
                    receiveReply(Integer.parseInt(keyWords[2]), Integer.parseInt(keyWords[1]));
                } else if (keyWords[0].equals("RELEASE")) {
                    System.out.println("Release received from node " + keyWords[2].trim());
                    receiveRelease(Integer.parseInt(keyWords[2]), Integer.parseInt(keyWords[1]));
                } else if (keyWords[0].equals("START")) {
                    IssueRequest issueRequest = new IssueRequest();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Something went wrong in the receiver");
        }
    }

    private void receiveRelease(int fromNode, int sequenceNumber) {
        Nodes.state = "Unlock";
        if (fromNode == Nodes.lockedBy) {
            if (!Nodes.nextInLineQueue.isEmpty()) {
                CSRequest nextNodeToBeLocked = (CSRequest) Nodes.nextInLineQueue.poll();
                lockNode(nextNodeToBeLocked.nodeNumber);
            }
        } else {
            System.out.println("size before remove" + Nodes.nextInLineQueue.size());
            Nodes.nextInLineQueue.remove(new CSRequest(fromNode, sequenceNumber));
            System.out.println("size after remove" + Nodes.nextInLineQueue.size());
        }

    }

    private void receiveReply(int fromNode, int sequenceNumber) {
        if (Nodes.entryCount <= 20) {
            Nodes.sequenceNumber = Math.max(Nodes.sequenceNumber, sequenceNumber + 1);
            Nodes.replyList.add(fromNode);
            if (checkQuorumFor(Nodes.rootNode.getNodeNumber())) {
//            System.out.println("Valid quorum " + fromNode);
                enterCriticalSection();
                releaseCriticalSection();
                makeRequest();
            }

        }
    }


    private void enterCriticalSection() {
        try {
//            inCriticalSection = true;
            Nodes.entryCount++;
            System.out.println("Entered Critical section.. " + new Date() + "  " + System.currentTimeMillis());
            Thread.sleep(3 * Nodes.TIME_UNIT);
            System.out.println("Exited critical section.." + new Date() + "  " + System.currentTimeMillis());
//            timeEnded=System.currentTimeMillis();
        } catch (Exception e) {
            System.out.println("Something went wrong in the critical section");
        }
    }

    public void releaseCriticalSection() {
        Nodes.replyList.clear();
        for (int fromNode = 1; fromNode <= Nodes.TOTAL_SERVERS; fromNode++) {
            new Sender().sendRelease(fromNode);
        }
//        timeElapsed = timeEnded-timeStarted;
//        totalMessages+=messageCount;
//        System.out.println("No. Of messages for Node : "+thisNode.id + " is : " + messageCount + ", Time Elapsed: "+timeElapsed);
//        if(minMessages > messageCount){
//            minMessages = messageCount;
//        }
//        if(maxMessages < messageCount){
//            maxMessages = messageCount;
//        }
//        criticalSectionRequested = false;
//        inCriticalSection = false;
    }


    private void makeRequest() {
        IssueRequest issueRequest = new IssueRequest();
    }

    private void receiveRequest(int fromNode, int sequenceNumber) {
        Nodes.sequenceNumber = Math.max(Nodes.sequenceNumber, sequenceNumber + 1);
        System.out.println("Received request");
        if (!Nodes.state.equals("Locked")) {
            System.out.println("if");
            lockNode(fromNode);
        } else {
            System.out.println("else");
            Nodes.nextInLineQueue.add(new CSRequest(fromNode, sequenceNumber));
        }
    }

    private void lockNode(int nodeNumber) {
        new Sender().sendReply(nodeNumber);
        Nodes.state = "Locked";
        Nodes.lockedBy = nodeNumber;
        System.out.println("locked by " + Nodes.lockedBy);
    }

    private boolean checkQuorumFor(int nodeNumber) {

//        System.out.println("entering CQF "+nodeNumber);
//        for (Integer replyNode : replyList) {
        ServerTree currentNode = Nodes.serverMap.get(nodeNumber);
        if (Nodes.replyList.contains(nodeNumber)) {
            if ((currentNode.leftChild != 0) && !checkQuorumFor(currentNode.leftChild)) {
//                System.out.println("left child not in quorum" + currentNode.leftChild);
                return currentNode.rightChild != 0 && checkQuorumFor(currentNode.rightChild);
            }
//            System.out.println("left child in quorum" +currentNode.leftChild);
            return true;
        } else {
//            System.out.println("no root node "+ currentNode.leftChild+" "+currentNode.rightChild);
            if (((currentNode.leftChild != 0) && checkQuorumFor(currentNode.leftChild))
                    && (currentNode.rightChild != 0 && checkQuorumFor(currentNode.rightChild))) {

//                System.out.println("returning true");
                return true;
            }
//            System.out.println("going to return");
            return false;
        }
    }
}
