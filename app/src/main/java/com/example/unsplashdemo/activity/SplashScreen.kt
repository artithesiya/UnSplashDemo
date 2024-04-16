package com.example.unsplashdemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.unsplashdemo.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE_ABOVE_34 = 99
    private val PERMISSION_REQUEST_CODE_ABOVE_33 = 98
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!checkPermission()) {
            requestPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }, 1000)
        }
    }

    private fun checkPermission(): Boolean {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (
                    ContextCompat.checkSelfPermission(
                        this@SplashScreen,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PERMISSION_GRANTED
                    )
        ) {
            // Full access on Android 13 (API level 33) or higher
            return true
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                this@SplashScreen,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED
        ) {
            return true
            // Partial access on Android 14 (API level 34) or higher
        } else if (ContextCompat.checkSelfPermission(
                this@SplashScreen,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {
            // Full access up to Android 12 (API level 32)
            return true
        } else {
            // Access denied
            return false
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ), PERMISSION_REQUEST_CODE_ABOVE_34
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.READ_MEDIA_IMAGES,
                ), PERMISSION_REQUEST_CODE_ABOVE_33
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_CODE
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val readStorageAccepted = grantResults[0] == PERMISSION_GRANTED
                val writeStorageAccepted = grantResults[1] == PERMISSION_GRANTED
                if (readStorageAccepted && writeStorageAccepted) {
                    //Permission Granted
                } else {
                    showMessageOKCancel("You need to allow access to both the permissions",
                        { dialog, which ->
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts(
                                "package",
                                packageName, null
                            )
                            intent.setData(uri)
                            startActivity(intent)
                        }) { dialog, which ->
                        val intent = Intent()
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            packageName, null
                        )
                        intent.setData(uri)
                        startActivity(intent)
                    }
                    return
                }
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_ABOVE_33 || requestCode == PERMISSION_REQUEST_CODE_ABOVE_34) {
            if (grantResults.isNotEmpty()) {
                val accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (accepted) {
//                    Permission Granted
                } else {

                    showMessageOKCancel("You need to allow access to both the permissions",
                        { dialog, which ->
                            val intent = Intent()
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts(
                                "package",
                                packageName, null
                            )
                            intent.setData(uri)
                            startActivity(intent)
                        }) { dialog, which ->
                        val intent = Intent()
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            packageName, null
                        )
                        intent.setData(uri)
                        startActivity(intent)
                    }
                    return
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener,
        cancelListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this@SplashScreen)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", cancelListener)
            .create()
            .show()
    }
}