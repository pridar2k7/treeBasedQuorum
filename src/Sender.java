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
//            messageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send request");
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
//            messageCount++;
        } catch (IOException e) {
            System.out.println("Something went wrong in send request");
        }
    }



}
