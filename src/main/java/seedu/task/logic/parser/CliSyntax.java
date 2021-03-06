package seedu.task.logic.parser;

import java.util.regex.Pattern;

import seedu.task.logic.parser.ArgumentTokenizer.Prefix;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    //@@author A0164466X-reused
    /* Prefix definitions */
    public static final Prefix PREFIX_GROUP = new Prefix("g/");
    public static final Prefix PREFIX_END_DATE = new Prefix("d/");
    public static final Prefix PREFIX_START_DATE = new Prefix("s/");

    /* Patterns definitions */
    public static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

}
