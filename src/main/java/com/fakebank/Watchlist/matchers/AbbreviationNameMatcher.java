package com.fakebank.Watchlist.matchers;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Name matcher that assumes abbreviation is used to spell middle name
 */
@Component
public class AbbreviationNameMatcher implements NameMatcher {

    @Override
    public MatchResult findBestMatch(String fullName, List<SanctionedPerson> sanctionedPeople) {
        for (SanctionedPerson person : sanctionedPeople) {
            if (person.getMiddleName() == null || person.getMiddleName().isBlank())
                continue;

            String sanctionedName = getJoinedName(person.getFirstName(), person.getMiddleName().substring(0, 1),
                    person.getLastName());
            if (fullName.equalsIgnoreCase(sanctionedName)) {
                return new MatchResult(person, 90);
            }
        }
        return null;
    }

}
