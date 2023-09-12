package chatlive.flooding;

import java.util.HashMap;
import java.util.Map;

import chatlive.models.XmppClient;

public class Flooding {
    private Map<Node, Boolean> visited;
    private Node startNode;
    private XmppClient client;
    private String mainUser;
    private int count;

    public Flooding(Node startNode, String message, XmppClient pClient){
        visited = new HashMap<Node, Boolean>();
        this.startNode = null;
        this.client = pClient;
        count = 0;
        algorithmFlooding(startNode, message);
    }

    private void algorithmFlooding(Node node, String message){
        
        if (visited.containsKey(node) && visited.get(node)) return;        
        
        if(startNode == null){
            startNode = node;
            System.out.println("Node messenger: " + node.getLabel() + " - " + node.getName());
            mainUser = node.getName();
        }
        else{
            System.out.println("Node " + node.getLabel() + " - " + node.getName() + " received message: " + message);
            // ! SEND message to the users
            try {
                client.createChat(node.getName());

                String jsonMessage = "{\n" + //
                        "    \"type\": \"message\",\n" + //
                        "    \"headers\": {\n" + //
                        "        \"from\": \"" + mainUser + "\",\n" + //
                        "        \"to\": \"" + node.getName() + "\",\n" + //
                        "        \"hop_count\": \"" + count + "\"\n" + //
                        "    },\n" + //
                        "    \"payload\": \"" + message + "\"\n" + //
                        "}";    
                
                if(!mainUser.equals(node.getName()))
                {
                    count++;
                    client.sendMessage(jsonMessage, false);                
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        visited.put(node, true);

        for (Edge edge : node.getNeighbors()) {
            algorithmFlooding(edge.getTarget(), message);
        }        
    }

    public void visitedNodes(){
        for(Map.Entry<Node, Boolean> visNode : visited.entrySet()){
            System.out.println("\n---------------------------------");
            Node node = visNode.getKey();
            boolean vis = visNode.getValue();

            System.out.println("Nodo: " + node.getLabel());
            System.out.println("Neighbors: ");
            for (Edge edge : node.getNeighbors()) {
                Node target = edge.getTarget();
                System.out.println("\t=> " + target.getLabel() + " - Weight: " + edge.getWeight());
            }
            System.out.println("Visited: " + vis);
        }
    }
}
