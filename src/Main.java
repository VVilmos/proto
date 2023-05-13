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
            } else if (command[0].equals("grease")) {
                if (command.length != 2)
                    System.out.println(argErrorMsg);
                else {
                    game.Grease(command[1]);
                }
            } else if (command[0].equals("switch")) {
                if (command.length != 4)
                    System.out.println(argErrorMsg);
                else {
                    game.SwitchPump(command[1], command[2], command[3]);
                }
            } else if (command[0].equals("placepump")) {
                if (command.length != 2)
                    System.out.println(argErrorMsg);
                else {
                    game.PlacePump(command[1]);
                }
            }else if(command[0].equals("repair")){
                if(command.length != 3)
                    System.out.println(argErrorMsg);
                else{
                    game.Repair(command[1], command[2]);
                }
            }else if(command[0].equals("add")) {
                if(command.length != 3)
                    System.out.println(argErrorMsg);
                else{
                    game.Add(command[1], command[2]);
                }
            }else if(command[0].equals("drain")) {
                if (command.length != 2)
                    System.out.println(argErrorMsg);
                else {
                    game.Drain(command[1]);
                }
            }else if(command[0].equals("step")){
                if(command.length != 2)
                    System.out.println(argErrorMsg);
                else{
                    game.Step(command[1]);
                }
            }else if(command[0].equals("holdpipe")) {
                if (command.length != 3)
                    System.out.println(argErrorMsg);
                else {
                    game.HoldPipe(command[1], command[2]);
                }
            }else if(command[0].equals("holdpump")) {
                if (command.length != 3)
                    System.out.println(argErrorMsg);
                else {
                    game.HoldPump(command[1], command[2]);
                }
            } else if(command[0].equals("connect")) {
                if(command.length == 3)
                    game.Connect(command[1], command[2]);
                else if(command.length == 4)
                    game.Connect(command[1], command[2], command[3]);
                else
                    System.out.println(argErrorMsg);
            } else if(command[0].equals("sticky")) {
                if(command.length == 2)
                    game.Sticky(command[1]);
                else
                    System.out.println(argErrorMsg);
            } else if(command[0].equals("connectpipe")) {
                if(command.length == 2)
                    game.ConnectPipe(command[1]);
                else
                    System.out.println(argErrorMsg);
            } else if(command[0].equals("leakpipe")) {
                if(command.length == 2)
                    game.LeakPipe(command[1]);
                else
                    System.out.println(argErrorMsg);
            } else if(command[0].equals("switch")) {
                if(command.length == 4)
                    game.Switch(command[1], command[2], command[3]);
                else
                    System.out.println(argErrorMsg);
            }

        }
        scanner.close();

    }
}