### 老王弄着玩的


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


How use ?

圆形彩色进度条：

xml:
  <com.example.librarymodule.diy.CircleProgessView
        android:id="@+id/circleProgessView"
        android:layout_width="300dp"
        android:layout_height="300dp"/>

java:
        CircleProgessView circleProgess = findViewById(R.id.circleProgessView);
        //文字颜色可传一个或多个，不传时默认为文字颜色
        circleProgess.setProgessColor(true, Color.GREEN, Color.RED, Color.BLUE, Color.GREEN)
                .setShadow(10, 5, 5, Color.GRAY)
                .setStrokeWidth(20)
                .setProgressAnimation(80, 500);


首字母索引：

xml:
   <com.example.librarymodule.diy.LetterIndexView
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

        //自定义属性：背景颜色、选中的文字颜色，未选中的文字颜色、文字大小
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

java:
LetterIndexView letterIndex = findViewById(R.id.letterIndex);
        letterIndex.bindTextView((TextView) findViewById(R.id.textView_letter));

        letterIndex.setOnSelectLinstener(new LetterIndexView.OnSelectLinstener() {
            @Override
            public void onSelcet(int SelectPostion, String selctStr) {
                    //此回调处做列表的滚动逻辑

            }
        });
