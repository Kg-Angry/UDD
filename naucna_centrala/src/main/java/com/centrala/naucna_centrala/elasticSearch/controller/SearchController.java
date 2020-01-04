package com.centrala.naucna_centrala.elasticSearch.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.centrala.naucna_centrala.DTO.Naucni_radDTO;
import com.centrala.naucna_centrala.DTO.PretragaDTO;
import com.centrala.naucna_centrala.elasticSearch.model.*;
import com.centrala.naucna_centrala.elasticSearch.repository.BookRepository;
import com.centrala.naucna_centrala.service.Korisnik_service;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/pretraga")
public class SearchController {

		@Autowired
		private Client nodeClient;

		@Autowired
		private ResultRetriever resultRetriever;
		
		@Autowired
		private Korisnik_service korisnikService;
		
		@Autowired
		private BookRepository elasticRepository;
		
		private HighlightBuilder highlightBuilder = new HighlightBuilder()
		.preTags("<mark>")
		.postTags("</mark>")
	    .field("sadrzaj", 50)
	    .field("naslovRada", 50)
	    .field("autor", 50)
	    .field("apstrakt", 50)
	    .field("kljucniPojmovi", 50)
	    .field("nazivCasopisa", 50)
	    .field("nazivNaucneOblasti", 50);
		
		@PostMapping(value="/term", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> searchTermQuery(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			String field = getFieldNameFromKriterijum(searchDTO.getKriterijum());
			simpleQuery.setField(field);
			//System.out.println("Upit: " + searchDTO.getUpit());
			simpleQuery.setValue(searchDTO.getUpit());
			org.elasticsearch.index.query.QueryBuilder query=  QueryBuilder.buildQuery(SearchType.regular, simpleQuery.getField(), simpleQuery.getValue());

			List<Naucni_radDTO> results = new ArrayList<>();

			highlightBuilder.highlightQuery(query);
			SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
					.setQuery(query)
					.highlighter(highlightBuilder);
			SearchResponse response = request.get();
			System.out.println("GETResponse: " + request.get());
			results = resultRetriever.getResponse(response);
			System.out.println("Uzeo " + results);
			//List<RadDTO> results = resultRetriever.getResults(query, rh);

			return new ResponseEntity<>(results, HttpStatus.OK);
		}



		/*@PostMapping(value="/search/fuzzy", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchFuzzy(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.fuzzy, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			//List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/search/prefix", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchPrefix(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.prefix, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/search/range", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchRange(@RequestBody SimpleQuery simpleQuery) throws Exception {
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.range, simpleQuery.getField(), simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}*/

		@PostMapping(value="/search/phrase", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> searchPhrase(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			String field = getFieldNameFromKriterijum(searchDTO.getKriterijum());
			simpleQuery.setField(field);
			simpleQuery.setValue(searchDTO.getUpit());
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.phrase, simpleQuery.getField(), simpleQuery.getValue());

			List<Naucni_radDTO> results = new ArrayList<>();

	        highlightBuilder.highlightQuery(query);

	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
	                .setQuery(query)
	                .highlighter(highlightBuilder);
	        SearchResponse response = request.get();
	        System.out.println(response.toString());
	        results = resultRetriever.getResponse(response);
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/search/withoutCriteria", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> searchAllFields(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			if(searchDTO.getKriterijum().equals("")) {
				simpleQuery.setValue(searchDTO.getUpit());
			}
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilders.queryStringQuery(simpleQuery.getValue());
			List<Naucni_radDTO> results = new ArrayList<>();

	        highlightBuilder.highlightQuery(query);

	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
	                .setQuery(query)
	                .highlighter(highlightBuilder);
	        SearchResponse response = request.get();
	        System.out.println(response.toString());
	        results = resultRetriever.getResponse(response);		
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}
		
		@PostMapping(value="/search/boolean", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> searchBoolean(@RequestBody PretragaDTO searchDTO) throws Exception {
			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			String query = searchDTO.getUpit();
			if(query.contains("AND") && !query.contains("OR")){
				String[] parsedQueryByAnd = query.split("AND");
				builder = buildBooleanForAnd(parsedQueryByAnd);
			}else if(query.contains("OR") && !query.contains("AND")){
				String[] parsedQueryByOr = query.split("OR");
				builder = buildBooleanForOr(parsedQueryByOr);
			}
			
			List<Naucni_radDTO> results = new ArrayList<>();

	        highlightBuilder.highlightQuery(builder);

	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
	                .setQuery(builder)
	                .highlighter(highlightBuilder);
	        SearchResponse response = request.get();
	        System.out.println(response.toString());
	        results = resultRetriever.getResponse(response);	
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}
		private BoolQueryBuilder buildBooleanForAnd(String[] querySplittedByAnd) throws IllegalArgumentException, ParseException{
			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			org.elasticsearch.index.query.QueryBuilder query = null;
			for(int i = 0; i < querySplittedByAnd.length; i++){
				
					String[] gettingFieldAndValue = querySplittedByAnd[i].split(":");
					String field = gettingFieldAndValue[0].trim();
					String value = gettingFieldAndValue[1].trim();
					query = QueryBuilder.buildQuery(SearchType.regular, field, value);
					builder.must(query);

			}
			
			return builder;
		}
		private BoolQueryBuilder buildBooleanForOr(String[] querySplittedByOr) throws IllegalArgumentException, ParseException{
			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			org.elasticsearch.index.query.QueryBuilder query = null;
			for(int i = 0; i < querySplittedByOr.length; i++){
				String[] gettingFieldAndValue = querySplittedByOr[i].split(":");
				String field = gettingFieldAndValue[0].trim();
				String value = gettingFieldAndValue[1].trim();
				query = QueryBuilder.buildQuery(SearchType.regular, field, value);
				builder.should(query);
			}
			
			return builder;
		}
		
		@PostMapping(value="/search/queryParser", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> search(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			String kriterijum = "";
			if(searchDTO.getKriterijum().equals("")) {
				simpleQuery.setValue(searchDTO.getUpit());
			}else{
				kriterijum = getFieldNameFromKriterijum(searchDTO.getKriterijum());
				simpleQuery.setField(kriterijum);
				simpleQuery.setValue(searchDTO.getUpit());
			}
			org.elasticsearch.index.query.QueryBuilder query=QueryBuilders.matchQuery(simpleQuery.getField(), simpleQuery.getValue());
			List<Naucni_radDTO> results = new ArrayList<>();

	        highlightBuilder.highlightQuery(query);

	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
	                .setQuery(query)
	                .highlighter(highlightBuilder);
	        SearchResponse response = request.get();
	        System.out.println(response.toString());
	        results = resultRetriever.getResponse(response);
			
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}
		
		/*@GetMapping(value="/search/geo")
		public ResponseEntity<List<Naucni_radDTO>> geoSearch() throws Exception {
			Korisnik k = korisnikService.findOne(9L);
			GeoDistanceQueryBuilder qb = new GeoDistanceQueryBuilder("geo_point");
			
			qb.point(k.getLattitude(), k.getLongitude()).distance(200, DistanceUnit.KILOMETERS);
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			
			List<Naucni_radDTO> results = resultRetriever.getResults(qb, rh);
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}*/
		
		@PostMapping(value="/search/download")
		public void downloadPDF(@RequestBody Naucni_radDTO rad, HttpServletResponse response) throws Exception {
			System.out.println(rad.getNaslov());
			/*List<IndexUnit> radFoundByNaslov = elasticRepository.findByNaslovRada(rad.getNaslov());
			String filePath = radFoundByNaslov.get(0).getFilename();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		//OutputStream outputStream = null;
		InputStream in = null;

	        try {
	            in = new FileInputStream(filePath); // I assume files are at /tmp
	            byte[] buffer = new byte[1024];
	            int bytesRead = 0;
	            response.setHeader(
	                "Content-Disposition",
	                "attachment;filename=file.pdf");
	            
	          //  outputStream = response.getOutputStream();
	            while( 0 < ( bytesRead = in.read( buffer ) ) )
	            {
	            	outputStream.write( buffer, 0, bytesRead );
	            }
	            download(outputStream.toByteArray(), "C:/Users/Alexandar/Desktop/" + rad.getNaslov() + ".pdf");
	        }   
	        finally
	        {
	            if ( null != in )
	            {
	                in.close();
	            }
	        }*/
		}
		
		private void download(byte[] content, String dest){
			FileOutputStream fos = null;
			
			try{
				fos = new FileOutputStream(dest);
				fos.write(content);
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				if(fos!=null){
					try{
						fos.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		private String getFieldNameFromKriterijum(String kriterijum) {
			String field = "";
			if(kriterijum.equals("naslovRada")) {
				field = "naslovRada";
			}else if(kriterijum.equals("imeAutora")) {
				field = "imeAutora";
			}else if(kriterijum.equals("prezimeAutora")) {
					field = "prezimeAutora";
			}else if(kriterijum.equals("kljucniPojmovi")) {
				field = "kljucniPojmovi";
			}else if(kriterijum.equals("nazivNaucneOblasti")) {
				field = "nazivNaucneOblasti";
			}else if(kriterijum.equals("nazivCasopisa")) {
				field = "nazivCasopisa";
			}else if(kriterijum.equals("apstrakt")) {
				field = "apstrakt";
			}else if(kriterijum.equals("sadrzaj")) {
				field = "sadrzaj";
			}
			return field;
		}
}
