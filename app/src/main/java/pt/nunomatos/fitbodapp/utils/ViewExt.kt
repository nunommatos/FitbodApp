package pt.nunomatos.fitbodapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.annotation.LayoutRes

fun ViewFlipper?.showChildAtIndex(index: Int) {
    this ?: return
    if (displayedChild != index) {
        displayedChild = index
    }
}

fun ViewGroup.inflateLayoutOnParent(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}