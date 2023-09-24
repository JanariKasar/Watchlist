package com.fakebank.Watchlist.matchers;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResult implements Comparable<MatchResult> {

    private SanctionedPerson sanctionedPerson;
    private int matchConfidence;

    @Override
    public int compareTo(MatchResult other) {
        return Integer.compare(this.matchConfidence, other.matchConfidence);
    }
}
