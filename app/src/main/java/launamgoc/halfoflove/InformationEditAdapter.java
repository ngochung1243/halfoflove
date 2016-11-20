package launamgoc.halfoflove;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KhaTran on 11/11/2016.
 */

public class InformationEditAdapter extends
        RecyclerView.Adapter<InformationEditAdapter.InformationHolder>{

    private List<Information> listView = new ArrayList<Information>();

    public InformationEditAdapter(List<Information> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    @Override
    public InformationHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_edit_profile, viewGroup, false);
        return new InformationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InformationHolder viewHolder, int position) {
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
        public ImageButton btnNext;

        public InformationHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvContent = (TextView) itemView.findViewById(R.id.content);
            btnNext = (ImageButton) itemView.findViewById(R.id.btn_next);
        }

        public void load(@NonNull final Information data) {
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditProfileActivity activity = (EditProfileActivity) view.getContext();
                    activity.onClickPost(data.getTitle(), data.getContent(), data.getId());
                }
            });
        }
    }
}
