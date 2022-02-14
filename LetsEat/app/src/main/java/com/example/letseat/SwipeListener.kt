package com.example.letseat

import android.content.Intent
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import androidx.core.content.ContextCompat.startActivity

import java.lang.Exception
import kotlin.coroutines.coroutineContext

class SwipeListener() : View.OnTouchListener  {

     lateinit var gestureDetector: GestureDetector


     fun SwipeListener(view: View ,leftSwipeIntent : Intent, rightSwipeIntent : Intent, context: Context)
     {

         val threshold = 100
         val velocity_threshold = 100
         val listener = object: GestureDetector.SimpleOnGestureListener(){
             override fun onDown(e: MotionEvent?): Boolean {
                 return true
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
                                 startActivity(context,rightSwipeIntent,null)
                                 //rightSwipe(rightSwipeIntent)
                             }
                             else
                             {
                                 startActivity(context,leftSwipeIntent,null)
                                 //leftSwipe(leftSwipeIntent)
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
     fun SwipeListener(view: View ,swipeIntent : Intent, isRight : Boolean, context: Context)
     {

         val threshold = 100
         val velocity_threshold = 100
         val listener = object: GestureDetector.SimpleOnGestureListener(){
             override fun onDown(e: MotionEvent?): Boolean {
                 return true
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
                             if((xDiff > 0))
                             {
                                 if(isRight) {
                                     startActivity(context, swipeIntent, null)
                                     rightSwipe(swipeIntent)
                                 }
                             }
                             else
                             {
                                 if(!isRight) {
                                     startActivity(context, swipeIntent, null)
                                     leftSwipe(swipeIntent)
                                 }
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

     private fun leftSwipe(leftSwipeIntent: Intent): Intent {
         return leftSwipeIntent

     }
     private fun rightSwipe(rightSwipeIntent: Intent): Intent {
        return rightSwipeIntent
     }

    override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }
}