package bpnn;

import util.Config;
import util.DataUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsc on 2017/1/5.
 * 标准BP神经网络分类器
 */
public class BpnnClassifier {
    private int mInputCount;
    private int mHiddenCount;
    private int mOutputCount;

    private List<NetworkNode> mInputNodes;//4
    private List<NetworkNode> mHiddenNodes;//10 delta（δ）个数等于隐藏层个数
    private List<NetworkNode> mOutputNodes;//3 delta（δ）个数等于输出层个数

    private float[][] mInputHiddenWeight;//输入层和隐藏层之间的权值
    private float[][] mHiddenOutputWeight;//隐藏层和输出层之间的权值
    private float[] mInputHiddenBias;//输入层和隐藏层之间的偏置
    private float[] mHiddenOutputBias;//隐藏层和输出层之间的偏置

    private float[][] mInputHiddenWeightOpt;//输入层和隐藏层之间的共享权值
    private float[] mInputHiddenBiasOpt;//输入层和隐藏层之间的偏置



//    private List<DataNode> trainNodes;
//
//    public void setTrainNodes(List<DataNode> trainNodes) {
//        this.trainNodes = trainNodes;
//    }

    public BpnnClassifier(int inputCount, int hiddenCount, int outputCount) {
//        trainNodes = new ArrayList<DataNode>();
        mInputCount = inputCount;//4
        mHiddenCount = hiddenCount;//10
        mOutputCount = outputCount;//3
        mInputNodes = new ArrayList<NetworkNode>();
        mHiddenNodes = new ArrayList<NetworkNode>();
        mOutputNodes = new ArrayList<NetworkNode>();
        mInputHiddenWeight = new float[inputCount][hiddenCount];//[4,10]
        mHiddenOutputWeight = new float[hiddenCount][outputCount];//[10,4]
        mInputHiddenBias = new float[hiddenCount];//10
        mHiddenOutputBias = new float[outputCount];//3
    }

    /**
     * 初始化
     */
    private void init() {
        mInputNodes.clear();
        mHiddenNodes.clear();
        mOutputNodes.clear();
        for (int i = 0; i < mInputCount; i++) {
            mInputNodes.add(new NetworkNode(NetworkNode.TYPE_INPUT));
        }

        for (int i = 0; i < mHiddenCount; i++) {
            mHiddenNodes.add(new NetworkNode(NetworkNode.TYPE_HIDDEN));
        }

        for (int i = 0; i < mOutputCount; i++) {
            mOutputNodes.add(new NetworkNode(NetworkNode.TYPE_OUTPUT));
        }

        for (int i = 0; i < mInputCount; i++) {
            for (int j = 0; j < mHiddenCount; j++) {
                mInputHiddenWeight[i][j] = (float) (Math.random() * 0.1);
                mInputHiddenBias[j] = (float) (Math.random() * 0.1);
            }
        }

        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] = (float) (Math.random() * 0.1);
                mHiddenOutputBias[j] = (float) (Math.random() * 0.1);
            }
        }
    }


    /**
     * 前向传播
     */
    private void forward(List<Float> list) {
        // 输入层 得到第一层结果，不变，还是输入层结果
        for (int k = 0; k < list.size(); k++) {
            mInputNodes.get(k).setForwardOutputValue(list.get(k));
        }

        // 隐层 得到第二层结果，即经过隐藏层乘以权重相加后经过函数的结果
        for (int j = 0; j < mHiddenCount; j++) {//10
            float temp = 0;
            for (int k = 0; k < mInputCount; k++) {//4
                temp += mInputHiddenWeight[k][j] * mInputNodes.get(k).getForwardOutputValue();
            }
            temp += mInputHiddenBias[j];
            mHiddenNodes.get(j).setForwardOutputValue(temp);
        }

        // 输出层 得到最后的结果，即隐藏层结果乘以权重相加后经过函数的结果
        for (int j = 0; j < mOutputCount; j++) {//4
            float temp = 0;
            for (int k = 0; k < mHiddenCount; k++) {//10
                temp += mHiddenOutputWeight[k][j]
                        * mHiddenNodes.get(k).getForwardOutputValue();
            }
            temp += mHiddenOutputBias[j];
            mOutputNodes.get(j).setForwardOutputValue(temp);
        }
    }

    /**
     * 反向传播
     * 对于输入层，有3种类型；0,1,2
     * 得到3个结果，mOutputNodes对应3个位置：0,1,2，与输入对应
     * 比如输入的类型为1，对输出，0,1,2分别赋值-1,1,-1
     */

    private void backword(int type, float eta) {
        calcDelta(type);
        updateWeights(eta);
    }

    private void calcDelta(int type) {
        // 输出层
        for (int j = 0; j < mOutputCount; j++) {//3
            // 输出层计算误差把误差反向传播，这里-1代表不属于，1代表属于
            float result = -1;
            if (j == type) {
                result = 1;
            }
            //得到输出层的δ
            mOutputNodes.get(j).setBackwardOutputValue(mOutputNodes.get(j).getForwardOutputValue() - result);
        }

        // 隐层
        for (int j = 0; j < mHiddenCount; j++) {//10
            float temp = 0;
            for (int k = 0; k < mOutputCount; k++) {//3
                temp += mHiddenOutputWeight[j][k] * mOutputNodes.get(k).getBackwardOutputValue();
            }

            //得到倒数第二层的δ
            mHiddenNodes.get(j).setBackwardOutputValue(temp);
        }
    }

    /**
     * 更新权重，每个权重的梯度都等于与其相连的前一层节点的输出乘以与其相连的后一层的反向传播的输出
     * 没有b的值
     */
    private void updateWeights(float eta) {
        // 更新输入层到隐层的权重矩阵
        for (int i = 0; i < mInputCount; i++) {
            for (int j = 0; j < mHiddenCount; j++) {
                mInputHiddenWeight[i][j] -= eta
                        * mInputNodes.get(i).getForwardOutputValue()
                        * mHiddenNodes.get(j).getBackwardOutputValue();
                mInputHiddenBias[j] -= eta * mHiddenNodes.get(j).getBackwardOutputValue();
            }
        }

        for (int j = 0; j < mHiddenCount; j++) {
            mInputHiddenBias[j] -= eta * mHiddenNodes.get(j).getBackwardOutputValue();
        }

        // 更新隐层到输出层的权重矩阵
        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] -= eta
                        * mHiddenNodes.get(i).getForwardOutputValue()
                        * mOutputNodes.get(j).getBackwardOutputValue();
            }
        }

        for (int j = 0; j < mOutputCount; j++) {
            mHiddenOutputBias[j] -= eta * mOutputNodes.get(j).getBackwardOutputValue();
        }

    }

    public void train(List<DataNode> trainList, float eta, int n) {
        init();
        for (int i = 0; i < n; i++) {//迭代次数
            for (int j = 0; j < trainList.size(); j++) {//训练集124个
                forward(trainList.get(j).getAttribList());
                backword(trainList.get(j).getType(), eta);
            }
//            System.out.println("n = " + i);

        }
    }


    public void test(List<DataNode> testList) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(new File(Config.resultPath)));

        for (DataNode testNode : testList) {
            int type = getType(testNode);
            System.out.println("***********");
            List<Float> attributes = testNode.getAttribList();
            for (int n = 0; n < attributes.size(); n++) {
                output.write(attributes.get(n) + Config.DELIMITER);
                output.flush();
            }
            output.write(DataUtil.getTypeName(type));
            output.newLine();
            output.flush();
        }
        output.close();

    }

    public int getType(DataNode dataNode) {
        forward(dataNode.getAttribList());
        float result = 2;
        int type = 0;
        // 取最接近1的
        for (int i = 0; i < mOutputCount; i++) {
            System.out.println("result " + mOutputNodes.get(i).getForwardOutputValue());
            if ((1 - mOutputNodes.get(i).getForwardOutputValue()) < result) {
                result = 1 - mOutputNodes.get(i).getForwardOutputValue();
                type = i;
            }
        }
        return type;
    }
}
