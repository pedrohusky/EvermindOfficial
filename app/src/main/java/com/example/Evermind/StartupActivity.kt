package com.example.Evermind
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.startup_activity.*
import java.util.*

class StartupActivity : AppCompatActivity() {

    private var notes = java.util.ArrayList<String>()
    private var titles = java.util.ArrayList<String>()
    private var dates = java.util.ArrayList<String>()
    private var ids = java.util.ArrayList<Int>()
    private var imageURL = java.util.ArrayList<String>()
    private var draws = java.util.ArrayList<String>()
    private var colors = java.util.ArrayList<String>()
    private val noteModels = java.util.ArrayList<Note_Model>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup_activity)

        val everDataBase: EverDataBase = EverDataBase(this)

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

            if (Build.VERSION.SDK_INT in 19..20) {
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

            val startButton = findViewById<Button>(R.id.startButton)
            val circle1 = findViewById<ImageView>(R.id.Circle)
            val circle2 = findViewById<ImageView>(R.id.Circle2)
            val circle3 = findViewById<ImageView>(R.id.Circle3)
            val circle4 = findViewById<ImageView>(R.id.Circle4)
            val circle5 = findViewById<ImageView>(R.id.Circle5)

            val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            val fadeInButton = AnimationUtils.loadAnimation(this, R.anim.fade_in_elephant)

            EvermindBlack.startAnimation(fadein)
            startButton.startAnimation(fadeInButton)

            val buttonAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)

            val fade = AnimationUtils.loadAnimation(this, R.anim.fade_title)

            val fadebutton = AnimationUtils.loadAnimation(this, R.anim.fade_out_button)

            val focusedButton = AnimationUtils.loadAnimation(this, R.anim.button_focused)

            val scaleup = AnimationUtils.loadAnimation(this, R.anim.scaleup)
            val scaleup2 = AnimationUtils.loadAnimation(this, R.anim.scaleup2)
            val scaleup3 = AnimationUtils.loadAnimation(this, R.anim.scaleup3)
            val scaleup4 = AnimationUtils.loadAnimation(this, R.anim.scaleup4)
            val scaleup5 = AnimationUtils.loadAnimation(this, R.anim.scaleup5)

            startButton.setOnClickListener {

                Handler(Looper.getMainLooper()).post {
                    startButton.startAnimation(buttonAnim)
                    EvermindBlack.startAnimation(fade)
                    circle1.visibility = View.VISIBLE
                    circle2.visibility = View.VISIBLE
                    circle3.visibility = View.VISIBLE
                    circle4.visibility = View.VISIBLE
                    circle5.visibility = View.VISIBLE
                    circle1.startAnimation(scaleup)
                    circle2.startAnimation(scaleup2)
                    circle3.startAnimation(scaleup3)
                    circle4.startAnimation(scaleup4)
                    circle5.startAnimation(scaleup5)
                    startButton.startAnimation(fadebutton)

                    noteModels.clear()

                    everDataBase.getAllContentsFromAllNotes()

                    ids = everDataBase.idFromDatabase

                    notes = everDataBase.contentsFromDatabase

                    titles = everDataBase.titlesFromDatabase

                    dates = everDataBase.dateFromDatabase

                    imageURL = everDataBase.imageURLFromDatabase

                    draws = everDataBase.drawLocationFromDatabase

                    colors = everDataBase.noteColorsFromDatabase


                    for (i in ids.indices) {
                        noteModels.add(
                            Note_Model(
                                ids[i],
                                i,
                                titles[i],
                                notes[i],
                                dates[i],
                                imageURL[i],
                                draws[i],
                                colors[i]
                            )
                        )
                    }


                    noteModels.sortWith(Comparator { obj1: Note_Model, obj2: Note_Model ->
                        obj2.id.compareTo(obj1.id)
                    })
                    var p = 0
                    for (i in noteModels) {
                        i.actualPosition = p;
                        p++
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        circle1.visibility = View.GONE
                        circle2.visibility = View.GONE
                        circle3.visibility = View.GONE
                        circle4.visibility = View.GONE
                        circle5.visibility = View.GONE

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("notes", noteModels)

                        startActivity(intent)
                    }, 1300)
                }
            }

                startButton.setOnTouchListener { v, event ->
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> startButton.startAnimation(focusedButton)//Do Something
                    }

                    v?.onTouchEvent(event) ?: true

            }
    }
}
