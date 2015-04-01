import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by priyadarshini on 3/29/15.
 */
class IssueRequest extends Thread {

    public IssueRequest() {
        start();
    }


    @Override
    public void run() {
        try {
            System.out.println("EntryCount " + Nodes.entryCount);
//            System.out.println("Total Messages " + server.totalMessages);
            int timeToSleep = 0;
            if (Nodes.entryCount <= 20) {
                timeToSleep = (int) ((5 + Nodes.entryCount * Maekawa.UNIT_DIFF) * Nodes.TIME_UNIT);
                System.out.println(timeToSleep);
                Thread.sleep(timeToSleep);
                requestCriticalSection();
//                server.requestCriticalSection();
//                server.criticalSectionRequested = true;
            }
//            else if (entryCount <= 40) {
//                if (server.isNodeOdd) {
//                    timeToSleep = (int) ((10 + (entryCount * server.unitDiff)) * server.TIME_UNIT);
//                    Thread.sleep(timeToSleep);
//                    server.requestCriticalSection();
//                } else {
//                    timeToSleep = (int) ((40 + ((entryCount % 20) * server.unitDiff)) * server.TIME_UNIT);
//                    Thread.sleep(timeToSleep);
//                    server.requestCriticalSection();
//                }
//                server.criticalSectionRequested = true;
//            }else {
//                System.out.println("Total messages: "+server.totalMessages);
//                System.out.println("Maximum messages: " + server.maxMessages + " Minimum messages: " + server.minMessages);
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void requestCriticalSection() {
        Nodes.replyList.clear();
        for (int nodeNumber = 1; nodeNumber <= Nodes.TOTAL_SERVERS; nodeNumber++) {
            System.out.println("Sending request to " + nodeNumber);
            new Sender().sendRequest(nodeNumber);
        }
    }




}
