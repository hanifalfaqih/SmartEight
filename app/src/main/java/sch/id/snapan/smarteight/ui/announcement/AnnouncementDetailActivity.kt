package sch.id.snapan.smarteight.ui.announcement

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.R
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.databinding.ActivityAnnouncementDetailBinding
import sch.id.snapan.smarteight.other.EventObserver
import sch.id.snapan.smarteight.ui.snackbar
import com.bumptech.glide.RequestManager
import javax.inject.Inject

@AndroidEntryPoint
class AnnouncementDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: ActivityAnnouncementDetailBinding? = null
    private val binding get() = _binding!!
    private var announcement: Announcement? = null
    private lateinit var announcementId: String
    private lateinit var announcementUid: String
    private val viewModel: AnnouncementViewModel by viewModels()

    companion object {
        const val EXTRA_ANNOUNCEMENT = "extra_announcement"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_announcement)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        announcement = intent.getParcelableExtra(EXTRA_ANNOUNCEMENT)
        announcementId = announcement?.id.toString()
        announcementUid = announcement?.uid.toString()
        viewModel.getDetailAnnouncement(announcementId)

        subscribeToObserveDetail()
        handleOnBackPressedCallback()

    }

    private fun handleOnBackPressedCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }
        this.onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun subscribeToObserveDetail() {
        viewModel.getDetailAnnouncementStatus.observe(this, EventObserver(
            onError = {
                binding.progressBarDetail.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarDetail.visibility = View.VISIBLE
            }
        ) { announcement ->
            binding.progressBarDetail.visibility = View.INVISIBLE
            if (announcement?.imageUrl != null) {
                glide.load(announcement.imageUrl).into(binding.ivPostAnnouncement)
            } else {
                binding.ivPostAnnouncement.visibility = View.GONE
            }
            binding.tvDateDetailAnnouncement.text = announcement?.date
            binding.tvTitleDetailAnnouncement.text = announcement?.title
            binding.tvMessageDetailAnnouncement.text= announcement?.message
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDetailAnnouncement(announcementId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}