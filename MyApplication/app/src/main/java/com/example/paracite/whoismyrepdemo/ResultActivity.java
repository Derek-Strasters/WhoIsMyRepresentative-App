package com.example.paracite.whoismyrepdemo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    Representative[] reps;
    String aBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Con.RSLT_REPS);
        //TODO: Determine if this should be written to savedInstanceState and what overrides to use (onReturn...)
        reps = Representative.toArray(parcelables);
        aBarTitle = intent.getStringExtra(Con.A_BAR_STRING);

        // Create the adapter that will return a fragment for each of the representatives
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), reps);

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.result_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        // Set title to contents of the edit text that were passed in.
        setTitle(getTitle().toString().concat(" ").concat(aBarTitle));

        //TODO: Make first time run, usage hint, popup.
        //TODO: Make careful analysis of how to persist data when focus leaves activity e.g. user gets a call.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner and Nested Classes
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Representative Info Page Fragment Class
    ///////////////////////////////////////////////////////////////////////////

    // This fragment returns data on both representatives and senators by zip code.
    public static class RepInfoPage extends Fragment implements
            View.OnClickListener,
            AsyncAjaxJsonGrab.AjaxJsonGrabListener {

        private View view;

        private TextView textViewRepName;
        private TextView textViewStateName;
        private TextView textViewDstr;
        private TextView textViewOfficeLoc;
        private TextView textViewPageNumber;

        private Button buttExit;
        private Button buttWeb;
        private Button buttCall;
        private Button buttNextImg;
        private Button buttLastImg;

        private RepImageFlipper flipper;
        private ImageURLAdapter urlAdapter;
        private ImageView placeHolder;
        //FIXME: delete the below if needed.
        private boolean isPlaceHolderAChild;
        AnimatorSet inLeftSet;
        AnimatorSet outLeftSet;
        AnimatorSet inRightSet;
        AnimatorSet outRightSet;

        private int repNumber;
        private int repTotal;
        private Representative rep;

        ///////////////////////////////////////////////////////////////////////////
        // RepInfoPage Methods
        ///////////////////////////////////////////////////////////////////////////

        // Not so busy constructor
        public RepInfoPage() {
        }

        // Returns a ready to go instantiation based on sectionNumber
        public static RepInfoPage newInstance(int pageNum, int totalPageNum, Representative rep) {
            RepInfoPage repInfoPageFrag = new RepInfoPage();
            Bundle args = new Bundle();
            args.putInt(Con.Arg.PG_NUM, pageNum);
            args.putInt(Con.Arg.TOT_PG_NUM, totalPageNum);
            args.putParcelable(Con.Arg.REP_OBJ, rep);
            repInfoPageFrag.setArguments(args);

            return repInfoPageFrag;
        }

        ///////////////////////////////////////////////////////////////////////////
        // RepInfoPage Overrides and Callbacks
        ///////////////////////////////////////////////////////////////////////////

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            // Get the fragment's arguments and load contents to the appropriate objects and fields.
            Bundle inBund = getArguments();
            repNumber = inBund.getInt(Con.Arg.PG_NUM);
            repTotal = inBund.getInt(Con.Arg.TOT_PG_NUM);
            rep = inBund.getParcelable(Con.Arg.REP_OBJ);

            // Setup all the views with the appropriate resources
            view = inflater.inflate(R.layout.frag_activity_result, container, false);
            buttExit = (Button) view.findViewById(R.id.exit_button);
            buttWeb = (Button) view.findViewById(R.id.web_button);
            buttCall = (Button) view.findViewById(R.id.call_button);
            buttNextImg = (Button) view.findViewById(R.id.next_image);
            buttLastImg = (Button) view.findViewById(R.id.last_image);
            textViewRepName = (TextView) view.findViewById(R.id.rep_name);
            textViewStateName = (TextView) view.findViewById(R.id.state_name);
            textViewDstr = (TextView) view.findViewById(R.id.district);
            textViewOfficeLoc = (TextView) view.findViewById(R.id.office_location);
            textViewPageNumber = (TextView) view.findViewById(R.id.page_number);
            flipper = (RepImageFlipper) view.findViewById(R.id.rep_image_flipper);

            // Set the onclick listeners for the buttons.
            buttExit.setOnClickListener(this);
            buttWeb.setOnClickListener(this);
            buttCall.setOnClickListener(this);

            // Image next and last button ClickListeners are set and made visible when the images are available.
            buttNextImg.setVisibility(View.INVISIBLE);
            buttLastImg.setVisibility(View.INVISIBLE);

            // Set text to the contents of the Representative object
            textViewRepName.setText(rep.getName());
            textViewStateName.setText(rep.getState());
            // Set text to diplay "District" if district string length is small implying number otherwise "Position".
            //TODO: make a real check for a number value.
            textViewDstr.setText(((rep.getDstr().length() <= 2) ? "District " : "Position ").concat(rep.getDstr()));
            textViewOfficeLoc.setText(rep.getOffice());
            // Set page numbering text
            textViewPageNumber.setText(String.valueOf(repNumber).concat(" of ").concat(String.valueOf(repTotal)));

            // Set color of background based on party
            if (rep.getParty().equals("D"))
                view.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.darkblue));
            else if (rep.getParty().equals("R"))
                view.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.darkred));
            else
                view.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.white));

            view.getBackground().setAlpha(85);

            new AsyncAjaxJsonGrab(this).execute(rep.getName());

            // Load animations
            inLeftSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                    this.getContext(),
                    R.animator.card_flip_left_in);
            outLeftSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                    this.getContext(),
                    R.animator.card_flip_left_out);
            inRightSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                    this.getContext(),
                    R.animator.card_flip_right_in);
            outRightSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                    this.getContext(),
                    R.animator.card_flip_right_out);

            flipper.setAnimators(
                    R.animator.card_flip_left_out,
                    R.animator.card_flip_left_in,
                    R.animator.card_flip_right_out,
                    R.animator.card_flip_right_in);


            return view;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // TODO: evaluate need for Bitmap recycling (How does Picasso handle such?)
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                // Exit button clicks finish the parent activity and return to the main activity.
                case (R.id.exit_button):
                    getActivity().finish();
                    break;

                // Call button will start the dialer activity with the representatives number ready to dial.
                case (R.id.call_button):
                    // TODO: Check for error and throw message instead of unexplained crash
                    // TODO: Add local popup with number displayed on hover
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:".concat(rep.getPhone())));
                    startActivity(callIntent);
                    break;

                // Web button will start default browser activity on the representatives URL.
                case (R.id.web_button):
                    // TODO: add popup to notify of bad web page
                    // TODO: Add local popup with URL displayed on hover
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rep.getLink()));
                    startActivity(browserIntent);
                    break;

                //TODO: Show next/last image if loaded.
                // Setup animations for the flipper going one way on next button.
                case (R.id.next_image):
                    flipper.showNext();
                    break;

                // Setup animations for the flipper going the opposite way on previous button.
                case (R.id.last_image):
                    flipper.showPrevious();
                    break;
            }
        }

        // TODO: check for and mitigate memory leak possibilities due to strong referencing.
        @Override
        public void onAjaxJsonGrabComplete(String[] urls) {
            flipper.loadURLs(urls);



            // Set click listeners for the image navigation buttons.
            // TODO: move to when first two images are ready.
            buttNextImg.setOnClickListener(this);
            buttLastImg.setOnClickListener(this);
            buttNextImg.setVisibility(View.VISIBLE);
            buttLastImg.setVisibility(View.VISIBLE);

            //flipper.startFlipping(); //TODO: Related to flickering issue?
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Pager Adapter
    ///////////////////////////////////////////////////////////////////////////

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Representative[] reps;

        public SectionsPagerAdapter(FragmentManager fm, Representative[] reps) {
            super(fm);
            this.reps = reps;
        }

        @Override
        public Fragment getItem(int position) {
            int natural_pos = position + 1;
            return RepInfoPage.newInstance(natural_pos, reps.length, reps[position]);
        }

        @Override
        public int getCount() {
            return reps.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return "Result Number".concat(String.valueOf(position));
        }

    }

}
