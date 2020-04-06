package com.example.eventExplorer.presentation.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventExplorer.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_card.view.*

class EventAdapter(
    private var eventList: ArrayList<EventView>,
    private val callback: ClickCallback
) : RecyclerView.Adapter<ItemViewHolder>() {

    interface ClickCallback {
        fun cardClick(position: Int)
        fun checkinClick(position: Int)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(eventList[position])
        holder.itemView.apply {
            card.setOnClickListener {
                callback.cardClick(position)
            }
            checkinIcon.setOnClickListener {
                callback.checkinClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount() = eventList.size

    fun insert(eventItem: EventView) {
        eventList.add(eventItem)
        notifyDataSetChanged()
    }

    fun setList(eventList: List<EventView>) {
        this.eventList = ArrayList(eventList)
        notifyDataSetChanged()
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindView(item: EventView) {
        with(itemView) {
            titleLabel.text = item.title
            dayLabel.text = item.day
            monthLabel.text = item.month
            addressLabel.text = item.local
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.placeholder_img)
                .error(R.drawable.error_img)
                .into(image)
        }
    }
}