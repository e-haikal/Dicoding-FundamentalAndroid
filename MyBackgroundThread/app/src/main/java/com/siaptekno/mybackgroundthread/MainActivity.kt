package com.siaptekno.mybackgroundthread

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnStart = findViewById<Button>(R.id.btn_start)
        val tvStatus = findViewById<TextView>(R.id.tv_status)


        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())


        btnStart.setOnClickListener {
            executor.execute {
                try {
                    //simulate process compressing
                    for (i in 0..10) {
                        Thread.sleep(500)
                        val percentage = i * 10
                        handler.post {
                            //update ui in main thread
                            if (percentage == 100) {
                                tvStatus.setText(R.string.task_completed)
                            } else {
                                tvStatus.text = String.format(getString(R.string.compressing), percentage)
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }
}