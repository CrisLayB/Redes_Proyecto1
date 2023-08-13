package controller;

import view.ViewTerminal;

public class Controller {
    private ViewTerminal view;

    public Controller(){
        view = new ViewTerminal();
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
                    //
                    break;
    
                case "2":
                    //
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
}
