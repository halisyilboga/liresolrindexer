package net.semanticmetadata.lire.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.swing.ProgressMonitor;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.bovw.LocalFeatureHistogramBuilder;
import net.semanticmetadata.lire.imageanalysis.bovw.SurfFeatureHistogramBuilder;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.impl.SurfDocumentBuilder;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.util.FileUtils;



public class SearcherConfig {

  final static Properties prop = new Properties();

  static {

    try {
      prop.load(new FileInputStream("./config.properties"));
    }
    catch (final IOException e) {
      System.out.println("Cannot read config.properties file.");
    }
  }



  public static final void main(final String[] args) {

    final String property = System.getProperty("user.dir");
    System.out.println(property);
    if ((args.length == 2) && "index".equals(args[0])) {

      if ("index".equals(args[0])) {
        try {
          createIndex(args[1]);
        }
        catch (final FileNotFoundException e) {
          e.printStackTrace();
          System.exit(1);
        }
        catch (final IOException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
      else {
        printHelp();
      }
    }
    else if (args.length == 1) {
      if ("import".equals(args[0])) {
        try {
          importIndex();
        }
        catch (final IOException e) {
          e.printStackTrace();
          System.exit(1);
        }
        catch (final SolrServerException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
      else if ("visualwords".equals(args[0])) {
        try {
          visualWords();
        }
        catch (final IOException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
      else {
        printHelp();
      }
    }
    else {
      printHelp();
    }
  }

  private static void createIndex(final String imagesFile) throws FileNotFoundException,
      IOException {
    final int numberOfThreads = Integer.parseInt(getProperties().getProperty("numberOfThreads"));
    final ParallelIndexer indexer =
        new ParallelIndexer(numberOfThreads, "index", new File(imagesFile)) {
          @Override
          public void addBuilders(final ChainedDocumentBuilder builder) {
            builder.addBuilder(new SurfDocumentBuilder());
            builder.addBuilder(new GenericDocumentBuilder(ColorLayout.class,
                DocumentBuilder.FIELD_NAME_COLORLAYOUT, true));
          }
        };
    indexer.run();

    System.out.println("Indexing finished");
    System.out.println("Creating visual words...");

    final IndexReader ir = DirectoryReader.open(FSDirectory.open(new File("index")));
    LocalFeatureHistogramBuilder.DELETE_LOCAL_FEATURES = false;
    final int numDocsForVocabulary =
        Integer.parseInt(getProperties().getProperty("numDocsForVocabulary"));
    final int numClusters = Integer.parseInt(getProperties().getProperty("numClusters"));
    final SurfFeatureHistogramBuilder sh =
        new SurfFeatureHistogramBuilder(ir, numDocsForVocabulary, numClusters);
    sh.setProgressMonitor(new ProgressMonitor(null, "", "", 0, 100));
    sh.index();
    System.out.println("Creating visual words finished.");
    System.out.println("Now you can import data to solr by typing.");
    System.out.println("java -jar indexer.jar import");
  }

  private static void importIndex() throws IOException, SolrServerException {
    final Properties prop = getProperties();
    final String solrCoreData = prop.getProperty("solrCoreData");
    System.out.println("Copying clusters-surf.dat to " + solrCoreData);
    FileUtils
        .copyFile(new File("clusters-surf.dat"), new File(solrCoreData + "/clusters-surf.dat"));

    final String url = prop.getProperty("solrCoreUrl");
    System.out.println("Load data to: " + url);
    final SolrServer server = new HttpSolrServer(url);

    final Collection<SolrInputDocument> buffer = new ArrayList<>(30);

    final IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("index")));
    for (int i = 0; i < reader.maxDoc(); ++i) {
      final Document doc = reader.document(i);
      final SolrInputDocument inputDoc = new SolrInputDocument();
      // ID
      inputDoc.addField("id", doc.getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue());
      // ColorLayout
      final BytesRef clHiBin = doc.getField(DocumentBuilder.FIELD_NAME_COLORLAYOUT).binaryValue();
      inputDoc.addField("cl_hi", ByteBuffer.wrap(clHiBin.bytes, clHiBin.offset, clHiBin.length));
      // inputDoc.addField("cl_hi", Base64.byteArrayToBase64(clHiBin.bytes, clHiBin.offset,
      // clHiBin.length));
      inputDoc.addField(
          "cl_ha",
          doc.getField(
              DocumentBuilder.FIELD_NAME_COLORLAYOUT + GenericDocumentBuilder.HASH_FIELD_SUFFIX)
              .stringValue());
      // SURF
      final IndexableField[] features = doc.getFields(DocumentBuilder.FIELD_NAME_SURF);
      for (final IndexableField feature : features) {
        final BytesRef featureBin = feature.binaryValue();
        inputDoc.addField("su_hi",
            ByteBuffer.wrap(featureBin.bytes, featureBin.offset, featureBin.length));
        // inputDoc.addField("su_hi", Base64.byteArrayToBase64(feature.binaryValue().bytes,
        // feature.binaryValue().offset, feature.binaryValue().bytes.length));
      }
      inputDoc.addField("su_ha", doc.getField(DocumentBuilder.FIELD_NAME_SURF_VISUAL_WORDS)
          .stringValue());

      buffer.add(inputDoc);

      if (buffer.size() >= 1) {
        // Flush buffer
        server.add(buffer);
        buffer.clear();
      }
    }

    if (buffer.size() > 0) {
      server.add(buffer);
      buffer.clear();
    }

    try {
      server.commit();
      server.shutdown();
    }
    catch (final SolrServerException e) {
      e.printStackTrace();
    }
  }

  private static void visualWords() throws IOException {
    final Properties prop = getProperties();
    final IndexReader ir = DirectoryReader.open(FSDirectory.open(new File("index")));
    LocalFeatureHistogramBuilder.DELETE_LOCAL_FEATURES = false;
    final int numDocsForVocabulary = Integer.parseInt(prop.getProperty("numDocsForVocabulary"));
    final int numClusters = Integer.parseInt(prop.getProperty("numClusters"));
    final SurfFeatureHistogramBuilder sh =
        new SurfFeatureHistogramBuilder(ir, numDocsForVocabulary, numClusters);
    sh.setProgressMonitor(new ProgressMonitor(null, "", "", 0, 100));
    sh.index();
  }

  public static Properties getProperties() {

    return prop;
  }

  private static void printHelp() {
    System.out.println("USAGE:");
    System.out.println("\t index file - File contains paths to the images, which will be indexed.");
    System.out
        .println("\t import - It sends data from index to solr server specific in the config.properties file.");
    System.out
        .println("\t visualwords - It creates data for visual words technique. This step is automatically execute after index step. You can execute this step again if you want to create visual words with other parameters specific in config.properties file.");
  }
}
