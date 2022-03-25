package br.cup.stickercontrol

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import br.cup.stickercontrol.album.ui.AlbumFragment
import br.cup.stickercontrol.album.ui.AlbumViewModel
import br.cup.stickercontrol.album.ui.AlbumViewModelFactory
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var shareImage: ImageView
    private lateinit var clearAllImage: ImageView
    private lateinit var tabLayout: TabLayout
    private lateinit var mainFragment: FragmentContainerView
    private lateinit var loading: ConstraintLayout

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar)

        tabLayout = findViewById(R.id.tabLayout)
        mainFragment = findViewById(R.id.mainFragment)
        loading = findViewById(R.id.loading_container)
        clearAllImage = findViewById(R.id.clear_all_image)
        shareImage = findViewById(R.id.share_image)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModelFactory((this.application as StickerControlApplication).repository)
        ).get(AlbumViewModel::class.java)

        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.mainFragment,
                AlbumFragment()
            )
        }.commit()

        tabLayout.addOnTabSelectedListener(onTabSelectedListener())

        val actionBarClearAllButton = findViewById<LinearLayout>(R.id.clear_all_button)
        val actionBarShareButton = findViewById<LinearLayout>(R.id.share_button)
        actionBarClearAllButton.setOnTouchListener(clearAllButtonClickEvent)
        actionBarShareButton.setOnTouchListener(shareButtonClickEvent)

        setObservers()
    }

    private fun setObservers() {
        albumViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                tabLayout.visibility = GONE
                mainFragment.visibility = GONE
                loading.visibility = VISIBLE
            } else {
                tabLayout.visibility = VISIBLE
                mainFragment.visibility = VISIBLE
                loading.visibility = GONE
            }
        }
    }

    private fun onTabSelectedListener() = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            if (tab.position == 0) {
                albumViewModel.setRepeatedTab(false)
            } else {
                albumViewModel.setRepeatedTab(true)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    }

    private val shareButtonClickEvent: (View, MotionEvent) -> Boolean = { _, arg1 ->
        when (arg1.action) {
            MotionEvent.ACTION_DOWN -> {
                shareImage.setImageResource(R.drawable.share_pressed)
            }
            MotionEvent.ACTION_UP -> {
                shareImage.setImageResource(R.drawable.share)
            }
        }
        true
    }

    private val clearAllButtonClickEvent: (View, MotionEvent) -> Boolean = { _, arg1 ->
        when (arg1.action) {
            MotionEvent.ACTION_DOWN -> {
                clearAllImage.setImageResource(R.drawable.trash_pressed)
            }
            MotionEvent.ACTION_UP -> {
                clearAllImage.setImageResource(R.drawable.trash)
            }
        }
        true
    }
}