package net.semanticmetadata.indexing.controllers;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.indexing.hashing.BitSampling;
import net.semanticmetadata.lire.utils.SerializationUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchImages {

  private static JTabbedPane tp = new JTabbedPane();

  static final HttpSolrServer serverlireq = new HttpSolrServer("http://localhost:8983/solr/core0/");

  public static void main(final String[] args) throws IOException, InterruptedException,
      SolrServerException {

    BitSampling.readHashFunctions();

    searchDoc(new File(
        "/Users/halisyilboga/Desktop/works/SUN2012/Images/a/abbey/sun_aaalbzqrimafwbiv.jpg"),
        new ColorLayout(), "cl_ha");

  }

  public static void searchDoc(final File queryImageFile, final LireFeature feature,
      final String field) throws IOException, SolrServerException {
    final LireFeature lireFeature = feature;
    final BufferedImage img = ImageIO.read(queryImageFile);
    lireFeature.extract(img);
    final String encodedLireFeature =
        Base64.encodeBase64String(lireFeature.getByteArrayRepresentation());
    final SolrQuery query = new SolrQuery();
    query.set("hashes", SerializationUtils.arrayToString(BitSampling.generateHashes(lireFeature
        .getDoubleHistogram())));
    query.set("feature", encodedLireFeature);
    query.setFields(field);
    query.setFields("version=3");
    query.setRequestHandler("/lireq");

    final QueryResponse response = serverlireq.query(query);
    final ArrayList<LinkedHashMap<String, String>> returnDocList =
        (ArrayList<LinkedHashMap<String, String>>) response.getResponse().get("docs");

    for (int i = 0; i < 7; i++) {

      System.out.println("id: " + returnDocList.get(i).get("imageid"));
      System.out.println("title: " + returnDocList.get(i).get("title"));
      System.out.println("distance: " + String.valueOf(returnDocList.get(i).get("d")));
      System.out
          .println("_________________________________________________________________________");
      final ImageIcon icon = new ImageIcon(returnDocList.get(i).get("id"));


      final JPanel panel = new JPanel();
      final BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
      panel.setLayout(boxLayout);

      panel.add(new JLabel("d: " + String.valueOf(returnDocList.get(i).get("d"))),
          BorderLayout.SOUTH);
      panel.add(new JLabel(icon), BorderLayout.CENTER);
      tp.addTab(returnDocList.get(i).get("title"), panel);

    }

    final JFrame frame = new JFrame();
    tp.setTabPlacement(SwingConstants.LEFT);
    frame.add(tp);
    frame.setVisible(true);
    frame.setSize(800, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

}
