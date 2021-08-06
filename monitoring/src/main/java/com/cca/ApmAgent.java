package com.cca;
import java.util.HashMap;

import com.cca.kafka.common.payload.ConsumerDesState;
import com.wily.introscope.agent.AgentNotAvailableException;
import com.wily.introscope.agent.AgentShim;
import com.wily.introscope.agent.IAgent;
import com.cca.kafka.common.payload.RestHttpClient;
import com.wily.introscope.agent.stat.IIntegerCounterDataAccumulator;
import com.wily.introscope.agent.stat.ILongCounterDataAccumulator;
import com.wily.introscope.agent.stat.IStringEveryEventDataAccumulator;

public class ApmAgent {
    IAgent fAgent;
    private static final Long BILLION = 1000000000L;
    ConsumerDesState state;
    private final String rootPath = "CloudCapstone|Project|";
    private final String TOTAL_RECORDS ="Total Records";
    private final String  RESULT="Total Female";
    private final String MESSAGE_RESIDENCY_TIME = "Average Latency (ms)";
    private final String  MAX_MSG_DELIVERY_DELAY="Maximum Message Delivery Delay (s)";
    private final String AVG_MSG_DELIVERY_DELAY ="Average Message Delivery Delay (microSec)";
    //private final String  TOTAL_MSG_RECEIVE_TIME="Total Message Receive Time (ms)";
    private final String AVERAGE_MSG_PROCESSING_TIME ="Average Message Processing Time (ns)";
    private final String  MESSAGE_THROUGPUT="Message Throughput";

    public ApmAgent() throws AgentNotAvailableException {
        this.fAgent = AgentShim.getAgent();
    }

    public static void main(java.lang.String[] args) throws InterruptedException, AgentNotAvailableException, java.io.IOException {
        System.out.println("test");
        ApmAgent agent = new ApmAgent();
       /* Thread.sleep(10000);*/
        agent.doProcessing();
    }

    private void doProcessing() throws java.io.IOException, InterruptedException {
        RestHttpClient client = new RestHttpClient();

        while(true)
        {
            String s = client.doGetRequest("consumer:8080/consumer/state1", new HashMap<>());
            state = new com.google.gson.Gson().fromJson(s, ConsumerDesState.class);
            sendDataToApm();
            Thread.sleep(7500);
        }

    }

    public void sendDataToApm()
    {
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+TOTAL_RECORDS,state.getNormal().getRecords());
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+RESULT,state.getNormal().getResult());
        //sendLongMetrics(rootPath+"Consumer|Java|Normal:"+MAX_MSG_DELIVERY_DELAY,(long)state.getNormal().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getNormal().getAvgMsgDeliveryTime());
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+MESSAGE_RESIDENCY_TIME,(long) (1000L * state.getNormal().getMessageResidencyTime()));
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getNormal().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Java|Normal:"+MESSAGE_THROUGPUT,state.getNormal().getThroughput());

        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+TOTAL_RECORDS,state.getArrow().getRecords());
        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+RESULT,state.getArrow().getResult());
        //sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+MAX_MSG_DELIVERY_DELAY,(long)state.getArrow().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getArrow().getAvgMsgDeliveryTime());
        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+MESSAGE_RESIDENCY_TIME,(long) (1000L * state.getArrow().getMessageResidencyTime()));
        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getArrow().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Java|Java16Vectorization:"+MESSAGE_THROUGPUT,state.getArrow().getThroughput());

        sendLongMetrics(rootPath+"Consumer|Java|Total:"+TOTAL_RECORDS,state.getTotal().getRecords());
        sendLongMetrics(rootPath+"Consumer|Java|Total:"+RESULT,state.getTotal().getResult());
        //sendLongMetrics(rootPath+"Consumer|Java|Total:"+MAX_MSG_DELIVERY_DELAY,(long)state.getTotal().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Java|Total:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getTotal().getAvgMsgDeliveryTime());
        sendLongMetrics(rootPath+"Consumer|Java|Total:"+MESSAGE_RESIDENCY_TIME,(long) (1000L * state.getTotal().getMessageResidencyTime()));
        sendLongMetrics(rootPath+"Consumer|Java|Total:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getTotal().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Java|Total:"+MESSAGE_THROUGPUT,state.getTotal().getThroughput());

        sendLongMetrics(rootPath+"Consumer|Python|Normal:"+TOTAL_RECORDS,state.getPythonNormal().getRecords());
        sendLongMetrics(rootPath+"Consumer|Python|Normal:"+RESULT,state.getPythonNormal().getResult());
        //sendLongMetrics(rootPath+"Consumer|Python|Normal:"+MAX_MSG_DELIVERY_DELAY,(long)state.getPythonNormal().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Python|Normal:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getPythonNormal().getAvgMsgDeliveryTime());
        //sendLongMetrics(rootPath+"Consumer|Python|Normal:"+TOTAL_MSG_RECEIVE_TIME,(long)state.getPythonNormal().getReceiveTime());
        sendLongMetrics(rootPath+"Consumer|Python|Normal:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getPythonNormal().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Python|Normal:"+MESSAGE_THROUGPUT,state.getPythonNormal().getThroughput());


        sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+TOTAL_RECORDS,state.getPyArrow().getRecords());
        sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+RESULT,state.getPyArrow().getResult());
        //sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+MAX_MSG_DELIVERY_DELAY,(long)state.getPyArrow().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getPyArrow().getAvgMsgDeliveryTime());
        //sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+TOTAL_MSG_RECEIVE_TIME,(long)state.getPyArrow().getReceiveTime());
        sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getPyArrow().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Python|PyArrow:"+MESSAGE_THROUGPUT,state.getPyArrow().getThroughput());


        sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+TOTAL_RECORDS,state.getCuda().getRecords());
        sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+RESULT,state.getCuda().getResult());
        //sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+MAX_MSG_DELIVERY_DELAY,(long)state.getCuda().getMaxMsgDeliveryDelay());
        sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+ AVG_MSG_DELIVERY_DELAY,(long)state.getCuda().getAvgMsgDeliveryTime());
        //sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+TOTAL_MSG_RECEIVE_TIME,(long)state.getCuda().getReceiveTime());
        sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+ AVERAGE_MSG_PROCESSING_TIME,(long)state.getCuda().getAvgProcessingTime());
        sendLongMetrics(rootPath+"Consumer|Python|Cuda:"+MESSAGE_THROUGPUT,state.getCuda().getThroughput());

    }

    private void sendLongMetrics(String path, Long value) {
        ILongCounterDataAccumulator longRecorder = fAgent.IAgent_getDataAccumulatorFactory().
            safeGetLongFluctuatingCounterDataAccumulator(path);
        longRecorder
            .ILongCounterDataAccumulator_setValue(value);

    }

    private void sendIntMetrics(String path, Integer value) {
        IIntegerCounterDataAccumulator intRecorder = fAgent.IAgent_getDataAccumulatorFactory().
            safeGetIntegerFluctuatingCounterDataAccumulator(path);
        intRecorder
            .IIntegerCounterDataAccumulator_setValue(value);

    }

    protected void sendStringMetrics(String path, String value) {
        IStringEveryEventDataAccumulator stringRecorder = fAgent.IAgent_getDataAccumulatorFactory().
            safeGetStringEveryEventDataAccumulator(path);
        stringRecorder.IStringEveryEventDataAccumulator_addString(value);

    }
}
