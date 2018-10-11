/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialdesigncodelab;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the view with List.
 */
public class ListContentFragment extends Fragment {

    final static String EXTENSION_NAME = ".pdf";
    static List<File> fileList;
    static List<String> names;
    private ArrayAdapter<String> adapter;
    private ListView list;
    static File selected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if ((fileList == null) || (fileList.size() == 0)) {
            fileList = getPdfList(Environment.getExternalStorageDirectory());
        }


        names = getFileNames(fileList);
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
       ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(),fileList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    selected = fileList.get(getAdapterPosition());
                    Intent i= new Intent(v.getContext(), ViewPdfActivity.class);
                    i.putExtra("PDF_PATH", selected.getAbsolutePath());
                    v.getContext().startActivity(i);
                    /*Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);*/
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.

        private final String[] mPlaces;
        //private final String[] mPlaceDesc;
        private final Drawable[] mPlaceAvators;

        public ContentAdapter(Context context,List<File> fileList) {
            Resources resources = context.getResources();
            //this.fileList=new ArrayList<>(fileList.size());
            //this.fileList=fileList;

            //mPlaces = resources.getStringArray(R.array.places);
            //mPlaceDesc = resources.getStringArray(R.array.place_desc);
            TypedArray a = resources.obtainTypedArray(R.array.place_avator);
            mPlaceAvators = new Drawable[a.length()];
            for (int i = 0; i < mPlaceAvators.length; i++) {
                mPlaceAvators[i] = a.getDrawable(i);
            }
            mPlaces= new String[fileList.size()];
            for (int i = 0; i < fileList.size(); i++) {
                mPlaces[i]=(fileList.get(i).getName().replace(".pdf", ""));
            }

            a.recycle();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
            holder.name.setText(mPlaces[position % mPlaces.length]);
            //holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

    }
    private List<String> getFileNames(List<File> files) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
            res.add(files.get(i).getName().replace(".pdf", ""));

        }
        return res;
    }

    // TODO: hardcoded string
    // TODO: check with mimetype, not with filename extension
    private List<File> getPdfList(File dir) {
        List<File> res = new ArrayList<File>();
        if (dir.isDirectory()) {
            File[] f = dir.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    if (f[i].isDirectory()) {
                        res.addAll(getPdfList(f[i]));
                    } else {
                        String lowerCasedName = f[i].getName().toLowerCase();
                        if (lowerCasedName.endsWith(".pdf")) {
                            res.add(f[i]);
                        }

                    }
                }
            }
        }
       return res;

    }

    /*private void openPdfFile(String filePath) {


        /*File file = new File(filePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PDFActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }*/

/*        Intent mIntent = new Intent(PDFActivity.this, ViewPdfActivity.class);
        mIntent.putExtra("PDF_PATH", filePath);
        startActivity(mIntent);


    }*/


    private void refreshList() {
        fileList = getPdfList(Environment.getExternalStorageDirectory());
        names.clear();
        names.addAll(getFileNames(fileList));
        this.adapter.notifyDataSetChanged();
    }


}
