package org.jfl.Day07_Generics.practice;

import org.jfl.Day08_Exceptions.PropertyNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Repository<T extends Propriedade> {
    List<T> listItems = new ArrayList<>();

    public void add(T item){
        listItems.add(item);
    }

    public T getItem(int index){
        if (index > this.size()){
            return null;
        }
        return listItems.get(index);
    }

    public List<T> getAll(){
        return listItems;
    }

    public void print(){
        listItems.stream().forEach(System.out::println);
    }

    int size(){
        return listItems.size();
    }

    public static double calculateTotalPrice(List<? extends Propriedade> properties){
        return properties.stream()
                         .mapToDouble(Propriedade::getPrice)
                         .sum();
    }

    public static void copyProperties(List<? extends Propriedade> source, List <? super Propriedade> dest) {
        source.forEach(dest::add);
    }

    //Day08 - Exceptions
    public Propriedade findPropriedade(Long id) throws PropertyNotFoundException {
       return listItems.stream()
               .filter( p -> p.getId().equals(id))
               .findFirst()
               .orElseThrow(() -> new PropertyNotFoundException(id));
    }



}
