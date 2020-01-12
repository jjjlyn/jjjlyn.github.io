```java
package com.esumtech.naisvn.view.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final int HEADER = 0;
    protected final int ITEM = 1;
    protected final int FOOTER = 2;

    protected List<T> mItems;
    protected OnItemClickListener mOnItemClickListener;
    protected OnReloadClickListener mOnReloadClickListener;
    protected boolean mIsFooterAdded = false;

    private static final String TAG = BaseAdapter.class.getSimpleName();


    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }

    public BaseAdapter() {
        mItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case HEADER: {
                viewHolder = createHeaderViewHolder(parent);
                break;
            }
            case ITEM: {
                viewHolder = createItemViewHolder(parent);
                break;
            }
            case FOOTER: {
                viewHolder = createFooterViewHolder(parent);
                break;
            }
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER: {
                bindHeaderViewHolder(holder, position);
                break;
            }
            case ITEM: {
                bindItemViewHolder(holder, position);
                break;
            }
            case FOOTER: {
                bindFooterViewHolder(holder);
                break;
            }
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mItems.size());
        return mItems.size();
    }

    protected abstract RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent);

    protected abstract void bindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    protected abstract void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder);

    protected abstract void displayLoadMoreFooter();

    protected abstract void displayErrorFooter();

    protected abstract void addFooter();


    public T getItem(int position) {
        return mItems.get(position);
    }

    public void add(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
        notifyDataSetChanged();
    }

    public void remove(T item) {
        int position = mItems.indexOf(item);
        if (position > -1) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mIsFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public boolean isLastPosition(int position) {
        return (position == mItems.size() - 1);
    }

    public void removeFooter() {
        mIsFooterAdded = false;
        int position = mItems.size() - 1;
        T item = getItem(position);
        if (item != null) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFooter(FooterType footerType) {
        switch (footerType) {
            case LOAD_MORE: {
                displayLoadMoreFooter();
                break;
            }
            case ERROR: {
                displayErrorFooter();
                break;
            }
            default:
                break;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnReloadClickListener(OnReloadClickListener onReloadClickListener) {
        this.mOnReloadClickListener = onReloadClickListener;
    }

    public enum FooterType {
        LOAD_MORE,
        ERROR
    }
}
```
