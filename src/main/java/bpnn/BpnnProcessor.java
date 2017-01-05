package bpnn;

import util.Config;
import util.DataUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by zsc on 2017/1/5.
 */
public class BpnnProcessor {
    private DataUtil dataUtil = DataUtil.getInstance();
    private BpnnClassifier bpnnClassifier;
    private int trainCount;
    private int hiddenCount;
    private int resultCount;
    private String DELIMITER = ",";

    //训练
    public void train() throws IOException{
        List<DataNode> trainList = dataUtil.getDataList(Config.trainPath, DELIMITER);
        trainCount = trainList.get(0).getAttribList().size();
        resultCount = dataUtil.getResultsCount();
        hiddenCount = (int) Math.sqrt(trainCount + resultCount) + 8;
        bpnnClassifier = new BpnnClassifier(trainCount, hiddenCount, resultCount);
        bpnnClassifier.setTrainNodes(trainList);
        bpnnClassifier.train(Config.eta, Config.nIter);
    }

    //测试
    public void test() throws IOException{
        List<DataNode> testList = dataUtil.getDataList(Config.testPath, DELIMITER);
        BufferedWriter output = new BufferedWriter(new FileWriter(new File(Config.resultPath)));

        for (int i = 0; i < testList.size(); i++) {
            DataNode testNode = testList.get(i);
            int type = bpnnClassifier.test(testNode);
            System.out.println("***********");
            List<Float> attribs = testNode.getAttribList();
            for (int n = 0; n < attribs.size(); n++) {
                output.write(attribs.get(n) + DELIMITER);
                output.flush();
            }
            output.write(dataUtil.getTypeName(type));
            output.newLine();
            output.flush();
        }
        output.close();

    }
}
