package com.fakebank.Watchlist.config;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import com.fakebank.Watchlist.service.SanctionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@Profile("test")
public class SampleDataConfig {

    @Autowired
    private SanctionsService sanctionsService;

    @Bean
    public void populateSampleSanctionData() {
        log.info("Populating sanctions table with sample data");
        LocalDateTime currentDate = LocalDateTime.now();
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("Osama", "", "Bin Laden", currentDate));
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("Adolf", "", "Hitler", currentDate));
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("Kim", "", "Jong-un", currentDate));
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("Saddam", "", "Hussein", currentDate));
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("John", "Something", "Doe", currentDate));
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        sanctionsService.saveSanctionedPerson(new SanctionedPerson("Expired", "", "Name", currentDate, yesterday));
    }

}
