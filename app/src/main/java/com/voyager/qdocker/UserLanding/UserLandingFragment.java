package com.voyager.qdocker.UserLanding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.voyager.qdocker.R;
import com.voyager.qdocker.custom.qrmodule.activity.QrScannerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by User on 28-Sep-17.
 */

@SuppressLint("ValidFragment")
public class UserLandingFragment extends Fragment {

    Activity activity;
    private Unbinder unbinder;
    @BindView(R.id.viewAppStoredDoc)
    Button viewAppStoredDoc;
    @BindView(R.id.viewFireBaseStoredDoc)
    Button viewFireBaseStoredDoc;

    public UserLandingFragment(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.user_landing_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        System.out.println("UserLandingFragment");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.viewAppStoredDoc)
    void viewAppStoredDoc() {
        System.out.println("UserLandingFragment viewAppStoredDoc");

    }

    @OnClick(R.id.viewFireBaseStoredDoc)
    void viewFireBaseStoredDoc() {
        System.out.println("UserLandingFragment viewFireBaseStoredDoc");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*case QrScannerActivity.QR_REQUEST_CODE:
                break;*/

            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
