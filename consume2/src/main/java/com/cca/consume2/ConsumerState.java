package com.cca.consume2;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import com.cca.kafka.common.payload.Type;
@Configuration
@Scope("singleton")
public class ConsumerState {
    Type.Normal normal;
    Type.Arrow arrow;
    Type.Total total;
    ConcurrentHashMap<Integer, Type.Partition> partitions;
    Type.PythonNormal pythonNormal;
    Type.PyArrow pyArrow;
    Type.Cuda cuda;

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

    public ConcurrentHashMap<Integer, Type.Partition> getPartitions() {
        return partitions;
    }

    public ConsumerState() {
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

    /*@Override
    public String toString() {
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
