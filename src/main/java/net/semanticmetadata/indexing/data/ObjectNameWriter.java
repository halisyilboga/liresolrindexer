package net.semanticmetadata.indexing.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.semanticmetadata.indexing.util.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Hiç bir işe yaradığı yok. Tam oalrak ne için kullanıldığı anlaşılmadı. 
 * @author halisyilboga
 *
 */

public class ObjectNameWriter {

    private static ArrayList<String> annotationsPathList = new ArrayList<String>();;
    private static ArrayList<String> objectList;

    public static void main(String[] args) {

        File folder = new File("/Users/halisyilboga/Desktop/works/SUN2012/Annotations");

        ArrayList<String> xmlFilePathList = listFilesForFolder(folder);
        HashMap<String, String> hash = new HashMap<String, String>();

        for (String path : xmlFilePathList) {
            SolrImage solrImage = readXML(path);
            for (int j = 0; j < solrImage.getObjectList().size(); j++) {
                hash.put(solrImage.getObjectList().get(j), solrImage.getObjectList().get(j));
            }
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("Objects.txt")));
            for (String string : hash.values()) {
                bufferedWriter.write(string);
                bufferedWriter.newLine();
                System.out.println(string);
            }
            bufferedWriter.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static ArrayList<String> listFilesForFolder(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getPath());
                annotationsPathList.add(fileEntry.getPath());
            }
        }

        return annotationsPathList;
    }

    public static SolrImage readXML(String xmlFilePath) {

        objectList = new ArrayList<String>();

        try {
            File xml = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList filenameNode = doc.getElementsByTagName("filename");

            SolrImage solrImage = new SolrImage();
            solrImage.setFileName(StringUtil.strip(filenameNode.item(0).getTextContent()));

            NodeList folderNode = doc.getElementsByTagName("folder");
            solrImage.setFolder(StringUtil.strip(folderNode.item(0).getTextContent()));

            NodeList nodes = doc.getElementsByTagName("object");
            for (int i = 0; i < nodes.getLength(); i++) {

                Node nNode = nodes.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    objectList.add(StringUtil.strip(eElement.getElementsByTagName("name").item(0).getTextContent()));
                }
            }
            solrImage.setObjectList(objectList);

            return solrImage;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
