// package scheduleplanner;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// course data format ["0-Division: DIV", "1-Subject: Code + Name + Credit(n-n-n)", "2-Section(n)", "3-Type: Master/Joint", "4-Instructor", "5-Max Seat", "6-Actual Registered", "7-Registered", "8-Seat Available", "9-Room/Time", "10-Final", "11-Info", "12-0x101???", "13-Remark"]
public class planner {

    private static final String fileLocation = "src/schedule.txt";
    private static final JSONObject commands = new JSONObject().put("help","adasd"); // help filter/search/

    public static final String title = muicData().getString("title");
    public static JSONArray data = muicData().getJSONArray("data");
    public static Plan currentPlan; // pointer

    static final Scanner in = new Scanner(System.in);

    private static ArrayList<Plan> plans = new ArrayList<>();

    public void addPlan(Plan plan){
        plans.add(plan);
    }

    public void removePlan(String name){
        for (int i=0;i<plans.size();i++){
            if(plans.get(i).getName().equalsIgnoreCase(name)){
                plans.remove(plans.get(i));
                return;
            }
        }
    }


    public static void main(String[] args) throws IOException {

        File currentFile = new File("src/planner.java");
        FileWriter fWriter = new FileWriter(currentFile, false); // false = rewrite; true = append
        fWriter.write(IOUtils.toString(new URL("https://raw.githubusercontent.com/Ken19149/MUIC-Schedule-Planner/refs/heads/main/src/planner.java")));
        fWriter.close();

        String initialData = initialFile();
        System.out.println(title + "\n\n" + Messages.initialMessage + "\n" + Messages.instructionMessage);
        // loop command

        while (true) {
            // Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            String command = input.split(" ", 2)[0].toLowerCase();    // first keyword command | the rest parameters
            String suffix;
            try {
                suffix = input.split(" ", 2)[(input.split(" ", 2)).length-1];  // last array index even if it has one
            } catch (Exception e) {
                suffix = "";
            }

            try {
                // stop program command
                if (command.matches("|exit|stop|break")) {
                    break;
                } else if (command.matches("search")) { // add
                    // System.out.print("Search: ");
                    Course course = new Course(searchCourse(data, suffix));
                    currentPlan.addCourse(course);
                    System.out.println(course.courseName + " added to your plan");
                    System.out.println(currentPlan.toString()); // list
                } else if (command.matches("new_plan")) {
                    Plan plan = new Plan(suffix);
                    plans.add(plan);
                    currentPlan = plan;
                    System.out.printf("Plan \"%s\" added\n", suffix);
                } else if (command.matches("list")) {
                    System.out.println(currentPlan.toString());
                } else if (command.matches("remove")) {
                    currentPlan.removeCourse(suffix.toLowerCase());
                    System.out.println(currentPlan.toString()); // list
                } else if (command.matches("help")) {
                    System.out.println(Messages.helpCommand);
                } else {
                    System.out.println(Messages.invalidCommand);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
                System.out.println("PLease try again");
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

        if (value.matches("mon|tue|wed|thu|fri|sat|sun")){  // search by day
            for(int i=0;i<data.length();i++){
                if (data.getJSONArray(i).getString(9).toLowerCase().contains(value.toLowerCase())){
                    candidates.put(data.getJSONArray(i));
                    System.out.println(Integer.toString(candidates.length()) + " " + new Course(data.getJSONArray(i)).toString());
                }
            }
        } else {
            for(int i=0;i<data.length();i++){   // search by name
                if (data.getJSONArray(i).getString(1).toLowerCase().contains(value.toLowerCase())){
                    candidates.put(data.getJSONArray(i));
                    System.out.println(Integer.toString(candidates.length()) + " " + new Course(data.getJSONArray(i)).toString());
                }
            }
        }

        // check if no course found
        if (candidates.isEmpty()) {
            System.out.println(Messages.noCourseFound);
            System.out.print("Search: ");
            return searchCourse(data, in.nextLine());
        }

        // select listed courses from index //


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
    static String initialMessage = "Type \"new_plan\" <plan name> to create a new plan.\nThen type \"search\" <class code|keyword> to start selecting a course";

    static String helpCommand = "new_plan <plan name>\nsearch <class code|keyword|day>\nremove <index|class keyword>\nhelp\nexit|stop|break\nlist";

    // course-related messages
    static String selectCourse = "Select course: ";
    static String searchCancel = "Search canceled";
    static String selectAgain = "Please choose the course by index: ";
    static String noCourseFound = "No course found. Please try another term";

    // error messages
    static String invalidCommand = "Invalid command. Please type again";

}

class Course {
    // course data format ["0-Division: DIV", "1-Subject: Code + Name + Credit(n-n-n)", "2-Section(n)",
    // "3-Type: Master/Joint", "4-Instructor", "5-Max Seat", "6-Actual Registered", "7-Registered", "8-Seat Available",
    // "9-Room/Time", "10-Final", "11-Info", "12-0x101???", "13-Remark"]

    public final String Division, courseCode, courseName, creditFormat, instructor, room;
    public final ArrayList<String> days, times;
    public final int credits, hours, section, maxSeat, actualRegistered, registered, seatAvailable;
    public final boolean full;
    public final JSONArray jsonArray;

    public static String courseFormat;

    Course(JSONArray course) {
        jsonArray = course; // to back to json array

        Division = course.get(0).toString(); // SCI
        courseCode = course.get(1).toString().split(" ", 2)[0]; // EGCI202
        courseName = course.get(1).toString().split(" ", 2)[1].split("\\(")[0].substring(0, course.get(1).toString().split(" ", 2)[1].split("\\(")[0].length()-2); // Engineering Maths for Signal and System
        creditFormat = course.get(1).toString().split(" ")[course.get(1).toString().split(" ").length-1]; // 4(4-0-8)
        credits = Integer.parseInt(course.get(1).toString().split(" ")[course.get(1).toString().split(" ").length-1].split("\\(")[0]); // 4
        hours = Integer.parseInt(course.get(1).toString().split(" ")[course.get(1).toString().split(" ").length-1].split("\\(")[1].split("-")[0] + course.get(1).toString().split(" ")[course.get(1).toString().split(" ").length-1].split("\\(")[1].split("-")[1]); // 4-0-8) -> 4 + 0 = 4; hours per week (calculate using the 1st and 2nd number in the credits format)
        section = Integer.parseInt(course.get(2).toString());
        instructor = course.get(4).toString();
        maxSeat = Integer.parseInt(course.get(5).toString());;
        actualRegistered = Integer.parseInt(course.get(6).toString());
        registered = Integer.parseInt(course.get(7).toString()); // actually registered (don't know the different between registered vs actual registered)
        seatAvailable = Integer.parseInt(course.get(8).toString());
        full = seatCheck(seatAvailable);
        room = room(course.get(9).toString());
        days = days(course.get(9).toString());
        times = times(course.get(9).toString());
    }

    private Boolean seatCheck(int seat){if(seat>0){return false;}else {return true;}} // if seat > 0 then not full//full = false
    private String room(String datePlaceFormat){if(datePlaceFormat.isEmpty()){return "-";}else{return datePlaceFormat.split("\\(", 2)[0].substring(0, datePlaceFormat.split("\\(", 2)[0].length()-2);}}
    private ArrayList<String> days(String datePlaceFormat){ArrayList<String> day = new ArrayList<>(); try{for(int i = 0; i<datePlaceFormat.split("\\(").length-1; i++){day.add(datePlaceFormat.split("\\(")[i+1].split(" ", 2)[0]);} return day;}catch (Exception e){return day;}}
    private ArrayList<String> times(String datePlaceFormat){ArrayList<String> time = new ArrayList<>();try{for(int i = 0; i<datePlaceFormat.split("\\(").length-1; i++){time.add(datePlaceFormat.split("\\(")[i+1].split("\\)")[0].split(" ", 2)[1]);} return time;}catch (Exception e){return time;}}

    @Override
    public String toString(){
        String dateTime = "";
        try {
            for (int i = 0; i < days.size(); i++) {
                dateTime += days.get(i) + " " + times.get(i) + " | ";
            }
            dateTime = dateTime.substring(0,dateTime.length()-3);
        } catch (Exception e) {dateTime = "-";}
        return String.format("%s %s | %s/%s | Day & Time: %s | Room: %s | SEC: %s", courseCode, courseName, actualRegistered, maxSeat, dateTime, room, section);
    }
}

class Plan {
    private String planName;
    private ArrayList<Course> courses = new ArrayList<>();

    public static String planFormat;

    Plan(String name){
        planName = name;
    }

    @Override
    public String toString(){
        String format = planName + ": \n";
        for(int i=0;i<courses.size();i++){
            format += (i+1) + ") " + courses.get(i).toString() + "\n";
        }
        return format;
    }

    public String getName(){
        return planName;
    }

    public String getCourses(){
        return courses.toString();
    }

    public void setName(String name){
        planName = name;
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public void removeCourse(String name){
        // remove by index
        if (name.matches("\\d+")) {
            String removedCourse = courses.get(Integer.parseInt(name)-1).courseName;
            courses.remove(Integer.parseInt(name)-1);
            System.out.println(removedCourse + " removed from your plan");
            return;
        }

        // remove by name
        for (int i=0;i<courses.size();i++){
            if(courses.get(i).courseName.toLowerCase().contains(name.toLowerCase())){
                String removedCourse = courses.get(i).courseName;
                courses.remove(courses.get(i));
                System.out.println(removedCourse + " removed from your plan");
                return;
            }
        }
    }

}
