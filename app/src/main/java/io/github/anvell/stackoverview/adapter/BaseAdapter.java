package io.github.anvell.stackoverview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter<T>.ViewHolder> {

    public interface OnInteractionListener<T> {
        void onListInteraction(T item);
    }

    private ArrayList<T> values;
    private OnInteractionListener<T> interactionListener;
    private View.OnClickListener listener;

    public BaseAdapter(ArrayList<T> values, OnInteractionListener<T> interactionListener) {
        this.values = values;
        this.interactionListener = interactionListener;

        listener = v -> {
            if (v.getTag() != null) {
                interactionListener.onListInteraction((T) v.getTag());
            }
        };
    }

    abstract void bind(ViewHolder holder, T item);

    abstract int getResourceId();

    public void replaceItems(ArrayList<T> newValues) {
        values = newValues;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getResourceId(), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = values.get(position);
        bind(holder, item);

        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}