package com.gp.abcpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksListViewHolder>{
    private Context mCtx;
    private List<Book> booksList;
    private RecyclerViewClickListener listener;

    public BooksAdapter(Context mCtx, List<Book> booksList, RecyclerViewClickListener listener) {
        this.mCtx = mCtx;
        this.booksList = booksList;
        this.listener = listener;

    }

    @NonNull

    @Override
    public BooksListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.book_layout, null);
        return new BooksListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksListViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bookName.setText(book.getTitle());
        Glide.with(mCtx).load(book.getCoverImg()).into(holder.bookCover);
    }

    @Override
    public int getItemCount() { return booksList.size(); }


    class BooksListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookName;
        ImageView bookCover;

        public BooksListViewHolder(final View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookCover = itemView.findViewById(R.id.bookCover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
