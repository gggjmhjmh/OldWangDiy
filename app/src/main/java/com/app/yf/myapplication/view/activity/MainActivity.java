package com.app.yf.myapplication.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.yf.myapplication.R;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import cn.jzvd.JzvdStd;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageView iv,iv2;

    private GSYADVideoPlayer ADVideoPlayer;
    private OrientationUtils orientationUtils;
    private StandardGSYVideoPlayer videoPlayer;

    private JzvdStd jzvdLocalPath;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        iv2 = findViewById(R.id.iv2);

        loadImg();
        downImg();

//        gsyVideo();
//        jiaoZi();

        test();
    }

    private void test() {
        iv.setOnClickListener(view -> {
            startActivity(new Intent(this,ThreeActivity.class));
        });
    }


    private void jiaoZi() {

        //checkPermission
        int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }

        jzvdLocalPath = findViewById(R.id.lcoal_path);
        //播放本地sd视频文件
        String url = copyVideoToLocalPath("local_video.mp4", "aaa");
        jzvdLocalPath.setUp(url, "Play Local Video");
        jzvdLocalPath.fullscreenButton.performClick();  //全屏
        jzvdLocalPath.startButton.performClick();  //播放
    }


    /**
     * 检查权限
     *
     * @return
     */
    private boolean checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_LONG).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            Toast.makeText(this, "已经授权 存储权限！", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkPermission: 已经授权！");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean flag = true; //是否全部权限都同意
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == -1) { //如果有一个没同意,-1是没同意; 0是同意
                flag = false;
                break;
            }
        }
        //如果都同意了
        if (flag) {
            // TODO:  做权限同意后才能做的事
            gsyVideo();
        }

    }

    /**
     * 复制Assets下的文件到存储卡
     *
     * @param assetsFileName assets下的要复制的文件名
     * @param sdFolder       sd卡保存位置的文件夹路径
     * @return sd卡保存的文件路径
     */
    public String copyVideoToLocalPath(String assetsFileName, String sdFolder) {
        String localFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + sdFolder + "/" + assetsFileName;
        if (new File(localFilePath).exists()) return localFilePath;

        try {
            InputStream myInput = this.getAssets().open(assetsFileName);
            //用的是com.blankj.utilcode 库的工具
            if (FileIOUtils.writeFileFromIS(localFilePath, myInput)) {
                return localFilePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(this, false, true);
    }

    private void gsyVideo() {

        if (!checkPermission()) return;

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

//本地视频地址
        String url = copyVideoToLocalPath("local_video.mp4", "aaa");

        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel(url, "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD));
        //正式内容1
//        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://a.mp4", "正文1标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));
        //广告2
        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4", "", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD, true));
        //正式内容2
//        urls.add(new GSYSampleADVideoPlayer.GSYADVideoModel("http://b.mp4", "正文2标题", GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL));


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
/*
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

        *//*ADVideoPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });*//*


        ADVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(ADVideoPlayer);
            }
        });*/
    }

    private void resolveNormalVideoUI() {
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
//        ADVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        ADVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
    }


    private void downImg() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "gsyVideo";
        File file = new File(dir);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            System.out.println("~~~~~~~~~~" + b);
        }
        String url = "http://wx.sunzhoubo.top/img/b.jpg";
        OkGo.<File>get(url)
                .headers("Accept-Encoding", "gzip, default").
                execute(new FileCallback(dir, "b.jpg") {
                    @Override
                    public void onSuccess(Response<File> response) {
//                iv.setImageURI(Uri.fromFile(response.body()));
                        Glide.with(MainActivity.this).load(response.body()).into(iv2);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                    }
                });

    }

    private void loadImg() {
        String url = "http://wx.sunzhoubo.top/img/b.jpg";
//        Glide.with(this).load(url).into(iv);

       /* //加载gzip，不这样做好像也可以加载gzip
        GlideUrl glideUrl = new GlideUrl(url, new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                //不一定都要添加，具体看原站的请求信息
                header.put("Accept-Encoding", "gzip, default");
                return header;
            }
        });
        System.out.println("~~~~~~~~~~~" + glideUrl.getHeaders());

        Glide.with(this).load(glideUrl).into(iv);*/


        Glide.with(this).load(url)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                .into(iv);

    }


    @Override
    protected void onPause() {
        super.onPause();
//        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        videoPlayer.onVideoResume();
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
