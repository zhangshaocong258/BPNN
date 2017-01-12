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
     * 分隔符
     */
    public static String DELIMITER;

    /**
     * eta
     */
    public static float eta;

    /**
     * 迭代次数
     */
    public static int nIter;

    /**
     * 脾气虚证
     */
    public static String PiQiXuZhengNum;

    /**
     * 脾阳虚证
     */
    public static String PiYangXuZhengNum;

    /**
     * 脾肾阳虚证
     */
    public static String PiShenYangXuZhengNum;

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
        DELIMITER = properties.getProperty("DELIMITER");
        eta = Float.valueOf(properties.getProperty("eta"));
        nIter = Integer.valueOf(properties.getProperty("nIter"));
        PiQiXuZhengNum = properties.getProperty("PiQiXuZhengNum");
        PiYangXuZhengNum = properties.getProperty("PiYangXuZhengNum");
        PiShenYangXuZhengNum = properties.getProperty("PiShenYangXuZhengNum");

    }
}
