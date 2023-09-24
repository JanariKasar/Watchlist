package com.fakebank.Watchlist.controller;

import com.fakebank.Watchlist.matchers.MatchResult;
import com.fakebank.Watchlist.service.SanctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sanctions")
public class SanctionsController {

    @Autowired
    private SanctionsService sanctionsService;

    @GetMapping("/match/{name}")
    public MatchResult findMatch(@PathVariable String name) {
        return sanctionsService.findSanctionedMatch(name);
    }

}
