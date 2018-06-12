package io.github.anvell.stackoverview.adapter

import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.extension.fromHtml
import io.github.anvell.stackoverview.model.Answer
import kotlinx.android.synthetic.main.fragment_details_answer_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class AnswersAdapter(private var values: MutableList<Answer>,
                     private val interactionListener: OnInteractionListener<Answer>? = null)
    : BaseAdapter<Answer>(values, interactionListener) {

    companion object {
        private const val DATE_FORMAT = "yyyy.MM.dd HH:mm"
    }

    override fun getResourceId(): Int = R.layout.fragment_details_answer_item

    override fun bind(holder: ViewHolder, item: Answer) {
        holder.view.answerBody.text = item.body.fromHtml()
        holder.view.postOwner.text = item.owner.displayName

        val date = Date(item.creationDate.toLong() * 1000)
        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        holder.view.postedDate.text = format.format(date)

        holder.view.iconAccepted.visibility = if(item.isAccepted) View.VISIBLE else View.GONE

        if(Patterns.WEB_URL.matcher(item.owner.profileImage).matches()) {
            val uri = Uri.parse(item.owner.profileImage)
            Glide.with(holder.view.context).asBitmap()
                    .load(uri)
                    .into(holder.view.iconAvatar)
        }
    }
}