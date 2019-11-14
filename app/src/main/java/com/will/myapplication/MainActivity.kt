package com.will.myapplication

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_top.setOnClickListener {
            scroll_text.showPre()
            scroll_text_content.showPre()
        }

        btn_bottom.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
//                scroll_text.showPre()
                scroll_text_content.showNext()
                val scaleAnim =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.store_slide_close)
                scaleAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        scroll_text.showNext()
                    }

                })

                val objectAnimationX = ObjectAnimator.ofFloat(shopping_car, "scaleX", 1F, 1.4F)
                    .setDuration(300)
                val objectAnimationY = ObjectAnimator.ofFloat(shopping_car, "scaleY", 1F, 1.4F)
                    .setDuration(300)
                val animatorSet = AnimatorSet()
                animatorSet.play(objectAnimationX).with(objectAnimationY)
                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        scroll_text.showNext()
                        val objectAnimationX1 =
                            ObjectAnimator.ofFloat(shopping_car, "scaleX", 1.4F, 1F)
                                .setDuration(300)
                        val objectAnimationY1 =
                            ObjectAnimator.ofFloat(shopping_car, "scaleY", 1.4F, 1F)
                                .setDuration(300)
                        val animatorSet2 = AnimatorSet()
                        animatorSet2.play(objectAnimationX1).with(objectAnimationY1)
                        animatorSet2.start()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }

                })
                animatorSet.start()


            }

        })
    }
}
