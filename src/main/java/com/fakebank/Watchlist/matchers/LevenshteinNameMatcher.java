package com.fakebank.Watchlist.matchers;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Name matcher that uses Levenshtein distance to find difference between input name sanctioned names
 * difference is calculated in percentages to allow more mistakes on longer names.
 */
@Component
public class LevenshteinNameMatcher implements NameMatcher {

    /**
     * amount of match confidence that is decreased per different percentage
     * for example 10% difference with value 2 would decrease confidence from 100 to 80
     */
    private static final Integer DIFFERENCE_PERCENT_CONFIDENCE_VALUE = 2;

    private final LevenshteinDistance distance;

    public LevenshteinNameMatcher() {
        this.distance = new LevenshteinDistance();
    }

    @Override
    public MatchResult findBestMatch(String fullName, List<SanctionedPerson> sanctionedPeople) {
        String nameLowercase = fullName.toLowerCase();
        MatchResult bestMatch = null;
        for (SanctionedPerson sanctionedPerson : sanctionedPeople) {
            String sanctionedName = getFullName(sanctionedPerson).toLowerCase();
            int difference = this.distance.apply(nameLowercase, sanctionedName);
            int confidence = calculateConfidenceFromDifference(sanctionedName.length(), difference);
            if (confidence <= 0)
                continue;

            if (bestMatch == null || confidence > bestMatch.getMatchConfidence()) {
                bestMatch = new MatchResult(sanctionedPerson, confidence);
            }
        }
        return bestMatch;
    }

    private int calculateConfidenceFromDifference(int nameLength, int difference) {
        double differentPercentage = (difference / (double) nameLength) * 100;
        return 100 - (int) Math.round(differentPercentage * DIFFERENCE_PERCENT_CONFIDENCE_VALUE);
    }

}
