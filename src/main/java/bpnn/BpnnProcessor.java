package bpnn;

import util.Config;
import util.DataUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by zsc on 2017/1/5.
 */
public class BpnnProcessor {
    private BpnnClassifier bpnnClassifier;
    private int trainCount;
    private int hiddenCount;
    private int resultCount;
    private List<DataNode> trainList;
    private List<DataNode> testList;

    public void init() throws IOException {
        trainList = DataUtil.getDataList(Config.trainPath, Config.DELIMITER);
        testList = DataUtil.getDataList(Config.testPath, Config.DELIMITER);

        trainCount = DataUtil.getTrainCount();
        resultCount = DataUtil.getResultCount();
        hiddenCount = DataUtil.getHiddenCount();
        System.out.println("trainCount " + trainCount);
        System.out.println("hiddenCount " + hiddenCount);
        System.out.println("resultCount " + resultCount);
    }

    public void start() throws IOException {
        bpnnClassifier = new BpnnClassifier(trainCount, hiddenCount, resultCount);
        bpnnClassifier.train(trainList, Config.eta, Config.nIter);
        bpnnClassifier.test(testList);
    }
}
