package com.example.letseat

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.lang.Exception

private class SwipeListener : View.OnTouchListener {


    fun SwipeListener(view: View)
    {
        val gestureDetector: GestureDetector

        val threshold = 100
        val velocity_threshold = 100
        val listener = object: GestureDetector.SimpleOnGestureListener(){
            override fun onDown(e: MotionEvent?): Boolean {
                return super.onDown(e)
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                //Get x and y difference
                val xDiff = (e2!!.x - e1!!.x)
                val yDiff = (e2.y - e1.y)
                try {
                    //When x is greater then y
                    if(Math.abs(xDiff) > Math.abs(yDiff))
                    {
                        //Checking conditions
                        if(Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold )
                        {
                            //When x difference is greater then threshold
                            //When x velocity is greater then threshold
                            if(xDiff > 0)
                            {
                                //Right swipe
                            }
                            else
                            {
                                //Left swipe
                            }
                            return true
                        }
                    }
                    else{
                            //When y is greater than x
                        if(Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold)
                        {
                            //When y difference is greater than threshold
                            //When y velocity is greater than velocity threshold
                            if(yDiff > 0)
                            {
                                //When swiped down

                            }
                            else
                            {
                                //When swiped up
                            }
                            return true
                        }
                    }

                }catch ( e: Exception){
                    e.stackTrace
                }
                return false
            }
        }
         gestureDetector = GestureDetector(listener)

        view.setOnTouchListener(this)

    }
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetect
    }
}