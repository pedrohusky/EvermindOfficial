package com.example.Evermind
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.startup_activity.*


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

        val everDataBase = EverDataBase(this)

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
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
        //window.navigationBarColor = Color.TRANSPARENT //DONT KNOW IF IT WORKS

        /////ABOVE SET STATUS BAR TRANSPARENT

            val animationDrawable = background.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(20)
            animationDrawable.setExitFadeDuration(1000)
            animationDrawable.start()

            val startButton = findViewById<Button>(R.id.startButton)

        val intentToStart = Intent(applicationContext, MainActivity::class.java)

        startButton.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> findViewById<MotionLayout>(R.id.background).setTransitionListener(
                    object : MotionLayout.TransitionListener {
                        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
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
                                //this is for clean the database when theres notes with nothing in it
                                if (notes[i] == "┼" && draws[i] == "" && imageURL[i] == "") {
                                    everDataBase.deleteNote(ids[i])
                                } else if (notes[i] == "┼┼" && draws[i] == "" && imageURL[i] == "") {
                                    everDataBase.deleteNote(ids[i])
                                } else if (notes[i] == "" && draws[i] == "" && imageURL[i] == "") {
                                    everDataBase.deleteNote(ids[i])
                                }
                                //after clean, then add the notes
                                else {
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
                            }


                            //and sort them to make the last become the first
                            noteModels.sortWith { obj1: Note_Model, obj2: Note_Model ->
                                obj2.id.compareTo(obj1.id)
                            }
                            for ((p, i) in noteModels.withIndex()) {
                                i.actualPosition = p
                            }

                            intentToStart.putExtra("notes", noteModels)
                        }

                        override fun onTransitionChange(
                            p0: MotionLayout?,
                            p1: Int,
                            p2: Int,
                            p3: Float
                        ) {
                        }

                        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                            startActivity(intentToStart)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in)
                        }

                        override fun onTransitionTrigger(
                            p0: MotionLayout?,
                            p1: Int,
                            p2: Boolean,
                            p3: Float
                        ) {
                            TODO("Not yet implemented")
                        }
                    })//Do Something
            }

            v?.onTouchEvent(event) ?: true

        }
    }

    override fun onStop() {
        super.onStop()
        onTrimMemory(TRIM_MEMORY_UI_HIDDEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        onTrimMemory(TRIM_MEMORY_UI_HIDDEN)
    }
}
