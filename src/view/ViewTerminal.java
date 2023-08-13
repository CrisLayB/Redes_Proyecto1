package view;

import java.util.Scanner;

public class ViewTerminal {
    
    private Scanner scanner;

    public ViewTerminal(){
        scanner = new Scanner(System.in);        
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
        System.out.println("\nUsername: ");
        String username = getText();
        System.out.println("\nFull Name: ");
        String fullname = getText();
        System.out.println("\nEmail: ");
        String email = getText();
        System.out.println("\nPassword: ");
        String password = getText();
        return new String[]{username, fullname, email, password};
    }

    public String[] createNewUser(){
        System.out.println("\nUser (user@example.org): ");
        String username = getText();
        System.out.println("\nPassword: ");
        String password = getText();
        return new String[]{username, password};
    }
    
    public String mainMenu(){
        line();
        System.out.println("WELCOME TO THE CHAT XMPP APP");
        System.out.println("1. Shot all the users/contacts and their state");
        System.out.println("2. Add a user in the contacts");
        System.out.println("3. Show details of conact of an user");
        System.out.println("4. Comunication 1 to 1 with any user/contact");
        System.out.println("5. Participate in grupal conversations");
        System.out.println("6. Define presence messague");
        System.out.println("7. Send/Receive notifications");
        System.out.println("8. Send/Receive files");
        System.out.println("9. Close Session");
        System.out.println("10. Delete User");
        System.out.println("11. Quit App\n");

        return scanner.nextLine();
    }

    private String getText(){
        String text = "";

        while(text.isBlank()){
            text = scanner.nextLine();

            if(text.isBlank()) 
                System.out.println("\nThe input is empty, please write something right\n");
        }
        
        return text;
    }

    private void line(){
        System.out.println("============================================");
    }
    
    public void invalidOption(){
        System.out.println("\nInvalid Option\n");
        wait(1000);
        clean();
    }

    private static void wait(int ms) {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    private void clean(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}
