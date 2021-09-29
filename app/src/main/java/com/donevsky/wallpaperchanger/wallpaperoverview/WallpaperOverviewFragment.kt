package com.donevsky.wallpaperchanger.wallpaperoverview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.donevsky.wallpaperchanger.R
import com.donevsky.wallpaperchanger.databinding.WallpaperOverviewFragmentBinding

class WallpaperOverviewFragment : Fragment() {

    private lateinit var viewModel: WallpaperOverviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: WallpaperOverviewFragmentBinding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.wallpaper_overview_fragment,
                container,
                false)

        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = WallpaperRecyclerViewAdapter()
        binding.wallpaperGridRecyclerView.adapter = adapter

        val viewModelFactory = WallpaperOverviewViewModel.Factory(requireNotNull(activity).application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WallpaperOverviewViewModel::class.java)

        binding.overviewViewModel = viewModel

        binding.addFavesButton.setOnClickListener{
            viewModel.addFavourites(adapter.selectedPhotos)
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    viewModel.getWallpapers(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }
}