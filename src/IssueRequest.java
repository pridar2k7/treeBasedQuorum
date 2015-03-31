import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by priyadarshini on 3/29/15.
 */
class IssueRequest extends Thread {
    private Nodes nodes;
    int entryCount;

    public IssueRequest(Nodes nodes) {
        this.nodes=nodes;
        this.entryCount=0;
        start();
    }


    @Override
    public void run() {
        try {
            System.out.println("EntryCount " + entryCount);
//            System.out.println("Total Messages " + server.totalMessages);
            int timeToSleep = 0;
            if (entryCount <= 20) {
                timeToSleep = (int) ((5 + entryCount * Maekawa.UNIT_DIFF) * Maekawa.TIME_UNIT);
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
        for (int nodeNumber = 1; nodeNumber <= Nodes.TOTAL_SERVERS; nodeNumber++) {
            new Sender().sendRequest(nodeNumber);
        }
    }




}
