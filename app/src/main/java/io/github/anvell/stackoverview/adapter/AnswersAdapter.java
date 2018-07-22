package io.github.anvell.stackoverview.adapter;

import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.extension.Utils;
import io.github.anvell.stackoverview.model.Answer;
import io.github.anvell.stackoverview.model.Question;;
import java.text.SimpleDateFormat;
import java.util.*;

public class AnswersAdapter extends BaseAdapter<Answer> {

    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm";

    public AnswersAdapter(ArrayList<Answer> values,
                                OnInteractionListener<Answer> interactionListener) {
        super(values, interactionListener);
    }

    public AnswersAdapter(ArrayList<Answer> values) {
        super(values, null);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_details_answer_item;
    }

    @Override
    public void bind(ViewHolder holder, Answer item) {

        TextView answerBody = holder.itemView.findViewById(R.id.answerBody);
        TextView postOwner = holder.itemView.findViewById(R.id.postOwner);
        TextView postedDate = holder.itemView.findViewById(R.id.postedDate);

        ImageView iconAccepted = holder.itemView.findViewById(R.id.iconAccepted);
        ImageView iconAvatar = holder.itemView.findViewById(R.id.iconAvatar);

        answerBody.setText(Utils.fromHtml(item.body));
        postOwner.setText(item.owner.displayName);

        Date date = new Date(item.creationDate * 1000);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        postedDate.setText(format.format(date));

        iconAccepted.setVisibility(item.isAccepted? View.VISIBLE : View.GONE);

        if(Patterns.WEB_URL.matcher(item.owner.profileImage).matches()) {
            Uri uri = Uri.parse(item.owner.profileImage);
            Glide.with(holder.itemView.getContext()).asBitmap()
                    .load(uri)
                    .into(iconAvatar);
        }
    }
}