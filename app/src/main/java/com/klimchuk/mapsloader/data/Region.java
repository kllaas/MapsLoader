package com.klimchuk.mapsloader.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey on 04.05.17.
 */

public class Region implements Serializable {

    private String name;

    private List<Region> regions;

    private Region topRegion;

    private String fileName;

    private LoadingState loadingState = LoadingState.NOT_LOADING;

    public Region(String name, Region topRegion) {
        this.name = name;
        this.topRegion = topRegion;
        this.regions = new ArrayList<>();
    }

    public Region(String name, String fileName) {
        this.name = name;
        this.regions = new ArrayList<>();
        this.fileName = fileName;
        this.regions = new ArrayList<>();
    }

    public Region(String name) {
        this.name = name;
        this.regions = new ArrayList<>();
    }

    public boolean isFirstLevel() {
        return getDepth() == 1;
    }

    public int getDepth() {
        int depth = 0;

        Region tmpRegion = getTopRegion();
        while (tmpRegion != null) {
            tmpRegion = tmpRegion.getTopRegion();
            depth++;
        }

        return depth;
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Region> getNestedRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public Region getTopRegion() {
        return topRegion;
    }

    public void setTopRegion(Region topRegion) {
        this.topRegion = topRegion;
    }

    public enum LoadingState {
        NOT_LOADING,
        LOADING,
        LOADED
    }

}
