package net.semanticmetadata.indexing.data;

import java.util.ArrayList;

public class SolrImage {

    private String id;
    private String fileName;
    private String folder;
    private ArrayList<String> objectList;
    private String creationDate;

    public ArrayList<String> getObjectList() {
        return objectList;
    }

    public void setObjectList(ArrayList<String> objectList) {
        this.objectList = objectList;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
    	
    	return "id: "+id;
    }
    
}
