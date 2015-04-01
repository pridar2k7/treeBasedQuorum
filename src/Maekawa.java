import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by priyadarshini on 3/29/15.
 */
public class Maekawa {

    public static final double UNIT_DIFF = 0.25;

    public static void main(String[] args) throws Exception{
        List<String> nodeDetails = Files.readAllLines(Paths.get("resources/thisNode.txt"), StandardCharsets.UTF_8);
        String hostNode = nodeDetails.get(0);
        System.out.println("Host node details " + hostNode);
        String[] hostDetails = hostNode.split(" ");
        Nodes nodes = new Nodes();
        nodes.create(hostDetails);


//        Thread.sleep(120000);
        if(hostDetails[0].equals("server")){
            while(Nodes.connectedSockets.size()!=5) {
                System.out.println(" connected sockets " + Nodes.connectedSockets.size());
                Thread.sleep(2000);
            }
            if(Nodes.id==1&&Nodes.entryCount==0){
                for (int nodeNumber = 1; nodeNumber <= 5; nodeNumber++) {
                    new Sender().sendStart(nodeNumber);
                }
            }
            System.out.println("in server");
            Receiver receiver = new Receiver();
        }else if(hostDetails[0].equals("client")){
                System.out.println("in client");
                Receiver receiver = new Receiver();
        }
        System.out.println("all nodes connected");
    }
}
