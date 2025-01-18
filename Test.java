import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class Test {
    public static void main(String args[]){
       JSONObject json = new JSONObject(IOUtils.toString(new URL("https://os.muic.io/data.json"), Charset.forName("UTF-8")));

    }
}