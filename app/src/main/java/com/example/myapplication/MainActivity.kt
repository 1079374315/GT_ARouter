package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baselibrary.Config
import com.gsls.gt_databinding.route.annotation.GT_Route
//import com.example.baselibrary.Config
//import com.gsls.gt_databinding.route.annotation.GT_Route
import com.gsls.gtk.logt

@GT_Route(value = Config.AppConfig.MAIN_ACTIVITY)
class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        "初始化 MainActivity".logt()

//        startActivity(Intent(this@MainActivity, JavaActivity::class.java))
        /*findViewById<Button>(R.id.btn).setOnClickListener {
            ARouter.getInstance().build("/model1/ModelActivity1").navigation<Any>()
        }*/


    }

    override fun finish() {
        super.finish()
//        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }

}