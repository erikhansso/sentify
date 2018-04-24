package com.example.sentiment.repository;

import com.example.sentiment.entities.Query;
import org.springframework.data.repository.CrudRepository;

public interface QueryRepository extends CrudRepository<Query, Long> {

    //if querytext exists returns that query
    Query findByQueryText(String queryText);

}
