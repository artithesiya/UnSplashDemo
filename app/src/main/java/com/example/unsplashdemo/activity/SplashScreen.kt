package com.example.unsplashdemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
            },1000)
        }
    }

    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (locationAccepted && cameraAccepted) {
//                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                } else {

                    //                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR)) {
                        showMessageOKCancel("You need to allow access to both the permissions",
                            { dialog, which -> /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                requestPermissions(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                                                                        PERMISSION_REQUEST_CODE);
                                                            }*/
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
                        //                        }
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener,
        cancelListner: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this@SplashScreen)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", cancelListner)
            .create()
            .show()
    }
}