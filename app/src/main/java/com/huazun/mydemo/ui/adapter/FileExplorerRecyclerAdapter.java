package com.huazun.mydemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.huazun.mydemo.R;
import com.huazun.mydemo.utilities.DateUtils;

import java.io.File;
import java.util.List;

public class FileExplorerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private final static String TAG = "FileExplorerRecyclerAdapter";
    private List<File> itemList;
    private OnItemClickListener onItemClickListener = null;
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean hasParentFolder = false;

    public FileExplorerRecyclerAdapter(Context context, List<File> itemList, boolean hasParentFolder) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.hasParentFolder = hasParentFolder;
    }

    public boolean isHasParentFolder() {
        return hasParentFolder;
    }

    public void setHasParentFolder(boolean hasParentFolder) {
        this.hasParentFolder = hasParentFolder;
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconView;
        public TextView fileNameText;
        public TextView fileDescText;

        public FileViewHolder(View view) {
            super(view);
            iconView = (ImageView)view.findViewById(R.id.itemFileExploreImageView);
            fileNameText = (TextView) view.findViewById(R.id.itemFileExploreNameView);
            fileDescText = (TextView) view.findViewById(R.id.itemFileExploreDescView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return itemList.size() ;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        File file = itemList.get(position);
        FileViewHolder viewHolder = (FileViewHolder)holder;
        if (position == 0 && hasParentFolder){
            viewHolder.fileNameText.setText(R.string.parent_folder);
            viewHolder.fileDescText.setText("");
            viewHolder.fileDescText.setVisibility(View.INVISIBLE);
            viewHolder.iconView.setImageResource(R.mipmap.up_arrow);
        } else {
            viewHolder.fileNameText.setText(file.getName());
//            viewHolder.fileDescText.setText(new java.util.Date(file.lastModified()).toString());
            viewHolder.fileDescText.setText(com.huazun.mydemo.utilities.DateUtils.getDateString(file.lastModified()) + "       " + DateUtils.getDataSize(file.length()));
            viewHolder.fileDescText.setVisibility(View.VISIBLE);
            if (file.isDirectory()) {
                viewHolder.iconView.setImageResource(R.mipmap.folder_icon);
            } else {
                viewHolder.iconView.setImageResource(R.mipmap.file_icon);
            }
        }
        viewHolder.itemView.setTag(position);
        viewHolder.itemView.setBackgroundColor(position %2 == 0 ? ContextCompat.getColor(context, R.color.itemViewColor1) :
                ContextCompat.getColor(context, R.color.itemViewColor2));


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_file_explore, parent, false);
        FileViewHolder vh = new FileViewHolder(view);
        view.setOnClickListener(this);
        return vh;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, Integer.parseInt(String.valueOf(v.getTag())));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
