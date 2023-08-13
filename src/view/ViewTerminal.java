package view;

import java.util.Scanner;
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
        String username = getText("\nUser (user@example.org): ");
        String password = getPassword();
        return new String[]{username, password};
    }
    
    public String[] createNewUser(){
        String username = getText("\nUsername: ");
        String fullname = getText("\nFull Name: ");
        String email = getText("\nEmail: ");
        String password = getPassword();
        return new String[]{username, fullname, email, password};
    }
    
    public String menuChat(String username){
        line();
        System.out.println("WELCOME TO THE CHAT XMPP APP - " + username);
        System.out.println("1. Shot all the users/contacts and their state");
        System.out.println("2. Add a user in the contacts");
        System.out.println("3. Show details of conact of an user");
        System.out.println("4. Comunication 1 to 1 with any user/contact");
        System.out.println("5. Participate in grupal conversations");
        System.out.println("6. Define presence message");
        System.out.println("7. Send/Receive notifications");
        System.out.println("8. Send/Receive files");
        System.out.println("9. Delete User");
        System.out.println("10. Close Session");
        System.out.println("11. Quit App and Close Seccion\n");

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

    private String getText(String message){
        String text = "";

        while(text.isBlank()){
            System.out.println(message);
            text = scanner.nextLine();

            if(text.isBlank()) 
                System.out.println("\nThe input is empty, please write something right\n");
        }
        
        return text;
    }

    private String getPassword(){
        String password = "";

        while(password.isBlank() || password.length() < 4){
            char[] passwordArray = console.readPassword("Enter your password: ");
            password = new String(passwordArray);

            if(password.isBlank())
                System.out.println("\nThe input is empty, please write something right\n");

            if(password.length() < 4)
                System.out.println("\nThe password has less of 4 characters, please add more\n");
        }
        
        return password;
    }

    private void line(){
        System.out.println("============================================");
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
