package com.cca.arrow.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.arrow.vector.VectorSchemaRoot;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cca.kafka.common.payload.Person;

@Component
@Scope("singleton")
public class PersonDb {
    private CopyOnWriteArrayList<Person> persons;
    
    public PersonDb() {
        persons = new CopyOnWriteArrayList<>();
    }
    public void addPerson(Person p)
    {
        persons.add(p);
    }
    public void cleanAll()
    {
        persons.clear();
    }
    public List<Person> poll()
    {
        List<Person> x = new ArrayList<>(persons);
        persons.clear();
        return x;
    }
    public int size()
    {
        return persons.size();
    }
}
