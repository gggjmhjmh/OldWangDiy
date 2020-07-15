### 老王弄着玩


Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.gggjmhjmh:OldWangDiy:版本名'
	}


## How use ?


#### MessageDialog：

```
 @Override
    public void onBackPressed() {
        new MessageDialog(this, "确定要退出应用？")
                .show(new MessageDialog.OnBaseClickListener() {
                    @Override
                    public void onRightBtClick(View v) {
                        MainActivity.super.onBackPressed();
                    }
                });
    }
```
    
    
#### 圆形彩色进度条：

xml:
```
  <com.oldwang.librarymodule.diy.CircleProgessView
        android:id="@+id/circleProgessView"
        android:layout_width="300dp"
        android:layout_height="300dp"/>
```
java:

        CircleProgessView circleProgess = findViewById(R.id.circleProgessView);
        //进度条颜色可传一个或多个，不传时默认为文字颜色
        circleProgess.setProgessColor(true, Color.GREEN, Color.RED, Color.BLUE, Color.GREEN)
                .setShadow(10, 5, 5, Color.GRAY)
                .setStrokeWidth(20)
                .setProgressAnimation(80, 500);


#### 首字母索引：

xml:

```
  //自定义属性：背景颜色、选中的文字颜色，未选中的文字颜色、文字大小
   <com.oldwang.librarymodule.diy.LetterIndexView
        android:id="@+id/letterIndex"
        android:layout_width="35dp"
        android:layout_height="match_parent"
        android:layout_marginTop="50dp"
        android:background="#cccccccc"
        android:paddingTop="15dp"
        android:paddingBottom="20dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"

        app:bg_color="@color/colorAccent"
        app:select_text_color="#E61A5F"
        app:unselect_text_color="#666"
        app:text_size="16sp"/>

    //屏幕中间显示选中的tv
    <TextView
        android:id="@+id/textView_letter"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:background="#cccccccc"
        android:gravity="center"
        android:textColor="#000"
        android:textSize="24sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```
java:
```
	LetterIndexView letterIndex = findViewById(R.id.letterIndex);
        letterIndex.bindTextView((TextView) findViewById(R.id.textView_letter));

        letterIndex.setOnSelectLinstener(new LetterIndexView.OnSelectLinstener() {
            @Override
            public void onSelcet(int SelectPostion, String selctStr) {
                    //此回调处做列表的滚动逻辑

            }
        });
```

#### 高德定位：

  1、首先，要在高德开放平台创建应用、添加key
  2、然后，在AndroidManifest.xml配置文件application标签里添加代码，填写平台上生成的key
```
     <meta-data
         android:name="com.amap.api.v2.apikey"
         android:value="your key" />
     <service android:name="com.amap.api.location.APSService" />
```
  3、开始定位
```
     //定位结束后的监听
     GDLocationManager.getInstance().setListener(new MyLocationManager.LocationCallback() {
         @Override
         public void onLocationSucceed(LocationInfo info) {
             if (info != null) {
                 TextView tv_location = findViewById(R.id.tv_location);
                 tv_location.setText(info.getAddr());
             }
         }
     });
     //开始定位，每调用一次，只定位一次，不会连续定位
     GDLocationManager.getInstance().start(LocationActivity.this);
```
  但是，你可能没有开启定位权限和定位服务，你可以自己写这个逻辑，也可以参考我的代码：
```
public void doClick(View view) {

        //先检查是否开启定位权限和定位服务
        MyLocationManager.checkPermissionAndService(this, new MyLocationManager.IsOpenedCallback() {
            @Override
            public void opened() {
                //同意了定位权限并且开启了定位服务
                //定位结束后的监听
                GDLocationManager.getInstance().setListener(new MyLocationManager.LocationCallback() {
                    @Override
                    public void onLocationSucceed(LocationInfo info) {
                        if (info != null) {
                            TextView tv_location = findViewById(R.id.tv_location);
                            tv_location.setText(info.getAddr());
                        }
                    }
                });
                //开始定位，每调用一次，只定位一次，不会连续定位
                GDLocationManager.getInstance().start(LocationActivity.this);
            }

            @Override
            public void notOpenPermission() {
                //未打开权限,用户拒绝开开启权限了
                MyLocationManager.toSystemSetPage(LocationActivity.this);
            }

            @Override
            public void notOpenService() {
                //未打开定位服务
                MyLocationManager.toOpenLocationService(LocationActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //跳转到系统设置页面返回
        if (requestCode == LOCATION_PERMISSION_CODE) {
            //检查是否开启定位权限和定位服务
            MyLocationManager.checkPermissionAndService(this, new MyLocationManager.IsOpenedCallback() {
                @Override
                public void opened() {

                }

                @Override
                public void notOpenPermission() {
                    //未打开权限,用户拒绝开启权限了
                    Toast.makeText(LocationActivity.this, "未开启定位权限，无法定位", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void notOpenService() {

                }
            });
        }
        //跳转到系统定位设置页面返回
        else if (requestCode == LOCATION_SERVICE_CODE) {
            if (!MyLocationManager.isOpenLocationService(this)) {
                Toast.makeText(this, "未开启定位服务，无法定位", Toast.LENGTH_SHORT).show();
            }
        }
    }

```
