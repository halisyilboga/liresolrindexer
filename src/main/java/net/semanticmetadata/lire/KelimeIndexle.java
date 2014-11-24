package net.semanticmetadata.lire;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class KelimeIndexle {
	static final HttpSolrServer serverCore0 = new HttpSolrServer(
			"http://localhost:8983/solr/core2");
	public static void main(String[] args) throws SolrServerException, IOException {
		
		String word = "aydın";
		String url="http://www.tdk.gov.tr/index.php?option=com_gts&arama=gts&guid=TDK.GTS.54712abd623e31.67220260";
		SolrInputDocument objectDoc= new SolrInputDocument();
		BufferedReader in = new BufferedReader(new FileReader(new File("kelimeler.txt")));
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		
		query.set("sort","id desc" );
//		query.setFields(field);
		
		QueryResponse response = serverCore0.query(query);
		SolrDocumentList results = response.getResults();
		
		int indexed=0;
		if(results.size()!=0){
			
		Object fieldValue = results.get(0).getFieldValue("id");
		indexed=Integer.parseInt(fieldValue.toString());
		}
		
		 int i = 0;
		 while (in.ready()) {
		  i++;
		  word = in.readLine();
		if(i<=indexed)
		  continue;
		
		 objectDoc.setField("id",i);
		objectDoc.setField("word",word);
		objectDoc.setField("wordreverse",new StringBuilder(word).reverse().toString());
		
			Document doc = Jsoup.connect(url+"&kelime="+word).get();
			Element first = doc.getElementsByClass("main_body").first();
			Elements links = first.getElementsByAttributeValue("id", "hor-minimalist-a");
			
			if(links.size()>0)
			{
				objectDoc.setField("tdk_word",true);
				
			}else{
				
				objectDoc.setField("tdk_word",false);
				
			}
			for (Element element : links) {
				objectDoc.setField("description",element);
			}
			
			 links = first.getElementsByAttributeValue("id", "hor-minimalist-b");
			for (int j = 0; j < links.size(); j+=2) {
				Element element = links.get(j);
				if(element.getElementsByAttributeValue("id", "deyimLabel").first().text().contains("deyim")){
					objectDoc.setField("atasozdeyim",links.get(j).toString());
					if(j+1 < links.size())
						objectDoc.setField("bilesikSozler",links.get(j+1));
					
				}else
					objectDoc.setField("bilesikSozler",links.get(j));
					
			}	
			if(i%100==0)serverCore0.commit();
				
		serverCore0.add(objectDoc);
		
		System.out.println("merhaba dünya ");
		 }
		 serverCore0.commit();
		 in.close();
    }
	
	
	
}
