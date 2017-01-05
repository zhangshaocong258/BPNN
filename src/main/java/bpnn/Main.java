package bpnn;

/**
 * Created by zsc on 2017/1/5.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BpnnProcessor bpnnProcessor = new BpnnProcessor();
        bpnnProcessor.train();
        bpnnProcessor.test();
    }
}
