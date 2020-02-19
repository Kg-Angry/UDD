package com.centrala.naucna_centrala.elasticSearch.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.centrala.naucna_centrala.DTO.Naucna_oblastDTO;
import com.centrala.naucna_centrala.DTO.Naucni_casopisDTO;
import com.centrala.naucna_centrala.DTO.Naucni_radDTO;
import com.centrala.naucna_centrala.elasticSearch.handler.*;
import com.centrala.naucna_centrala.elasticSearch.repository.BookRepository;
import com.centrala.naucna_centrala.model.Naucna_oblast;
import com.centrala.naucna_centrala.model.Naucni_casopis;
import com.centrala.naucna_centrala.repository.Naucna_oblast_repository;
import com.centrala.naucna_centrala.repository.Naucni_casopis_repository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ResultRetriever {
	
	@Autowired
	private BookRepository repository;
	@Autowired
	private Naucni_casopis_repository ncr;
	@Autowired
	private Naucna_oblast_repository nor;

	public ResultRetriever(){
	}

	public List<ResultData> getResults(org.elasticsearch.index.query.QueryBuilder query,
										  List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}
		
		//highlightBuilder.highlightQuery(query);
		List<ResultData> results = new ArrayList<ResultData>();
       //System.out.println("Repository " + repository.search(query, PageRequest.of(0,10)));
        for (IndexUnit indexUnit : repository.search(query)) {
        	if(indexUnit.getNazivCasopisa() != null){
        		ResultData rd = new ResultData();
				rd.setApstrakt(indexUnit.getApstrakt());
	        	rd.setAutor(indexUnit.getAutor());
	        	rd.setNazivnaucneOblasti(indexUnit.getNazivNaucneOblasti());
	        	rd.setKljucniPojmovi(indexUnit.getKljucniPojmovi());
	        	rd.setNaslovRada(indexUnit.getNaslovRada());
	        	rd.setNazivCasopisa(indexUnit.getNazivCasopisa());
	        	rd.setSadrzaj(indexUnit.getSadrzaj());
	        	results.add(rd);
        	}

		}

		
		return results;
	}
	
	protected DocumentHandler getHandler(String fileName){
		if(fileName.endsWith(".txt")){
			return new TextDocHandler();
		}else if(fileName.endsWith(".pdf")){
			return new PDFHandler();
		}else if(fileName.endsWith(".doc")){
			return new WordHandler();
		}else if(fileName.endsWith(".docx")){
			return new Word2007Handler();
		}else{
			return null;
		}
	}
	
	public List<Naucni_radDTO> getResponse(SearchResponse response) {
		ArrayList<Naucni_radDTO> retVal = new ArrayList<>();

        for(SearchHit hit : response.getHits().getHits()) {
            if(response.getHits().getHits().length <= 0){
            	return null;
            }
			Naucni_radDTO pronadjeniRad = new Naucni_radDTO();
           System.out.println("USAO OVDE U RESPONE");
            String allHighlights = "...";
            String autor = (String) hit.getSourceAsMap().get("autor");
            String apstrakt = (String) hit.getSourceAsMap().get("apstrakt");
            String casopis = (String) hit.getSourceAsMap().get("nazivCasopisa");
            String naucnaOblast = (String) hit.getSourceAsMap().get("nazivNaucneOblasti");
            String keywords = (String) hit.getSourceAsMap().get("kljucniPojmovi");
            String naslov = (String) hit.getSourceAsMap().get("naslovRada");
            System.out.println("apstrakt " + apstrakt);
            if((String) hit.getSourceAsMap().get("nazivCasopisa") != null){
            	//pronadjeniRad.setAutor(autor);
				;
				System.out.println("NAZIV CASOPISA je dobar");
            	pronadjeniRad.setApstrakt(apstrakt);
            	pronadjeniRad.setNaucni_casopis(new Naucni_casopisDTO(ncr.findByNaziv(casopis)));
            	pronadjeniRad.setOblast_pripadanja(new Naucna_oblastDTO(nor.getByNaziv(naucnaOblast)));
            	pronadjeniRad.setKljucni_pojmovi(keywords);
            	pronadjeniRad.setNaslov(naslov);
            	retVal.add(pronadjeniRad);
            }
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()){
                //ne moze da se smesti direktno HighlightField jer je vrednost tipa Text[] i pukne mi serializer
                String value = Arrays.toString(entry.getValue().fragments());
                allHighlights+=value.substring(1, value.length()-1);
                if(casopis!=null){
                	if(entry.getKey().equals("naslovRada")){
                		pronadjeniRad.setNaslov(value.substring(1, value.length()-1)); // da sklonim uglaste zagrade
                	}
                	if(entry.getKey().equals("kljucniPojmovi")){
                		pronadjeniRad.setKljucni_pojmovi(value.substring(1, value.length()-1));
                	}
                	if(entry.getKey().equals("apstrakt")){
                		pronadjeniRad.setApstrakt(value.substring(1, value.length()-1));
                	}
                	/*if(entry.getKey().equals("autor")){
                		//pronadjeniRad.setAutor(value.substring(1, value.length()-1));
                	}*/
                	if(entry.getKey().equals("nazivNaucneOblasti")){
                		pronadjeniRad.setOblast_pripadanja(new Naucna_oblastDTO(nor.getByNaziv(value.substring(1, value.length()-1))));
                	}
                	if(entry.getKey().equals("nazivCasopisa")){
                		pronadjeniRad.setNaucni_casopis(new Naucni_casopisDTO(ncr.findByNaziv(value.substring(1, value.length()-1))));
                	}
                }
                allHighlights+="...";
            }
            
        }
        return retVal;
	}
}
