package com.example.sentiment.repository;

import com.example.sentiment.entities.QueryEntity;
import org.springframework.data.repository.CrudRepository;

public interface QueryRepository extends CrudRepository<QueryEntity, Long> {

    //if querytext exists returns that query
    QueryEntity findByQueryText(String queryText);

}
