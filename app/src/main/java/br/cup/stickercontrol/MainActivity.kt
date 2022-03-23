package br.cup.stickercontrol

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val mainFragment = findViewById<FragmentContainerView>(R.id.mainFragment)
        val loading = findViewById<ConstraintLayout>(R.id.loading_container)

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


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
        })
    }
}