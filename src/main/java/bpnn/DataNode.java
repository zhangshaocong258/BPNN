package bpnn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsc on 2017/1/5.
 * 训练集、测试集数据结构
 */
public class DataNode {
    private List<Float> mAttribList;
    private int type;//三种类型，映射为1 2 3

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Float> getAttribList() {
        return mAttribList;
    }

    public void addAttrib(Float value) {
        mAttribList.add(value);
    }

    public DataNode() {
        mAttribList = new ArrayList<Float>();
    }
}
