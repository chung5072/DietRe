package com.ssafy.dietre.ui.pick;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ssafy.dietre.R;
import com.ssafy.dietre.databinding.FragmentLoadingBinding;

public class LoadingFragment extends DialogFragment {

    private FragmentLoadingBinding binding;

    public LoadingFragment() {
    }

    public static LoadingFragment newInstance(String location) {
        LoadingFragment loading = new LoadingFragment();
        Bundle args = new Bundle();
        args.putString("locate", location);
        loading.setArguments(args);
        return loading;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView loadingText = (TextView) binding.textLoading;

        if(getArguments() != null) {
            String locate = getArguments().getString("locate");
            if(locate == "pick") {
                loadingText.setText(getResources().getString(R.string.loading_menu));
            } else if(locate == "resolve") {
                loadingText.setText(getResources().getString(R.string.loading_resolve));
            }
        }

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        return root;
    }
}