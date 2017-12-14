package ru.tinted_knight.sberbanksms.ui.main_screen;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;
import ru.tinted_knight.sberbanksms.ui.adapters.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.ui.adapters.ListRecyclerViewAdapter;
import ru.tinted_knight.sberbanksms.viewmodel.MainViewModel;

public class MainFragment extends Fragment
        implements MainViewModel.IShowProgress {

    public static final String TAG = "MainFragment";

    private OnMainFragmentInteractionListener listener;

    private ListRecyclerViewAdapter adapter;

    private ProgressDialog progressDialog;

    private RecyclerView rvMain;

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_all, container, false);
        rvMain = root.findViewById(R.id.rvMain);
        adapter = new ListRecyclerViewAdapter(
                getActivity().getApplicationContext(),
                listItemClickListener);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMain.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvMain.setVerticalScrollBarEnabled(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onFragmentResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getActivity().getApplication(), this);
        final MainViewModel viewModel =
                ViewModelProviders.of(this, factory).get(MainViewModel.class);
        registerObservers(viewModel);
    }

    private void registerObservers(MainViewModel viewModel) {
        viewModel.liveData.observe(this, new Observer<List<SimpleEntity>>() {
            @Override
            public void onChanged(@Nullable List<SimpleEntity> entities) {
                if (entities != null) {
                    adapter.setData(entities);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        viewModel.popupMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null && !s.equals(""))
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.progress.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer progress) {
                if (progress != null)
                    progressDialog.setProgress(progress);
            }
        });

//        viewModel.setProgressListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            listener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onProgressStart(String title, String text, int max) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(max);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(text);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    public void onProgressHide() {
        progressDialog.dismiss();
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setTitle("Done");
        progressDialog.setMessage("Thank you for patience. Tap anywhere outside.");
    }

    public interface OnMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListItemClick(int id, ListRecyclerViewAdapter.ViewHolder holder);

        void onListItemLongClick(int id);

        void onFragmentResume();
    }

    private ListRecyclerViewAdapter.ListItemClickListener listItemClickListener = new ListRecyclerViewAdapter.ListItemClickListener() {
        @Override
        public void onItemClick(int id, ListRecyclerViewAdapter.ViewHolder holder) {
            listener.onListItemClick(id, holder);
        }

        @Override
        public void onItemLongClick(int id) {
            listener.onListItemLongClick(id);
        }
    };
}
