package com.example.bhanu.github.repos.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdater<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int REPOS = 0;
    public static final int USERS = 2;

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
            case USERS:
                UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user, parent, false));
                return userViewHolder;
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
            case USERS:
                ((UserViewHolder)holder).onBind((UserVO) mData.get(position));
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


        @BindView(R.id.repolayout)
        ConstraintLayout layout;

        @BindView(R.id.imageView2)
        ImageView avatar;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.full_name)
        TextView fullName;

        @BindView(R.id.watcher_count)
        TextView watcherCount;

        @BindView(R.id.commit_count)
        TextView commitCount;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public void onBind(Repo repo) {

            name.setText(repo.getName());
            fullName.setText(repo.getFull_name());
            watcherCount.setText("watchers: "+ repo.getWatchers_count());
            commitCount.setText("forks: " + repo.getForks_count());
            Glide.with(context)
                    .load(repo.getOwner().getAvatar_url())
                    .into(avatar);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdaterListner.onRepoSelected(repo.getId());
                }
            });
        }


    }


    class UserViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.repolayout)
        ConstraintLayout layout;

        @BindView(R.id.imageView2)
        ImageView avatar;

        @BindView(R.id.name)
        TextView name;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void onBind(UserVO user) {

            name.setText(user.getLogin());
            Glide.with(context)
                    .load(user.getAvatar_url())
                    .into(avatar);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdaterListner.onUserSelected(user.getLogin());
                }
            });
        }


    }


    public interface ItemAdaterListner{
        void onRepoSelected(int id);
        void onUserSelected(String username);
    }
}
