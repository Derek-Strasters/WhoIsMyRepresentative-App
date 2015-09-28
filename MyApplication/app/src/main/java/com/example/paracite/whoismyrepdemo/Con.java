package com.example.paracite.whoismyrepdemo;

/**
 Created by paracite on 9/9/15.
 <p/>
 A collection of unchanging parameters.
 <p/>
 This has potential to be easily modified to take it's values from an xml resource,
 which is perhaps in the future for this class.
 */
public final class Con {

    public static final String PREFS = "WIMRD-Preference-File.dat";

    public static final String MEM_BY_ZIP_BASE_URL = "http://whoismyrepresentative.com/getall_mems.php?zip=";

    public static final String JSON_SUFIX = "&output=json";
    public static final String RSLT_REPS = "Resultant_Reps";
    public static final String A_BAR_STRING = "Action_Bar_Result_String";

    public static final class Dlg {
        public static final String TITLE = "One Moment";
        public static final String MSG = "Please be patient while the interwebs is delivering the info";
    }

    public final class Arg {
        public static final String SEC_NUM = "section_number";
        public static final String REP_OBJ = "Rep_Passing_in";
        public static final String FRAG_SFX = "Fragment_Reflection_Suffix";
        public static final String PG_NUM = "Current_Result_Page_Number";
        public static final String TOT_PG_NUM = "Total_number_of_result_pages";
    }

    public final class Key {
        public static final String NAME = "name";
        public static final String PARTY = "party";
        public static final String STATE = "state";
        public static final String DISTRICT = "district";
        public static final String PHONE = "phone";
        public static final String OFFICE = "office";
        public static final String LINK = "link";
    }

    public final class Ajax {
        public static final String PREFIX = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=united%20states%20representative%20";
        public static final String SUFFIX = "&userip=";
    }
}
