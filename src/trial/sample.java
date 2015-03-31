package trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sample sample = new sample();
        sample.replyList.add(4);
        sample.replyList.add(7);
        sample.replyList.add(5);
        sample.replyList.add(3);
        sample.createServerTree();
        System.out.println("final" + sample.checkQuorumFor(1));
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
