package chatlive.flooding;

import java.util.HashMap;
import java.util.Map;

import chatlive.models.XmppClient;

public class Flooding {
    private Map<Node, Boolean> visited;
    private Node startNode;
    private XmppClient client;

    public Flooding(Node startNode, String message, XmppClient pClient){
        visited = new HashMap<Node, Boolean>();
        this.startNode = null;
        this.client = pClient;
        algorithmFlooding(startNode, message);
    }

    private void algorithmFlooding(Node node, String message){
        
        if (visited.containsKey(node) && visited.get(node)) return;        
        
        if(startNode == null){
            startNode = node;
            System.out.println("Node messenger: " + node.getLabel() + " - " + node.getName());
        }
        else{
            System.out.println("Node " + node.getLabel() + " - " + node.getName() + " received message: " + message);
            // ! SEND message to the users
            try {
                client.createChat(node.getName());
                
                client.sendMessage(message, false);                
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
