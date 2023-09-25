package com.vicky.wetalk

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val item_receive=1
    val item_sent=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            //inflate receive
            val view: View= LayoutInflater.from(context).inflate(R.layout.received,parent,false)
            return receivedViewHolder(view)
        }
        else{
            //inflate sent
            val view: View=LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return sentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentmessage=messageList[position]

        if (holder.javaClass==sentViewHolder::class.java){
            //do stuff for sent view holder

            val viewHolder=holder as sentViewHolder
            holder.sentmessage.text=currentmessage.message
        }
        else{
            //do stuff for received view holder

            val viewHolder=holder as receivedViewHolder
            holder.receivedmessage.text=currentmessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentmessage=messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderId)){
            return item_sent
        }
        else{
            return item_receive
        }
    }

    override fun getItemCount(): Int {
        return  messageList.size
    }

    class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val sentmessage=itemView.findViewById<TextView>(R.id.senttextview)
    }

    class receivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivedmessage=itemView.findViewById<TextView>(R.id.receive_tv)
    }

}