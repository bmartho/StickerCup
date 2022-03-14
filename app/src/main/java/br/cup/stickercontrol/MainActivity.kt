package br.cup.stickercontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.cup.stickercontrol.album.ui.AlbumFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.mainFragment,
                AlbumFragment()
            )
        }.commit()
    }
}