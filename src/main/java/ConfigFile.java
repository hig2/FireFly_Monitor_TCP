import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
        try {
            searchFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void searchFile() throws IOException {
        File file = new File(absolutePath);
        if (!file.isFile()) {
            new File(path).mkdirs();
            file.createNewFile();
            setIP(defaultAddressIP, file);
        }
    }


    public static boolean setIP(String addressIP, File file) {
        if (!validateIP(addressIP)) {
            return false;
        }

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




}
