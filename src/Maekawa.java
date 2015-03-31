import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by priyadarshini on 3/29/15.
 */
public class Maekawa {

    public static final int TIME_UNIT = 50;
    public static final double UNIT_DIFF = 0.25;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> nodeDetails = Files.readAllLines(Paths.get("resources/thisNode.txt"), StandardCharsets.UTF_8);
        String hostNode = nodeDetails.get(0);
        System.out.println("Host node details " + hostNode);
        String[] hostDetails = hostNode.split(" ");
        Nodes nodes = new Nodes();
        nodes.create(hostDetails);

        Thread.sleep(30000);
        if(hostDetails[0].equals("server")){
            System.out.println("in server");
            Receiver receiver = new Receiver();
        }else if(hostDetails[0].equals("client")){
            System.out.println("in client");
            IssueRequest issueRequest = new IssueRequest(nodes);
        }

        System.out.println("all nodes connected");
    }
}
