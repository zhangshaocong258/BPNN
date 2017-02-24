package preprocess;

import util.Config;

import java.io.*;
import java.util.*;

/**
 * Created by zsc on 2017/2/24.
 * 池化采样
 */
public class Pooling {

    private static String inputPath = "E:\\BPNNOPT\\data\\csv";
    private static String outputPath = "E:\\BPNNOPT\\data\\out.csv";
    private static List<File> files = new ArrayList<File>();

    public static void main(String args[]) throws IOException{
        File folder = new File(inputPath);
        getFiles(folder);
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).getName());

        }
        executePooling(files, outputPath);

    }

    private static void getFiles(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                getFiles(file);
            }
        } else {
            files.add(folder);
        }
    }

    private static void executePooling(List<File> files, String outputPath) throws IOException{
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)));
        for (File file : files) {
            //存储 证名_list，list按顺序存储主症、次症、舌象、脉象_各个判断条件权重
            LinkedHashMap<String, ArrayList<ArrayList<Double>>> hashMap = new LinkedHashMap<String, ArrayList<ArrayList<Double>>>();
            hashMap.put(Config.QiXueLiangXuZheng, new ArrayList<ArrayList<Double>>());
            hashMap.put(Config.PiWeiXuRuoZheng, new ArrayList<ArrayList<Double>>());
            hashMap.put(Config.GanShenYinXuZheng, new ArrayList<ArrayList<Double>>());
            for (int i = 0; i < 4; i++) {
                hashMap.get(Config.GanShenYinXuZheng).add(new ArrayList<Double>());
                hashMap.get(Config.PiWeiXuRuoZheng).add(new ArrayList<Double>());
                hashMap.get(Config.QiXueLiangXuZheng).add(new ArrayList<Double>());
            }
            String name = file.getName().substring(0, file.getName().indexOf("Zheng") + 5);
            System.out.println(name);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), 5 * 1024 * 1024);

            String line;
            StringBuilder outLine = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(Config.QiXueLiangXuZheng)) {
                    String[] str = line.split(Config.DELIMITER);
                    for (int i = 0; i < 4; i++) {
                        hashMap.get(Config.QiXueLiangXuZheng).get(i).add(Double.parseDouble(str[i]));
                    }
                } else if (line.contains(Config.PiWeiXuRuoZheng)) {
                    String[] str = line.split(Config.DELIMITER);
                    for (int i = 0; i < 4; i++) {
                        hashMap.get(Config.PiWeiXuRuoZheng).get(i).add(Double.parseDouble(str[i]));
                    }
                } else if (line.contains(Config.GanShenYinXuZheng)) {
                    String[] str = line.split(Config.DELIMITER);
                    for (int i = 0; i < 4; i++) {
                        hashMap.get(Config.GanShenYinXuZheng).get(i).add(Double.parseDouble(str[i]));
                    }
                }
            }
            for (Map.Entry<String, ArrayList<ArrayList<Double>>> entry : hashMap.entrySet()) {
                System.out.println("name: " + entry.getKey());
                for (int i = 0; i < entry.getValue().size(); i++) {//一共4项
                    double average = 0.0;
                    System.out.println("**********");
                    for (int j = 0; j < entry.getValue().get(i).size(); j++) {//判定规则个数
                        average += entry.getValue().get(i).get(j);
                        System.out.println(entry.getValue().get(i).get(j));

                    }
                    outLine.append(String.valueOf(average / entry.getValue().get(i).size())).append(",");
                }
            }
            outLine.append(name);
            bufferedWriter.write(outLine.toString());
            bufferedWriter.newLine();
            System.out.println(outLine);
        }
        bufferedWriter.close();

    }
}
