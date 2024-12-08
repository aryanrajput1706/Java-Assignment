package problem1;

import java.util.*;

public class Car {
    String name;
    List<String> variants;
    List<String> range;

    Map<String, Float> mp = new HashMap<>();

    public Car(String name, List<String> variants, List<String> range) {
        this.name = name;
        this.variants = variants;
        this.range = range;

        int sizeVariable = range.size();

        for (int i = 0; i < sizeVariable; i++) {
            float value = Float.parseFloat(range.get(i));
            mp.put(variants.get(i), value * 100000);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getVariants() {
        return variants;
    }

    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    public List<String> getRange() {
        return range;
    }

    public void setRange(List<String> range) {
        this.range = range;
    }

    public Map<String, Float> getMp() {
        return mp;
    }

}
