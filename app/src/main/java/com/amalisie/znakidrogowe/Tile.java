package com.amalisie.znakidrogowe;

public interface Tile {

    public void uncover();
    public void cover();
    public RoadSign getRoadSign();
    public void setRoadSign(RoadSign roadSign);
    public boolean isCovered();
    public boolean isPaired();
    public void setPaired();
}
