package chatlive;

import chatlive.controller.Controller;

/**
 * <h1>Networks - UVG</h1>
 * <h2> App </h2>
 * Principal Class for Run all the program.
 * 
 * Created By:
 * @author Cristian Fernando Laynez Bachez - 201281
 * @since 2023
 **/

public class App {
    
    /** 
     * Principal Method for Run the program.
     * 
     * @param args
     */
    public static void main( String[] args ) {
        Controller controller = new Controller();
        controller.app();
    }
}
