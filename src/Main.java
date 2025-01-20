import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
    start:
    try create -> catch already exist -> read

*/

public class Main {

    private static final String fileLocation = "src/schedule.txt";
    private static final String instructionMessage = "type help for information";

    public static void main(String[] args){
        String initialData = initialFile();
        System.out.println("initial data: " + initialData);
    }

    public static JSONObject muicData(){
        try{
            URL link = new URL("https://os.muic.io/data.json");
            JSONObject json = new JSONObject(IOUtils.toString(link, StandardCharsets.UTF_8));
            return json;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String initialFile(){
        // try to create schedule.txt with initial message but if it's already exist, return data from that file
        try {
            File file = new File(fileLocation);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                FileWriter myWriter = new FileWriter(fileLocation);
                myWriter.write(instructionMessage);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(file);
                String data = "";
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    data += line + "\n";
                }
                myReader.close();
                return data;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

}