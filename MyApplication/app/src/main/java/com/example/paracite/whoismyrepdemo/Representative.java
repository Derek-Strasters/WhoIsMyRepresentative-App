package com.example.paracite.whoismyrepdemo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 Created by paracite on 9/23/15.

 "Representative" class contains field for each string value found in a returned representative JSON object.
    It implements Parcelable so as to be easily passed to a new activity through an intent.
 */
public class Representative implements Parcelable{

    //TODO: validation that a name is returned
    private String name = null;
    private String party = null;
    private String state = null;
    private String district = null;
    private String phone = null;
    private String office = null;
    private String link = null;

    public Representative(JSONObject jRep) {

        try {
            if (jRep.has("name"))
                this.name = jRep.getString("name");
            if (jRep.has("party"))
                this.party = jRep.getString("party");
            if (jRep.has("district"))
                this.party = jRep.getString("district");
            if (jRep.has("phone"))
                this.party = jRep.getString("phone");
            if (jRep.has("office"))
                this.party = jRep.getString("office");
            if (jRep.has("link"))
                this.party = jRep.getString("link");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Representative(Parcel in) {
        name = in.readString();
        party = in.readString();
        state = in.readString();
        district = in.readString();
        phone = in.readString();
        office = in.readString();
        link =  in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(party);
        out.writeString(state);
        out.writeString(district);
        out.writeString(phone);
        out.writeString(office);
        out.writeString(link);
    }

    public static final Parcelable.Creator<Representative> CREATOR = new Parcelable.Creator<Representative>() {
        public Representative createFromParcel(Parcel in) {
            return new Representative(in);
        }

        public Representative[] newArray(int size) {
            return new Representative[size];
        }

    };

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getPhone() {
        return phone;
    }

    public String getOffice() {
        return office;
    }

    // Retruns the link in <a> tags for use in text view with "text" for the text
    public String getLinkInTags(String text) {
        String taggedLink = "";
        return taggedLink.concat("<a href=\"").concat(link).concat("\">").concat(text).concat("</a>");
    }

    @Override
    public int describeContents() {

        return 0;
    }

}
























