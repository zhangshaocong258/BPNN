package util;

import bpnn.DataNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by zsc on 2017/1/5.
 */
public class DataUtil {

    private Map<String, Integer> resultTypes = new HashMap<String, Integer>();
    private int resultsCount = 0;

    private DataUtil() {}

    private static class Holder {
        private static DataUtil instance = new DataUtil();
    }

    public static DataUtil getInstance() {
        return Holder.instance;

    }

    public Map<String, Integer> getTypeMap() {
        return resultTypes;
    }

    public int getResultsCount() {
        return resultsCount;
    }

    public String getTypeName(int type) {
        for (String key : resultTypes.keySet()) {
            if (resultTypes.get(key) == type)
                return key;
        }
        return null;
    }

    /**
     * 根据文件生成训练集，注意：程序将以第一个出现的非数字的属性作为类别名称
     *
     * @param fileName 文件名
     * @param sep      分隔符
     * @return
     * @throws Exception
     */
    public List<DataNode> getDataList(String fileName, String sep)
            throws IOException {
        List<DataNode> list = new ArrayList<DataNode>();
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = br.readLine()) != null) {
            String splits[] = line.split(sep);
            DataNode node = new DataNode();
            for (int i = 0; i < splits.length; i++) {
                try {
                    node.addAttrib(Float.valueOf(splits[i]));
                } catch (NumberFormatException e) {
                    // 非数字，则为类别名称，将类别映射为数字
                    if (!resultTypes.containsKey(splits[i])) {
                        resultTypes.put(splits[i], resultsCount);
                        resultsCount++;
                    }
                    node.setType(resultTypes.get(splits[i]));
                    list.add(node);
                }
            }
        }
        return list;
    }
}
