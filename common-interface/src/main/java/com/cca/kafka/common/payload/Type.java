package com.cca.kafka.common.payload;

public class Type {
    private static final Long BILLION = 1000000000L;
    public static class Normal{
        long records;
        long result;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;
        double lastReceiveTime;
        double messageResidencyTime;

        public double getLastReceiveTime() {
            return lastReceiveTime;
        }

        public void setLastReceiveTime(double lastReceiveTime) {
            this.lastReceiveTime = lastReceiveTime;
        }

        public double getTotalLatency() {
            return totalLatency;
        }

        double totalLatency;

        public void setMsgProcessingTime(double msgProcessingTime) {
            this.msgProcessingTime = msgProcessingTime;
        }

        public double getMessageResidencyTime() {
            return 1.0 * this.totalLatency/this.records;
        }

        public void setMessageResidencyTime(double messageResidencyTime) {
            this.messageResidencyTime = messageResidencyTime;
            this.totalLatency += this.messageResidencyTime;
        }

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }

        public Normal() {
            messageResidencyTime =0;
        }

        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }

        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }

        public double getReceiveTime() {
            return receiveTime;
        }
        public double getAvgMsgDeliveryTime()
        {

            return (records != 0) ? ((double)(1000.0D * ( this.lastReceiveTime - startReceiveTime)/records)) : 0;
        }
        public long getThroughput()
        {
            return (startReceiveTime != 0) ? 1000 * Math.round(records/( System.currentTimeMillis() - startReceiveTime)) : 0;
        }
        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }





        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }
        public void incrRecords(int i) {
            this.records +=i;
        }
        public void maxMsgDeliveryTime(double i) {
            if( i > getMaxMsgDeliveryDelay())
                setMaxMsgDeliveryDelay(i);
        }
        public void incrResult(int i) {
            this.result +=i;
        }
        public void incrMessageProcessingTime(double i) {
            this.msgProcessingTime +=i;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime;
        }

       /* @Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/
    }
    public static class Arrow{
        long records;
        long result;
        long arrowRecords;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;
        double messageResidencyTime;
        double totalLatenncy;
        double lastReceiveTime;

        public double getLastReceiveTime() {
            return lastReceiveTime;
        }

        public void setLastReceiveTime(double lastReceiveTime) {
            this.lastReceiveTime = lastReceiveTime;
        }

        public double getTotalLatenncy() {
            return totalLatenncy;
        }

        public double getMessageResidencyTime() {
            return 1.0 * this.totalLatenncy/this.records;
        }

        public void setMessageResidencyTime(double messageResidencyTime) {
            this.messageResidencyTime = messageResidencyTime;
            this.totalLatenncy += messageResidencyTime;
        }

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }
        public long getArrowRecords() {
            return arrowRecords;
        }

        public void setArrowRecords(long arrowRecords) {
            this.arrowRecords = arrowRecords;
        }

        public Arrow() {
            messageResidencyTime =0;
        }

        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }


        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }
        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }
        public double getReceiveTime() {
            return receiveTime;
        }
        public long getThroughput()
        {
            return (startReceiveTime != 0) ? 1000 * Math.round(records/( System.currentTimeMillis() - startReceiveTime)) : 0;
        }
        public double getAvgMsgDeliveryTime()
        {
            return (records != 0) ? ((double)(1000.0D * ( this.lastReceiveTime  - startReceiveTime)/records)) : 0;
        }
        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }
        public void incrRecords(int i) {
            this.records +=i;
        }
        public void maxMsgDeliveryTime(double i) {
            if( i > getMaxMsgDeliveryDelay())
                setMaxMsgDeliveryDelay(i);
        }
        public void incrResult(int i) {
            this.result +=i;
        }
        public void incrMessageProcessingTime(double i) {
            this.msgProcessingTime +=i;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime;
        }

        /*@Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", arrowRecords =" + arrowRecords +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/

        public void incrArrowMessage(int i) {
            this.arrowRecords += i;
        }
    }
    public static class Total{
        long records;
        long result;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;
        double messageResidencyTime;
        double latencyTime;
        double lastReceiveTime;

        public double getLastReceiveTime() {
            return lastReceiveTime;
        }

        public void setLastReceiveTime(double lastReceiveTime) {
            this.lastReceiveTime = lastReceiveTime;
        }

        public double getMessageResidencyTime() {
            return 1.0 * this.latencyTime/this.records;
        }

        public void setMessageResidencyTime(double messageResidencyTime) {
            this.messageResidencyTime = messageResidencyTime;
            this.latencyTime += messageResidencyTime;
        }

        public double getLatencyTime() {
            return latencyTime;
        }

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }
        public Total() {
            messageResidencyTime = 0;
        }

        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }


        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }
        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }
        public double getReceiveTime() {
            return receiveTime;
        }
        public double getAvgMsgDeliveryTime()
        {
            return (records != 0) ? ((double)(1000.0D * ( this.lastReceiveTime - startReceiveTime)/records)) : 0;
        }
        public long getThroughput()
        {
            return (startReceiveTime != 0) ? 1000 * Math.round(records/( System.currentTimeMillis() - startReceiveTime)) : 0;
        }

        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }
        public void incrMessageProcessingTime(double i) {
            this.msgProcessingTime +=i;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime ;
        }

        /*@Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/
        public void incrResult(int i) {
            this.result +=i;
        }
        public void incrRecords(int i) {
            this.records +=i;
        }
        public void maxMsgDeliveryTime(double i) {
            if( i > getMaxMsgDeliveryDelay())
                setMaxMsgDeliveryDelay(i);
        }

    }
    public static class PythonNormal
    {
        long records;
        long result;
        double totalMsgDeliveryDelay = 0;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }
        public PythonNormal() {
        }

        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }

        public double getTotalMsgDeliveryDelay() {
            return totalMsgDeliveryDelay;
        }

        public void setTotalMsgDeliveryDelay(double totalMsgDeliveryDelay) {
            this.totalMsgDeliveryDelay = totalMsgDeliveryDelay;
        }
        public double getAvgMsgDeliveryTime()
        {
            return (records != 0) ? ((double)(1000.0D * ( System.currentTimeMillis() - startReceiveTime)/records)) : 0;
        }
        public long getThroughput()
        {
            System.out.println(1000L * records/( System.currentTimeMillis() - startReceiveTime));
            return (startReceiveTime != 0) ? Math.round(  1000L * 1.0D *records/( System.currentTimeMillis() - startReceiveTime)) : 0;
        }

        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }

        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }
        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }
        public void setMsgProcessingTime(double msgProcessingTime) {
            this.msgProcessingTime = msgProcessingTime;
        }

        public double getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime;
        }
        /*@Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/

    }

    public static class PyArrow
    {
        long records;
        long result;
        double totalMsgDeliveryDelay = 0;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }
        public PyArrow() {
        }

        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }

        public double getTotalMsgDeliveryDelay() {
            return totalMsgDeliveryDelay;
        }

        public void setTotalMsgDeliveryDelay(double totalMsgDeliveryDelay) {
            this.totalMsgDeliveryDelay = totalMsgDeliveryDelay;
        }

        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }

        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }
        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }
        public void setMsgProcessingTime(double msgProcessingTime) {
            this.msgProcessingTime = msgProcessingTime;
        }

        public double getReceiveTime() {
            return receiveTime;
        }
        public double getAvgMsgDeliveryTime()
        {
            return (records != 0) ? ((double)(1000.0D * ( System.currentTimeMillis() - startReceiveTime)/records)) : 0;
        }
        public long getThroughput()
        {
            return (startReceiveTime != 0) ? Math.round(  1000L * (1.0D *records/( System.currentTimeMillis() - startReceiveTime))) : 0;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime;
        }
        /*@Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/

    }

    public static class Cuda
    {
        long records;
        long result;
        double totalMsgDeliveryDelay =0;
        double maxMsgDeliveryDelay;
        double msgProcessingTime;
        double receiveTime;
        double startReceiveTime;

        public double getStartReceiveTime() {
            return startReceiveTime;
        }

        public void setStartReceiveTime(double startReceiveTime) {
            this.startReceiveTime = startReceiveTime;
        }
        public Cuda() {
        }

        public long getRecords() {
            return records;
        }

        public void setRecords(long records) {
            this.records = records;
        }

        public long getResult() {
            return result;
        }

        public void setResult(long result) {
            this.result = result;
        }

        public double getTotalMsgDeliveryDelay() {
            return totalMsgDeliveryDelay;
        }

        public void setTotalMsgDeliveryDelay(double totalMsgDeliveryDelay) {
            this.totalMsgDeliveryDelay = totalMsgDeliveryDelay;
        }

        public double getMaxMsgDeliveryDelay() {
            return maxMsgDeliveryDelay;
        }

        public void setMaxMsgDeliveryDelay(double maxMsgDeliveryDelay) {
            this.maxMsgDeliveryDelay = maxMsgDeliveryDelay;
        }

        public double getMsgProcessingTime() {
            return msgProcessingTime;
        }
        public double getAvgProcessingTime() {
            return (1.0D * msgProcessingTime * BILLION)/records;
        }
        public void setMsgProcessingTime(double msgProcessingTime) {
            this.msgProcessingTime = msgProcessingTime;
        }

        public double getReceiveTime() {
            return receiveTime;
        }
        public double getAvgMsgDeliveryTime()
        {
            return (records != 0) ? ((double)(1000.0D * ( System.currentTimeMillis() - startReceiveTime)/records)) : 0;
        }
        public long getThroughput()
        {
            return (startReceiveTime != 0) ? 1000 * Math.round(records/( System.currentTimeMillis() - startReceiveTime)) : 0;
        }

        public void setReceiveTime(double receiveTime) {
            this.receiveTime = receiveTime;
        }
        /*@Override
        public String toString() {
            long throughput = (receiveTime != 0) ? Math.round(records/(0.001D * receiveTime)) : 0;
            double avgMsgDeliveryDelay = (records!=0) ? (1.0 * totalMsgDeliveryDelay/records) : 0;
            return "{" +
                "records=" + records +
                ", result=" + result +
                ", avgMsgDeliveryDelay(s)=" + BigDecimal.valueOf(avgMsgDeliveryDelay).toPlainString() +
                ", maxMsgDeliveryDelay(s)=" + BigDecimal.valueOf(maxMsgDeliveryDelay).toPlainString() +
                ", msgProcessingTime(ms)=" + BigDecimal.valueOf(msgProcessingTime).toPlainString() +
                ", totalReceiveTime(ms)=" + BigDecimal.valueOf(receiveTime).toPlainString() +
                ", Throughput(messages/sec)=" + throughput +
                '}';
        }*/

    }
    public static class Partition{
        int number;
        long normalReceiverOffset;
        long arrowStartOffset;
        boolean isHot;

        public Partition(int number, long readOffset, boolean isHot) {
            this.number = number;
            this.normalReceiverOffset = readOffset;
            this.isHot = isHot;
            this.arrowStartOffset =0;
        }

        public void setArrowStartOffset(long arrowStartOffset) {
            this.arrowStartOffset = arrowStartOffset;
        }

        public Partition() {
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public long getNormalReceiverOffset() {
            return normalReceiverOffset;
        }

        public void setNormalReceiverOffset(long normalReceiverOffset) {
            this.normalReceiverOffset = normalReceiverOffset;
        }

        public long getArrowStartOffset() {
            return arrowStartOffset;
        }

        public boolean isHot() {
            return isHot;
        }

        public void setHot(boolean hot) {
            isHot = hot;
        }

        /*@Override
        public String toString() {
            return "{" +
                "number=" + number +
                ", normalReceiverOffset=" + normalReceiverOffset +
                ", arrowStartOffset=" + arrowStartOffset +
                ", isHot=" + isHot +
                '}';
        }*/
    }

}
