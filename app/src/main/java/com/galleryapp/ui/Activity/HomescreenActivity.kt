package com.galleryapp.ui.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.galleryapp.Utilits.R
import com.tbruyelle.rxpermissions2.RxPermissions

class HomescreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
//                GalleryEngine.Builder()
//                    .choose(MimeType.IMAGE)
//                    .forResult(this)
                GalleryViewActivity.Builder()
                        .multiple(true)
                        .maxSelect(10)
                        .forResult(this)

//                buttonMultipleSelection.setOnClickListener {
//                    GalleryEngine.Builder()
//                        .multiple(true)
//                        .maxSelect(5)
//                        .forResult(this)
//                }
            }.apply { addDisposable(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_MEDIA && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, GalleryViewActivity.getSelectMediaPaths(data).toString(), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
    }
}
