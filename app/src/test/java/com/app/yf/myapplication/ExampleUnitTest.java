package com.app.yf.myapplication;

import android.util.Log;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

//        System.out.println("16进制: " + Integer.toHexString((int) (255 * 0.65)));

        String str = "哈希部落16485465900310";
        hash(str);

        String str2 = "御枫" + System.currentTimeMillis();
        hash(str2);

    }

    private void hash(String str) {
        System.out.println("");
        System.out.println(str);
        String hash = EncryptUtils.encryptSHA256ToString(str);
        System.out.println("哈希值: " + hash);
        System.out.println("最终值：" + ConvertUtils.hexString2Int(hash.substring(0, 5)));
    }
}