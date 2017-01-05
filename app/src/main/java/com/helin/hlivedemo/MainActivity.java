package com.helin.hlivedemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.helin.hlivedemo.adapter.TableFragmentPagerAdapter;
import com.helin.hlivedemo.fragment.CommentFragment;
import com.helin.hlivedemo.fragment.RewardFragment;
import com.helin.hlivedemo.view.HVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {


    private HVideoPlayer mVideoPlayerStandard;
    private int mRoomId;
    private EditText mMsgEditText;
    private String mBeginTime;

    private TabLayout mTableLayout;
    private List<Fragment> mFragmentList =  new ArrayList<Fragment>();

    //全屏下播放器对象
    public HVideoPlayer mFullScreenPlayer;
    private TableFragmentPagerAdapter mViewPageadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mVideoPlayerStandard = (HVideoPlayer) findViewById(R.id.custom_videoplayer_standard);
        mMsgEditText = (EditText) findViewById(R.id.message);
        mTableLayout=(TabLayout)findViewById(R.id.tabLayout);


        Button sendBtn = (Button) findViewById(R.id.sendMsg);
        mFragmentList.add(CommentFragment.newInstance());
        mFragmentList.add(RewardFragment.newInstance());
        //Fragment+ViewPager+FragmentViewPager组合的使用
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPageadapter = new TableFragmentPagerAdapter(getSupportFragmentManager(),
                mFragmentList);
        viewPager.setAdapter(mViewPageadapter);
        mTableLayout.setupWithViewPager(viewPager);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgContent = mMsgEditText.getText().toString();
                sendMessage(msgContent);
            }
        });

        mVideoPlayerStandard.setOnSendMsgListener(new HVideoPlayer.OnSendMsgListener() {
            @Override
            public void sendMsg(String msg) {
                sendMessage(msg);
            }
        });
        mVideoPlayerStandard.setOnPayListener(new HVideoPlayer.OnPayListener() {
            @Override
            public void showPay() {
                Toast.makeText(MainActivity.this,"赞赏",Toast.LENGTH_SHORT).show();
            }
        });
        mVideoPlayerStandard.setOnFullScreenListener(new HVideoPlayer.OnFullScreenListener() {
            @Override
            public void onFullScreen(HVideoPlayer hVideoPlayer) {
                mFullScreenPlayer = hVideoPlayer;
            }
        });

        mVideoPlayerStandard.setJcUserAction(new JCUserAction() {
            @Override
            public void onEvent(int type, String url, int screen, Object... objects) {
                switch (type){
                    case JCVideoPlayer.CURRENT_STATE_PLAYING:

                        break;
                    case JCVideoPlayer.CURRENT_STATE_PAUSE:

                        break;

                }
            }
        });

        //rtmp://203.207.99.19:1935/live/CCTV5
        //http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8
        //http://ivi.bupt.edu.cn/hls/cctv1.m3u8
        mVideoPlayerStandard.setUp("http://ivi.bupt.edu.cn/hls/cctv1.m3u8"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "房间名称");
    }

    private void sendMessage(String msgContent) {
        //判断是否全屏，全屏则显示弹幕
        if (mFullScreenPlayer != null && mFullScreenPlayer.isFullScreen()) {
            mFullScreenPlayer.addDanmaku(msgContent, true);
        }
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            //隐藏弹幕
            if (mFullScreenPlayer != null) {
                mFullScreenPlayer.hideDanmu();
                mFullScreenPlayer=null;
            }
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoPlayerStandard.danmaDes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        if (mFullScreenPlayer != null) {
            mFullScreenPlayer.hideDanmu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoPlayerStandard.danmaResume();
    }
}
