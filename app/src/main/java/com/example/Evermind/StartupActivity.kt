package com.example.Evermind

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.startup_activity.*


class StartupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup_activity)

        ////BELOW CODE SET STATUS BAR TO TRANSPARENT

        fun setWindowFlag(bits: Int, on: Boolean) {
            val win = window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
            //window.navigationBarColor = Color.TRANSPARENT //DONT KNOW IF IT WORKS
        }

        /////ABOVE SET STATUS BAR TRANSPARENT

        val animationDrawable = background.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(20)
        animationDrawable.setExitFadeDuration(1000)
        animationDrawable.start()

        val startButton = findViewById(R.id.startButton) as Button
        val circle1 = findViewById(R.id.Circle) as ImageView
        val circle2 = findViewById(R.id.Circle2) as ImageView
        val circle3 = findViewById(R.id.Circle3) as ImageView
        val circle4 = findViewById(R.id.Circle4) as ImageView
        val circle5 = findViewById(R.id.Circle5) as ImageView

        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeInButton = AnimationUtils.loadAnimation(this, R.anim.fade_in_elephant)

        EvermindBlack.startAnimation(fadein)
        startButton.startAnimation(fadeInButton)

        val button_Anim = AnimationUtils.loadAnimation(this, R.anim.button_press)

        val fade = AnimationUtils.loadAnimation(this, R.anim.fade_title)

        val fadebutton = AnimationUtils.loadAnimation(this, R.anim.fade_out_button)

        val focusedButton = AnimationUtils.loadAnimation(this, R.anim.button_focused)

        val scaleup = AnimationUtils.loadAnimation(this, R.anim.scaleup)
        val scaleup2 = AnimationUtils.loadAnimation(this, R.anim.scaleup2)
        val scaleup3 = AnimationUtils.loadAnimation(this, R.anim.scaleup3)
        val scaleup4 = AnimationUtils.loadAnimation(this, R.anim.scaleup4)
        val scaleup5 = AnimationUtils.loadAnimation(this, R.anim.scaleup5)

        startButton.setOnClickListener {
            startButton.startAnimation(button_Anim)
            EvermindBlack.startAnimation(fade)
            circle1.visibility=View.VISIBLE
            circle2.visibility=View.VISIBLE
            circle3.visibility=View.VISIBLE
            circle4.visibility=View.VISIBLE
            circle5.visibility=View.VISIBLE
            circle1.startAnimation(scaleup)
            circle2.startAnimation(scaleup2)
            circle3.startAnimation(scaleup3)
            circle4.startAnimation(scaleup4)
            circle5.startAnimation(scaleup5)
            startButton.startAnimation(fadebutton)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //overridePendingTransition(R.anim.fade_title, R.anim.fade_in);
        }

        //fun longclick() {
        //startButton.startAnimation(focusedButton)
        //EvermindBlack.startAnimation(button_Anim)
        //}

        //fun dispathAnEventOnLongClick(startButton: Button): Boolean {
        //longclick()
        //return true;
        //}

        //startButton.setOnLongClickListener {
        //dispathAnEventOnLongClick(startButton);
        //}
        startButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> startButton.startAnimation(focusedButton)//Do Something
            }

            v?.onTouchEvent(event) ?: true
        }
    }
}
