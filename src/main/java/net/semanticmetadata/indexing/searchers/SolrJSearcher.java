package net.semanticmetadata.indexing.searchers;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class SolrJSearcher {
	public static void main(String[] args) throws MalformedURLException,
			SolrServerException {
		HttpSolrServer solr = new HttpSolrServer(
				"http://localhost:8983/solr/core0");

		SolrQuery query = new SolrQuery();
		query.setQuery("objectname:house");
		// query.addFilterQuery("cat:electronics", "store:amazon.com");
		query.setFields("parentid", "objectname", "creationdate");
		query.setStart(0);
		query.setRows(200);
		query.set("defType", "edismax");

		QueryResponse response = solr.query(query);
		SolrDocumentList results = response.getResults();
		String str = "";

		ArrayList<String> arrayList = new ArrayList<String>();
		LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
		for (int i = 0; i < results.size(); ++i) {
			SolrDocument solrDocument = results.get(i);
			String parentid = solrDocument.getFieldValue("parentid").toString();
			linkedHashSet.add(parentid);
			// System.out.println("imagename " +
			// solrDocument.getFieldValue("imagename").toString());
			// System.out.println("imagefolder " +
			// solrDocument.getFieldValue("imagefolder").toString());
			// Date date = (Date) solrDocument.getFieldValue("creationdate");
			// System.out.println("creationdate " + date.toString());
			// System.out.println("_________________________________objectname_________________________________________");

			// ArrayList<String> arr = (ArrayList<String>)
			// results.get(i).getFieldValue("objectname");

			// for (String string : arr) {
			// System.out.println("STRING " + string);
			// }
		}

		for (String each : linkedHashSet) {
			// System.out.println(each);

		}

		ArrayList<String> list = new ArrayList<>();
		ArrayList<String> list2 = new ArrayList<>();
		ArrayList<String> list3 = new ArrayList<>();

		ArrayList<ArrayList<String>> lists = new ArrayList<>();
		lists.add(list);
		lists.add(list2);
		lists.add(list3);
		for (int i = 0; i < 100; i++) {
			list.add("" + i);
		}

		for (int i = 0; i < 100; i++) {
			list2.add("" + 2 * i);
		}

		for (int i = 0; i < 100; i++) {
			list3.add("" + 4 * i);
		}

		ArrayList<String> lis = intersect(lists);
		System.out.println("numFound " + lis.size());
		for (int i = 0; i < lis.size(); i++) {
			System.out.println("____ " + lis.get(i));
		}

	}

	private static ArrayList<String> intersect(
			ArrayList<ArrayList<String>> lists) {

		ArrayList<String> arrayList = new ArrayList<>();
		ArrayList<String> arrayListSource = new ArrayList<>();

		boolean isCommon = true;
		if (lists.size() > 0) {
			arrayListSource = lists.get(0);
			for (int index = 0; index < arrayListSource.size(); ++index) {
				for (int i = 1; i < lists.size(); ++i) {
					ArrayList<String> l = lists.get(i);
					if (!l.contains(arrayListSource.get(index))) {
						isCommon = false;
					}
				}

				if (isCommon) {
					arrayList.add(arrayListSource.get(index));
				}
				isCommon = true;
			}
		}

		return arrayList;
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
}