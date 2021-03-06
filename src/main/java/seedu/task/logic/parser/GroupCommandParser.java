package seedu.task.logic.parser;

import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.task.logic.parser.CliSyntax.KEYWORDS_ARGS_FORMAT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.GroupCommand;
import seedu.task.logic.commands.IncorrectCommand;

//@@author A0164889E
/**
 * Parses input arguments and creates a new GroupCommand object
 */
public class GroupCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the GroupCommand
     * and returns an GroupCommand object for execution.
     */
    public Command parse(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new GroupCommand(keywordSet);
    }

}
//@@author
