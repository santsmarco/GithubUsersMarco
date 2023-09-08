package com.app.githubusersmarco.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.githubusersmarco.R
import com.app.githubusersmarco.adapter.RepositoryAdapter
import com.app.githubusersmarco.model.GitHubUser
import com.app.githubusersmarco.service.GitHubService
import com.app.githubusersmarco.repository.UserDetailsRepository
import com.app.githubusersmarco.viewModel.UserDetailsViewModel
import com.app.githubusersmarco.factory.UserDetailsViewModelFactory
import com.app.githubusersmarco.model.UserRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_details.backDetail
import kotlinx.android.synthetic.main.activity_user_details.progressBarDetailUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: UserDetailsViewModel
    private val handler = Handler(Looper.getMainLooper())
    private val timeoutMillis = 15000
    private lateinit var listView: ListView
    private lateinit var repoAdapter: RepositoryAdapter
    private var user: GitHubUser? = null
    private lateinit var userRepositoryList: ArrayList<UserRepository>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val usernameTextView = findViewById<TextView>(R.id.userNameTextView)
        val idTextView = findViewById<TextView>(R.id.idTextView)
        val avatarImageView = findViewById<ImageView>(R.id.avatarImageView)
        val progressDetail = findViewById<ProgressBar>(R.id.progressBarDetailUser)

        user = intent.getParcelableExtra("user")

        if (user != null) {
            usernameTextView.text = user!!.login.toUpperCase()
            idTextView.text = "User ID: #00${user!!.id.toString()}"
            Glide.with(this)
                .load(user!!.avatar_url)
                .error(R.drawable.default_user_image)
                .into(avatarImageView)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val repository = UserDetailsRepository(service, user?.login ?: "")

        viewModel = ViewModelProvider(this, UserDetailsViewModelFactory(repository))
            .get(UserDetailsViewModel::class.java)

        startTimeoutTimer()

        viewModel.userDetailLiveData.observe(this, Observer { userDetails ->
            handler.removeCallbacksAndMessages(null)
            if (userDetails != null) {
                val location = findViewById<TextView>(R.id.locationTextView)
                setLocationField(location, "Location:", userDetails.location)

                val company = findViewById<TextView>(R.id.companyTextView)
                setLocationField(company, "Company:", userDetails.company)

                val email = findViewById<TextView>(R.id.emailTextView)
                setLocationField(email, "Email:", userDetails.email)

                val blog = findViewById<TextView>(R.id.blogTextView)
                setLocationField(blog, "Blog:", userDetails.blog)

                val twitter = findViewById<TextView>(R.id.twitterTextView)
                setLocationField(twitter, "Twitter:", userDetails.twitter_username)

                val publicRepository = findViewById<TextView>(R.id.publicReposTextView)
                publicRepository.visibility = View.GONE
                setLocationField(publicRepository, "Public Repos:", userDetails.repos_url)

                val publicGit = findViewById<TextView>(R.id.publicGistsTextView)
                publicGit.visibility = View.GONE
                setLocationField(publicGit, "Public Gists:", userDetails.gists_url)

                val follower = findViewById<TextView>(R.id.followersTextView)
                setLocationField(follower, "Followers:", userDetails.followers.toString())

                val following = findViewById<TextView>(R.id.followingTextView)
                setLocationField(following, "Following:", userDetails.following.toString())

                val createdAt = findViewById<TextView>(R.id.createdAtTextView)
                setLocationField(createdAt, "Created At:", userDetails.created_at)

                val updatedAt = findViewById<TextView>(R.id.updatedAtTextView)
                setLocationField(updatedAt, "Updated At:", userDetails.updated_at)
            }
        })

        viewModel.getDetailUser()

        listView = findViewById(R.id.listViewRepos)
        repoAdapter = RepositoryAdapter(this, R.layout.item_list_repository, ArrayList())
        listView.adapter = repoAdapter
        listView.divider = null
        listView.dividerHeight = 5

        viewModel.getUserRepositories(user?.login ?: "")
        userRepositoryList = ArrayList()

        viewModel.repositoriesLiveData.observe(this, Observer { repositories ->
            handler.removeCallbacksAndMessages(null)
            progressDetail.visibility = View.GONE
            if (repositories != null) {
                userRepositoryList.clear()
                userRepositoryList.addAll(repositories)
                repoAdapter.clear()
                repoAdapter.addAll(repositories)
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedRepository = userRepositoryList[position]
            showRepositoryDialog(selectedRepository)
        }

        backDetail.setOnClickListener {
            finish()
        }
    }

    private fun setLocationField(textView: TextView, label: String, value: String?) {
        if (value == null || value.isEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.text = "$label $value"
        }
    }

    private fun startTimeoutTimer() {
        handler.postDelayed({
            showRetryDialog()
        }, timeoutMillis.toLong())
    }

    private fun showRetryDialog() {
        progressBarDetailUser.visibility = View.GONE
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Timeout")
        alertDialog.setMessage("A consulta demorou muito para responder. Deseja tentar novamente?")

        alertDialog.setPositiveButton("Tentar Novamente", DialogInterface.OnClickListener { _, _ ->
            viewModel.getDetailUser()
            viewModel.getUserRepositories(user?.login ?: "")
            progressBarDetailUser.visibility = View.VISIBLE
            startTimeoutTimer()
        })

        alertDialog.show()
    }

    private fun showRepositoryDialog(selectedUserRepository: UserRepository) {
        progressBarDetailUser.visibility = View.GONE
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.users_logo)
        alertDialog.setTitle("Info Repository")
        alertDialog.setMessage("-> ID: ${selectedUserRepository.id}\n" +
                "-> Name: ${selectedUserRepository.name}\n" +
                "-> Description: ${selectedUserRepository.description ?: ""}\n" +
                "-> Forks Count: ${selectedUserRepository.forksCount}\n" +
                "-> Language: ${selectedUserRepository.language ?: ""}\n")

        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
        })

        alertDialog.show()
    }
}