package bpnn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsc on 2017/1/5.
 */
public class BpnnClassifier {
    private int mInputCount;
    private int mHiddenCount;
    private int mOutputCount;

    private List<NetworkNode> mInputNodes;
    private List<NetworkNode> mHiddenNodes;
    private List<NetworkNode> mOutputNodes;

    private float[][] mInputHiddenWeight;
    private float[][] mHiddenOutputWeight;

    private List<DataNode> trainNodes;

    public void setTrainNodes(List<DataNode> trainNodes) {
        this.trainNodes = trainNodes;
    }

    public BpnnClassifier(int inputCount, int hiddenCount, int outputCount) {
        trainNodes = new ArrayList<DataNode>();
        mInputCount = inputCount;//3
        mHiddenCount = hiddenCount;//11
        mOutputCount = outputCount;//3
        mInputNodes = new ArrayList<NetworkNode>();
        mHiddenNodes = new ArrayList<NetworkNode>();
        mOutputNodes = new ArrayList<NetworkNode>();
        mInputHiddenWeight = new float[inputCount][hiddenCount];//[3,11]
        mHiddenOutputWeight = new float[hiddenCount][outputCount];//[11,3]
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
        for (int j = 0; j < mHiddenCount; j++) {//11
            float temp = 0;
            for (int k = 0; k < mInputCount; k++) {//3
                temp += mInputHiddenWeight[k][j] * mInputNodes.get(k).getForwardOutputValue();
            }
            mHiddenNodes.get(j).setForwardOutputValue(temp);
        }

        // 输出层 得到最后的结果，即隐藏层结果乘以权重相加后经过函数的结果
        for (int j = 0; j < mOutputCount; j++) {
            float temp = 0;
            for (int k = 0; k < mHiddenCount; k++) {
                temp += mHiddenOutputWeight[k][j]
                        * mHiddenNodes.get(k).getForwardOutputValue();
            }
            mOutputNodes.get(j).setForwardOutputValue(temp);
        }
    }

    /**
     * 反向传播
     */
    private void backward(int type) {
        // 输出层
        for (int j = 0; j < mOutputCount; j++) {//3
            // 输出层计算误差把误差反向传播，这里-1代表不属于，1代表属于
            float result = -1;
            if (j == type)
                result = 1;
            mOutputNodes.get(j).setBackwardOutputValue(
                    mOutputNodes.get(j).getForwardOutputValue() - result);
        }
        // 隐层
        for (int j = 0; j < mHiddenCount; j++) {
            float temp = 0;
            for (int k = 0; k < mOutputCount; k++)
                temp += mHiddenOutputWeight[j][k]
                        * mOutputNodes.get(k).getBackwardOutputValue();
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
            }
        }
        // 更新隐层到输出层的权重矩阵
        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] -= eta
                        * mHiddenNodes.get(i).getForwardOutputValue()
                        * mOutputNodes.get(j).getBackwardOutputValue();
            }
        }
    }

    public void train(float eta, int n) {
        init();
        for (int i = 0; i < n; i++) {//迭代次数
            for (int j = 0; j < trainNodes.size(); j++) {//训练集124个
                forward(trainNodes.get(j).getAttribList());
                backward(trainNodes.get(j).getType());
                updateWeights(eta);
            }
            System.out.println("n = " + i);

        }
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
        for (int i = 0; i < mInputCount; i++)
            for (int j = 0; j < mHiddenCount; j++) {
                mInputHiddenWeight[i][j] = (float) (Math.random() * 0.1);
            }
        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] = (float) (Math.random() * 0.1);
            }
        }
    }

    public int test(DataNode dn) {
        forward(dn.getAttribList());
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
