package com.app.yf.myapplication.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.yf.myapplication.R;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView target;

    private GSYADVideoPlayer ADVideoPlayer;
    private OrientationUtils orientationUtils;
    private StandardGSYVideoPlayer videoPlayer;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        target = findViewById(R.id.iv);
//        okgo();

        gsyVideo();


//        downImg();
        loadImg();  //加载gzip压缩过的图片


        test();
    }

    private void test() {
        TextView tv1 = findViewById(R.id.tv1);
        TextView et = findViewById(R.id.et);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("", "checkPermission: 已经授权！");
        }
    }

    private String localVideoPath;

    public void copyVideoToLocalPath() {
        localVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4";
        if (new File(localVideoPath).exists()) return;

        try {

            InputStream myInput = this.getAssets().open("local_video.mp4");
            FileIOUtils.writeFileFromIS(localVideoPath, myInput);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(this, false, true);
    }

    private void gsyVideo() {

        checkPermission();
        ADVideoPlayer = findViewById(R.id.ad_player);
        videoPlayer = findViewById(R.id.player);

        //EXOPlayer内核，支持格式更多
//        PlayerFactory.setPlayManager(Exo2PlayerManager.class);

        ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> urls = new ArrayList<>();
        //广告1
//        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4",
//                "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD));

//            String url = Uri.parse("file:///android_asset/" +  "guide.mp4").getPath();
//        String url = RawResourceDataSource.buildRawResourceUri(R.raw.guide).getPath();

        copyVideoToLocalPath();
        String url = localVideoPath;


        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel(url, "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD));
        //正式内容1
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", "正文1标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));
        //广告2
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4", "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD, true));
        //正式内容2
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4", "正文2标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));


        //封面ImageView
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);

        resolveNormalVideoUI();

        videoPlayer.setUp(url, true, "啦啦");
        //增加封面
        videoPlayer.setThumbImageView(imageView);

        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setShowFullAnimation(false);
        videoPlayer.setNeedLockFull(true);
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });


        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoPlayer.startWindowFullscreen(MainActivity.this, false, true);
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();


//todo 待处理 广告视频播放器

        ADVideoPlayer.setUp(url, true, "");
//增加封面
        ADVideoPlayer.setThumbImageView(imageView);


        ADVideoPlayer.setIsTouchWiget(true);
        //关闭自动旋转
        ADVideoPlayer.setRotateViewAuto(false);
        ADVideoPlayer.setLockLand(false);
        ADVideoPlayer.setShowFullAnimation(true);
        ADVideoPlayer.setNeedLockFull(true);

//        ADVideoPlayer.setVideoAllCallBack(this);

        /*ADVideoPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });*/


        ADVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(ADVideoPlayer);
            }
        });
    }

    private void resolveNormalVideoUI() {
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
//        ADVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        ADVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
    }


    //画质压缩
    private void sdlk() {
        Bitmap bitmap = BitmapFactory.decodeFile(""); //图片源
//        ImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG); //bitmap转byte[]
        byte[] bytes = ImageUtils.compressByQuality(bitmap, 80);

        String path = Environment.getExternalStorageDirectory().getPath() + "/NAME_PIC_FILE.jpg";
        FileIOUtils.writeFileFromBytesByStream(path, bytes);
    }


    private void downImg() {
        String dir = Environment.getExternalStorageDirectory().getPath() + "/" + "gsyVideo";
        File file = new File(dir);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            System.out.println("~~~~~~~~~~" + b);
        }
        String url = "http://wx.sunzhoubo.top/img/b.jpg";
        OkGo.<File>get(url).
                headers("Accept-Encoding", "gzip, default").
                execute(new FileCallback(dir, "b.jpg") {
                    @Override
                    public void onSuccess(Response<File> response) {
//                iv.setImageURI(Uri.fromFile(response.body()));
                        Glide.with(MainActivity.this).load(response.body()).into(target);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                    }
                });

    }

    private void loadImg() {
        String url = "http://wx.sunzhoubo.top/img/b.jpg";

        Glide.with(this).load(url).into(target);

        /*GlideUrl glideUrl = new GlideUrl(url, new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                //不一定都要添加，具体看原站的请求信息
                header.put("Accept-Encoding", "gzip, default");
                return header;
            }
        });
        System.out.println("~~~~~~~~~~~"+glideUrl.getHeaders());

        Glide.with(this).load(glideUrl).into(iv);*/

    }



    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

}
