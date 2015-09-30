package com.mhzdev.apptemplate.Controller.Home.Fragment.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mhzdev.apptemplate.Controller.BaseFragment;
import com.mhzdev.apptemplate.Controller.Detail.DetailActivity;
import com.mhzdev.apptemplate.Controller.Home.HomeActivity;
import com.mhzdev.apptemplate.Model.GenericModel;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Services.API.BaseResponse;
import com.mhzdev.apptemplate.Services.API.Call.GetDataListAPI;
import com.mhzdev.apptemplate.Services.API.Response.GetDataListResponse;
import com.mhzdev.apptemplate.Services.ApiAdapterBuilder;
import com.mhzdev.apptemplate.Services.ApiCallback;
import com.mhzdev.apptemplate.Services.ApiList;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class ListFragment extends BaseFragment {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "ListFragment";
    @SuppressWarnings("unused")
    public final String LOG_TAG = "ListFragment";

    //Views
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout errorLoadingMessage;
    private LinearLayout errorNoItem;

    //Adapter
    private ListAdapter mAdapter;

    //Flags
    private boolean swipeLayoutOn;
    private ListFragmentListener mListener;

    //Model
    private List<GenericModel> dataList = new ArrayList<>();
    private RecyclerView listView;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init all Views
        initViews(view);
        initRecycleView(view);

        setSwipeToRefreshEnabled(true);
    }

    private void initViews(View view) {
        //When click on error loading, reload data
        errorLoadingMessage = (LinearLayout) view.findViewById(R.id.loading_error_label);
        errorLoadingMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        errorNoItem = (LinearLayout) view.findViewById(R.id.no_item_label);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.primary);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //When list is pulled down, refresh
                loadData();
            }
        });
    }

    private void initRecycleView(View view) {
        //Set the recycle view
        listView = (RecyclerView) view.findViewById(R.id.recycle_view);
        listView.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        //Enable the swipe only if the first item of the list is visible
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mLayoutManager;
                boolean firstItemVisible = linearLayoutManager.findFirstVisibleItemPosition() == 0;
                boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                boolean onTopOfList = firstItemVisible && topOfFirstItemVisible;

                if (swipeLayoutOn)
                    swipeLayout.setEnabled(onTopOfList);
            }
        });

        //Set the adapter
        List<GenericModel> modelList = new ArrayList<>();
        mAdapter = new ListAdapter(getContext(), modelList, new ListAdapter.RoutesListAdapterListener() {
            @Override
            public void onItemClick(int position) {
                onListItemClick(position);
            }
        });
        listView.setAdapter(mAdapter);
    }

    /**
     * When an item is clicked
     *
     * @param position position of the item clicked
     */
    private void onListItemClick(int position) {
        GenericModel model = dataList.get(position);

//        View listTitleView = listView.getChildAt(position).findViewById(R.id.title);
//        View listSubtitleView = listView.getChildAt(position).findViewById(R.id.subtitle);
//        Activity activity = getActivity();
//        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
//        detailIntent.putExtra(DetailActivity.EXTRA_MODEL, model);
//        ActivityOptions options =
//                ActivityOptions.makeSceneTransitionAnimation(activity,
//                Pair.create(listTitleView, "title"),
//                Pair.create(listSubtitleView, "subtitle"));
//        getActivity().startActivity(detailIntent, options.toBundle());

        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra(DetailActivity.EXTRA_MODEL, model);
        getActivity().startActivity(detailIntent);
    }

    /**
     * Load the data every time the fragment is shown
     */
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * Load the data
     */
    private void loadData() {

        loadDataFromServer();
    }

    /**
     * Make the call to load the data from the server
     */
    protected void loadDataFromServer() {
        //Start loading indicator
        setSwipeLoadingIndicator(true);

        ApiList apiList = ApiAdapterBuilder.getApiAdapter();
        GetDataListAPI getSearchRoutesAPI = new GetDataListAPI(getActivity());
        //Loading indicator of the api disabled, show error also disabled
        apiList.getDataList(getSearchRoutesAPI, new GetDataListCallback(getActivity(), false, false));

    }

    /**
     * Update the list according to the new model
     *
     * @param modelList modelList
     */
    protected void updatePathsList(List<GenericModel> modelList) {
        if (modelList == null)
            return;

        //If the list are empty show a "no item" error
        if (modelList.size() > 0)
            errorNoItem.setVisibility(View.GONE);
        else
            errorNoItem.setVisibility(View.VISIBLE);


        //Update the adapter
        mAdapter.updateDataSet(modelList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Enable or disable the swipe to reload feature
     *
     * @param enabled swipe enabled
     */
    protected void setSwipeToRefreshEnabled(boolean enabled) {
        swipeLayoutOn = enabled;
        swipeLayout.setEnabled(enabled);
    }

    /**
     * Show the loading indicator (Used inside the call..)
     *
     * @param enabled loading enabled
     */
    protected void setSwipeLoadingIndicator(final boolean enabled) {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(enabled);
            }
        });
    }

    public void setListener(ListFragmentListener ListFragmentListener) {
        mListener = ListFragmentListener;
    }


    /**
     * INTERFACE - Callback used by {@link HomeActivity}
     */
    public interface ListFragmentListener {
    }

    /**
     * ***************************
     * GetDataList Api - Callback
     * *****************************
     */
    protected class GetDataListCallback extends ApiCallback<GetDataListResponse> {
        public GetDataListCallback(Context context, boolean requestLoading, boolean notifyErrors) {
            super(context, requestLoading, notifyErrors);
        }

        @Override
        public void onSuccess(BaseResponse<GetDataListResponse> response, Response httpResponse) {
            //Disable the error view
            setSwipeLoadingIndicator(false);
            errorLoadingMessage.setVisibility(View.GONE);

            dataList = response.response.dataList;

            //Update the list
            updatePathsList(response.response.dataList);
        }

        @Override
        public void onResponseKO(BaseResponse<GetDataListResponse> response, Response httpResponse) {
            //Enable the error view
            setSwipeLoadingIndicator(false);
            errorLoadingMessage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFail(RetrofitError error) {
            //Enable the error view
            setSwipeLoadingIndicator(false);
            errorLoadingMessage.setVisibility(View.VISIBLE);
        }
    }


}

