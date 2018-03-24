package com.voyager.qdocker.userViewDoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.voyager.qdocker.R;
import com.voyager.qdocker.SignInPage.model.UserDetails;
import com.voyager.qdocker.userViewDoc.adapter.ListFileAdapter;
import com.voyager.qdocker.userViewDoc.model.DocList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by User on 22-Mar-18.
 */

public class UserViewDoc extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    UserDetails userDetails;
    Toolbar userViewDocToolbar;
    String fireBaseToken;

    public static final int RC_FILE_PICKER_PERM = 321;
    private int MAX_ATTACHMENT_COUNT = 20;
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<DocList> docLists;
    @BindView(R.id.recycleViewUserUploadDoc)
    RecyclerView recycleViewUserUploadDoc;
    ListFileAdapter listFileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view_doc);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        userDetails = (UserDetails) intent.getParcelableExtra("UserDetails");
        if (userDetails != null) {
            System.out.println("UserViewDoc -- UserDetails- name : " + userDetails.getUserName());
        }

        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        userViewDocToolbar = (Toolbar) findViewById(R.id.userViewDocToolbar);
        setSupportActionBar(userViewDocToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        userViewDocToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docLists = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    ArrayList<File> inFiles = new ArrayList<>();
                    File directory;
                    for(int i = 0; i < docPaths.size(); i++) {
                        System.out.println(docPaths.get(i).toString()); //prints element i
                        directory = new File(docPaths.get(i).toString());
                        inFiles.add(directory);
                        //System.out.println("Files", "Size: "+ files.length);

                    }
                    for(File f:inFiles){
                        DocList docList = new DocList();
                        docList.setSelectedListDoc(inFiles);
                        System.out.println("UserViewDoc onActivityResult for each Files name:  "+ f.getName());
                        System.out.println("UserViewDoc onActivityResult for each absolute path:  "+ f.getAbsolutePath());
                        docList.setDocFileName(f.getName());
                        docList.setDocFileAbsolutePath(f.getAbsolutePath());
                        docLists.add(docList);
                    }
                }
                break;
        }
        addThemToView(docPaths,docLists);
    }

    private void addThemToView(ArrayList<String> docPaths,ArrayList<DocList> docLists) {
        ArrayList<String> filePaths = new ArrayList<>();
        if (docPaths != null) filePaths.addAll(docPaths);
        if (recycleViewUserUploadDoc != null) {
            /*StaggeredGridLayoutManager layoutManager =
                    new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
            layoutManager.setGapStrategy(
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);*/
            listFileAdapter = new ListFileAdapter(docLists,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycleViewUserUploadDoc.setLayoutManager(mLayoutManager);
            recycleViewUserUploadDoc.setItemAnimator(new DefaultItemAnimator());
            recycleViewUserUploadDoc.setAdapter(listFileAdapter);
            recycleViewUserUploadDoc.setItemAnimator(new DefaultItemAnimator());
        }

        Toast.makeText(this, "Num of files selected: " + filePaths.size(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_view_doc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onPickDoc() {
        String[] doc = { ".doc", ".docx" };
        String[] pdfs = { ".pdf" };
        int maxCount = MAX_ATTACHMENT_COUNT - photoPaths.size();
        if ((docPaths.size() + photoPaths.size()) == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items",
                    Toast.LENGTH_SHORT).show();
        } else {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .setSelectedFiles(docPaths)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .setActivityTitle("Please select doc")
                    .addFileSupport("DOC", doc)
                    .addFileSupport("PDF", pdfs, R.drawable.pdf_blue)
                    .enableDocSupport(false)
                    .sortDocumentsBy(SortingTypes.name)
                    .withOrientation(Orientation.UNSPECIFIED)
                    .pickFile(this);
        }
    }

    @AfterPermissionGranted(RC_FILE_PICKER_PERM)
    public void pickDoc() {
        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            onPickDoc();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.pickDocs:
                pickDoc();
                return true;
            default:
                return false;
        }
    }
}