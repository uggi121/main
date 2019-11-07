package com.typee.logic.interactive.parser.state.calendarstate;

import static com.typee.logic.interactive.parser.CliSyntax.PREFIX_DATE;
import static java.util.Objects.requireNonNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.swing.text.html.Option;

import com.typee.logic.interactive.parser.ArgumentMultimap;
import com.typee.logic.interactive.parser.InteractiveParserUtil;
import com.typee.logic.interactive.parser.Prefix;
import com.typee.logic.interactive.parser.state.PenultimateState;
import com.typee.logic.interactive.parser.state.State;
import com.typee.logic.interactive.parser.state.StateTransitionException;

public class OpenDisplayState extends PenultimateState {

    private static final String MESSAGE_CONSTRAINTS = "Please enter a valid date in the dd/mm/yyyy format, prefixed"
            + " by \"d/\".";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String MESSAGE_MISSING_KEYWORD = "Invalid input! Please enter a valid date after \"d/\" in"
            + " the dd/mm/yyyy format.";
    private static final String MESSAGE_INVALID_INPUT = "Invalid input! The date must adhere to the dd/mm/yyyy format.";

    protected OpenDisplayState(ArgumentMultimap soFar) {
        super(soFar);
    }

    @Override
    public State transition(ArgumentMultimap newArgs) throws StateTransitionException {
        requireNonNull(newArgs);

        Optional<String> date = newArgs.getValue(PREFIX_DATE);
        performGuardChecks(newArgs, date);
        collateArguments(this, newArgs, PREFIX_DATE);

        enforceNoExcessiveArguments(newArgs);

        return new DisplayEndState(soFar);
    }

    private void performGuardChecks(ArgumentMultimap newArgs, Optional<String> date)
            throws StateTransitionException {
        disallowDuplicatePrefix(newArgs);
        requireKeywordPresence(date, MESSAGE_MISSING_KEYWORD);
        enforceValidity(date);
    }

    private void enforceValidity(Optional<String> date) throws StateTransitionException {
        String dateString = date.get();
        if (!isValid(dateString)) {
            throw new StateTransitionException(MESSAGE_INVALID_INPUT);
        }
    }

    private boolean isValid(String date) {
        try {
            LocalDate localDate = InteractiveParserUtil.parseLocalDate(date, DATE_PATTERN);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    @Override
    public String getStateConstraints() {
        return MESSAGE_CONSTRAINTS;
    }

    @Override
    public boolean isEndState() {
        return false;
    }

    @Override
    public Prefix getPrefix() {
        return PREFIX_DATE;
    }
}
