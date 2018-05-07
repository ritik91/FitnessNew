/*
*   Copyright 2016 Marco Gomiero
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
*/

package com.ritikakhiria.fitnessnew.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prof.rssparser.Article;
import com.ritikakhiria.fitnessnew.R;
import com.ritikakhiria.fitnessnew.activity.HealthBlogDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/** GitHub Repository:
 * Created by Marco Gomiero on 12/02/2015.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;

    private int rowLayout;
    private Context mContext;

    public ArticleAdapter(ArrayList<Article> list, int rowLayout, Context context) {

        this.articles = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }


    @Override
    public long getItemId(int item) {
        // TODO Auto-generated method stub
        return item;
    }
    public void clearData(){
        if (articles != null)
            articles.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)  {

        Article currentArticle = articles.get(position);

        Locale.setDefault(Locale.getDefault());
        Date date = currentArticle.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String pubDateString = sdf.format(date);

        viewHolder.title.setText(currentArticle.getTitle());

        //load the image. If the parser did not find an image in the article, it set a placeholder.
        Glide.with(mContext)
                .load(currentArticle.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200,200)
                .dontAnimate()
                .skipMemoryCache(true)
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String content = articles.get(position).getContent();
                Intent intent = new Intent(mContext, HealthBlogDetailActivity.class);
                intent.putExtra("HealtDetail",content);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return articles == null ? 0 : articles.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView pubDate;
        ImageView image;

        public ViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            pubDate = (TextView) itemView.findViewById(R.id.pubDate);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}