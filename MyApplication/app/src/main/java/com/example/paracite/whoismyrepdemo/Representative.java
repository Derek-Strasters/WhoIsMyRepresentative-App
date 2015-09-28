package com.example.paracite.whoismyrepdemo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 Created by paracite on 9/23/15.
 <p/>
 Contains a field for each value corresponding to keys found
 in a returned JSON object from any of the WhoIsMyRepresentative.com API's (09/25/15).
 It implements {@link Parcelable} such to be passed by intent when creating activities.
 */
public class Representative implements Parcelable {

    private String name = null;
    private String party = null;
    private String state = null;
    private String district = null;
    private String phone = null;
    private String office = null;
    private String link = null;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public Representative() {
    }

    public Representative(
            String name,
            String party,
            String state,
            String district,
            String phone,
            String office,
            String link) {
        this.name = name;
        this.party = party;
        this.state = state;
        this.district = district;
        this.phone = phone;
        this.office = office;
        this.link = link;
    }

    public Representative(JSONObject jRep) {

        try {
            if (jRep.has(Con.Key.NAME)) ;
            this.name = jRep.getString(Con.Key.NAME);
            if (jRep.has(Con.Key.PARTY))
                this.party = jRep.getString(Con.Key.PARTY);
            if (jRep.has(Con.Key.STATE))
                this.state = jRep.getString(Con.Key.STATE);
            if (jRep.has(Con.Key.DISTRICT))
                this.district = jRep.getString(Con.Key.DISTRICT);

            // Remove hyphens
            // TODO: Phone Call API creator and check for any other possibilities.
            if (jRep.has(Con.Key.PHONE)) {
                String hyphened = jRep.getString(Con.Key.PHONE);
                this.phone = hyphened.replace("-", "");
            }
            if (jRep.has(Con.Key.OFFICE))
                this.office = jRep.getString(Con.Key.OFFICE);

            // Validates URL prefix
            if (jRep.has(Con.Key.LINK)) {
                String url = jRep.getString(Con.Key.LINK);
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://".concat(url);
                this.link = url;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: Handle case of JSON exception with message.
        }
    }

    private Representative(Parcel in) {
        this.name = in.readString();
        this.party = in.readString();
        this.state = in.readString();
        this.district = in.readString();
        this.phone = in.readString();
        this.office = in.readString();
        this.link = in.readString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    // Handy duck-tape/bailing-wire solution to passing arrays of Representative objects.
    // Takes in a Parcelable array created from a Representative array and returns the Representative array.
    public static Representative[] toArray(Parcelable[] parcelables) {
        Representative[] reps = new Representative[parcelables.length];
        System.arraycopy(parcelables, 0, reps, 0, parcelables.length);
        return reps;
    }

    public boolean isRepsValid() {
        if (name == null ||
                party == null ||
                state == null ||
                district == null ||
                phone == null ||
                office == null ||
                link == null
                ) {
            return false;
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getState() {
        return state;
    }

    public String getDstr() {
        return district;
    }

    public String getPhone() {
        return phone;
    }

    public String getOffice() {
        return office;
    }

    public String getLink() {
        return link;
    }

    // Retruns the link in <a> tags for use in text view with "text" for the text
    public String getLinkInTags(String text) {
        String taggedLink = "";
        return taggedLink.concat("<a href=\"").concat(link).concat("\">").concat(text).concat("</a>");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Setters
    ///////////////////////////////////////////////////////////////////////////

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setLink(String link) {
        this.link = link;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Overrides
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(party);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(phone);
        dest.writeString(office);
        dest.writeString(link);

        //TODO: Throw exception if isRepsValid fails? Needs investigation.
    }

    public static final Creator<Representative> CREATOR = new Parcelable.Creator<Representative>() {

        @Override
        public Representative createFromParcel(Parcel in) {
            return new Representative(in);
        }

        @Override
        public Representative[] newArray(int size) {
            return new Representative[size];
        }

    };
}
























