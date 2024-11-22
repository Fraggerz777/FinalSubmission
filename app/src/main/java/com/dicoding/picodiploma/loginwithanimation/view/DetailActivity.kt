package com.dicoding.picodiploma.loginwithanimation.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.dicoding.picodiploma.loginwithanimation.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Ambil data dari Intent
        val storyName = intent.getStringExtra("STORY_NAME")
        val storyDescription = intent.getStringExtra("STORY_DESCRIPTION")
        val storyPhotoUrl = intent.getStringExtra("STORY_PHOTO_URL")

        // Temukan view dari layout
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val photoImageView: ImageView = findViewById(R.id.storyImageView)

        // Set data ke view
        nameTextView.text = storyName
        descriptionTextView.text = storyDescription

        // Muat foto menggunakan Glide
        Glide.with(this)
            .load(storyPhotoUrl)
            .into(photoImageView)
    }
}
