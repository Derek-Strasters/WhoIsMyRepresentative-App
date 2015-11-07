package com.example.paracite.whoismyrepdemo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v4.view.ViewGroupCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Derek Strasters on 10/8/15,
 * for the use within the
 * MyApplication project, as a part of the
 * com.example.paracite.whoismyrepdemo package.
 * <p/>
 * TODO: Update copyright information. (Auto Generated)
 * TODO: This file gets JavaDocsR
 */
public class RepImageFlipper extends View {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private Context context;
    private AnimatorSet animatorOutNext;
    private AnimatorSet animatorInNext;
    private AnimatorSet animatorOutPrevious;
    private AnimatorSet animatorInPrevious;

    private final int switchTime;
    private boolean flipping = false;

    private LayoutInflater inflater;
    private ImageView[] dummyViews;
    private List<ImageView> displayViews;

    private int position;
    private int size;

    private ImageView previousView;
    private ImageView currentView;
    private ImageView nextView;

    private Object displayViewsListLock = new Object();
    private Object lock02 = new Object(); //TODO: Deleteme?

    ///////////////////////////////////////////////////////////////////////////
    // Constructor and Overrides
    ///////////////////////////////////////////////////////////////////////////

    public RepImageFlipper(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);

        switchTime = context.getResources().getInteger(R.integer.time_between_flips);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //TODO: stub
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //TODO: stub
    }

///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    // Sets AnimatorSets for incoming, outgoing, views for both forward, and backward flipping
    public void setAnimators(
            int animatorOutNext,
            int animatorInNextID,
            int animatorOutPrevious,
            int animatorInPrevious) {
        this.animatorOutNext = (AnimatorSet) AnimatorInflater.loadAnimator(context, animatorOutNext);
        this.animatorInNext = (AnimatorSet) AnimatorInflater.loadAnimator(context, animatorInNextID);
        this.animatorOutPrevious = (AnimatorSet) AnimatorInflater.loadAnimator(context, animatorInPrevious);
        this.animatorInPrevious = (AnimatorSet) AnimatorInflater.loadAnimator(context, animatorOutPrevious);
    }

    public void loadURLs(String[] urls) {
        // TODO: add validation that throws an error if the URLs have not been loaded.
        dummyViews = new ImageView[urls.length];
        IndexedCallback[] callbacks = new IndexedCallback[urls.length];

        for (int i = 0; i < urls.length; i++) {
            dummyViews[i] = new ImageView(context);
            dummyViews[i].setVisibility(View.GONE);
            callbacks[i] = new IndexedCallback(i);

            Picasso
                    .with(context)
                    .load(urls[i])
                    .fit()
                    .centerInside()
                    .noFade()
                    .into(dummyViews[i], callbacks[i]);
            //.add((ImageView) inflater.inflate(R.layout.flipper_item_image, this, false));
        }
    }

    public void showNext() {
        flipping = true;

        // TODO: Animators go first
        animatorInNext.setTarget(nextView);
        animatorOutNext.setTarget(currentView);

        animatorInNext.start();
        animatorOutNext.start();

        previousView = currentView;
        currentView = nextView;

        nextView = (position + 1 >= size) ? displayViews.get(0) : displayViews.get(position + 1);

        position++;

        // TODO: need to turn flipping flag off
    }

    public void showPrevious() {
        flipping = true;

        // TODO: animations

        nextView = currentView;
        currentView = previousView;

        previousView = (position - 1 < 0) ? displayViews.get(size - 1) : displayViews.get(position - 1);

        position--;

        // TODO: need to tun flipping flag off
    }

    public boolean isFlipping() {
        return flipping;
    }

    private void addToFlippingViews(int i) {
        synchronized (displayViewsListLock) {
            // TODO: Still need to have placeholder and do swap out
            displayViews.add(dummyViews[i]);
            size = displayViews.size();

            // Handle first two cases of a view being added.
            if (size == 1) {
                currentView = displayViews.get(0);
                currentView.setVisibility(View.VISIBLE);
                position = 0;
            } else if (size == 2) {
                nextView = displayViews.get(1);
                previousView = displayViews.get(1);
                // TODO: This is the time to display the image navigation buttons.
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Nested and anonymous classes
    ///////////////////////////////////////////////////////////////////////////

    // Picasso Callback implementation that holds an index field for which dummy it belongs to.
    private class IndexedCallback implements Callback {
        int index;

        IndexedCallback(int index) {
            this.index = index;
        }

        @Override
        public void onSuccess() {
            addToFlippingViews(index);
        }

        @Override
        public void onError() {
        }
    }

    protected Runnable flipTimer = new Runnable() {
        @Override
        public void run() {
            while (isFlipping())
                synchronized (displayViewsListLock) {
                    showNext();
                }
            try {
                Thread.sleep(switchTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
