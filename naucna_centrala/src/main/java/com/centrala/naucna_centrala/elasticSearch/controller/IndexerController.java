package com.centrala.naucna_centrala.elasticSearch.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.centrala.naucna_centrala.DTO.Naucni_radDTO;
import com.centrala.naucna_centrala.elasticSearch.model.Indexer;
import com.centrala.naucna_centrala.elasticSearch.model.ResultRetriever;
import com.centrala.naucna_centrala.repository.Naucni_rad_repository;
import com.centrala.naucna_centrala.service.Korisnik_service;
import com.centrala.naucna_centrala.service.Naucna_oblast_service;
import com.centrala.naucna_centrala.service.Naucni_casopis_service;
import com.centrala.naucna_centrala.service.Naucni_rad_service;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/index")
public class IndexerController {
	
		@Autowired
		Naucni_rad_service radService;
		
		@Autowired
		Naucni_rad_repository radRepository;
		
		@Autowired
		Korisnik_service korisnikService;
		
		@Autowired
		private Naucni_casopis_service casopisService;
		
		@Autowired
		private ResultRetriever resultRetriever;
		
		@Autowired
		private Naucna_oblast_service naucnaOblastService;

		private static String DATA_DIR_PATH = "upload";
	
		/*static {
			ResourceBundle rb=ResourceBundle.getBundle("application");
			DATA_DIR_PATH=rb.getString("dataDir");
		}*/
		
		@Autowired
		private Indexer indexer;
		
		@GetMapping("/reindex")
		public ResponseEntity<String> index() throws IOException {
			File dataDir = getResourceFilePath(DATA_DIR_PATH);
			long start = new Date().getTime();
			int numIndexed = indexer.index(dataDir);
			long end = new Date().getTime();
			String text = "Indexing " + numIndexed + " files took "
					+ (end - start) + " milliseconds";
			return new ResponseEntity<String>(text, HttpStatus.OK);
		}
		
		private File getResourceFilePath(String path) {
		    URL url = this.getClass().getClassLoader().getResource(path);
		    File file = null;
		    try {
		        file = new File(url.toURI());
		    } catch (URISyntaxException e) {
		        file = new File(url.getPath());
		    }   
		    return file;
		}

		/*@RequestMapping(value = "/add", method = RequestMethod.POST)
	    public ResponseEntity<String> multiUploadFileModel(@ModelAttribute Naucni_radDTO model) throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {

	    	
	        try {
	        	  if(model.getNaslovRada().equals("") || model.getNaslovRada()==null){
	        		indexFileForMoreLikeThis(model.getFiles());
	        	}
	        	indexUploadedFile(model);

	        } catch (IOException e) {
	            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<String>("Successfully uploaded!", HttpStatus.OK);

	    }*/
		    
		    
	    //save file
	    /*private String saveUploadedFile(MultipartFile file) throws IOException {
	    	String retVal = null;
            if (! file.isEmpty()) {
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(getResourceFilePath(DATA_DIR_PATH).getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.write(path, bytes);
	            retVal = path.toString();
            }
            return retVal;
	    }*/
	    
	   /* private void indexUploadedFile(RadDTO model) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException{
	    	Korisnik k = korisnikService.getCurrentUser();	
			String decryptedString = Kriptovanje.decrypt(k.getEmail());
			String fileName = "";
			
	    	for (MultipartFile file : model.getFiles()) {

	            if (file.isEmpty()) {
	                continue; //next please
	            }
	            fileName = saveUploadedFile(file);
	            if(fileName != null){
	            	IndexUnit indexUnit = indexer.getHandler(fileName).getIndexUnit(new File(fileName));
	            	indexUnit.setTitle(model.getNaslovRada());
	            	indexUnit.setKeywords(model.getKljucneReci());
	            	indexUnit.setApstrakt(model.getApstrakt());
	            	indexUnit.setAutor(k.getIme() + " " + k.getPrezime());
	            	indexUnit.setNazivCasopisa(model.getCasopisRada());
	            	indexUnit.setNazivNaucneOblasti(model.getNaucnaOblast());
	            	org.springframework.data.elasticsearch.core.geo.GeoPoint geopoint = new org.springframework.data.elasticsearch.core.geo.GeoPoint(k.getLattitude(),k.getLongitude());
	            	indexUnit.setGeo_point(geopoint);
	            	indexer.add(indexUnit);
	            	Rad rad = new Rad();
	            	rad.setKljucniPojmovi(model.getKljucneReci());
	            	rad.setAutor(k);
	            	rad.setNaslov(model.getNaslovRada());
	            	NaucnaOblast naucnaObl = naucnaOblastService.findByNaziv(model.getNaucnaOblast());
	            	if(naucnaObl!=null) {
	            		rad.setNaucnaOblast(naucnaObl);
	            	}
	            	Casopis casopis = casopisService.findByNaziv(model.getCasopisRada());
	            	if(casopis!=null) {
	            		rad.setCasopis(casopis);
	            	}
	            	rad.setFajlName(fileName);
	            	rad.setApstrakt(model.getApstrakt());
	            	radService.save(rad);

	            }
	    	}
	    }*/
	    /*@PostMapping("/uploadForMoreLikeThis")
	    public ResponseEntity<List<RadDTO>> uploadFileForMoreLikeThis(@ModelAttribute RadDTO model) throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
	    	IndexUnit addedIU = null;
	        try {
	       		addedIU = indexFileForMoreLikeThis(model.getFiles());
	       		if(addedIU!=null){
	       			return searchMoreLikeThis(addedIU);
	       		}
	       		
	       	} catch (IOException e) {
	            return new ResponseEntity<List<RadDTO>>(HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<List<RadDTO>>(HttpStatus.OK);

	    }
	    private IndexUnit indexFileForMoreLikeThis(MultipartFile[] files) throws IOException{
	    	String fileName = "";
	    	
	    	for (MultipartFile file : files) {

	            if (file.isEmpty()) {
	                continue; 
	            }
	            fileName = saveUploadedFile(file);
	            if(fileName != null){
	            	IndexUnit indexUnit = indexer.getHandler(fileName).getIndexUnit(new File(fileName));
	            	indexer.add(indexUnit);
	            	return indexUnit;
	            }
	    	}
	    	return null;
	    }
	    private ResponseEntity<List<RadDTO>> searchMoreLikeThis(IndexUnit documentToCompare){
	    	String[] fields = {"text"};
	   
	    	
	    	MoreLikeThisQueryBuilder.Item [] items = new MoreLikeThisQueryBuilder.Item[1];
	    	items[0] = new MoreLikeThisQueryBuilder.Item("libraryserbian","radovi",documentToCompare.getFilename());
	    	MoreLikeThisQueryBuilder query = QueryBuilders.moreLikeThisQuery(fields, null, items)
	    			.minTermFreq(1)
	    			.minDocFreq(1)
	    			.minimumShouldMatch("40%");
	    	List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
	    	List<RadDTO> results = resultRetriever.getResults(query,rh);
	    
	    	return new ResponseEntity<List<RadDTO>>(results, HttpStatus.OK);
	    }
	*/
}
