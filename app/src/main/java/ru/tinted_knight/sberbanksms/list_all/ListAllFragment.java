package ru.tinted_knight.sberbanksms.list_all;

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
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;
import ru.tinted_knight.sberbanksms.list_all.ui.ListRecyclerViewAdapter;

public class ListAllFragment extends Fragment
        implements ListAllViewModel.IShowProgress {

    public static final String TAG = "ListAllFragment";

    private OnItemClickListener listener;

    private ListRecyclerViewAdapter adapter;

    private ProgressDialog progressDialog;
    private RecyclerView rvMain;

    public static ListAllFragment newInstance() {
        ListAllFragment fragment = new ListAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListAllViewModel viewModel = ViewModelProviders.of(this).get(ListAllViewModel.class);
        registerObservers(viewModel);
        viewModel.onCreate();
    }

    private void registerObservers(ListAllViewModel viewModel) {
        viewModel.liveData.observe(this, new Observer<List<SimpleEntity>>() {
            @Override
            public void onChanged(@Nullable List<SimpleEntity> simpleEntities) {
                if (simpleEntities != null) {
                    if (adapter == null) {
                        adapter = new ListRecyclerViewAdapter(
                                getActivity().getApplicationContext(),
                                simpleEntities,
                                listItemClickListener);
                        rvMain.setAdapter(adapter);
                        rvMain.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvMain.addItemDecoration(
                                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                        rvMain.setVerticalScrollBarEnabled(true);
                    } else {
                        adapter.swapData(simpleEntities);
                        adapter.notifyDataSetChanged();
                    }
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

        viewModel.setProgressListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener) {
            listener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemClickListener");
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
//        progressDialog.dismiss();
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setTitle("Done");
        progressDialog.setMessage("Thank you for patience. Tap anywhere outside.");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemClickListener {
        // TODO: Update argument type and name
        void onItemClick(int id);
    }

    private ListRecyclerViewAdapter.ListItemClickListener listItemClickListener = new ListRecyclerViewAdapter.ListItemClickListener() {
        @Override
        public void onItemClick(int id) {
            Toast.makeText(getActivity(), "item tag: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
        }
    };
}
