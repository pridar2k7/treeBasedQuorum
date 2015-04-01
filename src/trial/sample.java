package trial;

import java.util.*;

/**
 * Created by priyadarshini on 3/31/15.
 */
public class sample {
    private List replyList;
    private Map<Integer, ServerTree> serverMap;
    private ServerTree rootNode;

    public sample() {
        this.replyList = new ArrayList();
        this.serverMap =new HashMap<Integer, ServerTree>();
    }

    public static void main(String[] args) {
//        sample sample = new sample();
//        sample.replyList.add(4);
//        sample.replyList.add(7);
//        sample.replyList.add(5);
//        sample.replyList.add(3);
//        sample.createServerTree();
//        System.out.println("final" + sample.checkQuorumFor(1));
        PriorityQueue<CSRequest> queue= new PriorityQueue(10, new QueueComparator());
        queue.add(new CSRequest(5, 0));
        queue.add(new CSRequest(4, 0));
        queue.add(new CSRequest(2, 0));
        queue.add(new CSRequest(1, 0));
        queue.add(new CSRequest(3, 0));
        queue.add(new CSRequest(5, 4));

//        PriorityQueue<Integer> queue= new PriorityQueue(10, new IntCOmparator());
//        queue.add(1);
//        queue.add(5);
//        queue.add(2);
//        queue.add(6);
//        queue.add(5);
printQueue(queue);
        while (queue.size() != 0) {
            CSRequest remove = queue.poll();
            System.out.println(remove.nodeNumber + " " +remove.sequenceNumber);
        }
    }

    private static void printQueue(PriorityQueue<CSRequest> queue) {
        int i=0;
        Iterator<CSRequest> iterator = queue.iterator();
        while(iterator.hasNext()){
            CSRequest next = iterator.next();
            System.out.println("next value" + next.nodeNumber + " " + next.sequenceNumber);
        }
        for (CSRequest csRequest : queue) {
            System.out.println("csRequest value" + csRequest.nodeNumber + " " + csRequest.sequenceNumber);
        }
//        while(i<queue.size()){
//            CSRequest peek = queue.poll();
//            System.out.println("peek value" + peek.nodeNumber + " " + peek.sequenceNumber);
//            i+=1;
//        }
    }

    private boolean checkQuorumFor(int nodeNumber) {

        System.out.println("entering CQF "+nodeNumber);
//        for (Integer replyNode : replyList) {
        ServerTree currentNode = serverMap.get(nodeNumber);
        if (replyList.contains(nodeNumber)) {
                if ((currentNode.leftChild!=0) && !checkQuorumFor(currentNode.leftChild)) {
                    System.out.println("left child not in quorum" + currentNode.leftChild);
                    return currentNode.rightChild!=0 && checkQuorumFor(currentNode.rightChild);
                }
                    System.out.println("left child in quorum" +currentNode.leftChild);
                    return true;
            } else {
            System.out.println("no root node "+ currentNode.leftChild+" "+currentNode.rightChild);
            if (((currentNode.leftChild != 0) && checkQuorumFor(currentNode.leftChild))
                    && (currentNode.rightChild != 0 && checkQuorumFor(currentNode.rightChild))) {

                System.out.println("returning true");
                return true;
            }
            System.out.println("going to return");
            return false;
        }
//        }
//        }
//        else{
//            return false;
//        }
    }


    private void createServerTree() {
        for (int nodeNumber = 1; nodeNumber <= 7; nodeNumber++) {
            ServerTree serverTree = new ServerTree(nodeNumber);
            if(rootNode==null){
                rootNode= serverTree;
            }
            serverMap.put(nodeNumber, serverTree);
        }
    }
}
