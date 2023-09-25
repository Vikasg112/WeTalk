package com.vicky.wetalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendbutton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbdref: DatabaseReference

    var receiverRoom: String? =null
    var senderRoom: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name=intent.getStringExtra("name")
        val receiveruid=intent.getStringExtra("uid")

        val senderuid=FirebaseAuth.getInstance().currentUser?.uid

        mDbdref=FirebaseDatabase.getInstance().getReference()

        senderRoom=receiveruid+senderuid
        receiverRoom=senderuid+receiveruid

        supportActionBar?.title=name

        chatRecyclerView=findViewById(R.id.chatrecycleview)
        messagebox=findViewById(R.id.messagebox)
        sendbutton=findViewById(R.id.send_iv)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        //logic for adding data to recyclerview

        mDbdref.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()

                for(postsnapshot in snapshot.children){
                    val message=postsnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        //adding the message to database
        sendbutton.setOnClickListener {
            val message=messagebox.text.toString()
            val messageobj=Message(message,senderuid)

            if(message.isNotEmpty()) {
                mDbdref.child("chats").child((senderRoom!!)).child("messages").push()
                    .setValue(messageobj)
                    .addOnSuccessListener {
                        mDbdref.child("chats").child((receiverRoom!!)).child("messages").push()
                            .setValue(messageobj)
                    }
                messagebox.setText("")
            }
        }


    }
}