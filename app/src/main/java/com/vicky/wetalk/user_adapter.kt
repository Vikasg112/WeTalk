package com.vicky.wetalk

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class user_adapter(val context: Context, val userlist: ArrayList<user>):RecyclerView.Adapter<user_adapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View=LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser =userlist[position]
        holder.textname.text=currentUser.name

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }


    class UserViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val textname=itemView.findViewById<TextView>(R.id.txt_name)
    }


}