package com.example.smartzonetest.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartzonetest.R
import com.google.android.material.snackbar.Snackbar
import com.wang.avi.AVLoadingIndicatorView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun AVLoadingIndicatorView.show() {
    visibility = View.VISIBLE
}

fun AVLoadingIndicatorView.hide() {
    visibility = View.GONE
}


fun View?.removeWithAnimation(duration: Long = 300) {
    this?.animate()?.translationY(this@removeWithAnimation.height.toFloat())?.alpha(0.0f)
        ?.setDuration(duration)?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                this@removeWithAnimation.visibility = View.GONE
            }
        })
}

fun View?.addWithAnimation(duration: Long = 300) {
    if (this != null) {
        this.animate()
            .translationY(0F)
            .alpha(1f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    this@addWithAnimation.visibility = View.VISIBLE
                }
            })
    }
}

fun Context?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(this, "$message", timeout).show()
    } catch (e: Exception) {
    }
}

fun Fragment?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(context, "$message", timeout).show()
    } catch (e: Exception) {
    }
}

fun Activity?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(this, "$message", timeout).show()
    } catch (e: Exception) {
    }
}

fun ImageView?.loadFromUrl(imageUrl: String?, placeHolder: Int = R.drawable.placeholder_image) {
    try {
        if (this != null && imageUrl != null)
            Glide.with(this.context)
                .load(imageUrl)
//            .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
    } catch (e: Exception) {
    }
}


fun ImageView?.load(drawable: Int, placeHolder: Int = R.drawable.placeholder_image) {
    try {
        if (this != null)
            Glide.with(this.context)
                .load(drawable)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
    } catch (e: Exception) {
    }
}


fun Any?.debug(message: String?) {
    Log.d("SmartZONELogOkTest", "$message")
}

fun String?.fileExt(): String? {
    if (this == null)
        return null
    else {
        var url = this
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"))
        }
        return if (url.lastIndexOf(".") == -1) {
            null
        } else {
            var ext = url.substring(url.lastIndexOf(".") + 1)
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"))
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"))
            }
            ext.toLowerCase(Locale.ROOT)
        }
    }

}

fun Context.isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this.trim()) && Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}

fun Activity.addWindowFlags() {
    val window = window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = 0x00000000  // transparent
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        window.addFlags(flags)
    }
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}


//Is Second Big or not
fun String.toDate(addDays: Int? = null): Date? {
    return try {
        SimpleDateFormat("dd/MM/yyyy").parse(
            this
        )

    } catch (e: Exception) {
        null
    }

}


fun View?.bindSpinner(spinner: Spinner?, tv: TextView) {
    spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
//            spinner.remove()
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            debug("Pos:$position" + ":" + spinner?.selectedItem.toString())
            tv.text = spinner?.selectedItem.toString()
//            spinner.remove()
        }

    }
    this?.setOnClickListener {
//        spinner.add()
        spinner?.performClick()
    }
}


fun ImageView.show() {
    visibility = View.VISIBLE


}

fun ImageView.hide() {
    visibility = View.GONE


}

fun Button.show() {
    visibility = View.VISIBLE


}

fun Button.hide() {
    visibility = View.GONE


}

fun TextView.show() {
    visibility = View.VISIBLE


}

fun TextView.hide() {
    visibility = View.GONE


}

fun RecyclerView.hide() {
    visibility = View.GONE


}

fun RecyclerView.show() {
    visibility = View.VISIBLE


}


fun View?.add() {
    this?.visibility = View.VISIBLE
}


fun View?.remove() {
    this?.visibility = View.GONE
}

fun String?.toRequestBody() =
    if (this == null) null else RequestBody.create(
        MediaType.parse("text/plain"), this
    ) as RequestBody

fun File?.toMultipart(keyName: String = "image_url", type: String = "multipart/form-data") =
    if (this == null) null else MultipartBody.Part.createFormData(
        keyName, this.name,
        (RequestBody.create(MediaType.parse(type), this))
    )


fun Activity?.addTouchFlag() {
    this?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )

}

fun TextView?.setHtml(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this?.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
    } else {
        this?.setText(Html.fromHtml(text));
    }
}

fun Activity?.clearTouchFlag() {
    this?.window?.clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Fragment?.addTouchFlag() {
    this?.activity?.addWindowFlags()
}

fun Fragment.clearTouchFlag() {
    this?.activity?.clearTouchFlag()
}

inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    bottomView: View
) {
    val snack = Snackbar.make(this, resources.getString(messageRes), length).apply {
        view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
            setMargins(
                leftMargin,
                topMargin,
                rightMargin,
                bottomView.height
            )
        }
    }.show()

}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, bottomView: View) {
    val snack = Snackbar.make(this, message, length).apply {
        view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
            setMargins(
                leftMargin,
                topMargin,
                rightMargin,
                bottomView.height
            )
        }
    }.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}


fun Activity?.displayMessage(
    message: String,
    time: Int = Snackbar.LENGTH_SHORT
) {
    if (this != null) {
        try {
            Snackbar.make(this.currentFocus!!, message, time)
                .setAction("Action", null).show()
        } catch (e: Exception) {
            try {
                toast(message, Toast.LENGTH_SHORT)
            } catch (ee: Exception) {
                ee.printStackTrace()
            }
        }
    }

}


fun ScrollView.focusOnView(view: View) {
    this.post {

        this.scrollTo(0, view.y.toInt())
    }
}

fun NestedScrollView.focusOnView(view: View) {
    this.post {
        this.scrollTo(0, view.y.toInt())
    }
}





