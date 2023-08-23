package chatlive.controller;

import java.util.HashMap;

import org.jivesoftware.smack.SmackException;
import org.jxmpp.stringprep.XmppStringprepException;

import chatlive.models.XmppClient;
import chatlive.view.ViewTerminal;

/**
 * <h1>Networks - UVG</h1>
 * <h2> Controller </h2>
 * The Controller of the program that joins the functionallity of View and Model.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

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
                    
                    view.messageSuccessfully("Logged Successfully", 1000);
                    endApp = chatMenu();
                    client.disconnect();
                    break;
    
                case "2":
                    HashMap<String, String> newUser = view.createNewUser();
                
                    String result = client.createUser(newUser);

                    if(!view.countConfirmation(result, "created")) continue;
                  
                    try {
                        client.login(newUser.get("username"), newUser.get("password"));
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

    
    /** 
     * This methods start with the menu Chat when the user is logged.
     * 
     * @return boolean : If the all the program ends or not yet.
     */
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
                    // seeNotifications();
                    break;

                case "8":
                    // seeFiles();
                    break;

                case "9":
                    endMenuChat = deleteUser();
                    break;

                case "10":
                    endMenuChat = true;
                    break;

                case "11":
                    endMenuChat = true;
                    endAppToo = true;
                    break;

                case "clean":
                    view.clean();
                    break;
            
                default:
                    view.invalidOption();
                    break;
            }
        } while (!endMenuChat);

        return endAppToo;
    }

    private void addUserToContacts(){
        try {
            client.addContactToList(view.getText("\nWrite the user that you want to add: "));
            view.messageSuccessfully("User Added Successfully", 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDetailsOfUser(){
        try {
            view.displayContactDetail(
                client.getContactDetail(view.getText("\nWrite the username: "))
            );            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void communication1To1(){
        try {
            boolean ready = client.createChat(
                view.getText("Write the username to chat")
            );

            if(!ready) {
                view.errorUserNotFound();
                return;
            }

            boolean quit = false;
            while(!quit){
                String message = view.messageInput(
            "Write your message (write 'exit' for finish this chat) (write 'send-file' for send a file)"
                );

                if("exit".equalsIgnoreCase(message)){
                    quit = true;
                    continue;
                }

                if("send-file".equalsIgnoreCase(message)){
                    sendFile();
                    continue;
                }

                client.sendMessage(message, false);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void grupalConversations(){
        try {
            client.joinGroupChat(
                view.getText("Write the conference name"),
                view.getText("Please write a nickname")
            );
                        
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        boolean quit = false;
        while(!quit){
            String message = view.messageInput(
                "Write your message (write 'exit' for finish this chat) (write 'send-file' for send a file)"
            );

            if("exit".equalsIgnoreCase(message)){
                quit = true;
                continue;
            }

            if("send-file".equalsIgnoreCase(message)){
                sendFile();
                continue;
            }

            try {
                client.sendMessage(message, true);
                view.displayMessages(client.incomingMessagesGroup());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void definePresenceMessage(){
        try {
            client.setPressenceMessage(
                view.getText("Write your status Message: ")
            );
            view.messageSuccessfully("Status Changued Successfully", 1000);
        } 
        catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFile(){
        try {
            client.sendFile(
                view.getText("Please write your file path"), 
                view.getText("Please insert description")
            );
            view.fileSendSuccessfully();
        }
        catch(SmackException smackException){
            view.errorOfSmackSendFile(smackException.getMessage());
        }
        catch(XmppStringprepException xmppStringprepException){
            view.errorStringPrepException(xmppStringprepException.getMessage());
        }
        catch (Exception e) {
            view.unknownError(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This Method confirms if the user was deleted of not.
     * 
     * @return boolean : Confirmation of getted delete.
     */
    private boolean deleteUser(){
        if(!view.confirmDeleteCount()) return false;
        
        String information = client.deleteUser();
        view.countConfirmation(information, "deleted");
        return true;
    }
}
