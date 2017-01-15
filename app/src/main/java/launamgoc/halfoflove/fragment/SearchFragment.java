package launamgoc.halfoflove.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.SearchAdapter;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.User;

/**
 * Created by Admin on 1/16/2017.
 */

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static List<User> listView = new ArrayList<User>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(listView==null)
        {
            listView = new ArrayList<User>();
        }

        setRecyclerView(view);

        getAllUsers();
    }

    private void setRecyclerView(View v){
        // Set RecyclerView
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(listView, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void getAllUsers(){
        FirebaseHelper.getAllUser(new FirebaseHelper.FirebaseGetAllUserDelegate() {
            @Override
            public void onGetAllUserSuccess(List<User> users) {
                adapter.setListView(users);
            }

            @Override
            public void onGetAllUserFailed() {

            }
        });
    }
}
