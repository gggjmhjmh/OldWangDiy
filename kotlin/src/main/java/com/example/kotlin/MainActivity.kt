package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.oldwang.lib_location.GDLocationManager
import com.oldwang.lib_location.LocationInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        GDLocationManager.getInstance().setListener { info -> location.setText(info.addr) }
        location.setOnClickListener(View.OnClickListener { GDLocationManager.getInstance().start(this) })

        toast()


    }

    private fun toast() {
        Toast.makeText(this, "啊", Toast.LENGTH_SHORT).show()

    }


}
