package com.rao.smsreader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_item.view.*

class SmsAdapter(var list: MutableList<SMSData>, var context: Context) : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day_title.text = list.get(position).number
        holder.day_temp.text = list.get(position).body
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var day_title = view.heading
        var day_temp = view.desc
    }
}