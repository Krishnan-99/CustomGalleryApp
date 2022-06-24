package com.galleryapp.ui.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.galleryapp.Utilits.R
import com.galleryapp.Utilits.model.*
import com.galleryapp.ViewModels.GalleryViewModel
import com.galleryapp.ui.Fragment.FolderSelectFragment
import com.galleryapp.ui.Fragment.MediaSelectFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_gallery.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val REQUEST_SELECT_MEDIA = 88
private const val KEY_ALBUM_SETTING = "albumSetting"

class GalleryViewActivity : BaseActivity() {

    private val viewModel by viewModel<GalleryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        viewModel.setting = intent.getSerializableExtra(KEY_ALBUM_SETTING) as AlbumSetting
        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.mediaContainer,
                        MediaSelectFragment.newInstance()
                    )
                    .commit()
                setClickListener()
                subscribeChanges()
            }.apply { addDisposable(this) }


    }

    private fun setClickListener() {
        textAlbumName.setOnCheckedChangeListener { _, isChecked ->
            val transaction = supportFragmentManager.beginTransaction()

            if (isChecked) {
                transaction
                    .add(
                        R.id.mediaContainer,
                        FolderSelectFragment.newInstance()
                    )
            } else {
                supportFragmentManager.findFragmentById(R.id.mediaContainer)?.run {
                    transaction.remove(this)
                }
            }
            transaction.commit()
            supportFragmentManager.executePendingTransactions()
        }
        arrow.setOnClickListener { textAlbumName.toggle() }

    }

    private fun subscribeChanges() {
        viewModel.currentAlbumItem
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val albumName = if (it.name == ALL_MEDIA_ALBUM_NAME) textAlbumName.context.getString(
                    R.string.all_photos) else it.name
                textAlbumName.textOff = albumName
                textAlbumName.textOn = albumName
                textAlbumName.isChecked = false
            }.subscribe()
            .apply { addDisposable(this) }

        viewModel.singleSelectMedia
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                returnSelectMediaList(listOf(it))
            }
            .subscribe()
            .apply { addDisposable(this) }

        viewModel.multipleSelectMedia
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {  }
            .subscribe()
            .apply { addDisposable(this) }
    }

    private fun returnSelectMediaList(list: List<Media>) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putStringArrayListExtra(KEY_MEDIA_LIST, ArrayList<String>(list.map { it.path }))
        })
        finish()
    }

    class Builder {
        private val setting = AlbumSetting()

        fun choose(mimeType: MimeType = MimeType.ALL): Builder {
            setting.mimeType = mimeType
            return this
        }

        fun multiple(multipleSelect: Boolean = true): Builder {
            setting.multipleSelection = multipleSelect
            return this
        }

        fun maxSelect(max: Int): Builder {
            check(max > 0) { "The maximum selection should be greater than 0." }
            setting.maxSelection = max
            return this
        }

        fun imageMaxSize(max: Long): Builder {
            setting.imageMaxSize = max
            return this
        }

        fun videoMaxSecond(max: Int): Builder {
            setting.videoMaxSecond = max
            return this
        }

        fun videoMinSecond(min: Int): Builder {
            setting.videoMinSecond = min
            return this
        }

        fun forResult(activity: Activity, requestCode: Int = REQUEST_SELECT_MEDIA) {
            activity.startActivityForResult(getIntent(activity), requestCode)
        }

        fun forResult(fragment: Fragment, requestCode: Int = REQUEST_SELECT_MEDIA) {
            fragment.context?.run {
                fragment.startActivityForResult(getIntent(this), requestCode)
            }
        }

        private fun getIntent(context: Context) =
            Intent(context, GalleryViewActivity::class.java).apply {
                putExtra(KEY_ALBUM_SETTING, setting)
            }
    }

    companion object {

        fun getSelectMediaPaths(intent: Intent?): List<String> {
            return intent?.getStringArrayListExtra(KEY_MEDIA_LIST) ?: listOf()
        }
    }
    override fun onBackPressed() {
    }
}
