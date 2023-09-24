package com.fakebank.Watchlist.service;

import com.fakebank.Watchlist.config.WatchlistProperties;
import com.fakebank.Watchlist.entity.SanctionedPerson;
import com.fakebank.Watchlist.matchers.NameMatcher;
import com.fakebank.Watchlist.matchers.MatchResult;
import com.fakebank.Watchlist.repository.SanctionRepository;
import com.fakebank.Watchlist.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.fakebank.Watchlist.util.StringUtil.*;

@Service
public class SanctionsService {

    @Autowired
    private SanctionRepository repository;

    @Autowired
    private WatchlistProperties watchlistProperties;

    @Autowired
    private List<NameMatcher> nameMatchers;

    public List<SanctionedPerson> getActiveSanctionedPeople() {
        return repository.findActiveSanctionedEntries();
    }

    public void saveSanctionedPerson(SanctionedPerson sanctionedPerson) {
        repository.save(sanctionedPerson);
    }

    public MatchResult findSanctionedMatch(String fullName) {
        Objects.requireNonNull(fullName);
        if (fullName.isBlank()) {
            return null;
        }
        List<String> cleanedNameSections = cleanInputName(fullName);
        List<SanctionedPerson> sanctionedPeople = getActiveSanctionedPeople();

        MatchResult bestMatch = null;
        for (String namePermutation : getNamePermutations(cleanedNameSections)) {
            for (NameMatcher nameMatcher : getNameMatchers()) {
                MatchResult match = nameMatcher.findBestMatch(namePermutation, sanctionedPeople);
                if (match == null || match.getMatchConfidence() < getMinAllowedConfidence())
                    continue;

                if (bestMatch == null || match.compareTo(bestMatch) > 0) {
                    bestMatch = match;
                    if (bestMatch.getMatchConfidence() == 100) {
                        return bestMatch;
                    }
                }
            }
        }

        return bestMatch;
    }

    public Set<String> getNamePermutations(List<String> cleanedNameSections) {
        return StringUtil.getPermutations(cleanedNameSections);
    }

    /**
     * Cleans input name by doing following:
     *  - Removes noise words like to, the and mr
     *  - Removes leading and trailing non-alphabetic characters from each word
     *  - Removes symbols that are separated from the words
     *
     * @param fullName input name
     * @return List of cleaned name sections
     */
    public List<String> cleanInputName(String fullName) {
        List<String> nameSections = splitStringByWhiteSpaces(fullName);
        nameSections = nameSections.stream()
                .map(StringUtil::trimNonAlphabeticalCharacters)
                .collect(Collectors.toList());
        nameSections.removeIf(this::isNoiseWord);
        nameSections.removeIf(String::isBlank);
        return nameSections;
    }

    public boolean isNoiseWord(String word) {
        String cleanedWord = removeNonAlphabeticalCharacters(word);
        return watchlistProperties.getNoiseWords().stream().anyMatch(s -> s.equalsIgnoreCase(cleanedWord));
    }

    protected Integer getMinAllowedConfidence() {
        return watchlistProperties.getMinAllowedConfidence();
    }

    protected List<NameMatcher> getNameMatchers() {
        return nameMatchers;
    }
}
