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

// Adapter to handle AddFriendActivity RecyclerView inflates add_friend_item.xml

class FriendAdapter(val userList: ArrayList<User>,val user: User):RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.add_friend_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem= userList[position]
        holder.titleImage.setImageResource(currentItem.imageId)
        holder.textView.text=currentItem.name
    }
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val titleImage: ShapeableImageView=itemView.findViewById(R.id.friend_title_image)
        val textView: TextView=itemView.findViewById(R.id.tvFriendNameFriend)
        private val btnAdd: Button
        init {
            btnAdd=itemView.findViewById(R.id.btnAdd)
            btnAdd.setOnClickListener { addFriends(it) }
        }

        private fun addFriends(v: View){
            val item= userList[adapterPosition]
            userList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            user.listFriends?.add(item)
            Toast.makeText(v.context,"${item.name} added as friend!", Toast.LENGTH_SHORT).show()
        }

    }
}