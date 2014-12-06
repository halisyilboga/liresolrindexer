package net.semanticmetadata.indexing.controllers;

/*
 * This file is part of the LIRE project: http://www.semanticmetadata.net/lire LIRE is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * LIRE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with LIRE; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * We kindly ask you to refer the any or one of the following publications in any publication
 * mentioning or employing Lire:
 * 
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval Ã¢â‚¬â€œ An Extensible Java
 * CBIR Library. In proceedings of the 16th ACM International Conference on Multimedia, pp.
 * 1085-1088, Vancouver, Canada, 2008 URL: http://doi.acm.org/10.1145/1459359.1459577
 * 
 * Lux Mathias. Content Based Image Retrieval with LIRE. In proceedings of the 19th ACM
 * International Conference on Multimedia, pp. 735-738, Scottsdale, Arizona, USA, 2011 URL:
 * http://dl.acm.org/citation.cfm?id=2072432
 * 
 * Mathias Lux, Oge Marques. Visual Information Retrieval using Java and LIRE Morgan & Claypool,
 * 2013 URL: http://www.morganclaypool.com/doi/abs/10.2200/S00468ED1V01Y201301ICR025
 * 
 * Copyright statement: -------------------- (c) 2002-2013 by Mathias Lux (mathias@juggle.at)
 * http://www.semanticmetadata.net/lire, http://www.lire-project.net
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.semanticmetadata.indexing.data.MetaDataExtractor;
import net.semanticmetadata.indexing.data.SolrImage;
import net.semanticmetadata.indexing.util.StringUtil;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.JCD;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.imageanalysis.OpponentHistogram;
import net.semanticmetadata.lire.imageanalysis.PHOG;
import net.semanticmetadata.lire.indexing.hashing.BitSampling;
import net.semanticmetadata.lire.solr.SearcherConfig;
import net.semanticmetadata.lire.utils.SerializationUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.codec.binary.Base64;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.drew.imaging.ImageProcessingException;


/**
 * This file is part of LIRE, a Java library for content based image retrieval.
 * 
 * @author Mathias Lux, mathias@juggle.at, 22.06.13
 */



public class AddImages {

  private static ArrayList<String> annotationsPathList = new ArrayList<String>();
  private static ArrayList<String> objectList;

  private static long imageId = 0;
  private static long objectId = 0;
  private static SolrInputDocument imageDoc;
  private static SolrInputDocument objectDoc;

  private static long id = 0;

  public static Options options = new Options();

  static {

    options.addOption("d", false, "clear All index");
    options.addOption("r", false, "resume indexing");
    options.addOption("i", false, "index text info only");
    options.addOption("h", "--help", false, "index text info");


  }



  private static long generateImageId() {
    return ++imageId;
  }

  private static long generateIObjectId() {
    return ++objectId;
  }


  public static void fixIndexOfImages() throws SolrServerException {
    final SolrQuery query = new SolrQuery();

    query.setQuery("imageid:[* TO *]");

    query.set("sort", "imageid desc");
    // query.setFields(field);

    QueryResponse response;

    response = serverCore0.query(query);

    SolrDocumentList results = response.getResults();

    if (results.size() != 0) {

      final Object fieldValue = results.get(0).getFieldValue("imageid");
      imageId = Integer.parseInt(fieldValue.toString());
    }
    // query.setFields(field);
    query.setQuery("objectId:[* TO * ]");

    query.set("sort", "objectId desc");
    // query.setFields(field);
    response = serverCore0.query(query);
    results = response.getResults();

    if (results.size() != 0) {

      final Object fieldValue = results.get(0).getFieldValue("objectId");
      objectId = Integer.parseInt(fieldValue.toString());
    }
    query.setQuery("id:[* TO * ]");

    query.set("sort", "id desc");
    // query.setFields(field);
    response = serverCore0.query(query);
    results = response.getResults();

    if (results.size() != 0) {

      final Object fieldValue = results.get(0).getFieldValue("id");
      setId((Long) fieldValue);
    }


  }

  static final HttpSolrServer serverCore0 = new HttpSolrServer("http://localhost:8983/solr/core0");
  static final HttpSolrServer serverlireq = new HttpSolrServer(
      "http://localhost:8983/solr/core0/lireq");

  public static void main(final String[] args) throws IOException, InterruptedException,
      SolrServerException, org.apache.commons.cli.ParseException {

    final CommandLineParser parser = new GnuParser();
    final CommandLine cmd = parser.parse(options, args);


    if (cmd.hasOption("h")) {

      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("indexer", options);

    } else {

    }


    final String workingDir = SearcherConfig.getProperties().getProperty("imagesRoot");
    final File folder = new File(SearcherConfig.getProperties().getProperty("annotationsRoot"));

    final ArrayList<String> xmlFilePathList = listFilesForFolder(folder);
    final ArrayList<SolrImage> solrImageList = new ArrayList<SolrImage>();

    for (final String path : xmlFilePathList) {
      final SolrImage solrImage = readXML(path);
      solrImageList.add(solrImage);
    }

    try {
      BitSampling.readHashFunctions();
      BitSampling.readHashFunctions();
    } catch (IOException | NullPointerException e) {
      System.err
          .println("Could not read hashes from file when first creating a GenericDocumentBuilder instance.");
      e.printStackTrace();
    }
// @formatter:off
    
    if (cmd.hasOption("r")) {
      
      fixIndexOfImages();

    } 
    
    
    
    
    for (long i = AddImages.generateImageId(); i < solrImageList.size(); ++i) {

      try {
        imageDoc = new SolrInputDocument();
         final SolrImage image = solrImageList.get((int)i);

        final long imageID = i;
        imageDoc.setField("id", getId());
        imageDoc.addField("imageid", "" + imageID);
        imageDoc.addField("title", image.getFileName());
        imageDoc.addField("imagefolder", image.getFolder());
        

        final Collection<SolrInputDocument> solrObjectList = new ArrayList<>();
        for (int j = 0; j < image.getObjectList().size(); j++) {
          final long objectID = generateIObjectId();
          objectDoc = new SolrInputDocument();
          objectDoc.addField("id", getId());
          objectDoc.addField("objectId", objectID);
          objectDoc.addField("parentid", imageID);
          objectDoc.addField("objectname", image.getObjectList().get(j));
          objectDoc.addField("creationdate", image.getCreationDate());
          solrObjectList.add(objectDoc);
        }
        
        serverCore0.add(solrObjectList);
        final File imageOriginal = new File(workingDir+ "/"+image.getFolder()+"/"+image.getFileName());

        try {
          MetaDataExtractor.writeImageExifTags(imageDoc, imageOriginal);
        } catch (ImageProcessingException | IOException e) {
          System.out.println("exif data okumada hata" + e.getMessage());
          e.printStackTrace();
        }
        
        if(!cmd.hasOption("i")){
        final BufferedImage img = ImageIO.read(imageOriginal);
        // features:
        getFields(img, new ColorLayout(), "cl_hi", "cl_ha", imageDoc);
        getFields(img, new EdgeHistogram(), "eh_hi", "eh_ha", imageDoc);
        getFields(img, new JCD(), "jc_hi", "jc_ha", imageDoc);
        getFields(img, new PHOG(), "ph_hi", "ph_ha", imageDoc);
        getFields(img, new OpponentHistogram(), "oh_hi", "oh_ha", imageDoc);

        }
        serverCore0.add(imageDoc);

        if ((i % 100) == 0) {
          serverCore0.commit();
        }

      } catch (final IIOException e) {
        e.printStackTrace();
        continue;
      }
    }
    serverCore0.commit();


  }

  // formatter:on

  public static void fillDataFromXml(final SolrInputDocument doc, final Document parse) {

    final NodeList elementsByTagName = parse.getElementsByTagName("field");
    for (int i = 0; i < elementsByTagName.getLength(); i++) {
      final Element element = (Element) elementsByTagName.item(i);
      final String namedItem = element.getAttribute("name");
      if ((namedItem != null) && namedItem.equals("page")) {
        final String str =
            element.getTextContent().replaceAll("[*!^<>Â®â€�\"&Â«Â£â€¢Â©â– Â»%]", " "); // [^\\wÃ‡Ã§ÃœÃ¼Ä�ÄŸÃ–Ã¶Ä±Ä°]
        System.out.println(str);
      }

    }
  }



  private static void getFields(final BufferedImage img, final LireFeature feature,
      final String histogramField, final String hashesField, final SolrInputDocument doc) {
    feature.extract(img);
    doc.setField(histogramField, Base64.encodeBase64String(feature.getByteArrayRepresentation()));
    doc.setField(hashesField,
        SerializationUtils.arrayToString(BitSampling.generateHashes(feature.getDoubleHistogram())));
    // System.out.println(BitSampling.generateHashes(feature.getDoubleHistogram()));
  }

  public static ArrayList<String> listFilesForFolder(final File folder) {

    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        annotationsPathList.add(fileEntry.getPath());
      }
    }

    return annotationsPathList;
  }

  public static SolrImage readXML(final String xmlFilePath) {

    objectList = new ArrayList<String>();

    try {
      final File xml = new File(xmlFilePath);
      final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      final Document doc = dBuilder.parse(xml);
      doc.getDocumentElement().normalize();

      final NodeList filenameNode = doc.getElementsByTagName("filename");

      final SolrImage solrImage = new SolrImage();
      solrImage.setFileName(StringUtil.strip(filenameNode.item(0).getTextContent()));

      final NodeList folderNode = doc.getElementsByTagName("folder");
      solrImage.setFolder(StringUtil.strip(folderNode.item(0).getTextContent()));

      final NodeList nodes = doc.getElementsByTagName("object");
      for (int i = 0; i < nodes.getLength(); i++) {

        final Node nNode = nodes.item(i);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

          final Element eElement = (Element) nNode;
          objectList.add(StringUtil.strip(eElement.getElementsByTagName("name").item(0)
              .getTextContent()));
          final String date =
              StringUtil.strip(eElement.getElementsByTagName("date").item(0).getTextContent());
          solrImage.setCreationDate(date);
        }
      }
      solrImage.setObjectList(objectList);

      return solrImage;

    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  public static String toUtcDate(final Object dateStr) {
    if (dateStr == null) {
      return null;
    }
    final SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    // Add other parsing formats to try as you like:
    final String[] dateFormats = {"dd-MMM-yyyy H:mm:ss", "yyyy-MM-dd", "MMM dd, yyyy hh:mm:ss Z"};
    for (final String dateFormat : dateFormats) {
      try {

        return out
            .format(new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateStr.toString()));
      } catch (final ParseException ignore) {
        System.out.println(ignore);
      }
    }
    throw new IllegalArgumentException("Invalid date: " + dateStr);
  }

  public static long getId() {
    return ++id;
  }

  public static void setId(final Long id) {
    AddImages.id = id;
  }

}
