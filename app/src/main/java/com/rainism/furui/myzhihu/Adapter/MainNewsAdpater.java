package com.rainism.furui.myzhihu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainism.furui.myzhihu.Model.News;
import com.rainism.furui.myzhihu.R;
import com.rainism.furui.myzhihu.Tools.ImageTools;

import java.util.ArrayList;

/**
 * Created by furui on 16/5/26.
 */
public class MainNewsAdpater extends BaseAdapter {
    Context context;
    ArrayList<News> newsList;
    LayoutInflater layoutInflater;

    public MainNewsAdpater() {
        super();
    }

    public MainNewsAdpater(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<News> newsList) {
        this.newsList = newsList;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        News news = newsList.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.main_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imageview);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_textview);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_linearlayout);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (!news.isTopTitle()) {
            ImageTools.downlandImageView(context, viewHolder.imageView, news.getImageUrl().get(0));
            viewHolder.linearLayout.setBackgroundResource(R.drawable.main_list_item_bg);
        } else {
            viewHolder.imageView.setImageBitmap(null);
            viewHolder.linearLayout.setBackgroundColor(android.R.color.white);
        }
        viewHolder.textView.setText(news.getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
    }
}
