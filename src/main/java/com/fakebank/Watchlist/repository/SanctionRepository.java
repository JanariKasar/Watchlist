package com.fakebank.Watchlist.repository;

import com.fakebank.Watchlist.entity.SanctionedPerson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "sanctions", path = "sanctions")
public interface SanctionRepository extends CrudRepository<SanctionedPerson, Long> {

    @Query("SELECT s FROM SanctionedPerson s WHERE CURRENT_TIMESTAMP BETWEEN s.startDate AND IFNULL(s.endDate, CURRENT_TIMESTAMP)")
    List<SanctionedPerson> findActiveSanctionedEntries();

}
