package com.safeer.testapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.safeer.testapplication.model.User

// Adapter to handle ProfileActivity RecyclerView inflates user_list_item.xml
class MyAdapter(val userList: ArrayList<User>,val user: User):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.user_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.titleImage.setImageResource(currentItem.imageId)
        holder.textView.text=currentItem.name
    }
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val btnRemove: Button
        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val textView : TextView= itemView.findViewById(R.id.tvNameFriend)
        init {
            btnRemove=itemView.findViewById(R.id.btnRemove)
            btnRemove.setOnClickListener { remove(it) }
        }
       private fun remove(v: View){
            val item= userList[adapterPosition]
            userList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            user.listFriends=userList

            Toast.makeText(v.context,"${item.name} removed from friends List!",Toast.LENGTH_SHORT).show()

        }

    }
}