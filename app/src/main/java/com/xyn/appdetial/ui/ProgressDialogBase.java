package com.xyn.appdetial.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xyn.appdetial.R;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class ProgressDialogBase extends DialogFragment {

    private ProgressDialog mProgress;

    public static ProgressDialogBase newInstance(String title, String msg) {
        ProgressDialogBase f = new ProgressDialogBase();
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("msg", msg);
        f.setArguments(data);

        return f;
    }

    public ProgressDialogBase() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, getTheme());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_with_progressbar, container, false);
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("msg", "");

        TextView titleView = (TextView) view.findViewById(R.id.tvProgressTitle);
        TextView msgView = (TextView) view.findViewById(R.id.tvProgressMsg);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            msgView.setText(message);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        Dialog d = getDialog();
        d.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.getWindow().setGravity(Gravity.CENTER);
    }
}
