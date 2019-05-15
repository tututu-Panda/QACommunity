package com.qa.recommend.reWrite;

/**
 * Created by 3tu on 2019/5/13.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class Keyword implements Comparable<Keyword> {
    private String name;
    private double score;
    private double idf;
    private int freq;

    public Keyword(String name, int docFreq, double weight) {
        this.name = name;
        this.idf = Math.log(1.0D + 10000.0D / (double)(docFreq + 1));
        this.score = this.idf * weight;
        ++this.freq;
    }

    public Keyword(String name, double score) {
        this.name = name;
        this.score = score;
        this.idf = score;
        ++this.freq;
    }

    public void updateWeight(int weight) {
        this.score += (double)weight * this.idf;
        ++this.freq;
    }

    public int getFreq() {
        return this.freq;
    }

    public int compareTo(Keyword o) {
        return this.score < o.score?1:-1;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Keyword) {
            Keyword k = (Keyword)obj;
            return k.name.equals(this.name);
        } else {
            return false;
        }
    }

    public String toString() {
        return this.name + "/" + this.score;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
