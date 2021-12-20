package sch.id.snapan.smarteight.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sch.id.snapan.smarteight.data.entity.Announcement
import sch.id.snapan.smarteight.databinding.ListItemAnnouncementBinding
import sch.id.snapan.smarteight.ui.announcement.AnnouncementDetailActivity

class ListAnnouncementAdapter : RecyclerView.Adapter<ListAnnouncementAdapter.AnnouncementViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var announcements: List<Announcement>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAnnouncementAdapter.AnnouncementViewHolder {
        val listBinding = ListItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnnouncementViewHolder(listBinding)
    }

    override fun onBindViewHolder(
        holder: ListAnnouncementAdapter.AnnouncementViewHolder,
        position: Int
    ) {
        val list = announcements[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = announcements.size

    inner class AnnouncementViewHolder(private val binding: ListItemAnnouncementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(announcement: Announcement) {
            with(binding) {
                tvDateAnnouncement.text = announcement.date
                tvTitleAnnouncement.text = announcement.title
                tvMessageAnnouncement.text = announcement.message
                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, AnnouncementDetailActivity::class.java)
                    intentToDetail.putExtra(AnnouncementDetailActivity.EXTRA_ANNOUNCEMENT, announcement)
                    itemView.context.startActivity(intentToDetail)
                }
            }
        }
    }
}