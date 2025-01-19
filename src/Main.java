import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args){
        try{
            JSONObject json = new JSONObject(IOUtils.toString(new URL("https://os.muic.io/data.json"), StandardCharsets.UTF_8));
            //System.out.println(json);

            /*
            JSONArray dataArray = json.getJSONArray("data");
            JSONArray firstElement = dataArray.getJSONArray(0);
            String sciValue = firstElement.getString(0);
            */

            System.out.println(json.getString("title"));
            // System.out.println(json);

            // Printing the value "SCI"
            //System.out.println(sciValue);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}