package com.fakebank.Watchlist.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests using sample data defined in {@link com.fakebank.Watchlist.config.SampleDataConfig}
 */
@SpringBootTest
@ActiveProfiles("test")
public class SanctionsServiceIntegrationTests {

    @Autowired
    private SanctionsService sanctionsService;

    @Test
    public void findSanctionedMatch_matchCorrectSpelling() {
        assertNotNull(sanctionsService.findSanctionedMatch("Osama Bin Laden"));
    }

    @Test
    public void findSanctionedMatch_matchLowerCase() {
        assertNotNull(sanctionsService.findSanctionedMatch("osama bin laden"));
    }

    @Test
    public void findSanctionedMatch_matchIncorrectOrder() {
        assertNotNull(sanctionsService.findSanctionedMatch("Laden Osama Bin"));
    }

    @Test
    public void findSanctionedMatch_matchWithLeadingAndTrailingSymbols() {
        assertNotNull(sanctionsService.findSanctionedMatch("*Bin Laden, Osama!"));
    }

    @Test
    public void findSanctionedMatch_matchMissingCharacters() {
        assertNotNull(sanctionsService.findSanctionedMatch("Osama Laden"));
    }

    @Test
    public void findSanctionedMatch_matchContainingNoiseWords() {
        assertNotNull(sanctionsService.findSanctionedMatch("to the osama bin laden"));
    }

    @Test
    public void findSanctionedMatch_matchTypos() {
        assertNotNull(sanctionsService.findSanctionedMatch("Ben Osama Ladn"));
    }

    // @Test TODO
    public void findSanctionedMatch_matchSoundingSimilar() {
        assertNotNull(sanctionsService.findSanctionedMatch("Ladn the Asoma"));
    }

    @Test
    public void findSanctionedMatch_matchMiddleNameAbbreviation() {
        assertNotNull(sanctionsService.findSanctionedMatch("John S Doe"));
    }

}
