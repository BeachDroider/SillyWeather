package com.example.foad.sillyweather.ui.weather

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.foad.sillyweather.R
import kotlinx.android.synthetic.main.fragment_city.*

class CityFragment : Fragment(), View.OnClickListener {

    lateinit var citySelectionListener: CitySelectionListener

    companion object {
        fun newInstance(): CityFragment {
            return CityFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        citySelectionListener = context as CitySelectionListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_vancouver.setOnClickListener(this)
        btn_paris.setOnClickListener(this)
        btn_singapore.setOnClickListener(this)
        btn_sydney.setOnClickListener(this)
        btn_tehran.setOnClickListener(this)

    }

    interface CitySelectionListener {
        fun onCitySelected(city: String)
    }

    override fun onClick(v: View?) {
        val text = (v as Button).text.toString()
        citySelectionListener.onCitySelected(text)
    }
}