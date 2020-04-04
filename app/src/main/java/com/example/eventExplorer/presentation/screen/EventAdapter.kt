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
        fun cardClick(eventAdapterItem: EventView)
        fun checkinClick(eventAdapterItem: EventView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(eventList[position])
        holder.itemView.apply {
            card.setOnClickListener {
                callback.cardClick(eventList[position])
            }
            checkinIcon.setOnClickListener {
                callback.checkinClick(eventList[position])
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
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(image)
            val checkin: Boolean? = true
            checkinIcon.setImageDrawable(
                when (checkin) {
                    true -> context.getDrawable(R.drawable.ic_launcher_background)
                    false -> context.getDrawable(R.drawable.ic_launcher_foreground)
                    else -> context.getDrawable(android.R.color.transparent)
                }
            )
        }
    }
}