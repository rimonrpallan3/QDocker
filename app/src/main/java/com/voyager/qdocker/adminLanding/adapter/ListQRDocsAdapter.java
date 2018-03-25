package com.voyager.qdocker.adminLanding.adapter;

import android.app.Activity;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.qdocker.R;
import com.voyager.qdocker.adminLanding.presenter.IAdminLandingFrgPresenter;
import com.voyager.qdocker.adminLanding.presenter.IAdminLandingPresenter;
import com.voyager.qdocker.adminLanding.view.IAdminLandingFrgView;
import com.voyager.qdocker.common.NetworkDetector;
import com.voyager.qdocker.common.Pdf_Downloader;
import com.voyager.qdocker.userViewDoc.model.UploadDocs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rimon on 25-03-2018.
 */

public class ListQRDocsAdapter extends RecyclerView.Adapter<ListQRDocsAdapter.ViewHolder> {

    public ArrayList<UploadDocs> uploadDocsList;
    Activity activity;
    IAdminLandingFrgPresenter iAdminLandingFrgPresenter;

    public ListQRDocsAdapter(ArrayList<UploadDocs> uploadDocsList, Activity activity,IAdminLandingFrgPresenter iAdminLandingFrgPresenter) {
        this.activity = activity;
        this.uploadDocsList = uploadDocsList;
        this.iAdminLandingFrgPresenter = iAdminLandingFrgPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_file_download, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        UploadDocs uploadDocs = uploadDocsList.get(position);
        holder.contentFileNameDown.setText(uploadDocs.getFileNmae());
        File faith_directory = new File(Environment.getExternalStorageDirectory() + "/"+activity.getResources().getString(R.string.app_name)+"/");
        if (faith_directory.exists() && faith_directory.isDirectory()){
            File church_file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()+ "/"+activity.getResources().getString(R.string.app_name)+"/", uploadDocs.getFileNmae());
            if (church_file.exists()){
                holder.downloadImg.setImageResource(R.drawable.novena_downloaded);
            }
            else {
                holder.downloadImg.setImageResource(R.drawable.novena_download);
            }
        }
        else{
            holder.downloadImg.setImageResource(R.drawable.novena_download);
        }

        holder.contentFileDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkDetector.haveNetworkConnection(activity)){
                    iAdminLandingFrgPresenter.methodRequiresTwoPermission(uploadDocsList.get(position).getUrl(),uploadDocsList.get(position).getFileNmae(),holder.downloadImg);
                }
                else {
                    Snackbar.make(activity.findViewById(android.R.id.content), "No Network Connection !", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    public void qrFileDownload(String file_url, final String file_name, final ImageButton downloadButton){
        if (NetworkDetector.haveNetworkConnection(activity)){
            Pdf_Downloader pdf_downloader = new Pdf_Downloader(activity);
            pdf_downloader.download_Pdf(file_url,file_name,downloadButton);
        }
        else {
            Snackbar.make(activity.findViewById(android.R.id.content), "No Network Connection !", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return uploadDocsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView contentFileNameDown;
        public LinearLayout contentFileDownBtn;
        public ImageButton downloadImg;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            contentFileNameDown = (TextView) mView.findViewById(R.id.contentFileNameDown);
            contentFileDownBtn = (LinearLayout) mView.findViewById(R.id.contentFileDownBtn);
            downloadImg = (ImageButton) mView.findViewById(R.id.downloadImg);
        }

    }

}
