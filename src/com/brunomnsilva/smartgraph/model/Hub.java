package com.brunomnsilva.smartgraph.model;

public class Hub {

    private String code;
    private String name;
    private int population;
    private double x;
    private double y;

    public Hub(String code, String name) {
        this.code = code;
        this.name = name;
        this.population = -1;
        this.x = -1;
        this.y = -1;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Hub{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public int getPopulation() {
        return this.population;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getIdentifier() {
        return getName() + ", " + getCode();
    }

}
