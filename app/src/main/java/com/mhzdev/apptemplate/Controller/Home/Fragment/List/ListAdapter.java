package com.mhzdev.apptemplate.Controller.Home.Fragment.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mhzdev.apptemplate.Model.GenericModel;
import com.mhzdev.apptemplate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<GenericModel> listItems;

    private RoutesListAdapterListener mListener;

    private Context context;

    public ListAdapter(Context context, List<GenericModel> listItems, RoutesListAdapterListener listener) {
        this.listItems = listItems;
        this.mListener = listener;
        this.context = context;
        getResources();
    }

    private void getResources() {
        //Save the resource to local variables
    }

    public void updateDataSet(List<GenericModel> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Long itemId = listItems.get(position).getId();

        Context context = holder.rootView.getContext();

        GenericModel currentItem = listItems.get(position);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });

        holder.mTitle.setText(currentItem.getTitle());
        holder.mSubtitle.setText(currentItem.getSubtitle());

        if (!currentItem.getImage_url().equals(""))
            Picasso.with(context)
                    .load(currentItem.getImage_url())
                    .placeholder(R.drawable.logo_app)
                    .into(holder.mImageView);

    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface RoutesListAdapterListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rootView;
        public TextView mTitle;
        public TextView mSubtitle;
        public CircleImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            rootView = (RelativeLayout) v.findViewById(R.id.root_view);

            mTitle = (TextView) v.findViewById(R.id.title);
            mSubtitle = (TextView) v.findViewById(R.id.subtitle);
            mImageView = (CircleImageView) v.findViewById(R.id.image);

        }
    }
}