package launamgoc.halfoflove.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.activity.ChatListTabActivity;
import launamgoc.halfoflove.model.ChatElement;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class ChatListAdapter extends
        RecyclerView.Adapter<ChatListAdapter.ChatListHolder>{
    private List<ChatElement> listView = new ArrayList<ChatElement>();

    public ChatListAdapter(List<ChatElement> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    @Override
    public ChatListAdapter.ChatListHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_chatlist, viewGroup, false);
        return new ChatListAdapter.ChatListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ChatListHolder viewHolder, int position) {
        ChatElement data = listView.get(position);
        viewHolder.load(data);
    }

    public void addItem(int position, ChatElement data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public class ChatListHolder extends RecyclerView.ViewHolder {

        public CardView cvChat;
        public ImageView ivAvatar;
        public TextView tvName;

        public ChatListHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_chat_ava);
            tvName = (TextView) itemView.findViewById(R.id.cv_chat_name);
            cvChat = (CardView) itemView.findViewById(R.id.cv_chat);
        }

        public void load(@NonNull final ChatElement data) {
            ivAvatar.setImageResource(data.getImage());
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            tvName.setText(data.getName());
            cvChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatListTabActivity activity = (ChatListTabActivity) view.getContext();
                    activity.onItemClick();
                }
            });
        }
    }
}
