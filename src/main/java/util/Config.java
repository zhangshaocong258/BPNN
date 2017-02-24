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
     * 气血两虚证
     */
    public static String QiXueLiangXuZhengNum;
    public static String QiXueLiangXuZheng;

    /**
     * 脾气虚证/脾胃虚弱证
     */
    public static String PiWeiXuRuoZhengNum;
    public static String PiWeiXuRuoZheng;

    /**
     * 肝肾阴虚证
     */
    public static String GanShenYinXuZhengNum;
    public static String GanShenYinXuZheng;

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
        QiXueLiangXuZhengNum = properties.getProperty("QiXueLiangXuZhengNum");
        QiXueLiangXuZheng = properties.getProperty("QiXueLiangXuZheng");
        PiWeiXuRuoZhengNum = properties.getProperty("PiWeiXuRuoZhengNum");
        PiWeiXuRuoZheng = properties.getProperty("PiWeiXuRuoZheng");
        GanShenYinXuZhengNum = properties.getProperty("GanShenYinXuZhengNum");
        GanShenYinXuZheng = properties.getProperty("GanShenYinXuZheng");

    }
}
