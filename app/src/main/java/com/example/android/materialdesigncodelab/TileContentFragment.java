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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static com.example.android.materialdesigncodelab.TileContentFragment.ContentAdapter.epubs;

/**
 * Provides UI for the view with Tile.
 */

public class TileContentFragment extends Fragment {



    ArrayAdapter<String> adapter;
    static File selected;
    boolean firstTime = true;
    private WebView wv1;
    private static String line;
    //protected Unzip_Openbook uzip;
    MainActivity activity;
    private static Context context;
    String foldername = "unzip";
    public static TileContentFragment t1= new TileContentFragment();
    static List<File> epubs;
    static List<String> names;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    /*private List<String> fileNames(List<File> files) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
            res.add(files.get(i).getName().replace(".epub", ""));

        }
        return res;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if ((epubs == null) || (epubs.size() == 0)) {
            //Log.i("dirct",Environment.getExternalStorageDirectory().toString());
            epubs = epubList(Environment.getExternalStorageDirectory());

        }

        names = fileNames(epubs);
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(),epubs);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        String foldername = "unzip1";
        protected Unzip_Openbook uzip;
        public ImageView picture;
        public TextView name;
        public ViewHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.tile_picture);
            name = (TextView) itemView.findViewById(R.id.tile_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    /*if (parent.getTag()==null)
                        Log.i("sala","nullhbc");
                    String position = parent.getTag().toString();

                    int pos = Integer.valueOf(position);*/
                    selected = epubs.get(getAdapterPosition());
                    String abc = selected.getAbsolutePath();

                    try {
                        uzip = new Unzip_Openbook(abc,foldername);

                        //System.out.print("going into constructor");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // an intent to open file reader that can open books but we removed this to unzip files
                    Intent i= new Intent(v.getContext(), File_ReaderActivity.class);
                    i.putExtra("path", selected.getAbsolutePath());
                    context.startActivity(i);
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
        // Set numbers of Tiles in RecyclerView.

        //public Book[] book;
        static List<File> epubs;
        //static List<String> names;
        //private static final int LENGTH ;

        private final String[] mPlaces;
        public Bitmap[] coverImage;
        private final Drawable[] mPlacePictures;
        public ContentAdapter(Context context,List<File> epubs) {
            Resources resources = context.getResources();
            //mPlaces = resources.getStringArray(R.array.places);
            this.epubs=epubs;
            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
            mPlacePictures = new Drawable[a.length()];
            for (int i = 0; i < mPlacePictures.length; i++) {
                mPlacePictures[i] = a.getDrawable(i);
            }
            mPlaces= new String[epubs.size()];
            for (int i = 0; i < epubs.size(); i++) {
            mPlaces[i]=(epubs.get(i).getName().replace(".epub", ""));
            }
            //book=new Book[epubs.size()];
            coverImage= new Bitmap[epubs.size()];

            for (int j=0; j<epubs.size();j++){
                Log.i("forkandar","abcfdeg");
                selected = epubs.get(j);

                String path = selected.getAbsolutePath();
                Log.i("raasta",path);
                try {
                InputStream  inputStream = new FileInputStream(path);

                    //book[j] = (new EpubReader().readEpub(inputStream));
                    //if(book[j]==null) Log.i("nullbook","hvcjfc");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            a.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.itemView.setTag(position);
            holder.picture.setImageResource(R.drawable.pic5);
            holder.name.setText(mPlaces[position % mPlaces.length]);
        }

        @Override
        public int getItemCount() {
            return epubs.size();
        }





    }
    private List<String> fileNames(List<File> files) {
        List<String> res = new ArrayList<String>();

        for (int i = 0; i < files.size(); i++) {
            res.add(files.get(i).getName().replace(".epub", ""));



/*
* NOTE: future
res.add(files.get(i).getName().replace(".epub", "").replace(".e0", ""));
*/
        }
        return res;
    }

    private List<File> epubList(File dir) {
        List<File> res = new ArrayList<File>();
        if (dir.isDirectory()) {
            Log.i("kuchnhi",dir.toString());

            File[] f = dir.listFiles();
            if (f != null) {
                Log.i("kuchbi","456978");
                for (int i = 0; i < f.length; i++) {
                    if (f[i].isDirectory()) {
                        res.addAll(epubList(f[i]));
                    } else {
                        String lowerCasedName = f[i].getName().toLowerCase();
                        if (lowerCasedName.endsWith(".epub")) {
                            res.add(f[i]);
                        }

                    }
                }
            }
        }
        return res;
    }
}