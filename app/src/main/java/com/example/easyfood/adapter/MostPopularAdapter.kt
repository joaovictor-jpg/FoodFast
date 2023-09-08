package com.example.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.PopularItemsBinding
import com.example.easyfood.pojo.MealsByCategory

class MostPopularAdapter() : Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    var onLongItemClick: ((MealsByCategory) -> Unit)? = null
    private var mealList = ArrayList<MealsByCategory>()

    fun setMels(mealList: ArrayList<MealsByCategory>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealList[position])
            true
        }
    }
}