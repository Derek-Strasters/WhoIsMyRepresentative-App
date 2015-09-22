package com.example.paracite.whoismyrepdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

/**************************************************  Overrides   ******************************************************/

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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int natural_pos = position + 1;
            switch (natural_pos) {
                case 1:
                    return GetAllMembersFragment.newInstance(natural_pos);
                case 2:
                    return GetAllRepsByNameFragment.newInstance(natural_pos);
                case 3:
                    return GetAllRepsByStateFragment.newInstance(natural_pos);
                case 4:
                    return GetAllSensByNameFragment.newInstance(natural_pos);
                case 5:
                    return GetAllSensByStateFragment.newInstance(natural_pos);
                default:
                    return null;
            }
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

/*******************    This fragment returns data on both representatives and senators by zipcode.     ***************/
    public static class GetAllMembersFragment extends Fragment implements View.OnClickListener {

        private Button buttonMem;
        private EditText editMem;
        private View rootView;
        private AsyncJsonGrab jGrabber;

        public static GetAllMembersFragment newInstance(int sectionNumber) {
            GetAllMembersFragment fragment = new GetAllMembersFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);

            fragment.jGrabber = new AsyncJsonGrab(fragment.getContext().getApplicationContext());
            return fragment;
        }

        public GetAllMembersFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.frag_get_all_members, container, false);

            buttonMem = (Button) rootView.findViewById(R.id.button_get_all_members_zip);
            buttonMem.setOnClickListener(this);

            editMem = (EditText) rootView.findViewById(R.id.in_get_all_members_zip);

            //TODO: Make popup that informs of rejected data entry (invalid entry)

            return rootView;
        }

        //TODO: Change function of keyboard return to simulate "OK" button press
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_get_all_members_zip) {

                //TODO: Add input validation here

                URL url = JsonUtilities.formURLforGrab(Parms.MEM_BY_ZIP_BASE_URL, editMem.getText().toString());

                jGrabber.execute(url);
            }
        }
    }

/**********************    This fragment returns data on representatives by last name.  *******************************/
    public static class GetAllRepsByNameFragment extends Fragment {

        public static GetAllRepsByNameFragment newInstance(int sectionNumber) {
            GetAllRepsByNameFragment fragment = new GetAllRepsByNameFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetAllRepsByNameFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.frag_get_all_reps_name, container, false);
        }
    }

/**********************    This fragment returns data on representatives by state.    *********************************/
    public static class GetAllRepsByStateFragment extends Fragment {

        public static GetAllRepsByStateFragment newInstance(int sectionNumber) {
            GetAllRepsByStateFragment fragment = new GetAllRepsByStateFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetAllRepsByStateFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.frag_get_all_reps_state, container, false);
        }
    }

/**********************    This fragment returns data on senators by last name.     ***********************************/
    public static class GetAllSensByNameFragment extends Fragment {

        public static GetAllSensByNameFragment newInstance(int sectionNumber) {
            GetAllSensByNameFragment fragment = new GetAllSensByNameFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetAllSensByNameFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.frag_get_all_sens_name, container, false);
        }
    }

/**********************    This fragment returns data on senators by state.     ***************************************/
    public static class GetAllSensByStateFragment extends Fragment {

        public static GetAllSensByStateFragment newInstance(int sectionNumber) {
            GetAllSensByStateFragment fragment = new GetAllSensByStateFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetAllSensByStateFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.frag_get_all_sens_state, container, false);
        }
    }

/*******************************************    JSON and web     ******************************************************/


}















































