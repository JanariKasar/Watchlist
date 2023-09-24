package com.fakebank.Watchlist.service;

import com.fakebank.Watchlist.config.WatchlistProperties;
import com.fakebank.Watchlist.entity.SanctionedPerson;
import com.fakebank.Watchlist.matchers.NameMatcher;
import com.fakebank.Watchlist.matchers.MatchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SanctionsServiceTests {

    @Spy
    @InjectMocks
    private SanctionsService sanctionsService;

    @Mock
    private WatchlistProperties watchlistProperties;

    @Mock
    private NameMatcher nameMatcher;

    @Nested
    public class FindSanctionedMatch {

        @BeforeEach
        public void setUp() {
            doReturn(List.of(nameMatcher)).when(sanctionsService).getNameMatchers();
        }

        @Test
        public void shouldReturnMatchWithHighestConfidence() {
            String inputName = "Bin Laden Osama";
            List<String> cleanedInput = List.of("Bin", "Laden", "Osama");
            doReturn(cleanedInput).when(sanctionsService).cleanInputName(inputName);

            SanctionedPerson sanctionedPerson = mock(SanctionedPerson.class);
            List<SanctionedPerson> sanctionedPeople = List.of(sanctionedPerson);
            doReturn(sanctionedPeople).when(sanctionsService).getActiveSanctionedPeople();

            String permutation1 = "Bin Laden Osama";
            String permutation2 = "Osama Bin Laden";
            String permutation3 = "Bin Osama Laden";
            Set<String> permutations = Set.of(permutation1, permutation2, permutation3);
            doReturn(permutations).when(sanctionsService).getNamePermutations(cleanedInput);

            doReturn(null).when(nameMatcher).findBestMatch(permutation1, sanctionedPeople);
            doReturn(new MatchResult(sanctionedPerson, 95)).when(nameMatcher).findBestMatch(permutation2, sanctionedPeople);
            doReturn(new MatchResult(sanctionedPerson, 50)).when(nameMatcher).findBestMatch(permutation3, sanctionedPeople);

            MatchResult match = sanctionsService.findSanctionedMatch(inputName);

            verify(nameMatcher, times(3)).findBestMatch(anyString(), anyList());

            assertEquals(95, match.getMatchConfidence());
            assertEquals(sanctionedPerson, match.getSanctionedPerson());
        }

    }

    @Nested
    public class CLeanInputNames {

        private static final List<String> CLEAN_RESULT = List.of("Osama", "Bin", "Laden");

        @BeforeEach
        public void setUp() {
            doReturn(false).when(sanctionsService).isNoiseWord(anyString());
        }

        @Test
        public void removeAdditionalWhiteSpace() {
            String input = "Osama  Bin      Laden  ";
            assertEquals(CLEAN_RESULT, sanctionsService.cleanInputName(input));
        }

        @Test
        public void removeNoiseWords() {
            String input = "Osama the Bin Laden";
            doReturn(true).when(sanctionsService).isNoiseWord("the");
            assertEquals(CLEAN_RESULT, sanctionsService.cleanInputName(input));
        }

        @Test
        public void removeLeadingAndTrailingSymbols() {
            String input = "*Osama ~Bin~ Laden!";
            assertEquals(CLEAN_RESULT, sanctionsService.cleanInputName(input));
        }

        @Test
        public void removeSectionsContainingOnlySymbols() {
            String input = "Osama Bin -,.,- Laden :)";
            assertEquals(CLEAN_RESULT, sanctionsService.cleanInputName(input));
        }

    }

    @Nested
    public class IsNoiseWord {

        @BeforeEach
        public void setUp() {
            Set<String> noiseWords = Set.of("the", "to", "an", "mrs", "mr", "and");
            doReturn(noiseWords).when(watchlistProperties).getNoiseWords();
        }

        @Test
        public void shouldReturnTrue() {
            String input = "the";
            assertTrue(sanctionsService.isNoiseWord(input));
        }

        @Test
        public void shouldTrimSymbolsAndReturnTrue() {
            String input = "    .mrs... !' ";
            assertTrue(sanctionsService.isNoiseWord(input));
        }

        @Test
        public void shouldReturnFalse() {
            String input = "a";
            assertFalse(sanctionsService.isNoiseWord(input));
        }

    }
}
