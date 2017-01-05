package util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zsc on 2017/1/5.
 */
public class Config {
    /**
     * 训练集路径
     */
    public static String trainPath;


    /**
     * 测试集路径
     */
    public static String testPath;


    /**
     * 结果保存路径
     */
    public static String resultPath;


    /**
     * eta
     */
    public static float eta;

    /**
     * 迭代次数
     */
    public static int nIter;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        trainPath = properties.getProperty("trainPath");
        testPath = properties.getProperty("testPath");
        resultPath = properties.getProperty("resultPath");
        eta = Float.valueOf(properties.getProperty("eta"));
        trainPath = properties.getProperty("trainPath");
        nIter = Integer.valueOf(properties.getProperty("nIter"));

    }
}
