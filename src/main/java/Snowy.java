import java.util.Scanner;

public class Snowy {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;


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
                break;
            }

            // Lists out tasks
            if (input.equals("list")) {
                System.out.println("     _______________________________________________________");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("     _______________________________________________________");
            } else {
                // Adds user input to tasks
                tasks[taskCount] = input;
                taskCount++;

                // Confirms addition of new task
                System.out.println("     _______________________________________________________");
                System.out.println("     " + "added: " +  input);
                System.out.println("     _______________________________________________________");
            }
        }
        scanner.close();
    }
}
