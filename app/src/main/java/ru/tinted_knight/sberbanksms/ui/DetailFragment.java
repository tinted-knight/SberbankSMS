package ru.tinted_knight.sberbanksms.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.dao.query_pojos.MessageEntity;
import ru.tinted_knight.sberbanksms.databinding.FragmentDetailBinding;
import ru.tinted_knight.sberbanksms.viewmodel.DetailViewModel;

public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "detail_fragment";

    private static final String ITEM_ID = "item_id";
    private static final String ITEM_POSITION = "item_pos";

    // TODO: Rename and change types of parameters
    private int itemId;

    private FragmentDetailBinding binding;

    private OnDetailInteraction listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemId Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(int itemId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DetailViewModel.Factory factory = new DetailViewModel.Factory(
                getActivity().getApplication(),itemId);
        final DetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
        registerObservers(viewModel);
    }

    private void registerObservers(DetailViewModel viewModel) {
        viewModel.data.observe(this, new Observer<MessageEntity>() {
            @Override
            public void onChanged(@Nullable MessageEntity entity) {
                if (entity != null) {
                    binding.tvAgent.setText(entity.agent);
                    binding.tvSumma.setText(entity.getSummaString());
                    binding.tvBalance.setText(entity.getBalanceString());
                    binding.tvCard.setText(entity.getCardSummary());
                    binding.tvDate.setText(entity.getDateString());
                    binding.tvTime.setText(entity.getTimeString());

                    switch (entity.type) {
                        case Constants.OperationType.INCOME:
                            binding.tvSumma.setTextColor(ContextCompat.getColor(
                                    getActivity().getApplicationContext(), R.color.summa_income));
                            break;
                        case Constants.OperationType.OUTCOME:
                            binding.tvSumma.setTextColor(ContextCompat.getColor(
                                    getActivity().getApplicationContext(), R.color.summa_expense));
                            break;
                        case Constants.OperationType.ATM_OUT:
                            binding.tvSumma.setTextColor(ContextCompat.getColor(
                                    getActivity().getApplicationContext(), R.color.summa_atm));
                            break;
                    }

                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getInt(ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.tvAgent.setTransitionName("agent");
            binding.tvSumma.setTransitionName("summa");
        }
        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSmthHappened() {
        if (listener != null) {
            listener.onFragmentInteraction("interaction with DetailFragment");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailInteraction) {
            listener = (OnDetailInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnDetailInteraction {
        void onFragmentInteraction(String message);
    }
}
