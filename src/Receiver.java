import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

/**
 * Created by priyadarshini on 3/29/15.
 */
//Receiver class will run a thread which continuously looks into the blocking linked Q and whenever
//a message come for processing from any of the sockets
public class Receiver extends Thread {
    private TreeSet<Integer> replyList;
    private ServerTree currentNode;

    public Receiver() {
        replyList=new TreeSet<Integer>();
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
                System.out.println(receivedMessage);
                String[] keyWords = receivedMessage.split(" ");
                if (keyWords[0].equals("REQUEST")) {
                    System.out.println("Request received from node " + keyWords[2] + "with sequence number" + keyWords[1]);
                    receiveRequest(Integer.parseInt(keyWords[2]));
                } else if (keyWords[0].equals("REPLY")) {
                    System.out.println("Reply received from node " + keyWords[2].trim());
                    receiveReply(Integer.parseInt(keyWords[2]));
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Something went wrong in the receiver");
        }
    }

    private void receiveReply(int fromNode) {
        replyList.add(fromNode);
        checkQuorumFor(Nodes.rootNode.getNodeNumber());
    }

    private void receiveRequest(int fromNode) {
        if(Nodes.nextInLineQueue.isEmpty()){
            new Sender().sendReply(fromNode);
            Nodes.state="Locked";
            Nodes.lockedBy=fromNode;
        } else{
            Nodes.nextInLineQueue.add(Nodes.serverMap.get(fromNode));
        }
    }

    private boolean checkQuorumFor(int nodeNumber) {

//        for (Integer replyNode : replyList) {
            if(replyList.contains(nodeNumber)){
                ServerTree currentNode = Nodes.serverMap.get(nodeNumber);
                if(!checkQuorumFor(currentNode.leftChild)){
                    return checkQuorumFor(currentNode.rightChild);
                } else{
                    return false;
                }
            } else {
                return false;
            }
//        }
    }
}
