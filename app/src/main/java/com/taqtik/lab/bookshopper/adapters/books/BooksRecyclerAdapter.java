package com.taqtik.lab.bookshopper.adapters.books;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taqtik.lab.bookshopper.R;
import com.taqtik.lab.bookshopper.fragments.books.BooksFragment;
import com.taqtik.lab.bookshopper.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksRecyclerAdapter extends ArrayAdapter<Book> {
    private Context mContext;
    public List<Book> items = new ArrayList<Book>();

    private BooksFragment fragment;

    public BooksRecyclerAdapter(ArrayList<Book> data, Context context) {
        super(context, R.layout.book_list_item, data);
        this.items = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        Book item = items.get(position);
        BooksRecyclerAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new BooksRecyclerAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.book_list_item, parent, false);
            viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.title_textView);
            viewHolder.itemAuthor = (TextView) convertView.findViewById(R.id.author_textView);
            viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.price_textView);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BooksRecyclerAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.itemTitle.setText(item.title);
        String authorText = "Author: " + item.author.first_name +  " " + item.author.family_name;
        viewHolder.itemAuthor.setText(authorText);
        String priceText = "Price: $" + item.price;
        viewHolder.itemPrice.setText(priceText);
        return result;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(Book item) {
        items.add(item);
    }

    private static class ViewHolder {
        public TextView itemTitle;
        public TextView itemAuthor;
        public TextView itemPrice;
    }
}
