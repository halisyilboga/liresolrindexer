package net.semanticmetadata.indexing.controllers;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrCleaner {

	static final HttpSolrServer serverCore0 = new HttpSolrServer(
			"http://localhost:8983/solr/core0");

	public static void main(String[] args) throws IOException,
			InterruptedException, SolrServerException {

		deleteDoc();

	}

	public static void deleteDoc() throws IOException, SolrServerException {
		serverCore0.deleteByQuery("*:*");
		serverCore0.commit();
	}

}
