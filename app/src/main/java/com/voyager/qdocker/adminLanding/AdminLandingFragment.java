package com.voyager.qdocker.adminLanding;

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
public class AdminLandingFragment extends Fragment {

    public String urlOutPut = "";
    Activity activity;
    @BindView(R.id.scanQrCode)
    Button scanQrCode;
    private Unbinder unbinder;

    public AdminLandingFragment(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.admin_landing_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        System.out.println("AdminLandingFragment");
        return rootView;
    }

    @OnClick(R.id.scanQrCode)
    void onScanBtnClick() {
        System.out.println("AdminLandingFragment onScanBtnClick");
        startActivityForResult(new Intent(getActivity(), QrScannerActivity.class), QrScannerActivity.QR_REQUEST_CODE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case QrScannerActivity.QR_REQUEST_CODE:
                System.out.println("AdminLandingFragment QrScannerActivity");
                if (data!= null) {
                    urlOutPut = data.getExtras().getString(QrScannerActivity.QR_RESULT_STR);
                    System.out.println("AdminLandingFragment QrScannerActivity urlOutPut: " + urlOutPut);
                } else {
                    System.out.println("AdminLandingFragment QrScannerActivity null---: " + urlOutPut);
                }
                break;

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
