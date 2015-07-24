package com.api_github_users.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.api_github_users.R;
import com.api_github_users.utils.AppConst;

public class AvatarPreview extends DialogFragment implements View.OnClickListener{

    private String avatarSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        avatarSource = getArguments().getString(AppConst.AVATAR_SOURCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.avatar_preview_dialog_fragment, container, false);

        ImageButton bigImBtn = (ImageButton) v.findViewById(R.id.bigImBtn);

        bigImBtn.setOnClickListener(this);

        if(avatarSource != null) {
            ImageLoader.getInstance().displayImage(avatarSource, bigImBtn);
        } else {
            Toast.makeText(v.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}