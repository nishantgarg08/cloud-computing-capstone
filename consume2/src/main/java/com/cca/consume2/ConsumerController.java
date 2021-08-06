package com.cca.consume2;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cca.kafka.common.payload.ConsumerDesState;
import com.cca.kafka.common.payload.Type;

@RestController
@Configuration
@RequestMapping("/consumer")
public class ConsumerController  {
    @Autowired
    private ConsumerState state;

    @RequestMapping("/arrow-statrt-offset")
    public void setArrowOffset(@RequestParam Integer offset)
    {
        state.getPartitions().get(0).setArrowStartOffset(offset);
    }
    @RequestMapping("/state1")
    public Object getState() {
        //state.setHot(true);

        return new ConsumerDesState(state.normal,state.arrow, state.total, state.pythonNormal, state.pyArrow, state.cuda, state.partitions);
    }
    @RequestMapping("/state")
    public Object getCurrentState() {
        //state.setHot(true);
        return state;
    }
    /*@RequestMapping("/state")
    public String setHot() {
        //state.setHot(true);
        String ret = state.toString();
        return ret;
    }*/
    // GET localhost:8080/consumer/notify/1?hotStatus=true
    @RequestMapping(value = "/notify/{partionId}")
    public @ResponseBody
    Long updatePartitionInfo(@PathVariable("partionId") int partitionId, @RequestParam Boolean hotStatus) {
        state.getPartitions().get(partitionId).setHot(hotStatus);
        state.getPartitions().get(partitionId).setNumber(partitionId);
        long offset = state.getPartitions().get(partitionId).getNormalReceiverOffset();
        state.getPartitions().get(partitionId).setArrowStartOffset(offset +1);
        return offset;
    }
    @RequestMapping(value = "/python-normal", method = RequestMethod.POST)
    public @ResponseBody String registerPythonNormalRecords(@RequestBody String body) {
        JSONObject p = new JSONObject(body);
        Type.PythonNormal pythonNormal = new Type.PythonNormal();
        pythonNormal.setMaxMsgDeliveryDelay(p.getDouble("maxMsgDeliveryDelay"));
        pythonNormal.setRecords(p.getLong("records"));
        pythonNormal.setResult(p.getLong("result"));
        pythonNormal.setTotalMsgDeliveryDelay(p.getDouble("totalMsgDeliveryDelay"));
        pythonNormal.setMsgProcessingTime(p.getDouble("msgProcessingTime"));
        pythonNormal.setReceiveTime(p.getDouble("receiveTime"));
        pythonNormal.setStartReceiveTime(p.getDouble("recordStartTime"));
        state.setPythonNormal(pythonNormal);
        return "";
    }

    @RequestMapping(value = "/pyarrow", method = RequestMethod.POST)
    public @ResponseBody String registerArrowRecords(@RequestBody String body) {
        JSONObject p = new JSONObject(body);
        Type.PyArrow pyArrow = new Type.PyArrow();
        pyArrow.setMaxMsgDeliveryDelay(p.getDouble("maxMsgDeliveryDelay"));
        pyArrow.setRecords(p.getLong("records"));
        pyArrow.setResult(p.getLong("result"));
        pyArrow.setTotalMsgDeliveryDelay(p.getDouble("totalMsgDeliveryDelay"));
        pyArrow.setMsgProcessingTime(p.getDouble("msgProcessingTime"));
        pyArrow.setReceiveTime(p.getDouble("receiveTime"));
        pyArrow.setStartReceiveTime(p.getDouble("recordStartTime"));
        state.setPyArrow(pyArrow);
        return "";
    }
    @RequestMapping(value = "/cuda", method = RequestMethod.POST)
    public @ResponseBody String registerCudaRecords(@RequestBody String body) {
        JSONObject p = new JSONObject(body);
        Type.Cuda cuda = new Type.Cuda();
        cuda.setMaxMsgDeliveryDelay(p.getDouble("maxMsgDeliveryDelay"));
        cuda.setRecords(p.getLong("records"));
        cuda.setResult(p.getLong("result"));
        cuda.setTotalMsgDeliveryDelay(p.getDouble("totalMsgDeliveryDelay"));
        cuda.setMsgProcessingTime(p.getDouble("msgProcessingTime"));
        cuda.setReceiveTime(p.getDouble("receiveTime"));
        state.setCuda(cuda);
        return "";
    }
}
