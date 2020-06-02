package com.huazun.mydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huazun.mydemo.R;
import com.huazun.mydemo.ui.adapter.FileExplorerRecyclerAdapter;
import com.huazun.mydemo.utilities.PackageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileExplorerActivity extends AppCompatActivity {
    @BindView(R.id.fileExplorerBackBtn)
    ImageView backBtn;
    @BindView(R.id.fileExplorerPathText)
    TextView pathView;
    @BindView(R.id.fileExplorerFilesView)
    RecyclerView filesRecyclerView;
    private List<String> pathList;
    private List<File> fileList;
    private FileExplorerRecyclerAdapter adapter = null;
    private File rootFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        ButterKnife.bind(this);
        PackageUtils.verifyStoragePermissions(this);

        initPage();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initPage(){
        pathList = new ArrayList<String>();
        fileList = new ArrayList<File>();
        rootFile = Environment.getExternalStorageDirectory();
//        System.out.println("rrrrr ========= " + rootFile.getAbsolutePath() + "========" + rootFile.getAbsolutePath()
//                + "=======" + this.getExternalFilesDir(null) + "====" + getExternalCacheDir() + "====" + getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

//        File sd = new File(Environment.getRootDirectory().getAbsolutePath().getExternalStorageState());
//        String rootPath = getExternalFilesDir(null).getAbsolutePath();
//        System.out.println("rootPath ======= " + rootPath);
        getFileDir(rootFile);
        pathView.setText(rootFile.getAbsolutePath());
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileExplorerRecyclerAdapter(this, fileList, false);
        filesRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FileExplorerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                File file = fileList.get(position);

                if (file.isDirectory()) {

                    getFileDir(file);
                    adapter.notifyDataSetChanged();
                    pathView.setText(file.getAbsolutePath());
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("filePath", file.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void getFileDir(File file){
//        fileList.clear();
//        File file = new File(filePath);

        File[] files = file.listFiles();
        while (fileList.size() >0)
            fileList.remove(0);
        for (File f : files){
            if (!f.isHidden())
                fileList.add(f);
        }

        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        if (!file.getAbsolutePath().equals(rootFile.getAbsolutePath())) {
            fileList.add(0, file.getParentFile());
            if (adapter != null)
                adapter.setHasParentFolder(true);
        } else {
            if (adapter != null)
                adapter.setHasParentFolder(false);
        }

    }

}
