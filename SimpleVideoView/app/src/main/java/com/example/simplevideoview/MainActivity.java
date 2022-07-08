package com.example.simplevideoview;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private static final String VIDEO_SAMPLE = "https://www.youtube.com/watch?v=l4372qtZ4dc&ab_channel=nurimbet";

    //"https://github.com/google-developer-training/android-advanced/blob/master/SimpleVideoView/app/src/main/res/raw/tacoma_narrows.mp4";

    private static final String PLAYBACK_TIME = "play_time";
    private VideoView mVideoView;
    private int mCurrentPosition = 0;
    private TextView mBufferingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.video_view);
        mBufferingTextView = findViewById(R.id.buffering_textview);

        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);

        mVideoView.setMediaController(controller);
        if (mCurrentPosition > 0){
            mVideoView.seekTo(mCurrentPosition);
        }else {
            //Skipping to 1 shows the first frame of the video
            mVideoView.seekTo(1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private Uri getMedia(String mediaName){
        if (URLUtil.isValidUrl(mediaName)){
            //Media name is an external URL
            return Uri.parse(mediaName);
        }else {
            return Uri.parse("android.resource://" + getPackageName() + "/raw/" + mediaName);
        }
    }

    private void initializePlayer(){
        //Show the "Buffering..." message while the video loads
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mBufferingTextView.setVisibility(VideoView.INVISIBLE);
                if (mCurrentPosition > 0){
                    mVideoView.seekTo(mCurrentPosition);
                }else {
                    mVideoView.seekTo(1);
                }
                mVideoView.start();
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(0);
            }
        });
    }

    private void releasePlayer(){
        mVideoView.stopPlayback();
    }

    @Override
    protected void onStart(){
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop(){
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mVideoView.pause();
        }
    }
}