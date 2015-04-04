package Algo;

/**
 * Created by priyadarshini on 3/29/15.
 */
class IssueRequest extends Thread {
    public static final double UNIT_DIFF = 0.25;

    public IssueRequest() {
        start();
    }


    @Override
    public void run() {
        try {
            int timeToSleep = 0;
            if (Nodes.entryCount <= 20) {
                System.out.println("EntryCount " + Nodes.entryCount);
                timeToSleep = (int) ((5 + Nodes.entryCount * UNIT_DIFF) * Nodes.TIME_UNIT);
                Thread.sleep(timeToSleep);
                requestCriticalSection();
            } else{
                new Sender().sendComplete(1);
                Nodes.totalMessages = Nodes.receivedMessageCount + Nodes.sentMessageCount;
                System.out.println("Computation completed");
                System.out.println("Total sent messages: "+Nodes.sentMessageCount);
                System.out.println("Total received messages: "+Nodes.receivedMessageCount);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void requestCriticalSection() {
        Nodes.timeStarted = System.currentTimeMillis();
        Nodes.replyList.clear();
        for (int nodeNumber = 1; nodeNumber <= Nodes.TOTAL_SERVERS; nodeNumber++) {
            System.out.println("Sending request to " + nodeNumber);
            new Sender().sendRequest(nodeNumber);
        }
    }




}
