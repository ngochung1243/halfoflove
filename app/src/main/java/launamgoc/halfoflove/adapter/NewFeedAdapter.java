package launamgoc.halfoflove.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.NewFeedElement;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedAdapter  extends
        RecyclerView.Adapter<NewFeedAdapter.NewFeedHolder>{
    private List<NewFeedElement> listView = new ArrayList<NewFeedElement>();

    public NewFeedAdapter(List<NewFeedElement> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    @Override
    public NewFeedAdapter.NewFeedHolder onCreateViewHolder(ViewGroup viewGroup,
                                                             int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_newfeed, viewGroup, false);
        return new NewFeedAdapter.NewFeedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewFeedAdapter.NewFeedHolder viewHolder, int position) {
        NewFeedElement data = listView.get(position);
        viewHolder.load(data);
    }

    public void addItem(int position, NewFeedElement data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public class NewFeedHolder extends RecyclerView.ViewHolder {

        public ImageView ivAvatar;
        public TextView tvName;
        public TextView tvDay;
        public TextView tvContent;
        public ImageView ivImage;

        public NewFeedHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_nf_ava);
            tvName = (TextView) itemView.findViewById(R.id.cv_nf_name);
            tvDay = (TextView) itemView.findViewById(R.id.cv_nf_day);
            tvContent = (TextView) itemView.findViewById(R.id.cv_nf_content);
            ivImage = (ImageView) itemView.findViewById(R.id.cv_nf_image);
        }

        public void load(@NonNull final NewFeedElement data) {
            ivAvatar.setImageResource(data.getAva());
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            tvName.setText(data.getName());
            tvDay.setText(data.getDay());
            tvContent.setText(data.getContent());
            ivImage.setImageResource(data.getImage());
            ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
