package com.example.photoai


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.net.Uri


class MainActivity : AppCompatActivity() {

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private val LOADING_TIME = 2000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkAndRequestPermissions()) {
            Handler().postDelayed({
                val i = Intent(this@MainActivity, FragmentActivity::class.java)
                startActivity(i)
                finish()
            }, LOADING_TIME.toLong())
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS && grantResults.size > 0) {

            val perms = HashMap<String, Int>()
            perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

            for (i in permissions.indices) {
                perms[permissions[i]] = grantResults[i]
            }

            if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                val i = Intent(this@MainActivity, FragmentActivity::class.java)
                startActivity(i)
                finish()
            }
            else {
                askPermitionsSecondTime()
            }
        }
    }

    private fun askPermitionsSecondTime(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CAMERA)
            || ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            warnAboutPermissions(getString(R.string.permission_to_work_properly),
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                        DialogInterface.BUTTON_NEGATIVE -> finish()
                    }
                })
        } else {
            switchToSettings(getString(R.string.permission_to_continue))
        }
    }

    private fun warnAboutPermissions(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok), okListener)
            .setNegativeButton(getString(R.string.cancel), okListener)
            .create()
            .show()
    }

    private fun switchToSettings(msg: String) {
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton(R.string.ok) {
                    paramDialogInterface, paramInt -> startActivity(
                    Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)))
            }
            .setNegativeButton(R.string.cancel) { paramDialogInterface, paramInt -> finish() }
            .show()
    }
}
