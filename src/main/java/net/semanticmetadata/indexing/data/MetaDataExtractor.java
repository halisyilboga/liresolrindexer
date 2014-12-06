package net.semanticmetadata.indexing.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.common.SolrInputDocument;



import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;



public class MetaDataExtractor {

  private static ArrayList<String> imagesPathList = new ArrayList<String>();
  private static int a = 0;
  
  private static int neededExif=6;

  public static void main(String[] args) {

    try {

      ArrayList<String> list = listFilesForFolder(new File(
          "/Users/halisyilboga/Desktop/works/SUN2012/Images"));

      for (int i = 0; i < list.size(); i++) {
        getExifInfo(new File(list.get(i)));

      }
      System.out.println("list.size(): " + list.size());
      System.out.println("a: " + a);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  public static void writeImageExifTags(SolrInputDocument solrInputDocument, File imageFile) throws ImageProcessingException, IOException {
    
    int irequired=0;
    Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
    // iterate over the exif data and print to System.out
    for (Directory directory : metadata.getDirectories()) {
      if(irequired==MetaDataExtractor.neededExif) break;
      for (Tag tag : directory.getTags())
      {if(irequired==MetaDataExtractor.neededExif) break;
          
      try {
        if (tag.toString().contains("Compression Type")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Compression Type")+"Compression Type".length()+3);
          
          if(solrInputDocument.getField("compression_type")!=null){
            solrInputDocument.addField("compression_type", arr);irequired++;}
          
          
        } else if (tag.toString().contains("Data Precision")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Data Precision")+"Data Precision".length()+3);
          
          if(solrInputDocument.getField("data_precision")==null){
            solrInputDocument.addField("data_precision", arr);irequired++;}
          
          
        }else if (tag.toString().contains("Resolution Units")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Resolution Units")+"Resolution Units".length()+3);
          
          if(solrInputDocument.getField("resolution_units")==null){
            solrInputDocument.addField("resolution_units", arr);irequired++;}
          
          
        }  else if (tag.toString().contains("Image Width")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Image Width")+"Image Width".length()+3);
              arr=arr.substring(0,arr.indexOf("pixel"));
        
        if(solrInputDocument.getField("imageWith")==null){		
        solrInputDocument.addField("imageWith",Integer.parseInt(  arr.trim()));irequired++;}
          
        } else if (tag.toString().contains("Image Height")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Image Height")+"Image Height".length()+3);
              arr=arr.substring(0,arr.indexOf("pixel"));
              
        if(solrInputDocument.getField("imageHeight")==null)	{
        solrInputDocument.addField("imageHeight", Integer.parseInt(  arr.trim()));irequired++;}
        
        }else if (tag.toString().contains("X Resolution")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("X Resolution")+"X Resolution".length()+3);
          if(solrInputDocument.getField("yresolution")==null){
          solrInputDocument.addField("xresolution", arr);irequired++;}
        
        } else if (tag.toString().contains("Y Resolution")) {
          String str = tag.toString();
          String arr = str.substring(str.indexOf("Y Resolution")+"Y Resolution".length()+3);
          if(solrInputDocument.getField("yresolution")==null){
          solrInputDocument.addField("yresolution", arr);irequired++;}
        
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      
      }
      }
    }

  private static void getExifInfo(File imageFile) throws ImageProcessingException, IOException{
    
    

    System.out.println("imageFile.getAbsolutePath(): " + imageFile.getAbsolutePath());
      
      SolrInputDocument solrInputDocument =new SolrInputDocument();
      writeImageExifTags(solrInputDocument,imageFile);

  }

  public static ArrayList<String> listFilesForFolder(File folder) {

    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        imagesPathList.add(fileEntry.getPath());
      }
    }

    return imagesPathList;
  }

  
}
