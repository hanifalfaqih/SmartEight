package sch.id.snapan.smarteight.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sch.id.snapan.smarteight.ui.announcement.fragment.AnnouncementFragment
import sch.id.snapan.smarteight.ui.attendance.fragment.AttendanceFragment
import sch.id.snapan.smarteight.R

class SectionsPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    val tabsTitle = arrayOf(R.string.tab_presence, R.string.tab_announcement)
    val tabsIcon = arrayOf(R.drawable.ic_baseline_format_list_bulleted, R.drawable.ic_baseline_campaign)

    override fun getItemCount(): Int = tabsTitle.size

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> AttendanceFragment()
            1 -> AnnouncementFragment()
            else -> Fragment()
        }

}