package org.futa.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

/**
 * @author Dataempower6
 */
public class FlinkMain {
    public static void main(String[] args) {

        //TODO 0.envExecution
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //TODO 1.source
        DataSet<String> lines = env.fromElements("itcast hadoopspark", "itcast hadoop spark", "itcast hadoop", "itcast");
        //TODO 2.transformation

        // 切割
        /*  @FunctionalInterface
            public interface FlatMapFunction<T, O> extends Function,Serializable{
                void flatMap(T value, Collector<O> out) throwsException;
            }
        */

        DataSet<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            publicvoidflatMap(Stringvalue, Collector<String> out) throws Exception {
                //value表示每一行数据
                String[] arr = value.split(" ");
                for (Stringword:
                     arr) {
                    out.collect(word);
                }
            }
        });
    }
}