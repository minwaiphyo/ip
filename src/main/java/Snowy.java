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
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Sad puppy noises* Bye... Hope to play with you again soon!");
                System.out.println(INDENT + LINE);
                break;
            }

            // Lists out tasks
            if (input.equals("list")) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    Task task = tasks[i];
                    System.out.println(INDENT + (i + 1) + "." + task.printDetailed());
                }
                System.out.println(INDENT + LINE);

                // Unmark task
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Ok, I've marked this task as not done yet:");
                System.out.println(INDENT + tasks[taskIndex].printDetailed());
                System.out.println(INDENT + LINE);


                // Mark task
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Nice! I've marked this task as done:");
                System.out.println(INDENT + tasks[taskIndex].printDetailed());
                System.out.println(INDENT + LINE);

                // Creates todo
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                ToDo todo = new ToDo(description);
                tasks[taskCount] = todo;
                taskCount++;

                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + todo);
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                System.out.println(INDENT + LINE);


                // Creates deadline
            } else if (input.startsWith("deadline ")) {
                String details = input.substring(9);
                String[] parts = details.split(" /by ");
                String description = parts[0];
                String by = parts[1];

                Deadline deadline = new Deadline(description, by);
                tasks[taskCount] = deadline;
                taskCount++;

                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + deadline.printDetailed());
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                System.out.println(INDENT + LINE);

                // Creates event
            } else if (input.startsWith("event ")) {
                String details = input.substring(6);
                String[] parts = details.split(" /from | /to ");
                String description = parts[0];
                String from = parts[1];
                String to = parts[2];

                Event event = new Event(description, from, to);
                tasks[taskCount] = event;
                taskCount++;

                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + event.printDetailed());
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                System.out.println(INDENT + LINE);

                //Adds a generic task
            } else {
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
