import java.util.Scanner;
import Model.Timer;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] command = line.split(" ");

            //elágazások
            if (command[0].equals("state")) {
                   /* if (command.length > 2) Game.State(command[1], "");
                    else Game.State(command[2], command[1]);*/
            }
            else if (command.equals("add")) {

            }

        }
        scanner.close();

    }
}