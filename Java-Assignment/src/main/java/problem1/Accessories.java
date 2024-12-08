package problem1;

public class Accessories {
    String accessoriesType;
    String name;
    int lowerRange;
    int upperRange;

    public Accessories(String accessoriesType, String name, int lowerRange, int upperRange) {
        this.accessoriesType = accessoriesType;
        this.name = name;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    public boolean getKnow(int budget) {
        return this.lowerRange <= budget;
    }

    public String getAccessoriesType() {
        return accessoriesType;
    }

    public void setAccessoriesType(String accessoriesType) {
        this.accessoriesType = accessoriesType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(int lowerRange) {
        this.lowerRange = lowerRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(int upperRange) {
        this.upperRange = upperRange;
    }
}