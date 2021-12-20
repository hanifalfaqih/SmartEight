package sch.id.snapan.smarteight.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.R
import sch.id.snapan.smarteight.ui.home.HomeActivity

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()

        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}