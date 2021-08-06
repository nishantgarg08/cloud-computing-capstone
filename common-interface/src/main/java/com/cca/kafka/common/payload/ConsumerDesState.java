package com.cca.kafka.common.payload;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;



public class ConsumerDesState {
    Type.Normal normal;
    Type.Arrow arrow;
    Type.Total total;
    ConcurrentHashMap<java.lang.Integer, Type.Partition> partitions;
    Type.PythonNormal pythonNormal;
    Type.PyArrow pyArrow;
    Type.Cuda cuda;

    public ConsumerDesState(Type.Normal normal, Type.Arrow arrow, Type.Total total,
        Type.PythonNormal pythonNormal, Type.PyArrow pyArrow, Type.Cuda cuda,
        ConcurrentHashMap<Integer, Type.Partition> partitions) {
        this.normal = normal;
        this.arrow = arrow;
        this.total = total;
        this.pythonNormal = pythonNormal;
        this.pyArrow = pyArrow;
        this.cuda = cuda;
        this.partitions = partitions;
    }

    public void setNormal(Type.Normal normal) {
        this.normal = normal;
    }

    public void setArrow(Type.Arrow arrow) {
        this.arrow = arrow;
    }

    public void setTotal(Type.Total total) {
        this.total = total;
    }

    public void setPartitions(
        ConcurrentHashMap<Integer, Type.Partition> partitions) {
        this.partitions = partitions;
    }
/*@Override
    public String toString() {
        return "ConsumerState{" +
            "normal=" + normal +
            ", arrow=" + arrow +
            ", total=" + total +
            ", partitions=" + partitions +
            ", pythonNormal=" + pythonNormal +
            ", pyArrow=" + pyArrow +
            ", cuda=" + cuda +
            '}';
    }*/

    public Type.PythonNormal getPythonNormal() {
        return pythonNormal;
    }

    public Type.PyArrow getPyArrow() {
        return pyArrow;
    }

    public Type.Cuda getCuda() {
        return cuda;
    }

    public void setCuda(Type.Cuda cuda) {
        this.cuda = cuda;
    }

    public void setPyArrow(Type.PyArrow pyArrow) {
        this.pyArrow = pyArrow;
    }

    public void setPythonNormal(Type.PythonNormal pythonNormal) {
        this.pythonNormal = pythonNormal;
    }

    public Type.Normal getNormal() {
        return normal;
    }

    public Type.Arrow getArrow() {
        return arrow;
    }

    public Type.Total getTotal() {
        return total;
    }

    public ConcurrentHashMap<java.lang.Integer, Type.Partition> getPartitions() {
        return partitions;
    }

    public ConsumerDesState() {
        normal = new Type.Normal();
        arrow = new Type.Arrow();
        total = new Type.Total();
        pythonNormal = new Type.PythonNormal();
        pyArrow = new Type.PyArrow();
        cuda = new Type.Cuda();
        partitions = new ConcurrentHashMap<>();
        Type.Partition p = new Type.Partition(0, 0, false);
        partitions.put(0, p);
    }

    /*@java.lang.Override
    public java.lang.String toString() {
        return "ConsumerState1{" +
            "normal=" + normal +
            ", arrow=" + arrow +
            ", total=" + total +
            ", py-normal=" + pythonNormal +
            ", pyArrow=" + pyArrow +
            ", cuda=" + cuda +
            ", partition=" + partitions +
            '}';
    }*/


}
