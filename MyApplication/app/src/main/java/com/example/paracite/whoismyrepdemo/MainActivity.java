package com.example.paracite.whoismyrepdemo;

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

    /** The {@link ViewPager} that will host the section contents.*/
    ViewPager mViewPager;



    ///////////////////////////////////////////////////////////////////////////
    // Overrides
    ///////////////////////////////////////////////////////////////////////////

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

    //TODO: Specify parent activities.
    /*
    Handle action bar item clicks here. The action bar will
    automatically handle clicks on the Home/Up button, so long
    as you specify a parent activity in AndroidManifest.xml.
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //TODO: Create settings activity
            return true;
        }

        if (id == R.id.reinit) {
            //TODO: Spawn popup assertion for a "reset any hasRunOnce persistent values for helper dialogs"
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    // This fragment returns data on both representatives and senators by zip code.
    public static class GetReps
            extends Fragment
            implements
            View.OnClickListener,
            EditText.OnEditorActionListener,
            AsyncRepJsonGrab.RepJsonGrabListener {

        // Name to be used with reflection to get appropriate xml resource.
        private String fragSufx;

        private Button goButt;
        private EditText editBox;
        private TextView textBody;
        private View rootView;

        ///////////////////////////////////////////////////////////////////////////
        // GetReps Methods
        ///////////////////////////////////////////////////////////////////////////

        // Not so busy constructor
        public GetReps() {  // Stuberta (Not needed)
        }

        // Returns a ready to go instantiation based on sectionNumber
        //TODO: XML of suffixes is parsed for an array of the possible fragment pages.  The SectionsPagerAdapter is
        //todo: responsible for initiating this action, setting the appropriate index to the count, and passing in
        //todo: the appropriate suffix string in plase of "sectionNumber". The switch is then not needed.
        public static GetReps newInstance(int sectionNumber) {
            GetReps getRepsFrag = new GetReps();
            Bundle args = new Bundle();
            args.putInt(Con.Arg.SEC_NUM, sectionNumber);
            getRepsFrag.setArguments(args);

            //TODO: fragSufx finish defining

            switch (sectionNumber) {
                case 1:
                    args.putString(Con.Arg.FRAG_SFX, "get_by_zip");
                    break;
                case 2:
                    args.putString(Con.Arg.FRAG_SFX, "get_all_reps_name");
                    break;
                default:
                    args.putString(Con.Arg.FRAG_SFX, "get_all_reps_state");
            }

            return getRepsFrag;
        }

        public void setFragSufx(String fgmtSufx) {
            this.fragSufx = fgmtSufx;
        }

        private void jsonGrab() {
            URL url = JsonUtilities.formURLforGrab(Con.MEM_BY_ZIP_BASE_URL, editBox.getText().toString());
            new AsyncRepJsonGrab(this.getContext(), this).execute(url);
        }

        ///////////////////////////////////////////////////////////////////////////
        // GetReps Overrides and Callbacks
        ///////////////////////////////////////////////////////////////////////////

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            //TODO: is parcelable error handling needed?
            Bundle inBund = getArguments();
            fragSufx = inBund.getString(Con.Arg.FRAG_SFX);

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

            //TODO: Make popup that informs of rejected data entry (invalid entry)

            return rootView;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_get_by_zip) {

                //TODO: make keyboard pop up if field is empty
                //TODO: Add input validation here
                jsonGrab();
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO /* &&TODO: validation */) {
                jsonGrab();
            }

            return false;
        }

        // TODO: check for and mitigate memory leak possibilities due to strong referencing.
        @Override
        public void onRepJsonGrabComplete(Representative[] reps) {
            // Start result activity
            Intent i = new Intent(this.getContext(), ResultActivity.class);
            i.putExtra(Con.RSLT_REPS, reps);
            i.putExtra(Con.A_BAR_STRING, editBox.getText().toString());
            startActivity(i);
        }
    }

    /**
     A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
}















































