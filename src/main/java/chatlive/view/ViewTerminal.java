package chatlive.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import chatlive.models.Colors;

import java.io.Console;

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

    public String startApp(){
        line();
        System.out.println("GENERIC XMPP CHAT");
        System.out.println("1. Login");
        System.out.println("2. Create new User");
        System.out.println("3. Quit app");

        return scanner.nextLine();
    }
    
    public String[] login(){
        String username = getText("\nOnly write the username: ");
        String password = getPassword();
        return new String[]{username, password};
    }
    
    public HashMap<String, String> createNewUser(){
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("username", getText("\nUsername: "));
        attributes.put("name", getText("\nFull name: "));
        attributes.put("password", getPassword());
        return attributes;
    }
    
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

    public void errorConnect(String details){
        System.out.println("\nError to connect:");
        System.out.println(details + "\n");
    }

    public void errorClientNull(){
        System.out.println("\nError: The client is null");
    }

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

    public void messageSuccessfully(String message, int timeToWait){
        System.out.println("\n" + message);
        wait(timeToWait);
        clean();
    }

    public String messageInput(String prompt){
        System.out.println(prompt + ": ");
        return scanner.nextLine();
    }

    public void displayMessage(String from, String content){
        System.out.println("Message from " + from + ": " + content);
    }

    public void displayMessages(ArrayList<String> messages){
        for (String message : messages) {
            System.out.println(message);
        }
    }

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

    public boolean confirmDeleteCount(){
        System.out.println("Are you sure that you want to delete this count? (s/n) ");
        String option = scanner.nextLine();
        return (option.equals("S") || option.equals("s"));
    }

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

    public void errorOfSmackSendFile(String details){
        System.out.println(Colors.RED + "\nError sending the file: " + details + Colors.RESET);
    }

    public void errorStringPrepException(String details){
        System.out.println(Colors.RED + "\nError with the string: " + details + Colors.RESET);
    }

    public void unknownError(String details){
        System.out.println(Colors.RED + "\nUnknow Error: " + details + Colors.RESET);
    }

    public void fileSendSuccessfully(){
        System.out.println(Colors.GREEN + "\nFile Send Successfully" + Colors.RESET);
    }

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
