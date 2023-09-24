package com.fakebank.Watchlist.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "watchlist")
public class WatchlistProperties {

    private Set<String> noiseWords;
    private Integer minAllowedConfidence;

}
