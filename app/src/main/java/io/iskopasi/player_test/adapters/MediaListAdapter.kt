package io.iskopasi.player_test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.iskopasi.player_test.MediaFile
import io.iskopasi.player_test.R
import io.iskopasi.player_test.databinding.ListItemBinding

class MediaListAdapter(val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<MediaListAdapter.ViewHolder>() {
    var data: List<MediaFile> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var active: Int = -1
        set(value) {
            val oldValue = active
            field = value

            notifyItemChanged(value)
            notifyItemChanged(oldValue)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val context = holder.itemView.context

        with(holder.binding) {
            val bgColor = if (position == active) R.color.white else R.color.trans
            val textColor = if (position == active) R.color.black else R.color.white

            tv.text = item.name
            tv.setTextColor(ContextCompat.getColor(context, textColor))
            root.setBackgroundColor(ContextCompat.getColor(context, bgColor))


            root.setOnClickListener {
                onClick(item.id)
            }
        }
    }

    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}