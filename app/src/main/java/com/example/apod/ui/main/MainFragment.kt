package com.example.apod.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.apod.R
import com.example.apod.room.entity.PODImageData
import com.example.apod.utils.ImageStorage
import com.example.apod.utils.Util
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val observer = Observer<PODImageData?> { serverResponse ->
            serverResponse?.let{
                serverResponse.url?.let{url->
                    image_view.setImageURI(ImageStorage.getImageUri(context, ImageStorage.POD_FILENAME))
                    message.text = serverResponse.explanation
                    title.text = serverResponse.title
                    if(!serverResponse.date.equals(Util.getCurrentDate("yyyy-MM-dd"))) Toast.makeText(context, resources.getString(R.string.err_msg_last_pic), Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.getPictureOfTheDay(context)?.observe(viewLifecycleOwner, observer)
    }

}