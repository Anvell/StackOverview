package io.github.anvell.stackoverview.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseAdapter<T>(
        private var values: MutableList<T>,
        private val interactionListener: OnInteractionListener<T>?)
    : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {

    interface OnInteractionListener<T> {
        fun onListInteraction(item: T)
    }

    private val listener: View.OnClickListener

    init {
        listener = View.OnClickListener {
            @Suppress("UNCHECKED_CAST")
            val item = it.tag as? T
            item?.let {
                interactionListener?.onListInteraction(item)
            }
        }
    }

    abstract fun bind(holder: ViewHolder, item: T)
    abstract fun getResourceId(): Int

    fun replaceItems(newValues: MutableList<T>) {
        values = newValues
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getResourceId(), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        bind(holder, item)

        with(holder.itemView) {
            tag = item
            setOnClickListener(listener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}