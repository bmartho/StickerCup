package br.cup.stickercontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
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