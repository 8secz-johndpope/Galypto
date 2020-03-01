package com.example.cripto_photoaffix.Activities.GalleryActivities;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.example.cripto_photoaffix.Activities.MyActivity;
import com.example.cripto_photoaffix.Commands.Command;
import com.example.cripto_photoaffix.Commands.RemoveDecryptedMediaCommand;
import com.example.cripto_photoaffix.Commands.RemoveSharedCommand;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.DeleteButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.ShareButtonFactory;
import com.example.cripto_photoaffix.Factories.ButtonFactories.StoreButtonFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.IntentFactory;
import com.example.cripto_photoaffix.Factories.IntentsFactory.LoginIntentFactory;
import com.example.cripto_photoaffix.Gallery.Media;
import com.example.cripto_photoaffix.MediaTransferer;
import com.example.cripto_photoaffix.R;
import com.example.cripto_photoaffix.Visitors.ActivityVisitors.ActivityVisitor;

public abstract class ContentViewerActivity extends MyActivity {
    protected Media media;
    private boolean wentBack;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    protected static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    protected static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    protected static final int UI_ANIMATION_DELAY = 300;
    protected final Handler mHideHandler = new Handler();
    protected View mContentView;
    protected final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    protected View mControlsView;
    protected final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    protected boolean mVisible;
    protected final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    protected final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        media = getMedia();

        wentBack = true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    protected void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    protected void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    protected void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void accept(ActivityVisitor visitor) {}

    @Override
    public void refresh() {}

    /**
     * Inicializa los botones de eliminar, compartir y guardar.
     */
    protected void initializeButtons() {
        LinearLayout layout = findViewById(R.id.fullscreen_content_controls);
        ButtonFactory factory = new DeleteButtonFactory(layout, R.id.delete_button);
        factory.create();

        factory = new ShareButtonFactory(layout, R.id.share_button);
        factory.create();

        factory = new StoreButtonFactory(layout, R.id.save_button);
        factory.create();

        MediaTransferer.getInstance().setMedia(media);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!wentBack) {
            Command command = new RemoveDecryptedMediaCommand();
            command.execute();

            command = new RemoveSharedCommand();
            command.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!wentBack) {
            Command command = new RemoveDecryptedMediaCommand();
            command.execute();

            command = new RemoveSharedCommand();
            command.execute();

            IntentFactory factory = new LoginIntentFactory();
            startActivity(factory.create());
            wentBack = true;
        }
        else {
            wentBack = false;

            media = getMedia();

            loadMedia();

            initializeButtons();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        wentBack = true;
    }

    /**
     * Carga la "media" en la vista donde deberia ser cargada.
     */
    public abstract void loadMedia();

    /**
     * Actualiza la "media" que se va a mostrar.
     * @return Media a mostrar.
     */
    private Media getMedia() {
        MediaTransferer transferer = MediaTransferer.getInstance();

        return transferer.getMedia();
    }
}
