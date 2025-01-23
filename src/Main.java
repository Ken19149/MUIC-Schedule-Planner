import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/*
    start:
    try create -> catch already exist -> read

*/

// course data format ["0-Division: DIV", "1-Subject: Code + Name + Credit(n-n-n)", "2-Section(n)", "3-Type: Master/Joint", "4-Instructor", "5-Actual Registered", "6-Registered", "7-Seat Available", "8-Room/Time", "9-Final", "10-Info", "11-0x101???", "12-Remark"]

public class Main {

    private static final String fileLocation = "src/schedule.txt";
    private static final JSONObject commands = new JSONObject().put("help","adasd"); // help filter/search/

    public static final String title = muicData().getString("title");
    public static JSONArray data = muicData().getJSONArray("data");
    public static JSONObject currentPlan = new JSONObject();

    static final Scanner in = new Scanner(System.in);

    public static void main(String[] args){


        String initialData = initialFile();
        System.out.println(title + "\n\n" + Messages.initialMessage + "\n" + Messages.instructionMessage);
        // loop command
        while (true) {
            // Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            String command = input.split(" ", 2)[0].toLowerCase();    // first keyword command | the rest parameters
            String suffix = input.split(" ", 2)[(input.split(" ", 2)).length-1].toLowerCase();  // last array index even if it has one


            // stop program command
            if (command.matches("|exit|stop|break")){
                break;
            } else if (command.matches("search")) {
                System.out.print("Search: ");
                System.out.println(searchCourse(data, in.nextLine()));
            } else {
                System.out.println(Messages.invalidCommand);
            }

        }
        System.out.println("exited loop");

    }

    public static JSONObject muicData(){
        try{
            URL link = new URL("https://os.muic.io/data.json");
            return new JSONObject(IOUtils.toString(link, StandardCharsets.UTF_8));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String initialFile(){
        // try to create schedule.txt with initial message but if it's already exist, return data from that file
        try {
            File file = new File(fileLocation);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                FileWriter myWriter = new FileWriter(fileLocation);
                myWriter.write(Messages.instructionMessage);
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

    // input: code/name + sec/day–time
    public static JSONArray searchCourse(JSONArray data, String value){
        // search courses
        JSONArray candidates = new JSONArray();
        for(int i=0;i<data.length();i++){
            if (data.getJSONArray(i).getString(1).toLowerCase().contains(value.toLowerCase())){
                candidates.put(data.getJSONArray(i));
                System.out.println(Integer.toString(candidates.length()) + data.getJSONArray(i));
            }
        }
        // check if no course found
        if (candidates.isEmpty()) {
            System.out.println(Messages.noCourseFound);
            System.out.print("Search: ");
            return searchCourse(data, in.nextLine());
        }

        // select listed courses from index //

        // Scanner in = new Scanner(System.in);
        System.out.print(Messages.selectCourse);

        try {
            String inputStr = in.nextLine();
            if (inputStr.isEmpty()) { // if not choose then cancel search function
                System.out.println(Messages.searchCancel);
                return null;
            }
            int index = Integer.parseInt(inputStr);
            return candidates.getJSONArray(index-1);
        } catch (Exception e) {
            while (true) {
                try {
                    System.out.print(Messages.selectAgain);
                    String inputStr = in.nextLine();
                    if (inputStr.isEmpty()) { // if not choose then cancel search function
                        System.out.println(Messages.searchCancel);
                        return null;
                    }
                    int index = Integer.parseInt(inputStr);
                    return candidates.getJSONArray(index - 1);
                } catch (Exception ee) {

                }
            }
        }

    }

    public static JSONArray searchTime(JSONArray data, String value){
        return null;
    }

    public static String info(JSONArray course) {

        return "yes";
    }

}

class Messages {
    // instructions and setup messages
    static String instructionMessage = "Type \"help\" for information";
    static String initialMessage = "Type \"search\" to start selecting a course";

    // course-related messages
    static String selectCourse = "Select course: ";
    static String searchCancel = "Search canceled";
    static String selectAgain = "Please choose the course by index: ";
    static String noCourseFound = "No course found. Please try another term";

    // error messages
    static String invalidCommand = "Invalid command. Please type again";

}

class Commands {
    static String search = "";
}
