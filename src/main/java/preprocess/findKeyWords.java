package preprocess;

import java.io.*;

/**
 * Created by zsc on 2017/2/22.
 * 得到关键字
 */
public class FindKeyWords {

    private String inputPath = "E:\\BPNN\\data\\6.txt";
    private String outputPath = "E:\\BPNN\\data\\out6.txt";

    public static void main(String args[]) throws Exception{
        FindKeyWords main = new FindKeyWords();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(main.inputPath)), 5 * 1024 * 1024);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(main.outputPath)));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("辨证:")) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
        bufferedReader.close();
        bufferedWriter.close();
    }
}
