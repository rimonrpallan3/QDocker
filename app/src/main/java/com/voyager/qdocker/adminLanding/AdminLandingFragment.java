package com.voyager.qdocker.adminLanding;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.voyager.qdocker.R;
import com.voyager.qdocker.adminLanding.adapter.ListQRDocsAdapter;
import com.voyager.qdocker.adminLanding.model.QrResult;
import com.voyager.qdocker.adminLanding.presenter.AdminLandingFrgPresenter;
import com.voyager.qdocker.adminLanding.presenter.IAdminLandingFrgPresenter;
import com.voyager.qdocker.adminLanding.view.IAdminLandingFrgView;
import com.voyager.qdocker.common.Config;
import com.voyager.qdocker.common.NetworkDetector;
import com.voyager.qdocker.custom.qrmodule.activity.QrScannerActivity;
import com.voyager.qdocker.userViewDoc.model.UploadDocs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by User on 28-Sep-17.
 */

@SuppressLint("ValidFragment")
public class AdminLandingFragment extends Fragment implements IAdminLandingFrgView,EasyPermissions.PermissionCallbacks {

    public String urlOutPut = "";
    Activity activity;
    @BindView(R.id.scanQrCode)
    Button scanQrCode;
    @BindView(R.id.recycleQrList)
    RecyclerView recycleQrList;
    @BindView(R.id.qrLoader)
    FrameLayout qrLoader;
    private Unbinder unbinder;
    private DatabaseReference mDatabaseRef;
    ListQRDocsAdapter listQRDocsAdapter;
    ArrayList<UploadDocs> uploadDocsList;
    IAdminLandingFrgPresenter iAdminLandingFrgPresenter;
    String[] perms2 = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int RC_CAMERA_PERM = 122;

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
        iAdminLandingFrgPresenter = new AdminLandingFrgPresenter(this);
        uploadDocsList = new ArrayList<>();
        System.out.println("AdminLandingFragment");

        return rootView;
    }
    @AfterPermissionGranted(RC_CAMERA_PERM)
    @OnClick(R.id.scanQrCode)
    void onScanBtnClick() {
        System.out.println("AdminLandingFragment onScanBtnClick");
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            if(NetworkDetector.haveNetworkConnection(getActivity())){
                //Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.snack_error_network_available), Snackbar.LENGTH_SHORT).show();
                startActivityForResult(new Intent(getActivity(), QrScannerActivity.class), QrScannerActivity.QR_REQUEST_CODE);
            }else {
                Snackbar.make(activity.findViewById(android.R.id.content), getResources().getString(R.string.snack_error_network), Snackbar.LENGTH_LONG).show();
            }

        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, getString(R.string.error_message_permission_camera),
                    RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }


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
                    qrLoader.setVisibility(View.VISIBLE);
                    urlOutPut = data.getExtras().getString(QrScannerActivity.QR_RESULT_STR);
                    System.out.println("AdminLandingFragment QrScannerActivity urlOutPut: "+urlOutPut);
                    Gson gson = new Gson();
                    final QrResult qrResult = gson.fromJson(urlOutPut, QrResult.class);
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(qrResult.getRefDabaseRef()).child(qrResult.getQrUserId()).child(qrResult.getInnerChild());
                    System.out.println("AdminLandingFragment QrScannerActivity mDatabaseRef: "+mDatabaseRef.getRef());
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            uploadDocsList.clear();
                            qrLoader.setVisibility(View.GONE);
                            for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                                UploadDocs uploadDocs = objSnapshot.getValue(UploadDocs.class);
                                uploadDocsList.add(uploadDocs);
                            }
                            listQRDocsAdapter = new ListQRDocsAdapter(uploadDocsList,getActivity(),iAdminLandingFrgPresenter);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recycleQrList.setLayoutManager(mLayoutManager);
                            recycleQrList.setItemAnimator(new DefaultItemAnimator());
                            recycleQrList.setAdapter(listQRDocsAdapter);
                            recycleQrList.setItemAnimator(new DefaultItemAnimator());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            qrLoader.setVisibility(View.GONE);
                        }
                    });
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void getStoragePermission(String file_url, final String file_name, final ImageButton downloadButton) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (EasyPermissions.hasPermissions(activity, perms2)) {
                    listQRDocsAdapter.qrFileDownload(file_url,file_name,downloadButton);
                } else {
                    // Do not have permissions, request them now
                    EasyPermissions.requestPermissions(this, getString(R.string.storage_permission),
                            Config.STRORAGE_PERMISSION_REQUEST_CODE, perms2);
                }
            } else {
                listQRDocsAdapter.qrFileDownload(file_url,file_name,downloadButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
