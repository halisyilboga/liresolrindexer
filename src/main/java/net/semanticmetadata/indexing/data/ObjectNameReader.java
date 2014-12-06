package net.semanticmetadata.indexing.data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Aynısı yibi buda bir işe yaramıyot.
 * 
 * @author halisyilboga
 *
 */
public class ObjectNameReader {

    private static ArrayList<String> annotationsPathList = new ArrayList<String>();;
    private static ArrayList<String> objectList;

    public static void main(String[] args) {

        File file = new File("./Objects.txt");

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String string = bufferedReader.readLine();
            int index = 0;
            while (string != null) {
                System.out.println((index++) + ". eleman: " + string);
                string = bufferedReader.readLine();
            }

            bufferedReader.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
