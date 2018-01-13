package com.silwek.cleverchat.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * @author Silwèk on 13/01/2018
 */
abstract class SimpleRecyclerViewAdapter<T, U : RecyclerView.ViewHolder?>(private val onItemClick: (T) -> Unit) :
        RecyclerView.Adapter<U>() {

    var values: List<T>? = null
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as T
            onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): U {
        return createView(parent)
    }

    override fun onBindViewHolder(holder: U, position: Int) {
        values?.let {
            val item = values!![position]
            bind(holder, item, position)

            holder?.let {
                with(holder.itemView) {
                    tag = item
                    setOnClickListener(mOnClickListener)
                }
            }
        }
    }

    abstract fun createView(parent: ViewGroup): U
    abstract fun bind(holder: U, item: T, position: Int);

    override fun getItemCount(): Int {
        return when (values) {
            null -> 0
            else -> values!!.size
        }
    }
}