package com.fakebank.Watchlist.matchers;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Name matcher that assumes name is spelled correctly either in regular order or in reverse order
 */
@Component
public class SimpleNameMatcher implements NameMatcher {

    @Override
    public MatchResult findBestMatch(String fullName, List<SanctionedPerson> sanctionedPeople) {
        return sanctionedPeople.stream()
                .filter(sanctionedPerson -> fullName.equalsIgnoreCase(getFullName(sanctionedPerson)))
                .findAny()
                .map(sanctionedPerson -> new MatchResult(sanctionedPerson, 100))
                .orElse(null);
    }

}
