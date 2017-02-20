import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ImgGenerator {
    private static final String yuml = "https://yuml.me/diagram/scruffy/class/";  //URL of yuml
    private static String img_location = "src/test/output.png";

    public ImgGenerator(){
        try{
            URL url = new URL(yuml + "[A%7C-x:int;-y:int(*)]1-0..*[B],[A]-1[C],[A]-*[D]" + ".png");
            InputStream in = url.openStream();
            OutputStream out = new FileOutputStream(img_location);
            byte[] buffer = new byte[2048];
            int length;

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
