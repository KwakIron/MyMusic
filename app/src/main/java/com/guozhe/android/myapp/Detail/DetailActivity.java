package com.guozhe.android.myapp.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.guozhe.android.myapp.MusicPlayer;
import com.guozhe.android.myapp.R;
import com.guozhe.android.myapp.domain.MusicItem;
import com.guozhe.android.myapp.domain.MyMusic;
import com.guozhe.android.myapp.util.TimeUtil;

import java.util.List;

import static com.guozhe.android.myapp.MusicPlayer.getDuration;
import static com.guozhe.android.myapp.MusicPlayer.pause;
import static com.guozhe.android.myapp.MusicPlayer.play;
import static com.guozhe.android.myapp.MusicPlayer.replay;
import static com.guozhe.android.myapp.MusicPlayer.status;
import static com.guozhe.android.myapp.MusicPlayer.stop;
import static com.guozhe.android.myapp.util.TimeUtil.musicTime;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager pager;
    private ImageButton btn_play;
    private ImageButton btn_next;
    private ImageButton btn_previou;
    private TextView current;
    private TextView duration;
    private SeekBar seekBar;
    public static final int CHANGESEEKBAR = 100;
    public static final int STOP_THREAD = 99;
    private SeekBarThread seekBarThread = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGESEEKBAR:
                    setSeekBarCurrent(msg.arg1);
                    break;
                case STOP_THREAD:
                    seekBarThread.setRunFlag(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        setListenner();

        seekBarThread = new SeekBarThread(handler);
        seekBarThread.start();

        DetailAdapter adapter = new DetailAdapter(getData());
        pager.setAdapter(adapter);
        pager.setCurrentItem(getIntentResult());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pause();
                MusicPlayer.init(getData(), position, getApplicationContext(), handler);
                MusicPlayer.player();
                btn_play.setImageResource(android.R.drawable.ic_media_pause);
                duration.setText(musicTime(getDuration()));
                seekBar.setProgress(0);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    MusicPlayer.setCurrent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //데이터 가져오기
    private List<MusicItem> getData() {
        MyMusic myMusic = MyMusic.getInstance();
        myMusic.loader(this);
        return myMusic.getItem();
    }


    // intent로 넘어온 position 값
    private int getIntentResult() {
        int position = -1;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
        }
        return position;
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        btn_play = (ImageButton) findViewById(R.id.btn_play);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_previou = (ImageButton) findViewById(R.id.btn_previou);
        current = (TextView) findViewById(R.id.current);
        duration = (TextView) findViewById(R.id.duration);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    private void setListenner() {
        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_previou.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                play();
                break;
            case R.id.btn_next:
                next();
                break;
            case R.id.btn_previou:
                previou();
                break;
        }
    }

    private void play() {
        if (status == play) {
            btn_play.setImageResource(android.R.drawable.ic_media_pause);
            MusicPlayer.init(getData(), getIntentResult(), this, handler);
            MusicPlayer.player();
            duration.setText(TimeUtil.musicTime(getDuration()));
        } else if (status == stop) {
            btn_play.setImageResource(android.R.drawable.ic_media_play);
            pause();
        } else {
            btn_play.setImageResource(android.R.drawable.ic_media_pause);
            replay();
        }
        Log.d("Duration", "=========" + getDuration());
        seekBar.setMax(getDuration());

    }

    private void next() {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    private void previou() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    public void setSeekBarCurrent(int current) {
        seekBar.setProgress(current);
        this.current.setText(TimeUtil.musicTime(current));
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (MusicPlayer.player != null)
            MusicPlayer.player.pause();
        if(handler != null)
            handler.sendEmptyMessage(DetailActivity.STOP_THREAD);
    }
}