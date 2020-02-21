package com.centrala.naucna_centrala.elasticSearch.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.centrala.naucna_centrala.DTO.NaprednaPretragaDTO;
import com.centrala.naucna_centrala.DTO.Naucni_radDTO;
import com.centrala.naucna_centrala.DTO.PretragaDTO;
import com.centrala.naucna_centrala.elasticSearch.model.*;
import com.centrala.naucna_centrala.elasticSearch.repository.BookRepository;
import com.centrala.naucna_centrala.service.Korisnik_service;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

	public SearchController() throws UnknownHostException {
	}

	@PostMapping(value="/term", consumes="application/json")
		public ResponseEntity<List<ResultData>> searchTermQuery(@RequestBody PretragaDTO searchDTO) throws Exception {
		SimpleQuery simpleQuery = new SimpleQuery();
		simpleQuery.setField(searchDTO.getKriterijum());
		simpleQuery.setValue(searchDTO.getUpit());
		org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.phrase, simpleQuery.getField(), simpleQuery.getValue());
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		List<ResultData> results = resultRetriever.getResults(query, rh);
		return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

//	@PostMapping(value="/term", consumes="application/json")
//	public ResponseEntity<List<ResultData>> searchTermQuery(@RequestBody PretragaDTO searchDTO) throws Exception {
//		SimpleQuery simpleQuery = new SimpleQuery();
//		simpleQuery.setField(searchDTO.getKriterijum());
//		simpleQuery.setValue(searchDTO.getUpit());
//		//org.elasticsearch.index.query.QueryBuilder query= QueryBuilders.termQuery(simpleQuery.getField(), simpleQuery.getValue());
//		org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.regular, simpleQuery.getField(), simpleQuery.getValue());
//		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
////		rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
//		highlightBuilder.highlightQuery(query);
//		SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
//				.setQuery(query)
//				.highlighter(highlightBuilder);
//		SearchResponse response = request.get();
//		System.out.println(response.toString());
//		List<ResultData> results = resultRetriever.getResults(query, rh);
//		return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
//	}

		@PostMapping(value="/search/phrase", consumes="application/json")
		public ResponseEntity<List<Naucni_radDTO>> searchPhrase(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			String field = searchDTO.getKriterijum();
			simpleQuery.setField(field);
			simpleQuery.setValue(searchDTO.getUpit());
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilder.buildQuery(SearchType.phrase, simpleQuery.getField(), simpleQuery.getValue());

			List<Naucni_radDTO> results = new ArrayList<>();

	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
	                .setQuery(query);
	        SearchResponse response = request.get();
	        System.out.println(response.toString());
	        results = resultRetriever.getResponse(response);
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/poSvimPoljima", consumes="application/json")
		public ResponseEntity<List<ResultData>> pretragaSvihPolja(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			if(searchDTO.getKriterijum().equals("")) {
				simpleQuery.setValue(searchDTO.getUpit());
			}
			org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.queryStringQuery(simpleQuery.getValue());
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/all", consumes="application/json")
		public ResponseEntity<List<ResultData>> sviDokumenti(@RequestBody PretragaDTO searchDTO) throws Exception {
			SimpleQuery simpleQuery = new SimpleQuery();
			if(searchDTO.getKriterijum().equals("") && searchDTO.getUpit().equals("")) {
				simpleQuery.setField(searchDTO.getKriterijum());
				simpleQuery.setValue(searchDTO.getUpit());
			}
			org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.matchAllQuery();
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/booleanQuery")
		public ResponseEntity<List<ResultData>> booleanQuery(@RequestBody NaprednaPretragaDTO npDTO){
			List<org.elasticsearch.index.query.QueryBuilder> listQuery = new ArrayList<>();

			if(!npDTO.getVrednost1().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.matchQuery(npDTO.getKriterijum1(), npDTO.getVrednost1());
				listQuery.add(query);
			}
			if(!npDTO.getVrednost2().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query1 = QueryBuilders.matchQuery(npDTO.getKriterijum2(), npDTO.getVrednost2());
				listQuery.add(query1);
			}
			if(!npDTO.getVrednost3().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query2 = QueryBuilders.matchQuery(npDTO.getKriterijum3(), npDTO.getVrednost3());
				listQuery.add(query2);
			}
			if(!npDTO.getVrednost4().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query3 = QueryBuilders.matchQuery(npDTO.getKriterijum4(), npDTO.getVrednost4());
				listQuery.add(query3);
			}
			if(!npDTO.getVrednost5().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query4 = QueryBuilders.matchQuery(npDTO.getKriterijum5(), npDTO.getVrednost5());
				listQuery.add(query4);
			}
			if(!npDTO.getVrednost6().equals(""))
			{
				org.elasticsearch.index.query.QueryBuilder query5 = QueryBuilders.matchQuery(npDTO.getKriterijum6(), npDTO.getVrednost6());
				listQuery.add(query5);
			}

			BoolQueryBuilder builder = QueryBuilders.boolQuery();
			if(npDTO.getOperator().equalsIgnoreCase("AND"))
			{
				for(int i=0;i<listQuery.size();i++)
				{
					builder.must(listQuery.get(i));
				}
			}else if(npDTO.getOperator().equalsIgnoreCase("OR"))
			{
				for(int i=0;i<listQuery.size();i++)
				{
					builder.should(listQuery.get(i));
				}
			}
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			List<ResultData> results = resultRetriever.getResults(builder, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		@PostMapping(value="/moreLike")
		public ResponseEntity<List<ResultData>> moreLikePretraga(@RequestBody PretragaDTO pretragaDTO) throws ParseException {
			String[] kriterijum = {"naslovRada",
					"autor","kljucniPojmovi","nazivCasopisa","nazivNaucneOblasti","sadrzaj"};
			String[] text = {pretragaDTO.getUpit()};

			org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item[] item = {new MoreLikeThisQueryBuilder.Item("libraryserbian", "radovi", "filename")};
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilders.moreLikeThisQuery(kriterijum,text,item)
					.minTermFreq(1).maxQueryTerms(50).minDocFreq(1);
			System.out.println("More like this: " + query);
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}
//		@PostMapping(value="/search/queryParser", consumes="application/json")
//		public ResponseEntity<List<Naucni_radDTO>> search(@RequestBody PretragaDTO searchDTO) throws Exception {
//			SimpleQuery simpleQuery = new SimpleQuery();
//			String kriterijum = "";
//			if(searchDTO.getKriterijum().equals("")) {
//				simpleQuery.setValue(searchDTO.getUpit());
//			}else{
//				kriterijum = searchDTO.getKriterijum();
//				simpleQuery.setField(kriterijum);
//				simpleQuery.setValue(searchDTO.getUpit());
//			}
//			org.elasticsearch.index.query.QueryBuilder query=QueryBuilders.matchQuery(simpleQuery.getField(), simpleQuery.getValue());
//			List<Naucni_radDTO> results = new ArrayList<>();
//
//	        highlightBuilder.highlightQuery(query);
//
//	        SearchRequestBuilder request = nodeClient.prepareSearch("libraryserbian")
//	                .setQuery(query)
//	                .highlighter(highlightBuilder);
//	        SearchResponse response = request.get();
//	        System.out.println(response.toString());
//	        results = resultRetriever.getResponse(response);
//
//			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
//		}

		/*@GetMapping(value="/search/geo")
		public ResponseEntity<List<Naucni_radDTO>> geoSearch() throws Exception {
			Korisnik k = korisnikService.findOne(9L);
			GeoDistanceQueryBuilder qb = new GeoDistanceQueryBuilder("geo_point");

			qb.point(k.getLattitude(), k.getLongitude()).distance(200, DistanceUnit.KILOMETERS);
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();

			List<Naucni_radDTO> results = resultRetriever.getResults(qb, rh);
			return new ResponseEntity<List<Naucni_radDTO>>(results, HttpStatus.OK);
		}*/
}
