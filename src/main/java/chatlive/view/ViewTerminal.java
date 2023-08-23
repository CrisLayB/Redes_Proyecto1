package chatlive.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import chatlive.models.Colors;

import java.io.Console;

/**
 * <h1>Networks - UVG</h1>
 * <h2> View By Terminal </h2>
 * Class for controll all of the Terminal GUI and get the input of the user.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class ViewTerminal {
    private Scanner scanner;
    private Console console;

    public ViewTerminal(){
        scanner = new Scanner(System.in);
        console = System.console();

        if (console == null) {
            System.err.println("\nNo console available. Please run this program from a terminal.\n");
            System.exit(1);
        }
    }

    
    /** 
     * Initial Menu for start the correspondient app, we have login, create new user and quit app
     * 
     * @return String : Input of user for select
     */
    public String startApp(){
        line();
        System.out.println("GENERIC XMPP CHAT");
        System.out.println("1. Login");
        System.out.println("2. Create new User");
        System.out.println("3. Quit app");

        return scanner.nextLine();
    }
    
    
    /** 
     * Get the credentials for user with a pretty format.
     * 
     * @return String[] : username and password
     */
    public String[] login(){
        String username = getText("\nOnly write the username: ");
        String password = getPassword();
        return new String[]{username, password};
    }
    
    /**
     * Create the credentials for the new user to create and login.
     * 
     * @return HashMap<String, String> : attributes of the new user
     */
    public HashMap<String, String> createNewUser(){
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("username", getText("\nUsername: "));
        attributes.put("name", getText("\nFull name: "));
        attributes.put("password", getPassword());
        return attributes;
    }
    
    /**
     * Show the main menu after login the user with the information of the user.
     * 
     * @param details
     * @return String : Option to do with the menu Chat
     */
    public String menuChat(String[] details){
        line();
        System.out.println("WELCOME TO THE CHAT XMPP APP - " + details[0]);
        System.out.println("Presence: " + Colors.CYAN + details[1] + Colors.RESET);
        System.out.println("Status: " + Colors.GREEN + details[2] + Colors.RESET);
        System.out.println("1. Show all the users/contacts and their state");
        System.out.println("2. Add a user in the contacts");
        System.out.println("3. Show details of conact of an user");
        System.out.println("4. Comunication 1 to 1 with any user/contact");
        System.out.println("5. Participate in grupal conversations");
        System.out.println("6. Define presence message");
        System.out.println("7. Display notifications");
        System.out.println("8. Send/Receive files");
        System.out.println("9. Delete THIS User");
        System.out.println("10. Close Session");
        System.out.println("11. Quit App and Close Session\n");

        return scanner.nextLine();
    }

    public void invalidOption(){
        System.out.println("\nInvalid Option\n");
        wait(1000);
        clean();
    }

    /**
     * We show the error of connect and why of this error.
     * 
     * @param details
     */
    public void errorConnect(String details){
        System.out.println("\nError to connect:");
        System.out.println(details + "\n");
    }

    public void errorClientNull(){
        System.out.println("\nError: The client is null");
    }

    /**
     * This method display all the contacts that have the user logged with his details.
     * 
     * @param list
     */
    public void displayContactsList(ArrayList<String[]> list){
        System.out.println("\nAmount Contacts: " + list.get(0)[0]);

        if(list.size() <= 1) return;

        for (int i = 1; i < list.size(); i++) {
            String[] data = list.get(i);
            System.out.println("\n=> " + data[0]);
            System.out.println("Presence: " + Colors.CYAN + data[1] + Colors.RESET);
            System.out.println("Status: " + Colors.GREEN + data[2] + Colors.RESET);
        }
    }

    /**
     * Show "more" details of an specific user
     * 
     * @param deatil
     */
    public void displayContactDetail(String[] detail){
        if(detail.length == 1){
            System.out.println("\n" + detail[0]);
            return;
        }

        System.out.println("\nContact Detail: ");
        for (String string : detail) {
            System.out.println(string);
        }
    }

    /**
     * For send the successfull message and then wait for show and then clean the screen.
     * 
     * @param message
     * @param timeToWait
     */
    public void messageSuccessfully(String message, int timeToWait){
        System.out.println("\n" + message);
        wait(timeToWait);
        clean();
    }

    /**
     * Get the correspondent input for write a message.
     * 
     * @param prompt
     * @return String : Input Message of the user
     */
    public String messageInput(String prompt){
        System.out.println(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Show the message of a specific chat.
     * 
     * @param from
     * @param content
     */
    public void displayMessage(String from, String content){
        System.out.println("Message from " + from + ": " + content);
    }

    /**
     * Show all the messagues of a selected chat
     * 
     * @param messages
     */
    public void displayMessages(ArrayList<String> messages){
        for (String message : messages) {
            System.out.println(message);
        }
    }

    /**
     * This assistent method helps to get a String not empty.
     * 
     * @param message
     * @return String : String not empty and checked
     */
    public String getText(String message){
        String text = "";

        while(text.isBlank()){
            System.out.println(message);
            text = scanner.nextLine();

            if(text.isBlank()) 
                System.out.println("\nThe input is empty, please write something right\n");
        }
        
        return text;
    }
    
    public void errorUserNotFound(){
        System.out.println(Colors.RED + "\nUser not found" + Colors.RESET);
    }

    /**
     * This methods asks to the user if they really want to delete their count.
     * 
     * @return boolean : If the count will be deleted of not
     */
    public boolean confirmDeleteCount(){
        System.out.println("Are you sure that you want to delete this count? (s/n) ");
        String option = scanner.nextLine();
        return (option.equals("S") || option.equals("s"));
    }

    /**
     * Depend of the context they verify if any error exists, if the message has no errors
     * then will be show the real information.
     * 
     * @param message
     * @param information
     * @return boolean : If the process was successfully
     */
    public boolean countConfirmation(String message, String information){
        if(message.equals("-1")){
            System.out.println("\nAccount Manager doesn't supports account creation\n");
            return false;
        }
        
        if(!message.equals("1")){
            System.out.println("\nError: " + message);
            return false;
        }

        System.out.println("\nCount " + information + " Successfully\n");
        wait(1000);
        return true;
    }

    /**
     * Show error of smack of sending file
     * 
     * @param details
     */
    public void errorOfSmackSendFile(String details){
        System.out.println(Colors.RED + "\nError sending the file: " + details + Colors.RESET);
    }

    /**
     * Show error of string prep
     * 
     * @param details
     */
    public void errorStringPrepException(String details){
        System.out.println(Colors.RED + "\nError with the string: " + details + Colors.RESET);
    }

    /**
     * Show unknown error and then the details.
     * 
     * @param details
     */
    public void unknownError(String details){
        System.out.println(Colors.RED + "\nUnknow Error: " + details + Colors.RESET);
    }

    public void fileSendSuccessfully(){
        System.out.println(Colors.GREEN + "\nFile Send Successfully" + Colors.RESET);
    }

    /**
     * This assistent method helps to write the password that can't be seen
     * 
     * @return String : The password
     */
    private String getPassword(){
        String password = "";

        while(password.isBlank()){
            char[] passwordArray = console.readPassword("Enter your password: ");
            password = new String(passwordArray);

            if(password.isBlank())
                System.out.println("\nThe input is empty, please write something right\n");
        }
        
        return password;
    }

    private void line(){
        System.out.println("=============================================================");
    }    

    /**
     * Method for wait
     * 
     * @param ms
     */
    private static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void clean(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}
