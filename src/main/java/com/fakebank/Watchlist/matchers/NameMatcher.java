package com.fakebank.Watchlist.matchers;

import com.fakebank.Watchlist.entity.SanctionedPerson;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface NameMatcher {

    /**
     * @param fullName Full name to match against sanctioned people
     * @param sanctionedPeople List of people to find a match from
     * @return match with the highest confidence or null if no match could be found
     */
    MatchResult findBestMatch(String fullName, List<SanctionedPerson> sanctionedPeople);

    default String getFullName(SanctionedPerson sanctionedPerson) {
        return getJoinedName(sanctionedPerson.getFirstName(), sanctionedPerson.getMiddleName(), sanctionedPerson.getLastName());
    }

    default String getJoinedName(String... nameParts) {
        return Arrays.stream(nameParts)
                .filter(Objects::nonNull)
                .filter(name -> !name.isBlank())
                .collect(Collectors.joining(" "));
    }

}
