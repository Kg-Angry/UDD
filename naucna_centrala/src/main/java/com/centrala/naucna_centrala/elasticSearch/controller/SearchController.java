package com.centrala.naucna_centrala.elasticSearch.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.centrala.naucna_centrala.DTO.*;
import com.centrala.naucna_centrala.elasticSearch.model.*;
import com.centrala.naucna_centrala.elasticSearch.repository.BookRepository;
import com.centrala.naucna_centrala.model.Korisnik;
import com.centrala.naucna_centrala.model.Naucni_casopis;
import com.centrala.naucna_centrala.model.Naucni_rad;
import com.centrala.naucna_centrala.service.Korisnik_service;
import com.centrala.naucna_centrala.service.Naucni_casopis_service;
import com.centrala.naucna_centrala.service.Naucni_rad_service;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/pretraga")
public class SearchController {

		@Autowired
		private Client nodeClient;

		@Autowired
		private ResultRetriever resultRetriever;

		@Autowired
		private Naucni_rad_service nrs;
		@Autowired
		private Naucni_casopis_service ncs;

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
			String[] kriterijum = {"kljucniPojmovi",
					"autor","sadrzaj","nazivCasopisa","nazivNaucneOblasti","naslovRada"};
			String[] text = {pretragaDTO.getUpit()};

			org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item[] item = {new MoreLikeThisQueryBuilder.Item("libraryserbian", "radovi", "filename")};
			org.elasticsearch.index.query.QueryBuilder query= QueryBuilders.moreLikeThisQuery(kriterijum,text,item)
					.minTermFreq(1).maxQueryTerms(50).minDocFreq(1);
			System.out.println("More like this: " + query);
			List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
			List<ResultData> results = resultRetriever.getResults(query, rh);
			return new ResponseEntity<List<ResultData>>(results, HttpStatus.OK);
		}

		//uzimaju se samo recenzenti iz datog casopisa
//		@PostMapping(value = "/geoProstorna")
//        public ResponseEntity<List<KorisnikDTO>> geoProstorna(@RequestBody Naucni_casopisDTO ncDTO)
//        {
//        	Naucni_casopis n = ncs.findByNaziv(ncDTO.getNaziv());
//        	//Naucni_rad naucni_rad = nrs.findByNaslov(ncDTO.getNaslov());
//			GeoDistanceQueryBuilder rastojanje = new GeoDistanceQueryBuilder("geo_point");
//			List<ResultData> results = new ArrayList<>();
//			List<KorisnikDTO> recenzenti = new ArrayList<>();
//
//        	for(Korisnik k : n.getRecenzent())
//			{
//				double km = GeoDistance.ARC.calculate(32.88, 32.45, k.getLattitude(), k.getLongitude(), DistanceUnit.KILOMETERS);
//				System.out.println("KM: "+ km);
//				rastojanje.point(32.88,32.45).distance(150, DistanceUnit.KILOMETERS);
//				System.out.println(""+rastojanje);
//				List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
//				results = resultRetriever.getResults(rastojanje, rh);
//				System.out.println("Results: " + results);
//				if(!results.isEmpty() && km > 100)
//					recenzenti.add(new KorisnikDTO(k));
//			}
//            return new ResponseEntity<List<KorisnikDTO>>(recenzenti,HttpStatus.OK);
//        }

	// uzimaju se svi recenzenti koji postoje u sistemu
		@PostMapping(value = "/geoProstorna")
		public ResponseEntity<List<KorisnikDTO>> geoProstornaSaSvimRecenzentima(@RequestBody Naucni_radDTO nrDTO)
		{
			List<Naucni_casopis> naucni_casopis = ncs.findAll();
			String ime = nrDTO.getKoautori().split(" ")[0];
			String prezime = nrDTO.getKoautori().split(" ")[1];

			Korisnik autor = korisnikService.findByImeAndPrezime(ime,prezime);
            System.out.println("long: " +autor.getLongitude() +" lat:"+ autor.getLattitude());
			GeoDistanceQueryBuilder rastojanje = new GeoDistanceQueryBuilder("geo_point");
			List<ResultData> results = new ArrayList<>();
			List<KorisnikDTO> recenzenti = new ArrayList<>();

			for(Naucni_casopis nc : naucni_casopis)
			{
				for(Korisnik k : nc.getRecenzent())
				{
					double km = GeoDistance.ARC.calculate(autor.getLattitude(), autor.getLongitude(), k.getLattitude(), k.getLongitude(), DistanceUnit.KILOMETERS);
					System.out.println("KM: "+ km);
					rastojanje.point(autor.getLattitude(),autor.getLongitude()).distance(150, DistanceUnit.KILOMETERS);
					System.out.println(""+rastojanje);
					List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
					results = resultRetriever.getResults(rastojanje, rh);
					System.out.println("Results: " + results);
					if(!results.isEmpty() && km > 100)
					{
                        if(recenzenti.size()==0)
						{
							recenzenti.add(new KorisnikDTO(k));
						}else
						{
							KorisnikDTO korisnikDTO = recenzenti.stream().filter(x -> (x.getIme().equals(k.getIme()) && x.getPrezime().equals(k.getPrezime())))
									.findAny().orElse(null);
							if(korisnikDTO==null)
							{
								recenzenti.add(new KorisnikDTO(k));
							}
						}
					}
				}
			}

			return new ResponseEntity<List<KorisnikDTO>>(recenzenti,HttpStatus.OK);
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
}
