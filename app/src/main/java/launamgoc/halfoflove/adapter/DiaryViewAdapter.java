package launamgoc.halfoflove.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.Information;

/**
 * Created by KhaTran on 11/18/2016.
 */

public class DiaryViewAdapter extends
        RecyclerView.Adapter<DiaryViewAdapter.InformationHolder>{
    private List<Information> listView = new ArrayList<Information>();

    public DiaryViewAdapter(List<Information> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    @Override
    public DiaryViewAdapter.InformationHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                       int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_calendar, viewGroup, false);
        return new DiaryViewAdapter.InformationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiaryViewAdapter.InformationHolder viewHolder, int position) {
        Information data = listView.get(position);
        viewHolder.load(data);
    }

    public void addItem(int position, Information data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public void updateItem(int position, String data) {
        listView.get(position).setContent(data);
        notifyDataSetChanged();
    }

    public class InformationHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvContent;

        public InformationHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.date);
            tvContent = (TextView) itemView.findViewById(R.id.content);
        }

        public void load(@NonNull final Information data) {
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());
        }
    }
}
