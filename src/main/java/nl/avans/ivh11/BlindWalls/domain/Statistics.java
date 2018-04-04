package nl.avans.ivh11.BlindWalls.domain;

public class Statistics {

    //create an object of Statistics. search starts at 0
    private static Statistics instance = new Statistics();
    private int amountSearch = 0;

    //make the constructor private so that this class cannot be
    //instantiated
    private Statistics(){}

    //Get the only object available
    public static Statistics getInstance(){
        return instance;
    }

    public int getSearch() {
        return amountSearch;
    }

    public void addSearch() {
        this.amountSearch++;

    }

}