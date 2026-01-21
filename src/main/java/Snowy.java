import java.util.Scanner;

public class Snowy {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Greets user
        System.out.println("""
                ____________________________________________________________
                Woof woof! I'm Snowy!
                  /^-----^\\
                 V  o o  |\s
                  |  Y  | \s
                   \\ Q /  \s
                   / - \\  \s
                   |    \\
                   |     \\     )
                   || (___\\====
                What can I do for you? :3
                ____________________________________________________________
                
                """);

        while (true) {
            String input = scanner.nextLine();

            // User terminating
            if (input.equals("bye")) {
                System.out.println("     _______________________________________________________");
                System.out.println("     *Sad puppy noises* Bye... Hope to play with you again soon!");
                System.out.println("     _______________________________________________________");
                return;
            }

            // Echoes back user input
            System.out.println("     _______________________________________________________");
            System.out.println("     " + input);
            System.out.println("     _______________________________________________________");
        }

    }

}
