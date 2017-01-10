package bpnn;

import java.util.List;

/**
 * Created by zsc on 2017/1/10.
 * 分类算法接口
 */
public interface Classifier {
    int mInputCount = 4;
    int mHiddenCount = 10;
    int mOutputCount = 3;

    void forward(List<Float> list);
    void backward(int type, float eta);
    void train(float eta, int n);
    void test();
}
