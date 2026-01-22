import java.util.Scanner;
import java.util.ArrayList;

public class Snowy {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        int taskCount = 0;

        String LINE = "____________________________________________________________";
        String INDENT = "     ";


        // Greets user
        System.out.println(LINE);
        System.out.println("Woof woof! I'm Snowy!");
        System.out.println("  /^-----^\\");
        System.out.println(" V  o o  |");
        System.out.println("  |  Y  |");
        System.out.println("   \\ Q /");
        System.out.println("   / - \\");
        System.out.println("   |    \\");
        System.out.println("   |     \\     )");
        System.out.println("   || (___\\====");
        System.out.println("What can I do for you? :3");
        System.out.println(LINE);

        while (true) {
            String input = scanner.nextLine();

            try {
                // User terminating
                if (input.equals("bye")) {
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Sad puppy noises* Bye... Hope to play with you again soon!");
                    System.out.println(INDENT + LINE);
                    break;
                }

                // Lists out tasks
                if (input.trim().equals("list")) {
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        Task task = tasks.get(i);
                        System.out.println(INDENT + (i + 1) + "." + task.printDetailed());
                    }
                    System.out.println(INDENT + LINE);

                    // Unmark task
                } else if (input.startsWith("unmark ") || input.equals("unmark")) {

                    // Handles empty taskIndex
                    if (input.length() <= 7) {
                        throw new SnowyException("Woof woof! Please specify the number of the task to unmark!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new SnowyException("Woof woof! That number doesn't exist!");
                    }

                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Ok, I've marked this task as not done yet:");
                    System.out.println(INDENT + tasks.get(taskIndex).printDetailed());
                    System.out.println(INDENT + LINE);


                    // Mark task
                } else if (input.startsWith("mark ") || input.equals("mark")) {

                    // Handles empty taskIndex
                    if (input.length() < 6) {
                        throw new SnowyException("Woof! Please specify the number of the task to mark!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new SnowyException("Woof woof! That task number doesn't exist!");
                    }

                    tasks.get(taskIndex).markAsDone();
                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Nice! I've marked this task as done:");
                    System.out.println(INDENT + tasks.get(taskIndex).printDetailed());
                    System.out.println(INDENT + LINE);
                    // Creates todo
                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new SnowyException("Woof woof! The description of a ToDo cannot be empty!");
                    }
                    ToDo todo = new ToDo(description);
                    tasks.add(taskCount, todo);
                    taskCount++;

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + todo);
                    System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                    System.out.println(INDENT + LINE);
                } else if (input.equals("todo")) {
                    throw new SnowyException("Woof woof! The description of a ToDo cannot be empty!");

                    // Creates deadline
                } else if (input.startsWith("deadline "))  {
                    String details = input.substring(9).trim();

                    // Handles empty details
                    if (details.isEmpty()) {
                        throw new SnowyException("Woof! The description of a deadline cannot be empty!");
                    }
                    if (!details.contains(" /by ")) {
                        throw new SnowyException("Woof woof! Please use the format: deadline [task] /by [date]");
                    }

                    String[] parts = details.split(" /by ");

                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new SnowyException("Woof! Both the description and deadline are required!");
                    }

                    String description = parts[0];
                    String by = parts[1];

                    Deadline deadline = new Deadline(description, by);
                    tasks.add(taskCount, deadline);
                    taskCount++;

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + deadline.printDetailed());
                    System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                    System.out.println(INDENT + LINE);
                } else if (input.equals("deadline")){
                    throw new SnowyException("Woof! The description of a deadline cannot be empty!");

                    // Creates event
                } else if (input.startsWith("event ")) {
                    String details = input.substring(6);

                    if (details.isEmpty()) {
                        throw new SnowyException("Woof! The description of an event cannot be empty!");
                    }
                    if (!details.contains(" /from ") || !details.contains(" /to ")) {
                        throw new SnowyException("Woof woof! Please use the format: event [task] /from [start] /to [end]");
                    }

                    String[] parts = details.split(" /from | /to ");

                    if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                        throw new SnowyException("Woof! Description, start time, and end time are all required!");
                    }

                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];

                    Event event = new Event(description, from, to);
                    tasks.add(taskCount, event);
                    taskCount++;

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Got it. I've added this task:");
                    System.out.println(INDENT + event.printDetailed());
                    System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
                    System.out.println(INDENT + LINE);
                } else if (input.equals("event")) {
                    throw new SnowyException("Woof woof! The description of an event cannot be empty!");

                } else if (input.startsWith("delete ") || input.equals("delete")) {

                    // Handles empty taskIndex
                    if (input.length() <= 7) {
                        throw new SnowyException("Woof woof! Please specify the index  of the task to delete!");
                    }

                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;

                    // Handles invalid taskIndex
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new SnowyException("Woof woof! That number doesn't exist!");
                    }

                    Task task = tasks.get(taskIndex);
                    tasks.remove(taskIndex);
                    taskCount--;

                    System.out.println(INDENT + LINE);
                    System.out.println(INDENT + "Noted. I've removed this task:");
                    System.out.println(INDENT + task.printDetailed());
                    System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");

                } else {
                    throw new SnowyException("""
                        *Sad Snowy noises* I don't understand that command! Try 'todo', 'deadline', 'event', 'list', 'mark', or 'unmark'!
                        """);
                }
            } catch(SnowyException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + e.getMessage());
                System.out.println(INDENT + LINE);
            } catch (NumberFormatException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Woof! Please provide a valid task number");
                System.out.println(INDENT + LINE);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(INDENT + LINE);
                System.out.println(INDENT + "Woof woof! Something went wrong with parsing your command. Please check the format");
            }
        }
        scanner.close();
    }
}
