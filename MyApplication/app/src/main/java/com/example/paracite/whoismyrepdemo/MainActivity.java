package com.example.paracite.whoismyrepdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     The {@link android.support.v4.view.PagerAdapter} that will provide
     fragments for each of the sections. We use a
     {@link FragmentPagerAdapter} derivative, which will keep every
     loaded fragment in memory. If this becomes too memory intensive, it
     may be best to switch to a
     {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    // The {@link ViewPager} that will host the section contents.
    ViewPager mViewPager;

    /**************************************************
     Overrides
     ******************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Internet connectivity check. Throw popup that exits app on fail.

        // Create the adapter that will return a fragment for each of the five
        // primary api-containing sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //TODO: Make first time run, usage hint, popup.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //TODO: Create settings activity
            return true;
        }

        if (id == R.id.reinit) {
            //TODO: Resets any "hasRunOnce" persistent values
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*******************************************  Static Inner Classes  ***************************************************/

    /**
     A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Context parentContext;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.parentContext = parentContext;
        }

        @Override
        public Fragment getItem(int position) {
            int natural_pos = position + 1;
            return GetReps.newInstance(natural_pos);
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }

    }

    // ****** In this stage of development this is the most completed fragment for use in debugging.

    /*******************
     This fragment returns data on both representatives and senators by zip code.
     *************/
    public static class GetReps
            extends Fragment
            implements View.OnClickListener,
            EditText.OnEditorActionListener,
            JsonGrabListener {

        // Name to be used with reflection to get appropriate xml resource.
        private String fragSufx;

        private Button goButt;
        private EditText editBox;
        private TextView textBody;
        private View rootView;

        public static GetReps newInstance(int sectionNumber) {
            GetReps fragment = new GetReps();
            Bundle args = new Bundle();
            args.putInt(Consts.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);

            //TODO: fragSufx finish defining

            switch (sectionNumber) {
                case 1:
                    fragment.setFragSufx("get_by_zip");
                    //TODO: set text entry validation type here. (create method)
                    break;
                case 2:
                    fragment.setFragSufx("get_all_reps_name");
                    break;
                default:
                    fragment.setFragSufx("get_all_reps_state");
            }

            return fragment;
        }

        public GetReps() {  // Stuberta (Not needed)
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            //------------------------------------------------------------- NEEDS REFLECTION

            // This string will become a field and be set during instantiation by a parameter.

            try {

                Field buttonIdField = R.id.class.getField("button_".concat(fragSufx));
                Field textIdField = R.id.class.getField("textView_".concat(fragSufx));
                Field editTextField = R.id.class.getField("in_".concat(fragSufx));
                Field stringField = R.string.class.getField("string_".concat(fragSufx));
                Field viewField = R.layout.class.getField("frag_".concat(fragSufx));

                rootView = inflater.inflate(viewField.getInt(null), container, false);

                goButt = (Button) rootView.findViewById(buttonIdField.getInt(null));
                goButt.setOnClickListener(this);

                textBody = (TextView) rootView.findViewById(textIdField.getInt(null));
                textBody.setText(getString(stringField.getInt(null)));

                editBox = (EditText) rootView.findViewById(editTextField.getInt(null));
                editBox.setOnEditorActionListener(this);

            } catch (NoSuchFieldException e) {
                Log.e("Frag Reflection", "no such resource name found");
                e.printStackTrace();
                throw new RuntimeException("Reflection error, resource name not found");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.wtf("Frag Reflection", "Illegal Access, this error should never happen. Check resource spelling");
            }
            //------------------------------------------------------------------------

            //TODO: Make popup that informs of rejected data entry (invalid entry)

            return rootView;
        }

        private void startJsonGrab() {
            URL url = JsonUtilities.formURLforGrab(Consts.MEM_BY_ZIP_BASE_URL, editBox.getText().toString());
            new AsyncJsonGrab(this.getContext(), this).execute(url);
        }

        /**************************************
         LISTENERS
         ********************************/

        //TODO: Change function of keyboard return to simulate "OK" button press
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_get_by_zip) {

                //TODO: Add input validation here
                startJsonGrab();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO /* &&TODO: validation */) {
                startJsonGrab();
            }

            return false;
        }

        @Override
        public void onJsonGrabComplete(Representative[] reps) {
            // Start result activity
            Intent i = new Intent(this.getContext(), ResultActivity.class);
            i.putExtra(Consts.RSLT_REPS, reps);
            startActivity(i);
        }

        /********************************
         SETTERS
         ****************************/
        public void setFragSufx(String fgmtSufx) {
            this.fragSufx = fgmtSufx;
        }
    }
}















































