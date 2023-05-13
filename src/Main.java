import Model.Game;

import java.util.Scanner;

import Model.Timer;

public class Main {
    /**
     * Hibaüzenet arra az esetre, ha egy parancs argumentumai helytelenek.
     */
    private static final String argErrorMsg = "Invalid argument! Please check the correct syntax of the command in the documentation.";
    public static void main(String[] args) {

        //kezdetben üres, load parancsra újat töltünk be
        Game game = new Game();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] command = line.split(" ");

            //elágazások
            //TODO: Hiba az argumentumok számával: "Invalid argument! Please check the correct syntax of the command in the documentation."
            //TODO: A teljes üzenet kiírása helyett lehet használni az argErrorMsg változót is
            if (command[0].equals("state")) {
                if (command.length == 2) {
                    game.State(command[1], "");
                } else if (command.length == 3) {
                    game.State(command[1], command[2]);
                } else
                    System.out.println(argErrorMsg);
            } else if (command[0].equals("//")) {
                //nothing
            } else if (command[0].equals("leak")) {
                if (command.length != 2)
                    System.out.println(argErrorMsg);
                else {
                    game.Leak(command[1]);
                }
            } else if (command[0].equals("disconnectpipe")) {
                if (command.length != 3)
                    System.out.println(argErrorMsg);
                else {
                    game.DisconnectPipe(command[1], command[2]);
                }
            } else if (command[0].equals("move")) {
                if (command.length != 3)
                    System.out.println(argErrorMsg);
                else {
                    game.Move(command[1], command[2]);
                }
            } else if (command[0].equals("grease")){
                if(command.length != 2)
                    System.out.println(argErrorMsg);
                else{
                    game.Grease(command[1]);
                }
            }else if(command[0].equals("switch")){
                if(command.length!=4)
                    System.out.println(argErrorMsg);
                else{
                    game.SwitchPump(command[1], command[2], command[3]);
                }
            }

        }
        scanner.close();

    }
}