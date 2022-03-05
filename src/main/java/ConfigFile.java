import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ConfigFile {
    private static final String folderName = "FireFlyMonitorConfig";
    private static final String separator = "\\";
    private static final String fileName = "config.cfg";
    private static final String path = System.getProperty("user.home") + separator + "AppData" + separator + "Local" + separator + folderName;
    private static final String absolutePath = path + separator + fileName;
    private static final String defaultAddressIP = "127.0.0.1";
    private static final Pattern patternIP = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private ConfigFile() {
    }

    public static boolean validateIP(final String ip) {
        return patternIP.matcher(ip).matches();
    }

    public static String getIP() {
        File file = null;
        try {
           file = searchFile();
        } catch (IOException e) {
            e.printStackTrace();
            return defaultAddressIP;
        }

        return readIP(file);
    }

    private static File searchFile() throws IOException {
        File file = new File(absolutePath);
        if (!file.isFile()) {
            new File(path).mkdirs();
            file.createNewFile();
            setIP(defaultAddressIP);
        }
        return new File(absolutePath);
    }



    public static boolean setIP(String addressIP) {
        if (!validateIP(addressIP)) {
            return false;
        }
        File file = null;
        try {
            file = searchFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return writeIP(addressIP, file);
    }

    private static boolean writeIP(String addressIP, File file){
        try (FileWriter writer = new FileWriter(file, false)) {
            String key = "addressIP=";
            writer.write(key + addressIP + "\n");

            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    private static String readIP(File file)  {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            List<String> listAllProperty = new ArrayList<>();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                listAllProperty.add(line);
            }
            for (String e : listAllProperty){
                String[] result = e.split("=");
                if(result[0].equals("addressIP") && validateIP(result[1])){
                    return result[1];
                }
            }
            return defaultAddressIP;

        }catch (IOException e){
            e.printStackTrace();
            return defaultAddressIP;
        }
    }

}
