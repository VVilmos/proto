import Model.Game;

import java.util.Scanner;
import Model.Timer;

public class Main {

    public static void main(String[] args) {

        //kezdetben üres, load parancsra újat töltünk be
        Game game = new Game();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] command = line.split(" ");

            //elágazások
            //Hiba az argumentumok számával: "Invalid argument! Please check the correct syntax of the command in the documentation."
            if (command[0].equals("state")) {
                if (command.length == 2) {
                    game.State(command[1], "");
                }
                else if (command.length == 3) {
                    game.State(command[1], command[2]);
                }
                else System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
            }
            else if (command[0].equals("//")) {
                //nothing
            }
            else if (command[0].equals("leak")) {
                if (command.length != 2) System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                else {
                    game.Leak(command[1]);
                }
            }
            else if (command[0].equals("disconnectpipe")) {
                if (command.length != 3) System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                else {
                    game.DisconnectPipe(command[1], command[2]);
                }
            }
            else if(command[0].equals("fill")){
                if(command.length != 2){
                    System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                }
                else{
                    game.Fill(command[1]);
                }
            }
            else if(command[0].equals("break")){
                if(command.length != 2){
                    System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                }
                else{
                    game.Break(command[1]);
                }
            }
            else if(command[0].equals("pickup")){
                if(command.length != 4){
                    System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                }
                else{
                    game.PickUp(command[1], command[2], command[3]);
                }
            }
            else if(command[0].equals("slipperypipe")){
                if(command.length != 2){
                    System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                }
                else{
                    game.SlipperyPipe(command[1]);
                }
            }
            else if(command[0].equals("stickypipe")){
                if(command.length != 2){
                    System.out.println("Invalid argument! Please check the correct syntax of the command in the documentation.");
                }
                else{
                    game.StickyPipe(command[1]);
                }
            }

        }
        scanner.close();

    }
}