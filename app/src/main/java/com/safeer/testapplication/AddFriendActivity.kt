package com.safeer.testapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safeer.testapplication.model.User

class AddFriendActivity : AppCompatActivity() {

    private lateinit var imageIds: Array<Int>
    private lateinit var names: Array<String>
    private var userList= ArrayList<User>()
    private var friendList= ArrayList<User>()
    private lateinit var admin: User
    private lateinit var newRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_add_friend)

        this.imageIds = arrayOf(
            R.drawable.user1,
            R.drawable.user2,
            R.drawable.user3,
            R.drawable.user4,
            R.drawable.user5,
            R.drawable.user6,
            R.drawable.user7,
            R.drawable.user8,
            R.drawable.user9,
            R.drawable.user10,
        )
        this.names = arrayOf(
            "Username1",
            "Username2",
            "Username3",
            "Username4",
            "Username5",
            "Username6",
            "Username7",
            "Username8",
            "Username9",
            "Username10",
        )


        val friendNames: Array<String>? =intent.getStringArrayExtra("fNames")
        val friendImageIds: Array<Int>? =intent.getIntArrayExtra("fImgId")?.toTypedArray()

        var idArrayList: Array<Int>? = null
        val uniqueIdList= imageIds.toMutableList()
        if (friendImageIds != null) {
            uniqueIdList.removeAll(friendImageIds.toMutableList())
            idArrayList=uniqueIdList.toTypedArray()
        }
        var nameArrayList:Array<String>?=null
        val uniqueName= names.toMutableList()
        if(friendNames != null){
            uniqueName.removeAll(friendNames.toMutableList())
            nameArrayList=uniqueName.toTypedArray()
        }




        if (idArrayList != null) {
            for (i in idArrayList.indices){
                userList.add(User(nameArrayList?.get(i) ?: "AFA-97-err", idArrayList[i],null))

            }
        }


        if (friendImageIds != null) {
            for (i in friendImageIds.indices){
                friendList.add(User(friendNames?.get(i) ?: "AFA-105-err",friendImageIds[i],null))
            }
        }
                admin= User(intent.getStringExtra("aName")!!,intent.getIntExtra("aImgId",0),
                    friendList
                )



        this.newRecyclerView = this.findViewById(R.id.rvAddFriend)
        this.newRecyclerView.layoutManager= LinearLayoutManager(this)
        this.newRecyclerView.setHasFixedSize(true)
        this.newRecyclerView.adapter=FriendAdapter(userList,admin)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val friendNames= admin.listFriends?.map { it.name }?.toTypedArray() ?: names
        val friendsImgId=admin.listFriends?.map { it.imageId }?.toTypedArray() ?: imageIds


        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("aName",admin.name)
        intent.putExtra("aImgId",admin.imageId)
        intent.putExtra("fNames",friendNames )
        intent.putExtra("fImgId",friendsImgId.toIntArray())
        startActivity(intent)
        finish()

    }

    }






