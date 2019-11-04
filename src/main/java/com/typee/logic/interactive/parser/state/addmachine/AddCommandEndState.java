package com.typee.logic.interactive.parser.state.addmachine;

import static com.typee.logic.parser.CliSyntax.PREFIX_ATTENDEES;
import static com.typee.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static com.typee.logic.parser.CliSyntax.PREFIX_END_TIME;
import static com.typee.logic.parser.CliSyntax.PREFIX_ENGAGEMENT_TYPE;
import static com.typee.logic.parser.CliSyntax.PREFIX_LOCATION;
import static com.typee.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.time.LocalDateTime;

import com.typee.logic.commands.AddCommand;
import com.typee.logic.commands.Command;
import com.typee.logic.interactive.parser.ArgumentMultimap;
import com.typee.logic.interactive.parser.InteractiveParserUtil;
import com.typee.logic.interactive.parser.state.EndState;
import com.typee.logic.interactive.parser.state.State;
import com.typee.logic.interactive.parser.state.StateTransitionException;
import com.typee.logic.parser.CliSyntax;
import com.typee.logic.interactive.parser.Prefix;
import com.typee.logic.parser.exceptions.ParseException;
import com.typee.model.engagement.AttendeeList;
import com.typee.model.engagement.Engagement;
import com.typee.model.engagement.EngagementType;
import com.typee.model.engagement.Location;
import com.typee.model.engagement.Priority;
import com.typee.model.engagement.TimeSlot;
import com.typee.model.engagement.exceptions.InvalidTimeException;

public class AddCommandEndState extends EndState {

    private static final String MESSAGE_CONSTRAINTS = "Engagement successfully added!";

    protected AddCommandEndState(ArgumentMultimap soFar) {
        super(soFar);
    }

    @Override
    public Command buildCommand() throws ParseException {
        EngagementType engagementType = InteractiveParserUtil.parseType(soFar.getValue(PREFIX_ENGAGEMENT_TYPE).get());
        LocalDateTime startTime = InteractiveParserUtil.parseTime(soFar.getValue(PREFIX_START_TIME).get());
        LocalDateTime endTime = InteractiveParserUtil.parseTime(soFar.getValue(PREFIX_END_TIME).get());
        AttendeeList attendees = InteractiveParserUtil.parseAttendees(soFar.getValue(PREFIX_ATTENDEES).get());
        Location location = InteractiveParserUtil.parseLocation(soFar.getValue(PREFIX_LOCATION).get());
        String description = InteractiveParserUtil.parseDescription(soFar.getValue(PREFIX_DESCRIPTION).get());
        Priority priority = InteractiveParserUtil.parsePriority(soFar.getValue(CliSyntax.PREFIX_PRIORITY).get());
        TimeSlot timeSlot = new TimeSlot(startTime, endTime);

        try {
            Engagement engagement = Engagement.of(engagementType, timeSlot, attendees, location, description, priority);
            return new AddCommand(engagement);
        } catch (InvalidTimeException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public State transition(ArgumentMultimap newArgs) throws StateTransitionException {
        throw new StateTransitionException(MESSAGE_END_STATE);
    }

    @Override
    public String getStateConstraints() {
        return MESSAGE_CONSTRAINTS;
    }

    @Override
    public boolean isEndState() {
        return true;
    }

    @Override
    public Prefix getPrefix() {
        return new Prefix("");
    }
}
