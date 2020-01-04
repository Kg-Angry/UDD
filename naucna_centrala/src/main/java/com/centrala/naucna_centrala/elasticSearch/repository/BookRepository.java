package com.centrala.naucna_centrala.elasticSearch.repository;

import java.awt.print.Pageable;
import java.util.List;

import com.centrala.naucna_centrala.elasticSearch.model.IndexUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<IndexUnit, String> {

	List<IndexUnit> findByNaslovRada(String title);

	IndexUnit findByFilename(String filename);
}
