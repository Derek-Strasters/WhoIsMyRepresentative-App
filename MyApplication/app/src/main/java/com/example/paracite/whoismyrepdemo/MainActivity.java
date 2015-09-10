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

import java.util.Locale;

import static android.util.Log.wtf;

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

    /**
     The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Internet connectivity check. Throw popup that exits app on fail.

        // Create the adapter that will return a fragment for each of the five
        // primary api containing sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.item1) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*******************************************Static Inner Classes***************************************************/
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
            switch (position) {
                case 1:
                    return GetAllMembersFragment.newInstance(position);
                case 2:
                    return GetAllRepsByNameFragment.newInstance(position);
                case 3:
                    return GetAllRepsByStateFragment.newInstance(position);
                case 4:
                    return GetAllSensByNameFragment.newInstance(position);
                case 5:
                    return GetAllSensByStateFragment.newInstance(position);
                default:
                    wtf("MainActivity.SectionsPagerAdapter.getItem :", "invalid or null position value.");
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













    /**
     This fragment returns data on both representatives and senators by zipcode.
     */
    public static class GetAllMembersFragment extends Fragment {

        public static GetAllMembersFragment newInstance(int sectionNumber) {
            GetAllMembersFragment fragment = new GetAllMembersFragment();
            Bundle args = new Bundle();
            args.putInt(Parms.ARG_SEC_NUM, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GetAllMembersFragment() {
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            return inflater.inflate(R.layout.frag_get_all_members, container, false);
        }
    }













    /**
     This fragment returns data on representatives by last name.
     */
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





















    /**
     This fragment returns data on representatives by state.
     */
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















    /**
     This fragment returns data on senators by last name.
     */
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















    /**
     This fragment returns data on senators by state.
     */
    public static class GetAllSensByStateFragment extends Fragment {

        public static GetAllMembersFragment newInstance(int sectionNumber) {
            GetAllMembersFragment fragment = new GetAllMembersFragment();
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
}















































