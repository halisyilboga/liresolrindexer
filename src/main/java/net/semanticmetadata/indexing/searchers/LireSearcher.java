package net.semanticmetadata.indexing.searchers;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;

import net.semanticmetadata.indexing.data.SolrImage;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.JCD;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.imageanalysis.OpponentHistogram;
import net.semanticmetadata.lire.imageanalysis.PHOG;
import net.semanticmetadata.lire.indexing.hashing.BitSampling;
import net.semanticmetadata.lire.utils.SerializationUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;



public class LireSearcher {

	private JTabbedPane tp = new JTabbedPane();
	private HttpSolrServer serverlireq = new HttpSolrServer(
			"http://localhost:8983/solr/core0/");
	private String path;
	private int type;

	public LireSearcher(String path, int type) {
		this.path = path;
		this.type = type;

	}

	private static ArrayList<String> intersect(ArrayList<String> list1,
			ArrayList<String> list2) {

		ArrayList<String> arrayList = new ArrayList<>();

		for (String string : list1) {
			if (list2.contains(string)) {
				arrayList.add(string);
			}
		}

		return arrayList;
	}

	public ArrayList<SolrImage> searchDocs() {
		try {
			System.out.println(path);
			BitSampling.readHashFunctions();

			switch (type) {
			case 0:
				return searchDoc(new File(path.substring(6)),
						new ColorLayout(), "cl_ha");

			case 1:
				return searchDoc(new File(path.substring(6)), new PHOG(),
						"ph_ha");

			case 2:
				return searchDoc(new File(path.substring(6)),
						new OpponentHistogram(), "oh_ha");

			case 3:
				return searchDoc(new File(path.substring(6)),
						new EdgeHistogram(), "eh_ha");

			case 4:
				return searchDoc(new File(path.substring(6)), new JCD(),
						"jc_ha");

			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return new ArrayList<SolrImage>();
	}

	private ArrayList<SolrImage> searchDoc(File queryImageFile,
			LireFeature feature, String field) throws IOException,
			SolrServerException {
		LireFeature lireFeature = feature;
		BufferedImage img = ImageIO.read(queryImageFile);
		lireFeature.extract(img);
		String encodedLireFeature = Base64.encodeBase64String(lireFeature
				.getByteArrayRepresentation());
		SolrQuery query = new SolrQuery();

		query.set("hashes", SerializationUtils.arrayToString(BitSampling
				.generateHashes(lireFeature.getDoubleHistogram())));
		query.set("feature", encodedLireFeature);
		query.set("field", field);
		query.setFields("version=3");
		//query.setStart(ApplicationManager.getInstance().startLireIndex);
		// TODO
		// query.setRows(ApplicationManager.getInstance().shownLireRowCount);
		query.setRows(100000);
		query.setStart(60);
		query.setRequestHandler("/lireq");
		System.out
				.println("_______________________________________________________________________________________________");
		System.out.println("query: " + query);
		System.out
				.println("_______________________________________________________________________________________________");

		
		QueryResponse response = serverlireq.query(query);

		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("response.getQTime();" + response.getQTime());
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("-											-");
		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		System.out.println("---------------------------------------------");
		ArrayList<LinkedHashMap<String, String>> returnDocList = (ArrayList<LinkedHashMap<String, String>>) response
				.getResponse().get("docs");

		ArrayList<SolrImage> solrImages = new ArrayList<>();

		for (int i = 0; i < returnDocList.size(); i++) {
			SolrImage solrImage = new SolrImage();
			solrImage.setFileName(returnDocList.get(i).get("title"));
			solrImage.setId(returnDocList.get(i).get("id"));

			solrImages.add(solrImage);

		}

		return solrImages;
	}

}
