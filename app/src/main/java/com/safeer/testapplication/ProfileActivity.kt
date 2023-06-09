package com.safeer.testapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safeer.testapplication.model.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private var admin= User("Profile Admin",R.drawable.admin,null)
    private var imageUri: Uri? = null
    private lateinit var adminProfile: ImageView
    private lateinit var newRecyclerView : RecyclerView
    private lateinit var friendsList: ArrayList<User>

    private lateinit var imageIds: Array<Int>
    private lateinit var names: Array<String>
    private val SELECT_IMAGE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
         val adminName: TextView=findViewById(R.id.profile_username)
         adminProfile =findViewById(R.id.profile_image)

        val textView: TextView=findViewById(R.id.textView)
        val addFriendBtn: Button=findViewById(R.id.btnAddFriend)
        val editBtn: ImageButton= findViewById(R.id.ibEdit)
        editBtn.setOnClickListener {
            val builder=AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout=inflater.inflate(R.layout.et_edit,null)
            val editText=dialogLayout.findViewById<EditText>(R.id.etEditText)

            with(builder){
                setTitle("Name:")
                setPositiveButton("Ok"){dialog, which->
                    admin.name=editText.text.toString()
                    adminName.text=admin.name
                    Toast.makeText(context,"Name of Profile is Changed!",Toast.LENGTH_SHORT).show()
                    val str="Friends List:"
                    textView.text=str

                }
                setNegativeButton("Cancel"){dialog,which->
                    Toast.makeText(context,"Editing Canceled!",Toast.LENGTH_SHORT).show()
                }
                setView(dialogLayout)
                show()
            }
        }

        // if intent is not null then get values from intent otherwise execute below lines

        val friendNames: Array<String>? =intent.getStringArrayExtra("fNames")
        val friendImageIds: Array<Int>? =intent.getIntArrayExtra("fImgId")?.toTypedArray()
        if((friendNames != null) and (friendImageIds != null)){

            if (friendImageIds != null) {
                imageIds= friendImageIds.copyOf()
            }
            if (friendNames != null) {
                names=friendNames.copyOf()
            }
            admin=User(intent.getStringExtra("aName")!!,intent.getIntExtra("aImgId",0),null)
            adminName.text = admin.name
            adminProfile.setImageResource(admin.imageId)



            if (imageUri != null){
//                imageUri=getImageUriFromInternalStorage(imageUri!!)
                adminProfile.setImageURI(imageUri)
            }

            val str="Friends List:"
            textView.text = str

            newRecyclerView=findViewById(R.id.rvFriends)
            newRecyclerView.layoutManager=LinearLayoutManager(this)
            newRecyclerView.setHasFixedSize(true)
            friendsList= arrayListOf()

            addFriendBtn.setOnClickListener { startAddFriend() }
            getUserData()

        }else {
            adminName.text = admin.name
            adminProfile.setImageResource(admin.imageId)
            val str="Friends List:"
            textView.text = str



            imageIds = arrayOf(
                R.drawable.user1,
                R.drawable.user2,
                R.drawable.user3,
                R.drawable.user4,
                R.drawable.user5,
            )

            names = arrayOf(
                "Username1",
                "Username2",
                "Username3",
                "Username4",
                "Username5",
            )


            newRecyclerView = findViewById(R.id.rvFriends)
            newRecyclerView.layoutManager = LinearLayoutManager(this)
            newRecyclerView.setHasFixedSize(true)
            friendsList = arrayListOf()

            addFriendBtn.setOnClickListener { startAddFriend() }
            getUserData()
        }

        adminProfile.setOnLongClickListener {
            if(!checkPermission()){
                requestPermission()
            }else{
                openGallery()
            }
            true
        }

    }
    private fun startAddFriend(){

       val friendNames= admin.listFriends?.map { it.name }?.toTypedArray() ?: names
        val friendsImgId=admin.listFriends?.map { it.imageId }?.toTypedArray() ?: imageIds

        val intent = Intent(this, AddFriendActivity::class.java)
        intent.putExtra("aName",admin.name)
        intent.putExtra("aImgId",admin.imageId)
        intent.putExtra("fNames",friendNames )
        intent.putExtra("fImgId",friendsImgId.toIntArray())
//        intent.putExtra("imgUri",imageUri)
        startActivity(intent)
        finish()
    }


   private fun getUserData() {

        for (i in imageIds.indices){
            friendsList.add(User(names[i],imageIds[i],null))
        }
        admin.listFriends=friendsList
        newRecyclerView.adapter=MyAdapter(admin.listFriends!!,admin)

    }

    private fun saveImageToInternalStorage(imageUri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            Log.d("uri_path_is",imageUri.path.toString())
            val fileName = "Image1.jpg"
            val internalStorageDir = filesDir
            val destinationFile = File(internalStorageDir, fileName)

            val outputStream = FileOutputStream(destinationFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


//    private fun getImageUriFromInternalStorage(originalImageUri: Uri): Uri? {
//        val originalImagePath = originalImageUri.path
//        val internalStorageDir = filesDir
//        val files = internalStorageDir.listFiles()
//
//        for (file in files) {
//            if (file.isFile && file.path.endsWith(".jpg")) {
//                val filePath = file.absolutePath
//                if (originalImagePath == filePath) {
//                    Log.d("getImage",originalImagePath!!)
//                    return Uri.fromFile(file)
//                }
//            }
//        }
//
//        return null
//    }

    // Second code::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private fun checkPermission(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, SELECT_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                // Permission denied
                Toast.makeText(this,"Permission Denied!: won't be able to open Gallery",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageUri= data?.data
            if (imageUri != null) {
                // Save the selected image to internal storage
                saveImageToInternalStorage(imageUri!!)

                // Display the selected image in ImageView
                adminProfile.setImageURI(imageUri)
                intent.putExtra("image",imageUri.toString())
            }
        }
    }





}