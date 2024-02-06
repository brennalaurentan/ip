import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Tam {
    private static String dividerText = "____________________________________________________________\n";
    private static TaskList taskListObj;

    public static void main(String[] args) {
        taskListObj = Storage.getSavedTasks();
        Ui.greet();
        int status = Parser.processCommand(taskListObj);
        while (status == 1) {
            status = Parser.processCommand(taskListObj);
        }
        Ui.exit();
    }
//
//    public static int readCommand() {
//        String input = scannerObj.nextLine();
//        String[] splitInput = input.split(" ");
//        String command = splitInput[0].toLowerCase();
//        System.out.print(dividerText);
//        try {
//            switch (command) {
//                case "bye":
//                case "exit":
//                    return 0;
//
//                case "list":
//                    Tam.listTasks();
//                    return 1;
//
//                case "mark":
//                case "unmark":
//                    try {
//                        int taskNum = Integer.parseInt(splitInput[1]);
//                        if (command.equals("mark")) {
//                            Tam.markTaskDone(taskNum);
//                        } else {
//                            Tam.markTaskUndone(taskNum);
//                        }
//                    } catch (NumberFormatException e) {
//                        System.out.print("Invalid task number entered\n");
//                    } catch (IndexOutOfBoundsException e) {
//                        System.out.print("Task does not exist! Type 'list' to check task numbers\n");
//                    }
//                    Storage.saveTaskList(taskList);
//                    return 1;
//
//                case "delete":
//                    try {
//                        int taskNum = Integer.parseInt(splitInput[1]);
//                        Tam.deleteTask(taskNum);
//                    } catch (NumberFormatException e) {
//                        System.out.print("Invalid task number entered\n");
//                        System.out.print(dividerText);
//                    } catch (IndexOutOfBoundsException e) {
//                        System.out.print("Task does not exist! Type 'list' to check task numbers\n");
//                        System.out.print(dividerText);
//                    }
//                    Storage.saveTaskList(taskList);
//                    return 1;
//
//                case "todo":
//                    String todoDescription = input.replace("todo", "").trim();
//                    if (todoDescription.equals("")) { // missing description
//                        System.out.print("Missing task description!\n");
//                        System.out.print(dividerText);
//                    } else if (todoDescription.contains("/by")) { // unnecessary due date
//                        System.out.print("Todo task cannot have a due date!\n");
//                        System.out.print(dividerText);
//                    } else if (todoDescription.contains("/from") ||
//                            todoDescription.contains("/to")) { // unnecessary from and to date
//                        System.out.print("Todo task cannot have a from and to date!\n");
//                        System.out.print(dividerText);
//                    } else {
//                        Tam.addTodo(todoDescription);
//                    }
//                    Storage.saveTaskList(taskList);
//                    return 1;
//
//                case "deadline":
//                    String deadlineDescriptionDueDate = input.replace("deadline", "").trim();
//                    if (!deadlineDescriptionDueDate.contains(" /by ")) { // missing '/by' keyword
//                        System.out.print("Missing due date!\n");
//                        System.out.print(dividerText);
//                        Storage.saveTaskList(taskList);
//                        return 1;
//                    }
//                    String[] deadlineDescriptionDueDateArray = input.replace("deadline", "")
//                            .trim().split(" /by ");
//                    String deadlineDescription = deadlineDescriptionDueDateArray[0];
//                    String deadlineDueDate = deadlineDescriptionDueDateArray[1];
//                    if (deadlineDescription.equals("")) { // empty description
//                        System.out.print("Missing task description!\n");
//                        System.out.print(dividerText);
//                    }
//                    else if (deadlineDueDate.equals("")) { // empty due date
//                        System.out.print("Missing task due date!\n");
//                        System.out.print(dividerText);
//                    }
//                    else if (deadlineDescription.contains("/from") ||
//                            deadlineDescription.contains("/to")) { // unnecessary from and to date
//                        System.out.print("Deadline task cannot have a from and to date!\n");
//                        System.out.print(dividerText);
//                    }
//                    else {
//                        try {
//                            // assume yyyy-mm-dd format
//                            String[] dateStringArray = deadlineDueDate.split("-");
//                            int[] dateArray = new int[3];
//                            for (int i = 0; i < 3; i++) {
//                                dateArray[i] = Integer.parseInt(dateStringArray[i]);
//                            }
//                            LocalDate deadlineDueDateLocal = LocalDate.of(dateArray[0], dateArray[1], dateArray[2]);
//                            Tam.addDeadline(deadlineDescription, deadlineDueDateLocal);
//                        }
//                        catch (Exception e) {
//                            System.out.print("Incorrect formatting of due date! Ensure it is yyyy-mm-dd\n");
//                            System.out.print(dividerText);
//                        }
//                    }
//                    Storage.saveTaskList(taskList);
//                    return 1;
//
//                case "event":
//                    String eventDescriptionFromTo = input.replace("event", "").trim();
//                    if (!eventDescriptionFromTo.contains(" /from ") ||
//                            !eventDescriptionFromTo.contains(" /to ")) { // missing '/from' or '/to' keywords
//                        System.out.print("Missing event from and/or to date!\n");
//                        System.out.print(dividerText);
//                        Storage.saveTaskList(taskList);
//                        return 1;
//                    }
//                    String[] eventDescriptionFromToArray = input.replace("event", "")
//                            .trim().split(" /from ");
//                    String eventDescription = eventDescriptionFromToArray[0];
//                    String eventFromTo = eventDescriptionFromToArray[1];
//                    String[] eventFromToArray;
//                    String eventFrom = "";
//                    String eventTo = "";
//                    if (eventFromTo.contains(" /to ")) {
//                        eventFromToArray = eventDescriptionFromToArray[1].trim().split(" /to ");
//                        eventFrom = eventFromToArray[0];
//                        eventTo = eventFromToArray[1];
//                    }
//                    if (eventDescription.equals("")) { // missing description
//                        System.out.print("Missing task description!\n");
//                        System.out.print(dividerText);
//                    } else if (eventFrom.equals("")) { // missing from date
//                        System.out.print("Missing from date!\n");
//                        System.out.print(dividerText);
//                    } else if (eventTo.equals("")) { // missing to date
//                        System.out.print("Missing to date!\n");
//                        System.out.print(dividerText);
//                    } else if (eventDescription.contains("/by")) { // unnecessary due date
//                        System.out.print("Event task cannot have a due date!\n");
//                        System.out.print(dividerText);
//                    } else {
//                        try {
//                            // assume yyyy-mm-dd format
//                            String[] fromDateStringArray = eventFrom.split("-");
//                            String[] toDateStringArray = eventTo.split("-");
//                            int[] fromDateArray = new int[3];
//                            int[] toDateArray = new int[3];
//                            for (int i = 0; i < 3; i++) {
//                                fromDateArray[i] = Integer.parseInt(fromDateStringArray[i]);
//                                toDateArray[i] = Integer.parseInt(toDateStringArray[i]);
//                            }
//                            LocalDate eventFromDateLocal = LocalDate.of(fromDateArray[0], fromDateArray[1], fromDateArray[2]);
//                            LocalDate eventToDateLocal = LocalDate.of(toDateArray[0], toDateArray[1], toDateArray[2]);
//                            Tam.addEvent(eventDescription, eventFromDateLocal, eventToDateLocal);
//                        }
//                        catch (Exception e) {
//                            System.out.print("Incorrect formatting of due date! Ensure it is yyyy-mm-dd\n");
//                            System.out.print(dividerText);
//                        }
//                    }
//                    Storage.saveTaskList(taskList);
//                    return 1;
//
//                case "help":
//                    System.out.print("LIST OF COMMANDS\n");
//                    System.out.print("Create todo: todo [task name] (eg: todo borrow book)\n");
//                    System.out.print("Create deadline: deadline [task name] /by [due date] " +
//                            "(eg: deadline homework /by Sun 6pm)\n");
//                    System.out.print("Create event: event [task name] /from [from date] /to [to date] " +
//                            "(eg: event concert /from Mon 6pm /to Mon 8pm)\n");
//                    System.out.print("List current tasks: list\n");
//                    System.out.print("Mark a task as complete: mark [task number on list] " +
//                            "(eg: mark 2)\n");
//                    System.out.print("Mark a task as incomplete: unmark [task number on list] " +
//                            "(eg: unmark 2)\n");
//                    System.out.print("Delete a task: delete [task number on list] " +
//                            "(eg: delete 3)\n");
//                    System.out.print("Close Tam the Task Manager: bye\n");
//                    System.out.print(dividerText);
//                    return 1;
//
//                default:
//                    System.out.print("Invalid command entered. Type 'help' for list of commands\n");
//                    System.out.print(dividerText);
//                    return 1;
//            }
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
//            return 0;
//        }
//    }
}
