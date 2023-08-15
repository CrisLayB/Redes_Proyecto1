package chatlive.controller;

import org.jivesoftware.smack.SmackException;

import chatlive.models.XmppClient;
import chatlive.view.ViewTerminal;

public class Controller {
    private ViewTerminal view;
    private XmppClient client;

    public Controller(){
        view = new ViewTerminal();
        client = new XmppClient();
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

                    try {
                        client.login(loginCredentials[0], loginCredentials[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.errorConnect(e.getMessage());
                        continue;
                    }
                    
                    view.loggedSuccessfully();
                    endApp = chatMenu();
                    client.disconnect();
                    break;
    
                case "2":
                    String[] createuserCredentials = view.createNewUser();
                    
                    try {
                        client.createUser(createuserCredentials[0], createuserCredentials[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        view.errorConnect(e.getMessage());                        
                        continue;
                    }
                    
                    endApp = chatMenu();
                    client.disconnect();
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

    private boolean chatMenu(){
        boolean endAppToo = false;
        boolean endMenuChat = false;
        do {
            String selection = view.menuChat(client.getInformationUser());

            switch (selection) {
                case "1":
                    view.displayContactsList(client.displayContactsList());
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
                    break;

                case "11":
                    endMenuChat = true;
                    endAppToo = true;
                    break;
            
                default:
                    view.invalidOption();
                    break;
            }
        } while (!endMenuChat);

        return endAppToo;
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
        try {
            client.setPressenceMessage(
                view.getText("Write your status Message: ")
            );
            view.statusChanguedSuccessfully();
        } 
        catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seeNotifications(){
        
    }

    private void seeFiles(){

    }

    private void deleteUser(){

    }
}
