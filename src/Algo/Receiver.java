package Algo;


import javax.xml.soap.Node;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Created by priyadarshini on 3/29/15.
 */
//Algo.Receiver class will run a thread which continuously looks into the blocking linked Q and whenever
//a message come for processing from any of the sockets
public class Receiver extends Thread {
    private ServerTree currentNode;
    private int completeMessageCount = 0;

    public Receiver() {
        start();
    }

    @Override
    public void run() {
        String receivedMessage;
        try {
            while (true) {
                receivedMessage = (String) MessageReader.messageQueue.take();
                Nodes.receivedMessageCount++;

                String[] keyWords = receivedMessage.split(" ");
                if (keyWords[0].equals("REQUEST")) {
                    System.out.println("Request received from node " + keyWords[2] + " with sequence number" + keyWords[1]);
                    receiveRequest(Integer.parseInt(keyWords[2]), Long.parseLong(keyWords[1]));
                } else if (keyWords[0].equals("REPLY")) {
                    System.out.println("Reply received from node " + keyWords[2].trim());
                    receiveReply(Integer.parseInt(keyWords[2]), Long.parseLong(keyWords[1]));
                } else if (keyWords[0].equals("RELEASE")) {
                    System.out.println("Release received from node " + keyWords[2].trim());
                    receiveRelease(Integer.parseInt(keyWords[2]), Long.parseLong(keyWords[1]));
                } else if (keyWords[0].equals("START")) {
                    IssueRequest issueRequest = new IssueRequest();
                } else if (keyWords[0].equals("COMPLETE")){
                    receiveComplete();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Something went wrong in the receiver");
        }
    }

    //if we receive a complete message, we end the process
    private void receiveComplete() {
        completeMessageCount++;
        if(completeMessageCount== Nodes.TOTAL_CLIENTS){
            for (int key = 1; key <= Nodes.TOTAL_CLIENTS; key++) {
                Socket socket = Nodes.connectedSockets.get(key);
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error while closing sockets");
                    e.printStackTrace();
                }
            }
        }
    }

    //if a release is received from x, we see if the node is locked by that node x if so we release it and process next request in queue..
    // if the node is not locked by the node x then if it s already present in the queue then it removes the request as it already entered critical section
    private void receiveRelease(int fromNode, long sequenceNumber) {
        Nodes.state = "Unlock";
        if (fromNode == Nodes.lockedBy) {
            if (!Nodes.nextInLineQueue.isEmpty()) {
                CSRequest nextNodeToBeLocked = (CSRequest) Nodes.nextInLineQueue.poll();
                lockNode(nextNodeToBeLocked.nodeNumber);
            }
        } else {
            Nodes.nextInLineQueue.remove(new CSRequest(fromNode, sequenceNumber));
        }

    }

    //as we get a reply we check if we have got replies from any of the quorums and if so we can enter the critical section
    private void receiveReply(int fromNode, long sequenceNumber) {
        if (Nodes.entryCount <= 20) {
            Nodes.replyList.add(fromNode);
            if (checkQuorumFor(Nodes.rootNode.getNodeNumber())) {
                enterCriticalSection();
                releaseCriticalSection();
                Nodes.entryCount++;
                makeRequest();
            }
        }
    }

    //critical section with 3units of wait time..
    private void enterCriticalSection() {
        try {
            System.out.println("Entered Critical section.. " + new Date() + "  " + System.currentTimeMillis());
            Thread.sleep(3 * Nodes.TIME_UNIT);
            System.out.println("Exited critical section.." + new Date() + "  " + System.currentTimeMillis());
            Nodes.timeEnded=System.currentTimeMillis();
        } catch (Exception e) {
            System.out.println("Something went wrong in the critical section");
        }
    }

    ///after we come out of teh critical section we send release messages to all the servers to notify tat the request has been served..
    public void releaseCriticalSection() {
        Nodes.replyList.clear();
        for (int fromNode = 1; fromNode <= Nodes.TOTAL_SERVERS; fromNode++) {
            new Sender().sendRelease(fromNode);
        }
        Nodes.timeElapsed = Nodes.timeEnded-Nodes.timeStarted;
        System.out.println("Messages exchanged for this entry: " + ((Nodes.sentMessageCount+Nodes.receivedMessageCount)-Nodes.totalMessages));
        Nodes.totalMessages = Nodes.receivedMessageCount + Nodes.sentMessageCount;
        System.out.println("Latency for Node "+Nodes.id + " for entry count "+ Nodes.entryCount +" is : "+ Nodes.timeElapsed);
    }


    //after the critical section entry we need to make the next request calling..
    private void makeRequest() {
        IssueRequest issueRequest = new IssueRequest();
    }

    //every time we receive a request if we are in unlocked state, we should lock ourselves and if we are in lock state we should put it in the queue
    private void receiveRequest(int fromNode, long sequenceNumber) {
        if (!Nodes.state.equals("Locked")) {
            lockNode(fromNode);
        } else {
            Nodes.nextInLineQueue.add(new CSRequest(fromNode, sequenceNumber));
        }
    }

    //we shoudl lock the node
    private void lockNode(int nodeNumber) {
        new Sender().sendReply(nodeNumber);
        Nodes.state = "Locked";
        Nodes.lockedBy = nodeNumber;
        System.out.println("Locked by " + Nodes.lockedBy);
    }

    //function to check if the quorum has been acheieved
    private boolean checkQuorumFor(int nodeNumber) {

        ServerTree currentNode = Nodes.serverMap.get(nodeNumber);
        if (Nodes.replyList.contains(nodeNumber)) {
            if ((currentNode.leftChild != 0) && !checkQuorumFor(currentNode.leftChild)) {
                return currentNode.rightChild != 0 && checkQuorumFor(currentNode.rightChild);
            }
            return true;
        } else {
            if (((currentNode.leftChild != 0) && checkQuorumFor(currentNode.leftChild))
                    && (currentNode.rightChild != 0 && checkQuorumFor(currentNode.rightChild))) {
                return true;
            }
            return false;
        }
    }
}
