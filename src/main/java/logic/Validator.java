package logic;

import commands.Commands;
import exceptions.*;
import tasks.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static logic.Extractor.extractDeadlineParameters;
import static logic.Extractor.extractEventParameters;

public class Validator {
    public static void validateCommand(Commands commandType, String command, TaskList existingTaskList) throws Exception {
        switch (commandType) {
        case BYE:
            validateByeCommand(command);
            break;
        case LIST:
            validateListCommand(command);
            break;
        case MARK:
            validateMarkCommand(command, existingTaskList);
            break;
        case UNMARK:
            validateUnmarkCommand(command, existingTaskList);
            break;
        case DELETE:
            validateDeleteCommand(command, existingTaskList);
            break;
        case TODO:
            validateTodoCommand(command);
            break;
        case DEADLINE:
            validateDeadlineCommand(command);
            break;
        case EVENT:
            validateEventCommand(command);
            break;
        case FIND:
            validateFindCommand(command);
            break;
        case HELP:
            validateHelpCommand(command);
            break;
        default:
            throw new CommandNotFoundException(ErrorMessages.COMMAND_NOT_FOUND);
        }
    }
    public static void validateByeCommand(String command) throws CommandNotFoundException {
        // command contains more than the 'bye' word
        if (command.split(" ").length > 1) {
            throw new CommandNotFoundException(ErrorMessages.COMMAND_NOT_FOUND);
        }
    }

    public static void validateListCommand(String command) throws CommandNotFoundException {
        // command contains more than the 'list' word
        if (command.split(" ").length > 1) {
            throw new CommandNotFoundException(ErrorMessages.COMMAND_NOT_FOUND);
        }
    }

    public static void validateMarkCommand(String command, TaskList existingTaskList)
            throws IncorrectParametersException, NumberFormatException {
        // command contains more than the 'mark' word and another String
        if (command.split(" ").length > 2) {
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }
        try {
            int taskNum = Integer.parseInt(command.split(" ")[1]);
            if (taskNum > existingTaskList.getNumTasks()) {
                // command task number does not exist
                throw new IndexOutOfBoundsException(ErrorMessages.TASK_NUMBER_DOES_NOT_EXIST);
            }
        } catch (NumberFormatException e) {
            // command task number cannot be parsed into an int
            throw new NumberFormatException(ErrorMessages.TASK_NUMBER_PARSE_ERROR);
        }
    }

    public static void validateUnmarkCommand(String command, TaskList existingTaskList)
            throws IncorrectParametersException, NumberFormatException {
        // command contains more than the 'unmark' word and another String
        if (command.split(" ").length > 2) {
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }
        try {
            int taskNum = Integer.parseInt(command.split(" ")[1]);
            if (taskNum > existingTaskList.getNumTasks()) {
                // command task number does not exist
                throw new IndexOutOfBoundsException(ErrorMessages.TASK_NUMBER_DOES_NOT_EXIST);
            }
        } catch (NumberFormatException e) {
            // command task number cannot be parsed into an int
            throw new NumberFormatException(ErrorMessages.TASK_NUMBER_PARSE_ERROR);
        }
    }

    public static void validateDeleteCommand(String command, TaskList existingTaskList)
            throws IncorrectParametersException, NumberFormatException {
        // command contains more than the 'delete' word and another string
        if (command.split(" ").length > 2) {
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }
        try {
            int taskNum = Integer.parseInt(command.split(" ")[1]);
            if (taskNum > existingTaskList.getNumTasks()) {
                // command task number does not exist
                throw new IndexOutOfBoundsException(ErrorMessages.TASK_NUMBER_DOES_NOT_EXIST);
            }
        } catch (NumberFormatException e) {
            // command task number cannot be parsed into an int
            throw new NumberFormatException(ErrorMessages.TASK_NUMBER_PARSE_ERROR);
        }
    }

    public static void validateDeleteAllCommand(String command)
            throws IncorrectParametersException {
        // command contains more than the 'delete' and 'all' words
        if (command.split(" ").length > 2) {
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }
    }

    public static void validateTodoCommand(String command)
            throws MissingParametersException, IncorrectParametersException {
        String todoDescription = command.replace("todo", "").trim();
        if (todoDescription.equals("")) {
            throw new MissingParametersException(ErrorMessages.MISSING_TASK_DESCRIPTION);
        }
        if (todoDescription.contains("/by")) {
            throw new IncorrectParametersException(ErrorMessages.DUE_DATE_NOT_NEEDED);
        }
        if (todoDescription.contains("/from") || todoDescription.contains("/to")) {
            throw new IncorrectParametersException(ErrorMessages.FROM_TO_DATE_NOT_NEEDED);
        }
    }

    public static void validateDeadlineCommand(String command)
            throws MissingParametersException, IncorrectParametersException, DateTimeParseException, ParseDateException {
        String[] splitCommand = command.split(" ");
        if (!command.contains("/by")) {
            throw new MissingParametersException(ErrorMessages.MISSING_DUE_DATE);
        }
        if (command.contains("/from") || command.contains("/to")) {
            throw new IncorrectParametersException(ErrorMessages.FROM_TO_DATE_NOT_NEEDED);
        }

        if (splitCommand.length < 4) {
            // command has less than required parameters
            // requirement: deadline (description) /by (date) -> 4 parameters
            throw new MissingParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }

        int secondLastIndex = splitCommand.length - 2;
        // command has more than one parameter representing due date
        if (!splitCommand[secondLastIndex].equals("/by")) {
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }
        try {
            String[] parameters = new String[2];
            parameters = extractDeadlineParameters(command);
            String deadlineDescription = parameters[0];
            String deadlineDueDate = parameters[1];
            if (deadlineDescription.isEmpty()) {
                throw new MissingParametersException(ErrorMessages.MISSING_TASK_DESCRIPTION);
            }
            LocalDate deadlineDueDateLocal = LocalDate.parse(deadlineDueDate);
        } catch (DateTimeParseException e) {
            throw new ParseDateException(ErrorMessages.INCORRECT_DATE_FORMAT);
        }
    }

    public static void validateEventCommand(String command)
            throws MissingParametersException, IncorrectParametersException, DateTimeParseException, ParseDateException {
        String[] splitCommand = command.split(" ");
        if (!command.contains("/from") || !command.contains("/to")) {
            throw new MissingParametersException(ErrorMessages.MISSING_FROM_TO_DATE);
        }
        if (command.contains("/by")) {
            throw new IncorrectParametersException(ErrorMessages.DUE_DATE_NOT_NEEDED);
        }

        if (splitCommand.length < 6) {
            // command has less than required parameters
            // requirement: event (description) /from (date) /to (date)
            throw new MissingParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }

        int secondLastIndex = splitCommand.length - 2;
        int fourthLastIndex = splitCommand.length - 4;
        if (!splitCommand[secondLastIndex].equals("/to") || !splitCommand[fourthLastIndex].equals("/from")) {
            // command has more or less than one parameter representing to date
            // or more or less than one parameter representing from date
            throw new IncorrectParametersException(ErrorMessages.INCORRECT_PARAMETERS);
        }

        try {
            String[] parameters = new String[3];
            parameters = extractEventParameters(command);
            String eventDescription = parameters[0];
            String eventFrom = parameters[1];
            String eventTo = parameters[2];
            if (eventDescription.isEmpty()) {
                throw new MissingParametersException(ErrorMessages.MISSING_TASK_DESCRIPTION);
            }
            if (eventFrom.isEmpty() || eventTo.isEmpty()) {
                throw new MissingParametersException(ErrorMessages.MISSING_FROM_TO_DATE);
            }
            LocalDate eventFromDateLocal = LocalDate.parse(eventFrom);
            LocalDate eventToDateLocal = LocalDate.parse(eventTo);
        } catch (DateTimeParseException e) {
            throw new ParseDateException(ErrorMessages.INCORRECT_DATE_FORMAT);
        }
    }

    public static void validateFindCommand(String command) {
        // no validation needed
    }

    public static void validateHelpCommand(String command) throws CommandNotFoundException {
        // command contains more than the 'list' word
        if (command.split(" ").length > 1) {
            throw new CommandNotFoundException(ErrorMessages.COMMAND_NOT_FOUND);
        }
    }

}
