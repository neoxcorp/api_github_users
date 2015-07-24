package com.api_github_users.list_ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.api_github_users.R;
import com.api_github_users.dialog.AvatarPreview;
import com.api_github_users.get_data.GetDataActivity;
import com.api_github_users.utils.AppConst;

import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter {

    private ArrayList<User> list;
    private Activity activity;
    private DisplayImageOptions options;

    public UsersAdapter(ArrayList<User> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 3) {
            if (GetDataActivity.status) {
                if (((GetDataActivity) activity).isOnline()) {
                    ((GetDataActivity) activity).startLoading(list.get(getCount() - 1).getUserId());
                    list.add(new User(AppConst.PB_ZERO, "", "", ""));
                    notifyDataSetChanged();
                }
            }
        }

        final View view;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            view = inflater.inflate(R.layout.row_user, parent, false);
        } else {
            view = convertView;
        }
        final Holder h = new Holder();
        User u = list.get(position);

        h.avatarImBtn  = (ImageButton) view.findViewById(R.id.avatarImBtn);
        h.userNameTv  = (TextView) view.findViewById(R.id.userNameTv);
        h.linkTv  = (TextView) view.findViewById(R.id.linkTv);
        h.loadingStack  = (ProgressBar) view.findViewById(R.id.loadingStack);
        h.userData  = (LinearLayout) view.findViewById(R.id.userData);

        if (u.getUserId() == AppConst.PB_ZERO) {
            loadingOn(h);
        } else {
            loadingOff(h, u);
        }

        return view;
    }

    private class Holder {
        private TextView userNameTv, linkTv;
        private ImageButton avatarImBtn;
        private ProgressBar loadingStack;
        private LinearLayout userData;
    }

    private void loadingOn(Holder holder) {
        holder.loadingStack.setVisibility(View.VISIBLE);
        holder.userData.setVisibility(View.GONE);
    }

    private void loadingOff(Holder holder, final User u) {
        holder.loadingStack.setVisibility(View.GONE);
        holder.userData.setVisibility(View.VISIBLE);

        holder.userNameTv.setText(u.getUserName());
        holder.linkTv.setText(u.getLink());

        holder.avatarImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvatarPreview ap = new AvatarPreview();
                Bundle arg = new Bundle();
                arg.putString(AppConst.AVATAR_SOURCE, u.getAvatarUrl());
                ap.setArguments(arg);
                ap.show(activity.getFragmentManager(), null);
            }
        });
        ImageLoader.getInstance().displayImage(u.getAvatarUrl(), holder.avatarImBtn);
    }
}