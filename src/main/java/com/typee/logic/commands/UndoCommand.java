package com.typee.logic.commands;

import static java.util.Objects.requireNonNull;

import com.typee.logic.commands.exceptions.CommandException;
import com.typee.model.Model;

/**
 * Undos the last command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Successfully undone!";
    public static final String MESSAGE_FAILURE = "No commands to undo!";

    @Override
    public CommandResult execute(Model model) throws CommandException {

        requireNonNull(model);

        if (model.hasNoUndoableCommand()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoAppointmentList();
        model.updateFilteredEngagementList(Model.PREDICATE_SHOW_ALL_ENGAGEMENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}