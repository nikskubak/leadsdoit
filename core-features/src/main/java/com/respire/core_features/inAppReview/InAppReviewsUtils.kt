package com.respire.core_features.inAppReview

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.review.ReviewManagerFactory
import com.respire.core_features.R
import com.respire.core_features.databinding.DialogRateBinding

object InAppReviewHelper {
    fun reviewApp(
        applicationId: String,
        activity: Activity,
        onCompleteListener: (isSuccess: Boolean) -> Unit
    ) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo).addOnFailureListener {
                    it.printStackTrace()
                    onCompleteListener(false)
                }.addOnCompleteListener {
                    onCompleteListener(true)
                }
            } else {
                task.exception?.printStackTrace()
                onCompleteListener(false)
                openAppInGooglePlay(applicationId, activity)
            }
        }
    }

    public fun showReviewDialog(
        appName: String,
        applicationId: String,
        context: Context,
        activity: Activity,
        onCompleteListener: (isSuccess: Boolean) -> Unit
    ) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.CustomDialog)
        DialogRateBinding.inflate(LayoutInflater.from(context))
        val binding = DialogRateBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.rateButton.setOnClickListener {
            openAppInGooglePlay(applicationId, activity)
            dialog.dismiss()
        }
        binding.problemButton.setOnClickListener {
            openMailClient(appName, activity)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun openAppInGooglePlay(applicationId: String, activity: Activity) {
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$applicationId")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$applicationId")
                )
            )
        }
    }

    private fun openMailClient(appName: String, activity: Activity) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf("respirecorp@gmail.com"))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "${activity.getString(R.string.app_trouble)} \"${appName}\""
                )
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}