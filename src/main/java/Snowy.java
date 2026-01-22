import java.util.Scanner;

public class Snowy {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        String LINE = "____________________________________________________________";
        String INDENT = "     ";

        // Greets user
        System.out.println(LINE + "\n" +
                """
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
                """ + LINE);

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
                System.out.println(INDENT + LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    Task task = tasks[i];
                    System.out.println(INDENT + (i + 1) + "." + task);
                }
                System.out.println(INDENT + LINE);

                // Unmark task
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(LINE + "\n" + "Ok, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);

                //Mark task
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(LINE + "\n" + "Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);

                //Adds user input to tasks
            }else {
                tasks[taskCount] = new Task(input);
                taskCount++;

                // Confirms addition of new task
                System.out.println(INDENT + LINE);
                System.out.println("     " + "added: " +  input);
                System.out.println(INDENT + LINE);
            }
        }
        scanner.close();
    }
}
