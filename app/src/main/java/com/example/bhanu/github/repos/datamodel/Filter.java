package com.example.bhanu.github.repos.datamodel;

import java.util.ArrayList;

public class Filter {

    public enum PRIVACY{
        PUBLIC, PRIVATE, BOTH
    }


    public Filter(){
        privacyPossibleValues = new ArrayList<>();
        privacyPossibleValues.add(PRIVACY.PUBLIC);
        privacyPossibleValues.add(PRIVACY.PRIVATE);
        privacyPossibleValues.add(PRIVACY.BOTH);
    }

    private ArrayList<PRIVACY> privacyPossibleValues;


    public ArrayList<PRIVACY> getPrivacyPossibleValues() {
        return privacyPossibleValues;
    }

    public void setPrivacyPossibleValues(ArrayList<PRIVACY> privacyPossibleValues) {
        this.privacyPossibleValues = privacyPossibleValues;
    }

    private PRIVACY privacy;

    public PRIVACY getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PRIVACY privacy) {
        this.privacy = privacy;
    }
}
