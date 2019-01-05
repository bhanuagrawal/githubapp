package com.example.bhanu.github.repos.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.datamodel.EventVO;
import com.example.bhanu.github.repos.datamodel.Repo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdater<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NOTIFICATION = 1;
    public static final int REPOS = 0;

    private final int itemVIewType;
    private ArrayList<T> mData;
    private ItemAdaterListner itemAdaterListner;
    private Context context;

    public ItemAdater(Context context, ItemAdaterListner itemAdaterListner, int viewType) {
        this.mData = new ArrayList<>();
        this.itemAdaterListner = itemAdaterListner;
        this.context = context;
        this.itemVIewType = viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (itemVIewType){
            case REPOS:
                RepoViewHolder repoViewHolder = new RepoViewHolder(LayoutInflater.from(context).inflate(R.layout.repo, parent, false));
                return repoViewHolder;
            case NOTIFICATION:
                NotificationViewHolder notificationViewHolder = new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notification, parent, false));
                return notificationViewHolder;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case REPOS:
                ((RepoViewHolder)holder).onBind((Repo) mData.get(position));
                break;
            case NOTIFICATION:
                ((NotificationViewHolder)holder).onBind((EventVO) mData.get(position));
                break;
            default:
                ((RepoViewHolder)holder).onBind((Repo) mData.get(position));
        }



    }


    @Override
    public int getItemViewType(int position) {
        return itemVIewType;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void onDataChange(ArrayList<T> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }


    class RepoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.full_name)
        TextView fullName;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void onBind(Repo repo) {
            name.setText(repo.getName());
            fullName.setText(repo.getFull_name());
        }


    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.full_name)
        TextView fullName;


        @BindView(R.id.time)
        TextView time;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void onBind(EventVO event) {
            name.setText(event.getType());
            fullName.setText(event.getRepo().getName());
            time.setText(event.getCreated_at());
        }


    }


    public interface ItemAdaterListner{
    }
}