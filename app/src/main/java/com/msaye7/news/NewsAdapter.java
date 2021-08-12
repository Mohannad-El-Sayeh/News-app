package com.msaye7.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 * Created by Mohannad El-Sayeh on 30/07/2021
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    ArrayList<NewsModel> newsModels;
    Context context;

    public NewsAdapter(Context context, ArrayList<NewsModel> newsModels){
        this.newsModels = newsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent, false));
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel currentNewsModel = newsModels.get(position);

        holder.bindData(currentNewsModel);
    }

    protected class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView sectionTV, titleTV, publicationDateTV, authorTV;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTV = itemView.findViewById(R.id.section);
            titleTV = itemView.findViewById(R.id.tv_title);
            publicationDateTV = itemView.findViewById(R.id.publication_date);
            authorTV = itemView.findViewById(R.id.author);
        }


        public void bindData(NewsModel currentNewsModel) {

            sectionTV.setText(currentNewsModel.getSection() + ": ");
            titleTV.setText(currentNewsModel.getTitle());
            publicationDateTV.setText(currentNewsModel.getDatePublished());
            authorTV.setText(currentNewsModel.getAuthor());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(currentNewsModel.getWebUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if(intent.resolveActivity(context.getPackageManager()) != null){
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "No web browser found to open the news", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
