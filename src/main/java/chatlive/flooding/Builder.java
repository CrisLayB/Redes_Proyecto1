package chatlive.flooding;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import chatlive.models.XmppClient;

public class Builder {
    private String message;
    private XmppClient client;

    public Builder(String pMessage, XmppClient pClient){
        message = pMessage;
        client = pClient;
        // run the formater.py
        formatJsons();
    }

    private void formatJsons(){
        String[] cmd = new String[2];
        cmd[0] = "python3";
        cmd[1] = "formater.py";
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec( cmd );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void build(String principalName){
        Map<String, String> readedNodes = readJsonNames("./names1-x-randomX-2023.txt");

        if(readedNodes == null) return;

        // ! Create nodes and get my own node
        Map<String, Node> nodes = new HashMap<String, Node>();

        String ownNode = "";

        for(Map.Entry<String, String> node : readedNodes.entrySet()){
            String key = node.getKey();
            String value = node.getValue();
            String status = "unavailable";           
            
            // ! ADD nodes users in the list of the user
            try {
                client.addContactToList(value);     
                status = client.checkStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }

            nodes.put(key, new Node(key, value, status.equals("available")));

            if(principalName.equals(value)){
                ownNode = key;
            }
        }

        // ! Check if own node is defined or not
        if(ownNode.equals("")){
            System.out.println("\nOwn node not defined\n");
            return;
        }

        // ! Create edges
        Map<String, ArrayList<String>> readedTopology = readJsonTopology("./topo1-x-randomX-2023.txt");

        if(readedTopology == null) return;

        for(Map.Entry<String, ArrayList<String>> topology : readedTopology.entrySet()){
            String key = topology.getKey();
            Node tempNode = nodes.get(key);

            if(!tempNode.getAvaliable()) continue;

            ArrayList<String> list = topology.getValue();

            for (String item : list) {
                Node itemNode = nodes.get(item);

                if(itemNode.getAvaliable()){
                    Edge edge = new Edge(itemNode);
                    nodes.get(key).addNeighbor(edge);
                }
            }
        }

        Flooding flooding = new Flooding(nodes.get(ownNode), message, client);
        flooding.visitedNodes();
    }
    
    private Map<String, String> readJsonNames(String jsonPath){
        JSONParser jsonParser = new JSONParser();
        Map<String, String> address = null;

        try (FileReader reader = new FileReader(jsonPath))
        {
            Object obj = jsonParser.parse(reader);
 
            JSONObject element = (JSONObject) obj;
            System.out.println(element);

            address = ((Map<String, String>)element.get("config"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return address;
    }

    private Map<String, ArrayList<String>> readJsonTopology(String jsonPath){
        JSONParser jsonParser = new JSONParser();
        Map<String, ArrayList<String>> topology = null;

        try (FileReader reader = new FileReader(jsonPath))
        {
            Object obj = jsonParser.parse(reader);

            JSONObject element = (JSONObject) obj;
            System.out.println(element);

            topology = ((Map<String, ArrayList<String>>)element.get("config"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return topology;
    }
}
