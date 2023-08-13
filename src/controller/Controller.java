package controller;

import view.ViewTerminal;
import model.XmppClient;

public class Controller {
    private ViewTerminal view;
    private XmppClient client;

    public Controller(){
        view = new ViewTerminal();
        client = null;
    }

    public void app(){
        userSection();
    }

    private void userSection(){
        boolean endApp = false;
        do {
            String selectionUser = view.startApp();
    
            switch (selectionUser) {
                case "1":
                    String[] loginCredentials = view.login();

                    // Check if is user valid or not
                    client = new XmppClient(loginCredentials[0], loginCredentials[1]);

                    try {
                        client.connect();                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.errorConnect(e.getMessage());
                        continue;
                    }

                    if(client == null){
                        view.errorClientNull();
                        continue;
                    }
                    
                    endApp = chatMenu(loginCredentials[0]);
                    break;
    
                case "2":
                    String[] createuserCredentials = view.createNewUser();
                    // Create the new user
                    // !...
                    endApp = chatMenu(createuserCredentials[0]);
                    break;
    
                case "3":
                    endApp = true;
                    break;
            
                default:
                    view.invalidOption();
                    break;
            }            
        } while (!endApp);
    }

    private boolean chatMenu(String username){
        // ? Send presence
        boolean endAppToo = false;
        boolean endMenuChat = false;
        do {
            String selection = view.menuChat(username);

            switch (selection) {
                case "1":
                    showAllusers();
                    break;

                case "2":
                    addUserToContacts();
                    break;

                case "3":
                    showDetailsOfUser();
                    break;

                case "4":
                    communication1To1();
                    break;

                case "5":
                    grupalConversations();
                    break;

                case "6":
                    definePresenceMessage();
                    break;

                case "7":
                    seeNotifications();
                    break;

                case "8":
                    seeFiles();
                    break;

                case "9":
                    deleteUser();
                    break;

                case "10":
                    endMenuChat = true;
                    client.disconnect();
                    break;

                case "11":
                    endMenuChat = true;
                    endAppToo = true;
                    client.disconnect();
                    break;
            
                default:
                    view.invalidOption();
                    break;
            }
        } while (!endMenuChat);

        return endAppToo;
    }

    private void showAllusers(){

    }

    private void addUserToContacts(){

    }

    private void showDetailsOfUser(){

    }

    private void communication1To1(){

    }

    private void grupalConversations(){

    }

    private void definePresenceMessage(){

    }

    private void seeNotifications(){
        
    }

    private void seeFiles(){

    }

    private void deleteUser(){

    }
}
