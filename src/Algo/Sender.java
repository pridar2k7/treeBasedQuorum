package Algo;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by priyadarshini on 3/31/15.
 */
public class Sender {
    PrintWriter sender;

    protected void sendRequest(int channelCount) {
        try {
            sender = new PrintWriter((Nodes.connectedSockets.get(channelCount)).getOutputStream(), true);
            String requestMessage = new StringBuilder().append("REQUEST ")
                    .append(Nodes.sequenceNumber)
                    .append(" ")
                    .append(Nodes.id)
                    .toString();
            sender.println(requestMessage);
            Nodes.sentMessageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send REQUEST");
        }
    }

    protected void sendReply(int channelCount) {
        try {
            sender = new PrintWriter((Nodes.connectedSockets.get(channelCount)).getOutputStream(), true);
            String requestMessage = new StringBuilder().append("REPLY ")
                    .append(Nodes.sequenceNumber)
                    .append(" ")
                    .append(Nodes.id)
                    .toString();
            sender.println(requestMessage);
            Nodes.sentMessageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send REPLY");
        }
    }


    public void sendRelease(int fromNode) {
        try {
            sender = new PrintWriter((Nodes.connectedSockets.get(fromNode)).getOutputStream(), true);
            String requestMessage = new StringBuilder().append("RELEASE ")
                    .append(Nodes.sequenceNumber)
                    .append(" ")
                    .append(Nodes.id)
                    .toString();
            System.out.println("release msg "+ requestMessage);
            sender.println(requestMessage);
            Nodes.sentMessageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send RELEASE");
        }
    }

    public void sendStart(int nodeNumber) {
        try {
            sender = new PrintWriter((Nodes.connectedSockets.get(nodeNumber)).getOutputStream(), true);
            String requestMessage = new StringBuilder().append("START ")
                    .toString();
            System.out.println("start msg "+ requestMessage);
            sender.println(requestMessage);
            Nodes.sentMessageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send RELEASE");
        }
    }

    public void sendComplete(int nodeNumber) {
        try {
            sender = new PrintWriter((Nodes.connectedSockets.get(nodeNumber)).getOutputStream(), true);
            String requestMessage = new StringBuilder().append("COMPLETE ")
                    .append(Nodes.id)
                    .toString();
            System.out.println("Complete msg "+ requestMessage);
            sender.println(requestMessage);
            Nodes.sentMessageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send Complete");
        }
    }
}
