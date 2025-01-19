import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args){
        try{
            JSONObject json = new JSONObject(IOUtils.toString(new URL("https://os.muic.io/data.json"), StandardCharsets.UTF_8));
            //System.out.println(json);

            System.out.println(json.getString("title"));
            System.out.println(json.getJSONArray("data").getJSONArray(0));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}