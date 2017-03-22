package launamgoc.halfoflove.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.ChatMessage;
import launamgoc.halfoflove.model.MyBundle;

/**
 * Created by Admin on 1/12/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageHolder> {
    private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
    private Bitmap ava = null;
    private Bitmap my_ava = null;
    @Override
    public ChatMessageAdapter.ChatMessageHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView;
        if (viewType == 0){
            itemView = inflater
                    .inflate(R.layout.cardview_chat_message_right, viewGroup, false);
        }else {
            itemView = inflater
                    .inflate(R.layout.cardview_chat_message_left, viewGroup, false);
        }

        return new ChatMessageAdapter.ChatMessageHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            int a = 0;
        }
        if (chatMessages.get(position).id_sender.equals(MyBundle.mUserBusiness.mUser.fid)){
            int a = 0;
            return 0;
        }else {
            int a = 0;
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(ChatMessageHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.load(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void setList(List<ChatMessage>chatMessages){
        this.chatMessages = chatMessages;
        Log.d("message", "message count: " + chatMessages.size());
        notifyDataSetChanged();
    }

    public void setImage(Bitmap ava, Bitmap my_ava){
        this.ava = ava;
        this.my_ava = my_ava;
    }

    public class ChatMessageHolder extends RecyclerView.ViewHolder {

        public ImageView ivAvatar;
        public TextView tvChatMessage;
        public TextView tvChatDateTime;

        public ChatMessageHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_chat_ava);
            tvChatMessage = (TextView) itemView.findViewById(R.id.cv_chat_message);
            tvChatDateTime = (TextView) itemView.findViewById(R.id.cv_chat_datetime);
        }

        public void load(@NonNull final ChatMessage chatMessage) {

            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            tvChatMessage.setText(chatMessage.message);
            tvChatDateTime.setText(chatMessage.datetime);
            if (chatMessage.id_sender.equals(MyBundle.mUserBusiness.mUser.fid)){
                ivAvatar.setImageBitmap(my_ava);
            }else{
                ivAvatar.setImageBitmap(ava);
            }
        }
    }
}
